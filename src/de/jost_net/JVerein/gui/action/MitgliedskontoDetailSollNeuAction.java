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
import de.jost_net.JVerein.gui.control.MitgliedskontoNode;
import de.jost_net.JVerein.gui.view.MitgliedskontoDetailView;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class MitgliedskontoDetailSollNeuAction implements Action
{

	@Override
	public void handleAction(Object context) throws ApplicationException
	{
		MitgliedskontoNode mkn = null;
		Mitgliedskonto mk = null;

		if (context == null || !(context instanceof MitgliedskontoNode))
		{
			throw new ApplicationException("Kein Mitgliedskonto ausgewählt");
		}

		if (context != null && (context instanceof MitgliedskontoNode))
		{
			mkn = (MitgliedskontoNode) context;
			try
			{
				Mitglied m = (Mitglied) Einstellungen.getDBService()
						.createObject(Mitglied.class, mkn.getID());
				mk = (Mitgliedskonto) Einstellungen.getDBService()
						.createObject(Mitgliedskonto.class, null);
				mk.setZahlungsweg(m.getZahlungsweg());
				mk.setMitglied(m);
			}
			catch (RemoteException e)
			{
				throw new ApplicationException(
						"Fehler bei der Erzeugung eines Mitgliedskontos");
			}
		}
		GUI.startView(new MitgliedskontoDetailView(MitgliedskontoNode.SOLL), mk);
	}
}
