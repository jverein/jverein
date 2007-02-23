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
 * Revision 1.2  2006/12/20 20:25:44  jost
 * Patch von Ullrich Schäfer, der die Primitive vs. Object Problematik adressiert.
 *
 * Revision 1.1  2006/09/20 15:38:30  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.BeitragsgruppeDetailAction;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.input.DecimalInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class BeitragsgruppeControl extends AbstractControl
{
  private TablePart beitragsgruppeList;

  private Input bezeichnung;

  private DecimalInput betrag;

  private Beitragsgruppe beitrag;

  public BeitragsgruppeControl(AbstractView view)
  {
    super(view);
  }

  private Beitragsgruppe getBeitragsgruppe()
  {
    if (beitrag != null)
      return beitrag;
    beitrag = (Beitragsgruppe) getCurrentObject();
    return beitrag;
  }

  public Input getBezeichnung() throws RemoteException
  {
    if (bezeichnung != null)
      return bezeichnung;
    bezeichnung = new TextInput(getBeitragsgruppe().getBezeichnung(), 30);
    return bezeichnung;
  }

  public DecimalInput getBetrag() throws RemoteException
  {
    if (betrag != null)
    {
      return betrag;
    }
    betrag = new DecimalInput(getBeitragsgruppe().getBetrag(),
        Einstellungen.DECIMALFORMAT);
    return betrag;
  }

  public void handleStore()
  {
    try
    {
      Beitragsgruppe b = getBeitragsgruppe();
      b.setBezeichnung((String) getBezeichnung().getValue());
      Double d = (Double) getBetrag().getValue();
      b.setBetrag(d.doubleValue());
      try
      {
        b.store();
        GUI.getStatusBar().setSuccessText("Beitragsgruppe gespeichert");
      }
      catch (ApplicationException e)
      {
        GUI.getView().setErrorText(e.getMessage());
      }
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei speichern der Beitragsgruppe";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  public Part getBeitragsgruppeTable() throws RemoteException
  {
    if (beitragsgruppeList != null)
    {
      return beitragsgruppeList;
    }
    DBService service = Einstellungen.getDBService();
    DBIterator beitragsgruppen = service.createList(Beitragsgruppe.class);
    beitragsgruppeList = new TablePart(beitragsgruppen,
        new BeitragsgruppeDetailAction());
    beitragsgruppeList.addColumn("Bezeichnung", "bezeichnung");
    beitragsgruppeList.addColumn("Betrag", "betrag", new CurrencyFormatter("",
        Einstellungen.DECIMALFORMAT));
    return beitragsgruppeList;
  }
}
