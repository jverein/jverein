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
 * Revision 1.1  2006/09/20 15:39:10  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

/**
 * JVerein-Begrüßungsbildschirm.
 * 
 * @author heiner jostkleigrewe
 */
public class WelcomeView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Vereinsverwaltung unter Jamaica");
    LabelGroup group = new LabelGroup(this.getParent(), "welcome");
    group.addText("Diese Seite ist leer", false);
  }

  public void unbind() throws ApplicationException
  {
  }
}
