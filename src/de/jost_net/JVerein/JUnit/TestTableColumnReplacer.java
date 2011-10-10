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
 **********************************************************************/
package de.jost_net.JVerein.JUnit;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import de.jost_net.JVerein.util.TableColumnReplacer;

public class TestTableColumnReplacer
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
