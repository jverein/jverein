/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
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
 **********************************************************************/
package de.jost_net.JVerein.Variable;

import de.jost_net.JVerein.JVereinPlugin;

public enum AllgemeineVar
{
  AKTUELLERMONAT(JVereinPlugin.getI18n().tr("aktuellermonat")), //
  AKTUELLESJAHR(JVereinPlugin.getI18n().tr("aktuellesjahr")), //
  FOLGEJAHR(JVereinPlugin.getI18n().tr("folgejahr")), //
  FOLGEMONAT(JVereinPlugin.getI18n().tr("folgemonat")), //
  TAGESDATUM(JVereinPlugin.getI18n().tr("tagesdatum")), //
  VORMONAT(JVereinPlugin.getI18n().tr("vormonat")); //

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
