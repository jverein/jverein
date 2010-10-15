/**********************************************************************
 * $Source$
 *  * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * heiner@jverein.de
 * www.jverein.de
 * $Log$
 * Revision 1.3  2009-06-11 21:04:23  jost
 * Vorbereitung I18N
 *
 * Revision 1.2  2008/11/29 13:17:28  jost
 * Refactoring: Warnungen beseitigt.
 *
 * Revision 1.1  2007/05/07 19:27:06  jost
 * Neu: Wiedervorlage
 *
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;
import java.util.Date;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Wiedervorlage;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class WiedervorlageImpl extends AbstractDBObject implements
    Wiedervorlage
{
  private static final long serialVersionUID = 1L;

  public WiedervorlageImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "wiedervorlage";
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
  protected void insertCheck() throws ApplicationException
  {
    try
    {
      if (getDatum() == null)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Bitte Datum eingeben"));
      }
      if (getVermerk() == null)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Bitte Vermerk eingeben"));
      }
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Wiedervorlage kann nicht gespeichert werden. Siehe system log");
      Logger.error(fehler, e);
      throw new ApplicationException(fehler);
    }
  }

  @Override
  protected void updateCheck() throws ApplicationException
  {
    insertCheck();
  }

  @Override
  protected Class getForeignObject(String arg0)
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

  public Date getDatum() throws RemoteException
  {
    return (Date) getAttribute("datum");
  }

  public void setDatum(Date datum) throws RemoteException
  {
    setAttribute("datum", datum);
  }

  public String getVermerk() throws RemoteException
  {
    return (String) getAttribute("vermerk");
  }

  public void setVermerk(String vermerk) throws RemoteException
  {
    setAttribute("vermerk", vermerk);
  }

  public Date getErledigung() throws RemoteException
  {
    return (Date) getAttribute("erledigung");
  }

  public void setErledigung(Date erledigung) throws RemoteException
  {
    setAttribute("erledigung", erledigung);
  }
}
