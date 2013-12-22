/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
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

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeSet;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.Variable.AllgemeineMap;
import de.jost_net.JVerein.Variable.LastschriftMap;
import de.jost_net.JVerein.Variable.VarTools;
import de.jost_net.JVerein.gui.input.FormularInput;
import de.jost_net.JVerein.io.FormularAufbereitung;
import de.jost_net.JVerein.io.MailSender;
import de.jost_net.JVerein.keys.Formularart;
import de.jost_net.JVerein.rmi.Abrechnungslauf;
import de.jost_net.JVerein.rmi.Formular;
import de.jost_net.JVerein.rmi.Lastschrift;
import de.jost_net.JVerein.rmi.Mail;
import de.jost_net.JVerein.rmi.MailAnhang;
import de.jost_net.JVerein.rmi.MailEmpfaenger;
import de.jost_net.JVerein.util.Dateiname;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextAreaInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class PreNotificationControl extends AbstractControl
{

  private Settings settings = null;

  private SelectInput output = null;

  private TextInput mailsubject = null;

  private TextAreaInput mailbody = null;

  public static final String EMAIL = "EMail";

  public static final String PDF1 = "PDF (Lastschriften ohne Mailadresse)";

  public static final String PDF2 = "PDF (Alle)";

  private FormularInput formular = null;

  private FormularAufbereitung fa;

  public PreNotificationControl(AbstractView view)
  {
    super(view);
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public SelectInput getOutput()
  {
    if (output != null)
    {
      return output;
    }
    Object[] values = new Object[] { EMAIL, PDF1, PDF2 };
    output = new SelectInput(values, settings.getString("output", PDF1));
    output.setName("Ausgabe");
    return output;
  }

  public FormularInput getFormular(int formulartyp) throws RemoteException
  {
    if (formular != null)
    {
      return formular;
    }
    formular = new FormularInput(formulartyp);
    return formular;
  }

  public TextInput getMailSubject() throws RemoteException
  {
    if (mailsubject != null)
    {
      return mailsubject;
    }
    mailsubject = new TextInput(settings.getString("mail.subject", ""), 100);
    mailsubject.setName("Betreff");
    return mailsubject;

  }

  public TextAreaInput getMailBody() throws RemoteException
  {
    if (mailbody != null)
    {
      return mailbody;
    }
    mailbody = new TextAreaInput(settings.getString("mail.body", ""), 10000);
    mailbody.setHeight(200);
    mailbody.setName("Text");
    return mailbody;
  }

  public Button getStartButton(final Object currentObject)
  {
    Button button = new Button("starten", new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        try
        {
          String val = (String) getOutput().getValue();
          settings.setAttribute("output", val);
          settings.setAttribute("mail.subject", (String) mailsubject.getValue());
          settings.setAttribute("mail.body", (String) mailbody.getValue());
          if (val.equals(PDF1))
          {
            generierePDF(currentObject, false);
          }
          if (val.equals(PDF2))
          {
            generierePDF(currentObject, true);
          }
          if (val.equals(EMAIL))
          {
            generiereEMail(currentObject);
          }
        }
        catch (Exception e)
        {
          Logger.error("Fehler", e);
          GUI.getStatusBar().setErrorText(e.getMessage());
        }
      }
    }, null, true, "go.png");
    return button;
  }

  private void generierePDF(Object currentObject, boolean mitMail)
      throws IOException
  {
    Abrechnungslauf abrl = (Abrechnungslauf) currentObject;
    FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
    fd.setText("Ausgabedatei wählen.");
    String path = settings
        .getString("lastdir", System.getProperty("user.home"));
    if (path != null && path.length() > 0)
    {
      fd.setFilterPath(path);
    }
    fd.setFileName(new Dateiname("prenotification", "", Einstellungen
        .getEinstellung().getDateinamenmuster(), "PDF").get());
    fd.setFilterExtensions(new String[] { "*.PDF" });

    String s = fd.open();
    if (s == null || s.length() == 0)
    {
      return;
    }
    if (!s.endsWith(".PDF"))
    {
      s = s + ".PDF";
    }
    final File file = new File(s);
    settings.setAttribute("lastdir", file.getParent());
    Formular form = (Formular) getFormular(Formularart.SEPA_PRENOTIFICATION)
        .getValue();
    if (form == null)
    {
      throw new IOException("kein SEPA Pre-Notification-Formular ausgewählt");
    }
    Formular fo = (Formular) Einstellungen.getDBService().createObject(
        Formular.class, form.getID());
    fa = new FormularAufbereitung(file);
    DBIterator it = Einstellungen.getDBService().createList(Lastschrift.class);
    it.addFilter("abrechnungslauf = ?", abrl.getID());
    if (!mitMail)
    {
      it.addFilter("(email is null or length(email)=0)");
    }
    it.setOrder("order by name, vorname");
    while (it.hasNext())
    {
      Lastschrift ls = (Lastschrift) it.next();
      aufbereitenFormular(ls, fo);
    }
    fa.showFormular();

  }

  private void generiereEMail(Object currentObject) throws IOException
  {
    Abrechnungslauf abrl = (Abrechnungslauf) currentObject;
    DBIterator it = Einstellungen.getDBService().createList(Lastschrift.class);
    it.addFilter("abrechnungslauf = ?", abrl.getID());
    it.addFilter("email is not null and length(email) > 0");
    it.setOrder("order by name, vorname");
    String betr = (String) getMailSubject().getValue();
    String text = (String) getMailBody().getValue();
    sendeMail(it, betr, text);
  }

  private void aufbereitenFormular(Lastschrift ls, Formular fo)
      throws RemoteException
  {
    Map<String, Object> map = new LastschriftMap().getMap(ls, null);
    map = new AllgemeineMap().getMap(map);
    fa.writeForm(fo, map);
  }

  private void sendeMail(final DBIterator it, final String betr,
      final String txt) throws RemoteException
  {

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
                  .getEinstellung().getSmtpFromAnzeigename(), 
                  Einstellungen.getEinstellung().getMailAlwaysBcc(),
                  Einstellungen.getEinstellung().getMailAlwaysCc(), Einstellungen
                  .getEinstellung().getSmtpSsl(), Einstellungen
                  .getEinstellung().getSmtpStarttls(),
                  Einstellungen.getImapCopyData());

          Velocity.init();
          Logger.debug("preparing velocity context");
          monitor.setStatus(ProgressMonitor.STATUS_RUNNING);
          monitor.setPercentComplete(0);
          int sentCount = 0;
          while (it.hasNext())
          {
            Lastschrift ls = (Lastschrift) it.next();
            VelocityContext context = new VelocityContext();
            context.put("dateformat", new JVDateFormatTTMMJJJJ());
            context.put("decimalformat", Einstellungen.DECIMALFORMAT);
            context.put("email", ls.getEmail());
            Map<String, Object> map = new LastschriftMap().getMap(ls, null);
            VarTools.add(context, map);

            StringWriter wtext1 = new StringWriter();
            Velocity.evaluate(context, wtext1, "LOG", betr);

            StringWriter wtext2 = new StringWriter();
            Velocity.evaluate(context, wtext2, "LOG", txt);

            sender.sendMail(ls.getEmail(), wtext1.getBuffer().toString(),
                wtext2.getBuffer().toString(), new TreeSet<MailAnhang>());

            // Mail in die Datenbank schreiben
            if (ls.getMitglied() != null)
            {
              Mail mail = (Mail) Einstellungen.getDBService().createObject(
                  Mail.class, null);
              Timestamp ts = new Timestamp(new Date().getTime());
              mail.setBearbeitung(ts);
              mail.setBetreff(wtext1.getBuffer().toString());
              mail.setTxt(wtext2.getBuffer().toString());
              mail.setVersand(ts);
              mail.store();
              MailEmpfaenger empf = (MailEmpfaenger) Einstellungen
                  .getDBService().createObject(MailEmpfaenger.class, null);
              empf.setMail(mail);
              empf.setMitglied(ls.getMitglied());
              empf.setVersand(ts);
              empf.store();
            }

            sentCount++;
            monitor.log(ls.getEmail() + " - versendet");

          }
          monitor.setPercentComplete(100);
          monitor.setStatus(ProgressMonitor.STATUS_DONE);
          monitor.setStatusText(MessageFormat.format(
              "Anzahl verschickter Mails: {0}", sentCount + ""));
          GUI.getStatusBar().setSuccessText(
              "Mail" + (sentCount > 1 ? "s" : "") + " verschickt");
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

}
