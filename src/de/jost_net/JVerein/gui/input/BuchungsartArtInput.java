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
 * Combo-Box, fuer die Auswahl des Zahlungsweges.
 */
public class BuchungsartArtInput extends SelectInput
{
  public static final int EINNAHME = 0;

  public static final int AUSGABE = 1;

  public static final int UMBUCHUNG = 2;

  public BuchungsartArtInput() throws RemoteException
  {
    super(init(), new BeitragsArtObject(EINNAHME));
  }

  public BuchungsartArtInput(int art) throws RemoteException
  {
    super(init(), new BeitragsArtObject(art));
  }

  /**
   * @return initialisiert die Liste der Optionen.
   * @throws RemoteException
   */
  private static GenericIterator init() throws RemoteException
  {

    ArrayList<BeitragsArtObject> l = new ArrayList<BeitragsArtObject>();
    l.add(new BeitragsArtObject(EINNAHME));
    l.add(new BeitragsArtObject(AUSGABE));
    l.add(new BeitragsArtObject(UMBUCHUNG));
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
      return new Integer(EINNAHME);
    }
    return new Integer(o.art);
  }

  /**
   * Hilfs-Objekt zur Anzeige der Labels.
   */
  private static class BeitragsArtObject implements GenericObject
  {
    public int art = BuchungsartArtInput.EINNAHME;

    private String label = null;

    private BeitragsArtObject(int art)
    {
      this.art = art;

      switch (art)
      {
        case BuchungsartArtInput.EINNAHME:
          this.label = "Einnahme";
          break;
        case BuchungsartArtInput.AUSGABE:
          this.label = "Ausgabe";
          break;
        case BuchungsartArtInput.UMBUCHUNG:
          this.label = "Umbuchung";
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
