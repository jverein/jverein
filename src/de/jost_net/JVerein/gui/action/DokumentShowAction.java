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
package de.jost_net.JVerein.gui.action;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.AbstractDokument;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.action.Program;
import de.willuhn.jameica.messaging.QueryMessage;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Anzeigen von Dokumenten
 */
public class DokumentShowAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null || !(context instanceof AbstractDokument))
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Kein Dokument ausgewählt"));
    }
    try
    {
      AbstractDokument ad = (AbstractDokument) context;
      if (ad.isNewObject())
      {
        return;
      }
      QueryMessage qm = new QueryMessage(ad.getUUID(), null);
      Application.getMessagingFactory().getMessagingQueue(
          "jameica.messaging.getmeta").sendSyncMessage(qm);
      Map map = (Map) qm.getData();

      qm = new QueryMessage(ad.getUUID(), null);
      Application.getMessagingFactory().getMessagingQueue(
          "jameica.messaging.get").sendSyncMessage(qm);
      byte[] data = (byte[]) qm.getData();
      final File file = new File(System.getProperty("java.io.tmpdir") + "/"
          + map.get("filename"));
      FileOutputStream fos = new FileOutputStream(file);
      fos.write(data);
      fos.close();
      file.deleteOnExit();
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
    catch (Exception e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Fehler beim Löschen des Dokuments");
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
