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
import de.jost_net.JVerein.rmi.Abrechnungslauf;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.db.AbstractDBObject;

public class AbrechnungslaufImpl extends AbstractDBObject implements
    Abrechnungslauf
{

  private static final long serialVersionUID = 1L;

  public AbrechnungslaufImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "abrechnungslauf";
  }

  @Override
  public String getPrimaryAttribute()
  {
    return "idtext";
  }

  @Override
  protected void deleteCheck()
  {
    //
  }

  @Override
  protected void insertCheck()
  {
    //
  }

  @Override
  protected void updateCheck()
  {
    insertCheck();
  }

  @Override
  protected Class<?> getForeignObject(String arg0)
  {
    return null;
  }

  @Override
  public Integer getNr() throws RemoteException
  {
    return new Integer(getID());
  }

  @Override
  public Date getDatum() throws RemoteException
  {
    Date d = (Date) getAttribute("datum");
    if (d == null)
    {
      return Einstellungen.NODATE;
    }
    return d;
  }

  @Override
  public void setDatum(Date datum) throws RemoteException
  {
    setAttribute("datum", datum);
  }

  @Override
  public Integer getModus() throws RemoteException
  {
    Integer modus = (Integer) getAttribute("modus");
    return modus;
  }

  @Override
  public void setModus(Integer modus) throws RemoteException
  {
    setAttribute("modus", modus);
  }

  @Override
  public Date getFaelligkeit() throws RemoteException
  {
    Date d = (Date) getAttribute("faelligkeit");
    if (d == null)
    {
      return Einstellungen.NODATE;
    }
    return d;
  }

  @Override
  public void setFaelligkeit(Date faelligkeit) throws RemoteException
  {
    setAttribute("faelligkeit", faelligkeit);
    setAttribute("faelligkeit2", faelligkeit);
  }

  @Override
  public Date getStichtag() throws RemoteException
  {
    Date d = (Date) getAttribute("stichtag");
    if (d == null)
    {
      return Einstellungen.NODATE;
    }
    return d;
  }

  @Override
  public void setStichtag(Date stichtag) throws RemoteException
  {
    setAttribute("stichtag", stichtag);
  }

  @Override
  public Date getEingabedatum() throws RemoteException
  {
    Date d = (Date) getAttribute("eingabedatum");
    if (d == null)
    {
      return Einstellungen.NODATE;
    }
    return d;
  }

  @Override
  public void setEingabedatum(Date eingabedatum) throws RemoteException
  {
    setAttribute("eingabedatum", eingabedatum);
  }

  @Override
  public Date getAustrittsdatum() throws RemoteException
  {
    Date d = (Date) getAttribute("austrittsdatum");
    if (d == null)
    {
      return Einstellungen.NODATE;
    }
    return d;
  }

  @Override
  public void setAustrittsdatum(Date austrittsdatum) throws RemoteException
  {
    setAttribute("austrittsdatum", austrittsdatum);
  }

  @Override
  public String getZahlungsgrund() throws RemoteException
  {
    return (String) getAttribute("zahlungsgrund");
  }

  @Override
  public void setZahlungsgrund(String zahlungsgrund) throws RemoteException
  {
    setAttribute("zahlungsgrund", zahlungsgrund);
  }

  @Override
  public Boolean getZusatzbetraege() throws RemoteException
  {
    return Util.getBoolean(getAttribute("zusatzbetraege"));
  }

  @Override
  public void setZusatzbetraege(Boolean zusatzbetraege) throws RemoteException
  {
    setAttribute("zusatzbetraege", zusatzbetraege);
  }

  @Override
  public Boolean getKursteilnehmer() throws RemoteException
  {
    return Util.getBoolean(getAttribute("kursteilnehmer"));
  }

  @Override
  public void setKursteilnehmer(Boolean kursteilnehmer) throws RemoteException
  {
    setAttribute("kursteilnehmer", kursteilnehmer);
  }

  @Override
  public Boolean getDtausdruck() throws RemoteException
  {
    return Util.getBoolean(getAttribute("dtausdruck"));
  }

  @Override
  public void setDtausdruck(Boolean dtausdruck) throws RemoteException
  {
    setAttribute("dtausdruck", dtausdruck);
  }

  @Override
  public Integer getAbbuchungsausgabe() throws RemoteException
  {
    Integer modus = (Integer) getAttribute("abbuchungsausgabe");
    return modus;
  }

  @Override
  public void setAbbuchungsausgabe(Integer abbuchungsausgabe)
      throws RemoteException
  {
    setAttribute("abbuchungsausgabe", abbuchungsausgabe);
  }

  /**
   * Gibt den Text zur ID aus
   */
  public String getIDText() throws RemoteException
  {
    return getID() + " " + "vom" + " "
        + new JVDateFormatTTMMJJJJ().format(getDatum());
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    if (fieldName.equals("idtext"))
    {
      return getIDText();
    }
    if (fieldName.equals("nr"))
    {
      return getNr();
    }
    return super.getAttribute(fieldName);
  }

  @Override
  public Boolean getAbgeschlossen() throws RemoteException
  {
    return Util.getBoolean(getAttribute("abgeschlossen"));
  }

  @Override
  public void setAbgeschlossen(Boolean abgeschlossen) throws RemoteException
  {
    setAttribute("abgeschlossen", abgeschlossen);
  }

  @Override
  public String getBemerkung() throws RemoteException
  {
    return (String) getAttribute("bemerkung");
  }

  @Override
  public void setBemerkung(String bemerkung) throws RemoteException
  {
    setAttribute("bemerkung", bemerkung);
  }

}
