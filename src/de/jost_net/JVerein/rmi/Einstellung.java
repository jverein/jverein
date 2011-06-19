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
 * Revision 1.28  2011-05-20 13:00:51  jost
 * Neu: Individueller Beitrag
 *
 * Revision 1.27  2011-04-17 06:40:19  jost
 * Neu: Mitglieder-Selektion nach Zusatzfeldern
 *
 * Revision 1.26  2011-04-06 16:29:39  jost
 * Neu: Starttls
 *
 * Revision 1.25  2011-03-17 19:46:44  jost
 * Aktuelle Geburtstage und Wiedervorlage ausgemustert. Ersatz durch die neue Terminübersicht.
 *
 * Revision 1.24  2011-03-13 13:49:05  jost
 * Zusätzliches Feld f. Sachspendenbescheinigungen.
 *
 * Revision 1.23  2011-03-10 20:34:50  jost
 * Neu: Einstellungen f. Spendenbescheinigung
 *
 * Revision 1.22  2011-01-30 08:28:30  jost
 * Neu: Zusatzadressen
 *
 * Revision 1.21  2011-01-29 20:32:52  jost
 * Verzögerungszeit für Suchfelder
 *
 * Revision 1.20  2011-01-09 14:31:47  jost
 * Stammdaten in die Einstellungen verschoben.
 *
 * Revision 1.19  2011-01-08 15:56:31  jost
 * Einstellungen: Dokumentenspeicherung
 *
 * Revision 1.18  2010-11-17 04:52:09  jost
 * Erster Code zum Thema Arbeitseinsatz
 *
 * Revision 1.17  2010-11-13 09:28:09  jost
 * Mit V 1.5 deprecatete Spalten und Tabellen entfernt.
 *
 * Revision 1.16  2010-10-28 19:15:36  jost
 * Neu: Wohnsitzstaat
 *
 * Revision 1.15  2010-08-27 19:08:23  jost
 * neu: Mitgliedsfoto
 *
 * Revision 1.14  2010-08-10 18:07:02  jost
 * Zahlungswegtexte für den Rechnungsdruck
 *
 * Revision 1.13  2010-08-10 05:40:58  jost
 * Reaktivierung alter Rechnungen
 *
 * Revision 1.12  2010-07-26 08:23:24  jost
 * Manuelle Zahlungen defaultmäßig deaktviert. Reaktvierbar durch Einstellungen.
 *
 * Revision 1.11  2010-07-25 18:46:05  jost
 * Neu: Mitgliedskonto
 *
 * Revision 1.10  2010/01/01 22:36:09  jost
 * Standardwerte für Zahlungsweg und Zahlungsrhytmus können vorgegeben werden.
 *
 * Revision 1.9  2009/12/06 21:41:48  jost
 * Überflüssigen Code entfernt.
 *
 * Revision 1.8  2009/11/19 21:11:03  jost
 * Update-Option entfernt.
 *
 * Revision 1.7  2009/10/17 19:47:29  jost
 * Vorbereitung Mailversand.
 *
 * Revision 1.6  2009/09/13 19:20:29  jost
 * Neu: Prüfung auf Updates
 *
 * Revision 1.5  2009/07/14 07:30:18  jost
 * Bugfix Rechnungen.
 *
 * Revision 1.4  2009/04/25 05:31:07  jost
 * Neu: Juristische Personen  können als Mitglied gespeichert werden.
 *
 * Revision 1.3  2009/04/13 11:40:26  jost
 * Neu: Lehrgänge
 *
 * Revision 1.2  2008/12/22 21:20:17  jost
 * Zusatzabbuchung->Zusatzbetrag
 *
 * Revision 1.1  2008/11/16 16:58:49  jost
 * Speicherung der Einstellung von Property-Datei in die Datenbank verschoben.
 *
 **********************************************************************/
package de.jost_net.JVerein.rmi;

import java.rmi.RemoteException;
import java.util.Date;

import de.willuhn.datasource.rmi.DBObject;

public interface Einstellung extends DBObject
{
  public String getID() throws RemoteException;

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

  public String getBeguenstigterzweck() throws RemoteException;

  public void setBeguenstigterzweck(String beguenstigterzweck)
      throws RemoteException;

  public Boolean getMitgliedsbetraege() throws RemoteException;

  public void setMitgliedsbeitraege(Boolean mitgliedsbeitraege)
      throws RemoteException;

  public String getBlz() throws RemoteException;

  public void setBlz(String blz) throws RemoteException;

  public String getKonto() throws RemoteException;

  public void setKonto(String konto) throws RemoteException;

  public Boolean getGeburtsdatumPflicht() throws RemoteException;

  public void setGeburtsdatumPflicht(Boolean geburtsdatumpflicht)
      throws RemoteException;

  public Boolean getEintrittsdatumPflicht() throws RemoteException;

  public void setEintrittsdatumPflicht(Boolean eintrittsdatumpflicht)
      throws RemoteException;

  public Boolean getKommunikationsdaten() throws RemoteException;

  public void setKommunikationsdaten(Boolean kommunikationsdaten)
      throws RemoteException;

  public Boolean getZusatzbetrag() throws RemoteException;

  public void setZusatzbetrag(Boolean zusatzbetrag) throws RemoteException;

  public Boolean getVermerke() throws RemoteException;

  public void setVermerke(Boolean vermerke) throws RemoteException;

  public Boolean getWiedervorlage() throws RemoteException;

  public void setWiedervorlage(Boolean wiedervorlage) throws RemoteException;

  public Boolean getKursteilnehmer() throws RemoteException;

  public void setKursteilnehmer(Boolean kursteilnehmer) throws RemoteException;

  public Boolean getLehrgaenge() throws RemoteException;

  public void setJuristischePersonen(Boolean juristischePersonen)
      throws RemoteException;

  public Boolean getJuristischePersonen() throws RemoteException;

  public void setMitgliedskonto(Boolean mitgliedskonto) throws RemoteException;

  public Boolean getMitgliedskonto() throws RemoteException;

  public void setMitgliedfoto(Boolean mitgliedfoto) throws RemoteException;

  public Boolean getMitgliedfoto() throws RemoteException;

  public void setZusatzadressen(Boolean zusatzadressen) throws RemoteException;

  public Boolean getZusatzadressen() throws RemoteException;

  public void setAuslandsadressen(Boolean auslandsadressen)
      throws RemoteException;

  public Boolean getAuslandsadressen() throws RemoteException;

  public void setArbeitseinsatz(Boolean arbeitseinsatz) throws RemoteException;

  public Boolean getArbeitseinsatz() throws RemoteException;

  public void setDokumentenspeicherung(Boolean dokumentenspeicherung)
      throws RemoteException;

  public Boolean getDokumentenspeicherung() throws RemoteException;

  public void setIndividuelleBeitraege(Boolean individuellebeitraege)
      throws RemoteException;

  public Boolean getIndividuelleBeitraege() throws RemoteException;

  public void setRechnungTextAbbuchung(String rechnungtextabbuchung)
      throws RemoteException;

  public String getRechnungTextAbbuchung() throws RemoteException;

  public void setRechnungTextUeberweisung(String rechnungtextueberweisung)
      throws RemoteException;

  public String getRechnungTextUeberweisung() throws RemoteException;

  public void setRechnungTextBar(String rechnungtextbar) throws RemoteException;

  public String getRechnungTextBar() throws RemoteException;

  public void setLehrgaenge(Boolean lehrgaenge) throws RemoteException;

  public Boolean getExterneMitgliedsnummer() throws RemoteException;

  public void setExterneMitgliedsnummer(Boolean externemitgliedsnummer)
      throws RemoteException;

  public int getBeitragsmodel() throws RemoteException;

  public void setBeitragsmodel(int beitragsmodel) throws RemoteException;

  public String getDateinamenmuster() throws RemoteException;

  public void setDateinamenmuster(String dateinamenmuster)
      throws RemoteException;

  public String getBeginnGeschaeftsjahr() throws RemoteException;

  public void setBeginnGeschaeftsjahr(String beginngeschaeftsjahr)
      throws RemoteException;

  public String getSmtpServer() throws RemoteException;

  public void setSmtpServer(String smtp_server) throws RemoteException;

  public String getSmtpPort() throws RemoteException;

  public void setSmtpPort(String smtp_port) throws RemoteException;

  public String getSmtpAuthUser() throws RemoteException;

  public void setSmtpAuthUser(String smtp_auth_user) throws RemoteException;

  public String getSmtpAuthPwd() throws RemoteException;

  public void setSmtpAuthPwd(String smtp_auth_pwd) throws RemoteException;

  public String getSmtpFromAddress() throws RemoteException;

  public void setSmtpFromAddress(String smtp_from_address)
      throws RemoteException;

  public Boolean getSmtpSsl() throws RemoteException;

  public void setSmtpSsl(Boolean smtp_ssl) throws RemoteException;

  public Boolean getSmtpStarttls() throws RemoteException;

  public void setSmtpStarttls(Boolean smtp_starttls) throws RemoteException;

  public int getZahlungsrhytmus() throws RemoteException;

  public void setZahlungsrhytmus(int zahlungsrhytmus) throws RemoteException;

  public int getZahlungsweg() throws RemoteException;

  public void setZahlungsweg(int zahlungsweg) throws RemoteException;

  public String getAltersgruppen() throws RemoteException;

  public void setAltersgruppen(String altersgruppen) throws RemoteException;

  public String getJubilaeen() throws RemoteException;

  public void setJubilaeen(String jubilaeen) throws RemoteException;

  public String getAltersjubilaeen() throws RemoteException;

  public void setAltersjubilaeen(String altersjubilaeen) throws RemoteException;

  public int getDelaytime() throws RemoteException;

  public void setDelaytime(int delaytime) throws RemoteException;

  public boolean hasZusatzfelder() throws RemoteException;
}
