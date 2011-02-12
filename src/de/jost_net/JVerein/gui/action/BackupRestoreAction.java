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
 * Revision 1.9  2010-11-13 09:21:13  jost
 * Warnings entfernt.
 *
 * Revision 1.8  2010-10-15 09:58:03  jost
 * Code aufgeräumt
 *
 * Revision 1.7  2010-05-24 14:59:19  jost
 * Vermeidung Fehlermeldung.
 *
 * Revision 1.6  2010/03/03 20:11:23  jost
 * *** empty log message ***
 *
 * Revision 1.5  2009/11/17 20:51:06  jost
 * Meldung bei gefüllter Datenbank
 *
 * Revision 1.4  2009/06/11 21:02:05  jost
 * Vorbereitung I18N
 *
 * Revision 1.3  2009/01/27 18:50:15  jost
 * Import-Statement korrigiert
 *
 * Revision 1.2  2008/11/29 13:05:10  jost
 * Refactoring: Warnungen beseitigt.
 *
 * Revision 1.1  2008/09/29 14:42:38  jost
 * Neu: Backup und Restore im XML-Format
 *
 **********************************************************************/

package de.jost_net.JVerein.gui.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.EigenschaftGruppe;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.util.JVDateFormatJJJJMMTT;
import de.willuhn.datasource.BeanUtil;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.serialize.ObjectFactory;
import de.willuhn.datasource.serialize.Reader;
import de.willuhn.datasource.serialize.XmlReader;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.SimpleDialog;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

/**
 * Action zum Einspielen eines XML-Backups.
 */
public class BackupRestoreAction implements Action
{

  /**
   * @see de.willuhn.jameica.gui.Action#handleAction(java.lang.Object)
   */
  public void handleAction(Object context)
  {
    try
    {
      DBIterator it = Einstellungen.getDBService().createList(Mitglied.class);
      if (it.size() > 0)
      {
        SimpleDialog dialog = new SimpleDialog(SimpleDialog.POSITION_CENTER);
        dialog.setTitle("Fehler");
        dialog.setText("Datenbank ist nicht leer!");
        dialog.open();
        return;
      }
    }
    catch (Exception e1)
    {
      Logger.error("Fehler: ", e1);
    }

    FileDialog fd = new FileDialog(GUI.getShell(), SWT.OPEN);
    fd.setFileName("jverein-" + new JVDateFormatJJJJMMTT().format(new Date())
        + ".xml");
    fd.setFilterExtensions(new String[] { "*.xml" });
    fd.setText(JVereinPlugin.getI18n().tr(
        "Bitte wählen Sie die Backup-Datei aus"));
    String f = fd.open();
    if (f == null || f.length() == 0)
    {
      return;
    }

    final File file = new File(f);
    if (!file.exists())
    {
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
        monitor.setStatusText(JVereinPlugin.getI18n().tr("Importiere Backup"));
        Logger.info("importing backup " + file.getAbsolutePath());
        final ClassLoader loader = Application.getPluginLoader()
            .getPlugin(JVereinPlugin.class).getResources().getClassLoader();

        try
        {
          EigenschaftGruppe eg = (EigenschaftGruppe) Einstellungen
              .getDBService().createObject(EigenschaftGruppe.class, "1");
          eg.delete();
        }
        catch (RemoteException e1)
        {
          Logger.error("EigenschaftGruppe mit id=1 kann nicht gelöscht werden",
              e1);
        }

        Reader reader = null;
        try
        {
          InputStream is = new BufferedInputStream(new FileInputStream(file));
          reader = new XmlReader(is, new ObjectFactory()
          {

            public GenericObject create(String type, String id, Map values)
                throws Exception
            {
              AbstractDBObject object = (AbstractDBObject) Einstellungen
                  .getDBService().createObject(loader.loadClass(type), null);
              object.setID(id);
              Iterator<?> i = values.keySet().iterator();
              while (i.hasNext())
              {
                String name = (String) i.next();
                object.setAttribute(name, values.get(name));
              }
              return object;
            }
          });

          long count = 1;
          GenericObject o = null;
          while ((o = reader.read()) != null)
          {
            try
            {
              ((AbstractDBObject) o).insert();
            }
            catch (Exception e)
            {
              Logger.error("unable to import " + o.getClass().getName() + ":"
                  + o.getID() + ", skipping", e);
              monitor.log(JVereinPlugin.getI18n().tr(
                  " {0} fehlerhaft: {1}, überspringe ",
                  new String[] { BeanUtil.toString(o), e.getMessage() }));
            }
            if (count++ % 100 == 0)
            {
              monitor.addPercentComplete(1);
            }
          }
          monitor.setStatus(ProgressMonitor.STATUS_DONE);
          monitor
              .setStatusText(JVereinPlugin.getI18n().tr("Backup importiert"));
          monitor.setPercentComplete(100);
        }
        catch (Exception e)
        {
          Logger.error("error while importing data", e);
          throw new ApplicationException(e.getMessage());
        }
        finally
        {
          if (reader != null)
          {
            try
            {
              reader.close();
              Logger.info("backup imported");
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
}
