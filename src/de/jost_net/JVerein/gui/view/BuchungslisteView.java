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
    GUI.getView().setTitle("Liste der Buchungen");

    final BuchungsControl control = new BuchungsControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Suche Buchungen");
    group.addLabelPair("Konto", control.getSuchKonto());
    group.addLabelPair("Buchungsart", control.getSuchBuchungsart());
    group.addLabelPair("von Datum", control.getVondatum());
    group.addLabelPair("bis Datum", control.getBisdatum());

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
    }, null, true);

    buttons.addButton(button);

    control.getBuchungsList().paint(this.getParent());

    ButtonArea buttons2 = new ButtonArea(this.getParent(), 5);
    buttons2.addButton(new Back(false));
    buttons2.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.BUCHUNGEN);
    buttons2.addButton(control.getStartAuswertungEinzelbuchungenButton());
    buttons2.addButton(control.getStartAuswertungSummenButton());
    buttons2.addButton("neu", new BuchungNeuAction());

  }

  public void unbind() throws ApplicationException
  {
  }
}
