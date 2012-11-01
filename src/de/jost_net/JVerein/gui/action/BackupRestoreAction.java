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
import de.jost_net.JVerein.rmi.Adresstyp;
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
  @Override
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

      // Vom System eingefügte Sätze löschen. Ansonsten gibt es duplicate keys
      it = Einstellungen.getDBService().createList(Adresstyp.class);
      while (it.hasNext())
      {
        Adresstyp a = (Adresstyp) it.next();
        a.delete();
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
      @Override
      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        monitor.setStatusText(JVereinPlugin.getI18n().tr("Importiere Backup"));
        Logger.info("importing backup " + file.getAbsolutePath());
        final ClassLoader loader = Application.getPluginLoader()
            .getPlugin(JVereinPlugin.class).getManifest().getClassLoader();

        try
        {
          EigenschaftGruppe eg = (EigenschaftGruppe) Einstellungen
              .getDBService().createObject(EigenschaftGruppe.class, "1");
          eg.delete();
        }
        catch (RemoteException e1)
        {
          Logger.error(JVereinPlugin.getI18n().tr(
              "EigenschaftGruppe mit id=1 kann nicht gelöscht werden"));
        }

        Reader reader = null;
        try
        {
          InputStream is = new BufferedInputStream(new FileInputStream(file));
          reader = new XmlReader(is, new ObjectFactory()
          {

            @Override
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
}
