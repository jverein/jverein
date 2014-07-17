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

import de.willuhn.datasource.GenericObject;

public class IBANUpdate implements GenericObject
{
  private String id;

  private String name;

  private String blz;

  private String konto;

  private String iban;

  private String bic;

  private String status;

  public IBANUpdate(String id, String name, String blz, String konto,
      String iban, String bic, String status) 
  {
    this.id = id;
    this.name = name;
    this.blz = blz;
    this.konto = konto;
    this.iban = iban;
    this.bic = bic;
    this.status = status;
  }

  @Override
  public boolean equals(GenericObject arg0) throws RemoteException
  {
    return id.equals(arg0.getID());
  }

  @Override
  public Object getAttribute(String arg0)
  {
    if (arg0.equals("id"))
    {
      return id;
    }
    else if (arg0.equals("name"))
    {
      return name;
    }
    else if (arg0.equals("blz"))
    {
      return blz;
    }
    else if (arg0.equals("konto"))
    {
      return konto;
    }
    else if (arg0.equals("iban"))
    {
      return iban;
    }
    else if (arg0.equals("bic"))
    {
      return bic;
    }
    else if (arg0.equals("status"))
    {
      return status;
    }
    return null;
  }

  @Override
  public String[] getAttributeNames()
  {
    return new String[] { "id", "name", "blz", "konto", "iban", "bic", "status" };
  }

  @Override
  public String getID()
  {
    return id;
  }

  @Override
  public String getPrimaryAttribute() throws RemoteException
  {
    return name;
  }

  public boolean getStatusError()
  {
    return status != null && !status.equals("00");
  }

  public void setIban(String iban)
  {
    this.iban = iban;
  }

  public void setBic(String bic)
  {
    this.bic = bic;
  }

  public void setStatus(String status)
  {
    this.status = status;
  }

}
