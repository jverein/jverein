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

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Konto;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.logging.Logger;

public class JVereinKontoInput extends SelectInput
{
  public JVereinKontoInput() throws RemoteException
  {
    super(init(), null);
    this.setPleaseChoose("Wähle ein JVerein Konto");
  }

  private static GenericIterator init() throws RemoteException
  {
    DBIterator it = Einstellungen.getDBService().createList(Konto.class);
    it.setOrder("Order by bezeichnung");
    return it;
  }

  @Override
  protected String format(Object bean)
  {
    if (null == bean)
      return null;
    if (bean instanceof Konto == false)
      return bean.toString();

    try
    {
      Konto konto = (Konto) bean;
      return konto.getBezeichnung();
    }
    catch (RemoteException ex)
    {
      Logger.error("Konto kann nicht formatiert werden.", ex);
      return null;
    }
  }

}
