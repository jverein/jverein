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
 * Revision 1.17  2011-05-05 19:54:20  jost
 * neue Datumsformatierung.
 *
 * Revision 1.16  2011-04-11 21:04:32  jost
 * Bugfix Zusatzfelder
 *
 * Revision 1.15  2011-02-12 09:39:40  jost
 * Statische Codeanalyse mit Findbugs
 *
 * Revision 1.14  2010-10-15 09:58:29  jost
 * Code aufgeräumt
 *
 * Revision 1.13  2009-04-25 05:30:41  jost
 * Neu: Juristische Personen  können als Mitglied gespeichert werden.
 *
 * Revision 1.12  2008/12/04 20:00:55  jost
 * Handy aufgenommen
 *
 * Revision 1.11  2008/11/13 20:18:12  jost
 * Adressierungszusatz aufgenommen.
 *
 * Revision 1.10  2008/10/01 14:17:39  jost
 * Warnungen entfernt
 *
 * Revision 1.9  2008/07/12 19:09:06  jost
 * Bugfix bei leeren Zusatzfeldern
 *
 * Revision 1.8  2008/05/05 18:23:18  jost
 * *** empty log message ***
 *
 * Revision 1.7  2008/04/10 19:02:29  jost
 * Neu: Benutzerdefinierte Datenfelder
 *
 * Revision 1.6  2008/03/17 20:22:46  jost
 * Bezeichnung der Beitragsart wird ausgegeben.
 *
 * Revision 1.5  2008/01/27 09:42:37  jost
 * Vereinheitlichung der Mitgliedersuche durch die Klasse MitgliedQuery
 *
 * Revision 1.4  2008/01/01 12:36:01  jost
 * Javadoc korrigiert
 *
 * Revision 1.3  2007/02/23 20:28:04  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.2  2006/11/12 07:53:40  jost
 * Bugfix Anzahl Spalten
 *
 * Revision 1.1  2006/09/20 15:39:24  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.keys.Datentyp;
import de.jost_net.JVerein.rmi.Felddefinition;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Zusatzfelder;
import de.jost_net.JVerein.util.Datum;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.action.Program;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class MitgliedAuswertungCSValt
{

  public MitgliedAuswertungCSValt(ArrayList<Mitglied> list, final File file,
      ProgressMonitor monitor) throws ApplicationException
  {

    try
    {
      PrintWriter out = new PrintWriter(new FileOutputStream(file));
      out.print("id");
      if (Einstellungen.getEinstellung().getExterneMitgliedsnummer())
      {
        out.print(",externemitgliedsnummer");
      }
      out.print(";personenart;anrede;titel;name;vorname;adressierungszusatz;strasse;plz;ort;blz;konto;kontoinhaber;");
      out.print("geburtsdatum;geschlecht;telefonprivat;telefondienstlich;handy;email;");
      out.print("eintritt;beitragsgruppe;beitragsgruppetext;austritt;kuendigung");
      DBIterator it = Einstellungen.getDBService().createList(
          Felddefinition.class);
      while (it.hasNext())
      {
        Felddefinition fd = (Felddefinition) it.next();
        out.print(";" + fd.getName());
      }
      out.println("");
      int faelle = 0;

      for (int i = 0; i < list.size(); i++)
      {
        faelle++;
        monitor.setStatus(faelle);
        Mitglied m = list.get(i);
        out.print(m.getID() + ";");
        if (Einstellungen.getEinstellung().getExterneMitgliedsnummer())
        {
          out.print(m.getExterneMitgliedsnummer() + ";");
        }
        out.print(m.getPersonenart() + ";");
        out.print(m.getAnrede() + ";");
        out.print(m.getTitel() + ";");
        out.print(m.getName() + ";");
        out.print(m.getVorname() + ";");
        out.print(m.getAdressierungszusatz() + ";");
        out.print(m.getStrasse() + ";");
        out.print(m.getPlz() + ";");
        out.print(m.getOrt() + ";");
        out.print(m.getBlz() + ";");
        out.print(m.getKonto() + ";");
        out.print(m.getKontoinhaber() + ";");
        out.print(Datum.formatDate(m.getGeburtsdatum()) + ";");
        out.print(m.getGeschlecht() + ";");
        out.print(m.getTelefonprivat() + ";");
        out.print(m.getTelefondienstlich() + ";");
        out.print(m.getHandy() + ";");
        out.print(m.getEmail() + ";");
        out.print(Datum.formatDate(m.getEintritt()) + ";");
        out.print(m.getBeitragsgruppe().getID() + ";");
        out.print(m.getBeitragsgruppe().getBezeichnung() + ";");
        out.print(Datum.formatDate(m.getAustritt()) + ";");
        out.print(Datum.formatDate(m.getKuendigung()));
        it.begin();
        while (it.hasNext())
        {
          Felddefinition fd = (Felddefinition) it.next();
          DBIterator it2 = Einstellungen.getDBService().createList(
              Zusatzfelder.class);
          it2.addFilter("mitglied = ?", new Object[] { m.getID() });
          it2.addFilter("felddefinition = ?", new Object[] { fd.getID() });
          if (it2.size() > 0)
          {
            Zusatzfelder zf = (Zusatzfelder) it2.next();
            out.print(";");
            switch (fd.getDatentyp())
            {
              case Datentyp.ZEICHENFOLGE:
                out.print(zf.getFeld());
                break;
              case Datentyp.DATUM:
                out.print(zf.getFeldDatum() != null ? new JVDateFormatTTMMJJJJ()
                    .format(zf.getFeldDatum()) : "");
                break;
              case Datentyp.JANEIN:
                out.print(zf.getFeldJaNein());
                break;
              case Datentyp.GANZZAHL:
                out.print(zf.getFeldGanzzahl());
                break;
              case Datentyp.WAEHRUNG:
                out.print(zf.getFeldWaehrung() != null ? Einstellungen.DECIMALFORMAT
                    .format(zf.getFeldWaehrung()) : "");
                break;
            }
          }
          else
          {
            out.print(";");
          }
        }
        out.println("");
      }
      monitor.setStatusText("Auswertung fertig. " + list.size() + " Sätze.");
      out.close();
      GUI.getDisplay().asyncExec(new Runnable()
      {

        public void run()
        {
          try
          {
            new Program().handleAction(file);
          }
          catch (ApplicationException ae)
          {
            Application.getMessagingFactory().sendMessage(
                new StatusBarMessage(ae.getLocalizedMessage(),
                    StatusBarMessage.TYPE_ERROR));
          }
        }
      });

    }
    catch (FileNotFoundException e)
    {
      Logger.error("error while creating report", e);
      throw new ApplicationException("Fehler beim Erzeugen des Reports", e);
    }
    catch (IOException e)
    {
      Logger.error("error while creating report", e);
      throw new ApplicationException("Fehler beim Erzeugen des Reports", e);
    }

  }

}
