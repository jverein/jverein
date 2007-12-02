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

import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Hashtable;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.input.AbbuchungsmodusInput;
import de.jost_net.JVerein.gui.input.BeitragsmodelInput;
import de.jost_net.JVerein.gui.input.IntervallInput;
import de.jost_net.JVerein.gui.input.ZahlungsrhytmusInput;
import de.jost_net.JVerein.gui.input.ZahlungswegInput;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Kursteilnehmer;
import de.jost_net.JVerein.rmi.ManuellerZahlungseingang;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Stammdaten;
import de.jost_net.JVerein.rmi.Zusatzabbuchung;
import de.jost_net.JVerein.util.Datum;
import de.jost_net.OBanToo.Dtaus.CSatz;
import de.jost_net.OBanToo.Dtaus.DtausDateiWriter;
import de.jost_net.OBanToo.Dtaus.DtausException;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class Abbuchung
{
  public Abbuchung(FileOutputStream out, int modus, String verwendungszweck,
      Date vondatum, Boolean zusatzabbuchung, ProgressMonitor monitor)
      throws ApplicationException
  {
    try
    {
      // Stammdatensatz ermitteln.
      DBIterator list = Einstellungen.getDBService().createList(
          Stammdaten.class);
      if (list.size() == 0)
      {
        monitor.log("Keine Stammdaten gespeichert");
        throw new ApplicationException(
            "Keine Stammdaten gespeichert. Bitte erfassen.");
      }
      Stammdaten stamm = (Stammdaten) list.next();

      // Vorbereitung: A-Satz bestücken und schreiben
      DtausDateiWriter dtaus = new DtausDateiWriter(out);
      dtaus.setABLZBank(Long.parseLong(stamm.getBlz()));
      dtaus.setADatum(new Date());
      dtaus.setAGutschriftLastschrift("LK");
      dtaus.setAKonto(Long.parseLong(stamm.getKonto()));
      dtaus.setAKundenname(stamm.getName());
      dtaus.writeASatz();

      // Ermittlung der beitragsfreien Beitragsgruppen
      String beitragsfrei = "";
      list = Einstellungen.getDBService().createList(Beitragsgruppe.class);
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

      // Hier beginnt die eigentliche Abbuchung

      if (modus != AbbuchungsmodusInput.KEINBEITRAG)
      {
        // Alle Mitglieder lesen
        list = Einstellungen.getDBService().createList(Mitglied.class);
        // Die bereits ausgetretenen werden ignoriert.
        list.addFilter("austritt is null");
        // Beitragsfreie Mitglieder können auch unberücksichtigt bleiben.
        if (beitragsfrei.length() > 0)
        {
          list.addFilter(beitragsfrei);
        }
        // Zahlungsweg Abbuchung
        // list.addFilter("zahlungsweg = ?", new Object[] { new Integer(
        // ZahlungswegInput.ABBUCHUNG) });
        // Bei Abbuchungen im laufe des Jahres werden nur die Mitglieder
        // berücksichtigt, die ab einem bestimmten Zeitpunkt eingetreten sind.
        if (vondatum != null)
        {
          list.addFilter("eingabedatum >= ?", new Object[] { new java.sql.Date(
              vondatum.getTime()) });
          // list.addFilter("tonumber(eingabedatum) >= " + vondatum.getTime());
        }
        if (Einstellungen.getBeitragsmodel() == BeitragsmodelInput.MONATLICH12631)
        {
          if (modus == AbbuchungsmodusInput.HAVIMO)
          {
            list
                .addFilter(
                    "zahlungsrhytmus = ? or zahlungsrhytmus = ? or zahlungsrhytmus = ?",
                    new Object[] {
                        new Integer(ZahlungsrhytmusInput.HALBJAEHRLICH),
                        new Integer(ZahlungsrhytmusInput.VIERTELJAEHRLICH),
                        new Integer(ZahlungsrhytmusInput.MONATLICH) });
          }
          if (modus == AbbuchungsmodusInput.VIMO)
          {
            list.addFilter("zahlungsrhytmus = ? or zahlungsrhytmus = ?",
                new Object[] {
                    new Integer(ZahlungsrhytmusInput.VIERTELJAEHRLICH),
                    new Integer(ZahlungsrhytmusInput.MONATLICH) });
          }
          if (modus == AbbuchungsmodusInput.MO)
          {
            list.addFilter("zahlungsrhytmus = ?", new Object[] { new Integer(
                ZahlungsrhytmusInput.MONATLICH) });
          }
        }
        // Sätze im Resultset
        monitor.log("Anzahl Sätze: " + list.size());

        // list.setOrder(" ORDER BY name, vorname ");
        monitor.setPercentComplete(100);
        int count = 0;
        while (list.hasNext())
        {
          monitor
              .setStatus((int) ((double) count / (double) list.size() * 100d));
          Mitglied m = (Mitglied) list.next();
          Double betr;
          if (Einstellungen.getBeitragsmodel() != BeitragsmodelInput.MONATLICH12631)
          {
            betr = (Double) beitr.get(m.getBeitragsgruppeId() + "");
          }
          else
          {
            betr = (Double) beitr.get(m.getBeitragsgruppeId() + "")
                * m.getZahlungsrhytmus();
          }
          if (m.getZahlungsweg() == ZahlungswegInput.ABBUCHUNG)
          {
            writeCSatz(dtaus, m, verwendungszweck, betr);
          }
          else
          {
            writeManuellerZahlungseingang(m, verwendungszweck, betr);
          }
        }
      }
      // Schritt 2: Zusatzabbuchungen verarbeiten
      list = Einstellungen.getDBService().createList(Zusatzabbuchung.class);
      while (list.hasNext())
      {
        Zusatzabbuchung z = (Zusatzabbuchung) list.next();
        if (z.isAktiv())
        {
          Mitglied m = z.getMitglied();
          writeCSatz(dtaus, m, z.getBuchungstext(), new Double(z.getBetrag()));
          if (z.getIntervall().intValue() != IntervallInput.KEIN
              && (z.getEndedatum() == null || z.getFaelligkeit().getTime() <= z
                  .getEndedatum().getTime()))
          {
            z.setFaelligkeit(Datum.addInterval(z.getFaelligkeit(), z
                .getIntervall(), z.getEndedatum()));
          }
          z.setAusfuehrung(Datum.getHeute());
          z.store();
        }
      }
      // Ende von Schritt 2

      // Schritt 3: Kursteilnehmer verarbeiten
      list = Einstellungen.getDBService().createList(Kursteilnehmer.class);
      list.addFilter("abbudatum is null");
      while (list.hasNext())
      {
        Kursteilnehmer kt = (Kursteilnehmer) list.next();
        kt.setAbbudatum();
        kt.store();
        dtaus.setCBetragInEuro(kt.getBetrag());
        dtaus.setCBLZEndbeguenstigt(Integer.parseInt(kt.getBlz()));
        dtaus.setCInterneKundennummer(Integer.parseInt(kt.getID() + 100000));
        dtaus.setCKonto(Long.parseLong(kt.getKonto()));
        dtaus.setCName(kt.getName());
        dtaus
            .setCTextschluessel(CSatz.TS_LASTSCHRIFT_EINZUGSERMAECHTIGUNGSVERFAHREN);
        dtaus.addCVerwendungszweck(kt.getVZweck1());
        dtaus.addCVerwendungszweck(kt.getVZweck2());
        dtaus.writeCSatz();
      }

      // Ende der Abbuchung. Jetzt wird noch der E-Satz geschrieben. Die Werte
      // wurden beim Schreiben der C-Sätze ermittelt.
      dtaus.writeESatz();
      monitor.log("Anzahl Abbuchungen: " + dtaus.getAnzahlSaetze());
      monitor.log("Gesamtsumme: "
          + de.jost_net.JVerein.Einstellungen.DECIMALFORMAT.format(dtaus
              .getSummeBetraegeDecimal()) + " ¤");
      dtaus.close();
    }
    catch (DtausException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (ApplicationException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

  }

  private void writeCSatz(DtausDateiWriter dtaus, Mitglied m,
      String verwendungszweck, Double betr) throws DtausException,
      NumberFormatException, IOException
  {
    dtaus.setCBetragInEuro(betr.doubleValue());
    dtaus.setCBLZEndbeguenstigt(Integer.parseInt(m.getBlz()));
    dtaus.setCInterneKundennummer(Integer.parseInt(m.getID()));
    dtaus.setCKonto(Long.parseLong(m.getKonto()));
    String name = m.getName() + ", " + m.getVorname();
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
    dtaus.addCVerwendungszweck(m.getName() + "," + m.getVorname());
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
}
