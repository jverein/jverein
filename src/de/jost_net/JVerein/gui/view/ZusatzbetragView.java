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
 * Revision 1.1  2008/12/22 21:18:44  jost
 * Zusatzabbuchung->Zusatzbetrag
 *
 * Revision 1.5  2008/05/24 14:04:08  jost
 * Redatkionelle Ã„nderung
 *
 * Revision 1.4  2008/01/01 19:53:57  jost
 * Erweiterung um Hilfe-Funktion
 *
 * Revision 1.3  2007/03/30 13:25:23  jost
 * Wiederkehrende Zusatzabbuchungen.
 *
 * Revision 1.2  2007/02/23 20:27:42  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.1  2006/09/20 15:39:10  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.ZusatzbetraegeDeleteAction;
import de.jost_net.JVerein.gui.control.ZusatzbetragControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class ZusatzbetragView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Zusatzbetrag");
    final ZusatzbetragControl control = new ZusatzbetragControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Zusatzbetrag");
    group.addLabelPair("Startdatum", control.getStartdatum());
    group.addLabelPair("nächste Fälligkeit", control.getFaelligkeit());
    group.addLabelPair("Intervall", control.getIntervall());
    group.addLabelPair("Endedatum", control.getEndedatum());
    group.addLabelPair("Buchungstext", control.getBuchungstext());
    group.addLabelPair("Betrag", control.getBetrag());

    ButtonArea buttons = new ButtonArea(getParent(), 4);
    buttons.addButton(new Back(false));
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.ZUSATZABBUCHUNGEN, false, "help-browser.png");
    buttons.addButton("Löschen", new ZusatzbetraegeDeleteAction(), control
        .getZusatzbetrag(), false, "user-trash.png");
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
