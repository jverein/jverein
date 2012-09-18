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

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.AdresseDetailAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.rmi.Adresstyp;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.util.LabelGroup;

public class AdressenSucheView extends AbstractAdresseSucheView
{
  public AdressenSucheView() throws RemoteException
  {
    control.getSuchAdresstyp(2).getValue();
    Adresstyp at = (Adresstyp) Einstellungen.getDBService().createObject(
        Adresstyp.class, "2");
    control.getSuchAdresstyp(2).setValue(at);
  }

  @Override
  public String getTitle()
  {
    return JVereinPlugin.getI18n().tr("Adressen suchen");
  }

  @Override
  public void getFilter() throws RemoteException
  {
    LabelGroup group = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Filter"));
    TextInput suchName = control.getSuchname();
    suchName.addListener(new FilterListener());
    group.addInput(suchName);

    Input adrtyp = control.getSuchAdresstyp(2);
    Adresstyp at = (Adresstyp) Einstellungen.getDBService().createObject(
        Adresstyp.class, "2");
    control.getSuchAdresstyp(2).setValue(at);
    adrtyp.addListener(new FilterListener());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Adresstyp"), adrtyp);
  }

  @Override
  public Action getDetailAction()
  {
    return new AdresseDetailAction();
  }

  @Override
  public Button getHilfeButton()
  {
    return new Button(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.ADRESSEN, false,
        "help-browser.png");
  }
}