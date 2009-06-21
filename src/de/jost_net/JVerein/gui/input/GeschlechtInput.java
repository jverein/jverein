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
    System.out.println("GeschlechtInput wurde initialisiert");
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
    return PseudoIterator.fromArray((GeschlechtObject[]) l
        .toArray(new GeschlechtObject[l.size()]));
  }

  /**
   * @see de.willuhn.jameica.gui.input.Input#getValue()
   */
  public Object getValue()
  {
    GeschlechtObject o = (GeschlechtObject) super.getValue();
    if (o == null)
    {
      return MAENNLICH;
    }
    return o.geschlecht;
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

    public Object getAttribute(String arg0) throws RemoteException
    {
      return label;
    }

    public String[] getAttributeNames() throws RemoteException
    {
      return new String[] { "name" };
    }

    public String getID() throws RemoteException
    {
      return geschlecht;
    }

    public String getPrimaryAttribute() throws RemoteException
    {
      return "name";
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
