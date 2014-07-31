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
package de.jost_net.JVerein.gui.action;

import de.jost_net.JVerein.rmi.MitgliedNextBGruppe;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * @author Rolf Mamat
 */
public class MitgliedNextBGruppeLoeschenAction implements Action
{

  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null || !(context instanceof MitgliedNextBGruppe))
      throw new ApplicationException("Keine Beitragsgruppe ausgewählt");

    try
    {
      MitgliedNextBGruppe k = (MitgliedNextBGruppe) context;
      if (k.isNewObject())
      {
        return;
      }

      YesNoDialog d = new YesNoDialog(YesNoDialog.POSITION_CENTER);
      d.setTitle("Beitragsgruppe löschen");
      d.setText("Zukünfigte Beitragsgruppe für Mitglied löschen?");
      Boolean choice = (Boolean) d.open();
      if (!choice.booleanValue())
        return;

      k.delete();
      GUI.getStatusBar().setSuccessText("Zukünftige Beitragsgruppe gelöscht.");
    }
    catch (Exception e)
    {
      Logger.error("Kann nicht entfert werden!", e);
      throw new ApplicationException(
          "Beitragsgruppe kann nicht von Mitglied entfernt werden!", e);
    }
  }

}
