/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * This program is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,  but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See 
 *  the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, 
 * see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de
 * www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Abrechnungslauf;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Jahresabschluss;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Löschen eines Abrechnungslaufes
 */
public class AbrechnungslaufDeleteAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null || !(context instanceof Abrechnungslauf))
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Keinen Abrechnungslauf ausgewählt"));
    }
    try
    {
      Abrechnungslauf abrl = (Abrechnungslauf) context;
      if (abrl.isNewObject())
      {
        return;
      }

      YesNoDialog d = new YesNoDialog(YesNoDialog.POSITION_CENTER);
      d.setTitle(JVereinPlugin.getI18n().tr(
          "Abrechnungslauf " + abrl.getID() + " löschen"));
      d.setText(JVereinPlugin.getI18n().tr(
          "Wollen Sie diesen Abrechnungslauf wirklich löschen?"));

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
                "Fehler beim Löschen eines Abrechnungslaufes"), e);
        return;
      }
      DBIterator it = Einstellungen.getDBService().createList(Buchung.class);
      it.addFilter("abrechnungslauf = ?", new Object[] { abrl.getID() });
      while (it.hasNext())
      {
        Buchung bu = (Buchung) it.next();
        Buchung b = (Buchung) Einstellungen.getDBService().createObject(
            Buchung.class, bu.getID());
        Jahresabschluss ja = b.getJahresabschluss();
        if (ja != null)
        {
          throw new ApplicationException(JVereinPlugin.getI18n().tr(
              "Buchung wurde bereits am {0} von {1} abgeschlossen.",
              new String[] { new JVDateFormatTTMMJJJJ().format(ja.getDatum()),
                  ja.getName() }));
        }
        b.setMitgliedskontoID(null);
        b.store();
        b.delete();
      }
      it = Einstellungen.getDBService().createList(Mitgliedskonto.class);
      it.addFilter("abrechnungslauf = ?", new Object[] { abrl.getID() });
      while (it.hasNext())
      {
        Mitgliedskonto mkt = (Mitgliedskonto) it.next();
        DBIterator it2 = Einstellungen.getDBService().createList(Buchung.class);
        it2.addFilter("mitgliedskonto = ?", new Object[] { mkt.getID() });
        while (it2.hasNext())
        {
          Buchung bu = (Buchung) it2.next();
          Buchung b = (Buchung) Einstellungen.getDBService().createObject(
              Buchung.class, bu.getID());
          b.setMitgliedskontoID(null);
          b.store();
        }
        Mitgliedskonto mk = (Mitgliedskonto) Einstellungen.getDBService()
            .createObject(Mitgliedskonto.class, mkt.getID());
        mk.delete();
      }

      abrl.delete();
      GUI.getStatusBar().setSuccessText(
          JVereinPlugin.getI18n().tr("Abrechnungslauf gelöscht."));
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Fehler beim Löschen eines Abrechnungslaufes");
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
