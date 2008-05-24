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
 * Revision 1.5  2008/05/22 06:53:14  jost
 * Buchführung
 *
 * Revision 1.4  2008/03/16 07:36:29  jost
 * Reaktivierung Buchführung
 *
 * Revision 1.2  2007/02/23 20:27:28  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.1  2006/09/20 15:39:10  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.BackAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.BuchungsControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class BuchungView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Buchung");

    final BuchungsControl control = new BuchungsControl(this);

    LabelGroup grKontoauszug = new LabelGroup(getParent(), "Buchung");
    grKontoauszug.addLabelPair("Buchungsnummer", control.getID());
    grKontoauszug.addLabelPair("Umsatz-ID", control.getUmsatzid());
    grKontoauszug.addLabelPair("Konto", control.getKonto());
    grKontoauszug.addLabelPair("Name", control.getName());
    grKontoauszug.addLabelPair("Betrag", control.getBetrag());
    grKontoauszug.addLabelPair("Verwendungszweck", control.getZweck());
    grKontoauszug.addLabelPair("Verwendungszweck 2", control.getZweck2());
    grKontoauszug.addLabelPair("Datum", control.getDatum());
    grKontoauszug.addLabelPair("Saldo", control.getSaldo());
    grKontoauszug.addLabelPair("Art", control.getArt());
    grKontoauszug.addLabelPair("Kommentar", control.getKommentar());

    LabelGroup grBuchungsart = new LabelGroup(getParent(), "Buchungsart");
    grBuchungsart.addLabelPair("Buchungsart", control.getBuchungsart());

    ButtonArea buttons = new ButtonArea(getParent(), 3);
    buttons.addButton("<< Zur�ck", new BackAction());
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.BUCHUNGEN);
    buttons.addButton("Speichern", new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        control.handleStore();
      }
    }, null, true);
  }

  public void unbind() throws ApplicationException
  {
  }
}
