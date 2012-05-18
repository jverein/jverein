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

package de.jost_net.JVerein.io;

import java.util.ArrayList;

import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ClassFinder;

/**
 * Ueber diese Klasse koennen alle verfuegbaren Export-und Import Formate
 * abgerufen werden.
 */
public class IORegistry
{

  // Liste der Export-Filter
  private static ArrayList<IO> exporters = null;

  // Liste der Importer
  private static ArrayList<IO> importers = null;

  static
  {
    Logger.info("looking for installed export filters");
    exporters = load(Exporter.class);
    Logger.info("looking for installed import filters");
    importers = load(Importer.class);
  }

  /**
   * Sucht im Classpath nach allen Importern/Exportern.
   * 
   * @param type
   *          zu ladender Typ.
   * @return Liste der gefundenen Importer/Exporter.
   */
  private static synchronized ArrayList<IO> load(Class<?> type)
  {
    ArrayList<IO> l = new ArrayList<IO>();
    try
    {
      ClassFinder finder = Application.getClassLoader().getClassFinder();
      Class<?>[] list = finder.findImplementors(type);
      if (list == null || list.length == 0)
        throw new ClassNotFoundException();

      // Initialisieren
      for (int i = 0; i < list.length; ++i)
      {
        try
        {
          IO io = (IO) list[i].newInstance();
          Logger.info("  " + io.getName() + " - " + list[i].getName());
          l.add(io);
        }
        catch (Exception e)
        {
          Logger.error(
              "error while loading import/export filter " + list[i].getName(),
              e);
        }
      }

    }
    catch (ClassNotFoundException e)
    {
      Logger.warn("no filters found for type: " + type.getName());
    }
    return l;
  }

  /**
   * Liefert eine Liste aller verfuegbaren Export-Formate.
   * 
   * @return Export-Filter.
   */
  public static Exporter[] getExporters()
  {
    return (Exporter[]) exporters.toArray(new Exporter[exporters.size()]);
  }

  /**
   * Liefert eine Liste aller verfuegbaren Import-Formate.
   * 
   * @return Import-Filter.
   */
  public static Importer[] getImporters()
  {
    return (Importer[]) importers.toArray(new Importer[importers.size()]);
  }

}

/**********************************************************************
 * $Log$ Revision 1.8 2012/04/15 08:30:24 jverein Generics
 * Revision 1.7 2012/02/27 21:11:10 jverein Import-Logik überarbeitet. Revision
 * 1.3 2010/06/01 21:55:40 willuhn
 * 
 * @N uebersichtlichere Log-Ausgabe
 * 
 *    Revision 1.2 2006/01/17 00:22:36 willuhn
 * @N erster Code fuer Swift MT940-Import
 * 
 *    Revision 1.1 2005/06/08 16:48:54 web0
 * @N new Import/Export-System
 * 
 *    Revision 1.2 2005/06/02 22:57:34 web0
 * @N Export von Konto-Umsaetzen
 * 
 *    Revision 1.1 2005/06/02 21:48:44 web0
 * @N Exporter-Package
 * @N CSV-Exporter
 * 
 **********************************************************************/
