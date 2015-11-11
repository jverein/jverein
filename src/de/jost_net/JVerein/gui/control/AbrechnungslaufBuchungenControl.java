/**********************************************************************
 * $Author: Dietmar Janz $
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
 ***********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.io.File;
import java.rmi.RemoteException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.MitgliedDetailAction;
import de.jost_net.JVerein.gui.formatter.ZahlungswegFormatter;
import de.jost_net.JVerein.io.AbrechnungslaufPDF;
import de.jost_net.JVerein.rmi.Abrechnungslauf;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.jost_net.JVerein.util.Dateiname;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.IntegerInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.Column;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class AbrechnungslaufBuchungenControl extends AbstractControl
{

  private de.willuhn.jameica.system.Settings settings;

  private IntegerInput lauf;

  private DateInput datum;

  private TextInput zahlungsgrund;

  private TextInput bm;

  private String bemerkung;

  private Abrechnungslauf abrl;

  private TablePart SollbuchungsList;

  public AbrechnungslaufBuchungenControl(AbstractView view)
  {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
    abrl = (Abrechnungslauf) getCurrentObject();
  }

  public IntegerInput getLauf() throws RemoteException
  {
    if (lauf != null)
    {
      return lauf;
    }
    lauf = new IntegerInput(abrl.getNr());
    lauf.setEnabled(false);
    return lauf;
  }

  public DateInput getDatum(boolean withFocus) throws RemoteException
  {
    if (datum != null)
    {
      return datum;
    }
    datum = new DateInput(abrl.getDatum());
    datum.setEnabled(false);
    if (withFocus)
    {
      datum.focus();
    }
    return datum;
  }

  public TextInput getZahlungsgrund() throws RemoteException
  {
    if (zahlungsgrund != null)
    {
      return zahlungsgrund;
    }
    zahlungsgrund = new TextInput(abrl.getZahlungsgrund());
    return zahlungsgrund;
  }

  public TextInput getBemerkung() throws RemoteException
  {
    if (bm != null)
    {
      return bm;
    }
    bemerkung = abrl.getBemerkung();
    if (bemerkung == null)
      bm = new TextInput(" ", 35);
    else
      bm = new TextInput(bemerkung, 35);
    bm.setEnabled(false);
    return bm;
  }

  public void handleStore()
  {
    //
  }

  private DBIterator getIterator(int lauf) throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator it = service.createList(Mitgliedskonto.class);

    it.addFilter("ABRECHNUNGSLAUF = (?)", lauf);
    it.setOrder("ORDER BY mitglied");
    return it;
  }

  public Part getSollbuchungsList() throws RemoteException
  {
    DBIterator it = getIterator((Integer) lauf.getValue());
    if (SollbuchungsList == null)
    {
      SollbuchungsList = new TablePart(it, new MitgliedDetailAction());
      SollbuchungsList.addColumn("Fälligkeit", "datum", new DateFormatter(
          new JVDateFormatTTMMJJJJ()));

      SollbuchungsList.addColumn("Mitglied", "mitglied");
      SollbuchungsList.addColumn("Zweck", "zweck1");
      SollbuchungsList.addColumn("Betrag", "betrag", new CurrencyFormatter("",
          Einstellungen.DECIMALFORMAT));
      SollbuchungsList.addColumn("Eingang", "istsumme", new CurrencyFormatter(
          "", Einstellungen.DECIMALFORMAT));
      SollbuchungsList.addColumn("Zahlungsweg", "zahlungsweg",
          new ZahlungswegFormatter(), false, Column.ALIGN_LEFT);
      SollbuchungsList.setRememberColWidths(true);
      SollbuchungsList.setRememberOrder(true);
      SollbuchungsList.setSummary(true);
    }
    else
    {
      SollbuchungsList.removeAll();
      while (it.hasNext())
      {
        SollbuchungsList.addItem(it.next());
      }
    }
    return SollbuchungsList;
  }

  public Button getStartListeButton()
  {
    Button b = new Button("Abrechnungslaufliste", new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        starteAuswertung();
      }
    }, null, true, "pdf.png"); // "true" defines this button as the default
    return b;
  }

  private void starteAuswertung()
  {

    try
    {
      DBIterator it = getIterator((Integer) lauf.getValue());

      FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
      fd.setText("Ausgabedatei wählen.");

      String path = settings.getString("lastdir",
          System.getProperty("user.home"));
      if (path != null && path.length() > 0)
      {
        fd.setFilterPath(path);
      }
      fd.setFileName(new Dateiname("abrechnungslauf", "", Einstellungen
          .getEinstellung().getDateinamenmuster(), "PDF").get());

      final String s = fd.open();

      if (s == null || s.length() == 0)
      {
        return;
      }

      final File file = new File(s);
      settings.setAttribute("lastdir", file.getParent());

      auswertungPDF(it, file, abrl);
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    }
  }

  private void auswertungPDF(final DBIterator it, final File file,
      final Abrechnungslauf lauf)
  {
    BackgroundTask t = new BackgroundTask()
    {

      @Override
      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        try
        {
          GUI.getStatusBar().setSuccessText("Auswertung gestartet");
          new AbrechnungslaufPDF(it, file, lauf);
        }
        catch (ApplicationException ae)
        {
          Logger.error("Fehler", ae);
          GUI.getStatusBar().setErrorText(ae.getMessage());
          throw ae;
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