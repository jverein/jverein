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
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Felddefinition;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Zusatzfelder;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.action.Program;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class MitgliedAuswertungCSV
{
  public MitgliedAuswertungCSV(ArrayList<Mitglied> list, final File file,
      ProgressMonitor monitor) throws ApplicationException, RemoteException
  {

    try
    {
      PrintWriter out = new PrintWriter(new FileOutputStream(file));
      out
          .print("id;anrede;titel;name;vorname;adressierungszusatz;strasse;plz;ort;blz;konto;kontoinhaber;");
      out
          .print("geburtsdatum;geschlecht;telefonprivat;telefondienstlich;email;");
      out
          .print("eintritt;beitragsgruppe;beitragsgruppetext;austritt;kuendigung");
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
        out.print(formatDate(m.getGeburtsdatum()) + ";");
        out.print(m.getGeschlecht() + ";");
        out.print(m.getTelefonprivat() + ";");
        out.print(m.getTelefondienstlich() + ";");
        out.print(m.getEmail() + ";");
        out.print(formatDate(m.getEintritt()) + ";");
        out.print(m.getBeitragsgruppe().getID() + ";");
        out.print(m.getBeitragsgruppe().getBezeichnung() + ";");
        out.print(formatDate(m.getAustritt()) + ";");
        out.print(formatDate(m.getKuendigung()));
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
            out.print(";" + zf.getFeld());
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

  /**
   * Gibt einen Leerstring aus, falls der Text null ist.
   * 
   * @return der Text oder Leerstring - niemals null.
   */
  private String formatDate(Date d)
  {
    return d == null ? "" : Einstellungen.DATEFORMAT.format(d);
  }

}
