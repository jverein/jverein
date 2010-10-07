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
 * Revision 1.7  2010-08-23 13:39:32  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.6  2009/06/11 21:03:39  jost
 * Vorbereitung I18N
 *
 * Revision 1.5  2009/01/20 20:09:24  jost
 * neue Icons
 *
 * Revision 1.4  2009/01/20 19:15:19  jost
 * neu: Back-Button mit Icon
 *
 * Revision 1.3  2008/05/26 18:58:52  jost
 * Neu: ErÃ¶ffnungsdatum
 *
 * Revision 1.2  2008/05/24 14:04:08  jost
 * Redatkionelle Ã„nderung
 *
 * Revision 1.1  2008/05/22 06:54:09  jost
 * BuchfÃ¼hrung: Beginn des GeschÃ¤ftsjahres
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.KontoControl;
import de.jost_net.JVerein.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class KontoView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Konto"));

    final KontoControl control = new KontoControl(this);

    LabelGroup group = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Konto"));
    group.addLabelPair(JVereinPlugin.getI18n().tr("Nummer"), control
        .getNummer());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Bezeichnung"), control
        .getBezeichnung());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Konto-Eröffnung"), control
        .getEroeffnung());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Konto-Auflösung"), control
        .getAufloesung());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Hibiscus-ID"), control
        .getHibiscusId());

    ButtonArea buttons = new ButtonArea(getParent(), 3);

    buttons.addButton(new Back(false));
    buttons.addButton(JVereinPlugin.getI18n().tr("&Hilfe"),
        new DokumentationAction(), DokumentationUtil.KONTEN, false,
        "help-browser.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("&speichern"), new Action()
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

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Konto</span></p>"
        + "<p>Kontonummer, Bezeichnung und Datum der Auflösung des Kontos können gespeichert "
        + "werden.</p>"
        + "<p>Für Hibiscus-Konten wird zusätzlich die Hibiscus-ID des Kontos gespeichert.</p>"
        + "</form>";
  }
}
