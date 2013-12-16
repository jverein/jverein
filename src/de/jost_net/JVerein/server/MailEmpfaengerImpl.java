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
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;
import java.sql.Timestamp;

import de.jost_net.JVerein.io.Adressbuch.Adressaufbereitung;
import de.jost_net.JVerein.rmi.Mail;
import de.jost_net.JVerein.rmi.MailEmpfaenger;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.datasource.db.AbstractDBObject;

public class MailEmpfaengerImpl extends AbstractDBObject implements
    MailEmpfaenger, Comparable<MailEmpfaenger>
{

  private static final long serialVersionUID = 1L;

  public MailEmpfaengerImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "mailempfaenger";
  }

  @Override
  public String getPrimaryAttribute()
  {
    return "id";
  }

  @Override
  protected void deleteCheck()
  {
    //
  }

  @Override
  protected void insertCheck()
  {
    // 
  }

  @Override
  protected void updateCheck()
  {
    insertCheck();
  }

  @Override
  protected Class<?> getForeignObject(String arg0)
  {
    if ("mitglied".equals(arg0))
    {
      return Mitglied.class;
    }
    else if ("mail".equals(arg0))
    {
      return Mail.class;
    }
    return null;
  }

  @Override
  public Mail getMail() throws RemoteException
  {
    return (Mail) getAttribute("mail");
  }

  @Override
  public void setMail(Mail mail) throws RemoteException
  {
    setAttribute("mail", mail);
  }

  @Override
  public Mitglied getMitglied() throws RemoteException
  {
    return (Mitglied) getAttribute("mitglied");
  }

  @Override
  public void setMitglied(Mitglied mitglied) throws RemoteException
  {
    setAttribute("mitglied", mitglied);
  }

  @Override
  public String getMailAdresse() throws RemoteException
  {
    return getMitglied().getEmail();
  }

  @Override
  public Timestamp getVersand() throws RemoteException
  {
    return (Timestamp) getAttribute("versand");
  }

  @Override
  public void setVersand(Timestamp versand) throws RemoteException
  {
    setAttribute("versand", versand);
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    if (fieldName.equals("mailadresse"))
    {
      return getMailAdresse();
    }
    else if (fieldName.equals("name"))
    {
      if (getMitglied() != null)
      {
        return Adressaufbereitung.getNameVorname(getMitglied());
      }
      else
      {
        return "";
      }
    }
    return super.getAttribute(fieldName);
  }

  @Override
  public int compareTo(MailEmpfaenger o)
  {
    try
    {
      return getMitglied().getID().compareTo(o.getMitglied().getID());
    }
    catch (RemoteException e)
    {
      //
    }
    return 0;
  }
}
