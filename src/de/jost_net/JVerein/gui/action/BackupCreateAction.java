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
 * Revision 1.2  2008/11/29 13:04:57  jost
 * Refactoring: Warnungen beseitigt.
 *
 * Revision 1.1  2008/09/29 14:42:28  jost
 * Neu: Backup und Restore im XML-Format
 *
 **********************************************************************/

package de.jost_net.JVerein.gui.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.server.AbrechnungImpl;
import de.jost_net.JVerein.server.AnfangsbestandImpl;
import de.jost_net.JVerein.server.BeitragsgruppeImpl;
import de.jost_net.JVerein.server.BuchungImpl;
import de.jost_net.JVerein.server.BuchungsartImpl;
import de.jost_net.JVerein.server.EigenschaftenImpl;
import de.jost_net.JVerein.server.FelddefinitionImpl;
import de.jost_net.JVerein.server.FormularImpl;
import de.jost_net.JVerein.server.FormularfeldImpl;
import de.jost_net.JVerein.server.JahresabschlussImpl;
import de.jost_net.JVerein.server.KontoImpl;
import de.jost_net.JVerein.server.KursteilnehmerImpl;
import de.jost_net.JVerein.server.ManuellerZahlungseingangImpl;
import de.jost_net.JVerein.server.MitgliedImpl;
import de.jost_net.JVerein.server.SpendenbescheinigungImpl;
import de.jost_net.JVerein.server.StammdatenImpl;
import de.jost_net.JVerein.server.WiedervorlageImpl;
import de.jost_net.JVerein.server.ZusatzbetragImpl;
import de.jost_net.JVerein.server.ZusatzfelderImpl;
import de.willuhn.datasource.BeanUtil;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.serialize.Writer;
import de.willuhn.datasource.serialize.XmlWriter;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

/**
 * Action zum Erstellen eines Komplett-Backups im XML-Format.
 */
public class BackupCreateAction implements Action
{
  /**
   * Dateformat, welches fuer den Dateinamen genutzt wird.
   */
  public static DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");

  /**
   * @see de.willuhn.jameica.gui.Action#handleAction(java.lang.Object)
   */
  public void handleAction(Object context) throws ApplicationException
  {
    FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
    fd.setFilterPath(System.getProperty("user.home"));
    fd.setFileName("jverein-backup-" + DATEFORMAT.format(new Date()) + ".xml");
    fd.setFilterExtensions(new String[] { "*.xml" });
    fd
        .setText("Bitte wählen Sie die Datei, in der das Backup gespeichert wird");
    String f = fd.open();
    if (f == null || f.length() == 0)
      return;

    final File file = new File(f);
    try
    {
      if (file.exists()
          && !Application.getCallback().askUser(
              "Datei existiert bereits. Überschreiben?"))
        return;
    }
    catch (ApplicationException ae)
    {
      throw ae;
    }
    catch (Exception e)
    {
      Logger.error("error while asking user", e);
      Application.getMessagingFactory().sendMessage(
          new StatusBarMessage("Fehler beim Erstellen der Backup-Datei",
              StatusBarMessage.TYPE_ERROR));
      return;
    }

    Application.getController().start(new BackgroundTask()
    {
      private boolean cancel = false;

      /**
       * @see de.willuhn.jameica.system.BackgroundTask#run(de.willuhn.util.ProgressMonitor)
       */
      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        Writer writer = null;
        try
        {
          Logger.info("creating xml backup to " + file.getAbsolutePath());

          writer = new XmlWriter(new BufferedOutputStream(new FileOutputStream(
              file)));

          monitor.setStatusText("Speichere Stammdaten");
          backup(StammdatenImpl.class, writer, monitor);
          monitor.addPercentComplete(5);

          monitor.setStatusText("Speichere Beitragsgruppe");
          backup(BeitragsgruppeImpl.class, writer, monitor);
          monitor.addPercentComplete(5);

          monitor.setStatusText("Speichere Buchungsart");
          backup(BuchungsartImpl.class, writer, monitor);
          monitor.addPercentComplete(5);

          monitor.setStatusText("Speichere Konten");
          backup(KontoImpl.class, writer, monitor);
          monitor.addPercentComplete(5);

          monitor.setStatusText("Speichere Buchungen");
          backup(BuchungImpl.class, writer, monitor);
          monitor.addPercentComplete(5);

          monitor.setStatusText("Speichere Felddefinitionen");
          backup(FelddefinitionImpl.class, writer, monitor);
          monitor.addPercentComplete(5);

          monitor.setStatusText("Speichere Spendenbescheinigungen");
          backup(SpendenbescheinigungImpl.class, writer, monitor);
          monitor.addPercentComplete(5);

          monitor.setStatusText("Speichere Formulare");
          backup(FormularImpl.class, writer, monitor);
          monitor.addPercentComplete(5);

          monitor.setStatusText("Speichere Formularfelder");
          backup(FormularfeldImpl.class, writer, monitor);
          monitor.addPercentComplete(5);

          monitor.setStatusText("Speichere Felddefinitionen");
          backup(FelddefinitionImpl.class, writer, monitor);
          monitor.addPercentComplete(5);

          monitor.setStatusText("Speichere Mitgliedsdaten");
          backup(MitgliedImpl.class, writer, monitor);
          monitor.addPercentComplete(5);

          monitor.setStatusText("Speichere Abrechnungsdaten");
          backup(AbrechnungImpl.class, writer, monitor);
          monitor.addPercentComplete(5);

          monitor.setStatusText("Speichere Eigenschaften");
          backup(EigenschaftenImpl.class, writer, monitor);
          monitor.addPercentComplete(5);

          monitor.setStatusText("Speichere Anfangsbestände");
          backup(AnfangsbestandImpl.class, writer, monitor);
          monitor.addPercentComplete(5);

          monitor.setStatusText("Speichere Jahresabschlüsse");
          backup(JahresabschlussImpl.class, writer, monitor);
          monitor.addPercentComplete(5);

          monitor.setStatusText("Speichere Jahresabschluss");
          backup(JahresabschlussImpl.class, writer, monitor);
          monitor.addPercentComplete(5);

          monitor.setStatusText("Speichere manuelle Zahlungseingänge");
          backup(ManuellerZahlungseingangImpl.class, writer, monitor);
          monitor.addPercentComplete(5);

          monitor.setStatusText("Speichere Kursteilnehmer");
          backup(KursteilnehmerImpl.class, writer, monitor);
          monitor.addPercentComplete(5);

          monitor.setStatusText("Speichere Wiedervorlagen");
          backup(WiedervorlageImpl.class, writer, monitor);
          monitor.addPercentComplete(5);

          monitor.setStatusText("Speichere Zusatzbetraege");
          backup(ZusatzbetragImpl.class, writer, monitor);
          monitor.addPercentComplete(5);

          monitor.setStatusText("Speichere Zusatzfelder");
          backup(ZusatzfelderImpl.class, writer, monitor);
          monitor.addPercentComplete(5);

          // Die Versionstabelle wird nicht mit kopiert

          monitor.setStatusText("Backup erstellt");
          monitor.setPercentComplete(100);
          monitor.setStatus(ProgressMonitor.STATUS_DONE);
        }
        catch (Exception e)
        {
          throw new ApplicationException(e.getMessage());
        }
        finally
        {
          if (writer != null)
          {
            try
            {
              writer.close();
              Logger.info("backup created");
            }
            catch (Exception e)
            {/* useless */
            }
          }
        }
      }

      /**
       * @see de.willuhn.jameica.system.BackgroundTask#isInterrupted()
       */
      public boolean isInterrupted()
      {
        return this.cancel;
      }

      /**
       * @see de.willuhn.jameica.system.BackgroundTask#interrupt()
       */
      public void interrupt()
      {
        this.cancel = true;
      }

    });
  }

  /**
   * Hilfsfunktion.
   * 
   * @param type
   * @param writer
   * @param monitor
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  private static void backup(Class type, Writer writer, ProgressMonitor monitor)
      throws Exception
  {
    DBIterator list = Einstellungen.getDBService().createList(type);
    long count = 1;
    while (list.hasNext())
    {
      GenericObject o = null;
      try
      {
        o = list.next();
        writer.write(o);
        if (count++ % 200 == 0)
          monitor.addPercentComplete(1);
      }
      catch (Exception e)
      {
        Logger.error("error while writing object " + BeanUtil.toString(o)
            + " - skipping", e);
        monitor.log("  " + BeanUtil.toString(o) + " fehlerhaft "
            + e.getMessage() + ", überspringe");
      }
    }
  }

}
