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
 * Revision 1.5  2010-11-27 19:29:35  jost
 * Optional: max. eine Eigenschaft auswählbar
 *
 * Revision 1.4  2010-11-13 09:29:39  jost
 * Warnings entfernt.
 *
 * Revision 1.3  2010-10-15 09:58:27  jost
 * Code aufgeräumt
 *
 * Revision 1.2  2010-09-09 18:51:13  jost
 * Eigenschaftengruppen können jetzt auch das Merkmal "Pflicht" haben. Dann muß mindestens eine Eigenschaft ausgewählt werden.
 *
 * Revision 1.1  2009/11/17 21:03:38  jost
 * Neu: Eigenschaft und EigenschaftGruppe
 *
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.EigenschaftGruppe;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class EigenschaftGruppeImpl extends AbstractDBObject implements
    EigenschaftGruppe
{
  private static final long serialVersionUID = -5906609226109964967L;

  public EigenschaftGruppeImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "eigenschaftgruppe";
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
      if (getBezeichnung() == null)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Bitte Bezeichnung eingeben"));
      }
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "EigenschaftGruppe kann nicht gespeichert werden. Siehe system log");
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
  protected Class<?> getForeignObject(String arg0)
  {
    return null;
  }

  public String getBezeichnung() throws RemoteException
  {
    return (String) getAttribute("bezeichnung");
  }

  public void setBezeichnung(String bezeichnung) throws RemoteException
  {
    setAttribute("bezeichnung", bezeichnung);
  }

  public Boolean getPflicht() throws RemoteException
  {
    return Util.getBoolean(getAttribute("pflicht"));
  }

  public void setPflicht(Boolean pflicht) throws RemoteException
  {
    setAttribute("pflicht", pflicht);
  }

  public Boolean getMax1() throws RemoteException
  {
    return Util.getBoolean(getAttribute("max1"));
  }

  public void setMax1(Boolean max1) throws RemoteException
  {
    setAttribute("max1", max1);
  }

}
