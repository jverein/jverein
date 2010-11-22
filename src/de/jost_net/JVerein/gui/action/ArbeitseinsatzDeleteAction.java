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
 * Revision 1.1  2010-11-17 04:48:49  jost
 * Erster Code zum Thema Arbeitseinsatz
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Arbeitseinsatz;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Loeschen eines Arbeitseinsatzes.
 */
public class ArbeitseinsatzDeleteAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null || !(context instanceof Arbeitseinsatz))
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Keinen Arbeitseinsatz ausgewählt"));
    }
    try
    {
      Arbeitseinsatz aeins = (Arbeitseinsatz) context;
      if (aeins.isNewObject())
      {
        return;
      }
      YesNoDialog d = new YesNoDialog(YesNoDialog.POSITION_CENTER);
      d.setTitle(JVereinPlugin.getI18n().tr("Arbeitseinsatz löschen"));
      d.setText(JVereinPlugin.getI18n().tr(
          "Wollen Sie diesen Arbeitseinsatz wirklich löschen?"));
      try
      {
        Boolean choice = (Boolean) d.open();
        if (!choice.booleanValue())
        {
          return;
        }
      }
      catch (Exception e)
      {
        Logger.error(JVereinPlugin.getI18n().tr(
            "Fehler beim Löschen des Arbeitseinsatzes"), e);
        return;
      }

      aeins.delete();
      GUI.getStatusBar().setSuccessText(
          JVereinPlugin.getI18n().tr("Arbeitseinsatz gelöscht."));
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Fehler beim Löschen des Arbeitseinsatzes.");
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
