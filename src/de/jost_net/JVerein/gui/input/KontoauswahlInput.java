package de.jost_net.JVerein.gui.input;

import java.rmi.RemoteException;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.dialogs.KontoAuswahlDialog;
import de.jost_net.JVerein.rmi.Konto;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.DialogInput;
import de.willuhn.logging.Logger;

public class KontoauswahlInput
{
  private DialogInput kontoAuswahl = null;

  private Konto konto;

  public KontoauswahlInput()
  {
    this.konto = null;
  }

  public KontoauswahlInput(Konto konto)
  {
    this.konto = konto;
  }

  /**
   * Liefert ein Auswahlfeld fuer das Konto.
   * 
   * @return Auswahl-Feld.
   * @throws RemoteException
   */
  public DialogInput getKontoAuswahl() throws RemoteException
  {
    if (kontoAuswahl != null)
    {
      return kontoAuswahl;
    }
    KontoAuswahlDialog d = new KontoAuswahlDialog(
        KontoAuswahlDialog.POSITION_MOUSE);
    d.addCloseListener(new KontoListener());

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
        return;
      }
      konto = (Konto) event.data;

      try
      {
        String b = konto.getBezeichnung();
        getKontoAuswahl().setText(konto.getNummer());
        getKontoAuswahl().setComment(b == null ? "" : b);
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
