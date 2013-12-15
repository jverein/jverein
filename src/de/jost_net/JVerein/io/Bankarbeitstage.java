package de.jost_net.JVerein.io;

import java.util.Calendar;

import de.jollyday.HolidayManager;

public class Bankarbeitstage
{
  private HolidayManager m;

  public Bankarbeitstage()
  {
    m = HolidayManager.getInstance("Bankfeiertage");
  }

  public Calendar getCalendar(Calendar from, int anzahl)
  {
    for (int i = 0; i < anzahl; i++)
    {
      from.add(Calendar.DAY_OF_YEAR, 1);
      while (from.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
          || from.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
          || m.isHoliday(from, ""))
      {
        from.add(Calendar.DAY_OF_YEAR, 1);
      }
    }
    return from;
  }

  // public static void main(String[] args)
  // {
  // Bankarbeitstage bat = new Bankarbeitstage();
  // Calendar cal = Calendar.getInstance();
  // cal.set(2013, Calendar.DECEMBER, 30);
  // System.out.println(bat.getDate(cal.getTime(), 1));
  // }
}
