/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * heiner@jverein.de
 * www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;

import de.jost_net.JVerein.rmi.Mail;
import de.jost_net.JVerein.rmi.MailAnhang;
import de.willuhn.datasource.db.AbstractDBObject;

public class MailAnhangImpl extends AbstractDBObject implements MailAnhang,
    Comparable<MailAnhang>
{

  private static final long serialVersionUID = 1L;

  public MailAnhangImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "mailanhang";
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
    // try
    // {
    // if (getBetreff() == null || getBetreff().length() == 0)
    // {
    // throw new ApplicationException(JVereinPlugin.getI18n().tr(
    // "Bitte Betreff eingeben"));
    // }
    // }
    // catch (RemoteException e)
    // {
    // Logger.error("insert check of mailvorlage failed", e);
    // throw new ApplicationException(JVereinPlugin.getI18n().tr(
    // "MailVorlage kann nicht gespeichert werden. Siehe system log"));
    // }
  }

  @Override
  protected void updateCheck()
  {
    insertCheck();
  }

  @Override
  protected Class<?> getForeignObject(String arg0)
  {
    if ("mail".equals(arg0))
    {
      return Mail.class;
    }
    return null;
  }

  public Mail getMail() throws RemoteException
  {
    return (Mail) getAttribute("mail");
  }

  public void setMail(Mail mail) throws RemoteException
  {
    setAttribute("mail", mail);
  }

  public String getDateiname() throws RemoteException
  {
    return (String) getAttribute("dateiname");
  }

  public void setDateiname(String dateiname) throws RemoteException
  {
    setAttribute("dateiname", dateiname);
  }

  public byte[] getAnhang() throws RemoteException
  {
    return (byte[]) this.getAttribute("anhang");
  }

  public void setAnhang(byte[] anhang) throws RemoteException
  {
    setAttribute("anhang", anhang);
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }

  public int compareTo(MailAnhang o)
  {
    try
    {
      return getDateiname().compareTo(o.getDateiname());
    }
    catch (RemoteException e)
    {
      //
    }
    return 0;
  }

}
