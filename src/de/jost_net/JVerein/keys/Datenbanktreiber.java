package de.jost_net.JVerein.keys;

public enum Datenbanktreiber
{
  MYSQL("com.mysql.jdbc.Driver"), H2("org.h2.Driver");
  private String bezeichnung;

  Datenbanktreiber(String bezeichnung)
  {
    this.bezeichnung = bezeichnung;
  }

  public String getBezeichnung()
  {
    return bezeichnung;
  }

}
