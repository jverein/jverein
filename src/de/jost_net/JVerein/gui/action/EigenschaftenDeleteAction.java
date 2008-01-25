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

import de.jost_net.JVerein.gui.control.EigenschaftenControl;
import de.jost_net.JVerein.rmi.Eigenschaften;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Loeschen einer Eigenschaft.
 */
public class EigenschaftenDeleteAction implements Action
{
  private EigenschaftenControl control;

  public EigenschaftenDeleteAction(EigenschaftenControl control)
  {
    this.control = control;
  }

  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null || !(context instanceof Eigenschaften))
    {
      throw new ApplicationException("Keine Eigenschaft ausgewählt");
    }
    try
    {
      Eigenschaften ei = (Eigenschaften) context;
      if (ei.isNewObject())
      {
        return;
      }
      YesNoDialog d = new YesNoDialog(YesNoDialog.POSITION_CENTER);
      d.setTitle("Eigenschaft löschen");
      d.setText("Wollen Sie diese Eigenschaft wirklich löschen?");

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
        Logger.error("Fehler beim Löschen der Eigenschaft", e);
        return;
      }
      ei.delete();
      control.refreshTable();
      GUI.getStatusBar().setSuccessText("Eigenschaft gelöscht.");
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler beim Löschen der Eigenschaft";
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
