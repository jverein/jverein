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
import de.jost_net.JVerein.gui.action.LehrgangsartAction;
import de.jost_net.JVerein.gui.menu.LehrgangsartMenu;
import de.jost_net.JVerein.rmi.Lehrgangsart;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class LehrgangsartControl extends AbstractControl
{
  private de.willuhn.jameica.system.Settings settings;

  private TablePart lehrgangsartList;

  private TextInput bezeichnung;

  private DateInput von;

  private DateInput bis;

  private TextInput veranstalter;

  private Lehrgangsart lehrgangsart;

  public LehrgangsartControl(AbstractView view)
  {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public Lehrgangsart getLehrgangsart()
  {
    if (lehrgangsart != null)
    {
      return lehrgangsart;
    }
    lehrgangsart = (Lehrgangsart) getCurrentObject();
    return lehrgangsart;
  }

  public TextInput getBezeichnung() throws RemoteException
  {
    if (bezeichnung != null)
    {
      return bezeichnung;
    }
    bezeichnung = new TextInput((String) getLehrgangsart().getBezeichnung(), 50);
    return bezeichnung;
  }

  public DateInput getVon() throws RemoteException
  {
    if (von != null)
    {
      return von;
    }
    Date d = getLehrgangsart().getVon();
    if (d != null && d.equals(Einstellungen.NODATE))
    {
      d = null;
    }
    this.von = new DateInput(d, Einstellungen.DATEFORMAT);
    this.von.setTitle("von/am");
    this.von.setText("Bitte Beginn oder Tag der Veranstaltung wählen");
    this.von.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) von.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return von;
  }

  public DateInput getBis() throws RemoteException
  {
    if (bis != null)
    {
      return bis;
    }
    Date d = getLehrgangsart().getBis();
    if (d != null && d.equals(Einstellungen.NODATE))
    {
      d = null;
    }
    this.bis = new DateInput(d, Einstellungen.DATEFORMAT);
    this.bis.setTitle("bis");
    this.bis.setText("Bitte Ende der Veranstaltung wählen");
    this.bis.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) bis.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return bis;
  }

  public TextInput getVeranstalter() throws RemoteException
  {
    if (veranstalter != null)
    {
      return veranstalter;
    }
    veranstalter = new TextInput((String) getLehrgangsart().getVeranstalter(),
        50);
    return veranstalter;
  }

  /**
   * This method stores the project using the current values.
   */
  public void handleStore()
  {
    try
    {
      Lehrgangsart l = getLehrgangsart();
      l.setBezeichnung((String) getBezeichnung().getValue());
      l.setVon((Date) getVon().getValue());
      l.setBis((Date) getBis().getValue());
      l.setVeranstalter((String) getVeranstalter().getValue());
      l.store();
      GUI.getStatusBar().setSuccessText("Lehrgangsart gespeichert");
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler beim Speichern der Lehrgangsart";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
    catch (ApplicationException e)
    {
      Logger.error("Fehler", e);
      GUI.getStatusBar().setErrorText(e.getMessage());
    }
  }

  public Part getLehrgangsartList() throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator formulare = service.createList(Lehrgangsart.class);
    formulare.setOrder("ORDER BY bezeichnung");

    lehrgangsartList = new TablePart(formulare, new LehrgangsartAction());
    lehrgangsartList.addColumn("Bezeichnung", "bezeichnung");
    lehrgangsartList.addColumn("von/am", "von", new DateFormatter(
        Einstellungen.DATEFORMAT));
    lehrgangsartList.addColumn("bis", "bis", new DateFormatter(
        Einstellungen.DATEFORMAT));
    lehrgangsartList.addColumn("Veranstalter", "veranstalter");
    lehrgangsartList.setRememberColWidths(true);
    lehrgangsartList.setContextMenu(new LehrgangsartMenu());
    lehrgangsartList.setRememberOrder(true);
    lehrgangsartList.setSummary(false);
    return lehrgangsartList;
  }

  public void refreshTable() throws RemoteException
  {
    lehrgangsartList.removeAll();
    DBIterator lehrgangsarten = Einstellungen.getDBService().createList(
        Lehrgangsart.class);
    lehrgangsarten.setOrder("ORDER BY bezeichnung");
    while (lehrgangsarten.hasNext())
    {
      lehrgangsartList.addItem((Lehrgangsart) lehrgangsarten.next());
    }
  }

}
