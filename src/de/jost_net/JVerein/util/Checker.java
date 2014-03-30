/**********************************************************************
 * Copyright (c) by Heiner Jostkleigrewe
 * This program is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,  but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See 
 *  the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, 
 * see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de
 * www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.util;

import java.util.StringTokenizer;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Checker
{
  public static boolean isValidEmailAddress(String emailAddress)
  {
    // a null string is invalid
    if (emailAddress == null)
    {
      return false;
    }

    if (emailAddress.startsWith(" ") || emailAddress.endsWith(" "))
    {
      return false;
    }

    int atindex = emailAddress.indexOf("@");
    // a string without a "@" is an invalid email address
    if (atindex < 0)
    {
      return false;
    }

    // a string without a "." after "@" is an invalid email address
    if (emailAddress.substring(atindex).indexOf(".") < 0)
    {
      return false;
    }

    // "." before "@" is invalid
    if (emailAddress.charAt(atindex - 1) == '.')
    {
      return false;
    }

    if (lastEmailFieldTwoCharsOrMore(emailAddress) == false)
    {
      return false;
    }
    try
    {
      new InternetAddress(emailAddress);
      return true;
    }
    catch (AddressException ae)
    {
      // log exception
      return false;
    }
  }

  /**
   * Returns true if the last email field (i.e., the country code, or something
   * like .com, .biz, .cc, etc.) is two chars or more in length, which it really
   * must be to be legal.
   */
  private static boolean lastEmailFieldTwoCharsOrMore(String emailAddress)
  {
    if (emailAddress == null)
    {
      return false;
    }
    StringTokenizer st = new StringTokenizer(emailAddress, ".");
    String lastToken = null;
    while (st.hasMoreTokens())
    {
      lastToken = st.nextToken();
    }

    if (lastToken.length() >= 2)
    {
      return true;
    }
    else
    {
      return false;
    }
  }
}