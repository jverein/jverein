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

  public boolean getRechnungFuerAbbuchung() throws RemoteException;

  public void setRechnungFuerAbbuchung(Boolean rechnungfuerabbuchung)
      throws RemoteException;

  public boolean getRechnungFuerUeberweisung() throws RemoteException;

  public void setRechnungFuerUeberweisung(Boolean rechnungfuerueberweisung)
      throws RemoteException;

  public boolean getRechnungFuerBarzahlung() throws RemoteException;

  public void setRechnungFuerBarzahlung(Boolean rechnungfuerbarzahlung)
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
}
