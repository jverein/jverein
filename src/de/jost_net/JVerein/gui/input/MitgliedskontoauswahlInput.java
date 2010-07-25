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
 * $Log$
**********************************************************************/
package de.jost_net.JVerein.gui.input;

import java.rmi.RemoteException;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.dialogs.MitgliedskontoAuswahlDialog;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.DialogInput;
import de.willuhn.logging.Logger;

public class MitgliedskontoauswahlInput
{
  private DialogInput mitgliedskontoAuswahl = null;

  private Buchung buchung = null;

  private Mitgliedskonto konto = null;

  public MitgliedskontoauswahlInput()
  {
  }

  public MitgliedskontoauswahlInput(Buchung buchung) throws RemoteException
  {
    this.buchung = buchung;
    System.out.println(buchung.getName());
    this.konto = buchung.getMitgliedskonto();
  }

  /**
   * Liefert ein Auswahlfeld fuer das Mitgliedskonto.
   * 
   * @return Auswahl-Feld.
   * @throws RemoteException
   */
  public DialogInput getMitgliedskontoAuswahl() throws RemoteException
  {
    if (mitgliedskontoAuswahl != null)
    {
      return mitgliedskontoAuswahl;
    }
    MitgliedskontoAuswahlDialog d = new MitgliedskontoAuswahlDialog(
        MitgliedskontoAuswahlDialog.POSITION_MOUSE, buchung);
    d.addCloseListener(new MitgliedskontoListener());

    mitgliedskontoAuswahl = new DialogInput(konto != null ? konto.getMitglied()
        .getNameVorname()
        + ", "
        + Einstellungen.DATEFORMAT.format(konto.getDatum())
        + ", "
        + Einstellungen.DECIMALFORMAT.format(konto.getBetrag()) : "", d);
    mitgliedskontoAuswahl.disableClientControl();
    mitgliedskontoAuswahl.setValue(buchung.getMitgliedskonto());
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
    public void handleEvent(Event event)
    {

      if (event == null || event.data == null)
      {
        return;
      }
      konto = (Mitgliedskonto) event.data;

      try
      {
        String b = konto.getMitglied().getNameVorname() + ", "
            + Einstellungen.DATEFORMAT.format(konto.getDatum()) + ", "
            + Einstellungen.DECIMALFORMAT.format(konto.getBetrag());
        getMitgliedskontoAuswahl().setText(b);
      }
      catch (RemoteException er)
      {
        String error = JVereinPlugin.getI18n().tr(
            "Fehler bei des Mitgliedskontos");
        Logger.error(error, er);
        GUI.getStatusBar().setErrorText(error);
      }
    }
  }

}
