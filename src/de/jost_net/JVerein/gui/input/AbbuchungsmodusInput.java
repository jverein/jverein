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
 * Revision 1.7  2008/12/19 06:54:02  jost
 * Eingetretene Mitglieder auch bei monatlicher, .... Abrechnung
 *
 * Revision 1.6  2008/11/29 13:10:11  jost
 * Refactoring: Code-Optimierung
 *
 * Revision 1.5  2008/11/16 16:57:13  jost
 * Speicherung der Einstellung von Property-Datei in die Datenbank verschoben.
 *
 * Revision 1.4  2008/08/10 12:35:19  jost
 * Abbuchung -> Abrechnung
 * Vorbereitung der Rechnungserstellung
 *
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
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.keys.Beitragsmodel;
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
    if (Einstellungen.getEinstellung().getBeitragsmodel() == Beitragsmodel.JAEHRLICH)
    {
      l.add(new AbbuchungsmodusObject(JAEHRLICH));
      l.add(new AbbuchungsmodusObject(EINGETRETENEMITGLIEDER));
    }
    if (Einstellungen.getEinstellung().getBeitragsmodel() == Beitragsmodel.HALBJAEHRLICH)
    {
      l.add(new AbbuchungsmodusObject(HALBJAEHRLICH));
      l.add(new AbbuchungsmodusObject(EINGETRETENEMITGLIEDER));
    }
    if (Einstellungen.getEinstellung().getBeitragsmodel() == Beitragsmodel.VIERTELJAEHRLICH)
    {
      l.add(new AbbuchungsmodusObject(VIERTELJAEHRLICH));
      l.add(new AbbuchungsmodusObject(EINGETRETENEMITGLIEDER));
    }
    if (Einstellungen.getEinstellung().getBeitragsmodel() == Beitragsmodel.MONATLICH)
    {
      l.add(new AbbuchungsmodusObject(MONATLICH));
      l.add(new AbbuchungsmodusObject(EINGETRETENEMITGLIEDER));
    }
    if (Einstellungen.getEinstellung().getBeitragsmodel() == Beitragsmodel.MONATLICH12631)
    {
      l.add(new AbbuchungsmodusObject(JAHAVIMO));
      l.add(new AbbuchungsmodusObject(JAVIMO));
      l.add(new AbbuchungsmodusObject(HAVIMO));
      l.add(new AbbuchungsmodusObject(VIMO));
      l.add(new AbbuchungsmodusObject(JA));
      l.add(new AbbuchungsmodusObject(HA));
      l.add(new AbbuchungsmodusObject(VI));
      l.add(new AbbuchungsmodusObject(MO));
      l.add(new AbbuchungsmodusObject(EINGETRETENEMITGLIEDER));
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
          this.label = JVereinPlugin.getI18n().tr("keine Beitragsabrechnung");
          break;
        case AbbuchungsmodusInput.JAEHRLICH:
          this.label = JVereinPlugin.getI18n().tr("Jahresbeitr‰ge");
          break;
        case AbbuchungsmodusInput.HALBJAEHRLICH:
          this.label = JVereinPlugin.getI18n().tr("Halbjahresbeitr‰ge");
          break;
        case AbbuchungsmodusInput.VIERTELJAEHRLICH:
          this.label = JVereinPlugin.getI18n().tr("Vierteljahresbeitr‰ge");
          break;
        case AbbuchungsmodusInput.MONATLICH:
        case AbbuchungsmodusInput.MO:
          this.label = JVereinPlugin.getI18n().tr("Monatsbeitr‰ge");
          break;
        case AbbuchungsmodusInput.VI:
          this.label = JVereinPlugin.getI18n().tr("Viertelj‰hrlich");
          break;
        case AbbuchungsmodusInput.HA:
          this.label = JVereinPlugin.getI18n().tr("Halbj‰hrlich");
          break;
        case AbbuchungsmodusInput.JA:
          this.label = JVereinPlugin.getI18n().tr("J‰hrlich");
          break;
        case AbbuchungsmodusInput.JAHAVIMO:
          this.label = JVereinPlugin.getI18n().tr(
              "Jahres-, Halbjahres-, Vierteljahres- und Monatsbeitr‰ge");
          break;
        case AbbuchungsmodusInput.JAVIMO:
          this.label = JVereinPlugin.getI18n().tr(
              "Jahres-, Vierteljahres- und Monatsbeitr‰ge");
          break;
        case AbbuchungsmodusInput.HAVIMO:
          this.label = JVereinPlugin.getI18n().tr(
              "Halbjahres-, Vierteljahres- und Monatsbeitr‰ge");
          break;
        case AbbuchungsmodusInput.VIMO:
          this.label = JVereinPlugin.getI18n().tr(
              "Vierteljahres- und Monatsbeitr‰ge");
          break;
        case AbbuchungsmodusInput.EINGETRETENEMITGLIEDER:
          this.label = JVereinPlugin.getI18n().tr("eingetretene Mitglieder");
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
