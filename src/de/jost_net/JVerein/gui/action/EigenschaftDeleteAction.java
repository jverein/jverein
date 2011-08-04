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
 * Revision 1.3  2011-03-13 18:29:17  jost
 * redakt. Kommentare
 *
 * Revision 1.2  2009/11/23 20:38:36  jost
 * Bugfix Lösch-Button
 *
 * Revision 1.1  2009/11/17 20:54:14  jost
 * Neu: Eigenschaft und EigenschaftGruppe
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Eigenschaft;
import de.jost_net.JVerein.rmi.Eigenschaften;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Löschen einer Eigenschaft.
 */
public class EigenschaftDeleteAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    if (context instanceof TablePart)
    {
      TablePart tp = (TablePart) context;
      context = tp.getSelection();
    }
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
      DBIterator it = Einstellungen.getDBService().createList(
          Eigenschaften.class);
      it.addFilter("eigenschaft = ?", new Object[] { ei.getID() });
      d.setText(JVereinPlugin
          .getI18n()
          .tr("Wollen Sie diese Eigenschaft wirklich löschen? Sie ist noch mit {0} Mitglied(ern) verknüpft.",
              it.size() + ""));
      try
      {
        Boolean choice = (Boolean) d.open();
        if (!choice.booleanValue())
          return;
      }
      catch (Exception e)
      {
        Logger.error(
            JVereinPlugin.getI18n().tr("Fehler beim Löschen der Eigenschaft"),
            e);
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
