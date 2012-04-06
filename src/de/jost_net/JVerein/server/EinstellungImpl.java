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
import de.willuhn.jameica.system.Application;
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

  public EinstellungImpl() throws RemoteException
  {
    super();
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
    throw new ApplicationException(JVereinPlugin.getI18n().tr(
        "Einstellung darf nicht gelöscht werden"));
  }

  @Override
  protected void insertCheck() throws ApplicationException
  {
    try
    {
      if (getName() == null || getName().length() == 0)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Bitte Namen eingeben"));
      }
      if (getBlz() == null || getBlz().length() == 0)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Bitte Bankleitzahl eingeben"));
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
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Bitte Kontonummer eingeben"));
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
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Ungültige BLZ/Kontonummer. Bitte prüfen Sie Ihre Eingaben."));
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
              "Plugin jameica.messaging ist nicht installiert oder im LAN verfügbar!");
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
      String fehler = JVereinPlugin.getI18n().tr(
          "Einstellung kann nicht gespeichert werden. Siehe system log");
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

  public void setID() throws RemoteException
  {
    setAttribute("id", "1");
  }

  public String getName() throws RemoteException
  {
    return (String) getAttribute("name");
  }

  public void setName(String name) throws RemoteException
  {
    setAttribute("name", name);
  }

  public String getNameLang() throws RemoteException
  {
    return (String) getAttribute("namelang");
  }

  public void setNameLang(String name) throws RemoteException
  {
    setAttribute("namelang", name);
  }

  public String getStrasse() throws RemoteException
  {
    return (String) getAttribute("strasse");
  }

  public void setStrasse(String strasse) throws RemoteException
  {
    setAttribute("strasse", strasse);
  }

  public String getPlz() throws RemoteException
  {
    return (String) getAttribute("plz");
  }

  public void setPlz(String plz) throws RemoteException
  {
    setAttribute("plz", plz);
  }

  public String getOrt() throws RemoteException
  {
    return (String) getAttribute("ort");
  }

  public void setOrt(String ort) throws RemoteException
  {
    setAttribute("ort", ort);
  }

  public String getFinanzamt() throws RemoteException
  {
    return (String) getAttribute("finanzamt");
  }

  public void setFinanzamt(String finanzamt) throws RemoteException
  {
    setAttribute("finanzamt", finanzamt);
  }

  public String getSteuernummer() throws RemoteException
  {
    return (String) getAttribute("steuernummer");
  }

  public void setSteuernummer(String steuernummer) throws RemoteException
  {
    setAttribute("steuernummer", steuernummer);
  }

  public Date getBescheiddatum() throws RemoteException
  {
    Date d = (Date) getAttribute("bescheiddatum");
    if (d == null)
    {
      return new Date();
    }
    return d;
  }

  public void setBescheiddatum(Date bescheiddatum) throws RemoteException
  {
    setAttribute("bescheiddatum", bescheiddatum);
  }

  public Boolean getVorlaeufig() throws RemoteException
  {
    return Util.getBoolean(getAttribute("vorlaeufig"));
  }

  public void setVorlaeufig(Boolean vorlaeufig) throws RemoteException
  {
    setAttribute("vorlaeufig", Boolean.valueOf(vorlaeufig));
  }

  public Date getVorlaeufigab() throws RemoteException
  {
    Date d = (Date) getAttribute("vorlaeufigab");
    if (d == null)
    {
      return new Date();
    }
    return d;
  }

  public void setVorlaeufigab(Date vorlaeufigab) throws RemoteException
  {
    setAttribute("vorlaeufigab", vorlaeufigab);
  }

  public String getBeguenstigterzweck() throws RemoteException
  {
    return (String) getAttribute("beguenstigterzweck");
  }

  public void setBeguenstigterzweck(String beguenstigterzweck)
      throws RemoteException
  {
    setAttribute("beguenstigterzweck", beguenstigterzweck);
  }

  public Boolean getMitgliedsbetraege() throws RemoteException
  {
    return Util.getBoolean(getAttribute("mitgliedsbeitraege"));
  }

  public void setMitgliedsbeitraege(Boolean mitgliedsbeitraege)
      throws RemoteException
  {
    setAttribute("mitgliedsbeitraege", Boolean.valueOf(mitgliedsbeitraege));
  }

  public String getBlz() throws RemoteException
  {
    return (String) getAttribute("blz");
  }

  public void setBlz(String blz) throws RemoteException
  {
    setAttribute("blz", blz);
  }

  public String getKonto() throws RemoteException
  {
    return (String) getAttribute("konto");
  }

  public void setKonto(String konto) throws RemoteException
  {
    setAttribute("konto", konto);
  }

  public Boolean getGeburtsdatumPflicht() throws RemoteException
  {
    return Util.getBoolean(getAttribute("geburtsdatumpflicht"));
  }

  public void setGeburtsdatumPflicht(Boolean geburtsdatumpflicht)
      throws RemoteException
  {
    setAttribute("geburtsdatumpflicht", Boolean.valueOf(geburtsdatumpflicht));
  }

  public Boolean getEintrittsdatumPflicht() throws RemoteException
  {
    return Util.getBoolean(getAttribute("eintrittsdatumpflicht"));
  }

  public void setEintrittsdatumPflicht(Boolean eintrittsdatumpflicht)
      throws RemoteException
  {
    setAttribute("eintrittsdatumpflicht",
        Boolean.valueOf(eintrittsdatumpflicht));
  }

  public Boolean getSterbedatum() throws RemoteException
  {
    return Util.getBoolean(getAttribute("sterbedatum"));
  }

  public void setSterbedatum(Boolean sterbedatum) throws RemoteException
  {
    setAttribute("sterbedatum", Boolean.valueOf(sterbedatum));
  }

  public Boolean getKommunikationsdaten() throws RemoteException
  {
    return Util.getBoolean(getAttribute("kommunikationsdaten"));
  }

  public void setKommunikationsdaten(Boolean kommunikationsdaten)
      throws RemoteException
  {
    setAttribute("kommunikationsdaten", Boolean.valueOf(kommunikationsdaten));
  }

  public Boolean getZusatzbetrag() throws RemoteException
  {
    return Util.getBoolean(getAttribute("zusatzabbuchung"));
  }

  public void setZusatzbetrag(Boolean zusatzabbuchung) throws RemoteException
  {
    setAttribute("zusatzabbuchung", Boolean.valueOf(zusatzabbuchung));
  }

  public Boolean getVermerke() throws RemoteException
  {
    return Util.getBoolean(getAttribute("vermerke"));
  }

  public void setVermerke(Boolean vermerke) throws RemoteException
  {
    setAttribute("vermerke", Boolean.valueOf(vermerke));
  }

  public Boolean getWiedervorlage() throws RemoteException
  {
    return Util.getBoolean(getAttribute("wiedervorlage"));
  }

  public void setWiedervorlage(Boolean wiedervorlage) throws RemoteException
  {
    setAttribute("wiedervorlage", Boolean.valueOf(wiedervorlage));
  }

  public Boolean getKursteilnehmer() throws RemoteException
  {
    return Util.getBoolean(getAttribute("kursteilnehmer"));
  }

  public void setKursteilnehmer(Boolean kursteilnehmer) throws RemoteException
  {
    setAttribute("kursteilnehmer", Boolean.valueOf(kursteilnehmer));
  }

  public Boolean getLehrgaenge() throws RemoteException
  {
    return Util.getBoolean(getAttribute("lehrgaenge"));
  }

  public void setLehrgaenge(Boolean lehrgaenge) throws RemoteException
  {
    setAttribute("lehrgaenge", Boolean.valueOf(lehrgaenge));
  }

  public Boolean getJuristischePersonen() throws RemoteException
  {
    return Util.getBoolean(getAttribute("juristischepersonen"));
  }

  public void setJuristischePersonen(Boolean juristischepersonen)
      throws RemoteException
  {
    setAttribute("juristischepersonen", Boolean.valueOf(juristischepersonen));
  }

  public Boolean getMitgliedskonto() throws RemoteException
  {
    return Util.getBoolean(getAttribute("mitgliedskonto"));
  }

  public void setMitgliedskonto(Boolean mitgliedskonto) throws RemoteException
  {
    setAttribute("mitgliedskonto", Boolean.valueOf(mitgliedskonto));
  }

  public Boolean getMitgliedfoto() throws RemoteException
  {
    return Util.getBoolean(getAttribute("mitgliedfoto"));
  }

  public void setMitgliedfoto(Boolean mitgliedfoto) throws RemoteException
  {
    setAttribute("mitgliedfoto", Boolean.valueOf(mitgliedfoto));
  }

  public Boolean getZusatzadressen() throws RemoteException
  {
    return Util.getBoolean(getAttribute("zusatzadressen"));
  }

  public void setZusatzadressen(Boolean zusatzadressen) throws RemoteException
  {
    setAttribute("zusatzadressen", Boolean.valueOf(zusatzadressen));
  }

  public Boolean getAuslandsadressen() throws RemoteException
  {
    return Util.getBoolean(getAttribute("auslandsadressen"));
  }

  public void setAuslandsadressen(Boolean auslandsadressen)
      throws RemoteException
  {
    setAttribute("auslandsadressen", Boolean.valueOf(auslandsadressen));
  }

  public Boolean getArbeitseinsatz() throws RemoteException
  {
    return Util.getBoolean(getAttribute("arbeitseinsatz"));
  }

  public void setArbeitseinsatz(Boolean arbeitseinsatz) throws RemoteException
  {
    setAttribute("arbeitseinsatz", Boolean.valueOf(arbeitseinsatz));
  }

  public Boolean getDokumentenspeicherung() throws RemoteException
  {
    return Util.getBoolean(getAttribute("dokumentenspeicherung"));
  }

  public void setDokumentenspeicherung(Boolean dokumentenspeicherung)
      throws RemoteException
  {
    setAttribute("dokumentenspeicherung",
        Boolean.valueOf(dokumentenspeicherung));
  }

  public Boolean getIndividuelleBeitraege() throws RemoteException
  {
    return Util.getBoolean(getAttribute("individuellebeitraege"));
  }

  public void setIndividuelleBeitraege(Boolean individuellebeitraege)
      throws RemoteException
  {
    setAttribute("individuellebeitraege",
        Boolean.valueOf(individuellebeitraege));
  }

  public String getRechnungTextAbbuchung() throws RemoteException
  {
    String text = (String) getAttribute("rechnungtextabbuchung");
    if (text == null)
    {
      text = "Der Betrag wird vom Konto ${Konto} (BLZ ${BLZ}) abgebucht.";
    }
    return text;
  }

  public void setRechnungTextAbbuchung(String rechnungtextabbuchung)
      throws RemoteException
  {
    setAttribute("rechnungtextabbuchung", rechnungtextabbuchung);
  }

  public String getRechnungTextUeberweisung() throws RemoteException
  {
    String text = (String) getAttribute("rechnungtextueberweisung");
    if (text == null)
    {
      text = "Bitte überweisen Sie den Betrag auf das angegebene Konto.";
    }
    return text;
  }

  public void setRechnungTextUeberweisung(String rechnungtextueberweisung)
      throws RemoteException
  {
    setAttribute("rechnungtextueberweisung", rechnungtextueberweisung);
  }

  public String getRechnungTextBar() throws RemoteException
  {
    String text = (String) getAttribute("rechnungtextbar");
    if (text == null)
    {
      text = "Bitte zahlen Sie den Betrag auf das angegebene Konto ein.";
    }
    return text;
  }

  public void setRechnungTextBar(String rechnungtextbar) throws RemoteException
  {
    setAttribute("rechnungtextbar", rechnungtextbar);
  }

  public Boolean getExterneMitgliedsnummer() throws RemoteException
  {
    return Util.getBoolean(getAttribute("externemitgliedsnummer"));
  }

  public void setExterneMitgliedsnummer(Boolean externemitgliedsnummer)
      throws RemoteException
  {
    setAttribute("externemitgliedsnummer",
        Boolean.valueOf(externemitgliedsnummer));
  }

  public int getBeitragsmodel() throws RemoteException
  {
    return (Integer) getAttribute("beitragsmodel");
  }

  public void setBeitragsmodel(int beitragsmodel) throws RemoteException
  {
    setAttribute("beitragsmodel", beitragsmodel);
  }

  public String getDateinamenmuster() throws RemoteException
  {
    String muster = (String) getAttribute("dateinamenmuster");
    if (muster == null)
    {
      muster = "a$s$-d$-z$";
    }
    return muster;
  }

  public void setDateinamenmuster(String dateinamenmuster)
      throws RemoteException
  {
    setAttribute("dateinamenmuster", dateinamenmuster);
  }

  public String getDateinamenmusterSpende() throws RemoteException
  {
    String muster = (String) getAttribute("dateinamenmusterspende");
    if (muster == null)
    {
      muster = "a$-d$-n$-v$";
    }
    return muster;
  }

  public void setDateinamenmusterSpende(String dateinamenmusterspende)
      throws RemoteException
  {
    setAttribute("dateinamenmusterspende", dateinamenmusterspende);
  }

  public double getSpendenbescheinigungminbetrag() throws RemoteException
  {
    Double d = (Double) getAttribute("spendenbescheinigungminbetrag");
    if (d == null)
    {
      d = new Double(0);
    }
    return (d);
  }

  public void setSpendenbescheinigungminbetrag(double minbetrag)
      throws RemoteException
  {
    setAttribute("spendenbescheinigungminbetrag", minbetrag);
  }

  public String getSpendenbescheinigungverzeichnis() throws RemoteException
  {
    return (String) getAttribute("spendenbescheinigungverzeichnis");
  }

  public void setSpendenbescheinigungverzeichnis(
      String spendenbescheinigungverzeichnis) throws RemoteException
  {
    setAttribute("spendenbescheinigungverzeichnis",
        spendenbescheinigungverzeichnis);
  }

  public boolean getSpendenbescheinigungPrintBuchungsart()
      throws RemoteException
  {
    return Util
        .getBoolean(getAttribute("spendenbescheinigungprintbuchungsart"));
  }

  public void setSpendenbescheinigungPrintBuchungsart(Boolean printbuchungsart)
      throws RemoteException
  {
    setAttribute("spendenbescheinigungprintbuchungsart",
        Boolean.valueOf(printbuchungsart));
  }

  public String getBeginnGeschaeftsjahr() throws RemoteException
  {
    return (String) getAttribute("beginngeschaeftsjahr");
  }

  public void setBeginnGeschaeftsjahr(String beginngeschaeftsjahr)
      throws RemoteException
  {
    setAttribute("beginngeschaeftsjahr", beginngeschaeftsjahr);
  }

  public String getMitgliedskontoIstzahlung() throws RemoteException
  {
    return (String) getAttribute("mitgliedskontoistzahlung");
  }

  public void setMitgliedskontoIstzahlung(String mitgliedskontoistzahlung)
      throws RemoteException
  {
    setAttribute("mitgliedskontoistzahlung", mitgliedskontoistzahlung);
  }

  public String getSmtpServer() throws RemoteException
  {
    return (String) getAttribute("smtp_server");
  }

  public void setSmtpServer(String smtp_server) throws RemoteException
  {
    setAttribute("smtp_server", smtp_server);
  }

  public String getSmtpPort() throws RemoteException
  {
    String ret = (String) getAttribute("smtp_port");
    if (ret == null)
    {
      ret = "25";
    }
    return ret;
  }

  public void setSmtpPort(String smtp_port) throws RemoteException
  {
    setAttribute("smtp_port", smtp_port);
  }

  public String getSmtpAuthUser() throws RemoteException
  {
    return (String) getAttribute("smtp_auth_user");
  }

  public void setSmtpAuthUser(String smtp_auth_user) throws RemoteException
  {
    setAttribute("smtp_auth_user", smtp_auth_user);
  }

  public String getSmtpAuthPwd() throws RemoteException
  {
    return (String) getAttribute("smtp_auth_pwd");
  }

  public void setSmtpAuthPwd(String smtp_auth_pwd) throws RemoteException
  {
    setAttribute("smtp_auth_pwd", smtp_auth_pwd);
  }

  public String getSmtpFromAddress() throws RemoteException
  {
    return (String) getAttribute("smtp_from_address");
  }

  public void setSmtpFromAddress(String smtp_from_address)
      throws RemoteException
  {
    setAttribute("smtp_from_address", smtp_from_address);
  }

  public Boolean getSmtpSsl() throws RemoteException
  {
    return Util.getBoolean(getAttribute("smtp_ssl"));
  }

  public void setSmtpSsl(Boolean smtp_ssl) throws RemoteException
  {
    setAttribute("smtp_ssl", smtp_ssl);
  }

  public Boolean getSmtpStarttls() throws RemoteException
  {
    return Util.getBoolean(getAttribute("smtp_starttls"));
  }

  public void setSmtpStarttls(Boolean smtp_starttls) throws RemoteException
  {
    setAttribute("smtp_starttls", smtp_starttls);
  }

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

  public void setZahlungsrhytmus(int zahlungsrhytmus) throws RemoteException
  {
    setAttribute("zahlungsrhytmus", zahlungsrhytmus);
  }

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

  public void setZahlungsweg(int zahlungsweg) throws RemoteException
  {
    setAttribute("zahlungsweg", zahlungsweg);
  }

  public String getDtausTextschluessel() throws RemoteException
  {
    return (String) getAttribute("dtaustextschluessel");
  }

  public void setDtausTextschluessel(String dtaustextschluessel)
      throws RemoteException
  {
    setAttribute("dtaustextschluessel", dtaustextschluessel);
  }

  public String getAltersgruppen() throws RemoteException
  {
    return (String) getAttribute("altersgruppen");
  }

  public void setAltersgruppen(String altersgruppen) throws RemoteException
  {
    setAttribute("altersgruppen", altersgruppen);
  }

  public String getJubilaeen() throws RemoteException
  {
    String ag = (String) getAttribute("jubilaeen");
    if (ag == null || ag.length() == 0)
    {
      ag = "10,25,40,50";
    }
    return ag;
  }

  public void setJubilaeen(String jubilaeen) throws RemoteException
  {
    setAttribute("jubilaeen", jubilaeen);
  }

  public String getAltersjubilaeen() throws RemoteException
  {
    String aj = (String) getAttribute("altersjubilaeen");
    if (aj == null || aj.length() == 0)
    {
      aj = "50,60,65,70,75,80,85,90,95,100";
    }
    return aj;
  }

  public void setAltersjubilaeen(String altersjubilaeen) throws RemoteException
  {
    setAttribute("altersjubilaeen", altersjubilaeen);
  }

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

  public void setDelaytime(int delaytime) throws RemoteException
  {
    setAttribute("delaytime", delaytime);
  }

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

}
