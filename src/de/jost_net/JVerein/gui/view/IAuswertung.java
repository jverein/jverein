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

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;

import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.util.ApplicationException;

/**
 * Interface für alle Mitgliederauswertungen
 * 
 * Die implementierende Klasse muss das MitgliedControl im Konstruktor
 * übergeben.
 * 
 * @author heiner
 * 
 */
public interface IAuswertung
{

  /**
   * @return Dateiname ohne Endung
   */
  public String getDateiname();

  /**
   * @return Endung des Dateinamens
   */
  public String getDateiendung();

  /**
   * Vorbereitende Arbeiten
   */
  public void beforeGo() throws RemoteException;

  /**
   * Startet die Auswertung
   */
  public void go(ArrayList<Mitglied> list, File file)
      throws ApplicationException;

  /**
   * Bezeichnung der Auswertung für die Drop-Down-Liste
   */
  @Override
  public String toString();

  /**
   * Datei nach der Erzeugung öffnen
   */
  public boolean openFile();
}
