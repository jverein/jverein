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

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.control.BuchungsControl;
import de.jost_net.JVerein.gui.dialogs.BuchungsartZuordnungDialog;
import de.jost_net.JVerein.gui.dialogs.KontoauszugZuordnungDialog;
import de.jost_net.JVerein.rmi.Buchung;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Kontoauszugsinformationen zuordnen.
 */
public class BuchungKontoauszugZuordnungAction implements Action
{

  private BuchungsControl control;

  public BuchungKontoauszugZuordnungAction(BuchungsControl control)
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
        KontoauszugZuordnungDialog kaz = new KontoauszugZuordnungDialog(
            BuchungsartZuordnungDialog.POSITION_MOUSE);
        kaz.open();
        Integer auszug = kaz.getAuszugValue();
        Integer blatt = kaz.getBlattValue();
        int counter = 0;

        for (Buchung buchung : b)
        {
          boolean protect = ((buchung.getAuszugsnummer() != null && buchung
              .getAuszugsnummer().intValue() > 0) || (buchung.getBlattnummer() != null && buchung
              .getBlattnummer().intValue() > 0))
              && !kaz.getOverride();
          if (protect)
          {
            counter++;
          }
          else
          {
            buchung.setAuszugsnummer(auszug);
            buchung.setBlattnummer(blatt);
            buchung.store();
          }
        }
        control.getBuchungsList();
        String protecttext = "";
        if (counter > 0)
        {
          protecttext = JVereinPlugin.getI18n().tr(
              ", {0} Buchungen wurden nicht überschrieben. ",
              new String[] { counter + "" });
        }
        GUI.getStatusBar().setSuccessText(
            JVereinPlugin.getI18n().tr("Kontoauszugsinformationen zugeordnet")
                + protecttext);
      }
      catch (Exception e)
      {
        Logger.error("Fehler", e);
        GUI.getStatusBar().setErrorText(
            JVereinPlugin.getI18n().tr(
                "Fehler bei der Zuordnung der Kontoauszugsinformationen"));
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
