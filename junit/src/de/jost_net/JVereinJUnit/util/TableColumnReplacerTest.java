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
package de.jost_net.JVereinJUnit.util;

import org.junit.Before;
import org.junit.Test;

import de.jost_net.JVerein.util.TableColumnReplacer;
import junit.framework.Assert;

public class TableColumnReplacerTest
{

  private TableColumnReplacer tcr;

  private String testColumn;

  private String testReplaceColumn;

  @Before
  public void setUp()
  {
    tcr = new TableColumnReplacer();
    testColumn = "TestColumn1";
    testReplaceColumn = "ABC";
  }

  @Test
  public void testAddColumnNecessary()
  {

    tcr.addColumn(testColumn, true);
    tcr.setColumnReplacement(testColumn, testReplaceColumn);
    Assert
        .assertEquals(true, tcr.getNecessaryColumns().containsKey(testColumn));
  }

  @Test
  public void testAddColumnOptional()
  {

    tcr.addColumn(testColumn, false);
    tcr.setColumnReplacement(testColumn, testReplaceColumn);
    Assert.assertEquals(true, tcr.getOptionalColumns().containsKey(testColumn));
  }

  @Test
  public void testRemoveColumnNecessary()
  {

    tcr.addColumn(testColumn, true);
    tcr.setColumnReplacement(testColumn, testReplaceColumn);
    Assert
        .assertEquals(true, tcr.getNecessaryColumns().containsKey(testColumn));
    tcr.removeColumn(testColumn);
    Assert.assertEquals(false, tcr.getNecessaryColumns()
        .containsKey(testColumn));
  }

  @Test
  public void testRemoveColumnOptional()
  {

    tcr.addColumn(testColumn, false);
    tcr.setColumnReplacement(testColumn, testReplaceColumn);
    Assert.assertEquals(true, tcr.getOptionalColumns().containsKey(testColumn));
    tcr.removeColumn(testColumn);
    Assert
        .assertEquals(false, tcr.getOptionalColumns().containsKey(testColumn));
  }

  @Test
  public void testAllNecessaryColumnsAvailableTrue()
  {

    tcr.addColumn(testColumn, true);
    tcr.setColumnReplacement(testColumn, testReplaceColumn);
    Assert.assertEquals(true, tcr.allNecessaryColumnsAvailable());
  }

  @Test
  public void testAllNecessaryColumnsAvailableTrueAfterRemove()
  {

    tcr.addColumn(testColumn, true);
    tcr.removeColumn(testColumn);
    Assert.assertEquals(true, tcr.allNecessaryColumnsAvailable());
  }

  @Test
  public void testAllNecessaryColumnsAvailableFalse()
  {

    tcr.addColumn(testColumn, true);
    Assert.assertEquals(false, tcr.allNecessaryColumnsAvailable());
  }

}
