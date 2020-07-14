/**********************************************************************
 * Copyright (c) by Heiner Jostkleigrewe
 * This program is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,  but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See 
 *  the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, 
 * see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de
 * www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.dialogs.BuchungUebernahmeProtokollDialog;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Jahresabschluss;
import de.jost_net.JVerein.rmi.Konto;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.jameica.hbci.HBCI;
import de.willuhn.jameica.hbci.rmi.Umsatz;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;

public class Buchungsuebernahme
{
  private ArrayList<Buchung> buchungen;

  private Buchung fehlerbuchung = null;

  private Exception exception = null;

  public Buchungsuebernahme()
  {
    uebernahme();
  }

  private void uebernahme()
  {
    try
    {
      Logger.info("Buchungsübernahme zu JVerein gestartet");

      // Protokollliste initialisieren
      buchungen = new ArrayList<>();
      // Über alle Hibiscus-Konten (aus JVerein-Sicht) iterieren
      DBIterator<Konto> hibkto = Einstellungen.getDBService()
          .createList(Konto.class);
      hibkto.addFilter("hibiscusid > 0");
      while (hibkto.hasNext())
      {
        Konto kto = (Konto) hibkto.next();
        leseHibiscus(kto);
      }
      Logger.info("Buchungsübernahme zu JVerein abgeschlossen");
    }
    catch (Exception e)
    {
      Logger.error("Buchungsübernahme zu JVerein fehlerhaft", e);
    }
    try
    {
      BuchungUebernahmeProtokollDialog bup = new BuchungUebernahmeProtokollDialog(
          buchungen, fehlerbuchung, exception);
      bup.open();
    }
    catch (Exception e)
    {
      Logger.error("Fehler", e);
    }

  }

  private void leseHibiscus(Konto kto) throws Exception
  {
    Integer hibid = kto.getHibiscusId();
    Integer jvid = new Integer(kto.getID());
    DBService service = Einstellungen.getDBService();
    String sql = "select max(umsatzid) from buchung where konto = "
        + jvid.toString();

    ResultSetExtractor rs = new ResultSetExtractor()
    {
      @Override
      public Object extract(ResultSet rs) throws SQLException
      {
        if (!rs.next())
        {
          return Integer.valueOf(0);
        }
        return Integer.valueOf(rs.getInt(1));
      }
    };
    Integer maximum = (Integer) service.execute(sql, new Object[] {}, rs);

    DBIterator<Jahresabschluss> itjahresabschl = Einstellungen.getDBService()
        .createList(Jahresabschluss.class);
    itjahresabschl.setOrder("order by bis desc");
    Jahresabschluss ja = null;
    if (itjahresabschl.hasNext())
    {
      ja = (Jahresabschluss) itjahresabschl.next();
    }

    DBService hibservice = (DBService) Application.getServiceFactory()
        .lookup(HBCI.class, "database");
    DBIterator<Umsatz> hibbuchungen = hibservice.createList(Umsatz.class);
    if (maximum.intValue() > 0)
    {
      hibbuchungen.addFilter("id > ?", maximum);
    }
    hibbuchungen.addFilter("konto_id = ?", hibid);
    if (ja != null)
    {
      Logger.info("datum=" + ja.getBis());
      hibbuchungen.addFilter("datum > ?", ja.getBis());
    }
    hibbuchungen.setOrder("ORDER BY id");
    while (hibbuchungen.hasNext())
    {
      Umsatz u = (Umsatz) hibbuchungen.next();
      importiereUmsatz(u, kto);
    }
  }

  private void importiereUmsatz(Umsatz u, Konto kto) throws Exception
  {
    if ((u.getFlags() & Umsatz.FLAG_NOTBOOKED) == 0)
    {
      Buchung b = null;
      try
      {
        b = (Buchung) Einstellungen.getDBService().createObject(Buchung.class,
            null);
        b.setUmsatzid(new Integer(u.getID()));
        b.setKonto(kto);
        b.setName(u.getGegenkontoName());
        b.setIban(u.getGegenkontoNummer());
        b.setBetrag(u.getBetrag());
        b.setZweck(u.getZweck());
        String[] moreLines = u.getWeitereVerwendungszwecke();
        String zweck = u.getZweck();
        String line2 = u.getZweck2();
        if (line2 != null && line2.trim().length() > 0)
        {
          zweck += "\r\n" + line2.trim();
        }
        if (moreLines != null && moreLines.length > 0)
        {
          for (String s : moreLines)
          {
            if (s == null || s.trim().length() == 0)
              continue;
            zweck += "\r\n" + s.trim();
          }
        }
        if (zweck != null && zweck.length() > 500)
        {
          zweck = zweck.substring(0, 500);
        }
        b.setZweck(zweck);
        b.setDatum(u.getDatum());
        b.setArt(u.getArt());
        b.setKommentar(u.getKommentar());
        b.store();
        buchungen.add(b);
      }
      catch (Exception e)
      {
        this.fehlerbuchung = b;
        this.exception = e;
        throw e;
      }
    }
  }

}
