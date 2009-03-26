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
 * Revision 1.1  2009/02/15 20:03:36  jost
 * Erster Code für neue Report-Mimik
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.formatter.ReportartFormatter;
import de.jost_net.JVerein.io.JasperreportsInterface;
import de.jost_net.JVerein.keys.Reportart;
import de.jost_net.JVerein.rmi.Formular;
import de.jost_net.JVerein.rmi.Report;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.FileInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Column;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class ReportControl extends AbstractControl
{
  private de.willuhn.jameica.system.Settings settings;

  private TablePart reportList;

  private TextInput bezeichnung;

  private SelectInput art;

  private FileInput datei;

  private Report report;

  private Reportart reportart = null;

  public ReportControl(AbstractView view)
  {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public Report getReport()
  {
    if (report != null)
    {
      return report;
    }
    report = (Report) getCurrentObject();
    return report;
  }

  public TextInput getBezeichnung() throws RemoteException
  {
    if (bezeichnung != null)
    {
      return bezeichnung;
    }
    bezeichnung = new TextInput((String) getReport().getBezeichnung(), 50);
    return bezeichnung;
  }

  public SelectInput getArt() throws RemoteException
  {
    if (art != null)
    {
      return art;
    }
    art = new SelectInput(Reportart.getArray(), new Reportart(
        (Integer) getReport().getArt()));
    return art;
  }

  public FileInput getDatei() throws RemoteException
  {
    if (datei != null)
    {
      return datei;
    }
    datei = new FileInput("", false, new String[] { "*.jrxml" });
    return datei;
  }

  /**
   * This method stores the project using the current values.
   */
  public void handleStore()
  {
    try
    {
      Report r = getReport();
      r.setBezeichnung((String) getBezeichnung().getValue());
      Reportart ra = (Reportart) getArt().getValue();
      r.setArt(ra.getKey());
      String dat = (String) getDatei().getValue();

      if (dat.length() > 0)
      {
        FileInputStream fis = new FileInputStream(dat);
        byte[] b = new byte[fis.available()];
        fis.read(b);
        fis.close();
        r.setQuelle(b);
        JasperreportsInterface jr = new JasperreportsInterface(r);
        jr.compile();
        Date jetzt = new Date();
        r.setAenderung(new Timestamp(jetzt.getTime()));
      }

      r.store();
      GUI.getStatusBar().setSuccessText("Report gespeichert");
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler beim Speichern des Reports";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
    catch (ApplicationException e)
    {
      Logger.error("Fehler", e);
      GUI.getStatusBar().setErrorText(e.getMessage());
    }
    catch (FileNotFoundException e)
    {
      Logger.error("Fehler", e);
      GUI.getStatusBar().setErrorText("Datei nicht gefunden");
    }
    catch (IOException e)
    {
      Logger.error("Fehler", e);
      GUI.getStatusBar().setErrorText("Ein-/Ausgabe-Fehler");
    }
  }

  public TablePart getReportList(Reportart reportart) throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator reports = service.createList(Report.class);
    this.reportart = reportart;
    if (reportart != null)
    {
      reports.addFilter("art = ?", new Object[] { reportart.getKey() });
    }
    reports.setOrder("ORDER BY bezeichnung");

    reportList = new TablePart(reports, null);
    reportList.addColumn("Bezeichnung", "bezeichnung");
    if (reportart == null)
    {
      reportList.addColumn("Art", "art", new ReportartFormatter(), false,
          Column.ALIGN_LEFT);
    }
    reportList.addColumn("letzte Änderung", "aenderung");
    reportList.addColumn("letzte Nutzung", "nutzung");
    reportList.setRememberColWidths(true);
    // reportList.setContextMenu(new FormularMenu());
    reportList.setRememberOrder(true);
    reportList.setSummary(false);
    return reportList;
  }

  public void refreshTable() throws RemoteException
  {
    reportList.removeAll();
    DBIterator reports = Einstellungen.getDBService().createList(Report.class);
    if (reportart != null)
    {
      reports.addFilter("art = ?", new Object[] { reportart.getKey() });
    }
    reports.setOrder("ORDER BY bezeichnung");
    while (reports.hasNext())
    {
      reportList.addItem((Formular) reports.next());
    }
  }
}
