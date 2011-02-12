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
 * Revision 1.10  2011-01-09 14:31:00  jost
 * Stammdaten in die Einstellungen verschoben.
 *
 * Revision 1.9  2009/06/11 21:03:52  jost
 * Vorbereitung I18N
 *
 * Revision 1.8  2008/12/22 21:19:17  jost
 * Zusatzabbuchung->Zusatzbetrag
 *
 * Revision 1.7  2008/11/30 10:45:42  jost
 * Neu: Konfiguration der Spalten einer Tabelle
 *
 * Revision 1.6  2008/11/29 13:12:13  jost
 * Refactoring: Code-Optimierung
 *
 * Revision 1.5  2008/02/13 18:18:16  jost
 * Überflüssigen Import entfernt.
 *
 * Revision 1.4  2008/02/09 14:35:51  jost
 * Bugfix. Zusatzabbuchungen und Kursteilnehmer nur abbuchen, wenn das Häkchen gesetzt ist.
 *
 * Revision 1.3  2008/01/31 19:41:18  jost
 * Berücksichtigung eines Stichtages für die Abbuchung
 *
 * Revision 1.2  2007/12/28 13:14:25  jost
 * Bugfix beim erzeugen eines Stammdaten-Objektes
 *
 * Revision 1.1  2007/12/26 18:13:47  jost
 * Lastschriften können jetzt als Einzellastschriften oder Sammellastschriften direkt in Hibuscus verbucht werden.
 *
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.control.AbbuchungControl;
import de.jost_net.JVerein.keys.Abrechnungsausgabe;
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

  public final Boolean zusatzbetraege;

  public final Boolean kursteilnehmer;

  public final Boolean kompakteabbuchung;

  public final Boolean dtausprint;

  public final File dtausfile;

  public final String pdffile;

  public final DBService service;

  public Konto konto;

  public AbbuchungParam(AbbuchungControl ac, File dtausfile, String pdffile)
      throws ApplicationException, RemoteException
  {
    abbuchungsmodus = (Integer) ac.getAbbuchungsmodus().getValue();
    stichtag = (Date) ac.getStichtag().getValue();
    Abrechnungsausgabe aa = (Abrechnungsausgabe) ac.getAbbuchungsausgabe()
        .getValue();
    abbuchungsausgabe = aa.getKey();
    vondatum = (Date) ac.getVondatum().getValue();
    verwendungszweck = (String) ac.getZahlungsgrund().getValue();
    zusatzbetraege = (Boolean) ac.getZusatzbetrag().getValue();
    kursteilnehmer = (Boolean) ac.getKursteilnehmer().getValue();
    kompakteabbuchung = (Boolean) ac.getKompakteAbbuchung().getValue();
    dtausprint = (Boolean) ac.getDtausPrint().getValue();
    this.pdffile = pdffile;
    this.dtausfile = dtausfile;

    if (abbuchungsausgabe == Abrechnungsausgabe.HIBISCUS_EINZELBUCHUNGEN
        || abbuchungsausgabe == Abrechnungsausgabe.HIBISCUS_SAMMELBUCHUNG)
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
          if (Einstellungen.getEinstellung().getKonto()
              .equals(konto.getKontonummer())
              && Einstellungen.getEinstellung().getBlz().equals(konto.getBLZ()))
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
            throw new ApplicationException(JVereinPlugin.getI18n().tr(
                "Bitte w�hlen Sie ein Konto aus"));
          }
        }
      }
      catch (OperationCanceledException e)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Bitte w�hlen Sie ein Konto aus"));
      }
      catch (Exception e)
      {
        e.printStackTrace();
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Hibiscus-Datenbank kann nicht ge�ffnet werden."));
      }
    }
    else
    {
      service = null;
    }

  }
}
