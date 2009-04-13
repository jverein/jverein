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
import de.jost_net.JVerein.rmi.Lehrgang;
import de.jost_net.JVerein.rmi.Lehrgangsart;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class LehrgangControl extends AbstractControl
{
  private SelectInput lehrgangsart;

  private DateInput von = null;

  private DateInput bis = null;

  private TextInput veranstalter = null;

  private TextInput ergebnis = null;

  private Lehrgang lehrg = null;

  public LehrgangControl(AbstractView view)
  {
    super(view);
  }

  public Lehrgang getLehrgang()
  {
    if (lehrg != null)
    {
      return lehrg;
    }
    lehrg = (Lehrgang) getCurrentObject();
    return lehrg;
  }

  public SelectInput getLehrgangsart() throws RemoteException
  {
    if (lehrgangsart != null)
    {
      return lehrgangsart;
    }
    DBIterator it = Einstellungen.getDBService().createList(Lehrgangsart.class);
    it.setOrder("order by bezeichnung");
    lehrgangsart = new SelectInput(it, getLehrgang().getLehrgangsart());
    lehrgangsart.setPleaseChoose("Bitte auswählen");
    lehrgangsart.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Lehrgangsart la = (Lehrgangsart) lehrgangsart.getValue();
        try
        {
          getVon().setValue(la.getVon());
          getBis().setValue(la.getBis());
          getVeranstalter().setValue(la.getVeranstalter());
        }
        catch (RemoteException e)
        {
          e.printStackTrace();
        }
      }
    });

    return lehrgangsart;
  }

  public DateInput getVon() throws RemoteException
  {
    if (von != null)
    {
      return von;
    }

    Date d = getLehrgang().getVon();

    this.von = new DateInput(d, Einstellungen.DATEFORMAT);
    this.von.setTitle("Datum");
    this.von.setText("Bitte (Beginn-)Datum wählen");
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

    Date d = getLehrgang().getBis();

    this.bis = new DateInput(d, Einstellungen.DATEFORMAT);
    this.bis.setTitle("Datum");
    this.bis.setText("Bitte Ende-Datum wählen");
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

  public Input getVeranstalter() throws RemoteException
  {
    if (veranstalter != null)
    {
      return veranstalter;
    }
    veranstalter = new TextInput(getLehrgang().getVeranstalter(), 50);
    return veranstalter;
  }

  public Input getErgebnis() throws RemoteException
  {
    if (ergebnis != null)
    {
      return ergebnis;
    }
    ergebnis = new TextInput(getLehrgang().getErgebnis(), 50);
    return ergebnis;
  }

  public void handleStore()
  {
    try
    {
      Lehrgang l = getLehrgang();
      Lehrgangsart la = (Lehrgangsart) getLehrgangsart().getValue();
      l.setLehrgangsart(new Integer(la.getID()));
      l.setVon((Date) getVon().getValue());
      l.setBis((Date) getBis().getValue());
      l.setVeranstalter((String) getVeranstalter().getValue());
      l.setErgebnis((String) getErgebnis().getValue());
      l.store();
      GUI.getStatusBar().setSuccessText("Lehrgang gespeichert");
    }
    catch (ApplicationException e)
    {
      GUI.getView().setErrorText(e.getMessage());
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei speichern des Lehrgangs";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }
}
