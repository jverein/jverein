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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bsh.EvalError;
import bsh.Interpreter;
import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Lesefeld;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.logging.Logger;

/**
 * Bietet Funktionalität rund um Lesefelder.
 * 
 * Lesefelder werden durch Skripte konfiguriert. Jedes Skript kann für jedes
 * Mitglied aufgerufen werden und erstellt so für jedes Mitglied ein Feld, das
 * nur gelesen werden kann. Skripte können auf alle Daten des jeweiligen
 * Mitgliedes zugreifen und diese weiterverarbeiten.
 * 
 * Beispiel: Skript: String mein_name = mitglied_vorname;
 * return(mein_name.substring(0, 3)); Ausgabe für Mitlied... Beate --> Bea
 * Christian --> Chr
 * 
 * Die Daten des jeweiligen Mitgliedes müssen gesetzt werden mit: setMap()
 * Außerdem müssen die aktuellen Lesefelder-Skripte geladen werden. Dies
 * geschieht mit: setLesefelderDefinitions() oder
 * setLesefelderDefinitionsFromDatabase()
 * 
 * LesefeldAuswerter l = new LesefeldAuswerter();
 * l.setLesefelderDefinitionsFromDatabase(); l.setMap(map); lesefelderMap =
 * l.getLesefelderMap();
 * 
 * @author Julian
 * 
 */
public class LesefeldAuswerter
{

  // BeanShell-Interpreter.
  Interpreter bsh;

  List<Lesefeld> lesefelder;

  // Name des aktuellen Mitgliedes für Debug-Zwecke:
  String vornamename = "";

  /**
   * Legt eine Instanz vom Bean-Shell-Interpreter an.
   */
  public LesefeldAuswerter()
  {
    bsh = new Interpreter();
    lesefelder = new ArrayList<>();
  }

  public Map<String, Object> getMap() throws EvalError
  {
    Map<String, Object> map = new HashMap<>();
    String[] vars = bsh.getNameSpace().getVariableNames();
    for (int i = 0; i < vars.length; i++)
    {
      if (vars[i].compareTo("bsh") == 0)
        continue;
      String s2 = "return " + vars[i] + ";";
      Object o = bsh.eval(s2);
      map.put(vars[i], o);
    }
    return map;
  }

  /**
   * Nimmt eine map mit Mitgliedsdaten entgegen und macht es dem Interpreter
   * verfügbar. map enthält Zuordnung von Variablen-Name zu Variablen-Inhalt.
   * map kann direkt von einem Mitglied-Objekt über die Funktion getMap(null,
   * true) erhalten werden.
   * 
   * @param map
   *          map mit Mitgliedsdaten
   */
  public void setMap(Map<String, Object> map)
  {
    vornamename = (String) map.get("mitglied_vornamename");

    // Mache alle Variablen aus map in BeanScript verfügbar.
    // '.', '-' und ' ' werden ersetzt durch '_'.
    for (String key : map.keySet())
    {

      // TODO: gibt es noch mehr Zeichen, die ersetzt werden müssen?
      String keyNormalized = key.replace("-", "_").replace(".", "_")
          .replace(" ", "_");

      try
      {
        bsh.set(keyNormalized, map.get(key));
      }
      catch (EvalError e)
      {
        Logger.error("Interner Fehler beim Auswerten eines Skriptes: \""
            + e.getMessage() + "\".", e);
      }
    }

    // DEBUG: Zeige alle gesetzten Variablen.
    /*
     * String[] vars = bsh.getNameSpace().getVariableNames(); try { for (int i =
     * 0; i < vars.length; i++) { if (vars[i].compareTo("bsh") == 0) continue;
     * String s2 = "\"" + vars[i] + ":\" + " + vars[i] + ";"; Object o =
     * bsh.eval(s2); Logger.debug("Skript-Variable: " + o); } } catch (EvalError
     * e) { e.printStackTrace(); }
     */
    // END DEBUG.
  }

  /**
   * Liest alle Lesefelder aus Datenbank um sie später wiederzuverwenden (z.B.
   * von getLesefelderMap()).
   * 
   * @throws RemoteException
   */
  public void setLesefelderDefinitionsFromDatabase() throws RemoteException
  {
    DBIterator<Lesefeld> itlesefelder = Einstellungen.getDBService()
        .createList(Lesefeld.class);
    while (itlesefelder.hasNext())
    {
      Lesefeld lesefeld = itlesefelder.next();
      lesefelder.add(lesefeld);
    }
  }

  /**
   * Liefert Anzahl der definierten Lesefelder.
   * 
   * @return Anzahl der definierten Lesefelder.
   */
  public int countLesefelder()
  {
    if (lesefelder != null)
      return lesefelder.size();
    return 0;
  }

  public void setLesefelderDefinitions(List<Lesefeld> list)
  {
    lesefelder = list;
  }

  /**
   * Evaluiert alle Lesefelder-Definitionen, die mit
   * readLesefelderDefinitionsFromDatabase() oder setLesefelderDefinitions()
   * gesetzt wurden. Dabei werden alle Mitglieder-Variablen berücksichtig, die
   * vorher mit setMap() gesetzt wurden. Evaluation-Ausnahmen (EvalError) werden
   * abgefangen und ignoriert (kein Eintrag in Rückgabe map)
   * 
   * @return Liste von Mitglieder-Lesefelder-Variablen
   * @throws RemoteException
   */
  public Map<String, Object> getLesefelderMap() throws RemoteException
  {
    Map<String, Object> map = new HashMap<>();
    for (Lesefeld lesefeld : lesefelder)
    {

      lesefeld = evalLesefeld(lesefeld);
      if (lesefeld == null)
      {
        continue;
      }
      map.put(Einstellungen.LESEFELD_PRE + lesefeld.getBezeichnung(),
          lesefeld.getEvaluatedContent());
    }
    Logger.debug(
        String.format("Lesefeld-Variablen für Mitglied %s:", vornamename));
    for (String key : map.keySet())
    {
      Logger.debug(key + "=" + map.get(key));
    }
    return map;
  }

  /**
   * Liefert geladene Lesefelder. Wurde keine geladen (mit
   * setLesefelderDefinitions*()-Funktionen, liefert getLesefelder() null
   * zurück.
   * 
   * @return geladene Lesefelder.
   */
  public List<Lesefeld> getLesefelder()
  {
    return lesefelder;
  }

  /**
   * Evaluiert Skript script. Dabei werden alle Mitglieder-Variablen
   * berücksichtig, die vorher mit setMap() gesetzt wurden.
   * 
   * @param script
   *          Auszuwertendes Skript.
   * @return Ergebnis der Skript-Ausführung
   * @throws EvalError
   */
  public Object eval(String script) throws EvalError
  {
    return bsh.eval(script);
  }

  /**
   * Wertet das Skript des Lesefeldes lesefeld aus und speichert den Inhalt in
   * lesefeld.evaluatedContent. Dabei werden alle Mitglieder-Variablen
   * berücksichtig, die vorher mit setMap() gesetzt wurden.
   * 
   * @param lesefeld
   *          Auszuwertendes Lesefeld
   * @return Lesefeld in das der ausgewertete Inhalt des Skriptes geschrieben
   *         wurde.
   * @throws RemoteException
   */
  private Lesefeld evalLesefeld(Lesefeld lesefeld) throws RemoteException
  {
    String script = lesefeld.getScript();
    Object scriptResult = null;
    try
    {
      scriptResult = eval(script);
      String val = scriptResult == null ? "" : scriptResult.toString();
      lesefeld.setEvaluatedContent(val);
    }
    catch (EvalError e)
    {
      Logger.error(
          "Fehler beim Auswerten des Skriptes: \"" + e.getMessage() + "\".", e);
      return null;
    }
    return lesefeld;
  }

  /**
   * Wertet Skripte aller geladenen Lesefelder aus und speichert den Inhalt
   * jeweils in lesefeld.evaluatedContent. Dabei werden alle
   * Mitglieder-Variablen berücksichtig, die vorher mit setMap() gesetzt wurden.
   * 
   * @throws RemoteException
   */
  public void evalAlleLesefelder() throws RemoteException
  {
    for (Lesefeld lesefeld : lesefelder)
    {
      lesefeld = evalLesefeld(lesefeld);
    }
  }

  public void addLesefelderDefinition(Lesefeld lf)
  {
    lesefelder.add(lf);
  }

  public void updateLesefelderDefinition(Lesefeld lf) throws RemoteException
  {

    for (Lesefeld lesefeld : lesefelder)
    {
      if (lesefeld.getID() != null && lesefeld.getID().endsWith(lf.getID()))
        lesefeld = lf;
    }
  }

  public void deleteLesefelderDefinition(Lesefeld lf)
  {
    lesefelder.remove(lf);
  }
}
