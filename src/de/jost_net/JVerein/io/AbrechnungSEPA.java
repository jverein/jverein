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
package de.jost_net.JVerein.io;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import com.itextpdf.text.DocumentException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.io.Adressbuch.Adressaufbereitung;
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
import de.jost_net.JVerein.rmi.Lastschrift;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.jost_net.JVerein.rmi.Zusatzbetrag;
import de.jost_net.JVerein.rmi.ZusatzbetragAbrechnungslauf;
import de.jost_net.JVerein.server.MitgliedUtils;
import de.jost_net.JVerein.util.Datum;
import de.jost_net.OBanToo.SEPA.BIC;
import de.jost_net.OBanToo.SEPA.IBAN;
import de.jost_net.OBanToo.SEPA.SEPAException;
import de.jost_net.OBanToo.SEPA.Basislastschrift.Basislastschrift;
import de.jost_net.OBanToo.SEPA.Basislastschrift.Basislastschrift2Pdf;
import de.jost_net.OBanToo.SEPA.Basislastschrift.MandatSequence;
import de.jost_net.OBanToo.SEPA.Basislastschrift.Zahler;
import de.jost_net.OBanToo.StringLatin.Zeichen;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.action.Program;
import de.willuhn.jameica.hbci.rmi.SepaLastSequenceType;
import de.willuhn.jameica.hbci.rmi.SepaLastType;
import de.willuhn.jameica.hbci.rmi.SepaLastschrift;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class AbrechnungSEPA
{
  private AbrechnungSEPAParam param;

  private Calendar sepagueltigkeit;

  private int counter = 0;

  public AbrechnungSEPA(AbrechnungSEPAParam param, ProgressMonitor monitor)
      throws Exception
  {
    this.param = param;

    if (Einstellungen.getEinstellung().getName() == null
        || Einstellungen.getEinstellung().getName().length() == 0
        || Einstellungen.getEinstellung().getIban() == null
        || Einstellungen.getEinstellung().getIban().length() == 0)
    {
      throw new ApplicationException(
          "Name des Vereins oder Bankverbindung fehlt. Bitte unter Administration|Einstellungen erfassen.");
    }

    if (Einstellungen.getEinstellung().getGlaeubigerID() == null
        || Einstellungen.getEinstellung().getGlaeubigerID().length() == 0)
    {
      throw new ApplicationException(
          "Gläubiger-ID fehlt. Gfls. unter https://extranet.bundesbank.de/scp/ oder http://www.oenb.at/idakilz/cid?lang=de beantragen und unter Administration|Einstellungen|Allgemein eintragen.\n"
              + "Zu Testzwecken kann DE98ZZZ09999999999 eingesetzt werden.");
    }

    Abrechnungslauf abrl = getAbrechnungslauf();

    sepagueltigkeit = Calendar.getInstance();
    sepagueltigkeit.add(Calendar.MONTH, -36);
    Basislastschrift lastschrift = new Basislastschrift();
    // Vorbereitung: Allgemeine Informationen einstellen
    lastschrift.setBIC(Einstellungen.getEinstellung().getBic());
    lastschrift.setGlaeubigerID(Einstellungen.getEinstellung()
        .getGlaeubigerID());
    lastschrift.setIBAN(Einstellungen.getEinstellung().getIban());
    lastschrift.setKomprimiert(param.kompakteabbuchung.booleanValue());
    lastschrift.setName(Zeichen.convert(Einstellungen.getEinstellung()
        .getName()));

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

    monitor.log(counter + " abgerechnete Fälle");

    lastschrift.setMessageID(abrl.getID());
    lastschrift.write(param.sepafile);

    ArrayList<Zahler> z = lastschrift.getZahler();
    BigDecimal summemitgliedskonto = new BigDecimal("0");
    for (Zahler za : z)
    {
      Lastschrift ls = (Lastschrift) Einstellungen.getDBService().createObject(
          Lastschrift.class, null);
      ls.setAbrechnungslauf(Integer.parseInt(abrl.getID()));

      assert (za instanceof JVereinZahler) : "Illegaler Zahlertyp in Sepa-Abrechnung detektiert.";

      JVereinZahler vza = (JVereinZahler) za;

      switch (vza.getPersonTyp())
      {
        case KURSTEILNEHMER:
          ls.setKursteilnehmer(Integer.parseInt(vza.getPersonId()));
          Kursteilnehmer k = (Kursteilnehmer) Einstellungen.getDBService()
              .createObject(Kursteilnehmer.class, vza.getPersonId());
          ls.setPersonenart(k.getPersonenart());
          ls.setAnrede(k.getAnrede());
          ls.setTitel(k.getTitel());
          ls.setName(k.getName());
          ls.setVorname(k.getVorname());
          ls.setStrasse(k.getStrasse());
          ls.setAdressierungszusatz(k.getAdressierungszusatz());
          ls.setPlz(k.getPlz());
          ls.setOrt(k.getOrt());
          ls.setStaat(k.getStaat());
          ls.setEmail(k.getEmail());
          break;
        case MITGLIED:
          ls.setMitglied(Integer.parseInt(vza.getPersonId()));
          Mitglied m = (Mitglied) Einstellungen.getDBService().createObject(
              Mitglied.class, vza.getPersonId());
          if (m.getKtoiName() == null || m.getKtoiName().length() == 0)
          {
            ls.setPersonenart(m.getPersonenart());
            ls.setAnrede(m.getAnrede());
            ls.setTitel(m.getTitel());
            ls.setName(m.getName());
            ls.setVorname(m.getVorname());
            ls.setStrasse(m.getStrasse());
            ls.setAdressierungszusatz(m.getAdressierungszusatz());
            ls.setPlz(m.getPlz());
            ls.setOrt(m.getOrt());
            ls.setStaat(m.getStaat());
            ls.setEmail(m.getEmail());
          }
          else
          {
            ls.setPersonenart(m.getKtoiPersonenart());
            ls.setAnrede(m.getKtoiAnrede());
            ls.setTitel(m.getKtoiTitel());
            ls.setName(m.getKtoiName());
            ls.setVorname(m.getKtoiVorname());
            ls.setStrasse(m.getKtoiStrasse());
            ls.setAdressierungszusatz(m.getKtoiAdressierungszusatz());
            ls.setPlz(m.getKtoiPlz());
            ls.setOrt(m.getKtoiOrt());
            ls.setStaat(m.getKtoiStaat());
            ls.setEmail(m.getKtoiEmail());
          }
          break;
        default:
          assert false : "Personentyp ist nicht implementiert";
      }
      ls.setBetrag(za.getBetrag().doubleValue());
      summemitgliedskonto = summemitgliedskonto.add(za.getBetrag());
      ls.setBIC(za.getBic());
      ls.setIBAN(za.getIban());
      ls.setMandatDatum(za.getMandatdatum());
      ls.setMandatSequence(za.getMandatsequence().getTxt());
      ls.setMandatID(za.getMandatid());
      ls.setVerwendungszweck(za.getVerwendungszweckOrig());
      ls.store();
    }

    // Gegenbuchung für das Mitgliedskonto schreiben
    if (!summemitgliedskonto.equals(new BigDecimal("0")))
    {
      writeMitgliedskonto(null, new Date(), "Gegenbuchung",
          summemitgliedskonto.doubleValue() * -1, abrl, true, getKonto(), null);
    }
    if (param.abbuchungsausgabe == Abrechnungsausgabe.HIBISCUS)
    {
      buchenHibiscus(z);
    }
    // monitor.log(JVereinPlugin.getI18n().tr(
    // "Anzahl Abbuchungen/Lastschrift: {0}",
    // new String[] { dtaus.getAnzahlSaetze() + "" }));
    // monitor.log(JVereinPlugin.getI18n().tr(
    // "Gesamtsumme Abbuchung/Lastschrift: {0} EUR",
    // Einstellungen.DECIMALFORMAT.format(dtaus.getSummeBetraegeDecimal())));
    // dtaus.close();
    monitor.setPercentComplete(100);
    if (param.sepaprint)
    {
      ausdruckenSEPA(lastschrift, param.pdffile);
    }
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

      int count = 0;
      while (list.hasNext())
      {
        monitor.setStatus((int) ((double) count / (double) list.size() * 100d));
        Mitglied m = (Mitglied) list.next();

        if (!checkSEPA(m, monitor))
        {
          continue;
        }
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
            if (Einstellungen.getEinstellung().getIndividuelleBeitraege()
                && m.getIndividuellerBeitrag() > 0)
            {
              bbetr = new BigDecimal(m.getIndividuellerBeitrag());
            }
            betr = bbetr.doubleValue();
          }
          catch (NullPointerException e)
          {
            Logger.error(Adressaufbereitung.getVornameName(m) + ": "
                + m.getBeitragsgruppeId());
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
        counter++;
        writeMitgliedskonto(m,
            m.getMandatSequence().getTxt().equals("FRST") ? param.faelligkeit1
                : param.faelligkeit2, param.verwendungszweck, betr, abrl,
            m.getZahlungsweg() == Zahlungsweg.BASISLASTSCHRIFT, konto,
            beitragsgruppe.get(m.getBeitragsgruppeId() + ""));
        if (m.getZahlungsweg() == Zahlungsweg.BASISLASTSCHRIFT)
        {
          try
          {
            JVereinZahler zahler = new JVereinZahler();
            zahler.setPersonId(m.getID());
            zahler.setPersonTyp(JVereinZahlerTyp.MITGLIED);
            zahler.setBetrag(new BigDecimal(betr).setScale(2,
                BigDecimal.ROUND_HALF_UP));
            new BIC(m.getBic()); // Prüfung des BIC
            zahler.setBic(m.getBic());
            new IBAN(m.getIban()); // Prüfung der IBAN
            zahler.setIban(m.getIban());
            zahler.setMandatid(m.getMandatID());
            zahler.setMandatdatum(m.getMandatDatum());
            zahler.setMandatsequence(m.getMandatSequence());
            zahler.setFaelligkeit(param.faelligkeit1, param.faelligkeit2, m
                .getMandatSequence().getCode());
            zahler.setVerwendungszweck(param.verwendungszweck + " "
                + getVerwendungszweck2(m));
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
            zahler.setName(m.getKontoinhaber(1));
            lastschrift.add(zahler);
          }
          catch (Exception e)
          {
            throw new ApplicationException(Adressaufbereitung.getNameVorname(m)
                + ": " + e.getMessage());
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
        if (m.isAngemeldet(param.stichtag)
            || Einstellungen.getEinstellung().getZusatzbetragAusgetretene())
        {
          //
        }
        else
        {
          continue;
        }
        if (!checkSEPA(m, monitor))
        {
          continue;
        }
        counter++;
        if (m.getZahlungsweg() == Zahlungsweg.BASISLASTSCHRIFT)
        {
          try
          {
            JVereinZahler zahler = new JVereinZahler();
            zahler.setPersonId(m.getID());
            zahler.setPersonTyp(JVereinZahlerTyp.MITGLIED);
            zahler.setBetrag(new BigDecimal(z.getBetrag()).setScale(2,
                BigDecimal.ROUND_HALF_UP));
            new BIC(m.getBic());
            new IBAN(m.getIban());
            zahler.setBic(m.getBic());
            zahler.setIban(m.getIban());
            zahler.setMandatid(m.getMandatID());
            zahler.setMandatdatum(m.getMandatDatum());
            zahler.setMandatsequence(m.getMandatSequence());
            zahler.setFaelligkeit(param.faelligkeit1, param.faelligkeit2, m
                .getMandatSequence().getCode());
            zahler.setName(m.getKontoinhaber(1));
            zahler.setVerwendungszweck(z.getBuchungstext());
            lastschrift.add(zahler);
          }
          catch (Exception e)
          {
            throw new ApplicationException(Adressaufbereitung.getNameVorname(m)
                + ": " + e.getMessage());
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
          Logger.error(Adressaufbereitung.getNameVorname(z.getMitglied()) + " "
              + debString, e);
          monitor.log(z.getMitglied().getName() + " " + debString + " " + e);
          throw e;
        }
        writeMitgliedskonto(m,
            m.getMandatSequence().getTxt().equals("FRST") ? param.faelligkeit1
                : param.faelligkeit2, z.getBuchungstext(), z.getBetrag(), abrl,
            m.getZahlungsweg() == Zahlungsweg.BASISLASTSCHRIFT, konto, null);
      }
    }
  }

  private void abbuchenKursteilnehmer(Basislastschrift lastschrift)
      throws ApplicationException, IOException
  {
    DBIterator list = Einstellungen.getDBService().createList(
        Kursteilnehmer.class);
    list.addFilter("abbudatum is null");
    while (list.hasNext())
    {
      counter++;
      Kursteilnehmer kt = (Kursteilnehmer) list.next();
      try
      {
        JVereinZahler zahler = new JVereinZahler();
        zahler.setPersonId(kt.getID());
        zahler.setPersonTyp(JVereinZahlerTyp.KURSTEILNEHMER);
        zahler.setBetrag(new BigDecimal(kt.getBetrag()).setScale(2,
            BigDecimal.ROUND_HALF_UP));
        new BIC(kt.getBic());
        new IBAN(kt.getIban());
        zahler.setBic(kt.getBic());
        zahler.setIban(kt.getIban());
        zahler.setMandatid(kt.getMandatID());
        zahler.setMandatdatum(kt.getMandatDatum());
        zahler.setMandatsequence(MandatSequence.OOFF);
        zahler.setFaelligkeit(param.faelligkeit1);
        zahler.setName(kt.getName());
        zahler.setVerwendungszweck(kt.getVZweck1());
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

  private void ausdruckenSEPA(final Basislastschrift lastschrift,
      final String pdf) throws IOException, DocumentException, SEPAException
  {
    new Basislastschrift2Pdf(lastschrift, pdf);
    GUI.getDisplay().asyncExec(new Runnable()
    {

      @Override
      public void run()
      {
        try
        {
          new Program().handleAction(new File(pdf));
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

  private void buchenHibiscus(ArrayList<Zahler> z) throws ApplicationException
  {
    try
    {
      Date d = new Date();
      for (Zahler za : z)
      {
        SepaLastschrift sl = (SepaLastschrift) param.service.createObject(
            SepaLastschrift.class, null);
        sl.setBetrag(za.getBetrag().doubleValue());
        sl.setCreditorId(Einstellungen.getEinstellung().getGlaeubigerID());
        sl.setGegenkontoName(za.getName());
        sl.setGegenkontoBLZ(za.getBic());
        sl.setGegenkontoNummer(za.getIban());
        sl.setKonto(param.konto);
        sl.setMandateId(za.getMandatid());
        sl.setSequenceType(SepaLastSequenceType.valueOf(za.getMandatsequence()
            .getTxt()));
        sl.setSignatureDate(za.getMandatdatum());
        sl.setTargetDate(za.getFaelligkeit());
        sl.setTermin(d);
        sl.setType(SepaLastType.CORE);
        sl.setZweck(za.getVerwendungszweck());
        sl.store();
      }
    }
    catch (RemoteException e)
    {
      throw new ApplicationException(e);
    }
    catch (SEPAException e)
    {
      throw new ApplicationException(e);
    }
  }

  private Abrechnungslauf getAbrechnungslauf() throws RemoteException,
      ApplicationException
  {
    Abrechnungslauf abrl = (Abrechnungslauf) Einstellungen.getDBService()
        .createObject(Abrechnungslauf.class, null);
    abrl.setDatum(new Date());
    abrl.setAbbuchungsausgabe(param.abbuchungsausgabe);
    abrl.setFaelligkeit(param.faelligkeit1);
    abrl.setFaelligkeit2(param.faelligkeit2);
    abrl.setDtausdruck(param.sepaprint);
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
      String zweck1, double betrag, Abrechnungslauf abrl, boolean haben,
      Konto konto, Beitragsgruppe beitragsgruppe) throws ApplicationException,
      RemoteException
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
      buchung.setName(mitglied != null ? Adressaufbereitung
          .getNameVorname(mitglied) : "JVerein");
      buchung.setZweck(zweck1);
      if (mk != null)
      {
        buchung.setMitgliedskonto(mk);
      }
      if (beitragsgruppe != null && beitragsgruppe.getBuchungsart() != null)
      {
        buchung
            .setBuchungsart(new Long(beitragsgruppe.getBuchungsart().getID()));
      }
      buchung.store();
    }
  }

  /**
   * Ist das Abbuchungskonto in der Buchführung eingerichtet?
   * 
   * @throws SEPAException
   */
  private Konto getKonto() throws ApplicationException, RemoteException,
      SEPAException
  {
    // Variante 1: IBAN
    DBIterator it = Einstellungen.getDBService().createList(Konto.class);
    it.addFilter("nummer = ?", Einstellungen.getEinstellung().getIban());
    if (it.size() == 1)
    {
      return (Konto) it.next();
    }
    // Variante 2: Kontonummer aus IBAN
    it = Einstellungen.getDBService().createList(Konto.class);
    IBAN iban = new IBAN(Einstellungen.getEinstellung().getIban());
    it.addFilter("nummer = ?", iban.getKonto());
    if (it.size() == 1)
    {
      return (Konto) it.next();
    }
    throw new ApplicationException(
        MessageFormat
            .format(
                "Weder Konto {0} noch Konto {1} ist in der Buchführung eingerichtet. Menu: Buchführung | Konten",
                Einstellungen.getEinstellung().getIban(), iban.getKonto()));
  }

  private String getVerwendungszweck2(Mitglied m) throws RemoteException
  {
    String mitgliedname = (Einstellungen.getEinstellung()
        .getExterneMitgliedsnummer() ? m.getExterneMitgliedsnummer() : m
        .getID())
        + "/" + Adressaufbereitung.getNameVorname(m);
    return mitgliedname;
  }

  private boolean checkSEPA(Mitglied m, ProgressMonitor monitor)
      throws RemoteException, ApplicationException
  {
    if (m.getZahlungsweg() == null
        || m.getZahlungsweg() != Zahlungsweg.BASISLASTSCHRIFT)
    {
      return true;
    }
    if (m.getLetzteLastschrift() != null
        && m.getLetzteLastschrift().before(sepagueltigkeit.getTime()))
    {
      monitor.log(Adressaufbereitung.getNameVorname(m)
          + ": Letzte Lastschrift ist älter als 36 Monate.");
      return false;
    }
    if (m.getMandatSequence().equals(MandatSequence.FRST)
        && m.getLetzteLastschrift() != null)
    {
      Mitglied m1 = (Mitglied) Einstellungen.getDBService().createObject(
          Mitglied.class, m.getID());
      m1.setMandatSequence(MandatSequence.RCUR);
      m1.store();
      m.setMandatSequence(MandatSequence.RCUR);
    }
    if (m.getMandatDatum() == Einstellungen.NODATE)
    {
      monitor.log(Adressaufbereitung.getNameVorname(m)
          + ": Kein Mandat-Datum vorhanden.");
      return false;
    }
    return true;
  }
}
