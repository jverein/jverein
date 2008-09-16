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

import de.jost_net.JVerein.rmi.Abrechnung;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Loeschen einer oder mehrerer Abrechnungs-Sätze.
 */
public class RechnungDeleteAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null
        || (!(context instanceof Abrechnung) && !(context instanceof Abrechnung[])))
    {
      throw new ApplicationException("Keinen Rechnungssatz ausgewählt");
    }
    try
    {
      Abrechnung a = null;
      Abrechnung[] a2 = null;
      if (context instanceof Abrechnung)
      {
        a = (Abrechnung) context;
      }
      if (context instanceof Abrechnung[])
      {
        a2 = (Abrechnung[]) context;
      }
      String title = "Abrechnungsatz";
      if (a != null && a.isNewObject())
      {
        return;
      }
      if (a2.length > 0 && a2[0].isNewObject())
      {
        return;
      }
      if (a2.length > 0)
      {
        title = "Abrechnungssätze";
      }
      YesNoDialog d = new YesNoDialog(YesNoDialog.POSITION_CENTER);
      d.setText(title + " wirklich löschen?");
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
        Logger.error("Fehler beim Löschen des Abrechungssatzes", e);
        return;
      }

      if (a != null)
      {
        a.delete();
      }
      if (a2.length > 0)
      {
        for (Abrechnung abr : a2)
        {
          abr.delete();
        }
      }
      GUI.getStatusBar().setSuccessText(title + " gelöscht.");
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler beim Löschen.";
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
