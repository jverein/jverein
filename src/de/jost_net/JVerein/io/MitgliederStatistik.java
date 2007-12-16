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
 * Revision 1.3  2007/02/23 20:28:04  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.2  2007/02/02 19:40:51  jost
 * Bugfix: Nur aktive Mitglieder
 *
 * Revision 1.1  2006/10/29 07:50:08  jost
 * Neu: Mitgliederstatistik
 *
 *
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Calendar;

import org.eclipse.swt.graphics.Point;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Stammdaten;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.action.Program;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class MitgliederStatistik
{
  public MitgliederStatistik(final File file, ProgressMonitor monitor)
      throws ApplicationException, RemoteException
  {
    try
    {
      FileOutputStream fos = new FileOutputStream(file);
      Reporter reporter = new Reporter(fos, monitor, "Mitgliederstatistik", "",
          3);

      Paragraph pAltersgruppen = new Paragraph("\nAltersgruppen", FontFactory
          .getFont(FontFactory.HELVETICA, 11));
      reporter.add(pAltersgruppen);

      reporter.addHeaderColumn("Altersgruppe", Element.ALIGN_CENTER, 100,
          Color.LIGHT_GRAY);
      reporter.addHeaderColumn("Insgesamt", Element.ALIGN_CENTER, 30,
          Color.LIGHT_GRAY);
      reporter.addHeaderColumn("männlich", Element.ALIGN_CENTER, 30,
          Color.LIGHT_GRAY);
      reporter.addHeaderColumn("weiblich", Element.ALIGN_CENTER, 30,
          Color.LIGHT_GRAY);
      reporter.createHeader();

      DBIterator stammd = Einstellungen.getDBService().createList(
          Stammdaten.class);
      Stammdaten stamm = (Stammdaten) stammd.next();

      AltersgruppenParser ap = new AltersgruppenParser(stamm.getAltersgruppen());
      while (ap.hasNext())
      {
        Point p = ap.getNext();
        addAltersgruppe(reporter, p.x, p.y);
      }
      addAltersgruppe(reporter, 0, 100);
      reporter.closeTable();

      Paragraph pBeitragsgruppen = new Paragraph("\nBeitragsgruppen",
          FontFactory.getFont(FontFactory.HELVETICA, 11));
      reporter.add(pBeitragsgruppen);

      reporter.addHeaderColumn("Beitragsgruppe", Element.ALIGN_CENTER, 100,
          Color.LIGHT_GRAY);
      reporter.addHeaderColumn("Insgesamt", Element.ALIGN_CENTER, 30,
          Color.LIGHT_GRAY);
      reporter.addHeaderColumn("männlich", Element.ALIGN_CENTER, 30,
          Color.LIGHT_GRAY);
      reporter.addHeaderColumn("weiblich", Element.ALIGN_CENTER, 30,
          Color.LIGHT_GRAY);
      reporter.createHeader();

      DBIterator beitragsgruppen = Einstellungen.getDBService().createList(
          Beitragsgruppe.class);
      while (beitragsgruppen.hasNext())
      {
        Beitragsgruppe bg = (Beitragsgruppe) beitragsgruppen.next();
        addBeitragsgruppe(reporter, bg);
      }
      addBeitragsgruppe(reporter, null);

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

  private void addAltersgruppe(Reporter reporter, int von, int bis)
      throws RemoteException
  {
    if (von == 0 && bis == 100)
    {
      reporter.addColumn(reporter
          .getDetailCell("Insgesamt", Element.ALIGN_LEFT));
    }
    else
    {
      reporter.addColumn(reporter.getDetailCell("Altersgruppe " + von + "-"
          + bis, Element.ALIGN_LEFT));
    }
    reporter.addColumn(reporter.getDetailCell(getAltersgruppe(von, bis, null)
        + "", Element.ALIGN_RIGHT));
    reporter.addColumn(reporter.getDetailCell(getAltersgruppe(von, bis, "m")
        + "", Element.ALIGN_RIGHT));
    reporter.addColumn(reporter.getDetailCell(getAltersgruppe(von, bis, "w")
        + "", Element.ALIGN_RIGHT));

  }

  private void addBeitragsgruppe(Reporter reporter, Beitragsgruppe bg)
      throws RemoteException
  {
    if (bg == null)
    {
      reporter.addColumn(reporter
          .getDetailCell("Insgesamt", Element.ALIGN_LEFT));
    }
    else
    {
      reporter.addColumn(reporter.getDetailCell(bg.getBezeichnung(),
          Element.ALIGN_LEFT));
    }
    reporter.addColumn(reporter.getDetailCell(getBeitragsgruppe(bg, null) + "",
        Element.ALIGN_RIGHT));
    reporter.addColumn(reporter.getDetailCell(getBeitragsgruppe(bg, "m") + "",
        Element.ALIGN_RIGHT));
    reporter.addColumn(reporter.getDetailCell(getBeitragsgruppe(bg, "w") + "",
        Element.ALIGN_RIGHT));

  }

  /**
   * Anzahl der Mitglieder in einer Altersgruppe ermitteln
   * 
   * @param von
   *          Alter in Jahren
   * @param bis
   *          Alter in Jahren
   * @param geschlecht
   *          m, w oder null
   * @return Anzahl der Mitglieder
   */
  private int getAltersgruppe(int von, int bis, String geschlecht)
      throws RemoteException
  {
    Calendar calVon = Calendar.getInstance();
    calVon.add(Calendar.YEAR, bis * -1);
    calVon.set(Calendar.MONTH, Calendar.JANUARY);
    calVon.set(Calendar.DAY_OF_MONTH, 1);
    calVon.set(Calendar.HOUR, 0);
    calVon.set(Calendar.MINUTE, 0);
    calVon.set(Calendar.SECOND, 0);
    calVon.set(Calendar.MILLISECOND, 0);
    java.sql.Date vd = new java.sql.Date(calVon.getTimeInMillis());

    Calendar calBis = Calendar.getInstance();
    calBis.add(Calendar.YEAR, von * -1);
    calBis.set(Calendar.MONTH, Calendar.DECEMBER);
    calBis.set(Calendar.DAY_OF_MONTH, 31);
    java.sql.Date bd = new java.sql.Date(calBis.getTimeInMillis());

    DBIterator list;
    list = Einstellungen.getDBService().createList(Mitglied.class);
    list.addFilter("geburtsdatum >= ?", new Object[] { vd });
    list.addFilter("geburtsdatum <= ?", new Object[] { bd });
    list.addFilter("austritt is null");

    if (geschlecht != null)
    {
      list.addFilter("geschlecht = ?", new Object[] { geschlecht });
    }

    return list.size();
  }

  private int getBeitragsgruppe(Beitragsgruppe bg, String geschlecht)
      throws RemoteException
  {
    DBIterator list = Einstellungen.getDBService().createList(Mitglied.class);
    list.addFilter("austritt is null");
    if (bg != null)
    {
      list.addFilter("beitragsgruppe = ?", new Object[] { new Integer(bg
          .getID()) });
    }
    if (geschlecht != null)
    {
      list.addFilter("geschlecht = ?", new Object[] { geschlecht });
    }

    return list.size();
  }

}
