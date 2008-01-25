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
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.gui.control.EigenschaftenControl;
import de.jost_net.JVerein.gui.control.HilfsEigenschaft;
import de.willuhn.jameica.gui.Action;
import de.willuhn.util.ApplicationException;

public class EigenschaftenSelectAction implements Action
{
  private EigenschaftenControl control;

  public EigenschaftenSelectAction(EigenschaftenControl control)
  {
    this.control = control;
  }

  public void handleAction(Object context) throws ApplicationException
  {
    HilfsEigenschaft ei = null;
    if (context != null && context instanceof HilfsEigenschaft)
    {
      ei = (HilfsEigenschaft) context;
      try
      {
        control.getNeu().setValue(ei.getEigenschaft());
      }
      catch (RemoteException e)
      {
        e.printStackTrace();
      }
    }
  }
}
