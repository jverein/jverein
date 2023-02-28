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
package de.jost_net.JVerein.gui.dialogs;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.keys.Datentyp;
import de.jost_net.JVerein.rmi.Felddefinition;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.DecimalInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.IntegerInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.gui.util.ScrolledContainer;
import de.willuhn.util.Settings;

public class ZusatzfelderAuswahlDialog extends AbstractDialog<Object>
{

	private ArrayList<Input> felder;

	private Settings settings;

	public ZusatzfelderAuswahlDialog(Settings settings)
	{
		super(EigenschaftenAuswahlDialog.POSITION_CENTER);
		this.setSize(400, 700);
		setTitle("Zusatzfelder-Bedingungen ");
		this.settings = settings;
	}

	@Override
	protected void paint(Composite parent) throws RemoteException
	{
		ScrolledContainer sc = new ScrolledContainer(parent);
		LabelGroup group = new LabelGroup(sc.getComposite(), "Bedingungen", false);
		felder = new ArrayList<>();

		DBIterator<Felddefinition> it = Einstellungen.getDBService()
				.createList(Felddefinition.class);
		int counter = 0;
		while (it.hasNext())
		{
			Felddefinition fd = it.next();
			switch (fd.getDatentyp())
			{
				case Datentyp.ZEICHENFOLGE:
				{
					TextInput input = new TextInput("", fd.getLaenge());
					input.setName(fd.getLabel());
					felder.add(input);
					group.addInput(input);
					counter++;
					settings.setAttribute("zusatzfeld." + counter + ".name",
							fd.getName());
					settings.setAttribute("zusatzfeld." + counter + ".cond", "LIKE");
					settings.setAttribute("zusatzfeld." + counter + ".datentyp",
							fd.getDatentyp());
					settings.setAttribute("zusatzfeld." + counter + ".definition",
							fd.getID());
					input.setValue(
							settings.getString("zusatzfeld." + counter + ".value", ""));
					break;
				}
				case Datentyp.DATUM:
				{
					DateInput inputvon = new DateInput();
					inputvon.setName(fd.getLabel() + " von");
					felder.add(inputvon);
					group.addInput(inputvon);
					counter++;
					settings.setAttribute("zusatzfeld." + counter + ".name",
							fd.getName());
					settings.setAttribute("zusatzfeld." + counter + ".cond", ">=");
					settings.setAttribute("zusatzfeld." + counter + ".datentyp",
							fd.getDatentyp());
					settings.setAttribute("zusatzfeld." + counter + ".definition",
							fd.getID());
					String datum = settings.getString("zusatzfeld." + counter + ".value",
							"");
					if (datum.length() == 10)
					{
						try
						{
							inputvon.setValue(new JVDateFormatTTMMJJJJ().parse(datum));
						}
						catch (ParseException e)
						{
							//
						}
					}
					DateInput inputbis = new DateInput();
					inputbis.setName(fd.getLabel() + " bis");
					felder.add(inputbis);
					group.addInput(inputbis);
					counter++;
					settings.setAttribute("zusatzfeld." + counter + ".name",
							fd.getName());
					settings.setAttribute("zusatzfeld." + counter + ".cond", "<=");
					settings.setAttribute("zusatzfeld." + counter + ".datentyp",
							fd.getDatentyp());
					settings.setAttribute("zusatzfeld." + counter + ".definition",
							fd.getID());
					datum = settings.getString("zusatzfeld." + counter + ".value", "");
					if (datum.length() == 10)
					{
						try
						{
							inputbis.setValue(new JVDateFormatTTMMJJJJ().parse(datum));
						}
						catch (ParseException e)
						{
							//
						}
					}
					break;
				}
				case Datentyp.GANZZAHL:
				{
					IntegerInput inputvon = new IntegerInput(-1);
					inputvon.setName(fd.getLabel() + " von");
					felder.add(inputvon);
					group.addInput(inputvon);
					counter++;
					settings.setAttribute("zusatzfeld." + counter + ".name",
							fd.getName());
					settings.setAttribute("zusatzfeld." + counter + ".cond", ">=");
					settings.setAttribute("zusatzfeld." + counter + ".datentyp",
							fd.getDatentyp());
					settings.setAttribute("zusatzfeld." + counter + ".definition",
							fd.getID());
					inputvon.setValue(
							settings.getInt("zusatzfeld." + counter + ".value", -1));

					IntegerInput inputbis = new IntegerInput(-1);
					inputbis.setName(fd.getLabel() + " bis");
					felder.add(inputbis);
					group.addInput(inputbis);
					counter++;
					settings.setAttribute("zusatzfeld." + counter + ".name",
							fd.getName());
					settings.setAttribute("zusatzfeld." + counter + ".cond", "<=");
					settings.setAttribute("zusatzfeld." + counter + ".datentyp",
							fd.getDatentyp());
					settings.setAttribute("zusatzfeld." + counter + ".definition",
							fd.getID());
					inputbis.setValue(
							settings.getInt("zusatzfeld." + counter + ".value", -1));
					break;
				}
				case Datentyp.JANEIN:
				{
					CheckboxInput input = new CheckboxInput(false);
					input.setName(fd.getLabel());
					felder.add(input);
					group.addInput(input);
					counter++;
					settings.setAttribute("zusatzfeld." + counter + ".name",
							fd.getName());
					settings.setAttribute("zusatzfeld." + counter + ".cond", "=");
					settings.setAttribute("zusatzfeld." + counter + ".datentyp",
							fd.getDatentyp());
					settings.setAttribute("zusatzfeld." + counter + ".definition",
							fd.getID());
					input.setValue(
							settings.getBoolean("zusatzfeld." + counter + ".value", false));
					break;
				}
				case Datentyp.WAEHRUNG:
				{
					DecimalInput inputvon = new DecimalInput(Einstellungen.DECIMALFORMAT);
					inputvon.setName(fd.getLabel() + " von");
					felder.add(inputvon);
					group.addInput(inputvon);
					counter++;
					settings.setAttribute("zusatzfeld." + counter + ".name",
							fd.getName());
					settings.setAttribute("zusatzfeld." + counter + ".cond", ">=");
					settings.setAttribute("zusatzfeld." + counter + ".datentyp",
							fd.getDatentyp());
					settings.setAttribute("zusatzfeld." + counter + ".definition",
							fd.getID());
					try
					{
						String value = settings
								.getString("zusatzfeld." + counter + ".value", "");

						if (value.length() > 0)
						{
							Number n = Einstellungen.DECIMALFORMAT.parse(value);
							inputvon.setValue(n.doubleValue());
						}
					}
					catch (ParseException e)
					{
						//
					}

					DecimalInput inputbis = new DecimalInput(Einstellungen.DECIMALFORMAT);
					inputbis.setName(fd.getLabel() + " bis");
					felder.add(inputbis);
					group.addInput(inputbis);
					counter++;
					settings.setAttribute("zusatzfeld." + counter + ".name",
							fd.getName());
					settings.setAttribute("zusatzfeld." + counter + ".cond", "<=");
					settings.setAttribute("zusatzfeld." + counter + ".datentyp",
							fd.getDatentyp());
					settings.setAttribute("zusatzfeld." + counter + ".definition",
							fd.getID());
					inputbis.setValue(
							settings.getString("zusatzfeld." + counter + ".value", ""));
					break;
				}
			}
			settings.setAttribute("zusatzfelder.counter", counter);
		}

		ButtonArea buttons = new ButtonArea();
		buttons.addButton("OK", new Action()
		{

			@Override
			public void handleAction(Object context)
			{
				int counter = 0;
				int selcounter = 0;
				for (Input inp : felder)
				{
					counter++;
					switch (settings.getInt("zusatzfeld." + counter + ".datentyp", 0))
					{
						case Datentyp.ZEICHENFOLGE:
						{
							String s = (String) inp.getValue();
							settings.setAttribute("zusatzfeld." + counter + ".value", s);
							if (s.length() > 0)
							{
								selcounter++;
							}
							break;
						}
						case Datentyp.DATUM:
						{
							if (inp.getValue() != null)
							{
								settings.setAttribute("zusatzfeld." + counter + ".value",
										new JVDateFormatTTMMJJJJ().format((Date) inp.getValue()));
								selcounter++;
							}
							else
							{
								String s = null;
								settings.setAttribute("zusatzfeld." + counter + ".value", s);
							}
							break;
						}
						case Datentyp.GANZZAHL:
						{
							if (inp.getValue() != null)
							{
								settings.setAttribute("zusatzfeld." + counter + ".value",
										(Integer) inp.getValue());
								selcounter++;
							}
							else
							{
								String s = null;
								settings.setAttribute("zusatzfeld." + counter + ".value", s);
							}
							break;
						}
						case Datentyp.JANEIN:
						{
							Boolean b = (Boolean) inp.getValue();
							settings.setAttribute("zusatzfeld." + counter + ".value", b);
							if (b)
							{
								selcounter++;
							}
							break;
						}
						case Datentyp.WAEHRUNG:
						{
							if (inp.getValue() != null)
							{
								settings.setAttribute("zusatzfeld." + counter + ".value",
										Einstellungen.DECIMALFORMAT.format(inp.getValue()));
								selcounter++;
							}
							else
							{
								String s = null;
								settings.setAttribute("zusatzfeld." + counter + ".value", s);
							}
							break;
						}
					}
				}
				settings.setAttribute("zusatzfelder.selected", selcounter);
				close();
			}
		});
		buttons.paint(parent);
	}

	@Override
	protected Object getData()
	{
		return null;
	}

	public void reset()
	{
		int counter = 0;
		for (Input f : felder)
		{
			counter++;
			if (f instanceof CheckboxInput)
			{
				settings.setAttribute("zusatzfeld." + counter + ".value", "false");
				f.setValue(false);
			}
			if (f instanceof IntegerInput)
			{
				settings.setAttribute("zusatzfeld." + counter + ".value", -1);
				f.setValue(-1);
			}
			else if (f instanceof DecimalInput)
			{
				settings.setAttribute("zusatzfeld." + counter + ".value", "");
				f.setValue(null);
			}
			else if (f instanceof DateInput)
			{
				settings.setAttribute("zusatzfeld." + counter + ".value", "");
				f.setValue(null);
			}
			else
			{
				settings.setAttribute("zusatzfeld." + counter + ".value", "");
				f.setValue("");
			}
		}
		settings.setAttribute("zusatzfelder.selected", 0);

	}
}
