/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * heiner@jverein.de
 * www.jverein.de
 * $Log$
 *
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.io.IOException;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.keys.Datentyp;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Felddefinition;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Zusatzfelder;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;

public class Variable
{
  private VelocityContext context = null;

  private HashMap<String, String> format;

  public Variable() throws Exception
  {
    Velocity.init();
    format = new HashMap<String, String>();
  }

  public void set(Mitglied m) throws RemoteException
  {
    context = new VelocityContext();
    context.put("dateformat", new JVDateFormatTTMMJJJJ());
    context.put("decimalformat", Einstellungen.DECIMALFORMAT);
    context.put("mitglied", m);
    for (String st : m.getAttributeNames())
    {
      format.put("mitglied." + st, m.getAttributeType(st));
    }
    Beitragsgruppe bg = (Beitragsgruppe) Einstellungen.getDBService()
        .createObject(Beitragsgruppe.class, null);
    for (String st : bg.getAttributeNames())
    {
      format.put("mitglied.beitragsgruppe." + st, bg.getAttributeType(st));
      System.out.println(bg.getAttributeType(st));
    }

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    HashMap<String, String> zusatzf = new HashMap<String, String>();
    DBIterator itfd = Einstellungen.getDBService().createList(
        Felddefinition.class);
    while (itfd.hasNext())
    {
      Felddefinition fd = (Felddefinition) itfd.next();
      DBIterator itzus = Einstellungen.getDBService().createList(
          Zusatzfelder.class);
      itzus.addFilter("mitglied = ? and felddefinition = ? ",
          new Object[] { m.getID(), fd.getID() });
      Zusatzfelder z = null;
      if (itzus.hasNext())
      {
        z = (Zusatzfelder) itzus.next();
      }
      else
      {
        z = (Zusatzfelder) Einstellungen.getDBService().createObject(
            Zusatzfelder.class, null);
      }

      switch (fd.getDatentyp())
      {
        case Datentyp.DATUM:
          if (z.getFeldDatum() != null)
          {
            zusatzf.put(fd.getName(), sdf.format(z.getFeldDatum()));
            format.put("zusatzfelder." + fd.getName(), "DATE");
          }
          else
          {
            zusatzf.put(fd.getName(), "");
          }
          break;
        case Datentyp.JANEIN:
          zusatzf.put(fd.getName(), z.getFeldJaNein() ? "X" : " ");
          break;
        case Datentyp.GANZZAHL:
          zusatzf.put(fd.getName(), z.getFeldGanzzahl() + "");
          format.put("zusatzfelder." + fd.getName(), "INTEGER");
          break;
        case Datentyp.WAEHRUNG:
          if (z.getFeldWaehrung() != null)
          {
            zusatzf.put(fd.getName(),
                Einstellungen.DECIMALFORMAT.format(z.getFeldWaehrung()));
            format.put("zusatzfelder." + fd.getName(), "DOUBLE");
          }
          else
          {
            zusatzf.put(fd.getName(), "");
          }
          break;
        case Datentyp.ZEICHENFOLGE:
          zusatzf.put(fd.getName(), z.getFeld());
          break;
      }
      context.put("zusatzfeld", zusatzf);
    }
    context.put("tagesdatum", sdf.format(new Date()));
    format.put("tagesdatum", "DATE");
    sdf.applyPattern("MM.yyyy");
    context.put("aktuellermonat", sdf.format(new Date()));
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.MONTH, 1);
    context.put("folgemonat", sdf.format(calendar.getTime()));
    calendar.add(Calendar.MONTH, -2);
    context.put("vormonat", sdf.format(calendar.getTime()));
  }

  public ArrayList<String> getVariablenNamen() throws RemoteException
  {
    if (context == null)
    {
      throw new RuntimeException("Set(Mitglied) wurde noch nicht aufgerufen");
    }
    ArrayList<String> liste = new ArrayList<String>();
    for (Object o : context.getKeys())
    {
      String s = (String) o;
      if (s.equals("mitglied"))
      {
        Mitglied m = (Mitglied) context.get("mitglied");
        for (String st : m.getAttributeNames())
        {
          liste.add("mitglied." + st);
        }
        liste.add("mitglied.namevorname");
        liste.add("mitglied.vornamename");
        liste.add("mitglied.empfaenger");
        liste.remove("mitglied.beitragsgruppe");
        // liste.add("mitglied.beitragsgruppe.betrag");
        // liste.add("mitglied.beitragsgruppe.bezeichnung");
        Beitragsgruppe bg = m.getBeitragsgruppe();
        if (bg == null)
        {
          bg = (Beitragsgruppe) Einstellungen.getDBService().createObject(
              Beitragsgruppe.class, null);
        }
        for (String st : bg.getAttributeNames())
        {
          liste.add("mitglied.beitragsgruppe." + st);
        }
      }
      else if (s.equals("zusatzfeld"))
      {
        DBIterator it = Einstellungen.getDBService().createList(
            Felddefinition.class);
        while (it.hasNext())
        {
          Felddefinition f = (Felddefinition) it.next();
          liste.add("zusatzfeld." + f.getName());
        }
      }
      else
      {
        String st = (String) o;
        if (!st.equals("dateformat") && !st.equals("decimalformat"))
        {
          liste.add((String) o);
        }
      }
    }
    Collections.sort(liste);
    return liste;
  }

  public String evaluate(String value, boolean addVelocity)
      throws ParseErrorException, MethodInvocationException,
      ResourceNotFoundException, IOException
  {
    StringWriter ret = new StringWriter();
    if (addVelocity)
    {
      String s = "${" + value + "}";
      String f = format.get(value);
      if (f == null)
      {
        f = "VARCHAR";
      }
      if (f.equals("DATE"))
      {
        s = "$!{dateformat.format(" + s + ")}";
      }
      if (f.equals("DOUBLE"))
      {
        s = "$!{decimalformat.format(" + s + ")}";
      }
      value = s;
    }
    try
    {
      Velocity.evaluate(context, ret, "LOG", value);
    }
    catch (MethodInvocationException e)
    {
      // tritt auf, wenn ein Feld formatiert werden soll (DecimalFormat oder
      // DateFormat), dass null ist
      // Es ist nichts weiter zu tun.
    }
    return ret.toString();
  }
}
