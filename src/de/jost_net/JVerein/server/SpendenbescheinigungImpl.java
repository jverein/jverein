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
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jonelo.NumericalChameleon.SpokenNumbers.GermanNumber;
import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.Variable.SpendenbescheinigungVar;
import de.jost_net.JVerein.keys.HerkunftSpende;
import de.jost_net.JVerein.keys.Spendenart;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Formular;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Spendenbescheinigung;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.jost_net.JVerein.util.StringTool;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class SpendenbescheinigungImpl extends AbstractDBObject implements
    Spendenbescheinigung
{

  private static final long serialVersionUID = -1861750218155086064L;

  private List<Buchung> buchungen = null;

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
    return getAttribute("zeile1") == null ? ""
        : (String) getAttribute("zeile1");
  }

  public void setZeile1(String zeile1) throws RemoteException
  {
    setAttribute("zeile1", zeile1);
  }

  public String getZeile2() throws RemoteException
  {
    return getAttribute("zeile2") == null ? ""
        : (String) getAttribute("zeile2");
  }

  public void setZeile2(String zeile2) throws RemoteException
  {
    setAttribute("zeile2", zeile2);
  }

  public String getZeile3() throws RemoteException
  {
    return getAttribute("zeile3") == null ? ""
        : (String) getAttribute("zeile3");
  }

  public void setZeile3(String zeile3) throws RemoteException
  {
    setAttribute("zeile3", zeile3);
  }

  public String getZeile4() throws RemoteException
  {
    return getAttribute("zeile4") == null ? ""
        : (String) getAttribute("zeile4");
  }

  public void setZeile4(String zeile4) throws RemoteException
  {
    setAttribute("zeile4", zeile4);
  }

  public String getZeile5() throws RemoteException
  {
    return getAttribute("zeile5") == null ? ""
        : (String) getAttribute("zeile5");
  }

  public void setZeile5(String zeile5) throws RemoteException
  {
    setAttribute("zeile5", zeile5);
  }

  public String getZeile6() throws RemoteException
  {
    return getAttribute("zeile6") == null ? ""
        : (String) getAttribute("zeile6");
  }

  public void setZeile6(String zeile6) throws RemoteException
  {
    setAttribute("zeile6", zeile6);
  }

  public String getZeile7() throws RemoteException
  {
    return getAttribute("zeile7") == null ? ""
        : (String) getAttribute("zeile7");
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

  /**
   * Liefert aus der Buchungsliste entweder das größte Datum zurück. Falls noch
   * keine Buchungen eingetragen sind, wird das Spendendatum zurückgeliefert.
   * 
   * @return
   * @throws RemoteException
   */
  public Date getZeitraumBis() throws RemoteException
  {
    Date maxDate = getSpendedatum();
    if (buchungen.size() > 0)
    {
      for (Buchung b : buchungen)
      {
        if (maxDate.before(b.getDatum()))
        {
          maxDate = b.getDatum();
        }
      }
    }
    return maxDate;
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

  @Override
  public void store() throws RemoteException, ApplicationException
  {
    super.store();
    int id = Integer.parseInt(getID());
    for (Buchung b : buchungen)
    {
      b.setSpendenbescheinigungId(id);
      b.store();
    }
  }

  @Override
  public void delete() throws RemoteException, ApplicationException
  {
    for (Buchung b : getBuchungen())
    {
      b.setSpendenbescheinigungId(null);
      b.store();
    }
    super.delete();
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

  /**
   * Liefert als Kennzeichen zurück, ob die Spendenbescheinigung eine
   * Sammelbestaetigung ist. Dies ist der Fall, wenn die Liste der Buchungen
   * mehr als eine Buchung enthält. Ist keine oder nur eine Buchung zugewiesen,
   * liegt eine Einzelbestätigung vor.
   * 
   * @return Flag, ob Sammelbestätigung
   * @throws RemoteException
   */
  public boolean isSammelbestaetigung() throws RemoteException
  {
    if (getBuchungen() == null)
      return false;
    return getBuchungen().size() > 1;
  }

  /**
   * Fügt der Liste der Buchungen eine Buchung hinzu. Der Gesamtbetrag der
   * Spendenbescheinigung wird anhand der Einzelbeträge der Buchungen berechnet.
   * Die Spendenart wird auf "GELDSPENDE" gesetzt. Das Spendendatum wird auf das
   * kleinste Datum der Buchungen gesetzt.
   * 
   * @param buchung
   *          Die Buchung zum Hinzufügen
   */
  public void addBuchung(Buchung buchung) throws RemoteException
  {
    if (buchung != null)
    {
      double betrag = 0.0;
      if (buchungen == null)
      {
        buchungen = new ArrayList<Buchung>();
      }
      buchungen.add(buchung);
      for (Buchung b : buchungen)
      {
        betrag += b.getBetrag();
      }
      setBetrag(betrag);
      if (getSpendedatum() != null
          && buchung.getDatum().before(getSpendedatum()))
      {
        setSpendedatum(buchung.getDatum());
      }
      else if (getSpendedatum() == null)
      {
        setSpendedatum(buchung.getDatum());
      }
      setSpendenart(Spendenart.GELDSPENDE);
    }
  }

  /**
   * Hängt eine Buchung an die Spendenbescheinigung, wenn es eine
   * Einzelbestätigung werden soll. Sollten vorher schon Buchungen eingetragen
   * worden sein, wird die Liste der Buchungen vorher gelöscht.
   * 
   * @param buchung
   *          Die Buchung, die der Spendenbescheinigung zugeordnet wird
   */
  public void setBuchung(Buchung buchung) throws RemoteException
  {
    if (buchungen != null)
    {
      buchungen.clear();
      setBetrag(0.0);
    }
    if (buchung != null)
    {
      addBuchung(buchung);
      setSpendedatum(buchung.getDatum());
    }
  }

  /**
   * Liefert die Liste der Buchungen einer Spendenbescheinigung zurück. Falls
   * die Liste noch nicht angelegt wurde, wird sie aus der Datenbank
   * nachgeladen. Sollten der Spendenbescheinigung noch keine Buchungen
   * zugeordnet sein, wird eine leere Liste zurückgegeben.<br>
   * Beim Laden der Buchungen wird der Gesamtbetrag berechnet
   * 
   * @return Liste der der Spendenbescheinigung zugeordneten Buchungen
   */
  public List<Buchung> getBuchungen() throws RemoteException
  {
    if (getSpendenart() == Spendenart.GELDSPENDE && buchungen == null)
    {
      buchungen = new ArrayList<Buchung>();
      DBIterator it = Einstellungen.getDBService().createList(Buchung.class);
      it.addFilter("spendenbescheinigung = ?", getID());
      it.setOrder("ORDER BY datum asc");
      double summe = 0.0;
      while (it.hasNext())
      {
        Buchung bu = (Buchung) it.next();
        buchungen.add(bu);
        summe += bu.getBetrag();
      }
      setBetrag(summe);
    }
    return buchungen;
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

  public Boolean getAutocreate() throws RemoteException
  {
    return Util.getBoolean(getAttribute("autocreate"));
  }

  public void setAutocreate(Boolean autocreate) throws RemoteException
  {
    setAttribute("autocreate", Boolean.valueOf(autocreate));
  }

  public Map<String, Object> getMap(Map<String, Object> inma)
      throws RemoteException
  {
    Map<String, Object> map = null;
    final String newLineStr = "\n";
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
      this.setZeile5(null);
      this.setZeile6(null);
      this.setZeile7(null);
    }
    String empfaenger = getZeile1() + newLineStr
        + getZeile2() + newLineStr
        + getZeile3() + newLineStr
        + getZeile4() + newLineStr
        + getZeile5() + newLineStr
        + getZeile6() + newLineStr
        + getZeile7() + newLineStr;
    map.put(SpendenbescheinigungVar.EMPFAENGER.getName(), empfaenger);
    Double dWert = getBetrag();
    // Hier keinen String, sondern ein Double-Objekt in die Map stellen,
    // damit eine rechtsbündige Ausrichtung des Betrages in der Formular-
    // aufbereitung.getString() erfolgt.
    // Dies ist der Zustand vor Version 2.0
    // map.put(SpendenbescheinigungVar.BETRAG.getName(),
    //     Einstellungen.DECIMALFORMAT.format(getBetrag()));
    map.put(SpendenbescheinigungVar.BETRAG.getName(), dWert);
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
    switch(getSpendenart())
    {
      case Spendenart.GELDSPENDE:
        String art = "Geldzuwendungen";
        if (Einstellungen.getEinstellung().getMitgliedsbetraege())
        {
          art += "/Mitgliedsbeitrag";
        }
        map.put(SpendenbescheinigungVar.SPENDEART.getName(), art);
        break;
      case Spendenart.SACHSPENDE:
        map.put(SpendenbescheinigungVar.SPENDEART.getName(), "Sachzuwendungen");
        break;
    }
    String spendedatum = new JVDateFormatTTMMJJJJ().format(getSpendedatum());
    boolean printBuchungsart = Einstellungen.getEinstellung()
        .getSpendenbescheinigungPrintBuchungsart();

    // bei Sammelbestätigungen ein Zeitraum und "siehe Anlage"
    if (getBuchungen() != null && getBuchungen().size() > 1)
    {
      String zeitraumende = new JVDateFormatTTMMJJJJ().format(getZeitraumBis());
      map.put(SpendenbescheinigungVar.SPENDEDATUM.getName(), "s. Anlage");
      map.put(SpendenbescheinigungVar.SPENDENZEITRAUM.getName(), spendedatum
          + " bis " + zeitraumende);
      StringBuilder bl = new StringBuilder();
      bl.append(StringTool.rpad("Datum", 10));
      bl.append("  ");
      bl.append(StringTool.rpad(StringTool.lpad("Betrag", 8), 11));
      bl.append("  ");
      bl.append("Verwendung");
      bl.append(newLineStr);

      bl.append("----------");
      bl.append("  ");
      bl.append("-----------");
      bl.append("  ");
      bl.append("-----------------------------------------");
      bl.append(newLineStr);
      for (Buchung b : buchungen)
      {
        bl.append(new JVDateFormatTTMMJJJJ().format(b.getDatum()));
        bl.append("  ");
        String str = Einstellungen.DECIMALFORMAT.format(b.getBetrag());
        bl.append(StringTool.lpad(str, 11));
        bl.append("  ");
        if (printBuchungsart)
        {
          bl.append(b.getBuchungsart().getBezeichnung());
        }
        else
        {
          bl.append(b.getZweck());
        }
        bl.append(" ");
        bl.append((b.getVerzicht() ? "(b)" : "(a)"));
        bl.append(newLineStr);
      }
      bl.append(newLineStr);
      bl.append("----------");
      bl.append("  ");
      bl.append("-----------");
      bl.append("  ");
      bl.append("-----------------------------------------");
      bl.append(newLineStr);
      bl.append(StringTool.rpad("Summe:", 10));
      bl.append("  ");
      String str = Einstellungen.DECIMALFORMAT.format(getBetrag());
      bl.append(StringTool.lpad(str, 11));
      bl.append(newLineStr);
      bl.append(newLineStr);
      bl.append(newLineStr);
      bl.append("Legende:");
      bl.append(newLineStr);
      bl.append("(a): Es handelt sich nicht um den Verzicht auf Erstattung von Aufwendungen");
      bl.append(newLineStr);
      bl.append("(b): Es handelt sich um den Verzicht auf Erstattung von Aufwendungen");
      bl.append(newLineStr);
      map.put(SpendenbescheinigungVar.BUCHUNGSLISTE.getName(), bl.toString());
    }
    else
    {
      map.put(SpendenbescheinigungVar.SPENDEDATUM.getName(), spendedatum);
    }
    map.put(SpendenbescheinigungVar.ERSATZAUFWENDUNGEN.getName(),
        (getErsatzAufwendungen() ? "X" : ""));
    return map;
  }

}
