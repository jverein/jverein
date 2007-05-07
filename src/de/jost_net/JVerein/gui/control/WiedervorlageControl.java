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
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;
import java.util.Date;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.WiedervorlageAction;
import de.jost_net.JVerein.gui.menu.WiedervorlageMenu;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Wiedervorlage;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.formatter.Formatter;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class WiedervorlageControl extends AbstractControl
{
  private DateInput datum = null;

  private Input vermerk = null;

  private DateInput erledigung = null;

  private TablePart wiedervorlageList;

  private Wiedervorlage wvl = null;

  public WiedervorlageControl(AbstractView view)
  {
    super(view);
  }

  public Wiedervorlage getWiedervorlage()
  {
    if (wvl != null)
    {
      return wvl;
    }
    wvl = (Wiedervorlage) getCurrentObject();
    return wvl;
  }

  public DateInput getDatum() throws RemoteException
  {
    if (datum != null)
    {
      return datum;
    }

    Date d = getWiedervorlage().getDatum();

    this.datum = new DateInput(d, Einstellungen.DATEFORMAT);
    this.datum.setTitle("Datum");
    this.datum.setText("Bitte Wiedervorlagedatum wählen");
    this.datum.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) datum.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return datum;
  }

  public Input getVermerk() throws RemoteException
  {
    if (vermerk != null)
    {
      return vermerk;
    }
    vermerk = new TextInput(getWiedervorlage().getVermerk(), 50);
    vermerk.setMandatory(true);
    return vermerk;
  }

  public DateInput getErledigung() throws RemoteException
  {
    if (erledigung != null)
    {
      return erledigung;
    }

    Date d = getWiedervorlage().getErledigung();

    this.erledigung = new DateInput(d, Einstellungen.DATEFORMAT);
    this.erledigung.setTitle("Erledigung");
    this.erledigung.setText("Bitte Erledigungsdatum wählen");
    this.erledigung.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) erledigung.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return erledigung;
  }

  public void handleStore()
  {
    try
    {
      Wiedervorlage w = getWiedervorlage();
      w.setDatum((Date) getDatum().getValue());
      w.setVermerk((String) getVermerk().getValue());
      w.setErledigung((Date) getErledigung().getValue());
      w.store();
      GUI.getStatusBar().setSuccessText("Wiedervorlage gespeichert");
    }
    catch (ApplicationException e)
    {
      GUI.getView().setErrorText(e.getMessage());
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei speichern der Wiedervorlage";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  public Part getWiedervorlageList() throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator wiedervorlagen = service.createList(Wiedervorlage.class);
    wiedervorlagen.setOrder("ORDER BY datum DESC");

    if (wiedervorlageList == null)
    {
      wiedervorlageList = new TablePart(wiedervorlagen,
          new WiedervorlageAction(null));
      wiedervorlageList.addColumn("Name", "mitglied", new Formatter()
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
      wiedervorlageList.addColumn("Datum", "datum", new DateFormatter(
          Einstellungen.DATEFORMAT));
      wiedervorlageList.addColumn("Vermerk", "vermerk");
      wiedervorlageList.addColumn("Erledigung", "erledigung",
          new DateFormatter(Einstellungen.DATEFORMAT));
      wiedervorlageList
          .setContextMenu(new WiedervorlageMenu(wiedervorlageList));
      wiedervorlageList.setRememberColWidths(true);
      wiedervorlageList.setRememberOrder(true);
      wiedervorlageList.setSummary(true);
    }
    else
    {
      wiedervorlageList.removeAll();
      while (wiedervorlagen.hasNext())
      {
        wiedervorlageList.addItem((Wiedervorlage) wiedervorlagen.next());
      }
    }
    return wiedervorlageList;
  }
}
