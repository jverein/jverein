/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
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
package de.jost_net.JVerein.io;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Hashtable;

import com.itextpdf.text.DocumentException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.keys.Abrechnungsausgabe;
import de.jost_net.JVerein.keys.Abrechnungsmodi;
import de.jost_net.JVerein.keys.ArtBeitragsart;
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
import de.jost_net.JVerein.rmi.ZusatzbetragAbrechnungslauf;
import de.jost_net.JVerein.server.MitgliedUtils;
import de.jost_net.JVerein.util.Datum;
import de.jost_net.OBanToo.Dtaus.CSatz;
import de.jost_net.OBanToo.Dtaus.Dtaus2Pdf;
import de.jost_net.OBanToo.Dtaus.DtausDateiParser;
import de.jost_net.OBanToo.Dtaus.DtausException;
import de.jost_net.OBanToo.SEPA.BIC;
import de.jost_net.OBanToo.SEPA.IBAN;
import de.jost_net.OBanToo.SEPA.Basislastschrift.Basislastschrift;
import de.jost_net.OBanToo.SEPA.Basislastschrift.Zahler;
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

public class AbrechnungSEPA
{

  private AbrechnungSEPAParam param;

  public AbrechnungSEPA(AbrechnungSEPAParam param, ProgressMonitor monitor)
      throws Exception
  {
    this.param = param;
    Abrechnungslauf abrl = getAbrechnungslauf();

    Basislastschrift lastschrift = new Basislastschrift();
    // Vorbereitung: Allgemeine Informationen einstellen
    lastschrift.setBIC(Einstellungen.getEinstellung().getBic());
    lastschrift.setFaelligskeitsdatum(new Date()); // In parameter übernehmen
    lastschrift.setGlaeubigerID(Einstellungen.getEinstellung()
        .getGlaeubigerID());
    lastschrift.setIBAN(Einstellungen.getEinstellung().getIban());
    lastschrift.setKomprimiert(param.kompakteabbuchung.booleanValue());
    lastschrift.setName(Einstellungen.getEinstellung().getNameLang());

    Konto konto = getKonto();
    abrechnenMitglieder(lastschrift, monitor, abrl, konto);
    if (param.zusatzbetraege)
    {
      abbuchenZusatzbetraege(lastschrift, abrl, konto, monitor);
    }
    if (param.kursteilnehmer)
    {
      abbuchenKursteilnehmer(lastschrift);
    }

    lastschrift.setMessageID(abrl.getID());
    lastschrift.create(param.dtausfile);

    // Gegenbuchung für das Mitgliedskonto schreiben
    writeMitgliedskonto(null, new Date(), "Gegenbuchung", "", lastschrift
        .getKontrollsumme().doubleValue() * -1, abrl, true, getKonto(), null);

    // if (param.abbuchungsausgabe ==
    // Abrechnungsausgabe.HIBISCUS_EINZELBUCHUNGEN
    // || param.abbuchungsausgabe == Abrechnungsausgabe.HIBISCUS_SAMMELBUCHUNG)
    // {
    // buchenHibiscus();
    // }
    // monitor.log(JVereinPlugin.getI18n().tr(
    // "Anzahl Abbuchungen/Lastschrift: {0}",
    // new String[] { dtaus.getAnzahlSaetze() + "" }));
    // monitor.log(JVereinPlugin.getI18n().tr(
    // "Gesamtsumme Abbuchung/Lastschrift: {0} EUR",
    // Einstellungen.DECIMALFORMAT.format(dtaus.getSummeBetraegeDecimal())));
    // dtaus.close();
    monitor.setPercentComplete(100);
    // if (param.dtausprint)
    // {
    // ausdruckenDTAUS(param.dtausfile.getAbsolutePath(), param.pdffile);
    // }
  }

  private void abrechnenMitglieder(Basislastschrift lastschrift,
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
    Hashtable<String, Beitragsgruppe> beitragsgruppe = new Hashtable<String, Beitragsgruppe>();
    while (list.hasNext())
    {
      Beitragsgruppe b = (Beitragsgruppe) list.next();
      beitragsgruppe.put(b.getID(), b);
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
      monitor.log(MessageFormat.format("Anzahl Sätze: {0}", list.size() + ""));

      int count = 0;
      while (list.hasNext())
      {
        monitor.setStatus((int) ((double) count / (double) list.size() * 100d));
        Mitglied m = (Mitglied) list.next();
        Double betr;
        if (Einstellungen.getEinstellung().getBeitragsmodel() != Beitragsmodel.MONATLICH12631)
        {
          betr = beitragsgruppe.get(m.getBeitragsgruppeId() + "").getBetrag();
          if (Einstellungen.getEinstellung().getIndividuelleBeitraege()
              && m.getIndividuellerBeitrag() > 0)
          {
            betr = m.getIndividuellerBeitrag();
          }
        }
        else
        {
          // Zur Vermeidung von Rundungsdifferenzen wird mit BigDecimal
          // gerechnet.
          try
          {
            BigDecimal bbetr = new BigDecimal(beitragsgruppe.get(
                m.getBeitragsgruppeId() + "").getBetrag());
            bbetr = bbetr.setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal bmonate = new BigDecimal(m.getZahlungsrhytmus());
            bbetr = bbetr.multiply(bmonate);
            betr = bbetr.doubleValue();
            if (Einstellungen.getEinstellung().getIndividuelleBeitraege()
                && m.getIndividuellerBeitrag() > 0)
            {
              betr = m.getIndividuellerBeitrag();
            }
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
        writeMitgliedskonto(m, new Date(), param.verwendungszweck, "", betr,
            abrl, m.getZahlungsweg() == Zahlungsweg.DTAUS, konto,
            beitragsgruppe.get(m.getBeitragsgruppeId() + ""));
        if (m.getZahlungsweg() == Zahlungsweg.DTAUS)
        {
          try
          {
            Zahler zahler = new Zahler();
            zahler.setBetrag(new BigDecimal(betr).setScale(2,
                BigDecimal.ROUND_HALF_UP));
            new BIC(m.getBic()); // Prüfung des BIC
            zahler.setBic(m.getBic());
            new IBAN(m.getIban()); // Prüfung der IBAN
            zahler.setIban(m.getIban());
            zahler.setMandatid(m.getID());
            zahler.setMandatdatum(m.getMandatDatum());
            zahler.setVerwendungszweck(param.verwendungszweck);
            if (m.getBeitragsgruppe().getBeitragsArt() == ArtBeitragsart.FAMILIE_ZAHLER)
            {
              DBIterator angeh = Einstellungen.getDBService().createList(
                  Mitglied.class);
              angeh.addFilter("zahlerid = ?", m.getID());
              String an = "";
              int i = 0;
              while (angeh.hasNext())
              {
                Mitglied a = (Mitglied) angeh.next();
                if (i > 0)
                {
                  an += ", ";
                }
                i++;
                an += a.getVorname();
              }
              if (an.length() > 27)
              {
                an = an.substring(0, 24) + "...";
              }
              zahler.setVerwendungszweck(zahler.getVerwendungszweck() + " "
                  + an);
            }
            zahler.setName(getZahlungspflichtigen(m));
            lastschrift.add(zahler);
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

  private void abbuchenZusatzbetraege(Basislastschrift lastschrift,
      Abrechnungslauf abrl, Konto konto, ProgressMonitor monitor)
      throws NumberFormatException, IOException, ApplicationException
  {
    DBIterator list = Einstellungen.getDBService().createList(
        Zusatzbetrag.class);
    while (list.hasNext())
    {
      Zusatzbetrag z = (Zusatzbetrag) list.next();
      if (z.isAktiv())
      {
        Mitglied m = z.getMitglied();
        if (m.isAngemeldet(param.stichtag))
        {
          //
        }
        else
        {
          continue;
        }
        if (m.getZahlungsweg() == Zahlungsweg.DTAUS)
        {
          try
          {
            Zahler zahler = new Zahler();
            zahler.setBetrag(new BigDecimal(z.getBetrag()).setScale(2,
                BigDecimal.ROUND_HALF_UP));
            new BIC(m.getBic());
            new IBAN(m.getIban());
            zahler.setBic(m.getBic());
            zahler.setIban(m.getIban());
            zahler.setMandatid(m.getID());
            zahler.setMandatdatum(m.getMandatDatum());
            zahler.setName(getZahlungspflichtigen(m));
            String verwendungszweck = z.getBuchungstext();
            if (z.getBuchungstext2() != null
                && z.getBuchungstext2().length() > 0)
            {
              verwendungszweck += z.getBuchungstext2();
            }
            zahler.setVerwendungszweck(verwendungszweck);
            lastschrift.add(zahler);
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
        try
        {
          if (abrl != null)
          {
            ZusatzbetragAbrechnungslauf za = (ZusatzbetragAbrechnungslauf) Einstellungen
                .getDBService().createObject(ZusatzbetragAbrechnungslauf.class,
                    null);
            za.setAbrechnungslauf(abrl);
            za.setZusatzbetrag(z);
            za.setLetzteAusfuehrung(z.getAusfuehrung());
            za.store();
            z.setAusfuehrung(Datum.getHeute());
            z.store();
          }
        }
        catch (ApplicationException e)
        {
          String debString = z.getStartdatum() + ", " + z.getEndedatum() + ", "
              + z.getIntervallText() + ", " + z.getBuchungstext() + ", "
              + z.getBetrag();
          Logger.error(z.getMitglied().getNameVorname() + " " + debString, e);
          monitor.log(z.getMitglied().getName() + " " + debString + " " + e);
          throw e;
        }
        writeMitgliedskonto(m, new Date(), z.getBuchungstext(),
            z.getBuchungstext2() != null ? z.getBuchungstext2() : "",
            z.getBetrag(), abrl, m.getZahlungsweg() == Zahlungsweg.DTAUS,
            konto, null);
      }
    }
  }

  private void abbuchenKursteilnehmer(Basislastschrift lastschrift)
      throws ApplicationException, DtausException, IOException
  {
    DBIterator list = Einstellungen.getDBService().createList(
        Kursteilnehmer.class);
    list.addFilter("abbudatum is null");
    while (list.hasNext())
    {
      Kursteilnehmer kt = (Kursteilnehmer) list.next();
      try
      {
        Zahler zahler = new Zahler();
        zahler.setBetrag(new BigDecimal(kt.getBetrag()).setScale(2,
            BigDecimal.ROUND_HALF_UP));
        new BIC(kt.getBic());
        new IBAN(kt.getIban());
        zahler.setBic(kt.getBic());
        zahler.setIban(kt.getIban());
        zahler.setMandatid("K" + kt.getID());
        zahler.setMandatdatum(new Date()); // TODO korrektes Datum setzen.
        zahler.setName(kt.getName());
        zahler.setVerwendungszweck(kt.getVZweck1() + " " + kt.getVZweck2());
        lastschrift.add(zahler);
        kt.setAbbudatum();
        kt.store();
      }
      catch (Exception e)
      {
        throw new ApplicationException(kt.getName() + ": " + e.getMessage());
      }
    }
  }

  private void ausdruckenDTAUS(String dtaus, String dtauspdf)
      throws IOException, DtausException, DocumentException
  {
    final String dtauspdffinal = dtauspdf;
    new Dtaus2Pdf(dtaus, dtauspdf);
    GUI.getDisplay().asyncExec(new Runnable()
    {
      @Override
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
          o.setTextSchluessel(Einstellungen.getEinstellung()
              .getDtausTextschluessel());
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
          o.setTextSchluessel(Einstellungen.getEinstellung()
              .getDtausTextschluessel());
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

  private Abrechnungslauf getAbrechnungslauf() throws RemoteException,
      ApplicationException
  {
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
      boolean haben, Konto konto, Beitragsgruppe beitragsgruppe)
      throws ApplicationException, RemoteException
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
      if (mk != null)
      {
        buchung.setMitgliedskonto(mk);
      }
      if (beitragsgruppe != null && beitragsgruppe.getBuchungsart() != null)
      {
        buchung.setBuchungsart(new Integer(beitragsgruppe.getBuchungsart()
            .getID()));
      }
      buchung.store();
    }
  }

  /**
   * Ist das Abbuchungskonto in der Buchführung eingerichtet?
   */
  private Konto getKonto() throws ApplicationException, RemoteException
  {
    DBIterator it = Einstellungen.getDBService().createList(Konto.class);
    it.addFilter("nummer = ?", Einstellungen.getEinstellung().getKonto());
    if (it.size() != 1)
    {
      throw new ApplicationException(
          MessageFormat
              .format(
                  "Konto {0} ist in der Buchführung nicht eingerichtet. Menu: Buchführung | Konten",
                  Einstellungen.getEinstellung().getKonto()));
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
    zpfl = dtaus27(zpfl);
    return zpfl;
  }

  private String getVerwendungszweck2(Mitglied m) throws RemoteException
  {
    String mitgliedname = (Einstellungen.getEinstellung()
        .getExterneMitgliedsnummer() ? m.getExterneMitgliedsnummer() : m
        .getID())
        + "/" + m.getNameVorname();
    mitgliedname = dtaus27(mitgliedname);
    return mitgliedname;
  }

  public static String dtaus27(String in)
  {
    String out = in;
    if (in.length() > 27)
    {
      out = in.substring(0, 27);
    }
    while (out.length() < 27)
    {
      out += " ";
    }
    int lae = out.length();
    for (int i = 0; i < out.length(); i++)
    {
      Character c = out.charAt(i);
      if (c.equals('Ä') || c.equals('Ö') || c.equals('Ü') || c.equals('ä')
          || c.equals('ö') || c.equals('ü') || c.equals('ß'))
      {
        lae--;
      }
    }
    out = out.substring(0, lae);
    return out;
  }

}
