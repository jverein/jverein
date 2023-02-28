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
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;
import java.util.ArrayList;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.io.Adressbuch.Adressaufbereitung;
import de.jost_net.JVerein.keys.ArtBeitragsart;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.GenericObjectNode;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.logging.Logger;

public class FamilienbeitragNode implements GenericObjectNode
{
  public static final int ROOT = 0;

  public static final int ZAHLER = 1;

  public static final int ANGEHOERIGER = 2;

  private int type = ROOT;

  private String id;

  private Mitglied mitglied;

  private FamilienbeitragNode parent = null;

  private ArrayList<FamilienbeitragNode> children;

  public FamilienbeitragNode() throws RemoteException
  {
    this.parent = null;
    this.type = ROOT;
    this.children = new ArrayList<>();
    DBIterator<Beitragsgruppe> it = Einstellungen.getDBService()
        .createList(Beitragsgruppe.class);
    it.addFilter("beitragsart = ?",
        new Object[] { ArtBeitragsart.FAMILIE_ZAHLER.getKey() });
    while (it.hasNext())
    {
      Beitragsgruppe bg = it.next();
      DBIterator<Mitglied> it2 = Einstellungen.getDBService()
          .createList(Mitglied.class);
      it2.addFilter("beitragsgruppe = ?", new Object[] { bg.getID() });
      it2.addFilter("austritt is null");
      it2.setOrder("ORDER BY name, vorname");
      while (it2.hasNext())
      {
        Mitglied m = it2.next();
        FamilienbeitragNode fbn = new FamilienbeitragNode(this, m);
        children.add(fbn);
      }
    }
  }

  public FamilienbeitragNode(FamilienbeitragNode parent, Mitglied m)
      throws RemoteException
  {
    this.parent = parent;
    this.mitglied = m;
    this.id = mitglied.getID();
    this.type = ZAHLER;
    this.children = new ArrayList<>();
    DBIterator<Mitglied> it = Einstellungen.getDBService()
        .createList(Mitglied.class);
    it.addFilter("zahlerid = ?", new Object[] { m.getID() });
    it.addFilter("austritt is null");
    while (it.hasNext())
    {
      FamilienbeitragNode fbn = new FamilienbeitragNode(this, it.next(), 1);
      children.add(fbn);
    }
  }

  public FamilienbeitragNode(FamilienbeitragNode parent, Mitglied m, int dummy)
  {
    this.parent = parent;
    this.type = ANGEHOERIGER;
    this.mitglied = m;
    try
    {
      this.id = m.getID();
    }
    catch (RemoteException e)
    {
      Logger.error("Fehler", e);
    }
    this.children = new ArrayList<>();
  }

  public int getType()
  {
    return type;
  }

  public Mitglied getMitglied()
  {
    return mitglied;
  }

  @Override
  public String getPrimaryAttribute()
  {
    return null;
  }

  @Override
  public String getID()
  {
    return id;
  }

  @Override
  public String[] getAttributeNames()
  {
    return new String[] { "name", "vorname", "blz", "konto" };
  }

  @Override
  public Object getAttribute(String name)
  {
    try
    {
      if (mitglied == null)
      {
        return "Familienbeiträge";
      }
      JVDateFormatTTMMJJJJ jvttmmjjjj = new JVDateFormatTTMMJJJJ();
      return Adressaufbereitung.getNameVorname(mitglied)
          + (mitglied.getGeburtsdatum() != null
              ? ", " + jvttmmjjjj.format(mitglied.getGeburtsdatum())
              : "")
          + (mitglied.getIban().length() > 0
              ? ", " + mitglied.getBic() + ", " + mitglied.getIban()
              : "");
    }
    catch (RemoteException e)
    {
      Logger.error("Fehler", e);
    }
    return null;
  }

  @Override
  public boolean equals(GenericObject other)
  {
    return false;
  }

  @Override
  public boolean hasChild(GenericObjectNode object)
  {
    return children.size() > 0;
  }

  @Override
  public GenericIterator<?> getPossibleParents()
  {
    return null;
  }

  @Override
  public GenericIterator<?> getPath()
  {
    return null;
  }

  @Override
  public GenericObjectNode getParent()
  {
    return parent;
  }

  @Override
  public GenericIterator<?> getChildren() throws RemoteException
  {
    if (children != null)
    {
      return PseudoIterator
          .fromArray(children.toArray(new GenericObject[children.size()]));
    }
    return null;
  }

  public void remove()
  {
    if (parent != null)
    {
      parent.children.remove(this);
    }
  }

}
