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
package de.jost_net.JVerein.util;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import de.willuhn.jameica.gui.formatter.Formatter;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.system.Settings;

public abstract class Spaltenauswahl
{

  private Settings settings;

  private String tabelle;

  private TablePart spaltendefinitionList;

  private ArrayList<Spalte> spalten;

  public Spaltenauswahl(String tabelle)
  {
    this.settings = new Settings(this.getClass());
    this.tabelle = tabelle;
    spalten = new ArrayList<Spalte>();
  }

  public void add(String spaltenbezeichnung, String spaltenname,
      boolean defaultvalue, boolean nurMitglied)
  {
    spalten.add(new Spalte(spaltenbezeichnung, spaltenname, settings
        .getBoolean(tabelle + "." + spaltenname, defaultvalue), nurMitglied));
  }

  public void add(String spaltenbezeichnung, String spaltenname,
      boolean defaultvalue, Formatter formatter, int align, boolean nurMitglied)
  {
    spalten.add(new Spalte(spaltenbezeichnung, spaltenname, settings
        .getBoolean(tabelle + "." + spaltenname, defaultvalue), formatter,
        align, nurMitglied));
  }

  public void setColumns(TablePart part, int adresstyp)
  {
    for (Spalte spalte : spalten)
    {
      if (spalte.isChecked())
      {
        if ((adresstyp == 1) || adresstyp != 1 && spalte.isNurAdressen())
        {
          part.addColumn(spalte.getSpaltenbezeichnung(),
              spalte.getSpaltenname(), spalte.getFormatter());
        }
      }
    }
  }

  public ArrayList<Spalte> getSpalten()
  {
    return spalten;
  }

  public TablePart paintSpaltenpaintSpaltendefinitionTable()
      throws RemoteException
  {
    if (spaltendefinitionList != null)
    {
      return spaltendefinitionList;
    }
    spaltendefinitionList = new TablePart(new ArrayList<Spalte>(), null);
    spaltendefinitionList.addColumn("Spalte", "spaltenbezeichnung");
    spaltendefinitionList.setCheckable(true);
    spaltendefinitionList.setMulti(true);
    spaltendefinitionList.setSummary(false);
    for (Spalte sp : spalten)
    {
      spaltendefinitionList.addItem(sp, sp.isChecked());
    }

    return spaltendefinitionList;
  }

  public void setCheckSpalten()
  {
    for (Spalte spalte : spalten)
    {
      spaltendefinitionList.setChecked(spalte, spalte.isChecked());
    }
  }

  @SuppressWarnings("unchecked")
  public void save() throws RemoteException
  {
    for (Spalte spalte : spalten)
    {
      settings.setAttribute(tabelle + "." + spalte.getSpaltenname(), false);
    }
    List<Spalte> trues = spaltendefinitionList.getItems();
    for (int i = 0; i < trues.size(); i++)
    {
      Spalte spalte = trues.get(i);
      settings.setAttribute(tabelle + "." + spalte.getSpaltenname(), true);
    }
  }
}
