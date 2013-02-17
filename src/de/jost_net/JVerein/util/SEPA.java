/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Ralf Neumaier
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
package de.jost_net.JVerein.util;

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.HashMap;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Bank;
import de.jost_net.JVerein.rmi.SEPAParam;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.logging.Logger;

/**
 * 
 * @author rn und Heiner Jostkleigrewe
 * 
 */
public class SEPA
{
  private static final String[] ALPHABET = new String[] { "A", "B", "C", "D",
      "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
      "S", "T", "U", "V", "W", "X", "Y", "Z" };

  private static HashMap<String, SEPAParam> countries = new HashMap<String, SEPAParam>();
  static
  {
    try
    {
      DBIterator it = Einstellungen.getDBService().createList(SEPAParam.class);
      it.setOrder("ORDER BY id");
      while (it.hasNext())
      {
        SEPAParam sepapa = (SEPAParam) it.next();
        countries.put(sepapa.getID(), sepapa);
      }
    }
    catch (RemoteException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * 
   * Vor der Erstellung der persönlichen internationalen Bankkontonummer für
   * jeden Kontoinhaber wird von der Bank die Prüfziffer elektronisch berechnet.
   * Dazu werden in Deutschland die achtstellige Bankleitzahl, die zehnstellige
   * Kontonummer und die zweistellige, alphanumerische Länderkennung benötigt.
   * Kontonummern mit weniger als zehn Stellen werden mit führenden Nullen
   * aufgefüllt.
   * 
   * Die Berechnung erfolgt in mehreren Schritten. Zuerst wird die Länderkennung
   * um zwei Nullen ergänzt. Danach wird aus Kontonummer und Bankleitzahl die
   * BBAN kreiert. Also beispielsweise Bankleitzahl 70090100 und Kontonummer
   * 1234567890 ergeben die BBAN 700901001234567890.
   * 
   * Anschließend werden die beiden Alpha-Zeichen der Länderkennung sowie
   * weitere eventuell in der Kontonummer enthaltene Buchstaben in rein
   * numerische Ausdrücke umgewandelt. Die Grundlage für die Zahlen, die aus den
   * Buchstaben gebildet werden sollen, bildet ihre Position der jeweiligen
   * Alpha-Zeichen im lateinischen Alphabet. Zu diesem Zahlenwert wird 9
   * addiert. Die Summe ergibt die Zahl, die den jeweiligen Buchstaben ersetzen
   * soll. Dementsprechend steht für A (Position 1+9) die Zahl 10, für D
   * (Position 4+9) die 13 und für E (Position 5+9) die 14. Der Länderkennung DE
   * entspricht also die Ziffernfolge 1314.
   * 
   * Im nächsten Schritt wird diese Ziffernfolge, ergänzt um die beiden Nullen,
   * an die BBAN gehängt. Hieraus ergibt sich 700901001234567890131400. Diese
   * bei deutschen Konten immer 24-stellige Zahl wird anschließend Modulo 97
   * genommen. Das heißt, es wird der Rest berechnet, der sich bei der Teilung
   * der 24-stelligen Zahl durch 97 ergibt. Das ist für dieses Beispiel 90.
   * Dieses Ergebnis wird von der nach ISO-Standard festgelegten Zahl 98
   * subtrahiert. Ist das Resultat, wie in diesem Beispiel, kleiner als Zehn, so
   * wird der Zahl eine Null vorangestellt, sodass sich wieder ein zweistelliger
   * Wert ergibt. Somit ist die errechnete Prüfziffer 08. Aus der Länderkennung,
   * der zweistelligen Prüfsumme und der BBAN wird nun die IBAN generiert. Die
   * ermittelte IBAN lautet in unserem Beispiel: DE08700901001234567890.
   * 
   * D = 4.stelle im Alphabet + 9 = 13 E = 5.stelle im Alphabet + 9 = 14
   * 
   * -> DE = 1314
   * 
   * Länderkennung um 2 Nullen ergänzen
   * 
   * -> 131400
   * 
   * 1. Konstante zur Berechnung; Modulo 97
   * 
   * 700901001234567890131400 % 97
   * 
   * -> 90
   * 
   * 2. Konstante zur Berechnung: 98 - 90 = 8 -> ergänzt um führende 0 -> 08
   * 
   * DE08700901001234567890131400
   * 
   * @throws RemoteException
   * 
   */
  public static String createIban(String kontoNr, String blz, String landKuerzel)
      throws SEPAException, RemoteException
  {
    if (kontoNr == null || kontoNr.trim().length() == 0 || blz == null
        || blz.trim().length() == 0)
    {
      return "";
    }

    SEPAParam country = getSEPAParam(landKuerzel);
    String laenderKennung = getLandKennung(landKuerzel);

    if (blz.length() != country.getBankIdentifierLength().intValue())
    {
      throw new SEPAException("Bankleitzahl hat falsche Länge für "
          + country.getBezeichnung());
    }

    if (kontoNr.length() > country.getAccountLength().intValue())
    {
      throw new SEPAException("Kontonummer zu lang für "
          + country.getBezeichnung());
    }
    StringBuilder accountString = new StringBuilder();
    for (int i = 0; i < country.getAccountLength().intValue()
        - kontoNr.length(); i++)
    {
      accountString.append("0");
    }
    accountString.append(kontoNr);
    return landKuerzel
        + getPruefziffer(blz, accountString.toString(), laenderKennung) + blz
        + accountString.toString();
  }

  public static String getBLZFromIban(String iban) throws SEPAException
  {
    if (iban.length() == 0)
    {
      return "";
    }
    String ret = "";
    if (isValidIBAN(iban))
    {
      SEPAParam param = getSEPAParam(iban.substring(0, 2));
      try
      {
        ret = iban.substring(4, param.getBankIdentifierLength() + 4);
      }
      catch (RemoteException e)
      {
        throw new SEPAException(e.getMessage());
      }
    }
    return ret;
  }

  public static String getKontoFromIban(String iban) throws SEPAException
  {
    if (iban.length() == 0)
    {
      return "";
    }
    String ret = "";
    if (isValidIBAN(iban))
    {
      SEPAParam param = getSEPAParam(iban.substring(0, 2));
      try
      {
        ret = iban.substring(4 + param.getBankIdentifierLength(),
            param.getAccountLength() + param.getBankIdentifierLength() + 4);
      }
      catch (RemoteException e)
      {
        throw new SEPAException(e.getMessage());
      }
    }
    return ret;
  }

  public static SEPAParam getSEPAParam(String landkuerzel) throws SEPAException
  {
    if (landkuerzel == null || landkuerzel.length() != 2)
    {
      throw new SEPAException("Fehler ! Länderkürzel fehlt");
    }
    SEPAParam sepapa = countries.get(landkuerzel);
    if (sepapa == null)
    {
      throw new SEPAException("Ungültiges Land: " + landkuerzel);
    }
    return sepapa;
  }

  private static String getPruefziffer(String blz, String konto,
      String laenderkennung) throws SEPAException
  {
    BigInteger bi = null;
    try
    {
      bi = new BigInteger(blz + konto + laenderkennung);
    }
    catch (NumberFormatException e)
    {
      String error = JVereinPlugin.getI18n().tr(
          "Ungültige Bankverbindung: {0} {1} {2}", blz, konto, laenderkennung);
      Logger.error(error);
      throw new SEPAException(error);
    }
    BigInteger modulo = bi.mod(BigInteger.valueOf(97));
    String pruefZiffer = String.valueOf(98 - modulo.longValue());

    if (pruefZiffer.length() < 2)
    {
      pruefZiffer = "0" + pruefZiffer;
    }
    return pruefZiffer;
  }

  private static final String getLandKennung(String landKuerzel)
  {
    int[] landKnzAsNumber = new int[2];

    for (int i = 0; i < 2; i++)
    {
      for (int j = 0; j < ALPHABET.length; j++)
      {
        if (ALPHABET[j].toUpperCase().equals(
            String.valueOf(landKuerzel.charAt(i)).toUpperCase()))
        {
          landKnzAsNumber[i] = j + 10;
          break;
        }
      }
    }

    return String.valueOf(landKnzAsNumber[0])
        + String.valueOf(landKnzAsNumber[1]) + "00";
  }

  public static boolean isValidIBAN(String iban) throws SEPAException
  {
    if (iban == null)
    {
      throw new SEPAException("IBAN ist leer");
    }
    if (iban.length() < 4)
    {
      throw new SEPAException(
          "Ungültige IBAN. Landeskennung und/oder Prüfziffer fehlen");
    }
    String landkuerzel = iban.substring(0, 2);
    SEPAParam country = getSEPAParam(landkuerzel);
    // String pruefziffer = iban.substring(3, 4);
    try
    {
      int laebankid = country.getBankIdentifierLength();
      int laeaccount = country.getAccountLength();
      int laeiban = 4 + laebankid + laeaccount;
      if (iban.length() != laeiban)
      {
        throw new SEPAException("Ungültige IBAN-Länge. Vorgeschrieben sind "
            + laeiban + " Stellen für " + country.getBezeichnung());
      }
    }
    catch (RemoteException e)
    {
      throw new SEPAException(e.getMessage());
    }
    return true;
  }

  public static boolean isValidBIC(String bic) throws SEPAException
  {
    if (bic == null)
    {
      throw new SEPAException("BIC ist leer");
    }
    if (bic.length() != 8 && bic.length() != 11)
    {
      throw new SEPAException(
          "Ungültige Länge der BIC. Die BIC muss entweder 8 oder 11 Stellen lang sein.");
    }
    String landkuerzel = bic.substring(4, 6);
    getSEPAParam(landkuerzel); // Wirft eine Exception bei ungültigem Kürzel
    if (landkuerzel.equals("DE"))
    {
      try
      {
        Bank b = SEPA.getBankByBIC(bic);
        if (b == null)
        {
          throw new SEPAException(
              "BIC nicht in der Datenbank der Bundesbank enthalten");
        }
      }
      catch (RemoteException e)
      {
        e.printStackTrace();
      }
    }
    return true;
  }

  public static boolean isValid(String bic, String iban) throws SEPAException,
      RemoteException
  {
    if (bic.length() == 0 && iban.length() == 0)
    {
      return true;
    }
    if (iban.length() < 4)
    {
      return false;
    }
    SEPAParam param = getSEPAParam(iban.substring(0, 2));
    if (!param.getID().equals("DE"))
    {
      return true;
    }
    String blz = iban.substring(4, param.getBankIdentifierLength() + 4);
    Bank b = getBankByBLZ(blz);
    return (bic.equals(b.getBIC()));
  }

  public static Bank getBankByBLZ(String blz) throws RemoteException
  {
    DBIterator bankit = Einstellungen.getDBService().createList(Bank.class);
    bankit.addFilter("blz = ?", blz);
    if (bankit.hasNext())
    {
      return (Bank) bankit.next();
    }
    return null;
  }

  public static Bank getBankByBIC(String bic) throws RemoteException
  {
    DBIterator bankit = Einstellungen.getDBService().createList(Bank.class);
    bankit.addFilter("bic = ?", bic);
    if (bankit.hasNext())
    {
      return (Bank) bankit.next();
    }
    return null;
  }

}