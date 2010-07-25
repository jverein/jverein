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
 * Revision 1.1  2010/05/18 20:25:09  jost
 * Anpassung Klassenname
 *
 * Revision 1.1  2010/04/25 13:56:52  jost
 * Vorarbeiten Mitgliedskonto
 *
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Abrechnungslauf;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.util.ApplicationException;

public class AbrechnungslaufImpl extends AbstractDBObject implements
    Abrechnungslauf
{
  private static final long serialVersionUID = 1L;

  public AbrechnungslaufImpl() throws RemoteException
  {
    super();
  }

  protected String getTableName()
  {
    return "abrechnungslauf";
  }

  public String getPrimaryAttribute() throws RemoteException
  {
    return "idtext";
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

  public Integer getNr() throws RemoteException
  {
    return new Integer(getID());
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

  /**
   * Gibt den Text zur ID aus
   */
  public String getIDText() throws RemoteException
  {
    return getID() + " vom " + Einstellungen.DATEFORMAT.format(getDatum());
  }

  public Object getAttribute(String fieldName) throws RemoteException
  {
    if (fieldName.equals("idtext"))
    {
      return getIDText();
    }
    if (fieldName.equals("nr"))
    {
      return getNr();
    }
    return super.getAttribute(fieldName);
  }
}
