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

/**
 * Combo-Box, fuer die Auswahl des Abbuchungsmodus.
 */
public class GeschlechtInput extends SelectInput
{

  public static final String MAENNLICH = "m";

  public static final String WEIBLICH = "w";

  public static final String OHNEANGABE = "o";

  public GeschlechtInput(String geschlecht) throws RemoteException
  {
    super(init(), new GeschlechtObject(geschlecht));
  }

  /**
   * @return initialisiert die Liste der Optionen.
   * @throws RemoteException
   */
  private static GenericIterator<?> init() throws RemoteException
  {
    ArrayList<GeschlechtObject> l = new ArrayList<>();
    l.add(new GeschlechtObject(MAENNLICH));
    l.add(new GeschlechtObject(WEIBLICH));
    l.add(new GeschlechtObject(OHNEANGABE));
    return PseudoIterator.fromArray(l.toArray(new GeschlechtObject[l.size()]));
  }

  /**
   * @see de.willuhn.jameica.gui.input.Input#getValue()
   */
  @Override
  public Object getValue()
  {
    GeschlechtObject o = (GeschlechtObject) super.getValue();
    if (o == null)
    {
      return MAENNLICH;
    }
    return o.geschlecht;
  }

  /**
   * Liefert das gewählte Objekt ohne Default-Wert zurück
   */
  public GeschlechtObject getSelectedValue()
  {
    GeschlechtObject o = (GeschlechtObject) super.getValue();
    return o;
  }

  @Override
  public void setValue(Object obj)
  {
    if (obj == null)
    {
      super.setValue(null);
      return;
    }
    if (obj instanceof String)
    {
      super.setValue(new GeschlechtObject((String) obj));
    }
  }

  /**
   * Hilfs-Objekt zur Anzeige der Labels.
   */
  private static class GeschlechtObject implements GenericObject
  {

    public String geschlecht;

    private String label = null;

    private GeschlechtObject(String geschlecht)
    {
      this.geschlecht = geschlecht;
      if (geschlecht == null)
      {
        label = "";
        return;
      }

      if (geschlecht.equals(MAENNLICH))
      {
        this.label = "männlich";
      }
      else if (geschlecht.equals(WEIBLICH))
      {
        this.label = "weiblich";
      }
      else if (geschlecht.equals(OHNEANGABE))
      {
        this.label = "ohne Angabe";
      }
      else
      {
        this.label = "Programmfehler";
      }
    }

    @Override
    public Object getAttribute(String arg0)
    {
      if (arg0.equals("label"))
      {
        return label;
      }
      else if (arg0.equals("geschlecht"))
      {
        return geschlecht;
      }
      return null;
    }

    @Override
    public String[] getAttributeNames()
    {
      return new String[] { "label", "geschlecht" };
    }

    @Override
    public String getID()
    {
      return geschlecht;
    }

    @Override
    public String getPrimaryAttribute()
    {
      return "label";
    }

    @Override
    public boolean equals(GenericObject arg0) throws RemoteException
    {
      if (arg0 == null || !(arg0 instanceof GeschlechtObject))
      {
        return false;
      }
      return this.getID().equals(arg0.getID());
    }
  }
}
