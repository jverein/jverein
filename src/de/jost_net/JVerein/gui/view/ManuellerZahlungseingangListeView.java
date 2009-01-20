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
 * Revision 1.3  2008/05/24 14:04:08  jost
 * Redatkionelle Ã„nderung
 *
 * Revision 1.2  2008/01/01 19:52:17  jost
 * Erweiterung um Hilfe-Funktion
 *
 * Revision 1.1  2007/03/13 19:57:17  jost
 * Neu: Manueller Zahlungseingang.
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.ManuellerZahlungseingangControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.util.ApplicationException;

public class ManuellerZahlungseingangListeView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Überwachung des manuellen Zahlungseingangs");

    ManuellerZahlungseingangControl control = new ManuellerZahlungseingangControl(
        this);

    control.getTable().paint(this.getParent());

    ButtonArea buttons = new ButtonArea(this.getParent(), 2);
    buttons.addButton(new Back(false));
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.MANUELLEZAHLUNGSEINGAENGE, false, "help-browser.png");
  }

  public void unbind() throws ApplicationException
  {
  }

}
