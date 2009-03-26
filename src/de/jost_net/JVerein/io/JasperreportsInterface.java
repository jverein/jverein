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
package de.jost_net.JVerein.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.util.JRProperties;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Report;
import de.willuhn.jameica.plugin.PluginResources;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;

public class JasperreportsInterface
{
  private Report report;

  public JasperreportsInterface(Report report)
  {
    this.report = report;
  }

  public void compile()
  {
    // Aktuellen Classloader sichern
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    try
    {
      Logger.info("compiling");
      ByteArrayInputStream quellstream = new ByteArrayInputStream(report
          .getQuelle());
      ByteArrayOutputStream kompstream = new ByteArrayOutputStream();

      // Classloader gegen den von JasperReports ersetzen:
      Thread.currentThread().setContextClassLoader(
          JasperCompileManager.class.getClassLoader());

      PluginResources res = Application.getPluginLoader().getPlugin(
          JVereinPlugin.class).getResources();
      // String source = res.getPath();
      String target = res.getWorkPath();

      // Temp-Verzeichnis des Compilers auf ein Verzeichnis setzen,
      // in dem geschrieben werden darf. Standardmaessig verwendete
      // es bei mir das Jameica-Programmverzeichnis, in dem nicht
      // geschrieben werden darf.
      JRProperties.setProperty(JRProperties.COMPILER_TEMP_DIR, target);

      // Classpath setzen
      // JRProperties.setProperty(JRProperties.COMPILER_CLASSPATH, System
      // .getProperty("java.class.path"));

      // Compilieren
      JasperCompileManager.compileReportToStream(quellstream, kompstream);
      report.setKompilat(kompstream.toByteArray());
      Logger.info("done");
    }
    catch (Exception e)
    {
      Logger.error("unable to compile report", e);
    }
    finally
    {
      // Classloader wieder zuruecksetzen
      Thread.currentThread().setContextClassLoader(cl);
    }
  }

  public void fill(Map parameters, Connection connection, File file)
      throws RemoteException, JRException, FileNotFoundException
  {
    ByteArrayInputStream quellstream = new ByteArrayInputStream(report
        .getKompilat());
    JasperPrint jp = JasperFillManager.fillReport(quellstream, parameters,
        connection);
    FileOutputStream fos = new FileOutputStream(file);

    JasperExportManager.exportReportToPdfStream(jp, fos);
  }

}
