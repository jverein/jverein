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
import de.jost_net.JVerein.gui.action.EigenschaftGruppeDetailAction;
import de.jost_net.JVerein.gui.formatter.JaNeinFormatter;
import de.jost_net.JVerein.gui.menu.EigenschaftGruppeMenu;
import de.jost_net.JVerein.rmi.EigenschaftGruppe;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class EigenschaftGruppeControl extends AbstractControl
{

  private de.willuhn.jameica.system.Settings settings;

  private TablePart eigenschaftgruppeList;

  private Input bezeichnung;

  private CheckboxInput pflicht;

  private CheckboxInput max1;

  private EigenschaftGruppe eigenschaftgruppe;

  public EigenschaftGruppeControl(AbstractView view)
  {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  private EigenschaftGruppe getEigenschaftGruppe()
  {
    if (eigenschaftgruppe != null)
    {
      return eigenschaftgruppe;
    }
    eigenschaftgruppe = (EigenschaftGruppe) getCurrentObject();
    return eigenschaftgruppe;
  }

  public Input getBezeichnung() throws RemoteException
  {
    if (bezeichnung != null)
    {
      return bezeichnung;
    }
    bezeichnung = new TextInput(getEigenschaftGruppe().getBezeichnung(), 30);
    return bezeichnung;
  }

  public CheckboxInput getPflicht() throws RemoteException
  {
    if (pflicht != null)
    {
      return pflicht;
    }
    pflicht = new CheckboxInput(getEigenschaftGruppe().getPflicht());
    return pflicht;
  }

  public CheckboxInput getMax1() throws RemoteException
  {
    if (max1 != null)
    {
      return max1;
    }
    max1 = new CheckboxInput(getEigenschaftGruppe().getMax1());
    return max1;
  }

  /**
   * This method stores the project using the current values.
   */
  public void handleStore()
  {
    try
    {
      EigenschaftGruppe eg = getEigenschaftGruppe();
      eg.setBezeichnung((String) getBezeichnung().getValue());
      eg.setPflicht((Boolean) getPflicht().getValue());
      eg.setMax1((Boolean) getMax1().getValue());
      try
      {
        eg.store();
        GUI.getStatusBar().setSuccessText("Eigenschaften Gruppe gespeichert");
      }
      catch (ApplicationException e)
      {
        GUI.getStatusBar().setErrorText(e.getMessage());
      }
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei speichern der Eigenschaft Gruppe";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  public Part getEigenschaftGruppeList() throws RemoteException
  {
    if (eigenschaftgruppeList != null)
    {
      return eigenschaftgruppeList;
    }

    DBService service = Einstellungen.getDBService();
    DBIterator eigenschaftgruppe = service.createList(EigenschaftGruppe.class);
    eigenschaftgruppe.setOrder("ORDER BY bezeichnung");

    eigenschaftgruppeList = new TablePart(eigenschaftgruppe,
        new EigenschaftGruppeDetailAction(false));
    eigenschaftgruppeList.addColumn("Bezeichnung", "bezeichnung");
    eigenschaftgruppeList.addColumn("Pflicht", "pflicht", new JaNeinFormatter());
    eigenschaftgruppeList.addColumn("max. 1 Eigenschaft", "max1",
        new JaNeinFormatter());
    eigenschaftgruppeList.setContextMenu(new EigenschaftGruppeMenu());
    eigenschaftgruppeList.setRememberColWidths(true);
    eigenschaftgruppeList.setRememberOrder(true);
    eigenschaftgruppeList.setSummary(true);
    return eigenschaftgruppeList;
  }
}
