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
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;
import java.text.DecimalFormat;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.BeitragsgruppeDetailAction;
import de.jost_net.JVerein.gui.menu.BeitragsgruppeMenu;
import de.jost_net.JVerein.keys.ArtBeitragsart;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.input.DecimalInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class BeitragsgruppeControl extends AbstractControl
{
  private TablePart beitragsgruppeList;

  private Input bezeichnung;

  private DecimalInput betrag;

  private SelectInput beitragsart;

  private Beitragsgruppe beitrag;

  private DecimalInput arbeitseinsatzstunden;

  private DecimalInput arbeitseinsatzbetrag;

  private SelectInput buchungsart;

  public BeitragsgruppeControl(AbstractView view)
  {
    super(view);
  }

  private Beitragsgruppe getBeitragsgruppe()
  {
    if (beitrag != null)
    {
      return beitrag;
    }
    beitrag = (Beitragsgruppe) getCurrentObject();
    return beitrag;
  }

  public Input getBezeichnung(boolean withFocus) throws RemoteException
  {
    if (bezeichnung != null)
      return bezeichnung;
    bezeichnung = new TextInput(getBeitragsgruppe().getBezeichnung(), 30);
    bezeichnung.setMandatory(true);
    if (withFocus)
    {
      bezeichnung.focus();
    }
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
    betrag.setMandatory(true);
    return betrag;
  }

  public SelectInput getBeitragsArt() throws RemoteException
  {
    if (beitragsart != null)
    {
      return beitragsart;
    }
    beitragsart = new SelectInput(ArtBeitragsart.getArray(),
        new ArtBeitragsart(getBeitragsgruppe().getBeitragsArt()));
    return beitragsart;
  }

  public DecimalInput getArbeitseinsatzStunden() throws RemoteException
  {
    if (arbeitseinsatzstunden != null)
    {
      return arbeitseinsatzstunden;
    }
    arbeitseinsatzstunden = new DecimalInput(getBeitragsgruppe()
        .getArbeitseinsatzStunden(), new DecimalFormat("###,###.##"));
    return arbeitseinsatzstunden;
  }

  public DecimalInput getArbeitseinsatzBetrag() throws RemoteException
  {
    if (arbeitseinsatzbetrag != null)
    {
      return arbeitseinsatzbetrag;
    }
    arbeitseinsatzbetrag = new DecimalInput(getBeitragsgruppe()
        .getArbeitseinsatzBetrag(), new DecimalFormat("###,###.##"));
    return arbeitseinsatzbetrag;
  }

  public SelectInput getBuchungsart() throws RemoteException
  {
    if (buchungsart != null)
    {
      return buchungsart;
    }
    DBIterator it = Einstellungen.getDBService().createList(Buchungsart.class);
    buchungsart = new SelectInput(it, getBeitragsgruppe().getBuchungsart());
    buchungsart.setPleaseChoose("bitte auswählen");
    return buchungsart;
  }

  public void handleStore()
  {
    try
    {
      Beitragsgruppe b = getBeitragsgruppe();
      b.setBezeichnung((String) getBezeichnung(false).getValue());
      Double d = (Double) getBetrag().getValue();
      b.setBetrag(d.doubleValue());
      ArtBeitragsart ba = (ArtBeitragsart) getBeitragsArt().getValue();
      b.setBeitragsArt(ba.getKey());
      Buchungsart bua = (Buchungsart) getBuchungsart().getValue();
      if (bua != null)
      {
        b.setBuchungsart(bua);
      }
      d = (Double) getArbeitseinsatzStunden().getValue();
      b.setArbeitseinsatzStunden(d.doubleValue());
      d = (Double) getArbeitseinsatzBetrag().getValue();
      b.setArbeitseinsatzBetrag(d.doubleValue());
      b.store();
      GUI.getStatusBar().setSuccessText(
          JVereinPlugin.getI18n().tr("Beitragsgruppe gespeichert"));
    }
    catch (ApplicationException e)
    {
      GUI.getStatusBar().setErrorText(e.getMessage());
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Fehler bei speichern der Beitragsgruppe");
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  public TablePart getBeitragsgruppeTable() throws RemoteException
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
    if (Einstellungen.getEinstellung().getArbeitseinsatz())
    {
      beitragsgruppeList.addColumn("Arbeitseinsatz-Stunden",
          "arbeitseinsatzstunden", new CurrencyFormatter("",
              Einstellungen.DECIMALFORMAT));
      beitragsgruppeList.addColumn("Arbeitseinsatz-Betrag",
          "arbeitseinsatzbetrag", new CurrencyFormatter("",
              Einstellungen.DECIMALFORMAT));
    }
    beitragsgruppeList.addColumn("Buchungsart", "buchungsart");
    beitragsgruppeList.setContextMenu(new BeitragsgruppeMenu());
    return beitragsgruppeList;
  }
}
