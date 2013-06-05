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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.input.ArbeitseinsatzUeberpruefungInput;
import de.jost_net.JVerein.gui.parts.ArbeitseinsatzUeberpruefungList;
import de.jost_net.JVerein.io.ArbeitseinsatzZeile;
import de.jost_net.JVerein.io.FileViewer;
import de.jost_net.JVerein.io.Reporter;
import de.jost_net.JVerein.keys.IntervallZusatzzahlung;
import de.jost_net.JVerein.rmi.Arbeitseinsatz;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Zusatzbetrag;
import de.jost_net.JVerein.util.Dateiname;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.DecimalInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class ArbeitseinsatzControl extends AbstractControl
{

  private Settings settings = null;

  private DateInput datum = null;

  private DecimalInput stunden = null;

  private Arbeitseinsatz aeins = null;

  private TextInput bemerkung = null;

  private ArbeitseinsatzUeberpruefungList arbeitseinsatzueberpruefungList;

  private SelectInput suchjahr = null;

  private ArbeitseinsatzUeberpruefungInput auswertungschluessel = null;

  public ArbeitseinsatzControl(AbstractView view)
  {
    super(view);
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public Arbeitseinsatz getArbeitseinsatz()
  {
    if (aeins != null)
    {
      return aeins;
    }
    aeins = (Arbeitseinsatz) getCurrentObject();
    return aeins;
  }

  public DateInput getDatum() throws RemoteException
  {
    if (datum != null)
    {
      return datum;
    }

    Date d = getArbeitseinsatz().getDatum();
    if (d == null)
    {
      d = new Date();
    }
    this.datum = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.datum.setTitle("Datum");
    this.datum.setText("Datum Arbeitseinsatz wählen");
    this.datum.addListener(new Listener()
    {

      @Override
      public void handleEvent(Event event)
      {
        Date date = (Date) datum.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return datum;
  }

  public DecimalInput getStunden() throws RemoteException
  {
    if (stunden != null)
    {
      return stunden;
    }
    stunden = new DecimalInput(getArbeitseinsatz().getStunden(),
        new DecimalFormat("###,###.##"));
    return stunden;
  }

  public TextInput getBemerkung() throws RemoteException
  {
    if (bemerkung != null)
    {
      return bemerkung;
    }
    bemerkung = new TextInput(getArbeitseinsatz().getBemerkung(), 50);
    bemerkung.setName("Bemerkung");
    return bemerkung;
  }

  public ArbeitseinsatzUeberpruefungInput getAuswertungSchluessel()
      throws RemoteException
  {
    if (auswertungschluessel != null)
    {
      return auswertungschluessel;
    }
    auswertungschluessel = new ArbeitseinsatzUeberpruefungInput(1);
    return auswertungschluessel;
  }

  public void handleStore()
  {
    try
    {
      Arbeitseinsatz ae = getArbeitseinsatz();
      ae.setDatum((Date) getDatum().getValue());
      ae.setStunden((Double) getStunden().getValue());
      ae.setBemerkung((String) getBemerkung().getValue());
      ae.store();
      GUI.getStatusBar().setSuccessText("Arbeitseinsatz gespeichert");
    }
    catch (ApplicationException e)
    {
      GUI.getStatusBar().setErrorText(e.getMessage());
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei speichern des Arbeitseinsatzes";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  public SelectInput getSuchJahr() throws RemoteException
  {
    if (suchjahr != null)
    {
      return suchjahr;
    }
    DBIterator list = Einstellungen.getDBService().createList(
        Arbeitseinsatz.class);
    list.setOrder("ORDER BY datum");
    Arbeitseinsatz ae = null;
    Calendar von = Calendar.getInstance();
    if (list.hasNext())
    {
      ae = (Arbeitseinsatz) list.next();
      von.setTime(ae.getDatum());
    }
    Calendar bis = Calendar.getInstance();
    ArrayList<Integer> jahre = new ArrayList<Integer>();

    for (int i = von.get(Calendar.YEAR); i <= bis.get(Calendar.YEAR); i++)
    {
      jahre.add(i);
    }

    suchjahr = new SelectInput(jahre, settings.getInt("jahr", jahre.get(0)));
    // suchjahr.setPleaseChoose("Bitte auswählen");
    suchjahr.setPreselected(settings.getInt("jahr", bis.get(Calendar.YEAR)));
    return suchjahr;
  }

  public Button getPDFAusgabeButton()
  {
    Button b = new Button("PDF-Ausgabe", new Action()
    {

      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        try
        {
          startePDFAuswertung();
        }
        catch (RemoteException e)
        {
          Logger.error(e.getMessage());
          throw new ApplicationException(
              "Fehler beim Start der PDF-Ausgabe der Arbeitseinsatzüberprüfung");
        }
      }
    }, null, true, "acroread.png");
    return b;
  }

  public Button getCSVAusgabeButton()
  {
    Button b = new Button("CSV-Ausgabe", new Action()
    {

      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        try
        {
          starteCSVAuswertung();
        }
        catch (RemoteException e)
        {
          Logger.error(e.getMessage());
          throw new ApplicationException(
              "Fehler beim Start der CSV-Ausgabe der Arbeitseinsatzüberprüfung");
        }
      }
    }, null, true, "csv_text.png");
    return b;
  }

  public Button getArbeitseinsatzAusgabeButton()
  {
    Button b = new Button("Zusatzbeträge generieren", new Action()
    {

      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        try
        {
          starteArbeitseinsatzGenerierung();
        }
        catch (RemoteException e)
        {
          Logger.error(e.getMessage());
          throw new ApplicationException(
              "Fehler beim der Zusatzbetragsgenerierung");
        }
      }
    }, null, true, "zusatzbetraege.png");
    return b;
  }

  private void startePDFAuswertung() throws RemoteException
  {
    FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
    fd.setText("Ausgabedatei wählen.");
    String path = settings
        .getString("lastdir", System.getProperty("user.home"));
    if (path != null && path.length() > 0)
    {
      fd.setFilterPath(path);
    }
    fd.setFileName(new Dateiname("arbeitseinsaetze", "", Einstellungen
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
    final GenericIterator it = getIterator();
    final int jahr = (Integer) getSuchJahr().getValue();
    final String sub = getAuswertungSchluessel().getText();
    settings.setAttribute("lastdir", file.getParent());
    BackgroundTask t = new BackgroundTask()
    {

      @Override
      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        try
        {
          FileOutputStream fos = new FileOutputStream(file);
          Reporter reporter = new Reporter(fos, MessageFormat.format(
              "Arbeitseinsätze {0}", jahr + ""), sub, it.size());
          reporter.addHeaderColumn("Mitglied", Element.ALIGN_LEFT, 60,
              BaseColor.LIGHT_GRAY);
          reporter.addHeaderColumn("Sollstunden", Element.ALIGN_RIGHT, 30,
              BaseColor.LIGHT_GRAY);
          reporter.addHeaderColumn("Iststunden", Element.ALIGN_RIGHT, 30,
              BaseColor.LIGHT_GRAY);
          reporter.addHeaderColumn("Differenz", Element.ALIGN_RIGHT, 30,
              BaseColor.LIGHT_GRAY);
          reporter.addHeaderColumn("Stundensatz", Element.ALIGN_RIGHT, 30,
              BaseColor.LIGHT_GRAY);
          reporter.addHeaderColumn("Gesamtbetrag", Element.ALIGN_RIGHT, 30,
              BaseColor.LIGHT_GRAY);
          reporter.createHeader();
          while (it.hasNext())
          {
            ArbeitseinsatzZeile z = (ArbeitseinsatzZeile) it.next();
            reporter.addColumn((String) z.getAttribute("namevorname"),
                Element.ALIGN_LEFT);
            reporter.addColumn((Double) z.getAttribute("soll"));
            reporter.addColumn((Double) z.getAttribute("ist"));
            reporter.addColumn((Double) z.getAttribute("differenz"));
            reporter.addColumn((Double) z.getAttribute("stundensatz"));
            reporter.addColumn((Double) z.getAttribute("gesamtbetrag"));
          }
          reporter.closeTable();
          reporter.close();
          fos.close();
          GUI.getStatusBar().setSuccessText("Auswertung gestartet");
          GUI.getCurrentView().reload();
        }
        catch (Exception e)
        {
          Logger.error("Fehler", e);
          GUI.getStatusBar().setErrorText(e.getMessage());
          throw new ApplicationException(e);
        }
        FileViewer.show(file);
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

  private void starteCSVAuswertung() throws RemoteException
  {
    FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
    fd.setText("Ausgabedatei wählen.");
    String path = settings
        .getString("lastdir", System.getProperty("user.home"));
    if (path != null && path.length() > 0)
    {
      fd.setFilterPath(path);
    }
    fd.setFileName(new Dateiname("arbeitseinsaetze", "", Einstellungen
        .getEinstellung().getDateinamenmuster(), "CSV").get());
    fd.setFilterExtensions(new String[] { "*.CSV" });

    String s = fd.open();
    if (s == null || s.length() == 0)
    {
      return;
    }
    if (!s.endsWith(".CSV"))
    {
      s = s + ".CSV";
    }
    final File file = new File(s);
    final GenericIterator it = getIterator();
    settings.setAttribute("lastdir", file.getParent());
    BackgroundTask t = new BackgroundTask()
    {

      @Override
      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        try
        {
          CsvPreference csvp = CsvPreference.EXCEL_PREFERENCE;
          csvp.setDelimiterChar(';');
          ICsvMapWriter writer = new CsvMapWriter(new FileWriter(file), csvp);

          final String[] header = new String[] { "name", "vorname", "strasse",
              "adressierungszusatz", "plz", "ort", "anrede", "soll", "ist",
              "differenz", "stundensatz", "gesamtbetrag" };
          writer.writeHeader(header);
          // set up some data to write
          while (it.hasNext())
          {
            ArbeitseinsatzZeile z = (ArbeitseinsatzZeile) it.next();
            final HashMap<String, ? super Object> data1 = new HashMap<String, Object>();
            Mitglied m = (Mitglied) z.getAttribute("mitglied");
            data1.put(header[0], m.getName());
            data1.put(header[1], m.getVorname());
            data1.put(header[2], m.getStrasse());
            data1.put(header[3], m.getAdressierungszusatz());
            data1.put(header[4], m.getPlz());
            data1.put(header[5], m.getOrt());
            data1.put(header[6], m.getAnrede());
            data1.put(header[7], z.getAttribute("soll"));
            data1.put(header[8], z.getAttribute("ist"));
            data1.put(header[9], z.getAttribute("differenz"));
            data1.put(header[10], z.getAttribute("stundensatz"));
            data1.put(header[11], z.getAttribute("gesamtbetrag"));
            writer.write(data1, header);
          }
          writer.close();
          GUI.getStatusBar().setSuccessText("Auswertung gestartet");
          GUI.getCurrentView().reload();
        }
        catch (Exception e)
        {
          Logger.error("Fehler", e);
          GUI.getStatusBar().setErrorText(e.getMessage());
          throw new ApplicationException(e);
        }
        FileViewer.show(file);
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

  private void starteArbeitseinsatzGenerierung() throws RemoteException
  {
    final GenericIterator it = getIterator();
    final int jahr = (Integer) getSuchJahr().getValue();

    BackgroundTask t = new BackgroundTask()
    {

      @Override
      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        try
        {
          while (it.hasNext())
          {
            ArbeitseinsatzZeile z = (ArbeitseinsatzZeile) it.next();
            Zusatzbetrag zb = (Zusatzbetrag) Einstellungen.getDBService()
                .createObject(Zusatzbetrag.class, null);
            Double betrag = (Double) z.getAttribute("gesamtbetrag");
            betrag = betrag * -1;
            zb.setBetrag(betrag);
            zb.setBuchungstext(MessageFormat.format("Arbeitseinsatz {0}", +jahr
                + ""));
            zb.setFaelligkeit(new Date());
            zb.setStartdatum(new Date());
            zb.setIntervall(IntervallZusatzzahlung.KEIN);
            zb.setMitglied(new Integer((String) z.getAttribute("mitgliedid")));
            zb.store();
          }
          GUI.getStatusBar().setSuccessText("Liste Arbeitseinsätze gestartet");
          GUI.getCurrentView().reload();
        }
        catch (Exception e)
        {
          Logger.error("Fehler", e);
          GUI.getStatusBar().setErrorText(e.getMessage());
          throw new ApplicationException(e);
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

  private GenericIterator getIterator() throws RemoteException
  {
    ArrayList<ArbeitseinsatzZeile> zeile = arbeitseinsatzueberpruefungList
        .getInfo();

    GenericIterator gi = PseudoIterator.fromArray(zeile
        .toArray(new GenericObject[zeile.size()]));
    return gi;
  }

  public Part getArbeitseinsatzUeberpruefungList() throws ApplicationException
  {
    try
    {
      settings.setAttribute("jahr", (Integer) getSuchJahr().getValue());
      settings.setAttribute("schluessel", (Integer) getAuswertungSchluessel()
          .getValue());

      if (arbeitseinsatzueberpruefungList == null)
      {
        arbeitseinsatzueberpruefungList = new ArbeitseinsatzUeberpruefungList(
            null, (Integer) getSuchJahr().getValue(),
            (Integer) getAuswertungSchluessel().getValue());
      }
      else
      {
        arbeitseinsatzueberpruefungList.setJahr((Integer) getSuchJahr()
            .getValue());
        arbeitseinsatzueberpruefungList
            .setSchluessel((Integer) getAuswertungSchluessel().getValue());
        ArrayList<ArbeitseinsatzZeile> zeile = arbeitseinsatzueberpruefungList
            .getInfo();
        arbeitseinsatzueberpruefungList.removeAll();
        for (ArbeitseinsatzZeile az : zeile)
        {
          arbeitseinsatzueberpruefungList.addItem(az);
        }
      }
    }
    catch (RemoteException e)
    {
      Logger.error("Fehler", e);
      throw new ApplicationException("Fehler aufgetreten", e);
    }
    return arbeitseinsatzueberpruefungList.getArbeitseinsatzUeberpruefungList();
  }
}
