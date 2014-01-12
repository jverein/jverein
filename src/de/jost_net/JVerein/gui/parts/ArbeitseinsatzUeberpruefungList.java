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
package de.jost_net.JVerein.gui.parts;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.MitgliedDetailAction;
import de.jost_net.JVerein.gui.input.ArbeitseinsatzUeberpruefungInput;
import de.jost_net.JVerein.io.ArbeitseinsatzZeile;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.parts.Column;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.util.ApplicationException;

public class ArbeitseinsatzUeberpruefungList extends TablePart implements Part
{

  private TablePart arbeitseinsatzueberpruefungList;

  private int jahr;

  private int schluessel;

  public ArbeitseinsatzUeberpruefungList(Action action, int jahr, int schluessel)
  {
    super(action);
    this.jahr = jahr;
    this.schluessel = schluessel;
  }

  public Part getArbeitseinsatzUeberpruefungList() throws ApplicationException
  {
    ArrayList<ArbeitseinsatzZeile> zeile = null;
    try
    {
      zeile = getInfo();

      if (arbeitseinsatzueberpruefungList == null)
      {
        GenericIterator gi = PseudoIterator.fromArray(zeile
            .toArray(new GenericObject[zeile.size()]));

        arbeitseinsatzueberpruefungList = new TablePart(gi,
            new MitgliedDetailAction());
        arbeitseinsatzueberpruefungList.addColumn("Name, Vorname",
            "namevorname");
        arbeitseinsatzueberpruefungList.addColumn("Sollstunden", "soll",
            new CurrencyFormatter("", Einstellungen.DECIMALFORMAT), false,
            Column.ALIGN_RIGHT);
        arbeitseinsatzueberpruefungList.addColumn("Iststunden", "ist",
            new CurrencyFormatter("", Einstellungen.DECIMALFORMAT), false,
            Column.ALIGN_RIGHT);
        arbeitseinsatzueberpruefungList.addColumn("Differenz", "differenz",
            new CurrencyFormatter("", Einstellungen.DECIMALFORMAT), false,
            Column.ALIGN_RIGHT);
        arbeitseinsatzueberpruefungList.addColumn("Stundensatz", "stundensatz",
            new CurrencyFormatter("", Einstellungen.DECIMALFORMAT), false,
            Column.ALIGN_RIGHT);
        arbeitseinsatzueberpruefungList.addColumn("Gesamtbetrag",
            "gesamtbetrag", new CurrencyFormatter("",
                Einstellungen.DECIMALFORMAT), false, Column.ALIGN_RIGHT);
        arbeitseinsatzueberpruefungList.setRememberColWidths(true);
        arbeitseinsatzueberpruefungList.setSummary(true);
      }
      else
      {
        arbeitseinsatzueberpruefungList.removeAll();
        for (ArbeitseinsatzZeile az : zeile)
        {
          arbeitseinsatzueberpruefungList.addItem(az);
        }
      }
    }
    catch (RemoteException e)
    {
      throw new ApplicationException("Fehler aufgetreten" + e.getMessage());
    }
    return arbeitseinsatzueberpruefungList;
  }

  @SuppressWarnings("unchecked")
  public ArrayList<ArbeitseinsatzZeile> getInfo() throws RemoteException
  {

    String sql = "select mitglied.id as id, arbeitseinsatzstunden  sollstunden, beitragsgruppe.arbeitseinsatzbetrag as betrag, sum(stunden)  iststunden from mitglied "
        + "  join beitragsgruppe on mitglied.beitragsgruppe = beitragsgruppe.id "
        + "  left join arbeitseinsatz on mitglied.id = arbeitseinsatz.mitglied and year(arbeitseinsatz.datum) = ? "
        + "where  (mitglied.eintritt is null or year(mitglied.eintritt) <= ?) and "
        + "       (mitglied.austritt is null or year(mitglied.austritt) >= ?) ";

    if (schluessel != ArbeitseinsatzUeberpruefungInput.MEHRLEISTUNG)
    {
      sql += "        and beitragsgruppe.arbeitseinsatzstunden is not null and "
          + "        beitragsgruppe.arbeitseinsatzstunden > 0 ";
    }
    sql += "group by mitglied.id ";
    if (schluessel == ArbeitseinsatzUeberpruefungInput.MINDERLEISTUNG)
    {
      sql += "    having iststunden < arbeitseinsatzstunden or iststunden is null  ";
    }

    if (schluessel == ArbeitseinsatzUeberpruefungInput.PASSENDELEISTUNG)
    {
      sql += "    having iststunden = arbeitseinsatzstunden  ";
    }

    if (schluessel == ArbeitseinsatzUeberpruefungInput.MEHRLEISTUNG)
    {
      sql += "    having iststunden > arbeitseinsatzstunden  ";
    }

    sql += "  order by mitglied.name, mitglied.vorname, mitglied.id ";

    ResultSetExtractor rs = new ResultSetExtractor()
    {

      @Override
      public Object extract(ResultSet rs) throws SQLException
      {
        ArrayList<ArbeitseinsatzZeile> ergebnis = new ArrayList<ArbeitseinsatzZeile>();
        while (rs.next())
        {
          ArbeitseinsatzZeile z = new ArbeitseinsatzZeile(rs.getString("id"),
              rs.getDouble("sollstunden"), rs.getDouble("iststunden"),
              rs.getDouble("betrag"));
          ergebnis.add(z);
        }
        return ergebnis;
      }
    };
    return (ArrayList<ArbeitseinsatzZeile>) Einstellungen.getDBService()
        .execute(sql, new Object[] { jahr, jahr, jahr }, rs);
  }

  @Override
  public void removeAll()
  {
    arbeitseinsatzueberpruefungList.removeAll();
  }

  public void addItem(ArbeitseinsatzZeile az) throws RemoteException
  {
    arbeitseinsatzueberpruefungList.addItem(az);
  }

  @Override
  public synchronized void paint(Composite parent) throws RemoteException
  {
    super.paint(parent);
  }

  public void setJahr(int jahr)
  {
    this.jahr = jahr;
  }

  public void setSchluessel(int schluessel)
  {
    this.schluessel = schluessel;
  }

}
