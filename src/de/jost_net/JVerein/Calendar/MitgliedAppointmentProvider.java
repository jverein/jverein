package de.jost_net.JVerein.Calendar;

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
 * Revision 1.1  2010-11-25 15:11:15  jost
 * Initial Commit
 *
 **********************************************************************/

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.graphics.RGB;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.MitgliedDetailAction;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.calendar.Appointment;
import de.willuhn.jameica.gui.calendar.AppointmentProvider;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.I18N;

/**
 * Implementierung eines Termin-Providers fuer Geburtstage der Mitglieder.
 */
public class MitgliedAppointmentProvider implements AppointmentProvider
{

  private final static I18N i18n = JVereinPlugin.getI18n();

  /**
   * @see de.willuhn.jameica.gui.calendar.AppointmentProvider#getAppointments(java.util.Date,
   *      java.util.Date)
   */
  public List<Appointment> getAppointments(Date from, Date to)
  {
    try
    {
      DBIterator list = Einstellungen.getDBService().createList(Mitglied.class);
      Calendar cal = Calendar.getInstance();
      cal.setTime(from);
      if (from != null)
        list.addFilter("month(geburtsdatum)= ?",
            new Object[] { cal.get(Calendar.MONTH) + 1 });
      list.setOrder("ORDER BY day(geburtsdatum)");

      List<Appointment> result = new LinkedList<Appointment>();
      while (list.hasNext())
      {
        Mitglied m = (Mitglied) list.next();
        if (m.getGeburtsdatum() != null)
        {
          result.add(new MyAppointment(m, cal.get(Calendar.YEAR)));
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
  public String getName()
  {
    return i18n.tr("Geburtstage");
  }

  /**
   * Hilfsklasse zum Anzeigen und Oeffnen des Appointments.
   */
  private class MyAppointment implements Appointment
  {

    private Mitglied m = null;

    private int year = 0;

    private MyAppointment(Mitglied m, int year)
    {
      this.m = m;
      this.year = year;
    }

    /**
     * @see de.willuhn.jameica.gui.calendar.Appointment#execute()
     */
    public void execute() throws ApplicationException
    {
      new MitgliedDetailAction().handleAction(this.m);
    }

    /**
     * @see de.willuhn.jameica.gui.calendar.Appointment#getDate()
     */
    public Date getDate()
    {
      try
      {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(m.getGeburtsdatum().getTime());
        cal.set(Calendar.YEAR, year);
        return cal.getTime();
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
    public String getDescription()
    {
      try
      {
        return i18n.tr("Geburtstag von {0}", m.getNameVorname());
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
    public String getName()
    {
      try
      {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(m.getGeburtsdatum().getTime());
        return i18n.tr("{0}. Geburtstag von {1}", year - cal.get(Calendar.YEAR)
            + "", m.getNameVorname());
      }
      catch (RemoteException re)
      {
        Logger.error("unable to build name", re);
        return i18n.tr("Mitgliedergeburtstag");
      }
    }

    /**
     * @see de.willuhn.jameica.gui.calendar.Appointment#getColor()
     */
    public RGB getColor()
    {
      return new RGB(122, 122, 122);
    }

    /**
     * @see de.willuhn.jameica.gui.calendar.AbstractAppointment#getUid()
     */
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
    public boolean hasAlarm()
    {
      return false;
    }
  }

}
