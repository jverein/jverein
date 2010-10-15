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
 * Revision 1.5  2010-05-02 06:09:32  jost
 * Redakt. Änderung
 *
 * Revision 1.4  2010/02/23 21:16:13  jost
 * Individueller Zeitraum
 *
 * Revision 1.3  2009/09/19 16:28:38  jost
 * Weiterentwicklung
 *
 * Revision 1.2  2009/09/15 19:24:12  jost
 * Saldo-Bildung
 *
 * Revision 1.1  2009/09/12 19:05:02  jost
 * neu: Buchungsklassen
 *
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;

import de.jost_net.JVerein.Einstellungen;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.action.Program;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class BuchungsklassesaldoPDF
{

  public BuchungsklassesaldoPDF(ArrayList<BuchungsklasseSaldoZeile> zeile,
      final File file, ProgressMonitor monitor, Date datumvon, Date datumbis)
      throws ApplicationException
  {
    try
    {
      FileOutputStream fos = new FileOutputStream(file);
      String subtitle = Einstellungen.DATEFORMAT.format(datumvon) + " - "
          + Einstellungen.DATEFORMAT.format(datumbis);
      Reporter reporter = new Reporter(fos, monitor, "Buchungsklassen-Saldo",
          subtitle, zeile.size());
      makeHeader(reporter);

      for (BuchungsklasseSaldoZeile bkz : zeile)
      {
        switch (bkz.getStatus())
        {
          case BuchungsklasseSaldoZeile.HEADER:
          {
            reporter.addColumn(
                (String) bkz.getAttribute("buchungsklassenbezeichnung"),
                Element.ALIGN_LEFT, new Color(220, 220, 220), 4);
            break;
          }
          case BuchungsklasseSaldoZeile.DETAIL:
          {
            reporter.addColumn(
                (String) bkz.getAttribute("buchungsartbezeichnung"),
                Element.ALIGN_LEFT);
            reporter.addColumn((Double) bkz.getAttribute("einnahmen"));
            reporter.addColumn((Double) bkz.getAttribute("ausgaben"));
            reporter.addColumn((Double) bkz.getAttribute("umbuchungen"));
            break;
          }
          case BuchungsklasseSaldoZeile.SALDOFOOTER:
          {
            reporter.addColumn(
                (String) bkz.getAttribute("buchungsklassenbezeichnung"),
                Element.ALIGN_RIGHT);
            reporter.addColumn((Double) bkz.getAttribute("einnahmen"));
            reporter.addColumn((Double) bkz.getAttribute("ausgaben"));
            reporter.addColumn((Double) bkz.getAttribute("umbuchungen"));
            break;
          }
          case BuchungsklasseSaldoZeile.GESAMTSALDOFOOTER:
          {
            reporter.addColumn("Gesamt", Element.ALIGN_LEFT, 4);
            reporter.addColumn(
                (String) bkz.getAttribute("buchungsklassenbezeichnung"),
                Element.ALIGN_RIGHT);
            reporter.addColumn((Double) bkz.getAttribute("einnahmen"));
            reporter.addColumn((Double) bkz.getAttribute("ausgaben"));
            reporter.addColumn((Double) bkz.getAttribute("umbuchungen"));
            break;
          }
          case BuchungsklasseSaldoZeile.GESAMTGEWINNVERLUST:
          case BuchungsklasseSaldoZeile.SALDOGEWINNVERLUST:
          {
            reporter.addColumn(
                (String) bkz.getAttribute("buchungsklassenbezeichnung"),
                Element.ALIGN_RIGHT);
            reporter.addColumn((Double) bkz.getAttribute("einnahmen"));
            reporter.addColumn("", Element.ALIGN_LEFT, 2);
            break;
          }
          case BuchungsklasseSaldoZeile.NICHTZUGEORDNETEBUCHUNGEN:
          {
            reporter.addColumn(
                (String) bkz.getAttribute("buchungsklassenbezeichnung"),
                Element.ALIGN_LEFT);
            reporter.addColumn((Integer) bkz.getAttribute("anzahlbuchungen"));
            reporter.addColumn("", Element.ALIGN_LEFT, 2);
            break;
          }
        }
      }
      monitor.setStatusText("Auswertung fertig.");
      reporter.closeTable();
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

  private void makeHeader(Reporter reporter) throws DocumentException
  {
    reporter.addHeaderColumn("Buchungsart", Element.ALIGN_CENTER, 90,
        Color.LIGHT_GRAY);
    reporter.addHeaderColumn("Einnahmen", Element.ALIGN_CENTER, 45,
        Color.LIGHT_GRAY);
    reporter.addHeaderColumn("Ausgaben", Element.ALIGN_CENTER, 45,
        Color.LIGHT_GRAY);
    reporter.addHeaderColumn("Umbuchungen", Element.ALIGN_CENTER, 45,
        Color.LIGHT_GRAY);
    reporter.createHeader();

  }

}
