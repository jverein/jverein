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
 * Revision 1.3  2008/01/27 10:17:41  jost
 * Vereinheitlichung der Mitgliedersuche durch die Klasse MitgliedQuery
 *
 * Revision 1.2  2008/01/27 09:43:59  jost
 * Vereinheitlichung der Mitgliedersuche durch die Klasse MitgliedQuery
 *
 * Revision 1.1  2008/01/25 16:06:14  jost
 * Neu: Eigenschaften des Mitgliedes
 *
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
import de.willuhn.logging.Logger;

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
    if (control.isMitgliedStatusAktiv())
    {
      if (control.getMitgliedStatus().getValue().equals("Angemeldet"))
      {
        addCondition("austritt is null ");
      }
      else if (control.getMitgliedStatus().getValue().equals("Abgemeldet"))
      {
        addCondition("austritt is not null ");
      }
    }
    String eigenschaften = (String) control.getEigenschaftenAuswahl().getText();
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
    if (control.getEintrittvon().getValue() != null)
    {
      addCondition("eintritt >= ?");
    }
    if (control.getEintrittbis().getValue() != null)
    {
      addCondition("eintritt <= ?");
    }
    if (control.isAustrittbisAktiv())
    {
      if (control.getAustrittvon().getValue() != null)
      {
        addCondition("austritt >= ?");
      }
      if (control.getAustrittbis().getValue() != null)
      {
        addCondition("austritt <= ?");
      }
      if (control.getAustrittvon().getValue() == null
          && control.getAustrittbis().getValue() == null)
      {
        addCondition("austritt is null");
      }
    }
    Beitragsgruppe bg = (Beitragsgruppe) control.getBeitragsgruppeAusw()
        .getValue();
    if (bg != null)
    {
      addCondition("beitragsgruppe = ? ");
    }
    String sort = (String) control.getSortierung().getValue();
    if (sort.equals("Name, Vorname"))
    {
      sql += " ORDER BY name, vorname";
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
    if (control.isEintrittvonAktiv())
    {
      if (control.getEintrittvon().getValue() != null)
      {
        Date d = (Date) control.getEintrittvon().getValue();
        bedingungen.add(new java.sql.Date(d.getTime()));
      }
    }
    if (control.isEintrittbisAktiv())
    {
      if (control.getEintrittbis().getValue() != null)
      {
        Date d = (Date) control.getEintrittbis().getValue();
        bedingungen.add(new java.sql.Date(d.getTime()));
      }
    }
    if (control.isAustrittbisAktiv())
    {
      if (control.getAustrittvon().getValue() != null)
      {
        Date d = (Date) control.getAustrittvon().getValue();
        bedingungen.add(new java.sql.Date(d.getTime()));
      }
      if (control.getAustrittbis().getValue() != null)
      {
        Date d = (Date) control.getAustrittbis().getValue();
        bedingungen.add(new java.sql.Date(d.getTime()));
      }
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
