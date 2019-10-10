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
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.AbrechnungslaufBuchungenAction;
import de.jost_net.JVerein.gui.formatter.AbrechnungsmodusFormatter;
import de.jost_net.JVerein.gui.formatter.JaNeinFormatter;
import de.jost_net.JVerein.gui.menu.AbrechnungslaufMenu;
import de.jost_net.JVerein.keys.Abrechnungsmodi;
import de.jost_net.JVerein.rmi.Abrechnungslauf;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.LabelInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Column;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class AbrechnungslaufControl extends AbstractControl
{

  private final de.willuhn.jameica.system.Settings settings;

  private Abrechnungslauf abrl;

  private TablePart abrechnungslaufList;

  private LabelInput datum;

  private LabelInput abgeschlossen;

  private LabelInput modus;

  private LabelInput faelligkeit;

  private LabelInput stichtag;

  private LabelInput eingabedatum;

  private LabelInput austrittsdatum;

  private LabelInput zahlungsgrund;

  private LabelInput zusatzabrechnungen;

  private TextInput bemerkung;

  private LabelInput statistikbuchungen;

  private LabelInput statistiklastschriften;

  public AbrechnungslaufControl(AbstractView view)
  {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public Abrechnungslauf getAbrechnungslaeufe()
  {
    if (abrl != null)
    {
      return abrl;
    }
    abrl = (Abrechnungslauf) getCurrentObject();
    return abrl;
  }

  public LabelInput getDatum() throws RemoteException
  {
    if (datum != null)
    {
      return datum;
    }
    datum = new LabelInput(
        new JVDateFormatTTMMJJJJ().format(getAbrechnungslaeufe().getDatum()));
    datum.setName("Datum");
    return datum;
  }

  public LabelInput getAbgeschlossen() throws RemoteException
  {
    if (abgeschlossen != null)
    {
      return abgeschlossen;
    }
    Boolean b = getAbrechnungslaeufe().getAbgeschlossen();
    abgeschlossen = new LabelInput(b ? "Ja" : "Nein");
    abgeschlossen.setName("Abgeschlossen");
    return abgeschlossen;
  }

  public LabelInput getAbrechnungsmodus() throws RemoteException
  {
    if (modus != null)
    {
      return modus;
    }
    String m = Abrechnungsmodi.get(getAbrechnungslaeufe().getModus());
    modus = new LabelInput(m);
    modus.setName("Abrechnungsmodus");
    return modus;
  }

  public LabelInput getFaelligkeit() throws RemoteException
  {
    if (faelligkeit != null)
    {
      return faelligkeit;
    }
    faelligkeit = new LabelInput(new JVDateFormatTTMMJJJJ()
        .format(getAbrechnungslaeufe().getFaelligkeit()));
    faelligkeit.setName("Fälligkeit");
    return faelligkeit;
  }

  public LabelInput getStichtag() throws RemoteException
  {
    if (stichtag != null)
    {
      return stichtag;
    }
    stichtag = new LabelInput(new JVDateFormatTTMMJJJJ()
        .format(getAbrechnungslaeufe().getStichtag()));
    stichtag.setName("Stichtag");
    return stichtag;
  }

  public LabelInput getEingabedatum() throws RemoteException
  {
    if (eingabedatum != null)
    {
      return eingabedatum;
    }
    Date ed = getAbrechnungslaeufe().getEingabedatum();
    // TODO ungültige Daten ausfiltern
    eingabedatum = new LabelInput(new JVDateFormatTTMMJJJJ().format(ed));
    eingabedatum.setName("Eingabedatum");
    return eingabedatum;
  }

  public LabelInput getAustrittsdatum() throws RemoteException
  {
    if (austrittsdatum != null)
    {
      return austrittsdatum;
    }
    Date ed = getAbrechnungslaeufe().getAustrittsdatum();
    austrittsdatum = new LabelInput(new JVDateFormatTTMMJJJJ().format(ed));
    austrittsdatum.setName("Austrittsdatum");
    return austrittsdatum;
  }

  public LabelInput getZahlungsgrund() throws RemoteException
  {
    if (zahlungsgrund != null)
    {
      return zahlungsgrund;
    }
    zahlungsgrund = new LabelInput(getAbrechnungslaeufe().getZahlungsgrund());
    zahlungsgrund.setName("Zahlungsgrund");
    return zahlungsgrund;
  }

  public LabelInput getZusatzAbrechnungen() throws RemoteException
  {
    if (zusatzabrechnungen != null)
    {
      return zusatzabrechnungen;
    }
    String zs = "";
    if (getAbrechnungslaeufe().getZusatzbetraege())
    {
      zs += "Zusatzbeträge ";
    }
    if (getAbrechnungslaeufe().getKursteilnehmer())
    {
      zs += "Kursteilnehmer ";
    }
    zusatzabrechnungen = new LabelInput(zs);
    zusatzabrechnungen.setName("Weitere Abrechnungen");
    return zusatzabrechnungen;
  }

  public Input getBemerkung() throws RemoteException
  {
    if (bemerkung != null)
    {
      return bemerkung;
    }
    bemerkung = new TextInput(getAbrechnungslaeufe().getBemerkung(), 80);
    bemerkung.setName("Bemerkung");
    return bemerkung;
  }

  public LabelInput getStatistikBuchungen() throws RemoteException
  {
    // Summe und Anzahl der Buchungen. Es gibt einen weiterer Datensatz
    // wo die Buchungsart NULL ist, es handelt sich um die Gegenbuchung
    // mit umgekehrten Vorzeichen.

    if (statistikbuchungen != null)
    {
      return statistikbuchungen;
    }

    final class StatData
    {
      Double summe;

      Integer anzahl;
    }

    ResultSetExtractor rs = new ResultSetExtractor()
    {
      @Override
      public Object extract(ResultSet rs) throws SQLException
      {
        StatData ret = new StatData();
        while (rs.next())
        {
          ret.summe = rs.getDouble(1);
          ret.anzahl = rs.getInt(2);
        }
        return ret;
      }
    };

    String sql = "SELECT SUM(betrag), COUNT(id) " + "FROM buchung "
        + "WHERE abrechnungslauf=? AND buchungsart IS NOT NULL";
    StatData data = (StatData) Einstellungen.getDBService().execute(sql,
        new Object[] { getAbrechnungslaeufe().getID() }, rs);

    CurrencyFormatter cf = new CurrencyFormatter("EUR",
        Einstellungen.DECIMALFORMAT);
    String s = String.format("Anzahl: %s; Summe %s", data.anzahl.toString(),
        cf.format(data.summe));
    statistikbuchungen = new LabelInput(s);
    statistikbuchungen.setName("Buchungen");

    return statistikbuchungen;
  }

  public LabelInput getStatistikLastschriften() throws RemoteException
  {
    // Summe und Anzahl der Lastschriften.

    if (statistiklastschriften != null)
    {
      return statistiklastschriften;
    }

    final class StatData
    {
      Double summe;

      Integer anzahl;
    }

    ResultSetExtractor rs = new ResultSetExtractor()
    {
      @Override
      public Object extract(ResultSet rs) throws SQLException
      {
        StatData ret = new StatData();
        while (rs.next())
        {
          ret.summe = rs.getDouble(1);
          ret.anzahl = rs.getInt(2);
        }
        return ret;
      }
    };

    String sql = "SELECT SUM(betrag), COUNT(id) " + "FROM lastschrift "
        + "WHERE abrechnungslauf=?";
    StatData data = (StatData) Einstellungen.getDBService().execute(sql,
        new Object[] { getAbrechnungslaeufe().getID() }, rs);

    CurrencyFormatter cf = new CurrencyFormatter("EUR",
        Einstellungen.DECIMALFORMAT);
    String s = String.format("Anzahl: %s; Summe %s", data.anzahl.toString(),
        cf.format(data.summe));
    statistiklastschriften = new LabelInput(s);
    statistiklastschriften.setName("Lastschriften");

    return statistiklastschriften;
  }

  public void handleStore()
  {
    // Es kann nur die Bemerkung verändert werden
    try
    {
      Abrechnungslauf al = getAbrechnungslaeufe();
      al.setBemerkung((String) getBemerkung().getValue());
      try
      {
        al.store();
        GUI.getStatusBar()
            .setSuccessText("Bemerkung zum Abrechnungslauf gespeichert");
      }
      catch (ApplicationException e)
      {
        GUI.getStatusBar().setErrorText(e.getMessage());
      }
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler beim Speichern des Abrechnungslaufs";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  public Part getAbrechungslaeufeList() throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator<Abrechnungslauf> abrechnungslaeufe = service
        .createList(Abrechnungslauf.class);
    abrechnungslaeufe.setOrder("ORDER BY datum DESC");

    if (abrechnungslaufList == null)
    {
      abrechnungslaufList = new TablePart(abrechnungslaeufe,
          new AbrechnungslaufBuchungenAction());
      abrechnungslaufList.addColumn("Nr", "nr");
      abrechnungslaufList.addColumn("Datum", "datum",
          new DateFormatter(new JVDateFormatTTMMJJJJ()));
      abrechnungslaufList.addColumn("Modus", "modus",
          new AbrechnungsmodusFormatter(), false, Column.ALIGN_LEFT);
      abrechnungslaufList.addColumn("Fälligkeit", "faelligkeit",
          new DateFormatter(new JVDateFormatTTMMJJJJ()));
      abrechnungslaufList.addColumn("Stichtag", "stichtag",
          new DateFormatter(new JVDateFormatTTMMJJJJ()));
      abrechnungslaufList.addColumn("Eingabedatum", "eingabedatum",
          new DateFormatter(new JVDateFormatTTMMJJJJ()));
      abrechnungslaufList.addColumn("Austrittsdatum", "austrittsdatum",
          new DateFormatter(new JVDateFormatTTMMJJJJ()));
      abrechnungslaufList.addColumn("Zahlungsgrund", "zahlungsgrund");
      abrechnungslaufList.addColumn("Zusatzbeträge", "zusatzbetraege",
          new JaNeinFormatter());
      abrechnungslaufList.addColumn("Kursteilnehmer", "kursteilnehmer",
          new JaNeinFormatter());
      abrechnungslaufList.setContextMenu(new AbrechnungslaufMenu());
      abrechnungslaufList.setRememberColWidths(true);
      abrechnungslaufList.setRememberOrder(true);
      abrechnungslaufList.setSummary(true);
    }
    else
    {
      abrechnungslaufList.removeAll();
      while (abrechnungslaeufe.hasNext())
      {
        abrechnungslaufList.addItem(abrechnungslaeufe.next());
      }
    }
    return abrechnungslaufList;
  }

}
