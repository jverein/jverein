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
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.MitgliedNextBGruppe;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.util.ApplicationException;

/**
 * @author Rolf Mamat
 */
public class MitgliedNextBGruppeImpl extends AbstractDBObject implements
    MitgliedNextBGruppe
{
  private static final long serialVersionUID = -3249102287706112933L;

  public MitgliedNextBGruppeImpl() throws RemoteException
  {
    super();
  }

  @Override
  public String getPrimaryAttribute() throws RemoteException
  {
    return getIDField();
  }

  @Override
  protected String getIDField()
  {
    return COL_ID;
  }

  @Override
  protected String getTableName()
  {
    return TABLE_NAME;
  }

  @Override
  public void setMitglied(Mitglied mitglied) throws RemoteException
  {
    if (null == mitglied)
      throw new RemoteException("Mitglied fehlt!!");
    setAttribute(COL_MITGLIED, new Integer(mitglied.getID()));
  }

  @Override
  public Mitglied getMitglied() throws RemoteException
  {
    Integer id = (Integer) getAttribute(COL_MITGLIED);
    if (null == id)
      return null;

    return (Mitglied) Einstellungen.getDBService().createObject(Mitglied.class,
        id.toString());
  }

  @Override
  public void setBeitragsgruppe(Beitragsgruppe beitragsGruppe)
      throws RemoteException
  {
    if (null == beitragsGruppe)
      throw new RemoteException("Beitragsgruppe fehlt!!");
    setAttribute(COL_BEITRAGSGRUPPE, new Integer(beitragsGruppe.getID()));
  }

  @Override
  public Beitragsgruppe getBeitragsgruppe() throws RemoteException
  {
    Integer id = (Integer) getAttribute(COL_BEITRAGSGRUPPE);
    if (null == id)
      return null;
    return (Beitragsgruppe) Einstellungen.getDBService().createObject(
        Beitragsgruppe.class, id.toString());
  }

  @Override
  public void setBemerkung(String bemerkung) throws RemoteException
  {
    setAttribute(COL_BEMERKUNG, bemerkung);
  }

  @Override
  public String getBemerkung() throws RemoteException
  {
    return (String) getAttribute(COL_BEMERKUNG);
  }

  @Override
  public void setAbDatum(Date datum) throws RemoteException
  {
    if (null == datum)
      throw new RemoteException("Ab Datum fehlt!!");
    setAttribute(COL_AB_DATUM, datum);
  }

  @Override
  public Date getAbDatum() throws RemoteException
  {
    return (Date) getAttribute(COL_AB_DATUM);
  }

  @Override
  protected void insertCheck() throws ApplicationException
  {
    itemCheck();
    super.insertCheck();
  }

  @Override
  protected void updateCheck() throws ApplicationException
  {
    itemCheck();
    super.updateCheck();
  }

  private void itemCheck() throws ApplicationException
  {
    try
    {
      if (getMitglied() == null)
        throw new ApplicationException(
            "Ohne Mitgliedsreferenz kann nicht gespeichert werden!");
      if (getBeitragsgruppe() == null)
        throw new ApplicationException(
            "Es wurde keine neue Beitragsgruppe eingegeben!");

      Date abDatum = getAbDatum();
      if (null == abDatum)
        throw new ApplicationException(
            "Es wurde kein Datum eingegeben ab dem diese Änderung gültig sein soll!");

      if (abDatum.getTime() < System.currentTimeMillis())
        throw new ApplicationException(
            "Datum für diesen Datensatz muss in der Zukunft liegen.");
    }
    catch (RemoteException ex)
    {
      throw new ApplicationException("Daten können nicht geprüft werden", ex);
    }
  }

  @Override
  public Object getAttribute(String name) throws RemoteException
  {
    if (VIEW_BEITRAGSGRUPPE.equals(name))
    {
      Beitragsgruppe gruppe = getBeitragsgruppe();
      if (null == gruppe)
        return null;
      return gruppe.getBezeichnung();
    }
    if (VIEW_NAME_VORNAME.equals(name))
    {
      Mitglied mitglied = getMitglied();
      if (null == mitglied)
        return null;
      return mitglied.getAttribute(VIEW_NAME_VORNAME);
    }
    if (VIEW_AKT_BEITRAGSGRUPPE.equals(name))
    {
      Mitglied mitglied = getMitglied();
      if (null == mitglied)
        return null;
      Beitragsgruppe gruppe = mitglied.getBeitragsgruppe();
      if (null == gruppe)
        return null;
      return gruppe.getBezeichnung();
    }
    return super.getAttribute(name);
  }

}
