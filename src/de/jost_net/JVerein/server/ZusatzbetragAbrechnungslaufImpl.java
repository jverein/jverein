/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * This program is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,  but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See 
 *  the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, 
 * see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de
 * www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;
import java.util.Date;

import de.jost_net.JVerein.rmi.Abrechnungslauf;
import de.jost_net.JVerein.rmi.Zusatzbetrag;
import de.jost_net.JVerein.rmi.ZusatzbetragAbrechnungslauf;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.util.ApplicationException;

public class ZusatzbetragAbrechnungslaufImpl extends AbstractDBObject implements
    ZusatzbetragAbrechnungslauf
{
  private static final long serialVersionUID = 6084498628933735690L;

  public ZusatzbetragAbrechnungslaufImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "zusatzbetragabrechnungslauf";
  }

  @Override
  public String getPrimaryAttribute()
  {
    return "mitglied";
  }

  @Override
  protected void deleteCheck()
  {
    //
  }

  @Override
  protected void insertCheck() throws ApplicationException
  {
    //
  }

  @Override
  protected void updateCheck() throws ApplicationException
  {
    insertCheck();
  }

  @Override
  protected Class<?> getForeignObject(String arg0)
  {
    if ("zusatzbetrag".equals(arg0))
    {
      return Zusatzbetrag.class;
    }
    if ("abrechnungslauf".equals(arg0))
    {
      return Abrechnungslauf.class;
    }
    return null;
  }

  public Abrechnungslauf getAbrechnungslauf() throws RemoteException
  {
    return (Abrechnungslauf) getAttribute("abrechnungslauf");
  }

  public void setAbrechnungslauf(Abrechnungslauf abrechnungslauf)
      throws RemoteException
  {
    setAttribute("abrechnungslauf", new Integer(abrechnungslauf.getID()));
  }

  public Zusatzbetrag getZusatzbetrag() throws RemoteException
  {
    return (Zusatzbetrag) getAttribute("zusatzbetrag");
  }

  public void setZusatzbetrag(Zusatzbetrag zusatzbetrag) throws RemoteException
  {
    setAttribute("zusatzbetrag", new Integer(zusatzbetrag.getID()));
  }

  public Date getLetzteAusfuehrung() throws RemoteException
  {
    return (Date) getAttribute("letzteausfuehrung");
  }

  public void setLetzteAusfuehrung(Date letzteausfuehrung)
      throws RemoteException
  {
    setAttribute("letzteausfuehrung", letzteausfuehrung);
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }

}
