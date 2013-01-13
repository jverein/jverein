package de.jost_net.JVerein.SEPA.Countries;

public interface ISEPACountry
{
  public String getCountry();

  public String getBezeichnung();

  public int getBankIdentifierLength();

  public int getAccountLength();

  public String getBankIdentifierSample();

  public String getAccountSample();

  public String getIBANSample();
}
