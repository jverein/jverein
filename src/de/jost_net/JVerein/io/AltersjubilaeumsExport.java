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

package de.jost_net.JVerein.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;

import com.itextpdf.text.DocumentException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.server.MitgliedUtils;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public abstract class AltersjubilaeumsExport implements Exporter
{

  @Override
  public abstract String getName();

  @Override
  public abstract IOFormat[] getIOFormats(Class<?> objectType);

  protected File file;

  protected Integer jahr;

  @Override
  public void doExport(Object[] objects, IOFormat format, File file,
      ProgressMonitor monitor)
      throws ApplicationException, DocumentException, IOException
  {
    this.file = file;
    MitgliedControl control = (MitgliedControl) objects[0];
    jahr = control.getJJahr();
    Logger.debug(String.format("Altersjubiläumexport, Jahr=%d", jahr));
    open();
    JubilaeenParser jp = new JubilaeenParser(
        Einstellungen.getEinstellung().getAltersjubilaeen());
    while (jp.hasNext())
    {
      int jubi = jp.getNext();
      startJahrgang(jubi);

      DBIterator<Mitglied> mitgl = Einstellungen.getDBService()
          .createList(Mitglied.class);
      MitgliedUtils.setNurAktive(mitgl);
      MitgliedUtils.setMitglied(mitgl);
      Calendar cal = Calendar.getInstance();
      cal.set(Calendar.YEAR, jahr);
      cal.add(Calendar.YEAR, jubi * -1);
      cal.set(Calendar.MONTH, Calendar.JANUARY);
      cal.set(Calendar.DAY_OF_MONTH, 1);
      Date von = cal.getTime();
      mitgl.addFilter("geburtsdatum >= ?",
          new Object[] { new java.sql.Date(von.getTime()) });

      cal.set(Calendar.MONTH, Calendar.DECEMBER);
      cal.set(Calendar.DAY_OF_MONTH, 31);
      Date bis = cal.getTime();
      mitgl.addFilter("geburtsdatum <= ?",
          new Object[] { new java.sql.Date(bis.getTime()) });
      mitgl.setOrder("order by geburtsdatum");
      while (mitgl.hasNext())
      {
        add((Mitglied) mitgl.next());
      }
      endeJahrgang();
    }
    close();
  }

  @Override
  public String getDateiname()
  {
    return "altersjubilare";
  }

  protected abstract void startJahrgang(int jahr) throws DocumentException;

  protected abstract void endeJahrgang() throws DocumentException;

  protected abstract void open()
      throws DocumentException, FileNotFoundException;

  protected abstract void add(Mitglied m) throws RemoteException;

  protected abstract void close()
      throws IOException, DocumentException, ApplicationException;
}
