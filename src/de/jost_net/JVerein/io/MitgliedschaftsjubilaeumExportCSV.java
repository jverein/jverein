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

import java.rmi.RemoteException;
import java.util.ArrayList;

import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class MitgliedschaftsjubilaeumExportCSV
    extends MitgliedschaftsjubilaeumsExport
{

  private ArrayList<Mitglied> mitglieder = new ArrayList<>();

  private int jahrgang;

  @Override
  public String getName()
  {
    return "Mitgliedschaftsjubilare CSV-Export";
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

      @Override
      public String getName()
      {
        return MitgliedschaftsjubilaeumExportCSV.this.getName();
      }

      /**
       * @see de.willuhn.jameica.hbci.io.IOFormat#getFileExtensions()
       */
      @Override
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
    return "mitgliedschaftsjubilare";
  }

  @Override
  protected void open()
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
    m.addVariable("mitgliedsschaftsjubilaeum", jahrgang + "");
    mitglieder.add(m);
  }

  @Override
  protected void close()
  {
    MitgliedAuswertungCSV mcsv = new MitgliedAuswertungCSV();
    try
    {
      mcsv.go(mitglieder, file);
    }
    catch (ApplicationException e)
    {
      Logger.error("Fehler", e);
    }
  }
}