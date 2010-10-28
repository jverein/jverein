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
 * Revision 1.2  2009/06/11 21:02:05  jost
 * Vorbereitung I18N
 *
 * Revision 1.1  2008/07/18 20:08:27  jost
 * Neu: Spendenbescheinigung
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.view.SpendenbescheinigungView;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Spendenbescheinigung;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class SpendenbescheinigungAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    Spendenbescheinigung spb = null;

    try
    {
      if (context != null && context instanceof Spendenbescheinigung)
      {
        spb = (Spendenbescheinigung) context;
      }
      else
      {
        spb = (Spendenbescheinigung) Einstellungen.getDBService().createObject(
            Spendenbescheinigung.class, null);

        if (context != null && (context instanceof Mitglied))
        {
          Mitglied m = (Mitglied) context;
          spb.setZeile1(m.getAnrede());
          spb.setZeile2(m.getVornameName());
          spb.setZeile3(m.getStrasse());
          spb.setZeile4(m.getPlz() + " " + m.getOrt());
          if (m.getStaat() != null && m.getStaat().length() > 0)
          {
            spb.setZeile5(m.getStaat());
          }
        }
      }
      GUI.startView(SpendenbescheinigungView.class.getName(), spb);
    }
    catch (RemoteException e)
    {
      Logger.error("Fehler", e);
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Fehler bei der Erstellung der Spendenbescheinigung"));
    }
  }
}
