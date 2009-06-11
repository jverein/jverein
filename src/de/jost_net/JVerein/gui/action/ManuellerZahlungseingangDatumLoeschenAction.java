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
 * Revision 1.1  2007/03/20 07:56:25  jost
 * Probleme beim Encoding.
 *
 * Revision 1.1  2007/03/13 19:55:39  jost
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
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Datum des Zahlungseingangs löschen.
 */
public class ManuellerZahlungseingangDatumLoeschenAction implements Action
{
  private TablePart table;

  public ManuellerZahlungseingangDatumLoeschenAction(TablePart table)
  {
    this.table = table;
  }

  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null || !(context instanceof ManuellerZahlungseingang))
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Keinen manuellen Zahlungseingang ausgewählt"));
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

      GUI.getStatusBar().setSuccessText(
          JVereinPlugin.getI18n().tr("Datum entfernt."));
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Fehler beim entfernen des Zahlungseingangsdatums");
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
