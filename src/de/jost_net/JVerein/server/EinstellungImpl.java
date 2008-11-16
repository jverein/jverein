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
 *
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;

import de.jost_net.JVerein.rmi.Einstellung;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.util.ApplicationException;

public class EinstellungImpl extends AbstractDBObject implements Einstellung
{

  private static final long serialVersionUID = 3513343626868776722L;

  public EinstellungImpl() throws RemoteException
  {
    super();
  }

  protected String getTableName()
  {
    return "einstellung";
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

  public void setID() throws RemoteException
  {
    setAttribute("id", "1");
  }

  public boolean getGeburtsdatumPflicht() throws RemoteException
  {
    Object o = getAttribute("geburtsdatumpflicht");
    if (o instanceof Boolean)
    {
      return (Boolean) o;
    }
    if (o instanceof String)
    {
      return new Boolean((String) o);
    }
    throw new RemoteException("Weder String noch Boolean");

  }

  public void setGeburtsdatumPflicht(Boolean geburtsdatumpflicht)
      throws RemoteException
  {
    setAttribute("geburtsdatumpflicht", new Boolean(geburtsdatumpflicht));
  }

  public boolean getEintrittsdatumPflicht() throws RemoteException
  {
    Object o = getAttribute("eintrittsdatumpflicht");
    if (o instanceof Boolean)
    {
      return (Boolean) o;
    }
    if (o instanceof String)
    {
      return new Boolean((String) o);
    }
    throw new RemoteException("Weder String noch Boolean");
  }

  public void setEintrittsdatumPflicht(Boolean eintrittsdatumpflicht)
      throws RemoteException
  {
    setAttribute("eintrittsdatumpflicht", new Boolean(eintrittsdatumpflicht));
  }

  public boolean getKommunikationsdaten() throws RemoteException
  {
    Object o = getAttribute("kommunikationsdaten");
    if (o instanceof Boolean)
    {
      return (Boolean) o;
    }
    if (o instanceof String)
    {
      return new Boolean((String) o);
    }
    throw new RemoteException("Weder String noch Boolean");
  }

  public void setKommunikationsdaten(Boolean kommunikationsdaten)
      throws RemoteException
  {
    setAttribute("kommunikationsdaten", new Boolean(kommunikationsdaten));
  }

  public boolean getZusatzabbuchung() throws RemoteException
  {
    Object o = getAttribute("zusatzabbuchung");
    if (o instanceof Boolean)
    {
      return (Boolean) o;
    }
    if (o instanceof String)
    {
      return new Boolean((String) o);
    }
    throw new RemoteException("Weder String noch Boolean");
  }

  public void setZusatzabbuchung(Boolean zusatzabbuchung)
      throws RemoteException
  {
    setAttribute("zusatzabbuchung", new Boolean(zusatzabbuchung));
  }

  public boolean getVermerke() throws RemoteException
  {
    Object o = getAttribute("vermerke");
    if (o instanceof Boolean)
    {
      return (Boolean) o;
    }
    if (o instanceof String)
    {
      return new Boolean((String) o);
    }
    throw new RemoteException("Weder String noch Boolean");
  }

  public void setVermerke(Boolean vermerke) throws RemoteException
  {
    setAttribute("vermerke", new Boolean(vermerke));
  }

  public boolean getWiedervorlage() throws RemoteException
  {
    Object o = getAttribute("wiedervorlage");
    if (o instanceof Boolean)
    {
      return (Boolean) o;
    }
    if (o instanceof String)
    {
      return new Boolean((String) o);
    }
    throw new RemoteException("Weder String noch Boolean");
  }

  public void setWiedervorlage(Boolean wiedervorlage) throws RemoteException
  {
    setAttribute("wiedervorlage", new Boolean(wiedervorlage));
  }

  public boolean getKursteilnehmer() throws RemoteException
  {
    Object o = getAttribute("kursteilnehmer");
    if (o instanceof Boolean)
    {
      return (Boolean) o;
    }
    if (o instanceof String)
    {
      return new Boolean((String) o);
    }
    throw new RemoteException("Weder String noch Boolean");
  }

  public void setKursteilnehmer(Boolean kursteilnehmer) throws RemoteException
  {
    setAttribute("kursteilnehmer", new Boolean(kursteilnehmer));
  }

  public boolean getExterneMitgliedsnummer() throws RemoteException
  {
    Object o = getAttribute("externemitgliedsnummer");
    if (o instanceof Boolean)
    {
      return (Boolean) o;
    }
    if (o instanceof String)
    {
      return new Boolean((String) o);
    }
    throw new RemoteException("Weder String noch Boolean");
  }

  public void setExterneMitgliedsnummer(Boolean externemitgliedsnummer)
      throws RemoteException
  {
    setAttribute("externemitgliedsnummer", new Boolean(externemitgliedsnummer));
  }

  public int getBeitragsmodel() throws RemoteException
  {
    return (Integer) getAttribute("beitragsmodel");
  }

  public void setBeitragsmodel(int beitragsmodel) throws RemoteException
  {
    setAttribute("beitragsmodel", beitragsmodel);
  }

  public String getDateinamenmuster() throws RemoteException
  {
    return (String) getAttribute("dateinamenmuster");
  }

  public void setDateinamenmuster(String dateinamenmuster)
      throws RemoteException
  {
    setAttribute("dateinamenmuster", dateinamenmuster);
  }

  public String getBeginnGeschaeftsjahr() throws RemoteException
  {
    return (String) getAttribute("beginngeschaeftsjahr");
  }

  public void setBeginnGeschaeftsjahr(String beginngeschaeftsjahr)
      throws RemoteException
  {
    setAttribute("beginngeschaeftsjahr", beginngeschaeftsjahr);
  }

  public boolean getRechnungFuerAbbuchung() throws RemoteException
  {
    Object o = getAttribute("rechnungfuerabbuchung");
    if (o instanceof Boolean)
    {
      return (Boolean) o;
    }
    if (o instanceof String)
    {
      return new Boolean((String) o);
    }
    throw new RemoteException("Weder String noch Boolean");
  }

  public void setRechnungFuerAbbuchung(Boolean rechnungfuerabbuchung)
      throws RemoteException
  {
    setAttribute("rechnungfuerabbuchung", new Boolean(rechnungfuerabbuchung));
  }

  public boolean getRechnungFuerUeberweisung() throws RemoteException
  {
    Object o = getAttribute("rechnungfuerueberweisung");
    if (o instanceof Boolean)
    {
      return (Boolean) o;
    }
    if (o instanceof String)
    {
      return new Boolean((String) o);
    }
    throw new RemoteException("Weder String noch Boolean");
  }

  public void setRechnungFuerUeberweisung(Boolean rechnungfuerueberweisung)
      throws RemoteException
  {
    setAttribute("rechnungfuerueberweisung", new Boolean(
        rechnungfuerueberweisung));
  }

  public boolean getRechnungFuerBarzahlung() throws RemoteException
  {
    Object o = getAttribute("rechnungfuerbarzahlung");
    if (o instanceof Boolean)
    {
      return (Boolean) o;
    }
    if (o instanceof String)
    {
      return new Boolean((String) o);
    }
    throw new RemoteException("Weder String noch Boolean");
   }

  public void setRechnungFuerBarzahlung(Boolean rechnungfuerbarzahlung)
      throws RemoteException
  {
    setAttribute("rechnungfuerbarzahlung", new Boolean(rechnungfuerbarzahlung));
  }

}
