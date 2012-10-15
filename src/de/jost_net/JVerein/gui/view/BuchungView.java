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

import de.jost_net.JVerein.JVereinPlugin;
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
import de.willuhn.util.ApplicationException;

public class BuchungView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Buchung"));

    final BuchungsControl control = new BuchungsControl(this);

    boolean buchungabgeschlossen = false;
    try
    {
      if (!control.getBuchung().isNewObject())
      {
        Jahresabschluss ja = control.getBuchung().getJahresabschluss();
        if (ja != null)
        {
          GUI.getStatusBar().setErrorText(
              JVereinPlugin.getI18n().tr(
                  "Buchung wurde bereits am {0} von {1} abgeschlossen.",
                  new String[] {
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
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.BUCHUNGEN, false,
        "help-browser.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("neu"),
        new BuchungNeuAction(), null, false, "document-new.png");
    Button savButton = new Button(JVereinPlugin.getI18n().tr("speichern"),
        new Action()
        {
          @Override
          public void handleAction(Object context)
          {
            control.handleStore();
          }
        }, null, true, "document-save.png");
    savButton.setEnabled(!buchungabgeschlossen);
    buttons.addButton(savButton);
    buttons.paint(getParent());
  }

  @Override
  public String getHelp()
  {
    return JVereinPlugin.getI18n().tr(
        "<form><p><span color=\"header\" font=\"header\">Buchung</span></p>"
            + "<p>Zuordnung einer Buchungsart zu einer Buchung.</p></form>");
  }
}
