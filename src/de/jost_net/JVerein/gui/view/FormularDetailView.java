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
 * Revision 1.6  2009/07/24 20:21:34  jost
 * Focus auf erstes Feld setzen.
 *
 * Revision 1.5  2009/06/11 21:03:39  jost
 * Vorbereitung I18N
 *
 * Revision 1.4  2009/03/26 21:03:06  jost
 * Javadoc angepasst.
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.FormularControl;
import de.jost_net.JVerein.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class FormularDetailView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Formular"));

    final FormularControl control = new FormularControl(this);

    LabelGroup group = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Formular"));
    group.addLabelPair(JVereinPlugin.getI18n().tr("Bezeichnung"), control
        .getBezeichnung(true));
    group.addLabelPair(JVereinPlugin.getI18n().tr("Art"), control.getArt());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Datei"), control.getDatei());

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
    return "<form><p><span color=\"header\" font=\"header\">Formular</span></p>"
        + "<p>Jedem Formuar ist ein eindeutiger Name zu geben. Die Art ist auszuwählen. "
        + "Ein Dateiname ist bei der Neuaufnahme eines Formulares anzugeben oder wenn "
        + "das Formular aktualisiert wird. Das Formular muss im PDF-Format vorliegen.</p>"
        + "</form>";
  }
}
