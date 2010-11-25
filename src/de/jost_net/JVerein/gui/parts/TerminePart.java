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


package de.jost_net.JVerein.gui.parts;

import de.jost_net.JVerein.JVereinPlugin;
import de.willuhn.jameica.gui.calendar.AppointmentProvider;
import de.willuhn.jameica.gui.calendar.CalendarPart;
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
      ClassFinder finder = Application.getPluginLoader().getPlugin(
          JVereinPlugin.class).getResources().getClassLoader().getClassFinder();
      Class[] classes = finder.findImplementors(AppointmentProvider.class);
      for (Class c : classes)
      {
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