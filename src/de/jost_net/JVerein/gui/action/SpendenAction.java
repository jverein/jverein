/**********************************************************************
ei * $Source$
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

import de.jost_net.JVerein.gui.view.SpendenView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

/**
 * Action zum Starten der Spenden-View.
 */
public class SpendenAction implements Action
{

  /**
   * @see de.willuhn.jameica.gui.Action#handleAction(java.lang.Object)
   */
  public void handleAction(Object context) throws ApplicationException
  {
    GUI.startView(SpendenView.class, null);
  }

}

/**********************************************************************
 * $Log$ Revision 1.1 2010-08-20 12:42:02 willuhn
 * 
 * @N Neuer Spenden-Aufruf. Ich bin gespannt, ob das klappt ;)
 * 
 **********************************************************************/
