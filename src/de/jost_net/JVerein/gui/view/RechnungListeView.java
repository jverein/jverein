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
 * Revision 1.4  2009/01/20 19:15:19  jost
 * neu: Back-Button mit Icon
 *
 * Revision 1.3  2008/09/30 12:08:29  jost
 * Abrechnungsinformationen können nach Datum und Verwendungszweck gefiltert werden.
 *
 * Revision 1.2  2008/09/21 08:06:47  jost
 * Redaktionelle Änderung
 *
 * Revision 1.1  2008/09/16 18:52:35  jost
 * Neu: Rechnung
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.RechnungControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class RechnungListeView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Rechnungen");

    final RechnungControl control = new RechnungControl(this);
    LabelGroup group = new LabelGroup(getParent(), "Filter");
    group.addLabelPair("Verwendungszweck", control.getSuchverwendungszweck());
    group.addLabelPair("Eingabedatum von", control.getVondatum());
    group.addLabelPair("Eingabedatum bis", control.getBisdatum());

    control.getAbrechungList().paint(this.getParent());

    ButtonArea buttons = new ButtonArea(this.getParent(), 3);
    buttons.addButton(new Back(false));
    buttons.addButton("Drucken", new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        GUI.startView(RechnungView.class.getName(), null);
      }
    }, "printer.png");
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.RECHNUNG, false, "help-browser.png");
  }

  public void unbind() throws ApplicationException
  {
  }

}
