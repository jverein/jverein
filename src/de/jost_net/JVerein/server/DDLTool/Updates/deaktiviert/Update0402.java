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
package de.jost_net.JVerein.server.DDLTool.Updates.deaktiviert;

import java.sql.Connection;

import de.jost_net.JVerein.server.DDLTool.AbstractDDLUpdate;
import de.jost_net.JVerein.server.DDLTool.Column;
import de.jost_net.JVerein.server.DDLTool.Index;
import de.jost_net.JVerein.server.DDLTool.Table;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class Update0402 extends AbstractDDLUpdate
{
  public Update0402(String driver, ProgressMonitor monitor, Connection conn)
  {
    super(driver, monitor, conn);
  }

  public void run() throws ApplicationException
  {
    Table table = new Table("inventarausleihe");
    Column primco = new Column("id", COLTYPE.BIGINT, 19, null, false, true);
    table.add(primco);
    Column inventar = new Column("inventar", COLTYPE.BIGINT, 1, null, true,
        false);
    table.add(inventar);
    Column mitglied = new Column("mitglied", COLTYPE.BIGINT, 1, null, true,
        false);
    table.add(mitglied);
    table.add(new Column("nichtmitglied", COLTYPE.VARCHAR, 200, null, true,
        false));
    table.add(new Column("von", COLTYPE.DATE, 1, null, true, false));
    table.add(new Column("bis", COLTYPE.BIGINT, 1, null, true, false));
    table.add(new Column("rueckgabe", COLTYPE.DATE, 1, null, true, false));
    table.setPrimaryKey(primco);

    execute(createTable(table));

    Index idx1 = new Index("ixInventarAusleihe1", false);
    idx1.add(mitglied);
    execute(idx1.getCreateIndex("inventarausleihe"));

    Index idx2 = new Index("ixInventarAusleihe2", false);
    idx2.add(inventar);
    execute(idx2.getCreateIndex("inventarausleihe"));

    execute(createForeignKey("fkInventarAusleihe1", "inventarausleihe",
        "inventar", "inventar", "id", "RESTRICT", "NO ACTION"));
    execute(createForeignKey("fkInventarAusleihe2", "inventarausleihe",
        "mitglied", "mitglied", "id", "RESTRICT", "NO ACTION"));
  }
}
