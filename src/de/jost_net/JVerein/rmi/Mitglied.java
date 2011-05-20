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
 * Revision 1.19  2011-05-06 15:02:28  jost
 * Neue Variablenmimik
 *
 * Revision 1.18  2011-04-23 06:57:18  jost
 * Neu: Freie Formulare
 *
 * Revision 1.17  2011-01-30 10:30:12  jost
 * Datum der letzten Änderung wird gespeichert
 *
 * Revision 1.16  2011-01-27 22:24:16  jost
 * Neu: Speicherung von weiteren Adressen in der Mitgliedertabelle
 *
 * Revision 1.15  2010-10-30 11:31:51  jost
 * Neu: Sterbetag
 *
 * Revision 1.14  2010-10-28 19:15:50  jost
 * Neu: Wohnsitzstaat
 *
 * Revision 1.13  2010-08-27 19:08:41  jost
 * neu: Mitgliedsfoto
 *
 * Revision 1.12  2009/10/20 18:01:17  jost
 * Neu: Anzeige IBAN
 *
 * Revision 1.11  2009/04/25 05:31:24  jost
 * Neu: Juristische Personen  können als Mitglied gespeichert werden.
 *
 * Revision 1.10  2008/11/13 20:18:28  jost
 * Adressierungszusatz aufgenommen.
 *
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
 * Vermerke eingefÃ¼hrt.
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
import java.util.Map;

import de.willuhn.datasource.rmi.DBObject;
import de.willuhn.util.ApplicationException;

public interface Mitglied extends DBObject
{
  public void setExterneMitgliedsnummer(Integer extnr) throws RemoteException;

  public Integer getExterneMitgliedsnummer() throws RemoteException;

  public void setID(String id) throws RemoteException;

  public void setAdresstyp(Integer adresstyp) throws RemoteException;

  public Adresstyp getAdresstyp() throws RemoteException;

  public String getPersonenart() throws RemoteException;

  public void setPersonenart(String personenart) throws RemoteException;

  public String getAnrede() throws RemoteException;

  public void setAnrede(String anrede) throws RemoteException;

  public String getTitel() throws RemoteException;

  public void setTitel(String titel) throws RemoteException;

  public String getName() throws RemoteException;

  public void setName(String name) throws RemoteException;

  public String getVorname() throws RemoteException;

  public void setVorname(String vorname) throws RemoteException;

  public String getAdressierungszusatz() throws RemoteException;

  public void setAdressierungszusatz(String adressierungszusatz)
      throws RemoteException;

  public String getStrasse() throws RemoteException;

  public void setStrasse(String strasse) throws RemoteException;

  public String getPlz() throws RemoteException;

  public void setPlz(String plz) throws RemoteException;

  public String getOrt() throws RemoteException;

  public void setOrt(String ort) throws RemoteException;

  public String getStaat() throws RemoteException;

  public void setStaat(String staat) throws RemoteException;

  public Integer getZahlungsweg() throws RemoteException;

  public void setZahlungsweg(Integer zahlungsweg) throws RemoteException;

  public Integer getZahlungsrhytmus() throws RemoteException;

  public void setZahlungsrhytmus(Integer zahlungsrhytmus)
      throws RemoteException;

  public String getBlz() throws RemoteException;

  public void setBlz(String blz) throws RemoteException;

  public String getKonto() throws RemoteException;

  public void setKonto(String konto) throws RemoteException;

  public String getIban() throws RemoteException;

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

  public double getIndividuellerBeitrag() throws RemoteException;

  public void setIndividuellerBeitrag(double individuellerbeitrag)
      throws RemoteException;

  public Integer getZahlerID() throws RemoteException;

  public void setZahlerID(Integer id) throws RemoteException;

  public Date getAustritt() throws RemoteException;

  public void setAustritt(Date austritt) throws RemoteException;

  public void setAustritt(String austritt) throws RemoteException;

  public Date getKuendigung() throws RemoteException;

  public void setKuendigung(Date kuendigung) throws RemoteException;

  public void setKuendigung(String kuendigung) throws RemoteException;

  public Date getSterbetag() throws RemoteException;

  public void setSterbetag(Date sterbetag) throws RemoteException;

  public void setSterbetag(String sterbetag) throws RemoteException;

  public String getVermerk1() throws RemoteException;

  public void setVermerk1(String vermerk1) throws RemoteException;

  public String getVermerk2() throws RemoteException;

  public void setVermerk2(String vermerk2) throws RemoteException;

  public void insert() throws RemoteException, ApplicationException;

  public void setEingabedatum() throws RemoteException;

  public Date getEingabedatum() throws RemoteException;

  public void setLetzteAenderung() throws RemoteException;

  public Date getLetzteAenderung() throws RemoteException;

  public String getNameVorname() throws RemoteException;

  public String getVornameName() throws RemoteException;

  public String getAnschrift() throws RemoteException;

  public Mitgliedfoto getFoto() throws RemoteException;

  public void setFoto(Mitgliedfoto foto) throws RemoteException;

  public String getEmpfaenger() throws RemoteException;

  public Map<String, Object> getMap(Map<String, Object> map)
      throws RemoteException;

}
