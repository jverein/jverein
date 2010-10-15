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
 * Revision 1.1  2009-09-13 19:20:05  jost
 * Neu: Prüfung auf Updates
 *
 **********************************************************************/

package de.jost_net.JVerein.gui.input;

import java.rmi.RemoteException;
import java.util.ArrayList;

import de.jost_net.JVerein.JVereinPlugin;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.jameica.gui.input.SelectInput;

/**
 * Combo-Box, fuer die Auswahl des Abbuchungsmodus.
 */
public class UpdateIntervalInput extends SelectInput
{

  public static final int MANUELL = 0;

  public static final int TAEGLICH = 1;

  public static final int MONATLICH = 30;

  public UpdateIntervalInput(int updateinterval) throws RemoteException
  {
    super(init(), new UpdateIntervalObject(updateinterval));
  }

  /**
   * @return initialisiert die Liste der Optionen.
   * @throws RemoteException
   */
  private static GenericIterator init() throws RemoteException
  {
    ArrayList<UpdateIntervalObject> l = new ArrayList<UpdateIntervalObject>();
    l.add(new UpdateIntervalObject(TAEGLICH));
    l.add(new UpdateIntervalObject(MONATLICH));
    l.add(new UpdateIntervalObject(MANUELL));
    return PseudoIterator.fromArray(l.toArray(new UpdateIntervalObject[l.size()]));
  }

  /**
   * @see de.willuhn.jameica.gui.input.Input#getValue()
   */
  @Override
  public Object getValue()
  {
    UpdateIntervalObject o = (UpdateIntervalObject) super.getValue();
    if (o == null)
    {
      return MONATLICH;
    }
    return o.updateinterval;
  }

  /**
   * Hilfs-Objekt zur Anzeige der Labels.
   */
  private static class UpdateIntervalObject implements GenericObject
  {

    public Integer updateinterval;

    private String label = null;

    private UpdateIntervalObject(Integer updateinterval)
    {
      this.updateinterval = updateinterval;

      if (updateinterval.equals(TAEGLICH))
      {
        this.label = JVereinPlugin.getI18n().tr("Täglich");
      }
      else if (updateinterval.equals(MONATLICH))
      {
        this.label = JVereinPlugin.getI18n().tr("Monatlich");
      }
      else if (updateinterval.equals(MANUELL))
      {
        this.label = JVereinPlugin.getI18n().tr("Manuell");
      }
      else
      {
        this.label = "Programmfehler";
      }
    }

    public Object getAttribute(String arg0)
    {
      return label;
    }

    public String[] getAttributeNames()
    {
      return new String[] { "name"};
    }

    public String getID()
    {
      return updateinterval.toString();
    }

    public String getPrimaryAttribute()
    {
      return "name";
    }

    public boolean equals(GenericObject arg0) throws RemoteException
    {
      if (arg0 == null || !(arg0 instanceof UpdateIntervalObject))
      {
        return false;
      }
      return this.getID().equals(arg0.getID());
    }
  }
}
