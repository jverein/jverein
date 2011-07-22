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
 * Revision 1.1  2011-05-05 19:50:20  jost
 * Neu: Istbuchungen können vom Mitgliedskonto gelöst werden.
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.Messaging.MitgliedskontoMessage;
import de.jost_net.JVerein.gui.control.MitgliedskontoNode;
import de.jost_net.JVerein.rmi.Buchung;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class MitgliedskontoIstLoesenAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    YesNoDialog d = new YesNoDialog(YesNoDialog.POSITION_CENTER);
    d.setTitle("Istbuchung vom Mitgliedskonto lösen");
    d.setText("Wollen Sie die Istbuchung wirklich vom Mitgliedskonto lösen?");

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
      Logger.error("Fehler beim lösen der Istbuchung vom Mitgliedskonto", e);
      return;
    }
    MitgliedskontoNode mkn = null;
    Buchung bu = null;

    if (context != null && (context instanceof MitgliedskontoNode))
    {
      mkn = (MitgliedskontoNode) context;
      try
      {
        bu = (Buchung) Einstellungen.getDBService().createObject(Buchung.class,
            mkn.getID());
        bu.setMitgliedskonto(null);
        bu.store();
        GUI.getStatusBar().setSuccessText(
            "Istbuchung vom Mitgliedskonto gelöst.");
        Application.getMessagingFactory().sendMessage(
                new MitgliedskontoMessage(mkn.getMitglied()));
      }
      catch (RemoteException e)
      {
        throw new ApplicationException(
            "Fehler beim lösen der Istbuchung vom Mitgliedskonto");
      }
    }
  }
}
