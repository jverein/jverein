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

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.view.SpendenbescheinigungView;
import de.jost_net.JVerein.rmi.Spendenbescheinigung;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Spendenbescheinigung duplizieren
 */
public class SpendenbescheinigungDuplizierenAction implements Action
{

  /**
   * Duplizieren einer Spendenbescheinigung. Es kann nur dann eine Kopie
   * erstellt werden, wenn genau ein Eintrag selektiert ist. Es wird zusätzlich
   * auch das Mitglied in die neue Spendenbescheinugung übernommen.
   */
  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null || !(context instanceof Spendenbescheinigung))
    {
      throw new ApplicationException("Keine Spendenbescheinigung ausgewählt");
    }
    try
    {
      Spendenbescheinigung spb = (Spendenbescheinigung) context;
      Spendenbescheinigung spb2 = (Spendenbescheinigung) Einstellungen.getDBService().createObject(
          Spendenbescheinigung.class, null);
      spb2.setBetrag(spb.getBetrag());
      spb2.setErsatzAufwendungen(spb.getErsatzAufwendungen());
      spb2.setFormular(spb.getFormular());
      spb2.setZeile1(spb.getZeile1());
      spb2.setZeile2(spb.getZeile2());
      spb2.setZeile3(spb.getZeile3());
      spb2.setZeile4(spb.getZeile4());
      spb2.setZeile5(spb.getZeile5());
      spb2.setZeile6(spb.getZeile6());
      spb2.setZeile7(spb.getZeile7());
      spb2.setMitglied(spb.getMitglied());
      GUI.startView(SpendenbescheinigungView.class.getName(), spb2);
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler beim Duplizieren der Spendenbescheinigung";
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
