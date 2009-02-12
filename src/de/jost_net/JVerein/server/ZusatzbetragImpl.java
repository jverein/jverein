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
 * Revision 1.1  2008/12/22 21:23:10  jost
 * Zusatzabbuchung->Zusatzbetrag
 *
 * Revision 1.7  2008/12/19 12:25:52  jost
 * Bugfix FÃ¤lligkeitsberechung
 *
 * Revision 1.6  2008/11/29 13:17:37  jost
 * Refactoring: Warnungen beseitigt.
 *
 * Revision 1.5  2008/09/22 20:26:03  jost
 * Plausi korrigiert
 *
 * Revision 1.4  2007/03/30 13:26:12  jost
 * Wiederkehrende Zusatzabbuchungen.
 *
 * Revision 1.3  2007/02/23 20:28:42  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.2  2006/12/20 20:25:44  jost
 * Patch von Ullrich Schäfer, der die Primitive vs. Object Problematik adressiert.
 *
 * Revision 1.1  2006/09/20 15:39:48  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;
import java.util.Date;

import de.jost_net.JVerein.keys.IntervallZusatzzahlung;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Zusatzbetrag;
import de.jost_net.JVerein.util.Datum;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class ZusatzbetragImpl extends AbstractDBObject implements Zusatzbetrag
{
  private static final long serialVersionUID = 1L;

  public ZusatzbetragImpl() throws RemoteException
  {
    super();
  }

  protected String getTableName()
  {
    return "zusatzabbuchung";
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
    try
    {
      if (getStartdatum() == null)
      {
        throw new ApplicationException("Bitte Startdatum eingeben");
      }
      if (getFaelligkeit() == null)
      {
        throw new ApplicationException("Bitte nächste Fälligkeit eingeben");
      }
      if (getIntervall() == null)
      {
        throw new ApplicationException("Bitte Intervall eingeben");
      }
      if (getBuchungstext() == null || getBuchungstext().length() == 0)
      {
        throw new ApplicationException("Bitte Buchungstext eingeben");
      }
      if (getEndedatum() != null)
      {
        if (!Datum
            .isImInterval(getStartdatum(), getEndedatum(), getIntervall()))
        {
          throw new ApplicationException("Endedatum liegt nicht im Intervall");
        }
      }
      if (getFaelligkeit().getTime() < getStartdatum().getTime())
      {
        throw new ApplicationException(
            "Das Fälligkeitsdatum darf nicht vor dem Startdatum liegen");
      }
      if (!Datum
          .isImInterval(getStartdatum(), getFaelligkeit(), getIntervall()))
      {
        throw new ApplicationException(
            "Nächste Fälligkeit liegt nicht im Intervall");
      }
      if (getBetrag() <= 0)
      {
        throw new ApplicationException("Betrag nicht gültig");
      }
    }
    catch (RemoteException e)
    {
      String fehler = "Zusatzbetrag kann nicht gespeichert werden. Siehe system log";
      Logger.error(fehler, e);
      throw new ApplicationException(fehler);
    }
  }

  protected void updateCheck() throws ApplicationException
  {
    insertCheck();
  }

  @SuppressWarnings("unchecked")
  protected Class getForeignObject(String arg0) throws RemoteException
  {
    if ("mitglied".equals(arg0))
    {
      return Mitglied.class;
    }
    return null;
  }

  public Mitglied getMitglied() throws RemoteException
  {
    return (Mitglied) getAttribute("mitglied");
  }

  public void setMitglied(int mitglied) throws RemoteException
  {
    setAttribute("mitglied", new Integer(mitglied));
  }

  public Date getFaelligkeit() throws RemoteException
  {
    return (Date) getAttribute("faelligkeit");
  }

  public void setFaelligkeit(Date faelligkeit) throws RemoteException
  {
    setAttribute("faelligkeit", faelligkeit);
  }

  public String getBuchungstext() throws RemoteException
  {
    return (String) getAttribute("buchungstext");
  }

  public void setBuchungstext(String buchungstext) throws RemoteException
  {
    setAttribute("buchungstext", buchungstext);
  }

  public double getBetrag() throws RemoteException
  {
    Double d = (Double) getAttribute("betrag");
    if (d == null)
      return 0;
    return d.doubleValue();
  }

  public void setBetrag(double d) throws RemoteException
  {
    setAttribute("betrag", new Double(d));
  }

  public Date getAusfuehrung() throws RemoteException
  {
    return (Date) getAttribute("ausfuehrung");
  }

  public Date getStartdatum() throws RemoteException
  {
    return (Date) getAttribute("startdatum");
  }

  public void setStartdatum(Date value) throws RemoteException
  {
    setAttribute("startdatum", value);
  }

  public Integer getIntervall() throws RemoteException
  {
    return (Integer) getAttribute("intervall");
  }

  public String getIntervallText() throws RemoteException
  {
    return IntervallZusatzzahlung.get(getIntervall());
  }

  public void setIntervall(Integer value) throws RemoteException
  {
    setAttribute("intervall", value);
  }

  public Date getEndedatum() throws RemoteException
  {
    return (Date) getAttribute("endedatum");
  }

  public void setEndedatum(Date value) throws RemoteException
  {
    setAttribute("endedatum", value);
  }

  public void setAusfuehrung(Date ausfuehrung) throws RemoteException
  {
    setAttribute("ausfuehrung", ausfuehrung);
  }

  public Object getAttribute(String fieldName) throws RemoteException
  {
    if (fieldName.equals("intervalltext"))
    {
      return getIntervallText();
    }
    if (fieldName.equals("aktiv"))
    {
      return isAktiv();
    }
    return super.getAttribute(fieldName);
  }

  public boolean isAktiv() throws RemoteException
  {
    // Wenn der Auftrag noch nie ausgeführt wurde, ist er auszuführen
    // if (getAusfuehrung() == null)
    // {
    // return true;
    // }
    // Einmalige Ausführung
    if (getIntervall().intValue() == IntervallZusatzzahlung.KEIN)
    {
      // Ist das Ausführungsdatum gesetzt?
      if (getAusfuehrung() == null)
      {
        // nein: Dann ausführen
        return true;
      }
      else
      {
        // ja: nicht mehr ausführen
        return false;
      }
    }

    // Wenn das Endedatum gesetzt ist und das Ausführungsdatum liegt hinter
    // dem Endedatum: nicht mehr ausführen
    if (getEndedatum() != null && getAusfuehrung() != null
        && getAusfuehrung().getTime() >= getEndedatum().getTime())
    {
      return false;
    }
    if (getFaelligkeit().getTime() <= Datum.getHeute().getTime())
    {
      return true;
    }

    return false;
  }
}
