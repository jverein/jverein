/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * jost@berlios.de
 * jverein.berlios.de
 * $Log$
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Mitglied;
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
  public MitgliedAuswertungCSV(DBIterator list, final File file,
      ProgressMonitor monitor) throws ApplicationException, RemoteException
  {

    try
    {
      PrintWriter out = new PrintWriter(new FileOutputStream(file));

      out
          .print("id;anrede;titel;name;vorname;strasse;plz;ort;blz;konto;kontoinhaber;");
      out
          .print("geburtsdatum;geschlecht;telefonprivat;telefondienstlich;email;");
      out.println("eintritt;beitragsgruppe;austritt;kuendigung");
      int faelle = 0;

      while (list.hasNext())
      {
        faelle++;
        monitor.setStatus(faelle);
        Mitglied m = (Mitglied) list.next();
        out.print(m.getID() + ";");
        out.print(m.getAnrede() + ";");
        out.print(m.getTitel() + ";");
        out.print(m.getName() + ";");
        out.print(m.getVorname() + ";");
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
        out.print(formatDate(m.getAustritt()) + ";");
        out.println(formatDate(m.getKuendigung()) + ";");
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
   * @param text
   *          der Text.
   * @return der Text oder Leerstring - niemals null.
   */
  private String formatDate(Date d)
  {
    return d == null ? "" : Einstellungen.DATEFORMAT.format(d);
  }

}
