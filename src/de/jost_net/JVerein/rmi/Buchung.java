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
 **********************************************************************/
package de.jost_net.JVerein.rmi;

import java.rmi.RemoteException;
import java.util.Date;

import de.willuhn.datasource.rmi.DBObject;

public interface Buchung extends DBObject
{
  public Integer getUmsatzid() throws RemoteException;

  public void setUmsatzid(Integer umsatzid) throws RemoteException;

  public Konto getKonto() throws RemoteException;

  public void setKonto(Konto konto) throws RemoteException;

  public Integer getAuszugsnummer() throws RemoteException;

  public void setAuszugsnummer(Integer auszugsnummer) throws RemoteException;

  public Integer getBlattnummer() throws RemoteException;

  public void setBlattnummer(Integer blattnummer) throws RemoteException;

  public String getName() throws RemoteException;

  public void setName(String name) throws RemoteException;

  public double getBetrag() throws RemoteException;

  public void setBetrag(double betrag) throws RemoteException;

  public String getZweck() throws RemoteException;

  public void setZweck(String zweck) throws RemoteException;

  public String getZweck2() throws RemoteException;

  public void setZweck2(String zweck2) throws RemoteException;

  public Date getDatum() throws RemoteException;

  public void setDatum(Date datum) throws RemoteException;

  public String getArt() throws RemoteException;

  public void setArt(String art) throws RemoteException;

  public String getKommentar() throws RemoteException;

  public void setKommentar(String kommentar) throws RemoteException;

  public Buchungsart getBuchungsart() throws RemoteException;

  public int getBuchungsartId() throws RemoteException;

  public void setBuchungsart(Integer buchungsart) throws RemoteException;

  public Abrechnungslauf getAbrechnungslauf() throws RemoteException;

  public int getAbrechnungslaufID() throws RemoteException;

  public void setAbrechnungslauf(Abrechnungslauf abrechnungslauf)
      throws RemoteException;

  public void setAbrechnungslauf(Integer abrechnungslauf)
      throws RemoteException;

  public Mitgliedskonto getMitgliedskonto() throws RemoteException;

  public int getMitgliedskontoID() throws RemoteException;

  public void setMitgliedskonto(Mitgliedskonto mitgliedskonto)
      throws RemoteException;

  public void setMitgliedskontoID(Integer mitgliedskonto)
      throws RemoteException;

  public Jahresabschluss getJahresabschluss() throws RemoteException;

  public Integer getSplitId() throws RemoteException;

  public void setSplitId(Integer splitid) throws RemoteException;

  public Spendenbescheinigung getSpendenbescheinigung() throws RemoteException;

  public void setSpendenbescheinigungId(Integer spendenbescheinigung)
      throws RemoteException;
}
