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
import java.rmi.RemoteException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.input.FormularInput;
import de.jost_net.JVerein.io.FormularAufbereitung;
import de.jost_net.JVerein.keys.Formularart;
import de.jost_net.JVerein.rmi.Formular;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.jost_net.JVerein.util.Dateiname;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.system.Settings;

public class PreNotificationControl extends AbstractControl
{

  private Settings settings = null;

  private CheckboxInput email = null;

  private CheckboxInput pdf = null;

  private FormularInput formular = null;

  private FormularAufbereitung fa;

  public PreNotificationControl(AbstractView view)
  {
    super(view);
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public CheckboxInput getEmail() throws RemoteException
  {
    if (email != null)
    {
      return email;
    }
    email = new CheckboxInput(false);
    return email;
  }

  public CheckboxInput getPdf() throws RemoteException
  {
    if (pdf != null)
    {
      return pdf;
    }
    pdf = new CheckboxInput(false);
    return pdf;
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

  public Button getStartButton(final Object currentObject)
  {
    Button button = new Button("starten", new Action()
    {
      @Override
      public void handleAction(Object context)
      {
        try
        {
          boolean pdf = (Boolean) getPdf().getValue();
          if (pdf)
          {
            generierePDF(currentObject);
          }
        }
        catch (Exception e)
        {
          GUI.getStatusBar().setErrorText(e.getMessage());
        }
      }
    }, null, true, "go.png");
    return button;
  }

  private void generierePDF(Object currentObject) throws IOException
  {
    System.out.println(currentObject);
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
    ArrayList<ArrayList<Mitgliedskonto>> mks = null;
    DBIterator it = Einstellungen.getDBService().createList(
        Mitgliedskonto.class);
    // Date d = null;
    // if (getVondatum(datumverwendung).getValue() != null)
    // {
    // d = (Date) getVondatum(datumverwendung).getValue();
    // if (d != null)
    // {
    // settings.setAttribute(datumverwendung + "datumvon",
    // new JVDateFormatTTMMJJJJ().format(d));
    // }
    //
    // it.addFilter("datum >= ?", new Object[] { d });
    // }
    // else
    // {
    // settings.setAttribute(datumverwendung + "datumvon", "");
    // }
    // if (getBisdatum(datumverwendung).getValue() != null)
    // {
    // d = (Date) getBisdatum(datumverwendung).getValue();
    // if (d != null)
    // {
    // settings.setAttribute(datumverwendung + "datumbis",
    // new JVDateFormatTTMMJJJJ().format(d));
    // }
    // it.addFilter("datum <= ?", new Object[] { d });
    // }
    // else
    // {
    // settings.setAttribute(datumverwendung + "datumbis", "");
    // }
    // if ((Boolean) getOhneAbbucher().getValue())
    // {
    // it.addFilter("zahlungsweg <> ?",
    // new Object[] { Zahlungsweg.BASISLASTSCHRIFT });
    // }
    //
    // Mitgliedskonto[] mk = new Mitgliedskonto[it.size()];
    // int i = 0;
    // while (it.hasNext())
    // {
    // mk[i] = (Mitgliedskonto) it.next();
    // i++;
    // }
    // mks = getRechnungsempfaenger(mk);
    // }
    // for (ArrayList<Mitgliedskonto> mk : mks)
    // {
    // aufbereitenFormular(mk, fo);
    // }
    // fa.showFormular();

  }
  // private void doAbrechnung() throws ApplicationException, RemoteException
  // {
  // File sepafile;
  // settings.setAttribute("zahlungsgrund", (String) zahlungsgrund.getValue());
  // settings.setAttribute("zusatzbetraege", (Boolean) zusatzbetrag.getValue());
  // settings
  // .setAttribute("kursteilnehmer", (Boolean) kursteilnehmer.getValue());
  // settings.setAttribute("kompakteabbuchung",
  // (Boolean) kompakteabbuchung.getValue());
  // settings.setAttribute("sepaprint", (Boolean) sepaprint.getValue());
  // Abrechnungsausgabe aa = (Abrechnungsausgabe) ausgabe.getValue();
  // settings.setAttribute("abrechnungsausgabe", aa.getKey());
  // Integer modus = null;
  // try
  // {
  // modus = (Integer) getAbbuchungsmodus().getValue();
  // }
  // catch (RemoteException e)
  // {
  // throw new ApplicationException(
  // "Interner Fehler - kann Abrechnungsmodus nicht auslesen");
  // }
  // if (faelligkeit.getValue() == null)
  // {
  // throw new ApplicationException("Fälligkeitsdatum fehlt");
  // }
  // Date f = (Date) faelligkeit.getValue();
  // if (f.before(new Date()))
  // {
  // throw new ApplicationException("Fälligkeit muss in der Zukunft liegen");
  // }
  // Date vondatum = null;
  // if (stichtag.getValue() == null)
  // {
  // throw new ApplicationException("Stichtag fehlt");
  // }
  // if (modus != Abrechnungsmodi.KEINBEITRAG)
  // {
  // vondatum = (Date) getVondatum().getValue();
  // if (modus == Abrechnungsmodi.EINGETRETENEMITGLIEDER && vondatum == null)
  // {
  // throw new ApplicationException("von-Datum fehlt");
  // }
  // }
  // Integer ausgabe;
  // aa = (Abrechnungsausgabe) this.getAbbuchungsausgabe().getValue();
  // ausgabe = aa.getKey();
  //
  // if (ausgabe == Abrechnungsausgabe.SEPA_DATEI)
  // {
  // FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
  // fd.setText("SEPA-Ausgabedatei wählen.");
  //
  // String path = settings.getString("lastdir",
  // System.getProperty("user.home"));
  // if (path != null && path.length() > 0)
  // {
  // fd.setFilterPath(path);
  // }
  // fd.setFileName(new Dateiname("abbuchung", "", Einstellungen
  // .getEinstellung().getDateinamenmuster(), "XML").get());
  // String file = fd.open();
  //
  // if (file == null || file.length() == 0)
  // {
  // throw new ApplicationException("keine Datei ausgewählt!");
  // }
  // sepafile = new File(file);
  // }
  // else
  // {
  // try
  // {
  // sepafile = File.createTempFile("sepa", null);
  // }
  // catch (IOException e)
  // {
  // throw new ApplicationException(
  // "Temporäre Datei für die Abbuchung kann nicht erstellt werden.");
  // }
  // }
  //
  // // PDF-Datei für Basislastschrift2PDF
  // String pdffile = null;
  // final Boolean pdfprintb = (Boolean) sepaprint.getValue();
  // if (pdfprintb)
  // {
  // FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
  // fd.setText("PDF-Ausgabedatei wählen");
  //
  // String path = settings.getString("lastdir",
  // System.getProperty("user.home"));
  // if (path != null && path.length() > 0)
  // {
  // fd.setFilterPath(path);
  // }
  // fd.setFileName(new Dateiname("abbuchung", "", Einstellungen
  // .getEinstellung().getDateinamenmuster(), "PDF").get());
  // pdffile = fd.open();
  // }
  //
  // // Wir merken uns noch das Verzeichnis fürs nächste mal
  // settings.setAttribute("lastdir", sepafile.getParent());
  // final AbrechnungSEPAParam abupar;
  // try
  // {
  // abupar = new AbrechnungSEPAParam(this, sepafile, pdffile);
  // }
  // catch (RemoteException e)
  // {
  // throw new ApplicationException(e);
  // }
  // BackgroundTask t = new BackgroundTask()
  // {
  // @Override
  // public void run(ProgressMonitor monitor) throws ApplicationException
  // {
  // try
  // {
  // new AbrechnungSEPA(abupar, monitor);
  // monitor.setPercentComplete(100);
  // monitor.setStatus(ProgressMonitor.STATUS_DONE);
  // GUI.getStatusBar().setSuccessText(
  // MessageFormat.format(
  // "Abrechung durchgeführt., SEPA-Datei {0} geschrieben.",
  // abupar.sepafile.getAbsolutePath()));
  // GUI.getCurrentView().reload();
  // }
  // catch (ApplicationException ae)
  // {
  // monitor.setStatusText(ae.getMessage());
  // monitor.setStatus(ProgressMonitor.STATUS_ERROR);
  // GUI.getStatusBar().setErrorText(ae.getMessage());
  // throw ae;
  // }
  // catch (Exception e)
  // {
  // monitor.setStatus(ProgressMonitor.STATUS_ERROR);
  // Logger.error(MessageFormat.format(
  // "error while reading objects from {0}",
  // abupar.sepafile.getAbsolutePath()), e);
  // ApplicationException ae = new ApplicationException(
  // MessageFormat.format(
  // "Fehler beim erstellen der Abbuchungsdatei: {0}",
  // abupar.sepafile.getAbsolutePath()), e);
  // monitor.setStatusText(ae.getMessage());
  // GUI.getStatusBar().setErrorText(ae.getMessage());
  // throw ae;
  // }
  // }
  //
  // @Override
  // public void interrupt()
  // {
  // //
  // }
  //
  // @Override
  // public boolean isInterrupted()
  // {
  // return false;
  // }
  // };
  // Application.getController().start(t);
  // }
}
