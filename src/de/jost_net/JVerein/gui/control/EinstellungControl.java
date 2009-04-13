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
 * Buchführung
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
 * Wegfall Standardtab für die Suche
 *
 * Revision 1.2  2007/08/23 18:42:51  jost
 * Standard-Tab für die Mitglieder-Suche
 *
 * Revision 1.1  2007/08/22 20:42:56  jost
 * Bug #011762
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.keys.Beitragsmodel;
import de.jost_net.JVerein.rmi.Einstellung;
import de.jost_net.JVerein.util.MitgliedSpaltenauswahl;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.system.Settings;
import de.willuhn.util.ApplicationException;

public class EinstellungControl extends AbstractControl
{

  private CheckboxInput geburtsdatumpflicht;

  private CheckboxInput eintrittsdatumpflicht;

  private CheckboxInput kommunikationsdaten;

  private CheckboxInput zusatzbetrag;

  private CheckboxInput vermerke;

  private CheckboxInput wiedervorlage;

  private CheckboxInput kursteilnehmer;

  private CheckboxInput lehrgaenge;

  private CheckboxInput externemitgliedsnummer;

  private SelectInput beitragsmodel;

  private TextInput dateinamenmuster;

  private TextInput beginngeschaeftsjahr;

  private CheckboxInput rechnungfuerabbuchung;

  private CheckboxInput rechnungfuerueberweisung;

  private CheckboxInput rechnungfuerbarzahlung;

  private Settings settings;

  private MitgliedSpaltenauswahl spalten;

  public EinstellungControl(AbstractView view)
  {
    super(view);
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
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

  public CheckboxInput getRechnungFuerAbbuchung() throws RemoteException
  {
    if (rechnungfuerabbuchung != null)
    {
      return rechnungfuerabbuchung;
    }
    rechnungfuerabbuchung = new CheckboxInput(Einstellungen.getEinstellung()
        .getRechnungFuerAbbuchung());
    return rechnungfuerabbuchung;
  }

  public CheckboxInput getRechnungFuerUeberweisung() throws RemoteException
  {
    if (rechnungfuerueberweisung != null)
    {
      return rechnungfuerueberweisung;
    }
    rechnungfuerueberweisung = new CheckboxInput(Einstellungen.getEinstellung()
        .getRechnungFuerUeberweisung());
    return rechnungfuerueberweisung;
  }

  public CheckboxInput getRechnungFuerBarzahlung() throws RemoteException
  {
    if (rechnungfuerbarzahlung != null)
    {
      return rechnungfuerbarzahlung;
    }
    rechnungfuerbarzahlung = new CheckboxInput(Einstellungen.getEinstellung()
        .getRechnungFuerBarzahlung());
    return rechnungfuerbarzahlung;
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
      e.setGeburtsdatumPflicht((Boolean) geburtsdatumpflicht.getValue());
      e.setEintrittsdatumPflicht((Boolean) eintrittsdatumpflicht.getValue());
      e.setKommunikationsdaten((Boolean) kommunikationsdaten.getValue());
      e.setZusatzbetrag((Boolean) zusatzbetrag.getValue());
      e.setVermerke((Boolean) vermerke.getValue());
      e.setWiedervorlage((Boolean) wiedervorlage.getValue());
      e.setKursteilnehmer((Boolean) kursteilnehmer.getValue());
      e.setLehrgaenge((Boolean) lehrgaenge.getValue());
      e.setExterneMitgliedsnummer((Boolean) externemitgliedsnummer.getValue());
      Beitragsmodel bm = (Beitragsmodel) beitragsmodel.getValue();
      e.setBeitragsmodel(bm.getKey());
      e.setRechnungFuerAbbuchung((Boolean) rechnungfuerabbuchung.getValue());
      e.setRechnungFuerUeberweisung((Boolean) rechnungfuerueberweisung
          .getValue());
      e.setRechnungFuerBarzahlung((Boolean) rechnungfuerbarzahlung.getValue());
      e.setDateinamenmuster((String) dateinamenmuster.getValue());
      e.setBeginnGeschaeftsjahr((String) beginngeschaeftsjahr.getValue());
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

}
