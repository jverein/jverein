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
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.RechnungListeAction;
import de.jost_net.JVerein.gui.action.BackAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.RechnungControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class RechnungDetailView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Rechnungsinformationen");

    final RechnungControl control = new RechnungControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Beitrag");
    group.addLabelPair("Zweck1", control.getZweck1());
    group.addLabelPair("Zweck2", control.getZweck2());
    group.addLabelPair("Datum", control.getDatum());
    group.addLabelPair("Betrag", control.getBetrag());

    ButtonArea buttons = new ButtonArea(getParent(), 4);
    buttons.addButton("<< Zurück", new BackAction());
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.RECHNUNG);
    buttons.addButton("Suche", new RechnungListeAction());
    buttons.addButton("Speichern", new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        control.handleStore();
      }
    }, null, true);
  }

  public void unbind() throws ApplicationException
  {
  }
}
