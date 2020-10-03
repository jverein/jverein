/**********************************************************************
 * Copyright (c) by Heiner Jostkleigrewe
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de | www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.Queries;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.io.Suchbetrag;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.jost_net.JVerein.rmi.Konto;
import de.jost_net.JVerein.rmi.Projekt;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;

public class BuchungQuery {
  private Date datumvon;

  private Date datumbis;

  private Konto konto;

  public Buchungsart buchungart;

  private Projekt projekt;

  public String text;

  public String betrag;

  private List<Buchung> ergebnis;

  private Boolean hasMitglied;

  private static final int ORDER_UMSATZID = 0;

  private static final int ORDER_DATUM = 1;

  private static final int ORDER_DATUM_AUSZUGSNUMMER_BLATTNUMMER = 2;

  private static final int ORDER_DATUM_NAME = 3;

  private static final int ORDER_ID = 4;

  private static final int ORDER_DATUM_ID = 5;

  private int order = ORDER_UMSATZID;

  public BuchungQuery(Date datumvon, Date datumbis, Konto konto, Buchungsart buchungsart,
      Projekt projekt, String text, String betrag, Boolean hasMitglied) {
    this.datumvon = datumvon;
    this.datumbis = datumbis;
    this.konto = konto;
    this.buchungart = buchungsart;
    this.projekt = projekt;
    this.text = text;
    this.betrag = betrag;
    this.hasMitglied = hasMitglied;
  }

  public void setOrderID() {
    order = ORDER_ID;
  }

  public Boolean getHasMitglied() {
    return hasMitglied;
  }

  public void setHasMitglied(Boolean hasMitglied) {
    this.hasMitglied = hasMitglied;
  }

  public void setOrderDatum() {
    order = ORDER_DATUM;
  }

  public void setOrderDatumID() {
    order = ORDER_DATUM_ID;
  }

  public void setOrderDatumAuszugsnummerBlattnummer() {
    order = ORDER_DATUM_AUSZUGSNUMMER_BLATTNUMMER;
  }

  public void setOrderDatumName() {
    order = ORDER_DATUM_NAME;
  }

  public Date getDatumvon() {
    return datumvon;
  }

  public Date getDatumbis() {
    return datumbis;
  }

  public Konto getKonto() {
    return konto;
  }

  public Buchungsart getBuchungsart() {
    return buchungart;
  }

  public Projekt getProjekt() {
    return projekt;
  }

  public String getText() {
    return text;
  }

  @SuppressWarnings("unchecked")
  public List<Buchung> get() throws RemoteException {
    final DBService service = Einstellungen.getDBService();

    DBIterator<Buchung> it = service.createList(Buchung.class);
    it.addFilter("datum >= ? ", datumvon);
    it.addFilter("datum <= ? ", datumbis);

    if (konto != null) {
      it.addFilter("konto = ? ", konto.getID());
    }
    if (buchungart != null) {
      if (buchungart.getNummer() == -1) {
        it.addFilter("buchungsart is null ");
      } else if (buchungart.getNummer() >= 0) {
        it.addFilter("buchungsart = ? ", buchungart.getID());
      }
    }

    if (hasMitglied != null) {
      if (hasMitglied) {
        it.addFilter("mitgliedskonto is not null");
      } else {
        it.addFilter("mitgliedskonto is null");
      }
    }

    if (projekt != null) {
      if (projekt.getID() == null) {
        it.addFilter("projekt is null");
      } else {
        it.addFilter("projekt = ?", projekt.getID());
      }
    }

    if (betrag != null && betrag.length() > 0) {
      try {
        Suchbetrag suchbetrag = new Suchbetrag(betrag);
        switch (suchbetrag.getSuchstrategie()) {
          case GLEICH: {
            it.addFilter("betrag = ?", suchbetrag.getBetrag());
            break;
          }
          case GRÖSSER: {
            it.addFilter("betrag > ?", suchbetrag.getBetrag());
            break;
          }
          case GRÖSSERGLEICH: {
            it.addFilter("betrag >= ?", suchbetrag.getBetrag());
            break;
          }
          case BEREICH:
            it.addFilter("betrag >= ? AND betrag <= ?", suchbetrag.getBetrag(),
                suchbetrag.getBetrag2());
            break;
          case KEINE:
            break;
          case KLEINER:
            it.addFilter("betrag < ?", suchbetrag.getBetrag());
            break;
          case KLEINERGLEICH:
            it.addFilter("betrag <= ?", suchbetrag.getBetrag());
            break;
          default:
            break;
        }
      } catch (Exception e) {
        // throw new RemoteException(e.getMessage());
      }
    }

    if (text.length() > 0) {
      String ttext = text.toUpperCase();
      ttext = "%" + ttext + "%";
      it.addFilter("(upper(name) like ? or upper(zweck) like ? or upper(kommentar) like ?) ", ttext,
          ttext, ttext);
    }
    switch (order) {
      case ORDER_UMSATZID: {
        it.setOrder("ORDER BY umsatzid DESC");
        break;
      }
      case ORDER_DATUM: {
        it.setOrder("ORDER BY datum");
        break;
      }
      case ORDER_DATUM_AUSZUGSNUMMER_BLATTNUMMER: {
        it.setOrder("ORDER BY datum, auszugsnummer, blattnummer, "
            + (Einstellungen.getEinstellung().getVerwendeBelegnummer() ? "belegnummer, " : "")
            + "id");
        break;
      }
      case ORDER_DATUM_NAME: {
        it.setOrder("ORDER BY datum, name, "
            + (Einstellungen.getEinstellung().getVerwendeBelegnummer() ? "belegnummer, " : "")
            + "id");
        break;
      }
      case ORDER_ID: {
        it.setOrder("ORDER BY "
            + (Einstellungen.getEinstellung().getVerwendeBelegnummer() ? "belegnummer, " : "")
            + "id");
        break;
      }
      case ORDER_DATUM_ID: {
        it.setOrder("ORDER BY datum, "
            + (Einstellungen.getEinstellung().getVerwendeBelegnummer() ? "belegnummer, " : "")
            + "id");
        break;
      }

    }

    this.ergebnis = PseudoIterator.asList(it);
    return ergebnis;
  }

  public String getSubtitle() throws RemoteException {
    String subtitle =
        String.format("vom %s bis %s", new JVDateFormatTTMMJJJJ().format(getDatumvon()),
            new JVDateFormatTTMMJJJJ().format(getDatumbis()));
    if (getKonto() != null) {
      subtitle += " "
          + String.format("für Konto %s - %s", getKonto().getNummer(), getKonto().getBezeichnung());
    }
    if (getProjekt() != null) {
      subtitle += ", " + String.format("Projekt %s", getProjekt().getBezeichnung());
    }
    if (getText() != null && getText().length() > 0) {
      subtitle += ", " + String.format("Text=%s", getText());
    }
    return subtitle;
  }

  public int getSize() {
    return ergebnis.size();
  }

}
