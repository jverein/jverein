/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
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

  public String getTitle()
  {
    return "Adressen suchen";
  }

  public void getFilter() throws RemoteException
  {
    LabelGroup group = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Filter"));
    Input adrtyp = control.getSuchAdresstyp(2);
    Adresstyp at = (Adresstyp) Einstellungen.getDBService().createObject(
        Adresstyp.class, "2");
    control.getSuchAdresstyp(2).setValue(at);
    adrtyp.addListener(new FilterListener());
    group.addLabelPair("Adresstyp", adrtyp);
  }

  public Action getDetailAction()
  {
    return new AdresseDetailAction();
  }

  public Button getHilfeButton()
  {
    return new Button(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.ADRESSEN, false,
        "help-browser.png");
  }
}