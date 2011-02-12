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
 * Revision 1.9  2011-02-03 22:02:21  jost
 * Bugfix Kontextmenu
 *
 * Revision 1.8  2011-01-27 22:19:52  jost
 * Neu: Speicherung von weiteren Adressen in der Mitgliedertabelle
 *
 * Revision 1.7  2010-10-15 09:58:26  jost
 * Code aufgeräumt
 *
 * Revision 1.6  2010-05-23 07:11:51  jost
 * Kontextmenü aufgenommen.
 *
 * Revision 1.5  2010/05/16 12:32:31  jost
 * Direkter Link zum Mitglied. Kommunikationsdaten aufgenommen.
 *
 * Revision 1.4  2010/05/16 10:43:58  jost
 * Einheitlicher Umgang mit ausgetretenen Mitgliedern
 *
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

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.MitgliedDetailAction;
import de.jost_net.JVerein.gui.menu.MitgliedMenu;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.server.MitgliedUtils;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
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
    MitgliedUtils.setMitglied(geburtstage);
    StringBuffer filter = new StringBuffer();
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
        filter.append(" OR ");
      }
      filter.append("(month(geburtsdatum) = ");
      filter.append(cal.get(Calendar.MONTH) + 1);
      filter.append(" AND day(geburtsdatum) = ");
      filter.append(cal.get(Calendar.DAY_OF_MONTH));
      filter.append(")");
      ;
      cal.add(Calendar.DAY_OF_MONTH, 1);
    }

    for (int i = 0; i <= nachher; i++)
    {
      if (filter.length() > 0)
      {
        filter.append(" OR ");
      }
      filter.append("(month(geburtsdatum) = ");
      filter.append(cal.get(Calendar.MONTH) + 1);
      filter.append(" AND day(geburtsdatum) = ");
      filter.append(Calendar.DAY_OF_MONTH);
      filter.append(")");
      cal.add(Calendar.DAY_OF_MONTH, 1);
    }
    geburtstage.addFilter(filter.toString());
    geburtstage.setOrder("ORDER BY month(geburtsdatum), day(geburtsdatum)");

    if (aktuelleGeburtstageList == null)
    {
      aktuelleGeburtstageList = new TablePart(geburtstage,
          new MitgliedDetailAction());
      aktuelleGeburtstageList.addColumn(JVereinPlugin.getI18n().tr("Name"),
          "name");
      aktuelleGeburtstageList.addColumn(JVereinPlugin.getI18n().tr("Vorname"),
          "vorname");
      aktuelleGeburtstageList.addColumn(
          JVereinPlugin.getI18n().tr("Geburtsdatum"), "geburtsdatum",
          new DateFormatter(new JVDateFormatTTMMJJJJ()));
      aktuelleGeburtstageList.addColumn(
          JVereinPlugin.getI18n().tr("Tel. priv"), "telefonprivat");
      aktuelleGeburtstageList.addColumn(
          JVereinPlugin.getI18n().tr("Tel. dienstlich"), "telefondienstlich");
      aktuelleGeburtstageList.addColumn(JVereinPlugin.getI18n().tr("Handy"),
          "handy");
      aktuelleGeburtstageList.addColumn(JVereinPlugin.getI18n().tr("Email"),
          "email");
      aktuelleGeburtstageList.setContextMenu(new MitgliedMenu(
          new MitgliedDetailAction()));
      aktuelleGeburtstageList.setRememberColWidths(true);
      aktuelleGeburtstageList.setRememberOrder(true);
      aktuelleGeburtstageList.setSummary(true);
    }
    else
    {
      aktuelleGeburtstageList.removeAll();
      while (geburtstage.hasNext())
      {
        aktuelleGeburtstageList.addItem(geburtstage.next());
      }
    }
    return aktuelleGeburtstageList;
  }

  @Override
  public synchronized void paint(Composite parent) throws RemoteException
  {
    super.paint(parent);
  }

}
