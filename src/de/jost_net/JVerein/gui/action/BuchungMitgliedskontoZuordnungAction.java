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

package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.control.BuchungsControl;
import de.jost_net.JVerein.gui.dialogs.MitgliedskontoAuswahlDialog;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Mitgliedskonto zuordnen.
 */
public class BuchungMitgliedskontoZuordnungAction implements Action
{
  private BuchungsControl control;

  public BuchungMitgliedskontoZuordnungAction(BuchungsControl control)
  {
    this.control = control;
  }

  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null || !(context instanceof Buchung)
        && !(context instanceof Buchung[]))
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Keine Buchung(en) ausgewählt"));
    }
    try
    {
      Buchung[] b = null;
      if (context instanceof Buchung)
      {
        b = new Buchung[1];
        b[0] = (Buchung) context;
      }
      if (context instanceof Buchung[])
      {
        b = (Buchung[]) context;
      }
      if (b != null && b.length > 0 && b[0].isNewObject())
      {
        return;
      }
      try
      {
        MitgliedskontoAuswahlDialog mkaz = new MitgliedskontoAuswahlDialog(b[0]);
        Object open = mkaz.open();
        Mitgliedskonto mk = null;

        if (open instanceof Mitgliedskonto)
        {
          mk = (Mitgliedskonto) open;
        }
        else if (open instanceof Mitglied)
        {
          Mitglied m = (Mitglied) open;
          mk = (Mitgliedskonto) Einstellungen.getDBService().createObject(
              Mitgliedskonto.class, null);

          Double betrag = 0d;
          for (Buchung buchung : b)
          {
            betrag += buchung.getBetrag();
          }

          mk.setBetrag(betrag);
          mk.setDatum(b[0].getDatum());
          mk.setMitglied(m);
          mk.setZahlungsweg(Zahlungsweg.ÜBERWEISUNG);
          mk.setZweck1(b[0].getZweck());
          mk.setZweck2(b[0].getZweck2());
          mk.store();
        }

        if (mk == null)
        {
          GUI.getStatusBar().setErrorText(
              JVereinPlugin.getI18n().tr(
                  "Fehler bei der Ermittlung des Mitgliedskontos"));
        }

        for (Buchung buchung : b)
        {
          buchung.setMitgliedskonto(mk);
          buchung.store();
        }
        control.getBuchungsList();
        GUI.getStatusBar().setSuccessText(
            JVereinPlugin.getI18n().tr("Mitgliedskonto zugeordnet"));
      }
      catch (Exception e)
      {
        Logger.error("Fehler", e);
        GUI.getStatusBar().setErrorText(
            JVereinPlugin.getI18n().tr(
                "Fehler bei der Zuordnung des Mitgliedskontos"));
        return;
      }
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr("Fehler beim Speichern.");
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
