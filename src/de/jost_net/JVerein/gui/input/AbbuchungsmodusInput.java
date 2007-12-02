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

import de.jost_net.JVerein.Einstellungen;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.jameica.gui.input.SelectInput;

/**
 * Combo-Box, fuer die Auswahl des Abbuchungsmodus.
 */
public class AbbuchungsmodusInput extends SelectInput
{
  public static final int KEINBEITRAG = 0;

  public static final int JAEHRLICH = 1;

  public static final int HALBJAEHRLICH = 2;

  public static final int VIERTELJAEHRLICH = 3;

  public static final int MONATLICH = 4;

  public static final int JAHAVIMO = 5;

  public static final int HAVIMO = 6;

  public static final int VIMO = 7;

  public static final int MO = 8;

  public static final int EINGETRETENEMITGLIEDER = 99;

  public AbbuchungsmodusInput(int abbuchungsmodus) throws RemoteException
  {
    super(init(), new AbbuchungsmodusObject(abbuchungsmodus));
  }

  /**
   * @return initialisiert die Liste der Optionen.
   * @throws RemoteException
   */
  private static GenericIterator init() throws RemoteException
  {
    ArrayList<AbbuchungsmodusObject> l = new ArrayList<AbbuchungsmodusObject>();
    l.add(new AbbuchungsmodusObject(KEINBEITRAG));
    if (Einstellungen.getBeitragsmodel() == BeitragsmodelInput.JAEHRLICH)
    {
      l.add(new AbbuchungsmodusObject(JAEHRLICH));
      l.add(new AbbuchungsmodusObject(EINGETRETENEMITGLIEDER));
    }
    if (Einstellungen.getBeitragsmodel() == BeitragsmodelInput.HALBJAEHRLICH)
    {
      l.add(new AbbuchungsmodusObject(HALBJAEHRLICH));
      l.add(new AbbuchungsmodusObject(EINGETRETENEMITGLIEDER));
    }
    if (Einstellungen.getBeitragsmodel() == BeitragsmodelInput.VIERTELJAEHRLICH)
    {
      l.add(new AbbuchungsmodusObject(VIERTELJAEHRLICH));
      l.add(new AbbuchungsmodusObject(EINGETRETENEMITGLIEDER));
    }
    if (Einstellungen.getBeitragsmodel() == BeitragsmodelInput.MONATLICH)
    {
      l.add(new AbbuchungsmodusObject(MONATLICH));
      l.add(new AbbuchungsmodusObject(EINGETRETENEMITGLIEDER));
    }
    if (Einstellungen.getBeitragsmodel() == BeitragsmodelInput.MONATLICH12631)
    {
      l.add(new AbbuchungsmodusObject(JAHAVIMO));
      l.add(new AbbuchungsmodusObject(HAVIMO));
      l.add(new AbbuchungsmodusObject(VIMO));
      l.add(new AbbuchungsmodusObject(MO));
    }
    return PseudoIterator.fromArray((AbbuchungsmodusObject[]) l
        .toArray(new AbbuchungsmodusObject[l.size()]));
  }

  /**
   * @see de.willuhn.jameica.gui.input.Input#getValue()
   */
  public Object getValue()
  {
    AbbuchungsmodusObject o = (AbbuchungsmodusObject) super.getValue();
    if (o == null)
    {
      return new Integer(JAEHRLICH);
    }
    return new Integer(o.abbuchungsmodus);
  }

  /**
   * Hilfs-Objekt zur Anzeige der Labels.
   */
  private static class AbbuchungsmodusObject implements GenericObject
  {
    public int abbuchungsmodus;

    private String label = null;

    private AbbuchungsmodusObject(int abbuchungsmodus)
    {
      this.abbuchungsmodus = abbuchungsmodus;

      switch (abbuchungsmodus)
      {
        case AbbuchungsmodusInput.KEINBEITRAG:
          this.label = "keine Beitragsabbuchung";
          break;
        case AbbuchungsmodusInput.JAEHRLICH:
          this.label = "Jahresbeiträge";
          break;
        case AbbuchungsmodusInput.HALBJAEHRLICH:
          this.label = "Halbjahresbeiträge";
          break;
        case AbbuchungsmodusInput.VIERTELJAEHRLICH:
          this.label = "Vierteljahresbeiträge";
          break;
        case AbbuchungsmodusInput.MONATLICH:
        case AbbuchungsmodusInput.MO:
          this.label = "Monatsbeiträge";
          break;
        case AbbuchungsmodusInput.JAHAVIMO:
          this.label = "Jahres-, Halbjahres-, Vierteljahres- und Monatsbeiträge";
          break;
        case AbbuchungsmodusInput.HAVIMO:
          this.label = "Halbjahres-, Vierteljahres- und Monatsbeiträge";
          break;
        case AbbuchungsmodusInput.VIMO:
          this.label = "Vierteljahres- und Monatsbeiträge";
          break;
        case AbbuchungsmodusInput.EINGETRETENEMITGLIEDER:
          this.label = "eingetretene Mitglieder";
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
      return "" + abbuchungsmodus;
    }

    public String getPrimaryAttribute() throws RemoteException
    {
      return "name";
    }

    public boolean equals(GenericObject arg0) throws RemoteException
    {
      if (arg0 == null || !(arg0 instanceof AbbuchungsmodusObject))
      {
        return false;
      }
      return this.getID().equals(arg0.getID());
    }
  }
}
