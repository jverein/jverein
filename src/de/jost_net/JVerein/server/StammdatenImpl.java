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
 * Revision 1.2  2006/10/29 07:50:38  jost
 * Neu: Mitgliederstatistik
 *
 * Revision 1.1  2006/09/20 15:39:48  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.io.AltersgruppenParser;
import de.jost_net.JVerein.rmi.Stammdaten;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class StammdatenImpl extends AbstractDBObject implements Stammdaten
{
  private static final long serialVersionUID = 1L;

  public StammdatenImpl() throws RemoteException
  {
    super();
  }

  protected String getTableName()
  {
    return "stammdaten";
  }

  public String getPrimaryAttribute() throws RemoteException
  {
    return "id";
  }

  protected void deleteCheck() throws ApplicationException
  {
    throw new ApplicationException(
        "Der Stammdatensatz darf nicht gelöscht werden");
  }

  protected void insertCheck() throws ApplicationException
  {
    try
    {
      if (getName() == null || getName().length() == 0)
      {
        throw new ApplicationException("Bitte Namen eingeben");
      }
      if (getBlz() == null || getBlz().length() == 0)
      {
        throw new ApplicationException("Bitte Bankleitzahl eingeben");
      }
      if (getKonto() == null || getKonto().length() == 0)
      {
        throw new ApplicationException("Bitte Kontonummer eingeben");
      }
      if (!Einstellungen.checkAccountCRC(getBlz(), getKonto()))
      {
        throw new ApplicationException(
            "Ungültige BLZ/Kontonummer. Bitte prüfen Sie Ihre Eingaben.");
      }
      try
      {
        new AltersgruppenParser(getAltersgruppen());
      }
      catch (RuntimeException e)
      {
        throw new ApplicationException(e.getMessage());
      }
    }
    catch (RemoteException e)
    {
      String fehler = "Stammdaten können nicht gespeichert werden. Siehe system log";
      Logger.error(fehler, e);
      throw new ApplicationException(fehler);
    }
  }

  protected void updateCheck() throws ApplicationException
  {
    insertCheck();
  }

  protected Class getForeignObject(String arg0) throws RemoteException
  {
    return null;
  }

  public String getName() throws RemoteException
  {
    return (String) getAttribute("name");
  }

  public void setName(String name) throws RemoteException
  {
    setAttribute("name", name);
  }

  public String getBlz() throws RemoteException
  {
    return (String) getAttribute("blz");
  }

  public void setBlz(String blz) throws RemoteException
  {
    setAttribute("blz", blz);
  }

  public String getKonto() throws RemoteException
  {
    return (String) getAttribute("konto");
  }

  public void setKonto(String konto) throws RemoteException
  {
    setAttribute("konto", konto);
  }

  public String getAltersgruppen() throws RemoteException
  {
    return (String) getAttribute("altersgruppen");
  }

  public void setAltersgruppen(String altersgruppen) throws RemoteException
  {
    setAttribute("altersgruppen", altersgruppen);
  }

  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }
}
