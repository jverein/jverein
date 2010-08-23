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
 * Revision 1.83  2010/05/16 10:43:42  jost
 * Einheitlicher Umgang mit ausgetretenen Mitgliedern
 *
 * Revision 1.82  2010/04/12 17:21:08  jost
 * ‹berfl¸ssigen Code entfernt.
 *
 * Revision 1.81  2010/04/08 17:56:20  jost
 * Bugfix
 *
 * Revision 1.80  2010/03/30 17:43:04  jost
 * Eigenschaftenauswahl: Klick auf Gruppe ‰ndert alle Kind-Eintr‰ge
 *
 * Revision 1.79  2010/03/27 20:18:24  jost
 * ‹berfl¸ssigen Code entfernt.
 *
 * Revision 1.78  2010/03/27 20:08:48  jost
 * EigenschaftenAuswahl ¸berarbeitet.
 *
 * Revision 1.77  2010/02/15 19:53:16  jost
 * IBAN direkt bei der Eingabe von Kontonummer und BLZ berechnen
 *
 * Revision 1.76  2010/02/08 18:14:17  jost
 * JaNeinFormatter f. Zusatzzahlungen
 *
 * Revision 1.75  2010/02/01 20:58:33  jost
 * Vermeidung Warnings.
 *
 * Revision 1.74  2010/01/01 22:35:44  jost
 * Standardwerte f¸r Zahlungsweg und Zahlungsrhytmus kˆnnen vorgegeben werden.
 *
 * Revision 1.73  2010/01/01 20:27:58  jost
 * Konkrete Fehlermeldung, wenn bei der Erstellung einer Altersjubil‰umsliste der Eintag in den Stammdaten fehlt.
 *
 * Revision 1.72  2010/01/01 18:38:04  jost
 * Typisierung der Zusatzfelder
 *
 * Revision 1.71  2009/12/03 16:32:26  jost
 * Bugfix Anzeige Eigenschaften
 *
 * Revision 1.70  2009/11/19 19:43:49  jost
 * Bugfix Eigenschaften
 *
 * Revision 1.69  2009/11/17 20:56:55  jost
 * Neu: Eigenschaft und EigenschaftGruppe
 *
 * Revision 1.68  2009/10/20 17:57:36  jost
 * Neu: Anzeige IBAN
 *
 * Revision 1.67  2009/07/27 15:05:21  jost
 * Vereinheitlichung Eigenschaften
 *
 * Revision 1.66  2009/07/24 20:19:00  jost
 * Focus auf erstes Feld setzen.
 *
 * Revision 1.65  2009/07/18 13:43:08  jost
 * Logging erweitert.
 *
 * Revision 1.64  2009/06/22 18:13:57  jost
 * Einheitliche Ausgabe von Fehlermeldungen in der Statusbar
 *
 * Revision 1.63  2009/06/21 11:45:46  jost
 * Pflichtfelder als Pflichtfelder markiert.
 *
 * Revision 1.62  2009/06/21 08:53:00  jost
 * Ausgabe von Langtexten beim Geschlecht.
 *
 * Revision 1.61  2009/06/11 21:02:30  jost
 * Vorbereitung I18N
 *
 * Revision 1.60  2009/05/20 05:55:14  jost
 * SearchInput f¸r Name, Vorname und Straﬂennamen
 *
 * Revision 1.59  2009/05/13 20:46:55  jost
 * Straﬂe als Suchfeld
 *
 * Revision 1.58  2009/04/25 05:28:26  jost
 * Neu: Juristische Personen  kˆnnen als Mitglied gespeichert werden.
 *
 * Revision 1.57  2009/04/13 11:39:33  jost
 * Neu: Lehrg‰nge
 *
 * Revision 1.56  2009/04/10 09:41:45  jost
 * Versuch "Reports" abgebrochen
 *
 * Revision 1.55  2009/03/26 20:59:06  jost
 * Neu: Reports - Erste Version
 *
 * Revision 1.54  2009/03/07 14:19:43  jost
 * Bugfix: Beitragsgruppe gelˆscht aber noch in den Default-Werten referenziert.
 *
 * Revision 1.53  2009/01/22 21:05:57  jost
 * Zus‰tzliches Jahr in die Vergangenheit.
 *
 * Revision 1.52  2009/01/22 18:24:01  jost
 * neue Icons
 *
 * Revision 1.51  2009/01/20 20:09:24  jost
 * neue Icons
 *
 * Revision 1.50  2008/12/29 08:40:49  jost
 * Korrekte Verarbeitung bei fehlendem Geburts- und/oder Eintrittsdatum
 *
 * Revision 1.49  2008/12/22 21:43:46  jost
 * Telefonnummern auf 20 Stellen erweitert.
 *
 * Revision 1.48  2008/12/22 21:09:21  jost
 * Zusatzabbuchung->Zusatzbetrag
 *
 * Revision 1.47  2008/12/19 06:53:30  jost
 * Bugfix Dropdown Zahlungsweg
 *
 * Revision 1.46  2008/12/13 16:22:22  jost
 * Bugfix Standardwert
 *
 * Revision 1.45  2008/11/30 10:45:05  jost
 * Neu: Konfiguration der Spalten einer Tabelle
 *
 * Revision 1.44  2008/11/29 13:07:54  jost
 * Refactoring: Code-Optimierung
 *
 * Revision 1.43  2008/11/16 16:56:53  jost
 * Speicherung der Einstellung von Property-Datei in die Datenbank verschoben.
 *
 * Revision 1.42  2008/11/13 20:17:13  jost
 * Adressierungszusatz aufgenommen.
 *
 * Revision 1.41  2008/10/01 14:17:29  jost
 * Warnungen entfernt
 *
 * Revision 1.40  2008/09/21 08:45:18  jost
 * Neu: Mitgliedschaftsjubil√§en
 *
 * Revision 1.39  2008/07/11 07:34:00  jost
 * Ausgabeverzeichnis f√ºr den n√§chsten Aufruf merken.
 *
 * Revision 1.38  2008/07/10 09:20:24  jost
 * redaktionelle √Ñnderung
 *
 * Revision 1.37  2008/06/29 07:58:01  jost
 * Neu: Handy
 *
 * Revision 1.36  2008/05/22 06:49:00  jost
 * Vermeiund NPE
 *
 * Revision 1.35  2008/05/05 18:21:49  jost
 * Bugfix NPE bei Zusatzfeldern
 *
 * Revision 1.34  2008/04/10 18:58:30  jost
 * Neu: Benutzerdefinierte Datenfelder
 *
 * Revision 1.33  2008/04/04 15:14:12  jost
 * Felder Titel und PLZ verl√§ngert.
 *
 * Revision 1.32  2008/03/17 20:22:12  jost
 * Bugfix Eintritts- und Austrittsdatum aus dem Bereich Auswertung wurden auch im Dialog ber√ºcksichtigt.
 *
 * Revision 1.31  2008/03/08 19:29:07  jost
 * Neu: Externe Mitgliedsnummer
 *
 * Revision 1.30  2008/01/27 10:17:22  jost
 * Vereinheitlichung der Mitgliedersuche durch die Klasse MitgliedQuery
 *
 * Revision 1.29  2008/01/27 09:41:28  jost
 * Vereinheitlichung der Mitgliedersuche durch die Klasse MitgliedQuery
 *
 * Revision 1.28  2008/01/26 16:21:58  jost
 * Sortierung der Beitragsgruppen eingef√ºhrt
 * Bugfix Filter.
 *
 * Revision 1.27  2008/01/25 16:02:32  jost
 * Neu: Eigenschaften des Mitgliedes
 *
 * Revision 1.26  2008/01/01 13:13:56  jost
 * Neu: Dateinamenmuster
 *
 * Revision 1.25  2007/12/22 08:25:13  jost
 * Neu: Jubil√§enliste
 *
 * Revision 1.24  2007/12/21 11:27:46  jost
 * Mitgliederstatistik jetzt Stichtagsbezogen
 *
 * Revision 1.23  2007/12/16 20:25:21  jost
 * Mitgliederstatistik l√§uft jetzt in einem eigenen Thread
 *
 * Revision 1.22  2007/12/02 13:39:47  jost
 * Neu: Beitragsmodelle
 *
 * Revision 1.21  2007/12/01 19:05:58  jost
 * Neu: Geburtstagsliste
 *
 * Revision 1.20  2007/12/01 10:05:49  jost
 * √Ñnderung wg. neuem Classloader in Jameica
 *
 * Revision 1.19  2007/09/16 17:52:04  jost
 * Selektion nach Mitgliedsstatus
 *
 * Revision 1.18  2007/09/06 17:15:46  jost
 * Mitgliederstatistik: *.PDF als Standardvorgabe im Datei-Speichern-Dialog
 *
 * Revision 1.17  2007/08/31 05:35:32  jost
 * Automatische Erg√§nzung der Dateiendung.
 *
 * Revision 1.16  2007/08/30 19:48:29  jost
 * 1. Korrekte Darstellung von Pflichtfeldern
 * 2. Neues Kontext-Men√º
 *
 * Revision 1.15  2007/08/24 13:33:53  jost
 * Bugfix
 *
 * Revision 1.14  2007/08/22 20:43:17  jost
 * Bug #011762
 *
 * Revision 1.13  2007/05/07 19:25:03  jost
 * Neu: Wiedervorlage
 *
 * Revision 1.12  2007/03/30 18:42:53  jost
 * Bei der Neuerfassung von Mitgliedern wird nach der Eingabe der PLZ automatisch der Ort eingetragen, sofern die PLZ bereits im Mitgliederbestand gespeichert ist.
 *
 * Revision 1.11  2007/03/30 13:22:57  jost
 * Wiederkehrende Zusatzabbuchungen.
 *
 * Revision 1.10  2007/03/27 19:21:10  jost
 * Familienangehˆrige anzeigen
 *
 * Revision 1.9  2007/03/25 16:57:40  jost
 * 1. Famlienverband herstellen
 * 2. Tab mit allen Mitgliedern
 *
 * Revision 1.8  2007/03/18 08:38:49  jost
 * Pflichtfelder gekennzeichnet
 * Bugfix Zahlungsweg
 *
 * Revision 1.7  2007/03/10 20:28:01  jost
 * Neu: Zahlungsweg
 *
 * Revision 1.6  2007/03/10 13:41:34  jost
 * Vermerke eingef√ºhrt.
 *
 * Revision 1.5  2007/02/23 20:26:38  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.4  2006/12/20 20:25:44  jost
 * Patch von Ullrich Sch‰fer, der die Primitive vs. Object Problematik adressiert.
 *
 * Revision 1.3  2006/10/29 07:48:29  jost
 * Neu: Mitgliederstatistik
 *
 * Revision 1.2  2006/10/20 07:36:14  jost
 * Fehlermeldung ausgeben, wenn keine Beitragsgruppe ausgew‰hlt wurde.
 *
 * Revision 1.1  2006/09/20 15:38:30  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.io.File;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.Queries.MitgliedQuery;
import de.jost_net.JVerein.gui.action.LehrgangAction;
import de.jost_net.JVerein.gui.action.MitgliedDetailAction;
import de.jost_net.JVerein.gui.action.WiedervorlageAction;
import de.jost_net.JVerein.gui.action.ZusatzbetraegeAction;
import de.jost_net.JVerein.gui.dialogs.EigenschaftenAuswahlDialog;
import de.jost_net.JVerein.gui.formatter.JaNeinFormatter;
import de.jost_net.JVerein.gui.input.GeschlechtInput;
import de.jost_net.JVerein.gui.menu.LehrgangMenu;
import de.jost_net.JVerein.gui.menu.MitgliedMenu;
import de.jost_net.JVerein.gui.menu.WiedervorlageMenu;
import de.jost_net.JVerein.gui.menu.ZusatzbetraegeMenu;
import de.jost_net.JVerein.gui.parts.Familienverband;
import de.jost_net.JVerein.io.Jubilaeenliste;
import de.jost_net.JVerein.io.MitgliedAuswertungCSV;
import de.jost_net.JVerein.io.MitgliedAuswertungPDF;
import de.jost_net.JVerein.io.MitgliederStatistik;
import de.jost_net.JVerein.keys.ArtBeitragsart;
import de.jost_net.JVerein.keys.Datentyp;
import de.jost_net.JVerein.keys.Zahlungsrhytmus;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Eigenschaft;
import de.jost_net.JVerein.rmi.Eigenschaften;
import de.jost_net.JVerein.rmi.Felddefinition;
import de.jost_net.JVerein.rmi.Lehrgang;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Wiedervorlage;
import de.jost_net.JVerein.rmi.Zusatzbetrag;
import de.jost_net.JVerein.rmi.Zusatzfelder;
import de.jost_net.JVerein.server.EigenschaftenNode;
import de.jost_net.JVerein.server.MitgliedUtils;
import de.jost_net.JVerein.util.Dateiname;
import de.jost_net.JVerein.util.IbanBicCalc;
import de.jost_net.JVerein.util.MitgliedSpaltenauswahl;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.GenericObjectNode;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ObjectNotFoundException;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.formatter.TreeFormatter;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.DecimalInput;
import de.willuhn.jameica.gui.input.DialogInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.IntegerInput;
import de.willuhn.jameica.gui.input.SearchInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextAreaInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.gui.parts.TreePart;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class MitgliedControl extends AbstractControl
{
  private IntegerInput externemitgliedsnummer;

  private Input anrede;

  private Input titel;

  private SearchInput name;

  private SearchInput vorname;

  private Input adressierungszusatz;

  private SearchInput strasse;

  private Input plz;

  private Input ort;

  private DateInput geburtsdatum = null;

  private GeschlechtInput geschlecht;

  private SelectInput zahlungsweg;

  private SelectInput zahlungsrhytmus;

  private Input blz;

  private Input konto;

  private Input iban;

  private Input kontoinhaber;

  private Input telefonprivat;

  private Input telefondienstlich;

  private Input handy;

  private Input email;

  private DateInput eintritt = null;

  private SelectInput beitragsgruppe;

  private Familienverband famverb;

  private SelectInput zahler;

  private DateInput austritt = null;

  private DateInput kuendigung = null;

  private Input[] zusatzfelder;

  private TreePart eigenschaftenTree;

  private TreePart eigenschaftenAuswahlTree;

  // Elemente f¸r die Auswertung

  private DateInput geburtsdatumvon = null;

  private DateInput geburtsdatumbis = null;

  private DateInput eintrittvon = null;

  private DateInput eintrittbis;

  private DateInput austrittvon;

  private DateInput austrittbis;

  private TextAreaInput vermerk1;

  private TextAreaInput vermerk2;

  private SelectInput ausgabe;

  private SelectInput sortierung;

  private SelectInput status;

  private DateInput stichtag;

  private SelectInput jubeljahr;

  private SelectInput jubelart;

  public final static String JUBELART_MITGLIEDSCHAFT = "Mitgliedschaftsjubil‰en";

  public final static String JUBELART_ALTER = "Altersjubil‰en";

  private SelectInput beitragsgruppeausw;

  private DialogInput eigenschaftenabfrage;

  private IntegerInput suchexternemitgliedsnummer;

  private Mitglied mitglied;

  // Liste aller Zusatzbetr‰ge
  private TablePart zusatzbetraegeList;

  // Liste der Wiedervorlagen
  private TablePart wiedervorlageList;

  // Liste der Lehrg‰nge
  private TablePart lehrgaengeList;

  private TablePart familienangehoerige;

  private Settings settings = null;

  public MitgliedControl(AbstractView view)
  {
    super(view);
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public Mitglied getMitglied()
  {
    if (mitglied != null)
    {
      return mitglied;
    }
    mitglied = (Mitglied) getCurrentObject();
    return mitglied;
  }

  public IntegerInput getExterneMitgliedsnummer() throws RemoteException
  {
    if (externemitgliedsnummer != null)
    {
      return externemitgliedsnummer;
    }
    Integer ex = getMitglied().getExterneMitgliedsnummer();
    if (ex == null)
    {
      ex = -1;
    }
    externemitgliedsnummer = new IntegerInput(ex);
    externemitgliedsnummer.setName("Ext. Mitgliedsnummer");
    return externemitgliedsnummer;
  }

  public Input getAnrede() throws RemoteException
  {
    if (anrede != null)
    {
      return anrede;
    }
    anrede = new TextInput(getMitglied().getAnrede(), 40);
    anrede.setName("Anrede");
    return anrede;
  }

  public Input getTitel() throws RemoteException
  {
    if (titel != null)
    {
      return titel;
    }
    titel = new TextInput(getMitglied().getTitel(), 40);
    titel.setName("Titel");
    return titel;
  }

  public SearchInput getName(boolean withFocus) throws RemoteException
  {
    if (name != null)
    {
      return name;
    }

    name = new SearchInput()
    {
      public List<?> startSearch(String text)
      {
        try
        {
          if (text != null)
          {
            text = text + "%";
          }
          ResultSetExtractor rs = new ResultSetExtractor()
          {
            public Object extract(ResultSet rs) throws RemoteException,
                SQLException
            {
              List<String> namen = new ArrayList<String>();
              while (rs.next())
              {
                namen.add(rs.getString(1));
              }
              return namen;
            }
          };
          String sql = "select name from mitglied where name like ? "
              + "group by name order by name";
          return (List<?>) Einstellungen.getDBService().execute(sql,
              new Object[] { text }, rs);
        }
        catch (Exception e)
        {
          Logger.error("kann Namenliste nicht aufbauen", e);
          return null;
        }
      }
    };
    name.setValue(getMitglied().getName());
    name.setName("Name");
    name.setMaxLength(40);
    name.setDelay(1000);
    name.setMandatory(true);
    name.setSearchString("");
    if (withFocus)
    {
      name.focus();
    }
    return name;
  }

  public SearchInput getVorname() throws RemoteException
  {
    if (vorname != null)
    {
      return vorname;
    }

    vorname = new SearchInput()
    {
      public List<?> startSearch(String text)
      {
        try
        {
          if (text != null)
          {
            text = text + "%";
          }
          ResultSetExtractor rs = new ResultSetExtractor()
          {
            public Object extract(ResultSet rs) throws RemoteException,
                SQLException
            {
              List<String> vornamen = new ArrayList<String>();
              while (rs.next())
              {
                vornamen.add(rs.getString(1));
              }
              return vornamen;
            }
          };
          String sql = "select vorname from mitglied where vorname like ? "
              + "group by vorname order by vorname";
          return (List<?>) Einstellungen.getDBService().execute(sql,
              new Object[] { text }, rs);
        }
        catch (Exception e)
        {
          Logger.error("kann Vornamenliste nicht aufbauen", e);
          return null;
        }
      }
    };
    vorname.setValue(getMitglied().getVorname());
    vorname.setName("Vorname");
    vorname.setMaxLength(40);
    vorname.setDelay(1000);
    vorname.setSearchString("");
    vorname.setMandatory(true);
    return vorname;
  }

  public Input getAdressierungszusatz() throws RemoteException
  {
    if (adressierungszusatz != null)
    {
      return adressierungszusatz;
    }
    adressierungszusatz = new TextInput(getMitglied().getAdressierungszusatz(),
        40);
    adressierungszusatz.setName("Adressierungszusatz");
    return adressierungszusatz;
  }

  public SearchInput getStrasse() throws RemoteException
  {
    if (strasse != null)
    {
      return strasse;
    }
    // strasse = new TextInput(getMitglied().getStrasse(), 40);

    strasse = new SearchInput()
    {
      public List<?> startSearch(String text)
      {
        try
        {
          if (text != null)
          {
            text = text + "%";
          }
          ResultSetExtractor rs = new ResultSetExtractor()
          {
            public Object extract(ResultSet rs) throws RemoteException,
                SQLException
            {
              List<String> strassen = new ArrayList<String>();
              while (rs.next())
              {
                strassen.add(rs.getString(1));
              }
              return strassen;
            }
          };
          String sql = "select strasse from mitglied where strasse like ? "
              + "group by strasse order by strasse";
          return (List<?>) Einstellungen.getDBService().execute(sql,
              new Object[] { text }, rs);
        }
        catch (Exception e)
        {
          Logger.error("kann Straﬂenliste nicht aufbauen", e);
          return null;
        }
      }
    };
    strasse.setValue(getMitglied().getStrasse());
    strasse.setText("bla");
    strasse.setName("Straﬂe");
    strasse.setMaxLength(40);
    strasse.setDelay(1000);
    strasse.setSearchString("");

    return strasse;
  }

  public Input getPlz() throws RemoteException
  {
    if (plz != null)
    {
      return plz;
    }
    plz = new TextInput(getMitglied().getPlz(), 10);
    plz.setName("PLZ");
    plz.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        if (event.type == SWT.FocusOut)
        {
          try
          {
            DBIterator it = Einstellungen.getDBService().createList(
                Mitglied.class);
            it.addFilter("plz='" + (String) plz.getValue() + "'");
            if (it.hasNext())
            {
              Mitglied mplz = (Mitglied) it.next();
              ort.setValue(mplz.getOrt());
            }
          }
          catch (RemoteException e)
          {
            e.printStackTrace();
          }
        }
      }
    });
    return plz;
  }

  public Input getOrt() throws RemoteException
  {
    if (ort != null)
    {
      return ort;
    }
    ort = new TextInput(getMitglied().getOrt(), 40);
    ort.setName("Ort");
    return ort;
  }

  public DateInput getGeburtsdatum() throws RemoteException
  {
    if (geburtsdatum != null)
    {
      return geburtsdatum;
    }
    Date d = getMitglied().getGeburtsdatum();
    if (d.equals(Einstellungen.NODATE))
    {
      d = null;
    }
    this.geburtsdatum = new DateInput(d, Einstellungen.DATEFORMAT);
    this.geburtsdatum.setName("Geburtsdatum");
    this.geburtsdatum.setTitle("Geburtsdatum");
    this.geburtsdatum.setText("Bitte Geburtsdatum w‰hlen");
    this.geburtsdatum.setMandatory(Einstellungen.getEinstellung()
        .getGeburtsdatumPflicht());
    this.geburtsdatum.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) geburtsdatum.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return geburtsdatum;
  }

  public GeschlechtInput getGeschlecht() throws RemoteException
  {
    if (geschlecht != null)
    {
      return geschlecht;
    }
    geschlecht = new GeschlechtInput(getMitglied().getGeschlecht());
    geschlecht.setName("Geschlecht");
    geschlecht.setPleaseChoose("Bitte ausw‰hlen");
    geschlecht.setMandatory(true);
    return geschlecht;
  }

  public SelectInput getZahlungsweg() throws RemoteException
  {
    if (zahlungsweg != null)
    {
      return zahlungsweg;
    }
    if (getMitglied().getZahlungsweg() != null)
    {
      zahlungsweg = new SelectInput(Zahlungsweg.getArray(), new Zahlungsweg(
          getMitglied().getZahlungsweg().intValue()));
    }
    else
    {
      zahlungsweg = new SelectInput(Zahlungsweg.getArray(), new Zahlungsweg(
          Einstellungen.getEinstellung().getZahlungsweg()));
    }
    zahlungsweg.setName("Zahlungsweg");
    zahlungsweg.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Zahlungsweg z = (Zahlungsweg) zahlungsweg.getValue();
        blz.setMandatory(z.getKey() == Zahlungsweg.ABBUCHUNG);
        konto.setMandatory(z.getKey() == Zahlungsweg.ABBUCHUNG);
      }
    });
    return zahlungsweg;
  }

  public SelectInput getZahlungsrhytmus() throws RemoteException
  {
    if (zahlungsrhytmus != null)
    {
      return zahlungsrhytmus;
    }
    if (getMitglied().getZahlungsrhytmus() != null)
    {
      zahlungsrhytmus = new SelectInput(Zahlungsrhytmus.getArray(),
          new Zahlungsrhytmus(getMitglied().getZahlungsrhytmus().intValue()));
    }
    else
    {
      zahlungsrhytmus = new SelectInput(Zahlungsrhytmus.getArray(),
          new Zahlungsrhytmus(Einstellungen.getEinstellung()
              .getZahlungsrhytmus()));
    }
    zahlungsrhytmus.setName("Zahlungsrhytmus");
    return zahlungsrhytmus;
  }

  public Input getBlz() throws RemoteException
  {
    if (blz != null)
    {
      return blz;
    }
    blz = new TextInput(getMitglied().getBlz(), 8);
    blz.setName("BLZ");
    blz.setMandatory(getMitglied().getZahlungsweg() == null
        || getMitglied().getZahlungsweg().intValue() == Zahlungsweg.ABBUCHUNG);
    BLZListener l = new BLZListener();
    blz.addListener(l);
    l.handleEvent(null); // Einmal initial ausfuehren
    blz.addListener(new Listener()
    {
      public void handleEvent(Event arg0)
      {
        try
        {
          getIban().setValue(
              IbanBicCalc.createIban((String) getKonto().getValue(),
                  (String) getBlz().getValue(), "DE"));
        }
        catch (RemoteException e)
        {
          // 
        }
      }

    });
    return blz;
  }

  public Input getKonto() throws RemoteException
  {
    if (konto != null)
    {
      return konto;
    }
    konto = new TextInput(getMitglied().getKonto(), 10);
    konto.setName("Konto");
    konto.setMandatory(getMitglied().getZahlungsweg() == null
        || getMitglied().getZahlungsweg().intValue() == Zahlungsweg.ABBUCHUNG);
    konto.addListener(new Listener()
    {
      public void handleEvent(Event arg0)
      {
        try
        {
          getIban().setValue(
              IbanBicCalc.createIban((String) getKonto().getValue(),
                  (String) getBlz().getValue(), "DE"));
        }
        catch (RemoteException e)
        {
          // 
        }
      }
    });
    return konto;
  }

  public Input getIban() throws RemoteException
  {
    if (iban != null)
    {
      return iban;
    }
    iban = new TextInput(getMitglied().getIban(), 30);
    iban.setName("IBAN");
    iban.setMandatory(getMitglied().getZahlungsweg() == null
        || getMitglied().getZahlungsweg().intValue() == Zahlungsweg.ABBUCHUNG);
    iban.setEnabled(false);
    return iban;
  }

  public Input getKontoinhaber() throws RemoteException
  {
    if (kontoinhaber != null)
    {
      return kontoinhaber;
    }
    kontoinhaber = new TextInput(getMitglied().getKontoinhaber(), 27);
    kontoinhaber.setName("Kontoinhaber");
    return kontoinhaber;
  }

  public Input getTelefonprivat() throws RemoteException
  {
    if (telefonprivat != null)
    {
      return telefonprivat;
    }
    telefonprivat = new TextInput(getMitglied().getTelefonprivat(), 20);
    telefonprivat.setName("Telefon priv.");
    return telefonprivat;
  }

  public Input getTelefondienstlich() throws RemoteException
  {
    if (telefondienstlich != null)
    {
      return telefondienstlich;
    }
    telefondienstlich = new TextInput(getMitglied().getTelefondienstlich(), 20);
    telefondienstlich.setName("Telefon dienstl.");
    return telefondienstlich;
  }

  public Input getHandy() throws RemoteException
  {
    if (handy != null)
    {
      return handy;
    }
    handy = new TextInput(getMitglied().getHandy(), 20);
    handy.setName("Handy");
    return handy;
  }

  public Input getEmail() throws RemoteException
  {
    if (email != null)
    {
      return email;
    }
    email = new TextInput(getMitglied().getEmail(), 50);
    email.setName("EMail");
    return email;
  }

  public DateInput getEintritt() throws RemoteException
  {
    if (eintritt != null)
    {
      return eintritt;
    }

    Date d = getMitglied().getEintritt();
    if (d.equals(Einstellungen.NODATE))
    {
      d = null;
    }
    this.eintritt = new DateInput(d, Einstellungen.DATEFORMAT);
    this.eintritt.setTitle("Eintrittsdatum");
    this.eintritt.setName("Eintrittsdatum");
    this.eintritt.setText("Bitte Eintrittsdatum w‰hlen");
    this.eintritt.setMandatory(Einstellungen.getEinstellung()
        .getEintrittsdatumPflicht());
    this.eintritt.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) eintritt.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return eintritt;
  }

  public Input getBeitragsgruppe() throws RemoteException
  {
    if (beitragsgruppe != null)
    {
      return beitragsgruppe;
    }
    DBIterator list = Einstellungen.getDBService().createList(
        Beitragsgruppe.class);
    list.setOrder("ORDER BY bezeichnung");
    beitragsgruppe = new SelectInput(list, getMitglied().getBeitragsgruppe());
    beitragsgruppe.setName("Beitragsgruppe");
    beitragsgruppe.setValue(getMitglied().getBeitragsgruppe());
    beitragsgruppe.setMandatory(true);
    beitragsgruppe.setAttribute("bezeichnung");
    beitragsgruppe.setPleaseChoose("Bitte ausw‰hlen");
    beitragsgruppe.addListener(new Listener()
    {

      public void handleEvent(Event event)
      {
        if (event.type != SWT.Selection)
        {
          return;
        }
        try
        {
          Beitragsgruppe bg = (Beitragsgruppe) beitragsgruppe.getValue();
          if (famverb != null)
          {
            famverb.setBeitragsgruppe(bg);
          }
          if (bg.getBeitragsArt() == ArtBeitragsart.FAMILIE_ANGEHOERIGER)
          {
            zahler.setEnabled(true);
          }
          else if (bg.getBeitragsArt() == ArtBeitragsart.FAMILIE_ZAHLER)
          {
            zahler.setValue((Mitglied) Einstellungen.getDBService()
                .createObject(Mitglied.class, ""));
            getMitglied().setZahlerID(null);
            zahler.setEnabled(false);
          }
        }
        catch (RemoteException e)
        {
          e.printStackTrace();
        }
      }
    });
    return beitragsgruppe;
  }

  public SelectInput getBeitragsgruppeAusw() throws RemoteException
  {
    if (beitragsgruppeausw != null)
    {
      return beitragsgruppeausw;
    }
    Beitragsgruppe bg = null;
    String beitragsgru = settings.getString("mitglied.beitragsgruppe", "");
    if (beitragsgru.length() > 0)
    {
      try
      {
        bg = (Beitragsgruppe) Einstellungen.getDBService().createObject(
            Beitragsgruppe.class, beitragsgru);
      }
      catch (ObjectNotFoundException e)
      {
        bg = (Beitragsgruppe) Einstellungen.getDBService().createObject(
            Beitragsgruppe.class, null);
      }
    }
    DBIterator list = Einstellungen.getDBService().createList(
        Beitragsgruppe.class);
    list.setOrder("ORDER BY bezeichnung");
    beitragsgruppeausw = new SelectInput(list, bg);
    beitragsgruppeausw.setName("Beitragsgruppe");
    beitragsgruppeausw.setAttribute("bezeichnung");
    beitragsgruppeausw.setPleaseChoose("Bitte ausw‰hlen");
    return beitragsgruppeausw;
  }

  public Familienverband getFamilienverband() throws RemoteException
  {
    if (famverb != null)
    {
      return famverb;
    }
    famverb = new Familienverband(this, getMitglied().getBeitragsgruppe());
    return famverb;
  }

  public Input getZahler() throws RemoteException
  {
    if (zahler != null)
    {
      return zahler;
    }

    String cond = "";

    // Beitragsgruppen ermitteln, die Zahler f¸r andere Mitglieder sind
    DBIterator bg = Einstellungen.getDBService().createList(
        Beitragsgruppe.class);
    bg.addFilter("beitragsart = 1");
    while (bg.hasNext())
    {
      if (cond.length() > 0)
      {
        cond += " OR ";
      }
      Beitragsgruppe beitragsgruppe = (Beitragsgruppe) bg.next();
      cond += "beitragsgruppe = " + beitragsgruppe.getID();
    }
    DBIterator zhl = Einstellungen.getDBService().createList(Mitglied.class);
    zhl.addFilter(cond);
    MitgliedUtils.setNurAktive(zhl);
    zhl.setOrder("ORDER BY name, vorname");

    String suche = "";
    if (getMitglied().getZahlerID() != null)
    {
      suche = getMitglied().getZahlerID().toString();
    }
    Mitglied zahlmitglied = (Mitglied) Einstellungen.getDBService()
        .createObject(Mitglied.class, suche);

    zahler = new SelectInput(zhl, zahlmitglied);
    zahler.setAttribute("namevorname");
    zahler.setPleaseChoose("Bitte ausw‰hlen");
    zahler.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        if (event.type != SWT.Selection)
        {
          return;
        }
        try
        {
          Mitglied m = (Mitglied) zahler.getValue();
          if (m.getID() != null)
          {
            getMitglied().setZahlerID(new Integer(m.getID()));
          }
          else
          {
            getMitglied().setZahlerID(null);
          }
        }
        catch (RemoteException e)
        {
          e.printStackTrace();
        }
      }
    });

    if (getBeitragsgruppe() != null && getBeitragsgruppe().getValue() != null)
    {
      Beitragsgruppe bg2 = (Beitragsgruppe) getBeitragsgruppe().getValue();
      if (bg2.getBeitragsArt() == ArtBeitragsart.FAMILIE_ANGEHOERIGER)
      {
        zahler.setEnabled(true);
      }
      else
      {
        zahler.setEnabled(false);
      }
    }
    return zahler;
  }

  public DateInput getAustritt() throws RemoteException
  {
    if (austritt != null)
    {
      return austritt;
    }
    Date d = getMitglied().getAustritt();

    this.austritt = new DateInput(d, Einstellungen.DATEFORMAT);
    this.austritt.setTitle("Austrittsdatum");
    this.austritt.setName("Austrittsdatum");
    this.austritt.setText("Bitte Austrittsdatum w‰hlen");
    this.austritt.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) austritt.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return austritt;
  }

  public DateInput getKuendigung() throws RemoteException
  {
    if (kuendigung != null)
    {
      return kuendigung;
    }
    Date d = getMitglied().getKuendigung();

    this.kuendigung = new DateInput(d, Einstellungen.DATEFORMAT);
    this.kuendigung.setName("K¸ndigungsdatum");
    this.kuendigung.setTitle("K¸ndigungsdatum");
    this.kuendigung.setText("Bitte K¸ndigungsdatum w‰hlen");
    this.kuendigung.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) kuendigung.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return kuendigung;
  }

  public TextAreaInput getVermerk1() throws RemoteException
  {
    if (vermerk1 != null)
    {
      return vermerk1;
    }
    vermerk1 = new TextAreaInput(getMitglied().getVermerk1(), 255);
    vermerk1.setName("Vermerk 1");
    return vermerk1;
  }

  public TextAreaInput getVermerk2() throws RemoteException
  {
    if (vermerk2 != null)
    {
      return vermerk2;
    }
    vermerk2 = new TextAreaInput(getMitglied().getVermerk2(), 255);
    vermerk2.setName("Vermerk 2");
    return vermerk2;
  }

  public Input[] getZusatzfelder() throws RemoteException
  {
    if (zusatzfelder != null)
    {
      return zusatzfelder;
    }
    DBIterator it = Einstellungen.getDBService().createList(
        Felddefinition.class);
    int anzahl = it.size();
    if (anzahl == 0)
    {
      return null;
    }
    zusatzfelder = new Input[anzahl];
    Zusatzfelder zf = null;
    int i = 0;
    while (it.hasNext())
    {
      Felddefinition fd = (Felddefinition) it.next();
      zf = (Zusatzfelder) Einstellungen.getDBService().createObject(
          Zusatzfelder.class, null);
      zf.setFelddefinition(Integer.parseInt(fd.getID()));

      if (getMitglied().getID() != null)
      {
        DBIterator it2 = Einstellungen.getDBService().createList(
            Zusatzfelder.class);
        it2.addFilter("mitglied=?", new Object[] { getMitglied().getID() });
        it2.addFilter("felddefinition=?", new Object[] { fd.getID() });
        if (it2.size() > 0)
        {
          zf.setMitglied(Integer.parseInt(getMitglied().getID()));
          zf = (Zusatzfelder) it2.next();
        }
      }
      switch (fd.getDatentyp())
      {
        case Datentyp.ZEICHENFOLGE:
          zusatzfelder[i] = new TextInput(zf.getFeld(), fd.getLaenge());
          break;
        case Datentyp.DATUM:
          Date d = zf.getFeldDatum();
          DateInput di = new DateInput(d, Einstellungen.DATEFORMAT);
          di.setName(fd.getLabel());
          di.setTitle(fd.getLabel());
          di.setText("Bitte " + fd.getLabel() + " w‰hlen");
          zusatzfelder[i] = di;
          break;
        case Datentyp.GANZZAHL:
          if (zf.getFeldGanzzahl() == null)
          {
            zf.setFeldGanzzahl(0);
          }
          zusatzfelder[i] = new IntegerInput(zf.getFeldGanzzahl());
          break;
        case Datentyp.WAEHRUNG:
          zusatzfelder[i] = new DecimalInput(zf.getFeldWaehrung(),
              Einstellungen.DECIMALFORMAT);
          break;
        case Datentyp.JANEIN:
          zusatzfelder[i] = new CheckboxInput(zf.getFeldJaNein());
          break;
        default:
          zusatzfelder[i] = new TextInput("", fd.getLaenge());
          break;
      }
      zusatzfelder[i].setName(fd.getLabel());
      if (fd.getLabel() == null)
      {
        zusatzfelder[i].setName(fd.getName());
      }
      i++;
    }
    return zusatzfelder;
  }

  public Part getFamilienangehoerigenTable() throws RemoteException
  {
    if (familienangehoerige != null)
    {
      return familienangehoerige;
    }
    DBService service = Einstellungen.getDBService();
    DBIterator famiter = service.createList(Mitglied.class);
    famiter.addFilter("zahlerid = " + getMitglied().getID());
    familienangehoerige = new TablePart(famiter, null);
    familienangehoerige.setRememberColWidths(true);
    familienangehoerige.setRememberOrder(true);

    familienangehoerige.addColumn("Name", "name");
    familienangehoerige.addColumn("Vorname", "vorname");

    return familienangehoerige;
  }

  public Part getZusatzbetraegeTable() throws RemoteException
  {
    if (zusatzbetraegeList != null)
    {
      return zusatzbetraegeList;
    }
    DBService service = Einstellungen.getDBService();
    DBIterator zusatzbetraege = service.createList(Zusatzbetrag.class);
    zusatzbetraege.addFilter("mitglied = " + getMitglied().getID());
    zusatzbetraegeList = new TablePart(zusatzbetraege,
        new ZusatzbetraegeAction(getMitglied()));
    zusatzbetraegeList.setRememberColWidths(true);
    zusatzbetraegeList.setRememberOrder(true);

    zusatzbetraegeList.addColumn("Startdatum", "startdatum", new DateFormatter(
        Einstellungen.DATEFORMAT));
    zusatzbetraegeList.addColumn("n‰chste F‰lligkeit", "faelligkeit",
        new DateFormatter(Einstellungen.DATEFORMAT));
    zusatzbetraegeList.addColumn("letzte Ausf¸hrung", "ausfuehrung",
        new DateFormatter(Einstellungen.DATEFORMAT));
    zusatzbetraegeList.addColumn("Intervall", "intervalltext");
    zusatzbetraegeList.addColumn("Endedatum", "endedatum", new DateFormatter(
        Einstellungen.DATEFORMAT));
    zusatzbetraegeList.addColumn("Buchungstext", "buchungstext");
    zusatzbetraegeList.addColumn("Betrag", "betrag", new CurrencyFormatter("",
        Einstellungen.DECIMALFORMAT));
    zusatzbetraegeList.addColumn("aktiv", "aktiv", new JaNeinFormatter());
    zusatzbetraegeList
        .setContextMenu(new ZusatzbetraegeMenu(zusatzbetraegeList));
    return zusatzbetraegeList;
  }

  public Part getWiedervorlageTable() throws RemoteException
  {
    if (wiedervorlageList != null)
    {
      return wiedervorlageList;
    }
    DBService service = Einstellungen.getDBService();
    DBIterator wiedervorlagen = service.createList(Wiedervorlage.class);
    wiedervorlagen.addFilter("mitglied = " + getMitglied().getID());
    wiedervorlageList = new TablePart(wiedervorlagen, new WiedervorlageAction(
        getMitglied()));
    wiedervorlageList.setRememberColWidths(true);
    wiedervorlageList.setRememberOrder(true);

    wiedervorlageList.addColumn("Datum", "datum", new DateFormatter(
        Einstellungen.DATEFORMAT));
    wiedervorlageList.addColumn("Vermerk", "vermerk");
    wiedervorlageList.addColumn("Erledigung", "erledigung", new DateFormatter(
        Einstellungen.DATEFORMAT));
    wiedervorlageList.setContextMenu(new WiedervorlageMenu(wiedervorlageList));
    return wiedervorlageList;
  }

  public Part getLehrgaengeTable() throws RemoteException
  {
    if (lehrgaengeList != null)
    {
      return lehrgaengeList;
    }
    DBService service = Einstellungen.getDBService();
    DBIterator lehrgaenge = service.createList(Lehrgang.class);
    lehrgaenge.addFilter("mitglied = " + getMitglied().getID());
    lehrgaengeList = new TablePart(lehrgaenge,
        new LehrgangAction(getMitglied()));
    lehrgaengeList.setRememberColWidths(true);
    lehrgaengeList.setRememberOrder(true);

    lehrgaengeList.addColumn("Lehrgangsart", "lehrgangsart");
    lehrgaengeList.addColumn("von/am", "von", new DateFormatter(
        Einstellungen.DATEFORMAT));
    lehrgaengeList.addColumn("bis", "bis", new DateFormatter(
        Einstellungen.DATEFORMAT));
    lehrgaengeList.addColumn("Veranstalter", "veranstalter");
    lehrgaengeList.addColumn("Ergebnis", "ergebnis");
    lehrgaengeList.setContextMenu(new LehrgangMenu());
    return lehrgaengeList;
  }

  public DateInput getGeburtsdatumvon() throws RemoteException
  {
    if (geburtsdatumvon != null)
    {
      return geburtsdatumvon;
    }
    Date d = null;
    String tmp = settings.getString("mitglied.geburtsdatumvon", null);
    if (tmp != null)
    {
      try
      {
        d = Einstellungen.DATEFORMAT.parse(tmp);
      }
      catch (ParseException e)
      {
        d = null;
      }
    }
    this.geburtsdatumvon = new DateInput(d, Einstellungen.DATEFORMAT);
    this.geburtsdatumvon.setTitle("Geburtsdatum");
    this.geburtsdatumvon.setText("Beginn des Geburtszeitraumes");
    this.geburtsdatumvon.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) geburtsdatumvon.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return geburtsdatumvon;
  }

  public DateInput getGeburtsdatumbis() throws RemoteException
  {
    if (geburtsdatumbis != null)
    {
      return geburtsdatumbis;
    }
    Date d = null;
    String tmp = settings.getString("mitglied.geburtsdatumbis", null);
    if (tmp != null)
    {
      try
      {
        d = Einstellungen.DATEFORMAT.parse(tmp);
      }
      catch (ParseException e)
      {
        d = null;
      }
    }
    this.geburtsdatumbis = new DateInput(d, Einstellungen.DATEFORMAT);
    this.geburtsdatumbis.setTitle("Geburtsdatum");
    this.geburtsdatumbis.setText("Ende des Geburtszeitraumes");
    this.geburtsdatumbis.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) geburtsdatumbis.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return geburtsdatumbis;
  }

  public DateInput getEintrittvon() throws RemoteException
  {
    if (eintrittvon != null)
    {
      return eintrittvon;
    }
    Date d = null;
    String tmp = settings.getString("mitglied.eintrittvon", null);
    if (tmp != null)
    {
      try
      {
        d = Einstellungen.DATEFORMAT.parse(tmp);
      }
      catch (ParseException e)
      {
        d = null;
      }
    }
    this.eintrittvon = new DateInput(d, Einstellungen.DATEFORMAT);
    this.eintrittvon.setTitle("Eintrittsdatum");
    this.eintrittvon.setText("Beginn des Eintrittszeitraumes");
    this.eintrittvon.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) eintrittvon.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return eintrittvon;
  }

  public boolean isEintrittbisAktiv()
  {
    return eintrittbis != null;
  }

  public DateInput getEintrittbis() throws RemoteException
  {
    if (eintrittbis != null)
    {
      return eintrittbis;
    }
    Date d = null;
    String tmp = settings.getString("mitglied.eintrittbis", null);
    if (tmp != null)
    {
      try
      {
        d = Einstellungen.DATEFORMAT.parse(tmp);
      }
      catch (ParseException e)
      {
        d = null;
      }
    }
    this.eintrittbis = new DateInput(d, Einstellungen.DATEFORMAT);
    this.eintrittbis.setTitle("Eintrittsdatum");
    this.eintrittbis.setText("Ende des Eintrittszeitraumes");
    this.eintrittbis.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) eintrittbis.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return eintrittbis;
  }

  public DateInput getAustrittvon() throws RemoteException
  {
    if (austrittvon != null)
    {
      return austrittvon;
    }
    Date d = null;
    String tmp = settings.getString("mitglied.austrittvon", null);
    if (tmp != null)
    {
      try
      {
        d = Einstellungen.DATEFORMAT.parse(tmp);
      }
      catch (ParseException e)
      {
        d = null;
      }
    }
    this.austrittvon = new DateInput(d, Einstellungen.DATEFORMAT);
    this.austrittvon.setTitle("Austrittsdatum");
    this.austrittvon.setText("Beginn des Austrittszeitraumes");
    this.austrittvon.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) austrittvon.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return austrittvon;
  }

  public boolean isAustrittbisAktiv()
  {
    return austrittbis != null;
  }

  public DateInput getAustrittbis() throws RemoteException
  {
    if (austrittbis != null)
    {
      return austrittbis;
    }
    Date d = null;
    String tmp = settings.getString("mitglied.austrittbis", null);
    if (tmp != null)
    {
      try
      {
        d = Einstellungen.DATEFORMAT.parse(tmp);
      }
      catch (ParseException e)
      {
        d = null;
      }
    }
    this.austrittbis = new DateInput(d, Einstellungen.DATEFORMAT);
    this.austrittbis.setTitle("Austrittsdatum");
    this.austrittbis.setText("Ende des Austrittszeitraumes");
    this.austrittbis.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) austrittbis.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return austrittbis;
  }

  public DateInput getStichtag() throws RemoteException
  {
    if (stichtag != null)
    {
      return stichtag;
    }
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.MONTH, Calendar.DECEMBER);
    cal.set(Calendar.DAY_OF_MONTH, 31);
    Date d = new Date(cal.getTimeInMillis());
    this.stichtag = new DateInput(d, Einstellungen.DATEFORMAT);
    this.stichtag.setTitle("Stichtag");
    this.stichtag.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) stichtag.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return stichtag;
  }

  public SelectInput getJubeljahr() throws RemoteException
  {
    if (jubeljahr != null)
    {
      return jubeljahr;
    }
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.YEAR, -2);
    Integer[] jubeljahre = new Integer[5];
    for (int i = 0; i < 5; i++)
    {
      jubeljahre[i] = cal.get(Calendar.YEAR);
      cal.add(Calendar.YEAR, 1);
    }
    jubeljahr = new SelectInput(jubeljahre, jubeljahre[2]);
    return jubeljahr;
  }

  public SelectInput getJubelArt() throws RemoteException
  {
    if (jubelart != null)
    {
      return jubelart;
    }
    String[] ja = { JUBELART_MITGLIEDSCHAFT, JUBELART_ALTER };
    jubelart = new SelectInput(ja, JUBELART_MITGLIEDSCHAFT);
    return jubelart;
  }

  public DialogInput getEigenschaftenAuswahl() throws RemoteException
  {
    String tmp = settings.getString("mitglied.eigenschaften", "");
    final EigenschaftenAuswahlDialog d = new EigenschaftenAuswahlDialog(tmp);
    d.addCloseListener(new EigenschaftenListener());

    StringTokenizer stt = new StringTokenizer(tmp, ",");
    String text = "";
    while (stt.hasMoreElements())
    {
      if (text.length() > 0)
      {
        text += ", ";
      }
      try
      {
        Eigenschaft ei = (Eigenschaft) Einstellungen.getDBService()
            .createObject(Eigenschaft.class, stt.nextToken());
        text += ei.getBezeichnung();
      }
      catch (ObjectNotFoundException e)
      {
        //
      }
    }
    eigenschaftenabfrage = new DialogInput(text, d);
    eigenschaftenabfrage.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        d.setDefaults(settings.getString("mitglied.eigenschaften", ""));
      }
    });
    return eigenschaftenabfrage;
  }

  public Input getAusgabe() throws RemoteException
  {
    if (ausgabe != null)
    {
      return ausgabe;
    }
    String[] ausg = { "PDF", "CSV" };
    ausgabe = new SelectInput(ausg, "PDF");
    return ausgabe;
  }

  public Input getSortierung() throws RemoteException
  {
    if (sortierung != null)
    {
      return sortierung;
    }
    String[] sort = { "Name, Vorname", "Eintrittsdatum", "Geburtsdatum",
        "Geburtstagsliste" };
    sortierung = new SelectInput(sort, "Name, Vorname");
    return sortierung;
  }

  public boolean isMitgliedStatusAktiv()
  {
    return status != null;
  }

  public IntegerInput getSuchExterneMitgliedsnummer() throws RemoteException
  {
    if (suchexternemitgliedsnummer != null)
    {
      return suchexternemitgliedsnummer;
    }
    suchexternemitgliedsnummer = new IntegerInput(-1);
    return suchexternemitgliedsnummer;
  }

  public Input getMitgliedStatus() throws RemoteException
  {
    if (status != null)
    {
      return status;
    }
    status = new SelectInput(new String[] { "Angemeldet", "Abgemeldet",
        "An- und Abgemeldete" }, settings.getString("status.mitglied",
        "Angemeldete"));
    return status;
  }

  public Button getStartAuswertungButton()
  {
    Button b = new Button("&starten", new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        try
        {
          starteAuswertung();
        }
        catch (RemoteException e)
        {
          Logger.error(e.getMessage());
          throw new ApplicationException(
              "Fehler beim Start der Mitgliederauswertung");
        }
      }
    }, null, true, "go.png"); // "true" defines this button as the default
    // button
    return b;
  }

  public Button getStartStatistikButton()
  {
    Button b = new Button("&starten", new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        try
        {
          starteStatistik();
        }
        catch (RemoteException e)
        {
          throw new ApplicationException(e);
        }
      }
    }, null, true, "go.png"); // "true" defines this button as the default
    // button
    return b;
  }

  public Button getStartJubilaeenButton()
  {
    Button b = new Button("&Start", new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        try
        {
          starteJubilaeenListe();
        }
        catch (RemoteException e)
        {
          Logger.error("Fehler:", e);
          throw new ApplicationException(e);
        }
      }
    }, null, true, "go.png"); // "true" defines this button as the default
    // button
    return b;
  }

  public Button getZusatzbetragNeu()
  {
    return new Button("Neu", new ZusatzbetraegeAction(getMitglied()), null,
        false, "document-new.png");
  }

  public Button getWiedervorlageNeu()
  {
    return new Button("Neu", new WiedervorlageAction(getMitglied()), null,
        false, "document-new.png");
  }

  public Button getLehrgangNeu()
  {
    return new Button("Neu", new LehrgangAction(getMitglied()), null, false,
        "document-new.png");
  }

  public TablePart getMitgliedTable(String anfangsbuchstabe)
      throws RemoteException
  {
    TablePart part;
    saveDefaults();
    part = new TablePart(new MitgliedQuery(this, true).get(anfangsbuchstabe),
        new MitgliedDetailAction());
    new MitgliedSpaltenauswahl().setColumns(part);
    part.setContextMenu(new MitgliedMenu());
    part.setMulti(true);
    part.setRememberColWidths(true);
    part.setRememberOrder(true);
    part.setRememberState(true);
    return part;
  }

  /**
   * Default-Werte f¸r die MitgliederSuchView speichern.
   * 
   * @throws RemoteException
   */
  public void saveDefaults() throws RemoteException
  {
    if (status != null)
    {
      settings.setAttribute("status.mitglied", (String) getMitgliedStatus()
          .getValue());
    }

    if (geburtsdatumvon != null)
    {
      Date tmp = (Date) getGeburtsdatumvon().getValue();
      if (tmp != null)
      {
        settings.setAttribute("mitglied.geburtsdatumvon",
            Einstellungen.DATEFORMAT.format(tmp));
      }
      else
      {
        settings.setAttribute("mitglied.geburtsdatumvon", "");
      }
    }

    if (geburtsdatumbis != null)
    {
      Date tmp = (Date) getGeburtsdatumbis().getValue();
      if (tmp != null)
      {
        settings.setAttribute("mitglied.geburtsdatumbis",
            Einstellungen.DATEFORMAT.format(tmp));
      }
      else
      {
        settings.setAttribute("mitglied.geburtsdatumbis", "");
      }
    }

    if (eintrittvon != null)
    {
      Date tmp = (Date) getEintrittvon().getValue();
      if (tmp != null)
      {
        settings.setAttribute("mitglied.eintrittvon", Einstellungen.DATEFORMAT
            .format(tmp));
      }
      else
      {
        settings.setAttribute("mitglied.eintrittvon", "");
      }
    }

    if (eintrittbis != null)
    {
      Date tmp = (Date) getEintrittbis().getValue();
      if (tmp != null)
      {
        settings.setAttribute("mitglied.eintrittbis", Einstellungen.DATEFORMAT
            .format(tmp));
      }
      else
      {
        settings.setAttribute("mitglied.eintrittbis", "");
      }
    }

    if (austrittvon != null)
    {
      Date tmp = (Date) getAustrittvon().getValue();
      if (tmp != null)
      {
        settings.setAttribute("mitglied.austrittvon", Einstellungen.DATEFORMAT
            .format(tmp));
      }
      else
      {
        settings.setAttribute("mitglied.austrittvon", "");
      }
    }

    if (austrittbis != null)
    {
      Date tmp = (Date) getAustrittbis().getValue();
      if (tmp != null)
      {
        settings.setAttribute("mitglied.austrittbis", Einstellungen.DATEFORMAT
            .format(tmp));
      }
      else
      {
        settings.setAttribute("mitglied.austrittbis", "");
      }
    }

    if (eigenschaftenAuswahlTree != null)
    {
      String tmp = "";
      for (Object o : eigenschaftenAuswahlTree.getItems())
      {
        EigenschaftenNode node = (EigenschaftenNode) o;
        if (node.getNodeType() == EigenschaftenNode.EIGENSCHAFTEN)
        {
          if (tmp.length() > 0)
          {
            tmp += ",";
          }
          tmp += node.getEigenschaft().getID();
        }
      }
      settings.setAttribute("mitglied.eigenschaften", tmp);
    }

    if (beitragsgruppeausw != null)
    {
      Beitragsgruppe tmpbg = (Beitragsgruppe) getBeitragsgruppeAusw()
          .getValue();
      if (tmpbg != null)
      {
        settings.setAttribute("mitglied.beitragsgruppe", tmpbg.getID());
      }
      else
      {
        settings.setAttribute("mitglied.beitragsgruppe", "");
      }
    }
  }

  public String getEigenschaftenString()
  {
    return settings.getString("mitglied.eigenschaften", "");
  }

  public TreePart getEigenschaftenTree() throws RemoteException
  {
    if (eigenschaftenTree != null)
    {
      return eigenschaftenTree;
    }
    eigenschaftenTree = new TreePart(new EigenschaftenNode(mitglied), null);
    eigenschaftenTree.setCheckable(true);
    // eigenschaftenTree.setCheckListener(new Listener()
    // {
    // public void handleEvent(Event event)
    // {
    // if (event.detail == SWT.CHECK)
    // {
    // EigenschaftenNode node = (EigenschaftenNode) eigenschaftenTree
    // .getSelection();
    // if (node.getNodeType() == EigenschaftenNode.EIGENSCHAFTEN)
    // {
    // if (node.getEigenschaften() == null)
    // {
    // try
    // {
    // Eigenschaften ei = (Eigenschaften) Einstellungen.getDBService()
    // .createObject(Eigenschaften.class, null);
    // {
    // ei.setMitglied(node.getMitglied().getID());
    // ei.setEigenschaft(node.getEigenschaft().getID());
    // ei.store();
    // }
    // }
    // catch (Exception e)
    // {
    // String error = "Eigenschaft kann nicht gespeichert werden";
    // Logger.error(error, e);
    // }
    // }
    // else
    // {
    // try
    // {
    // node.getEigenschaften().delete();
    // }
    // catch (Exception e)
    // {
    // String error = "Eigenschaft kann nicht gelˆscht werden";
    // Logger.error(error, e);
    // }
    // }
    // }
    // }
    // }
    // });

    eigenschaftenTree.setFormatter(new EigenschaftTreeFormatter());
    return eigenschaftenTree;
  }

  public void handleStore()
  {
    try
    {
      Mitglied m = getMitglied();
      m.setAdressierungszusatz((String) getAdressierungszusatz().getValue());
      m.setAustritt((Date) getAustritt().getValue());
      m.setAnrede((String) getAnrede().getValue());
      GenericObject o = (GenericObject) getBeitragsgruppe().getValue();
      try
      {
        m.setBeitragsgruppe(new Integer(o.getID()));
      }
      catch (NullPointerException e)
      {
        throw new ApplicationException("Beitragsgruppe fehlt");
      }
      Zahlungsweg zw = (Zahlungsweg) getZahlungsweg().getValue();
      m.setZahlungsweg(zw.getKey());
      Zahlungsrhytmus zr = (Zahlungsrhytmus) getZahlungsrhytmus().getValue();
      m.setZahlungsrhytmus(zr.getKey());
      m.setBlz((String) getBlz().getValue());
      m.setEintritt((Date) getEintritt().getValue());
      m.setEmail((String) getEmail().getValue());
      if (Einstellungen.getEinstellung().getExterneMitgliedsnummer())
      {
        if (externemitgliedsnummer != null)
        {
          m.setExterneMitgliedsnummer((Integer) getExterneMitgliedsnummer()
              .getValue());
        }
      }
      else
      {
        m.setExterneMitgliedsnummer(null);
      }

      if (m.getPersonenart().equals("n"))
      {
        m.setGeburtsdatum((Date) getGeburtsdatum().getValue());
        m.setGeschlecht((String) getGeschlecht().getValue());
      }
      m.setKonto((String) getKonto().getValue());
      m.setKontoinhaber((String) getKontoinhaber().getValue());
      m.setKuendigung((Date) getKuendigung().getValue());
      m.setName((String) getName(false).getText());
      m.setOrt((String) getOrt().getValue());
      m.setPlz((String) getPlz().getValue());
      m.setStrasse((String) getStrasse().getText());
      m.setTelefondienstlich((String) getTelefondienstlich().getValue());
      m.setTelefonprivat((String) getTelefonprivat().getValue());
      m.setHandy((String) getHandy().getValue());
      m.setTitel((String) getTitel().getValue());
      m.setVermerk1((String) getVermerk1().getValue());
      m.setVermerk2((String) getVermerk2().getValue());
      m.setVorname((String) getVorname().getText());
      if (m.getID() == null)
      {
        m.setEingabedatum();
      }
      m.store();

      if (eigenschaftenTree != null)
      {
        if (!getMitglied().isNewObject())
        {
          DBIterator it = Einstellungen.getDBService().createList(
              Eigenschaften.class);
          it.addFilter("mitglied = ?", new Object[] { getMitglied().getID() });
          while (it.hasNext())
          {
            Eigenschaften ei = (Eigenschaften) it.next();
            ei.delete();
          }
        }
        for (Object o1 : eigenschaftenTree.getItems())
        {
          if (o1 instanceof EigenschaftenNode)
          {
            EigenschaftenNode node = (EigenschaftenNode) o1;
            if (node.getNodeType() == EigenschaftenNode.EIGENSCHAFTEN)
            {
              Eigenschaften eig = (Eigenschaften) Einstellungen.getDBService()
                  .createObject(Eigenschaften.class, null);
              eig.setEigenschaft(node.getEigenschaft().getID());
              eig.setMitglied(getMitglied().getID());
              eig.store();
            }
          }
        }
      }

      if (zusatzfelder != null)
      {
        for (Input ti : zusatzfelder)
        {
          // Felddefinition ermitteln
          DBIterator it0 = Einstellungen.getDBService().createList(
              Felddefinition.class);
          it0.addFilter("label = ?", new Object[] { ti.getName() });
          Felddefinition fd = (Felddefinition) it0.next();
          // Ist bereits ein Datensatz f¸r diese Definiton vorhanden ?
          DBIterator it = Einstellungen.getDBService().createList(
              Zusatzfelder.class);
          it.addFilter("mitglied =?", new Object[] { m.getID() });
          it.addFilter("felddefinition=?", new Object[] { fd.getID() });
          Zusatzfelder zf = null;
          if (it.size() > 0)
          {
            zf = (Zusatzfelder) it.next();
          }
          else
          {
            zf = (Zusatzfelder) Einstellungen.getDBService().createObject(
                Zusatzfelder.class, null);
          }
          zf.setMitglied(new Integer(m.getID()));
          zf.setFelddefinition(new Integer(fd.getID()));
          switch (fd.getDatentyp())
          {
            case Datentyp.ZEICHENFOLGE:
              zf.setFeld((String) ti.getValue());
              break;
            case Datentyp.DATUM:
              zf.setFeldDatum((Date) ti.getValue());
              break;
            case Datentyp.GANZZAHL:
              zf.setFeldGanzzahl((Integer) ti.getValue());
              break;
            case Datentyp.WAEHRUNG:
              if (ti.getValue() != null)
              {
                zf.setFeldWaehrung(new BigDecimal((Double) ti.getValue()));
              }
              else
              {
                zf.setFeldWaehrung(null);
              }
              break;
            case Datentyp.JANEIN:
              zf.setFeldJaNein((Boolean) ti.getValue());
              break;
            default:
              zf.setFeld((String) ti.getValue());
              break;
          }
          zf.store();
        }
      }
      GUI.getStatusBar().setSuccessText("Mitglied gespeichert");
    }
    catch (ApplicationException e)
    {
      GUI.getStatusBar().setErrorText(e.getMessage());
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei Speichern des Mitgliedes";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  public TreePart getEigenschaftenAuswahlTree(String vorbelegung)
      throws RemoteException
  {
    eigenschaftenAuswahlTree = new TreePart(new EigenschaftenNode(vorbelegung),
        null);
    eigenschaftenAuswahlTree.setCheckable(true);
    eigenschaftenAuswahlTree.addSelectionListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        // "o" ist das Objekt, welches gerade markiert
        // wurde oder die Checkbox geaendert wurde.
        GenericObjectNode o = (GenericObjectNode) event.data;

        // Da der Listener sowohl dann aufgerufen wird,
        // nur nur eine Zeile selektiert wurde als auch,
        // wenn die Checkbox geaendert wurde, musst du jetzt
        // noch ersteres ausfiltern - die Checkboxen sollen
        // ja nicht geaendert werden, wenn nur eine Zeile
        // selektiert aber die Checkbox nicht geaendert wurde.
        // Hierzu schreibe ich in event.detail einen Int-Wert.
        // event.detail = -1 // Nur selektiert
        // event.detail = 1 // Checkbox aktiviert
        // event.detail = 0 // Checkbox deaktiviert

        if (event.detail == -1)
        {
          return;
        }
        try
        {
          if (o.getChildren() == null)
          {
            return;
          }
          List children = PseudoIterator.asList(o.getChildren());
          boolean b = event.detail > 0;
          eigenschaftenAuswahlTree.setChecked(children
              .toArray(new Object[children.size()]), b);
        }
        catch (RemoteException e)
        {
          e.printStackTrace();
        }
      }
    });

    eigenschaftenAuswahlTree.setFormatter(new EigenschaftTreeFormatter());
    return eigenschaftenAuswahlTree;
  }

  @SuppressWarnings("unchecked")
  private void starteAuswertung() throws RemoteException
  {
    saveDefaults();
    ArrayList list = null;
    list = new MitgliedQuery(this, false).get();
    try
    {
      String subtitle = "";
      if (geburtsdatumvon.getValue() != null)
      {
        Date d = (Date) geburtsdatumvon.getValue();
        subtitle += "Geburtsdatum von " + Einstellungen.DATEFORMAT.format(d)
            + "  ";
      }
      if (geburtsdatumbis.getValue() != null)
      {
        Date d = (Date) geburtsdatumbis.getValue();
        subtitle += "Geburtsdatum bis " + Einstellungen.DATEFORMAT.format(d)
            + "  ";
      }
      if (eintrittvon.getValue() != null)
      {
        Date d = (Date) eintrittvon.getValue();
        subtitle += "Eintritt von " + Einstellungen.DATEFORMAT.format(d) + "  ";
      }
      if (eintrittbis.getValue() != null)
      {
        Date d = (Date) eintrittbis.getValue();
        subtitle += "Eintritt bis " + Einstellungen.DATEFORMAT.format(d) + "  ";
      }
      if (austrittvon.getValue() != null)
      {
        Date d = (Date) austrittvon.getValue();
        subtitle += "Austritt von " + Einstellungen.DATEFORMAT.format(d) + "  ";
      }
      if (austrittbis.getValue() != null)
      {
        Date d = (Date) austrittbis.getValue();
        subtitle += "Austritt bis " + Einstellungen.DATEFORMAT.format(d) + "  ";
      }
      if (austrittvon.getValue() == null && austrittbis.getValue() == null)
      {
        subtitle += "nur Angemeldete, keine Ausgetretenen (nur lfd. Jahr)  ";
      }
      if (beitragsgruppeausw.getValue() != null)
      {
        Beitragsgruppe bg = (Beitragsgruppe) beitragsgruppeausw.getValue();
        subtitle += "nur Beitragsgruppe " + bg.getBezeichnung();
      }

      String sort = (String) sortierung.getValue();
      String dateinamensort = "";
      if (sort.equals("Name, Vorname"))
      {
        dateinamensort = "name";
      }
      else if (sort.equals("Eintrittsdatum"))
      {
        dateinamensort = "eintrittsdatum";
      }
      else if (sort.equals("Geburtsdatum"))
      {
        dateinamensort = "geburtsdatum";
      }
      else if (sort.equals("Geburtstagsliste"))
      {
        dateinamensort = "geburtstagsliste";
      }

      FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
      fd.setText("Ausgabedatei w‰hlen.");

      String path = settings.getString("lastdir", System
          .getProperty("user.home"));
      if (path != null && path.length() > 0)
      {
        fd.setFilterPath(path);
      }
      String ausgformat = (String) ausgabe.getValue();
      fd.setFileName(new Dateiname("auswertung", dateinamensort, Einstellungen
          .getEinstellung().getDateinamenmuster(), ausgformat).get());
      fd.setFilterExtensions(new String[] { "*." + ausgformat });

      String s = fd.open();
      if (s == null || s.length() == 0)
      {
        return;
      }
      if (!s.endsWith(ausgformat))
      {
        s = s + "." + ausgformat;
      }
      final File file = new File(s);
      if (ausgformat.equals("PDF"))
      {
        auswertungMitgliedPDF(list, file, subtitle);
      }
      if (ausgformat.equals("CSV"))
      {
        auswertungMitgliedCSV(list, file);
      }
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    }
  }

  private void starteStatistik() throws RemoteException
  {
    FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
    fd.setText("Ausgabedatei w‰hlen.");
    fd.setFilterExtensions(new String[] { "*.PDF" });
    Settings settings = new Settings(this.getClass());

    String path = settings
        .getString("lastdir", System.getProperty("user.home"));
    if (path != null && path.length() > 0)
    {
      fd.setFilterPath(path);
    }
    fd.setFileName(new Dateiname("statistik", Einstellungen.getEinstellung()
        .getDateinamenmuster(), "PDF").get());

    String s = fd.open();

    if (s == null || s.length() == 0)
    {
      return;
    }
    if (!s.toUpperCase().endsWith("PDF"))
    {
      s = s + ".PDF";
    }

    final File file = new File(s);
    final Date sticht = (Date) stichtag.getValue();

    BackgroundTask t = new BackgroundTask()
    {
      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        try
        {
          new MitgliederStatistik(file, monitor, sticht);
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

      public void interrupt()
      {
      }

      public boolean isInterrupted()
      {
        return false;
      }
    };
    Application.getController().start(t);

  }

  private void starteJubilaeenListe() throws RemoteException
  {
    FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
    fd.setText("Ausgabedatei w‰hlen.");
    fd.setFilterExtensions(new String[] { "*.PDF" });
    Settings settings = new Settings(this.getClass());
    String path = settings
        .getString("lastdir", System.getProperty("user.home"));
    if (path != null && path.length() > 0)
    {
      fd.setFilterPath(path);
    }
    fd.setFileName(new Dateiname((String) getJubelArt().getValue(),
        Einstellungen.getEinstellung().getDateinamenmuster(), "PDF").get());
    String s = fd.open();

    if (s == null || s.length() == 0)
    {
      return;
    }
    if (!s.toUpperCase().endsWith("PDF"))
    {
      s = s + ".PDF";
    }

    final File file = new File(s);
    settings.setAttribute("lastdir", file.getParent());
    final Integer jahr = (Integer) jubeljahr.getValue();
    final String art = (String) jubelart.getValue();

    BackgroundTask t = new BackgroundTask()
    {
      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        try
        {
          new Jubilaeenliste(file, monitor, jahr, art);
        }
        catch (RemoteException e)
        {
          e.printStackTrace();
        }
      }

      public void interrupt()
      {
      }

      public boolean isInterrupted()
      {
        return false;
      }
    };
    Application.getController().start(t);

  }

  private void auswertungMitgliedPDF(final ArrayList<Mitglied> list,
      final File file, final String subtitle)
  {
    BackgroundTask t = new BackgroundTask()
    {
      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        try
        {
          new MitgliedAuswertungPDF(list, file, monitor, subtitle);
          monitor.setPercentComplete(100);
          monitor.setStatus(ProgressMonitor.STATUS_DONE);
          GUI.getStatusBar().setSuccessText("Auswertung gestartet");
          GUI.getCurrentView().reload();
        }
        catch (ApplicationException ae)
        {
          Logger.error("", ae);
          monitor.setStatusText(ae.getMessage());
          monitor.setStatus(ProgressMonitor.STATUS_ERROR);
          GUI.getStatusBar().setErrorText(ae.getMessage());
          throw ae;
        }
        catch (Exception re)
        {
          Logger.error("", re);
          monitor.setStatusText(re.getMessage());
          monitor.setStatus(ProgressMonitor.STATUS_ERROR);
          GUI.getStatusBar().setErrorText(re.getMessage());
          throw new ApplicationException(re);
        }
      }

      public void interrupt()
      {
      }

      public boolean isInterrupted()
      {
        return false;
      }
    };
    Application.getController().start(t);
  }

  private void auswertungMitgliedCSV(final ArrayList<Mitglied> list,
      final File file)
  {
    BackgroundTask t = new BackgroundTask()
    {
      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        try
        {
          new MitgliedAuswertungCSV(list, file, monitor);
          monitor.setPercentComplete(100);
          monitor.setStatus(ProgressMonitor.STATUS_DONE);
          GUI.getStatusBar().setSuccessText("Auswertung gestartet");
          GUI.getCurrentView().reload();
        }
        catch (ApplicationException ae)
        {
          monitor.setStatusText(ae.getMessage());
          monitor.setStatus(ProgressMonitor.STATUS_ERROR);
          GUI.getStatusBar().setErrorText(ae.getMessage());
          throw ae;
        }
        catch (RemoteException re)
        {
          monitor.setStatusText(re.getMessage());
          monitor.setStatus(ProgressMonitor.STATUS_ERROR);
          GUI.getStatusBar().setErrorText(re.getMessage());
          throw new ApplicationException(re);
        }
      }

      public void interrupt()
      {
      }

      public boolean isInterrupted()
      {
        return false;
      }
    };
    Application.getController().start(t);
  }

  /**
   * Sucht das Geldinstitut zur eingegebenen BLZ und zeigt es als Kommentar
   * hinter dem BLZ-Feld an.
   */
  private class BLZListener implements Listener
  {
    public void handleEvent(Event event)
    {
      try
      {
        String blz = (String) getBlz().getValue();
        getBlz().setComment(Einstellungen.getNameForBLZ(blz));
      }
      catch (RemoteException e)
      {
        Logger.error("error while updating blz comment", e);
      }
    }
  }

  /**
   * Listener, der die Auswahl der Eigenschaften ueberwacht.
   */
  private class EigenschaftenListener implements Listener
  {
    public void handleEvent(Event event)
    {
      if (event == null || event.data == null)
      {
        return;
      }
      ArrayList<Object> sel = (ArrayList<Object>) event.data;
      String id = "";
      String text = "";
      for (Object o : sel)
      {
        if (text.length() > 0)
        {
          id += ",";
          text += ", ";
        }
        EigenschaftenNode node = (EigenschaftenNode) o;
        try
        {
          id += node.getEigenschaft().getID();
          text += node.getEigenschaft().getBezeichnung();
        }
        catch (RemoteException e)
        {
          Logger.error("Fehler", e);
        }
      }
      eigenschaftenabfrage.setText(text);
      settings.setAttribute("mitglied.eigenschaften", id);
    }
  }

  public class EigenschaftTreeFormatter implements TreeFormatter
  {
    public void format(TreeItem item)
    {
      EigenschaftenNode eigenschaftitem = (EigenschaftenNode) item.getData();
      if (eigenschaftitem.getNodeType() == EigenschaftenNode.ROOT
          || eigenschaftitem.getNodeType() == EigenschaftenNode.EIGENSCHAFTGRUPPE)
      {
        //
      }
      else
      {
        if (eigenschaftitem.getEigenschaften() != null
            || eigenschaftitem.isPreset())
        {
          item.setChecked(true);
        }
        else
        {
          item.setChecked(false);
        }
      }
    }
  }

}
