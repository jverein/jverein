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
 * Revision 1.2  2009/01/20 19:15:19  jost
 * neu: Back-Button mit Icon
 *
 * Revision 1.1  2008/07/18 20:15:38  jost
 * Neu: Spendenbescheinigung
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.SpendenbescheinigungControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class SpendenbescheinigungView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Spendenbescheinigung");

    final SpendenbescheinigungControl control = new SpendenbescheinigungControl(
        this);

    LabelGroup group1 = new LabelGroup(getParent(), "Empfänger");
    group1.addLabelPair("Zeile 1", control.getZeile1());
    group1.addLabelPair("Zeile 2", control.getZeile2());
    group1.addLabelPair("Zeile 3", control.getZeile3());
    group1.addLabelPair("Zeile 4", control.getZeile4());
    group1.addLabelPair("Zeile 5", control.getZeile5());
    group1.addLabelPair("Zeile 6", control.getZeile6());
    group1.addLabelPair("Zeile 7", control.getZeile7());

    LabelGroup group2 = new LabelGroup(getParent(), "Datum");
    group2.addLabelPair("Spende", control.getSpendedatum());
    group2.addLabelPair("Bescheinigung", control.getBescheinigungsdatum());

    LabelGroup group3 = new LabelGroup(getParent(), "Betrag");
    group3.addLabelPair("Betrag", control.getBetrag());

    LabelGroup group4 = new LabelGroup(getParent(), "Formular");
    group4.addLabelPair("Formular", control.getFormular());

    ButtonArea buttons = new ButtonArea(getParent(), 4);
    buttons.addButton(new Back(false));
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.SPENDENBESCHEINIGUNG, false, "help-browser.png");
    buttons.addButton(control.getPDFButton());
    buttons.addButton("Speichern", new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        control.handleStore();
      }
    }, null, true, "document-save.png");
  }

  public void unbind() throws ApplicationException
  {
  }
}
