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

import de.jost_net.JVerein.gui.view.TermineView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;

/**
 * Startet die View mit den Terminen.
 */
public class TermineAction implements Action
{

  /**
   * @see de.willuhn.jameica.gui.Action#handleAction(java.lang.Object)
   */
  public void handleAction(Object context)
  {
    GUI.startView(TermineView.class, context);
  }
}

/**********************************************************************
 * $Log$ Revision 1.1 2010-11-19 18:37:19 willuhn
 * 
 * @N Erste Version der Termin-View mit Appointment-Providern
 * 
 **********************************************************************/
