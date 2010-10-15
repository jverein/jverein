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
 * Revision 1.5  2010-07-28 07:28:21  jost
 * deprecated
 *
 * Revision 1.4  2009/06/20 12:34:06  jost
 * Vereinheitlichung der Bezeichner
 *
 * Revision 1.3  2009/06/11 21:04:24  jost
 * Vorbereitung I18N
 *
 * Revision 1.2  2008/11/29 13:16:44  jost
 * Refactoring: Warnungen beseitigt.
 *
 * Revision 1.1  2007/03/13 19:58:53  jost
 * Neu: Manueller Zahlungseingang.
 *
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;
import java.util.Date;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.ManuellerZahlungseingang;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * @deprecated In Version 1.5 ausmustern
 */
@Deprecated
public class ManuellerZahlungseingangImpl extends AbstractDBObject implements
    ManuellerZahlungseingang
{

  private static final long serialVersionUID = 1L;

  public ManuellerZahlungseingangImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "manuellerzahlungseingang";
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
      plausi();
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "ManuellerZahlungseingang kann nicht gespeichert werden. Siehe system log");
      Logger.error(fehler, e);
      throw new ApplicationException(fehler);
    }
  }

  private void plausi() throws RemoteException, ApplicationException
  {
    if (getName() == null || getName().length() == 0)
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Bitte Namen eingeben"));
    }
    if (getVZweck1() == null || getVZweck1().length() == 0)
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Bitte Verwendungszweck 1 eingeben"));
    }
    if (getBetrag() <= 0)
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Bitte Betrag größer als eingeben"));
    }
  }

  @Override
  protected void updateCheck() throws ApplicationException
  {
    try
    {
      plausi();
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "ManuellerZahlungseingang kann nicht gespeichert werden. Siehe system log");
      Logger.error(fehler, e);
      throw new ApplicationException(fehler);
    }
  }

  @Override
  protected Class getForeignObject(String field)
  {
    return null;
  }

  public String getName() throws RemoteException
  {
    return (String) getAttribute("name");
  }

  public void setName(String name) throws RemoteException
  {
    setAttribute("name", name);
  }

  public String getVZweck1() throws RemoteException
  {
    return (String) getAttribute("vzweck1");
  }

  public void setVZweck1(String vzweck1) throws RemoteException
  {
    setAttribute("vzweck1", vzweck1);
  }

  public String getVZweck2() throws RemoteException
  {
    return (String) getAttribute("vzweck2");
  }

  public void setVZweck2(String vzweck2) throws RemoteException
  {
    setAttribute("vzweck2", vzweck2);
  }

  public void setEingabedatum() throws RemoteException
  {
    setAttribute("eingabedatum", new Date());
  }

  public Date getEingabedatum() throws RemoteException
  {
    return (Date) getAttribute("eingabedatum");
  }

  public void setEingangsdatum(Date datum) throws RemoteException
  {
    setAttribute("eingangsdatum", datum);
  }

  public Date getEingangsdatum() throws RemoteException
  {
    return (Date) getAttribute("eingangsdatum");
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

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }
}
