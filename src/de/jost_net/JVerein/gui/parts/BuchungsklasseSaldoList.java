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
 * Revision 1.6  2010-02-23 21:15:49  jost
 * Individueller Zeitraum
 *
 * Revision 1.5  2009/09/19 16:28:16  jost
 * Weiterentwicklung
 *
 * Revision 1.4  2009/09/16 21:36:05  jost
 * Bugfix Saldobildung
 *
 * Revision 1.3  2009/09/15 19:22:10  jost
 * Korrekte Bildung der Summen.
 *
 * Revision 1.2  2009/09/12 19:03:00  jost
 * neu: Buchungsjournal
 *
 * Revision 1.1  2009/09/10 18:17:58  jost
 * neu: Buchungsklassen
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.parts;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.io.BuchungsklasseSaldoZeile;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.jost_net.JVerein.rmi.Buchungsklasse;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.parts.Column;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.util.ApplicationException;

public class BuchungsklasseSaldoList extends TablePart implements Part
{

  private TablePart saldoList;

  private Date datumvon = null;

  private Date datumbis = null;

  public BuchungsklasseSaldoList(Action action, Date datumvon, Date datumbis)
  {
    super(action);
    this.datumvon = datumvon;
    this.datumbis = datumbis;
  }

  public Part getSaldoList() throws ApplicationException
  {
    ArrayList<BuchungsklasseSaldoZeile> zeile = null;
    try
    {
      zeile = getInfo();

      if (saldoList == null)
      {
        GenericIterator gi = PseudoIterator.fromArray(zeile.toArray(new GenericObject[zeile.size()]));

        saldoList = new TablePart(gi, null);
        saldoList.addColumn(JVereinPlugin.getI18n().tr("Buchungsklasse"),
            "buchungsklassenbezeichnung", null, false);
        saldoList.addColumn(JVereinPlugin.getI18n().tr("Buchungsart"),
            "buchungsartbezeichnung");
        saldoList.addColumn(JVereinPlugin.getI18n().tr("Einnahmen"),
            "einnahmen",
            new CurrencyFormatter("", Einstellungen.DECIMALFORMAT), false,
            Column.ALIGN_RIGHT);
        saldoList.addColumn(JVereinPlugin.getI18n().tr("Ausgaben"), "ausgaben",
            new CurrencyFormatter("", Einstellungen.DECIMALFORMAT), false,
            Column.ALIGN_RIGHT);
        saldoList.addColumn(JVereinPlugin.getI18n().tr("Umbuchungen"),
            "umbuchungen", new CurrencyFormatter("",
                Einstellungen.DECIMALFORMAT), false, Column.ALIGN_RIGHT);
        saldoList.setRememberColWidths(true);
        saldoList.setSummary(false);
      }
      else
      {
        saldoList.removeAll();
        for (BuchungsklasseSaldoZeile sz : zeile)
        {
          saldoList.addItem(sz);
        }
      }
    }
    catch (RemoteException e)
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Fehler aufgetreten")
          + e.getMessage());
    }
    return saldoList;
  }

  public ArrayList<BuchungsklasseSaldoZeile> getInfo() throws RemoteException
  {
    ArrayList<BuchungsklasseSaldoZeile> zeile = new ArrayList<BuchungsklasseSaldoZeile>();
    Buchungsklasse buchungsklasse = null;
    Buchungsart buchungsart = null;
    Double einnahmen = new Double(0);
    Double ausgaben = new Double(0);
    Double umbuchungen = new Double(0);
    Double suBukEinnahmen = new Double(0);
    Double suBukAusgaben = new Double(0);
    Double suBukUmbuchungen = new Double(0);
    Double suEinnahmen = new Double(0);
    Double suAusgaben = new Double(0);
    Double suUmbuchungen = new Double(0);

    ResultSetExtractor rsd = new ResultSetExtractor()
    {

      public Object extract(ResultSet rs) throws SQLException
      {
        if (!rs.next())
        {
          return new Double(0);
        }
        return new Double(rs.getDouble(1));
      }
    };
    ResultSetExtractor rsi = new ResultSetExtractor()
    {

      public Object extract(ResultSet rs) throws SQLException
      {
        if (!rs.next())
        {
          return new Integer(0);
        }
        return new Integer(rs.getInt(1));
      }
    };

    DBService service = Einstellungen.getDBService();
    DBIterator buchungsklassenIt = service.createList(Buchungsklasse.class);
    buchungsklassenIt.setOrder("ORDER BY nummer");
    while (buchungsklassenIt.hasNext())
    {
      buchungsklasse = (Buchungsklasse) buchungsklassenIt.next();
      zeile.add(new BuchungsklasseSaldoZeile(BuchungsklasseSaldoZeile.HEADER,
          buchungsklasse));
      DBIterator buchungsartenIt = service.createList(Buchungsart.class);
      buchungsartenIt.addFilter("buchungsklasse = ?",
          new Object[] { buchungsklasse.getID()});
      suBukEinnahmen = new Double(0);
      suBukAusgaben = new Double(0);
      suBukUmbuchungen = new Double(0);
      while (buchungsartenIt.hasNext())
      {
        buchungsart = (Buchungsart) buchungsartenIt.next();
        String sql = "select sum(betrag) from buchung, buchungsart "
            + "where datum >= ? and datum <= ?  "
            + "and buchung.buchungsart = buchungsart.id "
            + "and buchungsart.id = ? " + "and buchungsart.art = ?";
        einnahmen = (Double) service.execute(sql, new Object[] { datumvon,
            datumbis, buchungsart.getID(), 0}, rsd);
        suBukEinnahmen += einnahmen;
        ausgaben = (Double) service.execute(sql, new Object[] { datumvon,
            datumbis, buchungsart.getID(), 1}, rsd);
        suBukAusgaben += ausgaben;
        umbuchungen = (Double) service.execute(sql, new Object[] { datumvon,
            datumbis, buchungsart.getID(), 2}, rsd);
        suBukUmbuchungen += umbuchungen;
        zeile.add(new BuchungsklasseSaldoZeile(BuchungsklasseSaldoZeile.DETAIL,
            buchungsart, einnahmen, ausgaben, umbuchungen));
      }
      suEinnahmen += suBukEinnahmen;
      suAusgaben += suBukAusgaben;
      suUmbuchungen += suBukUmbuchungen;
      zeile.add(new BuchungsklasseSaldoZeile(
          BuchungsklasseSaldoZeile.SALDOFOOTER, "Saldo "
              + buchungsklasse.getBezeichnung(), suBukEinnahmen, suBukAusgaben,
          suBukUmbuchungen));
      zeile.add(new BuchungsklasseSaldoZeile(
          BuchungsklasseSaldoZeile.SALDOGEWINNVERLUST, "Gewinn/Verlust "
              + buchungsklasse.getBezeichnung(), suBukEinnahmen + suBukAusgaben
              + suBukUmbuchungen));
    }
    String sql = "select sum(betrag) from buchung, buchungsart "
        + "where datum >= ? and datum <= ?  "
        + "and buchung.buchungsart = buchungsart.id "
        + "and buchungsart.buchungsklasse is null and buchungsart.art = ?";
    einnahmen = (Double) service.execute(sql, new Object[] { datumvon,
        datumbis, 0}, rsd);
    suBukEinnahmen += einnahmen;
    suEinnahmen += einnahmen;
    ausgaben = (Double) service.execute(sql, new Object[] { datumvon, datumbis,
        1}, rsd);
    suBukAusgaben += ausgaben;
    suAusgaben += ausgaben;
    umbuchungen = (Double) service.execute(sql, new Object[] { datumvon,
        datumbis, 2}, rsd);
    suBukUmbuchungen += umbuchungen;
    suUmbuchungen += umbuchungen;
    if (einnahmen != 0 || ausgaben != 0 || umbuchungen != 0)
    {
      Buchungsklasse b = (Buchungsklasse) service.createObject(
          Buchungsklasse.class, null);
      b.setBezeichnung("Nicht zugeordnet");
      zeile.add(new BuchungsklasseSaldoZeile(BuchungsklasseSaldoZeile.HEADER, b));
      zeile.add(new BuchungsklasseSaldoZeile(BuchungsklasseSaldoZeile.DETAIL,
          "Nicht zugeordnet", einnahmen, ausgaben, umbuchungen));
    }
    zeile.add(new BuchungsklasseSaldoZeile(
        BuchungsklasseSaldoZeile.GESAMTSALDOFOOTER, "Gesamtsaldo ",
        suEinnahmen, suAusgaben, suUmbuchungen));
    zeile.add(new BuchungsklasseSaldoZeile(
        BuchungsklasseSaldoZeile.GESAMTGEWINNVERLUST, "Gesamt Gewinn/Verlust ",
        suEinnahmen + suAusgaben + suUmbuchungen));

    sql = "select count(*) from buchung " + "where datum >= ? and datum <= ?  "
        + "and buchung.buchungsart is null";
    Integer anzahl = (Integer) service.execute(sql, new Object[] { datumvon,
        datumbis}, rsi);
    if (anzahl > 0)
    {
      zeile.add(new BuchungsklasseSaldoZeile(
          BuchungsklasseSaldoZeile.NICHTZUGEORDNETEBUCHUNGEN,
          "Anzahl Buchungen ohne Buchungsart ", anzahl));
    }
    return zeile;
  }

  public void setDatumvon(Date datumvon)
  {
    this.datumvon = datumvon;
  }

  public void setDatumbis(Date datumbis)
  {
    this.datumbis = datumbis;
  }

  @Override
  public void removeAll()
  {
    saldoList.removeAll();
  }

  @Override
  public synchronized void paint(Composite parent) throws RemoteException
  {
    super.paint(parent);
  }

}
