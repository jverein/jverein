/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * basiert auf KontoList aus Hibiscus
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

package de.jost_net.JVerein.gui.parts;

import java.rmi.RemoteException;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Konto;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.parts.TablePart;

/**
 * Implementierung einer fix und fertig vorkonfigurierten Liste aller Konten.
 */
public class KontoList extends TablePart implements Part
{
  public KontoList(Action action) throws RemoteException
  {
    this(init(), action);
  }

  public KontoList(GenericIterator konten, Action action)
  {
    super(konten, action);

    addColumn(JVereinPlugin.getI18n().tr("Kontonummer"), "nummer");
    addColumn(JVereinPlugin.getI18n().tr("Bezeichnung"), "bezeichnung");
    setRememberOrder(true);
    setRememberColWidths(true);

  }

  /**
   * @see de.willuhn.jameica.gui.Part#paint(org.eclipse.swt.widgets.Composite)
   */
  @Override
  public synchronized void paint(Composite parent) throws RemoteException
  {
    super.paint(parent);
  }

  /**
   * Initialisiert die Konten-Liste.
   * 
   * @return Liste der Konten.
   * @throws RemoteException
   */
  private static DBIterator init() throws RemoteException
  {
    DBIterator i = Einstellungen.getDBService().createList(Konto.class);
    i.setOrder("ORDER BY nummer, bezeichnung");
    return i;
  }
}
