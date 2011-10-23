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

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.jost_net.JVerein.rmi.Buchungsklasse;
import de.jost_net.JVerein.server.Cache;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class BuchungsartImpl extends AbstractDBObject implements Buchungsart
{

  private static final long serialVersionUID = 500102542884220658L;

  public BuchungsartImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "buchungsart";
  }

  @Override
  public String getPrimaryAttribute()
  {
    return "bezeichnung";
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
      if (getNummer() < 0)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Nummer nicht gültig"));
      }
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Buchungsart kann nicht gespeichert werden. Siehe system log");
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
    if ("buchungsklasse".equals(arg0))
    {
      return Buchungsklasse.class;
    }
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

  public int getNummer() throws RemoteException
  {
    Integer i = (Integer) getAttribute("nummer");
    if (i == null)
      return 0;
    return i.intValue();
  }

  public void setNummer(int i) throws RemoteException
  {
    setAttribute("nummer", Integer.valueOf(i));
  }

  public int getArt() throws RemoteException
  {
    Integer i = (Integer) getAttribute("art");
    if (i == null)
    {
      return 0;
    }
    return i.intValue();
  }

  public void setArt(int art) throws RemoteException
  {
    setAttribute("art", art);
  }

  public Buchungsklasse getBuchungsklasse() throws RemoteException
  {
    return (Buchungsklasse) getAttribute("buchungsklasse");
  }

  public int getBuchungsklasseId() throws RemoteException
  {
    return Integer.parseInt(getBuchungsklasse().getID());
  }

  public void setBuchungsklasse(Integer buchungsklasse) throws RemoteException
  {
    setAttribute("buchungsklasse", buchungsklasse);
  }

  public Boolean getSpende() throws RemoteException
  {
    return Util.getBoolean(getAttribute("spende"));
  }

  public void setSpende(Boolean spende) throws RemoteException
  {
    setAttribute("spende", Boolean.valueOf(spende));
  }

  public void delete() throws RemoteException, ApplicationException
  {
    super.delete();
    Cache.get(Buchungsart.class, false).remove(this); // Aus Cache loeschen
  }

  public void store() throws RemoteException, ApplicationException
  {
    super.store();
    Cache.get(Buchungsart.class, false).put(this); // Cache aktualisieren
  }
}
