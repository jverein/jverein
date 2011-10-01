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
package de.jost_net.JVerein.gui.input;

import java.rmi.RemoteException;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
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
  public DialogInput getKontoAuswahl(boolean keinkonto, String kontoid)
      throws RemoteException
  {
    if (kontoAuswahl != null)
    {
      return kontoAuswahl;
    }
    this.keinkonto = keinkonto;
    KontoAuswahlDialog d = new KontoAuswahlDialog(
        KontoAuswahlDialog.POSITION_MOUSE, keinkonto);
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
    public void handleEvent(Event event)
    {

      if (event == null || event.data == null)
      {
        if (keinkonto)
        {
          konto = null;
          try
          {
            getKontoAuswahl(keinkonto, "").setText("");
            getKontoAuswahl(keinkonto, "").setComment("");
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
        getKontoAuswahl(keinkonto, "").setText(konto.getNummer());
        getKontoAuswahl(keinkonto, "").setComment(b == null ? "" : b);
        settings.setAttribute("kontoid", konto.getID());
      }
      catch (RemoteException er)
      {
        String error = JVereinPlugin.getI18n().tr(
            "Fehler bei der Ermittlung der Kontobezeichnung");
        Logger.error(error, er);
        GUI.getStatusBar().setErrorText(error);
      }
    }
  }

}
