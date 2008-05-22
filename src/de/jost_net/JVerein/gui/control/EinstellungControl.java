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
 * Wegfall Standardtab fÃ¼r die Suche
 *
 * Revision 1.2  2007/08/23 18:42:51  jost
 * Standard-Tab fÃ¼r die Mitglieder-Suche
 *
 * Revision 1.1  2007/08/22 20:42:56  jost
 * Bug #011762
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;
import java.text.ParseException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.input.BeitragsmodelInput;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.util.ApplicationException;

public class EinstellungControl extends AbstractControl
{

  private CheckboxInput geburtsdatumpflicht;

  private CheckboxInput eintrittsdatumpflicht;

  private CheckboxInput kommunikationsdaten;

  private CheckboxInput zusatzabbuchung;

  private CheckboxInput vermerke;

  private CheckboxInput wiedervorlage;

  private CheckboxInput kursteilnehmer;

  private CheckboxInput externemitgliedsnummer;

  private SelectInput beitragsmodel;

  private TextInput dateinamenmuster;

  private TextInput beginngeschaeftsjahr;

  public EinstellungControl(AbstractView view)
  {
    super(view);
  }

  public CheckboxInput getGeburtsdatumPflicht() throws RemoteException
  {
    if (geburtsdatumpflicht != null)
    {
      return geburtsdatumpflicht;
    }
    geburtsdatumpflicht = new CheckboxInput(Einstellungen
        .isGeburtsdatumPflicht());
    return geburtsdatumpflicht;
  }

  public CheckboxInput getEintrittsdatumPflicht() throws RemoteException
  {
    if (eintrittsdatumpflicht != null)
    {
      return eintrittsdatumpflicht;
    }
    eintrittsdatumpflicht = new CheckboxInput(Einstellungen
        .isEintrittsdatumPflicht());
    return eintrittsdatumpflicht;
  }

  public CheckboxInput getKommunikationsdaten() throws RemoteException
  {
    if (kommunikationsdaten != null)
    {
      return kommunikationsdaten;
    }
    kommunikationsdaten = new CheckboxInput(Einstellungen
        .isKommunikationsdaten());
    return kommunikationsdaten;
  }

  public CheckboxInput getZusatzabbuchung() throws RemoteException
  {
    if (zusatzabbuchung != null)
    {
      return zusatzabbuchung;
    }
    zusatzabbuchung = new CheckboxInput(Einstellungen.isZusatzabbuchung());
    return zusatzabbuchung;
  }

  public CheckboxInput getVermerke() throws RemoteException
  {
    if (vermerke != null)
    {
      return vermerke;
    }
    vermerke = new CheckboxInput(Einstellungen.isVermerke());
    return vermerke;
  }

  public CheckboxInput getWiedervorlage() throws RemoteException
  {
    if (wiedervorlage != null)
    {
      return wiedervorlage;
    }
    wiedervorlage = new CheckboxInput(Einstellungen.isWiedervorlage());
    return wiedervorlage;
  }

  public CheckboxInput getKursteilnehmer() throws RemoteException
  {
    if (kursteilnehmer != null)
    {
      return kursteilnehmer;
    }
    kursteilnehmer = new CheckboxInput(Einstellungen.isKursteilnehmer());
    return kursteilnehmer;
  }

  public CheckboxInput getExterneMitgliedsnummer() throws RemoteException
  {
    if (externemitgliedsnummer != null)
    {
      return externemitgliedsnummer;
    }
    externemitgliedsnummer = new CheckboxInput(Einstellungen
        .isExterneMitgliedsnummer());
    return externemitgliedsnummer;
  }

  public SelectInput getBeitragsmodel() throws RemoteException
  {
    if (beitragsmodel != null)
    {
      return beitragsmodel;
    }
    beitragsmodel = new BeitragsmodelInput(Einstellungen.getBeitragsmodel());
    return beitragsmodel;
  }

  public TextInput getDateinamenmuster() throws RemoteException
  {
    if (dateinamenmuster != null)
    {
      return dateinamenmuster;
    }
    dateinamenmuster = new TextInput(Einstellungen.getDateinamenmuster(), 30);
    return dateinamenmuster;
  }

  public TextInput getBeginnGeschaeftsjahr() throws RemoteException
  {
    if (beginngeschaeftsjahr != null)
    {
      return beginngeschaeftsjahr;
    }
    beginngeschaeftsjahr = new TextInput(Einstellungen
        .getBeginnGeschaeftsjahr(), 6);
    return beginngeschaeftsjahr;
  }

  public void handleStore()
  {
    try
    {
      Boolean _geburtsdatumpflicht = (Boolean) geburtsdatumpflicht.getValue();
      Boolean _eintrittsdatumpflicht = (Boolean) eintrittsdatumpflicht
          .getValue();
      Boolean _kommunikationsdaten = (Boolean) kommunikationsdaten.getValue();
      Boolean _zusatzabbuchung = (Boolean) zusatzabbuchung.getValue();
      Boolean _vermerke = (Boolean) vermerke.getValue();
      Boolean _wiedervorlage = (Boolean) wiedervorlage.getValue();
      Boolean _kursteilnehmer = (Boolean) kursteilnehmer.getValue();
      Boolean _externemitgliedsnummer = (Boolean) externemitgliedsnummer
          .getValue();
      Integer _beitragsmodel = (Integer) beitragsmodel.getValue();
      Einstellungen.setGeburtsdatumPflicht(_geburtsdatumpflicht.booleanValue());
      Einstellungen.setEintrittsdatumPflicht(_eintrittsdatumpflicht
          .booleanValue());
      Einstellungen.setKommunikationsdaten(_kommunikationsdaten.booleanValue());
      Einstellungen.setZusatzabbuchungen(_zusatzabbuchung.booleanValue());
      Einstellungen.setVermerke(_vermerke.booleanValue());
      Einstellungen.setWiedervorlage(_wiedervorlage.booleanValue());
      Einstellungen.setKursteilnehmer(_kursteilnehmer.booleanValue());
      Einstellungen.setExterneMitgliedsnummern(_externemitgliedsnummer
          .booleanValue());
      Einstellungen.setBeitragsmodel(_beitragsmodel.intValue());
      Einstellungen.setDateinamenmuster((String) dateinamenmuster.getValue());
      try
      {
        String bg = (String) beginngeschaeftsjahr.getValue();
        bg += "2008";
        Einstellungen.DATEFORMAT.parse(bg);
      }
      catch (ParseException e)
      {
        throw new ApplicationException("Beginn Geschäftsjahr ungültig");
      }
      Einstellungen.setBeginnGeschaeftsjahr((String) beginngeschaeftsjahr
          .getValue());
      GUI.getStatusBar().setSuccessText("Einstellungen gespeichert");
    }
    catch (ApplicationException e)
    {
      GUI.getStatusBar().setErrorText(e.getMessage());
    }
  }
}
