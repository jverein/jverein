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
 * Revision 1.3  2011-01-30 22:57:33  jost
 * Bugfix
 *
 * Revision 1.2  2011-01-20 18:26:15  jost
 * AppointmentCode Hibiscus -> Jameica
 *
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
      list.addFilter("geburtsdatum is not null");
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

    private Date datum = null;

    private int alter = -1;

    private MyAppointment(Mitglied m, Date datum, int alter)
    {
      this.m = m;
      this.datum = datum;
      this.alter = alter;
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
      return datum;
    }

    /**
     * @see de.willuhn.jameica.gui.calendar.Appointment#getDescription()
     */
    public String getDescription()
    {
      try
      {
        return i18n.tr("{0}. Geburtstag von {1}", alter + "",
            m.getNameVorname());
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
        return i18n.tr("{0}. Geburtstag von {1}", alter + "",
            m.getNameVorname());
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
      return new RGB(255, 178, 31);
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
