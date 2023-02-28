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
package de.jost_net.JVerein.gui.menu;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.AdresseDetailAction;
import de.jost_net.JVerein.gui.action.FreiesFormularAction;
import de.jost_net.JVerein.gui.action.KontoauszugAction;
import de.jost_net.JVerein.gui.action.MitgliedArbeitseinsatzZuordnungAction;
import de.jost_net.JVerein.gui.action.MitgliedDeleteAction;
import de.jost_net.JVerein.gui.action.MitgliedDuplizierenAction;
import de.jost_net.JVerein.gui.action.MitgliedEigenschaftZuordnungAction;
import de.jost_net.JVerein.gui.action.MitgliedInZwischenablageKopierenAction;
import de.jost_net.JVerein.gui.action.MitgliedLastschriftAction;
import de.jost_net.JVerein.gui.action.MitgliedMailSendenAction;
import de.jost_net.JVerein.gui.action.MitgliedVCardDateiAction;
import de.jost_net.JVerein.gui.action.MitgliedVCardQRCodeAction;
import de.jost_net.JVerein.gui.action.MitgliedZusatzbetraegeZuordnungAction;
import de.jost_net.JVerein.gui.action.PersonalbogenAction;
import de.jost_net.JVerein.gui.action.SpendenbescheinigungAction;
import de.jost_net.JVerein.gui.view.MitgliedDetailView;
import de.jost_net.JVerein.keys.FormularArt;
import de.jost_net.JVerein.rmi.Formular;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.SimpleDialog;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.CheckedSingleContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;
import de.willuhn.jameica.gui.parts.ContextMenuItem;
import de.willuhn.jameica.gui.util.SWTUtil;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Kontext-Menu zu den Mitgliedern
 */
public class MitgliedMenu extends ContextMenu
{

	/**
	 * Erzeugt ein Kontext-Menu für die Liste der Mitglieder.
	 * 
	 * @throws RemoteException
	 */
	public MitgliedMenu(Action detailaction)
			throws RemoteException
	{
		addItem(new CheckedSingleContextMenuItem("bearbeiten", detailaction,
				"edit.png"));
		addItem(new CheckedSingleContextMenuItem("duplizieren",
				new MitgliedDuplizierenAction(), "copy.png"));
		addItem(new CheckedContextMenuItem("in Zwischenablage kopieren",
				new MitgliedInZwischenablageKopierenAction(), "copy.png"));
		if (detailaction instanceof AdresseDetailAction)
		{
			addItem(new CheckedContextMenuItem("zu Mitglied umwandeln", new Action()
			{

				@Override
				public void handleAction(Object context) throws ApplicationException
				{
					Mitglied m = (Mitglied) context;
					try
					{
						SimpleDialog sd = new SimpleDialog(SimpleDialog.POSITION_CENTER);
						sd.setText(
								"Bitte die für Mitglieder erforderlichen Daten nacherfassen.");
						sd.setSideImage(SWTUtil.getImage("dialog-warning-large.png"));
						sd.setSize(400, 150);
						sd.setTitle("Daten nacherfassen");
						try
						{
							sd.open();
						}
						catch (Exception e)
						{
							Logger.error("Fehler", e);
						}
						m.setAdresstyp(1);
						m.setEingabedatum();
						GUI.startView(MitgliedDetailView.class.getName(), m);
					}
					catch (RemoteException e)
					{
						throw new ApplicationException(e);
					}
				}
			}, "arrows-alt-h.png"));
		}
		addItem(new CheckedSingleContextMenuItem("löschen...",
				new MitgliedDeleteAction(), "trash-alt.png"));
		addItem(ContextMenuItem.SEPARATOR);
		addItem(new CheckedContextMenuItem("Mail senden ...",
				new MitgliedMailSendenAction(), "envelope-open.png"));
		addItem(new CheckedContextMenuItem("vCard-Datei",
				new MitgliedVCardDateiAction(), "address-card.png"));
		addItem(new CheckedSingleContextMenuItem("vCard QR-Code",
				new MitgliedVCardQRCodeAction(), "qr-code.png"));
		addItem(new CheckedContextMenuItem("Eigenschaften",
				new MitgliedEigenschaftZuordnungAction(), "check-double.png"));
		if (Einstellungen.getEinstellung().getArbeitseinsatz())
		{
			addItem(new CheckedContextMenuItem("Arbeitseinsätze zuweisen",
					new MitgliedArbeitseinsatzZuordnungAction(), "screwdriver.png"));
		}
		addItem(new CheckedContextMenuItem("Zusatzbeträge zuweisen",
				new MitgliedZusatzbetraegeZuordnungAction(), "coins.png"));
		addItem(new CheckedContextMenuItem("Kontoauszug", new KontoauszugAction(),
				"rechnung.png"));
		addItem(new CheckedSingleContextMenuItem("Spendenbescheinigung",
				new SpendenbescheinigungAction(), "file-invoice.png"));
		addItem(new CheckedContextMenuItem("Personalbogen",
				new PersonalbogenAction(), "file-invoice.png"));
		addItem(new CheckedSingleContextMenuItem("Manuelle Lastschrift ...",
				new MitgliedLastschriftAction(), "file-invoice.png"));
		DBIterator<
				Formular> it = Einstellungen.getDBService().createList(Formular.class);
		it.addFilter("art = ?", new Object[]
		{
				FormularArt.FREIESFORMULAR.getKey()
		});
		while (it.hasNext())
		{
			Formular f = it.next();
			addItem(new CheckedContextMenuItem(f.getBezeichnung(),
					new FreiesFormularAction(f.getID()), "file-invoice.png"));
		}
	}
}
