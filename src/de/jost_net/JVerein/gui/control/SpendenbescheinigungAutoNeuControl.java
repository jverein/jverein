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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.io.Adressbuch.Adressaufbereitung;
import de.jost_net.JVerein.keys.FormularArt;
import de.jost_net.JVerein.keys.Spendenart;
import de.jost_net.JVerein.rmi.Formular;
import de.jost_net.JVerein.rmi.Spendenbescheinigung;
import de.jost_net.JVerein.server.SpendenbescheinigungNode;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.TreePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class SpendenbescheinigungAutoNeuControl extends AbstractControl
{

	private de.willuhn.jameica.system.Settings settings;

	private SelectInput jahr;

	private TreePart spbTree;

	private SelectInput formularEinzel;

	private SelectInput formularSammel;

	public SpendenbescheinigungAutoNeuControl(AbstractView view)
	{
		super(view);
		settings = new de.willuhn.jameica.system.Settings(this.getClass());
		settings.setStoreWhenRead(true);
	}

	public SelectInput getJahr()
	{
		if (jahr != null)
		{
			return jahr;
		}
		Calendar cal = Calendar.getInstance();
		jahr = new SelectInput(new Object[]
		{
				cal.get(Calendar.YEAR), cal.get(Calendar.YEAR) - 1,
				cal.get(Calendar.YEAR) - 2, cal.get(Calendar.YEAR) - 3
		}, cal.get(Calendar.YEAR));
		jahr.addListener(new Listener()
		{

			@Override
			public void handleEvent(Event event)
			{
				try
				{
					spbTree.setRootObject(
							new SpendenbescheinigungNode((Integer) getJahr().getValue()));
				}
				catch (RemoteException e)
				{
					Logger.error("Fehler", e);
				}
			}

		});
		return jahr;
	}

	public SelectInput getFormular() throws RemoteException
	{
		if (formularEinzel != null)
		{
			return formularEinzel;
		}
		DBIterator<
				Formular> it = Einstellungen.getDBService().createList(Formular.class);
		it.addFilter("art = ?", new Object[]
		{
				FormularArt.SPENDENBESCHEINIGUNG.getKey()
		});
		formularEinzel = new SelectInput(it, null);
		return formularEinzel;
	}

	public SelectInput getFormularSammelbestaetigung() throws RemoteException
	{
		if (formularSammel != null)
		{
			return formularSammel;
		}
		DBIterator<
				Formular> it = Einstellungen.getDBService().createList(Formular.class);
		it.addFilter("art = ?", new Object[]
		{
				FormularArt.SAMMELSPENDENBESCHEINIGUNG.getKey()
		});
		formularSammel = new SelectInput(it, null);
		return formularSammel;
	}

	/**
	 * This method stores the project using the current values.
	 */
	public void handleStore()
	{
		//
	}

	public Button getSpendenbescheinigungErstellenButton()
	{
		Button b = new Button("erstellen", new Action()
		{

			@Override
			public void handleAction(Object context) throws ApplicationException
			{
				try
				{
					@SuppressWarnings("rawtypes")
					List items = spbTree.getItems();
					SpendenbescheinigungNode spn = (SpendenbescheinigungNode) items
							.get(0);
					// Loop über die Mitglieder
					GenericIterator<?> it1 = spn.getChildren();
					while (it1.hasNext())
					{
						SpendenbescheinigungNode sp1 = (SpendenbescheinigungNode) it1
								.next();
						Spendenbescheinigung spbescheinigung = (Spendenbescheinigung) Einstellungen
								.getDBService().createObject(Spendenbescheinigung.class, null);
						spbescheinigung.setSpendenart(Spendenart.GELDSPENDE);
						spbescheinigung.setMitglied(sp1.getMitglied());
						spbescheinigung.setZeile1(sp1.getMitglied().getAnrede());
						spbescheinigung.setZeile2(
								Adressaufbereitung.getVornameName(sp1.getMitglied()));
						spbescheinigung.setZeile3(sp1.getMitglied().getStrasse());
						spbescheinigung.setZeile4(
								sp1.getMitglied().getPlz() + " " + sp1.getMitglied().getOrt());
						spbescheinigung.setZeile5(sp1.getMitglied().getStaat());
						spbescheinigung.setErsatzAufwendungen(false);
						spbescheinigung.setBescheinigungsdatum(new Date());
						spbescheinigung.setSpendedatum(new Date());
						spbescheinigung.setBetrag(0.01);
						spbescheinigung.setAutocreate(Boolean.TRUE);
						// Loop über die Buchungen eines Mitglieds
						GenericIterator<?> it2 = sp1.getChildren();
						while (it2.hasNext())
						{
							SpendenbescheinigungNode sp2 = (SpendenbescheinigungNode) it2
									.next();
							spbescheinigung.addBuchung(sp2.getBuchung());
						}
						// Nun noch das korrekte Formular setzen
						if (spbescheinigung.getBuchungen().size() > 1)
						{
							spbescheinigung.setFormular(
									(Formular) getFormularSammelbestaetigung().getValue());
						}
						else
						{
							spbescheinigung.setFormular((Formular) getFormular().getValue());
						}
						spbescheinigung.store();
					}
					GUI.getStatusBar()
							.setSuccessText("Spendenbescheinigung(en) erstellt");
					spbTree.removeAll();
				}
				catch (RemoteException e)
				{
					Logger.error(e.getMessage());
					throw new ApplicationException(
							"Fehler bei der Aufbereitung der Spendenbescheinigung");
				}
			}
		}, null, false, "save.png");
		return b;
	}

	public Part getSpendenbescheinigungTree() throws RemoteException
	{
		spbTree = new TreePart(
				new SpendenbescheinigungNode((Integer) getJahr().getValue()), null);
		return spbTree;
	}

}
