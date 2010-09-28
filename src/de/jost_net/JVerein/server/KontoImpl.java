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
 * Revision 1.6  2010-09-19 16:15:16  jost
 * Länge der Kontobezeichnung auf 255  Zeichen verlängert.
 *
 * Revision 1.5  2009/06/11 21:04:23  jost
 * Vorbereitung I18N
 *
 * Revision 1.4  2008/11/29 13:16:26  jost
 * Refactoring: Warnungen beseitigt.
 *
 * Revision 1.3  2008/06/28 17:07:46  jost
 * Neu: Jahresabschluss
 *
 * Revision 1.2  2008/05/26 18:59:17  jost
 * Neu: ErÃ¶ffnungsdatum
 *
 * Revision 1.1  2008/05/22 06:56:28  jost
 * BuchfÃ¼hrung
 *
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Konto;
import de.jost_net.JVerein.util.Geschaeftsjahr;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class KontoImpl extends AbstractDBObject implements Konto
{
  private static final long serialVersionUID = 1L;

  public KontoImpl() throws RemoteException
  {
    super();
  }

  protected String getTableName()
  {
    return "konto";
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
      plausi();
      DBIterator it = Einstellungen.getDBService().createList(Konto.class);
      it.addFilter("nummer = ?", new Object[] { getNummer() });
      if (it.size() > 0)
      {
        throw new ApplicationException("Konto existiert bereits");
      }
    }
    catch (RemoteException e)
    {
      Logger.error("insert check of konto failed", e);
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Konto kann nicht gespeichert werden. Siehe system log"));
    }
  }

  protected void updateCheck() throws ApplicationException
  {
    plausi();
  }

  private void plausi() throws ApplicationException
  {
    try
    {
      if (getBezeichnung() == null || getBezeichnung().length() == 0)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Bitte Bezeichnung eingeben"));
      }
      if (getBezeichnung().length() > 255)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Maximale Länge der Bezeichnung: 255 Zeichen"));
      }
      if (getNummer() == null || getNummer().length() == 0)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Bitte Nummer eingeben"));
      }
    }
    catch (RemoteException e)
    {
      Logger.error("insert check of konto failed", e);
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Konto kann nicht gespeichert werden. Siehe system log"));
    }

  }

  @SuppressWarnings("unchecked")
  protected Class getForeignObject(String arg0) throws RemoteException
  {
    return null;
  }

  public String getNummer() throws RemoteException
  {
    return (String) getAttribute("nummer");
  }

  public void setNummer(String nummer) throws RemoteException
  {
    setAttribute("nummer", nummer);
  }

  public String getBezeichnung() throws RemoteException
  {
    return (String) getAttribute("bezeichnung");
  }

  public void setBezeichnung(String bezeichnung) throws RemoteException
  {
    setAttribute("bezeichnung", bezeichnung);
  }

  public Date getEroeffnung() throws RemoteException
  {
    return (Date) getAttribute("eroeffnung");
  }

  public void setEroeffnung(Date eroeffnungdatum) throws RemoteException
  {
    setAttribute("eroeffnung", eroeffnungdatum);
  }

  public Date getAufloesung() throws RemoteException
  {
    return (Date) getAttribute("aufloesung");
  }

  public void setAufloesung(Date aufloesungsdatum) throws RemoteException
  {
    setAttribute("aufloesung", aufloesungsdatum);
  }

  public Integer getHibiscusId() throws RemoteException
  {
    return (Integer) getAttribute("hibiscusid");
  }

  public void setHibiscusId(Integer id) throws RemoteException
  {
    setAttribute("hibiscusid", id);
  }

  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }

  public DBIterator getKontenEinesJahres(Geschaeftsjahr gj)
      throws RemoteException
  {
    DBIterator konten = Einstellungen.getDBService().createList(Konto.class);
    konten.addFilter("(eroeffnung is null or eroeffnung <= ?)",
        new Object[] { gj.getEndeGeschaeftsjahr() });
    konten.addFilter("(aufloesung is null or year(aufloesung) = ? or "
        + "aufloesung >= ? )", new Object[] { gj.getBeginnGeschaeftsjahrjahr(),
        gj.getEndeGeschaeftsjahr() });
    konten.setOrder("order by bezeichnung");
    return konten;
  }

}
