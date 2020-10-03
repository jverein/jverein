/**********************************************************************
 * Copyright (c) by Heiner Jostkleigrewe
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de | www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.control.BuchungsControl;
import de.jost_net.JVerein.gui.view.BuchungView;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Konto;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.logging.Logger;

public class BuchungNeuAction implements Action {
  @Override
  public void handleAction(Object context) {
    Buchung buch;
    try {
      buch = (Buchung) Einstellungen.getDBService().createObject(Buchung.class, null);
      if (context instanceof BuchungsControl) {
        BuchungsControl control = (BuchungsControl) context;
        Konto konto = (Konto) control.getSuchKonto().getValue();
        if (null != konto)
          buch.setKonto(konto);
        buch.setBelegnummer(
            BuchungsControl.getLastBelegnummer(buch.getDatum(), buch.getKonto().getID()) + 1);
      }
      GUI.startView(BuchungView.class, buch);
    } catch (RemoteException e) {
      Logger.error("Fehler", e);
    }
  }
}
