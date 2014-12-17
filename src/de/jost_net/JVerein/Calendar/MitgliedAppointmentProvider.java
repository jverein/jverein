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
package de.jost_net.JVerein.Calendar;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.graphics.RGB;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.MitgliedDetailAction;
import de.jost_net.JVerein.io.Adressbuch.Adressaufbereitung;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.calendar.Appointment;
import de.willuhn.jameica.gui.calendar.AppointmentProvider;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Implementierung eines Termin-Providers fuer Geburtstage der Mitglieder.
 */
public class MitgliedAppointmentProvider implements AppointmentProvider
{

  /**
   * @see de.willuhn.jameica.gui.calendar.AppointmentProvider#getAppointments(java.util.Date,
   *      java.util.Date)
   */
  @Override
  public List<Appointment> getAppointments(Date from, Date to)
  {
    try
    {
      DBIterator list = Einstellungen.getDBService().createList(Mitglied.class);
      list.addFilter("geburtsdatum is not null");
      list.addFilter("(austritt is null or austritt > ?)", new Date());
      Calendar calf = Calendar.getInstance();
      Calendar calt = Calendar.getInstance();
      Calendar calm = Calendar.getInstance();

      List<Appointment> result = new LinkedList<Appointment>();
      while (list.hasNext())
      {
        Mitglied m = (Mitglied) list.next();
        calm.setTime(m.getGeburtsdatum());
        calf.setTime(from);
        calf.set(Calendar.DAY_OF_MONTH, calm.get(Calendar.DAY_OF_MONTH));
        calf.set(Calendar.MONTH, calm.get(Calendar.MONTH));
        calt.setTime(to);
        calt.set(Calendar.DAY_OF_MONTH, calm.get(Calendar.DAY_OF_MONTH));
        calt.set(Calendar.MONTH, calm.get(Calendar.MONTH));

        if (calf.getTime().after(from) && calf.getTime().before(to))
        {
          result.add(new MyAppointment(m, calf.getTime(), calf
              .get(Calendar.YEAR) - calm.get(Calendar.YEAR)));
        }
        else if (calt.getTime().after(from) && calt.getTime().before(to))
        {
          result.add(new MyAppointment(m, calt.getTime(), calt
              .get(Calendar.YEAR) - calm.get(Calendar.YEAR)));
        }
      }
      return result;
    }
    catch (Exception e)
    {
      Logger.error("unable to load data", e);
    }
    return null;
  }

  /**
   * @see de.willuhn.jameica.gui.calendar.AppointmentProvider#getName()
   */
  @Override
  public String getName()
  {
    return "Geburtstage";
  }

  /**
   * Hilfsklasse zum Anzeigen und Oeffnen des Appointments.
   */
  private static class MyAppointment implements Appointment
  {

    private Mitglied m = null;

    private Date datum = null;

    private int alter = -1;

    private Settings settings;

    private MyAppointment(Mitglied m, Date datum, int alter)
    {
      this.m = m;
      this.datum = datum;
      this.alter = alter;
      settings = new Settings(this.getClass());
      settings.setStoreWhenRead(true);
    }

    /**
     * @see de.willuhn.jameica.gui.calendar.Appointment#execute()
     */
    @Override
    public void execute() throws ApplicationException
    {
      new MitgliedDetailAction().handleAction(this.m);
    }

    /**
     * @see de.willuhn.jameica.gui.calendar.Appointment#getDate()
     */
    @Override
    public Date getDate()
    {
      return datum;
    }

    /**
     * @see de.willuhn.jameica.gui.calendar.Appointment#getDescription()
     */
    @Override
    public String getDescription()
    {
      try
      {
        return String.format("%d. Geburtstag von %s", alter + "",
            Adressaufbereitung.getNameVorname(m));
      }
      catch (RemoteException re)
      {
        Logger.error("unable to build description", re);
        return null;
      }
    }

    /**
     * @see de.willuhn.jameica.gui.calendar.Appointment#getName()
     */
    @Override
    public String getName()
    {
      try
      {
        return String.format("%d. Geburtstag von %s", alter + "",
            Adressaufbereitung.getNameVorname(m));
      }
      catch (RemoteException re)
      {
        Logger.error("unable to build name", re);
        return "Mitgliedergeburtstag";
      }
    }

    /**
     * @see de.willuhn.jameica.gui.calendar.Appointment#getColor()
     */
    @Override
    public RGB getColor()
    {
      return settings.getRGB("color", new RGB(255, 178, 31));
    }

    /**
     * @see de.willuhn.jameica.gui.calendar.AbstractAppointment#getUid()
     */
    @Override
    public String getUid()
    {
      try
      {
        return "jverein.mitglied." + m.getID();
      }
      catch (RemoteException re)
      {
        Logger.error("unable to create uid", re);
        return "*Error*";
      }
    }

    /**
     * @see de.willuhn.jameica.gui.calendar.AbstractAppointment#hasAlarm()
     */
    @Override
    public boolean hasAlarm()
    {
      return false;
    }
  }

}
