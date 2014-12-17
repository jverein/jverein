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

package de.jost_net.JVerein.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.jost_net.JVerein.gui.view.IAuswertung;
import de.jost_net.JVerein.io.Adressbuch.Txt;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class MitgliedAdressbuchExport implements IAuswertung
{

  public MitgliedAdressbuchExport()
  {
  }

  @Override
  public void beforeGo()
  {
    // Nothing to do
  }

  @Override
  public void go(ArrayList<Mitglied> list, File file)
      throws ApplicationException
  {
    try
    {
      Txt txt = new Txt(file, ";");
      for (Mitglied m : list)
      {
        txt.add(m);
      }
      txt.close();
      GUI.getStatusBar().setSuccessText(
          String.format("Auswertung fertig. %d Sätze.", list.size()));
    }
    catch (IOException e)
    {
      throw new ApplicationException(e);
    }
  }

  @Override
  public String getDateiname()
  {
    return "adressbuchexport";
  }

  @Override
  public String getDateiendung()
  {
    return "CSV";
  }

  @Override
  public boolean openFile()
  {
    return true;
  }

  @Override
  public String toString()
  {
    return "Adressbuchexport CSV";
  }
}
