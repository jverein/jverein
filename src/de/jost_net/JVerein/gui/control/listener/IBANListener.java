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
package de.jost_net.JVerein.gui.control.listener;

import java.rmi.RemoteException;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.rmi.Bank;
import de.jost_net.JVerein.rmi.SEPAParam;
import de.jost_net.JVerein.util.SEPAException;
import de.jost_net.JVerein.util.SEPA;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.logging.Logger;

/**
 * Sucht das Geldinstitut zur eingegebenen IBAN und zeigt es als Kommentar
 * hinter dem Feld an.
 */

public class IBANListener implements Listener
{
  private TextInput iban;

  public IBANListener(TextInput iban)
  {
    this.iban = iban;
  }

  @Override
  public void handleEvent(Event event)
  {
    try
    {
      String ib = (String) iban.getValue();
      if (ib == null)
      {
        return;
      }
      if (ib.length() == 0)
      {
        iban.setComment("");
      }
      if (ib.length() > 4)
      {
        String laendercode = ib.substring(0, 2);
        try
        {
          SEPAParam country = SEPA.getSEPAParam(laendercode);
          String bankcode = ib.substring(4,
              country.getBankIdentifierLength() + 4);
          Bank b = SEPA.getBankByBLZ(bankcode);
          if (b != null)
          {
            iban.setComment(b.getBezeichnung());
          }
          return;
        }
        catch (SEPAException e)
        {
          iban.setComment(e.getMessage());
          return;
        }
      }
      Bank b = SEPA.getBankByBIC((String) iban.getValue());
      iban.setComment(b.getBezeichnung());
    }
    catch (RemoteException e)
    {
      Logger.error("Fehler beim setzen des Banknamen-Kommentars", e);
    }
  }
}
