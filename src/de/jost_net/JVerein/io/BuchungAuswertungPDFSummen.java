/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * This program is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,  but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See 
 *  the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, 
 * see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de
 * www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Date;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.keys.ArtBuchungsart;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.jost_net.JVerein.rmi.Konto;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.action.Program;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class BuchungAuswertungPDFSummen
{

  private double summe = 0;

  private double summeeinnahmen = 0;

  private double summeausgaben = 0;

  private double summeumbuchungen = 0;

  public BuchungAuswertungPDFSummen(DBIterator list, final File file,
      ProgressMonitor monitor, Konto konto, Buchungsart buchungsart, Date dVon,
      Date dBis) throws ApplicationException
  {
    try
    {
      FileOutputStream fos = new FileOutputStream(file);
      String subtitle = "vom " + new JVDateFormatTTMMJJJJ().format(dVon)
          + " bis " + new JVDateFormatTTMMJJJJ().format(dBis);
      if (konto != null)
      {
        subtitle += " für Konto " + konto.getNummer() + " - "
            + konto.getBezeichnung();
      }
      Reporter reporter = new Reporter(fos, monitor, "Summenliste", subtitle,
          list.size());

      createTableHeader(reporter);
      while (list.hasNext())
      {
        createTableContent(reporter, list, konto, dVon, dBis);
      }
      if (buchungsart.getArt() == -1)
      {
        createTableContent(reporter, null, konto, dVon, dBis);
      }
      monitor.setStatusText("Auswertung fertig. " + list.size() + " Sätze.");
      reporter.addColumn("Summe Einnahmen", Element.ALIGN_LEFT);
      reporter.addColumn(summeeinnahmen);
      reporter.addColumn("Summe Ausgaben", Element.ALIGN_LEFT);
      reporter.addColumn(summeausgaben);
      reporter.addColumn("Summe Umbuchungen", Element.ALIGN_LEFT);
      reporter.addColumn(summeumbuchungen);
      reporter.addColumn("Saldo", Element.ALIGN_LEFT);
      reporter.addColumn(summe);

      reporter.close();
      fos.close();
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
    catch (DocumentException e)
    {
      e.printStackTrace();
      Logger.error("error while creating report", e);
      throw new ApplicationException("Fehler beim Erzeugen des Reports", e);
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

  private void createTableHeader(Reporter reporter) throws DocumentException
  {
    reporter.addHeaderColumn("Buchungsart", Element.ALIGN_CENTER, 200,
        Color.LIGHT_GRAY);
    reporter.addHeaderColumn("Betrag", Element.ALIGN_CENTER, 60,
        Color.LIGHT_GRAY);
    reporter.createHeader();

  }

  private void createTableContent(Reporter reporter, DBIterator list,
      Konto konto, Date dVon, Date dBis) throws RemoteException
  {
    Buchungsart ba = null;
    if (list != null)
    {
      ba = (Buchungsart) list.next();
      reporter.addColumn(ba.getBezeichnung(), Element.ALIGN_LEFT);
    }
    else
    {
      reporter.addColumn("ohne Zuordnung", Element.ALIGN_LEFT);
    }

    DBIterator listb = Einstellungen.getDBService().createList(Buchung.class);
    listb.addFilter("datum >= ?",
        new Object[] { new java.sql.Date(dVon.getTime()) });
    listb.addFilter("datum <= ?",
        new Object[] { new java.sql.Date(dBis.getTime()) });
    if (konto != null)
    {
      listb.addFilter("konto = ?", new Object[] { konto.getID() });
    }
    if (list != null)
    {
      listb.addFilter("buchungsart = ?",
          new Object[] { new Integer(ba.getID()) });
    }
    else
    {
      listb.addFilter("buchungsart is null");
    }
    double buchungsartSumme = 0;
    while (listb.hasNext())
    {
      Buchung b = (Buchung) listb.next();
      buchungsartSumme += b.getBetrag();
      if (ba.getArt() == ArtBuchungsart.EINNAHME)
      {
        summeeinnahmen += b.getBetrag();
      }
      if (ba.getArt() == ArtBuchungsart.AUSGABE)
      {
        summeausgaben += b.getBetrag();
      }
      if (ba.getArt() == ArtBuchungsart.UMBUCHUNG)
      {
        summeumbuchungen += b.getBetrag();
      }
    }
    summe += buchungsartSumme;
    reporter.addColumn(buchungsartSumme);
  }

}
