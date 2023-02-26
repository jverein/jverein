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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Kursteilnehmer;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.jost_net.OBanToo.SEPA.BIC;
import de.jost_net.OBanToo.SEPA.IBAN;
import de.jost_net.OBanToo.SEPA.SEPAException;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class KursteilnehmerImpl extends AbstractDBObject implements
    Kursteilnehmer
{

  private static final long serialVersionUID = 1L;

  public KursteilnehmerImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "kursteilnehmer";
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
      String fehler = "Kursteilnehmer kann nicht gespeichert werden. Siehe system log";
      Logger.error(fehler, e);
      throw new ApplicationException(fehler);
    }
  }

  private void plausi() throws RemoteException, ApplicationException
  {
    if (getName() == null || getName().length() == 0)
    {
      throw new ApplicationException("Bitte Namen eingeben");
    }
    if (getVZweck1() == null || getVZweck1().length() == 0)
    {
      throw new ApplicationException("Bitte Verwendungszweck eingeben");
    }
    if (getMandatDatum() == Einstellungen.NODATE)
    {
      throw new ApplicationException("Bitte Datum des Mandats eingeben");
    }
    if (Einstellungen.getEinstellung().getKursteilnehmerGebGesPflicht())
    {
      if (getGeburtsdatum() == null)
      {
        throw new ApplicationException("Bitte Geburtsdatum eingeben");
      }
    }
    if (getIban() == null || getIban().length() == 0)
    {
      throw new ApplicationException("Bitte IBAN eingeben");
    }
    if (getBic() == null || getBic().length() == 0)
    {
      throw new ApplicationException("Bitte BIC eingeben");
    }

    try
    {
      new IBAN(getIban());
    }
    catch (SEPAException e1)
    {
      throw new ApplicationException(e1.getMessage());
    }
    try
    {
      new BIC(getBic());
    }
    catch (SEPAException e1)
    {
      throw new ApplicationException(e1.getMessage());
    }
    if (getBetrag() <= 0)
    {
      throw new ApplicationException("Bitte Betrag größer als 0 eingeben");
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
      String fehler = "Kursteilnehmer kann nicht gespeichert werden. Siehe system log";
      Logger.error(fehler, e);
      throw new ApplicationException(fehler);
    }
  }

  @Override
  protected Class<?> getForeignObject(String field)
  {
    return null;
  }

  @Override
  public String getPersonenart() throws RemoteException
  {
    String pa = (String) getAttribute("personenart");
    if (pa == null)
    {
      return "n";
    }
    return pa;
  }

  @Override
  public void setPersonenart(String personenart) throws RemoteException
  {
    setAttribute("personenart", personenart);
  }

  @Override
  public String getAnrede() throws RemoteException
  {
    return (String) getAttribute("anrede");
  }

  @Override
  public void setAnrede(String anrede) throws RemoteException
  {
    setAttribute("anrede", anrede);
  }

  @Override
  public String getTitel() throws RemoteException
  {
    return (String) getAttribute("titel");
  }

  @Override
  public void setTitel(String titel) throws RemoteException
  {
    setAttribute("titel", titel);
  }

  @Override
  public String getName() throws RemoteException
  {
    return (String) getAttribute("name");
  }

  @Override
  public void setName(String name) throws RemoteException
  {
    setAttribute("name", name);
  }

  @Override
  public String getVorname() throws RemoteException
  {
    return (String) getAttribute("vorname");
  }

  @Override
  public void setVorname(String vorname) throws RemoteException
  {
    setAttribute("vorname", vorname);
  }

  @Override
  public String getStrasse() throws RemoteException
  {
    return (String) getAttribute("strasse");
  }

  @Override
  public void setStrasse(String strasse) throws RemoteException
  {
    setAttribute("strasse", strasse);
  }

  @Override
  public String getAdressierungszusatz() throws RemoteException
  {
    return (String) getAttribute("adressierungszusatz");
  }

  @Override
  public void setAdressierungszuatz(String adressierungszusatz)
      throws RemoteException
  {
    setAttribute("adressierungszusatz", adressierungszusatz);
  }

  @Override
  public String getPlz() throws RemoteException
  {
    return (String) getAttribute("plz");
  }

  @Override
  public void setPlz(String plz) throws RemoteException
  {
    setAttribute("plz", plz);
  }

  @Override
  public String getOrt() throws RemoteException
  {
    return (String) getAttribute("ort");
  }

  @Override
  public void setOrt(String ort) throws RemoteException
  {
    setAttribute("ort", ort);
  }

  @Override
  public String getStaat() throws RemoteException
  {
    return (String) getAttribute("staat");
  }

  @Override
  public void setStaat(String staat) throws RemoteException
  {
    setAttribute("staat", staat);
  }

  @Override
  public String getEmail() throws RemoteException
  {
    return (String) getAttribute("email");
  }

  @Override
  public void setEmail(String email) throws RemoteException
  {
    setAttribute("email", email);
  }

  @Override
  public Date getMandatDatum() throws RemoteException
  {
    Date d = (Date) getAttribute("mandatdatum");
    if (d == null)
    {
      return Einstellungen.NODATE;
    }
    return d;
  }

  @Override
  public void setMandatDatum(Date mandatdatum) throws RemoteException
  {
    setAttribute("mandatdatum", mandatdatum);
  }

  @Override
  public String getMandatID() throws RemoteException
  {
    return "K" + getID();
  }

  @Override
  public Date getLetzteLastschrift() throws RemoteException
  {
    ResultSetExtractor rs = new ResultSetExtractor()
    {
      @Override
      public Object extract(ResultSet rs) throws SQLException
      {
        Date letzteLastschrift = Einstellungen.NODATE;
        while (rs.next())
        {
          letzteLastschrift = rs.getDate(1);
        }
        return letzteLastschrift;
      }
    };

    String sql = "select max(abrechnungslauf.FAELLIGKEIT) from lastschrift, abrechnungslauf "
        + "where lastschrift.ABRECHNUNGSLAUF = abrechnungslauf.id and lastschrift.KURSTEILNEHMER = ?";
    Date d = (Date) Einstellungen.getDBService().execute(sql,
        new Object[] { getID() }, rs);

    return d;
  }

  @Override
  public Integer getZahlungsweg()
  {
    return Zahlungsweg.BASISLASTSCHRIFT;
  }

  @Override
  public String getBic() throws RemoteException
  {
    return (String) getAttribute("bic");
  }

  @Override
  public void setBic(String bic) throws RemoteException
  {
    setAttribute("bic", bic);
  }

  @Override
  public String getIban() throws RemoteException
  {
    return (String) getAttribute("iban");
  }

  @Override
  public void setIban(String iban) throws RemoteException
  {
    setAttribute("iban", iban);
  }

  @Override
  public Date getGeburtsdatum() throws RemoteException
  {
    return (Date) getAttribute("geburtsdatum");
  }

  @Override
  public void setGeburtsdatum(Date geburtsdatum) throws RemoteException
  {
    setAttribute("geburtsdatum", geburtsdatum);
  }

  @Override
  public void setGeburtsdatum(String geburtsdatum) throws RemoteException
  {
    setAttribute("geburtsdatum", toDate(geburtsdatum));
  }

  @Override
  public String getGeschlecht() throws RemoteException
  {
    return (String) getAttribute("geschlecht");
  }

  @Override
  public void setGeschlecht(String geschlecht) throws RemoteException
  {
    setAttribute("geschlecht", geschlecht);
  }

  @Override
  public String getVZweck1() throws RemoteException
  {
    return (String) getAttribute("vzweck1");
  }

  @Override
  public void setVZweck1(String vzweck1) throws RemoteException
  {
    setAttribute("vzweck1", vzweck1);
  }

  @Override
  public void setEingabedatum() throws RemoteException
  {
    setAttribute("eingabedatum", new Date());
  }

  @Override
  public Date getEingabedatum() throws RemoteException
  {
    return (Date) getAttribute("eingabedatum");
  }

  @Override
  public void setAbbudatum() throws RemoteException
  {
    setAttribute("abbudatum", new Date());
  }

  @Override
  public void resetAbbudatum() throws RemoteException
  {
    setAttribute("abbudatum", null);
  }

  @Override
  public Date getAbbudatum() throws RemoteException
  {
    return (Date) getAttribute("abbudatum");
  }

  @Override
  public double getBetrag() throws RemoteException
  {
    Double d = (Double) getAttribute("betrag");
    if (d == null)
      return 0;
    return d.doubleValue();
  }

  @Override
  public void setBetrag(double d) throws RemoteException
  {
    setAttribute("betrag",  Double.valueOf(d));
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    if (fieldName.equals("mandatid"))
    {
      return getMandatID();
    }
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
}
