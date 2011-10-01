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

package de.jost_net.JVerein.gui.input;

import java.rmi.RemoteException;
import java.util.ArrayList;

import de.jost_net.JVerein.JVereinPlugin;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.jameica.gui.input.SelectInput;

/**
 * Combo-Box, fuer die Auswahl des Abbuchungsmodus.
 */
public class GeschlechtInput extends SelectInput
{

  public static final String MAENNLICH = "m";

  public static final String WEIBLICH = "w";

  public GeschlechtInput(String geschlecht) throws RemoteException
  {
    super(init(), new GeschlechtObject(geschlecht));
  }

  /**
   * @return initialisiert die Liste der Optionen.
   * @throws RemoteException
   */
  private static GenericIterator init() throws RemoteException
  {
    ArrayList<GeschlechtObject> l = new ArrayList<GeschlechtObject>();
    l.add(new GeschlechtObject(MAENNLICH));
    l.add(new GeschlechtObject(WEIBLICH));
    return PseudoIterator.fromArray(l.toArray(new GeschlechtObject[l.size()]));
  }

  /**
   * @see de.willuhn.jameica.gui.input.Input#getValue()
   */
  @Override
  public Object getValue()
  {
    GeschlechtObject o = (GeschlechtObject) super.getValue();
    if (o == null)
    {
      return MAENNLICH;
    }
    return o.geschlecht;
  }

  @Override
  public void setValue(Object obj)
  {
    if (obj instanceof String)
    {
      super.setValue(new GeschlechtObject((String) obj));
    }
  }

  /**
   * Hilfs-Objekt zur Anzeige der Labels.
   */
  private static class GeschlechtObject implements GenericObject
  {

    public String geschlecht;

    private String label = null;

    private GeschlechtObject(String geschlecht)
    {
      this.geschlecht = geschlecht;
      if (geschlecht == null)
      {
        label = "";
        return;
      }

      if (geschlecht.equals(MAENNLICH))
      {
        this.label = JVereinPlugin.getI18n().tr("männlich");
      }
      else if (geschlecht.equals(WEIBLICH))
      {
        this.label = JVereinPlugin.getI18n().tr("weiblich");
      }
      else
      {
        this.label = "Programmfehler";
      }
    }

    public Object getAttribute(String arg0)
    {
      if (arg0.equals("label"))
      {
        return label;
      }
      else if (arg0.equals("geschlecht"))
      {
        return geschlecht;
      }
      return null;
    }

    public String[] getAttributeNames()
    {
      return new String[] { "label", "geschlecht" };
    }

    public String getID()
    {
      return geschlecht;
    }

    public String getPrimaryAttribute()
    {
      return "label";
    }

    public boolean equals(GenericObject arg0) throws RemoteException
    {
      if (arg0 == null || !(arg0 instanceof GeschlechtObject))
      {
        return false;
      }
      return this.getID().equals(arg0.getID());
    }
  }
}
