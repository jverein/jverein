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
 * Revision 1.11  2010-10-15 09:58:25  jost
 * Code aufgeräumt
 *
 * Revision 1.10  2010-10-07 19:49:23  jost
 * Hilfe in die View verlagert.
 *
 * Revision 1.9  2010-09-29 16:38:12  jost
 * Button umbenannt.
 *
 * Revision 1.8  2010-08-23 13:39:32  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.7  2009/06/11 21:03:39  jost
 * Vorbereitung I18N
 *
 * Revision 1.6  2009/02/27 14:21:58  jost
 * Bug #15324
 *
 * Revision 1.5  2009/01/22 18:24:24  jost
 * neue Icons
 *
 * Revision 1.4  2009/01/20 20:09:24  jost
 * neue Icons
 *
 * Revision 1.3  2009/01/20 19:15:19  jost
 * neu: Back-Button mit Icon
 *
 * Revision 1.2  2008/05/24 14:04:08  jost
 * Redatkionelle Ã„nderung
 *
 * Revision 1.1  2008/05/22 06:53:57  jost
 * BuchfÃ¼hrung: Beginn des GeschÃ¤ftsjahres
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.HibiscusKontenImportAction;
import de.jost_net.JVerein.gui.action.KontoAction;
import de.jost_net.JVerein.gui.control.KontoControl;
import de.jost_net.JVerein.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;

public class KontoListView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Konten"));

    KontoControl control = new KontoControl(this);

    control.getKontenList().paint(this.getParent());

    ButtonArea buttons = new ButtonArea(this.getParent(), 4);
    buttons.addButton(new Back(false));
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.KONTEN, false,
        "help-browser.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("Hibiscus-Konten-Import"),
        new HibiscusKontenImportAction(control), null, false, "go.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("neu"), new KontoAction(),
        null, false, "document-new.png");
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Konten</span></p>"
        + "<p>Konten können entweder von Hibuscus übernommen werden oder durch einen "
        + "Klick auf neu aufgenommen werden.</p>"
        + "<p>Durch einen Rechtsklick auf ein Konto kann entweder der Anfangsbestand des "
        + "Kontos zu einem Zeitpunkt eingegeben werden oder das Konto kann gelöscht werden, "
        + "sofern keine Buchungen für das Konto existieren.</p></form>";
  }
}
