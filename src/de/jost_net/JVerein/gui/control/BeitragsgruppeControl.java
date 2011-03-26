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
 * Revision 1.16  2010-11-17 04:49:31  jost
 * Erster Code zum Thema Arbeitseinsatz
 *
 * Revision 1.15  2009/07/24 20:16:56  jost
 * Focus auf erstes Feld setzen.
 *
 * Revision 1.14  2009/07/18 13:42:50  jost
 * Bugfix DecimalFormat
 *
 * Revision 1.13  2009/06/22 18:12:10  jost
 * Einheitliche Ausgabe von Fehlermeldungen in der Statusbar
 *
 * Revision 1.12  2009/06/21 08:52:21  jost
 * Vorbereitung I18N
 *
 * Revision 1.11  2008/12/19 06:52:57  jost
 * Überflüssiges Import-Statement entfernt
 *
 * Revision 1.10  2008/12/13 16:21:17  jost
 * Bugfix Beitragsart.
 *
 * Revision 1.9  2008/11/30 18:56:00  jost
 * Neu: Konfiguration der Spalten einer Tabelle
 *
 * Revision 1.8  2008/11/29 13:05:48  jost
 * Refactoring: Code-Optimierung
 *
 * Revision 1.7  2007/08/30 19:47:45  jost
 * Part -> TablePart
 *
 * Revision 1.6  2007/08/23 19:24:23  jost
 * Bug #11819 - Beitragsgruppen können jetzt gelöscht werden
 *
 * Revision 1.5  2007/03/25 16:56:48  jost
 * Beitragsart aufgenommen.
 *
 * Revision 1.4  2007/03/18 08:38:24  jost
 * Pflichtfelder gekennzeichnet
 *
 * Revision 1.3  2007/02/23 20:26:22  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.2  2006/12/20 20:25:44  jost
 * Patch von Ullrich Sch�fer, der die Primitive vs. Object Problematik adressiert.
 *
 * Revision 1.1  2006/09/20 15:38:30  jost
 * *** empty log message ***
 *
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
    buchungsart.setPleaseChoose("bitte ausw�hlen");
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
