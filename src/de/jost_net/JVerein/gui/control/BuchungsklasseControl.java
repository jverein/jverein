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
import de.jost_net.JVerein.gui.action.BuchungsklasseAction;
import de.jost_net.JVerein.gui.menu.BuchungsklasseMenu;
import de.jost_net.JVerein.rmi.Buchungsklasse;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.IntegerInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class BuchungsklasseControl extends AbstractControl
{
  private de.willuhn.jameica.system.Settings settings;

  private TablePart buchungsklassenList;

  private IntegerInput nummer;

  private Input bezeichnung;

  private Buchungsklasse buchungsklasse;

  public BuchungsklasseControl(AbstractView view)
  {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  private Buchungsklasse getBuchungsklasse()
  {
    if (buchungsklasse != null)
    {
      return buchungsklasse;
    }
    buchungsklasse = (Buchungsklasse) getCurrentObject();
    return buchungsklasse;
  }

  public IntegerInput getNummer(boolean withFocus) throws RemoteException
  {
    if (nummer != null)
    {
      return nummer;
    }
    nummer = new IntegerInput(getBuchungsklasse().getNummer());
    if (withFocus)
    {
      nummer.focus();
    }
    return nummer;
  }

  public Input getBezeichnung() throws RemoteException
  {
    if (bezeichnung != null)
    {
      return bezeichnung;
    }
    bezeichnung = new TextInput(getBuchungsklasse().getBezeichnung(), 30);
    return bezeichnung;
  }

  /**
   * This method stores the project using the current values.
   */
  public void handleStore()
  {
    try
    {
      Buchungsklasse b = getBuchungsklasse();
      b.setNummer(((Integer) getNummer(false).getValue()).intValue());
      b.setBezeichnung((String) getBezeichnung().getValue());
      try
      {
        b.store();
        GUI.getStatusBar().setSuccessText("Buchungsklasse gespeichert");
      }
      catch (ApplicationException e)
      {
        GUI.getStatusBar().setErrorText(e.getMessage());
      }
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei speichern der Buchungsklasse";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  public Part getBuchungsklasseList() throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator buchungsklassen = service.createList(Buchungsklasse.class);
    buchungsklassen.addFilter("nummer >= 0");
    buchungsklassen.setOrder("ORDER BY nummer");

    buchungsklassenList = new TablePart(buchungsklassen,
        new BuchungsklasseAction());
    buchungsklassenList.addColumn("Nummer", "nummer");
    buchungsklassenList.addColumn("Bezeichnung", "bezeichnung");
    buchungsklassenList.setContextMenu(new BuchungsklasseMenu());
    buchungsklassenList.setRememberColWidths(true);
    buchungsklassenList.setRememberOrder(true);
    buchungsklassenList.setSummary(true);
    return buchungsklassenList;
  }
}
