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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.io.BeitragsUtil;
import de.jost_net.JVerein.io.Adressbuch.Adressaufbereitung;
import de.jost_net.JVerein.keys.ArtBeitragsart;
import de.jost_net.JVerein.keys.Datentyp;
import de.jost_net.JVerein.keys.SepaMandatIdSource;
import de.jost_net.JVerein.keys.Zahlungsrhythmus;
import de.jost_net.JVerein.keys.Zahlungstermin;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Adresstyp;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Felddefinition;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedfoto;
import de.jost_net.JVerein.rmi.Zusatzfelder;
import de.jost_net.JVerein.util.Datum;
import de.jost_net.JVerein.util.EmailValidator;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.jost_net.OBanToo.SEPA.BIC;
import de.jost_net.OBanToo.SEPA.IBAN;
import de.jost_net.OBanToo.SEPA.SEPAException;
import de.jost_net.OBanToo.SEPA.SEPAException.Fehler;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class MitgliedImpl extends AbstractDBObject implements Mitglied
{

  private transient Map<String, String> variable;

  private static final long serialVersionUID = 1L;

  public MitgliedImpl() throws RemoteException
  {
    super();
    variable = new HashMap<>();
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

  @SuppressWarnings("unused")
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
      if (!EmailValidator.isValid(getEmail()))
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
        && BeitragsUtil.getBeitrag(
            Einstellungen.getEinstellung().getBeitragsmodel(),
            this.getZahlungstermin(), this.getZahlungsrhythmus().getKey(),
            this.getBeitragsgruppe(), new Date(), getEintritt(),
            getAustritt()) > 0)
    {
      if (getBic() == null || getBic().length() == 0 || getIban() == null
          || getIban().length() == 0)
      {
        throw new ApplicationException("Bitte BIC und IBAN eingeben");
      }
    }
    if (getIban() != null && getIban().length() != 0)
    {
      try
      {
        new IBAN(getIban());
      }
      catch (SEPAException e)
      {
        if (e.getFehler() != Fehler.UNGUELTIGES_LAND)
        {
          throw new ApplicationException(e.getMessage());
        }
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
        if (!e.getMessage().startsWith("Ungültiges Land"))
        {
          throw new ApplicationException(e.getMessage());
        }
      }
    }
    if (getZahlungsrhythmus() == null)
    {
      throw new ApplicationException(
          "Ungültiger Zahlungsrhytmus: " + getZahlungsrhythmus());
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
      if (getBeitragsgruppe().getBeitragsArt() == ArtBeitragsart.FAMILIE_ZAHLER)
      {
        // ja
        DBIterator<Mitglied> famang = Einstellungen.getDBService()
            .createList(Mitglied.class);
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
        && getBeitragsgruppe()
            .getBeitragsArt() == ArtBeitragsart.FAMILIE_ANGEHOERIGER
        && getZahlerID() == null)
    {
      throw new ApplicationException("Bitte Zahler auswählen!");
    }
  }

  /***
   * Prüfe die externe Mitgliedsnummer. Ist es ein Mitgliedssatz und ist in den
   * Einstellungen die externe Mitgliedsnummer aktiviert, dann muss eine
   * vorhanden sein und diese muss eindeutig sein.
   * 
   * @throws RemoteException
   * @throws ApplicationException
   */
  private void checkExterneMitgliedsnummer()
      throws RemoteException, ApplicationException
  {
    if (getAdresstyp().getJVereinid() != 1)
      return;
    if (Einstellungen.getEinstellung().getExterneMitgliedsnummer() == false)
      return;

    if (getExterneMitgliedsnummer() == null)
    {
      throw new ApplicationException("Externe Mitgliedsnummer fehlt");
    }

    DBIterator<Mitglied> mitglieder = Einstellungen.getDBService()
        .createList(Mitglied.class);
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
  public void setExterneMitgliedsnummer(String extnr) throws RemoteException
  {
    setAttribute("externemitgliedsnummer", extnr);
  }

  @Override
  public String getExterneMitgliedsnummer() throws RemoteException
  {
    return (String) getAttribute("externemitgliedsnummer");
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
  public Zahlungsrhythmus getZahlungsrhythmus() throws RemoteException
  {
    if (getAttribute("zahlungsrhytmus") != null)
    {
      return new Zahlungsrhythmus((Integer) getAttribute("zahlungsrhytmus"));
    }
    else
    {
      return null;
    }
  }

  @Override
  public void setZahlungsrhythmus(Integer zahlungsrhythmus)
      throws RemoteException
  {
    setAttribute("zahlungsrhytmus", zahlungsrhythmus);
  }

  @Override
  public void setZahlungstermin(Integer zahlungstermin) throws RemoteException
  {
    setAttribute("zahlungstermin", zahlungstermin);
  }

  @Override
  public Zahlungstermin getZahlungstermin() throws RemoteException
  {
    Integer i = (Integer) getAttribute("zahlungstermin");
    if (i == null)
    {
      return null;
    }
    return Zahlungstermin.getByKey(i);
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
    int sepaMandatIdSource = Einstellungen.getEinstellung()
        .getSepaMandatIdSource();
    if (sepaMandatIdSource == SepaMandatIdSource.EXTERNE_MITGLIEDSNUMMER)
    {
      return getExterneMitgliedsnummer() + "-" + getMandatVersion();
    }
    else
    {
      return getID() + "-" + getMandatVersion();
    }
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
        + "where lastschrift.ABRECHNUNGSLAUF = abrechnungslauf.id and lastschrift.MITGLIED = ? and lastschrift.mandatid = ?";
    Date d = (Date) Einstellungen.getDBService().execute(sql,
        new Object[] { getID(), getMandatID() }, rs);

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

  @Override
  public String getKtoiGeschlecht() throws RemoteException
  {
    return (String) getAttribute("ktoigeschlecht");
  }

  @Override
  public void setKtoiGeschlecht(String ktoigeschlecht) throws RemoteException
  {
    setAttribute("ktoigeschlecht", ktoigeschlecht);
  }

  /**
   * art = 1: Name, Vorname
   */
  @Override
  public String getKontoinhaber(int art) throws RemoteException
  {
    Mitglied m2 = (Mitglied) Einstellungen.getDBService()
        .createObject(Mitglied.class, getID());
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
  public void setIndividuellerBeitrag(Double d) throws RemoteException
  {
    setAttribute("individuellerbeitrag", d);
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
  public Long getZahlerID() throws RemoteException
  {
    Long zahlerid = (Long) getAttribute("zahlerid");
    return zahlerid;
  }

  @Override
  public void setZahlerID(Long id) throws RemoteException
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
  public boolean isAngemeldet(Date stichtag) throws RemoteException
  {
    return getEintritt() != null && !stichtag.before(getEintritt())
        && (getAustritt() == null || getAustritt().after(stichtag));
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
    else if (fieldName.startsWith("zusatzfelder_"))
    {
      DBIterator<Felddefinition> it = Einstellungen.getDBService()
          .createList(Felddefinition.class);
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

  @Override
  public void addVariable(String name, String wert)
  {
    variable.put(name, wert);
  }

  @Override
  public Map<String, String> getVariablen()
  {
    return variable;
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
