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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;

import com.itextpdf.text.DocumentException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.server.MitgliedUtils;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.util.ProgressMonitor;

public abstract class StatistikDSBExport implements Exporter
{

  @Override
  public abstract String getName();

  @Override
  public abstract IOFormat[] getIOFormats(Class<?> objectType);

  protected File file;

  protected Date stichtag;

  protected TreeMap<String, StatistikDSBJahrgang> statistik;

  @Override
  public void doExport(final Object[] objects, IOFormat format, File file,
      ProgressMonitor monitor) throws DocumentException, IOException
  {
    this.file = file;
    statistik = new TreeMap<String, StatistikDSBJahrgang>();
    MitgliedControl control = (MitgliedControl) objects[0];
    stichtag = (Date) control.getStichtag().getValue();
    /*
     * Teil 1: natürliche Personen
     */
    DBIterator mitgl = Einstellungen.getDBService().createList(Mitglied.class);
    MitgliedUtils.setNurAktive(mitgl, stichtag);
    MitgliedUtils.setMitglied(mitgl);
    MitgliedUtils.setMitgliedNatuerlichePerson(mitgl);
    mitgl.addFilter("geburtsdatum is not null");
    mitgl.setOrder("order by geburtsdatum");
    Calendar cal = Calendar.getInstance();
    while (mitgl.hasNext())
    {
      Mitglied m = (Mitglied) mitgl.next();
      cal.setTime(m.getGeburtsdatum());
      String jg = cal.get(Calendar.YEAR) + "";
      StatistikDSBJahrgang dsbj = statistik.get(jg);
      if (dsbj == null)
      {
        dsbj = new StatistikDSBJahrgang();
        statistik.put(jg, dsbj);
      }
      dsbj.incrementGesamt();
      if (m.getGeschlecht().equals("m"))
      {
        dsbj.incrementMaennlich();
      }
      if (m.getGeschlecht().equals("w"))
      {
        dsbj.incrementWeiblich();
      }
    }
    /*
     * Teil 2: Juristische Personen
     */
    mitgl = Einstellungen.getDBService().createList(Mitglied.class);
    MitgliedUtils.setNurAktive(mitgl, stichtag);
    MitgliedUtils.setMitglied(mitgl);
    MitgliedUtils.setMitgliedJuristischePerson(mitgl);
    while (mitgl.hasNext())
    {
      String jg = "juristische Personen";
      StatistikDSBJahrgang dsbj = statistik.get(jg);
      if (dsbj == null)
      {
        dsbj = new StatistikDSBJahrgang();
        statistik.put(jg, dsbj);
      }
      dsbj.incrementGesamt();
    }

    open();
    close();
  }

  @Override
  public String getDateiname()
  {
    return "statistikdsb";
  }

  protected abstract void open() throws DocumentException,
      FileNotFoundException;

  protected abstract void close() throws IOException, DocumentException;

  public class StatistikDSBJahrgang
  {

    private int anzahlgesamt = 0;

    private int anzahlmaennlich = 0;

    private int anzahlweiblich = 0;

    public int getAnzahlgesamt()
    {
      return anzahlgesamt;
    }

    public void incrementGesamt()
    {
      this.anzahlgesamt++;
    }

    public int getAnzahlmaennlich()
    {
      return anzahlmaennlich;
    }

    public void incrementMaennlich()
    {
      this.anzahlmaennlich++;
    }

    public int getAnzahlweiblich()
    {
      return anzahlweiblich;
    }

    public void incrementWeiblich()
    {
      this.anzahlweiblich++;
    }
  }

}
