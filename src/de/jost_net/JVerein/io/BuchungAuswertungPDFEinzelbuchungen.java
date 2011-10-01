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
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;

import de.jost_net.JVerein.Einstellungen;
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

public class BuchungAuswertungPDFEinzelbuchungen
{

  private double summe = 0;

  public BuchungAuswertungPDFEinzelbuchungen(DBIterator list, final File file,
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
      Reporter reporter = new Reporter(fos, monitor, "Buchungsliste", subtitle,
          list.size());

      while (list.hasNext())
      {
        createTableContent(reporter, list, konto, dVon, dBis);
      }
      if (buchungsart.getArt() == -1)
      {
        createTableContent(reporter, null, konto, dVon, dBis);
      }
      createTableHeader(reporter);
      reporter.addColumn("", Element.ALIGN_LEFT);
      reporter.addColumn("", Element.ALIGN_LEFT);
      reporter.addColumn("", Element.ALIGN_LEFT);
      reporter.addColumn("", Element.ALIGN_LEFT);
      reporter.addColumn("Gesamtsumme ", Element.ALIGN_LEFT);
      reporter.addColumn(summe);
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
    reporter.addHeaderColumn("Datum", Element.ALIGN_CENTER, 40,
        Color.LIGHT_GRAY);
    reporter.addHeaderColumn("Auszug", Element.ALIGN_CENTER, 20,
        Color.LIGHT_GRAY);
    reporter.addHeaderColumn("Name", Element.ALIGN_CENTER, 100,
        Color.LIGHT_GRAY);
    reporter.addHeaderColumn("Zahlungsgrund", Element.ALIGN_CENTER, 100,
        Color.LIGHT_GRAY);
    reporter.addHeaderColumn("Zahlungsgrund2", Element.ALIGN_CENTER, 100,
        Color.LIGHT_GRAY);
    reporter.addHeaderColumn("Betrag", Element.ALIGN_CENTER, 50,
        Color.LIGHT_GRAY);
    reporter.createHeader();

  }

  private void createTableContent(Reporter reporter, DBIterator list,
      Konto konto, Date dVon, Date dBis) throws RemoteException,
      DocumentException
  {
    Buchungsart ba = null;
    Paragraph pBuchungsart = null;
    if (list != null)
    {
      ba = (Buchungsart) list.next();
      pBuchungsart = new Paragraph(ba.getBezeichnung(), FontFactory.getFont(
          FontFactory.HELVETICA_BOLD, 10));
    }
    else
    {
      pBuchungsart = new Paragraph("ohne Zuordnung", FontFactory.getFont(
          FontFactory.HELVETICA_BOLD, 10));
    }

    reporter.add(pBuchungsart);
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
    listb.setOrder("ORDER BY datum");
    double buchungsartSumme = 0;
    createTableHeader(reporter);
    boolean gedruckt = false;
    while (listb.hasNext())
    {
      gedruckt = true;
      Buchung b = (Buchung) listb.next();
      reporter.addColumn(new JVDateFormatTTMMJJJJ().format(b.getDatum()),
          Element.ALIGN_LEFT);
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
      reporter.addColumn(b.getZweck(), Element.ALIGN_LEFT);
      reporter.addColumn(b.getZweck2(), Element.ALIGN_LEFT);
      reporter.addColumn(b.getBetrag());
      buchungsartSumme += b.getBetrag();
    }
    if (!gedruckt)
    {
      reporter.addColumn("", Element.ALIGN_LEFT);
      reporter.addColumn("", Element.ALIGN_LEFT);
      reporter.addColumn("keine Buchung", Element.ALIGN_LEFT);
      reporter.addColumn("", Element.ALIGN_LEFT);
      reporter.addColumn("", Element.ALIGN_LEFT);
      reporter.addColumn("", Element.ALIGN_LEFT);
    }
    reporter.addColumn("", Element.ALIGN_LEFT);
    reporter.addColumn("", Element.ALIGN_LEFT);
    reporter.addColumn("", Element.ALIGN_LEFT);
    reporter.addColumn("", Element.ALIGN_LEFT);
    if (list != null)
    {
      reporter.addColumn("Summe " + ba.getBezeichnung(), Element.ALIGN_LEFT);
    }
    else
    {
      reporter.addColumn("Summe ohne Zuordnung", Element.ALIGN_LEFT);
    }
    summe += buchungsartSumme;
    reporter.addColumn(buchungsartSumme);
    reporter.closeTable();
  }

}
