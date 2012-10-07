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

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import com.itextpdf.text.DocumentException;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class AltersjubilaeumsExportCSV extends AltersjubilaeumsExport
{
  private ArrayList<Mitglied> mitglieder = new ArrayList<Mitglied>();

  private int jahrgang;

  @Override
  public String getName()
  {
    return JVereinPlugin.getI18n().tr("Altersjubilare CSV-Export");
  }

  @Override
  public IOFormat[] getIOFormats(Class<?> objectType)
  {
    if (objectType != Mitglied.class)
    {
      return null;
    }
    IOFormat f = new IOFormat()
    {
      public String getName()
      {
        return AltersjubilaeumsExportCSV.this.getName();
      }

      /**
       * @see de.willuhn.jameica.hbci.io.IOFormat#getFileExtensions()
       */
      public String[] getFileExtensions()
      {
        return new String[] { "*.csv" };
      }
    };
    return new IOFormat[] { f };
  }

  @Override
  public String getDateiname()
  {
    return JVereinPlugin.getI18n().tr("altersjubilare");
  }

  @Override
  protected void open() throws DocumentException, FileNotFoundException
  {
    //
  }

  @Override
  protected void startJahrgang(int jahrgang)
  {
    this.jahrgang = jahrgang;
  }

  @Override
  protected void endeJahrgang()
  {
    //
  }

  @Override
  protected void add(Mitglied m) throws RemoteException
  {
    m.addVariable("altersjubilaeum", jahrgang + "");
    mitglieder.add(m);
  }

  @Override
  protected void close() throws ApplicationException
  {
    Logger.debug(JVereinPlugin.getI18n().tr(
        "Alterjubiläum-CSV-Export, Jahr={0}", jahr + ""));
    MitgliedAuswertungCSV mcsv = new MitgliedAuswertungCSV();
    mcsv.go(mitglieder, file);
  }
}
