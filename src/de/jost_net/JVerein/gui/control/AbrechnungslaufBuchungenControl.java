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

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.MitgliedDetailAction;
import de.jost_net.JVerein.gui.formatter.ZahlungswegFormatter;
import de.jost_net.JVerein.rmi.Abrechnungslauf;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.IntegerInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Column;
import de.willuhn.jameica.gui.parts.TablePart;

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

  public Part getSollbuchungsList() throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator SollBuchungen = service.createList(Mitgliedskonto.class);

    SollBuchungen.addFilter("ABRECHNUNGSLAUF = (?)", lauf.getValue());
    SollBuchungen.setOrder("ORDER BY mitglied");

    if (SollbuchungsList == null)
    {
      SollbuchungsList = new TablePart(SollBuchungen,
          new MitgliedDetailAction());
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
      while (SollBuchungen.hasNext())
      {
        SollbuchungsList.addItem(SollBuchungen.next());
      }
    }
    return SollbuchungsList;
  }

}