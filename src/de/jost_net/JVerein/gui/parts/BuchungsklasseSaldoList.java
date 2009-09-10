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
 **********************************************************************/
package de.jost_net.JVerein.gui.parts;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.io.BuchungsklasseSaldoZeile;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.jost_net.JVerein.rmi.Buchungsklasse;
import de.jost_net.JVerein.util.Geschaeftsjahr;
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

  private Geschaeftsjahr gj = null;

  public BuchungsklasseSaldoList(Action action, int jahr)
      throws RemoteException
  {
    super(action);
    try
    {
      gj = new Geschaeftsjahr(jahr);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
  }

  public BuchungsklasseSaldoList(Action action, Geschaeftsjahr gj)
  {
    super(action);
    this.gj = gj;
  }

  public Part getSaldoList() throws ApplicationException
  {
    ArrayList<BuchungsklasseSaldoZeile> zeile = null;
    try
    {
      zeile = getInfo();

      if (saldoList == null)
      {
        GenericIterator gi = PseudoIterator.fromArray((GenericObject[]) zeile
            .toArray(new GenericObject[zeile.size()]));

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
    catch (ParseException e)
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Fehler aufgetreten")
          + e.getMessage());
    }
    return saldoList;
  }

  public ArrayList<BuchungsklasseSaldoZeile> getInfo() throws RemoteException,
      ParseException
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

    ResultSetExtractor rs = new ResultSetExtractor()
    {
      public Object extract(ResultSet rs) throws RemoteException, SQLException
      {
        if (!rs.next())
        {
          return new Double(0);
        }
        return new Double(rs.getDouble(1));
      }
    };

    DBService service = Einstellungen.getDBService();
    DBIterator buchungsklassenIt = service.createList(Buchungsklasse.class);
    buchungsklassenIt.setOrder("ORDER BY nummer");
    while (buchungsklassenIt.hasNext())
    {
      buchungsklasse = (Buchungsklasse) buchungsklassenIt.next();
      zeile.add(new BuchungsklasseSaldoZeile(buchungsklasse));
      DBIterator buchungsartenIt = service.createList(Buchungsart.class);
      buchungsartenIt.addFilter("buchungsklasse = ?",
          new Object[] { buchungsklasse.getID() });

      while (buchungsartenIt.hasNext())
      {
        buchungsart = (Buchungsart) buchungsartenIt.next();
        String sql = "select sum(betrag) from buchung, buchungsart "
            + "where datum >= ? and datum <= ?  "
            + "and buchung.BUCHUNGSART = BUCHUNGSART.ID "
            + "and buchungsart.id = ? " + "and BUCHUNGSART.ART=?";
        einnahmen = (Double) service.execute(sql, new Object[] {
            gj.getBeginnGeschaeftsjahr(), gj.getEndeGeschaeftsjahr(),
            buchungsart.getID(), 0 }, rs);
        suBukEinnahmen += einnahmen;
        ausgaben = (Double) service.execute(sql, new Object[] {
            gj.getBeginnGeschaeftsjahr(), gj.getEndeGeschaeftsjahr(),
            buchungsart.getID(), 1 }, rs);
        suBukAusgaben += ausgaben;
        umbuchungen = (Double) service.execute(sql, new Object[] {
            gj.getBeginnGeschaeftsjahr(), gj.getEndeGeschaeftsjahr(),
            buchungsart.getID(), 2 }, rs);
        suBukUmbuchungen += umbuchungen;
        zeile.add(new BuchungsklasseSaldoZeile(buchungsart, einnahmen,
            ausgaben, umbuchungen));
      }
      zeile.add(new BuchungsklasseSaldoZeile("Saldo "
          + buchungsklasse.getBezeichnung(), suBukEinnahmen, suBukAusgaben,
          suBukUmbuchungen));
    }
    String sql = "select sum(betrag) from buchung, buchungsart "
        + "where datum >= ? and datum <= ?  "
        + "and buchung.BUCHUNGSART = BUCHUNGSART.ID "
        + "and buchungsart.buchungsklasse is null and BUCHUNGSART.ART=?";
    einnahmen = (Double) service.execute(sql, new Object[] {
        gj.getBeginnGeschaeftsjahr(), gj.getEndeGeschaeftsjahr(), 0 }, rs);
    suBukEinnahmen += einnahmen;
    ausgaben = (Double) service.execute(sql, new Object[] {
        gj.getBeginnGeschaeftsjahr(), gj.getEndeGeschaeftsjahr(), 1 }, rs);
    suBukAusgaben += ausgaben;
    umbuchungen = (Double) service.execute(sql, new Object[] {
        gj.getBeginnGeschaeftsjahr(), gj.getEndeGeschaeftsjahr(), 2 }, rs);
    suBukUmbuchungen += umbuchungen;
    zeile.add(new BuchungsklasseSaldoZeile("Nicht zugeordnet", einnahmen,
        ausgaben, umbuchungen));

    return zeile;
  }

  public void setGeschaeftsjahr(Geschaeftsjahr gj)
  {
    this.gj = gj;
  }

  public void removeAll()
  {
    saldoList.removeAll();
  }

  public synchronized void paint(Composite parent) throws RemoteException
  {
    super.paint(parent);
  }

}
