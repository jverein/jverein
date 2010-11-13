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
 * Revision 1.10  2010-10-15 09:58:28  jost
 * Code aufgeräumt
 *
 * Revision 1.9  2009-09-10 18:19:47  jost
 * neu: Buchungsklassen
 *
 * Revision 1.8  2009/06/11 21:04:24  jost
 * Vorbereitung I18N
 *
 * Revision 1.7  2009/01/04 16:28:02  jost
 * Neu: Für mehrere Buchungen gleichzeitig die Buchungsart festlegen.
 *
 * Revision 1.6  2008/11/29 13:15:19  jost
 * Refactoring: Warnungen beseitigt.
 *
 * Revision 1.5  2008/05/22 06:56:17  jost
 * BuchfÃ¼hrung
 *
 * Revision 1.4  2008/03/16 07:38:12  jost
 * Reaktivierung BuchfÃ¼hrung
 *
 * Revision 1.2  2007/02/23 20:28:41  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.1  2006/09/20 15:39:48  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.jost_net.JVerein.rmi.Buchungsklasse;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class BuchungsartImpl extends AbstractDBObject implements Buchungsart
{

  private static final long serialVersionUID = 500102542884220658L;

  public BuchungsartImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "buchungsart";
  }

  @Override
  public String getPrimaryAttribute()
  {
    return "bezeichnung";
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
      if (getBezeichnung() == null || getBezeichnung().length() == 0)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Bitte Bezeichnung eingeben"));
      }
      if (getNummer() < 0)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Nummer nicht gültig"));
      }
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Buchungsart kann nicht gespeichert werden. Siehe system log");
      Logger.error(fehler, e);
      throw new ApplicationException(fehler);
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
    if ("buchungsklasse".equals(arg0))
    {
      return Buchungsklasse.class;
    }
    return null;
  }

  public String getBezeichnung() throws RemoteException
  {
    return (String) getAttribute("bezeichnung");
  }

  public void setBezeichnung(String bezeichnung) throws RemoteException
  {
    setAttribute("bezeichnung", bezeichnung);
  }

  public int getNummer() throws RemoteException
  {
    Integer i = (Integer) getAttribute("nummer");
    if (i == null)
      return 0;
    return i.intValue();
  }

  public void setNummer(int i) throws RemoteException
  {
    setAttribute("nummer", new Integer(i));
  }

  public int getArt() throws RemoteException
  {
    Integer i = (Integer) getAttribute("art");
    if (i == null)
    {
      return 0;
    }
    return i.intValue();
  }

  public void setArt(int art) throws RemoteException
  {
    setAttribute("art", art);
  }

  public Buchungsklasse getBuchungsklasse() throws RemoteException
  {
    return (Buchungsklasse) getAttribute("buchungsklasse");
  }

  public int getBuchungsklasseId() throws RemoteException
  {
    return Integer.parseInt(getBuchungsklasse().getID());
  }

  public void setBuchungsklasse(Integer buchungsklasse) throws RemoteException
  {
    setAttribute("buchungsklasse", buchungsklasse);
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }
}
