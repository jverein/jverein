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
package de.jost_net.JVerein.io;

import java.io.IOException;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.control.MitgliedskontoControl;
import de.jost_net.JVerein.keys.FormularArt;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Formular;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;

public class Rechnungsausgabe extends AbstractMitgliedskontoDokument
{

  public Rechnungsausgabe(MitgliedskontoControl control) throws IOException
  {
    super(control, MitgliedskontoControl.TYP.RECHNUNG);
    Formular form = (Formular) control.getFormular(FormularArt.RECHNUNG)
        .getValue();
    if (form == null)
    {
      throw new IOException("kein Rechnungsformular ausgewählt");
    }
    Formular formular = (Formular) Einstellungen.getDBService()
        .createObject(Formular.class, form.getID());

    // Wurde ein Object übergeben?
    if (control.getCurrentObject() != null)
    {
      // Ja: Einzeldruck aus dem Kontextmenu
      mks = getRechnungsempfaenger(control.getCurrentObject());
    }
    else
    {
      // Nein: Sammeldruck aus der MitgliedskontoRechnungView
      DBIterator<Mitgliedskonto> it = Einstellungen.getDBService()
          .createList(Mitgliedskonto.class);
      Date d = null;
      if (control.getVondatum(control.getDatumverwendung()).getValue() != null)
      {
        d = (Date) control.getVondatum(control.getDatumverwendung()).getValue();
        if (d != null)
        {
          control.getSettings().setAttribute(
              control.getDatumverwendung() + "datumvon",
              new JVDateFormatTTMMJJJJ().format(d));
        }
        it.addFilter("datum >= ?", d);
      }
      else
      {
        control.getSettings()
            .setAttribute(control.getDatumverwendung() + "datumvon", "");
      }
      if (control.getBisdatum(control.getDatumverwendung()).getValue() != null)
      {
        d = (Date) control.getBisdatum(control.getDatumverwendung()).getValue();
        if (d != null)
        {
          control.getSettings().setAttribute(
              control.getDatumverwendung() + "datumbis",
              new JVDateFormatTTMMJJJJ().format(d));
        }
        it.addFilter("datum <= ?", d);
      }
      else
      {
        control.getSettings()
            .setAttribute(control.getDatumverwendung() + "datumbis", "");
      }
      if ((Boolean) control.getOhneAbbucher().getValue())
      {
        it.addFilter("zahlungsweg <> ?", Zahlungsweg.BASISLASTSCHRIFT);
      }

      Mitgliedskonto[] mk = new Mitgliedskonto[it.size()];
      int i = 0;
      while (it.hasNext())
      {
        mk[i] =  it.next();
        i++;
      }
      mks = getRechnungsempfaenger(mk);
    }
    aufbereitung(formular);
  }

}
