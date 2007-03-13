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
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Datum des Zahlungseingangs löschen.
 */
public class ManuellerZahlungseingangDatumLöschenAction implements Action
{
  private TablePart table;

  public ManuellerZahlungseingangDatumLöschenAction(TablePart table)
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
      int ind = table.removeItem(mz);
      mz.setEingangsdatum(null);
      mz.store();
      table.addItem(mz, ind);

      GUI.getStatusBar().setSuccessText("Datum entfernt.");
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler beim entfernen des Zahlungseingangsdatums";
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
