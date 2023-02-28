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

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.TreeSet;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.TabFolder;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.Variable.AllgemeineMap;
import de.jost_net.JVerein.Variable.LastschriftMap;
import de.jost_net.JVerein.Variable.VarTools;
import de.jost_net.JVerein.gui.input.FormularInput;
import de.jost_net.JVerein.io.Ct1Ueberweisung;
import de.jost_net.JVerein.io.FormularAufbereitung;
import de.jost_net.JVerein.io.MailSender;
import de.jost_net.JVerein.keys.Abrechnungsausgabe;
import de.jost_net.JVerein.keys.FormularArt;
import de.jost_net.JVerein.rmi.Abrechnungslauf;
import de.jost_net.JVerein.rmi.Formular;
import de.jost_net.JVerein.rmi.Lastschrift;
import de.jost_net.JVerein.rmi.Mail;
import de.jost_net.JVerein.rmi.MailAnhang;
import de.jost_net.JVerein.rmi.MailEmpfaenger;
import de.jost_net.JVerein.util.Dateiname;
import de.jost_net.JVerein.util.Datum;
import de.jost_net.JVerein.util.JVDateFormatDATETIME;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.DateInput;
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

  private TabFolder folder = null;

  private SelectInput output = null;

  private TextInput mailsubject = null;

  private TextAreaInput mailbody = null;

  public static final String EMAIL = "EMail";

  public static final String PDF1 = "PDF (Lastschriften ohne Mailadresse)";

  public static final String PDF2 = "PDF (Alle)";

  private SelectInput pdfModus = null;

  public static final String NICHT_EINZELN = "eine PDF-Datei";

  public static final String EINZELN_NUMMERIERT = "einzelne PDF-Dateien, nummeriert";

  public static final String EINZELN_MITGLIEDSNUMMER = "einzelne PDF-Dateien, mit Mitgliedsnummer";

  public static final String EINZELN_NUMMERIERT_UND_MNR = "einzelne PDF-Dateien, nummeriert mit Mitgliedsnummer";

  private FormularInput formular = null;

  private FormularAufbereitung fa;

  private DateInput ausfuehrungsdatum;

  private SelectInput ct1ausgabe;

  private TextInput verwendungszweck;

  public PreNotificationControl(AbstractView view)
  {
    super(view);
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public TabFolder getFolder(Composite parent)
  {
    if (folder != null)
    {
      return folder;
    }
    folder = new TabFolder(parent, SWT.NONE);
    folder.setSelection(settings.getInt("tab.selection", 0));
    return folder;
  }

  public SelectInput getPdfModus()
  {
    if (pdfModus != null)
    {
      return pdfModus;
    }
    Object[] values = new Object[] { NICHT_EINZELN, EINZELN_NUMMERIERT,
        EINZELN_MITGLIEDSNUMMER, EINZELN_NUMMERIERT_UND_MNR };
    pdfModus = new SelectInput(values,
        settings.getString("pdfModus", NICHT_EINZELN));
    pdfModus.setName("PDF als");
    return pdfModus;
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

  public FormularInput getFormular(FormularArt formulartyp)
      throws RemoteException
  {
    if (formular != null)
    {
      return formular;
    }
    formular = new FormularInput(formulartyp);
    return formular;
  }

  public TextInput getMailSubject()
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

  public DateInput getAusfuehrungsdatum()
  {
    if (ausfuehrungsdatum != null)
    {
      return ausfuehrungsdatum;
    }
    ausfuehrungsdatum = new DateInput();
    ausfuehrungsdatum.setName("Ausführungsdatum");
    return ausfuehrungsdatum;
  }

  public SelectInput getct1Ausgabe()
  {
    if (ct1ausgabe != null)
    {
      return ct1ausgabe;
    }
    Abrechnungsausgabe aus = Abrechnungsausgabe.getByKey(
        settings.getInt("ct1ausgabe", Abrechnungsausgabe.SEPA_DATEI.getKey()));
    if (aus != Abrechnungsausgabe.SEPA_DATEI
        && aus != Abrechnungsausgabe.HIBISCUS)
    {
      aus = Abrechnungsausgabe.HIBISCUS;
    }
    ct1ausgabe = new SelectInput(Abrechnungsausgabe.values(), aus);
    ct1ausgabe.setName("Ausgabe");
    return ct1ausgabe;
  }

  public TextInput getVerwendungszweck()
  {
    if (verwendungszweck != null)
    {
      return verwendungszweck;
    }
    verwendungszweck = new TextInput(
        settings.getString("verwendungszweck", ""));
    verwendungszweck.setName("Verwendungszweck");
    return verwendungszweck;
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
          String pdfMode = (String) getPdfModus().getValue();

          settings.setAttribute("output", val);
          settings.setAttribute("pdfModus", pdfMode);
          settings.setAttribute("mail.subject",
              (String) mailsubject.getValue());
          settings.setAttribute("mail.body", (String) mailbody.getValue());
          settings.setAttribute("tab.selection", folder.getSelectionIndex());

          if (val.equals(PDF1))
          {
            generierePDF(currentObject, false, pdfMode);
          }
          if (val.equals(PDF2))
          {
            generierePDF(currentObject, true, pdfMode);
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
    }, null, true, "walking.png");
    return button;
  }

  public Button getStart1ctUeberweisungButton(final Object currentObject)
  {
    Button button = new Button("starten", new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        try
        {
          Abrechnungsausgabe aa = (Abrechnungsausgabe) ct1ausgabe.getValue();
          settings.setAttribute("ct1ausgabe", aa.getKey());
          if (ausfuehrungsdatum.getValue() == null)
          {
            GUI.getStatusBar().setErrorText("Ausführungsdatum fehlt");
            return;
          }
          Date d = (Date) ausfuehrungsdatum.getValue();
          settings.setAttribute("faelligkeitsdatum",
              new JVDateFormatDATETIME().format(d));
          settings.setAttribute("verwendungszweck",
              (String) getVerwendungszweck().getValue());
          settings.setAttribute("tab.selection", folder.getSelectionIndex());
          generiere1ct(currentObject);
        }
        catch (Exception e)
        {
          Logger.error("Fehler", e);
          GUI.getStatusBar().setErrorText(e.getMessage());
        }
      }
    }, null, true, "walking.png");
    return button;
  }

  private void generierePDF(Object currentObject, boolean mitMail,
      String pdfMode) throws IOException
  {
    boolean einzelnePdfs = false;
    if (pdfMode.equals(EINZELN_NUMMERIERT)
        || pdfMode.equals(EINZELN_MITGLIEDSNUMMER)
        || pdfMode.equals(EINZELN_NUMMERIERT_UND_MNR))
    {
      einzelnePdfs = true;
    }

    Abrechnungslauf abrl = (Abrechnungslauf) currentObject;
    FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
    fd.setText("Ausgabedatei wählen.");
    String path = settings.getString("lastdir",
        System.getProperty("user.home"));
    if (path != null && path.length() > 0)
    {
      fd.setFilterPath(path);
    }
    fd.setFileName(new Dateiname("prenotification", "",
        Einstellungen.getEinstellung().getDateinamenmuster(), "PDF").get());
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
    Formular form = (Formular) getFormular(FormularArt.SEPA_PRENOTIFICATION)
        .getValue();
    if (form == null)
    {
      throw new IOException("kein SEPA Pre-Notification-Formular ausgewählt");
    }
    Formular fo = (Formular) Einstellungen.getDBService()
        .createObject(Formular.class, form.getID());
    if (!einzelnePdfs)
    {
      fa = new FormularAufbereitung(file);
    }
    DBIterator<Lastschrift> it = Einstellungen.getDBService()
        .createList(Lastschrift.class);
    it.addFilter("abrechnungslauf = ?", abrl.getID());
    if (!mitMail)
    {
      it.addFilter("(email is null or length(email)=0)");
    }
    it.setOrder("order by name, vorname");

    int dateinummer = 0;
    String postfix = ".PDF";
    String prefix = s.substring(0, s.length() - postfix.length());

    while (it.hasNext())
    {
      Lastschrift ls = it.next();

      if (einzelnePdfs)
      {
        // schalte Dateinamen um
        StringBuilder sb = new StringBuilder(prefix);
        if (pdfMode.equals(EINZELN_MITGLIEDSNUMMER)
            || pdfMode.equals(EINZELN_NUMMERIERT_UND_MNR))
        {
          sb.append("_");
          sb.append(ls.getMitglied().getID());
        }
        if (pdfMode.equals(EINZELN_NUMMERIERT)
            || pdfMode.equals(EINZELN_NUMMERIERT_UND_MNR))
        {
          sb.append(String.format("_%05d", dateinummer));
        }
        sb.append(postfix);

        final File fx = new File(sb.toString());
        fa = new FormularAufbereitung(fx);
      }

      aufbereitenFormular(ls, fo);

      if (einzelnePdfs)
      {
        fa.closeFormular();
      }
      dateinummer++;
    }

    fa.showFormular();

  }

  private void generiere1ct(Object currentObject) throws Exception
  {
    Abrechnungslauf abrl = (Abrechnungslauf) currentObject;
    File file = null;
    Abrechnungsausgabe aa = Abrechnungsausgabe.getByKey(
        settings.getInt("ct1ausgabe", Abrechnungsausgabe.SEPA_DATEI.getKey()));
    if (aa == Abrechnungsausgabe.SEPA_DATEI)
    {
      FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
      fd.setText("SEPA-Ausgabedatei wählen.");
      String path = settings.getString("lastdir",
          System.getProperty("user.home"));
      if (path != null && path.length() > 0)
      {
        fd.setFilterPath(path);
      }
      fd.setFileName(new Dateiname("1ctueberweisung", "",
          Einstellungen.getEinstellung().getDateinamenmuster(), "XML").get());
      fd.setFilterExtensions(new String[] { "*.XML" });

      String s = fd.open();
      if (s == null || s.length() == 0)
      {
        return;
      }
      settings.setAttribute("ausgabedateiname", s);
      if (!s.endsWith(".XML"))
      {
        s = s + ".XML";
      }
      file = new File(s);
      settings.setAttribute("lastdir", file.getParent());
    }
    String faelligkeitsdatum = settings.getString("faelligkeitsdatum", null);
    Date faell = Datum.toDate(faelligkeitsdatum);
    Abrechnungsausgabe ct1ausgabe = Abrechnungsausgabe.getByKey(
        settings.getInt("ct1ausgabe", Abrechnungsausgabe.SEPA_DATEI.getKey()));
    String verwendungszweck = settings.getString("verwendungszweck", "");
    Ct1Ueberweisung ct1ueberweisung = new Ct1Ueberweisung();
    int anzahl = ct1ueberweisung.write(abrl, file, faell, ct1ausgabe,
        verwendungszweck);
    GUI.getStatusBar().setSuccessText("Anzahl Überweisungen: " + anzahl);
  }

  private void generiereEMail(Object currentObject) throws IOException
  {
    Abrechnungslauf abrl = (Abrechnungslauf) currentObject;
    DBIterator<Lastschrift> it = Einstellungen.getDBService()
        .createList(Lastschrift.class);
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

  private void sendeMail(final DBIterator<Lastschrift> it, final String betr,
      final String txt) throws RemoteException
  {

    BackgroundTask t = new BackgroundTask()
    {

      @Override
      public void run(ProgressMonitor monitor)
      {
        try
        {
          MailSender sender = new MailSender(
              Einstellungen.getEinstellung().getSmtpServer(),
              Einstellungen.getEinstellung().getSmtpPort(),
              Einstellungen.getEinstellung().getSmtpAuthUser(),
              Einstellungen.getEinstellung().getSmtpAuthPwd(),
              Einstellungen.getEinstellung().getSmtpFromAddress(),
              Einstellungen.getEinstellung().getSmtpFromAnzeigename(),
              Einstellungen.getEinstellung().getMailAlwaysBcc(),
              Einstellungen.getEinstellung().getMailAlwaysCc(),
              Einstellungen.getEinstellung().getSmtpSsl(),
              Einstellungen.getEinstellung().getSmtpStarttls(),
              Einstellungen.getEinstellung().getMailVerzoegerung(),
              Einstellungen.getImapCopyData());

          Velocity.init();
          Logger.debug("preparing velocity context");
          monitor.setStatus(ProgressMonitor.STATUS_RUNNING);
          monitor.setPercentComplete(0);
          int sentCount = 0;
          while (it.hasNext())
          {
            Lastschrift ls =it.next();
            VelocityContext context = new VelocityContext();
            context.put("dateformat", new JVDateFormatTTMMJJJJ());
            context.put("decimalformat", Einstellungen.DECIMALFORMAT);
            context.put("email", ls.getEmail());

            Map<String, Object> map = new AllgemeineMap().getMap(null);
            map = new LastschriftMap().getMap(ls, map);
            VarTools.add(context, map);

            StringWriter wtext1 = new StringWriter();
            Velocity.evaluate(context, wtext1, "LOG", betr);

            StringWriter wtext2 = new StringWriter();
            Velocity.evaluate(context, wtext2, "LOG", txt);

            try
            {
              sender.sendMail(ls.getEmail(), wtext1.getBuffer().toString(),
                  wtext2.getBuffer().toString(), new TreeSet<MailAnhang>());

              // Mail in die Datenbank schreiben
              if (ls.getMitglied() != null)
              {
                Mail mail = (Mail) Einstellungen.getDBService()
                    .createObject(Mail.class, null);
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
            catch (Exception e)
            {
              Logger.error("Fehler beim Mailversand", e);
              monitor.log(ls.getEmail() + " - " + e.getMessage());
            }
          }
          monitor.setPercentComplete(100);
          monitor.setStatus(ProgressMonitor.STATUS_DONE);
          monitor.setStatusText(
              String.format("Anzahl verschickter Mails: %d", sentCount));
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
