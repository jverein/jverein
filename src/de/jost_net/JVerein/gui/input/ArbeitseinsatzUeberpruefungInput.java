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

package de.jost_net.JVerein.gui.input;

import java.rmi.RemoteException;
import java.util.ArrayList;

import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.jameica.gui.input.SelectInput;

public class ArbeitseinsatzUeberpruefungInput extends SelectInput
{

  public static final int MINDERLEISTUNG = 1;

  public static final int PASSENDELEISTUNG = 2;

  public static final int MEHRLEISTUNG = 3;

  public static final int ALLE = 4;

  public ArbeitseinsatzUeberpruefungInput(int schluessel)
      throws RemoteException
  {
    super(init(), new ArbeitseinsatzUeberpruefungObject(schluessel));
  }

  /**
   * @return initialisiert die Liste der Optionen.
   * @throws RemoteException
   */
  private static GenericIterator init() throws RemoteException
  {
    ArrayList<ArbeitseinsatzUeberpruefungObject> l = new ArrayList<ArbeitseinsatzUeberpruefungObject>();
    l.add(new ArbeitseinsatzUeberpruefungObject(MINDERLEISTUNG));
    l.add(new ArbeitseinsatzUeberpruefungObject(PASSENDELEISTUNG));
    l.add(new ArbeitseinsatzUeberpruefungObject(MEHRLEISTUNG));
    l.add(new ArbeitseinsatzUeberpruefungObject(ALLE));
    return PseudoIterator.fromArray(l
        .toArray(new ArbeitseinsatzUeberpruefungObject[l.size()]));
  }

  /**
   * @see de.willuhn.jameica.gui.input.Input#getValue()
   */
  @Override
  public Object getValue()
  {
    ArbeitseinsatzUeberpruefungObject o = (ArbeitseinsatzUeberpruefungObject) super
        .getValue();
    return o.schluessel;
  }

  /**
   * Hilfs-Objekt zur Anzeige der Labels.
   */
  private static class ArbeitseinsatzUeberpruefungObject implements
      GenericObject
  {

    public int schluessel;

    private String label = null;

    private ArbeitseinsatzUeberpruefungObject(int schluessel)
    {
      this.schluessel = schluessel;

      if (schluessel == ALLE)
      {
        this.label = "alle";
      }
      else if (schluessel == MINDERLEISTUNG)
      {
        this.label = "Minderleistung";
      }
      else if (schluessel == PASSENDELEISTUNG)
      {
        this.label = "passende Leistung";
      }
      else if (schluessel == MEHRLEISTUNG)
      {
        this.label = "Mehrleistung";
      }
      else
      {
        this.label = "Programmfehler";
      }
    }

    @Override
    public Object getAttribute(String arg0)
    {
      return label;
    }

    @Override
    public String[] getAttributeNames()
    {
      return new String[] { "name" };
    }

    @Override
    public String getID()
    {
      return schluessel + "";
    }

    @Override
    public String getPrimaryAttribute()
    {
      return "name";
    }

    @Override
    public boolean equals(GenericObject arg0) throws RemoteException
    {
      if (arg0 == null || !(arg0 instanceof ArbeitseinsatzUeberpruefungObject))
      {
        return false;
      }
      return this.getID().equals(arg0.getID());
    }
  }
}
