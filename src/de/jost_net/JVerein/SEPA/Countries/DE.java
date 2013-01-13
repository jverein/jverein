package de.jost_net.JVerein.SEPA.Countries;

public class DE implements ISEPACountry
{

  @Override
  public String getCountry()
  {
    return "DE";
  }

  @Override
  public String getBezeichnung()
  {
    return "Deutschland";
  }

  @Override
  public int getBankIdentifierLength()
  {
    return 8;
  }

  @Override
  public int getAccountLength()
  {
    return 10;
  }

  @Override
  public String getBankIdentifierSample()
  {
    return "37040044";
  }

  @Override
  public String getAccountSample()
  {
    return "532013000";
  }

  @Override
  public String getIBANSample()
  {
    return "DE89370400440532013000";
  }

}
