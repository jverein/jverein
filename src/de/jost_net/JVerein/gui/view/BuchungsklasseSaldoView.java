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
package de.jost_net.JVerein.gui.view;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collections;
import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.BuchungsklasseSaldoControl;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class BuchungsklasseSaldoView extends AbstractView
{

  private class QuickAccessAction implements Action
  {

    private BuchungsklasseSaldoControl control;

    private Date von;

    private Date bis;

    QuickAccessAction(BuchungsklasseSaldoControl control, Date von, Date bis)
    {
      this.control = control;
      this.von = von;
      this.bis = bis;
    }

    @Override
    public void handleAction(Object context) throws ApplicationException
    {
      control.getDatumvon().setValue(von);
      control.getDatumbis().setValue(bis);
      control.getSaldoList();
    }
  }

  private Integer getYearBounds(String fun) throws Exception
  {
    String sql = "select " + fun + "(datum) as val from buchung";

    DBService service = Einstellungen.getDBService();
    Date earliest = (Date) service.execute(sql,
        Collections.emptyList().toArray(), new ResultSetExtractor()
        {

          @Override
          public Object extract(ResultSet rs)
              throws RemoteException, SQLException
          {
            rs.next();
            return rs.getDate("val");
          }
        });

    Calendar calendar = new GregorianCalendar();
    if (earliest != null)
    {
      calendar.setTime(earliest);
    }
    return calendar.get(Calendar.YEAR);
  }

  public Date deltaDaysFromNow(Integer delta)
  {
    Date now = new Date();
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(now);

    // add 5 days to calendar instance
    calendar.add(Calendar.DAY_OF_MONTH, delta);

    // get the date instance
    return calendar.getTime();
  }

  public Date genYearStartDate(Integer year)
  {
    Calendar calendarStart = Calendar.getInstance();
    calendarStart.set(Calendar.YEAR, year);
    calendarStart.set(Calendar.MONTH, 0);
    calendarStart.set(Calendar.DAY_OF_MONTH, 1);
    return calendarStart.getTime();
  }

  public Date genYearEndDate(Integer year)
  {
    Calendar calendarStart = Calendar.getInstance();
    calendarStart.set(Calendar.YEAR, year);
    calendarStart.set(Calendar.MONTH, 11);
    calendarStart.set(Calendar.DAY_OF_MONTH, 31);
    return calendarStart.getTime();
  }

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Buchungsklassen-Saldo");

    final BuchungsklasseSaldoControl control = new BuchungsklasseSaldoControl(
        this);

    LabelGroup group = new LabelGroup(getParent(), "Zeitraum");
    group.addLabelPair("von", control.getDatumvon());
    group.addLabelPair("bis", control.getDatumbis());

    LabelGroup quickGroup = new LabelGroup(getParent(), "Schnellzugriff");
    ButtonArea quickBtns = new ButtonArea();
    for (Integer i = getYearBounds("min"); i < getYearBounds("max") + 1; i++)
    {
      quickBtns.addButton(i.toString(), new QuickAccessAction(control,
          genYearStartDate(i), genYearEndDate(i)), null, false);
    }

    quickBtns.addButton("Letzte 30 Tage",
        new QuickAccessAction(control, deltaDaysFromNow(-30), new Date()));
    quickBtns.addButton("Letzte 90 Tage",
        new QuickAccessAction(control, deltaDaysFromNow(-90), new Date()));
    quickGroup.addPart(quickBtns);

    ButtonArea buttons = new ButtonArea();
    Button button = new Button("suchen", new Action()
    {
      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        control.getSaldoList();
      }
    }, null, true, "search.png");
    buttons.addButton(button);
    buttons.paint(this.getParent());

    LabelGroup group2 = new LabelGroup(getParent(), "Saldo", true);
    group2.addPart(control.getSaldoList());

    ButtonArea buttons2 = new ButtonArea();
    buttons2.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.JAHRESSALDO, false, "question-circle.png");
    buttons2.addButton(control.getStartAuswertungCSVButton());
    buttons2.addButton(control.getStartAuswertungButton());
    buttons2.paint(this.getParent());
  }
}
