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
 * Revision 1.55  2009/03/26 20:59:06  jost
 * Neu: Reports - Erste Version
 *
 * Revision 1.54  2009/03/07 14:19:43  jost
 * Bugfix: Beitragsgruppe gelöscht aber noch in den Default-Werten referenziert.
 *
 * Revision 1.53  2009/01/22 21:05:57  jost
 * Zusätzliches Jahr in die Vergangenheit.
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
 * Neu: MitgliedschaftsjubilÃ¤en
 *
 * Revision 1.39  2008/07/11 07:34:00  jost
 * Ausgabeverzeichnis fÃ¼r den nÃ¤chsten Aufruf merken.
 *
 * Revision 1.38  2008/07/10 09:20:24  jost
 * redaktionelle Ã„nderung
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
 * Felder Titel und PLZ verlÃ¤ngert.
 *
 * Revision 1.32  2008/03/17 20:22:12  jost
 * Bugfix Eintritts- und Austrittsdatum aus dem Bereich Auswertung wurden auch im Dialog berÃ¼cksichtigt.
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
 * Sortierung der Beitragsgruppen eingefÃ¼hrt
 * Bugfix Filter.
 *
 * Revision 1.27  2008/01/25 16:02:32  jost
 * Neu: Eigenschaften des Mitgliedes
 *
 * Revision 1.26  2008/01/01 13:13:56  jost
 * Neu: Dateinamenmuster
 *
 * Revision 1.25  2007/12/22 08:25:13  jost
 * Neu: JubilÃ¤enliste
 *
 * Revision 1.24  2007/12/21 11:27:46  jost
 * Mitgliederstatistik jetzt Stichtagsbezogen
 *
 * Revision 1.23  2007/12/16 20:25:21  jost
 * Mitgliederstatistik lÃ¤uft jetzt in einem eigenen Thread
 *
 * Revision 1.22  2007/12/02 13:39:47  jost
 * Neu: Beitragsmodelle
 *
 * Revision 1.21  2007/12/01 19:05:58  jost
 * Neu: Geburtstagsliste
 *
 * Revision 1.20  2007/12/01 10:05:49  jost
 * Ã„nderung wg. neuem Classloader in Jameica
 *
 * Revision 1.19  2007/09/16 17:52:04  jost
 * Selektion nach Mitgliedsstatus
 *
 * Revision 1.18  2007/09/06 17:15:46  jost
 * Mitgliederstatistik: *.PDF als Standardvorgabe im Datei-Speichern-Dialog
 *
 * Revision 1.17  2007/08/31 05:35:32  jost
 * Automatische ErgÃ¤nzung der Dateiendung.
 *
 * Revision 1.16  2007/08/30 19:48:29  jost
 * 1. Korrekte Darstellung von Pflichtfeldern
 * 2. Neues Kontext-MenÃ¼
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
 * Familienangehörige anzeigen
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
 * Vermerke eingefÃ¼hrt.
 *
 * Revision 1.5  2007/02/23 20:26:38  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.4  2006/12/20 20:25:44  jost
 * Patch von Ullrich Schäfer, der die Primitive vs. Object Problematik adressiert.
 *
 * Revision 1.3  2006/10/29 07:48:29  jost
 * Neu: Mitgliederstatistik
 *
 * Revision 1.2  2006/10/20 07:36:14  jost
 * Fehlermeldung ausgeben, wenn keine Beitragsgruppe ausgewählt wurde.
 *
 * Revision 1.1  2006/09/20 15:38:30  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.io.File;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.Queries.MitgliedQuery;
import de.jost_net.JVerein.gui.action.MitgliedDetailAction;
import de.jost_net.JVerein.gui.action.WiedervorlageAction;
import de.jost_net.JVerein.gui.action.ZusatzbetraegeAction;
import de.jost_net.JVerein.gui.dialogs.EigenschaftenAuswahlDialog;
import de.jost_net.JVerein.gui.menu.MitgliedMenu;
import de.jost_net.JVerein.gui.menu.WiedervorlageMenu;
import de.jost_net.JVerein.gui.menu.ZusatzbetraegeMenu;
import de.jost_net.JVerein.gui.parts.Familienverband;
import de.jost_net.JVerein.io.Jubilaeenliste;
import de.jost_net.JVerein.io.MitgliedAuswertungCSV;
import de.jost_net.JVerein.io.MitgliedAuswertungPDF;
import de.jost_net.JVerein.io.MitgliederStatistik;
import de.jost_net.JVerein.keys.ArtBeitragsart;
import de.jost_net.JVerein.keys.Zahlungsrhytmus;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Felddefinition;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Wiedervorlage;
import de.jost_net.JVerein.rmi.Zusatzbetrag;
import de.jost_net.JVerein.rmi.Zusatzfelder;
import de.jost_net.JVerein.util.Dateiname;
import de.jost_net.JVerein.util.MitgliedSpaltenauswahl;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ObjectNotFoundException;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.DialogInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.IntegerInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextAreaInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.TablePart;
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

  private Input name;

  private Input vorname;

  private Input adressierungszusatz;

  private Input strasse;

  private Input plz;

  private Input ort;

  private DateInput geburtsdatum = null;

  private SelectInput geschlecht;

  private SelectInput zahlungsweg;

  private SelectInput zahlungsrhytmus;

  private Input blz;

  private Input konto;

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

  private TextInput[] zusatzfelder;

  // Elemente für die Auswertung

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

  public final static String JUBELART_MITGLIEDSCHAFT = "Mitgliedschaftsjubiläen";

  public final static String JUBELART_ALTER = "Altersjubiläen";

  private SelectInput beitragsgruppeausw;

  private DialogInput eigenschaftenabfrage;

  private IntegerInput suchexternemitgliedsnummer;

  private Mitglied mitglied;

  // Liste aller Zusatzbeträge
  private TablePart zusatzbetraegeList;

  // Liste der Wiedervorlagen
  private TablePart wiedervorlageList;

  private TablePart familienangehoerige;

  private Settings settings = null;

  public MitgliedControl(AbstractView view)
  {
    super(view);
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  private Mitglied getMitglied()
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
    return externemitgliedsnummer;
  }

  public Input getAnrede() throws RemoteException
  {
    if (anrede != null)
    {
      return anrede;
    }
    anrede = new TextInput(getMitglied().getAnrede(), 10);
    return anrede;
  }

  public Input getTitel() throws RemoteException
  {
    if (titel != null)
    {
      return titel;
    }
    titel = new TextInput(getMitglied().getTitel(), 20);
    return titel;
  }

  public Input getName() throws RemoteException
  {
    if (name != null)
    {
      return name;
    }
    name = new TextInput(getMitglied().getName(), 40);
    name.setMandatory(true);
    return name;
  }

  public Input getVorname() throws RemoteException
  {
    if (vorname != null)
    {
      return vorname;
    }
    vorname = new TextInput(getMitglied().getVorname(), 40);
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
    return adressierungszusatz;
  }

  public Input getStrasse() throws RemoteException
  {
    if (strasse != null)
    {
      return strasse;
    }
    strasse = new TextInput(getMitglied().getStrasse(), 40);
    return strasse;
  }

  public Input getPlz() throws RemoteException
  {
    if (plz != null)
    {
      return plz;
    }
    plz = new TextInput(getMitglied().getPlz(), 10);
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
    this.geburtsdatum.setTitle("Geburtsdatum");
    this.geburtsdatum.setText("Bitte Geburtsdatum wählen");
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

  public SelectInput getGeschlecht() throws RemoteException
  {
    if (geschlecht != null)
    {
      return geschlecht;
    }
    geschlecht = new SelectInput(new String[] { "m", "w" }, getMitglied()
        .getGeschlecht());
    geschlecht.setPleaseChoose("Bitte auswählen");
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
          Zahlungsweg.ABBUCHUNG));
    }
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
          new Zahlungsrhytmus(Zahlungsrhytmus.JAEHRLICH));
    }
    return zahlungsrhytmus;
  }

  public Input getBlz() throws RemoteException
  {
    if (blz != null)
    {
      return blz;
    }
    blz = new TextInput(getMitglied().getBlz(), 8);
    blz.setMandatory(getMitglied().getZahlungsweg() == null
        || getMitglied().getZahlungsweg().intValue() == Zahlungsweg.ABBUCHUNG);
    BLZListener l = new BLZListener();
    blz.addListener(l);
    l.handleEvent(null); // Einmal initial ausfuehren
    return blz;
  }

  public Input getKonto() throws RemoteException
  {
    if (konto != null)
    {
      return konto;
    }
    konto = new TextInput(getMitglied().getKonto(), 10);
    konto.setMandatory(getMitglied().getZahlungsweg() == null
        || getMitglied().getZahlungsweg().intValue() == Zahlungsweg.ABBUCHUNG);
    return konto;
  }

  public Input getKontoinhaber() throws RemoteException
  {
    if (kontoinhaber != null)
    {
      return kontoinhaber;
    }
    kontoinhaber = new TextInput(getMitglied().getKontoinhaber(), 27);
    return kontoinhaber;
  }

  public Input getTelefonprivat() throws RemoteException
  {
    if (telefonprivat != null)
    {
      return telefonprivat;
    }
    telefonprivat = new TextInput(getMitglied().getTelefonprivat(), 20);
    return telefonprivat;
  }

  public Input getTelefondienstlich() throws RemoteException
  {
    if (telefondienstlich != null)
    {
      return telefondienstlich;
    }
    telefondienstlich = new TextInput(getMitglied().getTelefondienstlich(), 20);
    return telefondienstlich;
  }

  public Input getHandy() throws RemoteException
  {
    if (handy != null)
    {
      return handy;
    }
    handy = new TextInput(getMitglied().getHandy(), 20);
    return handy;
  }

  public Input getEmail() throws RemoteException
  {
    if (email != null)
    {
      return email;
    }
    email = new TextInput(getMitglied().getEmail(), 50);
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
    this.eintritt.setText("Bitte Eintrittsdatum wählen");
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
    beitragsgruppe.setValue(getMitglied().getBeitragsgruppe());
    beitragsgruppe.setMandatory(true);
    beitragsgruppe.setAttribute("bezeichnung");
    beitragsgruppe.setPleaseChoose("Bitte auswählen");
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
    beitragsgruppeausw.setAttribute("bezeichnung");
    beitragsgruppeausw.setPleaseChoose("Bitte auswählen");
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

    // Beitragsgruppen ermitteln, die Zahler für andere Mitglieder sind
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
    zhl.addFilter("austritt is null");
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
    zahler.setPleaseChoose("Bitte auswählen");
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

    if (getMitglied().getBeitragsgruppe() != null
        && getMitglied().getBeitragsgruppe().getBeitragsArt() == ArtBeitragsart.FAMILIE_ANGEHOERIGER)
    {
      zahler.setEnabled(true);
    }
    else
    {
      zahler.setEnabled(false);
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
    this.austritt.setText("Bitte Austrittsdatum wählen");
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
    this.kuendigung.setTitle("Kündigungsdatum");
    this.kuendigung.setText("Bitte Kündigungsdatum wählen");
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
    return vermerk1;
  }

  public TextAreaInput getVermerk2() throws RemoteException
  {
    if (vermerk2 != null)
    {
      return vermerk2;
    }
    vermerk2 = new TextAreaInput(getMitglied().getVermerk2(), 255);
    return vermerk2;
  }

  public TextInput[] getZusatzfelder() throws RemoteException
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
    zusatzfelder = new TextInput[anzahl];
    for (int i = 0; i < anzahl; i++)
    {
      Felddefinition fd = (Felddefinition) it.next();
      zusatzfelder[i] = new TextInput("", fd.getLaenge());
      zusatzfelder[i].setName(fd.getLabel());
      if (fd.getLabel() == null)

      {
        zusatzfelder[i].setName(fd.getName());
      }
      if (getMitglied().getID() != null)
      {
        DBIterator it2 = Einstellungen.getDBService().createList(
            Zusatzfelder.class);
        it2.addFilter("mitglied=?", new Object[] { getMitglied().getID() });
        it2.addFilter("felddefinition=?", new Object[] { fd.getID() });
        if (it2.size() > 0)
        {
          Zusatzfelder zf = (Zusatzfelder) it2.next();
          zusatzfelder[i].setValue(zf.getFeld());
        }
      }
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
    zusatzbetraegeList.addColumn("nächste Fälligkeit", "faelligkeit",
        new DateFormatter(Einstellungen.DATEFORMAT));
    zusatzbetraegeList.addColumn("letzte Ausführung", "ausfuehrung",
        new DateFormatter(Einstellungen.DATEFORMAT));
    zusatzbetraegeList.addColumn("Intervall", "intervalltext");
    zusatzbetraegeList.addColumn("Endedatum", "endedatum", new DateFormatter(
        Einstellungen.DATEFORMAT));
    zusatzbetraegeList.addColumn("Buchungstext", "buchungstext");
    zusatzbetraegeList.addColumn("Betrag", "betrag", new CurrencyFormatter("",
        Einstellungen.DECIMALFORMAT));
    zusatzbetraegeList.addColumn("aktiv", "aktiv");
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
    if (eigenschaftenabfrage != null)
    {
      return eigenschaftenabfrage;
    }
    EigenschaftenAuswahlDialog d = new EigenschaftenAuswahlDialog();
    d.addCloseListener(new EigenschaftenListener());
    String tmp = settings.getString("mitglied.eigenschaften", "");
    eigenschaftenabfrage = new DialogInput(tmp, d);

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
    Button b = new Button("Start", new Action()
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
    Button b = new Button("Start", new Action()
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
    Button b = new Button("Start", new Action()
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

  public TablePart getMitgliedTable(String anfangsbuchstabe)
      throws RemoteException
  {
    TablePart part;
    saveDefaults();
    part = new TablePart(new MitgliedQuery(this, true).get(anfangsbuchstabe),
        new MitgliedDetailAction());
    new MitgliedSpaltenauswahl().setColumns(part);
    part.setContextMenu(new MitgliedMenu());
    part.setRememberColWidths(true);
    part.setRememberOrder(true);
    return part;
  }

  /**
   * Default-Werte für die MitgliederSuchView speichern.
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

    if (eigenschaftenabfrage != null)
    {
      settings.setAttribute("mitglied.eigenschaften", getEigenschaftenAuswahl()
          .getText());
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

      m.setGeburtsdatum((Date) getGeburtsdatum().getValue());
      m.setGeschlecht((String) getGeschlecht().getValue());
      m.setKonto((String) getKonto().getValue());
      m.setKontoinhaber((String) getKontoinhaber().getValue());
      m.setKuendigung((Date) getKuendigung().getValue());
      m.setName((String) getName().getValue());
      m.setOrt((String) getOrt().getValue());
      m.setPlz((String) getPlz().getValue());
      m.setStrasse((String) getStrasse().getValue());
      m.setTelefondienstlich((String) getTelefondienstlich().getValue());
      m.setTelefonprivat((String) getTelefonprivat().getValue());
      m.setHandy((String) getHandy().getValue());
      m.setTitel((String) getTitel().getValue());
      m.setVermerk1((String) getVermerk1().getValue());
      m.setVermerk2((String) getVermerk2().getValue());
      m.setVorname((String) getVorname().getValue());
      if (m.getID() == null)
      {
        m.setEingabedatum();
      }
      m.store();

      if (zusatzfelder != null)
      {
        for (TextInput ti : zusatzfelder)
        {
          // Felddefinition ermitteln
          DBIterator it0 = Einstellungen.getDBService().createList(
              Felddefinition.class);
          it0.addFilter("label = ?", new Object[] { ti.getName() });
          Felddefinition fd = (Felddefinition) it0.next();
          // Ist bereits ein Datensatz für diese Definiton vorhanden ?
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
          zf.setFeld((String) ti.getValue());
          zf.store();
        }
      }
      GUI.getStatusBar().setSuccessText("Mitglied gespeichert");
    }
    catch (ApplicationException e)
    {
      GUI.getView().setErrorText(e.getMessage());
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei Speichern des Mitgliedes";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
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
        subtitle += "nur Angemeldete, keine Ausgetretenen  ";
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
      fd.setText("Ausgabedatei wählen.");

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
    fd.setText("Ausgabedatei wählen.");
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
    fd.setText("Ausgabedatei wählen.");
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
          monitor.setStatusText(ae.getMessage());
          monitor.setStatus(ProgressMonitor.STATUS_ERROR);
          GUI.getStatusBar().setErrorText(ae.getMessage());
          throw ae;
        }
        catch (Exception re)
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
      String selection = (String) event.data;
      eigenschaftenabfrage.setText(selection);
    }
  }

}
