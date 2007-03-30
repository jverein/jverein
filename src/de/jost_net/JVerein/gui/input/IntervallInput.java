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
 * Combo-Box, fuer die Auswahl des Intervalls von Zusatzabbuchungen.
 */
public class IntervallInput extends SelectInput
{
  public static final int KEIN = 0;

  public static final int MONATLICH = 1;

  public static final int ZWEIMONATLICH = 2;

  public static final int VIERTELJAEHRLICH = 3;

  public static final int HALBJAEHRLICH = 6;

  public static final int JAEHRLICH = 12;

  protected static final int[] schluessel = { KEIN, MONATLICH, ZWEIMONATLICH,
      VIERTELJAEHRLICH, HALBJAEHRLICH, JAEHRLICH };

  protected static final String[] klartext = { "kein", "monatlich",
      "zweimonatlich", "vierteljährlich", "halbjährlich", "jährlich" };

  public IntervallInput() throws RemoteException
  {
    super(init(), new IntervallObject(KEIN));
  }

  public IntervallInput(int intervall) throws RemoteException
  {
    super(init(), new IntervallObject(intervall));
  }

  /**
   * @return initialisiert die Liste der Optionen.
   * @throws RemoteException
   */
  private static GenericIterator init() throws RemoteException
  {

    ArrayList<IntervallObject> l = new ArrayList<IntervallObject>();
    for (int i = 0; i < schluessel.length; i++)
    {
      l.add(new IntervallObject(schluessel[i]));
    }
    return PseudoIterator.fromArray((IntervallObject[]) l
        .toArray(new IntervallObject[l.size()]));
  }

  /**
   * @see de.willuhn.jameica.gui.input.Input#getValue()
   */
  public Object getValue()
  {
    IntervallObject o = (IntervallObject) super.getValue();
    if (o == null)
    {
      return new Integer(KEIN);
    }
    return new Integer(o.schl);
  }

  public static String getText(int schl)
  {
    for (int i = 0; i < schluessel.length; i++)
    {
      if (schluessel[i] == schl)
      {
        return klartext[i];
      }
    }
    return "";

  }

  /**
   * Hilfs-Objekt zur Anzeige der Labels.
   */
  private static class IntervallObject implements GenericObject
  {
    public int schl = IntervallInput.KEIN;

    private String text = null;

    private IntervallObject(int schl)
    {
      this.schl = schl;
      this.text = getText(schl);
    }

    public Object getAttribute(String arg0) throws RemoteException
    {
      return text;
    }

    public String[] getAttributeNames() throws RemoteException
    {
      return new String[] { "name" };
    }

    public String getID() throws RemoteException
    {
      return "" + schl;
    }

    public String getPrimaryAttribute() throws RemoteException
    {
      return "name";
    }

    public boolean equals(GenericObject arg0) throws RemoteException
    {
      if (arg0 == null || !(arg0 instanceof IntervallObject))
      {
        return false;
      }
      return this.getID().equals(arg0.getID());
    }

  }
}
