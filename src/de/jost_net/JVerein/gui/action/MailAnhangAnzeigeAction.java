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

import java.io.File;
import java.io.FileOutputStream;

import de.jost_net.JVerein.io.FileViewer;
import de.jost_net.JVerein.rmi.MailAnhang;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Löschen eines Mailanhanges
 */
public class MailAnhangAnzeigeAction implements Action
{


  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null || !(context instanceof MailAnhang))
    {
      throw new ApplicationException("Keinen Mail-Anhang ausgewählt");
    }
    try
    {
      MailAnhang ma = (MailAnhang) context;
      File tmp = new File(System.getProperty("java.io.tmpdir"),
          ma.getDateiname());
      tmp.deleteOnExit();
      FileOutputStream fos = new FileOutputStream(tmp);
      fos.write(ma.getAnhang());
      fos.close();
      FileViewer.show(tmp);
    }
    catch (Exception e)
    {
      String fehler = "Fehler beim entfernen eines Mailanhanges";
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
