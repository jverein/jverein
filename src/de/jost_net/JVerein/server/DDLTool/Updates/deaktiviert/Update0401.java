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

public class Update0401 extends AbstractDDLUpdate
{
  public Update0401(String driver, ProgressMonitor monitor, Connection conn)
  {
    super(driver, monitor, conn);
  }

  @Override
  public void run() throws ApplicationException
  {
    Table table = new Table("inventar");
    Column primco = new Column("id", COLTYPE.BIGINT, 19, null, false, true);
    table.add(primco);
    Column bezeichnung = new Column("bezeichnung", COLTYPE.VARCHAR, 200, null,
        false, false);
    table.add(bezeichnung);
    Column lagerort = new Column("lagerort", COLTYPE.BIGINT, 1, null, true,
        false);
    table.add(lagerort);
    table.add(
        new Column("anschaffungsdatum", COLTYPE.DATE, 1, null, true, false));
    table.add(
        new Column("anschaffungswert", COLTYPE.DOUBLE, 1, null, true, false));
    table.setPrimaryKey(primco);

    execute(createTable(table));

    Index idx1 = new Index("ixInventar1", false);
    idx1.add(lagerort);
    execute(idx1.getCreateIndex("inventar"));
    Index idx2 = new Index("ixInventar2", true);
    idx2.add(bezeichnung);
    execute(idx2.getCreateIndex("inventar"));

    execute(createForeignKey("fkInventar1", "inventar", "lagerort",
        "inventarlagerort", "id", "RESTRICT", "NO ACTION"));
  }
}
