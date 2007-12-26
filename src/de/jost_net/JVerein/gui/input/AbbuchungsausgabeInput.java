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
 * Combo-Box, fuer die Auswahl des Abbuchungszieles.
 */
public class AbbuchungsausgabeInput extends SelectInput
{
  public static final int DTAUS = 1;

  public static final int HIBISCUS_EINZELBUCHUNGEN = 2;

  public static final int HIBISCUS_SAMMELBUCHUNG = 3;

  public AbbuchungsausgabeInput(int abbuchungsmodus) throws RemoteException
  {
    super(init(), new AbbuchungsausgabeObject(abbuchungsmodus));
  }

  /**
   * @return initialisiert die Liste der Optionen.
   * @throws RemoteException
   */
  private static GenericIterator init() throws RemoteException
  {
    ArrayList<AbbuchungsausgabeObject> l = new ArrayList<AbbuchungsausgabeObject>();
    l.add(new AbbuchungsausgabeObject(DTAUS));
    l.add(new AbbuchungsausgabeObject(HIBISCUS_EINZELBUCHUNGEN));
    l.add(new AbbuchungsausgabeObject(HIBISCUS_SAMMELBUCHUNG));
    return PseudoIterator.fromArray((AbbuchungsausgabeObject[]) l
        .toArray(new AbbuchungsausgabeObject[l.size()]));
  }

  /**
   * @see de.willuhn.jameica.gui.input.Input#getValue()
   */
  public Object getValue()
  {
    AbbuchungsausgabeObject o = (AbbuchungsausgabeObject) super.getValue();
    if (o == null)
    {
      return new Integer(DTAUS);
    }
    return new Integer(o.abbuchungsausgabe);
  }

  /**
   * Hilfs-Objekt zur Anzeige der Labels.
   */
  private static class AbbuchungsausgabeObject implements GenericObject
  {
    public int abbuchungsausgabe;

    private String label = null;

    private AbbuchungsausgabeObject(int abbuchungsausgabe)
    {
      this.abbuchungsausgabe = abbuchungsausgabe;

      switch (abbuchungsausgabe)
      {
        case AbbuchungsausgabeInput.DTAUS:
          this.label = "DTAUS-Datei";
          break;
        case AbbuchungsausgabeInput.HIBISCUS_EINZELBUCHUNGEN:
          this.label = "Hibiscus (Einzelbuchungen)";
          break;
        case AbbuchungsausgabeInput.HIBISCUS_SAMMELBUCHUNG:
          this.label = "Hibiscus (Sammelbuchungen)";
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
      return "" + abbuchungsausgabe;
    }

    public String getPrimaryAttribute() throws RemoteException
    {
      return "name";
    }

    public boolean equals(GenericObject arg0) throws RemoteException
    {
      if (arg0 == null || !(arg0 instanceof AbbuchungsausgabeObject))
      {
        return false;
      }
      return this.getID().equals(arg0.getID());
    }
  }
}
