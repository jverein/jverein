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
 * Revision 1.3  2008/01/31 19:36:46  jost
 * J√§hrliche, Halbj√§hrliche und Viertelj√§hrliche Abbuchungen k√∂nnen jetzt separat ausgef√ºhrt werden.
 *
 * Revision 1.2  2007/12/30 10:09:48  jost
 * Neuer Rhytmus: Jahr, Vierteljahr und Monat
 *
 * Revision 1.1  2007/12/02 13:40:04  jost
 * Neu: Beitragsmodelle
 *
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

  public static final int JA = 5;

  public static final int HA = 6;

  public static final int VI = 7;

  public static final int JAHAVIMO = 8;

  public static final int JAVIMO = 9;

  public static final int HAVIMO = 10;

  public static final int VIMO = 11;

  public static final int MO = 12;

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
      l.add(new AbbuchungsmodusObject(JAVIMO));
      l.add(new AbbuchungsmodusObject(HAVIMO));
      l.add(new AbbuchungsmodusObject(VIMO));
      l.add(new AbbuchungsmodusObject(JA));
      l.add(new AbbuchungsmodusObject(HA));
      l.add(new AbbuchungsmodusObject(VI));
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
          this.label = "keine Beitragsabrechnung";
          break;
        case AbbuchungsmodusInput.JAEHRLICH:
          this.label = "Jahresbeitr‰ge";
          break;
        case AbbuchungsmodusInput.HALBJAEHRLICH:
          this.label = "Halbjahresbeitr‰ge";
          break;
        case AbbuchungsmodusInput.VIERTELJAEHRLICH:
          this.label = "Vierteljahresbeitr‰ge";
          break;
        case AbbuchungsmodusInput.MONATLICH:
        case AbbuchungsmodusInput.MO:
          this.label = "Monatsbeitr‰ge";
          break;
        case AbbuchungsmodusInput.VI:
          this.label = "Viertelj‰hrlich";
          break;
        case AbbuchungsmodusInput.HA:
          this.label = "Halbj‰hrlich";
          break;
        case AbbuchungsmodusInput.JA:
          this.label = "J‰hrlich";
          break;
        case AbbuchungsmodusInput.JAHAVIMO:
          this.label = "Jahres-, Halbjahres-, Vierteljahres- und Monatsbeitr‰ge";
          break;
        case AbbuchungsmodusInput.JAVIMO:
          this.label = "Jahres-, Vierteljahres- und Monatsbeitr‰ge";
          break;
        case AbbuchungsmodusInput.HAVIMO:
          this.label = "Halbjahres-, Vierteljahres- und Monatsbeitr‰ge";
          break;
        case AbbuchungsmodusInput.VIMO:
          this.label = "Vierteljahres- und Monatsbeitr‰ge";
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
