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
 * Revision 1.3  2009/06/11 21:03:39  jost
 * Vorbereitung I18N
 *
 * Revision 1.2  2007/02/23 20:27:42  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.1  2006/09/20 15:39:10  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
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
    GUI.getView().setTitle(
        JVereinPlugin.getI18n().tr("Vereinsverwaltung unter Jamaica"));
    LabelGroup group = new LabelGroup(this.getParent(), JVereinPlugin.getI18n()
        .tr("welcome"));
    group.addText(JVereinPlugin.getI18n().tr("Diese Seite ist leer"), false);
  }

  public void unbind() throws ApplicationException
  {
  }
  // TODO getHelp()

}
