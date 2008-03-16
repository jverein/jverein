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
 * Revision 1.4  2007/02/23 20:28:04  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.3  2006/12/20 20:25:44  jost
 * Patch von Ullrich Schäfer, der die Primitive vs. Object Problematik adressiert.
 *
 * Revision 1.2  2006/10/14 16:12:20  jost
 * Bugfix bei der Übernahme der Buchungen aus Hibiscus
 *
 * Revision 1.1  2006/09/20 15:39:24  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Buchung;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.jameica.hbci.HBCI;
import de.willuhn.jameica.hbci.rmi.Umsatz;
import de.willuhn.jameica.system.Application;
import de.willuhn.util.ProgressMonitor;

public class Buchungsuebernahme
{
  public Buchungsuebernahme(ProgressMonitor monitor)
  {
    try
    {
      String sql = "select max(tonumber(umsatzid)) from buchung";
      DBService service = Einstellungen.getDBService();

      ResultSetExtractor rs = new ResultSetExtractor()
      {
        public Object extract(ResultSet rs) throws RemoteException,
            SQLException
        {
          if (!rs.next())
          {
            return new Integer(0);
          }
          return new Integer(rs.getInt(1));
        }
      };
      Integer maximum = (Integer) service.execute(sql, new Object[] {}, rs);

      DBService hibservice = (DBService) Application.getServiceFactory()
          .lookup(HBCI.class, "database");
      DBIterator list = hibservice.createList(Umsatz.class);
      if (maximum.intValue() > 0)
      {
        list.addFilter("id >" + maximum);
      }
      list.setOrder("ORDER BY id");
      monitor.log("Anzahl Sätze: " + list.size());

      while (list.hasNext())
      {
        Umsatz u = (Umsatz) list.next();
        Buchung b = (Buchung) Einstellungen.getDBService().createObject(
            Buchung.class, null);
        b.setUmsatzid(u.getID());
        b.setKonto(u.getKonto().getKontonummer());
        b.setName(u.getGegenkontoName());
        b.setBetrag(u.getBetrag());
        b.setZweck(u.getZweck());
        b.setZweck2(u.getZweck2());
        b.setDatum(u.getDatum());
        b.setSaldo(u.getSaldo());
        b.setArt(u.getArt());
        b.setKommentar(u.getKommentar());
        b.store();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
