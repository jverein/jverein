/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * heiner@jverein.de
 * www.jverein.de
 * All rights reserved
 * $Log$
 * Revision 1.1  2011-05-06 15:03:54  jost
 * Neue Variablenmimik
 *
 **********************************************************************/
package de.jost_net.JVerein.Variable;

public enum AllgemeineVar
{
  AKTUELLERMONAT("aktuellermonat"), //
  FOLGEJAHR("folgejahr"), //
  FOLGEMONAT("folgemonat"), //
  TAGESDATUM("tagesdatum"), //
  VORMONAT("vormonat"); //

  private String name;

  AllgemeineVar(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }
}
