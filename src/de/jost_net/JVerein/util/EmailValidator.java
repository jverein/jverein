package de.jost_net.JVerein.util;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import de.willuhn.logging.Logger;

public class EmailValidator
{
  public static boolean isValid(String emailAddress)
  {
    if (emailAddress == null)
    {
      return false;
    }
    try
    {
      InternetAddress internetAddress = new InternetAddress(emailAddress);
      if (internetAddress.isGroup())
      {
        for (InternetAddress member : internetAddress.getGroup(true))
        {
          Check(member);
        }
      }
      else
      {
        Check(internetAddress);
      }
    }
    catch (AddressException e)
    {
      Logger.error("Ungültige E-Mail-Adresse \""+emailAddress+"\"", e);
      return false;
    }
    return true;
  }

  private static void Check(InternetAddress internetAddress) throws AddressException
  {
    internetAddress.validate();
    String[] tokens = internetAddress.getAddress().split("@");
    if (tokens.length != 2)
    {
      throw new AddressException("Missing final '@domain'", internetAddress.getAddress());
    }
    if (tokens[1].lastIndexOf('.') == -1)
    {
      throw new AddressException("Missing final '.tld'", internetAddress.getAddress());
    }
  }
}
