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
 * Revision 1.1  2006/10/14 06:02:45  jost
 * Erweiterung um Buchungsauswertung
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.BackAction;
import de.jost_net.JVerein.gui.control.BuchungsControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class AuswertungBuchungView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Auswertung Buchungen");

    final BuchungsControl control = new BuchungsControl(this);

    LabelGroup grZeitraum = new LabelGroup(getParent(), "Zeitraum");
    grZeitraum.addLabelPair("von", control.getVondatum());
    grZeitraum.addLabelPair("bis", control.getBisdatum());

//    LabelGroup grAusgabe = new LabelGroup(getParent(), "Ausgabe");
//    grAusgabe.addLabelPair("Ausgabe", control.getAusgabe());

    ButtonArea buttons = new ButtonArea(getParent(), 4);

    buttons.addButton("<< Zurück", new BackAction());
    buttons.addButton(control.getStartAuswertungButton());

  }

  public void unbind() throws ApplicationException
  {
  }
}
