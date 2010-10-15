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
 * Revision 1.1  2008-11-29 13:18:17  jost
 * Neu: Konfiguration der Spalten einer Tabelle
 *
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

  public Spalte(String spaltenbezeichnung, String spaltenname, boolean checked)
  {
    this(spaltenbezeichnung, spaltenname, checked, null, Column.ALIGN_AUTO);
  }

  public Spalte(String spaltenbezeichnung, String spaltenname, boolean checked,
      Formatter formatter, int align)
  {
    this.spaltenbezeichnung = spaltenbezeichnung;
    this.spaltenname = spaltenname;
    this.formatter = formatter;
    this.checked = checked;
    this.align = align;
  }

  @Override
  public boolean equals(Object arg0)
  {
    if (arg0 == null || !(arg0 instanceof Spalte))
      return false;

    Spalte o = (Spalte) arg0;
    return this.spaltenname.equals(o.spaltenname);
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
}
