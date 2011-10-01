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

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Felddefinition;
import de.jost_net.JVerein.rmi.Zusatzfelder;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Löschen einer Felddefiniton
 */
public class FelddefinitionDeleteAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    if (context instanceof TablePart)
    {
      TablePart tp = (TablePart) context;
      context = tp.getSelection();
    }
    if (context == null || !(context instanceof Felddefinition))
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Keine Felddefinition ausgewählt"));
    }
    try
    {
      Felddefinition fd = (Felddefinition) context;
      DBIterator it = Einstellungen.getDBService().createList(
          Zusatzfelder.class);
      it.addFilter("felddefinition=?", new Object[] { fd.getID() });
      if (fd.isNewObject())
      {
        return;
      }
      YesNoDialog d = new YesNoDialog(YesNoDialog.POSITION_CENTER);
      d.setTitle(JVereinPlugin.getI18n().tr("Felddefinition löschen"));
      d.setText("Das Feld ist bei "
          + it.size()
          + " Mitgliedern gespeichert. Wollen Sie diese Felddefinition wirklich löschen?");

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
            JVereinPlugin.getI18n()
                .tr("Fehler beim Löschen der Felddefinition"), e);
        return;
      }
      while (it.hasNext())
      {
        Zusatzfelder zf1 = (Zusatzfelder) it.next();
        Zusatzfelder zf2 = (Zusatzfelder) Einstellungen.getDBService()
            .createObject(Zusatzfelder.class, zf1.getID());
        zf2.delete();
      }
      fd.delete();
      GUI.getStatusBar().setSuccessText(
          JVereinPlugin.getI18n().tr("Felddefinition gelöscht."));
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Fehler beim Löschen der Felddefinition");
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
