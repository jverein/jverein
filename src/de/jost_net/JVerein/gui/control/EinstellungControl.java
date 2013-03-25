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
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.Date;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.control.listener.IBANListener;
import de.jost_net.JVerein.gui.dialogs.BankverbindungDialogButton;
import de.jost_net.JVerein.gui.input.BICInput;
import de.jost_net.JVerein.gui.input.DtausTextschluesselInput;
import de.jost_net.JVerein.gui.input.IBANInput;
import de.jost_net.JVerein.gui.input.SEPALandInput;
import de.jost_net.JVerein.gui.input.SEPALandObject;
import de.jost_net.JVerein.keys.Beitragsmodel;
import de.jost_net.JVerein.keys.Zahlungsrhytmus;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Einstellung;
import de.jost_net.JVerein.util.MitgliedSpaltenauswahl;
import de.jost_net.OBanToo.SEPA.SEPAException;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.DecimalInput;
import de.willuhn.jameica.gui.input.DirectoryInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.IntegerInput;
import de.willuhn.jameica.gui.input.PasswordInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class EinstellungControl extends AbstractControl
{

  private Input name;

  private Input namelang;

  private Input strasse;

  private Input plz;

  private Input ort;

  private TextInput finanzamt;

  private TextInput steuernummer;

  private DateInput bescheiddatum;

  private CheckboxInput vorlaeufig;

  private DateInput vorlaeufigab;

  private TextInput beguenstigterzweck;

  private CheckboxInput mitgliedsbetraege;

  private TextInput bic;

  private IBANInput iban;

  private TextInput glaeubigerid;

  private TextInput blz;

  private TextInput konto;

  private CheckboxInput geburtsdatumpflicht;

  private CheckboxInput eintrittsdatumpflicht;

  private CheckboxInput sterbedatum;

  private CheckboxInput kommunikationsdaten;

  private CheckboxInput zusatzbetrag;

  private CheckboxInput vermerke;

  private CheckboxInput wiedervorlage;

  private CheckboxInput kursteilnehmer;

  private CheckboxInput lehrgaenge;

  private CheckboxInput juristischepersonen;

  private CheckboxInput mitgliedskonto;

  private CheckboxInput mitgliedfoto;

  private CheckboxInput uselesefelder;

  private CheckboxInput zusatzadressen;

  private CheckboxInput auslandsadressen;

  private CheckboxInput arbeitseinsatz;

  private CheckboxInput dokumentenspeicherung;

  private CheckboxInput individuellebeitraege;

  private TextInput rechnungtextabbuchung;

  private TextInput rechnungtextueberweisung;

  private TextInput rechnungtextbar;

  private CheckboxInput externemitgliedsnummer;

  private SelectInput beitragsmodel;

  private TextInput dateinamenmuster;

  private TextInput dateinamenmusterspende;

  private DecimalInput spendenbescheinigungminbetrag;

  private DirectoryInput spendenbescheinigungverzeichnis;

  private CheckboxInput spendenbescheinigungprintbuchungsart;

  private TextInput beginngeschaeftsjahr;

  private TextInput smtp_server;

  private IntegerInput smtp_port;

  private TextInput smtp_auth_user;

  private PasswordInput smtp_auth_pwd;

  private TextInput smtp_from_address;

  private TextInput smtp_from_anzeigename;

  private CheckboxInput smtp_ssl;

  private CheckboxInput smtp_starttls;

  private SelectInput zahlungsweg;

  private SelectInput zahlungsrhytmus;

  private SelectInput dtaustextschluessel;

  private SelectInput sepaland;

  private Input altersgruppen;

  private Input jubilaeen;

  private Input altersjubilaeen;

  private IntegerInput jubilarStartAlter;

  private IntegerInput delaytime;

  private Settings settings;

  private MitgliedSpaltenauswahl spalten;

  private IntegerInput AnzahlSpaltenStammdatenInput;

  private IntegerInput AnzahlSpaltenZusatzfelderInput;

  private IntegerInput AnzahlSpaltenLesefelderInput;

  private IntegerInput AnzahlSpaltenMitgliedschaftInput;

  private IntegerInput AnzahlSpaltenZahlungInput;

  private CheckboxInput ZeigeStammdatenInTabInput;

  private CheckboxInput ZeigeMitgliedschaftInTabInput;

  private CheckboxInput ZeigeZahlungInTabInput;

  private CheckboxInput ZeigeZusatzbeitraegeInTabInput;

  private CheckboxInput ZeigeMitgliedskontoInTabInput;

  private CheckboxInput ZeigeVermerkeInTabInput;

  private CheckboxInput ZeigeWiedervorlageInTabInput;

  private CheckboxInput ZeigeMailsInTabInput;

  private CheckboxInput ZeigeEigenschaftenInTabInput;

  private CheckboxInput ZeigeZusatzfelderInTabInput;

  private CheckboxInput ZeigeLehrgaengeInTabInput;

  private CheckboxInput ZeigeFotoInTabInput;

  private CheckboxInput ZeigeLesefelderInTabInput;

  private CheckboxInput ZeigeArbeitseinsatzInTabInput;

  public EinstellungControl(AbstractView view)
  {
    super(view);
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public Einstellung getEinstellung()
  {
    return Einstellungen.getEinstellung();
  }

  public Input getName(boolean withFocus) throws RemoteException
  {
    if (name != null)
    {
      return name;
    }
    name = new TextInput(Einstellungen.getEinstellung().getName(), 27);
    name.setMandatory(true);
    if (withFocus)
    {
      name.focus();
    }
    return name;
  }

  public Input getNameLang() throws RemoteException
  {
    if (namelang != null)
    {
      return namelang;
    }
    namelang = new TextInput(Einstellungen.getEinstellung().getNameLang(), 100);
    return namelang;
  }

  public Input getStrasse() throws RemoteException
  {
    if (strasse != null)
    {
      return strasse;
    }
    strasse = new TextInput(Einstellungen.getEinstellung().getStrasse(), 50);
    return strasse;
  }

  public Input getPlz() throws RemoteException
  {
    if (plz != null)
    {
      return plz;
    }
    plz = new TextInput(Einstellungen.getEinstellung().getPlz(), 5);
    return plz;
  }

  public Input getOrt() throws RemoteException
  {
    if (ort != null)
    {
      return ort;
    }
    ort = new TextInput(Einstellungen.getEinstellung().getOrt(), 50);
    return ort;
  }

  public TextInput getFinanzamt() throws RemoteException
  {
    if (finanzamt != null)
    {
      return finanzamt;
    }
    finanzamt = new TextInput(Einstellungen.getEinstellung().getFinanzamt(), 30);
    return finanzamt;
  }

  public TextInput getSteuernummer() throws RemoteException
  {
    if (steuernummer != null)
    {
      return steuernummer;
    }
    steuernummer = new TextInput(Einstellungen.getEinstellung()
        .getSteuernummer(), 30);
    return steuernummer;
  }

  public DateInput getBescheiddatum() throws RemoteException
  {
    if (bescheiddatum != null)
    {
      return bescheiddatum;
    }
    bescheiddatum = new DateInput(Einstellungen.getEinstellung()
        .getBescheiddatum());
    return bescheiddatum;
  }

  public CheckboxInput getVorlaeufig() throws RemoteException
  {
    if (vorlaeufig != null)
    {
      return vorlaeufig;
    }
    vorlaeufig = new CheckboxInput(Einstellungen.getEinstellung()
        .getVorlaeufig());
    return vorlaeufig;
  }

  public DateInput getVorlaeufigab() throws RemoteException
  {
    if (vorlaeufigab != null)
    {
      return vorlaeufigab;
    }
    vorlaeufigab = new DateInput(Einstellungen.getEinstellung()
        .getVorlaeufigab());
    return vorlaeufigab;
  }

  public TextInput getBeguenstigterzweck() throws RemoteException
  {
    if (beguenstigterzweck != null)
    {
      return beguenstigterzweck;
    }
    beguenstigterzweck = new TextInput(Einstellungen.getEinstellung()
        .getBeguenstigterzweck(), 100);
    return beguenstigterzweck;
  }

  public CheckboxInput getMitgliedsbetraege() throws RemoteException
  {
    if (mitgliedsbetraege != null)
    {
      return mitgliedsbetraege;
    }
    mitgliedsbetraege = new CheckboxInput(Einstellungen.getEinstellung()
        .getMitgliedsbetraege());
    return mitgliedsbetraege;
  }

  public TextInput getBic() throws RemoteException
  {
    if (bic != null && !bic.getControl().isDisposed())
    {
      return bic;
    }
    bic = new BICInput(Einstellungen.getEinstellung().getBic());
    return bic;
  }

  public IBANInput getIban() throws RemoteException
  {
    if (iban != null)
    {
      return iban;
    }
    iban = new IBANInput(Einstellungen.getEinstellung().getIban());
    IBANListener l = new IBANListener(iban);
    iban.addListener(l);
    l.handleEvent(null);
    return iban;
  }

  public TextInput getGlaeubigerID() throws RemoteException
  {
    if (glaeubigerid != null)
    {
      return glaeubigerid;
    }
    glaeubigerid = new TextInput(Einstellungen.getEinstellung()
        .getGlaeubigerID(), 35);
    return glaeubigerid;
  }

  public TextInput getBlz() throws RemoteException
  {
    if (blz != null)
    {
      return blz;
    }
    blz = new TextInput(Einstellungen.getEinstellung().getBlz(), 8);
    blz.setMandatory(true);
    BLZListener l = new BLZListener();

    blz.addListener(l);
    l.handleEvent(null);
    return blz;
  }

  public TextInput getKonto() throws RemoteException
  {
    if (konto != null)
    {
      return konto;
    }
    konto = new TextInput(Einstellungen.getEinstellung().getKonto(), 12);
    konto.setMandatory(true);
    konto.setComment(JVereinPlugin.getI18n().tr("für die Abbuchung"));
    return konto;
  }

  public CheckboxInput getGeburtsdatumPflicht() throws RemoteException
  {
    if (geburtsdatumpflicht != null)
    {
      return geburtsdatumpflicht;
    }
    geburtsdatumpflicht = new CheckboxInput(Einstellungen.getEinstellung()
        .getGeburtsdatumPflicht());
    return geburtsdatumpflicht;
  }

  public CheckboxInput getEintrittsdatumPflicht() throws RemoteException
  {
    if (eintrittsdatumpflicht != null)
    {
      return eintrittsdatumpflicht;
    }
    eintrittsdatumpflicht = new CheckboxInput(Einstellungen.getEinstellung()
        .getEintrittsdatumPflicht());
    return eintrittsdatumpflicht;
  }

  public CheckboxInput getSterbedatum() throws RemoteException
  {
    if (sterbedatum != null)
    {
      return sterbedatum;
    }
    sterbedatum = new CheckboxInput(Einstellungen.getEinstellung()
        .getSterbedatum());
    return sterbedatum;
  }

  public CheckboxInput getKommunikationsdaten() throws RemoteException
  {
    if (kommunikationsdaten != null)
    {
      return kommunikationsdaten;
    }
    kommunikationsdaten = new CheckboxInput(Einstellungen.getEinstellung()
        .getKommunikationsdaten());
    return kommunikationsdaten;
  }

  public CheckboxInput getZusatzbetrag() throws RemoteException
  {
    if (zusatzbetrag != null)
    {
      return zusatzbetrag;
    }
    zusatzbetrag = new CheckboxInput(Einstellungen.getEinstellung()
        .getZusatzbetrag());
    return zusatzbetrag;
  }

  public CheckboxInput getVermerke() throws RemoteException
  {
    if (vermerke != null)
    {
      return vermerke;
    }
    vermerke = new CheckboxInput(Einstellungen.getEinstellung().getVermerke());
    return vermerke;
  }

  public CheckboxInput getWiedervorlage() throws RemoteException
  {
    if (wiedervorlage != null)
    {
      return wiedervorlage;
    }
    wiedervorlage = new CheckboxInput(Einstellungen.getEinstellung()
        .getWiedervorlage());
    return wiedervorlage;
  }

  public CheckboxInput getKursteilnehmer() throws RemoteException
  {
    if (kursteilnehmer != null)
    {
      return kursteilnehmer;
    }
    kursteilnehmer = new CheckboxInput(Einstellungen.getEinstellung()
        .getKursteilnehmer());
    return kursteilnehmer;
  }

  public CheckboxInput getLehrgaenge() throws RemoteException
  {
    if (lehrgaenge != null)
    {
      return lehrgaenge;
    }
    lehrgaenge = new CheckboxInput(Einstellungen.getEinstellung()
        .getLehrgaenge());
    return lehrgaenge;
  }

  public CheckboxInput getJuristischePersonen() throws RemoteException
  {
    if (juristischepersonen != null)
    {
      return juristischepersonen;
    }
    juristischepersonen = new CheckboxInput(Einstellungen.getEinstellung()
        .getJuristischePersonen());
    return juristischepersonen;
  }

  public CheckboxInput getMitgliedskonto() throws RemoteException
  {
    if (mitgliedskonto != null)
    {
      return mitgliedskonto;
    }
    mitgliedskonto = new CheckboxInput(Einstellungen.getEinstellung()
        .getMitgliedskonto());
    return mitgliedskonto;
  }

  public CheckboxInput getMitgliedfoto() throws RemoteException
  {
    if (mitgliedfoto != null)
    {
      return mitgliedfoto;
    }
    mitgliedfoto = new CheckboxInput(Einstellungen.getEinstellung()
        .getMitgliedfoto());
    return mitgliedfoto;
  }

  public CheckboxInput getUseLesefelder() throws RemoteException
  {
    if (uselesefelder == null)
    {
      uselesefelder = new CheckboxInput(Einstellungen.getEinstellung()
          .getUseLesefelder());
    }
    return uselesefelder;
  }

  public CheckboxInput getZusatzadressen() throws RemoteException
  {
    if (zusatzadressen != null)
    {
      return zusatzadressen;
    }
    zusatzadressen = new CheckboxInput(Einstellungen.getEinstellung()
        .getZusatzadressen());
    return zusatzadressen;
  }

  public CheckboxInput getAuslandsadressen() throws RemoteException
  {
    if (auslandsadressen != null)
    {
      return auslandsadressen;
    }
    auslandsadressen = new CheckboxInput(Einstellungen.getEinstellung()
        .getAuslandsadressen());
    return auslandsadressen;
  }

  public CheckboxInput getArbeitseinsatz() throws RemoteException
  {
    if (arbeitseinsatz != null)
    {
      return arbeitseinsatz;
    }
    arbeitseinsatz = new CheckboxInput(Einstellungen.getEinstellung()
        .getArbeitseinsatz());
    return arbeitseinsatz;
  }

  public CheckboxInput getDokumentenspeicherung() throws RemoteException
  {
    if (dokumentenspeicherung != null)
    {
      return dokumentenspeicherung;
    }
    dokumentenspeicherung = new CheckboxInput(Einstellungen.getEinstellung()
        .getDokumentenspeicherung());
    return dokumentenspeicherung;
  }

  public CheckboxInput getIndividuelleBeitraege() throws RemoteException
  {
    if (individuellebeitraege != null)
    {
      return individuellebeitraege;
    }
    individuellebeitraege = new CheckboxInput(Einstellungen.getEinstellung()
        .getIndividuelleBeitraege());
    return individuellebeitraege;
  }

  public TextInput getRechnungTextAbbuchung() throws RemoteException
  {
    if (rechnungtextabbuchung != null)
    {
      return rechnungtextabbuchung;
    }
    rechnungtextabbuchung = new TextInput(Einstellungen.getEinstellung()
        .getRechnungTextAbbuchung(), 100);
    return rechnungtextabbuchung;
  }

  public TextInput getRechnungTextUeberweisung() throws RemoteException
  {
    if (rechnungtextueberweisung != null)
    {
      return rechnungtextueberweisung;
    }
    rechnungtextueberweisung = new TextInput(Einstellungen.getEinstellung()
        .getRechnungTextUeberweisung(), 100);
    return rechnungtextueberweisung;
  }

  public TextInput getRechnungTextBar() throws RemoteException
  {
    if (rechnungtextbar != null)
    {
      return rechnungtextbar;
    }
    rechnungtextbar = new TextInput(Einstellungen.getEinstellung()
        .getRechnungTextBar(), 100);
    return rechnungtextbar;
  }

  public CheckboxInput getExterneMitgliedsnummer() throws RemoteException
  {
    if (externemitgliedsnummer != null)
    {
      return externemitgliedsnummer;
    }
    externemitgliedsnummer = new CheckboxInput(Einstellungen.getEinstellung()
        .getExterneMitgliedsnummer());
    return externemitgliedsnummer;
  }

  public SelectInput getBeitragsmodel() throws RemoteException
  {
    if (beitragsmodel != null)
    {
      return beitragsmodel;
    }
    beitragsmodel = new SelectInput(Beitragsmodel.getArray(),
        new Beitragsmodel(Einstellungen.getEinstellung().getBeitragsmodel()));
    return beitragsmodel;
  }

  public TextInput getDateinamenmuster() throws RemoteException
  {
    if (dateinamenmuster != null)
    {
      return dateinamenmuster;
    }
    dateinamenmuster = new TextInput(Einstellungen.getEinstellung()
        .getDateinamenmuster(), 30);
    dateinamenmuster.setComment(JVereinPlugin.getI18n().tr(
        "a$ = Aufgabe, d$ = Datum, s$ = Sortierung, z$ = Zeit"));
    return dateinamenmuster;
  }

  public TextInput getDateinamenmusterSpende() throws RemoteException
  {
    if (dateinamenmusterspende != null)
    {
      return dateinamenmusterspende;
    }
    dateinamenmusterspende = new TextInput(Einstellungen.getEinstellung()
        .getDateinamenmusterSpende(), 30);
    dateinamenmusterspende.setComment(JVereinPlugin.getI18n().tr(
        "n$ = Name, v$ = Vorname, d$ = Datum, z$ = Zeit"));
    return dateinamenmusterspende;
  }

  public DecimalInput getSpendenbescheinigungminbetrag() throws RemoteException
  {
    if (spendenbescheinigungminbetrag != null)
    {
      return spendenbescheinigungminbetrag;
    }
    spendenbescheinigungminbetrag = new DecimalInput(Einstellungen
        .getEinstellung().getSpendenbescheinigungminbetrag(),
        new DecimalFormat("###0.00"));
    return spendenbescheinigungminbetrag;
  }

  public DirectoryInput getSpendenbescheinigungverzeichnis()
      throws RemoteException
  {
    if (spendenbescheinigungverzeichnis != null)
    {
      return spendenbescheinigungverzeichnis;
    }
    spendenbescheinigungverzeichnis = new DirectoryInput(Einstellungen
        .getEinstellung().getSpendenbescheinigungverzeichnis());
    return spendenbescheinigungverzeichnis;
  }

  public CheckboxInput getSpendenbescheinigungPrintBuchungsart()
      throws RemoteException
  {
    if (spendenbescheinigungprintbuchungsart != null)
    {
      return spendenbescheinigungprintbuchungsart;
    }
    spendenbescheinigungprintbuchungsart = new CheckboxInput(Einstellungen
        .getEinstellung().getSpendenbescheinigungPrintBuchungsart());
    return spendenbescheinigungprintbuchungsart;
  }

  public TextInput getBeginnGeschaeftsjahr() throws RemoteException
  {
    if (beginngeschaeftsjahr != null)
    {
      return beginngeschaeftsjahr;
    }
    beginngeschaeftsjahr = new TextInput(Einstellungen.getEinstellung()
        .getBeginnGeschaeftsjahr(), 6);
    return beginngeschaeftsjahr;
  }

  public TextInput getSmtpServer() throws RemoteException
  {
    if (smtp_server != null)
    {
      return smtp_server;
    }
    smtp_server = new TextInput(Einstellungen.getEinstellung().getSmtpServer(),
        50);
    return smtp_server;
  }

  public IntegerInput getSmtpPort() throws RemoteException
  {
    if (smtp_port != null)
    {
      return smtp_port;
    }
    smtp_port = new IntegerInput(new Integer(Einstellungen.getEinstellung()
        .getSmtpPort()));
    return smtp_port;
  }

  public TextInput getSmtpAuthUser() throws RemoteException
  {
    if (smtp_auth_user != null)
    {
      return smtp_auth_user;
    }
    smtp_auth_user = new TextInput(Einstellungen.getEinstellung()
        .getSmtpAuthUser(), 50);
    return smtp_auth_user;
  }

  public PasswordInput getSmtpAuthPwd() throws RemoteException
  {
    if (smtp_auth_pwd != null)
    {
      return smtp_auth_pwd;
    }
    smtp_auth_pwd = new PasswordInput(Einstellungen.getEinstellung()
        .getSmtpAuthPwd());
    return smtp_auth_pwd;
  }

  public TextInput getSmtpFromAddress() throws RemoteException
  {
    if (smtp_from_address != null)
    {
      return smtp_from_address;
    }
    smtp_from_address = new TextInput(Einstellungen.getEinstellung()
        .getSmtpFromAddress(), 50);
    return smtp_from_address;
  }

  public TextInput getSmtpFromAnzeigename() throws RemoteException
  {
    if (smtp_from_anzeigename != null)
    {
      return smtp_from_anzeigename;
    }
    smtp_from_anzeigename = new TextInput(Einstellungen.getEinstellung()
        .getSmtpFromAnzeigename(), 50);
    return smtp_from_anzeigename;
  }

  public CheckboxInput getSmtpSsl() throws RemoteException
  {
    if (smtp_ssl != null)
    {
      return smtp_ssl;
    }
    smtp_ssl = new CheckboxInput(Einstellungen.getEinstellung().getSmtpSsl());
    return smtp_ssl;
  }

  public CheckboxInput getSmtpStarttls() throws RemoteException
  {
    if (smtp_starttls != null)
    {
      return smtp_starttls;
    }
    smtp_starttls = new CheckboxInput(Einstellungen.getEinstellung()
        .getSmtpStarttls());
    return smtp_starttls;
  }

  public SelectInput getZahlungsweg() throws RemoteException
  {
    if (zahlungsweg != null)
    {
      return zahlungsweg;
    }
    zahlungsweg = new SelectInput(Zahlungsweg.getArray(), new Zahlungsweg(
        Einstellungen.getEinstellung().getZahlungsweg()));
    zahlungsweg.setName("Standard-Zahlungsweg f. neue Mitglieder");
    return zahlungsweg;
  }

  public SelectInput getZahlungsrhytmus() throws RemoteException
  {
    if (zahlungsrhytmus != null)
    {
      return zahlungsrhytmus;
    }
    zahlungsrhytmus = new SelectInput(
        Zahlungsrhytmus.getArray(),
        new Zahlungsrhytmus(Einstellungen.getEinstellung().getZahlungsrhytmus()));
    zahlungsrhytmus.setName(JVereinPlugin.getI18n().tr(
        "Standard-Zahlungsrhytmus f. neue Mitglieder"));
    return zahlungsrhytmus;
  }

  public SelectInput getDefaultSEPALand() throws RemoteException
  {
    if (sepaland != null)
    {
      return sepaland;
    }
    try
    {
      sepaland = new SEPALandInput();
    }
    catch (SEPAException e)
    {
      throw new RemoteException(e.getMessage());
    }
    return sepaland;
  }

  public Input getAltersgruppen() throws RemoteException
  {
    if (altersgruppen != null)
    {
      return altersgruppen;
    }
    altersgruppen = new TextInput(Einstellungen.getEinstellung()
        .getAltersgruppen(), 50);
    return altersgruppen;
  }

  public SelectInput getDtausTextschluessel() throws RemoteException
  {
    if (dtaustextschluessel != null)
    {
      return dtaustextschluessel;
    }
    dtaustextschluessel = new DtausTextschluesselInput(Einstellungen
        .getEinstellung().getDtausTextschluessel());

    return dtaustextschluessel;
  }

  public Input getJubilaeen() throws RemoteException
  {
    if (jubilaeen != null)
    {
      return jubilaeen;
    }
    jubilaeen = new TextInput(Einstellungen.getEinstellung().getJubilaeen(), 50);
    return jubilaeen;
  }

  public Input getAltersjubilaeen() throws RemoteException
  {
    if (altersjubilaeen != null)
    {
      return altersjubilaeen;
    }
    altersjubilaeen = new TextInput(Einstellungen.getEinstellung()
        .getAltersjubilaeen(), 50);
    return altersjubilaeen;
  }

  public IntegerInput getJubilarStartAlter() throws RemoteException
  {
    if (null == jubilarStartAlter)
    {
      jubilarStartAlter = new IntegerInput(Einstellungen.getEinstellung()
          .getJubilarStartAlter());
    }
    return jubilarStartAlter;
  }

  public IntegerInput getDelaytime() throws RemoteException
  {
    if (delaytime != null)
    {
      return delaytime;
    }
    delaytime = new IntegerInput(Integer.valueOf(Einstellungen.getEinstellung()
        .getDelaytime()));
    return delaytime;
  }

  public TablePart getSpaltendefinitionTable(Composite parent)
      throws RemoteException
  {
    if (spalten == null)
    {
      spalten = new MitgliedSpaltenauswahl();
    }
    return spalten.paintSpaltenpaintSpaltendefinitionTable(parent);
  }

  // public void setCheckSpalten()
  // {
  // for (int i = 0; i < spalten.size(); ++i)
  // {
  // spaltendefinitionList.setChecked(spalten.get(i), spalten.get(i)
  // .isChecked());
  // }
  // }

  public IntegerInput getAnzahlSpaltenStammdatenInput() throws RemoteException
  {
    {
      if (AnzahlSpaltenStammdatenInput != null)
      {
        return AnzahlSpaltenStammdatenInput;
      }
      AnzahlSpaltenStammdatenInput = new IntegerInput(Einstellungen
          .getEinstellung().getAnzahlSpaltenStammdaten());
      return AnzahlSpaltenStammdatenInput;
    }
  }

  public IntegerInput getAnzahlSpaltenLesefelderInput() throws RemoteException
  {
    {
      if (AnzahlSpaltenLesefelderInput != null)
      {
        return AnzahlSpaltenLesefelderInput;
      }
      AnzahlSpaltenLesefelderInput = new IntegerInput(Einstellungen
          .getEinstellung().getAnzahlSpaltenLesefelder());
      return AnzahlSpaltenLesefelderInput;
    }
  }

  public IntegerInput getAnzahlSpaltenMitgliedschaftInput()
      throws RemoteException
  {
    {
      if (AnzahlSpaltenMitgliedschaftInput != null)
      {
        return AnzahlSpaltenMitgliedschaftInput;
      }
      AnzahlSpaltenMitgliedschaftInput = new IntegerInput(Einstellungen
          .getEinstellung().getAnzahlSpaltenMitgliedschaft());
      return AnzahlSpaltenMitgliedschaftInput;
    }
  }

  public IntegerInput getAnzahlSpaltenZahlungInput() throws RemoteException
  {
    {
      if (AnzahlSpaltenZahlungInput != null)
      {
        return AnzahlSpaltenZahlungInput;
      }
      AnzahlSpaltenZahlungInput = new IntegerInput(Einstellungen
          .getEinstellung().getAnzahlSpaltenZahlung());
      return AnzahlSpaltenZahlungInput;
    }
  }

  public IntegerInput getAnzahlSpaltenZusatzfelderInput()
      throws RemoteException
  {
    {
      if (AnzahlSpaltenZusatzfelderInput != null)
      {
        return AnzahlSpaltenZusatzfelderInput;
      }
      AnzahlSpaltenZusatzfelderInput = new IntegerInput(Einstellungen
          .getEinstellung().getAnzahlSpaltenZusatzfelder());
      return AnzahlSpaltenZusatzfelderInput;
    }
  }

  public CheckboxInput getZeigeStammdatenInTabCheckbox() throws RemoteException
  {
    if (ZeigeStammdatenInTabInput != null)
    {
      return ZeigeStammdatenInTabInput;
    }
    ZeigeStammdatenInTabInput = new CheckboxInput(Einstellungen
        .getEinstellung().getZeigeStammdatenInTab());
    return ZeigeStammdatenInTabInput;
  }

  public CheckboxInput getZeigeMitgliedschaftInTabCheckbox()
      throws RemoteException
  {
    if (ZeigeMitgliedschaftInTabInput != null)
    {
      return ZeigeMitgliedschaftInTabInput;
    }
    ZeigeMitgliedschaftInTabInput = new CheckboxInput(Einstellungen
        .getEinstellung().getZeigeMitgliedschaftInTab());
    return ZeigeMitgliedschaftInTabInput;
  }

  public CheckboxInput getZeigeZahlungInTabCheckbox() throws RemoteException
  {
    if (ZeigeZahlungInTabInput != null)
    {
      return ZeigeZahlungInTabInput;
    }
    ZeigeZahlungInTabInput = new CheckboxInput(Einstellungen.getEinstellung()
        .getZeigeZahlungInTab());
    return ZeigeZahlungInTabInput;
  }

  public CheckboxInput getZeigeZusatzbetrageInTabCheckbox()
      throws RemoteException
  {
    if (ZeigeZusatzbeitraegeInTabInput != null)
    {
      return ZeigeZusatzbeitraegeInTabInput;
    }
    ZeigeZusatzbeitraegeInTabInput = new CheckboxInput(Einstellungen
        .getEinstellung().getZeigeZusatzbetraegeInTab());
    return ZeigeZusatzbeitraegeInTabInput;
  }

  public CheckboxInput getZeigeMitgliedskontoInTabCheckbox()
      throws RemoteException
  {
    if (ZeigeMitgliedskontoInTabInput != null)
    {
      return ZeigeMitgliedskontoInTabInput;
    }
    ZeigeMitgliedskontoInTabInput = new CheckboxInput(Einstellungen
        .getEinstellung().getZeigeMitgliedskontoInTab());
    return ZeigeMitgliedskontoInTabInput;
  }

  public CheckboxInput getZeigeVermerkeInTabCheckbox() throws RemoteException
  {
    if (ZeigeVermerkeInTabInput != null)
    {
      return ZeigeVermerkeInTabInput;
    }
    ZeigeVermerkeInTabInput = new CheckboxInput(Einstellungen.getEinstellung()
        .getZeigeVermerkeInTab());
    return ZeigeVermerkeInTabInput;
  }

  public CheckboxInput getZeigeWiedervorlageInTabCheckbox()
      throws RemoteException
  {
    if (ZeigeWiedervorlageInTabInput != null)
    {
      return ZeigeWiedervorlageInTabInput;
    }
    ZeigeWiedervorlageInTabInput = new CheckboxInput(Einstellungen
        .getEinstellung().getZeigeWiedervorlageInTab());
    return ZeigeWiedervorlageInTabInput;
  }

  public CheckboxInput getZeigeMailsInTabCheckbox() throws RemoteException
  {
    if (ZeigeMailsInTabInput != null)
    {
      return ZeigeMailsInTabInput;
    }
    ZeigeMailsInTabInput = new CheckboxInput(Einstellungen.getEinstellung()
        .getZeigeMailsInTab());
    return ZeigeMailsInTabInput;
  }

  public CheckboxInput getZeigeEigenschaftenInTabCheckbox()
      throws RemoteException
  {
    if (ZeigeEigenschaftenInTabInput != null)
    {
      return ZeigeEigenschaftenInTabInput;
    }
    ZeigeEigenschaftenInTabInput = new CheckboxInput(Einstellungen
        .getEinstellung().getZeigeEigenschaftenInTab());
    return ZeigeEigenschaftenInTabInput;
  }

  public CheckboxInput getZeigeZusatzfelderInTabCheckbox()
      throws RemoteException
  {
    if (ZeigeZusatzfelderInTabInput != null)
    {
      return ZeigeZusatzfelderInTabInput;
    }
    ZeigeZusatzfelderInTabInput = new CheckboxInput(Einstellungen
        .getEinstellung().getZeigeZusatzfelderInTab());
    return ZeigeZusatzfelderInTabInput;
  }

  public CheckboxInput getZeigeLehrgaengeInTabCheckbox() throws RemoteException
  {
    if (ZeigeLehrgaengeInTabInput != null)
    {
      return ZeigeLehrgaengeInTabInput;
    }
    ZeigeLehrgaengeInTabInput = new CheckboxInput(Einstellungen
        .getEinstellung().getZeigeLehrgaengeInTab());
    return ZeigeLehrgaengeInTabInput;
  }

  public CheckboxInput getZeigeFotoInTabCheckbox() throws RemoteException
  {
    if (ZeigeFotoInTabInput != null)
    {
      return ZeigeFotoInTabInput;
    }
    ZeigeFotoInTabInput = new CheckboxInput(Einstellungen.getEinstellung()
        .getZeigeFotoInTab());
    return ZeigeFotoInTabInput;
  }

  public CheckboxInput getZeigeLesefelderInTabCheckbox() throws RemoteException
  {
    if (ZeigeLesefelderInTabInput != null)
    {
      return ZeigeLesefelderInTabInput;
    }
    ZeigeLesefelderInTabInput = new CheckboxInput(Einstellungen
        .getEinstellung().getZeigeLesefelderInTab());
    return ZeigeLesefelderInTabInput;
  }

  public CheckboxInput getZeigeArbeitseinsatzInTabCheckbox()
      throws RemoteException
  {
    if (ZeigeArbeitseinsatzInTabInput != null)
    {
      return ZeigeArbeitseinsatzInTabInput;
    }
    ZeigeArbeitseinsatzInTabInput = new CheckboxInput(Einstellungen
        .getEinstellung().getZeigeArbeitseinsatzInTab());
    return ZeigeArbeitseinsatzInTabInput;
  }

  public ButtonArea getButton() throws RemoteException
  {
    return new BankverbindungDialogButton(getEinstellung(), getBlz(),
        getKonto(), getBic(), getIban());
  }

  public void handleStore()
  {
    try
    {
      Einstellung e = Einstellungen.getEinstellung();
      e.setID();
      e.setName((String) getName(false).getValue());
      e.setNameLang((String) getNameLang().getValue());
      e.setStrasse((String) getStrasse().getValue());
      e.setPlz((String) getPlz().getValue());
      e.setOrt((String) getOrt().getValue());
      e.setFinanzamt((String) getFinanzamt().getValue());
      e.setSteuernummer((String) getSteuernummer().getValue());
      e.setBescheiddatum((Date) getBescheiddatum().getValue());
      e.setVorlaeufig((Boolean) getVorlaeufig().getValue());
      e.setVorlaeufigab((Date) getVorlaeufigab().getValue());
      e.setBeguenstigterzweck((String) getBeguenstigterzweck().getValue());
      e.setMitgliedsbeitraege((Boolean) getMitgliedsbetraege().getValue());
      e.setBic((String) getBic().getValue());
      e.setIban((String) getIban().getValue());
      e.setGlaeubigerID((String) getGlaeubigerID().getValue());
      e.setBlz((String) getBlz().getValue());
      e.setKonto((String) getKonto().getValue());
      e.setGeburtsdatumPflicht((Boolean) geburtsdatumpflicht.getValue());
      e.setEintrittsdatumPflicht((Boolean) eintrittsdatumpflicht.getValue());
      e.setSterbedatum((Boolean) sterbedatum.getValue());
      e.setKommunikationsdaten((Boolean) kommunikationsdaten.getValue());
      e.setZusatzbetrag((Boolean) zusatzbetrag.getValue());
      e.setVermerke((Boolean) vermerke.getValue());
      e.setWiedervorlage((Boolean) wiedervorlage.getValue());
      e.setKursteilnehmer((Boolean) kursteilnehmer.getValue());
      e.setLehrgaenge((Boolean) lehrgaenge.getValue());
      e.setJuristischePersonen((Boolean) juristischepersonen.getValue());
      e.setMitgliedskonto((Boolean) mitgliedskonto.getValue());
      e.setMitgliedfoto((Boolean) mitgliedfoto.getValue());
      e.setUseLesefelder((Boolean) uselesefelder.getValue());
      e.setZusatzadressen((Boolean) zusatzadressen.getValue());
      e.setAuslandsadressen((Boolean) auslandsadressen.getValue());
      e.setArbeitseinsatz((Boolean) arbeitseinsatz.getValue());
      e.setDokumentenspeicherung((Boolean) dokumentenspeicherung.getValue());
      e.setIndividuelleBeitraege((Boolean) individuellebeitraege.getValue());
      e.setRechnungTextAbbuchung((String) rechnungtextabbuchung.getValue());
      e.setRechnungTextAbbuchung((String) rechnungtextabbuchung.getValue());
      e.setRechnungTextUeberweisung((String) rechnungtextueberweisung
          .getValue());
      e.setRechnungTextBar((String) rechnungtextbar.getValue());
      e.setExterneMitgliedsnummer((Boolean) externemitgliedsnummer.getValue());
      Beitragsmodel bm = (Beitragsmodel) beitragsmodel.getValue();
      e.setBeitragsmodel(bm.getKey());
      e.setDateinamenmuster((String) dateinamenmuster.getValue());
      e.setDateinamenmusterSpende((String) dateinamenmusterspende.getValue());
      e.setSpendenbescheinigungminbetrag((Double) spendenbescheinigungminbetrag
          .getValue());
      e.setSpendenbescheinigungverzeichnis((String) spendenbescheinigungverzeichnis
          .getValue());
      e.setSpendenbescheinigungPrintBuchungsart((Boolean) spendenbescheinigungprintbuchungsart
          .getValue());
      e.setBeginnGeschaeftsjahr((String) beginngeschaeftsjahr.getValue());
      e.setSmtpServer((String) smtp_server.getValue());
      Integer port = (Integer) smtp_port.getValue();
      e.setSmtpPort(port.toString());
      e.setSmtpAuthUser((String) smtp_auth_user.getValue());
      e.setSmtpAuthPwd((String) smtp_auth_pwd.getValue());
      e.setSmtpFromAddress((String) smtp_from_address.getValue());
      e.setSmtpFromAnzeigename((String) smtp_from_anzeigename.getValue());
      e.setSmtpSsl((Boolean) smtp_ssl.getValue());

      e.setSmtpStarttls((Boolean) smtp_starttls.getValue());
      Zahlungsrhytmus zr = (Zahlungsrhytmus) zahlungsrhytmus.getValue();
      e.setZahlungsrhytmus(zr.getKey());
      Zahlungsweg zw = (Zahlungsweg) zahlungsweg.getValue();
      e.setZahlungsweg(zw.getKey());
      e.setDtausTextschluessel((String) getDtausTextschluessel().getValue());
      SEPALandObject slo = (SEPALandObject) getDefaultSEPALand().getValue();
      e.setDefaultLand(slo.getLand().getKennzeichen());
      e.setAltersgruppen((String) getAltersgruppen().getValue());
      e.setJubilaeen((String) getJubilaeen().getValue());
      e.setAltersjubilaeen((String) getAltersjubilaeen().getValue());
      Integer jubilaeumStartAlter = (Integer) jubilarStartAlter.getValue();
      e.setJubilarStartAlter(jubilaeumStartAlter);
      Integer delay = (Integer) delaytime.getValue();
      e.setDelaytime(delay);
      e.store();
      spalten.save();
      Einstellungen.setEinstellung(e);

      e.setAnzahlSpaltenStammdaten((Integer) getAnzahlSpaltenStammdatenInput()
          .getValue());
      e.setAnzahlSpaltenLesefelder((Integer) getAnzahlSpaltenLesefelderInput()
          .getValue());
      e.setAnzahlSpaltenZusatzfelder((Integer) getAnzahlSpaltenZusatzfelderInput()
          .getValue());
      e.setAnzahlSpaltenMitgliedschaft((Integer) getAnzahlSpaltenMitgliedschaftInput()
          .getValue());
      e.setAnzahlSpaltenZahlung((Integer) getAnzahlSpaltenZahlungInput()
          .getValue());
      e.setZeigeStammdatenInTab((Boolean) getZeigeStammdatenInTabCheckbox()
          .getValue());
      e.setZeigeMitgliedschaftInTab((Boolean) getZeigeMitgliedschaftInTabCheckbox()
          .getValue());
      e.setZeigeZahlungInTab((Boolean) getZeigeZahlungInTabCheckbox()
          .getValue());
      e.setZeigeZusatzbetrageInTab((Boolean) getZeigeZusatzbetrageInTabCheckbox()
          .getValue());
      e.setZeigeMitgliedskontoInTab((Boolean) getZeigeMitgliedskontoInTabCheckbox()
          .getValue());
      e.setZeigeVermerkeInTab((Boolean) getZeigeVermerkeInTabCheckbox()
          .getValue());
      e.setZeigeWiedervorlageInTab((Boolean) getZeigeWiedervorlageInTabCheckbox()
          .getValue());
      e.setZeigeMailsInTab((Boolean) getZeigeMailsInTabCheckbox().getValue());
      e.setZeigeEigentschaftenInTab((Boolean) getZeigeEigenschaftenInTabCheckbox()
          .getValue());
      e.setZeigeZusatzfelderInTab((Boolean) getZeigeZusatzfelderInTabCheckbox()
          .getValue());
      e.setZeigeLehrgaengeInTab((Boolean) getZeigeLehrgaengeInTabCheckbox()
          .getValue());
      e.setZeigeFotoInTab((Boolean) getZeigeFotoInTabCheckbox().getValue());
      e.setZeigeLesefelderInTab((Boolean) getZeigeLesefelderInTabCheckbox()
          .getValue());
      e.setZeigeArbeitseinsatzInTab((Boolean) getZeigeArbeitseinsatzInTabCheckbox()
          .getValue());

      GUI.getStatusBar().setSuccessText(
          JVereinPlugin.getI18n().tr("Einstellungen gespeichert"));
    }
    catch (RemoteException e)
    {
      GUI.getStatusBar().setErrorText(e.getMessage());
    }
    catch (ApplicationException e)
    {
      GUI.getStatusBar().setErrorText(e.getMessage());
    }
  }

  /**
   * Sucht das Geldinstitut zur eingegebenen BLZ und zeigt es als Kommentar
   * hinter dem BLZ-Feld an.
   */
  private class BLZListener implements Listener
  {
    @Override
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

}
