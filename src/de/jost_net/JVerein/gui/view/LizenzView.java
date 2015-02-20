/**********************************************************************
 *
 * Copyright (c) by Olaf Willuhn
 * All rights reserved
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.control.LizenzControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.FormTextPart;

/**
 * View fuer die Lizenz-Informationen
 */
public class LizenzView extends AbstractView
{

  /**
   * @see de.willuhn.jameica.gui.AbstractView#bind()
   */
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Lizenzinformationen JVerein");

    LizenzControl control = new LizenzControl(this);

    FormTextPart libs = control.getLibList();
    libs.paint(getParent());
  }

  /**
   * @see de.willuhn.jameica.gui.AbstractView#canBookmark()
   */
  public boolean canBookmark()
  {
    return false;
  }

}