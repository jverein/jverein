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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.Variable.MitgliedVar;
import de.jost_net.JVerein.io.Adressbuch.Adressaufbereitung;
import de.jost_net.JVerein.keys.ArtBeitragsart;
import de.jost_net.JVerein.keys.Datentyp;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Adresstyp;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Eigenschaft;
import de.jost_net.JVerein.rmi.Eigenschaften;
import de.jost_net.JVerein.rmi.Felddefinition;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedfoto;
import de.jost_net.JVerein.rmi.Zusatzfelder;
import de.jost_net.JVerein.util.Checker;
import de.jost_net.JVerein.util.Datum;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.jost_net.JVerein.util.LesefeldAuswerter;
import de.jost_net.JVerein.util.StringTool;
import de.jost_net.OBanToo.SEPA.BIC;
import de.jost_net.OBanToo.SEPA.IBAN;
import de.jost_net.OBanToo.SEPA.SEPAException;
import de.jost_net.OBanToo.SEPA.BankenDaten.Bank;
import de.jost_net.OBanToo.SEPA.BankenDaten.Banken;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class MitgliedImpl extends AbstractDBObject implements Mitglied
{

  private static final long serialVersionUID = 1L;

  private transient Map<String, String> variable;

  public MitgliedImpl() throws RemoteException
  {
    super();
    variable = new HashMap<String, String>();
  }

  @Override
  protected String getTableName()
  {
    return "mitglied";
  }

  @Override
  public String getPrimaryAttribute()
  {
    return "namevorname";
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
      String fehler = "Mitglied kann nicht gespeichert werden. Siehe system log";
      Logger.error(fehler, e);
      throw new ApplicationException(fehler);
    }
  }

  private void plausi() throws RemoteException, ApplicationException
  {
    checkExterneMitgliedsnummer();

    if (getPersonenart() == null
        || (!getPersonenart().equals("n") && !getPersonenart().equals("j")))
    {
      throw new ApplicationException("Personenstatus ist nicht 'n' oder 'j'");
    }
    if (getName() == null || getName().length() == 0)
    {
      throw new ApplicationException("Bitte Namen eingeben");
    }
    if (getPersonenart().equals("n")
        && (getVorname() == null || getVorname().length() == 0))
    {
      throw new ApplicationException("Bitte Vornamen eingeben");
    }
    if (getAdresstyp().getJVereinid() == 1 && getPersonenart().equals("n")
        && getGeburtsdatum().getTime() == Einstellungen.NODATE.getTime()
        && Einstellungen.getEinstellung().getGeburtsdatumPflicht())
    {
      throw new ApplicationException("Bitte Geburtsdatum eingeben");
    }
    if (getAdresstyp().getJVereinid() == 1 && getPersonenart().equals("n")
        && Einstellungen.getEinstellung().getGeburtsdatumPflicht())
    {
      Calendar cal1 = Calendar.getInstance();
      cal1.setTime(getGeburtsdatum());
      Calendar cal2 = Calendar.getInstance();
      if (cal1.after(cal2))
      {
        throw new ApplicationException("Geburtsdatum liegt in der Zukunft");
      }
      if (getSterbetag() != null)
      {
        cal2.setTime(getSterbetag());
      }
      cal2.add(Calendar.YEAR, -150);
      if (cal1.before(cal2))
      {
        throw new ApplicationException(
            "Ist das Mitglied wirklich älter als 150 Jahre?");
      }
    }
    if (getPersonenart().equals("n") && getGeschlecht() == null)
    {
      throw new ApplicationException("Bitte Geschlecht auswählen");
    }
    if (getEmail() != null && getEmail().length() > 0)
    {
      if (!Checker.isValidEmailAddress(getEmail()))
      {
        throw new ApplicationException("Ungültige Email-Adresse.");
      }
    }

    if (getAdresstyp().getJVereinid() == 1
        && getEintritt().getTime() == Einstellungen.NODATE.getTime()
        && Einstellungen.getEinstellung().getEintrittsdatumPflicht())
    {
      throw new ApplicationException("Bitte Eintrittsdatum eingeben");
    }
    if (getAdresstyp().getJVereinid() == 1
        && getZahlungsweg() == Zahlungsweg.BASISLASTSCHRIFT
        && getBeitragsgruppe().getBetrag() > 0)
    {
      if (getBic() == null || getBic().length() == 0 || getIban() == null
          || getIban().length() == 0)
      {
        throw new ApplicationException("Bitte BIC und IBAN eingeben");
      }
    }
    // SEPAParam sepaparam = null;
    // try
    // {
    // sepaparam = SEPA.getSEPAParam(Einstellungen.getEinstellung()
    // .getDefaultLand());
    // }
    // catch (SEPAException e1)
    // {
    // Logger.error(e1.getMessage());
    // }
    // if (getBlz() != null && getBlz().length() != 0
    // && getBlz().length() != sepaparam.getBankIdentifierLength())
    // {
    // throw new ApplicationException(
    // "Die Bankleitzahl muss {0}stellig sein",
    // sepaparam.getBankIdentifierLength() + ""));
    // }
    if (getBlz() != null && getBlz().length() > 0 && getKonto() == null)
    {
      throw new ApplicationException("Kontonummer fehlt");
    }
    if (getKonto() != null && getKonto().length() > 0 && getBlz() == null)
    {
      throw new ApplicationException("BLZ fehlt");
    }
    // if (getBlz().length() != 0 || getKonto().length() != 0l)
    // {
    // try
    // {
    // Long.parseLong(getKonto());
    // }
    // catch (NumberFormatException e)
    // {
    // throw new ApplicationException("Kontonummer ist nicht numerisch");
    // }
    // if (!Einstellungen.checkAccountCRC(getBlz(), getKonto()))
    // {
    // throw new ApplicationException(
    // JVereinPlugin
    // .getI18n()
    // .tr("BLZ/Kontonummer ({0}/{1}) ungültig. Bitte prüfen Sie Ihre Eingaben.",
    // new String[] { getBlz(), getKonto() }));
    // }
    // try
    // {
    // IBAN i = new IBAN(getIban());
    // if (!Einstellungen.checkAccountCRC(i.getBLZ(), i.getKonto()))
    // {
    // throw new ApplicationException(
    // JVereinPlugin
    // .getI18n()
    // .tr("BLZ/Kontonummer-Kombination in IBAN ungültig. Bitte prüfen Sie Ihre Eingaben.",
    // new String[] { getBlz(), getKonto() }));
    //
    // }
    // }
    // catch (SEPAException e)
    // {
    // throw new ApplicationException(e.getMessage());
    // }
    // }

    if (getIban() != null && getIban().length() != 0)
    {
      try
      {
        new IBAN(getIban());
      }
      catch (SEPAException e)
      {
        throw new ApplicationException(e.getMessage());
      }
    }
    if (getBic() != null && getBic().length() != 0)
    {
      try
      {
        new BIC(getBic());
      }
      catch (SEPAException e)
      {
        throw new ApplicationException(e.getMessage());
      }
    }
    if (getBic() == null || getBic().length() == 0)
    {
      setKonto(null);
      setBlz(null);
    }
    if (getZahlungsrhytmus() != 12 && getZahlungsrhytmus() != 6
        && getZahlungsrhytmus() != 3 && getZahlungsrhytmus() != 1)
    {
      throw new ApplicationException("Ungültiger Zahlungsrhytmus: "
          + getZahlungsrhytmus());
    }
    if (getSterbetag() != null && getAustritt() == null)
    {
      throw new ApplicationException(
          "Bei verstorbenem Mitglied muss das Austrittsdatum gefüllt sein!");
    }
    if (getAustritt() != null || getKuendigung() != null)
    {
      // Person ist ausgetreten
      // Hat das Mitglied für andere gezahlt?
      if (getBeitragsgruppe().getBeitragsArt() == 1)
      {
        // ja
        DBIterator famang = Einstellungen.getDBService().createList(
            Mitglied.class);
        famang.addFilter("zahlerid = " + getID());
        famang.addFilter("austritt is null");
        if (famang.hasNext())
        {
          throw new ApplicationException(
              "Dieses Mitglied zahlt noch für andere Mitglieder. Zunächst Beitragsart der Angehörigen ändern!");
        }
      }
    }
    if (getBeitragsgruppe() != null
        && getBeitragsgruppe().getBeitragsArt() == ArtBeitragsart.FAMILIE_ANGEHOERIGER
        && getZahlerID() == null)
    {
      throw new ApplicationException("Bitte Zahler auswählen!");
    }

    if (getGeschlecht() == null || getGeschlecht().length() == 0)
      throw new ApplicationException(
          "Bitte erfassen Sie das Geschlecht des Mitglieds!");
  }

  /***
   * Prüfe die externe Mitgliedsnummer. Ist es ein Mitgliedssatz und ist in den
   * Einstellungen die externe Mitgliedsnummer aktiviert, dann muss eine
   * vorhanden sein und diese muss eindeutig sein.
   * 
   * @throws RemoteException
   * @throws ApplicationException
   */
  private void checkExterneMitgliedsnummer() throws RemoteException,
      ApplicationException
  {
    if (getAdresstyp().getJVereinid() != 1)
      return;
    if (Einstellungen.getEinstellung().getExterneMitgliedsnummer() == false)
      return;

    if (getExterneMitgliedsnummer() == null)
    {
      throw new ApplicationException("Externe Mitgliedsnummer fehlt");
    }

    DBIterator mitglieder = Einstellungen.getDBService().createList(
        Mitglied.class);
    mitglieder.addFilter("id != ?", getID());
    mitglieder.addFilter("externemitgliedsnummer = ?",
        getExterneMitgliedsnummer());
    if (mitglieder.hasNext())
    {
      Mitglied mitglied = (Mitglied) mitglieder.next();
      throw new ApplicationException(
          "Die externe Mitgliedsnummer wird bereits verwendet für Mitglied : "
              + mitglied.getAttribute("namevorname"));
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
      String fehler = "Mitglied kann nicht gespeichert werden. Siehe system log";
      Logger.error(fehler, e);
      throw new ApplicationException(fehler);
    }
  }

  @Override
  protected Class<?> getForeignObject(String field)
  {
    if ("beitragsgruppe".equals(field))
    {
      return Beitragsgruppe.class;
    }
    if ("foto".equals(field))
    {
      return Mitgliedfoto.class;
    }
    if ("adresstyp".equals(field))
    {
      return Adresstyp.class;
    }
    return null;
  }

  @Override
  public void setAdresstyp(Integer adresstyp) throws RemoteException
  {
    setAttribute("adresstyp", adresstyp);
  }

  @Override
  public Adresstyp getAdresstyp() throws RemoteException
  {
    return (Adresstyp) getAttribute("adresstyp");
  }

  @Override
  public void setExterneMitgliedsnummer(Integer extnr) throws RemoteException
  {
    setAttribute("externemitgliedsnummer", extnr);
  }

  @Override
  public Integer getExterneMitgliedsnummer() throws RemoteException
  {
    return (Integer) getAttribute("externemitgliedsnummer");
  }

  @Override
  public String getPersonenart() throws RemoteException
  {
    return (String) getAttribute("personenart");
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
    String t = (String) getAttribute("titel");
    if (t == null)
    {
      t = "";
    }
    return t;
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
  public String getAdressierungszusatz() throws RemoteException
  {
    if (getAttribute("adressierungszusatz") != null)
    {
      return (String) getAttribute("adressierungszusatz");
    }
    else
    {
      return "";
    }
  }

  @Override
  public void setAdressierungszusatz(String adressierungszusatz)
      throws RemoteException
  {
    setAttribute("adressierungszusatz", adressierungszusatz);
  }

  @Override
  public String getStrasse() throws RemoteException
  {
    if (getAttribute("strasse") != null)
    {
      return (String) getAttribute("strasse");
    }
    else
    {
      return "";
    }
  }

  @Override
  public void setStrasse(String strasse) throws RemoteException
  {
    setAttribute("strasse", strasse);
  }

  @Override
  public String getPlz() throws RemoteException
  {
    if (getAttribute("plz") != null)
    {

      return (String) getAttribute("plz");
    }
    else
    {
      return "";
    }
  }

  @Override
  public void setPlz(String plz) throws RemoteException
  {
    setAttribute("plz", plz);
  }

  @Override
  public String getOrt() throws RemoteException
  {
    if (getAttribute("ort") != null)
    {
      return (String) getAttribute("ort");
    }
    else
    {
      return "";
    }
  }

  @Override
  public void setOrt(String ort) throws RemoteException
  {
    setAttribute("ort", ort);
  }

  @Override
  public String getStaat() throws RemoteException
  {
    if (getAttribute("staat") != null)
    {
      return (String) getAttribute("staat");
    }
    else
    {
      return "";
    }
  }

  @Override
  public void setStaat(String staat) throws RemoteException
  {
    if (staat != null)
    {
      staat = staat.toUpperCase();
    }
    setAttribute("staat", staat);
  }

  @Override
  public Integer getZahlungsweg() throws RemoteException
  {
    return (Integer) getAttribute("zahlungsweg");
  }

  @Override
  public void setZahlungsweg(Integer zahlungsweg) throws RemoteException
  {
    setAttribute("zahlungsweg", zahlungsweg);
  }

  @Override
  public Integer getZahlungsrhytmus() throws RemoteException
  {
    return (Integer) getAttribute("zahlungsrhytmus");
  }

  @Override
  public void setZahlungsrhytmus(Integer zahlungsrhytmus)
      throws RemoteException
  {
    setAttribute("zahlungsrhytmus", zahlungsrhytmus);
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
  public Integer getMandatVersion() throws RemoteException
  {
    Integer vers = (Integer) getAttribute("mandatversion");
    if (vers == null)
    {
      vers = new Integer(0);
    }
    return vers;
  }

  @Override
  public void setMandatVersion(Integer mandatversion) throws RemoteException
  {
    setAttribute("mandatversion", mandatversion);
  }

  @Override
  public String getMandatID() throws RemoteException
  {
    return getID() + "-" + getMandatVersion();
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
        + "where lastschrift.ABRECHNUNGSLAUF = abrechnungslauf.id and lastschrift.MITGLIED = ?";
    Date d = (Date) Einstellungen.getDBService().execute(sql,
        new Object[] { getID() }, rs);

    return d;
  }

  @Override
  public String getBic() throws RemoteException
  {
    String ret = (String) getAttribute("bic");
    if (ret == null)
    {
      return "";
    }
    return ret;
  }

  private Object getBankname() throws RemoteException
  {
    String bic = getBic();
    if (null != bic)
    {
      Bank bank = Banken.getBankByBIC(bic);
      if (null != bank)
        return formatBankname(bank);
    }
    String blz = getBlz();
    if (null != blz)
    {
      Bank bank = Banken.getBankByBLZ(blz);
      if (null != bank)
        return formatBankname(bank);
    }
    return null;
  }

  private String formatBankname(Bank bank)
  {
    String name = bank.getBezeichnung();
    if (null != name)
      return name.trim();
    return null;
  }

  @Override
  public void setBic(String bic) throws RemoteException
  {
    setAttribute("bic", bic);
  }

  @Override
  public String getIban() throws RemoteException
  {
    String ret = (String) getAttribute("iban");
    if (ret == null)
    {
      return "";
    }
    return ret;
  }

  @Override
  public void setIban(String iban) throws RemoteException
  {
    setAttribute("iban", iban);
  }

  @Override
  public String getBlz() throws RemoteException
  {
    String ret = (String) getAttribute("blz");
    if (ret == null)
    {
      return "";
    }
    return ret;
  }

  @Override
  public void setBlz(String blz) throws RemoteException
  {
    setAttribute("blz", blz);
  }

  @Override
  public String getKonto() throws RemoteException
  {
    String ret = (String) getAttribute("konto");
    if (ret == null)
    {
      return "";
    }
    return ret;
  }

  @Override
  public void setKonto(String konto) throws RemoteException
  {
    setAttribute("konto", konto);
  }

  @Override
  public String getKtoiPersonenart() throws RemoteException
  {
    String ret = (String) getAttribute("ktoipersonenart");
    if (ret == null)
    {
      ret = "n";
    }
    return ret;
  }

  @Override
  public void setKtoiPersonenart(String ktoipersonenart) throws RemoteException
  {
    setAttribute("ktoipersonenart", ktoipersonenart);
  }

  @Override
  public String getKtoiAnrede() throws RemoteException
  {
    return (String) getAttribute("ktoianrede");
  }

  @Override
  public void setKtoiAnrede(String ktoianrede) throws RemoteException
  {
    setAttribute("ktoianrede", ktoianrede);
  }

  @Override
  public String getKtoiTitel() throws RemoteException
  {
    return (String) getAttribute("ktoititel");
  }

  @Override
  public void setKtoiTitel(String ktoititel) throws RemoteException
  {
    setAttribute("ktoititel", ktoititel);
  }

  @Override
  public String getKtoiName() throws RemoteException
  {
    return (String) getAttribute("ktoiname");
  }

  @Override
  public void setKtoiName(String ktoiname) throws RemoteException
  {
    setAttribute("ktoiname", ktoiname);
  }

  @Override
  public String getKtoiVorname() throws RemoteException
  {
    return (String) getAttribute("ktoivorname");
  }

  @Override
  public void setKtoiVorname(String ktoivorname) throws RemoteException
  {
    setAttribute("ktoivorname", ktoivorname);
  }

  @Override
  public String getKtoiStrasse() throws RemoteException
  {
    return (String) getAttribute("ktoistrasse");
  }

  @Override
  public void setKtoiStrasse(String ktoistrasse) throws RemoteException
  {
    setAttribute("ktoistrasse", ktoistrasse);
  }

  @Override
  public String getKtoiAdressierungszusatz() throws RemoteException
  {
    return (String) getAttribute("ktoiadressierungszusatz");
  }

  @Override
  public void setKtoiAdressierungszusatz(String ktoiadressierungszusatz)
      throws RemoteException
  {
    setAttribute("ktoiadressierungszusatz", ktoiadressierungszusatz);
  }

  @Override
  public String getKtoiPlz() throws RemoteException
  {
    return (String) getAttribute("ktoiplz");
  }

  @Override
  public void setKtoiPlz(String ktoiplz) throws RemoteException
  {
    setAttribute("ktoiplz", ktoiplz);
  }

  @Override
  public String getKtoiOrt() throws RemoteException
  {
    return (String) getAttribute("ktoiort");
  }

  @Override
  public void setKtoiOrt(String ktoiort) throws RemoteException
  {
    setAttribute("ktoiort", ktoiort);
  }

  @Override
  public String getKtoiStaat() throws RemoteException
  {
    return (String) getAttribute("ktoistaat");
  }

  @Override
  public void setKtoiStaat(String ktoistaat) throws RemoteException
  {
    setAttribute("ktoistaat", ktoistaat);
  }

  @Override
  public String getKtoiEmail() throws RemoteException
  {
    return (String) getAttribute("ktoiemail");
  }

  @Override
  public void setKtoiEmail(String ktoiemail) throws RemoteException
  {
    setAttribute("ktoiemail", ktoiemail);
  }

  /**
   * art = 1: Name, Vorname
   */
  @Override
  public String getKontoinhaber(int art) throws RemoteException
  {
    Mitglied m2 = (Mitglied) Einstellungen.getDBService().createObject(
        Mitglied.class, getID());
    if (m2.getKtoiVorname() != null && m2.getKtoiVorname().length() > 0)
    {
      m2.setVorname(getKtoiVorname());
      m2.setPersonenart(getKtoiPersonenart());
    }
    if (m2.getKtoiName() != null && m2.getKtoiName().length() > 0)
    {
      m2.setName(getKtoiName());
      m2.setPersonenart(getKtoiPersonenart());
    }
    if (m2.getKtoiAnrede() != null && m2.getKtoiAnrede().length() > 0)
    {
      m2.setAnrede(getKtoiAnrede());
    }
    if (m2.getKtoiTitel() != null && m2.getKtoiTitel().length() > 0)
    {
      m2.setTitel(getKtoiTitel());
    }
    switch (art)
    {
      case 1:
        return Adressaufbereitung.getNameVorname(m2);
    }
    return null;
  }

  @Override
  public Date getGeburtsdatum() throws RemoteException
  {
    Date d = (Date) getAttribute("geburtsdatum");
    if (d == null)
    {
      return Einstellungen.NODATE;
    }
    return d;
  }

  @Override
  public Integer getAlter() throws RemoteException
  {
    Date geburtstag = getGeburtsdatum();
    int altersmodel = Einstellungen.getEinstellung().getAltersModel();
    return Datum.getAlter(geburtstag, altersmodel);
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
  public String getTelefonprivat() throws RemoteException
  {
    String telefon = (String) getAttribute("telefonprivat");
    if (telefon == null)
    {
      telefon = "";
    }
    return telefon;
  }

  @Override
  public void setTelefonprivat(String telefonprivat) throws RemoteException
  {
    setAttribute("telefonprivat", telefonprivat);
  }

  @Override
  public String getTelefondienstlich() throws RemoteException
  {
    String telefon = (String) getAttribute("telefondienstlich");
    if (telefon == null)
    {
      telefon = "";
    }
    return telefon;
  }

  @Override
  public void setTelefondienstlich(String telefondienstlich)
      throws RemoteException
  {
    setAttribute("telefondienstlich", telefondienstlich);
  }

  @Override
  public String getHandy() throws RemoteException
  {
    String telefon = (String) getAttribute("handy");
    if (telefon == null)
    {
      telefon = "";
    }
    return telefon;
  }

  @Override
  public void setHandy(String handy) throws RemoteException
  {
    setAttribute("handy", handy);
  }

  @Override
  public String getEmail() throws RemoteException
  {
    String email = (String) getAttribute("email");
    if (email == null)
    {
      email = "";
    }
    return email;
  }

  @Override
  public void setEmail(String email) throws RemoteException
  {
    setAttribute("email", email);
  }

  @Override
  public Date getEintritt() throws RemoteException
  {
    Date d = (Date) getAttribute("eintritt");
    if (d == null)
    {
      return Einstellungen.NODATE;
    }
    return d;
  }

  @Override
  public void setEintritt(Date eintritt) throws RemoteException
  {
    setAttribute("eintritt", eintritt);
  }

  @Override
  public void setEintritt(String eintritt) throws RemoteException
  {
    setAttribute("eintritt", toDate(eintritt));
  }

  @Override
  public Beitragsgruppe getBeitragsgruppe() throws RemoteException
  {
    return (Beitragsgruppe) getAttribute("beitragsgruppe");
  }

  @Override
  public int getBeitragsgruppeId() throws RemoteException
  {
    return Integer.parseInt(getBeitragsgruppe().getID());
  }

  @Override
  public void setBeitragsgruppe(Integer beitragsgruppe) throws RemoteException
  {
    setAttribute("beitragsgruppe", beitragsgruppe);
  }

  @Override
  public Double getIndividuellerBeitrag() throws RemoteException
  {
    Double d = (Double) getAttribute("individuellerbeitrag");
    if (d == null)
    {
      return new Double(0);
    }
    return d;
  }

  @Override
  public void setIndividuellerBeitrag(double d) throws RemoteException
  {
    setAttribute("individuellerbeitrag", new Double(d));
  }

  @Override
  public Mitgliedfoto getFoto() throws RemoteException
  {
    return (Mitgliedfoto) getAttribute("foto");
  }

  @Override
  public void setFoto(Mitgliedfoto foto) throws RemoteException
  {
    setAttribute("foto", foto);
  }

  @Override
  public Integer getZahlerID() throws RemoteException
  {
    Integer zahlerid = (Integer) getAttribute("zahlerid");
    return zahlerid;
  }

  @Override
  public void setZahlerID(Integer id) throws RemoteException
  {
    setAttribute("zahlerid", id);
  }

  @Override
  public Date getAustritt() throws RemoteException
  {
    return (Date) getAttribute("austritt");
  }

  @Override
  public void setAustritt(Date austritt) throws RemoteException
  {
    setAttribute("austritt", austritt);
  }

  @Override
  public void setAustritt(String austritt) throws RemoteException
  {
    setAttribute("austritt", toDate(austritt));
  }

  @Override
  public Date getKuendigung() throws RemoteException
  {
    return (Date) getAttribute("kuendigung");
  }

  @Override
  public void setKuendigung(Date kuendigung) throws RemoteException
  {
    setAttribute("kuendigung", kuendigung);
  }

  @Override
  public void setKuendigung(String kuendigung) throws RemoteException
  {
    setAttribute("kuendigung", toDate(kuendigung));
  }

  @Override
  public Date getSterbetag() throws RemoteException
  {
    return (Date) getAttribute("sterbetag");
  }

  @Override
  public void setSterbetag(Date sterbetag) throws RemoteException
  {
    setAttribute("sterbetag", sterbetag);
  }

  @Override
  public void setSterbetag(String sterbetag) throws RemoteException
  {
    setAttribute("sterbetag", toDate(sterbetag));
  }

  @Override
  public String getVermerk1() throws RemoteException
  {
    return (String) getAttribute("vermerk1");
  }

  @Override
  public void setVermerk1(String vermerk1) throws RemoteException
  {
    setAttribute("vermerk1", vermerk1);
  }

  @Override
  public String getVermerk2() throws RemoteException
  {
    return (String) getAttribute("vermerk2");
  }

  @Override
  public void setVermerk2(String vermerk2) throws RemoteException
  {
    setAttribute("vermerk2", vermerk2);
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
  public void setLetzteAenderung() throws RemoteException
  {
    setAttribute("letzteaenderung", new Date());
  }

  @Override
  public Date getLetzteAenderung() throws RemoteException
  {
    return (Date) getAttribute("letzteaenderung");
  }

  @Override
  public Map<String, Object> getMap(Map<String, Object> inma)
      throws RemoteException
  {
    return getMap(inma, false);
  }

  @Override
  public Map<String, Object> getMap(Map<String, Object> inma,
      boolean ohneLesefelder) throws RemoteException
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
      this.setAdressierungszusatz("3. Hinterhof");
      this.setAdresstyp(1);
      this.setAnrede("Herrn");
      this.setAustritt("01.04.2011");
      DBIterator it = Einstellungen.getDBService().createList(
          Beitragsgruppe.class);
      Beitragsgruppe bg = (Beitragsgruppe) it.next();
      this.setBeitragsgruppe(Integer.parseInt(bg.getID()));
      this.setBic("XXXXXXXXXXX");
      this.setBlz("10020030");
      this.setEingabedatum();
      this.setEintritt("05.02.1999");
      this.setEmail("willi.wichtig@jverein.de");
      this.setExterneMitgliedsnummer(123456);
      this.setGeburtsdatum("02.03.1980");
      this.setGeschlecht("M");
      this.setHandy("0170/123456789");
      this.setIban("DE89370400440532013000");
      this.setID("1");
      this.setIndividuellerBeitrag(123.45);
      this.setKonto("1234567890");
      this.setKtoiPersonenart("n");
      this.setKtoiAnrede("Herrn");
      this.setKtoiTitel("Dr. Dr.");
      this.setKtoiName("Wichtig");
      this.setKtoiVorname("Willi");
      this.setKtoiStrasse("Bahnhofstr. 22");
      this.setAdressierungszusatz("Hinterhof bei Lieschen Müller");
      this.setPlz("12345");
      this.setOrt("Testenhausen");
      this.setKuendigung("21.02.2011");
      this.setLetzteAenderung();
      this.setName("Wichtig");
      this.setOrt("Testenhausen");
      this.setPersonenart("n");
      this.setPlz("12345");
      this.setStaat("Deutschland");
      this.setSterbetag(new Date());
      this.setStrasse("Hafengasse 124");
      this.setTelefondienstlich("123455600");
      this.setTelefonprivat("123456");
      this.setTitel("Dr.");
      this.setVermerk1("Vermerk 1");
      this.setVermerk2("Vermerk 2");
      this.setVorname("Willi");
      this.setZahlungsrhytmus(12);
      this.setZahlungsweg(1);
    }
    map.put(MitgliedVar.ADRESSIERUNGSZUSATZ.getName(),
        StringTool.toNotNullString(this.getAdressierungszusatz()));
    map.put(MitgliedVar.ADRESSTYP.getName(),
        StringTool.toNotNullString(this.getAdresstyp().getID()));
    map.put(MitgliedVar.ANREDE.getName(),
        StringTool.toNotNullString(this.getAnrede()));
    String anredefoermlich = "Sehr geehrte";
    if (getGeschlecht() != null)
    {
      if (getGeschlecht().equals("m"))
      {
        anredefoermlich += "r Herr " + getTitel()
            + (getTitel().length() > 0 ? " " : "") + getName() + ",";
      }
      else if (getGeschlecht().equals("w"))
      {
        anredefoermlich += " Frau " + getTitel()
            + (getTitel().length() > 0 ? " " : "") + getName() + ",";
      }
      else
      {
        anredefoermlich += " Damen und Herren,";
      }
    }
    else
    {
      anredefoermlich += " Damen und Herren,";
    }
    map.put(MitgliedVar.ANREDE_FOERMLICH.getName(), anredefoermlich);
    String anrededu = "Hallo";
    if (getPersonenart().equals("n"))
    {
      anrededu += " " + getVorname();
    }
    anrededu += ",";
    map.put(MitgliedVar.ANREDE_DU.getName(), anrededu);

    map.put(MitgliedVar.AUSTRITT.getName(),
        Datum.formatDate(this.getAustritt()));
    map.put(
        MitgliedVar.BEITRAGSGRUPPE_ARBEITSEINSATZ_BETRAG.getName(),
        this.getBeitragsgruppe() != null ? Einstellungen.DECIMALFORMAT
            .format(this.getBeitragsgruppe().getArbeitseinsatzBetrag()) : "");
    map.put(
        MitgliedVar.BEITRAGSGRUPPE_ARBEITSEINSATZ_STUNDEN.getName(),
        this.getBeitragsgruppe() != null ? Einstellungen.DECIMALFORMAT
            .format(this.getBeitragsgruppe().getArbeitseinsatzStunden()) : "");
    map.put(
        MitgliedVar.BEITRAGSGRUPPE_BETRAG.getName(),
        this.getBeitragsgruppe() != null ? Einstellungen.DECIMALFORMAT
            .format(this.getBeitragsgruppe().getBetrag()) : "");
    map.put(MitgliedVar.BEITRAGSGRUPPE_BEZEICHNUNG.getName(), this
        .getBeitragsgruppe() != null ? this.getBeitragsgruppe()
        .getBezeichnung() : "");
    map.put(MitgliedVar.BEITRAGSGRUPPE_ID.getName(),
        this.getBeitragsgruppe() != null ? this.getBeitragsgruppe().getID()
            : "");
    map.put(MitgliedVar.MANDATDATUM.getName(), this.getMandatDatum());
    map.put(MitgliedVar.MANDATDATUM.getName(), this.getMandatID());
    map.put(MitgliedVar.BIC.getName(), this.getBic());
    map.put(MitgliedVar.BLZ.getName(), this.getBlz());
    map.put(MitgliedVar.EINGABEDATUM.getName(),
        Datum.formatDate(this.getEingabedatum()));
    map.put(MitgliedVar.EINTRITT.getName(),
        Datum.formatDate(this.getEintritt()));
    map.put(MitgliedVar.EMAIL.getName(), this.getEmail());
    map.put(MitgliedVar.EMPFAENGER.getName(),
        Adressaufbereitung.getAdressfeld(this));
    map.put(MitgliedVar.EXTERNE_MITGLIEDSNUMMER.getName(),
        this.getExterneMitgliedsnummer());
    map.put(MitgliedVar.GEBURTSDATUM.getName(),
        Datum.formatDate(this.getGeburtsdatum()));
    map.put(MitgliedVar.GESCHLECHT.getName(), this.getGeschlecht());
    map.put(MitgliedVar.HANDY.getName(), this.getHandy());
    map.put(MitgliedVar.IBAN.getName(), this.getIban());
    map.put(MitgliedVar.ID.getName(), this.getID());
    map.put(MitgliedVar.INDIVIDUELLERBEITRAG.getName(),
        Einstellungen.DECIMALFORMAT.format(this.getIndividuellerBeitrag()));
    map.put(MitgliedVar.BANKNAME.getName(), this.getBankname());
    map.put(MitgliedVar.KONTO.getName(), this.getKonto());
    map.put(MitgliedVar.KONTOINHABER_ADRESSIERUNGSZUSATZ.getName(),
        this.getKtoiAdressierungszusatz());
    map.put(MitgliedVar.KONTOINHABER_ANREDE.getName(), this.getKtoiAnrede());
    map.put(MitgliedVar.KONTOINHABER_EMAIL.getName(), this.getKtoiEmail());
    map.put(MitgliedVar.KONTOINHABER_NAME.getName(), this.getKtoiName());
    map.put(MitgliedVar.KONTOINHABER_ORT.getName(), this.getKtoiOrt());
    map.put(MitgliedVar.KONTOINHABER_PERSONENART.getName(),
        this.getKtoiPersonenart());
    map.put(MitgliedVar.KONTOINHABER_PLZ.getName(), this.getKtoiPlz());
    map.put(MitgliedVar.KONTOINHABER_STAAT.getName(), this.getKtoiStaat());
    map.put(MitgliedVar.KONTOINHABER_STRASSE.getName(), this.getKtoiStrasse());
    map.put(MitgliedVar.KONTOINHABER_TITEL.getName(), this.getKtoiTitel());
    map.put(MitgliedVar.KONTOINHABER_VORNAME.getName(), this.getKtoiVorname());
    map.put(MitgliedVar.KUENDIGUNG.getName(),
        Datum.formatDate(this.getKuendigung()));
    map.put(MitgliedVar.LETZTEAENDERUNG.getName(),
        Datum.formatDate(this.getLetzteAenderung()));
    map.put(MitgliedVar.NAME.getName(), this.getName());
    map.put(MitgliedVar.NAMEVORNAME.getName(),
        Adressaufbereitung.getNameVorname(this));
    map.put(MitgliedVar.ORT.getName(), this.getOrt());
    map.put(MitgliedVar.PERSONENART.getName(), this.getPersonenart());
    map.put(MitgliedVar.PLZ.getName(), this.getPlz());
    map.put(MitgliedVar.STAAT.getName(), this.getStaat());
    map.put(MitgliedVar.STERBETAG.getName(),
        Datum.formatDate(this.getSterbetag()));
    map.put(MitgliedVar.STRASSE.getName(), this.getStrasse());
    map.put(MitgliedVar.TELEFONDIENSTLICH.getName(),
        this.getTelefondienstlich());
    map.put(MitgliedVar.TELEFONPRIVAT.getName(), this.getTelefonprivat());
    map.put(MitgliedVar.TITEL.getName(), this.getTitel());
    map.put(MitgliedVar.VERMERK1.getName(), this.getVermerk1());
    map.put(MitgliedVar.VERMERK2.getName(), this.getVermerk2());
    map.put(MitgliedVar.VORNAME.getName(), this.getVorname());
    map.put(MitgliedVar.VORNAMENAME.getName(),
        Adressaufbereitung.getVornameName(this));
    map.put(MitgliedVar.ZAHLERID.getName(), this.getZahlerID());
    map.put(MitgliedVar.ZAHLUNGSRHYTMUS.getName(), this.getZahlungsrhytmus()
        + "");
    map.put(MitgliedVar.ZAHLUNGSWEG.getName(), this.getZahlungsweg() + "");

    String zahlungsweg = "";
    switch (this.getZahlungsweg())
    {
      case Zahlungsweg.BASISLASTSCHRIFT:
      {
        zahlungsweg = Einstellungen.getEinstellung().getRechnungTextAbbuchung();
        zahlungsweg = zahlungsweg.replaceAll("\\$\\{Konto\\}", this.getKonto());
        zahlungsweg = zahlungsweg.replaceAll("\\$\\{BLZ\\}", this.getBlz());
        zahlungsweg = zahlungsweg.replaceAll("\\$\\{BIC\\}", this.getBic());
        zahlungsweg = zahlungsweg.replaceAll("\\$\\{IBAN\\}", this.getIban());
        break;
      }
      case Zahlungsweg.BARZAHLUNG:
      {
        zahlungsweg = Einstellungen.getEinstellung().getRechnungTextBar();
        break;
      }
      case Zahlungsweg.ÜBERWEISUNG:
      {
        zahlungsweg = Einstellungen.getEinstellung()
            .getRechnungTextUeberweisung();
        break;
      }
    }
    map.put(MitgliedVar.ZAHLUNGSWEGTEXT.getName(), zahlungsweg);

    HashMap<String, String> format = new HashMap<String, String>();
    DBIterator itfd = Einstellungen.getDBService().createList(
        Felddefinition.class);
    while (itfd.hasNext())
    {
      Felddefinition fd = (Felddefinition) itfd.next();
      DBIterator itzus = Einstellungen.getDBService().createList(
          Zusatzfelder.class);
      itzus.addFilter("mitglied = ? and felddefinition = ? ", new Object[] {
          getID(), fd.getID() });
      Zusatzfelder z = null;
      if (itzus.hasNext())
      {
        z = (Zusatzfelder) itzus.next();
      }
      else
      {
        z = (Zusatzfelder) Einstellungen.getDBService().createObject(
            Zusatzfelder.class, null);
      }

      switch (fd.getDatentyp())
      {
        case Datentyp.DATUM:
          map.put(Einstellungen.ZUSATZFELD_PRE + fd.getName(),
              Datum.formatDate(z.getFeldDatum()));
          format.put(Einstellungen.ZUSATZFELD_PRE + fd.getName(), "DATE");
          break;
        case Datentyp.JANEIN:
          map.put(Einstellungen.ZUSATZFELD_PRE + fd.getName(),
              z.getFeldJaNein() ? "X" : " ");
          break;
        case Datentyp.GANZZAHL:
          map.put(Einstellungen.ZUSATZFELD_PRE + fd.getName(),
              z.getFeldGanzzahl() + "");
          format.put(Einstellungen.ZUSATZFELD_PRE + fd.getName(), "INTEGER");
          break;
        case Datentyp.WAEHRUNG:
          if (z.getFeldWaehrung() != null)
          {
            map.put(Einstellungen.ZUSATZFELD_PRE + fd.getName(),
                Einstellungen.DECIMALFORMAT.format(z.getFeldWaehrung()));
            format.put(Einstellungen.ZUSATZFELD_PRE + fd.getName(), "DOUBLE");
          }
          else
          {
            map.put(Einstellungen.ZUSATZFELD_PRE + fd.getName(), "");
          }
          break;
        case Datentyp.ZEICHENFOLGE:
          map.put(Einstellungen.ZUSATZFELD_PRE + fd.getName(), z.getFeld());
          break;
      }
    }

    DBIterator iteig = Einstellungen.getDBService().createList(
        Eigenschaft.class);
    while (iteig.hasNext())
    {
      Eigenschaft eig = (Eigenschaft) iteig.next();
      DBIterator iteigm = Einstellungen.getDBService().createList(
          Eigenschaften.class);
      iteigm.addFilter("mitglied = ? and eigenschaft = ?",
          new Object[] { this.getID(), eig.getID() });
      String val = "";
      if (iteigm.size() > 0)
      {
        val = "X";
      }
      map.put("mitglied_eigenschaft_" + eig.getBezeichnung(), val);
    }

    for (String varname : variable.keySet())
    {
      map.put(varname, variable.get(varname));
    }

    if (!ohneLesefelder)
    {
      // Füge Lesefelder diesem Mitglied-Objekt hinzu.
      LesefeldAuswerter l = new LesefeldAuswerter();
      l.setLesefelderDefinitionsFromDatabase();
      l.setMap(map);
      map.putAll(l.getLesefelderMap());
    }

    return map;
  }

  @Override
  public boolean isAngemeldet(Date stichtag) throws RemoteException
  {
    return ((getEintritt() != null || getEintritt().before(stichtag))
        && getAustritt() == null || getAustritt().after(stichtag));
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    if (fieldName.equals("idint"))
    {
      return new Integer(getID());
    }
    if (fieldName.equals("namevorname"))
    {
      return Adressaufbereitung.getNameVorname(this);
    }
    else if (fieldName.equals("vornamename"))
    {
      return Adressaufbereitung.getVornameName(this);
    }
    else if (fieldName.equals("empfaenger"))
    {
      return Adressaufbereitung.getAdressfeld(this);
    }
    else if (fieldName.startsWith("zusatzfelder."))
    {
      DBIterator it = Einstellungen.getDBService().createList(
          Felddefinition.class);
      it.addFilter("name = ?", new Object[] { fieldName.substring(13) });
      Felddefinition fd = (Felddefinition) it.next();
      it = Einstellungen.getDBService().createList(Zusatzfelder.class);
      it.addFilter("felddefinition = ? AND mitglied = ?",
          new Object[] { fd.getID(), getID() });
      if (it.hasNext())
      {
        Zusatzfelder zf = (Zusatzfelder) it.next();
        switch (fd.getDatentyp())
        {
          case Datentyp.ZEICHENFOLGE:
            return zf.getFeld();
          case Datentyp.DATUM:
            return zf.getFeldDatum();
          case Datentyp.GANZZAHL:
            return zf.getFeldGanzzahl();
          case Datentyp.JANEIN:
            return zf.getFeldJaNein();
          case Datentyp.WAEHRUNG:
            return zf.getFeldWaehrung();
        }
      }
      else
      {
        switch (fd.getDatentyp())
        {
          case Datentyp.GANZZAHL:
            return null;
          default:
            return "";
        }
      }
    }
    else if ("alter".equals(fieldName))
    {
      return getAlter();
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

  @Override
  public void addVariable(String name, String wert)
  {
    variable.put(name, wert);
  }

}
