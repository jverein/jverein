package test.de.jost_net.JVerein.io;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import de.jost_net.JVerein.io.AltersgruppenParser;
import de.jost_net.JVerein.util.VonBis;
import de.willuhn.util.ApplicationException;

@RunWith(JUnit4.class)
public class AltersgruppenParserTest
{
  @Test
  public void test01() throws ApplicationException
  {
    AltersgruppenParser ap = new AltersgruppenParser(
        "0-5,6-10,11-16,17-25,25-100");
    VonBis vb = ap.getNext();
    assertEquals(0, vb.getVon());
    assertEquals(5, vb.getBis());
    vb = ap.getNext();
    assertEquals(6, vb.getVon());
    assertEquals(10, vb.getBis());
    vb = ap.getNext();
    assertEquals(11, vb.getVon());
    assertEquals(16, vb.getBis());
    vb = ap.getNext();
    assertEquals(17, vb.getVon());
    assertEquals(25, vb.getBis());
    vb = ap.getNext();
    assertEquals(25, vb.getVon());
    assertEquals(100, vb.getBis());
  }

  @Test
  public void test02() throws ApplicationException
  {
    AltersgruppenParser ap = new AltersgruppenParser("0-5,4-10");
  }
}
