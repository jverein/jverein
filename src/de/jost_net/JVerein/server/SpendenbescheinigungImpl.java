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
 * Revision 1.3  2009/01/26 18:48:36  jost
 * Neu: Ersatz Aufwendungen
 *
 * Revision 1.2  2008/11/29 13:17:02  jost
 * Refactoring: Warnungen beseitigt.
 *
 * Revision 1.1  2008/07/18 20:18:45  jost
 * Neu: Spendenbescheinigung
 *
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;
import java.util.Date;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Formular;
import de.jost_net.JVerein.rmi.Spendenbescheinigung;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class SpendenbescheinigungImpl extends AbstractDBObject implements
    Spendenbescheinigung
{
  private static final long serialVersionUID = -1861750218155086064L;

  public SpendenbescheinigungImpl() throws RemoteException
  {
    super();
  }

  protected String getTableName()
  {
    return "spendenbescheinigung";
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
      if (getBetrag().doubleValue() <= 0)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Betrag größer als 0 eingeben."));
      }
      if (getSpendedatum() == null)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Spendedatum fehlt."));
      }
      if (getBescheinigungsdatum() == null)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Datum der Bescheinigung fehlt."));
      }
      if (getZeile1() == null && getZeile2() == null && getZeile3() == null)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Spenderadresse fehlt"));
      }
    }
    catch (RemoteException e)
    {
      Logger.error("Fehler", e);
      throw new ApplicationException("Fehler bei der Plausi");
    }
  }

  protected void updateCheck() throws ApplicationException
  {
    insertCheck();
  }

  @SuppressWarnings("unchecked")
  protected Class getForeignObject(String field) throws RemoteException
  {
    if ("formular".equals(field))
    {
      return Formular.class;
    }
    return null;
  }

  public String getZeile1() throws RemoteException
  {
    return (String) getAttribute("zeile1");
  }

  public void setZeile1(String zeile1) throws RemoteException
  {
    setAttribute("zeile1", zeile1);
  }

  public String getZeile2() throws RemoteException
  {
    return (String) getAttribute("zeile2");
  }

  public void setZeile2(String zeile2) throws RemoteException
  {
    setAttribute("zeile2", zeile2);
  }

  public String getZeile3() throws RemoteException
  {
    return (String) getAttribute("zeile3");
  }

  public void setZeile3(String zeile3) throws RemoteException
  {
    setAttribute("zeile3", zeile3);
  }

  public String getZeile4() throws RemoteException
  {
    return (String) getAttribute("zeile4");
  }

  public void setZeile4(String zeile4) throws RemoteException
  {
    setAttribute("zeile4", zeile4);
  }

  public String getZeile5() throws RemoteException
  {
    return (String) getAttribute("zeile5");
  }

  public void setZeile5(String zeile5) throws RemoteException
  {
    setAttribute("zeile5", zeile5);
  }

  public String getZeile6() throws RemoteException
  {
    return (String) getAttribute("zeile6");
  }

  public void setZeile6(String zeile6) throws RemoteException
  {
    setAttribute("zeile6", zeile6);
  }

  public String getZeile7() throws RemoteException
  {
    return (String) getAttribute("zeile7");
  }

  public void setZeile7(String zeile7) throws RemoteException
  {
    setAttribute("zeile7", zeile7);
  }

  public Double getBetrag() throws RemoteException
  {
    Double ret = (Double) getAttribute("betrag");
    if (ret == null)
    {
      ret = new Double(0);
    }
    return ret;
  }

  public void setSpendedatum(Date datum) throws RemoteException
  {
    setAttribute("spendedatum", datum);
  }

  public Date getSpendedatum() throws RemoteException
  {
    return (Date) getAttribute("spendedatum");
  }

  public void setBescheinigungsdatum(Date datum) throws RemoteException
  {
    setAttribute("bescheinigungsdatum", datum);
  }

  public Date getBescheinigungsdatum() throws RemoteException
  {
    return (Date) getAttribute("bescheinigungsdatum");
  }

  public void setBetrag(Double betrag) throws RemoteException
  {
    setAttribute("betrag", betrag);
  }

  public Formular getFormular() throws RemoteException
  {
    return (Formular) getAttribute("formular");
  }

  public void setFormular(Formular formular) throws RemoteException
  {
    setAttribute("formular", formular);
  }

  public boolean getErsatzAufwendungen() throws RemoteException
  {
    return Util.getBoolean(getAttribute("ersatzaufwendungen"));
  }

  public void setErsatzAufwendungen(Boolean ersatzaufwendungen)
      throws RemoteException
  {
    setAttribute("ersatzaufwendungen", new Boolean(ersatzaufwendungen));
  }

  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }
}
