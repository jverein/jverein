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
 * Revision 1.2  2008/11/29 13:15:27  jost
 * Refactoring: Warnungen beseitigt.
 *
 * Revision 1.1  2008/01/25 16:07:06  jost
 * Neu: Eigenschaften des Mitgliedes
 *
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Eigenschaften;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class EigenschaftenImpl extends AbstractDBObject implements
    Eigenschaften
{
  private static final long serialVersionUID = -5906609226109964967L;

  public EigenschaftenImpl() throws RemoteException
  {
    super();
  }

  protected String getTableName()
  {
    return "eigenschaften";
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
      if (getEigenschaft() == null)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Bitte Eigenschaft eingeben"));
      }
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Eigenschaft kann nicht gespeichert werden. Siehe system log");
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

  public String getEigenschaft() throws RemoteException
  {
    return (String) getAttribute("eigenschaft");
  }

  public void setEigenschaft(String eigenschaft) throws RemoteException
  {
    setAttribute("eigenschaft", eigenschaft);
  }

}
