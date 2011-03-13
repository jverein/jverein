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
 * Revision 1.1  2009/09/10 18:16:18  jost
 * neu: Buchungsklassen
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Buchungsklasse;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Löschen einer Buchungsklasse.
 */
public class BuchungsklasseDeleteAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null || !(context instanceof Buchungsklasse))
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Keine Buchungsklasse ausgewählt"));
    }
    try
    {
      Buchungsklasse b = (Buchungsklasse) context;
      if (b.isNewObject())
      {
        return;
      }
      YesNoDialog d = new YesNoDialog(YesNoDialog.POSITION_CENTER);
      d.setTitle(JVereinPlugin.getI18n().tr("Buchungsklasse löschen"));
      d.setText(JVereinPlugin.getI18n().tr(
          "Wollen Sie diese Buchungsklasse wirklich löschen?"));
      try
      {
        Boolean choice = (Boolean) d.open();
        if (!choice.booleanValue())
          return;
      }
      catch (Exception e)
      {
        Logger.error(JVereinPlugin.getI18n().tr(
            "Fehler beim Löschen der Buchungsklasse"), e);
        return;
      }

      b.delete();
      GUI.getStatusBar().setSuccessText(
          JVereinPlugin.getI18n().tr("Buchungsklasse gelöscht."));
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Fehler beim Löschen der Buchungsklasse.");
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
