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
 * Revision 1.1  2007/03/10 20:28:15  jost
 * Neu: Zahlungsweg
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
public class ZahlungswegInput extends SelectInput
{
  public static final int ABBUCHUNG = 1;

  public static final int ÜBERWEISUNG = 2;

  public static final int BARZAHLUNG = 3;

  public ZahlungswegInput() throws RemoteException
  {
    super(init(), new ZahlungswegObject(ABBUCHUNG));
  }

  public ZahlungswegInput(int zahlungsweg) throws RemoteException
  {
    super(init(), new ZahlungswegObject(zahlungsweg));
  }

  /**
   * @return initialisiert die Liste der Optionen.
   * @throws RemoteException
   */
  private static GenericIterator init() throws RemoteException
  {

    ArrayList l = new ArrayList();
    l.add(new ZahlungswegObject(ABBUCHUNG));
    l.add(new ZahlungswegObject(ÜBERWEISUNG));
    l.add(new ZahlungswegObject(BARZAHLUNG));
    return PseudoIterator.fromArray((ZahlungswegObject[]) l
        .toArray(new ZahlungswegObject[l.size()]));
  }

  /**
   * @see de.willuhn.jameica.gui.input.Input#getValue()
   */
  public Object getValue()
  {
    ZahlungswegObject o = (ZahlungswegObject) super.getValue();
    if (o == null)
    {
      return new Integer(ABBUCHUNG);
    }
    return new Integer(o.weg);
  }

  /**
   * Hilfs-Objekt zur Anzeige der Labels.
   */
  private static class ZahlungswegObject implements GenericObject
  {
    public int weg = ZahlungswegInput.ABBUCHUNG;

    private String label = null;

    private ZahlungswegObject(int weg)
    {
      this.weg = weg;

      switch (weg)
      {
        case ZahlungswegInput.ABBUCHUNG:
          this.label = "Abbuchung";
          break;
        case 2:
          this.label = "Überweisung";
          break;
        case 3:
          this.label = "Barzahlung";
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
      return "" + weg;
    }

    public String getPrimaryAttribute() throws RemoteException
    {
      return "name";
    }

    public boolean equals(GenericObject arg0) throws RemoteException
    {
      if (arg0 == null || !(arg0 instanceof ZahlungswegObject))
      {
        return false;
      }
      return this.getID().equals(arg0.getID());
    }

  }
}
