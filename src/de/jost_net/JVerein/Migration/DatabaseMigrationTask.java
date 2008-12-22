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
 * Revision 1.4  2008/11/29 13:14:01  jost
 * Refactoring: Warnungen beseitigt.
 *
 * Revision 1.3  2008/01/02 10:59:17  jost
 * Bugfix Migration nach Neuinstallation
 *
 * Revision 1.2  2007/12/16 20:26:56  jost
 * Versions-Tabelle wird mitmigriert.
 *
 * Revision 1.1  2007/12/01 10:06:56  jost
 * H2-Support
 *
 **********************************************************************/

package de.jost_net.JVerein.Migration;

import java.rmi.RemoteException;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Version;
import de.jost_net.JVerein.server.BeitragsgruppeImpl;
import de.jost_net.JVerein.server.KursteilnehmerImpl;
import de.jost_net.JVerein.server.ManuellerZahlungseingangImpl;
import de.jost_net.JVerein.server.MitgliedImpl;
import de.jost_net.JVerein.server.StammdatenImpl;
import de.jost_net.JVerein.server.VersionImpl;
import de.jost_net.JVerein.server.WiedervorlageImpl;
import de.jost_net.JVerein.server.ZusatzbetragImpl;
import de.willuhn.datasource.BeanUtil;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBObject;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.I18N;
import de.willuhn.util.ProgressMonitor;

/**
 * Task zum Migrieren der Datenbank.
 */
public class DatabaseMigrationTask implements BackgroundTask
{
  protected I18N i18n = Application.getPluginLoader().getPlugin(
      JVereinPlugin.class).getResources().getI18N();

  static de.willuhn.jameica.system.Settings SETTINGS = new de.willuhn.jameica.system.Settings(
      DatabaseMigrationTask.class);

  private boolean cancel = false;

  protected DBService source = null;

  protected DBService target = null;

  /**
   * Legt die Datenquelle fest.
   */
  public void setSource(DBService source)
  {
    this.source = source;
  }

  /**
   * Legt das Datenziel fest.
   */
  public void setTarget(DBService target)
  {
    this.target = target;
  }

  public void interrupt()
  {
    this.cancel = true;
  }

  public boolean isInterrupted()
  {
    return cancel;
  }

  public void run(ProgressMonitor monitor) throws ApplicationException
  {
    try
    {
      monitor.log(i18n.tr("Starte Datenmigration"));
      Logger.info("################################################");
      Logger.info("starting data migration");
      copy(StammdatenImpl.class, monitor);
      copy(BeitragsgruppeImpl.class, monitor);
      copy(MitgliedImpl.class, monitor);
      copy(KursteilnehmerImpl.class, monitor);
      copy(ManuellerZahlungseingangImpl.class, monitor);
      DBIterator v = target.createList(Version.class);
      if (v.size() == 0)
      {
        copy(VersionImpl.class, monitor);
      }
      copy(WiedervorlageImpl.class, monitor);
      copy(ZusatzbetragImpl.class, monitor);

      monitor.setStatus(ProgressMonitor.STATUS_DONE);
      monitor.setStatusText(i18n.tr("Fertig"));
    }
    catch (ApplicationException ae)
    {
      monitor.setStatusText(ae.getMessage());
      monitor.setStatus(ProgressMonitor.STATUS_ERROR);
      throw ae;
    }
    catch (Exception e)
    {
      monitor.setStatusText(e.getMessage());
      monitor.setStatus(ProgressMonitor.STATUS_ERROR);
      throw new ApplicationException(e);
    }
    finally
    {
      monitor.setPercentComplete(100);
    }
  }

  /**
   * Kann von der abgeleiteten Klasse ueberschrieben werden, um Daten zu
   * korrigieren.
   * 
   * @param object
   *          das ggf noch zu korrigierende Objekt.
   * @param monitor
   *          Monitor.
   * @throws RemoteException
   */
  protected void fixObject(AbstractDBObject object, ProgressMonitor monitor)
      throws RemoteException
  {
  }

  /**
   * Kopiert eine einzelne Tabelle.
   * 
   * @param type
   *          Objekttyp.
   * @param monitor
   *          Monitor.
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  protected void copy(Class type, ProgressMonitor monitor) throws Exception
  {
    monitor.setStatusText(i18n.tr("Kopiere " + type.getName()));
    Logger.info("  copying " + type.getName());

    long count = 0;
    DBIterator i = source.createList(type);
    AbstractDBObject to = null;

    while (!cancel && i.hasNext())
    {
      DBObject from = (DBObject) i.next();
      to = (AbstractDBObject) target.createObject(type, null);

      String id = null;
      try
      {
        id = from.getID();
        to.transactionBegin();
        to.overwrite(from);
        if (++count % 100 == 0)
        {
          monitor.log(i18n.tr("  Kopierte Datensätze: {0}", "" + count));
          Logger.info("  copied records: " + count);
          monitor.addPercentComplete(1);
        }
        to.setID(id);
        fixObject(to, monitor);
        to.insert();
        to.transactionCommit();
      }
      catch (Exception e)
      {
        Logger.error("unable to copy record " + type.getName() + ":" + id
            + ": " + BeanUtil.toString(from), e);
        if (to == null)
        {
          monitor.log(i18n
              .tr("Fehler beim Kopieren des Datensatzes, überspringe"));
        }
        else
        {
          try
          {
            monitor.log(i18n.tr(
                "  Fehler beim Kopieren von [ID: {0}]: {1}, überspringe",
                new String[] { id, BeanUtil.toString(to) }));
            to.transactionRollback();
          }
          catch (Exception e2)
          {
            // ignore
          }
        }
      }
    }
    monitor.addPercentComplete(5);
  }
}
