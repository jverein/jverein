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
 * Revision 1.6  2008/11/29 13:17:11  jost
 * Refactoring: Warnungen beseitigt.
 *
 * Revision 1.5  2008/09/21 08:46:38  jost
 * Neu: AltersjubliÃ¤en
 *
 * Revision 1.4  2007/12/22 08:27:30  jost
 * Neu: JubilÃ¤enliste
 *
 * Revision 1.3  2007/02/23 20:28:41  jost
 * Mail- und Webadresse im Header korrigiert.
 *
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
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.io.AltersgruppenParser;
import de.jost_net.JVerein.io.JubilaeenParser;
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
    throw new ApplicationException(JVereinPlugin.getI18n().tr(
        "Der Stammdatensatz darf nicht gelöscht werden"));
  }

  protected void insertCheck() throws ApplicationException
  {
    try
    {
      if (getName() == null || getName().length() == 0)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Bitte Namen eingeben"));
      }
      if (getBlz() == null || getBlz().length() == 0)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Bitte Bankleitzahl eingeben"));
      }
      if (getKonto() == null || getKonto().length() == 0)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Bitte Kontonummer eingeben"));
      }
      if (!Einstellungen.checkAccountCRC(getBlz(), getKonto()))
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Ungültige BLZ/Kontonummer. Bitte prüfen Sie Ihre Eingaben."));
      }
      try
      {
        new AltersgruppenParser(getAltersgruppen());
      }
      catch (RuntimeException e)
      {
        throw new ApplicationException(e.getMessage());
      }
      try
      {
        new JubilaeenParser(getJubilaeen());
      }
      catch (RuntimeException e)
      {
        throw new ApplicationException(e.getMessage());
      }
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Stammdaten können nicht gespeichert werden. Siehe system log");
      Logger.error(fehler, e);
      throw new ApplicationException(fehler);
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

  public String getJubilaeen() throws RemoteException
  {
    String ag = (String) getAttribute("jubilaeen");
    if (ag == null || ag.length() == 0)
    {
      ag = "10,25,40,50";
    }
    return ag;
  }

  public void setJubilaeen(String jubilaeen) throws RemoteException
  {
    setAttribute("jubilaeen", jubilaeen);
  }

  public String getAltersjubilaeen() throws RemoteException
  {
    String aj = (String) getAttribute("altersjubilaeen");
    if (aj == null || aj.length() == 0)
    {
      aj = "50,60,65,70,75,80,85,90,95,100";
    }
    return aj;
  }

  public void setAltersjubilaeen(String altersjubilaeen) throws RemoteException
  {
    setAttribute("altersjubilaeen", altersjubilaeen);
  }

  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }
}
