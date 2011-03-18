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
package de.jost_net.JVerein.gui.boxes;

import java.rmi.RemoteException;
import java.util.Date;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.parts.TerminePart;
import de.jost_net.JVerein.gui.view.TermineView;
import de.willuhn.jameica.gui.boxes.AbstractBox;
import de.willuhn.jameica.gui.boxes.Box;
import de.willuhn.jameica.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.util.I18N;

public class Termine extends AbstractBox implements Box
{
  private TerminePart termine = null;

  private final static I18N i18n = JVereinPlugin.getI18n();

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
