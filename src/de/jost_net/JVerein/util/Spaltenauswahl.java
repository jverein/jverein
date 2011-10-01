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
 **********************************************************************/
package de.jost_net.JVerein.util;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.JVereinPlugin;
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

  public TablePart paintSpaltenpaintSpaltendefinitionTable(Composite parent)
      throws RemoteException
  {
    if (spaltendefinitionList != null)
    {
      return spaltendefinitionList;
    }
    spaltendefinitionList = new TablePart(spalten, null);
    spaltendefinitionList.addColumn(JVereinPlugin.getI18n().tr("Spalte"),
        "spaltenbezeichnung");
    spaltendefinitionList.setCheckable(true);
    spaltendefinitionList.setMulti(true);
    spaltendefinitionList.setSummary(false);
    spaltendefinitionList.paint(parent);
    for (int i = 0; i < spalten.size(); ++i)
    {
      spaltendefinitionList.setChecked(spalten.get(i), spalten.get(i)
          .isChecked());
    }

    return spaltendefinitionList;
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
