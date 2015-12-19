/**********************************************************************
 * Copyright (c) by Heiner Jostkleigrewe
 * This program is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,  but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See 
 *  the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, 
 * see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de
 * www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;
import java.util.Date;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.input.BuchungsartInput;
import de.jost_net.JVerein.keys.IntervallZusatzzahlung;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.jost_net.JVerein.rmi.ZusatzbetragVorlage;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.input.AbstractInput;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.DecimalInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.hbci.HBCIProperties;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class ZusatzbetragVorlageControl extends AbstractControl
{

  private de.willuhn.jameica.system.Settings settings;

  private DateInput faelligkeit = null;

  private TextInput buchungstext;

  private DecimalInput betrag;

  private ZusatzbetragVorlage zbv;

  private DateInput startdatum;

  private SelectInput intervall;

  private DateInput endedatum;

  private AbstractInput buchungsart;

  private TablePart zusatzbetraegeList;

  public ZusatzbetragVorlage auswahl;

  public ZusatzbetragVorlageControl(AbstractView view)
  {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public ZusatzbetragVorlage getZusatzbetragVorlage()
  {
    if (zbv != null)
    {
      return zbv;
    }
    zbv = (ZusatzbetragVorlage) getCurrentObject();
    return zbv;
  }

  public DateInput getFaelligkeit() throws RemoteException
  {
    if (faelligkeit != null)
    {
      return faelligkeit;
    }

    Date d = getZusatzbetragVorlage().getFaelligkeit();

    this.faelligkeit = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.faelligkeit.setTitle("Fälligkeit");
    this.faelligkeit.setText("Bitte Fälligkeitsdatum wählen");
    this.faelligkeit.addListener(new Listener()
    {
      @Override
      public void handleEvent(Event event)
      {
        Date date = (Date) faelligkeit.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return faelligkeit;
  }

  public TextInput getBuchungstext() throws RemoteException
  {
    if (buchungstext != null)
    {
      return buchungstext;
    }
    buchungstext = new TextInput(getZusatzbetragVorlage().getBuchungstext(),
        140);
    buchungstext.setMandatory(true);
    buchungstext.setValidChars(HBCIProperties.HBCI_DTAUS_VALIDCHARS);
    return buchungstext;
  }

  public DecimalInput getBetrag() throws RemoteException
  {
    if (betrag != null)
    {
      return betrag;
    }
    betrag = new DecimalInput(getZusatzbetragVorlage().getBetrag(),
        Einstellungen.DECIMALFORMAT);
    betrag.setMandatory(true);
    return betrag;
  }

  public DateInput getStartdatum(boolean withFocus) throws RemoteException
  {
    if (startdatum != null)
    {
      return startdatum;
    }

    Date d = getZusatzbetragVorlage().getStartdatum();
    this.startdatum = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.startdatum.setTitle("Startdatum");
    this.startdatum.setText("Bitte Startdatum wählen");
    this.startdatum.addListener(new Listener()
    {
      @Override
      public void handleEvent(Event event)
      {
        Date date = (Date) startdatum.getValue();
        if (date == null)
        {
          return;
        }
        startdatum.setValue(date);
        if (faelligkeit.getValue() == null)
        {
          faelligkeit.setValue(startdatum.getValue());
        }
      }
    });
    if (withFocus)
    {
      startdatum.focus();
    }
    return startdatum;
  }

  public SelectInput getIntervall() throws RemoteException
  {
    if (intervall != null)
    {
      return intervall;
    }
    Integer i = getZusatzbetragVorlage().getIntervall();
    if (i == null)
    {
      i = Integer.valueOf(0);
    }
    this.intervall = new SelectInput(IntervallZusatzzahlung.getArray(),
        new IntervallZusatzzahlung(i));
    return intervall;
  }

  public AbstractInput getBuchungsart() throws RemoteException
  {
    if (buchungsart != null)
    {
      return buchungsart;
    }
    buchungsart = new BuchungsartInput().getBuchungsartInput(buchungsart,
        getZusatzbetragVorlage().getBuchungsart());
    return buchungsart;
  }

  public DateInput getEndedatum() throws RemoteException
  {
    if (endedatum != null)
    {
      return endedatum;
    }

    Date d = getZusatzbetragVorlage().getEndedatum();
    this.endedatum = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.endedatum.setTitle("Endedatum");
    this.endedatum.setText("Bitte Endedatum wählen");
    this.endedatum.addListener(new Listener()
    {
      @Override
      public void handleEvent(Event event)
      {
        Date date = (Date) endedatum.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return endedatum;
  }

  public void handleStore()
  {
    try
    {
      ZusatzbetragVorlage z = getZusatzbetragVorlage();
      z.setFaelligkeit((Date) getFaelligkeit().getValue());
      z.setStartdatum((Date) getStartdatum(false).getValue());
      IntervallZusatzzahlung iz = (IntervallZusatzzahlung) getIntervall()
          .getValue();
      z.setIntervall(iz.getKey());
      z.setEndedatum((Date) getEndedatum().getValue());
      z.setBuchungstext((String) getBuchungstext().getValue());
      Double d = (Double) getBetrag().getValue();
      z.setBetrag(d.doubleValue());
      if (getBuchungsart().getValue() != null)
      {
        z.setBuchungsart((Buchungsart) getBuchungsart().getValue());
      }

      z.store();
      GUI.getStatusBar().setSuccessText("Zusatzbetrag-Vorlage gespeichert");
    }
    catch (ApplicationException e)
    {
      GUI.getStatusBar().setErrorText(e.getMessage());
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei speichern der Zusatzbetrag-Vorlage";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  public Part getZusatzbetraegeVorlageList() throws RemoteException
  {
    DBIterator zusatzbetragsvorlagen = Einstellungen.getDBService().createList(
        ZusatzbetragVorlage.class);
    zusatzbetragsvorlagen.setOrder("ORDER BY buchungstext");

    if (zusatzbetraegeList == null)
    {
      zusatzbetraegeList = new TablePart(zusatzbetragsvorlagen, new Action()
      {
        @Override
        public void handleAction(Object context) throws ApplicationException
        {
          auswahl = (ZusatzbetragVorlage) context;
        }
      });
      zusatzbetraegeList.addColumn("Startdatum", "startdatum",
          new DateFormatter(new JVDateFormatTTMMJJJJ()));
      zusatzbetraegeList.addColumn("nächste Fälligkeit", "faelligkeit",
          new DateFormatter(new JVDateFormatTTMMJJJJ()));
      zusatzbetraegeList.addColumn("Intervall", "intervalltext");
      zusatzbetraegeList.addColumn("Endedatum", "endedatum", new DateFormatter(
          new JVDateFormatTTMMJJJJ()));
      zusatzbetraegeList.addColumn("Buchungstext", "buchungstext");
      zusatzbetraegeList.addColumn("Betrag", "betrag", new CurrencyFormatter(
          "", Einstellungen.DECIMALFORMAT));
      // zusatzbetraegeList.setContextMenu(new ZusatzbetraegeMenu(
      // zusatzbetraegeList));
      zusatzbetraegeList.setRememberColWidths(true);
      zusatzbetraegeList.setRememberOrder(true);
      zusatzbetraegeList.setSummary(true);
      zusatzbetraegeList.setMulti(true);
    }
    else
    {
      zusatzbetraegeList.removeAll();
      while (zusatzbetragsvorlagen.hasNext())
      {
        zusatzbetraegeList.addItem(zusatzbetragsvorlagen.next());
      }
    }
    return zusatzbetraegeList;
  }
}