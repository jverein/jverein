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

import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.menu.JahresabschlussMenu;
import de.jost_net.JVerein.gui.parts.JahressaldoList;
import de.jost_net.JVerein.io.SaldoZeile;
import de.jost_net.JVerein.rmi.Anfangsbestand;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Jahresabschluss;
import de.jost_net.JVerein.rmi.Konto;
import de.jost_net.JVerein.util.Datum;
import de.jost_net.JVerein.util.Geschaeftsjahr;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class JahresabschlussControl extends AbstractControl
{

  private de.willuhn.jameica.system.Settings settings;

  private TablePart jahresabschlussList;

  private Part jahresabschlusssaldoList;

  private DateInput von;

  private DateInput bis;

  private DateInput datum;

  private TextInput name;

  private Jahresabschluss jahresabschluss;

  private CheckboxInput anfangsbestaende;

  public JahresabschlussControl(AbstractView view)
  {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public Jahresabschluss getJahresabschluss()
  {
    if (jahresabschluss != null)
    {
      return jahresabschluss;
    }
    jahresabschluss = (Jahresabschluss) getCurrentObject();
    return jahresabschluss;
  }

  public DateInput getVon() throws RemoteException, ParseException
  {
    if (von != null)
    {
      return von;
    }
    von = new DateInput(getJahresabschluss().getVon());
    von.setEnabled(false);
    if (getJahresabschluss().isNewObject())
    {
      von.setValue(computeVonDatum());
    }
    return von;
  }

  private Date computeVonDatum() throws RemoteException, ParseException
  {
    DBIterator it = Einstellungen.getDBService().createList(
        Jahresabschluss.class);
    it.setOrder("ORDER BY bis DESC");
    if (it.hasNext())
    {
      Jahresabschluss ja = (Jahresabschluss) it.next();
      Calendar cal = Calendar.getInstance();
      cal.setTime(ja.getBis());
      cal.add(Calendar.DAY_OF_MONTH, 1);
      return cal.getTime();
    }
    it = Einstellungen.getDBService().createList(Buchung.class);
    it.setOrder("ORDER BY datum");
    if (it.hasNext())
    {
      Buchung b = (Buchung) it.next();
      Geschaeftsjahr gj = new Geschaeftsjahr(b.getDatum());
      return gj.getBeginnGeschaeftsjahr();
    }
    Geschaeftsjahr gj = new Geschaeftsjahr(new Date());
    return gj.getBeginnGeschaeftsjahr();
  }

  public DateInput getBis() throws RemoteException, ParseException
  {
    if (bis != null)
    {
      return bis;
    }
    Geschaeftsjahr gj = new Geschaeftsjahr((Date) von.getValue());
    bis = new DateInput(gj.getEndeGeschaeftsjahr());
    bis.setEnabled(false);
    return bis;
  }

  public DateInput getDatum()
  {
    if (datum != null)
    {
      return datum;
    }
    datum = new DateInput(new Date());
    datum.setEnabled(false);
    return datum;
  }

  public TextInput getName() throws RemoteException
  {
    if (name != null)
    {
      return name;
    }
    name = new TextInput(getJahresabschluss().getName(), 50);
    return name;
  }

  public CheckboxInput getAnfangsbestaende()
  {
    if (anfangsbestaende != null)
    {
      return anfangsbestaende;
    }
    anfangsbestaende = new CheckboxInput(false);
    return anfangsbestaende;
  }

  public Part getJahresabschlussSaldo() throws RemoteException
  {
    if (jahresabschlusssaldoList != null)
    {
      return jahresabschlusssaldoList;
    }
    try
    {
      jahresabschlusssaldoList = new JahressaldoList(null, new Geschaeftsjahr(
          (Date) getVon().getValue())).getSaldoList();
    }
    catch (ApplicationException e)
    {
      throw new RemoteException(e.getMessage());
    }
    catch (ParseException e)
    {
      throw new RemoteException(e.getMessage());
    }
    return jahresabschlusssaldoList;
  }

  /**
   * This method stores the project using the current values.
   */
  public void handleStore()
  {
    try
    {
      Jahresabschluss ja = getJahresabschluss();
      ja.setVon((Date) getVon().getValue());
      ja.setBis((Date) getBis().getValue());
      ja.setDatum((Date) getDatum().getValue());
      ja.setName((String) getName().getValue());
      ja.store();
      if ((Boolean) getAnfangsbestaende().getValue())
      {
        JahressaldoList jsl = new JahressaldoList(null, new Geschaeftsjahr(
            ja.getVon()));
        ArrayList<SaldoZeile> zeilen = jsl.getInfo();
        for (SaldoZeile z : zeilen)
        {
          String ktonr = (String) z.getAttribute("kontonummer");
          if (ktonr.length() > 0)
          {
            Double endbestand = (Double) z.getAttribute("endbestand");
            Anfangsbestand anf = (Anfangsbestand) Einstellungen.getDBService().createObject(
                Anfangsbestand.class, null);
            Konto konto = (Konto) z.getAttribute("konto");
            anf.setBetrag(endbestand);
            anf.setDatum(Datum.addTage(ja.getBis(), 1));
            anf.setKonto(konto);
            anf.store();
          }
        }
      }
      GUI.getStatusBar().setSuccessText("Jahresabschluss gespeichert");
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler beim speichern des Jahresabschlusses";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
    catch (ParseException e)
    {

      String fehler = "Fehler beim speichern des Jahresabschlusses";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
    catch (ApplicationException e)
    {
      GUI.getStatusBar().setErrorText(e.getMessage());
    }
  }

  public Part getJahresabschlussList() throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator jahresabschluesse = service.createList(Jahresabschluss.class);
    jahresabschluesse.setOrder("ORDER BY von desc");

    jahresabschlussList = new TablePart(jahresabschluesse, null);
    jahresabschlussList.addColumn("von", "von", new DateFormatter(
        new JVDateFormatTTMMJJJJ()));
    jahresabschlussList.addColumn("bis", "bis", new DateFormatter(
        new JVDateFormatTTMMJJJJ()));
    jahresabschlussList.addColumn("Datum", "datum", new DateFormatter(
        new JVDateFormatTTMMJJJJ()));
    jahresabschlussList.addColumn("Name", "name");
    jahresabschlussList.setRememberColWidths(true);
    jahresabschlussList.setContextMenu(new JahresabschlussMenu());
    jahresabschlussList.setRememberOrder(true);
    jahresabschlussList.setSummary(false);
    return jahresabschlussList;
  }

  public void refreshTable() throws RemoteException
  {
    jahresabschlussList.removeAll();
    DBIterator jahresabschluesse = Einstellungen.getDBService().createList(
        Jahresabschluss.class);
    jahresabschluesse.setOrder("ORDER BY von desc");
    while (jahresabschluesse.hasNext())
    {
      jahresabschlussList.addItem(jahresabschluesse.next());
    }
  }

}
