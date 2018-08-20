/**********************************************************************
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
package de.jost_net.JVerein.gui.menu;

import java.rmi.RemoteException;

import de.jost_net.JVerein.gui.action.BuchungAction;
import de.jost_net.JVerein.gui.action.BuchungBuchungsartZuordnungAction;
import de.jost_net.JVerein.gui.action.BuchungDeleteAction;
import de.jost_net.JVerein.gui.action.BuchungDuplizierenAction;
import de.jost_net.JVerein.gui.action.BuchungKontoauszugZuordnungAction;
import de.jost_net.JVerein.gui.action.BuchungMitgliedskontoZuordnungAction;
import de.jost_net.JVerein.gui.action.BuchungNeuAction;
import de.jost_net.JVerein.gui.action.BuchungProjektZuordnungAction;
import de.jost_net.JVerein.gui.action.SplitBuchungAction;
import de.jost_net.JVerein.gui.control.BuchungsControl;
import de.jost_net.JVerein.rmi.Buchung;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.CheckedSingleContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;
import de.willuhn.jameica.gui.parts.ContextMenuItem;
import de.willuhn.logging.Logger;

/**
 * Kontext-Menu zu den Buchungen.
 */
public class BuchungMenu extends ContextMenu
{
  /**
   * Erzeugt ein Kontext-Menu fuer die Liste der Buchungen.
   */

  public BuchungMenu(BuchungsControl control)
  {
    addItem(new ContextMenuItem("neue Buchung", new BuchungNeuAction(),
        "file.png"));
    addItem(new CheckedSingleContextMenuItem("bearbeiten",
        new BuchungAction(false), "edit.png"));
    addItem(new SingleBuchungItem("duplizieren", new BuchungDuplizierenAction(),
        "copy.png"));
    addItem(new SingleBuchungItem("Splitbuchung", new SplitBuchungAction(),
        "edit.png"));
    addItem(new CheckedContextMenuItem("Buchungsart zuordnen",
        new BuchungBuchungsartZuordnungAction(control), "exchange-alt.png"));
    addItem(new CheckedContextMenuItem("Mitgliedskonto zuordnen",
        new BuchungMitgliedskontoZuordnungAction(control), "exchange-alt.png"));
    addItem(new CheckedContextMenuItem("Projekt zuordnen",
        new BuchungProjektZuordnungAction(control), "exchange-alt.png"));
    addItem(new CheckedContextMenuItem("Kontoauszug zuordnen",
        new BuchungKontoauszugZuordnungAction(control), "exchange-alt.png"));
    addItem(new BuchungItem("löschen...", new BuchungDeleteAction(false),
        "trash-alt.png"));
  }

  private static class SingleBuchungItem extends CheckedSingleContextMenuItem
  {
    private SingleBuchungItem(String text, Action action, String icon)
    {
      super(text, action, icon);
    }

    @Override
    public boolean isEnabledFor(Object o)
    {
      if (o instanceof Buchung)
      {
        Buchung b = (Buchung) o;
        try
        {
          return b.getSplitId() == null;
        }
        catch (RemoteException e)
        {
          Logger.error("Fehler", e);
        }
      }
      return false;
    }
  }

  private static class BuchungItem extends CheckedContextMenuItem
  {
    private BuchungItem(String text, Action action, String icon)
    {
      super(text, action, icon);
    }

    @Override
    public boolean isEnabledFor(Object o)
    {
      try
      {
        if (o instanceof Buchung)
        {
          return ((Buchung) o).getSplitId() == null;
        }
        if (o instanceof Buchung[])
        {
          for (Buchung bu : ((Buchung[]) o))
          {
            if (bu.getSplitId() != null)
            {
              return false;
            }
          }
          return true;
        }
      }
      catch (RemoteException e)
      {
        Logger.error("Fehler", e);
      }
      return false;
    }
  }
}
