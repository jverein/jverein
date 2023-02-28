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

import de.jost_net.OBanToo.SEPA.Land.SEPALaender;
import de.jost_net.OBanToo.SEPA.Land.SEPALand;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.jameica.gui.input.SelectInput;

/**
 * Combo-Box, fuer die Auswahl des SEPA-Landes.
 */
public class SEPALandInput extends SelectInput
{

  public SEPALandInput() throws RemoteException
  {
    this(null);
  }

  public SEPALandInput(SEPALand land) throws RemoteException
  {
    super(init(), new SEPALandObject(land));
    setName("SEPA-Land");
  }

  /**
   * @return initialisiert die Liste der Optionen.
   * @throws RemoteException
   */
  private static GenericIterator<?> init() throws RemoteException
  {
    ArrayList<SEPALand> l = SEPALaender.getLaender();
    ArrayList<SEPALandObject> lo = new ArrayList<>();
    for (SEPALand land : l)
    {
      lo.add(new SEPALandObject(land));
    }
    return PseudoIterator.fromArray(lo.toArray(new SEPALandObject[lo.size()]));
  }

}
