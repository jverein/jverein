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
 * Revision 1.1  2008/04/10 19:03:24  jost
 * Neu: Benutzerdefinierte Datenfelder
 *
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;

import de.jost_net.JVerein.rmi.Felddefinition;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Zusatzfelder;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.util.ApplicationException;

public class ZusatzfelderImpl extends AbstractDBObject implements Zusatzfelder
{
  private static final long serialVersionUID = 1L;

  public ZusatzfelderImpl() throws RemoteException
  {
    super();
  }

  protected String getTableName()
  {
    return "zusatzfelder";
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
    setAttribute("mitglied", new Integer(mitglied));
  }

  public Felddefinition getFelddefinition() throws RemoteException
  {
    return (Felddefinition) getAttribute("felddefinition");
  }

  public void setFelddefinition(int felddefinition) throws RemoteException
  {
    setAttribute("felddefinition", new Integer(felddefinition));
  }

  public String getFeld() throws RemoteException
  {
    return (String) getAttribute("feld");
  }

  public void setFeld(String feld) throws RemoteException
  {
    setAttribute("feld", feld);
  }

  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }

}
