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
 * Revision 1.3  2009/11/26 19:50:32  jost
 * *** empty log message ***
 *
 * Revision 1.2  2009/06/11 21:02:05  jost
 * Vorbereitung I18N
 *
 * Revision 1.1  2007/03/13 19:56:20  jost
 * Neu: Manueller Zahlungseingang.
 *
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.ManuellerZahlungseingang;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Loeschen eines ManuellenZahlungseingangs.
 * @deprecated In Version 1.5 ausmustern
 */
public class ManuellerZahlungseingangDeleteAction implements Action
{
  private TablePart table;

  public ManuellerZahlungseingangDeleteAction(TablePart table)
  {
    this.table = table;
  }

  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null)
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Keinen ManuellenZahlungseingang ausgewählt"));
    }

    Object[] objects = null;

    if (context instanceof Object[])
    {
      objects = (Object[]) context;
    }
    else
    {
      objects = new Object[] { context };
    }

    try
    {
      ManuellerZahlungseingang mz = (ManuellerZahlungseingang) objects[0];
      if (mz.isNewObject())
      {
        return;
      }
      YesNoDialog d = new YesNoDialog(YesNoDialog.POSITION_CENTER);
      d.setTitle(JVereinPlugin.getI18n().tr("Posten löschen"));
      d.setText(JVereinPlugin.getI18n().tr(
          "Wollen Sie diese(n) Posten wirklich löschen?"));

      try
      {
        Boolean choice = (Boolean) d.open();
        if (!choice.booleanValue())
          return;
      }
      catch (Exception e)
      {
        Logger.error(JVereinPlugin.getI18n().tr(
            "Fehler beim Löschen des ManuellenZahlungseingangs"), e);
        return;
      }
      for (Object cont : objects)
      {
        mz = (ManuellerZahlungseingang) cont;
        table.removeItem(mz);
        mz.delete();
      }
      GUI.getStatusBar().setSuccessText(
          JVereinPlugin.getI18n().tr("Posten gelöscht."));
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Fehler beim Löschen des manuellen Zahlungseingangs");
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
