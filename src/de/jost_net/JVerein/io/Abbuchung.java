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
import de.jost_net.JVerein.gui.input.AbbuchungsmodusInput;
import de.jost_net.JVerein.keys.Abrechnungsausgabe;
import de.jost_net.JVerein.keys.Beitragsmodel;
import de.jost_net.JVerein.keys.IntervallZusatzzahlung;
import de.jost_net.JVerein.keys.Zahlungsrhytmus;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Abrechnung;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Kursteilnehmer;
import de.jost_net.JVerein.rmi.ManuellerZahlungseingang;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Zusatzbetrag;
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

public class Abbuchung
{
  public Abbuchung(AbbuchungParam param, ProgressMonitor monitor)
      throws Exception
  {
    FileOutputStream out = new FileOutputStream(param.dtausfile);

    // Vorbereitung: A-Satz bestücken und schreiben
    DtausDateiWriter dtaus = new DtausDateiWriter(out);
    dtaus.setABLZBank(Long.parseLong(param.stamm.getBlz()));
    dtaus.setADatum(new Date());
    dtaus.setAGutschriftLastschrift("LK");
    dtaus.setAKonto(Long.parseLong(param.stamm.getKonto()));
    dtaus.setAKundenname(param.stamm.getName());
    dtaus.writeASatz();

    abbuchenMitglieder(dtaus, param.abbuchungsmodus, param.stichtag,
        param.vondatum, monitor, param.verwendungszweck);
    if (param.zusatzbetraege)
    {
      abbuchenZusatzbetraege(dtaus);
    }
    if (param.kursteilnehmer)
    {
      abbuchenKursteilnehmer(dtaus);
    }
    // Ende der Abbuchung. Jetzt wird noch der E-Satz geschrieben. Die Werte
    // wurden beim Schreiben der C-Sätze ermittelt.
    dtaus.writeESatz();
    if (param.abbuchungsausgabe == Abrechnungsausgabe.HIBISCUS_EINZELBUCHUNGEN
        || param.abbuchungsausgabe == Abrechnungsausgabe.HIBISCUS_SAMMELBUCHUNG)
    {
      buchenHibiscus(param);
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

  private void abbuchenMitglieder(DtausDateiWriter dtaus, int modus,
      Date stichtag, Date vondatum, ProgressMonitor monitor,
      String verwendungszweck) throws Exception
  {
    // Ermittlung der beitragsfreien Beitragsgruppen
    String beitragsfrei = "";
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
        beitragsfrei += " AND ";
      }
      beitragsfrei += " beitragsgruppe <> " + b.getID();
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

    if (modus != AbbuchungsmodusInput.KEINBEITRAG)
    {
      // Alle Mitglieder lesen
      list = Einstellungen.getDBService().createList(Mitglied.class);

      // Das Mitglied muss bereits eingetreten sein
      list.addFilter("(eintritt <= ? or eintritt is null) ",
          new Object[] { new java.sql.Date(stichtag.getTime()) });
      // Das Mitglied darf noch nicht ausgetreten sein
      list.addFilter("(austritt is null or austritt > ?)",
          new Object[] { new java.sql.Date(stichtag.getTime()) });
      // Beitragsfreie Mitglieder können auch unberücksichtigt bleiben.
      if (beitragsfrei.length() > 0)
      {
        list.addFilter(beitragsfrei);
      }
      // Bei Abbuchungen im Laufe des Jahres werden nur die Mitglieder
      // berücksichtigt, die ab einem bestimmten Zeitpunkt eingetreten sind.
      if (vondatum != null)
      {
        list.addFilter("eingabedatum >= ?", new Object[] { new java.sql.Date(
            vondatum.getTime()) });
      }
      if (Einstellungen.getEinstellung().getBeitragsmodel() == Beitragsmodel.MONATLICH12631)
      {
        if (modus == AbbuchungsmodusInput.HAVIMO)
        {
          list
              .addFilter(
                  "(zahlungsrhytmus = ? or zahlungsrhytmus = ? or zahlungsrhytmus = ?)",
                  new Object[] { new Integer(Zahlungsrhytmus.HALBJAEHRLICH),
                      new Integer(Zahlungsrhytmus.VIERTELJAEHRLICH),
                      new Integer(Zahlungsrhytmus.MONATLICH) });
        }
        if (modus == AbbuchungsmodusInput.JAVIMO)
        {
          list
              .addFilter(
                  "(zahlungsrhytmus = ? or zahlungsrhytmus = ? or zahlungsrhytmus = ?)",
                  new Object[] { new Integer(Zahlungsrhytmus.JAEHRLICH),
                      new Integer(Zahlungsrhytmus.VIERTELJAEHRLICH),
                      new Integer(Zahlungsrhytmus.MONATLICH) });
        }
        if (modus == AbbuchungsmodusInput.VIMO)
        {
          list.addFilter("(zahlungsrhytmus = ? or zahlungsrhytmus = ?)",
              new Object[] { new Integer(Zahlungsrhytmus.VIERTELJAEHRLICH),
                  new Integer(Zahlungsrhytmus.MONATLICH) });
        }
        if (modus == AbbuchungsmodusInput.MO)
        {
          list.addFilter("zahlungsrhytmus = ?", new Object[] { new Integer(
              Zahlungsrhytmus.MONATLICH) });
        }
        if (modus == AbbuchungsmodusInput.VI)
        {
          list.addFilter("zahlungsrhytmus = ?", new Object[] { new Integer(
              Zahlungsrhytmus.VIERTELJAEHRLICH) });
        }
        if (modus == AbbuchungsmodusInput.HA)
        {
          list.addFilter("zahlungsrhytmus = ?", new Object[] { new Integer(
              Zahlungsrhytmus.HALBJAEHRLICH) });
        }
        if (modus == AbbuchungsmodusInput.JA)
        {
          list.addFilter("zahlungsrhytmus = ?", new Object[] { new Integer(
              Zahlungsrhytmus.JAEHRLICH) });
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
        Double betr = new Double(0d);
        if (Einstellungen.getEinstellung().getBeitragsmodel() != Beitragsmodel.MONATLICH12631)
        {
          betr = (Double) beitr.get(m.getBeitragsgruppeId() + "");
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
        if (m.getZahlungsweg() == Zahlungsweg.ABBUCHUNG)
        {
          try
          {
            writeCSatz(dtaus, m, verwendungszweck, betr);
          }
          catch (Exception e)
          {
            throw new ApplicationException(m.getNameVorname() + ": "
                + e.getMessage());
          }
        }
        else
        {
          writeManuellerZahlungseingang(m, verwendungszweck, betr);
        }
        writeAbrechungsdaten(m, verwendungszweck, m.getNameVorname(), betr);
      }
    }
  }

  private void abbuchenZusatzbetraege(DtausDateiWriter dtaus)
      throws NumberFormatException, DtausException, IOException,
      ApplicationException
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
            writeCSatz(dtaus, m, z.getBuchungstext(), new Double(z.getBetrag()));
          }
          catch (DtausException e)
          {
            throw new ApplicationException(m.getNameVorname() + ": "
                + e.getMessage());
          }
        }
        else
        {
          writeManuellerZahlungseingang(m, z.getBuchungstext(), new Double(z
              .getBetrag()));
        }
        if (z.getIntervall().intValue() != IntervallZusatzzahlung.KEIN
            && (z.getEndedatum() == null || z.getFaelligkeit().getTime() <= z
                .getEndedatum().getTime()))
        {
          z.setFaelligkeit(Datum.addInterval(z.getFaelligkeit(), z
              .getIntervall(), z.getEndedatum()));
        }
        z.setAusfuehrung(Datum.getHeute());
        z.store();
        writeAbrechungsdaten(m, z.getBuchungstext(), "", z.getBetrag());
      }
    }
  }

  private void abbuchenKursteilnehmer(DtausDateiWriter dtaus)
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

      dtaus.setCBetragInEuro(kt.getBetrag());
      try
      {
        dtaus.setCBLZEndbeguenstigt(Integer.parseInt(kt.getBlz()));
      }
      catch (NumberFormatException e)
      {
        throw new ApplicationException("Ungültige Bankleitzahl " + kt.getBlz());
      }
      dtaus.setCInterneKundennummer(Integer.parseInt(kt.getID() + 100000));
      try
      {
        dtaus.setCKonto(Long.parseLong(kt.getKonto()));
      }
      catch (NumberFormatException e)
      {
        throw new ApplicationException("Ungültige Kontonummer " + kt.getKonto());
      }
      dtaus.setCName(kt.getName());
      dtaus
          .setCTextschluessel(CSatz.TS_LASTSCHRIFT_EINZUGSERMAECHTIGUNGSVERFAHREN);
      dtaus.addCVerwendungszweck(kt.getVZweck1());
      dtaus.addCVerwendungszweck(kt.getVZweck2());
      dtaus.writeCSatz();
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

  private void buchenHibiscus(AbbuchungParam param) throws ApplicationException
  {
    try
    {
      DtausDateiParser parser = new DtausDateiParser(param.dtausfile
          .getAbsolutePath());
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

  private void writeCSatz(DtausDateiWriter dtaus, Mitglied m,
      String verwendungszweck, Double betr) throws DtausException,
      NumberFormatException, IOException
  {
    dtaus.setCBetragInEuro(betr.doubleValue());
    try
    {
      dtaus.setCBLZEndbeguenstigt(Integer.parseInt(m.getBlz()));
    }
    catch (NumberFormatException e)
    {
      throw new DtausException("Ungültige Bankleitzahl " + m.getBlz());
    }
    dtaus.setCInterneKundennummer(Integer.parseInt(m.getID()));
    try
    {
      dtaus.setCKonto(Long.parseLong(m.getKonto()));
    }
    catch (NumberFormatException e)
    {
      throw new DtausException("Ungültige Kontonummer " + m.getKonto());
    }
    String name = m.getNameVorname();
    String mitgliedname = name;
    if (mitgliedname.length() > 27)
    {
      mitgliedname = mitgliedname.substring(0, 27);
    }
    if (m.getKontoinhaber().length() > 0)
    {
      name = m.getKontoinhaber();
    }
    if (name.length() > 27)
    {
      name = name.substring(0, 27);
    }
    dtaus.setCName(name);
    dtaus
        .setCTextschluessel(CSatz.TS_LASTSCHRIFT_EINZUGSERMAECHTIGUNGSVERFAHREN);
    dtaus.addCVerwendungszweck(verwendungszweck);
    dtaus.addCVerwendungszweck(mitgliedname);
    dtaus.writeCSatz();
  }

  private void writeManuellerZahlungseingang(Mitglied m,
      String verwendungszweck, Double betr) throws RemoteException,
      ApplicationException
  {
    ManuellerZahlungseingang mz = (ManuellerZahlungseingang) Einstellungen
        .getDBService().createObject(ManuellerZahlungseingang.class, null);
    mz.setBetrag(betr);
    mz.setEingabedatum();
    String name = m.getName() + ", " + m.getVorname();
    if (m.getKontoinhaber().length() > 0)
    {
      name = m.getKontoinhaber();
    }
    if (name.length() > 27)
    {
      name = name.substring(0, 27);
    }
    mz.setName(name);
    mz.setVZweck1(verwendungszweck);
    mz.setVZweck2(m.getName() + "," + m.getVorname());
    mz.store();
  }

  private void writeAbrechungsdaten(Mitglied m, String zweck1, String zweck2,
      double betrag) throws RemoteException, ApplicationException
  {
    if ((m.getZahlungsweg() == Zahlungsweg.ABBUCHUNG && Einstellungen
        .getEinstellung().getRechnungFuerAbbuchung())
        || (m.getZahlungsweg() == Zahlungsweg.ÜBERWEISUNG && Einstellungen
            .getEinstellung().getRechnungFuerUeberweisung())
        || (m.getZahlungsweg() == Zahlungsweg.BARZAHLUNG && Einstellungen
            .getEinstellung().getRechnungFuerBarzahlung()))
    {
      Abrechnung abr = (Abrechnung) Einstellungen.getDBService().createObject(
          Abrechnung.class, null);
      abr.setMitglied(m);
      abr.setZweck1(zweck1);
      abr.setZweck2(zweck2);
      abr.setDatum(new Date());
      abr.setBetrag(betrag);
      abr.store();
    }
  }
}
