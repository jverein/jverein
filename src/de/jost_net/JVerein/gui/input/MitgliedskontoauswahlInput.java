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
import java.util.Date;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.dialogs.MitgliedskontoAuswahlDialog;
import de.jost_net.JVerein.io.Adressbuch.Adressaufbereitung;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.DialogInput;
import de.willuhn.logging.Logger;

public class MitgliedskontoauswahlInput
{

  private DialogInput mitgliedskontoAuswahl = null;

  private Buchung[] buchungen = null;

  private Mitgliedskonto konto = null;

  private Mitglied mitglied = null;

  public MitgliedskontoauswahlInput(Buchung[] buchungen) throws RemoteException
  {
    this.buchungen = buchungen;
    this.konto = buchungen[0].getMitgliedskonto();
  }

  public MitgliedskontoauswahlInput(Buchung buchung) throws RemoteException
  {
    buchungen = new Buchung[1];
    buchungen[0] = buchung;
    this.konto = buchungen[0].getMitgliedskonto();
  }

  /**
   * Liefert ein Auswahlfeld fuer das Mitgliedskonto.
   * 
   * @return Auswahl-Feld.
   * @throws RemoteException
   */
  public DialogInput getMitgliedskontoAuswahl() throws RemoteException
  {
    if (mitgliedskontoAuswahl != null
        && !mitgliedskontoAuswahl.getControl().isDisposed())
    {
      return mitgliedskontoAuswahl;
    }
    MitgliedskontoAuswahlDialog d = new MitgliedskontoAuswahlDialog(
        buchungen[0]);
    d.addCloseListener(new MitgliedskontoListener());

    mitgliedskontoAuswahl = new DialogInput(
        konto != null ? Adressaufbereitung.getNameVorname(konto.getMitglied())
            + ", " + new JVDateFormatTTMMJJJJ().format(konto.getDatum()) + ", "
            + Einstellungen.DECIMALFORMAT.format(konto.getBetrag()) : "", d);
    mitgliedskontoAuswahl.disableClientControl();
    mitgliedskontoAuswahl.setValue(buchungen[0].getMitgliedskonto());
    return mitgliedskontoAuswahl;
  }

  /**
   * Listener, der die Auswahl des Kontos ueberwacht und die
   * Waehrungsbezeichnung hinter dem Betrag abhaengig vom ausgewaehlten Konto
   * anpasst.
   */
  private class MitgliedskontoListener implements Listener
  {

    /**
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    @Override
    public void handleEvent(Event event)
    {

      if (event == null)
      {
        return;
      }

      if (event.data == null)
      {
        try
        {
          getMitgliedskontoAuswahl().setText("");
          return;
        }
        catch (RemoteException er)
        {
          String error = "Fehler bei Auswahl des Mitgliedskontos";
          Logger.error(error, er);
          GUI.getStatusBar().setErrorText(error);
        }
      }
      try
      {
        String b = "";
        if (event.data instanceof Mitgliedskonto)
        {
          konto = (Mitgliedskonto) event.data;
          b = Adressaufbereitung.getNameVorname(konto.getMitglied()) + ", "
              + new JVDateFormatTTMMJJJJ().format(konto.getDatum()) + ", "
              + Einstellungen.DECIMALFORMAT.format(konto.getBetrag());
          String name = buchungen[0].getName();
          String zweck1 = buchungen[0].getZweck();
          if ((name == null || name.length() == 0)
              && (zweck1 == null || zweck1.length() == 0))
          {
            buchungen[0].setName(Adressaufbereitung.getNameVorname(konto
                .getMitglied()));
            buchungen[0].setZweck(konto.getZweck1());
            buchungen[0].setBetrag(konto.getBetrag());
            buchungen[0].setDatum(new Date());
          }
        }
        else if (event.data instanceof Mitglied)
        {
          mitglied = (Mitglied) event.data;
          b = Adressaufbereitung.getNameVorname(mitglied)
              + ", Sollbuchung erzeugen";
        }
        getMitgliedskontoAuswahl().setText(b);
      }
      catch (RemoteException er)
      {
        String error = "Fehler bei des Mitgliedskontos";
        Logger.error(error, er);
        GUI.getStatusBar().setErrorText(error);
      }
    }
  }
}
