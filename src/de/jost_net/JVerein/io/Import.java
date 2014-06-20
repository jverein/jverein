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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.input.GeschlechtInput;
import de.jost_net.JVerein.io.Adressbuch.Adressaufbereitung;
import de.jost_net.JVerein.keys.Datentyp;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Arbeitseinsatz;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Eigenschaft;
import de.jost_net.JVerein.rmi.EigenschaftGruppe;
import de.jost_net.JVerein.rmi.Eigenschaften;
import de.jost_net.JVerein.rmi.Felddefinition;
import de.jost_net.JVerein.rmi.Lastschrift;
import de.jost_net.JVerein.rmi.Lehrgang;
import de.jost_net.JVerein.rmi.MailEmpfaenger;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedfoto;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.jost_net.JVerein.rmi.Spendenbescheinigung;
import de.jost_net.JVerein.rmi.Wiedervorlage;
import de.jost_net.JVerein.rmi.Zusatzbetrag;
import de.jost_net.JVerein.rmi.Zusatzfelder;
import de.jost_net.JVerein.server.MitgliedUtils;
import de.jost_net.JVerein.util.Datum;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.jost_net.OBanToo.SEPA.BIC;
import de.jost_net.OBanToo.SEPA.IBAN;
import de.jost_net.OBanToo.SEPA.SEPAException;
import de.jost_net.OBanToo.SEPA.Basislastschrift.MandatSequence;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

/**
 * 
 * @author Heiner
 * @author Julian
 * @author Christian
 * 
 * 
 */
public class Import
{

  /* Variablen fuer Eigenschaften */
  private static final String EIGENSCHAFT = "Eigenschaft_";

  private EigenschaftGruppe eigenschaftgruppe;

  private HashMap<String, String> HM_eigenschaftsgruppen = new HashMap<String, String>();

  /* Variablen fuer Spalten und deren entsprechenden Namen */
  private Map<String, String> colMap;

  /* Variable fuer Statusinformationen */
  private final ProgressMonitor progMonitor;

  /**
   * Constructor
   * 
   * @param monitor
   */
  public Import(final ProgressMonitor monitor)
  {
    if (monitor == null)
      throw new NullPointerException("Monitor may not be null");
    this.progMonitor = monitor;

  }

  /**
   * Diese Methode erzeugt alle Beitragsgruppen die es in der zu importierenden
   * Datei gibt und legt diese als Map ab.
   * 
   * @param results
   *          der zu Importierende Datensatz
   * @return gibt die erzeugten Beitragsgruppen als Map zurueck mit dem Format
   *         key=Beitraggruppe item=BGID
   * @throws SQLException
   * @throws RemoteException
   * @throws ApplicationException
   */
  private HashMap<String, Integer> aufbauenBeitragsgruppenAusImport(
      final ResultSet results) throws SQLException, RemoteException,
      ApplicationException
  {

    HashMap<String, Double> bestehendeBeitragsgruppen = new HashMap<String, Double>();

    try
    {
      /* find all existing groups in the dataset */
      while (results.next())
      {
        bestehendeBeitragsgruppen.put(this.getResultFrom(results,
            InternalColumns.BEITRAGSART),
            new Double(this.getResultFrom(results, InternalColumns.BEITRAG)
                .replace(',', '.')));
      }
    }
    catch (NumberFormatException e)
    {
      throw new ApplicationException(MessageFormat.format(
          "In Zeile: {0} besteht folgende ungueltige Formatierung: {1}",
          results.getRow() + "", e.getMessage()));
    }

    /* add them to the db and return a string map */
    HashMap<String, Integer> beitragsgruppen = new HashMap<String, Integer>();
    for (String key : bestehendeBeitragsgruppen.keySet())
    {
      Double betr = bestehendeBeitragsgruppen.get(key);

      int bid = createBeitragsgruppeAndID(betr, key);

      beitragsgruppen.put(key, bid);
    }

    return beitragsgruppen;
  }

  /**
   * store the specified group and get the id what is assigned to the group
   * 
   * @param beitrag
   *          amount of money
   * @param beitragsgruppe
   *          group name
   * @return the id of the created group
   * @throws RemoteException
   * @throws ApplicationException
   */
  private int createBeitragsgruppeAndID(Double beitrag, String beitragsgruppe)
      throws RemoteException, ApplicationException
  {
    Beitragsgruppe b = (Beitragsgruppe) Einstellungen.getDBService()
        .createObject(Beitragsgruppe.class, null);

    /* if beitragsgruppe larger than 30 signs it will be cuted */
    b.setBezeichnung(beitragsgruppe.length() > 30 ? beitragsgruppe.substring(0,
        29) : beitragsgruppe);
    b.setBetrag(beitrag);
    b.store();

    return (new Integer(b.getID())).intValue();
  }

  /**
   * Beitragsgruppen in der Importdatei pruefen. Es wird Beitragsart und Beitrag
   * sowohl auf existens als auch auf den erwartet Wert geprueft.
   * 
   * @param results
   * @return true, wenn alles in Ordnung ist.
   * @throws SQLException
   */
  private boolean checkeBeitragsgruppen(final ResultSet results)
      throws SQLException
  {
    boolean groupsAreValid = true;

    while (results.next())
    {
      String ba = this.getResultFrom(results, InternalColumns.BEITRAGSART);
      String btr = this.getResultFrom(results, InternalColumns.BEITRAG);
      // Zeige Benutzer genauen Fehler.
      if (ba == null || ba.length() == 0)
      {
        progMonitor.log(MessageFormat.format(
            "{0}, {1}: keine Angaben zur Beitragsart_1",
            this.getResultFrom(results, InternalColumns.NACHNAME),
            this.getResultFrom(results, InternalColumns.VORNAME)));
        groupsAreValid = false;
      }
      else if (ba.length() > 30)
      {
        progMonitor
            .log(MessageFormat
                .format(
                    "{0}, {1}: maximale Laenge von 30 Zeichen in Beitragsart_1 ueberschritten, wird automatisch gekuerzt",
                    this.getResultFrom(results, InternalColumns.NACHNAME),
                    this.getResultFrom(results, InternalColumns.VORNAME)));
      }

      if (btr == null || btr.length() == 0)
      {
        progMonitor.log(MessageFormat.format(
            "{0}, {1}: keine Angaben zum Beitrag_1",
            this.getResultFrom(results, InternalColumns.NACHNAME),
            this.getResultFrom(results, InternalColumns.VORNAME)));
        groupsAreValid = false;
      }
    }
    return groupsAreValid;
  }

  /**
   * Passt die Formatierung von einem Datum immer so an dass es in folgendem
   * Format ist: dd.mm.yyyy Wenn date leer ist oder 0.0.00 enthaelt, dann wird
   * ein leerer String zuerueck gegeben.
   * 
   * @param date
   * @return Ein Datum in Format dd.mm.yyyy
   * @throws ApplicationException
   * @throws ParseException
   */
  private String formatDate(final String date) throws ParseException
  {
    if (date == null)
      throw new NullPointerException("Date may not be null");

    String result = "";

    /* change format from xx/xx/xxxx to xx.xx.xxxx */
    String dotDate = date.replace('/', '.');

    if (dotDate.matches("0?0\\.0?0\\.(00){1,2}") || dotDate.trim().equals("")) // format
                                                                               // 0.0.00
    {
      // dont do anything return an empty result, because date isn't defined
    }
    else if (dotDate.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) // format 01.01.2000
    {
      result = dotDate;
    }
    else if (dotDate.matches("\\d?\\d\\.\\d?\\d\\.(\\d{2})?\\d{2}")) // format
                                                                     // 1.1.00
    {
      String[] parts = dotDate.split("(\\.|/)");
      if (parts[0].length() == 1)
        parts[0] = "0" + parts[0];
      if (parts[1].length() == 1)
        parts[1] = "0" + parts[1];
      if (parts[2].length() == 2)
      {
        Date curDate = new Date(System.currentTimeMillis());
        // only the last two digets of the year
        String year = curDate.toString().substring(
            curDate.toString().lastIndexOf(" ") + 2);
        // compare them an than decide
        if ((new Integer(year)).compareTo(new Integer(parts[2])) < 0)
        {
          parts[2] = "19" + parts[2];
        }
        else
        {
          parts[2] = "20" + parts[2];
          progMonitor.log(MessageFormat.format(
              "Fuer {0} wurde das 21 Jahrhundert angenommen", dotDate + ""));
        }
      }

      result = parts[0] + "." + parts[1] + "." + parts[2];
    }
    else
    {
      throw new ParseException("Unsupported Date Format " + dotDate, 0);
    }

    return result;
  }

  /**
   * return the id of a specified property field
   * 
   * @param eigenschaft
   * @param groupName
   */
  private String getEigenschaftID(String eigenschaft, String groupName)
  {
    try
    {
      DBIterator it = Einstellungen.getDBService()
          .createList(Eigenschaft.class);
      it.addFilter("bezeichnung = ?", new Object[] { eigenschaft });
      if (it.hasNext())
      {
        Eigenschaft eig = (Eigenschaft) it.next();
        return eig.getID();
      }
      else
      {
        Eigenschaft eigenschaftneu = (Eigenschaft) Einstellungen.getDBService()
            .createObject(Eigenschaft.class, null);
        eigenschaftneu.setBezeichnung(eigenschaft);
        String id = HM_eigenschaftsgruppen.get(groupName);
        if (id != null) // no entry for this groupName
        {
          eigenschaftneu.setEigenschaftGruppe(Integer.parseInt(id));
        }
        eigenschaftneu.store();
        return eigenschaftneu.getID();
      }
    }
    catch (Exception e)
    {
      Logger.error("Fehler", e);
    }
    return null;
  }

  /**
   * 
   * @param results
   * @throws SQLException
   */
  private ArrayList<String> getEigenschaftspalten(ResultSet results)
      throws SQLException
  {
    ArrayList<String> ret = new ArrayList<String>();
    ResultSetMetaData rsm = results.getMetaData();
    int anzspalten = rsm.getColumnCount();
    for (int i = 1; i <= anzspalten; i++)
    {
      String colname = rsm.getColumnName(i);
      if (colname.startsWith(EIGENSCHAFT))
      {
        ret.add(colname);
      }
    }
    return ret;
  }

  /**
   * 
   * if it is a necessary column the result will be returned or an exception
   * will be raised if it is an optional column it will be checked, wheather it
   * exist or not if it exist the result will be return otherwise an empty
   * String
   * 
   * @param results
   * @param column
   * @throws SQLException
   */
  private String getResultFrom(final ResultSet results,
      final InternalColumns column) throws SQLException
  {
    String resultValue = "";

    if (column.isNecessary()
        || (!column.isNecessary() && colMap.containsKey(column.getColumnName())))
    {
      try
      {
        resultValue = results.getString(colMap.get(column.getColumnName()));

        /* remove white spaces */
        resultValue = resultValue.trim();

        /* remove leading and trailing commatas */
        if (resultValue.startsWith("\"") && resultValue.endsWith("\""))
        {
          resultValue = resultValue.substring(1, resultValue.length() - 2)
              .trim();
        }
      }
      catch (NullPointerException e)
      {
        progMonitor.log(MessageFormat.format(
            "Zuordnung wurde fuer folgenden Spaltennamen nicht gefunden: {0}",
            column.getColumnName()));

        /*
         * unhandled Exception because it shouldn't happen if the maps are used
         * correctly
         */
        throw new NullPointerException();
      }
      catch (SQLException e)
      {
        progMonitor.log(MessageFormat.format(
            "Fehler beim lesen der Importdatei in der Spalte: {0}",
            colMap.get(column.getColumnName())));
        throw new SQLException();
      }
    }
    return resultValue;
  }

  /**
   * This method starts the import
   * 
   * @throws SQLException
   * @throws ApplicationException
   * @throws RemoteException
   */
  public boolean importFile(final ResultSet results, final int nrOfResults,
      final Map<String, String> colMap) throws RemoteException,
      ApplicationException
  {
    if (results == null)
    {
      throw new NullPointerException("results my not be null");
    }
    if (colMap == null)
    {
      throw new NullPointerException("colMap may not be null");
    }

    this.colMap = colMap;

    try
    {
      /* verify if beitrag and groups available and in the correct format */
      results.beforeFirst();
      if (!checkeBeitragsgruppen(results))
      {
        progMonitor.log("Abbruch");
        return false;
      }

      /*
       * Step 1 - delete complete exisiting dataset
       */

      /* delete existing dataset */
      loescheBestand();

      /*
       * Step 2 - build up the inital properties, groups etc
       */

      /* create groups in the db save them locally for the import process */
      results.beforeFirst();
      HashMap<String, Integer> beitragsgruppen = aufbauenBeitragsgruppenAusImport(results);

      /* Zusatzfelder ermitteln und in Liste ablegen */
      DBIterator it = Einstellungen.getDBService().createList(
          Felddefinition.class);
      LinkedList<Felddefinition> zusfeld = new LinkedList<Felddefinition>();
      for (int i = 0; i < it.size(); i++)
      {
        zusfeld.add((Felddefinition) it.next());
      }

      /* create a default property group */
      eigenschaftgruppe = (EigenschaftGruppe) Einstellungen.getDBService()
          .createObject(EigenschaftGruppe.class, null);
      eigenschaftgruppe.setBezeichnung("Noch nicht zugeordnet");
      eigenschaftgruppe.store();

      results.beforeFirst();
      ArrayList<String> eigenschaftenspalten = getEigenschaftspalten(results);
      for (String feld : eigenschaftenspalten)
      {
        String groupName = new String(feld); // working copy

        /*
         * ignore all group containing a #Number and Number larger than two at
         * the end, because, only one group will be created with same suffix
         * e.g. Eigenschaft_001#1 Eigenschaft_001#2, the group Eigenschaft_001
         * is only necessary once.
         */
        if (!groupName.matches(EIGENSCHAFT + ".*#([2-9]|[1-9][0-9]+)"))
        {
          if (groupName.matches(EIGENSCHAFT + ".*#1"))
          {
            groupName = groupName.substring(0, groupName.length() - 2);
          }

          eigenschaftgruppe = (EigenschaftGruppe) Einstellungen.getDBService()
              .createObject(EigenschaftGruppe.class, null);
          eigenschaftgruppe.setBezeichnung(groupName);
          eigenschaftgruppe.store();
          HM_eigenschaftsgruppen.put(groupName, eigenschaftgruppe.getID());
        }
      }

      /*
       * Step 3 - start importing dataset
       */
      int anz = 0;

      results.beforeFirst();
      while (results.next())
      {
        /* Status - Progress */
        anz++;
        progMonitor.setPercentComplete(anz * 100 / nrOfResults);

        /* import new member */
        Mitglied m = (Mitglied) Einstellungen.getDBService().createObject(
            Mitglied.class, null);

        try
        {
          importMitglied(results, m, beitragsgruppen);
        }
        catch (ParseException e1)
        {
          progMonitor.log("ID= "
              + getResultFrom(results, InternalColumns.MITGLIEDSNR) + " NAME= "
              + getResultFrom(results, InternalColumns.NACHNAME) + " "
              + e1.getMessage());
          return false;
        }

        /* import all additonal fields */
        for (Felddefinition f : zusfeld)
        {
          Zusatzfelder zf = (Zusatzfelder) Einstellungen.getDBService()
              .createObject(Zusatzfelder.class, null);
          importZusatzfelder(results, zf, m, f);
        }

        /* import all property fields */
        try
        {
          for (String feld : eigenschaftenspalten)
          {
            String eig = results.getString(feld);
            String groupName = feld; // working copy
            if (groupName.matches(EIGENSCHAFT + ".*#[0-9]+"))
            {
              groupName = groupName.substring(0, groupName.lastIndexOf("#"));
            }

            if (eig.length() > 0) // only if not empty add not empty
            {
              Eigenschaften eigenschaften = (Eigenschaften) Einstellungen
                  .getDBService().createObject(Eigenschaften.class, null);
              eigenschaften.setMitglied(m.getID());
              eigenschaften.setEigenschaft(getEigenschaftID(eig, groupName));
              eigenschaften.store();
            }
          }
        }
        catch (Exception e)
        {
          progMonitor
              .log(MessageFormat
                  .format(
                      "Datensatz unvollstaending (Eigenschaften) -> Import wird abgebrochen: ID= {0}, NAME= {1}: {2}",
                      getResultFrom(results, InternalColumns.MITGLIEDSNR),
                      getResultFrom(results, InternalColumns.NACHNAME),
                      e.getMessage()));
          return false;
        }

      }

      progMonitor.setStatusText(MessageFormat.format(
          "Import vollstaendig - Es wurden {0} Datensaetze importiert", anz
              + ""));

    }
    catch (SQLException e)
    {
      Logger.error("Fehler", e);
      progMonitor.log("Fehler beim lesen der zu importierenden Datei");
      throw new ApplicationException("Datei Fehler");
    }
    return true;
  }

  /**
   * this method imports a new member from the specified ResultSet, it will only
   * insert the current selected one. If you want another to import you have to
   * move the cursor of the results by your own. All information will be saved
   * into the defined member m
   * 
   */
  private void importMitglied(final ResultSet results, final Mitglied m,
      final Map<String, Integer> beitragsGruppen) throws RemoteException,
      SQLException, ApplicationException, ParseException
  {
    m.setAdresstyp(1);

    /*
     * necessary columns
     */

    /* Setze Stammdaten */
    String id = getResultFrom(results, InternalColumns.MITGLIEDSNR);
    if (id.length() == 0)
    {
      throw new RemoteException("Mitglieds_Nr ist leer");
    }
    try
    {
      Integer.parseInt(id);
      m.setID(id); // wird übersprungen bei nichtnumerischen externen
                   // Mitgliedsnummern.
    }
    catch (NumberFormatException e)
    {
      // nichts tun. Es kann bei der Verwendung von nichtnumerischen
      // Mitgliedsnummer zu der Exception kommen. In diesem Fall wird die ID
      // automatisch von der DB vergeben.
    }
    m.setAnrede(getResultFrom(results, InternalColumns.ANREDE));
    m.setTitel(getResultFrom(results, InternalColumns.TITEL));
    m.setName(getResultFrom(results, InternalColumns.NACHNAME));
    m.setVorname(getResultFrom(results, InternalColumns.VORNAME));
    m.setStrasse(getResultFrom(results, InternalColumns.STRASSE));
    m.setPlz(getResultFrom(results, InternalColumns.PLZ));
    m.setOrt(getResultFrom(results, InternalColumns.ORT));

    String geschlecht = getResultFrom(results, InternalColumns.GESCHLECHT);
    if (geschlecht.matches("^(m|M).*"))
      geschlecht = GeschlechtInput.MAENNLICH;
    else if (geschlecht.matches("^(w|W).*"))
      geschlecht = GeschlechtInput.WEIBLICH;
    else if (geschlecht.matches("^(o|O).*"))
      geschlecht = GeschlechtInput.OHNEANGABE;
    else
    {
      /*
       * Wenn Anrede gesetzt ist koennte auch damit das Geschlecht ermittelt
       * werden
       */
      if (m.getAnrede().equalsIgnoreCase("Frau"))
        geschlecht = GeschlechtInput.WEIBLICH;
      else if (m.getAnrede().startsWith("Herr"))
        geschlecht = GeschlechtInput.MAENNLICH;
      else
        geschlecht = "";
    }
    m.setGeschlecht(geschlecht);

    String gebDatum = formatDate(getResultFrom(results,
        InternalColumns.GEBDATUM));
    if (gebDatum.length() == 0)
    {
      gebDatum = null;

      if (Einstellungen.getEinstellung().getGeburtsdatumPflicht())
      {
        throw new ApplicationException(MessageFormat.format(
            "{0}: Geburtsdatum fehlt!", Adressaufbereitung.getNameVorname(m)));
      }
    }
    m.setGeburtsdatum(gebDatum);

    m.setTelefonprivat(getResultFrom(results, InternalColumns.TELEPRIVAT));
    m.setTelefondienstlich(getResultFrom(results, InternalColumns.TELEDIENST));
    m.setEmail(getResultFrom(results, InternalColumns.EMAIL));

    /* Setze Konto und Zahlungsvorgaenge */
    int zahlweg = Zahlungsweg.BARZAHLUNG;
    String blz = getResultFrom(results, InternalColumns.BLZ);
    String ktnr = getResultFrom(results, InternalColumns.KONTONR);
    String bic = getResultFrom(results, InternalColumns.BIC);
    String iban = getResultFrom(results, InternalColumns.IBAN);
    String zahlart = getResultFrom(results, InternalColumns.ZAHLART);
    if (blz.length() > 0 && ktnr.length() > 0 && iban.length() == 0
        && bic.length() == 0)
    {
      try
      {
        IBAN ib = new IBAN(ktnr, blz, Einstellungen.getEinstellung()
            .getDefaultLand());
        if (ib.getCode().getStatus() == 1 || ib.getCode().getStatus() == 2)
        {
          iban = ib.getIBAN();
        }
        else
        {
          progMonitor.log(Adressaufbereitung.getNameVorname(m) + ": "
              + ib.getCode().getMessage());
          throw new ApplicationException();
        }
        BIC bi = new BIC(ktnr, blz, "DE");
        bic = bi.getBIC();
      }
      catch (SEPAException e)
      {
        e.printStackTrace();
        progMonitor.log(Adressaufbereitung.getNameVorname(m) + ": "
            + e.getMessage());
      }
    }

    if (zahlart.equalsIgnoreCase("l")
        || zahlart.equalsIgnoreCase("lastschrift")
        || zahlart.equalsIgnoreCase("abbuchung")
        || zahlart.equalsIgnoreCase("bankeinzug")
        || zahlart.equalsIgnoreCase("s")) // SPG-Verein, SEPA-Lastschrift
    {

      /*
       * Wenn als Zahlungsweg Abbuchung definiert ist muss es auch eine
       * Bankverbindung existieren (BIC und IBAN oder übergangsweise BLZ und
       * Konto)
       */
      zahlweg = Zahlungsweg.BASISLASTSCHRIFT;

      boolean altebankverbindung = blz.length() > 0 && ktnr.length() > 0;
      boolean neuebankverbindung = iban != null && bic != null
          && iban.length() > 0 && bic.length() > 0;

      if (!altebankverbindung && !neuebankverbindung)
      {
        progMonitor
            .log(MessageFormat
                .format(
                    "Bei {0} ist als Zahlungsart Basislastschrift gesetzt aber weder neue Bankverbindung (IBAN  / BIC) noch (BLZ /Konto) vorhanden",
                    Adressaufbereitung.getNameVorname(m)));
        throw new ApplicationException();
      }
    }
    else if (zahlart.equalsIgnoreCase("b") || zahlart.equalsIgnoreCase("bar")
        || zahlart.equalsIgnoreCase("barzahlung"))
    {
      zahlweg = Zahlungsweg.BARZAHLUNG;
    }
    else if (zahlart.equalsIgnoreCase("u")
        || zahlart.equalsIgnoreCase("ueberweisung"))
    {
      zahlweg = Zahlungsweg.ÜBERWEISUNG;
    }
    else
    {
      progMonitor.log(MessageFormat.format(
          "{0}: ungueltige Zahlungsart. Bar wird angenommen.",
          Adressaufbereitung.getNameVorname(m)));
    }

    m.setBlz(blz);
    m.setKonto(ktnr);
    m.setBic(bic);
    m.setIban(iban);
    try
    {
      m.setMandatSequence(MandatSequence.fromString(getResultFrom(results,
          InternalColumns.MANDATSEQUENCE)));
    }
    catch (NullPointerException e)
    {
      progMonitor.log(MessageFormat.format(
          "{0}: Ungültige Mandatssequence. RCUR wird angenommen.",
          Adressaufbereitung.getNameVorname(m)));
      m.setMandatSequence(MandatSequence.RCUR);
    }
    try
    {
      m.setMandatVersion(new Integer(getResultFrom(results,
          InternalColumns.MANDATVERSION)));
    }
    catch (NumberFormatException e)
    {
      progMonitor.log(MessageFormat.format(
          "{0}: Ungültige Mandatsversion. 1 wird angenommen.",
          Adressaufbereitung.getNameVorname(m)));
      m.setMandatVersion(1);
    }
    String m_d = getResultFrom(results, InternalColumns.MANDATDATUM);
    if (m_d.length() > 0)
    {
      m.setMandatDatum(Datum.toDate(formatDate(m_d)));
    }
    m.setZahlungsweg(zahlweg);
    m.setKtoiPersonenart(getResultFrom(results, InternalColumns.KTOIPERSONENART));
    m.setKtoiAnrede(getResultFrom(results, InternalColumns.KTOIANREDE));
    m.setKtoiTitel(getResultFrom(results, InternalColumns.KTOITITEL));
    m.setKtoiName(getResultFrom(results, InternalColumns.KTOINAME));
    m.setKtoiVorname(getResultFrom(results, InternalColumns.KTOIVORNAME));
    m.setKtoiStrasse(getResultFrom(results, InternalColumns.KTOISTRASSE));
    m.setKtoiAdressierungszusatz(getResultFrom(results,
        InternalColumns.KTOIADRESSIERUNGSZUSATZ));
    m.setKtoiPlz(getResultFrom(results, InternalColumns.KTOIPLZ));
    m.setKtoiOrt(getResultFrom(results, InternalColumns.KTOIORT));
    m.setKtoiStaat(getResultFrom(results, InternalColumns.KTOISTAAT));
    m.setKtoiEmail(getResultFrom(results, InternalColumns.KTOIEMAIL));
    Integer bg = beitragsGruppen.get(getResultFrom(results,
        InternalColumns.BEITRAGSART));
    m.setBeitragsgruppe(bg);

    /* Setze verschiedene Ein/Ausdritt etc Daten */
    String eintritt = formatDate(getResultFrom(results,
        InternalColumns.EINTRITTSDATUM));
    if (eintritt.length() == 0)
    {
      eintritt = null;

      if (Einstellungen.getEinstellung().getEintrittsdatumPflicht())
      {
        throw new ApplicationException(MessageFormat.format(
            "{0}: Eintrittsdatum fehlt!", Adressaufbereitung.getNameVorname(m)));
      }
    }
    m.setEintritt(eintritt);

    String austritt = formatDate(getResultFrom(results,
        InternalColumns.AUSTRITTSDATUM));
    if (austritt.length() == 0)
    {
      austritt = null;
    }
    m.setAustritt(austritt);

    String sterbeTag = formatDate(getResultFrom(results,
        InternalColumns.STERBEDATUM));
    if (sterbeTag.length() == 0)
    {
      sterbeTag = null;
    }
    else
    {
      if (austritt == null)
      {
        progMonitor
            .log(MessageFormat
                .format(
                    "{0}: beim einem definierten Sterbedatum muss es auch ein Austrittsdatum geben, setze Austrittsdatum gleich dem Sterbedatum",
                    Adressaufbereitung.getNameVorname(m)));
        m.setAustritt(sterbeTag);
      }
    }
    m.setSterbetag(sterbeTag);

    String kuendigung = formatDate(getResultFrom(results,
        InternalColumns.KUENDIGUNGSDATUM));
    if (kuendigung.length() == 0)
    {
      kuendigung = null;
    }
    m.setKuendigung(kuendigung);

    /*
     * optional columns
     */

    String individuellerBeitrag = getResultFrom(results,
        InternalColumns.BEITRAGINDI);
    if (individuellerBeitrag.length() > 0)
    {

      /* Preprocessing - replace all , with . */
      individuellerBeitrag = individuellerBeitrag.replaceAll(",", ".");

      /* Check if remaining string matches the supported format */
      if (individuellerBeitrag.matches("[0-9]+(\\.[0-9]+)?"))
      {
        Double beitrag = new Double(individuellerBeitrag);
        if (beitrag > 0)
        {
          m.setIndividuellerBeitrag(beitrag);
        }
      }
      else
      {
        progMonitor
            .log(MessageFormat
                .format(
                    "Individueller Beitrag fuer {0} enthält keine gültige Formatierung und wird verworfen.",
                    Adressaufbereitung.getNameVorname(m)));
      }
    }

    String personenArt = getResultFrom(results, InternalColumns.PERSONENART);
    if (personenArt.length() > 0)
    {
      if (personenArt.matches("^(j|J).*"))
      {
        m.setPersonenart("j");
      }
      else if (personenArt.matches("^(n|N).*"))
      {
        m.setPersonenart("n");
      }
      else
      {
        progMonitor
            .log(MessageFormat
                .format(
                    "Personenart für {0} enthält keine gültige Formatierung. Es dürfen nur Wörter verwendet werden, die mit einem j fuer juristische Personen oder n fuer natürliche Personen beginnen. Bei leerem Inhalt wird der Standardwert n verwendet",
                    Adressaufbereitung.getNameVorname(m)));
        throw new ApplicationException();
      }

    }
    else
    { // Default value
      m.setPersonenart("n");
    }

    m.setHandy(getResultFrom(results, InternalColumns.TELEMOBIL));
    m.setAdressierungszusatz(getResultFrom(results, InternalColumns.ADRZUSATZ));
    m.setStaat(getResultFrom(results, InternalColumns.STAAT)); // Default was
                                                               // null warum?

    String zahlungsrhythmus = getResultFrom(results, InternalColumns.ZAHLRYTHM);
    if (zahlungsrhythmus.length() > 0)
    {
      if (zahlungsrhythmus.matches("[0-9]+"))
      {
        m.setZahlungsrhythmus(Integer.parseInt(zahlungsrhythmus));
      }
      else
      {
        progMonitor
            .log(MessageFormat
                .format(
                    "Zahlungsrythmus bei: {0}  ist entweder leer oder besteht nicht nur aus Zahlen, setze auf 12 Monate",
                    Adressaufbereitung.getNameVorname(m)));
        m.setZahlungsrhythmus(new Integer(12));
      }
    }
    else
    { // Default value
      m.setZahlungsrhythmus(new Integer(12));
    }

    String adresstyp = getResultFrom(results, InternalColumns.ADRESSTYP);
    if (adresstyp.length() > 0)
    {
      if (adresstyp.matches("[0-9]+"))
      {
        m.setAdresstyp(Integer.parseInt(adresstyp));
      }
      else
      {
        progMonitor
            .log(MessageFormat
                .format(
                    "Adresstyp bei: {0} ist entweder leer oder besteht nicht nur aus Zahlen, setze auf 1 (Mitglied)",
                    Adressaufbereitung.getNameVorname(m)));
        m.setAdresstyp(new Integer(1));
      }
    }
    else
    { // Default value
      m.setAdresstyp(new Integer(1));
    }

    m.setVermerk1(getResultFrom(results, InternalColumns.VERMERKA));
    m.setVermerk2(getResultFrom(results, InternalColumns.VERMERKB));

    /*
     * set properties correctly
     */

    if (Einstellungen.getEinstellung().getExterneMitgliedsnummer())
    {
      m.setExterneMitgliedsnummer(new String(getResultFrom(results,
          InternalColumns.MITGLIEDSNR)));
    }

    try
    {
      m.insert();
    }
    catch (ApplicationException e)
    {
      progMonitor.log(Adressaufbereitung.getNameVorname(m) + ": "
          + e.getMessage());
      throw e;
    }
  }

  /**
   * to import the additional mandatory fields
   * 
   * @param results
   * @param f
   * @throws RemoteException
   * @throws SQLException
   * @throws ApplicationException
   */
  private void importZusatzfelder(final ResultSet results,
      final Zusatzfelder zusatzfeld, final Mitglied curMitglied,
      final Felddefinition f) throws RemoteException, SQLException,
      ApplicationException
  {
    zusatzfeld.setMitglied(new Integer(curMitglied.getID()));
    zusatzfeld.setFelddefinition(new Integer(f.getID()));
    String inhalt = results.getString(colMap.get(f.getName()));

    switch (f.getDatentyp())
    {
      case Datentyp.DATUM:
        if (inhalt.length() > 0)
        {
          try
          {
            zusatzfeld.setFeldDatum(new JVDateFormatTTMMJJJJ().parse(inhalt));
          }
          catch (ParseException e)
          {
            throw new ApplicationException(MessageFormat.format(
                "{0} : ungültiges Datumsformat {1}: {2}",
                Adressaufbereitung.getNameVorname(curMitglied), f.getName(),
                inhalt));
          }
        }
        else
        {
          zusatzfeld.setFeldDatum(null);
        }
        break;
      case Datentyp.GANZZAHL:
        if (inhalt.length() > 0)
        {
          try
          {
            zusatzfeld.setFeldGanzzahl(Integer.parseInt(inhalt));
          }
          catch (NumberFormatException e)
          {
            throw new ApplicationException(MessageFormat.format(
                "{0}: ungültiges Datenformat {1}: {2}",
                Adressaufbereitung.getNameVorname(curMitglied), f.getName(),
                inhalt));
          }
        }
        else
        {
          zusatzfeld.setFeldGanzzahl(null);
        }
        break;
      case Datentyp.JANEIN:
        if (inhalt.equalsIgnoreCase("true") || inhalt.equalsIgnoreCase("ja"))
        {
          zusatzfeld.setFeldJaNein(true);
        }
        else if (inhalt.equalsIgnoreCase("false")
            || inhalt.equalsIgnoreCase("nein"))
        {
          zusatzfeld.setFeldJaNein(false);
        }
        else
        {
          throw new ApplicationException(MessageFormat.format(
              "{0}: ungültiges Datenformat {1}: {2}",
              Adressaufbereitung.getNameVorname(curMitglied), f.getName(),
              inhalt));
        }
        break;
      case Datentyp.WAEHRUNG:
        inhalt = inhalt.replace(",", ".");
        if (inhalt.length() > 0)
        {
          try
          {
            zusatzfeld.setFeldWaehrung(new BigDecimal(inhalt));
          }
          catch (NumberFormatException e)
          {
            throw new ApplicationException(MessageFormat.format(
                "{0}: ungültiges Datenformat {1}: {2}",
                Adressaufbereitung.getNameVorname(curMitglied), f.getName(),
                inhalt));
          }
        }
        else
        {
          zusatzfeld.setFeldGanzzahl(null);
        }
        break;
      case Datentyp.ZEICHENFOLGE:
        zusatzfeld.setFeld(inhalt);
        break;
    }
    zusatzfeld.store();
  }

  /**
   * remove all Data from the Database
   */
  private void loescheBestand()
  {
    try
    {
      // Arbeitseinsätze
      DBIterator list = Einstellungen.getDBService().createList(
          Arbeitseinsatz.class);
      while (list.hasNext())
      {
        Arbeitseinsatz a = (Arbeitseinsatz) list.next();
        a.delete();
      }
      // Lastschriften
      list = Einstellungen.getDBService().createList(Lastschrift.class);
      while (list.hasNext())
      {
        Lastschrift l = (Lastschrift) list.next();
        l.delete();
      }
      // Lehrgänge
      list = Einstellungen.getDBService().createList(Lehrgang.class);
      while (list.hasNext())
      {
        Lehrgang l = (Lehrgang) list.next();
        l.delete();
      }
      // Mailempfänger
      list = Einstellungen.getDBService().createList(MailEmpfaenger.class);
      while (list.hasNext())
      {
        MailEmpfaenger m = (MailEmpfaenger) list.next();
        m.delete();
      }
      // Mitgliedsfoto
      list = Einstellungen.getDBService().createList(Mitgliedfoto.class);
      while (list.hasNext())
      {
        Mitgliedfoto m = (Mitgliedfoto) list.next();
        m.delete();
      }
      // Mitgliedskonto
      list = Einstellungen.getDBService().createList(Mitgliedskonto.class);
      while (list.hasNext())
      {
        Mitgliedskonto m = (Mitgliedskonto) list.next();
        m.delete();
      }
      // Spendenbescheinigung
      list = Einstellungen.getDBService()
          .createList(Spendenbescheinigung.class);
      while (list.hasNext())
      {
        Spendenbescheinigung s = (Spendenbescheinigung) list.next();
        s.delete();
      }
      // Zusatzbetraege
      list = Einstellungen.getDBService().createList(Zusatzbetrag.class);
      while (list.hasNext())
      {
        Zusatzbetrag z = (Zusatzbetrag) list.next();
        z.delete();
      }
      // Zusatzfelder
      list = Einstellungen.getDBService().createList(Zusatzfelder.class);
      while (list.hasNext())
      {
        Zusatzfelder z = (Zusatzfelder) list.next();
        z.delete();
      }

      // Wiedervorlage
      list = Einstellungen.getDBService().createList(Wiedervorlage.class);
      while (list.hasNext())
      {
        Wiedervorlage w = (Wiedervorlage) list.next();
        w.delete();
      }
      // Eigenschaften
      list = Einstellungen.getDBService().createList(Eigenschaften.class);
      while (list.hasNext())
      {
        Eigenschaften e = (Eigenschaften) list.next();
        e.delete();
      }
      // Eigenschaft
      list = Einstellungen.getDBService().createList(Eigenschaft.class);
      while (list.hasNext())
      {
        Eigenschaft e = (Eigenschaft) list.next();
        e.delete();
      }
      // Eigenschaftgruppe
      list = Einstellungen.getDBService().createList(EigenschaftGruppe.class);
      while (list.hasNext())
      {
        EigenschaftGruppe e = (EigenschaftGruppe) list.next();
        e.delete();
      }
      // Mitglieder
      list = Einstellungen.getDBService().createList(Mitglied.class);
      MitgliedUtils.setMitglied(list);
      while (list.hasNext())
      {
        Mitglied m = (Mitglied) list.next();
        m.delete();
      }
      // Beitragsgruppe
      list = Einstellungen.getDBService().createList(Beitragsgruppe.class);
      while (list.hasNext())
      {
        Beitragsgruppe b = (Beitragsgruppe) list.next();
        b.delete();
      }
    }
    catch (RemoteException e)
    {
      Logger.error("Fehler", e);
    }
    catch (ApplicationException e)
    {
      Logger.error("Fehler", e);
    }
  }

}
