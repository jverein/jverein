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
package de.jost_net.JVerein.rmi;

import java.rmi.RemoteException;

import de.willuhn.datasource.rmi.DBObject;

public interface Formularfeld extends DBObject
{
  public void setID(String id) throws RemoteException;

  public Formular getFormular() throws RemoteException;

  public void setFormular(Formular formular) throws RemoteException;

  public String getName() throws RemoteException;

  public void setName(String name) throws RemoteException;

  public int getSeite() throws RemoteException;

  public void setSeite(int seite) throws RemoteException;

  public Double getX() throws RemoteException;

  public void setX(Double x) throws RemoteException;

  public Double getY() throws RemoteException;

  public void setY(Double y) throws RemoteException;

  public String getFont() throws RemoteException;

  public void setFont(String font) throws RemoteException;

  public Integer getFontsize() throws RemoteException;

  public void setFontsize(Integer fontsize) throws RemoteException;

  public Integer getFontstyle() throws RemoteException;

  public void setFontstyle(Integer fontstyle) throws RemoteException;
}
