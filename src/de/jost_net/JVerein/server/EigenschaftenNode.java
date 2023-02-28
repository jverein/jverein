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
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Eigenschaft;
import de.jost_net.JVerein.rmi.EigenschaftGruppe;
import de.jost_net.JVerein.rmi.Eigenschaften;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.GenericObjectNode;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.datasource.rmi.DBIterator;

public class EigenschaftenNode implements GenericObjectNode
{

  private EigenschaftenNode parent = null;

  private Mitglied mitglied = null;

  private EigenschaftGruppe eigenschaftgruppe = null;

  private Eigenschaft eigenschaft = null;

  private Eigenschaften eigenschaften = null;

  private ArrayList<GenericObjectNode> childrens;

  private boolean preset;

  public static final int NONE = 0;

  public static final int ROOT = 1;

  public static final int EIGENSCHAFTGRUPPE = 2;

  public static final int EIGENSCHAFTEN = 3;

  private int nodetype = NONE;

  private ArrayList<String> eigenschaftids = new ArrayList<>();

  public EigenschaftenNode(Mitglied mitglied) throws RemoteException
  {
    this(mitglied, "", false);
  }

  public EigenschaftenNode(String vorbelegung, boolean ohnePflicht)
      throws RemoteException
  {
    this(null, vorbelegung, ohnePflicht);
  }

  private EigenschaftenNode(Mitglied mitglied, String vorbelegung,
      boolean ohnePflicht) throws RemoteException
  {
    this.mitglied = mitglied;
    StringTokenizer stt = new StringTokenizer(vorbelegung, ",");
    while (stt.hasMoreElements())
    {
      eigenschaftids.add(stt.nextToken());
    }
    childrens = new ArrayList<>();
    nodetype = ROOT;
    DBIterator<EigenschaftGruppe> it = Einstellungen.getDBService()
        .createList(EigenschaftGruppe.class);
    if (ohnePflicht)
    {
      it.addFilter(
          "(PFLICHT <> true OR PFLICHT IS NULL) AND (MAX1 <> true OR MAX1 IS NULL)");
    }
    it.setOrder("order by bezeichnung");
    while (it.hasNext())
    {
      EigenschaftGruppe eg =  it.next();
      childrens.add(new EigenschaftenNode(this, mitglied, eg, eigenschaftids));
    }
  }

  private EigenschaftenNode(EigenschaftenNode parent, Mitglied mitglied,
      EigenschaftGruppe eg, ArrayList<String> eigenschaftsids)
      throws RemoteException
  {
    this.parent = parent;
    this.mitglied = mitglied;
    childrens = new ArrayList<>();
    this.eigenschaftgruppe = eg;
    nodetype = EIGENSCHAFTGRUPPE;
    DBIterator<Eigenschaft> it = Einstellungen.getDBService()
        .createList(Eigenschaft.class);
    it.addFilter("eigenschaftgruppe = ?",
        new Object[] { eigenschaftgruppe.getID() });
    it.setOrder("order by bezeichnung");
    while (it.hasNext())
    {
      Eigenschaft eigenschaft =  it.next();
      Eigenschaften eigenschaften = null;
      if (mitglied != null)
      {
        DBIterator<Eigenschaften> it2 = Einstellungen.getDBService()
            .createList(Eigenschaften.class);
        it2.addFilter("mitglied = ? AND eigenschaft = ?",
            new Object[] { mitglied.getID(), eigenschaft.getID() });
        if (it2.hasNext())
        {
          eigenschaften = it2.next();
        }
      }
      childrens.add(new EigenschaftenNode(this, mitglied, eigenschaft,
          eigenschaften, eigenschaftsids));
    }
  }

  private EigenschaftenNode(EigenschaftenNode parent, Mitglied mitglied,
      Eigenschaft eigenschaft, Eigenschaften eigenschaften,
      ArrayList<String> eigenschaftids) throws RemoteException
  {
    this.parent = parent;
    nodetype = EIGENSCHAFTEN;
    this.mitglied = mitglied;
    this.eigenschaft = eigenschaft;
    this.eigenschaften = eigenschaften;
    if (eigenschaftids.contains(this.eigenschaft.getID()))
    {
      preset = true;
    }
  }

  @Override
  public GenericIterator<?> getChildren() throws RemoteException
  {
    if (childrens == null)
    {
      return null;
    }
    return PseudoIterator
        .fromArray(childrens.toArray(new GenericObject[childrens.size()]));
  }

  public boolean removeChild(GenericObjectNode child)
  {
    return childrens.remove(child);
  }

  @Override
  public EigenschaftenNode getParent()
  {
    return parent;
  }

  @Override
  public GenericIterator<?> getPath()
  {
    return null;
  }

  @Override
  public GenericIterator<?> getPossibleParents()
  {
    return null;
  }

  @Override
  public boolean hasChild(GenericObjectNode object)
  {
    return childrens.size() > 0;
  }

  @Override
  public boolean equals(GenericObject other)
  {
    return false;
  }

  @Override
  public Object getAttribute(String name) throws RemoteException
  {
    switch (nodetype)
    {
      case ROOT:
      {
        return "Eigenschaften";
      }
      case EIGENSCHAFTGRUPPE:
      {
        return eigenschaftgruppe.getBezeichnung();
      }
      case EIGENSCHAFTEN:
      {
        return eigenschaft.getBezeichnung();
      }
    }
    return null;
  }

  @Override
  public String[] getAttributeNames()
  {
    return null;
  }

  @Override
  public String getID()
  {
    return null;
  }

  @Override
  public String getPrimaryAttribute()
  {
    return null;
  }

  public Object getObject()
  {
    switch (nodetype)
    {
      case ROOT:
      {
        return mitglied;
      }
      case EIGENSCHAFTGRUPPE:
      {
        return eigenschaftgruppe;
      }
      case EIGENSCHAFTEN:
      {
        return eigenschaft;
      }
    }
    return null;
  }

  public int getNodeType()
  {
    return nodetype;
  }

  public Mitglied getMitglied()
  {
    return this.mitglied;
  }

  public Eigenschaft getEigenschaft()
  {
    return this.eigenschaft;
  }

  public Eigenschaften getEigenschaften()
  {
    return this.eigenschaften;
  }

  public boolean isPreset()
  {
    return preset;
  }
}
