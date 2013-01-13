package de.jost_net.JVerein.SEPA.Countries;

public class AT implements ISEPACountry
{

  @Override
  public String getCountry()
  {
    return "AT";
  }

  @Override
  public String getBezeichnung()
  {
    return "Österreich";
  }

  @Override
  public int getBankIdentifierLength()
  {
    return 5;
  }

  @Override
  public int getAccountLength()
  {
    return 11;
  }

  @Override
  public String getBankIdentifierSample()
  {
    return "00762";
  }

  @Override
  public String getAccountSample()
  {
    return "011623852957";
  }

  @Override
  public String getIBANSample()
  {
    return "CH9300762011623852957";
  }

}
