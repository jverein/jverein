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
 * $Log$
 * Revision 1.3  2010-09-13 15:26:32  jost
 * Länge des Textes auf 10.000 Zeichen verlängert.
 * http://www.jverein.de/forum/viewtopic.php?f=5&t=194
 *
 * Revision 1.2  2010/02/15 17:24:43  jost
 * Mail-Anhang implementiert
 *
 * Revision 1.1  2010/02/01 21:03:15  jost
 * Neu: Einfache Mailfunktion
 *
 *
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.TreeSet;

import de.jost_net.JVerein.JVereinPlugin;
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
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Bitte Betreff eingeben"));
      }
      if (getTxt() == null || getTxt().length() == 0)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Bitte Text eingeben"));
      }
      if (getTxt().length() > 10000)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Maximale Länge des Textes 10.000 Zeichen"));
      }
    }
    catch (RemoteException e)
    {
      Logger.error("insert check of mailvorlage failed", e);
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "MailVorlage kann nicht gespeichert werden. Siehe system log"));
    }
  }

  @Override
  protected void updateCheck() throws ApplicationException
  {
    insertCheck();
  }

  @Override
  protected Class getForeignObject(String arg0)
  {

    return null;
  }

  public TreeSet<MailEmpfaenger> getEmpfaenger()
  {
    return empfaenger;
  }

  public void setEmpfaenger(TreeSet<MailEmpfaenger> empfaenger)
  {
    this.empfaenger = empfaenger;
  }

  public TreeSet<MailAnhang> getAnhang()
  {
    return anhang;
  }

  public void setAnhang(TreeSet<MailAnhang> anhang)
  {
    this.anhang = anhang;
  }

  public String getBetreff() throws RemoteException
  {
    return (String) getAttribute("betreff");
  }

  public void setBetreff(String betreff) throws RemoteException
  {
    setAttribute("betreff", betreff);
  }

  public String getTxt() throws RemoteException
  {
    return (String) getAttribute("txt");
  }

  public void setTxt(String txt) throws RemoteException
  {
    setAttribute("txt", txt);
  }

  public Timestamp getBearbeitung() throws RemoteException
  {
    return (Timestamp) getAttribute("bearbeitung");
  }

  public void setBearbeitung(Timestamp bearbeitung) throws RemoteException
  {
    setAttribute("bearbeitung", bearbeitung);
  }

  public Timestamp getVersand() throws RemoteException
  {
    return (Timestamp) getAttribute("versand");
  }

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
