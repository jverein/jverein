/**********************************************************************
 * Copyright (c) by Vinzent Rudolf
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * vinzent.rudolf@web.de | www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.server.DDLTool.Updates;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import de.jost_net.JVerein.gui.dialogs.AuswahlDialog;
import de.jost_net.JVerein.server.DDLTool.AbstractDDLUpdate;
import de.jost_net.JVerein.server.DDLTool.Column;

import de.willuhn.jameica.gui.dialogs.ListDialog;
import de.willuhn.jameica.system.OperationCanceledException;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class Update0418 extends AbstractDDLUpdate {
  public Update0418(String driver, ProgressMonitor monitor, Connection conn) {
    super(driver, monitor, conn);
  }

  @Override
  public void run() throws ApplicationException {
    HashMap<String, String> auswahl_moeglichkeiten = new HashMap<String, String>();
    auswahl_moeglichkeiten.put("Buchungsnummer", "id");
    auswahl_moeglichkeiten.put("Name", "name");
    auswahl_moeglichkeiten.put("Verwendungszweck", "zweck");
    auswahl_moeglichkeiten.put("Auszugsnummer", "auszugsnummer");
    auswahl_moeglichkeiten.put("Blattnummer", "blattnummer");

    AuswahlDialog d = new AuswahlDialog(ListDialog.POSITION_CENTER,
        new ArrayList<String>(auswahl_moeglichkeiten.keySet()), "Belegnummern Import", "");
    d.setText(
        "In der neueste jVerein Version besteht die Möglichkeit manuelle Belegnummern anstatt "
            + "der Buchungsnummer aus der Datenbank zu verwenden.\n\nFalls du bisher schon Belegnummern verwendet hast, "
            + "kannst du im folgenden Dialog auswählen, aus welchem bisherigen Buchungsfeld diese importiert werden sollen. "
            + "Falls du dieses Feature nicht nutzen möchtest, wähle \"Buchungsnummer\" aus.\n\n");
    d.setSize(400, 300);

    try {
      String choice = (String) d.open();
      String feld = auswahl_moeglichkeiten.get(choice);

      // neue Felder hinzufügen und dabei VersionsNr erst einmal nicht verändern!
      execute(addColumn("einstellung",
          new Column("verwendebelegnummer", COLTYPE.BOOLEAN, 0, "FALSE", false, false)), false);
      execute(
          addColumn("einstellung",
              new Column("belegnummer_pro_konto", COLTYPE.BOOLEAN, 0, "TRUE", false, false)),
          false);
      execute(addColumn("einstellung",
          new Column("belegnummer_pro_jahr", COLTYPE.BOOLEAN, 0, "TRUE", false, false)), false);
      execute(
          addColumn("buchung", new Column("belegnummer", COLTYPE.INTEGER, 19, "-1", false, false)),
          false);

      // Belegnummern importieren
      Statement stmt =
          conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      ResultSet res = stmt.executeQuery(
          "SELECT " + (feld != "id" ? "id, " : "") + feld + ", belegnummer FROM buchung");
      while (res.next()) {
        long belegnummer = 0;
        switch (choice) {
          case "Buchungsnummer":
          case "Auszugsnummer":
          case "Blattnummer":
            belegnummer = res.getLong(feld);
            break;
          case "Name":
          case "Verwendungszweck":
            // Falls ein String als Grundlage für den Import verwendet werden soll, werden die
            // Zahlen bis zur ersten "Nichtzahl" für die Belegnummer verwendet!
            String value = res.getString(feld);
            int endIndex = 0;
            if (value != null) {
              while ((endIndex < value.length()) && Character.isDigit(value.charAt(endIndex))) {
                endIndex++;
              }
            }
            if (endIndex >= 1) {
              belegnummer = Long.parseLong(value.substring(0, endIndex));
            } else {
              belegnummer = -1;
              Logger.warn("Cannot convert \"" + value + "\" into a Belegnummer!");
            }
            break;
        }
        res.updateLong("belegnummer", belegnummer);
        res.updateRow();
      }

      // Falls alles geklappt hat, neue Versionsnummer setzen!
      setNewVersion(nr);

    } catch (OperationCanceledException oce) {
      Logger.error(oce.getMessage());
      throw new ApplicationException("user canceled import of Belegnummern");
    } catch (Exception e) {
      // Falls irgendein Fehler aufgetreten ist, die neuen Spalten wieder löschen!
      execute(dropColumn("einstellung", "verwendebelegnummer"), false);
      execute(dropColumn("einstellung", "belegnummer_pro_konto"), false);
      execute(dropColumn("einstellung", "belegnummer_pro_jahr"), false);
      execute(dropColumn("buchung", "belegnummer"), false);
      Logger.error("error while importing Belegnummer", e);
      throw new ApplicationException("error while importing Belegnummern");
    }
  }
}
