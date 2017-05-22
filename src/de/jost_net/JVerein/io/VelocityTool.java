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
package de.jost_net.JVerein.io;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import de.willuhn.logging.Logger;

public class VelocityTool
{
  static
  {
    try
    {
      Velocity.init();
    }
    catch (Exception e)
    {
      Logger.error("Fehler beim Velocity Init", e);
    }
  }

  public static String eval(Map<String, Object> map, String text)
      throws ParseErrorException, MethodInvocationException,
      ResourceNotFoundException, IOException
  {
    VelocityContext context = new VelocityContext(map);
    StringWriter wtext = new StringWriter();
    Velocity.evaluate(context, wtext, "LOG", text);
    return wtext.getBuffer().toString();
  }
}
