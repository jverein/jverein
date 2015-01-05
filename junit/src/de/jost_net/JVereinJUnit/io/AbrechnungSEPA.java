package de.jost_net.JVereinJUnit.io;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import de.jost_net.OBanToo.SEPA.SEPAException;
import de.jost_net.OBanToo.SEPA.Basislastschrift.Zahler;

@RunWith(JUnit4.class)
public class AbrechnungSEPA
{
  @Test
  public void test01() throws SEPAException
  {
    Zahler z1 = new Zahler();
    z1.setBetrag(new BigDecimal("1.00"));
    z1.setVerwendungszweck("Zweck 1");
    Zahler z2 = new Zahler();
    z2.setBetrag(new BigDecimal("1.00"));
    z2.setVerwendungszweck("Zweck 2");
    z1.add(z2);

    Zahler z3 = new Zahler();
    z3.setBetrag(new BigDecimal("1.00"));
    z3.setVerwendungszweck("Zweck 3");
    z1.add(z3);
    assertEquals("bla", z1.getVerwendungszweck());
  }

}
