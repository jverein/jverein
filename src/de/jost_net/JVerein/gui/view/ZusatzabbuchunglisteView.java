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
 * Revision 1.3  2006/12/20 20:25:44  jost
 * Patch von Ullrich Schäfer, der die Primitive vs. Object Problematik adressiert.
 *
 * Revision 1.2  2006/10/07 14:47:25  jost
 * Alten Code entfernt
 *
 * Revision 1.1  2006/09/20 15:39:10  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.BackAction;
import de.jost_net.JVerein.gui.control.ZusatzabbuchungControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class ZusatzabbuchunglisteView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Liste der Zusatzabbuchungen");

    final ZusatzabbuchungControl control = new ZusatzabbuchungControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Ausführungstag");
    group.addLabelPair("Ausführungstag", control.getAusfuehrungSuch());

    ButtonArea buttons = new ButtonArea(this.getParent(), 2);
    buttons.addButton("<< Zurück", new BackAction());

    control.getZusatzabbuchungsList().paint(this.getParent());
  }

  public void unbind() throws ApplicationException
  {
  }

}
