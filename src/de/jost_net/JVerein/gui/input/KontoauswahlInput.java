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

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.dialogs.KontoAuswahlDialog;
import de.jost_net.JVerein.rmi.Konto;
import de.willuhn.datasource.rmi.ObjectNotFoundException;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.DialogInput;
import de.willuhn.logging.Logger;

public class KontoauswahlInput
{

  private DialogInput kontoAuswahl = null;

  private Konto konto;

  private de.willuhn.jameica.system.Settings settings;

  private boolean keinkonto;

  public KontoauswahlInput()
  {
    this(null);
  }

  /**
   * 
   * @param konto
   */
  public KontoauswahlInput(Konto konto)
  {
    this.konto = konto;
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  /**
   * Liefert ein Auswahlfeld fuer das Konto.
   * 
   * @param keinkonto
   *          true= ein kann auch kein Konto ausgewählt werden. false = es muss
   *          ein Konto ausgewählt werden.
   * 
   * @return Auswahl-Feld.
   * @throws RemoteException
   */
  public DialogInput getKontoAuswahl(boolean keinkonto, String kontoid,
      boolean onlyHibiscus, boolean nurAktuelleKunden) throws RemoteException
  {
    if (kontoAuswahl != null)
    {
      return kontoAuswahl;
    }
    this.keinkonto = keinkonto;
    KontoAuswahlDialog d = new KontoAuswahlDialog(
        KontoAuswahlDialog.POSITION_MOUSE, keinkonto, onlyHibiscus,
        nurAktuelleKunden);
    d.addCloseListener(new KontoListener());

    if (kontoid == null || kontoid.length() == 0)
    {
      kontoid = null;
    }
    if (konto == null && kontoid != null)
    {
      try
      {
        konto = (Konto) Einstellungen.getDBService().createObject(Konto.class,
            kontoid);
      }
      catch (ObjectNotFoundException e)
      {
        settings.setAttribute("kontoid", "");
      }
    }

    kontoAuswahl = new DialogInput(konto == null ? "" : konto.getNummer(), d);
    kontoAuswahl.setComment(konto == null ? "" : konto.getBezeichnung());
    kontoAuswahl.disableClientControl();
    kontoAuswahl.setValue(konto);
    kontoAuswahl.setMandatory(true);
    return kontoAuswahl;
  }

  /**
   * Listener, der die Auswahl des Kontos ueberwacht und die
   * Waehrungsbezeichnung hinter dem Betrag abhaengig vom ausgewaehlten Konto
   * anpasst.
   */
  private class KontoListener implements Listener
  {

    /**
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    @Override
    public void handleEvent(Event event)
    {

      if (event == null || event.data == null)
      {
        if (keinkonto)
        {
          konto = null;
          try
          {
            // TODO warum wird das doppelt aufgerufen?
            getKontoAuswahl(keinkonto, "", false, false).setText("");
            getKontoAuswahl(keinkonto, "", false, false).setComment("");
            settings.setAttribute("kontoid", "");
          }
          catch (RemoteException e)
          {
            Logger.error("Fehler", e);
          }
        }
        return;
      }
      konto = (Konto) event.data;
      try
      {
        String b = konto.getBezeichnung();
        getKontoAuswahl(keinkonto, "", false, false).setText(konto.getNummer());
        getKontoAuswahl(keinkonto, "", false, false).setComment(
            b == null ? "" : b);
        settings.setAttribute("kontoid", konto.getID());
      }
      catch (RemoteException er)
      {
        String error = "Fehler bei der Ermittlung der Kontobezeichnung";
        Logger.error(error, er);
        GUI.getStatusBar().setErrorText(error);
      }
    }
  }

}
