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
 * Revision 1.7  2009/01/20 19:14:45  jost
 * neu: Back-Button mit Icon
 *
 * Revision 1.6  2008/05/24 14:04:08  jost
 * Redatkionelle Ã„nderung
 *
 * Revision 1.5  2008/01/01 19:48:33  jost
 * Erweiterung um Hilfe-Funktion
 *
 * Revision 1.4  2007/03/25 17:00:12  jost
 * Beitragsart aufgenommen.
 *
 * Revision 1.3  2007/02/23 20:27:28  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.2  2006/10/29 07:49:11  jost
 * Redaktionelle Änderung
 *
 * Revision 1.1  2006/09/20 15:39:10  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.BeitragsgruppeSucheAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.BeitragsgruppeControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class BeitragsgruppeDetailView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Beitragsgruppe");

    final BeitragsgruppeControl control = new BeitragsgruppeControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Beitrag");
    group.addLabelPair("Bezeichnung", control.getBezeichnung());
    group.addLabelPair("Betrag", control.getBetrag());
    group.addLabelPair("Beitragsart", control.getBeitragsArt());

    ButtonArea buttons = new ButtonArea(getParent(), 4);
    buttons.addButton(new Back(false));
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.BEITRAGSGRUPPEN, false, "help-browser.png");
    buttons.addButton("Suche", new BeitragsgruppeSucheAction());
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
