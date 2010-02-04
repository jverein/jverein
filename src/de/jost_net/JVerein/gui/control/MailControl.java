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
 * Revision 1.1  2010/02/01 20:57:58  jost
 * Neu: Einfache Mailfunktion
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.io.StringWriter;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeSet;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.MailDetailAction;
import de.jost_net.JVerein.gui.menu.MailAuswahlMenu;
import de.jost_net.JVerein.gui.menu.MailMenu;
import de.jost_net.JVerein.io.MailSender;
import de.jost_net.JVerein.rmi.Mail;
import de.jost_net.JVerein.rmi.MailEmpfaenger;
import de.jost_net.JVerein.rmi.Mitglied;
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

  public TablePart getMitgliedMitMail() throws RemoteException
  {
    if (mitgliedmitmail != null)
    {
      return mitgliedmitmail;
    }
    DBIterator it = Einstellungen.getDBService().createList(Mitglied.class);
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
    betreff = new TextInput((String) getMail().getBetreff(), 100);
    betreff.setName("Betreff");
    return betreff;
  }

  public TextAreaInput getTxt() throws RemoteException
  {
    if (txt != null)
    {
      return txt;
    }
    txt = new TextAreaInput((String) getMail().getTxt(), 1000);
    txt.setName("Text");
    return txt;
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
      public void handleAction(Object context) throws ApplicationException
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
      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        try
        {
          MailSender sender = new MailSender(Einstellungen.getEinstellung()
              .getSmtpServer(), Einstellungen.getEinstellung().getSmtpPort(),
              Einstellungen.getEinstellung().getSmtpAuthUser(), Einstellungen
                  .getEinstellung().getSmtpAuthPwd(), Einstellungen
                  .getEinstellung().getSmtpFromAddress(), Einstellungen
                  .getEinstellung().getSmtpSsl());

          Velocity.init();
          Logger.debug("preparing velocity context");
          for (MailEmpfaenger empf : getMail().getEmpfaenger())
          {
            VelocityContext context = new VelocityContext();
            context.put("email", empf.getMailAdresse());
            context.put("empf", empf.getMitglied());
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            context.put("tagesdatum", sdf.format(new Date()));
            sdf.applyPattern("MM.yyyy");
            context.put("aktuellermonat", sdf.format(new Date()));
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 1);
            context.put("folgemonat", sdf.format(calendar.getTime()));
            calendar.add(Calendar.MONTH, -2);
            context.put("vormonat", sdf.format(calendar.getTime()));
            StringWriter wbetr = new StringWriter();
            Velocity.evaluate(context, wbetr, "LOG", betr);
            StringWriter wtext = new StringWriter();
            Velocity.evaluate(context, wtext, "LOG", txt);
            sender.sendMail(empf.getMailAdresse(),
                wbetr.getBuffer().toString(), wtext.getBuffer().toString());
          }

          monitor.setPercentComplete(100);
          monitor.setStatus(ProgressMonitor.STATUS_DONE);
          GUI.getStatusBar().setSuccessText(
              "Mail" + (getMail().getEmpfaenger().size() > 1 ? "s" : "")
                  + " verschickt");
          getMail().store();
          GUI.getCurrentView().reload();
        }
        catch (ApplicationException ae)
        {
          Logger.error("", ae);
          monitor.setStatusText(ae.getMessage());
          monitor.setStatus(ProgressMonitor.STATUS_ERROR);
          GUI.getStatusBar().setErrorText(ae.getMessage());
          throw ae;
        }
        catch (Exception re)
        {
          Logger.error("", re);
          monitor.setStatusText(re.getMessage());
          monitor.setStatus(ProgressMonitor.STATUS_ERROR);
          GUI.getStatusBar().setErrorText(re.getMessage());
          throw new ApplicationException(re);
        }
      }

      public void interrupt()
      {
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
