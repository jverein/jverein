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
 * Revision 1.3  2009/06/11 21:02:05  jost
 * Vorbereitung I18N
 *
 * Revision 1.2  2008/10/01 14:17:02  jost
 * Warnungen entfernt
 *
 * Revision 1.1  2008/07/19 19:23:39  jost
 * Neu: Spendenbescheinigung
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Spendenbescheinigung;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Loeschen einer Spendenbescheinigung
 */
public class SpendenbescheinigungDeleteAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    if (context instanceof TablePart)
    {
      TablePart tp = (TablePart) context;
      context = tp.getSelection();
    }
    if (context == null || !(context instanceof Spendenbescheinigung))
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Keine Spendenbescheinigung ausgewählt"));
    }
    try
    {
      Spendenbescheinigung spb = (Spendenbescheinigung) context;
      if (spb.isNewObject())
      {
        return;
      }

      YesNoDialog d = new YesNoDialog(YesNoDialog.POSITION_CENTER);
      d.setTitle(JVereinPlugin.getI18n().tr("Spendenbescheinigung löschen"));
      d.setText(JVereinPlugin.getI18n().tr(
          "Wollen Sie die Spendenbescheinigung wirklich löschen?"));

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
        Logger.error(
            JVereinPlugin.getI18n().tr(
                "Fehler beim Löschen der Spendenbescheinigung"), e);
        return;
      }
      DBIterator it = Einstellungen.getDBService().createList(Buchung.class);
      it.addFilter("spendenbescheinigung = ?", new Object[] { spb.getID() });
      while (it.hasNext())
      {
        Buchung bu = (Buchung) it.next();
        bu.setSpendenbescheinigungId(null);
        bu.store();
      }
      spb.delete();
      GUI.getStatusBar().setSuccessText(
          JVereinPlugin.getI18n().tr("Spendenbescheinigung gelöscht."));
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Fehler beim Löschen der Spendenbescheinigung");
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
