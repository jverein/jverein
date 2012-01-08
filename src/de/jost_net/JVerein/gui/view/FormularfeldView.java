/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * This program is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,  but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See 
 *  the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, 
 * see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de
 * www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.FormularfeldControl;
import de.jost_net.JVerein.rmi.Formularfeld;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

public class FormularfeldView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Formularfeld"));
    Formularfeld ff = (Formularfeld) getCurrentObject();

    final FormularfeldControl control = new FormularfeldControl(this,
        ff.getFormular());

    LabelGroup group = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Formularfeld"));
    group.addLabelPair(JVereinPlugin.getI18n().tr("Name"), control.getName());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Seite"), control.getSeite());
    group.addLabelPair(JVereinPlugin.getI18n().tr("von links"), control.getX());
    group.addLabelPair(JVereinPlugin.getI18n().tr("von unten"), control.getY());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Font"), control.getFont());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Font-Höhe"),
        control.getFontsize());

    ButtonArea buttons = new ButtonArea(getParent(), 3);
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.FORMULARE, false,
        "help-browser.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("speichern"), new Action()
    {

      public void handleAction(Object context)
      {
        control.handleStore();
      }
    }, null, true, "document-save.png");
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
