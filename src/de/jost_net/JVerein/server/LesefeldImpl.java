/**********************************************************************
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

import de.jost_net.JVerein.rmi.Lesefeld;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class LesefeldImpl extends AbstractDBObject implements Lesefeld
{

  private static final long serialVersionUID = 1610434197155984031L;

  private String sampleContent;

  public LesefeldImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "lesefeld";
  }

  @Override
  public String getPrimaryAttribute()
  {
    return "id";
  }

  @Override
  protected void insertCheck() throws ApplicationException
  {
    try
    {
      if (getScript() == null || getScript().length() == 0)
      {
        throw new ApplicationException("Bitte gültiges Script eingeben");
      }

    }
    catch (RemoteException e)
    {
      String fehler = "Lesefeld kann nicht gespeichert werden. Siehe system log";
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

  @Override
  public String getScript() throws RemoteException
  {
    return (String) getAttribute("script");
  }

  @Override
  public void setScript(String script) throws RemoteException
  {
    setAttribute("script", script);
  }

  @Override
  public String getBezeichnung() throws RemoteException
  {
    return (String) getAttribute("bezeichnung");
  }

  @Override
  public void setBezeichnung(String bezeichnung) throws RemoteException
  {
    setAttribute("bezeichnung", bezeichnung);
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }

  @Override
  public String getEvaluatedContent()
  {
    return sampleContent;
  }

  @Override
  public void setEvaluatedContent(String content)
  {
    sampleContent = content;
  }
}
