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
 * Revision 1.2  2010-11-26 08:11:06  jost
 * Änderung von Olaf übernommen.
 *
 * Revision 1.1  2010-11-25 15:11:45  jost
 * Initial Commit
 *
 **********************************************************************/

package de.jost_net.JVerein.gui.parts;

import de.jost_net.JVerein.JVereinPlugin;
import de.willuhn.jameica.gui.calendar.AppointmentProvider;
import de.willuhn.jameica.gui.calendar.CalendarPart;
import de.willuhn.jameica.plugin.AbstractPlugin;
import de.willuhn.jameica.plugin.PluginLoader;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ClassFinder;

/**
 * Zeigt die anstehenden Termine in JVerein an.
 */
public class TerminePart extends CalendarPart
{

  public TerminePart()
  {
    // Wir laden automatisch die Termin-Provider.
    try
    {
      PluginLoader loader = Application.getPluginLoader();
      AbstractPlugin plugin = loader.getPlugin(JVereinPlugin.class);
      ClassFinder finder = plugin.getResources().getClassLoader()
          .getClassFinder();

      Class[] classes = finder.findImplementors(AppointmentProvider.class);
      for (Class c : classes)
      {
        // Checken, ob die Klasse zu Hibiscus gehoert
        AbstractPlugin p = loader.findByClass(c);
        if (p == null || p != plugin)
          continue; // Gehoert nicht zu uns.
        try
        {
          addAppointmentProvider((AppointmentProvider) c.newInstance());
        }
        catch (Exception e)
        {
          Logger.error("unable to load appointment provider " + c
              + ", skipping", e);
        }
      }
    }
    catch (ClassNotFoundException e)
    {
      Logger.debug("no appointment providers found");
    }
  }
}