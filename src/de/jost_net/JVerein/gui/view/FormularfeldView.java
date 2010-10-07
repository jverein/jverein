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
 * Revision 1.5  2010-08-23 13:39:32  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.4  2009/06/11 21:03:39  jost
 * Vorbereitung I18N
 *
 * Revision 1.3  2009/01/20 20:09:24  jost
 * neue Icons
 *
 * Revision 1.2  2009/01/20 19:15:19  jost
 * neu: Back-Button mit Icon
 *
 * Revision 1.1  2008/07/18 20:13:25  jost
 * Neu: Formulare
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.FormularfeldControl;
import de.jost_net.JVerein.gui.internal.buttons.Back;
import de.jost_net.JVerein.rmi.Formularfeld;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class FormularfeldView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Formularfeld"));
    Formularfeld ff = (Formularfeld) getCurrentObject();

    final FormularfeldControl control = new FormularfeldControl(this, ff
        .getFormular());

    LabelGroup group = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Formularfeld"));
    group.addLabelPair(JVereinPlugin.getI18n().tr("Name"), control.getName());
    group.addLabelPair(JVereinPlugin.getI18n().tr("von links"), control.getX());
    group.addLabelPair(JVereinPlugin.getI18n().tr("von unten"), control.getY());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Font"), control.getFont());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Font-Höhe"), control
        .getFontsize());

    ButtonArea buttons = new ButtonArea(getParent(), 4);
    buttons.addButton(new Back(false));
    buttons.addButton(JVereinPlugin.getI18n().tr("&Hilfe"),
        new DokumentationAction(), DokumentationUtil.FORMULARE, false,
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
    return "<form><p><span color=\"header\" font=\"header\">Formularfeld</span></p>"
        + "<p>Je nach Formulartyp können unterschiedliche Formularfelder ausgewählt werden. "
        + "Jedem Formulartyp ist eine Koordinate mitzugeben. Dabei handelt es sich um die "
        + "Position von links und von unten in Millimetern. Zusätzlich sind der Zeichensatz "
        + "und die Höhe des Zeichens anzugeben.</p></form>";
  }
}
