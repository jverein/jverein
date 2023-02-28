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

package de.jost_net.JVerein.gui.input;

import java.rmi.RemoteException;
import java.util.ArrayList;

import de.jost_net.JVerein.keys.Abrechnungsmodi;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.jameica.gui.input.SelectInput;

/**
 * Combo-Box, fuer die Auswahl der Mail-Auswertungsmodi.
 */
public class MailAuswertungInput extends SelectInput
{

  public static final int ALLE = 1;

  public static final int OHNE = 2;

  public static final int MIT = 3;

  public MailAuswertungInput(int mail) throws RemoteException
  {
    super(init(), new MailAuswertungObject(mail));
  }

  /**
   * @return initialisiert die Liste der Optionen.
   * @throws RemoteException
   */
  private static GenericIterator<?> init() throws RemoteException
  {
    ArrayList<MailAuswertungObject> l = new ArrayList<>();
    l.add(new MailAuswertungObject(MailAuswertungInput.ALLE));
    l.add(new MailAuswertungObject(MailAuswertungInput.OHNE));
    l.add(new MailAuswertungObject(MailAuswertungInput.MIT));
    return PseudoIterator
        .fromArray(l.toArray(new MailAuswertungObject[l.size()]));
  }

  /**
   * @see de.willuhn.jameica.gui.input.Input#getValue()
   */
  @Override
  public Object getValue()
  {
    MailAuswertungObject o = (MailAuswertungObject) super.getValue();
    if (o == null)
    {
      return Integer.valueOf(Abrechnungsmodi.ALLE);
    }
    return Integer.valueOf(o.mailauswertung);
  }

  /**
   * Hilfs-Objekt zur Anzeige der Labels.
   */
  private static class MailAuswertungObject implements GenericObject
  {

    public int mailauswertung;

    private String label = null;

    private MailAuswertungObject(int mail)
    {
      this.mailauswertung = mail;
      if (mail == 0)
      {
        mail = 1;
      }

      if (mail == ALLE)
      {
        this.label = "Alle";
      }
      else if (mail == MIT)
      {
        this.label = "nur mit Mailadresse";
      }
      else if (mail == OHNE)
      {
        this.label = "nur ohne Mailadresse";
      }
      else
      {
        this.label = "Programmfehler";
      }
    }

    @Override
    public Object getAttribute(String arg0)
    {
      return label;
    }

    @Override
    public String[] getAttributeNames()
    {
      return new String[] { "name" };
    }

    @Override
    public String getID()
    {
      return "" + mailauswertung;
    }

    @Override
    public String getPrimaryAttribute()
    {
      return "name";
    }

    @Override
    public boolean equals(GenericObject arg0) throws RemoteException
    {
      if (arg0 == null || !(arg0 instanceof MailAuswertungObject))
      {
        return false;
      }
      return this.getID().equals(arg0.getID());
    }
  }
}
