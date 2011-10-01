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
 **********************************************************************/
package de.jost_net.JVerein.gui.boxes;

import java.rmi.RemoteException;
import java.util.Date;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.parts.TerminePart;
import de.willuhn.jameica.gui.boxes.AbstractBox;
import de.willuhn.jameica.gui.boxes.Box;

public class Termine extends AbstractBox implements Box
{
  private TerminePart termine = null;

  private static Date currentDate = null;

  public Termine()
  {
    super();
    currentDate = new Date();
  }

  public String getName()
  {
    return JVereinPlugin.getI18n().tr("JVerein: Termine");
  }

  public int getDefaultIndex()
  {
    return 3;
  }

  public boolean getDefaultEnabled()
  {
    return false;
  }

  public void paint(Composite parent) throws RemoteException
  {
    this.termine = new TerminePart();
    this.termine.setCurrentDate(currentDate);
    this.termine.paint(parent);
  }

  @Override
  public boolean isActive()
  {
    return super.isActive();
  }

  @Override
  public int getHeight()
  {
    return 500;
  }

}
