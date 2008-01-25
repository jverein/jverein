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

import de.jost_net.JVerein.gui.control.EigenschaftenControl;
import de.jost_net.JVerein.gui.dialogs.EigenschaftenNeuDialog;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.jameica.gui.Action;
import de.willuhn.util.ApplicationException;

public class EigenschaftenNewAction implements Action
{
  private Mitglied m;

  private EigenschaftenControl control;

  public EigenschaftenNewAction(EigenschaftenControl control, Mitglied m)
  {
    this.control = control;
    this.m = m;
  }

  public void handleAction(Object context) throws ApplicationException
  {
    try
    {
      EigenschaftenNeuDialog ned = new EigenschaftenNeuDialog(m);
      ned.open();
      control.refreshTable();
    }
    catch (Exception e)
    {
      throw new ApplicationException(
          "Diaglog zur Eingabe neuer Eigenschaften kann nicht geöffnet werden ("
              + e.getMessage() + ")");
    }
  }
}
