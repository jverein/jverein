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
package de.jost_net.JVereinJUnit.io;

import java.rmi.RemoteException;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.io.BeitragsUtil;
import de.jost_net.JVerein.keys.Beitragsmodel;
import de.jost_net.JVerein.keys.Zahlungsrhythmus;
import de.jost_net.JVerein.keys.Zahlungstermin;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.willuhn.util.ApplicationException;

@RunWith(JUnit4.class)
public class BeitragsUtilTest
{
  public BeitragsUtilTest() throws RemoteException
  {
    super();
  }

  @Test
  public void test01() throws ApplicationException
  {
    try
    {
      Beitragsgruppe bg = getBeitragsgruppe();
      Assert.assertEquals(10d, BeitragsUtil.getBeitrag(
          Beitragsmodel.GLEICHERTERMINFUERALLE, null, 0, bg, new Date(), null,
          null));
      Assert.assertEquals(10d, BeitragsUtil.getBeitrag(
          Beitragsmodel.MONATLICH12631, null, Zahlungsrhythmus.MONATLICH, bg,
          new Date(), null, null));
      Assert.assertEquals(30d, BeitragsUtil.getBeitrag(
          Beitragsmodel.MONATLICH12631, null,
          Zahlungsrhythmus.VIERTELJAEHRLICH, bg, new Date(), null, null));
      Assert.assertEquals(60d, BeitragsUtil.getBeitrag(
          Beitragsmodel.MONATLICH12631, null, Zahlungsrhythmus.HALBJAEHRLICH,
          bg, new Date(), null, null));
      Assert.assertEquals(120d, BeitragsUtil.getBeitrag(
          Beitragsmodel.MONATLICH12631, null, Zahlungsrhythmus.JAEHRLICH, bg,
          new Date(), null, null));
      Assert.assertEquals(20d, BeitragsUtil.getBeitrag(Beitragsmodel.FLEXIBEL,
          Zahlungstermin.MONATLICH, 0, bg, new Date(), null, null));

    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    }
  }

  private Beitragsgruppe getBeitragsgruppe() throws RemoteException
  {
    Beitragsgruppe bg = (Beitragsgruppe) Einstellungen.getDBService()
        .createObject(Beitragsgruppe.class, null);
    bg.setBezeichnung("Test");
    bg.setBetrag(10d);
    bg.setBetragMonatlich(20d);
    bg.setBetragVierteljaehrlich(60d);
    bg.setBetragHalbjaehrlich(120d);
    bg.setBetragJaehrlich(240d);
    return bg;
  }
}
