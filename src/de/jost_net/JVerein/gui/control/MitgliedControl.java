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
package de.jost_net.JVerein.gui.control;

import java.io.File;
import java.io.FilenameFilter;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.Messaging.FamilienbeitragMessage;
import de.jost_net.JVerein.Queries.MitgliedQuery;
import de.jost_net.JVerein.Variable.MitgliedMap;
import de.jost_net.JVerein.gui.action.ArbeitseinsatzAction;
import de.jost_net.JVerein.gui.action.LehrgangAction;
import de.jost_net.JVerein.gui.action.LesefelddefinitionenAction;
import de.jost_net.JVerein.gui.action.MailDetailAction;
import de.jost_net.JVerein.gui.action.MitgliedDetailAction;
import de.jost_net.JVerein.gui.action.MitgliedNextBGruppeBearbeitenAction;
import de.jost_net.JVerein.gui.action.WiedervorlageAction;
import de.jost_net.JVerein.gui.action.ZusatzbetraegeAction;
import de.jost_net.JVerein.gui.dialogs.EigenschaftenAuswahlDialog;
import de.jost_net.JVerein.gui.dialogs.EigenschaftenAuswahlParameter;
import de.jost_net.JVerein.gui.dialogs.ZusatzfelderAuswahlDialog;
import de.jost_net.JVerein.gui.input.BICInput;
import de.jost_net.JVerein.gui.input.EmailInput;
import de.jost_net.JVerein.gui.input.GeschlechtInput;
import de.jost_net.JVerein.gui.input.IBANInput;
import de.jost_net.JVerein.gui.input.MailAuswertungInput;
import de.jost_net.JVerein.gui.input.PersonenartInput;
import de.jost_net.JVerein.gui.menu.ArbeitseinsatzMenu;
import de.jost_net.JVerein.gui.menu.FamilienbeitragMenu;
import de.jost_net.JVerein.gui.menu.LehrgangMenu;
import de.jost_net.JVerein.gui.menu.MitgliedMailMenu;
import de.jost_net.JVerein.gui.menu.MitgliedMenu;
import de.jost_net.JVerein.gui.menu.MitgliedNextBGruppeMenue;
import de.jost_net.JVerein.gui.menu.WiedervorlageMenu;
import de.jost_net.JVerein.gui.menu.ZusatzbetraegeMenu;
import de.jost_net.JVerein.gui.parts.Familienverband;
import de.jost_net.JVerein.gui.parts.MitgliedNextBGruppePart;
import de.jost_net.JVerein.gui.parts.MitgliedSekundaereBeitragsgruppePart;
import de.jost_net.JVerein.gui.view.AbstractAdresseDetailView;
import de.jost_net.JVerein.gui.view.AuswertungVorlagenCsvView;
import de.jost_net.JVerein.gui.view.IAuswertung;
import de.jost_net.JVerein.gui.view.MitgliederSuchProfilView;
import de.jost_net.JVerein.io.FileViewer;
import de.jost_net.JVerein.io.MitgliedAdressbuchExport;
import de.jost_net.JVerein.io.MitgliedAuswertungCSV;
import de.jost_net.JVerein.io.MitgliedAuswertungPDF;
import de.jost_net.JVerein.io.MitgliederStatistik;
import de.jost_net.JVerein.keys.ArtBeitragsart;
import de.jost_net.JVerein.keys.Datentyp;
import de.jost_net.JVerein.keys.Zahlungsrhythmus;
import de.jost_net.JVerein.keys.Zahlungstermin;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Adresstyp;
import de.jost_net.JVerein.rmi.Arbeitseinsatz;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Eigenschaft;
import de.jost_net.JVerein.rmi.EigenschaftGruppe;
import de.jost_net.JVerein.rmi.Eigenschaften;
import de.jost_net.JVerein.rmi.Felddefinition;
import de.jost_net.JVerein.rmi.Lehrgang;
import de.jost_net.JVerein.rmi.Mail;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.MitgliedNextBGruppe;
import de.jost_net.JVerein.rmi.Mitgliedfoto;
import de.jost_net.JVerein.rmi.SekundaereBeitragsgruppe;
import de.jost_net.JVerein.rmi.Wiedervorlage;
import de.jost_net.JVerein.rmi.Zusatzbetrag;
import de.jost_net.JVerein.rmi.Zusatzfelder;
import de.jost_net.JVerein.server.EigenschaftenNode;
import de.jost_net.JVerein.server.MitgliedUtils;
import de.jost_net.JVerein.util.Dateiname;
import de.jost_net.JVerein.util.Datum;
import de.jost_net.JVerein.util.JVDateFormatTIMESTAMP;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.jost_net.JVerein.util.LesefeldAuswerter;
import de.jost_net.JVerein.util.MitgliedSpaltenauswahl;
import de.jost_net.OBanToo.SEPA.Basislastschrift.MandatSequence;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.GenericObjectNode;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ObjectNotFoundException;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.formatter.Formatter;
import de.willuhn.jameica.gui.formatter.TreeFormatter;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.DecimalInput;
import de.willuhn.jameica.gui.input.DialogInput;
import de.willuhn.jameica.gui.input.FileInput;
import de.willuhn.jameica.gui.input.ImageInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.IntegerInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.SpinnerInput;
import de.willuhn.jameica.gui.input.TextAreaInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.gui.parts.TreePart;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.messaging.Message;
import de.willuhn.jameica.messaging.MessageConsumer;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class MitgliedControl extends AbstractControl
{

  private TablePart part;

  private SelectInput adresstyp;

  private TextInput externemitgliedsnummer;

  private TextInput mitgliedsnummer;

  private Input anrede;

  private Input titel;

  private TextInput name;

  private TextInput vorname;

  private Input adressierungszusatz;

  private TextInput strasse;

  private Input plz;

  private Input ort;

  private Input staat;

  private DateInput geburtsdatum = null;

  private GeschlechtInput geschlecht;

  private SelectInput zahlungsweg;

  private LabelGroup bankverbindungLabelGroup;

  private SelectInput zahlungsrhytmus;

  private SelectInput zahlungstermin;

  private TextInput mandatid = null;

  private DateInput mandatdatum = null;

  private SpinnerInput mandatversion = null;

  private SelectInput mandatsequence = null;

  private DateInput letztelastschrift = null;

  private TextInput bic;

  private TextInput iban;

  private PersonenartInput ktoipersonenart;

  private TextInput ktoianrede;

  private TextInput ktoititel;

  private TextInput ktoiname;

  private TextInput ktoivorname;

  private TextInput ktoistrasse;

  private TextInput ktoiadressierungszusatz;

  private TextInput ktoiplz;

  private TextInput ktoiort;

  private TextInput ktoistaat;

  private EmailInput ktoiemail;

  private GeschlechtInput ktoigeschlecht;

  private Input telefonprivat;

  private Input telefondienstlich;

  private Input handy;

  private EmailInput email;

  private DateInput eintritt = null;

  private SelectInput beitragsgruppe;

  private TreePart sekundaerebeitragsgruppe;

  private DecimalInput individuellerbeitrag;

  private Familienverband famverb;

  private MitgliedSekundaereBeitragsgruppePart mitgliedSekundaereBeitragsgruppeView;

  private MitgliedNextBGruppePart zukueftigeBeitraegeView;

  private TreePart familienbeitragtree;

  private SelectInput zahler;

  private DateInput austritt = null;

  private DateInput kuendigung = null;

  private DateInput sterbetag = null;

  private Input[] zusatzfelder;

  private ZusatzfelderAuswahlDialog zad;

  private Input[] lesefelder;

  private TreePart eigenschaftenTree;

  private TreePart eigenschaftenAuswahlTree;

  // Elemente für die Auswertung
  private TextInput auswertungUeberschrift = null;

  private SelectInput suchadresstyp = null;

  private TextInput suchname = null;

  private DateInput geburtsdatumvon = null;

  private DateInput geburtsdatumbis = null;

  private DateInput sterbedatumvon = null;

  private DateInput sterbedatumbis = null;

  private DateInput eintrittvon = null;

  private DateInput eintrittbis;

  private DateInput austrittvon;

  private DateInput austrittbis;

  private TextAreaInput vermerk1;

  private TextAreaInput vermerk2;

  private SelectInput ausgabe;

  private FileInput vorlagedateicsv; // RWU

  private SelectInput sortierung;

  private SelectInput status;

  private DateInput stichtag;

  private SelectInput jubeljahr;

  private SelectInput beitragsgruppeausw;

  private DialogInput eigenschaftenabfrage;

  private DialogInput zusatzfelderabfrage;

  private TextInput suchexternemitgliedsnummer;

  private SelectInput mailAuswahl;

  private Mitglied mitglied;

  private FamilienbeitragMessageConsumer fbc = null;

  // Liste aller Zusatzbeträge
  private TablePart zusatzbetraegeList;

  // Liste der Wiedervorlagen
  private TablePart wiedervorlageList;

  // Liste der Mails
  private TablePart mailList;

  // Liste der Arbeitseinsätze
  private TablePart arbeitseinsatzList;

  // Liste der Lehrgänge
  private TablePart lehrgaengeList;

  private TablePart familienangehoerige;

  private ImageInput foto;

  private Settings settings = null;

  private LesefeldAuswerter lesefeldAuswerter = null;

  private int jjahr = 0;

  private TablePart beitragsTabelle;

  private ArrayList<SekundaereBeitragsgruppe> listeSeB;

  // Zeitstempel merken, wann der Letzte refresh ausgeführt wurde.
  private long lastrefresh = 0;

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

  public void setMitglied(Mitglied mitglied)
  {
    this.mitglied = mitglied;
  }

  /**
   * 
   * @param typ
   *          1=Mitglieder 2= alle ohne Mitglieder
   * @throws RemoteException
   */
  public SelectInput getSuchAdresstyp(int typ) throws RemoteException
  {
    if (suchadresstyp != null)
    {
      return suchadresstyp;
    }
    if (typ != 1)
    {
      settings.setAttribute("mitglied.austrittvon", "");
      settings.setAttribute("mitglied.austrittbis", "");
      settings.setAttribute("mitglied.beitragsgruppe", "");
      settings.setAttribute("mitglied.eigenschaften", "");
      settings.setAttribute("mitglied.eintrittbis", "");
      settings.setAttribute("mitglied.eintrittvon", "");
      settings.setAttribute("mitglied.geburtsdatumbis", "");
      settings.setAttribute("mitglied.geburtsdatumvon", "");
      settings.setAttribute("mitglied.sterbedatumbis", "");
      settings.setAttribute("mitglied.sterbedatumvon", "");
      settings.setAttribute("mitglied.stichtag", "");
      settings.setAttribute("status.mitglied", "");
      settings.setAttribute("zusatzfelder.selected", 0);
    }
    DBIterator<Adresstyp> at = Einstellungen.getDBService()
        .createList(Adresstyp.class);
    switch (typ)
    {
      case 1:
        at.addFilter("jvereinid = 1");
        break;
      case 2:
        at.addFilter("jvereinid != 1 or jvereinid is null");
        break;
    }
    at.addFilter("jvereinid != 1 or jvereinid is null");
    at.setOrder("order by bezeichnung");

    if (typ == 1)
    {
      Adresstyp def = (Adresstyp) Einstellungen.getDBService()
          .createObject(Adresstyp.class, "1");
      suchadresstyp = new SelectInput(at, def);
    }
    else
    {
      Adresstyp def = null;
      try
      {
        def = (Adresstyp) Einstellungen.getDBService().createObject(
            Adresstyp.class, settings.getString("suchadresstyp", "2"));
      }
      catch (Exception e)
      {
        settings.setAttribute("suchadresstyp", "2");
        def = (Adresstyp) Einstellungen.getDBService().createObject(
            Adresstyp.class, settings.getString("suchadresstyp", "2"));
      }
      suchadresstyp = new SelectInput(at, def);
    }
    suchadresstyp.setName("Adresstyp");
    suchadresstyp.addListener(new Listener()
    {

      @Override
      public void handleEvent(Event event)
      {
        Adresstyp sel = (Adresstyp) suchadresstyp.getValue();
        try
        {
          settings.setAttribute("suchadresstyp", sel.getID());
        }
        catch (RemoteException e)
        {
          Logger.error("Fehler", e);
        }
      }
    });
    return suchadresstyp;
  }

  public SelectInput getAdresstyp() throws RemoteException
  {
    if (adresstyp != null)
    {
      return adresstyp;
    }
    DBIterator<Adresstyp> at = Einstellungen.getDBService()
        .createList(Adresstyp.class);
    at.addFilter("jvereinid != 1 or jvereinid is null");
    at.setOrder("order by bezeichnung");
    adresstyp = new SelectInput(at, getMitglied().getAdresstyp());
    adresstyp.setName("Adresstyp");
    return adresstyp;
  }

  public TextInput getExterneMitgliedsnummer() throws RemoteException
  {
    if (externemitgliedsnummer != null)
    {
      return externemitgliedsnummer;
    }
    externemitgliedsnummer = new TextInput(
        getMitglied().getExterneMitgliedsnummer(), 50);
    externemitgliedsnummer.setName("Ext. Mitgliedsnummer");
    externemitgliedsnummer.setMandatory(isExterneMitgliedsnummerMandatory());
    return externemitgliedsnummer;
  }

  private boolean isExterneMitgliedsnummerMandatory() throws RemoteException
  {
    if (Einstellungen.getEinstellung().getExterneMitgliedsnummer() == false)
      return false;
    if (view instanceof AbstractAdresseDetailView == false)
      return false;
    AbstractAdresseDetailView detailView = (AbstractAdresseDetailView) view;
    return detailView.isMitgliedDetail();
  }

  public TextInput getMitgliedsnummer() throws RemoteException
  {
    if (mitgliedsnummer != null)
    {
      return mitgliedsnummer;
    }
    mitgliedsnummer = new TextInput(getMitglied().getID(), 10);
    mitgliedsnummer.setName("Mitgliedsnummer");
    mitgliedsnummer.setEnabled(false);
    return mitgliedsnummer;
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

  public TextInput getName(boolean withFocus) throws RemoteException
  {
    if (name != null)
    {
      return name;
    }

    name = new TextInput(getMitglied().getName(), 40);
    name.setName("Name");
    name.setMandatory(true);
    if (withFocus)
    {
      name.focus();
    }
    return name;
  }

  public TextInput getVorname() throws RemoteException
  {
    if (vorname != null)
    {
      return vorname;
    }

    vorname = new TextInput(getMitglied().getVorname(), 40);
    vorname.setName("Vorname");
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

  public TextInput getStrasse() throws RemoteException
  {
    if (strasse != null)
    {
      return strasse;
    }
    strasse = new TextInput(getMitglied().getStrasse(), 40);

    strasse.setName("Straße");
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

      @Override
      public void handleEvent(Event event)
      {
        if (event.type == SWT.FocusOut)
        {
          String hplz = (String) plz.getValue();
          if (hplz.equals(""))
          {
            return;
          }
          try
          {
            DBIterator<Mitglied> it = Einstellungen.getDBService()
                .createList(Mitglied.class);
            it.addFilter("plz='" + (String) plz.getValue() + "'");
            if (it.hasNext())
            {
              Mitglied mplz = it.next();
              ort.setValue(mplz.getOrt());
            }
          }
          catch (RemoteException e)
          {
            Logger.error("Fehler", e);
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

  public Input getStaat() throws RemoteException
  {
    if (staat != null)
    {
      return staat;
    }
    staat = new TextInput(getMitglied().getStaat(), 50);
    staat.setName("Staat");
    return staat;
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
    this.geburtsdatum = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.geburtsdatum.setName("Geburtsdatum");
    this.geburtsdatum.setTitle("Geburtsdatum");
    this.geburtsdatum.setText("Bitte Geburtsdatum wählen");
    zeigeAlter(d);
    this.geburtsdatum
        .setMandatory(Einstellungen.getEinstellung().getGeburtsdatumPflicht());
    return geburtsdatum;
  }

  private void zeigeAlter(Date datum)
  {
    Integer alter = Datum.getAlter(datum);
    if (null != alter)
      geburtsdatum.setComment(" Alter: " + alter.toString());
    else
      geburtsdatum.setComment(" ");
  }

  public GeschlechtInput getGeschlecht() throws RemoteException
  {
    if (geschlecht != null)
    {
      return geschlecht;
    }
    geschlecht = new GeschlechtInput(getMitglied().getGeschlecht());
    geschlecht.setName("Geschlecht");
    geschlecht.setPleaseChoose("Bitte auswählen");
    geschlecht.setMandatory(true);
    geschlecht.setName("Geschlecht");
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
      zahlungsweg = new SelectInput(Zahlungsweg.getArray(),
          new Zahlungsweg(getMitglied().getZahlungsweg().intValue()));
    }
    else
    {
      zahlungsweg = new SelectInput(Zahlungsweg.getArray(),
          new Zahlungsweg(Einstellungen.getEinstellung().getZahlungsweg()));
    }
    zahlungsweg.setName("Zahlungsweg");
    zahlungsweg.addListener(new Listener()
    {

      @Override
      public void handleEvent(Event event)
      {
        if (event != null && event.type == SWT.Selection)
        {
          Zahlungsweg zahlungswegValue = (Zahlungsweg) zahlungsweg.getValue();
          boolean isLastschrift = zahlungswegValue
              .getKey() == Zahlungsweg.BASISLASTSCHRIFT;

          // Optimalerweise mit Prüfung auf zahlungsweg.hasChanged() und
          // zahlungsweg.getOldValue == BASISLASTSCHRIFT
          // Allerdings funktioniert hasChanged erst beim zweiten Aufruf, und
          // getOldValue gibt es in Jameica nicht.
          if (!isLastschrift)
          {
            YesNoDialog dialog = new YesNoDialog(YesNoDialog.POSITION_CENTER);
            dialog.setTitle("Bankverbindungsdaten");
            dialog.setText(
                "Die Bankverbindung wird beim gewählten Zahlungsweg nicht benötigt.\n"
                    + "Sollen eventuell vorhandene Werte gelöscht werden?");
            boolean delete = false;
            try
            {
              delete = ((Boolean) dialog.open()).booleanValue();
            }
            catch (Exception e)
            {
              Logger.error("Fehler beim Bankverbindung-Löschen-Dialog.", e);
            }
            if (delete)
            {
              deleteBankverbindung();
            }
          }

          // if (bankverbindungLabelGroup != null)
          // {
          // bankverbindungLabelGroup.getComposite().setVisible(isLastschrift);
          // }
        }
      }
    });
    return zahlungsweg;
  }

  // Lösche alle Daten aus der Bankverbindungsmaske
  private void deleteBankverbindung()
  {
    try
    {
      getZahlungsrhythmus().setValue(new Zahlungsrhythmus(
          Einstellungen.getEinstellung().getZahlungsrhytmus()));
      getMandatID().setValue(null);
      getMandatDatum().setValue(null);
      getMandatVersion().setValue(null);
      getMandatSequence().setValue(null);
      getLetzteLastschrift().setValue(null);
      getBic().setValue(null);
      getIban().setValue(null);
      getKtoiPersonenart().setValue(null);
      getKtoiAnrede().setValue(null);
      getKtoiTitel().setValue(null);
      getKtoiName().setValue(null);
      getKtoiVorname().setValue(null);
      getKtoiStrasse().setValue(null);
      getKtoiAdressierungszusatz().setValue(null);
      getKtoiPlz().setValue(null);
      getKtoiOrt().setValue(null);
      getKtoiStaat().setValue(null);
      getKtoiEmail().setValue(null);
    }
    catch (Exception e)
    {
      Logger.error("Fehler beim Leeren der Bankverbindungsdaten", e);
    }
  }

  public LabelGroup getBankverbindungLabelGroup(Composite parent)
  {
    if (bankverbindungLabelGroup == null)
    {
      bankverbindungLabelGroup = new LabelGroup(parent, "Bankverbindung");
    }
    return bankverbindungLabelGroup;
  }

  public SelectInput getZahlungsrhythmus() throws RemoteException
  {
    if (zahlungsrhytmus != null)
    {
      return zahlungsrhytmus;
    }
    if (getMitglied().getZahlungsrhythmus() != null)
    {
      zahlungsrhytmus = new SelectInput(Zahlungsrhythmus.getArray(),
          new Zahlungsrhythmus(getMitglied().getZahlungsrhythmus().getKey()));
    }
    else
    {
      zahlungsrhytmus = new SelectInput(Zahlungsrhythmus.getArray(),
          new Zahlungsrhythmus(
              Einstellungen.getEinstellung().getZahlungsrhytmus()));
    }
    zahlungsrhytmus.setName("Zahlungsrhytmus");
    return zahlungsrhytmus;
  }

  public SelectInput getZahlungstermin() throws RemoteException
  {
    if (zahlungstermin != null)
    {
      return zahlungstermin;
    }
    zahlungstermin = new SelectInput(Zahlungstermin.values(),
        getMitglied().getZahlungstermin());
    zahlungstermin.setName("Zahlungstermin");
    return zahlungstermin;
  }

  public TextInput getBic() throws RemoteException
  {
    if (bic != null)
    {
      return bic;
    }
    bic = new BICInput(getMitglied().getBic());
    bic.setMandatory(getMitglied().getZahlungsweg() == null || getMitglied()
        .getZahlungsweg().intValue() == Zahlungsweg.BASISLASTSCHRIFT);
    return bic;
  }

  public TextInput getMandatID() throws RemoteException
  {
    if (mandatid != null)
    {
      return mandatid;
    }
    mandatid = new TextInput(getMitglied().getMandatID());
    mandatid.setName("Mandats-ID");
    mandatid.disable();
    return mandatid;
  }

  public DateInput getMandatDatum() throws RemoteException
  {
    if (mandatdatum != null)
    {
      return mandatdatum;
    }

    Date d = getMitglied().getMandatDatum();
    if (d.equals(Einstellungen.NODATE))
    {
      d = null;
    }
    this.mandatdatum = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.mandatdatum.setTitle("Datum des Mandats");
    this.mandatdatum.setName("Datum des Mandats");
    this.mandatdatum.setText("Bitte Datum des Mandats wählen");
    return mandatdatum;
  }

  public SpinnerInput getMandatVersion() throws RemoteException
  {
    if (mandatversion != null)
    {
      return mandatversion;
    }
    mandatversion = new SpinnerInput(0, 1000, getMitglied().getMandatVersion());
    mandatversion.setName("Mandatsversion");
    mandatversion.addListener(new Listener()
    {
      @Override
      public void handleEvent(Event event)
      {
        try
        {
          getMitglied()
              .setMandatVersion((Integer) getMandatVersion().getValue());
          mandatid.setValue(getMitglied().getMandatID());
        }
        catch (RemoteException e)
        {
          Logger.error("Fehler", e);
        }

      }
    });
    return mandatversion;
  }

  public SelectInput getMandatSequence() throws RemoteException
  {
    if (mandatsequence != null)
    {
      return mandatsequence;
    }
    mandatsequence = new SelectInput(MandatSequence.values(),
        getMitglied().getMandatSequence());
    mandatsequence.setEnabled(false);
    mandatsequence.setName("Sequenz");
    return mandatsequence;
  }

  public DateInput getLetzteLastschrift() throws RemoteException
  {
    if (letztelastschrift != null)
    {
      return letztelastschrift;
    }

    Date d = getMitglied().getLetzteLastschrift();
    this.letztelastschrift = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.letztelastschrift.setEnabled(false);
    this.letztelastschrift.setName("letzte Lastschrift");
    return letztelastschrift;
  }

  public TextInput getIban() throws RemoteException
  {
    if (iban != null)
    {
      return iban;
    }
    iban = new IBANInput(getMitglied().getIban(), getBic());
    iban.setMandatory(getMitglied().getZahlungsweg() == null || getMitglied()
        .getZahlungsweg().intValue() == Zahlungsweg.BASISLASTSCHRIFT);
    return iban;
  }

  public SelectInput getKtoiPersonenart() throws RemoteException
  {
    if (ktoipersonenart != null)
    {
      return ktoipersonenart;
    }
    ktoipersonenart = new PersonenartInput(getMitglied().getKtoiPersonenart());
    ktoipersonenart.addListener(new Listener()
    {

      @Override
      public void handleEvent(Event event)
      {
        String pa = (String) ktoipersonenart.getValue();
        if (pa.startsWith("n"))
        {
          ktoiname.setName("Name");
          ktoivorname.setName("Vorname");
        }
        else
        {
          ktoiname.setName("Zeile 1");
          ktoivorname.setName("Zeile 2");
        }
      }

    });

    ktoipersonenart.setName("Personenart");
    return ktoipersonenart;
  }

  public TextInput getKtoiAnrede() throws RemoteException
  {
    if (ktoianrede != null)
    {
      return ktoianrede;
    }
    ktoianrede = new TextInput(getMitglied().getKtoiAnrede(), 40);
    ktoianrede.setName("Anrede");
    return ktoianrede;
  }

  public TextInput getKtoiTitel() throws RemoteException
  {
    if (ktoititel != null)
    {
      return ktoititel;
    }
    ktoititel = new TextInput(getMitglied().getKtoiTitel(), 40);
    ktoititel.setName("Titel");
    return ktoititel;
  }

  public TextInput getKtoiName() throws RemoteException
  {
    if (ktoiname != null)
    {
      return ktoiname;
    }
    ktoiname = new TextInput(getMitglied().getKtoiName(), 40);
    ktoiname.setName("Name");
    return ktoiname;
  }

  public TextInput getKtoiVorname() throws RemoteException
  {
    if (ktoivorname != null)
    {
      return ktoivorname;
    }
    ktoivorname = new TextInput(getMitglied().getKtoiVorname(), 40);
    ktoivorname.setName("Vorname");
    return ktoivorname;
  }

  public TextInput getKtoiStrasse() throws RemoteException
  {
    if (ktoistrasse != null)
    {
      return ktoistrasse;
    }
    ktoistrasse = new TextInput(getMitglied().getKtoiStrasse(), 40);
    ktoistrasse.setName("Straße");
    return ktoistrasse;
  }

  public TextInput getKtoiAdressierungszusatz() throws RemoteException
  {
    if (ktoiadressierungszusatz != null)
    {
      return ktoiadressierungszusatz;
    }
    ktoiadressierungszusatz = new TextInput(
        getMitglied().getKtoiAdressierungszusatz(), 40);
    ktoiadressierungszusatz.setName("Adressierungszusatz");
    return ktoiadressierungszusatz;
  }

  public TextInput getKtoiPlz() throws RemoteException
  {
    if (ktoiplz != null)
    {
      return ktoiplz;
    }
    ktoiplz = new TextInput(getMitglied().getKtoiPlz(), 10);
    ktoiplz.setName("Plz");
    return ktoiplz;
  }

  public TextInput getKtoiOrt() throws RemoteException
  {
    if (ktoiort != null)
    {
      return ktoiort;
    }
    ktoiort = new TextInput(getMitglied().getKtoiOrt(), 40);
    ktoiort.setName("Ort");
    return ktoiort;
  }

  public TextInput getKtoiStaat() throws RemoteException
  {
    if (ktoistaat != null)
    {
      return ktoistaat;
    }
    ktoistaat = new TextInput(getMitglied().getKtoiStaat(), 50);
    ktoistaat.setName("Staat");
    return ktoistaat;
  }

  public EmailInput getKtoiEmail() throws RemoteException
  {
    if (ktoiemail != null)
    {
      return ktoiemail;
    }
    ktoiemail = new EmailInput(getMitglied().getKtoiEmail());
    return ktoiemail;
  }

  public GeschlechtInput getKtoiGeschlecht() throws RemoteException
  {
    if (ktoigeschlecht != null)
    {
      return ktoigeschlecht;
    }
    ktoigeschlecht = new GeschlechtInput(getMitglied().getKtoiGeschlecht());
    ktoigeschlecht.setName("Geschlecht");
    ktoigeschlecht.setPleaseChoose("Bitte auswählen");
    ktoigeschlecht.setMandatory(true);
    ktoigeschlecht.setName("Geschlecht");
    ktoigeschlecht.setMandatory(false);
    return ktoigeschlecht;
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

  public EmailInput getEmail() throws RemoteException
  {
    if (email != null)
    {
      return email;
    }
    email = new EmailInput(getMitglied().getEmail());
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
    this.eintritt = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.eintritt.setTitle("Eintrittsdatum");
    this.eintritt.setName("Eintrittsdatum");
    this.eintritt.setText("Bitte Eintrittsdatum wählen");
    this.eintritt.setMandatory(
        Einstellungen.getEinstellung().getEintrittsdatumPflicht());
    return eintritt;
  }

  public Input getBeitragsgruppe(boolean allgemein) throws RemoteException
  {
    if (beitragsgruppe != null)
    {
      return beitragsgruppe;
    }
    DBIterator<Beitragsgruppe> list = Einstellungen.getDBService()
        .createList(Beitragsgruppe.class);
    list.addFilter("(sekundaer is null or sekundaer=?)", false);
    list.setOrder("ORDER BY bezeichnung");
    if (!allgemein)
    {
      // alte Beitragsgruppen hatten das Feld Beitragsarten noch nicht gesetzt
      // (NULL)
      // diese Beitragsgruppen müssen hier auch erlaubt sein.
      list.addFilter("beitragsart <> ? or beitragsart IS NULL",
          new Object[] { ArtBeitragsart.FAMILIE_ANGEHOERIGER.getKey() });
    }
    beitragsgruppe = new SelectInput(list, getMitglied().getBeitragsgruppe());
    beitragsgruppe.setName("Beitragsgruppe");
    beitragsgruppe.setValue(getMitglied().getBeitragsgruppe());
    beitragsgruppe.setMandatory(true);
    beitragsgruppe.setAttribute("bezeichnung");
    beitragsgruppe.setPleaseChoose("Bitte auswählen");
    beitragsgruppe.addListener(new Listener()
    {

      @Override
      public void handleEvent(Event event)
      {
        if (event.type != SWT.Selection)
        {
          return;
        }
        try
        {
          Beitragsgruppe bg = (Beitragsgruppe) beitragsgruppe.getValue();
          // Feld zahler ist nur aktiviert, wenn aktuelles Mitglied nicht das
          // zahlende Mitglied der Familie ist.
          if (bg != null
              && bg.getBeitragsArt() == ArtBeitragsart.FAMILIE_ANGEHOERIGER)
          {
            getFamilienverband().setVisible(true);
            getZukuenftigeBeitraegeView().setVisible(false);
            if (zahler != null)
            {
              zahler.setEnabled(true);
            }
            // Aktiviere "richtigen" Tab in der Tabs-Tabelle Familienverband
            if (famverb != null)
            {
              famverb.setBeitragsgruppe(bg);
            }
          }
          else if (bg != null
              && bg.getBeitragsArt() == ArtBeitragsart.FAMILIE_ZAHLER)
          {
            boolean ist_neu = getMitglied().getID() == null;
            getFamilienverband().setVisible(!ist_neu);
            // Zukünftige Beiträge nur bei bereits gespeicherten Mitgliedern
            if (!ist_neu)
            {
              getZukuenftigeBeitraegeView().setVisible(true);
            }
            getMitglied().setZahlerID(null);
            if (zahler != null)
            {
              zahler.setValue(Einstellungen.getDBService()
                  .createObject(Mitglied.class, ""));
              zahler.setEnabled(false);
            }
          }
          else
          {
            getMitglied().setZahlerID(null);
            if (zahler != null)
            {
              zahler.setPreselected(null);
              zahler.setEnabled(false);
            }
            getFamilienverband().setVisible(false);
            // Zukünftige Beiträge nur bei bereits gespeicherten Mitgliedern
            if (getMitglied().getID() != null)
            {
              getZukuenftigeBeitraegeView().setVisible(true);
            }
          }

          refreshFamilienangehoerigeTable();

        }
        catch (RemoteException e)
        {
          Logger.error("Fehler", e);
        }
      }
    });
    return beitragsgruppe;
  }

  public MitgliedSekundaereBeitragsgruppePart getMitgliedSekundaereBeitragsgruppeView()
  {
    if (null == mitgliedSekundaereBeitragsgruppeView)
      mitgliedSekundaereBeitragsgruppeView = new MitgliedSekundaereBeitragsgruppePart(
          this);
    return mitgliedSekundaereBeitragsgruppeView;
  }

  public TreePart getSekundaereBeitragsgruppe() throws RemoteException
  {
    if (sekundaerebeitragsgruppe != null)
    {
      return sekundaerebeitragsgruppe;
    }
    listeSeB = new ArrayList<>();
    if (!getMitglied().isNewObject())
    {
      DBIterator<Beitragsgruppe> bei = Einstellungen.getDBService()
          .createList(Beitragsgruppe.class);
      bei.addFilter("sekundaer=?", true);
      bei.setOrder("ORDER BY bezeichnung");
      while (bei.hasNext())
      {
        Beitragsgruppe b = bei.next();
        DBIterator<SekundaereBeitragsgruppe> sebei = Einstellungen
            .getDBService().createList(SekundaereBeitragsgruppe.class);
        sebei.addFilter("mitglied=?", getMitglied().getID());
        sebei.addFilter("beitragsgruppe=?", b.getID());
        if (sebei.hasNext())
        {
          SekundaereBeitragsgruppe sb = (SekundaereBeitragsgruppe) sebei.next();
          listeSeB.add(sb);
        }
        else
        {
          SekundaereBeitragsgruppe sb = (SekundaereBeitragsgruppe) Einstellungen
              .getDBService()
              .createObject(SekundaereBeitragsgruppe.class, null);
          sb.setMitglied(Integer.parseInt(getMitglied().getID()));
          sb.setBeitragsgruppe(Integer.parseInt(b.getID()));
          listeSeB.add(sb);
        }
      }
    }
    sekundaerebeitragsgruppe = new TreePart(listeSeB, null);
    sekundaerebeitragsgruppe.addColumn("Beitragsgruppe",
        "beitragsgruppebezeichnung");
    sekundaerebeitragsgruppe.setCheckable(true);
    sekundaerebeitragsgruppe.setMulti(true);
    sekundaerebeitragsgruppe.setFormatter(new TreeFormatter()
    {
      @Override
      public void format(TreeItem item)
      {
        SekundaereBeitragsgruppe sb = (SekundaereBeitragsgruppe) item.getData();
        try
        {
          item.setChecked(!sb.isNewObject());
        }
        catch (RemoteException e)
        {
          Logger.error("Fehler beim TreeFormatter", e);
        }
      }
    });
    return sekundaerebeitragsgruppe;
  }

  public DecimalInput getIndividuellerBeitrag() throws RemoteException
  {
    if (individuellerbeitrag != null)
    {
      return individuellerbeitrag;
    }
    individuellerbeitrag = new DecimalInput(
        getMitglied().getIndividuellerBeitrag(), Einstellungen.DECIMALFORMAT);
    individuellerbeitrag.setName("individueller Beitrag");
    return individuellerbeitrag;
  }

  public TextInput getAuswertungUeberschrift()
  {
    if (auswertungUeberschrift != null)
    {
      return auswertungUeberschrift;
    }
    auswertungUeberschrift = new TextInput(
        settings.getString("auswertung.ueberschrift", ""));
    auswertungUeberschrift.setName("Überschrift");
    return auswertungUeberschrift;
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
        bg = (Beitragsgruppe) Einstellungen.getDBService()
            .createObject(Beitragsgruppe.class, beitragsgru);
      }
      catch (ObjectNotFoundException e)
      {
        bg = (Beitragsgruppe) Einstellungen.getDBService()
            .createObject(Beitragsgruppe.class, null);
      }
    }
    DBIterator<Beitragsgruppe> list = Einstellungen.getDBService()
        .createList(Beitragsgruppe.class);
    list.setOrder("ORDER BY bezeichnung");
    beitragsgruppeausw = new SelectInput(list, bg);
    beitragsgruppeausw.setName("Beitragsgruppe");
    beitragsgruppeausw.setAttribute("bezeichnung");
    beitragsgruppeausw.setPleaseChoose("Bitte auswählen");
    beitragsgruppeausw.setName("Beitragsgruppe");
    return beitragsgruppeausw;
  }

  /**
   * Liefert ein Part zurück, das den Familienverband anzeigt. Da Container
   * jedoch nur das Hinzufügen von Parts zulassen, ist das Part Familienverband
   * dynamisch: Entweder wird der Familienverband angezeigt (setShow(true)),
   * oder ein leeres Composite (setShow(false))
   * 
   * @return Familienverband Part
   * @throws RemoteException
   */
  public Familienverband getFamilienverband() throws RemoteException
  {
    if (famverb != null)
    {
      return famverb;
    }
    famverb = new Familienverband(this, getMitglied().getBeitragsgruppe());
    return famverb;
  }

  public MitgliedNextBGruppePart getZukuenftigeBeitraegeView()
  {
    if (null == zukueftigeBeitraegeView)
      zukueftigeBeitraegeView = new MitgliedNextBGruppePart(this);
    return zukueftigeBeitraegeView;
  }

  public Input getZahler() throws RemoteException
  {
    return getZahler(false);
  }

  public Input getZahler(boolean force) throws RemoteException
  {
    if (zahler != null)
    {
      // wenn force nicht gesetzt, gib aktuellen zahler zurück.
      if (force != true)
        return zahler;
      // ansonsten: erzeuge neuen...
      // Dies ist nötig, wenn Zahler ausgeblendet wurde und daher der
      // Parent vom GC disposed wurde.
    }

    StringBuffer cond = new StringBuffer();

    // Beitragsgruppen ermitteln, die Zahler für andere Mitglieder sind
    DBIterator<Beitragsgruppe> bg = Einstellungen.getDBService()
        .createList(Beitragsgruppe.class);
    bg.addFilter("beitragsart = ?", ArtBeitragsart.FAMILIE_ZAHLER.getKey());
    while (bg.hasNext())
    {
      if (cond.length() > 0)
      {
        cond.append(" OR ");
      }
      Beitragsgruppe beitragsgruppe = bg.next();
      cond.append("beitragsgruppe = ");
      cond.append(beitragsgruppe.getID());
    }
    DBIterator<Mitglied> zhl = Einstellungen.getDBService()
        .createList(Mitglied.class);
    zhl.addFilter(cond.toString());
    MitgliedUtils.setNurAktive(zhl);
    MitgliedUtils.setMitglied(zhl);
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

      @Override
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
            getMitglied().setZahlerID(new Long(m.getID()));
          }
          else
          {
            getMitglied().setZahlerID(null);
          }
          refreshFamilienangehoerigeTable();
        }
        catch (RemoteException e)
        {
          Logger.error("Fehler", e);
        }
      }
    });

    if (getBeitragsgruppe(true) != null
        && getBeitragsgruppe(true).getValue() != null)
    {
      Beitragsgruppe bg2 = (Beitragsgruppe) getBeitragsgruppe(true).getValue();
      if (bg2.getBeitragsArt() == ArtBeitragsart.FAMILIE_ANGEHOERIGER)
      {
        zahler.setEnabled(true);
      }
      else
      {
        zahler.setPreselected(getMitglied());
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

    this.austritt = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.austritt.setTitle("Austrittsdatum");
    this.austritt.setName("Austrittsdatum");
    this.austritt.setText("Bitte Austrittsdatum wählen");
    this.austritt.addListener(new Listener()
    {

      @Override
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

    this.kuendigung = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.kuendigung.setName("Kündigungsdatum");
    this.kuendigung.setTitle("Kündigungsdatum");
    this.kuendigung.setText("Bitte Kündigungsdatum wählen");
    this.kuendigung.addListener(new Listener()
    {

      @Override
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

  public DateInput getSterbetag() throws RemoteException
  {
    if (sterbetag != null)
    {
      return sterbetag;
    }
    Date d = getMitglied().getSterbetag();

    this.sterbetag = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.sterbetag.setName("Sterbetag");
    this.sterbetag.setTitle("Sterbetag");
    this.sterbetag.setText("Bitte Sterbetag wählen");
    this.sterbetag.addListener(new Listener()
    {

      @Override
      public void handleEvent(Event event)
      {
        Date date = (Date) sterbetag.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return sterbetag;
  }

  public TextAreaInput getVermerk1() throws RemoteException
  {
    if (vermerk1 != null)
    {
      return vermerk1;
    }
    vermerk1 = new TextAreaInput(getMitglied().getVermerk1(), 2000);
    vermerk1.setName("Vermerk 1");
    return vermerk1;
  }

  public TextAreaInput getVermerk2() throws RemoteException
  {
    if (vermerk2 != null)
    {
      return vermerk2;
    }
    vermerk2 = new TextAreaInput(getMitglied().getVermerk2(), 2000);
    vermerk2.setName("Vermerk 2");
    return vermerk2;
  }

  public ImageInput getFoto() throws RemoteException
  {
    if (foto != null)
    {
      return foto;
    }
    DBIterator<Mitgliedfoto> it = Einstellungen.getDBService()
        .createList(Mitgliedfoto.class);
    it.addFilter("mitglied = ?", new Object[] { mitglied.getID() });
    Mitgliedfoto fo = null;
    if (it.size() > 0)
    {
      fo = (Mitgliedfoto) it.next();
    }
    byte[] f = null;
    if (fo != null)
    {
      f = fo.getFoto();
    }
    foto = new ImageInput(f, 150, 200);
    return foto;
  }

  public Input[] getZusatzfelder() throws RemoteException
  {
    if (zusatzfelder != null)
    {
      return zusatzfelder;
    }
    DBIterator<Felddefinition> it = Einstellungen.getDBService()
        .createList(Felddefinition.class);
    it.setOrder("order by label");
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
      Felddefinition fd = it.next();
      zf = (Zusatzfelder) Einstellungen.getDBService()
          .createObject(Zusatzfelder.class, null);
      zf.setFelddefinition(Integer.parseInt(fd.getID()));

      if (getMitglied().getID() != null)
      {
        DBIterator<Zusatzfelder> it2 = Einstellungen.getDBService()
            .createList(Zusatzfelder.class);
        it2.addFilter("mitglied=?", new Object[] { getMitglied().getID() });
        it2.addFilter("felddefinition=?", new Object[] { fd.getID() });
        if (it2.size() > 0)
        {
          zf.setMitglied(Integer.parseInt(getMitglied().getID()));
          zf = it2.next();
        }
      }
      switch (fd.getDatentyp())
      {
        case Datentyp.ZEICHENFOLGE:
          zusatzfelder[i] = new TextInput(zf.getFeld(), fd.getLaenge());
          break;
        case Datentyp.DATUM:
          Date d = zf.getFeldDatum();
          DateInput di = new DateInput(d, new JVDateFormatTTMMJJJJ());
          di.setName(fd.getLabel());
          di.setTitle(fd.getLabel());
          di.setText(String.format("Bitte %s wählen", fd.getLabel()));
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

  public Input[] getLesefelder() throws RemoteException
  {
    if (lesefelder != null)
    {
      return lesefelder;
    }

    // erstelle lesefeldAuswerter, der alle Daten und Methoden
    // zum Evaluieren von Skripten enthält.
    if (lesefeldAuswerter == null)
    {
      lesefeldAuswerter = new LesefeldAuswerter();
      lesefeldAuswerter.setLesefelderDefinitionsFromDatabase();
    }

    // Sind keine Lesefelder definiert, erzeuge keine GUI-Elemente
    if (lesefeldAuswerter.countLesefelder() == 0)
      return null;

    // Ist noch keine ID verfügbar, wird das Mitglied gerade angelegt.
    // Dann darf getMap() nicht aufgerufen werden, da sonst Standard-Werte
    // für Mitglied gesetzt werden (z.B. das Sterbedatum auf heute!)
    // Da lesefeldAuswerter aber einen kompletten Datensatz eines Mitglieds
    // benötigt um alle Skripte fehlerfrei zu parsen, dürfen die Lesefelder
    // noch nicht ausgewertet werden. Die GUI-Elemente werden daher beim
    // ersten Erstellen eines neuen Mitglieds noch nicht angezeigt.
    if (getMitglied().getID() == null)
      return null;

    lesefeldAuswerter
        .setMap(new MitgliedMap().getMap(getMitglied(), null, true));

    lesefelder = new Input[lesefeldAuswerter.countLesefelder()];

    int i = 0;
    Iterator<Entry<String, Object>> it = lesefeldAuswerter.getLesefelderMap()
        .entrySet().iterator();
    while (it.hasNext())
    {
      // Evaluiere Skripte und erzeuge für jedes ein TextAreaInput mit
      // dem ausgewerteten Inhalt sowie dem Skriptnamen davor.
      Entry<String, Object> pairs = it.next();
      TextAreaInput t = new TextAreaInput(pairs.getValue().toString());
      t.setEnabled(false);
      t.setName(pairs.getKey());
      lesefelder[i] = t;
      i++;
    }
    return lesefelder;
  }

  public void refreshFamilienangehoerigeTable() throws RemoteException
  {
    if (familienangehoerige == null)
      return;
    familienangehoerige.removeAll();
    DBService service = Einstellungen.getDBService();
    DBIterator<Mitglied> famiter = service.createList(Mitglied.class);
    famiter.addFilter("zahlerid = ? or zahlerid = ? or id = ? or id = ?",
        getMitglied().getID(), getMitglied().getZahlerID(),
        getMitglied().getID(), getMitglied().getZahlerID());
    famiter.setOrder("ORDER BY name, vorname");
    while (famiter.hasNext())
    {
      Mitglied m = famiter.next();
      // Wenn der Iterator auf das aktuelle Mitglied zeigt,
      // nutze stattdessen getMitglied() damit nicht das alte, unveränderte
      // Mitglied
      // aus der DB verwendet wird, sondern das vom Nutzer veränderte Mitglied.
      if (m.getID().equalsIgnoreCase(getMitglied().getID()))
        m = getMitglied();
      familienangehoerige.addItem(m);
    }
  }

  public Part getFamilienangehoerigenTable() throws RemoteException
  {
    if (familienangehoerige != null)
    {
      return familienangehoerige;
    }

    familienangehoerige = new TablePart(new MitgliedDetailAction());
    familienangehoerige.setRememberColWidths(true);
    familienangehoerige.setRememberOrder(true);
    refreshFamilienangehoerigeTable();
    familienangehoerige.addColumn("Name", "name");
    familienangehoerige.addColumn("Vorname", "vorname");
    familienangehoerige.addColumn("", "zahlerid", new Formatter()
    {

      @Override
      public String format(Object o)
      {
        // Alle Familienmitglieder, die eine Zahler-ID eingetragen haben, sind
        // nicht selbst das zahlende Mitglied.
        // Der Eintrag ohne zahlerid ist also das zahlende Mitglied.
        Long m = (Long) o;
        if (m == null)
          return "";
        else
          return "beitragsbefreites Familienmitglied";
      }
    });

    return familienangehoerige;
  }

  public Part getZusatzbetraegeTable() throws RemoteException
  {
    if (zusatzbetraegeList != null)
    {
      return zusatzbetraegeList;
    }
    DBService service = Einstellungen.getDBService();
    DBIterator<Zusatzbetrag> zusatzbetraege = service
        .createList(Zusatzbetrag.class);
    zusatzbetraege.addFilter("mitglied = " + getMitglied().getID());
    zusatzbetraegeList = new TablePart(zusatzbetraege,
        new ZusatzbetraegeAction(getMitglied()));
    zusatzbetraegeList.setRememberColWidths(true);
    zusatzbetraegeList.setRememberOrder(true);

    zusatzbetraegeList.addColumn("Startdatum", "startdatum",
        new DateFormatter(new JVDateFormatTTMMJJJJ()));
    zusatzbetraegeList.addColumn("nächste Fälligkeit", "faelligkeit",
        new DateFormatter(new JVDateFormatTTMMJJJJ()));
    zusatzbetraegeList.addColumn("letzte Ausführung", "ausfuehrung",
        new DateFormatter(new JVDateFormatTTMMJJJJ()));
    zusatzbetraegeList.addColumn("Intervall", "intervalltext");
    zusatzbetraegeList.addColumn("Endedatum", "endedatum",
        new DateFormatter(new JVDateFormatTTMMJJJJ()));
    zusatzbetraegeList.addColumn("Buchungstext", "buchungstext");
    zusatzbetraegeList.addColumn("Betrag", "betrag",
        new CurrencyFormatter("", Einstellungen.DECIMALFORMAT));
    zusatzbetraegeList.addColumn("Buchungsart", "buchungsart");
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
    DBIterator<Zusatzbetrag> wiedervorlagen = service
        .createList(Wiedervorlage.class);
    wiedervorlagen.addFilter("mitglied = " + getMitglied().getID());
    wiedervorlagen.setOrder("ORDER BY datum DESC");
    wiedervorlageList = new TablePart(wiedervorlagen,
        new WiedervorlageAction(getMitglied()));
    wiedervorlageList.setRememberColWidths(true);
    wiedervorlageList.setRememberOrder(true);

    wiedervorlageList.addColumn("Datum", "datum",
        new DateFormatter(new JVDateFormatTTMMJJJJ()));
    wiedervorlageList.addColumn("Vermerk", "vermerk");
    wiedervorlageList.addColumn("Erledigung", "erledigung",
        new DateFormatter(new JVDateFormatTTMMJJJJ()));
    wiedervorlageList.setContextMenu(new WiedervorlageMenu(wiedervorlageList));
    return wiedervorlageList;
  }

  public TablePart getMailTable() throws RemoteException
  {
    if (mailList != null)
    {
      return mailList;
    }
    DBService service = Einstellungen.getDBService();
    DBIterator<Mail> me = service.createList(Mail.class);
    me.join("mailempfaenger");
    me.addFilter("mailempfaenger.mail = mail.id");
    me.addFilter("mailempfaenger.mitglied = ?", getMitglied().getID());
    mailList = new TablePart(me, new MailDetailAction());
    mailList.setRememberColWidths(true);
    mailList.setRememberOrder(true);

    mailList.addColumn("Bearbeitung", "bearbeitung",
        new DateFormatter(new JVDateFormatTIMESTAMP()));
    mailList.addColumn("Versand", "versand",
        new DateFormatter(new JVDateFormatTIMESTAMP()));
    mailList.addColumn("Betreff", "betreff");
    mailList.setContextMenu(new MitgliedMailMenu(this));
    return mailList;
  }

  public Part getArbeitseinsatzTable() throws RemoteException
  {
    if (arbeitseinsatzList != null)
    {
      return arbeitseinsatzList;
    }
    DBService service = Einstellungen.getDBService();
    DBIterator<Arbeitseinsatz> arbeitseinsaetze = service
        .createList(Arbeitseinsatz.class);
    arbeitseinsaetze.addFilter("mitglied = " + getMitglied().getID());
    arbeitseinsaetze.setOrder("ORDER by datum desc");
    arbeitseinsatzList = new TablePart(arbeitseinsaetze,
        new ArbeitseinsatzAction(mitglied));
    arbeitseinsatzList.setRememberColWidths(true);
    arbeitseinsatzList.setRememberOrder(true);
    arbeitseinsatzList.setContextMenu(new ArbeitseinsatzMenu());

    arbeitseinsatzList.addColumn("Datum", "datum",
        new DateFormatter(new JVDateFormatTTMMJJJJ()));
    arbeitseinsatzList.addColumn("Stunden", "stunden",
        new CurrencyFormatter("", Einstellungen.DECIMALFORMAT));
    arbeitseinsatzList.addColumn("Bemerkung", "bemerkung");
    // wiedervorlageList.setContextMenu(new
    // WiedervorlageMenu(wiedervorlageList));
    return arbeitseinsatzList;
  }

  public Part getLehrgaengeTable() throws RemoteException
  {
    if (lehrgaengeList != null)
    {
      return lehrgaengeList;
    }
    DBService service = Einstellungen.getDBService();
    DBIterator<Lehrgang> lehrgaenge = service.createList(Lehrgang.class);
    lehrgaenge.addFilter("mitglied = " + getMitglied().getID());
    lehrgaengeList = new TablePart(lehrgaenge,
        new LehrgangAction(getMitglied()));
    lehrgaengeList.setRememberColWidths(true);
    lehrgaengeList.setRememberOrder(true);

    lehrgaengeList.addColumn("Lehrgangsart", "lehrgangsart");
    lehrgaengeList.addColumn("von/am", "von",
        new DateFormatter(new JVDateFormatTTMMJJJJ()));
    lehrgaengeList.addColumn("bis", "bis",
        new DateFormatter(new JVDateFormatTTMMJJJJ()));
    lehrgaengeList.addColumn("Veranstalter", "veranstalter");
    lehrgaengeList.addColumn("Ergebnis", "ergebnis");
    lehrgaengeList.setContextMenu(new LehrgangMenu());
    return lehrgaengeList;
  }

  public DateInput getGeburtsdatumvon()
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
        d = new JVDateFormatTTMMJJJJ().parse(tmp);
      }
      catch (ParseException e)
      {
        //
      }
    }
    this.geburtsdatumvon = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.geburtsdatumvon.setTitle("Geburtsdatum");
    this.geburtsdatumvon.setText("Beginn des Geburtszeitraumes");
    this.geburtsdatumvon.addListener(new Listener()
    {

      @Override
      public void handleEvent(Event event)
      {
        Date date = (Date) geburtsdatumvon.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    geburtsdatumvon.setName("Geburtsdatum von");
    return geburtsdatumvon;
  }

  public DateInput getGeburtsdatumbis()
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
        d = new JVDateFormatTTMMJJJJ().parse(tmp);
      }
      catch (ParseException e)
      {
        //
      }
    }
    this.geburtsdatumbis = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.geburtsdatumbis.setTitle("Geburtsdatum");
    this.geburtsdatumbis.setText("Ende des Geburtszeitraumes");
    this.geburtsdatumbis.addListener(new Listener()
    {

      @Override
      public void handleEvent(Event event)
      {
        Date date = (Date) geburtsdatumbis.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    geburtsdatumbis.setName("Geburtsdatum bis");
    return geburtsdatumbis;
  }

  public DateInput getSterbedatumvon()
  {
    if (sterbedatumvon != null)
    {
      return sterbedatumvon;
    }
    Date d = null;
    String tmp = settings.getString("mitglied.sterbedatumvon", null);
    if (tmp != null)
    {
      try
      {
        d = new JVDateFormatTTMMJJJJ().parse(tmp);
      }
      catch (ParseException e)
      {
        //
      }
    }
    this.sterbedatumvon = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.sterbedatumvon.setTitle("Sterbedatum");
    this.sterbedatumvon.setText("Beginn des Sterbezeitraumes");
    this.sterbedatumvon.addListener(new Listener()
    {

      @Override
      public void handleEvent(Event event)
      {
        Date date = (Date) sterbedatumvon.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    sterbedatumvon.setName("Sterbedatum von");
    return sterbedatumvon;
  }

  public DateInput getSterbedatumbis()
  {
    if (sterbedatumbis != null)
    {
      return sterbedatumbis;
    }
    Date d = null;
    String tmp = settings.getString("mitglied.sterbedatumbis", null);
    if (tmp != null)
    {
      try
      {
        d = new JVDateFormatTTMMJJJJ().parse(tmp);
      }
      catch (ParseException e)
      {
        //
      }
    }
    this.sterbedatumbis = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.sterbedatumbis.setTitle("Sterbedatum");
    this.sterbedatumbis.setText("Ende des Sterbezeitraumes");
    this.sterbedatumbis.addListener(new Listener()
    {

      @Override
      public void handleEvent(Event event)
      {
        Date date = (Date) sterbedatumbis.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    sterbedatumbis.setName("Sterbedatum bis");
    return sterbedatumbis;
  }

  public DateInput getEintrittvon()
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
        d = new JVDateFormatTTMMJJJJ().parse(tmp);
      }
      catch (ParseException e)
      {
        //
      }
    }
    this.eintrittvon = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.eintrittvon.setTitle("Eintrittsdatum");
    this.eintrittvon.setText("Beginn des Eintrittszeitraumes");
    this.eintrittvon.addListener(new Listener()
    {

      @Override
      public void handleEvent(Event event)
      {
        Date date = (Date) eintrittvon.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    eintrittvon.setName("Eintritt von");
    return eintrittvon;
  }

  public boolean isEintrittbisAktiv()
  {
    return eintrittbis != null;
  }

  public DateInput getEintrittbis()
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
        d = new JVDateFormatTTMMJJJJ().parse(tmp);
      }
      catch (ParseException e)
      {
        //
      }
    }
    this.eintrittbis = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.eintrittbis.setTitle("Eintrittsdatum");
    this.eintrittbis.setText("Ende des Eintrittszeitraumes");
    this.eintrittbis.addListener(new Listener()
    {

      @Override
      public void handleEvent(Event event)
      {
        Date date = (Date) eintrittbis.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    eintrittbis.setName("Eintritt bis");
    return eintrittbis;
  }

  public DateInput getAustrittvon()
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
        d = new JVDateFormatTTMMJJJJ().parse(tmp);
      }
      catch (ParseException e)
      {
        //
      }
    }
    this.austrittvon = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.austrittvon.setTitle("Austrittsdatum");
    this.austrittvon.setText("Beginn des Austrittszeitraumes");
    this.austrittvon.addListener(new Listener()
    {

      @Override
      public void handleEvent(Event event)
      {
        Date date = (Date) austrittvon.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    austrittvon.setName("Austritt von");
    return austrittvon;
  }

  public boolean isAustrittbisAktiv()
  {
    return austrittbis != null;
  }

  public DateInput getAustrittbis()
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
        d = new JVDateFormatTTMMJJJJ().parse(tmp);
      }
      catch (ParseException e)
      {
        //
      }
    }
    this.austrittbis = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.austrittbis.setTitle("Austrittsdatum");
    this.austrittbis.setText("Ende des Austrittszeitraumes");
    this.austrittbis.addListener(new Listener()
    {

      @Override
      public void handleEvent(Event event)
      {
        Date date = (Date) austrittbis.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    austrittbis.setName("Austritt bis");
    return austrittbis;
  }

  public TextInput getSuchname()
  {
    if (suchname != null)
    {
      return suchname;
    }
    this.suchname = new TextInput(settings.getString("mitglied.suchname", ""),
        50);
    suchname.setName("Name");
    return suchname;
  }

  public DateInput getStichtag()
  {
    if (stichtag != null)
    {
      return stichtag;
    }
    Date d = null;
    String tmp = settings.getString("mitglied.stichtag", null);
    if (tmp != null)
    {
      try
      {
        d = new JVDateFormatTTMMJJJJ().parse(tmp);
      }
      catch (ParseException e)
      {
        //
      }
    }
    this.stichtag = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.stichtag.setTitle("Stichtag");
    this.stichtag.setText("Stichtag");
    this.stichtag.addListener(new Listener()
    {

      @Override
      public void handleEvent(Event event)
      {
        Date date = (Date) stichtag.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    stichtag.setName("Stichtag");
    return stichtag;
  }

  public DateInput getStichtag(boolean jahresende)
  {
    if (stichtag != null)
    {
      return stichtag;
    }
    Date d = new Date();
    if (jahresende)
    {
      Calendar cal = Calendar.getInstance();
      cal.set(Calendar.MONTH, Calendar.DECEMBER);
      cal.set(Calendar.DAY_OF_MONTH, 31);
      d = new Date(cal.getTimeInMillis());
    }
    this.stichtag = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.stichtag.setTitle("Stichtag");
    this.stichtag.setName("Stichtag");
    return stichtag;
  }

  public SelectInput getJubeljahr()
  {
    if (jubeljahr != null)
    {
      return jubeljahr;
    }
    Calendar cal = Calendar.getInstance();
    jjahr = cal.get(Calendar.YEAR);
    cal.add(Calendar.YEAR, -2);
    Integer[] jubeljahre = new Integer[5];
    for (int i = 0; i < 5; i++)
    {
      jubeljahre[i] = cal.get(Calendar.YEAR);
      cal.add(Calendar.YEAR, 1);
    }
    jubeljahr = new SelectInput(jubeljahre, jubeljahre[2]);
    jubeljahr.addListener(new Listener()
    {

      @Override
      public void handleEvent(Event event)
      {
        jjahr = (Integer) jubeljahr.getValue();
      }
    });
    return jubeljahr;
  }

  public int getJJahr()
  {
    return jjahr;
  }

  public DialogInput getEigenschaftenAuswahl() throws RemoteException
  {
    String tmp = settings.getString("mitglied.eigenschaften", "");
    final EigenschaftenAuswahlDialog d = new EigenschaftenAuswahlDialog(tmp,
        false, true);
    d.addCloseListener(new EigenschaftenListener());

    StringTokenizer stt = new StringTokenizer(tmp, ",");
    StringBuilder text = new StringBuilder();
    while (stt.hasMoreElements())
    {
      if (text.length() > 0)
      {
        text.append(", ");
      }
      try
      {
        Eigenschaft ei = (Eigenschaft) Einstellungen.getDBService()
            .createObject(Eigenschaft.class, stt.nextToken());
        text.append(ei.getBezeichnung());
      }
      catch (ObjectNotFoundException e)
      {
        //
      }
    }
    eigenschaftenabfrage = new DialogInput(text.toString(), d);
    eigenschaftenabfrage.setName("Eigenschaften");
    eigenschaftenabfrage.addListener(new Listener()
    {

      @Override
      public void handleEvent(Event event)
      {
        d.setDefaults(settings.getString("mitglied.eigenschaften", ""));
      }
    });
    return eigenschaftenabfrage;
  }

  public void resetEigenschaftenAuswahl()
  {
    settings.setAttribute("mitglied.eigenschaften", "");
    eigenschaftenabfrage.setText("");
    eigenschaftenabfrage.getControl().redraw();
  }

  public DialogInput getZusatzfelderAuswahl()
  {
    if (zusatzfelderabfrage != null)
    {
      return zusatzfelderabfrage;
    }
    zad = new ZusatzfelderAuswahlDialog(settings);
    zad.addCloseListener(new ZusatzfelderListener());

    zusatzfelderabfrage = new DialogInput("", zad);
    setZusatzfelderAuswahl();
    zusatzfelderabfrage.setName("Zusatzfelder");
    return zusatzfelderabfrage;
  }

  public void resetZusatzfelderAuswahl()
  {
    settings.setAttribute("mitglied.eigenschaften", "");
    settings.setAttribute("zusatzfelder.selected", 0);
    setZusatzfelderAuswahl();
  }

  public Input getAusgabe() throws RemoteException
  {
    if (ausgabe != null)
    {
      return ausgabe;
    }

    // Hilfsklasse FilenameFilter *.csv
    FilenameFilter csvFilter = new FilenameFilter()
    {

      @Override
      public boolean accept(File dir, String name)
      {
        return name.toLowerCase().endsWith(".csv");
      }
    };

    // Suche alle *.csv Dateien im vorlagencsvverzeichnis
    String vorlagencsvverzeichnis = "";
    String[] vorlagencsvList = {};
    vorlagencsvverzeichnis = Einstellungen.getEinstellung()
        .getVorlagenCsvVerzeichnis();
    if (vorlagencsvverzeichnis.length() > 0)
    {
      File verzeichnis = new File(vorlagencsvverzeichnis);
      if (verzeichnis.isDirectory())
      {
        vorlagencsvList = verzeichnis.list(csvFilter);
      }
    }

    // erzeuge Auswertungsobjekte
    List<Object> objectList = new ArrayList<>();
    objectList.add(new MitgliedAuswertungPDF(this));
    objectList.add(new MitgliedAuswertungCSV());
    objectList.add(new MitgliedAdressbuchExport());

    for (String vorlagecsv : vorlagencsvList)
    {
      objectList.add(new MitgliedAuswertungCSV(
          vorlagencsvverzeichnis + File.separator + vorlagecsv));
    }

    ausgabe = new SelectInput(objectList.toArray(), null);
    ausgabe.setName("Ausgabe");
    return ausgabe;
  }

  // RWU: vorlage fuer .csv ausgabe
  public Input getVorlagedateicsv()
  {
    if (vorlagedateicsv != null)
    {
      return vorlagedateicsv;
    }
    String lastValue = settings.getString("auswertung.vorlagedateicsv", "");
    String[] extensions = { "*.csv" };
    vorlagedateicsv = new FileInput(lastValue, false, extensions);
    vorlagedateicsv.setName("Vorlagedatei CSV");
    vorlagedateicsv.setEnabled(false); // default is PDF
    return vorlagedateicsv;
  }

  public Input getSortierung()
  {
    if (sortierung != null)
    {
      return sortierung;
    }
    String[] sort = { "Name, Vorname", "Eintrittsdatum", "Geburtsdatum",
        "Geburtstagsliste" };
    sortierung = new SelectInput(sort, "Name, Vorname");
    sortierung.setName("Sortierung");
    return sortierung;
  }

  public boolean isMitgliedStatusAktiv()
  {
    return status != null;
  }

  public TextInput getSuchExterneMitgliedsnummer()
  {
    if (suchexternemitgliedsnummer != null)
    {
      return suchexternemitgliedsnummer;
    }
    suchexternemitgliedsnummer = new TextInput("", 50);
    return suchexternemitgliedsnummer;
  }

  public Input getMitgliedStatus()
  {
    if (status != null)
    {
      return status;
    }
    status = new SelectInput(
        new String[] { "Angemeldet", "Abgemeldet", "An- und Abgemeldete" },
        settings.getString("status.mitglied", "Angemeldet"));
    status.setName("Mitgliedschaft");
    return status;
  }

  public SelectInput getMailauswahl() throws RemoteException
  {
    if (mailAuswahl != null)
    {
      return mailAuswahl;
    }
    mailAuswahl = new MailAuswertungInput(1);
    mailAuswahl.setName("Mail");
    return mailAuswahl;
  }

  public Button getStartAuswertungButton()
  {
    Button b = new Button("starten", new Action()
    {

      @Override
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
    }, null, true, "walking.png"); // "true" defines this button as the default
    // button
    return b;
  }

  public Button getMitglied2KontoinhaberEintragenButton()
  {
    Button b = new Button("Mitglied-Daten eintragen", new Action()
    {

      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        try
        {
          getKtoiName().setValue(getName(false).getValue());
          getKtoiStrasse().setValue(getStrasse().getValue());
          getKtoiAdressierungszusatz()
              .setValue(getAdressierungszusatz().getValue());
          getKtoiPlz().setValue(getPlz().getValue());
          getKtoiOrt().setValue(getOrt().getValue());
          getKtoiEmail().setValue(getEmail().getValue());
          if (Einstellungen.getEinstellung().getAuslandsadressen())
          {
            getKtoiStaat().setValue(getStaat().getValue());
          }
        }
        catch (RemoteException e)
        {
          Logger.error(e.getMessage());
          throw new ApplicationException(
              "Fehler beim Start der Mitgliederauswertung");
        }
      }
    }, null, true, "walking.png"); // "true" defines this button as the default
    // button
    return b;
  }

  public Button getProfileButton()
  {
    Button b = new Button("Such-Profile", new Action()
    {

      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        try
        {
          saveDefaults();
        }
        catch (RemoteException e)
        {
          throw new ApplicationException(e);
        }
        GUI.startView(MitgliederSuchProfilView.class.getName(), settings);
      }
    }, null, true, "user-check.png"); // "true" defines this button as the
                                      // default button
    return b;
  }

  public Button getStartAdressAuswertungButton()
  {
    Button b = new Button("starten", new Action()
    {

      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        try
        {
          starteAdressAuswertung();
        }
        catch (RemoteException e)
        {
          Logger.error(e.getMessage());
          throw new ApplicationException(
              "Fehler beim Start der Adressauswertung");
        }
      }
    }, null, true, "walking.png"); // "true" defines this button as the default
    // button
    return b;
  }

  public Button getStartStatistikButton()
  {
    Button b = new Button("starten", new Action()
    {

      @Override
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
    }, null, true, "walking.png"); // "true" defines this button as the default
    // button
    return b;
  }

  public Button getVorlagenCsvEditButton()
  {
    Button b = new Button("CSV Vorlagen...", new Action()
    {
      @Override
      public void handleAction(Object context)
      {
        GUI.startView(AuswertungVorlagenCsvView.class.getName(), null);
      }
    }, null, false, "code.png");
    // button
    return b;

  }

  public Button getLesefelderEdit()
  {
    return new Button("Bearbeiten",
        new LesefelddefinitionenAction(getMitglied()), null, false, "edit.png");
  }

  public Button getZusatzbetragNeu()
  {
    return new Button("Neuer Zusatzbetrag",
        new ZusatzbetraegeAction(getMitglied()), null, false, "file.png");
  }

  public Button getWiedervorlageNeu()
  {
    return new Button("Neue Wiedervorlage",
        new WiedervorlageAction(getMitglied()), null, false, "file.png");
  }

  public Button getArbeitseinsatzNeu()
  {
    return new Button("Neuer Arbeitseinsatz",
        new ArbeitseinsatzAction(getMitglied()), null, false, "file.png");
  }

  public Button getLehrgangNeu()
  {
    return new Button("Neuer Lehrgang", new LehrgangAction(getMitglied()), null,
        false, "file.png");
  }

  public TablePart getMitgliedTable(int atyp, Action detailaction)
      throws RemoteException
  {
    part = new TablePart(new MitgliedQuery(this, false).get(atyp),
        detailaction);
    new MitgliedSpaltenauswahl().setColumns(part, atyp);
    part.setContextMenu(new MitgliedMenu(detailaction));
    part.setMulti(true);
    part.setRememberColWidths(true);
    part.setRememberOrder(true);
    part.setRememberState(true);
    return part;
  }

  public TablePart refreshMitgliedTable(int atyp) throws RemoteException
  {
    if (System.currentTimeMillis() - lastrefresh < 500)
    {
      Logger.debug(String.format("Zeit zwischen den Refreshs: %s",
          (System.currentTimeMillis() - lastrefresh)));
      return part;
    }
    lastrefresh = System.currentTimeMillis();
    saveDefaults();
    part.removeAll();
    ArrayList<Mitglied> mitglieder = new MitgliedQuery(this, false).get(atyp);
    for (Mitglied m : mitglieder)
    {
      part.addItem(m);
    }
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
      settings.setAttribute("status.mitglied",
          (String) getMitgliedStatus().getValue());
    }

    if (geburtsdatumvon != null)
    {
      Date tmp = (Date) getGeburtsdatumvon().getValue();
      if (tmp != null)
      {
        settings.setAttribute("mitglied.geburtsdatumvon",
            new JVDateFormatTTMMJJJJ().format(tmp));
      }
      else
      {
        settings.setAttribute("mitglied.geburtsdatumvon", "");
      }
    }
    if (auswertungUeberschrift != null)
    {
      String tmp = (String) getAuswertungUeberschrift().getValue();
      if (tmp != null)
      {
        settings.setAttribute("auswertung.ueberschrift", tmp);
      }
      else
      {
        settings.setAttribute("auswertung.ueberschrift", "");
      }
    }

    // RWU: vorlagedateicsv
    if (vorlagedateicsv != null)
    {
      String tmp = (String) getVorlagedateicsv().getValue();
      if (tmp != null)
      {
        settings.setAttribute("auswertung.vorlagedateicsv", tmp);
      }
      else
      {
        settings.setAttribute("auswertung.vorlagedateicsv", "");
      }
    }

    if (suchname != null)
    {
      String tmp = (String) getSuchname().getValue();
      if (tmp != null)
      {
        settings.setAttribute("mitglied.suchname", tmp);
      }
      else
      {
        settings.setAttribute("mitglied.suchname", "");
      }

    }

    if (geburtsdatumbis != null)
    {
      Date tmp = (Date) getGeburtsdatumbis().getValue();
      if (tmp != null)
      {
        settings.setAttribute("mitglied.geburtsdatumbis",
            new JVDateFormatTTMMJJJJ().format(tmp));
      }
      else
      {
        settings.setAttribute("mitglied.geburtsdatumbis", "");
      }
    }

    if (sterbedatumvon != null)
    {
      Date tmp = (Date) getSterbedatumvon().getValue();
      if (tmp != null)
      {
        settings.setAttribute("mitglied.sterbedatumvon",
            new JVDateFormatTTMMJJJJ().format(tmp));
      }
      else
      {
        settings.setAttribute("mitglied.sterbedatumvon", "");
      }
    }

    if (sterbedatumbis != null)
    {
      Date tmp = (Date) getSterbedatumbis().getValue();
      if (tmp != null)
      {
        settings.setAttribute("mitglied.sterbedatumbis",
            new JVDateFormatTTMMJJJJ().format(tmp));
      }
      else
      {
        settings.setAttribute("mitglied.sterbedatumbis", "");
      }
    }

    if (eintrittvon != null)
    {
      Date tmp = (Date) getEintrittvon().getValue();
      if (tmp != null)
      {
        settings.setAttribute("mitglied.eintrittvon",
            new JVDateFormatTTMMJJJJ().format(tmp));
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
        settings.setAttribute("mitglied.eintrittbis",
            new JVDateFormatTTMMJJJJ().format(tmp));
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
        settings.setAttribute("mitglied.austrittvon",
            new JVDateFormatTTMMJJJJ().format(tmp));
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
        settings.setAttribute("mitglied.austrittbis",
            new JVDateFormatTTMMJJJJ().format(tmp));
      }
      else
      {
        settings.setAttribute("mitglied.austrittbis", "");
      }
    }
    if (stichtag != null)
    {
      Date tmp = (Date) getStichtag().getValue();
      if (tmp != null)
      {
        settings.setAttribute("mitglied.stichtag",
            new JVDateFormatTTMMJJJJ().format(tmp));
      }
      else
      {
        settings.setAttribute("mitglied.stichtag", "");
      }
    }

    if (eigenschaftenAuswahlTree != null)
    {
      StringBuffer tmp = new StringBuffer();
      for (Object o : eigenschaftenAuswahlTree.getItems())
      {
        EigenschaftenNode node = (EigenschaftenNode) o;
        if (node.getNodeType() == EigenschaftenNode.EIGENSCHAFTEN)
        {
          if (tmp.length() > 0)
          {
            tmp.append(",");
          }
          tmp.append(node.getEigenschaft().getID());
        }
      }
      settings.setAttribute("mitglied.eigenschaften", tmp.toString());
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

  public String getEigenschaftenVerknuepfung()
  {
    return settings.getString("mitglied.eigenschaften.verknuepfung", "und");
  }

  public TreePart getEigenschaftenTree() throws RemoteException
  {
    if (eigenschaftenTree != null)
    {
      return eigenschaftenTree;
    }
    eigenschaftenTree = new TreePart(new EigenschaftenNode(mitglied), null);
    eigenschaftenTree.setCheckable(true);
    eigenschaftenTree
        .addSelectionListener(new EigenschaftListener(eigenschaftenTree));
    eigenschaftenTree.setFormatter(new EigenschaftTreeFormatter());
    return eigenschaftenTree;
  }

  public void handleStore()
  {
    try
    {
      Mitglied m = getMitglied();

      if (eigenschaftenTree != null)
      {
        HashMap<String, Boolean> pflichtgruppen = new HashMap<>();
        DBIterator<EigenschaftGruppe> it = Einstellungen.getDBService()
            .createList(EigenschaftGruppe.class);
        it.addFilter("pflicht = ?", new Object[] { Boolean.TRUE });
        while (it.hasNext())
        {
          EigenschaftGruppe eg = it.next();
          pflichtgruppen.put(eg.getID(), Boolean.valueOf(false));
        }
        for (Object o1 : eigenschaftenTree.getItems())
        {
          if (o1 instanceof EigenschaftenNode)
          {
            EigenschaftenNode node = (EigenschaftenNode) o1;
            if (node.getNodeType() == EigenschaftenNode.EIGENSCHAFTEN)
            {
              Eigenschaft ei = (Eigenschaft) node.getObject();
              pflichtgruppen.put(ei.getEigenschaftGruppeId() + "",
                  Boolean.valueOf(true));
            }
          }
        }
        for (String key : pflichtgruppen.keySet())
        {
          if (!pflichtgruppen.get(key))
          {
            EigenschaftGruppe eg = (EigenschaftGruppe) Einstellungen
                .getDBService().createObject(EigenschaftGruppe.class, key);
            throw new ApplicationException(String.format(
                "In der Eigenschaftengruppe \"%s\" fehlt ein Eintrag!",
                eg.getBezeichnung()));
          }
        }
        // Max eine Eigenschaft pro Gruppe
        HashMap<String, Boolean> max1gruppen = new HashMap<>();
        it = Einstellungen.getDBService().createList(EigenschaftGruppe.class);
        it.addFilter("max1 = ?", new Object[] { Boolean.TRUE });
        while (it.hasNext())
        {
          EigenschaftGruppe eg = it.next();
          max1gruppen.put(eg.getID(), Boolean.valueOf(false));
        }
        for (Object o1 : eigenschaftenTree.getItems())
        {
          if (o1 instanceof EigenschaftenNode)
          {
            EigenschaftenNode node = (EigenschaftenNode) o1;
            if (node.getNodeType() == EigenschaftenNode.EIGENSCHAFTEN)
            {
              Eigenschaft ei = (Eigenschaft) node.getObject();
              Boolean m1 = max1gruppen.get(ei.getEigenschaftGruppe().getID());
              if (m1 != null)
              {
                if (m1)
                {
                  throw new ApplicationException(String.format(
                      "In der Eigenschaftengruppe '%s' mehr als ein Eintrag markiert!",
                      ei.getEigenschaftGruppe().getBezeichnung()));
                }
                else
                {
                  max1gruppen.put(ei.getEigenschaftGruppe().getID(),
                      Boolean.valueOf(true));
                }
              }
            }
          }
        }

      }

      if (adresstyp != null)
      {
        Adresstyp at = (Adresstyp) getAdresstyp().getValue();
        m.setAdresstyp(new Integer(at.getID()));
      }
      else
      {
        m.setAdresstyp(1);
      }
      m.setAdressierungszusatz((String) getAdressierungszusatz().getValue());
      m.setAustritt((Date) getAustritt().getValue());
      m.setAnrede((String) getAnrede().getValue());
      GenericObject o = (GenericObject) getBeitragsgruppe(true).getValue();
      if (adresstyp == null)
      {
        try
        {
          Beitragsgruppe bg = (Beitragsgruppe) o;
          m.setBeitragsgruppe(new Integer(bg.getID()));
          if (bg.getBeitragsArt() != ArtBeitragsart.FAMILIE_ANGEHOERIGER)
          {
            m.setZahlerID(null);
          }
        }
        catch (NullPointerException e)
        {
          throw new ApplicationException("Beitragsgruppe fehlt");
        }
      }
      if (Einstellungen.getEinstellung().getIndividuelleBeitraege())
      {
        if (getIndividuellerBeitrag().getValue() != null)
        {
          m.setIndividuellerBeitrag(
              (Double) getIndividuellerBeitrag().getValue());
        }
        else
        {
          m.setIndividuellerBeitrag(null);
        }
      }
      Zahlungsweg zw = (Zahlungsweg) getZahlungsweg().getValue();
      m.setZahlungsweg(zw.getKey());
      Zahlungsrhythmus zr = (Zahlungsrhythmus) getZahlungsrhythmus().getValue();
      m.setZahlungsrhythmus(zr.getKey());
      Zahlungstermin zt = (Zahlungstermin) getZahlungstermin().getValue();
      if (zt != null)
      {
        m.setZahlungstermin(zt.getKey());
      }
      m.setMandatDatum((Date) getMandatDatum().getValue());
      m.setMandatVersion((Integer) getMandatVersion().getValue());
      m.setMandatSequence((MandatSequence) getMandatSequence().getValue());
      m.setBic((String) getBic().getValue());
      m.setIban((String) getIban().getValue());
      m.setEintritt((Date) getEintritt().getValue());
      m.setEmail((String) getEmail().getValue());
      if (Einstellungen.getEinstellung().getExterneMitgliedsnummer())
      {
        if (externemitgliedsnummer != null)
        {
          m.setExterneMitgliedsnummer(
              (String) getExterneMitgliedsnummer().getValue());
        }
      }
      else
      {
        m.setExterneMitgliedsnummer(null);
      }

      if (m.getPersonenart().equals("n"))
      {
        m.setGeburtsdatum((Date) getGeburtsdatum().getValue());
        if (getGeschlecht().getSelectedValue() == null)
        {
          throw new ApplicationException("Bitte Geschlecht auswählen!");
        }

        m.setGeschlecht((String) getGeschlecht().getValue());
      }
      m.setKtoiAdressierungszusatz(
          (String) getKtoiAdressierungszusatz().getValue());
      m.setKtoiAnrede((String) getKtoiAnrede().getValue());
      m.setKtoiEmail((String) getKtoiEmail().getValue());
      m.setKtoiName((String) getKtoiName().getValue());
      m.setKtoiOrt((String) getKtoiOrt().getValue());
      String persa = (String) getKtoiPersonenart().getValue();
      m.setKtoiPersonenart(persa.substring(0, 1));
      m.setKtoiPlz((String) getKtoiPlz().getValue());
      m.setKtoiStaat((String) getKtoiStaat().getValue());
      m.setKtoiStrasse((String) getKtoiStrasse().getValue());
      m.setKtoiTitel((String) getKtoiTitel().getValue());
      m.setKtoiVorname((String) getKtoiVorname().getValue());
      m.setKtoiGeschlecht((String) getKtoiGeschlecht().getValue());
      m.setKuendigung((Date) getKuendigung().getValue());
      m.setSterbetag((Date) getSterbetag().getValue());
      m.setName((String) getName(false).getValue());
      m.setOrt((String) getOrt().getValue());
      m.setPlz((String) getPlz().getValue());
      m.setStaat((String) getStaat().getValue());
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
      m.setLetzteAenderung();
      m.store();

      boolean ist_mitglied = m.getAdresstyp().getJVereinid() == 1;
      if (Einstellungen.getEinstellung().getMitgliedfoto() && ist_mitglied)
      {
        Mitgliedfoto f = null;
        DBIterator<Mitgliedfoto> it = Einstellungen.getDBService()
            .createList(Mitgliedfoto.class);
        it.addFilter("mitglied = ?", new Object[] { m.getID() });
        if (it.size() > 0)
        {
          f = it.next();
          if (foto == null)
          {
            f.delete();
          }
          else
          {
            f.setFoto((byte[]) foto.getValue());
            f.store();
          }
        }
        else
        {
          f = (Mitgliedfoto) Einstellungen.getDBService()
              .createObject(Mitgliedfoto.class, null);
          f.setMitglied(m);
          f.setFoto((byte[]) foto.getValue());
          f.store();
        }
      }
      if (eigenschaftenTree != null)
      {
        if (!getMitglied().isNewObject())
        {
          DBIterator<Eigenschaften> it = Einstellungen.getDBService()
              .createList(Eigenschaften.class);
          it.addFilter("mitglied = ?", new Object[] { getMitglied().getID() });
          while (it.hasNext())
          {
            Eigenschaften ei = it.next();
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
          DBIterator<Felddefinition> it0 = Einstellungen.getDBService()
              .createList(Felddefinition.class);
          it0.addFilter("label = ?", new Object[] { ti.getName() });
          Felddefinition fd = it0.next();
          // Ist bereits ein Datensatz für diese Definiton vorhanden ?
          DBIterator<Zusatzfelder> it = Einstellungen.getDBService()
              .createList(Zusatzfelder.class);
          it.addFilter("mitglied =?", new Object[] { m.getID() });
          it.addFilter("felddefinition=?", new Object[] { fd.getID() });
          Zusatzfelder zf = null;
          if (it.size() > 0)
          {
            zf = it.next();
          }
          else
          {
            zf = (Zusatzfelder) Einstellungen.getDBService()
                .createObject(Zusatzfelder.class, null);
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
      if (Einstellungen.getEinstellung().getSekundaereBeitragsgruppen()
          && ist_mitglied)
      {
        // Schritt 1: Die selektierten sekundären Beitragsgruppe prüfen, ob sie
        // bereits gespeichert sind. Ggfls. speichern.
        @SuppressWarnings("rawtypes")
        List items = sekundaerebeitragsgruppe.getItems();
        for (Object o1 : items)
        {
          SekundaereBeitragsgruppe sb = (SekundaereBeitragsgruppe) o1;
          if (sb.isNewObject())
          {
            sb.store();
          }
        }
        // Schritt 2: Die sekundären Beitragsgruppe in der Liste, die nicht mehr
        // selektiert sind, müssen gelöscht werden.
        for (SekundaereBeitragsgruppe sb : listeSeB)
        {
          if (!sb.isNewObject() && !items.contains(sb))
          {
            sb.delete();
          }
        }
      }
      String successtext = "";
      if (m.getAdresstyp().getJVereinid() == 1)
      {
        successtext = "Mitglied gespeichert";
      }
      else
      {
        successtext = "Adresse gespeichert";
      }
      GUI.getStatusBar().setSuccessText(successtext);
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

  public TreePart getEigenschaftenAuswahlTree(String vorbelegung,
      boolean ohnePflicht) throws RemoteException
  {
    eigenschaftenAuswahlTree = new TreePart(
        new EigenschaftenNode(vorbelegung, ohnePflicht), null);
    eigenschaftenAuswahlTree.setCheckable(true);
    eigenschaftenAuswahlTree.addSelectionListener(
        new EigenschaftListener(eigenschaftenAuswahlTree));
    eigenschaftenAuswahlTree.setFormatter(new EigenschaftTreeFormatter());
    return eigenschaftenAuswahlTree;
  }

  public TreePart getFamilienbeitraegeTree() throws RemoteException
  {
    familienbeitragtree = new TreePart(new FamilienbeitragNode(),
        new MitgliedDetailAction());
    familienbeitragtree.addColumn("Name", "name");
    familienbeitragtree.setContextMenu(new FamilienbeitragMenu());
    familienbeitragtree.setRememberColWidths(true);
    familienbeitragtree.setRememberOrder(true);
    this.fbc = new FamilienbeitragMessageConsumer();
    Application.getMessagingFactory().registerMessageConsumer(this.fbc);
    return familienbeitragtree;
  }

  private void starteAuswertung() throws RemoteException
  {
    final IAuswertung ausw = (IAuswertung) getAusgabe().getValue();
    saveDefaults();
    ArrayList<Mitglied> list = null;
    list = new MitgliedQuery(this, true).get(1);
    try
    {
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

      String path = settings.getString("lastdir",
          System.getProperty("user.home"));
      if (path != null && path.length() > 0)
      {
        fd.setFilterPath(path);
      }
      fd.setFileName(new Dateiname("auswertung", dateinamensort,
          Einstellungen.getEinstellung().getDateinamenmuster(),
          ausw.getDateiendung()).get());
      fd.setFilterExtensions(new String[] { "*." + ausw.getDateiendung() });

      String s = fd.open();
      if (s == null || s.length() == 0)
      {
        return;
      }
      if (!s.endsWith(ausw.getDateiendung()))
      {
        s = s + "." + ausw.getDateiendung();
      }
      final File file = new File(s);
      settings.setAttribute("lastdir", file.getParent());

      final ArrayList<Mitglied> flist = list;
      ausw.beforeGo();
      BackgroundTask t = new BackgroundTask()
      {

        @Override
        public void run(ProgressMonitor monitor) throws ApplicationException
        {
          try
          {
            ausw.go(flist, file);
            GUI.getCurrentView().reload();
            if (ausw.openFile())
            {
              FileViewer.show(file);
            }
          }
          catch (ApplicationException ae)
          {
            Logger.error("Fehler", ae);
            GUI.getStatusBar().setErrorText(ae.getMessage());
            throw ae;
          }
          catch (Exception re)
          {
            Logger.error("Fehler", re);
            GUI.getStatusBar().setErrorText(re.getMessage());
            throw new ApplicationException(re);
          }
        }

        @Override
        public void interrupt()
        {
          //
        }

        @Override
        public boolean isInterrupted()
        {
          return false;
        }
      };
      Application.getController().start(t);
    }
    catch (RemoteException e)
    {
      Logger.error("Fehler", e);
    }
  }

  private void starteAdressAuswertung() throws RemoteException
  {
    final IAuswertung ausw = (IAuswertung) getAusgabe().getValue();
    saveDefaults();
    ArrayList<Mitglied> list = null;
    Adresstyp atyp = (Adresstyp) getAdresstyp().getValue();
    list = new MitgliedQuery(this, true).get(Integer.parseInt(atyp.getID()));
    try
    {
      FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
      fd.setText("Ausgabedatei wählen.");

      String path = settings.getString("lastdir",
          System.getProperty("user.home"));
      if (path != null && path.length() > 0)
      {
        fd.setFilterPath(path);
      }
      fd.setFileName(new Dateiname("adressauswertung", "",
          Einstellungen.getEinstellung().getDateinamenmuster(),
          ausw.getDateiendung()).get());
      fd.setFilterExtensions(new String[] { "*." + ausw.getDateiendung() });

      String s = fd.open();
      if (s == null || s.length() == 0)
      {
        return;
      }
      if (!s.endsWith(ausw.getDateiendung()))
      {
        s = s + "." + ausw.getDateiendung();
      }
      final File file = new File(s);
      settings.setAttribute("lastdir", file.getParent());

      final ArrayList<Mitglied> flist = list;
      ausw.beforeGo();
      BackgroundTask t = new BackgroundTask()
      {
        @Override
        public void run(ProgressMonitor monitor) throws ApplicationException
        {
          try
          {
            ausw.go(flist, file);
            GUI.getCurrentView().reload();
            if (ausw.openFile())
            {
              FileViewer.show(file);
            }
          }
          catch (Exception re)
          {
            Logger.error("Fehler", re);
            GUI.getStatusBar().setErrorText(re.getMessage());
            throw new ApplicationException(re);
          }
        }

        @Override
        public void interrupt()
        {
          //
        }

        @Override
        public boolean isInterrupted()
        {
          return false;
        }
      };
      Application.getController().start(t);
    }
    catch (RemoteException e)
    {
      Logger.error("Fehler", e);
    }
  }

  public Settings getSettings()
  {
    return settings;
  }

  private void starteStatistik() throws RemoteException
  {
    FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
    fd.setText("Ausgabedatei wählen.");
    fd.setFilterExtensions(new String[] { "*.PDF" });
    String path = settings.getString("lastdir",
        System.getProperty("user.home"));
    if (path != null && path.length() > 0)
    {
      fd.setFilterPath(path);
    }
    fd.setFileName(new Dateiname("statistik", "",
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

    final Date sticht = (Date) stichtag.getValue();

    BackgroundTask t = new BackgroundTask()
    {

      @Override
      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        new MitgliederStatistik(file, sticht);
      }

      @Override
      public void interrupt()
      {
        //
      }

      @Override
      public boolean isInterrupted()
      {
        return false;
      }
    };
    Application.getController().start(t);

  }

  public void setZusatzfelderAuswahl()
  {
    int selected = settings.getInt("zusatzfelder.selected", 0);
    if (selected == 0)
    {
      zusatzfelderabfrage.setText("kein Feld ausgewählt");
    }
    else if (selected == 1)
    {
      zusatzfelderabfrage.setText("1 Feld ausgewählt");
    }
    else
    {
      zusatzfelderabfrage
          .setText(String.format("%d Felder ausgewählt", selected));
    }
  }

  /**
   * Listener, der die Auswahl der Eigenschaften ueberwacht.
   */
  private class EigenschaftenListener implements Listener
  {

    @Override
    public void handleEvent(Event event)
    {
      if (event == null || event.data == null)
      {
        return;
      }
      EigenschaftenAuswahlParameter param = (EigenschaftenAuswahlParameter) event.data;
      StringBuilder id = new StringBuilder();
      StringBuilder text = new StringBuilder();
      for (Object o : param.getEigenschaften())
      {
        if (text.length() > 0)
        {
          id.append(",");
          text.append(", ");
        }
        EigenschaftenNode node = (EigenschaftenNode) o;
        try
        {
          id.append(node.getEigenschaft().getID());
          text.append(node.getEigenschaft().getBezeichnung());
        }
        catch (RemoteException e)
        {
          Logger.error("Fehler", e);
        }
      }
      eigenschaftenabfrage.setText(text.toString());
      settings.setAttribute("mitglied.eigenschaften", id.toString());
      settings.setAttribute("mitglied.eigenschaften.verknuepfung",
          param.getVerknuepfung());
    }
  }

  /**
   * Listener, der die Auswahl der Zusatzfelder ueberwacht.
   */
  private class ZusatzfelderListener implements Listener
  {

    @Override
    public void handleEvent(Event event)
    {
      setZusatzfelderAuswahl();
    }
  }

  public static class EigenschaftTreeFormatter implements TreeFormatter
  {

    @Override
    public void format(TreeItem item)
    {
      EigenschaftenNode eigenschaftitem = (EigenschaftenNode) item.getData();
      if (eigenschaftitem.getNodeType() == EigenschaftenNode.ROOT
          || eigenschaftitem
              .getNodeType() == EigenschaftenNode.EIGENSCHAFTGRUPPE)
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

  static class EigenschaftListener implements Listener
  {

    private TreePart tree;

    public EigenschaftListener(TreePart tree)
    {
      this.tree = tree;
    }

    @Override
    public void handleEvent(Event event)
    {
      // "o" ist das Objekt, welches gerade markiert
      // wurde oder die Checkbox geaendert wurde.
      GenericObjectNode o = (GenericObjectNode) event.data;

      // Da der Listener sowohl dann aufgerufen wird,j
      // nur nur eine Zeile selektiert wurde als auch,
      // wenn die Checkbox geaendert wurde, musst du jetzt
      // noch ersteres ausfiltern - die Checkboxen sollen
      // ja nicht geaendert werden, wenn nur eine Zeile
      // selektiert aber die Checkbox nicht geaendert wurde.
      // Hierzu schreibe ich in event.detail einen Int-Wert.
      // event.detail = -1 // Nur selektiert
      // event.detail = 1 // Checkbox aktiviert
      // event.detail = 0 // Checkbox deaktiviert

      // Folgende Abfrage deaktiviert wegen Problemen mit Windows
      // if (event.detail == -1)
      // {
      // return;
      // }
      try
      {
        if (o.getChildren() == null)
        {
          return;
        }
        List<?> children = PseudoIterator.asList(o.getChildren());
        boolean b = event.detail > 0;
        tree.setChecked(children.toArray(new Object[children.size()]), b);
      }
      catch (RemoteException e)
      {
        Logger.error("Fehler", e);
      }
    }
  }

  /**
   * Wird benachrichtigt um die Anzeige zu aktualisieren.
   */
  private class FamilienbeitragMessageConsumer implements MessageConsumer
  {

    /**
     * @see de.willuhn.jameica.messaging.MessageConsumer#autoRegister()
     */
    @Override
    public boolean autoRegister()
    {
      return false;
    }

    /**
     * @see de.willuhn.jameica.messaging.MessageConsumer#getExpectedMessageTypes()
     */
    @Override
    public Class<?>[] getExpectedMessageTypes()
    {
      return new Class[] { FamilienbeitragMessage.class };
    }

    /**
     * @see de.willuhn.jameica.messaging.MessageConsumer#handleMessage(de.willuhn.jameica.messaging.Message)
     */
    @Override
    public void handleMessage(final Message message) throws Exception
    {
      GUI.getDisplay().syncExec(new Runnable()
      {

        @Override
        public void run()
        {
          try
          {
            if (familienbeitragtree == null)
            {
              // Eingabe-Feld existiert nicht. Also abmelden
              Application.getMessagingFactory().unRegisterMessageConsumer(
                  FamilienbeitragMessageConsumer.this);
              return;
            }
            familienbeitragtree.setRootObject(new FamilienbeitragNode());
          }
          catch (Exception e)
          {
            // Wenn hier ein Fehler auftrat, deregistrieren wir uns wieder
            Logger.error("unable to refresh saldo", e);
            Application.getMessagingFactory()
                .unRegisterMessageConsumer(FamilienbeitragMessageConsumer.this);
          }
        }
      });
    }
  }

  public Part getMitgliedBeitraegeTabelle() throws RemoteException
  {
    if (beitragsTabelle != null)
    {
      beitragsTabelle.setContextMenu(new MitgliedNextBGruppeMenue(this));
      return beitragsTabelle;
    }

    beitragsTabelle = new TablePart(new MitgliedNextBGruppeBearbeitenAction());
    beitragsTabelle.setRememberColWidths(true);
    beitragsTabelle.setRememberOrder(true);
    beitragsTabelle.setContextMenu(new MitgliedNextBGruppeMenue(this));
    beitragsTabelle.addColumn("Ab Datum", MitgliedNextBGruppe.COL_AB_DATUM,
        new DateFormatter(new JVDateFormatTTMMJJJJ()));
    beitragsTabelle.addColumn("Beitragsgruppe",
        MitgliedNextBGruppe.VIEW_BEITRAGSGRUPPE);
    beitragsTabelle.addColumn("Bemerkung", MitgliedNextBGruppe.COL_BEMERKUNG);
    refreshMitgliedBeitraegeTabelle();
    return beitragsTabelle;
  }

  public void refreshMitgliedBeitraegeTabelle() throws RemoteException
  {
    if (beitragsTabelle == null)
      return;
    beitragsTabelle.removeAll();

    DBService service = Einstellungen.getDBService();
    DBIterator<MitgliedNextBGruppe> datenIterator = service
        .createList(MitgliedNextBGruppe.class);
    datenIterator.addFilter(MitgliedNextBGruppe.COL_MITGLIED + " = ? ",
        getMitglied().getID());
    datenIterator.setOrder("order by " + MitgliedNextBGruppe.COL_AB_DATUM);

    while (datenIterator.hasNext())
    {
      MitgliedNextBGruppe m = datenIterator.next();
      beitragsTabelle.addItem(m);
    }
  }
}
