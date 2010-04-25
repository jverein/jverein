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
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Abrechnungslaeufe;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.util.ApplicationException;

public class AbrechnungslaeufeImpl extends AbstractDBObject implements
    Abrechnungslaeufe
{
  private static final long serialVersionUID = 1L;

  public AbrechnungslaeufeImpl() throws RemoteException
  {
    super();
  }

  protected String getTableName()
  {
    return "abrechnungslaeufe";
  }

  public String getPrimaryAttribute() throws RemoteException
  {
    return "id";
  }

  protected void deleteCheck() throws ApplicationException
  {
  }

  protected void insertCheck() throws ApplicationException
  {
  }

  protected void updateCheck() throws ApplicationException
  {
    insertCheck();
  }

  @SuppressWarnings("unchecked")
  protected Class getForeignObject(String arg0) throws RemoteException
  {
    return null;
  }

  public Date getDatum() throws RemoteException
  {
    Date d = (Date) getAttribute("datum");
    if (d == null)
    {
      return Einstellungen.NODATE;
    }
    return d;
  }

  public void setDatum(Date datum) throws RemoteException
  {
    setAttribute("datum", datum);
  }

  public Integer getModus() throws RemoteException
  {
    Integer modus = (Integer) getAttribute("modus");
    return modus;
  }

  public void setModus(Integer modus) throws RemoteException
  {
    setAttribute("modus", modus);
  }

  public Date getStichtag() throws RemoteException
  {
    Date d = (Date) getAttribute("stichtag");
    if (d == null)
    {
      return Einstellungen.NODATE;
    }
    return d;
  }

  public void setStichtag(Date stichtag) throws RemoteException
  {
    setAttribute("stichtag", stichtag);
  }

  public Date getEingabedatum() throws RemoteException
  {
    Date d = (Date) getAttribute("eingabedatum");
    if (d == null)
    {
      return Einstellungen.NODATE;
    }
    return d;
  }

  public void setEingabedatum(Date eingabedatum) throws RemoteException
  {
    setAttribute("eingabedatum", eingabedatum);
  }

  public String getZahlungsgrund() throws RemoteException
  {
    return (String) getAttribute("zahlungsgrund");
  }

  public void setZahlungsgrund(String zahlungsgrund) throws RemoteException
  {
    setAttribute("zahlungsgrund", zahlungsgrund);
  }

  public boolean getZusatzbetraege() throws RemoteException
  {
    return Util.getBoolean(getAttribute("zusatzbetraege"));
  }

  public void setZusatzbetraege(Boolean zusatzbetraege) throws RemoteException
  {
    setAttribute("zusatzbetraege", zusatzbetraege);
  }

  public boolean getKursteilnehmer() throws RemoteException
  {
    return Util.getBoolean(getAttribute("kursteilnehmer"));
  }

  public void setKursteilnehmer(Boolean kursteilnehmer) throws RemoteException
  {
    setAttribute("kursteilnehmer", kursteilnehmer);
  }

  public boolean getDtausdruck() throws RemoteException
  {
    return Util.getBoolean(getAttribute("dtausdruck"));
  }

  public void setDtausdruck(Boolean dtausdruck) throws RemoteException
  {
    setAttribute("dtausdruck", dtausdruck);
  }

  public Integer getAbbuchungsausgabe() throws RemoteException
  {
    Integer modus = (Integer) getAttribute("abbuchungsausgabe");
    return modus;
  }

  public void setAbbuchungsausgabe(Integer abbuchungsausgabe)
      throws RemoteException
  {
    setAttribute("abbuchungsausgabe", abbuchungsausgabe);
  }

  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }
}
