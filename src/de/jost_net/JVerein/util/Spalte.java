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

import de.willuhn.jameica.gui.formatter.Formatter;
import de.willuhn.jameica.gui.parts.Column;

public class Spalte
{
  private String spaltenbezeichnung;

  private String spaltenname;

  private Formatter formatter;

  private boolean checked;

  private int align;

  /**
   * Spezialfall Mitglied/Adressen
   */
  private boolean nurMitglied;

  public Spalte(String spaltenbezeichnung, String spaltenname, boolean checked,
      boolean nurMitglied)
  {
    this(spaltenbezeichnung, spaltenname, checked, null, Column.ALIGN_AUTO,
        nurMitglied);
  }

  public Spalte(String spaltenbezeichnung, String spaltenname, boolean checked,
      Formatter formatter, int align, boolean nurMitglied)
  {
    this.spaltenbezeichnung = spaltenbezeichnung;
    this.spaltenname = spaltenname;
    this.formatter = formatter;
    this.checked = checked;
    this.align = align;
    this.nurMitglied = nurMitglied;
  }

  @Override
  public boolean equals(Object arg0)
  {
    if (arg0 == null || !(arg0 instanceof Spalte))
      return false;

    Spalte o = (Spalte) arg0;
    return this.spaltenname.equals(o.spaltenname);
  }

  @Override
  public int hashCode()
  {
    return spaltenname.hashCode();
  }

  public String getSpaltenbezeichnung()
  {
    return this.spaltenbezeichnung;
  }

  public String getSpaltenname()
  {
    return this.spaltenname;
  }

  public Formatter getFormatter()
  {
    return this.formatter;
  }

  public boolean isChecked()
  {
    return this.checked;
  }

  public int getAlign()
  {
    return this.align;
  }

  public boolean isNurAdressen()
  {
    return this.nurMitglied;
  }
}
