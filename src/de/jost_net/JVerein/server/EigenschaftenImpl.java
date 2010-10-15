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
 * Revision 1.4  2009-11-17 21:03:14  jost
 * Neu: Eigenschaft und EigenschaftGruppe
 *
 * Revision 1.3  2009/06/11 21:04:23  jost
 * Vorbereitung I18N
 *
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
import de.jost_net.JVerein.rmi.Eigenschaft;
import de.jost_net.JVerein.rmi.Eigenschaften;
import de.jost_net.JVerein.rmi.Mitglied;
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

  @Override
  protected String getTableName()
  {
    return "eigenschaften";
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
    else if ("eigenschaft".equals(arg0))
    {
      return Eigenschaft.class;
    }
    return null;
  }

  public Mitglied getMitglied() throws RemoteException
  {
    return (Mitglied) getAttribute("mitglied");
  }

  public void setMitglied(String mitglied) throws RemoteException
  {
    setAttribute("mitglied", new Integer(mitglied));
  }

  public Eigenschaft getEigenschaft() throws RemoteException
  {
    return (Eigenschaft) getAttribute("eigenschaft");
  }

  public void setEigenschaft(String eigenschaft) throws RemoteException
  {
    setAttribute("eigenschaft", eigenschaft);
  }

}
