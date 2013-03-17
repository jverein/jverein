package de.jost_net.JVerein.gui.input;

import java.rmi.RemoteException;

import de.jost_net.OBanToo.SEPA.Land.SEPALand;
import de.willuhn.datasource.GenericObject;

/**
 * Hilfs-Objekt zur Anzeige der Labels.
 */
public class SEPALandObject implements GenericObject
{
  public SEPALand land;

  private String label = null;

  public SEPALandObject(SEPALand land)
  {
    this.land = land;
    if (land == null)
    {
      label = "";
      return;
    }
    this.label = land.getBezeichnung();
  }

  @Override
  public Object getAttribute(String arg0)
  {
    if (arg0.equals("label"))
    {
      return label;
    }
    else if (arg0.equals("land"))
    {
      return land;
    }
    return null;
  }

  @Override
  public String[] getAttributeNames()
  {
    return new String[] { "label", "land" };
  }

  @Override
  public String getID()
  {
    return label;
  }

  public SEPALand getLand()
  {
    return land;
  }

  @Override
  public String getPrimaryAttribute()
  {
    return "label";
  }

  @Override
  public boolean equals(GenericObject arg0) throws RemoteException
  {
    if (arg0 == null || !(arg0 instanceof SEPALandObject))
    {
      return false;
    }
    return this.getID().equals(arg0.getID());
  }
}
