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
 * Revision 1.1  2007/03/13 19:56:48  jost
 * Neu: Manueller Zahlungseingang.
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.menu.ManuellerZahlungseingangMenu;
import de.jost_net.JVerein.rmi.ManuellerZahlungseingang;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.input.DecimalInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class ManuellerZahlungseingangControl extends AbstractControl
{

  private Input name;

  private DecimalInput betrag;

  private Input vzweck1;

  private Input vzweck2;

  private TablePart manuellerZahlungseingangList;

  private ManuellerZahlungseingang manuellerzahlungseingang;

  public ManuellerZahlungseingangControl(AbstractView view)
  {
    super(view);
  }

  public Input getName() throws RemoteException
  {
    if (name != null)
    {
      return name;
    }
    name = new TextInput(getManuellerZahlungseingang().getName(), 27);
    name.setMandatory(true);
    return name;
  }

  public Input getVZweck1() throws RemoteException
  {
    if (vzweck1 != null)
    {
      return vzweck1;
    }
    vzweck1 = new TextInput(getManuellerZahlungseingang().getVZweck1(), 27);
    vzweck1.setMandatory(true);
    return vzweck1;
  }

  public Input getVZweck2() throws RemoteException
  {
    if (vzweck2 != null)
    {
      return vzweck2;
    }
    vzweck2 = new TextInput(getManuellerZahlungseingang().getVZweck2(), 27);
    return vzweck2;
  }

  public DecimalInput getBetrag() throws RemoteException
  {
    if (betrag != null)
    {
      return betrag;
    }
    betrag = new DecimalInput(getManuellerZahlungseingang().getBetrag(),
        Einstellungen.DECIMALFORMAT);
    betrag.setMandatory(true);
    return betrag;
  }

  private ManuellerZahlungseingang getManuellerZahlungseingang()
  {
    if (manuellerzahlungseingang != null)
    {
      return manuellerzahlungseingang;
    }
    manuellerzahlungseingang = (ManuellerZahlungseingang) getCurrentObject();
    return manuellerzahlungseingang;
  }

  public void handleStore()
  {
    try
    {
      ManuellerZahlungseingang mz = getManuellerZahlungseingang();
      mz.setName((String) getName().getValue());
      mz.setVZweck1((String) getVZweck1().getValue());
      mz.setVZweck2((String) getVZweck2().getValue());
      mz.setBetrag((Double) getBetrag().getValue());
      if (mz.getID() == null)
      {
        mz.setEingabedatum();
      }
      mz.store();
      GUI.getStatusBar().setSuccessText("ManuellerZahlungseingang gespeichert");
    }
    catch (ApplicationException e)
    {
      GUI.getStatusBar().setErrorText(e.getMessage());
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei Speichern des ManuellenZahlungseinganges";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  public Part getTable() throws RemoteException
  {
    if (manuellerZahlungseingangList != null)
    {
      return manuellerZahlungseingangList;
    }
    DBService service = Einstellungen.getDBService();
    DBIterator manzahl = service.createList(ManuellerZahlungseingang.class);
    manuellerZahlungseingangList = new TablePart(manzahl, null);

    manuellerZahlungseingangList.addColumn("Name", "name");
    manuellerZahlungseingangList.addColumn("VZweck 1", "vzweck1");
    manuellerZahlungseingangList.addColumn("VZweck 2", "vzweck2");
    manuellerZahlungseingangList.addColumn("Betrag", "betrag",
        new CurrencyFormatter("", Einstellungen.DECIMALFORMAT));
    manuellerZahlungseingangList.addColumn("Eingabedatum", "eingabedatum",
        new DateFormatter(Einstellungen.DATEFORMAT));
    manuellerZahlungseingangList.addColumn("Eingangsdatum", "eingangsdatum",
        new DateFormatter(Einstellungen.DATEFORMAT));
    manuellerZahlungseingangList
        .setContextMenu(new ManuellerZahlungseingangMenu(manuellerZahlungseingangList));

    return manuellerZahlungseingangList;
  }
}
