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
 * Revision 1.2  2011-02-25 15:00:19  jost
 * Bugfix Verwendungszwecke bei der Übergabe an Hibiscus
 *
 * Revision 1.1  2011-02-23 18:02:03  jost
 * Neu: Kompakte Abbuchung
 *
 * Revision 1.51  2011-02-12 09:36:59  jost
 * Statische Codeanalyse mit Findbugs
 * Vorbereitung kompakte Abbuchung
 *
 * Revision 1.50  2011-01-27 22:23:13  jost
 * Neu: Speicherung von weiteren Adressen in der Mitgliedertabelle
 *
 * Revision 1.49  2011-01-09 14:30:24  jost
 * Stammdaten in die Einstellungen verschoben.
 *
 * Revision 1.48  2010-12-16 19:07:15  jost
 * Verwendungszweck2 auf 27 Zeichen verlängert.
 *
 * Revision 1.47  2010-10-15 09:58:29  jost
 * Code aufgeräumt
 *
 * Revision 1.46  2010-07-26 08:05:53  jost
 * - Mitgliedskonto für Zusatzbeträge
 * - Rechnungsinformationen und Manuelle Zahlungseingänge deaktiviert.
 *
 * Revision 1.45  2010-07-25 18:44:04  jost
 * Neu: Mitgliedskonto
 *
 * Revision 1.44  2010/05/20 18:07:26  jost
 * Close eingefügt.
 *
 * Revision 1.43  2010/05/18 20:21:08  jost
 * Anpassung Klassenname
 *
 * Revision 1.42  2010/04/25 13:55:54  jost
 * Vorarbeiten Mitgliedskonto
 *
 * Revision 1.41  2010/04/18 06:54:23  jost
 * Zusätzliche Prüfung der Bankverbindung.
 *
 * Revision 1.40  2010/02/28 20:03:47  jost
 * Mitgliedsnummer mit ausgeben.
 *
 * Revision 1.39  2010/02/28 15:17:28  jost
 * Mitgliedsnummer in den Verwendungszweck übernommen.
 *
 * Revision 1.38  2010/02/15 17:23:18  jost
 * Bugfix zu lange Namen mit Umlauten.
 *
 * Revision 1.37  2009/12/17 19:25:25  jost
 * Überflüssigen Code entfernt.
 *
 * Revision 1.36  2009/12/13 17:43:35  jost
 * Debugging-Infos entfernt.
 *
 * Revision 1.35  2009/12/06 21:41:23  jost
 * Bugfix ungültige Kontonummer
 *
 * Revision 1.34  2009/08/19 21:00:30  jost
 * Manuelle Buchungen auch für Zusatzbeträge.
 *
 * Revision 1.33  2009/07/30 18:23:18  jost
 * Bugfix DTAUS-Datei mit überlangen Namen
 *
 * Revision 1.32  2009/07/19 13:49:03  jost
 * Bugfix Abrechnung
 *
 * Revision 1.31  2009/06/29 19:44:03  jost
 * Bugfix Zusatzbeträge jetzt auch ohne Bankverbindung.
 *
 * Revision 1.30  2009/06/11 21:03:52  jost
 * Vorbereitung I18N
 *
 * Revision 1.29  2009/01/27 18:51:37  jost
 * Abbuchung auch für Mitglieder ohne Eintrittsdatum
 *
 * Revision 1.28  2009/01/03 07:45:58  jost
 * Keine Abbuchungen für ausgetretene Mitglieder
 *
 * Revision 1.27  2009/01/02 14:21:57  jost
 * Rechnungen für Zusatzbeträge implementiert.
 *
 * Revision 1.26  2008/12/22 21:18:57  jost
 * Zusatzabbuchung->Zusatzbetrag
 *
 * Revision 1.25  2008/12/19 06:54:27  jost
 * Keine Abrechnung bei Eintrittsdatum in der Zukunft
 *
 * Revision 1.24  2008/11/29 13:12:04  jost
 * Refactoring: Code-Optimierung
 *
 * Revision 1.23  2008/11/16 16:58:18  jost
 * Speicherung der Einstellung von Property-Datei in die Datenbank verschoben.
 *
 * Revision 1.22  2008/08/10 12:37:25  jost
 * Abbuchung -> Abrechnung
 * Vorbereitung der Rechnungserstellung
 *
 * Revision 1.21  2008/07/09 13:01:16  jost
 * OBanToo-Fehlermeldung an die OberflÃ¤che bringen
 *
 * Revision 1.20  2008/02/09 14:35:32  jost
 * Bugfix. Zusatzabbuchungen und Kursteilnehmer nur abbuchen, wenn das HÃ¤kchen gesetzt ist.
 *
 * Revision 1.19  2008/01/31 19:40:57  jost
 * JÃ¤hrliche, HalbjÃ¤hrliche und VierteljÃ¤hrliche Abbuchungen kÃ¶nnen jetzt separat ausgefÃ¼hrt werden.
 * BerÃ¼cksichtigung eines Stichtages fÃ¼r die Abbuchung
 *
 * Revision 1.18  2008/01/07 20:28:21  jost
 * Bugfix Rundungsproblem
 *
 * Revision 1.17  2007/12/30 10:10:07  jost
 * Neuer Rhytmus: Jahr, Vierteljahr und Monat
 *
 * Revision 1.16  2007/12/26 18:13:33  jost
 * Lastschriften kÃ¶nnen jetzt als Einzellastschriften oder Sammellastschriften direkt in Hibuscus verbucht werden.
 *
 * Revision 1.15  2007/12/21 13:36:10  jost
 * Ausgabe der DTAUS-Datei im PDF-Format
 *
 * Revision 1.14  2007/12/02 14:15:25  jost
 * Neu: Beitragsmodelle
 *
 * Revision 1.13  2007/12/02 13:43:43  jost
 * Neu: Beitragsmodelle
 *
 * Revision 1.12  2007/08/14 19:20:57  jost
 * Bugfix wenn keine Beitragsgruppe mit 0 ? existiert.
 *
 * Revision 1.11  2007/04/20 12:17:46  jost
 * Bugfix: Mehr als eine Beitragsgruppe beitragsfrei
 *
 * Revision 1.10  2007/03/30 13:25:40  jost
 * Wiederkehrende Zusatzabbuchungen.
 *
 * Revision 1.9  2007/03/13 19:58:26  jost
 * BeitrÃ¤ge, die nicht abgebucht werden (Bar/Ãœberweisung) werden in die Liste der manuellen ZahlungseingÃ¤nge eingetragen.
 *
 * Revision 1.8  2007/03/10 20:37:06  jost
 * Neu: Zahlungsweg
 *
 * Revision 1.7  2007/03/10 19:42:36  jost
 * Bugfix: Abbuchungsdatum wird jetzt gesetzt.
 *
 * Revision 1.6  2007/02/25 19:14:22  jost
 * Neu: Kursteilnehmer
 *
 * Revision 1.5  2007/02/23 20:28:04  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.4  2007/01/14 12:42:29  jost
 * Java 1.5-KompatibilitÃ¤t
 *
 * Revision 1.3  2006/12/20 20:25:44  jost
 * Patch von Ullrich Schäfer, der die Primitive vs. Object Problematik adressiert.
 *
 * Revision 1.2  2006/09/21 18:49:00  jost
 * überflüssiges Import-Statement entfernt.
 *
 * Revision 1.1  2006/09/20 15:39:24  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Hashtable;

import com.lowagie.text.DocumentException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.keys.Abrechnungsausgabe;
import de.jost_net.JVerein.keys.Abrechnungsmodi;
import de.jost_net.JVerein.keys.Beitragsmodel;
import de.jost_net.JVerein.keys.IntervallZusatzzahlung;
import de.jost_net.JVerein.keys.Zahlungsrhytmus;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Abrechnungslauf;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Konto;
import de.jost_net.JVerein.rmi.Kursteilnehmer;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.jost_net.JVerein.rmi.Zusatzbetrag;
import de.jost_net.JVerein.server.MitgliedUtils;
import de.jost_net.JVerein.util.Datum;
import de.jost_net.OBanToo.Dtaus.CSatz;
import de.jost_net.OBanToo.Dtaus.Dtaus2Pdf;
import de.jost_net.OBanToo.Dtaus.DtausDateiParser;
import de.jost_net.OBanToo.Dtaus.DtausDateiWriter;
import de.jost_net.OBanToo.Dtaus.DtausException;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.action.Program;
import de.willuhn.jameica.hbci.rmi.Lastschrift;
import de.willuhn.jameica.hbci.rmi.SammelLastschrift;
import de.willuhn.jameica.hbci.rmi.SammelTransferBuchung;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class Abrechnung
{
  AbbuchungParam param;

  public Abrechnung(AbbuchungParam param, ProgressMonitor monitor)
      throws Exception
  {

    XLastschriften lastschriften = new XLastschriften();
    this.param = param;

    Abrechnungslauf abrl = getAbrechnungslauf();
    Konto konto = getKonto();
    abrechnenMitglieder(lastschriften, monitor, abrl, konto);
    if (param.zusatzbetraege)
    {
      abbuchenZusatzbetraege(lastschriften, abrl, konto);
    }
    if (param.kursteilnehmer)
    {
      abbuchenKursteilnehmer(lastschriften);
    }

    FileOutputStream out = new FileOutputStream(param.dtausfile);

    // Vorbereitung: A-Satz bestücken und schreiben
    DtausDateiWriter dtaus = new DtausDateiWriter(out);
    dtaus.setABLZBank(Long.parseLong(Einstellungen.getEinstellung().getBlz()));
    dtaus.setADatum(new Date());
    dtaus.setAGutschriftLastschrift("LK");
    dtaus.setAKonto(Long.parseLong(Einstellungen.getEinstellung().getKonto()));
    dtaus.setAKundenname(Einstellungen.getEinstellung().getName());
    dtaus.writeASatz();

    if (param.kompakteabbuchung)
    {
      lastschriften.compact();
    }
    for (XLastschrift lastschrift : lastschriften.getLastschriften())
    {
      writeCSatz(dtaus, lastschrift);
    }

    // Ende der Abbuchung. Jetzt wird noch der E-Satz geschrieben. Die Werte
    // wurden beim Schreiben der C-Sätze ermittelt.
    dtaus.writeESatz();
    dtaus.close();

    // Gegenbuchung für das Mitgliedskonto schreiben
    if (Einstellungen.getEinstellung().getMitgliedskonto())
    {
      writeMitgliedskonto(null, new Date(), "Gegenbuchung", "", dtaus
          .getSummeBetraegeDecimal().doubleValue() * -1, abrl, true, getKonto());
    }

    if (param.abbuchungsausgabe == Abrechnungsausgabe.HIBISCUS_EINZELBUCHUNGEN
        || param.abbuchungsausgabe == Abrechnungsausgabe.HIBISCUS_SAMMELBUCHUNG)
    {
      buchenHibiscus();
    }
    monitor.log(JVereinPlugin.getI18n().tr("Anzahl Abrechnungen: {0}",
        new String[] { dtaus.getAnzahlSaetze() + "" }));
    monitor.log(JVereinPlugin.getI18n().tr("Gesamtsumme: {0} EUR",
        Einstellungen.DECIMALFORMAT.format(dtaus.getSummeBetraegeDecimal())));
    dtaus.close();
    monitor.setPercentComplete(100);
    if (param.dtausprint)
    {
      ausdruckenDTAUS(param.dtausfile.getAbsolutePath(), param.pdffile);
    }
  }

  private void abrechnenMitglieder(XLastschriften lastschriften,
      ProgressMonitor monitor, Abrechnungslauf abrl, Konto konto)
      throws Exception
  {
    // Ermittlung der beitragsfreien Beitragsgruppen
    StringBuilder beitragsfrei = new StringBuilder();
    DBIterator list = Einstellungen.getDBService().createList(
        Beitragsgruppe.class);
    list.addFilter("betrag = 0");
    int gr = 0; // Anzahl beitragsfreier Gruppen
    while (list.hasNext())
    {
      gr++;
      Beitragsgruppe b = (Beitragsgruppe) list.next();
      if (gr > 1)
      {
        beitragsfrei.append(" AND ");
      }
      beitragsfrei.append(" beitragsgruppe <> ");
      beitragsfrei.append(b.getID());
    }

    // Beitragsgruppen-Tabelle lesen und cachen
    list = Einstellungen.getDBService().createList(Beitragsgruppe.class);
    list.addFilter("betrag > 0");
    Hashtable<String, Double> beitr = new Hashtable<String, Double>();
    while (list.hasNext())
    {
      Beitragsgruppe b = (Beitragsgruppe) list.next();
      beitr.put(b.getID(), new Double(b.getBetrag()));
    }

    if (param.abbuchungsmodus != Abrechnungsmodi.KEINBEITRAG)
    {
      // Alle Mitglieder lesen
      list = Einstellungen.getDBService().createList(Mitglied.class);
      MitgliedUtils.setMitglied(list);

      // Das Mitglied muss bereits eingetreten sein
      list.addFilter("(eintritt <= ? or eintritt is null) ",
          new Object[] { new java.sql.Date(param.stichtag.getTime()) });
      // Das Mitglied darf noch nicht ausgetreten sein
      list.addFilter("(austritt is null or austritt > ?)",
          new Object[] { new java.sql.Date(param.stichtag.getTime()) });
      // Beitragsfreie Mitglieder können auch unberücksichtigt bleiben.
      if (beitragsfrei.length() > 0)
      {
        list.addFilter(beitragsfrei.toString());
      }
      // Bei Abbuchungen im Laufe des Jahres werden nur die Mitglieder
      // berücksichtigt, die ab einem bestimmten Zeitpunkt eingetreten sind.
      if (param.vondatum != null)
      {
        list.addFilter("eingabedatum >= ?", new Object[] { new java.sql.Date(
            param.vondatum.getTime()) });
      }
      if (Einstellungen.getEinstellung().getBeitragsmodel() == Beitragsmodel.MONATLICH12631)
      {
        if (param.abbuchungsmodus == Abrechnungsmodi.HAVIMO)
        {
          list.addFilter(
              "(zahlungsrhytmus = ? or zahlungsrhytmus = ? or zahlungsrhytmus = ?)",
              new Object[] { new Integer(Zahlungsrhytmus.HALBJAEHRLICH),
                  new Integer(Zahlungsrhytmus.VIERTELJAEHRLICH),
                  new Integer(Zahlungsrhytmus.MONATLICH) });
        }
        if (param.abbuchungsmodus == Abrechnungsmodi.JAVIMO)
        {
          list.addFilter(
              "(zahlungsrhytmus = ? or zahlungsrhytmus = ? or zahlungsrhytmus = ?)",
              new Object[] { new Integer(Zahlungsrhytmus.JAEHRLICH),
                  new Integer(Zahlungsrhytmus.VIERTELJAEHRLICH),
                  new Integer(Zahlungsrhytmus.MONATLICH) });
        }
        if (param.abbuchungsmodus == Abrechnungsmodi.VIMO)
        {
          list.addFilter("(zahlungsrhytmus = ? or zahlungsrhytmus = ?)",
              new Object[] { Integer.valueOf(Zahlungsrhytmus.VIERTELJAEHRLICH),
                  Integer.valueOf(Zahlungsrhytmus.MONATLICH) });
        }
        if (param.abbuchungsmodus == Abrechnungsmodi.MO)
        {
          list.addFilter("zahlungsrhytmus = ?",
              new Object[] { Integer.valueOf(Zahlungsrhytmus.MONATLICH) });
        }
        if (param.abbuchungsmodus == Abrechnungsmodi.VI)
        {
          list.addFilter(
              "zahlungsrhytmus = ?",
              new Object[] { Integer.valueOf(Zahlungsrhytmus.VIERTELJAEHRLICH) });
        }
        if (param.abbuchungsmodus == Abrechnungsmodi.HA)
        {
          list.addFilter("zahlungsrhytmus = ?",
              new Object[] { Integer.valueOf(Zahlungsrhytmus.HALBJAEHRLICH) });
        }
        if (param.abbuchungsmodus == Abrechnungsmodi.JA)
        {
          list.addFilter("zahlungsrhytmus = ?",
              new Object[] { Integer.valueOf(Zahlungsrhytmus.JAEHRLICH) });
        }
      }
      list.setOrder("ORDER BY name, vorname");
      // Sätze im Resultset
      monitor.log("Anzahl Sätze: " + list.size());

      int count = 0;
      while (list.hasNext())
      {
        monitor.setStatus((int) ((double) count / (double) list.size() * 100d));
        Mitglied m = (Mitglied) list.next();
        Double betr;
        if (Einstellungen.getEinstellung().getBeitragsmodel() != Beitragsmodel.MONATLICH12631)
        {
          betr = beitr.get(m.getBeitragsgruppeId() + "");
        }
        else
        {
          // Zur Vermeidung von Rundungsdifferenzen wird mit BigDecimal
          // gerechnet.
          try
          {
            BigDecimal bbetr = new BigDecimal(beitr.get(m.getBeitragsgruppeId()
                + ""));
            bbetr = bbetr.setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal bmonate = new BigDecimal(m.getZahlungsrhytmus());
            bbetr = bbetr.multiply(bmonate);
            betr = bbetr.doubleValue();
          }
          catch (NullPointerException e)
          {
            Logger.error(m.getVornameName() + ": " + m.getBeitragsgruppeId());
            DBIterator li = Einstellungen.getDBService().createList(
                Beitragsgruppe.class);
            while (li.hasNext())
            {
              Beitragsgruppe bg = (Beitragsgruppe) li.next();
              Logger.error("Beitragsgruppe: " + bg.getID() + ", "
                  + bg.getBezeichnung() + ", " + bg.getBetrag() + ", "
                  + bg.getBeitragsArt());
            }
            throw e;
          }
        }
        if (Einstellungen.getEinstellung().getMitgliedskonto())
        {
          writeMitgliedskonto(m, new Date(), param.verwendungszweck, "", betr,
              abrl, m.getZahlungsweg() == Zahlungsweg.ABBUCHUNG, konto);
        }
        if (m.getZahlungsweg() == Zahlungsweg.ABBUCHUNG)
        {
          try
          {
            XLastschrift lastschrift = new XLastschrift();
            lastschrift.setBetrag(betr);
            if (!Einstellungen.checkAccountCRC(m.getBlz(), m.getKonto()))
            {
              throw new DtausException(
                  JVereinPlugin
                      .getI18n()
                      .tr("BLZ/Kontonummer ({0}/{1}) ungültig. Bitte prüfen Sie Ihre Eingaben.",
                          new String[] { m.getBlz(), m.getKonto() }));
            }
            lastschrift.setBlz(Integer.parseInt(m.getBlz()));
            lastschrift.setKonto(Long.parseLong(m.getKonto()));
            lastschrift.addVerwendungszweck(param.verwendungszweck);
            lastschrift.addVerwendungszweck(getVerwendungszweck2(m));
            lastschrift.addZahlungspflichtigen(getZahlungspflichtigen(m));
            lastschriften.add(lastschrift);
          }
          catch (Exception e)
          {
            throw new ApplicationException(m.getNameVorname() + ": "
                + e.getMessage());
          }
        }
      }
    }
  }

  private void abbuchenZusatzbetraege(XLastschriften lastschriften,
      Abrechnungslauf abrl, Konto konto) throws NumberFormatException,
      IOException, ApplicationException
  {
    DBIterator list = Einstellungen.getDBService().createList(
        Zusatzbetrag.class);
    while (list.hasNext())
    {
      Zusatzbetrag z = (Zusatzbetrag) list.next();
      if (z.isAktiv())
      {
        Mitglied m = z.getMitglied();
        if (m.getZahlungsweg() == Zahlungsweg.ABBUCHUNG)
        {
          try
          {
            XLastschrift lastschrift = new XLastschrift();
            lastschrift.setBetrag(z.getBetrag());
            if (!Einstellungen.checkAccountCRC(m.getBlz(), m.getKonto()))
            {
              throw new DtausException(
                  JVereinPlugin
                      .getI18n()
                      .tr("BLZ/Kontonummer ({0}/{1}) ungültig. Bitte prüfen Sie Ihre Eingaben.",
                          new String[] { m.getBlz(), m.getKonto() }));
            }
            lastschrift.setBlz(Integer.parseInt(m.getBlz()));
            lastschrift.setKonto(Long.parseLong(m.getKonto()));
            lastschrift.addZahlungspflichtigen(getZahlungspflichtigen(m));
            lastschrift.addVerwendungszweck(z.getBuchungstext());
            lastschrift.addVerwendungszweck(getVerwendungszweck2(m));
            lastschriften.add(lastschrift);
          }
          catch (Exception e)
          {
            throw new ApplicationException(m.getNameVorname() + ": "
                + e.getMessage());
          }
        }
        if (z.getIntervall().intValue() != IntervallZusatzzahlung.KEIN
            && (z.getEndedatum() == null || z.getFaelligkeit().getTime() <= z
                .getEndedatum().getTime()))
        {
          z.setFaelligkeit(Datum.addInterval(z.getFaelligkeit(),
              z.getIntervall()));
        }
        z.setAusfuehrung(Datum.getHeute());
        z.store();
        if (Einstellungen.getEinstellung().getMitgliedskonto())
        {
          writeMitgliedskonto(m, new Date(), z.getBuchungstext(), "",
              z.getBetrag(), abrl, m.getZahlungsweg() == Zahlungsweg.ABBUCHUNG,
              konto);
        }

      }
    }
  }

  private void abbuchenKursteilnehmer(XLastschriften lastschriften)
      throws ApplicationException, DtausException, IOException
  {
    DBIterator list = Einstellungen.getDBService().createList(
        Kursteilnehmer.class);
    list.addFilter("abbudatum is null");
    while (list.hasNext())
    {
      Kursteilnehmer kt = (Kursteilnehmer) list.next();
      kt.setAbbudatum();
      kt.store();

      XLastschrift lastschrift = new XLastschrift();
      lastschrift.setBetrag(kt.getBetrag());
      if (!Einstellungen.checkAccountCRC(kt.getBlz(), kt.getKonto()))
      {
        throw new DtausException(
            JVereinPlugin
                .getI18n()
                .tr("BLZ/Kontonummer ({0}/{1}) ungültig. Bitte prüfen Sie Ihre Eingaben.",
                    new String[] { kt.getBlz(), kt.getKonto() }));
      }

      lastschrift.setBlz(Integer.parseInt(kt.getBlz()));
      lastschrift.setKonto(Long.parseLong(kt.getKonto()));
      lastschrift.addZahlungspflichtigen(kt.getName());
      lastschrift.addVerwendungszweck(kt.getVZweck1());
      lastschrift.addVerwendungszweck(kt.getVZweck2());
      lastschriften.add(lastschrift);
    }
  }

  private void ausdruckenDTAUS(String dtaus, String dtauspdf)
      throws IOException, DtausException, DocumentException
  {
    final String dtauspdffinal = dtauspdf;
    new Dtaus2Pdf(dtaus, dtauspdf);
    GUI.getDisplay().asyncExec(new Runnable()
    {

      public void run()
      {
        try
        {
          new Program().handleAction(new File(dtauspdffinal));
        }
        catch (ApplicationException ae)
        {
          Application.getMessagingFactory().sendMessage(
              new StatusBarMessage(ae.getLocalizedMessage(),
                  StatusBarMessage.TYPE_ERROR));
        }
      }
    });
  }

  private void buchenHibiscus() throws ApplicationException
  {
    try
    {
      DtausDateiParser parser = new DtausDateiParser(
          param.dtausfile.getAbsolutePath());
      SammelLastschrift sl = null;
      CSatz c = parser.next();
      if (param.abbuchungsausgabe == Abrechnungsausgabe.HIBISCUS_SAMMELBUCHUNG)
      {
        sl = (SammelLastschrift) param.service.createObject(
            SammelLastschrift.class, null);
        sl.setKonto(param.konto);
        sl.setBezeichnung(param.verwendungszweck);
      }
      while (c != null)
      {
        if (param.abbuchungsausgabe == Abrechnungsausgabe.HIBISCUS_EINZELBUCHUNGEN)
        {
          Lastschrift o = (Lastschrift) param.service.createObject(
              Lastschrift.class, null);
          o.setKonto(param.konto);
          o.setBetrag(c.getBetragInEuro());
          o.setZweck(c.getVerwendungszweck(1));
          o.setZweck2(c.getVerwendungszweck(2));
          if (c.getAnzahlVerwendungszwecke() > 2)
          {
            String[] weiterzwecke = new String[c.getAnzahlVerwendungszwecke() - 2];
            for (int i = 3; i <= c.getAnzahlVerwendungszwecke(); i++)
            {
              weiterzwecke[i - 3] = c.getVerwendungszweck(i);
            }
            o.setWeitereVerwendungszwecke(weiterzwecke);
          }
          o.setGegenkontoName(c.getNameEmpfaenger());
          o.setGegenkontoBLZ(c.getBlzEndbeguenstigt() + "");
          o.setGegenkontoNummer(c.getKontonummer() + "");
          o.store();
        }
        if (param.abbuchungsausgabe == Abrechnungsausgabe.HIBISCUS_SAMMELBUCHUNG)
        {
          SammelTransferBuchung o = sl.createBuchung();
          o.setBetrag(c.getBetragInEuro());
          o.setZweck(c.getVerwendungszweck(1));
          o.setZweck2(c.getVerwendungszweck(2));
          if (c.getAnzahlVerwendungszwecke() > 2)
          {
            final String[] weiterzwecke = new String[c
                .getAnzahlVerwendungszwecke() - 2];
            for (int i = 3; i <= c.getAnzahlVerwendungszwecke(); i++)
            {
              weiterzwecke[i - 3] = c.getVerwendungszweck(i);
            }
            o.setWeitereVerwendungszwecke(weiterzwecke);
          }
          o.setGegenkontoName(c.getNameEmpfaenger());
          o.setGegenkontoBLZ(c.getBlzEndbeguenstigt() + "");
          o.setGegenkontoNummer(c.getKontonummer() + "");
          o.store();
        }
        c = parser.next();
      }
    }
    catch (RemoteException e)
    {
      throw new ApplicationException(e);
    }
    catch (IOException e)
    {
      throw new ApplicationException("Fehler beim öffnen der DTAUS-Datei");
    }
    catch (DtausException e)
    {
      throw new ApplicationException("Fehler beim parsen der DTAUS-Datei: "
          + e.getMessage());
    }
  }

  private void writeCSatz(DtausDateiWriter dtaus, XLastschrift lastschrift)
      throws DtausException, NumberFormatException, IOException
  {
    dtaus.setCBetragInEuro(lastschrift.getBetrag());
    dtaus.setCBLZEndbeguenstigt(lastschrift.getBlz());
    dtaus.setCKonto(lastschrift.getKonto());
    dtaus.setCName(lastschrift.getZahlungspflichtigen(0));
    dtaus.setCInterneKundennummer(0L);
    if (lastschrift.getAnzahlZahlungspflichtige() >= 2)
    {
      dtaus.setCName2(lastschrift.getZahlungspflichtigen(1));
    }
    for (int i = 0; i < lastschrift.getAnzahlVerwendungszwecke(); i++)
    {
      dtaus.addCVerwendungszweck(lastschrift.getVerwendungszweck(i));
    }
    dtaus
        .setCTextschluessel(CSatz.TS_LASTSCHRIFT_EINZUGSERMAECHTIGUNGSVERFAHREN);
    dtaus.writeCSatz();
  }

  private Abrechnungslauf getAbrechnungslauf() throws RemoteException,
      ApplicationException
  {
    if (!Einstellungen.getEinstellung().getMitgliedskonto())
    {
      return null;
    }
    Abrechnungslauf abrl = (Abrechnungslauf) Einstellungen.getDBService()
        .createObject(Abrechnungslauf.class, null);
    abrl.setDatum(new Date());
    abrl.setAbbuchungsausgabe(param.abbuchungsausgabe);
    abrl.setDtausdruck(param.dtausprint);
    abrl.setEingabedatum(param.vondatum);
    abrl.setKursteilnehmer(param.kursteilnehmer);
    abrl.setModus(param.abbuchungsmodus);
    abrl.setStichtag(param.stichtag);
    abrl.setZahlungsgrund(param.verwendungszweck);
    abrl.setZusatzbetraege(param.zusatzbetraege);
    abrl.store();
    return abrl;
  }

  private void writeMitgliedskonto(Mitglied mitglied, Date datum,
      String zweck1, String zweck2, double betrag, Abrechnungslauf abrl,
      boolean haben, Konto konto) throws ApplicationException, RemoteException
  {
    Mitgliedskonto mk = null;
    if (mitglied != null) /*
                           * Mitglied darf dann null sein, wenn die Gegenbuchung
                           * geschrieben wird
                           */
    {
      mk = (Mitgliedskonto) Einstellungen.getDBService().createObject(
          Mitgliedskonto.class, null);
      mk.setAbrechnungslauf(abrl);
      mk.setZahlungsweg(mitglied.getZahlungsweg());
      mk.setBetrag(betrag);
      mk.setDatum(datum);
      mk.setMitglied(mitglied);
      mk.setZweck1(zweck1);
      mk.setZweck2(zweck2);
      mk.store();
    }
    if (haben)
    {
      Buchung buchung = (Buchung) Einstellungen.getDBService().createObject(
          Buchung.class, null);
      buchung.setAbrechnungslauf(abrl);
      buchung.setBetrag(betrag);
      buchung.setDatum(datum);
      buchung.setKonto(konto);
      buchung.setName(mitglied != null ? mitglied.getNameVorname() : "JVerein");
      buchung.setZweck(zweck1);
      buchung.setZweck2(zweck2);
      if (mk != null)
      {
        buchung.setMitgliedskonto(mk);
      }
      buchung.store();
    }
  }

  /**
   * Ist das Abbuchungskonto in der Buchführung eingerichtet?
   */
  private Konto getKonto() throws ApplicationException, RemoteException
  {
    if (!Einstellungen.getEinstellung().getMitgliedskonto())
    {
      return null;
    }
    DBIterator it = Einstellungen.getDBService().createList(Konto.class);
    it.addFilter("nummer = ?", new String[] { Einstellungen.getEinstellung()
        .getKonto() });
    if (it.size() != 1)
    {
      throw new ApplicationException("Konto "
          + Einstellungen.getEinstellung().getKonto()
          + " ist in der Buchführung nicht eingerichtet.");
    }
    Konto k = (Konto) it.next();
    return k;
  }

  private String getZahlungspflichtigen(Mitglied m) throws RemoteException
  {
    String zpfl = m.getNameVorname();
    if (m.getKontoinhaber().length() > 0)
    {
      zpfl = m.getKontoinhaber();
    }
    if (zpfl.length() > 27)
    {
      zpfl = zpfl.substring(0, 27);
    }
    return zpfl;
  }

  private String getVerwendungszweck2(Mitglied m) throws RemoteException
  {
    String mitgliedname = (Einstellungen.getEinstellung()
        .getExterneMitgliedsnummer() ? m.getExterneMitgliedsnummer() : m
        .getID())
        + "/" + m.getNameVorname();
    if (mitgliedname.length() > 27)
    {
      mitgliedname = mitgliedname.substring(0, 27);
    }
    return mitgliedname;
  }
}
