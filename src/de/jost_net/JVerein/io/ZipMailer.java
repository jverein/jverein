/**********************************************************************
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
package de.jost_net.JVerein.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.Variable.AllgemeineMap;
import de.jost_net.JVerein.Variable.VarTools;
import de.jost_net.JVerein.rmi.MailAnhang;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

/**
 * Versand von Mails mit Anhang aus einer Zip-Datei an die Mitglieder. Wird z.
 * B. für den Rechnungsversand gebraucht<br>
 */
public class ZipMailer
{
  public ZipMailer(final File zipfile, final String betreff, final String text)
  {
    BackgroundTask t = new BackgroundTask()
    {
      @Override
      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        try
        {
          MailSender sender = new MailSender(Einstellungen.getEinstellung()
              .getSmtpServer(), Einstellungen.getEinstellung().getSmtpPort(),
              Einstellungen.getEinstellung().getSmtpAuthUser(), Einstellungen
                  .getEinstellung().getSmtpAuthPwd(), Einstellungen
                  .getEinstellung().getSmtpFromAddress(), Einstellungen
                  .getEinstellung().getSmtpFromAnzeigename(), Einstellungen
                  .getEinstellung().getMailAlwaysBcc(), Einstellungen
                  .getEinstellung().getMailAlwaysCc(), Einstellungen
                  .getEinstellung().getSmtpSsl(), Einstellungen
                  .getEinstellung().getSmtpStarttls(),
              Einstellungen.getImapCopyData());

          Velocity.init();
          Logger.debug("preparing velocity context");
          monitor.setStatus(ProgressMonitor.STATUS_RUNNING);
          monitor.setPercentComplete(0);
          // int sentCount = 0;

          ZipFile zip = new ZipFile(zipfile);
          for (@SuppressWarnings("rawtypes")
          Enumeration e = zip.entries(); e.hasMoreElements();)
          {
            ZipEntry entry = (ZipEntry) e.nextElement();
            String currentEntry = entry.getName();
            if (currentEntry.indexOf("@") > 0)
            {
              // Entry mit Mail-Adresse
              String id = currentEntry.substring(0, currentEntry.indexOf("#"));
              String mail = currentEntry
                  .substring(currentEntry.indexOf("#") + 1);
              mail = mail.substring(0, mail.length() - 4);
              Mitglied m = (Mitglied) Einstellungen.getDBService()
                  .createObject(Mitglied.class, id);
              MailAnhang ma = (MailAnhang) Einstellungen.getDBService()
                  .createObject(MailAnhang.class, null);
              InputStream in = zip.getInputStream(entry);
              ByteArrayOutputStream bos = new ByteArrayOutputStream();
              // buffer size
              byte[] b = new byte[1024];
              int count;
              while ((count = in.read(b)) > 0)
              {
                bos.write(b, 0, count);
              }
              in.close();
              ma.setAnhang(bos.toByteArray());
              ma.setDateiname("Rechnung.pdf");
              TreeSet<MailAnhang> anhang = new TreeSet<MailAnhang>();
              anhang.add(ma);

              VelocityContext context = new VelocityContext();
              context.put("dateformat", new JVDateFormatTTMMJJJJ());
              context.put("decimalformat", Einstellungen.DECIMALFORMAT);
              Map<String, Object> map = m.getMap(null);
              map = new AllgemeineMap().getMap(map);
              VarTools.add(context, map);

              StringWriter wtext1 = new StringWriter();
              Velocity.evaluate(context, wtext1, "LOG", betreff);

              StringWriter wtext2 = new StringWriter();
              Velocity.evaluate(context, wtext2, "LOG", text);

              monitor.log("Versende an " + mail);

              sender.sendMail(mail, wtext1.getBuffer().toString(), wtext2
                  .getBuffer().toString(), anhang);
            } // Ende von if
          } // Ende von for
          zip.close();
        }
        catch (ZipException e)
        {
          e.printStackTrace();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
        catch (Exception e)
        {
          e.printStackTrace();
          throw new ApplicationException(e);
        }

        monitor.setPercentComplete(100);
        monitor.setStatus(ProgressMonitor.STATUS_DONE);
        // monitor.setStatusText(MessageFormat.format(
        // "Anzahl verschickter Mails: {0}", sentCount + ""));
        // GUI.getStatusBar().setSuccessText(
        // "Mail" + (sentCount > 1 ? "s" : "") + " verschickt");
        GUI.getCurrentView().reload();
      } // Ende von run()

      @Override
      public void interrupt()
      {
        //
      }

      @Override
      public boolean isInterrupted()
      {
        return false;
      }

    };

    Application.getController().start(t);

  }
}