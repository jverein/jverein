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

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Abrechnungslauf;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.jost_net.JVerein.rmi.Jahresabschluss;
import de.jost_net.JVerein.rmi.Konto;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.jost_net.JVerein.rmi.Spendenbescheinigung;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class BuchungImpl extends AbstractDBObject implements Buchung
{

  private static final long serialVersionUID = 1L;

  public BuchungImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "buchung";
  }

  @Override
  public String getPrimaryAttribute()
  {
    return "id";
  }

  @Override
  protected void deleteCheck() throws ApplicationException
  {
    insertCheck();
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
      Logger.error("insert check of buchung failed", e);
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Buchung kann nicht gespeichert werden. Siehe system log"));
    }
  }

  private void plausi() throws RemoteException, ApplicationException
  {
    if (getKonto() == null)
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Bitte Konto eingeben"));
    }
    if (getDatum() == null)
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Bitte Datum eingeben"));
    }
    Jahresabschluss ja = getJahresabschluss();
    if (ja != null)
    {
      throw new ApplicationException(
          JVereinPlugin
              .getI18n()
              .tr("Buchung kann nicht gespeichert werden. Zeitraum ist bereits abgeschlossen!"));
    }
  }

  @Override
  protected void updateCheck() throws ApplicationException
  {
    insertCheck();
  }

  @Override
  protected Class<?> getForeignObject(String field)
  {
    if ("mitgliedskonto".equals(field))
    {
      return Mitgliedskonto.class;
    }
    else if ("abrechnungslauf".equals(field))
    {
      return Abrechnungslauf.class;
    }
    else if ("spendenbescheinigung".equals(field))
    {
      return Spendenbescheinigung.class;
    }
    return null;
  }

  public Integer getUmsatzid() throws RemoteException
  {

    return (Integer) getAttribute("umsatzid");
  }

  public void setUmsatzid(Integer umsatzid) throws RemoteException
  {
    setAttribute("umsatzid", umsatzid);
  }

  public Konto getKonto() throws RemoteException
  {
    Integer i = (Integer) super.getAttribute("konto");
    if (i == null)
      return null; // Kein Konto zugeordnet

    Cache cache = Cache.get(Konto.class, true);
    return (Konto) cache.get(i);
  }

  public void setKonto(Konto konto) throws RemoteException
  {
    setAttribute("konto", new Integer(konto.getID()));
  }

  public Integer getAuszugsnummer() throws RemoteException
  {
    return (Integer) getAttribute("auszugsnummer");
  }

  public void setAuszugsnummer(Integer auszugsnummer) throws RemoteException
  {
    setAttribute("auszugsnummer", auszugsnummer);
  }

  public Integer getBlattnummer() throws RemoteException
  {
    return (Integer) getAttribute("blattnummer");
  }

  public void setBlattnummer(Integer blattnummer) throws RemoteException
  {
    setAttribute("blattnummer", blattnummer);
  }

  public String getName() throws RemoteException
  {
    return (String) getAttribute("name");
  }

  public void setName(String name) throws RemoteException
  {
    setAttribute("name", name);
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

  public String getZweck() throws RemoteException
  {
    return (String) getAttribute("zweck");
  }

  public void setZweck(String zweck) throws RemoteException
  {
    setAttribute("zweck", zweck);
  }

  public String getZweck2() throws RemoteException
  {
    return (String) getAttribute("zweck2");
  }

  public void setZweck2(String zweck2) throws RemoteException
  {
    setAttribute("zweck2", zweck2);
  }

  public Date getDatum() throws RemoteException
  {
    return (Date) getAttribute("datum");
  }

  public void setDatum(Date datum) throws RemoteException
  {
    setAttribute("datum", datum);
  }

  public void setDatum(String datum) throws RemoteException
  {
    setAttribute("datum", toDate(datum));
  }

  public String getArt() throws RemoteException
  {
    return (String) getAttribute("art");
  }

  public void setArt(String art) throws RemoteException
  {
    setAttribute("art", art);
  }

  public String getKommentar() throws RemoteException
  {
    return (String) getAttribute("kommentar");
  }

  public void setKommentar(String kommentar) throws RemoteException
  {
    setAttribute("kommentar", kommentar);
  }

  public Buchungsart getBuchungsart() throws RemoteException
  {
    Integer i = (Integer) super.getAttribute("buchungsart");
    if (i == null)
      return null; // Keine Buchungsart zugeordnet

    Cache cache = Cache.get(Buchungsart.class, true);
    return (Buchungsart) cache.get(i);
  }

  public int getBuchungsartId() throws RemoteException
  {
    return Integer.parseInt(getBuchungsart().getID());
  }

  public void setBuchungsart(Integer buchungsart) throws RemoteException
  {
    setAttribute("buchungsart", buchungsart);
  }

  public Abrechnungslauf getAbrechnungslauf() throws RemoteException
  {
    return (Abrechnungslauf) getAttribute("abrechnungslauf");
  }

  public int getAbrechnungslaufID() throws RemoteException
  {
    return Integer.parseInt(getAbrechnungslauf().getID());
  }

  public void setAbrechnungslauf(Integer abrechnungslauf)
      throws RemoteException
  {
    setAttribute("abrechnungslauf", abrechnungslauf);
  }

  public void setAbrechnungslauf(Abrechnungslauf abrechnungslauf)
      throws RemoteException
  {
    setAttribute("abrechnungslauf", new Integer(abrechnungslauf.getID()));
  }

  public Mitgliedskonto getMitgliedskonto() throws RemoteException
  {
    return (Mitgliedskonto) getAttribute("mitgliedskonto");
  }

  public int getMitgliedskontoID() throws RemoteException
  {
    return Integer.parseInt(getMitgliedskonto().getID());
  }

  public void setMitgliedskontoID(Integer mitgliedskontoID)
      throws RemoteException
  {
    setAttribute("mitgliedskonto", mitgliedskontoID);
  }

  public void setMitgliedskonto(Mitgliedskonto mitgliedskonto)
      throws RemoteException
  {
    if (mitgliedskonto != null)
    {
      setAttribute("mitgliedskonto", new Integer(mitgliedskonto.getID()));
    }
    else
    {
      setAttribute("mitgliedskonto", null);
    }
  }

  public Spendenbescheinigung getSpendenbescheinigung() throws RemoteException
  {
    Integer i = (Integer) super.getAttribute("spendenbescheinigung");
    if (i == null)
      return null; // Keine Spendenbescheinigung zugeordnet

    Cache cache = Cache.get(Spendenbescheinigung.class, true);
    return (Spendenbescheinigung) cache.get(i);
  }

  public void setSpendenbescheinigungId(Integer spendenbescheinigung)
      throws RemoteException
  {
    setAttribute("spendenbescheinigung", spendenbescheinigung);
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    if ("id-int".equals(fieldName))
    {
      try
      {
        return new Integer(getID());
      }
      catch (Exception e)
      {
        Logger.error("unable to parse id: " + getID());
        return getID();
      }
    }

    if ("buchungsart".equals(fieldName))
      return getBuchungsart();

    if ("konto".equals(fieldName))
      return getKonto();

    return super.getAttribute(fieldName);
  }

  private Date toDate(String datum)
  {
    Date d = null;

    try
    {
      d = new JVDateFormatTTMMJJJJ().parse(datum);
    }
    catch (Exception e)
    {
      //
    }
    return d;
  }

  public Jahresabschluss getJahresabschluss() throws RemoteException
  {
    DBIterator it = Einstellungen.getDBService().createList(
        Jahresabschluss.class);
    it.addFilter("von <= ?", new Object[] { getDatum() });
    it.addFilter("bis >= ?", new Object[] { getDatum() });
    if (it.hasNext())
    {
      Jahresabschluss ja = (Jahresabschluss) it.next();
      return ja;
    }
    return null;
  }

  public Integer getSplitId() throws RemoteException
  {
    return (Integer) getAttribute("splitid");
  }

  public void setSplitId(Integer splitid) throws RemoteException
  {
    setAttribute("splitid", splitid);
  }

}
