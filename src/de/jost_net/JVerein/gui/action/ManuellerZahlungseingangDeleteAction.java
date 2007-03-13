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
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.rmi.ManuellerZahlungseingang;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Loeschen eines ManuellenZahlungseingangs.
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
    if (context == null || !(context instanceof ManuellerZahlungseingang))
    {
      throw new ApplicationException(
          "Keinen ManuellenZahlungseingang ausgewählt");
    }
    try
    {
      ManuellerZahlungseingang mz = (ManuellerZahlungseingang) context;
      if (mz.isNewObject())
      {
        return;
      }
      YesNoDialog d = new YesNoDialog(YesNoDialog.POSITION_CENTER);
      d.setTitle("Posten löschen");
      d.setText("Wollen Sie diesen Posten wirklich löschen?");

      try
      {
        Boolean choice = (Boolean) d.open();
        if (!choice.booleanValue())
          return;
      }
      catch (Exception e)
      {
        Logger.error("Fehler beim Löschen des ManuellenZahlungseingangs", e);
        return;
      }
      table.removeItem(mz);
      mz.delete();
      GUI.getStatusBar().setSuccessText("Posten gelöscht.");
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler beim Löschen des ManuellenZahlungseingangs";
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
