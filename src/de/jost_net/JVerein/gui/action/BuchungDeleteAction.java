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
 * Revision 1.6  2009/06/11 21:02:05  jost
 * Vorbereitung I18N
 *
 * Revision 1.5  2008/03/16 07:34:42  jost
 * Reaktivierung BuchfÃ¼hrung
 *
 * Revision 1.3  2007/02/23 20:25:42  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.2  2006/10/18 06:01:26  jost
 * Überflüssige Import-Statements entfernt.
 *
 * Revision 1.1  2006/10/14 16:10:44  jost
 * Buchungen löschen eingeführt
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Buchung;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Loeschen einer Buchung.
 */
public class BuchungDeleteAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null
        || (!(context instanceof Buchung) && !(context instanceof Buchung[])))
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Keine Buchung ausgewählt"));
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
      YesNoDialog d = new YesNoDialog(YesNoDialog.POSITION_CENTER);
      d.setTitle(JVereinPlugin.getI18n().tr(
          "Buchung" + (b.length > 1 ? "en" : "") + " löschen"));
      d.setText(JVereinPlugin.getI18n().tr(
          "Wollen Sie diese Buchung" + (b.length > 1 ? "en" : "")
              + " wirklich löschen?"));
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
            "Fehler beim Löschen der Buchung"), e);
        return;
      }
      for (Buchung bu : b)
      {
        bu.delete();
      }
      GUI.getStatusBar().setSuccessText(
          JVereinPlugin.getI18n().tr(
              "Buchung" + (b.length > 1 ? "en" : "") + " gelöscht."));
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Fehler beim Löschen der Buchung.");
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
