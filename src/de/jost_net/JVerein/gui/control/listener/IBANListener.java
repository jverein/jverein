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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.gui.input.IBANInput;
import de.jost_net.OBanToo.SEPA.IBAN;
import de.jost_net.OBanToo.SEPA.SEPAException;
import de.jost_net.OBanToo.SEPA.BankenDaten.Bank;
import de.jost_net.OBanToo.SEPA.BankenDaten.Banken;
import de.willuhn.jameica.gui.input.TextInput;

/**
 * Sucht das Geldinstitut zur eingegebenen IBAN und zeigt es als Kommentar
 * hinter dem Feld an. -Prüft die IBAN -Ermittelt die BIC
 */

public class IBANListener implements Listener
{
  private TextInput iban;

  private TextInput bic;

  public IBANListener(IBANInput iban, TextInput bic)
  {
    this.iban = iban;
    this.bic = bic;
  }

  @Override
  public void handleEvent(Event event)
  {
    if (event == null)
    {
      return;
    }
    if (event.type != SWT.FocusOut)
    {
      return;
    }
    String ib = (String) iban.getValue();
    if (ib == null)
    {
      return;
    }
    ib = ib.trim();
    String ib2 = "";
    for (int i = 0; i < ib.length(); i++)
    {
      String t = ib.substring(i, i + 1);
      if (!t.equals(" "))
      {
        ib2 += t;
      }
    }
    iban.setValue(ib2);
    if (ib2.length() == 0)
    {
      iban.setComment("");
      bic.setValue("");
      bic.setComment("");
    }
    if (ib2.length() > 4)
    {
      try
      {
        IBAN i = new IBAN(ib2);
        Bank b = Banken.getBankByBLZ(i.getBLZ());
        if (b != null)
        {
          iban.setComment(b.getBezeichnung());
          bic.setValue(b.getBIC());
          bic.setComment(b.getBezeichnung());
        }
        return;
      }
      catch (SEPAException e)
      {
        iban.setComment(e.getMessage());
        return;
      }
    }
    Bank b = Banken.getBankByBIC((String) iban.getValue());
    iban.setComment(b != null ? b.getBezeichnung() : "");
  }
}
