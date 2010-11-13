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
 * Revision 1.6  2010-10-15 09:58:27  jost
 * Code aufgeräumt
 *
 * Revision 1.5  2009-06-11 21:04:24  jost
 * Vorbereitung I18N
 *
 * Revision 1.4  2008/11/29 13:14:58  jost
 * Refactoring: Warnungen beseitigt.
 *
 * Revision 1.3  2007/03/25 17:04:58  jost
 * Beitragsart aufgenommen.
 *
 * Revision 1.2  2007/02/23 20:28:41  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.1  2006/09/20 15:39:48  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class BeitragsgruppeImpl extends AbstractDBObject implements
    Beitragsgruppe
{

  private static final long serialVersionUID = 1L;

  public BeitragsgruppeImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "beitragsgruppe";
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
      if (getBezeichnung() == null || getBezeichnung().length() == 0)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Bitte Bezeichnung eingeben"));
      }
      if (getBetrag() < 0)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Betrag nicht gültig"));
      }
    }
    catch (RemoteException e)
    {
      Logger.error("insert check of mitglied failed", e);
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Mitglied kann nicht gespeichert werden. Siehe system log"));
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

  public double getBetrag() throws RemoteException
  {
    Double d = (Double) getAttribute("betrag");
    if (d == null)
    {
      return 0;
    }
    return d.doubleValue();
  }

  public void setBetrag(double d) throws RemoteException
  {
    setAttribute("betrag", new Double(d));
  }

  public int getBeitragsArt() throws RemoteException
  {
    Integer i = (Integer) getAttribute("beitragsart");
    if (i == null)
    {
      return 0;
    }
    return i.intValue();
  }

  public void setBeitragsArt(int art) throws RemoteException
  {
    setAttribute("beitragsart", art);
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }
}
