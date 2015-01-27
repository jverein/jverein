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
package de.jost_net.JVerein.gui.control.listener;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.OBanToo.SEPA.BankenDaten.Bank;
import de.jost_net.OBanToo.SEPA.BankenDaten.Banken;
import de.willuhn.jameica.gui.input.TextInput;

/**
 * Sucht das Geldinstitut zum eingegebenen BIC und zeigt es als Kommentar hinter
 * dem Feld an.
 */

public class BICListener implements Listener
{
  private TextInput bic;

  public BICListener(TextInput bic)
  {
    this.bic = bic;
  }

  @Override
  public void handleEvent(Event event)
  {
    if (bic.getValue() != null)
    {
      String bi = ((String) bic.getValue()).toUpperCase();
      bic.setValue( bi );
      Bank b = Banken.getBankByBIC( bi );
      if (b != null)
      {
        bic.setComment(b.getBezeichnung());
      }
    }
  }
}
