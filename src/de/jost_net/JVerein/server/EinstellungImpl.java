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

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.io.AltersgruppenParser;
import de.jost_net.JVerein.io.JubilaeenParser;
import de.jost_net.JVerein.keys.Altermodel;
import de.jost_net.JVerein.keys.Beitragsmodel;
import de.jost_net.JVerein.keys.BuchungBuchungsartAuswahl;
import de.jost_net.JVerein.rmi.Einstellung;
import de.jost_net.JVerein.rmi.Felddefinition;
import de.jost_net.OBanToo.SEPA.BIC;
import de.jost_net.OBanToo.SEPA.IBAN;
import de.jost_net.OBanToo.SEPA.SEPAException;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.hbci.HBCI;
import de.willuhn.jameica.hbci.rmi.Konto;
import de.willuhn.jameica.security.Wallet;
import de.willuhn.jameica.system.Application;
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
  private final Settings settings;

  /**
   * Verschlüsselte Datei für besonders sensible Daten (Passwörter)
   */
  private Wallet wallet = null;

  /**
   * Passwort zwischenspeichern, erst bei store() ins Wallet schreiben
   */
  private String imap_auth_pwd = null;

  private String smtp_auth_pwd = null;

  public EinstellungImpl() throws RemoteException
  {
    super();
    settings = new Settings(this.getClass());
    try
    {
      wallet = new Wallet(this.getClass());
    }
    catch (Exception e)
    {
      Logger.error("Erstellen des Wallet-Objekts fehlgeschlagen");
    }

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
    String name = (String) getAttribute("name");
    if (name == null)
    {
      name = "Vereinsname fehlt! Unter Administration | Einstellungen erfassen.";
    }
    return name;
  }

  @Override
  public void setName(String name) throws RemoteException
  {
    setAttribute("name", name);
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
  public Date getVeranlagungVon() throws RemoteException
  {
    Date d = (Date) getAttribute("veranlagungvon");
    if (d == null)
    {
      return new Date();
    }
    return d;
  }

  @Override
  public void setVeranlagungVon(Date veranlagungvon) throws RemoteException
  {
    setAttribute("veranlagungvon", veranlagungvon);
  }

  @Override
  public Date getVeranlagungBis() throws RemoteException
  {
    Date d = (Date) getAttribute("veranlagungbis");
    if (d == null)
    {
      return new Date();
    }
    return d;
  }

  @Override
  public void setVeranlagungBis(Date veranlagungbis) throws RemoteException
  {
    setAttribute("veranlagungbis", veranlagungbis);
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
  public Konto getHibiscusKonto() throws RemoteException
  {
    try
    {
      // DB-Service holen
      DBService service = (DBService) Application.getServiceFactory()
          .lookup(HBCI.class, "database");
      DBIterator<Konto> konten = service.createList(Konto.class);
      konten.addFilter("iban = ?", Einstellungen.getEinstellung().getIban());
      Logger.debug("Vereinskonto: " + Einstellungen.getEinstellung().getIban());
      if (konten.hasNext())
      {
        return (Konto) konten.next();
      }
      else
      {
        throw new RemoteException("Vereinskonto nicht in Hibiscus gefunden");
      }
    }
    catch (Exception e)
    {
      throw new RemoteException(e.getMessage());
    }
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
  public Boolean getSekundaereBeitragsgruppen() throws RemoteException
  {
    return Util.getBoolean(getAttribute("sekundaerebeitragsgruppen"));
  }

  @Override
  public void setSekundaereBeitragsgruppen(Boolean sekundaerebeitragsgruppen)
      throws RemoteException
  {
    setAttribute("sekundaerebeitragsgruppen",
        Boolean.valueOf(sekundaerebeitragsgruppen));
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
  public Boolean getKursteilnehmerGebGesPflicht() throws RemoteException
  {
    return Util.getBoolean(getAttribute("kursteilnehmergebgespflicht"));
  }

  @Override
  public void setKursteilnehmerGebGesPflicht(
      Boolean kursteilnehmergebgespflicht) throws RemoteException
  {
    setAttribute("kursteilnehmergebgespflicht",
        Boolean.valueOf(kursteilnehmergebgespflicht));
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

  // TODO für Versionsbau deaktivert.
  // @Override
  // public Boolean getInventar() throws RemoteException
  // {
  // return Util.getBoolean(getAttribute("inventar"));
  // }

  // @Override
  // public void setInventar(Boolean inventar) throws RemoteException
  // {
  // setAttribute("inventar", Boolean.valueOf(inventar));
  // }

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
      text = "Der Betrag wird vom Konto ${IBAN}, (BIC ${BIC}) abgebucht.";
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
  public Beitragsmodel getBeitragsmodel() throws RemoteException
  {
    return Beitragsmodel.getByKey((Integer) getAttribute("beitragsmodel"));
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
  public String getVorlagenCsvVerzeichnis() throws RemoteException
  {
    String tmpdir = (String) getAttribute("vorlagencsvverzeichnis");
    if (tmpdir == null)
    {
      // das Verzeichnis ist optional
      tmpdir = "";
    }
    return tmpdir;
  }

  @Override
  public void setVorlagenCsvVerzeichnis(String vorlagencsvdir)
      throws RemoteException
  {
    setAttribute("vorlagencsvverzeichnis", vorlagencsvdir);
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
    return Util
        .getBoolean(getAttribute("spendenbescheinigungprintbuchungsart"));
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
    return (String) getAttribute("smtp_port");
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
    if (this.smtp_auth_pwd == null)
    {
      try
      {
        Serializable pwd = wallet.get("smtp_auth_pwd");
        if (pwd != null && pwd instanceof String)
        {
          this.smtp_auth_pwd = (String) pwd;
        }
      }
      catch (Exception e)
      {
        Logger.error("Fehler beim Auslesen des SMTP-Passworts aus dem Wallet",
            e);
      }
    }
    return this.smtp_auth_pwd;
  }

  @Override
  public void setSmtpAuthPwd(String smtp_auth_pwd) throws RemoteException
  {
    this.smtp_auth_pwd = smtp_auth_pwd;
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

  /**
   * Pause zwischen dem Versand von Mails in Sekunden
   */
  @Override
  public int getMailVerzoegerung() throws RemoteException
  {
    try
    {
      return (Integer) getAttribute("mailverzoegerung");
    }
    catch (NullPointerException e)
    {
      return 5;
    }
  }

  @Override
  public void setMailVerzoegerung(int verzoegerung) throws RemoteException
  {
    setAttribute("mailverzoegerung", verzoegerung);
  }

  @Override
  public String getMailAlwaysBcc() throws RemoteException
  {
    return (String) getAttribute("mail_always_bcc");
  }

  @Override
  public void setMailAlwaysBcc(String mail_always_bcc) throws RemoteException
  {
    setAttribute("mail_always_bcc", mail_always_bcc);
  }

  @Override
  public String getMailAlwaysCc() throws RemoteException
  {
    return (String) getAttribute("mail_always_cc");
  }

  @Override
  public void setMailAlwaysCc(String mail_always_cc) throws RemoteException
  {
    setAttribute("mail_always_cc", mail_always_cc);
  }

  @Override
  public Boolean getCopyToImapFolder() throws RemoteException
  {
    return Util.getBoolean(getAttribute("copy_to_imap_folder"));
  }

  @Override
  public void setCopyToImapFolder(Boolean copy_to_imap_folder)
      throws RemoteException
  {
    setAttribute("copy_to_imap_folder", copy_to_imap_folder);
  }

  @Override
  public String getImapAuthUser() throws RemoteException
  {
    return (String) getAttribute("imap_auth_user");
  }

  @Override
  public void setImapAuthUser(String imap_auth_user) throws RemoteException
  {
    this.imap_auth_pwd = imap_auth_user;
    setAttribute("imap_auth_user", imap_auth_user);

  }

  @Override
  public String getImapAuthPwd() throws RemoteException
  {
    if (imap_auth_pwd == null)
    {
      try
      {
        Serializable pwd = wallet.get("imap_auth_pwd");
        if (pwd != null && pwd instanceof String)
        {
          this.imap_auth_pwd = (String) pwd;
        }
      }
      catch (Exception e)
      {
        Logger.error("Fehler beim Auslesen des IMAP-Passworts aus dem Wallet",
            e);
      }
    }

    return imap_auth_pwd;
  }

  @Override
  public void setImapAuthPwd(String imap_auth_pwd) throws RemoteException
  {
    this.imap_auth_pwd = imap_auth_pwd;
  }

  @Override
  public String getImapHost() throws RemoteException
  {
    return (String) getAttribute("imap_host");
  }

  @Override
  public void setImapHost(String imap_host) throws RemoteException
  {
    setAttribute("imap_host", imap_host);
  }

  @Override
  public String getImapPort() throws RemoteException
  {
    return (String) getAttribute("imap_port");
  }

  @Override
  public void setImapPort(String imap_port) throws RemoteException
  {
    setAttribute("imap_port", imap_port);
  }

  @Override
  public Boolean getImapSsl() throws RemoteException
  {
    return Util.getBoolean(getAttribute("imap_ssl"));
  }

  @Override
  public void setImapSsl(Boolean imap_ssl) throws RemoteException
  {
    setAttribute("imap_ssl", imap_ssl);
  }

  @Override
  public Boolean getImapStartTls() throws RemoteException
  {
    return Util.getBoolean(getAttribute("imap_starttls"));
  }

  @Override
  public void setImapStartTls(Boolean imap_starttls) throws RemoteException
  {
    setAttribute("imap_starttls", imap_starttls);
  }

  @Override
  public String getImapSentFolder() throws RemoteException
  {
    String folder = (String) getAttribute("imap_sent_folder");
    if (folder == null || folder.isEmpty())
    {
      folder = "Sent";
    }
    return folder;
  }

  @Override
  public void setImapSentFolder(String imap_sent_folder) throws RemoteException
  {
    setAttribute("imap_sent_folder", imap_sent_folder);
  }

  @Override
  public String getMailSignatur(Boolean separator) throws RemoteException
  {
    String signatur = (String) getAttribute("mailsignatur");
    if (signatur == null || signatur.trim().length() == 0)
    {
      return "";
    }
    // Siehe RFC 3676, 4.3. Usenet Signature Convention
    if (separator && !signatur.startsWith("-- \n"))
    {
      return "-- \n" + signatur;
    }
    return signatur;
  }

  @Override
  public void setMailSignatur(String mailsignatur) throws RemoteException
  {
    setAttribute("mailsignatur", mailsignatur);
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
    String ag = (String) getAttribute("altersgruppen");
    if (ag == null || ag.length() == 0)
    {
      ag = "1-5,6-10,11-17,18-25,26-50,50-100";
    }
    return ag;
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
  public boolean hasZusatzfelder() throws RemoteException
  {
    if (hasZus == null)
    {
      DBIterator<Felddefinition> it = Einstellungen.getDBService()
          .createList(Felddefinition.class);
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

  @Override
  public int getArbeitsstundenmodel() throws RemoteException
  {
    return (Integer) getAttribute(COL_ARBEITS_MODEL);
  }

  @Override
  public void setArbeitsstundenmodel(int arbeitsstundenmodel)
      throws RemoteException
  {
    setAttribute(COL_ARBEITS_MODEL, arbeitsstundenmodel);
  }

  @Override
  public int getAltersModel() throws RemoteException
  {
    Integer wert = (Integer) getAttribute(COL_ALTER_MODEL);
    if (null == wert)
      return Altermodel.AKTUELLES_DATUM;
    return wert.intValue();
  }

  @Override
  public void setAltersModel(int altersmodel) throws RemoteException
  {
    setAttribute(COL_ALTER_MODEL, altersmodel);
  }

  @Override
  public boolean getZusatzbetragAusgetretene() throws RemoteException
  {
    return Util.getBoolean(getAttribute("zusatzbetragausgetretene"));
  }

  @Override
  public void setZusatzbetragAusgetretene(boolean zusatzbetragausgetretene)
      throws RemoteException
  {
    setAttribute("zusatzbetragausgetretene",
        Boolean.valueOf(zusatzbetragausgetretene));
  }

  @Override
  public int getSepaMandatIdSource() throws RemoteException
  {
    return (Integer) getAttribute(COL_SEPA_MANDANTID_SOURCE);
  }

  @Override
  public void setSepaMandatIdSource(int sepaMandatIdSource)
      throws RemoteException
  {
    setAttribute(COL_SEPA_MANDANTID_SOURCE, sepaMandatIdSource);
  }

  @Override
  public Boolean getAutoBuchunguebernahme() throws RemoteException
  {
    return Util.getBoolean(getAttribute("autobuchunguebernahme"));
  }

  @Override
  public void setAutoBuchunguebernahme(Boolean autobuchunguebernahme)
      throws RemoteException
  {
    setAttribute("autobuchunguebernahme", autobuchunguebernahme);
  }

  @Override
  public Boolean getUnterdrueckungOhneBuchung() throws RemoteException
  {
    return Util.getBoolean(getAttribute("unterdrueckungohnebuchung"));
  }

  @Override
  public void setUnterdrueckungOhneBuchung(Boolean unterdrueckungohnebuchung)
      throws RemoteException
  {
    setAttribute("unterdrueckungohnebuchung", unterdrueckungohnebuchung);
  }

  @Override
  public Integer getSEPADatumOffset() throws RemoteException
  {
    Integer offset = (Integer) getAttribute("sepadatumoffset");
    if (offset == null)
    {
      offset = new Integer(0);
    }
    return offset;
  }

  @Override
  public void setSEPADatumOffset(Integer sepadatumoffset) throws RemoteException
  {
    setAttribute("sepadatumoffset", sepadatumoffset);
  }

  @Override
  public int getBuchungBuchungsartAuswahl() throws RemoteException
  {
    Integer wert = (Integer) getAttribute(COL_BUCHUNG_BUCHUNGSART_AUSWAHL);
    if (null == wert)
      return BuchungBuchungsartAuswahl.SearchInput;
    return wert.intValue();
  }

  @Override
  public void setBuchungBuchungsartAuswahl(int auswahl) throws RemoteException
  {
    setAttribute(COL_BUCHUNG_BUCHUNGSART_AUSWAHL, auswahl);
  }

  @Override
  public int getBuchungsartSort() throws RemoteException
  {
    Integer wert = (Integer) getAttribute("buchungsartsort");
    if (wert == null)
    {
      return 1;
    }
    return wert.intValue();
  }

  @Override
  public void setBuchungsartSort(int buchungsartsort) throws RemoteException
  {
    setAttribute("buchungsartsort", buchungsartsort);
  }

  @Override
  public Boolean getAbrlAbschliessen() throws RemoteException
  {
    return Util.getBoolean(getAttribute("abrlabschliessen"));
  }

  @Override
  public void setAbrlAbschliessen(Boolean abschliessen) throws RemoteException
  {
    setAttribute("abrlabschliessen", Boolean.valueOf(abschliessen));
  }

}
