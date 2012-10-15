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
import java.util.ArrayList;

import de.jost_net.JVerein.JVereinPlugin;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.jameica.gui.input.SelectInput;

/**
 * Combo-Box, fuer die Auswahl des Dtaus-Textschluessels.
 */
public class DtausTextschluesselInput extends SelectInput
{

  public static final String ABBUCHUNG = "04";

  public static final String LASTSCHRIFT = "05";

  public DtausTextschluesselInput(String dtaustextschluessel)
      throws RemoteException
  {
    super(init(), new DtausTextschluesselObject(dtaustextschluessel));
    setName(JVereinPlugin.getI18n().tr("DTAUS-Textschlüssel"));
  }

  /**
   * @return initialisiert die Liste der Optionen.
   * @throws RemoteException
   */
  private static GenericIterator init() throws RemoteException
  {
    ArrayList<DtausTextschluesselObject> l = new ArrayList<DtausTextschluesselObject>();
    l.add(new DtausTextschluesselObject(LASTSCHRIFT));
    l.add(new DtausTextschluesselObject(ABBUCHUNG));
    return PseudoIterator.fromArray(l.toArray(new DtausTextschluesselObject[l
        .size()]));
  }

  /**
   * @see de.willuhn.jameica.gui.input.Input#getValue()
   */
  @Override
  public Object getValue()
  {
    DtausTextschluesselObject o = (DtausTextschluesselObject) super.getValue();
    if (o == null)
    {
      return LASTSCHRIFT;
    }
    return o.textschluessel;
  }

  @Override
  public void setValue(Object obj)
  {
    if (obj instanceof String)
    {
      super.setValue(new DtausTextschluesselObject((String) obj));
    }
  }

  /**
   * Hilfs-Objekt zur Anzeige der Labels.
   */
  private static class DtausTextschluesselObject implements GenericObject
  {

    public String textschluessel;

    private String label = null;

    private DtausTextschluesselObject(String textschluessel)
    {
      this.textschluessel = textschluessel;
      if (textschluessel == null)
      {
        label = "";
        return;
      }

      if (textschluessel.equals(ABBUCHUNG))
      {
        this.label = JVereinPlugin.getI18n().tr("Abbuchung");
      }
      else if (textschluessel.equals(LASTSCHRIFT))
      {
        this.label = JVereinPlugin.getI18n().tr("Lastschrift");
      }
      else
      {
        this.label = JVereinPlugin.getI18n().tr("Programmfehler");
      }
    }

    @Override
    public Object getAttribute(String arg0)
    {
      if (arg0.equals("label"))
      {
        return label;
      }
      else if (arg0.equals("textschluessel"))
      {
        return textschluessel;
      }
      return null;
    }

    @Override
    public String[] getAttributeNames()
    {
      return new String[] { "label", "textschluessel" };
    }

    @Override
    public String getID()
    {
      return textschluessel;
    }

    @Override
    public String getPrimaryAttribute()
    {
      return "label";
    }

    @Override
    public boolean equals(GenericObject arg0) throws RemoteException
    {
      if (arg0 == null || !(arg0 instanceof DtausTextschluesselObject))
      {
        return false;
      }
      return this.getID().equals(arg0.getID());
    }
  }
}
