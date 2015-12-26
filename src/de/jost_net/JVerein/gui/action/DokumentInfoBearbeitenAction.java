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

import de.jost_net.JVerein.Messaging.DokumentMessage;
import de.jost_net.JVerein.gui.dialogs.DokumentDialog;
import de.jost_net.JVerein.rmi.AbstractDokument;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.OperationCanceledException;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Bearbeiten von Dokument-Infos
 */
public class DokumentInfoBearbeitenAction implements Action
{

  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null || !(context instanceof AbstractDokument))
    {
      throw new ApplicationException("Kein Dokument ausgewählt");
    }
    try
    {
      AbstractDokument ad = (AbstractDokument) context;
      if (ad.isNewObject())
      {
        return;
      }
      DokumentDialog dd = new DokumentDialog(ad);
      ad = dd.open();
      ad.store();
      Application.getMessagingFactory().sendMessage(new DokumentMessage(ad));

    }
    catch (OperationCanceledException e)
    {
      // Nichts machen
    }
    catch (Exception e)
    {
      String fehler = "Fehler beim speichern der Dokument-Infos";
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}