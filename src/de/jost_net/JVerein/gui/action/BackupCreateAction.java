/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * This program is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,  but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See 
 *  the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, 
 * see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de
 * www.jverein.de
 **********************************************************************/

package de.jost_net.JVerein.gui.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
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
import de.jost_net.JVerein.server.MailEmpfaengerImpl;
import de.jost_net.JVerein.server.MailImpl;
import de.jost_net.JVerein.server.MailVorlageImpl;
import de.jost_net.JVerein.server.MitgliedDokumentImpl;
import de.jost_net.JVerein.server.MitgliedImpl;
import de.jost_net.JVerein.server.MitgliedfotoImpl;
import de.jost_net.JVerein.server.MitgliedskontoImpl;
import de.jost_net.JVerein.server.ProjektImpl;
import de.jost_net.JVerein.server.SpendenbescheinigungImpl;
import de.jost_net.JVerein.server.WiedervorlageImpl;
import de.jost_net.JVerein.server.ZusatzbetragImpl;
import de.jost_net.JVerein.server.ZusatzfelderImpl;
import de.jost_net.JVerein.util.JVDateFormatJJJJMMTT;
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

  Class<?>[] tab = { //
  AbrechnungslaufImpl.class,//
      AdresstypImpl.class,//
      KontoImpl.class,//
      AnfangsbestandImpl.class,//
      BuchungsklasseImpl.class,//
      EigenschaftGruppeImpl.class,//
      EinstellungImpl.class,//
      FelddefinitionImpl.class,//
      FormularImpl.class, //
      FormularfeldImpl.class,//
      JahresabschlussImpl.class,//
      KursteilnehmerImpl.class,//
      LehrgangsartImpl.class, //
      MailImpl.class,//
      MailAnhangImpl.class,//
      MailVorlageImpl.class,//
      BuchungsartImpl.class,//
      ProjektImpl.class,//
      EigenschaftImpl.class,//
      BeitragsgruppeImpl.class,//
      MitgliedImpl.class,//
      ArbeitseinsatzImpl.class,//
      MitgliedDokumentImpl.class,//
      MitgliedfotoImpl.class,//
      MitgliedskontoImpl.class, //
      SpendenbescheinigungImpl.class,//
      WiedervorlageImpl.class,//
      ZusatzbetragImpl.class,//
      ZusatzfelderImpl.class, //
      BuchungImpl.class,//
      BuchungDokumentImpl.class, //
      EigenschaftenImpl.class,//
      LehrgangImpl.class,//
      MailEmpfaengerImpl.class,//
  };

  /**
   * @see de.willuhn.jameica.gui.Action#handleAction(java.lang.Object)
   */
  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
    fd.setFilterPath(System.getProperty("user.home"));
    fd.setFileName("jverein-backup-"
        + new JVDateFormatJJJJMMTT().format(new Date()) + ".xml");
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
      @Override
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
      @Override
      public boolean isInterrupted()
      {
        return this.cancel;
      }

      /**
       * @see de.willuhn.jameica.system.BackgroundTask#interrupt()
       */
      @Override
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
