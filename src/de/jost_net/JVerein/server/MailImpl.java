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

  protected String getTableName()
  {
    return "mail";
  }

  public String getPrimaryAttribute() throws RemoteException
  {
    return "id";
  }

  protected void deleteCheck() throws ApplicationException
  {
  }

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
      if ( getTxt().length()>10000)
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

  protected void updateCheck() throws ApplicationException
  {
    insertCheck();
  }

  @SuppressWarnings("unchecked")
  protected Class getForeignObject(String arg0) throws RemoteException
  {

    return null;
  }

  public TreeSet<MailEmpfaenger> getEmpfaenger() throws RemoteException
  {
    return empfaenger;
  }

  public void setEmpfaenger(TreeSet<MailEmpfaenger> empfaenger)
      throws RemoteException
  {
    this.empfaenger = empfaenger;
  }

  public TreeSet<MailAnhang> getAnhang() throws RemoteException
  {
    return anhang;
  }

  public void setAnhang(TreeSet<MailAnhang> anhang) throws RemoteException
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

  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }
}
