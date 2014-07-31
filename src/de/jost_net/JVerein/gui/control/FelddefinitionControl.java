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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.FelddefinitionDetailAction;
import de.jost_net.JVerein.gui.formatter.DatentypFormatter;
import de.jost_net.JVerein.gui.menu.FelddefinitionMenu;
import de.jost_net.JVerein.keys.Datentyp;
import de.jost_net.JVerein.rmi.Felddefinition;
import de.jost_net.JVerein.rmi.Zusatzfelder;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.IntegerInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Column;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class FelddefinitionControl extends AbstractControl
{

  private TablePart felddefinitionList;

  private Input name;

  private Input label;

  private SelectInput datentyp;

  private IntegerInput laenge;

  private Felddefinition felddefinition;

  public FelddefinitionControl(AbstractView view)
  {
    super(view);
  }

  private Felddefinition getFelddefinition()
  {
    if (felddefinition != null)
    {
      return felddefinition;
    }
    felddefinition = (Felddefinition) getCurrentObject();
    return felddefinition;
  }

  public Input getName(boolean withFocus) throws RemoteException
  {
    if (name != null)
    {
      return name;
    }
    name = new TextInput(getFelddefinition().getName(), 50);
    name.setMandatory(true);
    if (withFocus)
    {
      name.focus();
    }
    return name;
  }

  public Input getLabel() throws RemoteException
  {
    if (label != null)
    {
      return label;
    }
    label = new TextInput(getFelddefinition().getLabel(), 50);
    label.setMandatory(true);
    return label;
  }

  public SelectInput getDatentyp() throws RemoteException
  {
    if (datentyp != null)
    {
      return datentyp;
    }
    datentyp = new SelectInput(Datentyp.getArray(), new Datentyp(
        getFelddefinition().getDatentyp()));
    return datentyp;
  }

  public IntegerInput getLaenge() throws RemoteException
  {
    if (laenge != null)
    {
      return laenge;
    }
    laenge = new IntegerInput(getFelddefinition().getLaenge());
    laenge.setMandatory(true);
    return laenge;
  }

  public void handleStore()
  {
    try
    {
      Felddefinition f = getFelddefinition();
      Datentyp d = (Datentyp) getDatentyp().getValue();
      konvertiereTyp(true, f, d);
      konvertiereTyp(false, f, d);
      f.setName((String) getName(true).getValue());
      f.setLabel((String) getLabel().getValue());
      f.setDatentyp(d.getKey());
      Integer i = (Integer) getLaenge().getValue();
      f.setLaenge(i.intValue());
      f.store();
      GUI.getStatusBar().setSuccessText("Felddefinition gespeichert");
    }
    catch (ApplicationException e)
    {
      GUI.getStatusBar().setErrorText(e.getMessage());
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei speichern der Felddefinition: "
          + e.getLocalizedMessage();
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  public TablePart getFelddefinitionTable() throws RemoteException
  {
    if (felddefinitionList != null)
    {
      return felddefinitionList;
    }
    DBService service = Einstellungen.getDBService();
    DBIterator fdef = service.createList(Felddefinition.class);
    felddefinitionList = new TablePart(fdef, new FelddefinitionDetailAction());
    felddefinitionList.addColumn("Name", "name");
    felddefinitionList.addColumn("Label", "label");
    felddefinitionList.addColumn("Datentyp", "datentyp",
        new DatentypFormatter(), false, Column.ALIGN_LEFT);
    felddefinitionList.addColumn("Länge", "laenge");
    felddefinitionList.setContextMenu(new FelddefinitionMenu());
    return felddefinitionList;
  }

  private void konvertiereTyp(boolean checkOnly, Felddefinition f, Datentyp d)
      throws RemoteException, ApplicationException
  {
    // Felddefinition enthält den bisherigen Stand, Datentyp ist die neue
    // Auswahl

    // Bei neuen Zusatzfeldern ist keine Prüfung möglich/erforderlich
    if (f.isNewObject())
    {
      return;
    }
    // Gibt es eine Veränderung? Wenn nicht: Ende der Prüfung
    if (f.getDatentyp() == d.getKey())
    {
      return;
    }
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    DBIterator it = Einstellungen.getDBService().createList(Zusatzfelder.class);
    it.addFilter("felddefinition = ?", new Object[] { f.getID()});
    switch (f.getDatentyp())
    {
      case Datentyp.ZEICHENFOLGE:
        it.addFilter("feld is not null");
        break;
      case Datentyp.DATUM:
        it.addFilter("felddatum is not null");
        break;
      case Datentyp.GANZZAHL:
        it.addFilter("feldganzzahl is not null");
        break;
      case Datentyp.JANEIN:
        it.addFilter("feldjanein is not null");
        break;
      case Datentyp.WAEHRUNG:
        it.addFilter("feldwaehrung is not null");
        break;
    }
    // Wenn das Zusatzfeld noch bei keinem Mitglied angelegt wurde, kann der Typ
    // generell geändert werden.
    if (it.size() == 0)
    {
      return;
    }
    switch (f.getDatentyp())
    {
      case Datentyp.ZEICHENFOLGE:
        switch (d.getKey())
        {
          case Datentyp.DATUM:
            while (it.hasNext())
            {
              Zusatzfelder z = (Zusatzfelder) it.next();
              try
              {
                Date datum = sdf.parse(z.getFeld());
                if (!checkOnly)
                {
                  Zusatzfelder z1 = (Zusatzfelder) Einstellungen.getDBService().createObject(
                      Zusatzfelder.class, z.getID());
                  z1.setFeldDatum(datum);
                  z1.setFeld(null);
                  z1.store();
                }
              }
              catch (ParseException e)
              {
                fehlermeldung(1, z.getFeld());
              }
            }
            break;
          case Datentyp.GANZZAHL:
            while (it.hasNext())
            {
              Zusatzfelder z = (Zusatzfelder) it.next();
              try
              {
                int i = Integer.parseInt(z.getFeld());
                if (!checkOnly)
                {
                  Zusatzfelder z1 = (Zusatzfelder) Einstellungen.getDBService().createObject(
                      Zusatzfelder.class, z.getID());
                  z1.setFeldGanzzahl(i);
                  z1.setFeld(null);
                  z1.store();
                }
              }
              catch (NumberFormatException e)
              {
                fehlermeldung(1, z.getFeld());
              }
            }
            break;
          case Datentyp.JANEIN:
            while (it.hasNext())
            {
              Zusatzfelder z = (Zusatzfelder) it.next();
              String b = z.getFeld();
              Boolean bool = null;
              if (b == null || b.equals("") || b.equalsIgnoreCase("false")
                  || b.equalsIgnoreCase("nein"))
              {
                bool = Boolean.valueOf(false);
              }
              else if (b.equalsIgnoreCase("true") || b.equalsIgnoreCase("ja"))
              {
                bool = Boolean.valueOf(true);
              }
              else
              {
                fehlermeldung(1, z.getFeld());
              }
              if (!checkOnly)
              {
                Zusatzfelder z1 = (Zusatzfelder) Einstellungen.getDBService().createObject(
                    Zusatzfelder.class, z.getID());
                z1.setFeldJaNein(bool);
                z1.setFeld(null);
                z1.store();
              }

            }
            break;
          case Datentyp.WAEHRUNG:
            while (it.hasNext())
            {
              Zusatzfelder z = (Zusatzfelder) it.next();
              try
              {
                Number doub = Einstellungen.DECIMALFORMAT.parse(z.getFeld());
                if (!checkOnly)
                {
                  Zusatzfelder z1 = (Zusatzfelder) Einstellungen.getDBService().createObject(
                      Zusatzfelder.class, z.getID());
                  z1.setFeldWaehrung(new BigDecimal(doub.doubleValue()));
                  z1.setFeld(null);
                  z1.store();
                }
              }
              catch (ParseException e)
              {
                fehlermeldung(1, z.getFeld());
              }
            }
            break;
        }
        break;
      case Datentyp.DATUM:
        switch (d.getKey())
        {
          case Datentyp.ZEICHENFOLGE:
            while (it.hasNext())
            {
              Zusatzfelder z = (Zusatzfelder) it.next();
              if (!checkOnly)
              {
                Zusatzfelder z1 = (Zusatzfelder) Einstellungen.getDBService().createObject(
                    Zusatzfelder.class, z.getID());
                z1.setFeld(new JVDateFormatTTMMJJJJ().format(z.getFeldDatum()));
                z1.setFeldDatum(null);
                z1.store();
              }
            }
            return;
          default:
            fehlermeldung(2, "");
        }
        break;
      case Datentyp.GANZZAHL:
        switch (d.getKey())
        {
          case Datentyp.ZEICHENFOLGE:
            while (it.hasNext())
            {
              Zusatzfelder z = (Zusatzfelder) it.next();
              if (!checkOnly)
              {
                Zusatzfelder z1 = (Zusatzfelder) Einstellungen.getDBService().createObject(
                    Zusatzfelder.class, z.getID());
                z1.setFeld(z.getFeldGanzzahl() + "");
                z1.setFeldGanzzahl(null);
                z1.store();
              }
            }
            return;
          default:
            fehlermeldung(2, "");
        }
        break;
      case Datentyp.JANEIN:
        switch (d.getKey())
        {
          case Datentyp.ZEICHENFOLGE:
            while (it.hasNext())
            {
              Zusatzfelder z = (Zusatzfelder) it.next();
              if (!checkOnly)
              {
                Zusatzfelder z1 = (Zusatzfelder) Einstellungen.getDBService().createObject(
                    Zusatzfelder.class, z.getID());
                z1.setFeld(z.getFeldJaNein() ? "ja" : "nein");
                z1.setFeldJaNein(null);
                z1.store();
              }
            }
            return;
          default:
            fehlermeldung(2, "");
        }
        break;
      case Datentyp.WAEHRUNG:
        switch (d.getKey())
        {
          case Datentyp.ZEICHENFOLGE:
            while (it.hasNext())
            {
              Zusatzfelder z = (Zusatzfelder) it.next();
              if (!checkOnly)
              {
                Zusatzfelder z1 = (Zusatzfelder) Einstellungen.getDBService().createObject(
                    Zusatzfelder.class, z.getID());
                z1.setFeld(Einstellungen.DECIMALFORMAT.format(z.getFeldWaehrung()));
                z1.setFeldWaehrung(null);
                z1.store();
              }
            }
            return;
          default:
            fehlermeldung(2, "");
        }
        break;
    }

  }

  private void fehlermeldung(int fehlertyp, String wert) throws RemoteException
  {
    switch (fehlertyp)
    {
      case 1:
        throw new RemoteException(
            "Typkonvertierung kann nicht durchgeführt werden. Inhalt: }" + wert);
      case 2:
        throw new RemoteException(
            "Die Konvertierung der Datentypen ist nicht vorgesehen. Ggfls. zunächst in Zeichenfolge umwandeln.");
    }
  }

}
