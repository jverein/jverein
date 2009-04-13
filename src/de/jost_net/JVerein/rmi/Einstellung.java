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

  public void setZusatzbetrag(Boolean zusatzbetrag)
      throws RemoteException;

  public boolean getVermerke() throws RemoteException;

  public void setVermerke(Boolean vermerke) throws RemoteException;

  public boolean getWiedervorlage() throws RemoteException;

  public void setWiedervorlage(Boolean wiedervorlage) throws RemoteException;

  public boolean getKursteilnehmer() throws RemoteException;

  public void setKursteilnehmer(Boolean kursteilnehmer) throws RemoteException;
  
  public boolean getLehrgaenge() throws RemoteException;
  
  public void setLehrgaenge(Boolean lehrgaenge) throws RemoteException;

  public boolean getExterneMitgliedsnummer() throws RemoteException;

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

  public boolean getRechnungFuerAbbuchung() throws RemoteException;

  public void setRechnungFuerAbbuchung(Boolean rechnungfuerabbuchung)
      throws RemoteException;

  public boolean getRechnungFuerUeberweisung() throws RemoteException;

  public void setRechnungFuerUeberweisung(Boolean rechnungfuerueberweisung)
      throws RemoteException;

  public boolean getRechnungFuerBarzahlung() throws RemoteException;

  public void setRechnungFuerBarzahlung(Boolean rechnungfuerbarzahlung)
      throws RemoteException;

}
