package de.jost_net.JVerein.io;

import de.jost_net.OBanToo.SEPA.Basislastschrift.Zahler;

public class JVereinZahler extends Zahler {

  private String personId;
  private JVereinZahlerTyp personTyp;

  public JVereinZahler() {
  }

  public String getPersonId() {
    return personId;
  }

  public void setPersonId(String personId) {
    this.personId = personId;
  }

  public JVereinZahlerTyp getPersonTyp() {
    return personTyp;
  }

  public void setPersonTyp(JVereinZahlerTyp personTyp) {
    this.personTyp = personTyp;
  }

  
}
