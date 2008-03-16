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

import de.jost_net.JVerein.gui.action.BackAction;
import de.jost_net.JVerein.gui.control.BuchungsControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
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
    group.addLabelPair("von Datum", control.getVondatum());
    group.addLabelPair("bis Datum", control.getBisdatum());

    ButtonArea buttons = new ButtonArea(this.getParent(), 2);
    buttons.addButton("<< Zurück", new BackAction());
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
  }

  public void unbind() throws ApplicationException
  {
  }
}
