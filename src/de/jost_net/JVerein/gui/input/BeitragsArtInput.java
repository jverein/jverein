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
 * Revision 1.1  2007/03/25 16:58:14  jost
 * Neu
 *
 *
 **********************************************************************/

package de.jost_net.JVerein.gui.input;

import java.rmi.RemoteException;
import java.util.ArrayList;

import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.jameica.gui.input.SelectInput;

/**
 * Combo-Box, fuer die Auswahl des Zahlungsweges.
 */
public class BeitragsArtInput extends SelectInput
{
  public static final int NORMAL = 0;

  public static final int FAMILIE_ZAHLER = 1;

  public static final int FAMILIE_ANGEHOERIGER = 2;

  public BeitragsArtInput() throws RemoteException
  {
    super(init(), new BeitragsArtObject(NORMAL));
  }

  public BeitragsArtInput(int beitragsart) throws RemoteException
  {
    super(init(), new BeitragsArtObject(beitragsart));
  }

  /**
   * @return initialisiert die Liste der Optionen.
   * @throws RemoteException
   */
  private static GenericIterator init() throws RemoteException
  {

    ArrayList<BeitragsArtObject> l = new ArrayList<BeitragsArtObject>();
    l.add(new BeitragsArtObject(NORMAL));
    l.add(new BeitragsArtObject(FAMILIE_ZAHLER));
    l.add(new BeitragsArtObject(FAMILIE_ANGEHOERIGER));
    return PseudoIterator.fromArray((BeitragsArtObject[]) l
        .toArray(new BeitragsArtObject[l.size()]));
  }

  /**
   * @see de.willuhn.jameica.gui.input.Input#getValue()
   */
  public Object getValue()
  {
    BeitragsArtObject o = (BeitragsArtObject) super.getValue();
    if (o == null)
    {
      return new Integer(NORMAL);
    }
    return new Integer(o.art);
  }

  /**
   * Hilfs-Objekt zur Anzeige der Labels.
   */
  private static class BeitragsArtObject implements GenericObject
  {
    public int art = BeitragsArtInput.NORMAL;

    private String label = null;

    private BeitragsArtObject(int art)
    {
      this.art = art;

      switch (art)
      {
        case BeitragsArtInput.NORMAL:
          this.label = "Normal";
          break;
        case BeitragsArtInput.FAMILIE_ZAHLER:
          this.label = "Familie: Zahler";
          break;
        case BeitragsArtInput.FAMILIE_ANGEHOERIGER:
          this.label = "Familie: Angehöriger";
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
      return "" + art;
    }

    public String getPrimaryAttribute() throws RemoteException
    {
      return "name";
    }

    public boolean equals(GenericObject arg0) throws RemoteException
    {
      if (arg0 == null || !(arg0 instanceof BeitragsArtObject))
      {
        return false;
      }
      return this.getID().equals(arg0.getID());
    }

  }
}
