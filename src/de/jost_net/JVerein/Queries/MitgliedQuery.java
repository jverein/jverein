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
 **********************************************************************/
package de.jost_net.JVerein.Queries;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ResultSetExtractor;

public class MitgliedQuery
{
  private MitgliedControl control;

  private boolean and = false;

  private String sql = "";

  public MitgliedQuery(MitgliedControl control)
  {
    this.control = control;
  }

  public ArrayList getQuery() throws RemoteException
  {
    return getQuery("*");
  }

  public ArrayList getQuery(String anfangsbuchstabe) throws RemoteException
  {
    final DBService service = Einstellungen.getDBService();

    sql = "select distinct mitglied.* " + "from mitglied ";
    String eigenschaften = (String) control.getEigenschaftenAuswahl().getText();
    sql += "where ";
    if (control.getMitgliedStatus().getValue().equals("Angemeldet"))
    {
      addCondition("austritt is null ");
    }
    else if (control.getMitgliedStatus().getValue().equals("Abgemeldet"))
    {
      addCondition("austritt is not null ");
    }
    if (eigenschaften != null && eigenschaften.length() > 0)
    {
      String condEigenschaft = "(select count(*) from eigenschaften where ";
      StringTokenizer st = new StringTokenizer(eigenschaften, "[]");
      condEigenschaft += "eigenschaften.mitglied = mitglied.id AND (";
      boolean first = true;
      while (st.hasMoreTokens())
      {
        if (!first)
        {
          condEigenschaft += "OR ";
        }
        st.nextToken();
        first = false;
        condEigenschaft += "eigenschaft = ? ";
      }
      condEigenschaft += ")) = ? ";
      addCondition(condEigenschaft);
    }

    if (!anfangsbuchstabe.equals("*"))
    {
      addCondition("(name like '" + anfangsbuchstabe + "%' OR " + "name like '"
          + anfangsbuchstabe.toLowerCase() + "%')");
    }

    if (control.getGeburtsdatumvon().getValue() != null)
    {
      addCondition("geburtsdatum >= ?");
    }
    if (control.getGeburtsdatumbis().getValue() != null)
    {
      addCondition("geburtsdatum <= ?");
    }
    Beitragsgruppe bg = (Beitragsgruppe) control.getBeitragsgruppeAusw()
        .getValue();
    if (bg != null)
    {
      addCondition("beitragsgruppe = ? ");
    }
    sql += "ORDER by name, vorname";
    ResultSetExtractor rs = new ResultSetExtractor()
    {
      public Object extract(ResultSet rs) throws RemoteException, SQLException
      {
        ArrayList<Mitglied> list = new ArrayList<Mitglied>();
        while (rs.next())
        {
          list.add((Mitglied) service.createObject(Mitglied.class, rs
              .getString(1)));
        }
        return list;
      }
    };
    ArrayList bedingungen = new ArrayList();
    if (eigenschaften != null && eigenschaften.length() > 0)
    {
      StringTokenizer st = new StringTokenizer(eigenschaften, "[]");
      int tokcount = 0;
      while (st.hasMoreTokens())
      {
        bedingungen.add(st.nextToken());
        tokcount++;
      }
      bedingungen.add(new Integer(tokcount));
    }
    if (control.getGeburtsdatumvon().getValue() != null)
    {
      Date d = (Date) control.getGeburtsdatumvon().getValue();
      bedingungen.add(new java.sql.Date(d.getTime()));
    }
    if (control.getGeburtsdatumbis().getValue() != null)
    {
      Date d = (Date) control.getGeburtsdatumbis().getValue();
      bedingungen.add(new java.sql.Date(d.getTime()));
    }
    if (bg != null)
    {
      bedingungen.add(new Integer(bg.getID()));
    }
    return (ArrayList) service.execute(sql, bedingungen.toArray(), rs);
  }

  private void addCondition(String condition)
  {
    if (and)
    {
      sql += "AND ";
    }
    and = true;
    sql += condition;
  }

}
