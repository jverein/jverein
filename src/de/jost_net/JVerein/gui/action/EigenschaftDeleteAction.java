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
import de.jost_net.JVerein.rmi.Eigenschaft;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Loeschen einer Eigenschaft.
 */
public class EigenschaftDeleteAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null || !(context instanceof Eigenschaft))
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Keine Eigenschaft ausgewählt"));
    }
    try
    {
      Eigenschaft ei = (Eigenschaft) context;
      if (ei.isNewObject())
      {
        return;
      }
      YesNoDialog d = new YesNoDialog(YesNoDialog.POSITION_CENTER);
      d.setTitle(JVereinPlugin.getI18n().tr("Eigenschaft löschen"));
      d.setText(JVereinPlugin.getI18n().tr(
          "Wollen Sie diese Eigenschaft wirklich löschen?"));
      try
      {
        Boolean choice = (Boolean) d.open();
        if (!choice.booleanValue())
          return;
      }
      catch (Exception e)
      {
        Logger.error(JVereinPlugin.getI18n().tr(
            "Fehler beim Löschen der Eigenschaft"), e);
        return;
      }

      ei.delete();
      GUI.getStatusBar().setSuccessText(
          JVereinPlugin.getI18n().tr("Eigenschaft gelöscht."));
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Fehler beim Löschen der Eigenschaft");
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
