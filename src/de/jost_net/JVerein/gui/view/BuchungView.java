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
package de.jost_net.JVerein.gui.view;

import java.rmi.RemoteException;
import java.text.MessageFormat;

import de.jost_net.JVerein.gui.action.BuchungNeuAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.BuchungsControl;
import de.jost_net.JVerein.gui.parts.BuchungPart;
import de.jost_net.JVerein.rmi.Jahresabschluss;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class BuchungView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    final BuchungsControl control = new BuchungsControl(this);
    if (control.getBuchung().getSpeicherung())
    {
      GUI.getView().setTitle("Buchung");
    }
    else
    {
      GUI.getView().setTitle("Splitbuchung");
    }
    boolean buchungabgeschlossen = false;
    try
    {
      if (!control.getBuchung().isNewObject())
      {
        Jahresabschluss ja = control.getBuchung().getJahresabschluss();
        if (ja != null)
        {
          GUI.getStatusBar().setErrorText(
              MessageFormat.format(
                  "Buchung wurde bereits am {0} von {1} abgeschlossen.",
                  new Object[] {
                      new JVDateFormatTTMMJJJJ().format(ja.getDatum()),
                      ja.getName() }));
          buchungabgeschlossen = true;
        }
      }
    }
    catch (RemoteException e)
    {
      throw new ApplicationException(e.getMessage());
    }

    BuchungPart part = new BuchungPart(control, this, buchungabgeschlossen);
    part.paint(this.getParent());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.BUCHUNGEN, false, "help-browser.png");
    if (control.getBuchung().getSpeicherung())
    {
      buttons.addButton("neu", new BuchungNeuAction(), null, false,
          "document-new.png");
    }
    Button savButton = new Button("speichern", new Action()
    {
      @Override
      public void handleAction(Object context)
      {
        control.handleStore();
        try
        {
          control.refreshSplitbuchungen();
        }
        catch (RemoteException e)
        {
          Logger.error("Fehler", e);
        }

      }
    }, null, true, "document-save.png");
    savButton.setEnabled(!buchungabgeschlossen);
    buttons.addButton(savButton);
    buttons.paint(getParent());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Buchung</span></p>"
        + "<p>Zuordnung einer Buchungsart zu einer Buchung.</p></form>";
  }
}
