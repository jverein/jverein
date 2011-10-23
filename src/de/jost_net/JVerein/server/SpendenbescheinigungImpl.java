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
import java.util.HashMap;
import java.util.Map;

import jonelo.NumericalChameleon.SpokenNumbers.GermanNumber;
import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.Variable.SpendenbescheinigungVar;
import de.jost_net.JVerein.keys.HerkunftSpende;
import de.jost_net.JVerein.keys.Spendenart;
import de.jost_net.JVerein.rmi.Formular;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Spendenbescheinigung;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class SpendenbescheinigungImpl extends AbstractDBObject implements
    Spendenbescheinigung
{

  private static final long serialVersionUID = -1861750218155086064L;

  public SpendenbescheinigungImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "spendenbescheinigung";
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
      if (getBetrag().doubleValue() <= 0)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Betrag größer als 0 eingeben."));
      }
      if (getSpendedatum() == null)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Spendedatum fehlt."));
      }
      if (getBescheinigungsdatum() == null)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Datum der Bescheinigung fehlt."));
      }
      if (getZeile1() == null && getZeile2() == null && getZeile3() == null)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Spenderadresse fehlt"));
      }
    }
    catch (RemoteException e)
    {
      Logger.error("Fehler", e);
      throw new ApplicationException("Fehler bei der Plausi");
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
    if ("formular".equals(field))
    {
      return Formular.class;
    }
    if ("mitglied".equals(field))
    {
      return Mitglied.class;
    }

    return null;
  }

  public int getSpendenart() throws RemoteException
  {
    Integer ret = (Integer) getAttribute("spendenart");
    if (ret == null)
    {
      ret = Spendenart.GELDSPENDE;
    }
    return ret;
  }

  public void setSpendenart(int spendenart) throws RemoteException
  {
    setAttribute("spendenart", spendenart);
  }

  public String getZeile1() throws RemoteException
  {
    return (String) getAttribute("zeile1");
  }

  public void setZeile1(String zeile1) throws RemoteException
  {
    setAttribute("zeile1", zeile1);
  }

  public String getZeile2() throws RemoteException
  {
    return (String) getAttribute("zeile2");
  }

  public void setZeile2(String zeile2) throws RemoteException
  {
    setAttribute("zeile2", zeile2);
  }

  public String getZeile3() throws RemoteException
  {
    return (String) getAttribute("zeile3");
  }

  public void setZeile3(String zeile3) throws RemoteException
  {
    setAttribute("zeile3", zeile3);
  }

  public String getZeile4() throws RemoteException
  {
    return (String) getAttribute("zeile4");
  }

  public void setZeile4(String zeile4) throws RemoteException
  {
    setAttribute("zeile4", zeile4);
  }

  public String getZeile5() throws RemoteException
  {
    return (String) getAttribute("zeile5");
  }

  public void setZeile5(String zeile5) throws RemoteException
  {
    setAttribute("zeile5", zeile5);
  }

  public String getZeile6() throws RemoteException
  {
    return (String) getAttribute("zeile6");
  }

  public void setZeile6(String zeile6) throws RemoteException
  {
    setAttribute("zeile6", zeile6);
  }

  public String getZeile7() throws RemoteException
  {
    return (String) getAttribute("zeile7");
  }

  public void setZeile7(String zeile7) throws RemoteException
  {
    setAttribute("zeile7", zeile7);
  }

  public Double getBetrag() throws RemoteException
  {
    Double ret = (Double) getAttribute("betrag");
    if (ret == null)
    {
      ret = new Double(0);
    }
    return ret;
  }

  public void setSpendedatum(Date datum) throws RemoteException
  {
    setAttribute("spendedatum", datum);
  }

  public Date getSpendedatum() throws RemoteException
  {
    return (Date) getAttribute("spendedatum");
  }

  public void setBescheinigungsdatum(Date datum) throws RemoteException
  {
    setAttribute("bescheinigungsdatum", datum);
  }

  public Date getBescheinigungsdatum() throws RemoteException
  {
    return (Date) getAttribute("bescheinigungsdatum");
  }

  public void setBetrag(Double betrag) throws RemoteException
  {
    setAttribute("betrag", betrag);
  }

  public Formular getFormular() throws RemoteException
  {
    return (Formular) getAttribute("formular");
  }

  public void setFormular(Formular formular) throws RemoteException
  {
    setAttribute("formular", formular);
  }

  public Boolean getErsatzAufwendungen() throws RemoteException
  {
    return Util.getBoolean(getAttribute("ersatzaufwendungen"));
  }

  public void setErsatzAufwendungen(Boolean ersatzaufwendungen)
      throws RemoteException
  {
    setAttribute("ersatzaufwendungen", Boolean.valueOf(ersatzaufwendungen));
  }

  public String getBezeichnungSachzuwendung() throws RemoteException
  {
    return (String) getAttribute("bezeichnungsachzuwendung");
  }

  public void setBezeichnungSachzuwendung(String bezeichnungsachzuwendung)
      throws RemoteException
  {
    setAttribute("bezeichnungsachzuwendung", bezeichnungsachzuwendung);
  }

  public int getHerkunftSpende() throws RemoteException
  {
    Integer ret = (Integer) getAttribute("herkunftspende");
    if (ret == null)
    {
      ret = HerkunftSpende.KEINEANGABEN;
    }
    return ret;
  }

  public void setHerkunftSpende(int herkunftspende) throws RemoteException
  {
    setAttribute("herkunftspende", herkunftspende);
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }

  public Mitglied getMitglied() throws RemoteException
  {
    return (Mitglied) getAttribute("mitglied");
  }

  public int getMitgliedID() throws RemoteException
  {
    return Integer.parseInt(getMitglied().getID());
  }

  public void setMitgliedID(Integer mitgliedID) throws RemoteException
  {
    setAttribute("mitglied", mitgliedID);
  }

  public void setMitglied(Mitglied mitglied) throws RemoteException
  {
    if (mitglied != null)
    {
      setAttribute("mitglied", new Integer(mitglied.getID()));
    }
    else
    {
      setAttribute("mitglied", null);
    }
  }

  public Boolean getUnterlagenWertermittlung() throws RemoteException
  {
    return Util.getBoolean(getAttribute("unterlagenwertermittlung"));
  }

  public void setUnterlagenWertermittlung(Boolean unterlagenwertermittlung)
      throws RemoteException
  {
    setAttribute("unterlagenwertermittlung",
        Boolean.valueOf(unterlagenwertermittlung));
  }

  public Map<String, Object> getMap(Map<String, Object> inma)
      throws RemoteException
  {
    Map<String, Object> map = null;
    if (inma == null)
    {
      map = new HashMap<String, Object>();
    }
    else
    {
      map = inma;
    }
    if (this.getID() == null)
    {
      this.setBescheinigungsdatum(new Date());
      this.setBetrag(1234.56);
      this.setBezeichnungSachzuwendung("Buch");
      this.setErsatzAufwendungen(false);
      this.setHerkunftSpende(1);
      this.setSpendedatum(new Date());
      this.setSpendenart(Spendenart.GELDSPENDE);
      this.setUnterlagenWertermittlung(true);
      this.setZeile1("Herrn");
      this.setZeile2("Dr. Willi Wichtig");
      this.setZeile3("Hinterm Bahnhof 1");
      this.setZeile4("12345 Testenhausen");
      this.setZeile5("");
      this.setZeile6("");
      this.setZeile7("");
    }
    String empfaenger = getZeile1() + "\n" + getZeile2() + "\n" + getZeile3()
        + "\n" + getZeile4() + "\n" + getZeile5() + "\n" + getZeile6() + "\n"
        + getZeile7() + "\n";
    map.put(SpendenbescheinigungVar.EMPFAENGER.getName(), empfaenger);
    map.put(SpendenbescheinigungVar.BETRAG.getName(),
        Einstellungen.DECIMALFORMAT.format(getBetrag()));
    Double dWert = (Double) getBetrag();
    try
    {
      String betraginworten = GermanNumber.toString(dWert.longValue());
      map.put(SpendenbescheinigungVar.BETRAGINWORTEN.getName(), "*"
          + betraginworten + "*");
    }
    catch (Exception e)
    {
      Logger.error("Fehler", e);
      throw new RemoteException(
          "Fehler bei der Aufbereitung des Betrages in Worten");
    }
    String bescheinigungsdatum = new JVDateFormatTTMMJJJJ()
        .format(getBescheinigungsdatum());
    map.put(SpendenbescheinigungVar.BESCHEINIGUNGDATUM.getName(),
        bescheinigungsdatum);
    String spendedatum = new JVDateFormatTTMMJJJJ().format(getSpendedatum());
    map.put(SpendenbescheinigungVar.SPENDEDATUM.getName(), spendedatum);
    map.put(SpendenbescheinigungVar.ERSATZAUFWENDUNGEN.getName(),
        (getErsatzAufwendungen() ? "X" : ""));
    return map;
  }

}
