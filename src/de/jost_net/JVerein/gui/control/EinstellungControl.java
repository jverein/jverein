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
 * Revision 1.35  2011-01-29 20:34:00  jost
 * Verzˆgerungszeit f¸r Suchfelder
 *
 * Revision 1.34  2011-01-09 14:29:18  jost
 * Stammdaten in die Einstellungen verschoben.
 *
 * Revision 1.33  2011-01-08 15:56:03  jost
 * Einstellungen: Dokumentenspeicherung
 *
 * Revision 1.32  2010-11-17 04:49:46  jost
 * Erster Code zum Thema Arbeitseinsatz
 *
 * Revision 1.31  2010-11-13 09:23:47  jost
 * Mit V 1.5 deprecatete Spalten und Tabellen entfernt.
 *
 * Revision 1.30  2010-10-28 19:13:07  jost
 * Neu: Wohnsitzstaat
 *
 * Revision 1.29  2010-08-27 19:07:06  jost
 * neu: Mitgliedsfoto
 *
 * Revision 1.28  2010-08-25 11:52:42  jost
 * Bugfix NPE
 *
 * Revision 1.27  2010-08-10 18:06:09  jost
 * Zahlungswegtexte f¸r den Rechnungsdruck
 *
 * Revision 1.26  2010-08-10 05:37:49  jost
 * Reaktivierung alter Rechnungen
 *
 * Revision 1.25  2010-07-26 08:22:48  jost
 * Manuelle Zahlungen defaultm‰ﬂig deaktviert. Reaktvierbar durch Einstellungen.
 *
 * Revision 1.24  2010-07-25 18:31:40  jost
 * Neu: Mitgliedskonto
 *
 * Revision 1.23  2010/02/01 20:57:47  jost
 * Neu: Einfache Mailfunktion
 *
 * Revision 1.22  2010/01/01 22:35:31  jost
 * Standardwerte f¸r Zahlungsweg und Zahlungsrhytmus kˆnnen vorgegeben werden.
 *
 * Revision 1.21  2009/12/06 21:40:23  jost
 * ‹berfl¸ssigen Code entfernt.
 *
 * Revision 1.20  2009/11/19 21:10:37  jost
 * Update-Option entfernt.
 *
 * Revision 1.19  2009/10/17 19:45:24  jost
 * Vorbereitung Mailversand.
 *
 * Revision 1.18  2009/09/13 19:19:39  jost
 * Neu: Pr¸fung auf Updates
 *
 * Revision 1.17  2009/07/14 07:28:53  jost
 * Neu: Box aktuelle Geburtstage
 *
 * Revision 1.16  2009/04/25 05:27:52  jost
 * Neu: Juristische Personen  kˆnnen als Mitglied gespeichert werden.
 *
 * Revision 1.15  2009/04/13 11:39:14  jost
 * Neu: Lehrg‰nge
 *
 * Revision 1.14  2008/12/22 21:08:50  jost
 * Zusatzabbuchung->Zusatzbetrag
 *
 * Revision 1.13  2008/12/13 16:21:56  jost
 * Bugfix Standardwert
 *
 * Revision 1.12  2008/11/30 18:56:38  jost
 * Refactoring: Code-Optimierung
 *
 * Revision 1.11  2008/11/29 13:07:10  jost
 * Refactoring: Code-Optimierung
 *
 * Revision 1.10  2008/11/24 19:43:03  jost
 * Bugfix
 *
 * Revision 1.9  2008/11/16 16:56:26  jost
 * Speicherung der Einstellung von Property-Datei in die Datenbank verschoben.
 *
 * Revision 1.8  2008/08/10 12:35:01  jost
 * Vorbereitung der Rechnungserstellung
 *
 * Revision 1.7  2008/05/22 06:48:19  jost
 * Buchf√ºhrung
 *
 * Revision 1.6  2008/03/08 19:28:49  jost
 * Neu: Externe Mitgliedsnummer
 *
 * Revision 1.5  2008/01/01 13:13:12  jost
 * Neu: Dateinamenmuster
 *
 * Revision 1.4  2007/12/02 13:39:31  jost
 * Neu: Beitragsmodelle
 *
 * Revision 1.3  2007/12/01 17:45:51  jost
 * Wegfall Standardtab f√ºr die Suche
 *
 * Revision 1.2  2007/08/23 18:42:51  jost
 * Standard-Tab f√ºr die Mitglieder-Suche
 *
 * Revision 1.1  2007/08/22 20:42:56  jost
 * Bug #011762
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.keys.Beitragsmodel;
import de.jost_net.JVerein.keys.Zahlungsrhytmus;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Einstellung;
import de.jost_net.JVerein.util.MitgliedSpaltenauswahl;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.IntegerInput;
import de.willuhn.jameica.gui.input.PasswordInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class EinstellungControl extends AbstractControl
{
  private Input name;

  private Input blz;

  private Input konto;

  private CheckboxInput geburtsdatumpflicht;

  private CheckboxInput eintrittsdatumpflicht;

  private CheckboxInput kommunikationsdaten;

  private CheckboxInput zusatzbetrag;

  private CheckboxInput vermerke;

  private CheckboxInput wiedervorlage;

  private CheckboxInput kursteilnehmer;

  private CheckboxInput lehrgaenge;

  private CheckboxInput juristischepersonen;

  private CheckboxInput mitgliedskonto;

  private CheckboxInput mitgliedfoto;

  private CheckboxInput zusatzadressen;

  private CheckboxInput auslandsadressen;

  private CheckboxInput arbeitseinsatz;

  private CheckboxInput dokumentenspeicherung;

  private TextInput rechnungtextabbuchung;

  private TextInput rechnungtextueberweisung;

  private TextInput rechnungtextbar;

  private CheckboxInput externemitgliedsnummer;

  private SelectInput beitragsmodel;

  private TextInput dateinamenmuster;

  private TextInput beginngeschaeftsjahr;

  private SelectInput aktuellegeburtstagevorher;

  private SelectInput aktuellegeburtstagenachher;

  private TextInput smtp_server;

  private IntegerInput smtp_port;

  private TextInput smtp_auth_user;

  private PasswordInput smtp_auth_pwd;

  private TextInput smtp_from_address;

  private CheckboxInput smtp_ssl;

  private SelectInput zahlungsweg;

  private SelectInput zahlungsrhytmus;

  private Input altersgruppen;

  private Input jubilaeen;

  private Input altersjubilaeen;

  private IntegerInput delaytime;

  private Settings settings;

  private MitgliedSpaltenauswahl spalten;

  public EinstellungControl(AbstractView view)
  {
    super(view);
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public Input getName(boolean withFocus) throws RemoteException
  {
    if (name != null)
    {
      return name;
    }
    name = new TextInput(Einstellungen.getEinstellung().getName(), 27);
    name.setMandatory(true);
    if (withFocus)
    {
      name.focus();
    }
    return name;
  }

  public Input getBlz() throws RemoteException
  {
    if (blz != null)
    {
      return blz;
    }
    blz = new TextInput(Einstellungen.getEinstellung().getBlz(), 8);
    blz.setMandatory(true);
    BLZListener l = new BLZListener();
    blz.addListener(l);
    l.handleEvent(null);
    return blz;
  }

  public Input getKonto() throws RemoteException
  {
    if (konto != null)
    {
      return konto;
    }
    konto = new TextInput(Einstellungen.getEinstellung().getKonto(), 10);
    konto.setMandatory(true);
    konto.setComment("f¸r die Abbuchung");
    return konto;
  }

  public CheckboxInput getGeburtsdatumPflicht() throws RemoteException
  {
    if (geburtsdatumpflicht != null)
    {
      return geburtsdatumpflicht;
    }
    geburtsdatumpflicht = new CheckboxInput(Einstellungen.getEinstellung()
        .getGeburtsdatumPflicht());
    return geburtsdatumpflicht;
  }

  public CheckboxInput getEintrittsdatumPflicht() throws RemoteException
  {
    if (eintrittsdatumpflicht != null)
    {
      return eintrittsdatumpflicht;
    }
    eintrittsdatumpflicht = new CheckboxInput(Einstellungen.getEinstellung()
        .getEintrittsdatumPflicht());
    return eintrittsdatumpflicht;
  }

  public CheckboxInput getKommunikationsdaten() throws RemoteException
  {
    if (kommunikationsdaten != null)
    {
      return kommunikationsdaten;
    }
    kommunikationsdaten = new CheckboxInput(Einstellungen.getEinstellung()
        .getKommunikationsdaten());
    return kommunikationsdaten;
  }

  public CheckboxInput getZusatzbetrag() throws RemoteException
  {
    if (zusatzbetrag != null)
    {
      return zusatzbetrag;
    }
    zusatzbetrag = new CheckboxInput(Einstellungen.getEinstellung()
        .getZusatzbetrag());
    return zusatzbetrag;
  }

  public CheckboxInput getVermerke() throws RemoteException
  {
    if (vermerke != null)
    {
      return vermerke;
    }
    vermerke = new CheckboxInput(Einstellungen.getEinstellung().getVermerke());
    return vermerke;
  }

  public CheckboxInput getWiedervorlage() throws RemoteException
  {
    if (wiedervorlage != null)
    {
      return wiedervorlage;
    }
    wiedervorlage = new CheckboxInput(Einstellungen.getEinstellung()
        .getWiedervorlage());
    return wiedervorlage;
  }

  public CheckboxInput getKursteilnehmer() throws RemoteException
  {
    if (kursteilnehmer != null)
    {
      return kursteilnehmer;
    }
    kursteilnehmer = new CheckboxInput(Einstellungen.getEinstellung()
        .getKursteilnehmer());
    return kursteilnehmer;
  }

  public CheckboxInput getLehrgaenge() throws RemoteException
  {
    if (lehrgaenge != null)
    {
      return lehrgaenge;
    }
    lehrgaenge = new CheckboxInput(Einstellungen.getEinstellung()
        .getLehrgaenge());
    return lehrgaenge;
  }

  public CheckboxInput getJuristischePersonen() throws RemoteException
  {
    if (juristischepersonen != null)
    {
      return juristischepersonen;
    }
    juristischepersonen = new CheckboxInput(Einstellungen.getEinstellung()
        .getJuristischePersonen());
    return juristischepersonen;
  }

  public CheckboxInput getMitgliedskonto() throws RemoteException
  {
    if (mitgliedskonto != null)
    {
      return mitgliedskonto;
    }
    mitgliedskonto = new CheckboxInput(Einstellungen.getEinstellung()
        .getMitgliedskonto());
    return mitgliedskonto;
  }

  public CheckboxInput getMitgliedfoto() throws RemoteException
  {
    if (mitgliedfoto != null)
    {
      return mitgliedfoto;
    }
    mitgliedfoto = new CheckboxInput(Einstellungen.getEinstellung()
        .getMitgliedfoto());
    return mitgliedfoto;
  }

  public CheckboxInput getZusatzadressen() throws RemoteException
  {
    if (zusatzadressen != null)
    {
      return zusatzadressen;
    }
    zusatzadressen = new CheckboxInput(Einstellungen.getEinstellung()
        .getZusatzadressen());
    return zusatzadressen;
  }

  public CheckboxInput getAuslandsadressen() throws RemoteException
  {
    if (auslandsadressen != null)
    {
      return auslandsadressen;
    }
    auslandsadressen = new CheckboxInput(Einstellungen.getEinstellung()
        .getAuslandsadressen());
    return auslandsadressen;
  }

  public CheckboxInput getArbeitseinsatz() throws RemoteException
  {
    if (arbeitseinsatz != null)
    {
      return arbeitseinsatz;
    }
    arbeitseinsatz = new CheckboxInput(Einstellungen.getEinstellung()
        .getArbeitseinsatz());
    return arbeitseinsatz;
  }

  public CheckboxInput getDokumentenspeicherung() throws RemoteException
  {
    if (dokumentenspeicherung != null)
    {
      return dokumentenspeicherung;
    }
    dokumentenspeicherung = new CheckboxInput(Einstellungen.getEinstellung()
        .getDokumentenspeicherung());
    return dokumentenspeicherung;
  }

  public TextInput getRechnungTextAbbuchung() throws RemoteException
  {
    if (rechnungtextabbuchung != null)
    {
      return rechnungtextabbuchung;
    }
    rechnungtextabbuchung = new TextInput(Einstellungen.getEinstellung()
        .getRechnungTextAbbuchung(), 100);
    return rechnungtextabbuchung;
  }

  public TextInput getRechnungTextUeberweisung() throws RemoteException
  {
    if (rechnungtextueberweisung != null)
    {
      return rechnungtextueberweisung;
    }
    rechnungtextueberweisung = new TextInput(Einstellungen.getEinstellung()
        .getRechnungTextUeberweisung(), 100);
    return rechnungtextueberweisung;
  }

  public TextInput getRechnungTextBar() throws RemoteException
  {
    if (rechnungtextbar != null)
    {
      return rechnungtextbar;
    }
    rechnungtextbar = new TextInput(Einstellungen.getEinstellung()
        .getRechnungTextBar(), 100);
    return rechnungtextbar;
  }

  public CheckboxInput getExterneMitgliedsnummer() throws RemoteException
  {
    if (externemitgliedsnummer != null)
    {
      return externemitgliedsnummer;
    }
    externemitgliedsnummer = new CheckboxInput(Einstellungen.getEinstellung()
        .getExterneMitgliedsnummer());
    return externemitgliedsnummer;
  }

  public SelectInput getAktuelleGeburtstageVorher() throws RemoteException
  {
    if (aktuellegeburtstagevorher != null)
    {
      return aktuellegeburtstagevorher;
    }
    Integer[] v = new Integer[30];
    for (int i = 0; i < 30; i++)
    {
      v[i] = i;
    }
    aktuellegeburtstagevorher = new SelectInput(v, Einstellungen
        .getEinstellung().getAktuelleGeburtstageVorher());
    return aktuellegeburtstagevorher;
  }

  public SelectInput getAktuelleGeburtstageNachher() throws RemoteException
  {
    if (aktuellegeburtstagenachher != null)
    {
      return aktuellegeburtstagenachher;
    }
    Integer[] v = new Integer[30];
    for (int i = 0; i < 30; i++)
    {
      v[i] = i;
    }
    aktuellegeburtstagenachher = new SelectInput(v, Einstellungen
        .getEinstellung().getAktuelleGeburtstageNachher());
    return aktuellegeburtstagenachher;
  }

  public SelectInput getBeitragsmodel() throws RemoteException
  {
    if (beitragsmodel != null)
    {
      return beitragsmodel;
    }
    beitragsmodel = new SelectInput(Beitragsmodel.getArray(),
        new Beitragsmodel(Einstellungen.getEinstellung().getBeitragsmodel()));
    return beitragsmodel;
  }

  public TextInput getDateinamenmuster() throws RemoteException
  {
    if (dateinamenmuster != null)
    {
      return dateinamenmuster;
    }
    dateinamenmuster = new TextInput(Einstellungen.getEinstellung()
        .getDateinamenmuster(), 30);
    return dateinamenmuster;
  }

  public TextInput getBeginnGeschaeftsjahr() throws RemoteException
  {
    if (beginngeschaeftsjahr != null)
    {
      return beginngeschaeftsjahr;
    }
    beginngeschaeftsjahr = new TextInput(Einstellungen.getEinstellung()
        .getBeginnGeschaeftsjahr(), 6);
    return beginngeschaeftsjahr;
  }

  public TextInput getSmtpServer() throws RemoteException
  {
    if (smtp_server != null)
    {
      return smtp_server;
    }
    smtp_server = new TextInput(Einstellungen.getEinstellung().getSmtpServer(),
        50);
    return smtp_server;
  }

  public IntegerInput getSmtpPort() throws RemoteException
  {
    if (smtp_port != null)
    {
      return smtp_port;
    }
    smtp_port = new IntegerInput(new Integer(Einstellungen.getEinstellung()
        .getSmtpPort()));
    return smtp_port;
  }

  public TextInput getSmtpAuthUser() throws RemoteException
  {
    if (smtp_auth_user != null)
    {
      return smtp_auth_user;
    }
    smtp_auth_user = new TextInput(Einstellungen.getEinstellung()
        .getSmtpAuthUser(), 50);
    return smtp_auth_user;
  }

  public PasswordInput getSmtpAuthPwd() throws RemoteException
  {
    if (smtp_auth_pwd != null)
    {
      return smtp_auth_pwd;
    }
    smtp_auth_pwd = new PasswordInput(Einstellungen.getEinstellung()
        .getSmtpAuthPwd());
    return smtp_auth_pwd;
  }

  public TextInput getSmtpFromAddress() throws RemoteException
  {
    if (smtp_from_address != null)
    {
      return smtp_from_address;
    }
    smtp_from_address = new TextInput(Einstellungen.getEinstellung()
        .getSmtpFromAddress(), 50);
    return smtp_from_address;
  }

  public CheckboxInput getSmtpSsl() throws RemoteException
  {
    if (smtp_ssl != null)
    {
      return smtp_ssl;
    }
    smtp_ssl = new CheckboxInput(Einstellungen.getEinstellung().getSmtpSsl());
    return smtp_ssl;
  }

  public SelectInput getZahlungsweg() throws RemoteException
  {
    if (zahlungsweg != null)
    {
      return zahlungsweg;
    }
    zahlungsweg = new SelectInput(Zahlungsweg.getArray(), new Zahlungsweg(
        Einstellungen.getEinstellung().getZahlungsweg()));
    zahlungsweg.setName("Zahlungsweg");
    return zahlungsweg;
  }

  public SelectInput getZahlungsrhytmus() throws RemoteException
  {
    if (zahlungsrhytmus != null)
    {
      return zahlungsrhytmus;
    }
    zahlungsrhytmus = new SelectInput(
        Zahlungsrhytmus.getArray(),
        new Zahlungsrhytmus(Einstellungen.getEinstellung().getZahlungsrhytmus()));
    zahlungsrhytmus.setName("Zahlungsrhytmus");
    return zahlungsrhytmus;
  }

  public Input getAltersgruppen() throws RemoteException
  {
    if (altersgruppen != null)
    {
      return altersgruppen;
    }
    altersgruppen = new TextInput(Einstellungen.getEinstellung()
        .getAltersgruppen(), 50);
    return altersgruppen;
  }

  public Input getJubilaeen() throws RemoteException
  {
    if (jubilaeen != null)
    {
      return jubilaeen;
    }
    jubilaeen = new TextInput(Einstellungen.getEinstellung().getJubilaeen(), 50);
    return jubilaeen;
  }

  public Input getAltersjubilaeen() throws RemoteException
  {
    if (altersjubilaeen != null)
    {
      return altersjubilaeen;
    }
    altersjubilaeen = new TextInput(Einstellungen.getEinstellung()
        .getAltersjubilaeen(), 50);
    return altersjubilaeen;
  }

  public IntegerInput getDelaytime() throws RemoteException
  {
    if (delaytime != null)
    {
      return delaytime;
    }
    delaytime = new IntegerInput(new Integer(Einstellungen.getEinstellung()
        .getDelaytime()));
    return delaytime;
  }

  public TablePart getSpaltendefinitionTable(Composite parent)
      throws RemoteException
  {
    if (spalten == null)
    {
      spalten = new MitgliedSpaltenauswahl();
    }
    return spalten.paintSpaltenpaintSpaltendefinitionTable(parent);
  }

  // public void setCheckSpalten()
  // {
  // for (int i = 0; i < spalten.size(); ++i)
  // {
  // spaltendefinitionList.setChecked(spalten.get(i), spalten.get(i)
  // .isChecked());
  // }
  // }

  public void handleStore()
  {
    try
    {
      Einstellung e = Einstellungen.getEinstellung();
      e.setID();
      e.setName((String) getName(false).getValue());
      e.setBlz((String) getBlz().getValue());
      e.setKonto((String) getKonto().getValue());
      e.setGeburtsdatumPflicht((Boolean) geburtsdatumpflicht.getValue());
      e.setEintrittsdatumPflicht((Boolean) eintrittsdatumpflicht.getValue());
      e.setKommunikationsdaten((Boolean) kommunikationsdaten.getValue());
      e.setZusatzbetrag((Boolean) zusatzbetrag.getValue());
      e.setVermerke((Boolean) vermerke.getValue());
      e.setWiedervorlage((Boolean) wiedervorlage.getValue());
      e.setKursteilnehmer((Boolean) kursteilnehmer.getValue());
      e.setLehrgaenge((Boolean) lehrgaenge.getValue());
      e.setJuristischePersonen((Boolean) juristischepersonen.getValue());
      e.setMitgliedskonto((Boolean) mitgliedskonto.getValue());
      e.setMitgliedfoto((Boolean) mitgliedfoto.getValue());
      e.setZusatzadressen((Boolean) zusatzadressen.getValue());
      e.setAuslandsadressen((Boolean) auslandsadressen.getValue());
      e.setArbeitseinsatz((Boolean) arbeitseinsatz.getValue());
      e.setDokumentenspeicherung((Boolean) dokumentenspeicherung.getValue());
      e.setRechnungTextAbbuchung((String) rechnungtextabbuchung.getValue());
      e.setRechnungTextAbbuchung((String) rechnungtextabbuchung.getValue());
      e.setRechnungTextUeberweisung((String) rechnungtextueberweisung
          .getValue());
      e.setRechnungTextBar((String) rechnungtextbar.getValue());
      e.setAktuelleGeburtstageVorher((Integer) aktuellegeburtstagevorher
          .getValue());
      e.setAktuelleGeburtstageNachher((Integer) aktuellegeburtstagenachher
          .getValue());
      e.setExterneMitgliedsnummer((Boolean) externemitgliedsnummer.getValue());
      Beitragsmodel bm = (Beitragsmodel) beitragsmodel.getValue();
      e.setBeitragsmodel(bm.getKey());
      // e.setRechnungFuerAbbuchung((Boolean) rechnungfuerabbuchung.getValue());
      // e.setRechnungFuerUeberweisung((Boolean) rechnungfuerueberweisung
      // .getValue());
      // e.setRechnungFuerBarzahlung((Boolean)
      // rechnungfuerbarzahlung.getValue());
      e.setDateinamenmuster((String) dateinamenmuster.getValue());
      e.setBeginnGeschaeftsjahr((String) beginngeschaeftsjahr.getValue());
      e.setSmtpServer((String) smtp_server.getValue());
      Integer port = (Integer) smtp_port.getValue();
      e.setSmtpPort(port.toString());
      e.setSmtpAuthUser((String) smtp_auth_user.getValue());
      e.setSmtpAuthPwd((String) smtp_auth_pwd.getValue());
      e.setSmtpFromAddress((String) smtp_from_address.getValue());
      e.setSmtpSsl((Boolean) smtp_ssl.getValue());
      Zahlungsrhytmus zr = (Zahlungsrhytmus) zahlungsrhytmus.getValue();
      e.setZahlungsrhytmus(zr.getKey());
      Zahlungsweg zw = (Zahlungsweg) zahlungsweg.getValue();
      e.setZahlungsweg(zw.getKey());
      e.setAltersgruppen((String) getAltersgruppen().getValue());
      e.setJubilaeen((String) getJubilaeen().getValue());
      e.setAltersjubilaeen((String) getAltersjubilaeen().getValue());
      Integer delay = (Integer) delaytime.getValue();
      e.setDelaytime(delay);
      e.store();
      spalten.save();
      GUI.getStatusBar().setSuccessText("Einstellungen gespeichert");
    }
    catch (RemoteException e)
    {
      GUI.getStatusBar().setErrorText(e.getMessage());
    }
    catch (ApplicationException e)
    {
      GUI.getStatusBar().setErrorText(e.getMessage());
    }
  }

  /**
   * Sucht das Geldinstitut zur eingegebenen BLZ und zeigt es als Kommentar
   * hinter dem BLZ-Feld an.
   */
  private class BLZListener implements Listener
  {
    public void handleEvent(Event event)
    {
      try
      {
        String blz = (String) getBlz().getValue();
        getBlz().setComment(Einstellungen.getNameForBLZ(blz));
      }
      catch (RemoteException e)
      {
        Logger.error("error while updating blz comment", e);
      }
    }
  }

}
