/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * jost@berlios.de
 * jverein.berlios.de
 * $Log$
 * Revision 1.1  2006/09/20 15:38:30  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.BuchungsartAction;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.IntegerInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class BuchungsartControl extends AbstractControl
{
  private de.willuhn.jameica.system.Settings settings;

  private TablePart buchungsartList;

  private IntegerInput nummer;

  private Input bezeichnung;

  private Buchungsart buchungsart;

  public BuchungsartControl(AbstractView view)
  {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  private Buchungsart getBuchungsart()
  {
    if (buchungsart != null)
    {
      return buchungsart;
    }
    buchungsart = (Buchungsart) getCurrentObject();
    return buchungsart;
  }

  public IntegerInput getNummer() throws RemoteException
  {
    if (nummer != null)
    {
      return nummer;
    }
    nummer = new IntegerInput(getBuchungsart().getNummer());
    return nummer;
  }

  public Input getBezeichnung() throws RemoteException
  {
    if (bezeichnung != null)
    {
      return bezeichnung;
    }
    bezeichnung = new TextInput(getBuchungsart().getBezeichnung(), 30);
    return bezeichnung;
  }

  /**
   * This method stores the project using the current values.
   */
  public void handleStore()
  {
    try
    {
      Buchungsart b = getBuchungsart();
      b.setNummer((Integer) getNummer().getValue());
      b.setBezeichnung((String) getBezeichnung().getValue());
      try
      {
        b.store();
        GUI.getStatusBar().setSuccessText("Buchungsart gespeichert");
      }
      catch (ApplicationException e)
      {
        GUI.getView().setErrorText(e.getMessage());
      }
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei speichern der Buchungsart";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  public Part getBuchungsartList() throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator buchungsarten = service.createList(Buchungsart.class);
    buchungsarten.addFilter("nummer >= 0");
    buchungsarten.setOrder("ORDER BY nummer");

    buchungsartList = new TablePart(buchungsarten, new BuchungsartAction());
    buchungsartList.addColumn("Nummer", "nummer");
    buchungsartList.addColumn("Bezeichnung", "bezeichnung");
    buchungsartList.setRememberColWidths(true);
    buchungsartList.setRememberOrder(true);
    buchungsartList.setSummary(true);
    return buchungsartList;
  }
}
