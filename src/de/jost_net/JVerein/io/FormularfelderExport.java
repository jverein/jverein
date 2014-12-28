/**********************************************************************
 * JVerein - Mitgliederverwaltung und einfache Buchhaltung für Vereine
 * Copyright (c) by Heiner Jostkleigrewe
 * Copyright (c) 2014 by Thomas Hooge
 * Main Project: heiner@jverein.dem  http://www.jverein.de/
 * Module Author: thomas@hoogi.de, http://www.hoogi.de/
 *
 * This file is part of JVerein.
 *
 * JVerein is free software: you can redistribute it and/or modify 
 * it under the terms of the  GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JVerein is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;

import com.itextpdf.text.DocumentException;

import de.jost_net.JVerein.rmi.Formularfeld;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public abstract class FormularfelderExport implements Exporter
{

	@Override
  public abstract String getName();

  @Override
  public abstract IOFormat[] getIOFormats(Class<?> objectType);

  protected File file;

  @Override
  public void doExport(Object[] objects, IOFormat format, File file,
      ProgressMonitor monitor) throws ApplicationException, DocumentException,
      IOException
  {
    this.file = file;
    Logger.debug(String.format("Formularfelder Export"));
    open();
    for (Object ob : objects)
    {
    	add((Formularfeld) ob);
    }
    close();
  }

  @Override
  public String getDateiname()
  {
    return "formularfelder";
  }

	protected abstract void add(Formularfeld ff) throws RemoteException;

  protected abstract void open() throws DocumentException,
      FileNotFoundException;
 
  protected abstract void close() throws IOException, DocumentException,
      ApplicationException;

}
