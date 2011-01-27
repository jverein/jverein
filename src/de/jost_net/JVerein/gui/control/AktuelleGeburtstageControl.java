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
 * Revision 1.3  2010-10-15 09:58:26  jost
 * Code aufgeräumt
 *
 * Revision 1.2  2010-05-16 10:42:55  jost
 * Einheitlicher Umgang mit ausgetretenen Mitgliedern
 *
 * Revision 1.1  2009/07/14 07:28:43  jost
 * Neu: Box aktuelle Geburtstage
 *
 **********************************************************************/

package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;
import java.util.Calendar;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.server.MitgliedUtils;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.system.Settings;

public class AktuelleGeburtstageControl extends AbstractControl
{

  private TablePart aktuelleGeburtstageList;

  private SelectInput vorher;

  private SelectInput nachher;

  private Settings settings = null;

  public AktuelleGeburtstageControl(AbstractView view)
  {
    super(view);
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public SelectInput getVorher()
  {
    if (vorher != null)
    {
      return vorher;
    }
    Integer[] v = new Integer[30];
    for (int i = 0; i < 30; i++)
    {
      v[i] = i;
    }
    vorher = new SelectInput(v, 3);
    return vorher;
  }

  public SelectInput getNachher()
  {
    if (nachher != null)
    {
      return nachher;
    }
    Integer[] v = new Integer[30];
    for (int i = 0; i < 30; i++)
    {
      v[i] = i;
    }
    nachher = new SelectInput(v, 3);
    return nachher;
  }

  public Part getAktuelleGeburtstageList() throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator geburtstage = service.createList(Mitglied.class);
    MitgliedUtils.setMitglied(geburtstage);
    String filter = "";
    Calendar cal = Calendar.getInstance();
    int vorher = 3;
    int nachher = 7;
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
    MitgliedUtils.setNurAktive(geburtstage);
    geburtstage.setOrder("ORDER BY month(geburtsdatum), day(geburtsdatum)");

    if (aktuelleGeburtstageList == null)
    {
      aktuelleGeburtstageList = new TablePart(geburtstage, null);
      aktuelleGeburtstageList.addColumn(JVereinPlugin.getI18n().tr("Name"),
          "name");
      aktuelleGeburtstageList.addColumn(JVereinPlugin.getI18n().tr("Vorname"),
          "vorname");
      aktuelleGeburtstageList.addColumn(
          JVereinPlugin.getI18n().tr("Geburtsdatum"), "geburtsdatum",
          new DateFormatter(Einstellungen.DATEFORMAT));
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

}
