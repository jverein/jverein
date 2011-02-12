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
 * Revision 1.3  2010-10-15 09:58:25  jost
 * Code aufgeräumt
 *
 * Revision 1.2  2009-06-11 21:03:24  jost
 * Vorbereitung I18N
 *
 * Revision 1.1  2009/04/13 11:40:03  jost
 * Neu: Lehrgänge
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.parts;

import java.rmi.RemoteException;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.LehrgangAction;
import de.jost_net.JVerein.gui.menu.LehrgangMenu;
import de.jost_net.JVerein.rmi.Lehrgang;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
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
      lehrgaengeList.addColumn(JVereinPlugin.getI18n().tr("Name"), "mitglied",
          new Formatter()
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
      lehrgaengeList.addColumn(JVereinPlugin.getI18n().tr("von/am"), "von",
          new DateFormatter(new JVDateFormatTTMMJJJJ()));
      lehrgaengeList.addColumn(JVereinPlugin.getI18n().tr("bis"), "bis",
          new DateFormatter(new JVDateFormatTTMMJJJJ()));
      lehrgaengeList.addColumn(JVereinPlugin.getI18n().tr("Veranstalter"),
          "veranstalter");
      lehrgaengeList.addColumn(JVereinPlugin.getI18n().tr("Ergebnis"),
          "ergebnis");
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
        lehrgaengeList.addItem(lehrgaenge.next());
      }
    }
    return lehrgaengeList;
  }

  @Override
  public synchronized void paint(Composite parent) throws RemoteException
  {
    super.paint(parent);
  }

}
