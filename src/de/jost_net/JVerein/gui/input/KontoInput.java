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

package de.jost_net.JVerein.gui.input;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.kapott.hbci.manager.HBCIUtils;

import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.hbci.HBCI;
import de.willuhn.jameica.hbci.Settings;
import de.willuhn.jameica.hbci.rmi.Konto;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.I18N;

/**
 * Autosuggest-Feld zur Eingabe/Auswahl eines Kontos.
 */
public class KontoInput extends SelectInput
{

	private final static I18N i18n = Application.getPluginLoader()
			.getPlugin(HBCI.class).getResources().getI18N();

	/**
	 * ct.
	 * 
	 * @param konto ausgewaehltes Konto.
	 * @throws RemoteException
	 */
	public KontoInput(Konto konto)
			throws RemoteException
	{
		super(init(), konto);
		setName(i18n.tr("Konto"));
		setPleaseChoose(i18n.tr("Bitte wählen..."));
	}

	/**
	 * Initialisiert die Liste der Konten.
	 * 
	 * @return Liste der Konten.
	 * @throws RemoteException
	 */
	private static GenericIterator<Konto> init() throws RemoteException
	{
		DBIterator<Konto> it = Settings.getDBService().createList(Konto.class);
		it.setOrder("ORDER BY blz, kontonummer");
		List<Konto> l = new ArrayList<>();
		while (it.hasNext())
		{
			l.add(it.next());
		}
		return PseudoIterator.fromArray(l.toArray(new Konto[l.size()]));
	}

	/**
	 * @see de.willuhn.jameica.gui.input.SelectInput#format(java.lang.Object)
	 */
	@Override
	protected String format(Object bean)
	{
		if (bean == null)
			return null;

		if (!(bean instanceof Konto))
			return bean.toString();

		try
		{
			Konto k = (Konto) bean;
			boolean disabled = (k.getFlags()
					& Konto.FLAG_DISABLED) == Konto.FLAG_DISABLED;
			StringBuffer sb = new StringBuffer();
			if (disabled)
				sb.append("[");

			sb.append(String.format("Kto. %s", k.getKontonummer()));

			String blz = k.getBLZ();
			sb.append(" [");
			String bankName = HBCIUtils.getNameForBLZ(blz);
			if (bankName != null && bankName.length() > 0)
			{
				sb.append(bankName);
			}
			else
			{
				sb.append("BLZ" + " ");
				sb.append(blz);
			}
			sb.append("] ");
			sb.append(k.getName());

			String bez = k.getBezeichnung();
			if (bez != null && bez.length() > 0)
			{
				sb.append(" - ");
				sb.append(bez);
			}
			if (disabled)
				sb.append("]");
			return sb.toString();
		}
		catch (RemoteException re)
		{
			Logger.error("unable to format address", re);
			return null;
		}
	}

}
