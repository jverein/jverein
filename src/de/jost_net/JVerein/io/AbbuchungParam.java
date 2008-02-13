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
 * Revision 1.4  2008/02/09 14:35:51  jost
 * Bugfix. Zusatzabbuchungen und Kursteilnehmer nur abbuchen, wenn das HÃ¤kchen gesetzt ist.
 *
 * Revision 1.3  2008/01/31 19:41:18  jost
 * BerÃ¼cksichtigung eines Stichtages fÃ¼r die Abbuchung
 *
 * Revision 1.2  2007/12/28 13:14:25  jost
 * Bugfix beim erzeugen eines Stammdaten-Objektes
 *
 * Revision 1.1  2007/12/26 18:13:47  jost
 * Lastschriften kÃ¶nnen jetzt als Einzellastschriften oder Sammellastschriften direkt in Hibuscus verbucht werden.
 *
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.control.AbbuchungControl;
import de.jost_net.JVerein.gui.input.AbbuchungsausgabeInput;
import de.jost_net.JVerein.rmi.Stammdaten;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.hbci.HBCI;
import de.willuhn.jameica.hbci.gui.dialogs.KontoAuswahlDialog;
import de.willuhn.jameica.hbci.rmi.Konto;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.OperationCanceledException;
import de.willuhn.util.ApplicationException;

public class AbbuchungParam
{
  public final int abbuchungsmodus;

  public final Date stichtag;

  public final int abbuchungsausgabe;

  public final Date vondatum;

  public final String verwendungszweck;

  public final Boolean zusatzabbuchung;

  public final Boolean kursteilnehmer;

  public final Boolean dtausprint;

  public final File dtausfile;

  public final String pdffile;

  public final Stammdaten stamm;

  public final DBService service;

  public Konto konto;

  public AbbuchungParam(AbbuchungControl ac, File dtausfile, String pdffile)
      throws ApplicationException, RemoteException
  {
    abbuchungsmodus = (Integer) ac.getAbbuchungsmodus().getValue();
    stichtag = (Date) ac.getStichtag().getValue();
    abbuchungsausgabe = (Integer) ac.getAbbuchungsausgabe().getValue();
    vondatum = (Date) ac.getVondatum().getValue();
    verwendungszweck = (String) ac.getZahlungsgrund().getValue();
    zusatzabbuchung = (Boolean) ac.getZusatzabbuchung().getValue();
    kursteilnehmer = (Boolean) ac.getKursteilnehmer().getValue();
    dtausprint = (Boolean) ac.getDtausPrint().getValue();
    this.pdffile = pdffile;
    this.dtausfile = dtausfile;

    try
    {
      DBIterator list = Einstellungen.getDBService().createList(
          Stammdaten.class);
      if (list.size() > 0)
      {
        stamm = (Stammdaten) list.next();
      }
      else
      {
        throw new RemoteException("Keine Stammdaten gespeichert");
      }
    }
    catch (RemoteException e)
    {
      throw new ApplicationException(
          "Keine Stammdaten gespeichert. Bitte erfassen.");
    }

    if (abbuchungsausgabe == AbbuchungsausgabeInput.HIBISCUS_EINZELBUCHUNGEN
        || abbuchungsausgabe == AbbuchungsausgabeInput.HIBISCUS_SAMMELBUCHUNG)
    {
      // DB-Service holen
      try
      {
        service = (DBService) Application.getServiceFactory().lookup(
            HBCI.class, "database");
        DBIterator konten = service.createList(Konto.class);
        while (konten.hasNext())
        {
          konto = (Konto) konten.next();
          if (stamm.getKonto().equals(konto.getKontonummer())
              && stamm.getBlz().equals(konto.getBLZ()))
          {
            // passendes Konto gefunden
            break;
          }
          else
          {
            konto = null;
          }
        }
        if (konto == null)
        {
          // Kein passendes Konto gefunden. Deshalb Kontoauswahldialog.
          KontoAuswahlDialog d = new KontoAuswahlDialog(
              KontoAuswahlDialog.POSITION_CENTER);
          konto = (Konto) d.open();
          if (konto == null)
          {
            throw new ApplicationException("Bitte wählen Sie ein Konto aus");
          }
        }
      }
      catch (OperationCanceledException e)
      {
        throw new ApplicationException("Bitte Konto auswählen");
      }
      catch (Exception e)
      {
        e.printStackTrace();
        throw new ApplicationException(
            "Hibiscus-Datenbank kann nicht geöffnet werden.");
      }
    }
    else
    {
      service = null;
    }

  }
}
