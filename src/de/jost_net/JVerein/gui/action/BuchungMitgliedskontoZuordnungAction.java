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

package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.control.BuchungsControl;
import de.jost_net.JVerein.gui.dialogs.MitgliedskontoAuswahlDialog;
import de.jost_net.JVerein.rmi.Buchung;
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
    if (context == null
        || (!(context instanceof Buchung) && !(context instanceof Buchung[])))
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
        Mitgliedskonto mk = (Mitgliedskonto) mkaz.open();
        for (Buchung buchung : b)
        {
          buchung.setMitgliedskonto(mk);
          buchung.store();
        }
        System.out.println(mk.getBetrag() + mk.getZweck1());
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
