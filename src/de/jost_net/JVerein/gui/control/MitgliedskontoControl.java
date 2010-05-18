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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.menu.RechungMenu;
import de.jost_net.JVerein.rmi.Abrechnung;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.DecimalInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class MitgliedskontoControl extends AbstractControl
{
  private de.willuhn.jameica.system.Settings settings;

  private DateInput datum = null;

  private Input zweck1;

  private Input zweck2;

  private DecimalInput betrag;

  private Mitgliedskonto mkto;

  private TablePart mitgliedskontoList;

  private DateInput vondatum = null;

  private DateInput bisdatum = null;

  public MitgliedskontoControl(AbstractView view)
  {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public Mitgliedskonto getMitgliedskonto()
  {
    if (mkto != null)
    {
      return mkto;
    }
    mkto = (Mitgliedskonto) getCurrentObject();
    return mkto;
  }

  public DateInput getDatum() throws RemoteException
  {
    if (datum != null)
    {
      return datum;
    }

    Date d = getMitgliedskonto().getDatum();

    this.datum = new DateInput(d, Einstellungen.DATEFORMAT);
    this.datum.setTitle("Datum");
    this.datum.setText("Bitte Datum wählen");
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

  public Input getZweck1(boolean withFocus) throws RemoteException
  {
    if (zweck1 != null)
    {
      return zweck1;
    }
    zweck1 = new TextInput(getMitgliedskonto().getZweck1(), 27);
    zweck1.setMandatory(true);
    if (withFocus)
    {
      zweck1.focus();
    }
    return zweck1;
  }

  public Input getZweck2() throws RemoteException
  {
    if (zweck2 != null)
    {
      return zweck2;
    }
    zweck2 = new TextInput(getMitgliedskonto().getZweck2(), 27);
    return zweck2;
  }

  public DecimalInput getBetrag() throws RemoteException
  {
    if (betrag != null)
    {
      return betrag;
    }
    betrag = new DecimalInput(getMitgliedskonto().getBetrag(),
        Einstellungen.DECIMALFORMAT);
    betrag.setMandatory(true);
    return betrag;
  }

  public DateInput getVondatum() throws RemoteException
  {
    if (vondatum != null)
    {
      return vondatum;
    }
    Date d = null;
    this.vondatum = new DateInput(d, Einstellungen.DATEFORMAT);
    this.vondatum.setTitle("Anfangsdatum");
    this.vondatum.setText("Bitte Anfangsdatum wählen");
    this.vondatum.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) vondatum.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    vondatum.addListener(new FilterListener());
    return vondatum;
  }

  public DateInput getBisdatum() throws RemoteException
  {
    if (bisdatum != null)
    {
      return bisdatum;
    }
    Date d = null;
    this.bisdatum = new DateInput(d, Einstellungen.DATEFORMAT);
    this.bisdatum.setTitle("Endedatum");
    this.bisdatum.setText("Bitte Endedatum wählen");
    this.bisdatum.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) bisdatum.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    bisdatum.addListener(new FilterListener());
    return bisdatum;
  }

  public void handleStore()
  {
    try
    {
      Mitgliedskonto mkto = getMitgliedskonto();
      mkto.setBetrag((Double) getBetrag().getValue());
      mkto.setDatum((Date) getDatum().getValue());
      mkto.setZweck1((String) getZweck1(false).getValue());
      mkto.setZweck2((String) getZweck2().getValue());
      mkto.store();
      GUI.getStatusBar().setSuccessText("Satz gespeichert");
    }
    catch (ApplicationException e)
    {
      GUI.getStatusBar().setErrorText(e.getMessage());
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei speichern";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  public Part getMitgliedskontoList() throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator mitgliedskonten = service.createList(Mitgliedskonto.class);
    mitgliedskonten.setOrder("ORDER BY datum DESC");

    if (mitgliedskontoList == null)
    {
      mitgliedskontoList = new TablePart(mitgliedskonten, null);
      mitgliedskontoList.addColumn("Name", "mitglied");
      mitgliedskontoList.addColumn("Datum", "datum", new DateFormatter(
          Einstellungen.DATEFORMAT));
      mitgliedskontoList.addColumn("Zweck1", "zweck1");
      mitgliedskontoList.addColumn("Zweck2", "zweck2");
      mitgliedskontoList.addColumn("Betrag", "betrag", new CurrencyFormatter(
          "", Einstellungen.DECIMALFORMAT));
      mitgliedskontoList.setContextMenu(new RechungMenu());
      mitgliedskontoList.setRememberColWidths(true);
      mitgliedskontoList.setRememberOrder(true);
      mitgliedskontoList.setMulti(true);
      mitgliedskontoList.setSummary(true);
    }
    else
    {
      mitgliedskontoList.removeAll();
      while (mitgliedskonten.hasNext())
      {
        mitgliedskontoList.addItem((Abrechnung) mitgliedskonten.next());
      }
    }
    return mitgliedskontoList;
  }

  private void refresh()
  {
    if (mitgliedskontoList == null)
    {
      return;
    }
    try
    {
      mitgliedskontoList.removeAll();
      DBIterator mitgliedskonten = Einstellungen.getDBService().createList(
          Mitgliedskonto.class);
      // String suchV = (String) getSuchverwendungszweck().getValue();
      // if (suchV != null && suchV.length() > 0)
      // {
      // abr.addFilter("(zweck1 like ? or zweck2 like ?)", new Object[] {
      // "%" + suchV + "%", "%" + suchV + "%" });
      // }
      // if (getVondatum().getValue() != null)
      // {
      // abr.addFilter("datum >= ?", new Object[] { (Date) getVondatum()
      // .getValue() });
      // }
      // if (getBisdatum().getValue() != null)
      // {
      // abr.addFilter("datum <= ?", new Object[] { (Date) getBisdatum()
      // .getValue() });
      // }
      // while (abr.hasNext())
      // {
      // Abrechnung ab = (Abrechnung) abr.next();
      // abrechnungsList.addItem(ab);
      // }
    }
    catch (RemoteException e1)
    {
      e1.printStackTrace();
    }
  }

  private class FilterListener implements Listener
  {
    public void handleEvent(Event event)
    {
      if (event.type != SWT.Selection && event.type != SWT.FocusOut)
      {
        return;
      }
      refresh();
    }
  }

}
