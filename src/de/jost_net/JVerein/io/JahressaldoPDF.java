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

import de.jost_net.JVerein.util.Geschaeftsjahr;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.action.Program;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class JahressaldoPDF
{
  public JahressaldoPDF(ArrayList<SaldoZeile> zeile, final File file,
      ProgressMonitor monitor, Geschaeftsjahr gj) throws ApplicationException,
      RemoteException
  {
    try
    {
      FileOutputStream fos = new FileOutputStream(file);
      String subtitle = gj.toString();
      Reporter reporter = new Reporter(fos, monitor, "Jahressaldo", subtitle,
          zeile.size());

      reporter.addHeaderColumn("Konto-\nnummer", Element.ALIGN_CENTER, 50,
          Color.LIGHT_GRAY);
      reporter.addHeaderColumn("Kontobezeichnung", Element.ALIGN_CENTER, 80,
          Color.LIGHT_GRAY);
      reporter.addHeaderColumn("Anfangs-\nbestand", Element.ALIGN_CENTER, 50,
          Color.LIGHT_GRAY);
      reporter.addHeaderColumn("Einnahmen", Element.ALIGN_CENTER, 50,
          Color.LIGHT_GRAY);
      reporter.addHeaderColumn("Ausgaben", Element.ALIGN_CENTER, 50,
          Color.LIGHT_GRAY);
      reporter.addHeaderColumn("Um-\nbuchungen", Element.ALIGN_CENTER, 50,
          Color.LIGHT_GRAY);
      reporter.addHeaderColumn("Endbestand", Element.ALIGN_CENTER, 50,
          Color.LIGHT_GRAY);
      reporter.addHeaderColumn("Bemerkung", Element.ALIGN_CENTER, 100,
          Color.LIGHT_GRAY);
      reporter.createHeader();

      for (SaldoZeile sz : zeile)
      {
        reporter.addColumn(reporter.getDetailCell((String) sz
            .getAttribute("kontonummer"), Element.ALIGN_LEFT));
        reporter.addColumn(reporter.getDetailCell((String) sz
            .getAttribute("kontobezeichnung"), Element.ALIGN_LEFT));
        reporter.addColumn(reporter.getDetailCell((Double) sz
            .getAttribute("anfangsbestand")));
        reporter.addColumn(reporter.getDetailCell((Double) sz
            .getAttribute("einnahmen")));
        reporter.addColumn(reporter.getDetailCell((Double) sz
            .getAttribute("ausgaben")));
        reporter.addColumn(reporter.getDetailCell((Double) sz
            .getAttribute("umbuchungen")));
        reporter.addColumn(reporter.getDetailCell((Double) sz
            .getAttribute("endbestand")));
        reporter.addColumn(reporter.getDetailCell((String) sz
            .getAttribute("bemerkung"), Element.ALIGN_LEFT));
      }
      reporter.closeTable();
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

}
