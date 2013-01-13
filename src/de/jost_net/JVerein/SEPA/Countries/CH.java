package de.jost_net.JVerein.SEPA.Countries;

public class CH implements ISEPACountry
{

  @Override
  public String getCountry()
  {
    return "CH";
  }

  @Override
  public String getBezeichnung()
  {
    return "Schweiz";
  }

  @Override
  public int getBankIdentifierLength()
  {
    return 5;
  }

  @Override
  public int getAccountLength()
  {
    return 12;
  }

  @Override
  public String getBankIdentifierSample()
  {
    return "19043";
  }

  @Override
  public String getAccountSample()
  {
    return "234573201";
  }

  @Override
  public String getIBANSample()
  {
    return "AT611904300234573201";
  }

}
