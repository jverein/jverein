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
 * Revision 1.8  2008/07/18 20:11:53  jost
 * Neu: Spendenbescheinigung
 *
 * Revision 1.7  2008/06/28 16:58:42  jost
 * Neu: Jahresabschluss
 *
 * Revision 1.6  2008/05/25 19:36:26  jost
 * Neu: Jahressaldo
 *
 * Revision 1.5  2008/05/22 06:51:20  jost
 * Buchführung
 *
 * Revision 1.4  2007/12/22 08:25:43  jost
 * Neu: Jubiläenliste
 *
 * Revision 1.3  2007/09/06 17:16:36  jost
 * Korrekte Behandlung des Menüpunktes Auswertung | Kursteilnehmer
 *
 * Revision 1.2  2007/08/23 19:25:05  jost
 * Header korrigiert.
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.navigation;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.AbbuchungAction;
import de.jost_net.JVerein.gui.action.AnfangsbestandListAction;
import de.jost_net.JVerein.gui.action.AuswertungKursteilnehmerAction;
import de.jost_net.JVerein.gui.action.AuswertungMitgliedAction;
import de.jost_net.JVerein.gui.action.BuchungsListeAction;
import de.jost_net.JVerein.gui.action.BuchungsuebernahmeAction;
import de.jost_net.JVerein.gui.action.JahresabschlussListAction;
import de.jost_net.JVerein.gui.action.JahressaldoAction;
import de.jost_net.JVerein.gui.action.JubilaeenAction;
import de.jost_net.JVerein.gui.action.KontoListAction;
import de.jost_net.JVerein.gui.action.KursteilnehmerSucheAction;
import de.jost_net.JVerein.gui.action.ManuellerZahlungseingangListeAction;
import de.jost_net.JVerein.gui.action.MitgliedSucheAction;
import de.jost_net.JVerein.gui.action.SpendenbescheinigungListeAction;
import de.jost_net.JVerein.gui.action.StatistikMitgliedAction;
import de.jost_net.JVerein.gui.action.WiedervorlageListeAction;
import de.jost_net.JVerein.gui.action.ZusatzabbuchungListeAction;
import de.willuhn.jameica.gui.NavigationItem;
import de.willuhn.jameica.gui.extension.Extendable;
import de.willuhn.jameica.gui.extension.Extension;
import de.willuhn.logging.Logger;

public class MyExtension implements Extension
{

  /**
   * @see de.willuhn.jameica.gui.extension.Extension#extend(de.willuhn.jameica.gui.extension.Extendable)
   */
  public void extend(Extendable extendable)
  {
    try
    {
      NavigationItem jverein = (NavigationItem) extendable;
      jverein.addChild(new MyItem(jverein, "Mitglieder",
          new MitgliedSucheAction()));
      if (Einstellungen.isKursteilnehmer())
      {
        jverein.addChild(new MyItem(jverein, "Kursteilnehmer",
            new KursteilnehmerSucheAction()));
      }
      jverein
          .addChild(new MyItem(jverein, "Abrechnung", new AbbuchungAction()));
      if (Einstellungen.isZusatzabbuchung())
      {
        jverein.addChild(new MyItem(jverein, "Zusatzabbuchung",
            new ZusatzabbuchungListeAction()));
      }
      jverein.addChild(new MyItem(jverein, "Manueller Zahlungseingang",
          new ManuellerZahlungseingangListeAction()));
      if (Einstellungen.isWiedervorlage())
      {
        jverein.addChild(new MyItem(jverein, "Wiedervorlage",
            new WiedervorlageListeAction()));
      }
      jverein.addChild(new MyItem(jverein, "Spendenbescheinigung",
          new SpendenbescheinigungListeAction()));

      NavigationItem auswertung = null;
      auswertung = new MyItem(auswertung, "Auswertungen", null);
      auswertung.addChild(new MyItem(auswertung, "Mitglieder",
          new AuswertungMitgliedAction()));
      auswertung.addChild(new MyItem(auswertung, "Jubil�en",
          new JubilaeenAction()));
      if (Einstellungen.isKursteilnehmer())
      {
        auswertung.addChild(new MyItem(auswertung, "Kursteilnehmer",
            new AuswertungKursteilnehmerAction()));
      }
      auswertung.addChild(new MyItem(auswertung, "Statistik",
          new StatistikMitgliedAction()));
      jverein.addChild(auswertung);

      NavigationItem buchfuehrung = null;
      buchfuehrung = new MyItem(buchfuehrung, "Buchf�hrung", null);
      buchfuehrung.addChild(new MyItem(buchfuehrung, "Konten",
          new KontoListAction()));
      buchfuehrung.addChild(new MyItem(buchfuehrung, "Anfangsbestand",
          new AnfangsbestandListAction()));
      buchfuehrung.addChild(new MyItem(buchfuehrung, "Buchungs�bernahme",
          new BuchungsuebernahmeAction()));
      buchfuehrung.addChild(new MyItem(buchfuehrung, "Buchungen",
          new BuchungsListeAction()));
      buchfuehrung.addChild(new MyItem(buchfuehrung, "Jahressaldo",
          new JahressaldoAction()));
      buchfuehrung.addChild(new MyItem(buchfuehrung, "Jahresabschluss",
          new JahresabschlussListAction()));
      jverein.addChild(buchfuehrung);

    }
    catch (Exception e)
    {
      Logger.error("unable to extend navigation");
    }

  }

}

/*******************************************************************************
 * $Log$ Revision 1.8 2008/07/18 20:11:53 jost Neu:
 * Spendenbescheinigung Revision 1.7 2008/06/28 16:58:42 jost Neu:
 * Jahresabschluss
 * 
 * Revision 1.6 2008/05/25 19:36:26 jost Neu: Jahressaldo Revision 1.5
 * 2008/05/22 06:51:20 jost Buchführung Revision 1.4 2007/12/22 08:25:43 jost
 * Neu: Jubiläenliste Revision 1.3 2007/09/06 17:16:36 jost Korrekte Behandlung
 * des Menüpunktes Auswertung | Kursteilnehmer Revision 1.2 2007/08/23 19:25:05
 * jost Header korrigiert.
 * 
 * Revision 1.1 2007/08/22 20:43:40 jost Bug #011762
 * 
 ******************************************************************************/
