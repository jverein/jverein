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
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Mitglied;
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
      ProgressMonitor monitor) throws ApplicationException
  {

    try
    {
      ICsvMapWriter writer = new CsvMapWriter(new FileWriter(file),
          CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

      String[] header = createHeader();
      CellProcessor[] processors = createCellProcessors();

      writer.writeHeader(header);

      for (Mitglied m : list)
      {
        writer.write(m.getMap(null), header, processors);
      }
      monitor.setStatusText("Auswertung fertig. " + list.size() + " Sätze.");
      writer.close();
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

  private String[] createHeader()
  {
    try
    {
      Mitglied m = (Mitglied) Einstellungen.getDBService().createObject(
          Mitglied.class, null);
      return m.getMap(null).keySet().toArray(new String[0]);
    }
    catch (RemoteException e)
    {
      Logger.error("Fehler", e);
    }
    return null;
  }

  private CellProcessor[] createCellProcessors()
  {
    try
    {
      Mitglied m = (Mitglied) Einstellungen.getDBService().createObject(
          Mitglied.class, null);
      Map<String, Object> map = m.getMap(null);
      CellProcessor[] ret = new CellProcessor[map.size()];
      for (int i = 0; i < map.size(); i++)
      {
        ret[i] = new ConvertNullTo("");
      }
      return ret;
    }
    catch (RemoteException e)
    {
      Logger.error("Fehler", e);
    }
    return null;
  }

}
