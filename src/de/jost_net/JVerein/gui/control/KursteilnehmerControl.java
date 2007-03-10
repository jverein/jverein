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
 * Revision 1.1  2007/02/25 19:12:29  jost
 * Neu: Kursteilnehmer
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;
import java.util.Date;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.kapott.hbci.manager.HBCIUtils;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.KursteilnehmerDetailAction;
import de.jost_net.JVerein.gui.menu.KursteilnehmerMenu;
import de.jost_net.JVerein.rmi.Kursteilnehmer;
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
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class KursteilnehmerControl extends AbstractControl
{

  private Input name;

  private DecimalInput betrag;

  private Input vzweck1;

  private Input vzweck2;

  private Input blz;

  private Input konto;

  private DateInput geburtsdatum = null;

  private SelectInput geschlecht;

  private Kursteilnehmer ktn;

  public KursteilnehmerControl(AbstractView view)
  {
    super(view);
  }

  private Kursteilnehmer getKursteilnehmer()
  {
    if (ktn != null)
    {
      return ktn;
    }
    ktn = (Kursteilnehmer) getCurrentObject();
    return ktn;
  }

  public Input getName() throws RemoteException
  {
    if (name != null)
    {
      return name;
    }
    name = new TextInput(getKursteilnehmer().getName(), 27);
    name.setMandatory(true);
    return name;
  }

  public Input getVZweck1() throws RemoteException
  {
    if (vzweck1 != null)
    {
      return vzweck1;
    }
    vzweck1 = new TextInput(getKursteilnehmer().getVZweck1(), 27);
    vzweck1.setMandatory(true);
    return vzweck1;
  }

  public Input getVZweck2() throws RemoteException
  {
    if (vzweck2 != null)
    {
      return vzweck2;
    }
    vzweck2 = new TextInput(getKursteilnehmer().getVZweck2(), 27);
    return vzweck2;
  }

  public Input getBlz() throws RemoteException
  {
    if (blz != null)
    {
      return blz;
    }
    blz = new TextInput(getKursteilnehmer().getBlz(), 8);
    blz.setMandatory(true);
    BLZListener l = new BLZListener();
    blz.addListener(l);
    l.handleEvent(null); // Einmal initial ausfuehren
    return blz;
  }

  public Input getKonto() throws RemoteException
  {
    if (konto != null)
    {
      return konto;
    }
    konto = new TextInput(getKursteilnehmer().getKonto(), 10);
    konto.setMandatory(true);
    return konto;
  }

  public DecimalInput getBetrag() throws RemoteException
  {
    if (betrag != null)
    {
      return betrag;
    }
    betrag = new DecimalInput(getKursteilnehmer().getBetrag(),
        Einstellungen.DECIMALFORMAT);
    betrag.setMandatory(true);
    return betrag;
  }

  public DateInput getGeburtsdatum() throws RemoteException
  {
    if (geburtsdatum != null)
    {
      return geburtsdatum;
    }
    Date d = getKursteilnehmer().getGeburtsdatum();
    this.geburtsdatum = new DateInput(d, Einstellungen.DATEFORMAT);
    this.geburtsdatum.setTitle("Geburtsdatum");
    this.geburtsdatum.setText("Bitte Geburtsdatum wählen");
    this.geburtsdatum.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) geburtsdatum.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    this.geburtsdatum.setMandatory(true);
    return geburtsdatum;
  }

  public SelectInput getGeschlecht() throws RemoteException
  {
    if (geschlecht != null)
    {
      return geschlecht;
    }
    geschlecht = new SelectInput(new String[] { "m", "w" }, getKursteilnehmer()
        .getGeschlecht());
    geschlecht.setPleaseChoose("Bitte auswählen");
    geschlecht.setMandatory(true);
    return geschlecht;
  }

  public Part getKursteilnehmerTable(TablePart part) throws RemoteException
  {
    if (part != null)
    {
      return part;
    }
    DBService service = Einstellungen.getDBService();
    DBIterator kursteilnehmer = service.createList(Kursteilnehmer.class);
    part = new TablePart(kursteilnehmer, new KursteilnehmerDetailAction());

    part.addColumn("Name", "name");
    part.addColumn("VZweck 1", "vzweck1");
    part.addColumn("BLZ", "blz");
    part.addColumn("Konto", "konto");
    part.addColumn("Betrag", "betrag", new CurrencyFormatter("",
        Einstellungen.DECIMALFORMAT));
    part.addColumn("Eingabedatum", "eingabedatum", new DateFormatter(
        Einstellungen.DATEFORMAT));
    part.addColumn("Abbuchungsdatum", "abbudatum", new DateFormatter(
        Einstellungen.DATEFORMAT));
    part.setContextMenu(new KursteilnehmerMenu());

    return part;
  }

  public void handleStore()
  {
    try
    {
      Kursteilnehmer k = getKursteilnehmer();
      k.setName((String) getName().getValue());
      k.setVZweck1((String) getVZweck1().getValue());
      k.setVZweck2((String) getVZweck2().getValue());
      k.setBlz((String) getBlz().getValue());
      k.setKonto((String) getKonto().getValue());
      k.setBetrag((Double) getBetrag().getValue());
      k.setGeburtsdatum((Date) getGeburtsdatum().getValue());
      if (k.getID() == null)
      {
        k.setEingabedatum();
      }
      k.store();
      GUI.getStatusBar().setSuccessText("Kursteilnehmer gespeichert");
    }
    catch (ApplicationException e)
    {
      GUI.getView().setErrorText(e.getMessage());
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei Speichern des Kursteilnehmers";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  /**
   * Sucht das Geldinstitut zur eingegebenen BLZ und zeigt es als Kommentar
   * hinter dem BLZ-Feld an.
   */
  private class BLZListener implements Listener
  {
    public void handleEvent(Event event)
    {
      try
      {
        String name = HBCIUtils.getNameForBLZ((String) getBlz().getValue());
        getBlz().setComment(name);
      }
      catch (RemoteException e)
      {
        Logger.error("error while updating blz comment", e);
      }
    }
  }
}
