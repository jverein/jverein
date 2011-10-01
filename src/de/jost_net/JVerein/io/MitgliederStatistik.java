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
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.graphics.Point;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.server.MitgliedUtils;
import de.jost_net.JVerein.util.Geschaeftsjahr;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
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

  public MitgliederStatistik(final File file, ProgressMonitor monitor,
      Date stichtag) throws ApplicationException
  {
    try
    {
      FileOutputStream fos = new FileOutputStream(file);
      String subtitle = "";
      if (stichtag != null)
      {
        subtitle = "Stichtag: " + new JVDateFormatTTMMJJJJ().format(stichtag);
      }
      Reporter reporter = new Reporter(fos, monitor, "Mitgliederstatistik",
          subtitle, 3);

      Paragraph pAltersgruppen = new Paragraph("\nAltersgruppen",
          FontFactory.getFont(FontFactory.HELVETICA, 11));
      reporter.add(pAltersgruppen);

      reporter.addHeaderColumn("Altersgruppe", Element.ALIGN_CENTER, 100,
          Color.LIGHT_GRAY);
      reporter.addHeaderColumn("Insgesamt", Element.ALIGN_CENTER, 30,
          Color.LIGHT_GRAY);
      reporter.addHeaderColumn("männlich", Element.ALIGN_CENTER, 30,
          Color.LIGHT_GRAY);
      reporter.addHeaderColumn("weiblich", Element.ALIGN_CENTER, 30,
          Color.LIGHT_GRAY);
      reporter.createHeader(60f, Element.ALIGN_LEFT);

      AltersgruppenParser ap = new AltersgruppenParser(Einstellungen
          .getEinstellung().getAltersgruppen());
      while (ap.hasNext())
      {
        Point p = ap.getNext();
        addAltersgruppe(reporter, p.x, p.y, stichtag);
      }
      addAltersgruppe(reporter, 0, 199, stichtag);
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
      reporter.createHeader(60f, Element.ALIGN_LEFT);

      DBIterator beitragsgruppen = Einstellungen.getDBService().createList(
          Beitragsgruppe.class);
      while (beitragsgruppen.hasNext())
      {
        Beitragsgruppe bg = (Beitragsgruppe) beitragsgruppen.next();
        addBeitragsgruppe(reporter, bg, stichtag);
      }
      addBeitragsgruppe(reporter, null, stichtag);
      reporter.closeTable();

      try
      {
        JVDateFormatTTMMJJJJ ttmmjj = new JVDateFormatTTMMJJJJ();
        Geschaeftsjahr gj = new Geschaeftsjahr(stichtag);
        Paragraph pGuV = new Paragraph("\nAnmeldungen/Abmeldungen ("
            + ttmmjj.format(gj.getBeginnGeschaeftsjahr()) + "-"
            + ttmmjj.format(gj.getEndeGeschaeftsjahr()) + ")",
            FontFactory.getFont(FontFactory.HELVETICA, 11));
        reporter.add(pGuV);
        reporter.addHeaderColumn("Text", Element.ALIGN_CENTER, 100,
            Color.LIGHT_GRAY);
        reporter.addHeaderColumn("Anzahl", Element.ALIGN_CENTER, 30,
            Color.LIGHT_GRAY);
        reporter.createHeader(60f, Element.ALIGN_LEFT);
        reporter.addColumn("Anmeldungen", Element.ALIGN_LEFT);
        reporter.addColumn(getAnmeldungen(gj) + "", Element.ALIGN_RIGHT);
        reporter.addColumn("Abmeldungen", Element.ALIGN_LEFT);
        reporter.addColumn(getAbmeldungen(gj) + "", Element.ALIGN_RIGHT);
        reporter.closeTable();
      }
      catch (ParseException e)
      {
        Logger.error("Fehler", e);
        throw new ApplicationException(e);
      }
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

  private void addAltersgruppe(Reporter reporter, int von, int bis,
      Date stichtag) throws RemoteException
  {
    if (von == 0 && bis == 199)
    {
      reporter.addColumn("Insgesamt", Element.ALIGN_LEFT);
    }
    else
    {
      reporter.addColumn("Altersgruppe " + von + "-" + bis, Element.ALIGN_LEFT);
    }
    reporter.addColumn(getAltersgruppe(von, bis, null, stichtag) + "",
        Element.ALIGN_RIGHT);
    reporter.addColumn(getAltersgruppe(von, bis, "m", stichtag) + "",
        Element.ALIGN_RIGHT);
    reporter.addColumn(getAltersgruppe(von, bis, "w", stichtag) + "",
        Element.ALIGN_RIGHT);
  }

  private void addBeitragsgruppe(Reporter reporter, Beitragsgruppe bg,
      Date stichtag) throws RemoteException
  {
    if (bg == null)
    {
      reporter.addColumn("Insgesamt", Element.ALIGN_LEFT);
    }
    else
    {
      reporter.addColumn(bg.getBezeichnung(), Element.ALIGN_LEFT);
    }
    reporter.addColumn(getBeitragsgruppe(bg, null, stichtag) + "",
        Element.ALIGN_RIGHT);
    reporter.addColumn(getBeitragsgruppe(bg, "m", stichtag) + "",
        Element.ALIGN_RIGHT);
    reporter.addColumn(getBeitragsgruppe(bg, "w", stichtag) + "",
        Element.ALIGN_RIGHT);

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
  private int getAltersgruppe(int von, int bis, String geschlecht, Date stichtag)
      throws RemoteException
  {
    Calendar calVon = Calendar.getInstance();
    calVon.setTime(stichtag);
    calVon.add(Calendar.YEAR, bis * -1);
    calVon.set(Calendar.MONTH, Calendar.JANUARY);
    calVon.set(Calendar.DAY_OF_MONTH, 1);
    calVon.set(Calendar.HOUR, 0);
    calVon.set(Calendar.MINUTE, 0);
    calVon.set(Calendar.SECOND, 0);
    calVon.set(Calendar.MILLISECOND, 0);
    java.sql.Date vd = new java.sql.Date(calVon.getTimeInMillis());

    Calendar calBis = Calendar.getInstance();
    calBis.setTime(stichtag);
    calBis.add(Calendar.YEAR, von * -1);
    calBis.set(Calendar.MONTH, Calendar.DECEMBER);
    calBis.set(Calendar.DAY_OF_MONTH, 31);

    DBIterator list = Einstellungen.getDBService().createList(Mitglied.class);
    list.addFilter("geburtsdatum >= ?", new Object[] { vd });
    list.addFilter("geburtsdatum <= ?",
        new Object[] { new java.sql.Date(calBis.getTimeInMillis()) });
    MitgliedUtils.setNurAktive(list, stichtag);
    MitgliedUtils.setMitglied(list);
    list.addFilter("(eintritt is null or eintritt <= ?)",
        new Object[] { stichtag });

    if (geschlecht != null)
    {
      list.addFilter("geschlecht = ?", new Object[] { geschlecht });
    }

    return list.size();
  }

  private int getBeitragsgruppe(Beitragsgruppe bg, String geschlecht,
      Date stichtag) throws RemoteException
  {
    DBIterator list = Einstellungen.getDBService().createList(Mitglied.class);
    MitgliedUtils.setNurAktive(list, stichtag);
    MitgliedUtils.setMitglied(list);
    list.addFilter("(eintritt is null or eintritt <= ?)",
        new Object[] { stichtag });
    if (bg != null)
    {
      list.addFilter("beitragsgruppe = ?",
          new Object[] { new Integer(bg.getID()) });
    }
    if (geschlecht != null)
    {
      list.addFilter("geschlecht = ?", new Object[] { geschlecht });
    }

    return list.size();
  }

  private int getAnmeldungen(Geschaeftsjahr gj) throws RemoteException
  {
    DBIterator list = Einstellungen.getDBService().createList(Mitglied.class);
    MitgliedUtils.setMitglied(list);
    list.addFilter("eintritt >= ? ",
        new Object[] { gj.getBeginnGeschaeftsjahr() });
    list.addFilter("eintritt <= ? ",
        new Object[] { gj.getEndeGeschaeftsjahr() });
    return list.size();
  }

  private int getAbmeldungen(Geschaeftsjahr gj) throws RemoteException
  {
    DBIterator list = Einstellungen.getDBService().createList(Mitglied.class);
    MitgliedUtils.setMitglied(list);
    list.addFilter("austritt >= ? ",
        new Object[] { gj.getBeginnGeschaeftsjahr() });
    list.addFilter("austritt <= ? ",
        new Object[] { gj.getEndeGeschaeftsjahr() });
    return list.size();
  }

}
