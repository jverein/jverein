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
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.AdresstypAction;
import de.jost_net.JVerein.gui.menu.AdresstypMenu;
import de.jost_net.JVerein.rmi.Adresstyp;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class AdresstypControl extends AbstractControl
{
  private de.willuhn.jameica.system.Settings settings;

  private TablePart adresstypList;

  private Input bezeichnung;

  private Input bezeichnungplural;

  private Adresstyp adresstyp;

  public AdresstypControl(AbstractView view)
  {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public Adresstyp getAdresstyp()
  {
    if (adresstyp != null)
    {
      return adresstyp;
    }
    adresstyp = (Adresstyp) getCurrentObject();
    return adresstyp;
  }

  public Input getBezeichnung() throws RemoteException
  {
    if (bezeichnung != null)
    {
      return bezeichnung;
    }
    bezeichnung = new TextInput(getAdresstyp().getBezeichnung(), 30);
    return bezeichnung;
  }

  public Input getBezeichnungPlural() throws RemoteException
  {
    if (bezeichnungplural != null)
    {
      return bezeichnungplural;
    }
    bezeichnungplural = new TextInput(getAdresstyp().getBezeichnungPlural(), 30);
    return bezeichnungplural;
  }

  /**
   * This method stores the project using the current values.
   */
  public void handleStore()
  {
    try
    {
      Adresstyp at = getAdresstyp();
      at.setBezeichnung((String) getBezeichnung().getValue());
      at.setBezeichnungPlural((String) getBezeichnungPlural().getValue());
      try
      {
        at.store();
        GUI.getStatusBar().setSuccessText("Adresstyp gespeichert");
      }
      catch (ApplicationException e)
      {
        GUI.getStatusBar().setErrorText(e.getMessage());
      }
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei speichern des Adresstypen";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  public Part getAdresstypList() throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator adresstypen = service.createList(Adresstyp.class);
    adresstypen.setOrder("ORDER BY bezeichnung");

    adresstypList = new TablePart(adresstypen, new AdresstypAction());
    adresstypList.addColumn("Bezeichnung", "bezeichnung");
    adresstypList.addColumn("Bezeichnung Plural", "bezeichnungplural");
    adresstypList.addColumn("ID", "id");
    adresstypList.setContextMenu(new AdresstypMenu());
    adresstypList.setRememberColWidths(true);
    adresstypList.setRememberOrder(true);
    adresstypList.setSummary(true);
    return adresstypList;
  }
}
