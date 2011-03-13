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
 * Revision 1.8  2011-03-07 21:09:50  jost
 * Neu:  Automatische Spendenbescheinigungen; Referenz zum Mitglied aufgenommen
 *
 * Revision 1.7  2011-02-12 09:43:37  jost
 * Statische Codeanalyse mit Findbugs
 *
 * Revision 1.6  2010-11-13 09:31:24  jost
 * Warnings entfernt.
 *
 * Revision 1.5  2010-10-15 09:58:28  jost
 * Code aufgeräumt
 *
 * Revision 1.4  2009-06-11 21:04:23  jost
 * Vorbereitung I18N
 *
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
import de.jost_net.JVerein.keys.HerkunftSpende;
import de.jost_net.JVerein.keys.Spendenart;
import de.jost_net.JVerein.rmi.Formular;
import de.jost_net.JVerein.rmi.Mitglied;
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

  @Override
  protected String getTableName()
  {
    return "spendenbescheinigung";
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

  @Override
  protected void updateCheck() throws ApplicationException
  {
    insertCheck();
  }

  @Override
  protected Class<?> getForeignObject(String field)
  {
    if ("formular".equals(field))
    {
      return Formular.class;
    }
    if ("mitglied".equals(field))
    {
      return Mitglied.class;
    }

    return null;
  }

  public int getSpendenart() throws RemoteException
  {
    Integer ret = (Integer) getAttribute("spendenart");
    if (ret == null)
    {
      ret = Spendenart.GELDSPENDE;
    }
    return ret;
  }

  public void setSpendenart(int spendenart) throws RemoteException
  {
    setAttribute("spendenart", spendenart);
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
    setAttribute("ersatzaufwendungen", Boolean.valueOf(ersatzaufwendungen));
  }

  public String getBezeichnungSachzuwendung() throws RemoteException
  {
    return (String) getAttribute("bezeichnungsachzuwendung");
  }

  public void setBezeichnungSachzuwendung(String bezeichnungsachzuwendung)
      throws RemoteException
  {
    setAttribute("bezeichnungsachzuwendung", bezeichnungsachzuwendung);
  }

  public int getHerkunftSpende() throws RemoteException
  {
    Integer ret = (Integer) getAttribute("herkunftspende");
    if (ret == null)
    {
      ret = HerkunftSpende.KEINEANGABEN;
    }
    return ret;
  }

  public void setHerkunftSpende(int herkunftspende) throws RemoteException
  {
    setAttribute("herkunftspende", herkunftspende);
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }

  public Mitglied getMitglied() throws RemoteException
  {
    return (Mitglied) getAttribute("mitglied");
  }

  public int getMitgliedID() throws RemoteException
  {
    return Integer.parseInt(getMitglied().getID());
  }

  public void setMitgliedID(Integer mitgliedID) throws RemoteException
  {
    setAttribute("mitglied", mitgliedID);
  }

  public void setMitglied(Mitglied mitglied) throws RemoteException
  {
    if (mitglied != null)
    {
      setAttribute("mitglied", new Integer(mitglied.getID()));
    }
    else
    {
      setAttribute("mitglied", null);
    }
  }

  public boolean getUnterlagenWertermittlung() throws RemoteException
  {
    return Util.getBoolean(getAttribute("unterlagenwertermittlung"));
  }

  public void setUnterlagenWertermittlung(Boolean unterlagenwertermittlung)
      throws RemoteException
  {
    setAttribute("unterlagenwertermittlung",
        Boolean.valueOf(unterlagenwertermittlung));
  }

}
