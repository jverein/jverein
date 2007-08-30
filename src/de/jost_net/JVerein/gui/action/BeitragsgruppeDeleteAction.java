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
 * Revision 1.1  2007/08/23 19:24:05  jost
 * Bug #11819 - Beitragsgruppen kÃ¶nnen jetzt gelÃ¶scht werden
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Loeschen einer Beitragsgruppe
 */
public class BeitragsgruppeDeleteAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    if (context instanceof TablePart)
    {
      TablePart tp = (TablePart) context;
      context = tp.getSelection();
      System.out.println("##>" + context);
    }
    if (context == null || !(context instanceof Beitragsgruppe))
    {
      throw new ApplicationException("Keine Beitragsgruppe ausgewählt");
    }
    try
    {
      Beitragsgruppe bg = (Beitragsgruppe) context;
      if (bg.isNewObject())
      {
        return;
      }
      YesNoDialog d = new YesNoDialog(YesNoDialog.POSITION_CENTER);
      d.setTitle("Beitragsgruppe löschen");
      d.setText("Wollen Sie diese Beitragsgruppe wirklich löschen?");

      try
      {
        Boolean choice = (Boolean) d.open();
        if (!choice.booleanValue())
          return;
      }
      catch (Exception e)
      {
        Logger.error("Fehler beim Löschen der Beitragsgruppe", e);
        return;
      }
      bg.delete();
      GUI.getStatusBar().setSuccessText("Beitragsgruppe gelöscht.");
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler beim Löschen der Beitragsgruppe";
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
