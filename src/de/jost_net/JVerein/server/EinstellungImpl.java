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
import de.jost_net.JVerein.io.AltersgruppenParser;
import de.jost_net.JVerein.io.JubilaeenParser;
import de.jost_net.JVerein.rmi.Einstellung;
import de.jost_net.JVerein.rmi.Felddefinition;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class EinstellungImpl extends AbstractDBObject implements Einstellung
{

  private static final long serialVersionUID = 3513343626868776722L;

  /**
   * Variable, in der gespeichert wird, ob für den Verein Zusatzfelder vorhanden
   * sind.
   */
  private Boolean hasZus = null;

  /**
   * settings speichert Benutzer-Präferenzen in einer config-Datei. Sie
   * unterscheiden sich also auf jedem System und hängen nicht an der DB.
   */
  private Settings settings;

  public EinstellungImpl() throws RemoteException
  {
    super();
    settings = new Settings(this.getClass());
  }

  @Override
  protected String getTableName()
  {
    return "einstellung";
  }

  @Override
  public String getPrimaryAttribute()
  {
    return "id";
  }

  @Override
  protected void deleteCheck() throws ApplicationException
  {
    throw new ApplicationException("Einstellung darf nicht gelöscht werden");
  }

  @Override
  protected void insertCheck() throws ApplicationException
  {
    try
    {
      if (getName() == null || getName().length() == 0)
      {
        throw new ApplicationException("Bitte Namen eingeben");
      }
      if (getBlz() == null || getBlz().length() == 0)
      {
        throw new ApplicationException("Bitte Bankleitzahl eingeben");
      }
      try
      {
        Integer.parseInt(getBlz());
      }
      catch (NumberFormatException e)
      {
        throw new ApplicationException(
            "Bankleitzahl enthält unzulässige Zeichen!");
      }
      if (getKonto() == null || getKonto().length() == 0)
      {
        throw new ApplicationException("Bitte Kontonummer eingeben");
      }
      try
      {
        Long.parseLong(getKonto());
      }
      catch (NumberFormatException e)
      {
        throw new ApplicationException(
            "Kontonummer enthält unzulässige Zeichen!");
      }
      if (!Einstellungen.checkAccountCRC(getBlz(), getKonto()))
      {
        throw new ApplicationException(
            "Ungültige BLZ/Kontonummer. Bitte prüfen Sie Ihre Eingaben.");
      }

      // TODO muss noch sauber implementiert werden
      // try
      // {
      // if (!SEPA.isValid(getBic(), getIban()))
      // {
      // throw new ApplicationException("Ungültige Kombination BIC und IBAN");
      // }
      // }
      // catch (SEPAException e1)
      // {
      // e1.printStackTrace();
      // }
      try
      {
        new AltersgruppenParser(getAltersgruppen());
      }
      catch (RuntimeException e)
      {
        throw new ApplicationException(e.getMessage());
      }

      if (getDokumentenspeicherung())
      {
        if (!JVereinPlugin.isArchiveServiceActive())
        {
          throw new ApplicationException(
              "Plugin jameica.messaging ist nicht installiert oder im LAN verfügbar! Wird zur Dokumentenspeicherung benötigt!");
        }
      }
      try
      {
        new JubilaeenParser(getJubilaeen());
      }
      catch (RuntimeException e)
      {
        throw new ApplicationException(e.getMessage());
      }
    }
    catch (RemoteException e)
    {
      String fehler = "Einstellung kann nicht gespeichert werden. Siehe system log";
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
    return null;
  }

  @Override
  public void setID() throws RemoteException
  {
    setAttribute("id", "1");
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
  public String getNameLang() throws RemoteException
  {
    return (String) getAttribute("namelang");
  }

  @Override
  public void setNameLang(String name) throws RemoteException
  {
    setAttribute("namelang", name);
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
  public String getFinanzamt() throws RemoteException
  {
    return (String) getAttribute("finanzamt");
  }

  @Override
  public void setFinanzamt(String finanzamt) throws RemoteException
  {
    setAttribute("finanzamt", finanzamt);
  }

  @Override
  public String getSteuernummer() throws RemoteException
  {
    return (String) getAttribute("steuernummer");
  }

  @Override
  public void setSteuernummer(String steuernummer) throws RemoteException
  {
    setAttribute("steuernummer", steuernummer);
  }

  @Override
  public Date getBescheiddatum() throws RemoteException
  {
    Date d = (Date) getAttribute("bescheiddatum");
    if (d == null)
    {
      return new Date();
    }
    return d;
  }

  @Override
  public void setBescheiddatum(Date bescheiddatum) throws RemoteException
  {
    setAttribute("bescheiddatum", bescheiddatum);
  }

  @Override
  public Boolean getVorlaeufig() throws RemoteException
  {
    return Util.getBoolean(getAttribute("vorlaeufig"));
  }

  @Override
  public void setVorlaeufig(Boolean vorlaeufig) throws RemoteException
  {
    setAttribute("vorlaeufig", Boolean.valueOf(vorlaeufig));
  }

  @Override
  public Date getVorlaeufigab() throws RemoteException
  {
    Date d = (Date) getAttribute("vorlaeufigab");
    if (d == null)
    {
      return new Date();
    }
    return d;
  }

  @Override
  public void setVorlaeufigab(Date vorlaeufigab) throws RemoteException
  {
    setAttribute("vorlaeufigab", vorlaeufigab);
  }

  @Override
  public String getBeguenstigterzweck() throws RemoteException
  {
    return (String) getAttribute("beguenstigterzweck");
  }

  @Override
  public void setBeguenstigterzweck(String beguenstigterzweck)
      throws RemoteException
  {
    setAttribute("beguenstigterzweck", beguenstigterzweck);
  }

  @Override
  public Boolean getMitgliedsbetraege() throws RemoteException
  {
    return Util.getBoolean(getAttribute("mitgliedsbeitraege"));
  }

  @Override
  public void setMitgliedsbeitraege(Boolean mitgliedsbeitraege)
      throws RemoteException
  {
    setAttribute("mitgliedsbeitraege", Boolean.valueOf(mitgliedsbeitraege));
  }

  @Override
  public String getBlz() throws RemoteException
  {
    return (String) getAttribute("blz");
  }

  @Override
  public void setBlz(String blz) throws RemoteException
  {
    setAttribute("blz", blz);
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
  public String getGlaeubigerID() throws RemoteException
  {
    return (String) getAttribute("glaeubigerid");
  }

  @Override
  public void setGlaeubigerID(String glaeubigerid) throws RemoteException
  {
    setAttribute("glaeubigerid", glaeubigerid);
  }

  @Override
  public String getKonto() throws RemoteException
  {
    return (String) getAttribute("konto");
  }

  @Override
  public void setKonto(String konto) throws RemoteException
  {
    setAttribute("konto", konto);
  }

  @Override
  public Boolean getGeburtsdatumPflicht() throws RemoteException
  {
    return Util.getBoolean(getAttribute("geburtsdatumpflicht"));
  }

  @Override
  public void setGeburtsdatumPflicht(Boolean geburtsdatumpflicht)
      throws RemoteException
  {
    setAttribute("geburtsdatumpflicht", Boolean.valueOf(geburtsdatumpflicht));
  }

  @Override
  public Boolean getEintrittsdatumPflicht() throws RemoteException
  {
    return Util.getBoolean(getAttribute("eintrittsdatumpflicht"));
  }

  @Override
  public void setEintrittsdatumPflicht(Boolean eintrittsdatumpflicht)
      throws RemoteException
  {
    setAttribute("eintrittsdatumpflicht",
        Boolean.valueOf(eintrittsdatumpflicht));
  }

  @Override
  public Boolean getSterbedatum() throws RemoteException
  {
    return Util.getBoolean(getAttribute("sterbedatum"));
  }

  @Override
  public void setSterbedatum(Boolean sterbedatum) throws RemoteException
  {
    setAttribute("sterbedatum", Boolean.valueOf(sterbedatum));
  }

  @Override
  public Boolean getKommunikationsdaten() throws RemoteException
  {
    return Util.getBoolean(getAttribute("kommunikationsdaten"));
  }

  @Override
  public void setKommunikationsdaten(Boolean kommunikationsdaten)
      throws RemoteException
  {
    setAttribute("kommunikationsdaten", Boolean.valueOf(kommunikationsdaten));
  }

  @Override
  public Boolean getZusatzbetrag() throws RemoteException
  {
    return Util.getBoolean(getAttribute("zusatzabbuchung"));
  }

  @Override
  public void setZusatzbetrag(Boolean zusatzabbuchung) throws RemoteException
  {
    setAttribute("zusatzabbuchung", Boolean.valueOf(zusatzabbuchung));
  }

  @Override
  public Boolean getVermerke() throws RemoteException
  {
    return Util.getBoolean(getAttribute("vermerke"));
  }

  @Override
  public void setVermerke(Boolean vermerke) throws RemoteException
  {
    setAttribute("vermerke", Boolean.valueOf(vermerke));
  }

  @Override
  public Boolean getWiedervorlage() throws RemoteException
  {
    return Util.getBoolean(getAttribute("wiedervorlage"));
  }

  @Override
  public void setWiedervorlage(Boolean wiedervorlage) throws RemoteException
  {
    setAttribute("wiedervorlage", Boolean.valueOf(wiedervorlage));
  }

  @Override
  public Boolean getKursteilnehmer() throws RemoteException
  {
    return Util.getBoolean(getAttribute("kursteilnehmer"));
  }

  @Override
  public void setKursteilnehmer(Boolean kursteilnehmer) throws RemoteException
  {
    setAttribute("kursteilnehmer", Boolean.valueOf(kursteilnehmer));
  }

  @Override
  public Boolean getLehrgaenge() throws RemoteException
  {
    return Util.getBoolean(getAttribute("lehrgaenge"));
  }

  @Override
  public void setLehrgaenge(Boolean lehrgaenge) throws RemoteException
  {
    setAttribute("lehrgaenge", Boolean.valueOf(lehrgaenge));
  }

  @Override
  public Boolean getJuristischePersonen() throws RemoteException
  {
    return Util.getBoolean(getAttribute("juristischepersonen"));
  }

  @Override
  public void setJuristischePersonen(Boolean juristischepersonen)
      throws RemoteException
  {
    setAttribute("juristischepersonen", Boolean.valueOf(juristischepersonen));
  }

  @Override
  public Boolean getMitgliedfoto() throws RemoteException
  {
    return Util.getBoolean(getAttribute("mitgliedfoto"));
  }

  @Override
  public void setMitgliedfoto(Boolean mitgliedfoto) throws RemoteException
  {
    setAttribute("mitgliedfoto", Boolean.valueOf(mitgliedfoto));
  }

  @Override
  public boolean getUseLesefelder() throws RemoteException
  {
    return Util.getBoolean(getAttribute("uselesefelder"));
  }

  @Override
  public void setUseLesefelder(boolean use) throws RemoteException
  {
    setAttribute("uselesefelder", Boolean.valueOf(use));
  }

  @Override
  public Boolean getZusatzadressen() throws RemoteException
  {
    return Util.getBoolean(getAttribute("zusatzadressen"));
  }

  @Override
  public void setZusatzadressen(Boolean zusatzadressen) throws RemoteException
  {
    setAttribute("zusatzadressen", Boolean.valueOf(zusatzadressen));
  }

  @Override
  public Boolean getAuslandsadressen() throws RemoteException
  {
    return Util.getBoolean(getAttribute("auslandsadressen"));
  }

  @Override
  public void setAuslandsadressen(Boolean auslandsadressen)
      throws RemoteException
  {
    setAttribute("auslandsadressen", Boolean.valueOf(auslandsadressen));
  }

  @Override
  public Boolean getArbeitseinsatz() throws RemoteException
  {
    return Util.getBoolean(getAttribute("arbeitseinsatz"));
  }

  @Override
  public void setArbeitseinsatz(Boolean arbeitseinsatz) throws RemoteException
  {
    setAttribute("arbeitseinsatz", Boolean.valueOf(arbeitseinsatz));
  }

  @Override
  public Boolean getDokumentenspeicherung() throws RemoteException
  {
    return Util.getBoolean(getAttribute("dokumentenspeicherung"));
  }

  @Override
  public void setDokumentenspeicherung(Boolean dokumentenspeicherung)
      throws RemoteException
  {
    setAttribute("dokumentenspeicherung",
        Boolean.valueOf(dokumentenspeicherung));
  }

  @Override
  public Boolean getIndividuelleBeitraege() throws RemoteException
  {
    return Util.getBoolean(getAttribute("individuellebeitraege"));
  }

  @Override
  public void setIndividuelleBeitraege(Boolean individuellebeitraege)
      throws RemoteException
  {
    setAttribute("individuellebeitraege",
        Boolean.valueOf(individuellebeitraege));
  }

  @Override
  public String getRechnungTextAbbuchung() throws RemoteException
  {
    String text = (String) getAttribute("rechnungtextabbuchung");
    if (text == null)
    {
      text = "Der Betrag wird vom Konto ${Konto} (BLZ ${BLZ}) abgebucht.";
    }
    return text;
  }

  @Override
  public void setRechnungTextAbbuchung(String rechnungtextabbuchung)
      throws RemoteException
  {
    setAttribute("rechnungtextabbuchung", rechnungtextabbuchung);
  }

  @Override
  public String getRechnungTextUeberweisung() throws RemoteException
  {
    String text = (String) getAttribute("rechnungtextueberweisung");
    if (text == null)
    {
      text = "Bitte überweisen Sie den Betrag auf das angegebene Konto.";
    }
    return text;
  }

  @Override
  public void setRechnungTextUeberweisung(String rechnungtextueberweisung)
      throws RemoteException
  {
    setAttribute("rechnungtextueberweisung", rechnungtextueberweisung);
  }

  @Override
  public String getRechnungTextBar() throws RemoteException
  {
    String text = (String) getAttribute("rechnungtextbar");
    if (text == null)
    {
      text = "Bitte zahlen Sie den Betrag auf das angegebene Konto ein.";
    }
    return text;
  }

  @Override
  public void setRechnungTextBar(String rechnungtextbar) throws RemoteException
  {
    setAttribute("rechnungtextbar", rechnungtextbar);
  }

  @Override
  public Boolean getExterneMitgliedsnummer() throws RemoteException
  {
    return Util.getBoolean(getAttribute("externemitgliedsnummer"));
  }

  @Override
  public void setExterneMitgliedsnummer(Boolean externemitgliedsnummer)
      throws RemoteException
  {
    setAttribute("externemitgliedsnummer",
        Boolean.valueOf(externemitgliedsnummer));
  }

  @Override
  public int getBeitragsmodel() throws RemoteException
  {
    return (Integer) getAttribute("beitragsmodel");
  }

  @Override
  public void setBeitragsmodel(int beitragsmodel) throws RemoteException
  {
    setAttribute("beitragsmodel", beitragsmodel);
  }

  @Override
  public String getDateinamenmuster() throws RemoteException
  {
    String muster = (String) getAttribute("dateinamenmuster");
    if (muster == null)
    {
      muster = "a$s$-d$-z$";
    }
    return muster;
  }

  @Override
  public void setDateinamenmuster(String dateinamenmuster)
      throws RemoteException
  {
    setAttribute("dateinamenmuster", dateinamenmuster);
  }

  @Override
  public String getDateinamenmusterSpende() throws RemoteException
  {
    String muster = (String) getAttribute("dateinamenmusterspende");
    if (muster == null)
    {
      muster = "a$-d$-n$-v$";
    }
    return muster;
  }

  @Override
  public void setDateinamenmusterSpende(String dateinamenmusterspende)
      throws RemoteException
  {
    setAttribute("dateinamenmusterspende", dateinamenmusterspende);
  }

  @Override
  public double getSpendenbescheinigungminbetrag() throws RemoteException
  {
    Double d = (Double) getAttribute("spendenbescheinigungminbetrag");
    if (d == null)
    {
      d = new Double(0);
    }
    return (d);
  }

  @Override
  public void setSpendenbescheinigungminbetrag(double minbetrag)
      throws RemoteException
  {
    setAttribute("spendenbescheinigungminbetrag", minbetrag);
  }

  @Override
  public String getSpendenbescheinigungverzeichnis() throws RemoteException
  {
    return (String) getAttribute("spendenbescheinigungverzeichnis");
  }

  @Override
  public void setSpendenbescheinigungverzeichnis(
      String spendenbescheinigungverzeichnis) throws RemoteException
  {
    setAttribute("spendenbescheinigungverzeichnis",
        spendenbescheinigungverzeichnis);
  }

  @Override
  public boolean getSpendenbescheinigungPrintBuchungsart()
      throws RemoteException
  {
    return Util.getBoolean(getAttribute("spendenbescheinigungprintbuchungsart"));
  }

  @Override
  public void setSpendenbescheinigungPrintBuchungsart(Boolean printbuchungsart)
      throws RemoteException
  {
    setAttribute("spendenbescheinigungprintbuchungsart",
        Boolean.valueOf(printbuchungsart));
  }

  @Override
  public String getBeginnGeschaeftsjahr() throws RemoteException
  {
    return (String) getAttribute("beginngeschaeftsjahr");
  }

  @Override
  public void setBeginnGeschaeftsjahr(String beginngeschaeftsjahr)
      throws RemoteException
  {
    setAttribute("beginngeschaeftsjahr", beginngeschaeftsjahr);
  }

  @Override
  public String getSmtpServer() throws RemoteException
  {
    return (String) getAttribute("smtp_server");
  }

  @Override
  public void setSmtpServer(String smtp_server) throws RemoteException
  {
    setAttribute("smtp_server", smtp_server);
  }

  @Override
  public String getSmtpPort() throws RemoteException
  {
    String ret = (String) getAttribute("smtp_port");
    if (ret == null)
    {
      ret = "25";
    }
    return ret;
  }

  @Override
  public void setSmtpPort(String smtp_port) throws RemoteException
  {
    setAttribute("smtp_port", smtp_port);
  }

  @Override
  public String getSmtpAuthUser() throws RemoteException
  {
    return (String) getAttribute("smtp_auth_user");
  }

  @Override
  public void setSmtpAuthUser(String smtp_auth_user) throws RemoteException
  {
    setAttribute("smtp_auth_user", smtp_auth_user);
  }

  @Override
  public String getSmtpAuthPwd() throws RemoteException
  {
    return (String) getAttribute("smtp_auth_pwd");
  }

  @Override
  public void setSmtpAuthPwd(String smtp_auth_pwd) throws RemoteException
  {
    setAttribute("smtp_auth_pwd", smtp_auth_pwd);
  }

  @Override
  public String getSmtpFromAddress() throws RemoteException
  {
    return (String) getAttribute("smtp_from_address");
  }

  @Override
  public void setSmtpFromAddress(String smtp_from_address)
      throws RemoteException
  {
    setAttribute("smtp_from_address", smtp_from_address);
  }

  @Override
  public String getSmtpFromAnzeigename() throws RemoteException
  {
    String ret = (String) getAttribute("smtp_from_anzeigename");
    if (ret == null || ret.length() == 0)
    {
      ret = getSmtpFromAddress();
    }
    return ret;
  }

  @Override
  public void setSmtpFromAnzeigename(String smtp_from_anzeigename)
      throws RemoteException
  {
    setAttribute("smtp_from_anzeigename", smtp_from_anzeigename);
  }

  @Override
  public Boolean getSmtpSsl() throws RemoteException
  {
    return Util.getBoolean(getAttribute("smtp_ssl"));
  }

  @Override
  public void setSmtpSsl(Boolean smtp_ssl) throws RemoteException
  {
    setAttribute("smtp_ssl", smtp_ssl);
  }

  @Override
  public Boolean getSmtpStarttls() throws RemoteException
  {
    return Util.getBoolean(getAttribute("smtp_starttls"));
  }

  @Override
  public void setSmtpStarttls(Boolean smtp_starttls) throws RemoteException
  {
    setAttribute("smtp_starttls", smtp_starttls);
  }

  @Override
  public int getZahlungsrhytmus() throws RemoteException
  {
    try
    {
      return (Integer) getAttribute("zahlungsrhytmus");
    }
    catch (NullPointerException e)
    {
      return 12;
    }
  }

  @Override
  public void setZahlungsrhytmus(int zahlungsrhytmus) throws RemoteException
  {
    setAttribute("zahlungsrhytmus", zahlungsrhytmus);
  }

  @Override
  public int getZahlungsweg() throws RemoteException
  {
    try
    {
      return (Integer) getAttribute("zahlungsweg");
    }
    catch (NullPointerException e)
    {
      return 1;
    }
  }

  @Override
  public void setZahlungsweg(int zahlungsweg) throws RemoteException
  {
    setAttribute("zahlungsweg", zahlungsweg);
  }

  @Override
  public String getDtausTextschluessel() throws RemoteException
  {
    return (String) getAttribute("dtaustextschluessel");
  }

  @Override
  public void setDtausTextschluessel(String dtaustextschluessel)
      throws RemoteException
  {
    setAttribute("dtaustextschluessel", dtaustextschluessel);
  }

  @Override
  public String getDefaultLand() throws RemoteException
  {
    String dl = (String) getAttribute("defaultland");
    if (dl == null)
    {
      dl = "DE";
    }
    return dl;
  }

  @Override
  public void setDefaultLand(String defaultland) throws RemoteException
  {
    setAttribute("defaultland", defaultland);
  }

  @Override
  public String getAltersgruppen() throws RemoteException
  {
    return (String) getAttribute("altersgruppen");
  }

  @Override
  public void setAltersgruppen(String altersgruppen) throws RemoteException
  {
    setAttribute("altersgruppen", altersgruppen);
  }

  @Override
  public String getJubilaeen() throws RemoteException
  {
    String ag = (String) getAttribute("jubilaeen");
    if (ag == null || ag.length() == 0)
    {
      ag = "10,25,40,50";
    }
    return ag;
  }

  @Override
  public void setJubilaeen(String jubilaeen) throws RemoteException
  {
    setAttribute("jubilaeen", jubilaeen);
  }

  @Override
  public int getJubilarStartAlter() throws RemoteException
  {
    try
    {
      return (Integer) getAttribute("jubilarstartalter");
    }
    catch (NullPointerException e)
    {
      return 0;
    }
  }

  @Override
  public void setJubilarStartAlter(int alter) throws RemoteException
  {
    setAttribute("jubilarstartalter", alter);
  }

  @Override
  public String getAltersjubilaeen() throws RemoteException
  {
    String aj = (String) getAttribute("altersjubilaeen");
    if (aj == null || aj.length() == 0)
    {
      aj = "50,60,65,70,75,80,85,90,95,100";
    }
    return aj;
  }

  @Override
  public void setAltersjubilaeen(String altersjubilaeen) throws RemoteException
  {
    setAttribute("altersjubilaeen", altersjubilaeen);
  }

  @Override
  public int getDelaytime() throws RemoteException
  {
    try
    {
      return (Integer) getAttribute("delaytime");
    }
    catch (NullPointerException e)
    {
      return 1000;
    }
  }

  @Override
  public void setDelaytime(int delaytime) throws RemoteException
  {
    setAttribute("delaytime", delaytime);
  }

  @Override
  public boolean hasZusatzfelder() throws RemoteException
  {
    if (hasZus == null)
    {
      DBIterator it = Einstellungen.getDBService().createList(
          Felddefinition.class);
      hasZus = new Boolean(it.size() > 0);
    }
    return hasZus;
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }

  @Override
  public boolean getZeigeStammdatenInTab()
  {
    return settings.getBoolean("ZeigeStammdatenInTab", false);
  }

  @Override
  public void setZeigeStammdatenInTab(boolean showInTab)
  {
    settings.setAttribute("ZeigeStammdatenInTab", showInTab);
  }

  @Override
  public boolean getZeigeMitgliedschaftInTab()
  {
    return settings.getBoolean("ZeigeMitgliedschaftInTab", true);
  }

  @Override
  public void setZeigeMitgliedschaftInTab(boolean showInTab)
  {
    settings.setAttribute("ZeigeMitgliedschaftInTab", showInTab);

  }

  @Override
  public boolean getZeigeZahlungInTab()
  {
    return settings.getBoolean("ZeigeZahlungInTab", true);
  }

  @Override
  public void setZeigeZahlungInTab(boolean showInTab)
  {
    settings.setAttribute("ZeigeZahlungInTab", showInTab);
  }

  @Override
  public boolean getZeigeZusatzbetraegeInTab()
  {
    return settings.getBoolean("ZeigeZusatzbetrageInTab", true);
  }

  @Override
  public void setZeigeZusatzbetrageInTab(boolean showInTab)
  {
    settings.setAttribute("ZeigeZusatzbetrageInTab", showInTab);
  }

  @Override
  public boolean getZeigeMitgliedskontoInTab()
  {
    return settings.getBoolean("ZeigeMitgliedskontoInTab", true);
  }

  @Override
  public void setZeigeMitgliedskontoInTab(boolean showInTab)
  {
    settings.setAttribute("ZeigeMitgliedskontoInTab", showInTab);
  }

  @Override
  public boolean getZeigeVermerkeInTab()
  {
    return settings.getBoolean("ZeigeVermerkeInTab", true);
  }

  @Override
  public void setZeigeVermerkeInTab(boolean showInTab)
  {
    settings.setAttribute("ZeigeVermerkeInTab", showInTab);
  }

  @Override
  public boolean getZeigeWiedervorlageInTab()
  {
    return settings.getBoolean("ZeigeWiedervorlageInTab", true);
  }

  @Override
  public void setZeigeWiedervorlageInTab(boolean showInTab)
  {
    settings.setAttribute("ZeigeWiedervorlageInTab", showInTab);
  }

  @Override
  public boolean getZeigeEigenschaftenInTab()
  {
    return settings.getBoolean("ZeigeEigenschaftenInTab", true);
  }

  @Override
  public void setZeigeEigentschaftenInTab(boolean showInTab)
  {
    settings.setAttribute("ZeigeEigenschaftenInTab", showInTab);
  }

  @Override
  public boolean getZeigeMailsInTab()
  {
    return settings.getBoolean("ZeigeMailsInTab", true);
  }

  @Override
  public void setZeigeMailsInTab(boolean showInTab)
  {
    settings.setAttribute("ZeigeMailsInTab", showInTab);
  }

  @Override
  public boolean getZeigeZusatzfelderInTab()
  {
    return settings.getBoolean("ZeigeZusatzfelderInTab", true);
  }

  @Override
  public void setZeigeZusatzfelderInTab(boolean showInTab)
  {
    settings.setAttribute("ZeigeZusatzfelderInTab", showInTab);
  }

  @Override
  public boolean getZeigeLehrgaengeInTab()
  {
    return settings.getBoolean("ZeigeLehrgaengeInTab", true);
  }

  @Override
  public void setZeigeLehrgaengeInTab(boolean showInTab)
  {
    settings.setAttribute("ZeigeLehrgaengeInTab", showInTab);
  }

  @Override
  public boolean getZeigeFotoInTab()
  {
    return settings.getBoolean("ZeigeFotoInTab", true);
  }

  @Override
  public void setZeigeFotoInTab(boolean showInTab)
  {
    settings.setAttribute("ZeigeFotoInTab", showInTab);
  }

  @Override
  public boolean getZeigeLesefelderInTab()
  {
    return settings.getBoolean("ZeigeLesefelderInTab", true);
  }

  @Override
  public void setZeigeLesefelderInTab(boolean showInTab)
  {
    settings.setAttribute("ZeigeLesefelderInTab", showInTab);
  }

  @Override
  public boolean getZeigeArbeitseinsatzInTab()
  {
    return settings.getBoolean("ZeigeArbeitseinsatzInTab", true);
  }

  @Override
  public void setZeigeArbeitseinsatzInTab(boolean showInTab)
  {
    settings.setAttribute("ZeigeArbeitseinsatzInTab", showInTab);
  }

  @Override
  public int getAnzahlSpaltenStammdaten()
  {
    return settings.getInt("AnzahlSpaltenStammdaten", 2);
  }

  @Override
  public void setAnzahlSpaltenStammdaten(int anzahlSpalten)
  {
    settings.setAttribute("AnzahlSpaltenStammdaten", anzahlSpalten);
  }

  @Override
  public int getAnzahlSpaltenZusatzfelder()
  {
    return settings.getInt("AnzahlSpaltenZusatzfelder", 1);
  }

  @Override
  public void setAnzahlSpaltenZusatzfelder(int anzahlSpalten)
  {
    settings.setAttribute("AnzahlSpaltenZusatzfelder", anzahlSpalten);
  }

  @Override
  public int getAnzahlSpaltenLesefelder()
  {
    return settings.getInt("AnzahlSpaltenLesefelder", 1);
  }

  @Override
  public void setAnzahlSpaltenLesefelder(int anzahlSpalten)
  {
    settings.setAttribute("AnzahlSpaltenLesefelder", anzahlSpalten);
  }

  @Override
  public int getAnzahlSpaltenMitgliedschaft()
  {
    return settings.getInt("AnzahlSpaltenMitgliedschaft", 1);
  }

  @Override
  public void setAnzahlSpaltenMitgliedschaft(int anzahlSpalten)
  {
    settings.setAttribute("AnzahlSpaltenMitgliedschaft", anzahlSpalten);
  }

  @Override
  public int getAnzahlSpaltenZahlung()
  {
    return settings.getInt("AnzahlSpaltenZahlung", 1);
  }

  @Override
  public void setAnzahlSpaltenZahlung(int anzahlSpalten)
  {
    settings.setAttribute("AnzahlSpaltenZahlung", anzahlSpalten);
  }

}
