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
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;

import de.jost_net.JVerein.util.Geschaeftsjahr;
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
      final File file, ProgressMonitor monitor, Geschaeftsjahr gj)
      throws ApplicationException, RemoteException
  {
    try
    {
      FileOutputStream fos = new FileOutputStream(file);
      String subtitle = gj.toString();
      Reporter reporter = new Reporter(fos, monitor, "Buchungsklassesaldo",
          subtitle, zeile.size());

      for (BuchungsklasseSaldoZeile bkz : zeile)
      {
        switch (bkz.getStatus())
        {
          case BuchungsklasseSaldoZeile.HEADER:
          {
            reporter.add(new Paragraph((String) bkz
                .getAttribute("buchungsklassenbezeichnung")));
            makeHeader(reporter);
            break;
          }
          case BuchungsklasseSaldoZeile.DETAIL:
          {
            reporter.addColumn((String) bkz
                .getAttribute("buchungsartbezeichnung"), Element.ALIGN_LEFT);
            reporter.addColumn((Double) bkz.getAttribute("einnahmen"));
            reporter.addColumn((Double) bkz.getAttribute("ausgaben"));
            reporter.addColumn((Double) bkz.getAttribute("umbuchungen"));
            break;
          }
          case BuchungsklasseSaldoZeile.FOOTER:
          {
            reporter
                .addColumn((String) bkz
                    .getAttribute("buchungsklassenbezeichnung"),
                    Element.ALIGN_LEFT);
            reporter.addColumn((Double) bkz.getAttribute("einnahmen"));
            reporter.addColumn((Double) bkz.getAttribute("ausgaben"));
            reporter.addColumn((Double) bkz.getAttribute("umbuchungen"));
            reporter.closeTable();
            break;
          }
        }
      }
      monitor.setStatusText("Auswertung fertig.");

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
