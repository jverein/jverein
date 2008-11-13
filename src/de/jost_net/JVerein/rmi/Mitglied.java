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
 * Revision 1.9  2008/07/18 20:16:53  jost
 * Neue Methode
 *
 * Revision 1.8  2008/06/29 07:58:45  jost
 * Neu: Handy
 *
 * Revision 1.7  2008/03/08 19:30:47  jost
 * Neu: Externe Mitgliedsnummer
 *
 * Revision 1.6  2007/12/02 13:44:00  jost
 * Neu: Beitragsmodelle
 *
 * Revision 1.5  2007/03/25 17:04:34  jost
 * Herstellung des Familienverbandes
 *
 * Revision 1.4  2007/03/10 20:28:58  jost
 * Neu: Zahlungsweg
 *
 * Revision 1.3  2007/03/10 13:44:56  jost
 * Vermerke eingeführt.
 *
 * Revision 1.2  2007/02/23 20:28:24  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.1  2006/09/20 15:39:35  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.rmi;

import java.rmi.RemoteException;
import java.util.Date;

import de.willuhn.datasource.rmi.DBObject;
import de.willuhn.util.ApplicationException;

public interface Mitglied extends DBObject
{
  public void setExterneMitgliedsnummer(Integer extnr) throws RemoteException;

  public Integer getExterneMitgliedsnummer() throws RemoteException;

  public void setID(String id) throws RemoteException;

  public String getAnrede() throws RemoteException;

  public void setAnrede(String anrede) throws RemoteException;

  public String getTitel() throws RemoteException;

  public void setTitel(String titel) throws RemoteException;

  public String getName() throws RemoteException;

  public void setName(String name) throws RemoteException;

  public String getVorname() throws RemoteException;

  public void setVorname(String vorname) throws RemoteException;

  public String getAdressierungszusatz() throws RemoteException;

  public void setAdressierungszusatz(String adressierungszusatz) throws RemoteException;

  public String getStrasse() throws RemoteException;

  public void setStrasse(String strasse) throws RemoteException;

  public String getPlz() throws RemoteException;

  public void setPlz(String plz) throws RemoteException;

  public String getOrt() throws RemoteException;

  public void setOrt(String ort) throws RemoteException;

  public Integer getZahlungsweg() throws RemoteException;

  public void setZahlungsweg(Integer zahlungsweg) throws RemoteException;

  public Integer getZahlungsrhytmus() throws RemoteException;

  public void setZahlungsrhytmus(Integer zahlungsrhytmus)
      throws RemoteException;

  public String getBlz() throws RemoteException;

  public void setBlz(String blz) throws RemoteException;

  public String getKonto() throws RemoteException;

  public void setKonto(String konto) throws RemoteException;

  public String getKontoinhaber() throws RemoteException;

  public void setKontoinhaber(String kontoinhaber) throws RemoteException;

  public Date getGeburtsdatum() throws RemoteException;

  public void setGeburtsdatum(Date geburtsdatum) throws RemoteException;

  public void setGeburtsdatum(String geburtsdatum) throws RemoteException;

  public String getGeschlecht() throws RemoteException;

  public void setGeschlecht(String geschlecht) throws RemoteException;

  public String getTelefonprivat() throws RemoteException;

  public void setTelefonprivat(String telefonprivat) throws RemoteException;

  public String getTelefondienstlich() throws RemoteException;

  public void setTelefondienstlich(String telefondienstlich)
      throws RemoteException;

  public String getHandy() throws RemoteException;

  public void setHandy(String handy) throws RemoteException;

  public String getEmail() throws RemoteException;

  public void setEmail(String email) throws RemoteException;

  public Date getEintritt() throws RemoteException;

  public void setEintritt(Date eintritt) throws RemoteException;

  public void setEintritt(String eintritt) throws RemoteException;

  public Beitragsgruppe getBeitragsgruppe() throws RemoteException;

  public int getBeitragsgruppeId() throws RemoteException;

  public void setBeitragsgruppe(Integer beitragsgruppe) throws RemoteException;

  public Integer getZahlerID() throws RemoteException;

  public void setZahlerID(Integer id) throws RemoteException;

  public Date getAustritt() throws RemoteException;

  public void setAustritt(Date austritt) throws RemoteException;

  public void setAustritt(String austritt) throws RemoteException;

  public Date getKuendigung() throws RemoteException;

  public void setKuendigung(Date kuendigung) throws RemoteException;

  public void setKuendigung(String kuendigung) throws RemoteException;

  public String getVermerk1() throws RemoteException;

  public void setVermerk1(String vermerk1) throws RemoteException;

  public String getVermerk2() throws RemoteException;

  public void setVermerk2(String vermerk2) throws RemoteException;

  public void insert() throws RemoteException, ApplicationException;

  public void setEingabedatum() throws RemoteException;

  public Date getEingabedatum() throws RemoteException;

  public String getNameVorname() throws RemoteException;
  
  public String getVornameName() throws RemoteException;

  public String getAnschrift() throws RemoteException;

}
