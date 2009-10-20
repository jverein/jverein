/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Ralf Neumaier
 * All rights reserved
 * www.jverein.de
 * $Log$
 **********************************************************************/
package de.jost_net.JVerein.util;

import java.math.BigInteger;

/**
 * 
 * @author rn
 * 
 */
public class IbanBicCalc
{
  public static final String[] ALPHABET = new String[] { "a", "b", "c", "d",
      "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
      "s", "t", "u", "v", "w", "x", "v", "z" };

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
   */
  public static String createIban(String kontoNr, String blz, String landKuerzel)
  {
    if (kontoNr == null || kontoNr.trim().length() == 0 || blz == null
        || blz.trim().length() == 0)
    {
      return "";
    }

    if (landKuerzel == null || landKuerzel.length() != 2)
    {
      return "Fehler ! Länderkürzel fehlt";
    }

    String leanderKennung = getLandKennung(landKuerzel);
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < 10 - kontoNr.length(); i++)
    {
      sb.append("0");
    }

    sb.append(kontoNr);
    BigInteger bi = new BigInteger(blz + sb.toString() + leanderKennung);
    BigInteger modulo = bi.mod(BigInteger.valueOf(97));
    String pruefZiffer = String.valueOf(98 - modulo.longValue());

    if (pruefZiffer.length() < 2)
    {
      pruefZiffer = "0" + pruefZiffer;
    }

    return landKuerzel + pruefZiffer + blz + sb.toString();
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
}