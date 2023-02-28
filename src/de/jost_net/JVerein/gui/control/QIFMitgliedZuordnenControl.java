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
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.QIFImportPos;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.formatter.TableFormatter;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Column;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.gui.util.Font;
import de.willuhn.jameica.gui.util.SWTUtil;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class QIFMitgliedZuordnenControl extends AbstractControl
{
	public static final String CB_TEXT_ALLE = "Zeige immer alle Mitglieder in der Auswahlbox";

	public static final String CB_TEXT_MANCHE = "Zeige nur Mitglieder passend zum externen Namen";

	private TablePart distinctExternNameListTable;

	private TablePart posBeispielListTable;

	private SelectInput mitgliederInput;

	private TextInput externerNameInput;

	private CheckboxInput alleMitgliederInput;

	private DBIterator<QIFImportPos> iteratorQIFImportPosList;

	private QIFImportPos qifImportPos;

	private Image imageOk;

	private Image imageError;

	private boolean mitgliederGeladen;

	public QIFMitgliedZuordnenControl(AbstractView view)
	{
		super(view);
		init();
	}

	private void init()
	{
		imageOk = SWTUtil.getImage("ok.png");
		imageError = SWTUtil.getImage("stop-circle.png");
		mitgliederGeladen = false;
	}

	public Input getExternerNameInput()
	{
		if (null == externerNameInput)
		{
			externerNameInput = new TextInput("", 30);
			externerNameInput.disable();
		}
		return externerNameInput;
	}

	public Input getCheckBoxAlleMitgliederZeigen()
	{
		if (null == alleMitgliederInput)
		{
			alleMitgliederInput = new CheckboxInput(false);
			setzeKommentar(false);
			alleMitgliederInput.addListener(new Listener()
			{
				@Override
				public void handleEvent(Event event)
				{
					alleMitgliederInput_ActionPerformed();
				}
			});
		}
		return alleMitgliederInput;
	}

	protected void alleMitgliederInput_ActionPerformed()
	{
		boolean wert = getCBValueAlleMitgliederZeigen();
		setzeKommentar(wert);
		mitgliederGeladen = false;
		selectionChanged_ExterneNameList();
	}

	private boolean getCBValueAlleMitgliederZeigen()
	{
		Boolean wert = (Boolean) alleMitgliederInput.getValue();
		return wert.booleanValue();
	}

	private void setzeKommentar(boolean wert)
	{
		if (wert)
			alleMitgliederInput.setComment(CB_TEXT_ALLE);
		else
			alleMitgliederInput.setComment(CB_TEXT_MANCHE);
	}

	private void aktuallisierenExternerNameInput()
	{
		try
		{
			if (null != qifImportPos)
			{
				getExternerNameInput().setValue(qifImportPos.getName());
				return;
			}
		}
		catch (RemoteException ex)
		{
			Logger.error("Fehler", ex);
		}
		getExternerNameInput().setValue("");
	}

	public Part getBeispielPositionsListe() throws RemoteException
	{
		if (null == posBeispielListTable)
		{
			posBeispielListTable = new TablePart(iteratorQIFImportPosList, null);
			posBeispielListTable.addColumn("Datum", QIFImportPos.COL_DATUM,
					new DateFormatter(new JVDateFormatTTMMJJJJ()));
			posBeispielListTable.addColumn("Beleg", QIFImportPos.COL_BELEG);
			posBeispielListTable.addColumn("Betrag", QIFImportPos.COL_BETRAG,
					new CurrencyFormatter("", Einstellungen.DECIMALFORMAT), false,
					Column.ALIGN_RIGHT);
			posBeispielListTable.addColumn("Name", QIFImportPos.COL_NAME);
			posBeispielListTable.addColumn("Zweck", QIFImportPos.COL_ZWECK);
			posBeispielListTable.addColumn("Konto", QIFImportPos.VIEW_QIFKONTO_NAME);

			posBeispielListTable.setRememberColWidths(true);
			posBeispielListTable.setRememberOrder(true);
		}
		else
		{
			aktuallisiereQIFPosListAnzeige();
		}
		return posBeispielListTable;
	}

	private void aktuallisiereQIFPosListAnzeige() throws RemoteException
	{
		iteratorQIFImportPosList.begin();

		posBeispielListTable.removeAll();
		while (iteratorQIFImportPosList.hasNext())
		{
			QIFImportPos importPos = iteratorQIFImportPosList.next();
			posBeispielListTable.addItem(importPos);
		}
		posBeispielListTable.sort();
	}

	private void synchronisiereComboBoxMitglieder() throws RemoteException
	{
		Mitglied mitglied = null;
		if (null != qifImportPos)
		{
			fuellenMitgliederInput(qifImportPos.getName());
			mitglied = qifImportPos.getMitglied();
		}
		mitgliederInput.setValue(mitglied);
	}

	private void fuellenMitgliederInput(String externerName)
			throws RemoteException
	{
		if (mitgliederGeladen)
			return;

		DBIterator<
				Mitglied> iteratorMitglieder = ladeMitgliederPassendFuerExternenNamen(
						externerName);
		int iAnzahl = iteratorMitglieder.size();
		Logger.info("Mitglieder zur Auswahl geladen. Anzahl : " + iAnzahl);

		SelectInput selMitglied = getMitgliederInput();
		selMitglied.setList(PseudoIterator.asList(iteratorMitglieder));
		selMitglied.setComment(
				Integer.toString(iAnzahl) + " Mitglieder zur Auswahl möglich..");
		mitgliederGeladen = true;
	}

	private DBIterator<Mitglied> ladeMitgliederPassendFuerExternenNamen(
			String externerName) throws RemoteException
	{
		DBIterator<Mitglied> iteratorMitglieder = Einstellungen.getDBService()
				.createList(Mitglied.class);
		if (getCBValueAlleMitgliederZeigen() == false)
		{
			iteratorMitglieder.addFilter(getWhere(externerName));
		}
		iteratorMitglieder.setOrder(" order by name ");
		return iteratorMitglieder;
	}

	private String getWhere(String externerName)
	{
		final String whereFormat = "lower(name) like '%%%s%%'";
		final String token = " \t\n\r\f/-.,";

		StringBuilder stb = new StringBuilder();
		StringTokenizer tokenizer = new StringTokenizer(externerName, token);
		while (tokenizer.hasMoreTokens())
		{
			String nextText = tokenizer.nextToken().toLowerCase();
			if (nextText.length() > 3)
			{
				if (stb.length() > 0)
					stb.append(" or ");
				stb.append(String.format(whereFormat, nextText));
			}
		}
		return stb.toString();
	}

	private void ladeQIFPosListFuerName(QIFImportPos importPos)
			throws RemoteException
	{
		String name = importPos.getName();
		iteratorQIFImportPosList = Einstellungen.getDBService()
				.createList(QIFImportPos.class);
		iteratorQIFImportPosList.addFilter(QIFImportPos.COL_NAME + " is not null ");
		iteratorQIFImportPosList.addFilter(QIFImportPos.COL_NAME + " = ?", name);
		iteratorQIFImportPosList.addFilter(QIFImportPos.COL_MITGLIEDBAR + " = ? ",
				QIFImportPos.MITGLIEDBAR_JA);
		iteratorQIFImportPosList.setOrder("ORDER BY " + QIFImportPos.COL_POSID);
	}

	public SelectInput getMitgliederInput() throws RemoteException
	{
		if (mitgliederInput != null && !mitgliederInput.getControl().isDisposed())
		{
			return mitgliederInput;
		}
		mitgliederInput = new SelectInput((GenericIterator<?>) null, null);
		mitgliederInput.setAttribute("namevorname");
		mitgliederInput.setPleaseChoose("Bitte auswählen");
		mitgliederInput.setComment(" ");
		return mitgliederInput;
	}

	public Action getSpeichernAction()
	{
		return new SpeichernAction();
	}

	class SpeichernAction implements Action
	{
		private Mitglied mitglied;

		@Override
		public void handleAction(Object context) throws ApplicationException
		{
			try
			{
				getInputValues();
				if (checkInputValues())
				{
					speichernMitgliedInImportPos(mitglied);
					refreshSelectedQIFImportPos();
				}
			}
			catch (RemoteException ex)
			{
				throw new ApplicationException("Mitglied kann nicht gespeichert werden",
						ex);
			}
		}

		private void getInputValues()
		{
			mitglied = (Mitglied) mitgliederInput.getValue();
		}

		private boolean checkInputValues()
		{
			if (null == mitglied)
			{
				GUI.getStatusBar().setErrorText("Keine JVerein Mitglied gewählt!!");
				return false;
			}
			return true;
		}

	}

	private void speichernMitgliedInImportPos(Mitglied mitglied)
			throws RemoteException, ApplicationException
	{
		iteratorQIFImportPosList.begin();
		while (iteratorQIFImportPosList.hasNext())
		{
			QIFImportPos importPos = iteratorQIFImportPosList.next();
			importPos.setMitglied(mitglied);
			importPos.store();
		}
	}

	private void refreshSelectedQIFImportPos() throws RemoteException
	{
		qifImportPos.load(qifImportPos.getID());
		distinctExternNameListTable.updateItem(qifImportPos, qifImportPos);
	}

	// ------------------------------ Test

	/**
	 * Erstelle eine Liste mit QIFImportPos in der alle externen Buchungsarten
	 * einmal vorhanden sind. Positionen ohne QIFBuchungsart werden alle in die
	 * Liste aufgenommen
	 * 
	 * @throws RemoteException
	 */
	private GenericIterator<?> getDistinctQIFNameList() throws RemoteException
	{
		DBIterator<QIFImportPos> it = Einstellungen.getDBService()
				.createList(QIFImportPos.class);
		it.addFilter(QIFImportPos.COL_NAME + " is not null ");
		it.addFilter(QIFImportPos.COL_MITGLIEDBAR + " = ? ",
				QIFImportPos.MITGLIEDBAR_JA);
		it.addFilter(QIFImportPos.COL_SPERRE + " = ? ", QIFImportPos.SPERRE_NEIN);
		it.setOrder("ORDER BY " + QIFImportPos.COL_NAME);

		List<QIFImportPos> l = new ArrayList<>();

		String letzterName = "";
		while (it.hasNext())
		{
			QIFImportPos pos = it.next();
			String name = pos.getName();
			if (letzterName.equals(name) == false)
			{
				letzterName = new String(name);
				l.add(pos);
			}
		}

		return PseudoIterator.fromArray(l.toArray(new QIFImportPos[l.size()]));
	}

	public Part getExterneNamensListe() throws RemoteException
	{
		if (null == distinctExternNameListTable)
		{
			distinctExternNameListTable = new TablePart(getDistinctQIFNameList(),
					null);
			distinctExternNameListTable.addColumn("Externer Name",
					QIFImportPos.COL_NAME);
			distinctExternNameListTable.addColumn("Status", "Status");
			distinctExternNameListTable.addColumn("JVerein Mitglied",
					QIFImportPos.VIEW_MITGLIEDS_NAME);

			distinctExternNameListTable.setRememberColWidths(true);
			distinctExternNameListTable.setRememberOrder(true);
			distinctExternNameListTable.setFormatter(new NamensListTableFormater());
			distinctExternNameListTable
					.addSelectionListener(new ExterneNamensListSelectionListener());
		}
		return distinctExternNameListTable;
	}

	class ExterneNamensListSelectionListener implements Listener
	{
		@Override
		public void handleEvent(Event event)
		{
			selectionChanged_ExterneNameList();
		}
	}

	private void selectionChanged_ExterneNameList()
	{
		try
		{
			if (getCBValueAlleMitgliederZeigen() == false)
				mitgliederGeladen = false;

			qifImportPos = (QIFImportPos) distinctExternNameListTable.getSelection();
			ladeQIFPosListFuerName(qifImportPos);
			aktuallisiereQIFPosListAnzeige();
			synchronisiereComboBoxMitglieder();
			aktuallisierenExternerNameInput();
		}
		catch (Throwable ex)
		{
			Logger.error("Fehler", ex);
		}
	}

	class NamensListTableFormater implements TableFormatter
	{
		@Override
		public void format(TableItem item)
		{
			if (null == item)
				return;

			try
			{
				QIFImportPos pos = (QIFImportPos) item.getData();
				if (pos.getMitglied() == null)
				{
					item.setImage(1, imageError);
					item.setFont(2, Font.ITALIC.getSWTFont());
					item.setText(2, "Selektiere Zeile und ordne zu!!");
				}
				else
					item.setImage(1, imageOk);
			}
			catch (Throwable ex)
			{
				Logger.error("Fehler", ex);
			}
		}
	}

	public Action getZuordnenEntfernenAction()
	{
		return new Action()
		{
			@Override
			public void handleAction(Object context) throws ApplicationException
			{
				zuordnungEntfernen();
			}

		};
	}

	private void zuordnungEntfernen() throws ApplicationException
	{
		try
		{
			speichernMitgliedInImportPos(null);
			refreshSelectedQIFImportPos();
		}
		catch (RemoteException ex)
		{
			throw new ApplicationException(
					"Fehler beim Entfernen der Mitgliedszuordnung!", ex);
		}
	}

}
