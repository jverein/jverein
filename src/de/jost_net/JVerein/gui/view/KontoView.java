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
 * Revision 1.3  2008/05/26 18:58:52  jost
 * Neu: Eröffnungsdatum
 *
 * Revision 1.2  2008/05/24 14:04:08  jost
 * Redatkionelle Änderung
 *
 * Revision 1.1  2008/05/22 06:54:09  jost
 * Buchführung: Beginn des Geschäftsjahres
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.KontoControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class KontoView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Konto");

    final KontoControl control = new KontoControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Konto");
    group.addLabelPair("Nummer", control.getNummer());
    group.addLabelPair("Bezeichnung", control.getBezeichnung());
    group.addLabelPair("Konto-Er�ffnung", control.getEroeffnung());
    group.addLabelPair("Konto-Aufl�sung", control.getAufloesung());
    group.addLabelPair("Hibiscus-ID", control.getHibiscusId());

    ButtonArea buttons = new ButtonArea(getParent(), 3);

    buttons.addButton(new Back(false));
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.KONTEN);
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
