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
 * Revision 1.7  2010/01/01 20:28:09  jost
 * Konkrete Fehlermeldung, wenn bei der Erstellung einer Altersjubiläumsliste der Eintag in den Stammdaten fehlt.
 *
 * Revision 1.6  2008/12/24 09:17:09  jost
 * Bei AltersjubilÃ¤en wird jetzt das Geburtsdatum anstatt des Eintrittsdatums ausgegeben.
 *
 * Revision 1.5  2008/12/06 16:46:57  jost
 * Debug-Meldung entfernt.
 *
 * Revision 1.4  2008/09/21 08:46:10  jost
 * Neu: AltersjubliÃ¤en
 *
 * Revision 1.3  2008/07/10 07:59:38  jost
 * Optimierung der internen Reporter-Klasse
 *
 * Revision 1.2  2007/12/28 13:14:50  jost
 * Bugfix beim erzeugen eines Stammdaten-Objektes
 *
 * Revision 1.1  2007/12/22 08:26:51  jost
 * Neu: JubilÃ¤enliste
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
import java.util.Date;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Stammdaten;
import de.jost_net.JVerein.server.MitgliedUtils;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.action.Program;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class Jubilaeenliste
{
  public Jubilaeenliste(final File file, ProgressMonitor monitor, Integer jahr,
      String art) throws ApplicationException, RemoteException
  {
    try
    {
      FileOutputStream fos = new FileOutputStream(file);
      Reporter reporter = new Reporter(fos, monitor, art + " " + jahr, "", 3);

      Stammdaten stamm = null;
      try
      {
        DBIterator list = Einstellungen.getDBService().createList(
            Stammdaten.class);
        if (list.size() > 0)
        {
          stamm = (Stammdaten) list.next();
        }
        else
        {
          throw new RemoteException("keine Stammdaten gespeichert");
        }

        if (art.equals(MitgliedControl.JUBELART_MITGLIEDSCHAFT))
        {
          mitgliedschaft(reporter, stamm, jahr);
        }
        else if (art.equals(MitgliedControl.JUBELART_ALTER))
        {
          alter(reporter, stamm, jahr);
        }

        reporter.close();
        fos.close();
      }
      catch (RemoteException e)
      {
        throw new ApplicationException(
            "Keine Stammdaten gespeichert. Bitte erfassen.");
      }
      catch (RuntimeException e)
      {
        throw new ApplicationException(
            "Keine Angaben zu Altersjubiläen in den Stammdaten");
      }
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

  private void mitgliedschaft(Reporter reporter, Stammdaten stamm, int jahr)
      throws RemoteException, RuntimeException, DocumentException
  {
    JubilaeenParser jp = new JubilaeenParser(stamm.getJubilaeen());
    while (jp.hasNext())
    {
      int jubi = jp.getNext();
      Paragraph pHeader = new Paragraph("\n" + jubi + "-jähriges Jubiläum",
          FontFactory.getFont(FontFactory.HELVETICA, 11));
      reporter.add(pHeader);

      addHeader(MitgliedControl.JUBELART_MITGLIEDSCHAFT, reporter);

      DBIterator mitgl = Einstellungen.getDBService()
          .createList(Mitglied.class);
      MitgliedUtils.setNurAktive(mitgl);
      Calendar cal = Calendar.getInstance();
      cal.set(Calendar.YEAR, jahr);
      cal.add(Calendar.YEAR, jubi * -1);
      cal.set(Calendar.MONTH, Calendar.JANUARY);
      cal.set(Calendar.DAY_OF_MONTH, 1);
      Date von = cal.getTime();
      mitgl.addFilter("eintritt >= ?", new Object[] { new java.sql.Date(von
          .getTime()) });

      cal.set(Calendar.MONTH, Calendar.DECEMBER);
      cal.set(Calendar.DAY_OF_MONTH, 31);
      Date bis = cal.getTime();
      mitgl.addFilter("eintritt <= ?", new Object[] { new java.sql.Date(bis
          .getTime()) });
      mitgl.setOrder("order by eintritt");

      while (mitgl.hasNext())
      {
        Mitglied m = (Mitglied) mitgl.next();
        addDetail(MitgliedControl.JUBELART_MITGLIEDSCHAFT, reporter, m);
      }
      if (mitgl.size() == 0)
      {
        reporter.addColumn("", Element.ALIGN_LEFT);
        reporter.addColumn("kein Mitglied", Element.ALIGN_LEFT);
        reporter.addColumn("", Element.ALIGN_LEFT);
        reporter.addColumn("", Element.ALIGN_LEFT);
      }
      reporter.closeTable();
    }

  }

  private void alter(Reporter reporter, Stammdaten stamm, int jahr)
      throws RemoteException, RuntimeException, DocumentException
  {
    JubilaeenParser jp = new JubilaeenParser(stamm.getAltersjubilaeen());
    while (jp.hasNext())
    {
      int jubi = jp.getNext();
      Paragraph pHeader = new Paragraph("\n" + jubi + ". Geburtstag",
          FontFactory.getFont(FontFactory.HELVETICA, 11));
      reporter.add(pHeader);

      addHeader(MitgliedControl.JUBELART_ALTER, reporter);

      DBIterator mitgl = Einstellungen.getDBService()
          .createList(Mitglied.class);
     MitgliedUtils.setNurAktive(mitgl);
      Calendar cal = Calendar.getInstance();
      cal.set(Calendar.YEAR, jahr);
      cal.add(Calendar.YEAR, jubi * -1);
      cal.set(Calendar.MONTH, Calendar.JANUARY);
      cal.set(Calendar.DAY_OF_MONTH, 1);
      Date von = cal.getTime();
      mitgl.addFilter("geburtsdatum >= ?", new Object[] { new java.sql.Date(von
          .getTime()) });

      cal.set(Calendar.MONTH, Calendar.DECEMBER);
      cal.set(Calendar.DAY_OF_MONTH, 31);
      Date bis = cal.getTime();
      mitgl.addFilter("geburtsdatum <= ?", new Object[] { new java.sql.Date(bis
          .getTime()) });
      mitgl.setOrder("order by geburtsdatum");

      while (mitgl.hasNext())
      {
        Mitglied m = (Mitglied) mitgl.next();
        addDetail(MitgliedControl.JUBELART_ALTER, reporter, m);
      }
      if (mitgl.size() == 0)
      {
        reporter.addColumn("", Element.ALIGN_LEFT);
        reporter.addColumn("kein Mitglied", Element.ALIGN_LEFT);
        reporter.addColumn("", Element.ALIGN_LEFT);
        reporter.addColumn("", Element.ALIGN_LEFT);
      }
      reporter.closeTable();
    }

  }

  private void addHeader(String art, Reporter reporter)
      throws DocumentException
  {
    if (art.equals(MitgliedControl.JUBELART_MITGLIEDSCHAFT))
    {
      reporter.addHeaderColumn("Eintrittsdatum", Element.ALIGN_CENTER, 50,
          Color.LIGHT_GRAY);
    }
    if (art.equals(MitgliedControl.JUBELART_ALTER))
    {
      reporter.addHeaderColumn("Geburtsdatum", Element.ALIGN_CENTER, 50,
          Color.LIGHT_GRAY);
    }

    reporter.addHeaderColumn("Name, Vorname", Element.ALIGN_CENTER, 100,
        Color.LIGHT_GRAY);
    reporter.addHeaderColumn("Anschrift", Element.ALIGN_CENTER, 120,
        Color.LIGHT_GRAY);
    reporter.addHeaderColumn("Kommunikation", Element.ALIGN_CENTER, 80,
        Color.LIGHT_GRAY);
    reporter.createHeader();

  }

  private void addDetail(String art, Reporter reporter, Mitglied m)
      throws RemoteException
  {
    if (art.equals(MitgliedControl.JUBELART_MITGLIEDSCHAFT))
    {
      reporter.addColumn(m.getEintritt(), Element.ALIGN_LEFT);
    }
    if (art.equals(MitgliedControl.JUBELART_ALTER))
    {
      reporter.addColumn(m.getGeburtsdatum(), Element.ALIGN_LEFT);
    }
    reporter.addColumn(m.getNameVorname(), Element.ALIGN_LEFT);
    reporter.addColumn(m.getAnschrift(), Element.ALIGN_LEFT);
    String kommunikation = m.getTelefonprivat();
    if (kommunikation.length() > 0 && m.getTelefondienstlich().length() > 0)
    {
      kommunikation += ", ";
    }
    kommunikation += m.getTelefondienstlich();
    if (kommunikation.length() > 0 && m.getEmail().length() > 0)
    {
      kommunikation += ", ";
    }
    kommunikation += m.getEmail();
    reporter.addColumn(kommunikation, Element.ALIGN_LEFT);

  }
}
