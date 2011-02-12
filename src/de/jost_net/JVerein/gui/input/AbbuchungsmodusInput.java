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
 * Revision 1.10  2010-10-15 09:58:29  jost
 * Code aufger�umt
 *
 * Revision 1.9  2010-04-25 13:54:45  jost
 * Vorarbeiten Mitgliedskonto
 *
 * Revision 1.8  2009/06/11 21:02:51  jost
 * Vorbereitung I18N
 *
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
 * Jährliche, Halbjährliche und Vierteljährliche Abbuchungen können jetzt separat ausgeführt werden.
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
import de.jost_net.JVerein.keys.Abrechnungsmodi;
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
    l.add(new AbbuchungsmodusObject(Abrechnungsmodi.KEINBEITRAG));
    if (Einstellungen.getEinstellung().getBeitragsmodel() == Beitragsmodel.JAEHRLICH)
    {
      l.add(new AbbuchungsmodusObject(Abrechnungsmodi.JAEHRLICH));
      l.add(new AbbuchungsmodusObject(Abrechnungsmodi.EINGETRETENEMITGLIEDER));
    }
    if (Einstellungen.getEinstellung().getBeitragsmodel() == Beitragsmodel.HALBJAEHRLICH)
    {
      l.add(new AbbuchungsmodusObject(Abrechnungsmodi.HALBJAEHRLICH));
      l.add(new AbbuchungsmodusObject(Abrechnungsmodi.EINGETRETENEMITGLIEDER));
    }
    if (Einstellungen.getEinstellung().getBeitragsmodel() == Beitragsmodel.VIERTELJAEHRLICH)
    {
      l.add(new AbbuchungsmodusObject(Abrechnungsmodi.VIERTELJAEHRLICH));
      l.add(new AbbuchungsmodusObject(Abrechnungsmodi.EINGETRETENEMITGLIEDER));
    }
    if (Einstellungen.getEinstellung().getBeitragsmodel() == Beitragsmodel.MONATLICH)
    {
      l.add(new AbbuchungsmodusObject(Abrechnungsmodi.MONATLICH));
      l.add(new AbbuchungsmodusObject(Abrechnungsmodi.EINGETRETENEMITGLIEDER));
    }
    if (Einstellungen.getEinstellung().getBeitragsmodel() == Beitragsmodel.MONATLICH12631)
    {
      l.add(new AbbuchungsmodusObject(Abrechnungsmodi.JAHAVIMO));
      l.add(new AbbuchungsmodusObject(Abrechnungsmodi.JAVIMO));
      l.add(new AbbuchungsmodusObject(Abrechnungsmodi.HAVIMO));
      l.add(new AbbuchungsmodusObject(Abrechnungsmodi.VIMO));
      l.add(new AbbuchungsmodusObject(Abrechnungsmodi.JA));
      l.add(new AbbuchungsmodusObject(Abrechnungsmodi.HA));
      l.add(new AbbuchungsmodusObject(Abrechnungsmodi.VI));
      l.add(new AbbuchungsmodusObject(Abrechnungsmodi.MO));
      l.add(new AbbuchungsmodusObject(Abrechnungsmodi.EINGETRETENEMITGLIEDER));
    }
    return PseudoIterator.fromArray(l.toArray(new AbbuchungsmodusObject[l
        .size()]));
  }

  /**
   * @see de.willuhn.jameica.gui.input.Input#getValue()
   */
  @Override
  public Object getValue()
  {
    AbbuchungsmodusObject o = (AbbuchungsmodusObject) super.getValue();
    if (o == null)
    {
      return Integer.valueOf(Abrechnungsmodi.JAEHRLICH);
    }
    return Integer.valueOf(o.abbuchungsmodus);
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
      this.label = Abrechnungsmodi.get(abbuchungsmodus);
    }

    public Object getAttribute(String arg0)
    {
      return label;
    }

    public String[] getAttributeNames()
    {
      return new String[] { "name" };
    }

    public String getID()
    {
      return "" + abbuchungsmodus;
    }

    public String getPrimaryAttribute()
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
