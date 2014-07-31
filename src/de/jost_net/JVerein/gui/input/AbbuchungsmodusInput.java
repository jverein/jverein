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

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.keys.Abrechnungsmodi;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.jameica.gui.input.SelectInput;

/**
 * Combo-Box, fuer die Auswahl des Abbuchungsmodus.
 */
public class AbbuchungsmodusInput extends SelectInput
{

  public AbbuchungsmodusInput(int abbuchungsmodus) throws RemoteException
  {
    super(init(), new AbbuchungsmodusObject(abbuchungsmodus));
  }

  /**
   * @return initialisiert die Liste der Optionen.
   * @throws RemoteException
   */
  private static GenericIterator init() throws RemoteException
  {
    ArrayList<AbbuchungsmodusObject> l = new ArrayList<AbbuchungsmodusObject>();
    l.add(new AbbuchungsmodusObject(Abrechnungsmodi.KEINBEITRAG));
    switch (Einstellungen.getEinstellung().getBeitragsmodel())
    {
      case GLEICHERTERMINFUERALLE:
      case FLEXIBEL:
        l.add(new AbbuchungsmodusObject(Abrechnungsmodi.ALLE));
        l.add(new AbbuchungsmodusObject(Abrechnungsmodi.EINGETRETENEMITGLIEDER));
        break;
      case MONATLICH12631:
        l.add(new AbbuchungsmodusObject(Abrechnungsmodi.JAHAVIMO));
        l.add(new AbbuchungsmodusObject(Abrechnungsmodi.JAVIMO));
        l.add(new AbbuchungsmodusObject(Abrechnungsmodi.HAVIMO));
        l.add(new AbbuchungsmodusObject(Abrechnungsmodi.VIMO));
        l.add(new AbbuchungsmodusObject(Abrechnungsmodi.JA));
        l.add(new AbbuchungsmodusObject(Abrechnungsmodi.HA));
        l.add(new AbbuchungsmodusObject(Abrechnungsmodi.VI));
        l.add(new AbbuchungsmodusObject(Abrechnungsmodi.MO));
        l.add(new AbbuchungsmodusObject(Abrechnungsmodi.EINGETRETENEMITGLIEDER));
        break;
    }
    return PseudoIterator.fromArray(l.toArray(new AbbuchungsmodusObject[l
        .size()]));
  }

  /**
   * @see de.willuhn.jameica.gui.input.Input#getValue()
   */
  @Override
  public Object getValue()
  {
    AbbuchungsmodusObject o = (AbbuchungsmodusObject) super.getValue();
    if (o == null)
    {
      return Integer.valueOf(Abrechnungsmodi.ALLE);
    }
    return Integer.valueOf(o.abbuchungsmodus);
  }

  /**
   * Hilfs-Objekt zur Anzeige der Labels.
   */
  private static class AbbuchungsmodusObject implements GenericObject
  {

    public int abbuchungsmodus;

    private String label = null;

    private AbbuchungsmodusObject(int abbuchungsmodus)
    {
      this.abbuchungsmodus = abbuchungsmodus;
      this.label = Abrechnungsmodi.get(abbuchungsmodus);
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
      return "" + abbuchungsmodus;
    }

    @Override
    public String getPrimaryAttribute()
    {
      return "name";
    }

    @Override
    public boolean equals(GenericObject arg0) throws RemoteException
    {
      if (arg0 == null || !(arg0 instanceof AbbuchungsmodusObject))
      {
        return false;
      }
      return this.getID().equals(arg0.getID());
    }
  }
}
