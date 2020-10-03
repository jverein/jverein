/**********************************************************************
 * Copyright (c) by Heiner Jostkleigrewe This program is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.control.BuchungsControl;
import de.jost_net.JVerein.keys.SplitbuchungTyp;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.util.ApplicationException;

public class SplitbuchungsContainer {

  private static ArrayList<Buchung> splitbuchungen = null;

  public static void init(Buchung b) throws RemoteException, ApplicationException {
    splitbuchungen = new ArrayList<>();
    // Wenn eine gesplittete Buchung aufgerufen wird, wird die Hauptbuchung
    // gelesen
    if (b.getSplitId() != null) {
      b = (Buchung) Einstellungen.getDBService().createObject(Buchung.class, b.getSplitId() + "");
    } else {
      b.setSplitId(new Long(b.getID()));
      SplitbuchungsContainer.add(b);
    }
    DBIterator<Buchung> it = Einstellungen.getDBService().createList(Buchung.class);
    it.addFilter("splitid = ?", b.getID());

    if (!it.hasNext()) {
      // Wenn keine Buchung gefunden wurde, gibt es auch keine Gegenbuchung.
      // Dann wird die jetzt erstellt.
      Buchung b2 = (Buchung) Einstellungen.getDBService().createObject(Buchung.class, null);
      b2.setArt(b.getArt());
      b2.setAuszugsnummer(b.getAuszugsnummer());
      b2.setBetrag(b.getBetrag() * -1);
      b2.setBlattnummer(b.getBlattnummer());
      b2.setBuchungsart(b.getBuchungsartId());
      b2.setDatum(b.getDatum());
      b2.setKommentar(b.getKommentar());
      b2.setKonto(b.getKonto());
      b2.setMitgliedskonto(b.getMitgliedskonto());
      b2.setName(b.getName());
      b2.setSplitId(new Long(b.getID()));
      b2.setUmsatzid(b.getUmsatzid());
      b2.setBelegnummer(b.getBelegnummer());
      b2.setZweck(b.getZweck());
      b2.setSplitTyp(SplitbuchungTyp.GEGEN);
      SplitbuchungsContainer.add(b2);
    }
    while (it.hasNext()) {
      SplitbuchungsContainer.add((Buchung) it.next());
    }
  }

  public static ArrayList<Buchung> get() {
    return splitbuchungen;
  }

  public static Buchung getMaster() throws RemoteException {
    for (Buchung b : splitbuchungen) {
      if (b.getSplitTyp() == SplitbuchungTyp.HAUPT) {
        return b;
      }
    }
    throw new RemoteException("Hauptbuchung fehlt");
  }

  public static void add(Buchung b) throws RemoteException, ApplicationException {
    b.plausi();
    b.setSpeicherung(false);
    if (!splitbuchungen.contains(b)) {
      splitbuchungen.add(b);
    }
  }

  public static BigDecimal getSumme(Integer typ) throws RemoteException {
    BigDecimal summe = new BigDecimal(0).setScale(2);
    for (Buchung b : splitbuchungen) {
      if (b.getSplitTyp().equals(typ) && !b.isToDelete()) {
        summe = summe.add(new BigDecimal(b.getBetrag()).setScale(2, BigDecimal.ROUND_HALF_UP));
      }
    }
    return summe;
  }

  public static String getDifference() throws RemoteException {
    return Einstellungen.DECIMALFORMAT
        .format(getSumme(SplitbuchungTyp.HAUPT).subtract(getSumme(SplitbuchungTyp.SPLIT)));
  }

  public static void aufloesen() throws RemoteException, ApplicationException {
    for (Buchung b : splitbuchungen) {
      if (b.getSplitTyp() == SplitbuchungTyp.HAUPT) {
        b.setSplitId(null);
        b.store();
      } else {
        b.delete();
      }
    }
    splitbuchungen.clear();
  }

  public static void store() throws RemoteException, ApplicationException {
    if (splitbuchungen.size() == 0) // Splitbuchungen wurden aufgelöst
    {
      return;
    }
    BigDecimal gegen = getSumme(SplitbuchungTyp.GEGEN).multiply(new BigDecimal(-1));
    if (!getSumme(SplitbuchungTyp.HAUPT).equals(gegen)) {
      throw new RemoteException(
          "Die Minusbuchung muss den gleichen Betrag mit umgekehrtem Vorzeichen wie die Hauptbuchung haben.");
    }
    BigDecimal differenz =
        getSumme(SplitbuchungTyp.HAUPT).subtract(getSumme(SplitbuchungTyp.SPLIT));
    if (!differenz.equals(new BigDecimal(0).setScale(2))) {
      throw new RemoteException("Differenz zwischen Hauptbuchung und Gegenbuchungen: " + differenz);
    }

    Buchungsart ba_haupt = null;
    Buchungsart ba_gegen = null;
    for (Buchung b : get()) {
      if (b.getSplitTyp() == SplitbuchungTyp.HAUPT) {
        ba_haupt = b.getBuchungsart();
        if (ba_haupt == null) {
          throw new RemoteException("Buchungsart bei der Hauptbuchung fehlt");
        }
      }
      if (b.getSplitTyp() == SplitbuchungTyp.GEGEN) {
        ba_gegen = b.getBuchungsart();
        if (ba_gegen == null) {
          throw new RemoteException("Buchungsart bei der Gegenbuchung fehlt");
        }
      }
    }
    if (ba_haupt.getNummer() != ba_gegen.getNummer()) {
      throw new RemoteException("Buchungsarten bei Haupt- und Gegenbuchung müssen identisch sein");
    }
    for (Buchung b : get()) {
      if (b.isToDelete()) {
        b.delete();
      } else {
        b.store();
        BuchungsControl.setNewLastBelegnummer(b.getBelegnummer(), b.getDatum(),
            b.getKonto().getID());
      }
    }
  }
}
