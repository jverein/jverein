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
 * Revision 1.7  2009/07/24 20:21:56  jost
 * Focus auf erstes Feld setzen.
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
 * Revision 1.3  2008/05/24 14:04:08  jost
 * Redatkionelle Ã„nderung
 *
 * Revision 1.2  2008/01/01 19:51:47  jost
 * Erweiterung um Hilfe-Funktion
 *
 * Revision 1.1  2007/02/25 19:13:34  jost
 * Neu: Kursteilnehmer
 *
 * Revision 1.1  2007/02/23 20:27:28  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.KursteilnehmerDeleteAction;
import de.jost_net.JVerein.gui.action.KursteilnehmerDetailAction;
import de.jost_net.JVerein.gui.control.KursteilnehmerControl;
import de.jost_net.JVerein.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class KursteilnehmerDetailView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Kursteilnehmer"));

    final KursteilnehmerControl control = new KursteilnehmerControl(this);

    LabelGroup grGrund = new LabelGroup(getParent(), JVereinPlugin.getI18n()
        .tr("Daten für die Abbuchung"));
    grGrund.getComposite().setSize(290, 190);
    grGrund.addLabelPair(JVereinPlugin.getI18n().tr("Name"), control
        .getName(true));
    grGrund.addLabelPair(JVereinPlugin.getI18n().tr("Verwendungszweck 1"),
        control.getVZweck1());
    grGrund.addLabelPair(JVereinPlugin.getI18n().tr("Verwendungszweck 2"),
        control.getVZweck2());
    grGrund.addLabelPair(JVereinPlugin.getI18n().tr("BLZ"), control.getBlz());
    grGrund.addLabelPair(JVereinPlugin.getI18n().tr("Konto"), control
        .getKonto());
    grGrund.addLabelPair(JVereinPlugin.getI18n().tr("Betrag"), control
        .getBetrag());

    LabelGroup grStatistik = new LabelGroup(getParent(), JVereinPlugin
        .getI18n().tr("Statistik"));
    grStatistik.getComposite().setSize(290, 190);
    grStatistik.addLabelPair(JVereinPlugin.getI18n().tr("Geburtsdatum"),
        control.getGeburtsdatum());
    grStatistik.addLabelPair(JVereinPlugin.getI18n().tr("Geschlecht"), control
        .getGeschlecht());

    ButtonArea buttons = new ButtonArea(getParent(), 5);

    buttons.addButton(new Back(false));
    buttons.addButton(JVereinPlugin.getI18n().tr("&Hilfe"),
        new DokumentationAction(), DokumentationUtil.KURSTEILNEHMER, false,
        "help-browser.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("&neu"),
        new KursteilnehmerDetailAction(), null, false, "document-new.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("&löschen"),
        new KursteilnehmerDeleteAction(), control.getCurrentObject(), false,
        "user-trash.png");
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

}
