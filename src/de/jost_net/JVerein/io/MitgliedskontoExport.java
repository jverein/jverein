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
import java.util.Date;

import com.itextpdf.text.DocumentException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.Queries.MitgliedskontoQuery;
import de.jost_net.JVerein.gui.action.MitgliedskontoExportAction.EXPORT_TYP;
import de.jost_net.JVerein.gui.control.MitgliedskontoControl.DIFFERENZ;
import de.jost_net.JVerein.io.Adressbuch.Adressaufbereitung;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.util.ProgressMonitor;

public abstract class MitgliedskontoExport implements Exporter
{

  @Override
  public abstract String getName();

  @Override
  public abstract IOFormat[] getIOFormats(Class<?> objectType);

  protected EXPORT_TYP exportTyp = EXPORT_TYP.MITGLIEDSKONTO;

  protected File file;

  protected Date vonDatum;

  protected Date bisDatum;

  protected DIFFERENZ differenz;

  protected Boolean ohneAbbucher;

  protected Mitglied selectedMitglied;

  @Override
  public void doExport(Object[] objects, IOFormat format, File file,
      ProgressMonitor monitor) throws DocumentException, IOException
  {
    this.file = file;
    vonDatum = (Date) objects[0];
    bisDatum = (Date) objects[1];
    differenz = (DIFFERENZ) objects[2];
    ohneAbbucher = (Boolean) objects[3];
    selectedMitglied = (Mitglied) objects[4];
    open();

    DBIterator<Mitglied> mitgl = Einstellungen.getDBService()
        .createList(Mitglied.class);
    if (null != selectedMitglied)
      mitgl.addFilter("id = ? ", selectedMitglied.getID());
    mitgl.setOrder("ORDER BY name, vorname");

    while (mitgl.hasNext())
    {
      Mitglied m = mitgl.next();
      startMitglied(m);
      MitgliedskontoQuery mkq = new MitgliedskontoQuery(m, vonDatum, bisDatum,
          differenz, ohneAbbucher);
      for (Mitgliedskonto mk : mkq.get())
      {
        add(mk);
        monitor.log("Vorbereitung: " + Adressaufbereitung.getNameVorname(m));
      }
      endeMitglied();
    }
    close(monitor);
  }

  public void setExportTyp(EXPORT_TYP typ)
  {
    exportTyp = typ;
  }

  @Override
  public String getDateiname()
  {
    return exportTyp.getDateiName();
  }

  protected abstract void startMitglied(Mitglied m) throws DocumentException;

  protected abstract void endeMitglied() throws DocumentException;

  protected abstract void open()
      throws DocumentException, FileNotFoundException;

  protected abstract void add(Mitgliedskonto mk) throws RemoteException;

  protected abstract void close(ProgressMonitor monitor)
      throws IOException, DocumentException;
}
