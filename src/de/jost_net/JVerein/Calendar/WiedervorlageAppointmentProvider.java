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
import de.jost_net.JVerein.rmi.Wiedervorlage;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.calendar.Appointment;
import de.willuhn.jameica.gui.calendar.AppointmentProvider;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;

/**
 * Implementierung eines Termin-Providers fuer Wiedervorlagen
 */
public class WiedervorlageAppointmentProvider implements AppointmentProvider
{

  /**
   * @see de.willuhn.jameica.gui.calendar.AppointmentProvider#getAppointments(java.util.Date,
   *      java.util.Date)
   */
  @Override
  public List<Appointment> getAppointments(Date from, Date to)
  {
    if (from == null)
    {
      return null;
    }
    try
    {
      DBIterator<Wiedervorlage> list = Einstellungen.getDBService()
          .createList(Wiedervorlage.class);
      Calendar cal = Calendar.getInstance();
      cal.setTime(from);
      if (from != null)
        list.addFilter("datum >= ?",
            new Object[] { new java.sql.Date(from.getTime()) });
      list.addFilter("datum <= ?",
          new Object[] { new java.sql.Date(to.getTime()) });
      list.setOrder("ORDER BY day(datum)");

      List<Appointment> result = new LinkedList<>();
      while (list.hasNext())
      {
        result.add(new MyAppointment(list.next()));
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
    return "Wiedervorlagen";
  }

  /**
   * Hilfsklasse zum Anzeigen und Oeffnen des Appointments.
   */
  private static class MyAppointment implements Appointment
  {

    private Wiedervorlage w = null;

    private Settings settings;

    private MyAppointment(Wiedervorlage w)
    {
      this.w = w;
      settings = new Settings(this.getClass());
      settings.setStoreWhenRead(true);
    }

    /**
     * @see de.willuhn.jameica.gui.calendar.Appointment#execute()
     */
    @Override
    public void execute()
    {
      try
      {
        new MitgliedDetailAction().handleAction(w.getMitglied());
      }
      catch (Exception e)
      {
        Logger.error("Fehler", e);
      }
    }

    /**
     * @see de.willuhn.jameica.gui.calendar.Appointment#getDate()
     */
    @Override
    public Date getDate()
    {
      try
      {
        return w.getDatum();
      }
      catch (Exception e)
      {
        Logger.error("unable to read date", e);
      }
      return null;
    }

    /**
     * @see de.willuhn.jameica.gui.calendar.Appointment#getDescription()
     */
    @Override
    public String getDescription()
    {
      try
      {
        return "Wiedervorlage:"
            + Adressaufbereitung.getNameVorname(w.getMitglied()) + ": "
            + w.getVermerk();
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
        return Adressaufbereitung.getNameVorname(w.getMitglied()) + ": "
            + w.getVermerk();
      }
      catch (RemoteException re)
      {
        Logger.error("unable to build name", re);
        return "Wiedervorlage";
      }
    }

    /**
     * @see de.willuhn.jameica.gui.calendar.Appointment#getColor()
     */
    @Override
    public RGB getColor()
    {
      return settings.getRGB("color", new RGB(31, 32, 255));
    }

    /**
     * @see de.willuhn.jameica.gui.calendar.AbstractAppointment#getUid()
     */
    @Override
    public String getUid()
    {
      try
      {
        return "jverein.wiedervorlage." + w.getID();
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
