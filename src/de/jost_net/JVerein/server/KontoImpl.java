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
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Konto;
import de.jost_net.JVerein.util.Geschaeftsjahr;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class KontoImpl extends AbstractDBObject implements Konto
{

  private static final long serialVersionUID = 1L;

  public KontoImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "konto";
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
      DBIterator it = Einstellungen.getDBService().createList(Konto.class);
      it.addFilter("nummer = ?", new Object[] { getNummer()});
      if (it.size() > 0)
      {
        throw new ApplicationException("Konto existiert bereits");
      }
    }
    catch (RemoteException e)
    {
      Logger.error("insert check of konto failed", e);
      throw new ApplicationException(
          "Konto kann nicht gespeichert werden. Siehe system log");
    }
  }

  @Override
  protected void updateCheck() throws ApplicationException
  {
    plausi();
  }

  private void plausi() throws ApplicationException
  {
    try
    {
      if (getBezeichnung() == null || getBezeichnung().length() == 0)
      {
        throw new ApplicationException("Bitte Bezeichnung eingeben");
      }
      if (getBezeichnung().length() > 255)
      {
        throw new ApplicationException(
            "Maximale Länge der Bezeichnung: 255 Zeichen");
      }
      if (getNummer() == null || getNummer().length() == 0)
      {
        throw new ApplicationException("Bitte Nummer eingeben");
      }
    }
    catch (RemoteException e)
    {
      Logger.error("insert check of konto failed", e);
      throw new ApplicationException(
          "Konto kann nicht gespeichert werden. Siehe system log");
    }
  }

  @Override
  protected Class<?> getForeignObject(String arg0)
  {
    return null;
  }

  @Override
  public String getNummer() throws RemoteException
  {
    return (String) getAttribute("nummer");
  }

  @Override
  public void setNummer(String nummer) throws RemoteException
  {
    setAttribute("nummer", nummer);
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
  public Date getEroeffnung() throws RemoteException
  {
    return (Date) getAttribute("eroeffnung");
  }

  @Override
  public void setEroeffnung(Date eroeffnungdatum) throws RemoteException
  {
    setAttribute("eroeffnung", eroeffnungdatum);
  }

  @Override
  public Date getAufloesung() throws RemoteException
  {
    return (Date) getAttribute("aufloesung");
  }

  @Override
  public void setAufloesung(Date aufloesungsdatum) throws RemoteException
  {
    setAttribute("aufloesung", aufloesungsdatum);
  }

  @Override
  public Integer getHibiscusId() throws RemoteException
  {
    return (Integer) getAttribute("hibiscusid");
  }

  @Override
  public void setHibiscusId(Integer id) throws RemoteException
  {
    setAttribute("hibiscusid", id);
  }

  @Override
  public DBIterator getKontenEinesJahres(Geschaeftsjahr gj)
      throws RemoteException
  {
    DBIterator konten = Einstellungen.getDBService().createList(Konto.class);
    konten.addFilter("(eroeffnung is null or eroeffnung <= ?)",
        new Object[] { gj.getEndeGeschaeftsjahr()});
    konten.addFilter("(aufloesung is null or year(aufloesung) = ? or "
        + "aufloesung >= ? )", new Object[] { gj.getBeginnGeschaeftsjahrjahr(),
        gj.getEndeGeschaeftsjahr()});
    konten.setOrder("order by bezeichnung");
    return konten;
  }

  @Override
  public void delete() throws RemoteException, ApplicationException
  {
    super.delete();
    Cache.get(Konto.class, false).remove(this); // Aus Cache loeschen
  }

  @Override
  public void store() throws RemoteException, ApplicationException
  {
    super.store();
    Cache.get(Konto.class, false).put(this); // Cache aktualisieren
  }
}
