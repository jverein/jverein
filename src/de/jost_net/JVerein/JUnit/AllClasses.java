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
package de.jost_net.JVerein.JUnit;

import junit.framework.Test;
import junit.framework.TestSuite;
import de.jost_net.JVerein.io.XLastschriftenTest;

public class AllClasses
{
  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(XLastschriftenTest.class);
    return suite;

  }
}
