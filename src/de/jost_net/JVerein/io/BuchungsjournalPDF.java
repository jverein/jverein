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
 * Revision 1.3  2010-10-15 09:58:28  jost
 * Code aufgeräumt
 *
 * Revision 1.2  2009-09-15 19:22:36  jost
 * Summenbildung.
 *
 * Revision 1.1  2009/09/12 19:04:44  jost
 * neu: Buchungsjournal
 *
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.keys.ArtBuchungsart;
import de.jost_net.JVerein.rmi.Buchung;
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

public class BuchungsjournalPDF
{

  public BuchungsjournalPDF(DBIterator list, final File file,
      ProgressMonitor monitor, Konto konto, Date dVon, Date dBis)
      throws ApplicationException
  {
    try
    {
      FileOutputStream fos = new FileOutputStream(file);
      String subtitle = "vom " + new JVDateFormatTTMMJJJJ().format(dVon) + " bis "
          + new JVDateFormatTTMMJJJJ().format(dBis);
      if (konto != null)
      {
        subtitle += " für Konto " + konto.getNummer() + " - "
            + konto.getBezeichnung();
      }
      Reporter reporter = new Reporter(fos, monitor, "Buchungsjournal",
          subtitle, list.size());

      double einnahmen = 0;
      double ausgaben = 0;
      double umbuchungen = 0;
      double nichtzugeordnet = 0;

      createTableHeader(reporter);

      while (list.hasNext())
      {
        Buchung b = (Buchung) list.next();
        DBIterator listk = Einstellungen.getDBService().createList(Konto.class);
        listk.addFilter("id = ?", new Object[] { b.getKonto().getID() });
        Konto k = (Konto) listk.next();
        reporter.addColumn(b.getID(), Element.ALIGN_RIGHT);
        reporter.addColumn(new JVDateFormatTTMMJJJJ().format(b.getDatum()),
            Element.ALIGN_LEFT);
        reporter.addColumn(k.getNummer(), Element.ALIGN_RIGHT);
        if (b.getAuszugsnummer() != null)
        {
          reporter.addColumn(b.getAuszugsnummer() + "/" + b.getBlattnummer(),
              Element.ALIGN_LEFT);
        }
        else
        {
          reporter.addColumn("", Element.ALIGN_LEFT);
        }
        reporter.addColumn(b.getName(), Element.ALIGN_LEFT);
        reporter
            .addColumn(
                b.getZweck()
                    + (b.getZweck2() != null ? (" " + b.getZweck2()) : ""),
                Element.ALIGN_LEFT);
        reporter.addColumn(b.getBuchungsart() != null ? b.getBuchungsart()
            .getBezeichnung() : "", Element.ALIGN_LEFT);
        reporter.addColumn(b.getBetrag());
        if (b.getBuchungsart() != null)
        {
          int buchungsartart = b.getBuchungsart().getArt();
          switch (buchungsartart)
          {
            case ArtBuchungsart.EINNAHME:
            {
              einnahmen += b.getBetrag();
              break;
            }
            case ArtBuchungsart.AUSGABE:
            {
              ausgaben += b.getBetrag();
              break;
            }
            case ArtBuchungsart.UMBUCHUNG:
            {
              umbuchungen += b.getBetrag();
              break;
            }
          }
        }
        else
        {
          nichtzugeordnet += b.getBetrag();
        }

      }

      for (int i = 0; i < 5; i++)
      {
        reporter.addColumn("", Element.ALIGN_LEFT);
      }
      reporter.addColumn("Summe Einnahmen", Element.ALIGN_LEFT);
      reporter.addColumn("", Element.ALIGN_LEFT);
      reporter.addColumn(einnahmen);

      for (int i = 0; i < 5; i++)
      {
        reporter.addColumn("", Element.ALIGN_LEFT);
      }
      reporter.addColumn("Summe Ausgaben", Element.ALIGN_LEFT);
      reporter.addColumn("", Element.ALIGN_LEFT);
      reporter.addColumn(ausgaben);

      for (int i = 0; i < 5; i++)
      {
        reporter.addColumn("", Element.ALIGN_LEFT);
      }
      reporter.addColumn("Summe Umbuchungen", Element.ALIGN_LEFT);
      reporter.addColumn("", Element.ALIGN_LEFT);
      reporter.addColumn(umbuchungen);

      if (nichtzugeordnet != 0)
      {
        for (int i = 0; i < 5; i++)
        {
          reporter.addColumn("", Element.ALIGN_LEFT);
        }
        reporter.addColumn("Summe nicht zugeordnet", Element.ALIGN_LEFT);
        reporter.addColumn("", Element.ALIGN_LEFT);
        reporter.addColumn(nichtzugeordnet);
      }

      reporter.closeTable();
      monitor.setStatusText("Auswertung fertig. " + list.size() + " Sätze.");

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
    reporter.addHeaderColumn("Nr", Element.ALIGN_RIGHT, 20, Color.LIGHT_GRAY);
    reporter.addHeaderColumn("Datum", Element.ALIGN_CENTER, 45,
        Color.LIGHT_GRAY);
    reporter.addHeaderColumn("Konto", Element.ALIGN_CENTER, 40,
        Color.LIGHT_GRAY);
    reporter.addHeaderColumn("Auszug", Element.ALIGN_CENTER, 20,
        Color.LIGHT_GRAY);
    reporter.addHeaderColumn("Name", Element.ALIGN_CENTER, 100,
        Color.LIGHT_GRAY);
    reporter.addHeaderColumn("Zahlungsgrund", Element.ALIGN_CENTER, 100,
        Color.LIGHT_GRAY);
    reporter.addHeaderColumn("Buchungsart", Element.ALIGN_CENTER, 60,
        Color.LIGHT_GRAY);
    reporter.addHeaderColumn("Betrag", Element.ALIGN_CENTER, 50,
        Color.LIGHT_GRAY);
    reporter.createHeader();

  }
}
