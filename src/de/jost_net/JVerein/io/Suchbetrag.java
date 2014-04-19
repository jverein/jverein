package de.jost_net.JVerein.io;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

public class Suchbetrag
{

  public enum Suchstrategie
  {
    KEINE, GLEICH, GRÖSSER, GRÖSSERGLEICH, KLEINER, KLEINERGLEICH, BEREICH, UNGÜLTIG
  }

  private Suchstrategie sustrat = Suchstrategie.KEINE;

  private BigDecimal betrag;

  private BigDecimal betrag2 = null;

  public final static DecimalFormat DECIMALFORMAT = new DecimalFormat(
      "###,###.##");

  public Suchbetrag(String suchbetrag) throws Exception
  {
    if (suchbetrag == null || suchbetrag.length() == 0)
    {
      sustrat = Suchstrategie.KEINE;
      return;
    }

    // Suchstring in Einzelteile zerlegen
    ArrayList<String> liste = new ArrayList<String>();
    StringTokenizer tok = new StringTokenizer(suchbetrag, "<>=.", true);
    while (tok.hasMoreTokens())
    {
      liste.add(tok.nextToken().trim());
    }
    for (int i = 0; i < liste.size() - 1; i++)
    {
      if (liste.get(i).equals(">") && liste.get(i + 1).equals("="))
      {
        liste.set(i, ">=");
        liste.remove(i + 1);
      }
      else if (liste.get(i).equals("<") && liste.get(i + 1).equals("="))
      {
        liste.set(i, "<=");
        liste.remove(i + 1);
      }
      else if (liste.get(i).equals(".") && liste.get(i + 1).equals("."))
      {
        liste.set(i, "..");
        liste.remove(i + 1);
      }
    }
    if (liste.get(0).equals(">"))
    {
      sustrat = Suchstrategie.GRÖSSER;
      liste.remove(0);
    }
    else if (liste.get(0).equals(">="))
    {
      sustrat = Suchstrategie.GRÖSSERGLEICH;
      liste.remove(0);
    }
    else if (liste.get(0).equals("<"))
    {
      sustrat = Suchstrategie.KLEINER;
      liste.remove(0);
    }
    else if (liste.get(0).equals("<="))
    {
      sustrat = Suchstrategie.KLEINERGLEICH;
      liste.remove(0);
    }
    else if (liste.get(0).equals("="))
    {
      sustrat = Suchstrategie.GLEICH;
      liste.remove(0);
    }
    else if (liste.size() > 1 && liste.get(1).equals(".."))
    {
      sustrat = Suchstrategie.BEREICH;
      liste.remove(1);
    }
    else if (liste.size() > 1 && !liste.get(1).equals(".."))
    {
      throw new Exception("Wert ungültig");
    }
    else if (liste.size() == 1)
    {
      sustrat = Suchstrategie.GLEICH; // Nur Betrag angegeben.
    }

    // jetzt muss ein Decimalwert kommen
    try
    {
      NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
      betrag = new BigDecimal(nf.parse(liste.get(0)).toString());

      if (sustrat == Suchstrategie.BEREICH)
      {
        betrag2 = new BigDecimal(nf.parse(liste.get(1)).toString());
      }
    }
    catch (ParseException e)
    {
      sustrat = Suchstrategie.UNGÜLTIG;
      throw new Exception("Wert ungültig");
    }
  }

  public Suchstrategie getSuchstrategie()
  {
    return sustrat;
  }

  public BigDecimal getBetrag()
  {
    return betrag;
  }

  public BigDecimal getBetrag2()
  {
    return betrag2;
  }
}
