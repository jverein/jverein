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
import java.text.DecimalFormat;

import de.jost_net.JVerein.Einstellungen;
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

  private DecimalInput betragmonatlich;

  private DecimalInput betragvierteljaehrlich;

  private DecimalInput betraghalbjaehrlich;

  private DecimalInput betragjaehrlich;

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
    return betrag;
  }

  public DecimalInput getBetragMonatlich() throws RemoteException
  {
    if (betragmonatlich != null)
    {
      return betragmonatlich;
    }
    betragmonatlich = new DecimalInput(
        getBeitragsgruppe().getBetragMonatlich(), Einstellungen.DECIMALFORMAT);
    return betragmonatlich;
  }

  public DecimalInput getBetragVierteljaehrlich() throws RemoteException
  {
    if (betragvierteljaehrlich != null)
    {
      return betragvierteljaehrlich;
    }
    betragvierteljaehrlich = new DecimalInput(getBeitragsgruppe()
        .getBetragVierteljaehrlich(), Einstellungen.DECIMALFORMAT);
    return betragvierteljaehrlich;
  }

  public DecimalInput getBetragHalbjaehrlich() throws RemoteException
  {
    if (betraghalbjaehrlich != null)
    {
      return betraghalbjaehrlich;
    }
    betraghalbjaehrlich = new DecimalInput(getBeitragsgruppe()
        .getBetragHalbjaehrlich(), Einstellungen.DECIMALFORMAT);
    return betraghalbjaehrlich;
  }

  public DecimalInput getBetragJaehrlich() throws RemoteException
  {
    if (betragjaehrlich != null)
    {
      return betragjaehrlich;
    }
    betragjaehrlich = new DecimalInput(
        getBeitragsgruppe().getBetragJaehrlich(), Einstellungen.DECIMALFORMAT);
    return betragjaehrlich;
  }

  public SelectInput getBeitragsArt() throws RemoteException
  {
    if (beitragsart != null)
    {
      return beitragsart;
    }
    beitragsart = new SelectInput(ArtBeitragsart.values(), getBeitragsgruppe()
        .getBeitragsArt());
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
      switch (Einstellungen.getEinstellung().getBeitragsmodel())
      {
        case GLEICHERTERMINFUERALLE:
        case MONATLICH12631:
          Double d = (Double) getBetrag().getValue();
          b.setBetrag(d.doubleValue());
          break;
        case FLEXIBEL:
          Double d1 = (Double) getBetragMonatlich().getValue();
          b.setBetragMonatlich(d1.doubleValue());
          Double d3 = (Double) getBetragVierteljaehrlich().getValue();
          b.setBetragVierteljaehrlich(d3.doubleValue());
          Double d6 = (Double) getBetragHalbjaehrlich().getValue();
          b.setBetragHalbjaehrlich(d6.doubleValue());
          Double d12 = (Double) getBetragJaehrlich().getValue();
          b.setBetragJaehrlich(d12.doubleValue());
          break;
      }
      ArtBeitragsart ba = (ArtBeitragsart) getBeitragsArt().getValue();
      // if (ba.getKey() == ArtBeitragsart.FAMILIE_ANGEHOERIGER && d != 0)
      // {
      // throw new ApplicationException(
      // "Familien-Angehörige sind beitragsbefreit. Bitte als Betrag 0,00 eingeben.");
      // }
      b.setBeitragsArt(ba.getKey());
      Buchungsart bua = (Buchungsart) getBuchungsart().getValue();
      if (bua != null)
      {
        b.setBuchungsart(bua);
      }
      Double d = (Double) getArbeitseinsatzStunden().getValue();
      b.setArbeitseinsatzStunden(d.doubleValue());
      d = (Double) getArbeitseinsatzBetrag().getValue();
      b.setArbeitseinsatzBetrag(d.doubleValue());
      b.store();
      GUI.getStatusBar().setSuccessText("Beitragsgruppe gespeichert");
    }
    catch (ApplicationException e)
    {
      GUI.getStatusBar().setErrorText(e.getMessage());
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei speichern der Beitragsgruppe";
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
    switch (Einstellungen.getEinstellung().getBeitragsmodel())
    {
      case GLEICHERTERMINFUERALLE:
      case MONATLICH12631:
        beitragsgruppeList.addColumn("Betrag", "betrag", new CurrencyFormatter(
            "", Einstellungen.DECIMALFORMAT));
        break;
      case FLEXIBEL:
        beitragsgruppeList.addColumn("Betrag monatlich", "betragmonatlich",
            new CurrencyFormatter("", Einstellungen.DECIMALFORMAT));
        beitragsgruppeList.addColumn("Betrag viertelj.",
            "betragvierteljaehrlich", new CurrencyFormatter("",
                Einstellungen.DECIMALFORMAT));
        beitragsgruppeList.addColumn("Betrag halbj.", "betraghalbjaehrlich",
            new CurrencyFormatter("", Einstellungen.DECIMALFORMAT));
        beitragsgruppeList.addColumn("Betrag jährlich", "betragjaehrlich",
            new CurrencyFormatter("", Einstellungen.DECIMALFORMAT));
        break;
    }
    if (Einstellungen.getEinstellung().getArbeitseinsatz())
    {
      beitragsgruppeList.addColumn("Arbeitseinsatz-Stunden",
          "arbeitseinsatzstunden", new CurrencyFormatter("",
              Einstellungen.DECIMALFORMAT));
      beitragsgruppeList.addColumn("Arbeitseinsatz-Stundensatz",
          "arbeitseinsatzbetrag", new CurrencyFormatter("",
              Einstellungen.DECIMALFORMAT));
    }
    beitragsgruppeList.addColumn("Buchungsart", "buchungsart");
    beitragsgruppeList.setContextMenu(new BeitragsgruppeMenu());
    return beitragsgruppeList;
  }
}
