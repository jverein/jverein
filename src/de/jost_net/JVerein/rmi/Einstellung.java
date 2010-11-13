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

import de.willuhn.datasource.rmi.DBObject;

public interface Einstellung extends DBObject
{
  public String getID() throws RemoteException;

  public void setID() throws RemoteException;

  public boolean getGeburtsdatumPflicht() throws RemoteException;

  public void setGeburtsdatumPflicht(Boolean geburtsdatumpflicht)
      throws RemoteException;

  public boolean getEintrittsdatumPflicht() throws RemoteException;

  public void setEintrittsdatumPflicht(Boolean eintrittsdatumpflicht)
      throws RemoteException;

  public boolean getKommunikationsdaten() throws RemoteException;

  public void setKommunikationsdaten(Boolean kommunikationsdaten)
      throws RemoteException;

  public boolean getZusatzbetrag() throws RemoteException;

  public void setZusatzbetrag(Boolean zusatzbetrag) throws RemoteException;

  public boolean getVermerke() throws RemoteException;

  public void setVermerke(Boolean vermerke) throws RemoteException;

  public boolean getWiedervorlage() throws RemoteException;

  public void setWiedervorlage(Boolean wiedervorlage) throws RemoteException;

  public boolean getKursteilnehmer() throws RemoteException;

  public void setKursteilnehmer(Boolean kursteilnehmer) throws RemoteException;

  public boolean getLehrgaenge() throws RemoteException;

  public void setJuristischePersonen(Boolean juristischePersonen)
      throws RemoteException;

  public boolean getJuristischePersonen() throws RemoteException;

  public void setMitgliedskonto(Boolean mitgliedskonto) throws RemoteException;

  public boolean getMitgliedskonto() throws RemoteException;

  public void setMitgliedfoto(Boolean mitgliedfoto) throws RemoteException;

  public boolean getMitgliedfoto() throws RemoteException;

  public void setAuslandsadressen(Boolean auslandsadressen)
      throws RemoteException;

  public boolean getAuslandsadressen() throws RemoteException;

  public void setRechnungTextAbbuchung(String rechnungtextabbuchung)
      throws RemoteException;

  public String getRechnungTextAbbuchung() throws RemoteException;

  public void setRechnungTextUeberweisung(String rechnungtextueberweisung)
      throws RemoteException;

  public String getRechnungTextUeberweisung() throws RemoteException;

  public void setRechnungTextBar(String rechnungtextbar) throws RemoteException;

  public String getRechnungTextBar() throws RemoteException;

  public void setLehrgaenge(Boolean lehrgaenge) throws RemoteException;

  public boolean getExterneMitgliedsnummer() throws RemoteException;

  public void setExterneMitgliedsnummer(Boolean externemitgliedsnummer)
      throws RemoteException;

  public Integer getAktuelleGeburtstageVorher() throws RemoteException;

  public void setAktuelleGeburtstageVorher(Integer vorher)
      throws RemoteException;

  public Integer getAktuelleGeburtstageNachher() throws RemoteException;

  public void setAktuelleGeburtstageNachher(Integer vorher)
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

  public int getZahlungsrhytmus() throws RemoteException;

  public void setZahlungsrhytmus(int zahlungsrhytmus) throws RemoteException;

  public int getZahlungsweg() throws RemoteException;

  public void setZahlungsweg(int zahlungsweg) throws RemoteException;
}
