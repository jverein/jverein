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
import de.jost_net.JVerein.gui.action.InventarLagerortAction;
import de.jost_net.JVerein.gui.menu.InventarLagerortMenu;
import de.jost_net.JVerein.rmi.InventarLagerort;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class InventarLagerortControl extends AbstractControl
{

  private TablePart lagerorteList;

  private TextInput bezeichnung = null;

  // Elemente für die Auswertung

  private TextInput suchBezeichnung = null;

  private Settings settings = null;

  private InventarLagerort lagerort;

  public InventarLagerortControl(AbstractView view)
  {
    super(view);
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public InventarLagerort getLagerort()
  {
    if (lagerort != null)
    {
      return lagerort;
    }
    lagerort = (InventarLagerort) getCurrentObject();
    return lagerort;
  }

  public TextInput getBezeichnung() throws RemoteException
  {
    if (bezeichnung != null)
    {
      return bezeichnung;
    }

    this.bezeichnung = new TextInput(getLagerort().getBezeichnung(), 200);
    this.bezeichnung.setName("Bezeichnung");
    return bezeichnung;
  }

  public TextInput getSuchbezeichnung() throws RemoteException
  {
    if (suchBezeichnung != null)
    {
      return suchBezeichnung;
    }

    this.suchBezeichnung = new TextInput(
        settings.getString("suchbezeichnung", "*"), 200);
    this.suchBezeichnung.setName("Bezeichnung");
    return suchBezeichnung;
  }

  public void handleStore()
  {
    try
    {
      InventarLagerort l = getLagerort();
      l.setBezeichnung((String) getBezeichnung().getValue());
      l.store();
      GUI.getStatusBar().setSuccessText("Lagerort gespeichert");
    }
    catch (ApplicationException e)
    {
      GUI.getStatusBar().setErrorText(e.getMessage());
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei speichern des Lagerorts";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  private void refresh()
  {
    try
    {
      saveDefaults();
      if (lagerorteList == null)
      {
        return;
      }
      lagerorteList.removeAll();
      DBIterator<InventarLagerort> lagerorte = getIterator();
      while (lagerorte.hasNext())
      {
        InventarLagerort lo = lagerorte.next();
        lagerorteList.addItem(lo);
      }
    }
    catch (RemoteException e1)
    {
      Logger.error("Fehler", e1);
    }
  }

  private DBIterator<InventarLagerort> getIterator() throws RemoteException
  {
    DBIterator<InventarLagerort> lagerorte = Einstellungen.getDBService()
        .createList(InventarLagerort.class);
    if (getSuchbezeichnung().getValue() != null)
    {
      lagerorte.addFilter("bezeichnung like ?",
          "%" + getSuchbezeichnung().getValue() + "%");
    }
    return lagerorte;
  }

  /**
   * Default-Werte speichern.
   * 
   * @throws RemoteException
   */
  public void saveDefaults() throws RemoteException
  {
    if (this.suchBezeichnung != null)
    {
      InventarLagerort lo = (InventarLagerort) getSuchbezeichnung().getValue();
      if (lo != null)
      {
        settings.setAttribute("suchbezeichnung", lo.getID());
      }
      else
      {
        settings.setAttribute("suchbezeichnung", "");
      }
    }
  }

  public Part getLagerorteList() throws RemoteException
  {
    DBIterator<InventarLagerort> lagerorte = getIterator();
    if (lagerorteList == null)
    {
      lagerorteList = new TablePart(lagerorte, new InventarLagerortAction());
      lagerorteList.addColumn("Bezeichnung", "bezeichnung");
      lagerorteList.setContextMenu(new InventarLagerortMenu());
      lagerorteList.setRememberColWidths(true);
      lagerorteList.setRememberOrder(true);
      lagerorteList.setSummary(true);
    }
    else
    {
      lagerorteList.removeAll();
      while (lagerorte.hasNext())
      {
        lagerorteList.addItem(lagerorte.next());
      }
    }
    return lagerorteList;
  }

}
