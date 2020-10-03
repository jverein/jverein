/**********************************************************************
 * Copyright (c) by Heiner Jostkleigrewe
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de | www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.rmi;

import java.rmi.RemoteException;
import java.util.Date;

import de.jost_net.JVerein.io.IBankverbindung;
import de.jost_net.JVerein.keys.Beitragsmodel;
import de.willuhn.datasource.rmi.DBObject;

public interface Einstellung extends DBObject, IBankverbindung {

    public static final String COL_ALTER_MODEL = "altermodel";

    public static final String COL_ARBEITS_MODEL = "arbeitsmodel";

    public static final String COL_SEPA_MANDANTID_SOURCE = "mandatid_source";

    public static final String COL_BUCHUNG_BUCHUNGSART_AUSWAHL = "buchungbuchungsartauswahl";

    public void setID() throws RemoteException;

    public String getName() throws RemoteException;

    public void setName(String name) throws RemoteException;

    public String getStrasse() throws RemoteException;

    public void setStrasse(String strasse) throws RemoteException;

    public String getPlz() throws RemoteException;

    public void setPlz(String plz) throws RemoteException;

    public String getOrt() throws RemoteException;

    public void setOrt(String ort) throws RemoteException;

    public String getFinanzamt() throws RemoteException;

    public void setFinanzamt(String finanzamt) throws RemoteException;

    public String getSteuernummer() throws RemoteException;

    public void setSteuernummer(String steuernummer) throws RemoteException;

    public Date getBescheiddatum() throws RemoteException;

    public void setBescheiddatum(Date bescheiddatum) throws RemoteException;

    public Boolean getVorlaeufig() throws RemoteException;

    public void setVorlaeufig(Boolean vorlaeufig) throws RemoteException;

    public Date getVorlaeufigab() throws RemoteException;

    public void setVorlaeufigab(Date vorlaeufigab) throws RemoteException;

    public Date getVeranlagungVon() throws RemoteException;

    public void setVeranlagungVon(Date veranlagungvon) throws RemoteException;

    public Date getVeranlagungBis() throws RemoteException;

    public void setVeranlagungBis(Date veranlagungbis) throws RemoteException;

    public String getBeguenstigterzweck() throws RemoteException;

    public void setBeguenstigterzweck(String beguenstigterzweck) throws RemoteException;

    public Boolean getMitgliedsbetraege() throws RemoteException;

    public void setMitgliedsbeitraege(Boolean mitgliedsbeitraege) throws RemoteException;

    @Override
    public void setBic(String bic) throws RemoteException;

    @Override
    public String getBic() throws RemoteException;

    @Override
    public void setIban(String iban) throws RemoteException;

    @Override
    public String getIban() throws RemoteException;

    public de.willuhn.jameica.hbci.rmi.Konto getHibiscusKonto() throws RemoteException;

    public void setGlaeubigerID(String glaeubigerid) throws RemoteException;

    public String getGlaeubigerID() throws RemoteException;

    public Boolean getGeburtsdatumPflicht() throws RemoteException;

    public void setGeburtsdatumPflicht(Boolean geburtsdatumpflicht) throws RemoteException;

    public Boolean getEintrittsdatumPflicht() throws RemoteException;

    public void setEintrittsdatumPflicht(Boolean eintrittsdatumpflicht) throws RemoteException;

    public Boolean getSterbedatum() throws RemoteException;

    public void setSterbedatum(Boolean sterbedatum) throws RemoteException;

    public Boolean getKommunikationsdaten() throws RemoteException;

    public void setKommunikationsdaten(Boolean kommunikationsdaten) throws RemoteException;

    public Boolean getSekundaereBeitragsgruppen() throws RemoteException;

    public void setSekundaereBeitragsgruppen(Boolean sekundaerebeitragsgruppen)
            throws RemoteException;

    public Boolean getZusatzbetrag() throws RemoteException;

    public void setZusatzbetrag(Boolean zusatzbetrag) throws RemoteException;

    public Boolean getVermerke() throws RemoteException;

    public void setVermerke(Boolean vermerke) throws RemoteException;

    public Boolean getWiedervorlage() throws RemoteException;

    public void setWiedervorlage(Boolean wiedervorlage) throws RemoteException;

    public Boolean getKursteilnehmer() throws RemoteException;

    public void setKursteilnehmer(Boolean kursteilnehmer) throws RemoteException;

    public Boolean getKursteilnehmerGebGesPflicht() throws RemoteException;

    public void setKursteilnehmerGebGesPflicht(Boolean kursteilnehmergebgespflicht)
            throws RemoteException;

    public Boolean getLehrgaenge() throws RemoteException;

    public void setJuristischePersonen(Boolean juristischePersonen) throws RemoteException;

    public Boolean getJuristischePersonen() throws RemoteException;

    public void setMitgliedfoto(Boolean mitgliedfoto) throws RemoteException;

    public Boolean getMitgliedfoto() throws RemoteException;

    // TODO Für Versionsbau deaktiviert
    // public void setInventar(Boolean inventar) throws RemoteException;

    // public Boolean getInventar() throws RemoteException;

    public void setZusatzadressen(Boolean zusatzadressen) throws RemoteException;

    public Boolean getZusatzadressen() throws RemoteException;

    public void setAuslandsadressen(Boolean auslandsadressen) throws RemoteException;

    public Boolean getAuslandsadressen() throws RemoteException;

    public void setArbeitseinsatz(Boolean arbeitseinsatz) throws RemoteException;

    public Boolean getArbeitseinsatz() throws RemoteException;

    public void setDokumentenspeicherung(Boolean dokumentenspeicherung) throws RemoteException;

    public Boolean getDokumentenspeicherung() throws RemoteException;

    public void setIndividuelleBeitraege(Boolean individuellebeitraege) throws RemoteException;

    public Boolean getIndividuelleBeitraege() throws RemoteException;

    public void setRechnungTextAbbuchung(String rechnungtextabbuchung) throws RemoteException;

    public String getRechnungTextAbbuchung() throws RemoteException;

    public void setRechnungTextUeberweisung(String rechnungtextueberweisung) throws RemoteException;

    public String getRechnungTextUeberweisung() throws RemoteException;

    public void setRechnungTextBar(String rechnungtextbar) throws RemoteException;

    public String getRechnungTextBar() throws RemoteException;

    public void setLehrgaenge(Boolean lehrgaenge) throws RemoteException;

    public Boolean getExterneMitgliedsnummer() throws RemoteException;

    public void setExterneMitgliedsnummer(Boolean externemitgliedsnummer) throws RemoteException;

    public Beitragsmodel getBeitragsmodel() throws RemoteException;

    public void setBeitragsmodel(int beitragsmodel) throws RemoteException;

    public String getDateinamenmuster() throws RemoteException;

    public void setDateinamenmuster(String dateinamenmuster) throws RemoteException;

    public String getDateinamenmusterSpende() throws RemoteException;

    public void setDateinamenmusterSpende(String dateinamenmusterspende) throws RemoteException;

    public String getVorlagenCsvVerzeichnis() throws RemoteException;

    public void setVorlagenCsvVerzeichnis(String vorlagenCsvVerzeichnis) throws RemoteException;

    public double getSpendenbescheinigungminbetrag() throws RemoteException;

    public void setSpendenbescheinigungminbetrag(double minbetrag) throws RemoteException;

    public String getSpendenbescheinigungverzeichnis() throws RemoteException;

    public void setSpendenbescheinigungverzeichnis(String spendenbescheinigungverzeichnis)
            throws RemoteException;

    public boolean getSpendenbescheinigungPrintBuchungsart() throws RemoteException;

    public void setSpendenbescheinigungPrintBuchungsart(Boolean printbuchungsart)
            throws RemoteException;

    public String getBeginnGeschaeftsjahr() throws RemoteException;

    public Boolean getAutoBuchunguebernahme() throws RemoteException;

    public void setAutoBuchunguebernahme(Boolean autobuchunguebernahme) throws RemoteException;

    public Boolean getUnterdrueckungOhneBuchung() throws RemoteException;

    public void setUnterdrueckungOhneBuchung(Boolean unterdrueckungohnebuchung)
            throws RemoteException;

    public Boolean getVerwendeBelegnummer() throws RemoteException;

    public void setVerwendeBelegnummer(Boolean verwendebelegnummer) throws RemoteException;

    public Boolean getBelegnummerProKonto() throws RemoteException;

    public void setBelegnummerProKonto(Boolean belegnummer_pro_konto) throws RemoteException;

    public Boolean getBelegnummerProJahr() throws RemoteException;

    public void setBelegnummerProJahr(Boolean belegnummer_pro_jahr) throws RemoteException;

    public void setBeginnGeschaeftsjahr(String beginngeschaeftsjahr) throws RemoteException;

    public String getSmtpServer() throws RemoteException;

    public void setSmtpServer(String smtp_server) throws RemoteException;

    public String getSmtpPort() throws RemoteException;

    public void setSmtpPort(String smtp_port) throws RemoteException;

    public String getSmtpAuthUser() throws RemoteException;

    public void setSmtpAuthUser(String smtp_auth_user) throws RemoteException;

    public String getSmtpAuthPwd() throws RemoteException;

    public void setSmtpAuthPwd(String smtp_auth_pwd) throws RemoteException;

    public String getSmtpFromAddress() throws RemoteException;

    public void setSmtpFromAddress(String smtp_from_address) throws RemoteException;

    public String getSmtpFromAnzeigename() throws RemoteException;

    public void setSmtpFromAnzeigename(String smtp_from_anzeigename) throws RemoteException;

    public Boolean getSmtpSsl() throws RemoteException;

    public void setSmtpSsl(Boolean smtp_ssl) throws RemoteException;

    public Boolean getSmtpStarttls() throws RemoteException;

    public void setSmtpStarttls(Boolean smtp_starttls) throws RemoteException;

    public int getMailVerzoegerung() throws RemoteException;

    public void setMailVerzoegerung(int verzoegerung) throws RemoteException;

    public String getMailAlwaysBcc() throws RemoteException;

    public void setMailAlwaysBcc(String mail_always_bcc) throws RemoteException;

    public String getMailAlwaysCc() throws RemoteException;

    public void setMailAlwaysCc(String mail_always_cc) throws RemoteException;

    public Boolean getCopyToImapFolder() throws RemoteException;

    public void setCopyToImapFolder(Boolean copy_to_imap_folder) throws RemoteException;

    public String getImapAuthUser() throws RemoteException;

    public void setImapAuthUser(String imap_auth_user) throws RemoteException;

    public String getImapAuthPwd() throws RemoteException;

    public void setImapAuthPwd(String imap_auth_pwd) throws RemoteException;

    public String getImapHost() throws RemoteException;

    public void setImapHost(String imap_host) throws RemoteException;

    public String getImapPort() throws RemoteException;

    public void setImapPort(String imap_port) throws RemoteException;

    public Boolean getImapSsl() throws RemoteException;

    public void setImapSsl(Boolean imap_ssl) throws RemoteException;

    public Boolean getImapStartTls() throws RemoteException;

    public void setImapStartTls(Boolean imap_starttls) throws RemoteException;

    public String getImapSentFolder() throws RemoteException;

    public void setImapSentFolder(String imap_sent_folder) throws RemoteException;

    public String getMailSignatur(Boolean separator) throws RemoteException;

    public void setMailSignatur(String mailsignatur) throws RemoteException;

    public int getZahlungsrhytmus() throws RemoteException;

    public void setZahlungsrhytmus(int zahlungsrhytmus) throws RemoteException;

    public int getZahlungsweg() throws RemoteException;

    public void setZahlungsweg(int zahlungsweg) throws RemoteException;

    public String getDefaultLand() throws RemoteException;

    public void setDefaultLand(String defaultland) throws RemoteException;

    public Integer getSEPADatumOffset() throws RemoteException;

    public void setSEPADatumOffset(Integer sepadatumoffset) throws RemoteException;

    public String getAltersgruppen() throws RemoteException;

    public void setAltersgruppen(String altersgruppen) throws RemoteException;

    public String getJubilaeen() throws RemoteException;

    public void setJubilaeen(String jubilaeen) throws RemoteException;

    public String getAltersjubilaeen() throws RemoteException;

    public void setAltersjubilaeen(String altersjubilaeen) throws RemoteException;

    public int getJubilarStartAlter() throws RemoteException;

    public void setJubilarStartAlter(int alter) throws RemoteException;

    public boolean hasZusatzfelder() throws RemoteException;

    public boolean getUseLesefelder() throws RemoteException;

    public void setUseLesefelder(boolean use) throws RemoteException;

    public boolean getZeigeStammdatenInTab() throws RemoteException;

    public void setZeigeStammdatenInTab(boolean showInTab) throws RemoteException;

    public boolean getZeigeMitgliedschaftInTab() throws RemoteException;

    public void setZeigeMitgliedschaftInTab(boolean showInTab) throws RemoteException;

    public boolean getZeigeZahlungInTab() throws RemoteException;

    public void setZeigeZahlungInTab(boolean showInTab) throws RemoteException;

    public boolean getZeigeZusatzbetraegeInTab() throws RemoteException;

    public void setZeigeZusatzbetrageInTab(boolean showInTab) throws RemoteException;

    public boolean getZeigeMitgliedskontoInTab() throws RemoteException;

    public void setZeigeMitgliedskontoInTab(boolean showInTab) throws RemoteException;

    public boolean getZeigeVermerkeInTab() throws RemoteException;

    public void setZeigeVermerkeInTab(boolean showInTab) throws RemoteException;

    public boolean getZeigeWiedervorlageInTab() throws RemoteException;

    public void setZeigeWiedervorlageInTab(boolean showInTab) throws RemoteException;

    public boolean getZeigeEigenschaftenInTab() throws RemoteException;

    public void setZeigeEigentschaftenInTab(boolean showInTab) throws RemoteException;

    public boolean getZeigeMailsInTab() throws RemoteException;

    public void setZeigeMailsInTab(boolean showInTab) throws RemoteException;

    public boolean getZeigeZusatzfelderInTab() throws RemoteException;

    public void setZeigeZusatzfelderInTab(boolean showInTab) throws RemoteException;

    public boolean getZeigeLehrgaengeInTab() throws RemoteException;

    public void setZeigeLehrgaengeInTab(boolean showInTab) throws RemoteException;

    public boolean getZeigeFotoInTab() throws RemoteException;

    public void setZeigeFotoInTab(boolean showInTab) throws RemoteException;

    public boolean getZeigeLesefelderInTab() throws RemoteException;

    public void setZeigeLesefelderInTab(boolean showInTab) throws RemoteException;

    public boolean getZeigeArbeitseinsatzInTab() throws RemoteException;

    public void setZeigeArbeitseinsatzInTab(boolean showInTab) throws RemoteException;

    public int getAnzahlSpaltenStammdaten() throws RemoteException;

    public void setAnzahlSpaltenStammdaten(int anzahlSpalten) throws RemoteException;

    public int getAnzahlSpaltenZusatzfelder() throws RemoteException;

    public void setAnzahlSpaltenZusatzfelder(int anzahlSpalten) throws RemoteException;

    public int getAnzahlSpaltenLesefelder() throws RemoteException;

    public void setAnzahlSpaltenLesefelder(int anzahlSpalten) throws RemoteException;

    public int getAnzahlSpaltenMitgliedschaft() throws RemoteException;

    public void setAnzahlSpaltenMitgliedschaft(int anzahlSpalten) throws RemoteException;

    public int getAnzahlSpaltenZahlung() throws RemoteException;

    public void setAnzahlSpaltenZahlung(int anzahlSpalten) throws RemoteException;

    public int getArbeitsstundenmodel() throws RemoteException;

    public void setArbeitsstundenmodel(int arbeitsstundenmodel) throws RemoteException;

    public int getAltersModel() throws RemoteException;

    public void setAltersModel(int altersmodel) throws RemoteException;

    public boolean getZusatzbetragAusgetretene() throws RemoteException;

    public void setZusatzbetragAusgetretene(boolean zusatzbetragAusgetretene)
            throws RemoteException;

    public int getSepaMandatIdSource() throws RemoteException;

    public void setSepaMandatIdSource(int sepaMandatIdSource) throws RemoteException;

    public int getBuchungBuchungsartAuswahl() throws RemoteException;

    public void setBuchungBuchungsartAuswahl(int buchungBuchungsartAuswahl) throws RemoteException;

    public int getBuchungsartSort() throws RemoteException;

    public void setBuchungsartSort(int buchungsartsort) throws RemoteException;

    public Boolean getAbrlAbschliessen() throws RemoteException;

    public void setAbrlAbschliessen(Boolean beleg) throws RemoteException;

}
