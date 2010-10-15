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
 * Revision 1.4  2010-07-28 07:25:48  jost
 * deprecated
 *
 * Revision 1.3  2009/11/26 19:50:10  jost
 * Mehrfachauswahl ermöglicht.
 *
 * Revision 1.2  2009/06/11 21:02:05  jost
 * Vorbereitung I18N
 *
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
 * @deprecated In Version 1.5 ausmustern
 */
@Deprecated
public class ManuellerZahlungseingangDatumLoeschenAction implements Action
{
  private TablePart table;

  public ManuellerZahlungseingangDatumLoeschenAction(TablePart table)
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
      for (Object cont : objects)
      {
        ManuellerZahlungseingang mz = (ManuellerZahlungseingang) cont;
        if (mz.isNewObject())
        {
          return;
        }
        int ind = table.removeItem(mz);
        mz.setEingangsdatum(null);
        mz.store();
        table.addItem(mz, ind);
      }
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
