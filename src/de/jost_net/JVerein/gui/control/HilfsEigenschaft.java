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
 **********************************************************************/package de.jost_net.JVerein.gui.control;

import java.io.Serializable;
import java.rmi.RemoteException;

import de.willuhn.datasource.GenericObject;

public class HilfsEigenschaft implements Serializable, GenericObject
{
  private static final long serialVersionUID = -7674540336633201857L;

  private String eigenschaft;

  public HilfsEigenschaft(String eigenschaft)
  {
    this.eigenschaft = eigenschaft;
  }

  public boolean equals(GenericObject other) throws RemoteException
  {
    return other.getAttribute("eigenschaft").equals(eigenschaft);
  }

  public Object getAttribute(String name) throws RemoteException
  {
    if (name.equals("eigenschaft"))
    {
      return eigenschaft;
    }
    return null;
  }

  public String[] getAttributeNames() throws RemoteException
  {
    return new String[] { "eigenschaft" };
  }

  public String getID() throws RemoteException
  {
    return (eigenschaft);
  }

  public String getEigenschaft() throws RemoteException
  {
    return eigenschaft;
  }

  public String getPrimaryAttribute() throws RemoteException
  {
    return "eigenschaft";
  }
}
