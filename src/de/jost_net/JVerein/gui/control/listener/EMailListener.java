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

import de.jost_net.JVerein.gui.input.EmailInput;
import de.jost_net.JVerein.util.EmailValidator;
import de.willuhn.jameica.gui.GUI;

/**
 * Mail-Adresse prüfen
 */

public class EMailListener implements Listener
{
  private EmailInput email;

  public EMailListener(EmailInput email)
  {
    this.email = email;
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
    String em = (String) email.getValue();
    if (em == null || em.length() == 0)
    {
      return;
    }
    if (!EmailValidator.isValid(em))
    {
      GUI.getStatusBar().setErrorText("Mailadresse ist ungültig");
    }
  }
}
