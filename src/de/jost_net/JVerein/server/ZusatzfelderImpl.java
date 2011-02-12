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
 * Revision 1.7  2010-11-13 09:31:24  jost
 * Warnings entfernt.
 *
 * Revision 1.6  2010-10-15 09:58:27  jost
 * Code aufgeräumt
 *
 * Revision 1.5  2010-09-14 15:41:03  jost
 * Vermeidung NPE
 *
 * Revision 1.4  2010-09-07 17:00:29  jost
 * neue Methode
 *
 * Revision 1.3  2010/01/01 20:12:19  jost
 * Typisierung der Zusatzfelder
 *
 * Revision 1.2  2008/11/29 13:17:46  jost
 * Refactoring: Warnungen beseitigt.
 *
 * Revision 1.1  2008/04/10 19:03:24  jost
 * Neu: Benutzerdefinierte Datenfelder
 *
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.keys.Datentyp;
import de.jost_net.JVerein.rmi.Felddefinition;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Zusatzfelder;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;

public class ZusatzfelderImpl extends AbstractDBObject implements Zusatzfelder
{

  private static final long serialVersionUID = 1L;

  public ZusatzfelderImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "zusatzfelder";
  }

  @Override
  public String getPrimaryAttribute()
  {
    return "id";
  }

  @Override
  protected void deleteCheck()
  {
    //
  }

  @Override
  protected void insertCheck()
  {
    // try
    // {
    // //
    // }
    // catch (RemoteException e)
    // {
    // String fehler = "Zusatzfeld kann nicht gespeichert werden. Siehe system
    // log";
    // Logger.error(fehler, e);
    // throw new ApplicationException(fehler);
    // }
  }

  @Override
  protected void updateCheck()
  {
    insertCheck();
  }

  @Override
  protected Class<?> getForeignObject(String arg0)
  {
    if ("mitglied".equals(arg0))
    {
      return Mitglied.class;
    }
    if ("felddefinition".equals(arg0))
    {
      return Felddefinition.class;
    }
    return null;
  }

  public Mitglied getMitglied() throws RemoteException
  {
    return (Mitglied) getAttribute("mitglied");
  }

  public void setMitglied(int mitglied) throws RemoteException
  {
    setAttribute("mitglied", Integer.valueOf(mitglied));
  }

  public Felddefinition getFelddefinition() throws RemoteException
  {
    return (Felddefinition) getAttribute("felddefinition");
  }

  public void setFelddefinition(int felddefinition) throws RemoteException
  {
    setAttribute("felddefinition", Integer.valueOf(felddefinition));
  }

  public String getFeld() throws RemoteException
  {
    return (String) getAttribute("feld");
  }

  public void setFeld(String feld) throws RemoteException
  {
    setAttribute("feld", feld);
  }

  public Date getFeldDatum() throws RemoteException
  {
    return (Date) getAttribute("felddatum");
  }

  public void setFeldDatum(Date datum) throws RemoteException
  {
    setAttribute("felddatum", datum);
  }

  public Integer getFeldGanzzahl() throws RemoteException
  {
    return (Integer) getAttribute("feldganzzahl");
  }

  public void setFeldGanzzahl(Integer ganzzahl) throws RemoteException
  {
    setAttribute("feldganzzahl", ganzzahl);
  }

  public double getFeldGleitkommazahl() throws RemoteException
  {
    return (Double) getAttribute("feldgleitkommazahl");
  }

  public void setFeldGleitkommazahl(double gleitkommazahl)
      throws RemoteException
  {
    setAttribute("feldgleitkommazahl", gleitkommazahl);
  }

  public BigDecimal getFeldWaehrung() throws RemoteException
  {
    return (BigDecimal) getAttribute("feldwaehrung");
  }

  public void setFeldWaehrung(BigDecimal waehrung) throws RemoteException
  {
    setAttribute("feldwaehrung", waehrung);
  }

  public Boolean getFeldJaNein() throws RemoteException
  {
    return Util.getBoolean(getAttribute("feldjanein"));
  }

  public void setFeldJaNein(Boolean janein) throws RemoteException
  {
    setAttribute("feldjanein", janein);
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }

  public String getString()
  {

    try
    {
      int typ = getFelddefinition().getDatentyp();
      switch (typ)
      {
        case Datentyp.DATUM:
          if (getFeldDatum() != null)
          {
            return new JVDateFormatTTMMJJJJ().format(getFeldDatum());
          }
          else
          {
            return "";
          }
        case Datentyp.GANZZAHL:
          return getFeldGanzzahl() + "";
        case Datentyp.JANEIN:
          return getFeldJaNein() ? "ja" : "nein";
        case Datentyp.WAEHRUNG:
          if (getFeldWaehrung() != null)
          {
            return Einstellungen.DECIMALFORMAT.format(getFeldWaehrung());
          }
          else
          {
            return "";
          }
        case Datentyp.ZEICHENFOLGE:
          return getFeld();
        default:
          return "ungültiger Datentyp";
      }
    }
    catch (RemoteException e)
    {
      Logger.error("Fehler", e);
      return e.getMessage();
    }
  }

}
