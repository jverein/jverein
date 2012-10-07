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
import java.rmi.RemoteException;
import java.util.Date;

import com.itextpdf.text.DocumentException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.Queries.MitgliedskontoQuery;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public abstract class MitgliedskontoExport implements Exporter
{

  public abstract String getName();

  public abstract IOFormat[] getIOFormats(Class<?> objectType);

  protected File file;

  protected Date vonDatum;

  protected Date bisDatum;

  protected String differenz;

  public void doExport(Object[] objects, IOFormat format, File file,
      ProgressMonitor monitor) throws ApplicationException, DocumentException,
      IOException
  {
    this.file = file;
    vonDatum = (Date) objects[0];
    bisDatum = (Date) objects[1];
    differenz = (String) objects[2];
    open();

    DBIterator mitgl = Einstellungen.getDBService().createList(Mitglied.class);
    mitgl.setOrder("ORDER BY name, vorname");

    while (mitgl.hasNext())
    {
      Mitglied m = (Mitglied) mitgl.next();
      startMitglied(m);
      MitgliedskontoQuery mkq = new MitgliedskontoQuery(m, vonDatum, bisDatum,
          differenz);
      for (Mitgliedskonto mk : mkq.get())
      {
        add(mk);
        monitor.log(JVereinPlugin.getI18n().tr("Vorbereitung: {0}",
            m.getNameVorname()));
      }
      endeMitglied();
    }
    close(monitor);
  }

  public String getDateiname()
  {
    return JVereinPlugin.getI18n().tr("mitgliedskonten");
  }

  protected abstract void startMitglied(Mitglied m) throws DocumentException;

  protected abstract void endeMitglied() throws DocumentException;

  protected abstract void open() throws DocumentException,
      FileNotFoundException;

  protected abstract void add(Mitgliedskonto mk) throws RemoteException;

  protected abstract void close(ProgressMonitor monitor) throws IOException,
      DocumentException;
}
