/**********************************************************************
 * Copyright (c) by Heiner Jostkleigrewe
 * This program is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,  but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See 
 *  the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, 
 * see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de
 * www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.io.IOException;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeSet;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.Variable.AllgemeineMap;
import de.jost_net.JVerein.Variable.MitgliedMap;
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
import de.jost_net.JVerein.util.JVDateFormatDATETIME;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.dialogs.SimpleDialog;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
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
    empfaenger.addColumn("Versand", "versand", new DateFormatter(
        new JVDateFormatDATETIME()));
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
    it.addFilter("email is not null and length(email) > 0");
    mitgliedmitmail = new TablePart(it, null);
    mitgliedmitmail.addColumn("EMail", "email");
    mitgliedmitmail.addColumn("Name", "name");
    mitgliedmitmail.addColumn("Vorname", "vorname");
    mitgliedmitmail.addColumn("Adresstyp", "adresstyp");
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
    anhang.setRememberColWidths(true);
    anhang.setContextMenu(new MailAnhangMenu(this));
    anhang.setRememberOrder(true);
    anhang.setSummary(false);
    return anhang;
  }

  public Button getMailSendButton()
  {
    Button b = new Button("speichern + senden", new Action()
    {

      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        try
        {
          int toBeSentCount = 0;
          for (final MailEmpfaenger empf : getMail().getEmpfaenger())
          {
            if (empf.getVersand() == null)
            {
              toBeSentCount++;
            }
          }
          if (toBeSentCount == 0)
          {
            SimpleDialog d = new SimpleDialog(SimpleDialog.POSITION_CENTER);
            d.setTitle("Mail bereits versendet");
            d.setText("Mail wurde bereits an alle Empfänger versendet!");
            try
            {
              d.open();
            }
            catch (Exception e)
            {
              Logger.error("Fehler beim Nicht-Senden der Mail", e);
            }
            return;
          }
          if (toBeSentCount != getMail().getEmpfaenger().size())
          {
            YesNoDialog d = new YesNoDialog(YesNoDialog.POSITION_CENTER);
            d.setTitle("Mail senden?");
            d.setText("Diese Mail wurde bereits an "
                + (getMail().getEmpfaenger().size() - toBeSentCount)
                + " der gewählten Empfänger versendet. Wollen Sie diese Mail an alle weiteren "
                + toBeSentCount + " Empfänger senden?");
            try
            {
              Boolean choice = (Boolean) d.open();
              if (!choice.booleanValue())
                return;
            }
            catch (Exception e)
            {
              Logger.error(

              "Fehler beim Senden der Mail", e);
              return;
            }
          }
          sendeMail(false);
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

  public Button getMailReSendButton()
  {
    Button b = new Button("speichern + erneut senden", new Action()
    {

      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        try
        {
          boolean mailAlreadySent = false;
          for (final MailEmpfaenger empf : getMail().getEmpfaenger())
          {
            if (empf.getVersand() != null)
            {
              mailAlreadySent = true;
              break;
            }
          }
          if (mailAlreadySent)
          {
            YesNoDialog d = new YesNoDialog(YesNoDialog.POSITION_CENTER);
            d.setTitle("Mail erneut senden?");
            d.setText("An mindestens einen Empfänger wurde diese Mail bereits versendet. Wollen Sie diese Mail wirklich erneut an alle Empfänger senden?");
            try
            {
              Boolean choice = (Boolean) d.open();
              if (!choice.booleanValue())
                return;
            }
            catch (Exception e)
            {
              Logger.error("Fehler beim Senden der Mail", e);
              return;
            }
          }
          sendeMail(true);
          handleStore(true);
        }
        catch (RemoteException e)
        {
          Logger.error(e.getMessage());
          throw new ApplicationException("Fehler beim Senden der Mail");
        }
      }
    }, null, false, "mail-message-new.png");
    return b;
  }

  public Button getMailSpeichernButton()
  {
    Button b = new Button("speichern", new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        handleStore(false);
      }
    }, null, true, "mail-message-new.png");
    return b;
  }

  public String getBetreffString() throws RemoteException
  {
    return (String) getBetreff().getValue();
  }

  public String getTxtString() throws RemoteException
  {
    return (String) getTxt().getValue();
  }

  /**
   * Versende Mail an Empfänger. Wenn erneutSenden==false wird Mail nur an
   * Empfänger versendet, die Mail noch nicht erhalten haben.
   */
  private void sendeMail(final boolean erneutSenden) throws RemoteException
  {
    final String betr = getBetreffString();
    final String txt = getTxtString()
        + Einstellungen.getEinstellung().getMailSignatur(true);
    BackgroundTask t = new BackgroundTask()
    {

      @Override
      public void run(ProgressMonitor monitor)
      {
        try
        {
          MailSender sender = new MailSender(Einstellungen.getEinstellung()
              .getSmtpServer(), Einstellungen.getEinstellung().getSmtpPort(),
              Einstellungen.getEinstellung().getSmtpAuthUser(), Einstellungen
                  .getEinstellung().getSmtpAuthPwd(), Einstellungen
                  .getEinstellung().getSmtpFromAddress(), Einstellungen
                  .getEinstellung().getSmtpFromAnzeigename(), Einstellungen
                  .getEinstellung().getMailAlwaysBcc(), Einstellungen
                  .getEinstellung().getMailAlwaysCc(), Einstellungen
                  .getEinstellung().getSmtpSsl(), Einstellungen
                  .getEinstellung().getSmtpStarttls(), Einstellungen
                  .getEinstellung().getMailVerzoegerung(),
              Einstellungen.getImapCopyData());

          Velocity.init();
          Logger.debug("preparing velocity context");
          monitor.setStatus(ProgressMonitor.STATUS_RUNNING);
          monitor.setPercentComplete(0);
          int zae = 0;
          int sentCount = 0;
          for (final MailEmpfaenger empf : getMail().getEmpfaenger())
          {
            EvalMail em = new EvalMail(empf);
            if (erneutSenden || empf.getVersand() == null)
            {
              sender.sendMail(empf.getMailAdresse(), em.evalBetreff(betr),
                  em.evalText(txt), getMail().getAnhang());
              sentCount++;
              monitor.log(empf.getMailAdresse() + " - versendet");
              // Nachricht wurde erfolgreich versendet; speicher Versand-Datum
              // persistent.
              empf.setVersand(new Timestamp(new Date().getTime()));
              empf.store();
              // aktualisiere TablePart getEmpfaenger() (zeige neues
              // Versand-Datum)
              GUI.startView(GUI.getCurrentView().getClass(), GUI
                  .getCurrentView().getCurrentObject());
            }
            else
            {
              monitor.log(empf.getMailAdresse() + " - übersprungen");
            }
            zae++;
            double proz = (double) zae
                / (double) getMail().getEmpfaenger().size() * 100d;
            monitor.setPercentComplete((int) proz);

          }
          monitor.setPercentComplete(100);
          monitor.setStatus(ProgressMonitor.STATUS_DONE);
          monitor.setStatusText(String.format("Anzahl verschickter Mails: %d",
              sentCount));
          GUI.getStatusBar().setSuccessText(
              "Mail" + (sentCount > 1 ? "s" : "") + " verschickt");
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

      @Override
      public void interrupt()
      {
        //
      }

      @Override
      public boolean isInterrupted()
      {
        return false;
      }
    };
    Application.getController().start(t);
  }

  public Map<String, Object> getVariables(Mitglied m) throws RemoteException
  {
    Map<String, Object> map = new MitgliedMap().getMap(m, null);
    map = new AllgemeineMap().getMap(map);
    return map;
  }

  /**
   * Speichert die Mail in der DB.
   * 
   * @param mitversand
   *          wenn true, wird Spalte Versand auf aktuelles Datum gesetzt.
   */
  public void handleStore(boolean mitversand)
  {
    try
    {
      Mail m = getMail();
      m.setBetreff(getBetreffString());
      m.setTxt(getTxtString());
      m.setBearbeitung(new Timestamp(new Date().getTime()));
      if (mitversand)
      {
        m.setVersand(new Timestamp(new Date().getTime()));
      }
      else
      {
        m.setVersand(null);
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
        new JVDateFormatDATETIME()));
    mailsList.addColumn("Versand", "versand", new DateFormatter(
        new JVDateFormatDATETIME()));
    mailsList.setRememberColWidths(true);
    mailsList.setContextMenu(new MailMenu());
    mailsList.setRememberOrder(true);
    return mailsList;
  }

  public class EvalMail
  {

    VelocityContext context = null;

    public EvalMail(MailEmpfaenger empf) throws RemoteException
    {
      context = new VelocityContext();
      context.put("dateformat", new JVDateFormatTTMMJJJJ());
      context.put("decimalformat", Einstellungen.DECIMALFORMAT);
      context.put("email", empf.getMailAdresse());
      context.put("empf", empf.getMitglied());
      Map<String, Object> map = getVariables(empf.getMitglied());
      VarTools.add(context, map);
    }

    public String evalBetreff(String betr) throws ParseErrorException,
        MethodInvocationException, ResourceNotFoundException, IOException
    {
      if (context == null)
        return null;
      StringWriter wbetr = new StringWriter();
      Velocity.evaluate(context, wbetr, "LOG", betr);
      return wbetr.getBuffer().toString();
    }

    public String evalText(String txt) throws ParseErrorException,
        MethodInvocationException, ResourceNotFoundException, IOException
    {
      if (context == null)
        return null;
      StringWriter wtext = new StringWriter();
      Velocity.evaluate(context, wtext, "LOG", txt);
      return wtext.getBuffer().toString();
    }
  }

}
