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
package de.jost_net.JVerein.Queries;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.gui.input.MailAuswertungInput;
import de.jost_net.JVerein.gui.util.EigenschaftenUtil;
import de.jost_net.JVerein.keys.Datentyp;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Eigenschaft;
import de.jost_net.JVerein.rmi.EigenschaftGruppe;
import de.jost_net.JVerein.rmi.Felddefinition;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.logging.Logger;
import de.willuhn.util.Settings;

public class MitgliedQuery
{

  private MitgliedControl control;

  private boolean batch = false;

  private boolean and = false;

  private String sql = "";

  public MitgliedQuery(MitgliedControl control, boolean batch)
  {
    this.control = control;
    this.batch = batch;
  }

  public ArrayList<Mitglied> get(int adresstyp) throws RemoteException
  {
    final DBService service = Einstellungen.getDBService();
    ArrayList<Object> bedingungen = new ArrayList<Object>();

    sql = "select distinct mitglied.*, ucase(name), ucase(vorname) ";
    String sort = (String) control.getSortierung().getValue();
    if (sort.equals("Geburtstagsliste"))
    {
      sql += ", month(geburtsdatum), day(geburtsdatum) ";
    }
    sql += "from mitglied ";
    Settings settings = control.getSettings();
    char synonym = 'a';
    DBIterator fdit = Einstellungen.getDBService().createList(
        Felddefinition.class);
    if (settings.getInt("zusatzfelder.selected", 0) > fdit.size())
    {
      settings.setAttribute("zusatzfelder.selected", 0);
    }
    if (settings.getInt("zusatzfelder.selected", 0) > 0)
    {
      for (int i = 1; i <= settings.getInt("zusatzfelder.counter", 0); i++)
      {
        int definition = settings.getInt("zusatzfeld." + i + ".definition", -1);
        switch (settings.getInt("zusatzfeld." + i + ".datentyp", -1))
        {
          case Datentyp.ZEICHENFOLGE:
          {
            String value = settings.getString("zusatzfeld." + i + ".value",
                null).replace('*', '%');
            String cond = settings.getString("zusatzfeld." + i + ".cond", null);
            if (value != null && value.length() > 0)
            {
              sql += "join zusatzfelder " + synonym + " on " + synonym
                  + ".mitglied = mitglied.id  and lower(" + synonym + ".FELD) "
                  + cond + " lower( ? ) and " + synonym
                  + ".felddefinition = ? ";
              synonym++;
              bedingungen.add(value);
              bedingungen.add(definition);
            }
            break;
          }
          case Datentyp.DATUM:
          {
            String value = settings.getString("zusatzfeld." + i + ".value",
                null);
            String cond = settings.getString("zusatzfeld." + i + ".cond", null);
            if (value != null)
            {
              try
              {
                Date datum = new JVDateFormatTTMMJJJJ().parse(value);
                sql += "join zusatzfelder " + synonym + " on " + synonym
                    + ".mitglied = mitglied.id  and " + synonym + ".FELDDATUM "
                    + cond + " ? and " + synonym + ".felddefinition = ? ";
                bedingungen.add(datum);
                bedingungen.add(definition);
                synonym++;
              }
              catch (ParseException e)
              {
                //
              }
            }
            break;
          }
          case Datentyp.GANZZAHL:
          {
            int value = settings.getInt("zusatzfeld." + i + ".value",
                Integer.MIN_VALUE);
            String cond = settings.getString("zusatzfeld." + i + ".cond", null);
            if (value != Integer.MIN_VALUE)
            {
              sql += "join zusatzfelder " + synonym + " on " + synonym
                  + ".mitglied = mitglied.id  and " + synonym
                  + ".FELDGANZZAHL " + cond + " ? and " + synonym
                  + ".felddefinition = ? ";
              bedingungen.add(value);
              bedingungen.add(definition);
              synonym++;
            }
            break;
          }
          case Datentyp.JANEIN:
          {
            boolean value = settings.getBoolean("zusatzfeld." + i + ".value",
                false);
            if (value)
            {
              sql += "join zusatzfelder " + synonym + " on " + synonym
                  + ".mitglied = mitglied.id  and " + synonym
                  + ".FELDJANEIN = true and " + synonym
                  + ".felddefinition = ? ";
              bedingungen.add(definition);
              synonym++;
            }
            break;
          }
          case Datentyp.WAEHRUNG:
          {
            String value = settings.getString("zusatzfeld." + i + ".value",
                null);
            String cond = settings.getString("zusatzfeld." + i + ".cond", null);
            if (value != null)
            {
              try
              {
                Number n = Einstellungen.DECIMALFORMAT.parse(value);
                sql += "join zusatzfelder " + synonym + " on " + synonym
                    + ".mitglied = mitglied.id  and " + synonym
                    + ".FELDWAEHRUNG " + cond + " ? and " + synonym
                    + ".felddefinition = ? ";
                bedingungen.add(n);
                bedingungen.add(definition);
              }
              catch (ParseException e)
              {
                //
              }
              synonym++;
            }
            break;
          }
        }
      }
    }
    addCondition("adresstyp = " + adresstyp);
    if (control.isMitgliedStatusAktiv())
    {
      if (control.getMitgliedStatus().getValue().equals("Angemeldet"))
      {
        addCondition("(austritt is null or austritt > current_date())");
      }
      else if (control.getMitgliedStatus().getValue().equals("Abgemeldet"))
      {
        addCondition("austritt is not null and austritt <= current_date()");
      }
    }
    int mailauswahl = (Integer) control.getMailauswahl().getValue();
    if (batch && mailauswahl == MailAuswertungInput.OHNE)
    {
      addCondition("(email is null or length(email) = 0)");
    }
    if (batch && mailauswahl == MailAuswertungInput.MIT)
    {
      addCondition("(email is  not null and length(email) > 0)");
    }
    String eigenschaften = "";
    eigenschaften = control.getEigenschaftenString();
    if (eigenschaften.length() > 0)
    {
      EigenschaftenUtil eiu = new EigenschaftenUtil(eigenschaften);
      if (control.getEigenschaftenVerknuepfung().equals("und"))
      {
        for (EigenschaftGruppe eg : eiu.getGruppen())
        {
          StringBuilder condEigenschaft = new StringBuilder(
              "(select count(*) from eigenschaften where ");
          boolean first = true;
          condEigenschaft.append("eigenschaften.mitglied = mitglied.id AND (");
          for (Eigenschaft ei : eiu.get(eg))
          {
            if (!first)
            {
              condEigenschaft.append("OR ");
            }
            first = false;
            condEigenschaft.append("eigenschaft = ? ");
            bedingungen.add(ei.getID());
          }
          condEigenschaft.append(")) > 0 ");
          addCondition(condEigenschaft.toString());
        }
      }
      else
      {
        StringBuilder condEigenschaft = new StringBuilder(
            "(select count(*) from eigenschaften where ");
        boolean first = true;
        condEigenschaft.append("eigenschaften.mitglied = mitglied.id AND (");
        for (Eigenschaft ei : eiu.getEigenschaften())
        {
          if (!first)
          {
            condEigenschaft.append("OR ");
          }
          first = false;
          condEigenschaft.append("eigenschaft = ? ");
          bedingungen.add(ei.getID());
        }
        condEigenschaft.append(")) > 0 ");
        addCondition(condEigenschaft.toString());
      }
    }
    String tmpSuchname = (String) control.getSuchname().getValue();
    if (!batch && tmpSuchname.length() > 0)
    {
      addCondition("(lower(name) like '" + tmpSuchname.toLowerCase() + "%')");
    }

    if (control.getGeburtsdatumvon().getValue() != null)
    {
      addCondition("geburtsdatum >= ?");
      Date d = (Date) control.getGeburtsdatumvon().getValue();
      bedingungen.add(new java.sql.Date(d.getTime()));
    }
    if (control.getGeburtsdatumbis().getValue() != null)
    {
      addCondition("geburtsdatum <= ?");
      Date d = (Date) control.getGeburtsdatumbis().getValue();
      bedingungen.add(new java.sql.Date(d.getTime()));
    }

    if (batch && control.getSterbedatumvon().getValue() != null)
    {
      addCondition("sterbetag >= ?");
      Date d = (Date) control.getSterbedatumvon().getValue();
      bedingungen.add(new java.sql.Date(d.getTime()));
    }
    if (batch && control.getSterbedatumbis().getValue() != null)
    {
      addCondition("sterbetag <= ?");
      Date d = (Date) control.getSterbedatumbis().getValue();
      bedingungen.add(new java.sql.Date(d.getTime()));
    }
    if (control.getGeschlecht().getText() != null
        && !control.getGeschlecht().getText().equals("Bitte auswählen"))
    {
      addCondition("geschlecht = ?");
      String g = (String) control.getGeschlecht().getValue();
      bedingungen.add(g);
    }
    if (control.getEintrittvon().getValue() != null)
    {
      addCondition("eintritt >= ?");
      Date d = (Date) control.getEintrittvon().getValue();
      bedingungen.add(new java.sql.Date(d.getTime()));
    }
    if (control.getEintrittbis().getValue() != null)
    {
      addCondition("eintritt <= ?");
      Date d = (Date) control.getEintrittbis().getValue();
      bedingungen.add(new java.sql.Date(d.getTime()));
    }
    if (control.isAustrittbisAktiv())
    {
      if (control.getAustrittvon().getValue() != null)
      {
        addCondition("austritt >= ?");
        Date d = (Date) control.getAustrittvon().getValue();
        bedingungen.add(new java.sql.Date(d.getTime()));
      }
      if (control.getAustrittbis().getValue() != null)
      {
        addCondition("austritt <= ?");
        Date d = (Date) control.getAustrittbis().getValue();
        bedingungen.add(new java.sql.Date(d.getTime()));
      }
      if (control.getSterbedatumvon() == null
          && control.getSterbedatumbis() == null
          && control.getAustrittvon().getValue() == null
          && control.getAustrittbis().getValue() == null)
      {
        addCondition("(austritt is null or austritt > current_date())");
      }
    }
    if (Einstellungen.getEinstellung().getExterneMitgliedsnummer())
    {
      try
      {
        if (control.getSuchExterneMitgliedsnummer().getValue() != null)
        {
          addCondition("externemitgliedsnummer = ?");
        }
      }
      catch (NullPointerException e)
      {
        // Workaround für einen Bug in IntegerInput
      }
    }
    Beitragsgruppe bg = (Beitragsgruppe) control.getBeitragsgruppeAusw()
        .getValue();
    if (bg != null)
    {
      addCondition("beitragsgruppe = ? ");
    }
    if (sort.equals("Name, Vorname"))
    {
      sql += " ORDER BY ucase(name), ucase(vorname)";
    }
    else if (sort.equals("Eintrittsdatum"))
    {
      sql += " ORDER BY eintritt";
    }
    else if (sort.equals("Geburtsdatum"))
    {
      sql += " ORDER BY geburtsdatum";
    }
    else if (sort.equals("Geburtstagsliste"))
    {
      sql += " ORDER BY month(geburtsdatum), day(geburtsdatum)";
    }
    else
    {
      sql += " ORDER BY name, vorname";
    }
    Logger.debug(sql);

    ResultSetExtractor rs = new ResultSetExtractor()
    {
      @Override
      public Object extract(ResultSet rs) throws RemoteException, SQLException
      {
        ArrayList<Mitglied> list = new ArrayList<Mitglied>();
        while (rs.next())
        {
          list.add((Mitglied) service.createObject(Mitglied.class,
              rs.getString(1)));
        }
        return list;
      }
    };

    try
    {
      if (Einstellungen.getEinstellung().getExterneMitgliedsnummer()
          && control.getSuchExterneMitgliedsnummer().getValue() != null)
      {
        bedingungen.add(control.getSuchExterneMitgliedsnummer().getValue());
      }
    }
    catch (NullPointerException e)
    {
      // Workaround f. Bug in IntegerInput
    }
    if (bg != null)
    {
      bedingungen.add(new Integer(bg.getID()));
    }
    return (ArrayList<Mitglied>) service
        .execute(sql, bedingungen.toArray(), rs);
  }

  private void addCondition(String condition)
  {
    if (and)
    {
      sql += " AND ";
    }
    else
    {
      sql += "where ";
    }
    and = true;
    sql += condition;
  }
}
