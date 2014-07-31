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
import java.util.TreeSet;

import de.jost_net.JVerein.rmi.Mail;
import de.jost_net.JVerein.rmi.MailAnhang;
import de.jost_net.JVerein.rmi.MailEmpfaenger;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class MailImpl extends AbstractDBObject implements Mail
{

  private static final long serialVersionUID = 1L;

  private TreeSet<MailEmpfaenger> empfaenger = null;

  private TreeSet<MailAnhang> anhang = null;

  public MailImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "mail";
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
  protected void insertCheck() throws ApplicationException
  {
    try
    {
      if (getBetreff() == null || getBetreff().length() == 0)
      {
        throw new ApplicationException("Bitte Betreff eingeben");
      }
      if (getTxt() == null || getTxt().length() == 0)
      {
        throw new ApplicationException("Bitte Text eingeben");
      }
      if (getTxt().length() > 10000)
      {
        throw new ApplicationException(
            "Maximale Länge des Textes 10.000 Zeichen");
      }
    }
    catch (RemoteException e)
    {
      Logger.error("insert check of mailvorlage failed", e);
      throw new ApplicationException(
          "MailVorlage kann nicht gespeichert werden. Siehe system log");
    }
  }

  @Override
  protected void updateCheck() throws ApplicationException
  {
    insertCheck();
  }

  @Override
  protected Class<?> getForeignObject(String arg0)
  {

    return null;
  }

  @Override
  public TreeSet<MailEmpfaenger> getEmpfaenger()
  {
    return empfaenger;
  }

  @Override
  public void setEmpfaenger(TreeSet<MailEmpfaenger> empfaenger)
  {
    this.empfaenger = empfaenger;
  }

  @Override
  public TreeSet<MailAnhang> getAnhang()
  {
    return anhang;
  }

  @Override
  public void setAnhang(TreeSet<MailAnhang> anhang)
  {
    this.anhang = anhang;
  }

  @Override
  public String getBetreff() throws RemoteException
  {
    return (String) getAttribute("betreff");
  }

  @Override
  public void setBetreff(String betreff) throws RemoteException
  {
    setAttribute("betreff", betreff);
  }

  @Override
  public String getTxt() throws RemoteException
  {
    return (String) getAttribute("txt");
  }

  @Override
  public void setTxt(String txt) throws RemoteException
  {
    setAttribute("txt", txt);
  }

  @Override
  public Timestamp getBearbeitung() throws RemoteException
  {
    return (Timestamp) getAttribute("bearbeitung");
  }

  @Override
  public void setBearbeitung(Timestamp bearbeitung) throws RemoteException
  {
    setAttribute("bearbeitung", bearbeitung);
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
    return super.getAttribute(fieldName);
  }
}
