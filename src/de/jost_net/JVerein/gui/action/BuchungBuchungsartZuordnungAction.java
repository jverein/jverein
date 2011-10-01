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
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Buchungsart zuordnen.
 */
public class BuchungBuchungsartZuordnungAction implements Action
{
  private BuchungsControl control;

  public BuchungBuchungsartZuordnungAction(BuchungsControl control)
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
        BuchungsartZuordnungDialog baz = new BuchungsartZuordnungDialog(
            BuchungsartZuordnungDialog.POSITION_MOUSE);
        baz.open();
        Buchungsart ba = baz.getBuchungsart();
        int counter = 0;

        for (Buchung buchung : b)
        {
          boolean protect = buchung.getBuchungsart() != null
              && !baz.getOverride();
          if (protect)
          {
            counter++;
          }
          else
          {
            buchung.setBuchungsart(new Integer(ba.getID()));
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
            JVereinPlugin.getI18n().tr("Buchungsarten zugeordnet")
                + protecttext);
      }
      catch (Exception e)
      {
        Logger.error("Fehler", e);
        GUI.getStatusBar().setErrorText(
            JVereinPlugin.getI18n().tr(
                "Fehler bei der Zuordnung der Buchungsart"));
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
