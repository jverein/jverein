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
package de.jost_net.JVerein.gui.dialogs;

import de.jost_net.JVerein.io.IBankverbindung;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.logging.Logger;

public class BankverbindungDialogButton extends ButtonArea
{

  public BankverbindungDialogButton(final IBankverbindung bankverbindung,
      final TextInput blz, final TextInput konto, final TextInput bic,
      final TextInput iban)
  {
    super();
    final Button b = new Button("", new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        try
        {
          BankverbindungDialog bvd = new BankverbindungDialog(
              BankverbindungDialog.POSITION_CENTER, bankverbindung);
          IBankverbindung ibv2 = bvd.open();
          bic.setValue(ibv2.getBic());
          bic.getControl().setFocus();
          blz.setValue(ibv2.getBlz());
          iban.setValue(ibv2.getIban());
          iban.getControl().setFocus();
          konto.setValue(ibv2.getKonto());
        }
        catch (Exception e)
        {
          Logger.error("Fehler", e);
        }
      }
    }, null, false, "wand.png");
    addButton(b);
  }

}
