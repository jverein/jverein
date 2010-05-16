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
 * Revision 1.3  2010/04/26 19:22:42  jost
 * Korrekte Behandlung von ausgetretenen Mitgliedern
 *
 * Revision 1.2  2009/07/24 18:42:26  jost
 * Vermeidung NullpointerException
 *
 * Revision 1.1  2009/07/14 07:29:12  jost
 * Neu: Box aktuelle Geburtstage
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.parts;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.server.MitgliedUtils;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.parts.TablePart;

public class AktuelleGeburtstageList extends TablePart implements Part
{
  private TablePart aktuelleGeburtstageList;

  public AktuelleGeburtstageList()
  {
    super(null);
  }

  public Part getAktuelleGeburtstageList() throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator geburtstage = service.createList(Mitglied.class);
    MitgliedUtils.setNurAktive(geburtstage);
    String filter = "";
    Calendar cal = Calendar.getInstance();
    int vorher = 0;
    int nachher = 0;

    try
    {
      vorher = Einstellungen.getEinstellung().getAktuelleGeburtstageVorher();
      nachher = Einstellungen.getEinstellung().getAktuelleGeburtstageNachher();
    }
    catch (NullPointerException e)
    {
      //
    }
    cal.add(Calendar.DAY_OF_YEAR, vorher * -1);
    for (int i = vorher; i > 0; i--)
    {
      if (filter.length() > 0)
      {
        filter += " OR ";
      }
      filter += "(month(geburtsdatum) = " + (cal.get(Calendar.MONTH) + 1)
          + " AND day(geburtsdatum) = " + cal.get(Calendar.DAY_OF_MONTH) + ")";
      cal.add(Calendar.DAY_OF_MONTH, 1);
    }

    for (int i = 0; i <= nachher; i++)
    {
      if (filter.length() > 0)
      {
        filter += " OR ";
      }
      filter += "(month(geburtsdatum) = " + (cal.get(Calendar.MONTH) + 1)
          + " AND day(geburtsdatum) = " + cal.get(Calendar.DAY_OF_MONTH) + ")";
      cal.add(Calendar.DAY_OF_MONTH, 1);
    }
    geburtstage.addFilter(filter);
    geburtstage.setOrder("ORDER BY month(geburtsdatum), day(geburtsdatum)");

    if (aktuelleGeburtstageList == null)
    {
      aktuelleGeburtstageList = new TablePart(geburtstage, null);
      aktuelleGeburtstageList.addColumn(JVereinPlugin.getI18n().tr("Name"),
          "name");
      aktuelleGeburtstageList.addColumn(JVereinPlugin.getI18n().tr("Vorname"),
          "vorname");
      aktuelleGeburtstageList.addColumn(JVereinPlugin.getI18n().tr(
          "Geburtsdatum"), "geburtsdatum", new DateFormatter(
          Einstellungen.DATEFORMAT));
      aktuelleGeburtstageList.setRememberColWidths(true);
      aktuelleGeburtstageList.setRememberOrder(true);
      aktuelleGeburtstageList.setSummary(true);
    }
    else
    {
      aktuelleGeburtstageList.removeAll();
      while (geburtstage.hasNext())
      {
        aktuelleGeburtstageList.addItem((Mitglied) geburtstage.next());
      }
    }
    return aktuelleGeburtstageList;
  }

  public synchronized void paint(Composite parent) throws RemoteException
  {
    super.paint(parent);
  }

}
