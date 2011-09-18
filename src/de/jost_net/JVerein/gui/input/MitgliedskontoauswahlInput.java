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
 * Revision 1.7  2011-04-24 09:50:53  jost
 * Automatisierte Befüllung von Istbuchungen bei der Auswahl des Mitgliedskontos.
 *
 * Revision 1.6  2011-02-26 15:54:42  jost
 * Bugfix Mitgliedskontoauswahl bei neuer Buchung, mehrfacher Mitgliedskontoauswahl
 *
 * Revision 1.5  2011-02-12 09:34:09  jost
 * Statische Codeanalyse mit Findbugs
 *
 * Revision 1.4  2011-01-08 10:45:40  jost
 * Erzeugung Sollbuchung bei Zuordnung des Mitgliedskontos
 *
 * Revision 1.3  2010-10-15 09:58:29  jost
 * Code aufgeräumt
 *
 * Revision 1.2  2010-09-12 19:59:55  jost
 * Mitgliedskontoauswahl kann rückgängig gemacht werden.
 *
 * Revision 1.1  2010-07-25 18:35:17  jost
 * Neu: Mitgliedskonto
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.input;

import java.rmi.RemoteException;
import java.util.Date;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.dialogs.MitgliedskontoAuswahlDialog;
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

  // private BuchungsControl buchungscontrol = null;

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
    if (mitgliedskontoAuswahl != null)
    {
      return mitgliedskontoAuswahl;
    }
    MitgliedskontoAuswahlDialog d = new MitgliedskontoAuswahlDialog(
        buchungen[0]);
    d.addCloseListener(new MitgliedskontoListener());

    mitgliedskontoAuswahl = new DialogInput(konto != null ? konto.getMitglied()
        .getNameVorname()
        + ", "
        + new JVDateFormatTTMMJJJJ().format(konto.getDatum())
        + ", "
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
          String error = JVereinPlugin.getI18n().tr(
              "Fehler bei des Mitgliedskontos");
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
          b = konto.getMitglied().getNameVorname() + ", "
              + new JVDateFormatTTMMJJJJ().format(konto.getDatum()) + ", "
              + Einstellungen.DECIMALFORMAT.format(konto.getBetrag());
          String name = (String) buchungen[0].getName();
          String zweck1 = (String) buchungen[0].getZweck();
          String zweck2 = (String) buchungen[0].getZweck2();
          if (name.length() == 0 && zweck1.length() == 0
              && zweck2.length() == 0)
          {
            buchungen[0].setName(konto.getMitglied().getNameVorname());
            buchungen[0].setZweck(konto.getZweck1());
            buchungen[0].setZweck2(konto.getZweck2());
            buchungen[0].setBetrag(konto.getBetrag());
            buchungen[0].setDatum(new Date());
          }
        }
        else if (event.data instanceof Mitglied)
        {
          mitglied = (Mitglied) event.data;
          b = mitglied.getNameVorname() + ", Sollbuchung erzeugen";
        }
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
