/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * heiner@jverein.de
 * www.jverein.de
 * $Log$
 * Revision 1.18  2011-04-19 19:16:08  jost
 * Bugfix
 *
 * Revision 1.17  2011-04-19 11:14:14  jost
 * Kein Abbruch bei fehlerafter Mailadresse
 *
 * Revision 1.16  2011-04-18 08:24:56  jost
 * Verlängerung der Texte auf 10.000 Zeichen
 *
 * Revision 1.15  2011-04-06 16:28:50  jost
 * Neu: Starttls
 *
 * Revision 1.14  2011-04-03 10:02:03  jost
 * Ausgabe der Zusatzfelder
 *
 * Revision 1.13  2011-03-28 18:07:14  jost
 * Überflüssigen Code entfernt.
 *
 * Revision 1.12  2011-03-20 19:18:19  jost
 * Formatierung von Datumsfeldern und Beträgen.
 *
 * Revision 1.11  2011-01-27 22:18:35  jost
 * Neu: Speicherung von weiteren Adressen in der Mitgliedertabelle
 *
 * Revision 1.10  2011-01-15 09:46:49  jost
 * Tastatursteuerung wegen Problemen mit Jameica/Hibiscus wieder entfernt.
 *
 * Revision 1.9  2010-10-15 09:58:26  jost
 * Code aufgeräumt
 *
 * Revision 1.8  2010-08-23 13:35:09  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.7  2010/05/16 10:43:19  jost
 * Einheitlicher Umgang mit ausgetretenen Mitgliedern
 *
 * Revision 1.6  2010/03/27 20:08:11  jost
 * Bugfix 2. Aufruf
 *
 * Revision 1.5  2010/02/17 21:26:09  jost
 * Statusanzeige
 *
 * Revision 1.4  2010/02/17 16:30:34  jost
 * Statusanzeige
 *
 * Revision 1.3  2010/02/15 17:22:14  jost
 * Mail-Anhang implementiert
 *
 * Revision 1.2  2010/02/04 18:38:38  jost
 * Zusätzliche Datenfelder
 *
 * Revision 1.1  2010/02/01 20:57:58  jost
 * Neu: Einfache Mailfunktion
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.io.StringWriter;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.Variable.AllgemeineMap;
import de.jost_net.JVerein.Variable.VarTools;
import de.jost_net.JVerein.gui.action.MailDetailAction;
import de.jost_net.JVerein.gui.menu.MailAnhangMenu;
import de.jost_net.JVerein.gui.menu.MailAuswahlMenu;
import de.jost_net.JVerein.gui.menu.MailMenu;
import de.jost_net.JVerein.io.MailSender;
import de.jost_net.JVerein.rmi.Mail;
import de.jost_net.JVerein.rmi.MailAnhang;
import de.jost_net.JVerein.rmi.MailEmpfaenger;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.server.MitgliedUtils;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.input.TextAreaInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class MailControl extends AbstractControl
{

  private de.willuhn.jameica.system.Settings settings;

  private TablePart empfaenger;

  private TextInput betreff;

  private TextAreaInput txt;

  private TablePart anhang;

  private TablePart mitgliedmitmail;

  private Mail mail;

  private TablePart mailsList;

  public MailControl(AbstractView view)
  {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public Mail getMail()
  {
    if (mail != null)
    {
      return mail;
    }
    mail = (Mail) getCurrentObject();
    return mail;
  }

  public TablePart getEmpfaenger() throws RemoteException
  {
    if (empfaenger != null)
    {
      return empfaenger;
    }
    if (!getMail().isNewObject() && getMail().getEmpfaenger() == null)
    {
      DBIterator it = Einstellungen.getDBService().createList(
          MailEmpfaenger.class);
      it.addFilter("mail = ?", new Object[] { getMail().getID() });
      TreeSet<MailEmpfaenger> empf = new TreeSet<MailEmpfaenger>();
      while (it.hasNext())
      {
        MailEmpfaenger me = (MailEmpfaenger) it.next();
        empf.add(me);
      }
      getMail().setEmpfaenger(empf);
    }
    else if (getMail().getEmpfaenger() == null)
    {
      getMail().setEmpfaenger(new TreeSet<MailEmpfaenger>());
    }
    // Umwandeln in ArrayList
    ArrayList<MailEmpfaenger> empf2 = new ArrayList<MailEmpfaenger>();
    for (MailEmpfaenger me : getMail().getEmpfaenger())
    {
      empf2.add(me);
    }
    empfaenger = new TablePart(empf2, null);
    empfaenger.addColumn("Mail-Adresse", "mailadresse");
    empfaenger.addColumn("Name", "name");
    empfaenger.setContextMenu(new MailAuswahlMenu(this));
    empfaenger.setRememberOrder(true);
    empfaenger.setSummary(false);
    return empfaenger;
  }

  public void addEmpfaenger(MailEmpfaenger me) throws RemoteException
  {
    if (!getMail().getEmpfaenger().contains(me))
    {
      getEmpfaenger().addItem(me);
      getMail().getEmpfaenger().add(me);
    }
  }

  public void removeEmpfaenger(MailEmpfaenger me) throws RemoteException
  {
    getEmpfaenger().removeItem(me);
    getMail().getEmpfaenger().remove(me);
  }

  public void addAnhang(MailAnhang ma) throws RemoteException
  {
    if (!getMail().getAnhang().contains(ma))
    {
      getAnhang().addItem(ma);
      getMail().getAnhang().add(ma);
    }
  }

  public void removeAnhang(MailAnhang ma) throws RemoteException
  {
    getAnhang().removeItem(ma);
    getMail().getAnhang().remove(ma);
  }

  public TablePart getMitgliedMitMail() throws RemoteException
  {
    if (mitgliedmitmail != null && mitgliedmitmail.size() > 0)
    {
      return mitgliedmitmail;
    }
    DBIterator it = Einstellungen.getDBService().createList(Mitglied.class);
    MitgliedUtils.setNurAktive(it);
    MitgliedUtils.setMitglied(it);
    it.addFilter("email is not null and length(email)  > 0");
    mitgliedmitmail = new TablePart(it, null);
    mitgliedmitmail.addColumn("EMail", "email");
    mitgliedmitmail.addColumn("Name", "name");
    mitgliedmitmail.addColumn("Vorname", "vorname");
    mitgliedmitmail.setRememberOrder(true);
    mitgliedmitmail.setCheckable(true);
    mitgliedmitmail.setSummary(false);
    return mitgliedmitmail;
  }

  public TextInput getBetreff() throws RemoteException
  {
    if (betreff != null)
    {
      return betreff;
    }
    betreff = new TextInput(getMail().getBetreff(), 100);
    betreff.setName("Betreff");
    return betreff;
  }

  public TextAreaInput getTxt() throws RemoteException
  {
    if (txt != null)
    {
      return txt;
    }
    txt = new TextAreaInput(getMail().getTxt(), 10000);
    txt.setName("Text");
    return txt;
  }

  public TablePart getAnhang() throws RemoteException
  {
    if (anhang != null)
    {
      return anhang;
    }
    if (!getMail().isNewObject() && getMail().getAnhang() == null)
    {
      DBIterator it = Einstellungen.getDBService().createList(MailAnhang.class);
      it.addFilter("mail = ?", new Object[] { getMail().getID() });
      TreeSet<MailAnhang> anh = new TreeSet<MailAnhang>();
      while (it.hasNext())
      {
        MailAnhang an = (MailAnhang) it.next();
        anh.add(an);
      }
      getMail().setAnhang(anh);
    }
    else if (getMail().getAnhang() == null)
    {
      getMail().setAnhang(new TreeSet<MailAnhang>());
    }
    // Umwandeln in ArrayList
    ArrayList<MailAnhang> anhang2 = new ArrayList<MailAnhang>();
    for (MailAnhang ma : getMail().getAnhang())
    {
      anhang2.add(ma);
    }
    anhang = new TablePart(anhang2, null);
    anhang.addColumn("Dateiname", "dateiname");
    anhang.setContextMenu(new MailAnhangMenu(this));
    anhang.setRememberOrder(true);
    anhang.setSummary(false);
    return anhang;
  }

  public Button getMailSendButton()
  {
    Button b = new Button("speichern + senden", new Action()
    {

      public void handleAction(Object context) throws ApplicationException
      {
        try
        {
          sendeMail();
          handleStore(true);
        }
        catch (RemoteException e)
        {
          Logger.error(e.getMessage());
          throw new ApplicationException("Fehler beim Senden der Mail");
        }
      }
    }, null, true, "mail-message-new.png");
    return b;
  }

  public Button getMailSpeichernButton()
  {
    Button b = new Button("speichern", new Action()
    {

      public void handleAction(Object context)
      {
        handleStore(false);
      }
    }, null, true, "mail-message-new.png");
    return b;
  }

  private void sendeMail() throws RemoteException
  {
    final String betr = (String) getBetreff().getValue();
    final String txt = (String) getTxt().getValue();

    BackgroundTask t = new BackgroundTask()
    {

      public void run(ProgressMonitor monitor)
      {
        try
        {
          MailSender sender = new MailSender(Einstellungen.getEinstellung()
              .getSmtpServer(), Einstellungen.getEinstellung().getSmtpPort(),
              Einstellungen.getEinstellung().getSmtpAuthUser(), Einstellungen
                  .getEinstellung().getSmtpAuthPwd(), Einstellungen
                  .getEinstellung().getSmtpFromAddress(), Einstellungen
                  .getEinstellung().getSmtpSsl(), Einstellungen
                  .getEinstellung().getSmtpStarttls());

          Velocity.init();
          Logger.debug("preparing velocity context");
          monitor.setStatus(ProgressMonitor.STATUS_RUNNING);
          monitor.setPercentComplete(0);
          int zae = 0;
          for (MailEmpfaenger empf : getMail().getEmpfaenger())
          {
            VelocityContext context = new VelocityContext();
            context.put("dateformat", new JVDateFormatTTMMJJJJ());
            context.put("decimalformat", Einstellungen.DECIMALFORMAT);
            context.put("email", empf.getMailAdresse());
            context.put("empf", empf.getMitglied());
            VarTools.add(context, empf.getMitglied().getMap(null));
            VarTools.add(context, new AllgemeineMap().getMap(null));
            StringWriter wbetr = new StringWriter();
            Velocity.evaluate(context, wbetr, "LOG", betr);
            StringWriter wtext = new StringWriter();
            Velocity.evaluate(context, wtext, "LOG", txt);
            sender.sendMail(empf.getMailAdresse(),
                wbetr.getBuffer().toString(), wtext.getBuffer().toString(),
                getMail().getAnhang());
            monitor.log(empf.getMailAdresse());
            zae++;
            double proz = (double) zae
                / (double) getMail().getEmpfaenger().size() * 100d;
            monitor.setPercentComplete((int) proz);
          }
          monitor.setPercentComplete(100);
          monitor.setStatus(ProgressMonitor.STATUS_DONE);
          monitor.setStatusText("Anzahl verschickter Mails: "
              + getMail().getEmpfaenger().size());
          GUI.getStatusBar().setSuccessText(
              "Mail" + (getMail().getEmpfaenger().size() > 1 ? "s" : "")
                  + " verschickt");
          getMail().store();
          GUI.getCurrentView().reload();
        }
        catch (ApplicationException ae)
        {
          Logger.error("", ae);
          monitor.log(ae.getMessage());
        }
        catch (Exception re)
        {
          Logger.error("", re);
          monitor.log(re.getMessage());
        }
      }

      public void interrupt()
      {
        //
      }

      public boolean isInterrupted()
      {
        return false;
      }
    };
    Application.getController().start(t);
  }

  public void handleStore(boolean mitversand)
  {
    try
    {
      Mail m = getMail();
      m.setBetreff((String) getBetreff().getValue());
      m.setTxt((String) getTxt().getValue());
      m.setBearbeitung(new Timestamp(new Date().getTime()));
      if (mitversand)
      {
        m.setVersand(new Timestamp(new Date().getTime()));
      }
      m.store();
      for (MailEmpfaenger me : getMail().getEmpfaenger())
      {
        me.setMail(m);
        me.store();
      }
      DBIterator it = Einstellungen.getDBService().createList(
          MailEmpfaenger.class);
      it.addFilter("mail = ?", new Object[] { m.getID() });
      while (it.hasNext())
      {
        MailEmpfaenger me = (MailEmpfaenger) it.next();
        if (!m.getEmpfaenger().contains(me))
        {
          me.delete();
        }
      }
      for (MailAnhang ma : getMail().getAnhang())
      {
        ma.setMail(m);
        ma.store();
      }
      it = Einstellungen.getDBService().createList(MailAnhang.class);
      it.addFilter("mail = ?", new Object[] { m.getID() });
      while (it.hasNext())
      {
        MailAnhang ma = (MailAnhang) it.next();
        if (!m.getAnhang().contains(ma))
        {
          ma.delete();
        }
      }
      GUI.getStatusBar().setSuccessText("Mail gespeichert");
    }
    catch (ApplicationException e)
    {
      GUI.getStatusBar().setErrorText(e.getMessage());
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei speichern der Mail: "
          + e.getLocalizedMessage();
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }

  }

  public Part getMailList() throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator mails = service.createList(Mail.class);
    mails.setOrder("ORDER BY betreff");

    mailsList = new TablePart(mails, new MailDetailAction());
    mailsList.addColumn("Betreff", "betreff");
    mailsList.addColumn("Bearbeitung", "bearbeitung", new DateFormatter(
        Einstellungen.DATETIMEFORMAT));
    mailsList.addColumn("Versand", "versand", new DateFormatter(
        Einstellungen.DATETIMEFORMAT));
    mailsList.setRememberColWidths(true);
    mailsList.setContextMenu(new MailMenu());
    mailsList.setRememberOrder(true);
    return mailsList;
  }

}
