/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * heiner@jverein.de
 * www.jverein.de
 * $Log$
 * Revision 1.12  2010/01/21 21:37:47  jost
 * Vermeidung NPE
 *
 * Revision 1.11  2010/01/01 22:36:19  jost
 * Standardwerte für Zahlungsweg und Zahlungsrhytmus können vorgegeben werden.
 *
 * Revision 1.10  2009/11/19 21:11:16  jost
 * Update-Option entfernt.
 *
 * Revision 1.9  2009/10/17 19:48:01  jost
 * Vorbereitung Mailversand.
 *
 * Revision 1.8  2009/09/13 19:26:44  jost
 * Vermeidung NPE
 *
 * Revision 1.7  2009/09/13 19:20:40  jost
 * Neu: Prüfung auf Updates
 *
 * Revision 1.6  2009/07/14 07:30:30  jost
 * Bugfix Rechnungen.
 *
 * Revision 1.5  2009/04/25 05:32:03  jost
 * Neu: Juristische Personen  können als Mitglied gespeichert werden.
 *
 * Revision 1.4  2009/04/13 11:41:02  jost
 * Neu: Lehrgänge
 *
 * Revision 1.3  2008/12/27 15:19:27  jost
 * Bugfix Booleans aus MySQL-DB lesen.
 *
 * Revision 1.2  2008/12/22 21:22:10  jost
 * Zusatzabbuchung->Zusatzbetrag
 *
 * Revision 1.1  2008/11/16 16:59:11  jost
 * Speicherung der Einstellung von Property-Datei in die Datenbank verschoben.
 *
 *
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;
import java.util.Date;

import de.jost_net.JVerein.rmi.Einstellung;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.util.ApplicationException;

public class EinstellungImpl extends AbstractDBObject implements Einstellung
{

  private static final long serialVersionUID = 3513343626868776722L;

  public EinstellungImpl() throws RemoteException
  {
    super();
  }

  protected String getTableName()
  {
    return "einstellung";
  }

  public String getPrimaryAttribute() throws RemoteException
  {
    return "id";
  }

  protected void deleteCheck() throws ApplicationException
  {
  }

  protected void insertCheck() throws ApplicationException
  {
  }

  protected void updateCheck() throws ApplicationException
  {
    insertCheck();
  }

  @SuppressWarnings("unchecked")
  protected Class getForeignObject(String arg0) throws RemoteException
  {
    return null;
  }

  public void setID() throws RemoteException
  {
    setAttribute("id", "1");
  }

  public boolean getGeburtsdatumPflicht() throws RemoteException
  {
    return Util.getBoolean(getAttribute("geburtsdatumpflicht"));
  }

  public void setGeburtsdatumPflicht(Boolean geburtsdatumpflicht)
      throws RemoteException
  {
    setAttribute("geburtsdatumpflicht", new Boolean(geburtsdatumpflicht));
  }

  public boolean getEintrittsdatumPflicht() throws RemoteException
  {
    return Util.getBoolean(getAttribute("eintrittsdatumpflicht"));
  }

  public void setEintrittsdatumPflicht(Boolean eintrittsdatumpflicht)
      throws RemoteException
  {
    setAttribute("eintrittsdatumpflicht", new Boolean(eintrittsdatumpflicht));
  }

  public boolean getKommunikationsdaten() throws RemoteException
  {
    return Util.getBoolean(getAttribute("kommunikationsdaten"));
  }

  public void setKommunikationsdaten(Boolean kommunikationsdaten)
      throws RemoteException
  {
    setAttribute("kommunikationsdaten", new Boolean(kommunikationsdaten));
  }

  public boolean getZusatzbetrag() throws RemoteException
  {
    return Util.getBoolean(getAttribute("zusatzabbuchung"));
  }

  public void setZusatzbetrag(Boolean zusatzabbuchung) throws RemoteException
  {
    setAttribute("zusatzabbuchung", new Boolean(zusatzabbuchung));
  }

  public boolean getVermerke() throws RemoteException
  {
    return Util.getBoolean(getAttribute("vermerke"));
  }

  public void setVermerke(Boolean vermerke) throws RemoteException
  {
    setAttribute("vermerke", new Boolean(vermerke));
  }

  public boolean getWiedervorlage() throws RemoteException
  {
    return Util.getBoolean(getAttribute("wiedervorlage"));
  }

  public void setWiedervorlage(Boolean wiedervorlage) throws RemoteException
  {
    setAttribute("wiedervorlage", new Boolean(wiedervorlage));
  }

  public boolean getKursteilnehmer() throws RemoteException
  {
    return Util.getBoolean(getAttribute("kursteilnehmer"));
  }

  public void setKursteilnehmer(Boolean kursteilnehmer) throws RemoteException
  {
    setAttribute("kursteilnehmer", new Boolean(kursteilnehmer));
  }

  public boolean getLehrgaenge() throws RemoteException
  {
    return Util.getBoolean(getAttribute("lehrgaenge"));
  }

  public void setLehrgaenge(Boolean lehrgaenge) throws RemoteException
  {
    setAttribute("lehrgaenge", new Boolean(lehrgaenge));
  }

  public boolean getJuristischePersonen() throws RemoteException
  {
    return Util.getBoolean(getAttribute("juristischepersonen"));
  }

  public void setJuristischePersonen(Boolean juristischepersonen)
      throws RemoteException
  {
    setAttribute("juristischepersonen", new Boolean(juristischepersonen));
  }

  public boolean getMitgliedskonto() throws RemoteException
  {
    return Util.getBoolean(getAttribute("mitgliedskonto"));
  }

  public void setMitgliedskonto(Boolean mitgliedskonto) throws RemoteException
  {
    setAttribute("mitgliedskonto", new Boolean(mitgliedskonto));
  }

  public boolean getExterneMitgliedsnummer() throws RemoteException
  {
    return Util.getBoolean(getAttribute("externemitgliedsnummer"));
  }

  public void setExterneMitgliedsnummer(Boolean externemitgliedsnummer)
      throws RemoteException
  {
    setAttribute("externemitgliedsnummer", new Boolean(externemitgliedsnummer));
  }

  public Integer getAktuelleGeburtstageVorher() throws RemoteException
  {
    return (Integer) getAttribute("aktuellegeburtstagevorher");
  }

  public void setAktuelleGeburtstageVorher(Integer vorher)
      throws RemoteException
  {
    setAttribute("aktuellegeburtstagevorher", vorher);
  }

  public Integer getAktuelleGeburtstageNachher() throws RemoteException
  {
    return (Integer) getAttribute("aktuellegeburtstagenachher");
  }

  public void setAktuelleGeburtstageNachher(Integer nachher)
      throws RemoteException
  {
    setAttribute("aktuellegeburtstagenachher", nachher);
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
    return (String) getAttribute("dateinamenmuster");
  }

  public void setDateinamenmuster(String dateinamenmuster)
      throws RemoteException
  {
    setAttribute("dateinamenmuster", dateinamenmuster);
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

  public boolean getRechnungFuerAbbuchung() throws RemoteException
  {
    return Util.getBoolean(getAttribute("rechnungfuerabbuchung"));
  }

  public void setRechnungFuerAbbuchung(Boolean rechnungfuerabbuchung)
      throws RemoteException
  {
    setAttribute("rechnungfuerabbuchung", new Boolean(rechnungfuerabbuchung));
  }

  public boolean getRechnungFuerUeberweisung() throws RemoteException
  {
    return Util.getBoolean(getAttribute("rechnungfuerueberweisung"));
  }

  public void setRechnungFuerUeberweisung(Boolean rechnungfuerueberweisung)
      throws RemoteException
  {
    setAttribute("rechnungfuerueberweisung", new Boolean(
        rechnungfuerueberweisung));
  }

  public boolean getRechnungFuerBarzahlung() throws RemoteException
  {
    return Util.getBoolean(getAttribute("rechnungfuerbarzahlung"));
  }

  public void setRechnungFuerBarzahlung(Boolean rechnungfuerbarzahlung)
      throws RemoteException
  {
    setAttribute("rechnungfuerbarzahlung", new Boolean(rechnungfuerbarzahlung));
  }

  public void setUpdateDiagInfos(Boolean updatediaginfos)
      throws RemoteException
  {
    setAttribute("updatediaginfos", new Boolean(updatediaginfos));
  }

  public Date getUpdateLastCheck() throws RemoteException
  {
    Date d = (Date) getAttribute("updatelastcheck");
    if (d == null)
    {
      return new Date();
    }
    return d;
  }

  public void setUpdateLastCheck(Date updatelastcheck) throws RemoteException
  {
    setAttribute("updatelastcheck", updatelastcheck);
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

}
