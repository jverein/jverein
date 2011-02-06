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
 * Revision 1.14  2011-02-06 10:23:32  jost
 * weitere Tabellen aufgenommen.
 *
 * Revision 1.13  2011-01-09 14:28:37  jost
 * Stammdaten in die Einstellungen verschoben.
 *
 * Revision 1.12  2010-12-12 17:18:56  jost
 * Neue Tabellen aufgenommen.
 *
 * Revision 1.11  2010-11-13 09:20:22  jost
 * Mit V 1.5 deprecatete Spalten und Tabellen entfernt.
 *
 * Revision 1.10  2010-10-15 09:58:01  jost
 * Code aufgeräumt
 *
 * Revision 1.9  2010-09-12 11:52:10  jost
 * Bugfixes
 *
 * Revision 1.8  2010/05/24 14:57:58  jost
 * Weitere Tabellen aufgenommen.
 *
 * Revision 1.7  2010/02/01 20:56:43  jost
 * Mail-Tabellen aufgenommen
 *
 * Revision 1.6  2009/11/17 20:50:09  jost
 * Codeoptimierung
 *
 * Revision 1.5  2009/06/11 21:02:05  jost
 * Vorbereitung I18N
 *
 * Revision 1.4  2009/04/13 11:37:36  jost
 * Neu: Lehrgänge
 *
 * Revision 1.3  2008/12/22 21:05:14  jost
 * Zusatzabbuchung->Zusatzbetrag
 *
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
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Mail;
import de.jost_net.JVerein.rmi.MailEmpfaenger;
import de.jost_net.JVerein.rmi.MailVorlage;
import de.jost_net.JVerein.rmi.Mitgliedfoto;
import de.jost_net.JVerein.server.AbrechnungslaufImpl;
import de.jost_net.JVerein.server.AdresstypImpl;
import de.jost_net.JVerein.server.AnfangsbestandImpl;
import de.jost_net.JVerein.server.ArbeitseinsatzImpl;
import de.jost_net.JVerein.server.BeitragsgruppeImpl;
import de.jost_net.JVerein.server.BuchungDokumentImpl;
import de.jost_net.JVerein.server.BuchungImpl;
import de.jost_net.JVerein.server.BuchungsartImpl;
import de.jost_net.JVerein.server.BuchungsklasseImpl;
import de.jost_net.JVerein.server.EigenschaftGruppeImpl;
import de.jost_net.JVerein.server.EigenschaftImpl;
import de.jost_net.JVerein.server.EigenschaftenImpl;
import de.jost_net.JVerein.server.EinstellungImpl;
import de.jost_net.JVerein.server.FelddefinitionImpl;
import de.jost_net.JVerein.server.FormularImpl;
import de.jost_net.JVerein.server.FormularfeldImpl;
import de.jost_net.JVerein.server.JahresabschlussImpl;
import de.jost_net.JVerein.server.KontoImpl;
import de.jost_net.JVerein.server.KursteilnehmerImpl;
import de.jost_net.JVerein.server.LehrgangImpl;
import de.jost_net.JVerein.server.LehrgangsartImpl;
import de.jost_net.JVerein.server.MailAnhangImpl;
import de.jost_net.JVerein.server.MitgliedDokumentImpl;
import de.jost_net.JVerein.server.MitgliedImpl;
import de.jost_net.JVerein.server.MitgliedskontoImpl;
import de.jost_net.JVerein.server.SpendenbescheinigungImpl;
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

  // Die Versionstabelle wird nicht mit kopiert
  Class<?>[] tab = { AdresstypImpl.class, EinstellungImpl.class,
      AbrechnungslaufImpl.class, BeitragsgruppeImpl.class,
      BuchungsklasseImpl.class, BuchungsartImpl.class, KontoImpl.class,
      MitgliedImpl.class, MitgliedskontoImpl.class, ArbeitseinsatzImpl.class,
      MitgliedDokumentImpl.class, Mitgliedfoto.class,
      BuchungDokumentImpl.class, BuchungImpl.class, FelddefinitionImpl.class,
      SpendenbescheinigungImpl.class, FormularImpl.class,
      FormularfeldImpl.class, EigenschaftGruppeImpl.class,
      EigenschaftImpl.class, EigenschaftenImpl.class, AnfangsbestandImpl.class,
      JahresabschlussImpl.class, KursteilnehmerImpl.class,
      WiedervorlageImpl.class, ZusatzbetragImpl.class, ZusatzfelderImpl.class,
      LehrgangsartImpl.class, LehrgangImpl.class, MailVorlage.class,
      MailEmpfaenger.class, Mail.class, MailAnhangImpl.class };

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
    fd.setText(JVereinPlugin.getI18n().tr(
        "Bitte wählen Sie die Datei, in der das Backup gespeichert wird"));
    String f = fd.open();
    if (f == null || f.length() == 0)
      return;

    final File file = new File(f);
    try
    {
      if (file.exists()
          && !Application.getCallback().askUser(
              JVereinPlugin.getI18n().tr(
                  "Datei existiert bereits. Überschreiben?")))
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
          new StatusBarMessage(JVereinPlugin.getI18n().tr(
              "Fehler beim Erstellen der Backup-Datei"),
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

          for (Class<?> clazz : tab)
          {
            backup(clazz, writer, monitor);
            monitor.addPercentComplete(100 / tab.length);
          }

          monitor.setStatusText(JVereinPlugin.getI18n().tr("Backup erstellt"));
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
  private static void backup(Class<?> type, Writer writer,
      ProgressMonitor monitor) throws Exception
  {
    DBIterator list = Einstellungen.getDBService().createList(type);
    list.setOrder("ORDER by id");
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
        monitor.log(JVereinPlugin.getI18n().tr(
            "  {0} fehlerhaft: {1}, überspringe",
            new String[] { BeanUtil.toString(o), e.getMessage() }));
      }
    }
  }
}
