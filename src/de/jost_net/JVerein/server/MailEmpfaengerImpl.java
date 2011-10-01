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

  public Mail getMail() throws RemoteException
  {
    return (Mail) getAttribute("mail");
  }

  public void setMail(Mail mail) throws RemoteException
  {
    setAttribute("mail", mail);
  }

  public Mitglied getMitglied() throws RemoteException
  {
    return (Mitglied) getAttribute("mitglied");
  }

  public void setMitglied(Mitglied mitglied) throws RemoteException
  {
    setAttribute("mitglied", mitglied);
  }

  public String getMailAdresse() throws RemoteException
  {
    return getMitglied().getEmail();
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
        return getMitglied().getNameVorname();
      }
      else
      {
        return "";
      }
    }
    return super.getAttribute(fieldName);
  }

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
