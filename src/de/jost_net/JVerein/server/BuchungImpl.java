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
 * Revision 1.19  2011-03-07 21:08:46  jost
 * Neu:  Automatische Spendenbescheinigungen: Referenz zur Spendenbescheinigung aufgenommen.
 *
 * Revision 1.18  2011-02-15 20:55:45  jost
 * Colins Patch zur Performancesteigerung
 *
 * Revision 1.17  2011-02-12 09:42:33  jost
 * Statische Codeanalyse mit Findbugs
 *
 * Revision 1.16  2010-12-27 13:58:44  jost
 * Splitid
 *
 * Revision 1.15  2010-11-13 09:29:39  jost
 * Warnings entfernt.
 *
 * Revision 1.14  2010-10-15 09:58:27  jost
 * Code aufger�umt
 *
 * Revision 1.13  2010-09-01 05:58:19  jost
 * Bugfix numerische Sortierung
 *
 * Revision 1.12  2010-08-27 17:59:23  jost
 * Vermeidung NPE
 *
 * Revision 1.11  2010-07-25 18:46:52  jost
 * Neu: Mitgliedskonto
 *
 * Revision 1.10  2009/06/11 21:04:23  jost
 * Vorbereitung I18N
 *
 * Revision 1.9  2008/12/03 22:01:02  jost
 * Erweiterung um Auszugs- und Blattnummer
 *
 * Revision 1.8  2008/11/29 13:15:09  jost
 * Refactoring: Warnungen beseitigt.
 *
 * Revision 1.7  2008/06/28 17:07:15  jost
 * Bearbeiten nur, wenn kein Jahresabschluss vorliegt.
 *
 * Revision 1.6  2008/05/24 16:40:39  jost
 * Wegfall der Spalte Saldo
 *
 * Revision 1.5  2008/05/22 06:56:05  jost
 * Buchführung
 *
 * Revision 1.4  2008/03/16 07:38:12  jost
 * Reaktivierung Buchführung
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
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Abrechnungslauf;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.jost_net.JVerein.rmi.Jahresabschluss;
import de.jost_net.JVerein.rmi.Konto;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.jost_net.JVerein.rmi.Spendenbescheinigung;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class BuchungImpl extends AbstractDBObject implements Buchung
{

  private static final long serialVersionUID = 1L;

  public BuchungImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "buchung";
  }

  @Override
  public String getPrimaryAttribute()
  {
    return "id";
  }

  @Override
  protected void deleteCheck() throws ApplicationException
  {
    insertCheck();
  }

  @Override
  protected void insertCheck() throws ApplicationException
  {
    try
    {
      plausi();
    }
    catch (RemoteException e)
    {
      Logger.error("insert check of buchung failed", e);
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Buchung kann nicht gespeichert werden. Siehe system log"));
    }
  }

  private void plausi() throws RemoteException, ApplicationException
  {
    if (getKonto() == null)
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Bitte Konto eingeben"));
    }
    if (getDatum() == null)
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Bitte Datum eingeben"));
    }
    Jahresabschluss ja = getJahresabschluss();
    if (ja != null)
    {
      throw new ApplicationException(
          JVereinPlugin
              .getI18n()
              .tr("Buchung kann nicht gespeichert werden. Zeitraum ist bereits abgeschlossen!"));
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
    if ("mitgliedskonto".equals(field))
    {
      return Mitgliedskonto.class;
    }
    else if ("abrechnungslauf".equals(field))
    {
      return Abrechnungslauf.class;
    }
    else if ("spendenbescheinigung".equals(field))
    {
      return Spendenbescheinigung.class;
    }
    return null;
  }

  public Integer getUmsatzid() throws RemoteException
  {

    return (Integer) getAttribute("umsatzid");
  }

  public void setUmsatzid(Integer umsatzid) throws RemoteException
  {
    setAttribute("umsatzid", umsatzid);
  }

  public Konto getKonto() throws RemoteException
  {
    Integer i = (Integer) super.getAttribute("konto");
    if (i == null)
      return null; // Kein Konto zugeordnet

    Cache cache = Cache.get(Konto.class, true);
    return (Konto) cache.get(i);
  }

  public void setKonto(Konto konto) throws RemoteException
  {
    setAttribute("konto", new Integer(konto.getID()));
  }

  public Integer getAuszugsnummer() throws RemoteException
  {
    return (Integer) getAttribute("auszugsnummer");
  }

  public void setAuszugsnummer(Integer auszugsnummer) throws RemoteException
  {
    setAttribute("auszugsnummer", auszugsnummer);
  }

  public Integer getBlattnummer() throws RemoteException
  {
    return (Integer) getAttribute("blattnummer");
  }

  public void setBlattnummer(Integer blattnummer) throws RemoteException
  {
    setAttribute("blattnummer", blattnummer);
  }

  public String getName() throws RemoteException
  {
    return (String) getAttribute("name");
  }

  public void setName(String name) throws RemoteException
  {
    setAttribute("name", name);
  }

  public double getBetrag() throws RemoteException
  {
    Double d = (Double) getAttribute("betrag");
    if (d == null)
      return 0;
    return d.doubleValue();
  }

  public void setBetrag(double d) throws RemoteException
  {
    setAttribute("betrag", new Double(d));
  }

  public String getZweck() throws RemoteException
  {
    return (String) getAttribute("zweck");
  }

  public void setZweck(String zweck) throws RemoteException
  {
    setAttribute("zweck", zweck);
  }

  public String getZweck2() throws RemoteException
  {
    return (String) getAttribute("zweck2");
  }

  public void setZweck2(String zweck2) throws RemoteException
  {
    setAttribute("zweck2", zweck2);
  }

  public Date getDatum() throws RemoteException
  {
    return (Date) getAttribute("datum");
  }

  public void setDatum(Date datum) throws RemoteException
  {
    setAttribute("datum", datum);
  }

  public void setDatum(String datum) throws RemoteException
  {
    setAttribute("datum", toDate(datum));
  }

  public String getArt() throws RemoteException
  {
    return (String) getAttribute("art");
  }

  public void setArt(String art) throws RemoteException
  {
    setAttribute("art", art);
  }

  public String getKommentar() throws RemoteException
  {
    return (String) getAttribute("kommentar");
  }

  public void setKommentar(String kommentar) throws RemoteException
  {
    setAttribute("kommentar", kommentar);
  }

  public Buchungsart getBuchungsart() throws RemoteException
  {
    Integer i = (Integer) super.getAttribute("buchungsart");
    if (i == null)
      return null; // Keine Buchungsart zugeordnet

    Cache cache = Cache.get(Buchungsart.class, true);
    return (Buchungsart) cache.get(i);
  }

  public int getBuchungsartId() throws RemoteException
  {
    return Integer.parseInt(getBuchungsart().getID());
  }

  public void setBuchungsart(Integer buchungsart) throws RemoteException
  {
    setAttribute("buchungsart", buchungsart);
  }

  public Abrechnungslauf getAbrechnungslauf() throws RemoteException
  {
    return (Abrechnungslauf) getAttribute("abrechnungslauf");
  }

  public int getAbrechnungslaufID() throws RemoteException
  {
    return Integer.parseInt(getAbrechnungslauf().getID());
  }

  public void setAbrechnungslauf(Integer abrechnungslauf)
      throws RemoteException
  {
    setAttribute("abrechnungslauf", abrechnungslauf);
  }

  public void setAbrechnungslauf(Abrechnungslauf abrechnungslauf)
      throws RemoteException
  {
    setAttribute("abrechnungslauf", new Integer(abrechnungslauf.getID()));
  }

  public Mitgliedskonto getMitgliedskonto() throws RemoteException
  {
    return (Mitgliedskonto) getAttribute("mitgliedskonto");
  }

  public int getMitgliedskontoID() throws RemoteException
  {
    return Integer.parseInt(getMitgliedskonto().getID());
  }

  public void setMitgliedskontoID(Integer mitgliedskontoID)
      throws RemoteException
  {
    setAttribute("mitgliedskonto", mitgliedskontoID);
  }

  public void setMitgliedskonto(Mitgliedskonto mitgliedskonto)
      throws RemoteException
  {
    if (mitgliedskonto != null)
    {
      setAttribute("mitgliedskonto", new Integer(mitgliedskonto.getID()));
    }
    else
    {
      setAttribute("mitgliedskonto", null);
    }
  }

  public Spendenbescheinigung getSpendenbescheinigung() throws RemoteException
  {
    Integer i = (Integer) super.getAttribute("spendenbescheinigung");
    if (i == null)
      return null; // Keine Spendenbescheinigung zugeordnet

    Cache cache = Cache.get(Spendenbescheinigung.class, true);
    return (Spendenbescheinigung) cache.get(i);
  }

  public void setSpendenbescheinigungId(Integer spendenbescheinigung)
      throws RemoteException
  {
    setAttribute("spendenbescheinigung", spendenbescheinigung);
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    if ("id-int".equals(fieldName))
    {
      try
      {
        return new Integer(getID());
      }
      catch (Exception e)
      {
        Logger.error("unable to parse id: " + getID());
        return getID();
      }
    }

    if ("buchungsart".equals(fieldName))
      return getBuchungsart();

    if ("konto".equals(fieldName))
      return getKonto();

    return super.getAttribute(fieldName);
  }

  private Date toDate(String datum)
  {
    Date d = null;

    try
    {
      d = new JVDateFormatTTMMJJJJ().parse(datum);
    }
    catch (Exception e)
    {
      //
    }
    return d;
  }

  public Jahresabschluss getJahresabschluss() throws RemoteException
  {
    DBIterator it = Einstellungen.getDBService().createList(
        Jahresabschluss.class);
    it.addFilter("von <= ?", new Object[] { getDatum() });
    it.addFilter("bis >= ?", new Object[] { getDatum() });
    if (it.hasNext())
    {
      Jahresabschluss ja = (Jahresabschluss) it.next();
      return ja;
    }
    return null;
  }

  public Integer getSplitId() throws RemoteException
  {
    return (Integer) getAttribute("splitid");
  }

  public void setSplitId(Integer splitid) throws RemoteException
  {
    setAttribute("splitid", splitid);
  }

}
