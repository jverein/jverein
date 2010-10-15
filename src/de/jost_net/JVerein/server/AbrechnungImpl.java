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
 * Revision 1.4  2010-07-25 18:46:31  jost
 * Neu: Mitgliedskonto
 *
 * Revision 1.3  2009/06/11 21:04:23  jost
 * Vorbereitung I18N
 *
 * Revision 1.2  2008/08/13 19:16:23  jost
 * Bugfix Spalte Mitglied
 *
 * Revision 1.1  2008/08/10 12:37:57  jost
 * Vorbereitung der Rechnungserstellung
 *
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;
import java.util.Date;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Abrechnung;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class AbrechnungImpl extends AbstractDBObject implements Abrechnung
{
  private static final long serialVersionUID = -7659952706648229169L;

  public AbrechnungImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "abrechnung";
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
      if (getBetrag() < 0)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Betrag nicht gültig"));
      }
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Abrechung kann nicht gespeichert werden. Siehe system log");
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

  public void setMitglied(Mitglied mitglied) throws RemoteException
  {
    setAttribute("mitglied", new Integer(mitglied.getID()));
  }

  public Date getDatum() throws RemoteException
  {
    return (Date) getAttribute("datum");
  }

  public void setDatum(Date datum) throws RemoteException
  {
    setAttribute("datum", datum);
  }

  public String getZweck1() throws RemoteException
  {
    return (String) getAttribute("zweck1");
  }

  public void setZweck1(String zweck1) throws RemoteException
  {
    if (zweck1.length() > 27)
    {
      zweck1 = zweck1.substring(0, 27);
    }
    setAttribute("zweck1", zweck1);
  }

  public String getZweck2() throws RemoteException
  {
    return (String) getAttribute("zweck2");
  }

  public void setZweck2(String zweck2) throws RemoteException
  {
    if (zweck2.length() > 27)
    {
      zweck2 = zweck2.substring(0, 27);
    }
    setAttribute("zweck2", zweck2);
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
