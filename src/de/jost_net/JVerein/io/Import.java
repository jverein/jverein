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
 * Revision 1.26  2009/11/22 16:20:09  jost
 * Bugfix Eigenschaften-Import
 *
 * Revision 1.25  2009/11/22 12:22:54  jost
 * Eigenschaften importieren.
 *
 * Revision 1.24  2009/09/15 19:24:49  jost
 * Bugfix Zahlungsrhytmus
 *
 * Revision 1.23  2009/08/19 21:01:13  jost
 * Zahlungsweg "überweisung" kann jetzt auch importiert werden.
 *
 * Revision 1.22  2009/08/18 17:31:03  jost
 * - Abrechnungsdaten löschen
 * - Bugfix Barzahlung
 *
 * Revision 1.21  2009/04/25 05:30:20  jost
 * Neu: Juristische Personen  können als Mitglied gespeichert werden.
 *
 * Revision 1.20  2009/04/15 21:04:22  jost
 * Vermeidung NPE
 *
 * Revision 1.19  2008/12/23 21:10:06  jost
 * Vermeidung von NPE's
 *
 * Revision 1.18  2008/12/22 21:19:31  jost
 * Zusatzabbuchung->Zusatzbetrag
 *
 * Revision 1.17  2008/12/19 06:55:10  jost
 * Wenn die Spalte adressierungszusatz in der Import-Datei fehlt, wird Leerstring in die DB eingetragen.
 *
 * Revision 1.16  2008/11/29 13:12:24  jost
 * Refactoring: Code-Optimierung
 *
 * Revision 1.15  2008/11/16 16:58:29  jost
 * Speicherung der Einstellung von Property-Datei in die Datenbank verschoben.
 *
 * Revision 1.14  2008/11/13 20:18:00  jost
 * Adressierungszusatz aufgenommen.
 *
 * Revision 1.13  2008/06/29 07:58:31  jost
 * Neu: Handy
 *
 * Revision 1.12  2008/05/07 05:48:54  jost
 * LÃ¶schung zusÃ¤tzlicher Tabellen bei wiederholtem Import
 *
 * Revision 1.11  2008/04/10 19:00:35  jost
 * Neu: Benutzerdefinierte Datenfelder
 *
 * Revision 1.10  2008/03/08 19:30:32  jost
 * Neu: Externe Mitgliedsnummer
 *
 * Revision 1.9  2008/02/22 17:31:29  jost
 * Fehlermeldung sauber ausgeben.
 *
 * Revision 1.8  2008/02/17 08:29:02  jost
 * Bugfix beim Import des Zahlungsrhytmusses
 *
 * Revision 1.7  2007/12/18 17:25:21  jost
 * Neu: Zahlungsrhytmus importieren
 *
 * Revision 1.6  2007/03/25 17:03:44  jost
 * 1. Zusätzliche Plausibilitäten
 * 2. Import des Zahlungsweges
 *
 * Revision 1.5  2007/03/24 20:22:19  jost
 * Bugfix. Jetzt kÃ¶nnen, wie dokumentiert, beliebige Dateinamen verwendet werden.
 *
 * Revision 1.4  2007/02/23 20:28:04  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.3  2007/01/14 12:42:42  jost
 * Java 1.5-KompatibilitÃ¤t
 *
 * Revision 1.2  2006/10/23 19:09:06  jost
 * Import optimiert
 *
 * Revision 1.1  2006/09/20 15:39:24  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Abrechnung;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Eigenschaft;
import de.jost_net.JVerein.rmi.EigenschaftGruppe;
import de.jost_net.JVerein.rmi.Eigenschaften;
import de.jost_net.JVerein.rmi.Felddefinition;
import de.jost_net.JVerein.rmi.ManuellerZahlungseingang;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Wiedervorlage;
import de.jost_net.JVerein.rmi.Zusatzbetrag;
import de.jost_net.JVerein.rmi.Zusatzfelder;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class Import
{
  private static final String EIGENSCHAFT = "Eigenschaft_";

  private EigenschaftGruppe eigenschaftgruppe;

  public Import(String path, String file, ProgressMonitor monitor)
  {
    ResultSet results;
    try
    {
      BufferedReader rea = new BufferedReader(new InputStreamReader(
          new FileInputStream(path + "/" + file), "ISO-8859-1"));
      String line = "";
      boolean abbruch = false;
      while ((line = rea.readLine()) != null)
      {
        int pos = line.indexOf("\"");
        if (pos >= 0)
        {
          monitor.log("Zeile enthält Anführungszeichen: " + line);
          abbruch = true;
        }
      }
      rea.close();
      if (abbruch)
      {
        monitor.log("Abbruch");
        return;
      }
      loescheBestand();
      int anz = 0;

      // 
      // Zusatzfelder ermitteln
      DBIterator it = Einstellungen.getDBService().createList(
          Felddefinition.class);
      Felddefinition[] zusfeld = new Felddefinition[it.size()];
      for (int i = 0; i < it.size(); i++)
      {
        zusfeld[i] = (Felddefinition) it.next();
      }

      Properties props = new java.util.Properties();
      props.put("separator", ";"); // separator is a bar
      props.put("suppressHeaders", "false"); // first line contains data
      props.put("charset", "ISO-8859-1");
      int pos = file.lastIndexOf('.');
      props.put("fileExtension", file.substring(pos));

      // load the driver into memory
      Class.forName("org.relique.jdbc.csv.CsvDriver");

      // create a connection. The first command line parameter is assumed to
      // be the directory in which the .csv files are held
      Connection conn = DriverManager.getConnection("jdbc:relique:csv:" + path,
          props);

      // create a Statement object to execute the query with
      Statement stmt = conn.createStatement();

      if (!checkeBeitragsgruppen(monitor, file.substring(0, pos), stmt))
      {
        monitor.log("Abbruch");
        return;
      }

      HashMap<String, String> beitragsgruppen2 = aufbauenBeitragsgruppenAusImport(
          file.substring(0, pos), stmt);

      results = stmt.executeQuery("SELECT * FROM " + file.substring(0, pos));

      eigenschaftgruppe = (EigenschaftGruppe) Einstellungen.getDBService()
          .createObject(EigenschaftGruppe.class, null);
      eigenschaftgruppe.setBezeichnung("Noch nicht zugeordnet");
      eigenschaftgruppe.store();

      ArrayList<String> eigenschaftenspalten = getEigenschaftspalten(results);
      while (results.next())
      {
        anz++;
        monitor.setStatus(anz);
        monitor.log("ID= " + results.getString("Mitglieds_Nr") + " NAME= "
            + results.getString("Nachname"));

        Mitglied m = (Mitglied) Einstellungen.getDBService().createObject(
            Mitglied.class, null);

        m.setID(results.getString("Mitglieds_Nr"));
        if (Einstellungen.getEinstellung().getExterneMitgliedsnummer())
        {
          m.setExterneMitgliedsnummer(new Integer(results
              .getString("Mitglieds_Nr")));
        }
        try
        {
          m.setPersonenart(results.getString("Personenart"));
        }
        catch (Exception e)
        {
          m.setPersonenart("n");
        }

        m.setAnrede(results.getString("Anrede"));
        m.setTitel(results.getString("Titel"));
        m.setName(results.getString("Nachname"));
        m.setVorname(results.getString("Vorname"));
        try
        {
          m.setAdressierungszusatz(results.getString("Adressierungszusatz"));
        }
        catch (Exception e)
        {
          m.setAdressierungszusatz("");
        }

        try
        {
          m.setStrasse(results.getString("Straße"));
        }
        catch (Exception e)
        {
          m.setStrasse(results.getString("Strasse"));
        }
        m.setPlz(results.getString("Plz"));
        m.setOrt(results.getString("Ort"));
        m.setGeburtsdatum(results.getString("Geburtsdatum"));
        m.setGeschlecht(results.getString("Geschlecht"));
        m.setBlz(results.getString("Bankleitzahl"));
        m.setKonto(results.getString("Kontonummer"));

        if (results.getString("Zahlungsart").equals("l"))
        {
          m.setZahlungsweg(Zahlungsweg.ABBUCHUNG);
        }
        else if (results.getString("Zahlungsart").equals("b"))
        {
          m.setZahlungsweg(Zahlungsweg.BARZAHLUNG);
        }
        else if (results.getString("Zahlungsart").equals("u"))
        {
          m.setZahlungsweg(Zahlungsweg.ÜBERWEISUNG);
        }
        else
        {
          monitor.log(m.getNameVorname()
              + " ungültige Zahlungsart. Bar wird angenommen.");
          m.setZahlungsweg(Zahlungsweg.BARZAHLUNG);
        }
        String zahlungsrhytmus = "12";
        try
        {
          zahlungsrhytmus = results.getString("Zahlungsrhytmus");
        }
        catch (SQLException e)
        {
          // Nichts tun
        }
        m.setZahlungsrhytmus(Integer.parseInt(zahlungsrhytmus));
        m.setKontoinhaber(results.getString("Zahler"));
        m.setTelefonprivat(results.getString("Telefon_privat"));
        m.setTelefondienstlich(results.getString("Telefon_dienstlich"));
        try
        {
          m.setHandy(results.getString("Handy"));
        }
        catch (SQLException e)
        {
          // Nichts tun
        }
        if (m.getTelefondienstlich() == null)
        {
          m.setTelefondienstlich("");
        }
        if (m.getTelefonprivat() == null)
        {
          m.setTelefonprivat("");
        }
        if (m.getHandy() == null)
        {
          m.setHandy("");
        }
        m.setEmail(results.getString("Email"));
        if (m.getEmail() == null)
        {
          m.setEmail("");
        }
        String eintritt = results.getString("Eintritt");
        if (eintritt == null || eintritt.length() == 0
            || eintritt.equals("00.00.0000"))
        {
          eintritt = "01.01.1900";
        }
        m.setEintritt(eintritt);
        Integer bg = new Integer(beitragsgruppen2.get(results
            .getString("Beitragsart_1")));
        m.setBeitragsgruppe(bg);
        // beitragsart.setValue(results.getString("Beitragsart_1"));
        String austritt = results.getString("Austritt");
        if (austritt != null && austritt.equals("00.00.0000"))
        {
          austritt = null;
        }
        m.setAustritt(austritt);
        String kuendigung;

        try
        {
          kuendigung = results.getString("Kündigung");
        }
        catch (Exception e)
        {
          kuendigung = results.getString("Kuendigung");
        }
        if (kuendigung != null && kuendigung.equals("00.00.0000"))
        {
          kuendigung = null;
        }
        m.setKuendigung(kuendigung);
        m.insert();
        for (Felddefinition f : zusfeld)
        {
          Zusatzfelder zf = (Zusatzfelder) Einstellungen.getDBService()
              .createObject(Zusatzfelder.class, null);
          zf.setMitglied(new Integer(m.getID()));
          zf.setFelddefinition(new Integer(f.getID()));
          zf.setFeld(results.getString(f.getName()));
          zf.store();
        }
        for (String feld : eigenschaftenspalten)
        {
          String eig = results.getString(feld);
          if (eig.length() == 0)
          {
            continue;
          }
          Eigenschaften eigenschaften = (Eigenschaften) Einstellungen
              .getDBService().createObject(Eigenschaften.class, null);
          eigenschaften.setMitglied(m.getID());
          eigenschaften.setEigenschaft(getEigenschaftID(eig));
          eigenschaften.store();
        }

      }

      // clean up
      results.close();
      stmt.close();
      conn.close();
    }
    catch (Exception e)
    {
      monitor.log(" nicht importiert: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void loescheBestand()
  {
    try
    {
      // Zusatzbeträge
      DBIterator list = Einstellungen.getDBService().createList(
          Zusatzbetrag.class);
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
      // Manueller Zahlungseingang
      list = Einstellungen.getDBService().createList(
          ManuellerZahlungseingang.class);
      while (list.hasNext())
      {
        ManuellerZahlungseingang m = (ManuellerZahlungseingang) list.next();
        m.delete();
      }
      // Abrechnung
      list = Einstellungen.getDBService().createList(Abrechnung.class);
      while (list.hasNext())
      {
        Abrechnung abr = (Abrechnung) list.next();
        abr.delete();
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
      // Eigenschaft
      list = Einstellungen.getDBService().createList(EigenschaftGruppe.class);
      while (list.hasNext())
      {
        EigenschaftGruppe e = (EigenschaftGruppe) list.next();
        e.delete();
      }
      // Mitglieder
      list = Einstellungen.getDBService().createList(Mitglied.class);
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
      e.printStackTrace();
    }
    catch (ApplicationException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Beitragsgruppen in der Importdatei prüfen
   * 
   * @param monitor
   * @param file
   * @param stmt
   * @return true, wenn alles in Ordnung ist.
   * @throws SQLException
   */
  private boolean checkeBeitragsgruppen(ProgressMonitor monitor, String file,
      Statement stmt) throws SQLException
  {
    ResultSet results = stmt.executeQuery("SELECT * FROM " + file);
    while (results.next())
    {
      String ba = results.getString("Beitragsart_1");
      String btr = results.getString("Beitrag_1");
      if (ba == null || ba.length() == 0 || btr == null || btr.length() == 0)
      {
        monitor.log(results.getString("Nachname") + ", "
            + results.getString("Vorname") + " keine Angaben zur Beitragsart");
        return false;
      }
    }
    return true;
  }

  @SuppressWarnings("unchecked")
  private HashMap<String, String> aufbauenBeitragsgruppenAusImport(String file,
      Statement stmt) throws SQLException, RemoteException,
      ApplicationException
  {
    HashMap<String, Double> beitragsgruppen1 = new HashMap<String, Double>();
    ResultSet results = stmt.executeQuery("SELECT * FROM " + file);

    while (results.next())
    {
      beitragsgruppen1.put(results.getString("Beitragsart_1"), new Double(
          results.getString("Beitrag_1").replace(',', '.')));
    }
    Set keys = beitragsgruppen1.keySet();
    Iterator it = keys.iterator();
    HashMap<String, String> beitragsgruppen2 = new HashMap<String, String>();
    while (it.hasNext())
    {
      Beitragsgruppe b = (Beitragsgruppe) Einstellungen.getDBService()
          .createObject(Beitragsgruppe.class, null);
      String key = (String) it.next();
      b.setBezeichnung(key);
      Double betr = beitragsgruppen1.get(key);
      b.setBetrag(betr.doubleValue());
      b.store();
      beitragsgruppen2.put(key, b.getID());
    }
    return beitragsgruppen2;
  }

  private String getEigenschaftID(String eigenschaft)
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
        eigenschaftneu.setEigenschaftGruppe(new Integer(eigenschaftgruppe
            .getID()));
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

  private ArrayList<String> getEigenschaftspalten(ResultSet results)
      throws SQLException
  {
    ArrayList<String> ret = new ArrayList<String>();
    ResultSetMetaData rsm = results.getMetaData();
    int anzspalten = rsm.getColumnCount();
    for (int i = 1; i <= anzspalten; i++)
    {
      String colname = rsm.getColumnName(i);
      System.out.println(colname);
      if (colname.startsWith(EIGENSCHAFT))
      {
        ret.add(colname);
      }
    }
    return ret;
  }

}
