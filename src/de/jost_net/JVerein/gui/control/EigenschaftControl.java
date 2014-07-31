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
import de.jost_net.JVerein.gui.action.EigenschaftDetailAction;
import de.jost_net.JVerein.gui.formatter.EigenschaftGruppeFormatter;
import de.jost_net.JVerein.gui.menu.EigenschaftMenu;
import de.jost_net.JVerein.rmi.Eigenschaft;
import de.jost_net.JVerein.rmi.EigenschaftGruppe;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class EigenschaftControl extends AbstractControl
{

  private de.willuhn.jameica.system.Settings settings;

  private TablePart eigenschaftList;

  private Input bezeichnung;

  private SelectInput eigenschaftgruppe;

  private Eigenschaft eigenschaft;

  public EigenschaftControl(AbstractView view)
  {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  private Eigenschaft getEigenschaft() throws RemoteException
  {
    if (eigenschaft != null)
    {
      return eigenschaft;
    }
    eigenschaft = (Eigenschaft) getCurrentObject();
    if (eigenschaft == null)
    {
      eigenschaft = (Eigenschaft) Einstellungen.getDBService().createObject(
          Eigenschaft.class, null);
    }
    return eigenschaft;
  }

  public Input getBezeichnung() throws RemoteException
  {
    if (bezeichnung != null)
    {
      return bezeichnung;
    }
    bezeichnung = new TextInput(getEigenschaft().getBezeichnung(), 30);
    return bezeichnung;
  }

  public Input getEigenschaftGruppe() throws RemoteException
  {
    if (eigenschaftgruppe != null)
    {
      return eigenschaftgruppe;
    }
    DBIterator list = Einstellungen.getDBService().createList(
        EigenschaftGruppe.class);
    list.setOrder("ORDER BY bezeichnung");
    eigenschaftgruppe = new SelectInput(list,
        getEigenschaft().getEigenschaftGruppe());
    eigenschaftgruppe.setValue(getEigenschaft().getEigenschaftGruppe());
    eigenschaftgruppe.setAttribute("bezeichnung");
    eigenschaftgruppe.setPleaseChoose("Bitte auswählen");
    return eigenschaftgruppe;
  }

  public void handleStore()
  {
    try
    {
      Eigenschaft ei = getEigenschaft();

      GenericObject o = (GenericObject) getEigenschaftGruppe().getValue();
      try
      {
        if (o != null)
        {
          ei.setEigenschaftGruppe(new Integer(o.getID()));
        }
        else
        {
          ei.setEigenschaftGruppe(null);
        }
        ei.setBezeichnung((String) getBezeichnung().getValue());
        ei.store();
        GUI.getStatusBar().setSuccessText("Eigenschaft gespeichert");
      }
      catch (ApplicationException e)
      {
        GUI.getStatusBar().setErrorText(e.getMessage());
      }
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei speichern der Eigenschaft";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  public Part getEigenschaftList() throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator eigenschaften = service.createList(Eigenschaft.class);
    eigenschaften.setOrder("ORDER BY bezeichnung");

    if (eigenschaftList == null)
    {
      eigenschaftList = new TablePart(eigenschaften,
          new EigenschaftDetailAction(false));
      eigenschaftList.addColumn("Bezeichnung", "bezeichnung");
      eigenschaftList.addColumn("Gruppe", "eigenschaftgruppe",
          new EigenschaftGruppeFormatter());
      eigenschaftList.setContextMenu(new EigenschaftMenu());
      eigenschaftList.setRememberColWidths(true);
      eigenschaftList.setRememberOrder(true);
      eigenschaftList.setRememberState(true);
      eigenschaftList.setSummary(true);
    }
    else
    {
      eigenschaftList.removeAll();
      while (eigenschaften.hasNext())
      {
        eigenschaftList.addItem(eigenschaften.next());
      }
    }
    return eigenschaftList;
  }
}
