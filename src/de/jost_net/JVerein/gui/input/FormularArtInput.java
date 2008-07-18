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
 * Combo-Box, fuer die Auswahl der Formularart.
 */
public class FormularArtInput extends SelectInput
{
  public static final int SPENDENBESCHEINIGUNG = 1;

  public static final int RECHNUNG = 2;

  public FormularArtInput(int formularart) throws RemoteException
  {
    super(init(), new FormularArtObject(formularart));
  }

  /**
   * @return initialisiert die Liste der Optionen.
   * @throws RemoteException
   */
  private static GenericIterator init() throws RemoteException
  {
    ArrayList<FormularArtObject> l = new ArrayList<FormularArtObject>();
    l.add(new FormularArtObject(SPENDENBESCHEINIGUNG));
    // l.add(new FormularArtObject(RECHNUNG));
    return PseudoIterator.fromArray((FormularArtObject[]) l
        .toArray(new FormularArtObject[l.size()]));
  }

  /**
   * @see de.willuhn.jameica.gui.input.Input#getValue()
   */
  public Object getValue()
  {
    FormularArtObject o = (FormularArtObject) super.getValue();
    if (o == null)
    {
      return new Integer(SPENDENBESCHEINIGUNG);
    }
    return new Integer(o.formularart);
  }

  /**
   * Hilfs-Objekt zur Anzeige der Labels.
   */
  private static class FormularArtObject implements GenericObject
  {
    public int formularart;

    private String label = null;

    private FormularArtObject(int formularart)
    {
      this.formularart = formularart;

      switch (formularart)
      {
        case FormularArtInput.SPENDENBESCHEINIGUNG:
          this.label = "Spendenbescheinigung";
          break;
        case FormularArtInput.RECHNUNG:
          this.label = "Rechnung (in Vorbereitung - kann noch nicht genutzt werden)";
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
      return "" + formularart;
    }

    public String getPrimaryAttribute() throws RemoteException
    {
      return "name";
    }

    public boolean equals(GenericObject arg0) throws RemoteException
    {
      if (arg0 == null || !(arg0 instanceof FormularArtObject))
      {
        return false;
      }
      return this.getID().equals(arg0.getID());
    }
  }
}
