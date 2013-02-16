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

package de.jost_net.JVerein.gui.input;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.SEPAParam;
import de.jost_net.JVerein.util.IBANException;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.input.SelectInput;

/**
 * Combo-Box, fuer die Auswahl des SEPA-Landes.
 */
public class SEPALandInput extends SelectInput
{
  public SEPALandInput() throws RemoteException, IBANException
  {
    super(init(), null);
    setName(JVereinPlugin.getI18n().tr("SEPA-Land"));
  }

  /**
   * @return initialisiert die Liste der Optionen.
   * @throws RemoteException
   */
  private static GenericIterator init() throws RemoteException
  {
    DBIterator it = Einstellungen.getDBService().createList(SEPAParam.class);
    return it;
  }

  // /**
  // * @see de.willuhn.jameica.gui.input.Input#getValue()
  // */
  // @Override
  // public Object getValue()
  // {
  // SEPALandObject o = (SEPALandObject) super.getValue();
  // if (o == null)
  // {
  // return DEUTSCHLAND;
  // }
  // return o.sepaland;
  // }

  // @Override
  // public void setValue(Object obj)
  // {
  // if (obj instanceof String)
  // {
  // super.setValue(new SEPALandObject((String) obj));
  // }
  // }

  /**
   * Hilfs-Objekt zur Anzeige der Labels.
   */
  // private static class SEPALandObject implements GenericObject
  // {
  //
  // public String sepaland;
  //
  // private String label = null;
  //
  // private SEPALandObject(String sepaland)
  // {
  // this.sepaland = sepaland;
  // if (sepaland == null)
  // {
  // label = "";
  // return;
  // }
  //
  // if (sepaland.equals(DEUTSCHLAND))
  // {
  // this.label = JVereinPlugin.getI18n().tr("Deutschland");
  // }
  // else if (sepaland.equals(OESTERREICH))
  // {
  // this.label = JVereinPlugin.getI18n().tr("Österreich");
  // }
  // else
  // {
  // this.label = JVereinPlugin.getI18n().tr("Programmfehler");
  // }
  // }
  //
  // @Override
  // public Object getAttribute(String arg0)
  // {
  // if (arg0.equals("label"))
  // {
  // return label;
  // }
  // else if (arg0.equals("sepaland"))
  // {
  // return sepaland;
  // }
  // return null;
  // }
  //
  // @Override
  // public String[] getAttributeNames()
  // {
  // return new String[] { "label", "sepaland" };
  // }
  //
  // @Override
  // public String getID()
  // {
  // return sepaland;
  // }
  //
  // @Override
  // public String getPrimaryAttribute()
  // {
  // return "label";
  // }
  //
  // @Override
  // public boolean equals(GenericObject arg0) throws RemoteException
  // {
  // if (arg0 == null || !(arg0 instanceof SEPALandObject))
  // {
  // return false;
  // }
  // return this.getID().equals(arg0.getID());
  // }
  // }
}
