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

import de.jost_net.JVerein.gui.control.listener.IBANListener;
import de.willuhn.jameica.gui.input.TextInput;

public class IBANInput extends TextInput
{

  public IBANInput(String value, TextInput bic)
  {
    super(value, 34);
    setName("IBAN");
    IBANListener l = new IBANListener(this, bic);
    addListener(l);
    l.handleEvent(null);
  }
}
