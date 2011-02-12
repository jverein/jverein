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
 * Revision 1.23  2011-02-02 22:00:26  jost
 * Auswertung erweitert um den Parameter "ohne EMail"
 *
 * Revision 1.22  2011-01-27 22:23:51  jost
 * Neu: Speicherung von weiteren Adressen in der Mitgliedertabelle
 *
 * Revision 1.21  2010-11-13 09:27:53  jost
 * Warnings entfernt.
 *
 * Revision 1.20  2010-10-30 11:31:38  jost
 * Neu: Sterbetag
 *
 * Revision 1.19  2010-10-15 09:58:29  jost
 * Code aufgeräumt
 *
 * Revision 1.18  2010-03-27 20:10:55  jost
 * Bugfix Eigenschaftensuche
 *
 * Revision 1.17  2009/11/19 19:44:54  jost
 * Bugfix Eigenschaften
 *
 * Revision 1.16  2009/11/17 21:01:25  jost
 * Neu: Eigenschaft und EigenschaftGruppe
 *
 * Revision 1.15  2009/09/14 19:14:51  jost
 * Mitglieder mit Austrittsdatum in der Zukunft werden mit ausgewertet.
 *
 * Revision 1.14  2009/07/05 10:26:26  jost
 * Bugfix Geschlechtsauswahl
 *
 * Revision 1.13  2009/06/11 21:04:24  jost
 * Vorbereitung I18N
 *
 * Revision 1.12  2009/04/10 09:43:58  jost
 * Versuch "Reports" abgebrochen
 *
 * Revision 1.11  2008/11/29 13:14:29  jost
 * Refactoring: Warnungen beseitigt.
 *
 * Revision 1.10  2008/11/16 16:58:37  jost
 * Speicherung der Einstellung von Property-Datei in die Datenbank verschoben.
 *
 * Revision 1.9  2008/11/11 20:48:35  jost
 * Selektion nach Geschlecht
 *
 * Revision 1.8  2008/09/28 12:55:30  jost
 * Bug https://developer.berlios.de/bugs/?func=detailbug&bug_id=14496&group_id=7335 gefixed
 *
 * Revision 1.7  2008/05/05 18:23:46  jost
 * Bugfix Geburtstagsliste
 *
 * Revision 1.6  2008/03/17 20:25:21  jost
 * Workaround f. Bug in Jameica
 *
 * Revision 1.5  2008/03/08 19:31:00  jost
 * Neu: Externe Mitgliedsnummer
 *
 * Revision 1.4  2008/02/02 17:50:43  jost
 * Bugfix Austrittsdatum
 *
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
import de.jost_net.JVerein.JVereinPlugin;
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

  /**
   * Wird die Abfrage für den Dialog (true) oder für die Batch-Auswertung
   * (false) instanziiert?
   */
  private boolean dialog;

  public MitgliedQuery(MitgliedControl control, boolean dialog)
  {
    this.control = control;
    this.dialog = dialog;
  }

  public ArrayList<?> get(int adresstyp) throws RemoteException
  {
    return get("*", adresstyp);
  }

  public ArrayList<?> get(String anfangsbuchstabe, int adresstyp)
      throws RemoteException
  {
    final DBService service = Einstellungen.getDBService();

    sql = "select distinct mitglied.* ";
    String sort = (String) control.getSortierung().getValue();
    if (sort.equals(JVereinPlugin.getI18n().tr("Geburtstagsliste")))
    {
      sql += ", month(geburtsdatum), day(geburtsdatum) ";
    }
    sql += "from mitglied ";
    addCondition("adresstyp = " + adresstyp);
    if (control.isMitgliedStatusAktiv())
    {
      if (control.getMitgliedStatus().getValue()
          .equals(JVereinPlugin.getI18n().tr("Angemeldet")))
      {
        addCondition("(austritt is null or austritt > current_date())");
      }
      else if (control.getMitgliedStatus().getValue()
          .equals(JVereinPlugin.getI18n().tr("Abgemeldet")))
      {
        addCondition("austritt is not null and austritt <= current_date()");
      }
    }
    if ((Boolean) control.getOhneMail().getValue())
    {
      addCondition("(email is null or length(email) = 0)");
    }
    String eigenschaften = "";
    eigenschaften = control.getEigenschaftenString();
    if (eigenschaften != null && eigenschaften.length() > 0)
    {
      StringBuilder condEigenschaft = new StringBuilder(
          "(select count(*) from eigenschaften where ");
      StringTokenizer st = new StringTokenizer(eigenschaften, ",");
      condEigenschaft.append("eigenschaften.mitglied = mitglied.id AND (");
      boolean first = true;
      while (st.hasMoreTokens())
      {
        if (!first)
        {
          condEigenschaft.append("OR ");
        }
        st.nextToken();
        first = false;
        condEigenschaft.append("eigenschaft = ? ");
      }
      condEigenschaft.append(")) > 0 ");
      addCondition(condEigenschaft.toString());
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

    if (control.getSterbedatumvon().getValue() != null)
    {
      addCondition("sterbetag >= ?");
    }
    if (control.getSterbedatumbis().getValue() != null)
    {
      addCondition("sterbetag <= ?");
    }
    if (control.getGeschlecht().getText() != null
        && !control.getGeschlecht().getText().equals("Bitte auswählen"))
    {
      addCondition("geschlecht = ?");
    }
    if (!dialog)
    {
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
        if (control.getSterbedatumvon() == null
            && control.getSterbedatumbis() == null
            && control.getAustrittvon().getValue() == null
            && control.getAustrittbis().getValue() == null)
        {
          addCondition("(austritt is null or austritt > current_date())");
        }
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
      sql += " ORDER BY name, vorname";
    }
    else if (sort.equals(JVereinPlugin.getI18n().tr("Eintrittsdatum")))
    {
      sql += " ORDER BY eintritt";
    }
    else if (sort.equals(JVereinPlugin.getI18n().tr("Geburtsdatum")))
    {
      sql += " ORDER BY geburtsdatum";
    }
    else if (sort.equals(JVereinPlugin.getI18n().tr("Geburtstagsliste")))
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
          list.add((Mitglied) service.createObject(Mitglied.class,
              rs.getString(1)));
        }
        return list;
      }
    };
    ArrayList<Object> bedingungen = new ArrayList<Object>();

    if (eigenschaften != null && eigenschaften.length() > 0)
    {
      StringTokenizer st = new StringTokenizer(eigenschaften, ",");
      int tokcount = 0;
      while (st.hasMoreTokens())
      {
        bedingungen.add(st.nextToken());
        tokcount++;
      }
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
    if (control.getSterbedatumvon().getValue() != null)
    {
      Date d = (Date) control.getSterbedatumvon().getValue();
      bedingungen.add(new java.sql.Date(d.getTime()));
    }
    if (control.getSterbedatumbis().getValue() != null)
    {
      Date d = (Date) control.getSterbedatumbis().getValue();
      bedingungen.add(new java.sql.Date(d.getTime()));
    }
    if (control.getGeschlecht().getText() != null
        && !control.getGeschlecht().getText().equals("Bitte auswählen"))
    {
      String g = (String) control.getGeschlecht().getValue();
      bedingungen.add(g);
    }
    if (!dialog)
    {
      if (control.getEintrittvon().getValue() != null)
      {
        Date d = (Date) control.getEintrittvon().getValue();
        bedingungen.add(new java.sql.Date(d.getTime()));
      }
      if (control.getEintrittbis().getValue() != null)
      {
        Date d = (Date) control.getEintrittbis().getValue();
        bedingungen.add(new java.sql.Date(d.getTime()));
      }
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
    return (ArrayList<?>) service.execute(sql, bedingungen.toArray(), rs);
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
