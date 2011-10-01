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
package de.jost_net.JVerein.gui.dialogs;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
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
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.Settings;

public class ZusatzfelderAuswahlDialog extends AbstractDialog
{

  private ArrayList<Input> felder;

  private ArrayList<Object> retval = new ArrayList<Object>();

  private Settings settings;

  public ZusatzfelderAuswahlDialog(Settings settings)
  {
    super(EigenschaftenAuswahlDialog.POSITION_CENTER);
    this.setSize(400, 700);
    setTitle(JVereinPlugin.getI18n().tr("Zusatzfelder-Bedingungen "));
    this.settings = settings;
  }

  @Override
  protected void paint(Composite parent) throws RemoteException
  {
    LabelGroup group = new LabelGroup(parent, JVereinPlugin.getI18n().tr(
        "Bedingungen"), false);
    felder = new ArrayList<Input>();

    DBIterator it = Einstellungen.getDBService().createList(
        Felddefinition.class);
    int counter = 0;
    while (it.hasNext())
    {
      Felddefinition fd = (Felddefinition) it.next();
      switch (fd.getDatentyp())
      {
        case Datentyp.ZEICHENFOLGE:
        {
          TextInput input = new TextInput("", fd.getLaenge());
          input.setName(fd.getLabel());
          felder.add(input);
          group.addInput(input);
          counter++;
          settings
              .setAttribute("zusatzfeld." + counter + ".name", fd.getName());
          settings.setAttribute("zusatzfeld." + counter + ".cond", "LIKE");
          settings.setAttribute("zusatzfeld." + counter + ".datentyp",
              fd.getDatentyp());
          settings.setAttribute("zusatzfeld." + counter + ".definition",
              fd.getID());
          input.setValue(settings.getString("zusatzfeld." + counter + ".value",
              ""));
          break;
        }
        case Datentyp.DATUM:
        {
          DateInput inputvon = new DateInput();
          inputvon.setName(fd.getLabel() + " von");
          felder.add(inputvon);
          group.addInput(inputvon);
          counter++;
          settings
              .setAttribute("zusatzfeld." + counter + ".name", fd.getName());
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
            }
          }
          DateInput inputbis = new DateInput();
          inputbis.setName(fd.getLabel() + " bis");
          felder.add(inputbis);
          group.addInput(inputbis);
          counter++;
          settings
              .setAttribute("zusatzfeld." + counter + ".name", fd.getName());
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
            }
          }
          break;
        }
        case Datentyp.GANZZAHL:
        {
          TextInput inputvon = new IntegerInput(-1);
          inputvon.setName(fd.getLabel() + " von");
          felder.add(inputvon);
          group.addInput(inputvon);
          counter++;
          settings
              .setAttribute("zusatzfeld." + counter + ".name", fd.getName());
          settings.setAttribute("zusatzfeld." + counter + ".cond", ">=");
          settings.setAttribute("zusatzfeld." + counter + ".datentyp",
              fd.getDatentyp());
          settings.setAttribute("zusatzfeld." + counter + ".definition",
              fd.getID());
          inputvon.setValue(settings.getInt("zusatzfeld." + counter + ".value",
              -1));

          TextInput inputbis = new IntegerInput(-1);
          inputbis.setName(fd.getLabel() + " bis");
          felder.add(inputbis);
          group.addInput(inputbis);
          counter++;
          settings
              .setAttribute("zusatzfeld." + counter + ".name", fd.getName());
          settings.setAttribute("zusatzfeld." + counter + ".cond", "<=");
          settings.setAttribute("zusatzfeld." + counter + ".datentyp",
              fd.getDatentyp());
          settings.setAttribute("zusatzfeld." + counter + ".definition",
              fd.getID());
          inputbis.setValue(settings.getInt("zusatzfeld." + counter + ".value",
              -1));
          break;
        }
        case Datentyp.JANEIN:
        {
          CheckboxInput input = new CheckboxInput(false);
          input.setName(fd.getLabel());
          felder.add(input);
          group.addInput(input);
          counter++;
          settings
              .setAttribute("zusatzfeld." + counter + ".name", fd.getName());
          settings.setAttribute("zusatzfeld." + counter + ".cond", "=");
          settings.setAttribute("zusatzfeld." + counter + ".datentyp",
              fd.getDatentyp());
          settings.setAttribute("zusatzfeld." + counter + ".definition",
              fd.getID());
          input.setValue(settings.getBoolean(
              "zusatzfeld." + counter + ".value", false));
          break;
        }
        case Datentyp.WAEHRUNG:
        {
          DecimalInput inputvon = new DecimalInput(Einstellungen.DECIMALFORMAT);
          inputvon.setName(fd.getLabel() + " von");
          felder.add(inputvon);
          group.addInput(inputvon);
          counter++;
          settings
              .setAttribute("zusatzfeld." + counter + ".name", fd.getName());
          settings.setAttribute("zusatzfeld." + counter + ".cond", ">=");
          settings.setAttribute("zusatzfeld." + counter + ".datentyp",
              fd.getDatentyp());
          settings.setAttribute("zusatzfeld." + counter + ".definition",
              fd.getID());
          try
          {
            String value = settings.getString("zusatzfeld." + counter
                + ".value", "");

            if (value.length() > 0)
            {
              Number n = Einstellungen.DECIMALFORMAT.parse(value);
              inputvon.setValue(n.doubleValue());
            }
          }
          catch (ParseException e)
          {
          }

          DecimalInput inputbis = new DecimalInput(Einstellungen.DECIMALFORMAT);
          inputbis.setName(fd.getLabel() + " bis");
          felder.add(inputbis);
          group.addInput(inputbis);
          counter++;
          settings
              .setAttribute("zusatzfeld." + counter + ".name", fd.getName());
          settings.setAttribute("zusatzfeld." + counter + ".cond", "<=");
          settings.setAttribute("zusatzfeld." + counter + ".datentyp",
              fd.getDatentyp());
          settings.setAttribute("zusatzfeld." + counter + ".definition",
              fd.getID());
          inputbis.setValue(settings.getString("zusatzfeld." + counter
              + ".value", ""));
          break;
        }
      }
      settings.setAttribute("zusatzfelder.counter", counter);
    }

    ButtonArea buttons = new ButtonArea(parent, 2);
    buttons.addButton(i18n.tr(JVereinPlugin.getI18n().tr("OK")), new Action()
    {

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
              settings.setAttribute("zusatzfeld." + counter + ".value",
                  (Boolean) b);
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
                settings
                    .setAttribute("zusatzfeld." + counter + ".value",
                        Einstellungen.DECIMALFORMAT.format((Double) inp
                            .getValue()));
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
  }

  @Override
  protected Object getData()
  {
    return retval;
  }
}
