/**********************************************************************
 * basiert auf dem KontoAuswahlDialog aus Hibiscus
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
import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Konto;
import de.jost_net.JVerein.util.JVDateFormatDATETIME;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.formatter.Formatter;
import de.willuhn.jameica.gui.input.LabelInput;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.logging.Logger;

/**
 * Ein Dialog, ueber den man ein Konto auswaehlen kann.
 */
public class BuchungUebernahmeProtokollDialog extends AbstractDialog<Buchung>
{
  private ArrayList<Buchung> buchungen;

  private Buchung fehlerbuchung;

  private Exception exception;

  public BuchungUebernahmeProtokollDialog(ArrayList<Buchung> buchungen,
      Buchung fehlerbuchung, Exception exeption)
  {
    super(AbstractDialog.POSITION_CENTER);
    super.setSize(650, 400);
    this.setTitle("Buchungsübernahme Hibiscus->JVerein");
    this.buchungen = buchungen;
    this.fehlerbuchung = fehlerbuchung;
    this.exception = exeption;
  }

  @Override
  protected void paint(Composite parent) throws Exception
  {
    // LabelGroup group = new LabelGroup(parent, "Übernommene Buchungen");

    final TablePart bu = new TablePart(buchungen, null);
    bu.addColumn("Nr", "id-int");
    bu.addColumn("Konto", "konto", new Formatter()
    {

      @Override
      public String format(Object o)
      {
        Konto k = (Konto) o;
        if (k != null)
        {
          try
          {
            return k.getBezeichnung();
          }
          catch (RemoteException e)
          {
            Logger.error("Fehler", e);
          }
        }
        return "";
      }
    });
    bu.addColumn("Datum", "datum",
        new DateFormatter(new JVDateFormatTTMMJJJJ()));
    bu.addColumn("Name", "name");
    bu.addColumn("IBAN oder Kontonummer", "iban");
    bu.addColumn("Verwendungszweck", "zweck", new Formatter()
    {

      @Override
      public String format(Object value)
      {
        if (value == null)
        {
          return null;
        }
        String s = value.toString();
        s = s.replaceAll("\r\n", " ");
        s = s.replaceAll("\r", " ");
        s = s.replaceAll("\n", " ");
        return s;
      }
    });
    bu.addColumn("Betrag", "betrag", new CurrencyFormatter("",
        Einstellungen.DECIMALFORMAT));
    bu.setRememberColWidths(true);
    bu.setRememberOrder(true);
    bu.paint(parent);

    if (exception != null)
    {
      LabelGroup lbFehler = new LabelGroup(parent, "Fehler");
      if (fehlerbuchung != null)
      {
        lbFehler.addInput(new LabelInput(new JVDateFormatDATETIME()
            .format(fehlerbuchung.getDatum())));
        lbFehler.addInput(new LabelInput(fehlerbuchung.getName()));
        lbFehler.addInput(new LabelInput(fehlerbuchung.getZweck()));
        lbFehler.addInput(new LabelInput(Einstellungen.DECIMALFORMAT
            .format(fehlerbuchung.getBetrag())));
        lbFehler.addText(exception.getMessage(), true);
      }
    }

    ButtonArea b = new ButtonArea();
    b.addButton("OK", new Action()
    {
      @Override
      public void handleAction(Object context)
      {
        close();
      }
    }, null, true, "ok.png");
    b.paint(parent);
  }

  @Override
  protected Buchung getData() throws Exception
  {
    return null;
  }

}
