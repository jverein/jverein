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

import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.jameica.gui.input.SelectInput;

/**
 * Combo-Box, fuer die Auswahl des Beitragsmodel.
 */
public class BeitragsmodelInput extends SelectInput
{
  public static final int JAEHRLICH = 1;

  public static final int HALBJAEHRLICH = 2;

  public static final int VIERTELJAEHRLICH = 3;

  public static final int MONATLICH = 4;

  public static final int MONATLICH12631 = 5;

  public BeitragsmodelInput() throws RemoteException
  {
    super(init(), new BeitragsmodelObject(JAEHRLICH));
  }

  public BeitragsmodelInput(int abbuchungsmodus) throws RemoteException
  {
    super(init(), new BeitragsmodelObject(abbuchungsmodus));
  }

  /**
   * @return initialisiert die Liste der Optionen.
   * @throws RemoteException
   */
  private static GenericIterator init() throws RemoteException
  {
    ArrayList<BeitragsmodelObject> l = new ArrayList<BeitragsmodelObject>();
    l.add(new BeitragsmodelObject(JAEHRLICH));
    l.add(new BeitragsmodelObject(HALBJAEHRLICH));
    l.add(new BeitragsmodelObject(VIERTELJAEHRLICH));
    l.add(new BeitragsmodelObject(MONATLICH));
    l.add(new BeitragsmodelObject(MONATLICH12631));
    return PseudoIterator.fromArray((BeitragsmodelObject[]) l
        .toArray(new BeitragsmodelObject[l.size()]));
  }

  /**
   * @see de.willuhn.jameica.gui.input.Input#getValue()
   */
  public Object getValue()
  {
    BeitragsmodelObject o = (BeitragsmodelObject) super.getValue();
    if (o == null)
    {
      return new Integer(JAEHRLICH);
    }
    return new Integer(o.beitragsmodel);
  }

  /**
   * Hilfs-Objekt zur Anzeige der Labels.
   */
  private static class BeitragsmodelObject implements GenericObject
  {
    public int beitragsmodel = BeitragsmodelInput.JAEHRLICH;

    private String label = null;

    private BeitragsmodelObject(int beitragsmodel)
    {
      this.beitragsmodel = beitragsmodel;

      switch (beitragsmodel)
      {
        case BeitragsmodelInput.JAEHRLICH:
          this.label = "jährlich";
          break;
        case BeitragsmodelInput.HALBJAEHRLICH:
          this.label = "halbjährlich";
          break;
        case BeitragsmodelInput.VIERTELJAEHRLICH:
          this.label = "vierteljährlich";
          break;
        case BeitragsmodelInput.MONATLICH:
          this.label = "monatlich";
          break;
        case BeitragsmodelInput.MONATLICH12631:
          this.label = "monatlich mit monatl., viertel-, halb- oder jährlicher Zahlungsweise";
          break;
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
      return "" + beitragsmodel;
    }

    public String getPrimaryAttribute() throws RemoteException
    {
      return "name";
    }

    public boolean equals(GenericObject arg0) throws RemoteException
    {
      if (arg0 == null || !(arg0 instanceof BeitragsmodelObject))
      {
        return false;
      }
      return this.getID().equals(arg0.getID());
    }
  }
}
