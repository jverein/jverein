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
 **********************************************************************/
package de.jost_net.JVerein.gui.parts;

import java.rmi.RemoteException;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.LehrgangAction;
import de.jost_net.JVerein.gui.menu.LehrgangMenu;
import de.jost_net.JVerein.rmi.Lehrgang;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.formatter.Formatter;
import de.willuhn.jameica.gui.parts.TablePart;

public class LehrgaengeList extends TablePart implements Part
{
  private TablePart lehrgaengeList;

  public LehrgaengeList(Action action)
  {
    super(action);
  }

  public Part getLehrgaengeList() throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator lehrgaenge = service.createList(Lehrgang.class);
    lehrgaenge.setOrder("ORDER BY von DESC");

    if (lehrgaengeList == null)
    {
      lehrgaengeList = new TablePart(lehrgaenge, new LehrgangAction(null));
      lehrgaengeList.addColumn("Name", "mitglied", new Formatter()
      {
        public String format(Object o)
        {
          Mitglied m = (Mitglied) o;
          if (m == null)
            return null;
          String name = null;
          try
          {
            name = m.getNameVorname();
          }
          catch (RemoteException e)
          {
            e.printStackTrace();
          }
          return name;
        }
      });
      lehrgaengeList.addColumn("von/am", "von", new DateFormatter(
          Einstellungen.DATEFORMAT));
      lehrgaengeList.addColumn("bis", "bis", new DateFormatter(
          Einstellungen.DATEFORMAT));
      lehrgaengeList.addColumn("Veranstalter", "veranstalter");
      lehrgaengeList.addColumn("Ergebnis", "ergebnis");
      lehrgaengeList.setContextMenu(new LehrgangMenu());
      lehrgaengeList.setRememberColWidths(true);
      lehrgaengeList.setRememberOrder(true);
      lehrgaengeList.setSummary(true);
    }
    else
    {
      lehrgaengeList.removeAll();
      while (lehrgaenge.hasNext())
      {
        lehrgaengeList.addItem((Lehrgang) lehrgaenge.next());
      }
    }
    return lehrgaengeList;
  }

  public synchronized void paint(Composite parent) throws RemoteException
  {
    super.paint(parent);
  }

}
