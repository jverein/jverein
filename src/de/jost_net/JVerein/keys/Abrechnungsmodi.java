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
 * Revision 1.3  2011-02-12 09:41:11  jost
 * Statische Codeanalyse mit Findbugs
 *
 * Revision 1.2  2010/05/18 20:23:26  jost
 * Überflüssige Imports entfernt.
 *
 * Revision 1.1  2010/04/25 13:56:11  jost
 * Vorarbeiten Mitgliedskonto
 *
 **********************************************************************/
package de.jost_net.JVerein.keys;

import de.jost_net.JVerein.JVereinPlugin;

/**
 * Abrechnungsmodi
 */
public class Abrechnungsmodi
{
  public static final int KEINBEITRAG = 0;

  public static final int ALLE = 1;

  public static final int JA = 5;

  public static final int HA = 6;

  public static final int VI = 7;

  public static final int JAHAVIMO = 8;

  public static final int JAVIMO = 9;

  public static final int HAVIMO = 10;

  public static final int VIMO = 11;

  public static final int MO = 12;

  public static final int EINGETRETENEMITGLIEDER = 99;

  private int abrechnungsmodus;

  public Abrechnungsmodi(int abrechnungsmodus)
  {
    this.abrechnungsmodus = abrechnungsmodus;
  }

  public int getKey()
  {
    return abrechnungsmodus;
  }

  public String getText()
  {
    return get(abrechnungsmodus);
  }

  public static String get(int key)
  {
    switch (key)
    {
      case KEINBEITRAG:
        return JVereinPlugin.getI18n().tr("keine Beitragsabrechnung");
      case ALLE:
        return JVereinPlugin.getI18n().tr("Alle");
      case MO:
        return JVereinPlugin.getI18n().tr("Monatsbeiträge");
      case VI:
        return JVereinPlugin.getI18n().tr("Vierteljährlich");
      case HA:
        return JVereinPlugin.getI18n().tr("Halbjährlich");
      case JA:
        return JVereinPlugin.getI18n().tr("Jährlich");
      case JAHAVIMO:
        return JVereinPlugin.getI18n().tr(
            "Jahres-, Halbjahres-, Vierteljahres- und Monatsbeiträge");
      case JAVIMO:
        return JVereinPlugin.getI18n().tr(
            "Jahres-, Vierteljahres- und Monatsbeiträge");
      case HAVIMO:
        return JVereinPlugin.getI18n().tr(
            "Halbjahres-, Vierteljahres- und Monatsbeiträge");
      case VIMO:
        return JVereinPlugin.getI18n().tr("Vierteljahres- und Monatsbeiträge");
      case EINGETRETENEMITGLIEDER:
        return JVereinPlugin.getI18n().tr("eingetretene Mitglieder");
      default:
        return null;
    }
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof Abrechnungsmodi)
    {
      Abrechnungsmodi v = (Abrechnungsmodi) obj;
      return (getKey() == v.getKey());
    }
    return false;
  }

  @Override
  public int hashCode()
  {
    return abrechnungsmodus;
  }

  @Override
  public String toString()
  {
    return get(abrechnungsmodus);
  }
}
