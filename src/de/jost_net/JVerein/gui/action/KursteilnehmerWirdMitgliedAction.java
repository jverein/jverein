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

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.input.PersonenartInput;
import de.jost_net.JVerein.gui.view.MitgliedDetailView;
import de.jost_net.JVerein.rmi.Kursteilnehmer;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class KursteilnehmerWirdMitgliedAction implements Action
{

  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null || !(context instanceof Kursteilnehmer))
    {
      throw new ApplicationException("keinen Kursteilnehmer ausgewählt");
    }
    Kursteilnehmer k = (Kursteilnehmer) context;
    try
    {
      Mitglied m = (Mitglied) Einstellungen.getDBService().createObject(
          Mitglied.class, null);
      m.setAdresstyp(1);
      m.setAnrede(k.getAnrede());
      m.setBic(k.getBic());
      m.setEmail(k.getEmail());
      m.setGeburtsdatum(k.getGeburtsdatum());
      m.setGeschlecht(k.getGeschlecht());
      m.setIban(k.getIban());
      m.setName(k.getName());
      m.setOrt(k.getOrt());
      m.setPersonenart(PersonenartInput.NATUERLICHE_PERSON.substring(0, 1));
      m.setPlz(k.getPlz());
      m.setStaat(k.getStaat());
      m.setStrasse(k.getStrasse());
      m.setTitel(k.getTitel());
      m.setVorname(k.getVorname());

      GUI.startView(new MitgliedDetailView(), m);
    }
    catch (Exception e)
    {
      throw new ApplicationException(
          "Fehler bei der Umwandlung eines Kursteilnehmers zum Mitglied", e);
    }
  }
}
