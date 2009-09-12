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
 * Revision 1.13  2009/06/11 21:03:39  jost
 * Vorbereitung I18N
 *
 * Revision 1.12  2009/01/20 20:09:24  jost
 * neue Icons
 *
 * Revision 1.11  2009/01/20 19:15:19  jost
 * neu: Back-Button mit Icon
 *
 * Revision 1.10  2008/07/10 07:57:21  jost
 * PDF-Export der Buchungen jetzt mit Einzelbuchungen und als Summen
 *
 * Revision 1.9  2008/05/24 19:32:21  jost
 * PDF-Ausgabe verschoben
 *
 * Revision 1.8  2008/05/24 16:39:48  jost
 * Zusätzliche Selektionskriterien
 *
 * Revision 1.7  2008/05/24 14:04:08  jost
 * Redatkionelle Änderung
 *
 * Revision 1.6  2008/05/22 06:52:50  jost
 * Buchführung
 *
 * Revision 1.5  2008/03/16 07:36:29  jost
 * Reaktivierung Buchführung
 *
 * Revision 1.3  2007/02/23 20:27:28  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.2  2006/10/14 16:47:34  jost
 * Reihenfolge der Buttons standardisiert.
 *
 * Revision 1.1  2006/09/20 15:39:10  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import java.rmi.RemoteException;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.BuchungNeuAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.BuchungsControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class BuchungslisteView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Liste der Buchungen"));

    final BuchungsControl control = new BuchungsControl(this);

    LabelGroup group = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Suche Buchungen"));
    group.addLabelPair(JVereinPlugin.getI18n().tr("Konto"), control
        .getSuchKonto());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Buchungsart"), control
        .getSuchBuchungsart());
    group.addLabelPair(JVereinPlugin.getI18n().tr("von Datum"), control
        .getVondatum());
    group.addLabelPair(JVereinPlugin.getI18n().tr("bis Datum"), control
        .getBisdatum());

    ButtonArea buttons = new ButtonArea(this.getParent(), 1);
    Button button = new Button("suchen", new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        try
        {
          control.getBuchungsList();
        }
        catch (RemoteException e)
        {
          e.printStackTrace();
        }
      }
    }, null, true, "system-search.png");

    buttons.addButton(button);

    control.getBuchungsList().paint(this.getParent());

    ButtonArea buttons2 = new ButtonArea(this.getParent(), 6);
    buttons2.addButton(new Back(false));
    buttons2.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.BUCHUNGEN, false,
        "help-browser.png");
    buttons2.addButton(control.getStartAuswertungBuchungsjournalButton());
    buttons2.addButton(control.getStartAuswertungEinzelbuchungenButton());
    buttons2.addButton(control.getStartAuswertungSummenButton());
    buttons2.addButton(JVereinPlugin.getI18n().tr("neu"),
        new BuchungNeuAction(), null, false, "document-new.png");

  }

  public void unbind() throws ApplicationException
  {
  }
}
