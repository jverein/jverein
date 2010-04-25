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
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.formatter.AbrechnungsmodusFormatter;
import de.jost_net.JVerein.gui.formatter.JaNeinFormatter;
import de.jost_net.JVerein.rmi.Abrechnungslaeufe;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.formatter.Formatter;
import de.willuhn.jameica.gui.parts.Column;
import de.willuhn.jameica.gui.parts.TablePart;

public class AbrechnungslaeufeControl extends AbstractControl
{
  private de.willuhn.jameica.system.Settings settings;

  private Abrechnungslaeufe abrl;

  private TablePart abrechnungslaeufeList;

  public AbrechnungslaeufeControl(AbstractView view)
  {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public Abrechnungslaeufe getAbrechnungslaeufe()
  {
    if (abrl != null)
    {
      return abrl;
    }
    abrl = (Abrechnungslaeufe) getCurrentObject();
    return abrl;
  }

  public void handleStore()
  {
    //
  }

  public Part getAbrechungslaeufeList() throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator abrechnungslaeufe = service.createList(Abrechnungslaeufe.class);
    abrechnungslaeufe.setOrder("ORDER BY datum DESC");

    if (abrechnungslaeufeList == null)
    {
      abrechnungslaeufeList = new TablePart(abrechnungslaeufe, null);
      abrechnungslaeufeList.addColumn("Datum", "datum", new DateFormatter(
          Einstellungen.DATEFORMAT));
      abrechnungslaeufeList.addColumn("Modus", "modus",
          new AbrechnungsmodusFormatter(), false, Column.ALIGN_LEFT);
      abrechnungslaeufeList.addColumn("Stichtag", "stichtag",
          new DateFormatter(Einstellungen.DATEFORMAT));
      abrechnungslaeufeList.addColumn("Eingabedatum", "eingabedatum",
          new DateFormatter(Einstellungen.DATEFORMAT));
      abrechnungslaeufeList.addColumn("Zahlungsgrund", "zahlungsgrund");
      abrechnungslaeufeList.addColumn("Zusatzbeträge", "zusatzbetraege",
          new JaNeinFormatter());
      abrechnungslaeufeList.addColumn("Kursteilnehmer", "kursteilnehmer",
          new JaNeinFormatter());
      // abrechnungsList.setContextMenu(new RechungMenu());
      abrechnungslaeufeList.setRememberColWidths(true);
      abrechnungslaeufeList.setRememberOrder(true);
      abrechnungslaeufeList.setSummary(true);
    }
    else
    {
      abrechnungslaeufeList.removeAll();
      while (abrechnungslaeufe.hasNext())
      {
        abrechnungslaeufeList.addItem((Abrechnungslaeufe) abrechnungslaeufe
            .next());
      }
    }
    return abrechnungslaeufeList;
  }

  private void refresh()
  {
    if (abrechnungslaeufeList == null)
    {
      return;
    }
    try
    {
      abrechnungslaeufeList.removeAll();
      DBIterator abrl = Einstellungen.getDBService().createList(
          Abrechnungslaeufe.class);
      while (abrl.hasNext())
      {
        Abrechnungslaeufe abl = (Abrechnungslaeufe) abrl.next();
        abrechnungslaeufeList.addItem(abl);
      }
    }
    catch (RemoteException e1)
    {
      e1.printStackTrace();
    }
  }
}
