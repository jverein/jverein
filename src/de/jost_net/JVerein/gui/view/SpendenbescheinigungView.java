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
 * Revision 1.4  2009/01/26 18:48:09  jost
 * Neu: Ersatz Aufwendungen
 *
 * Revision 1.3  2009/01/20 20:09:24  jost
 * neue Icons
 *
 * Revision 1.2  2009/01/20 19:15:19  jost
 * neu: Back-Button mit Icon
 *
 * Revision 1.1  2008/07/18 20:15:38  jost
 * Neu: Spendenbescheinigung
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.SpendenbescheinigungControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.ColumnLayout;
import de.willuhn.jameica.gui.util.ScrolledContainer;
import de.willuhn.jameica.gui.util.SimpleContainer;
import de.willuhn.util.ApplicationException;

public class SpendenbescheinigungView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Spendenbescheinigung");

    final SpendenbescheinigungControl control = new SpendenbescheinigungControl(
        this);
    ScrolledContainer scrolled = new ScrolledContainer(getParent());

    ColumnLayout cols1 = new ColumnLayout(scrolled.getComposite(), 2);
    SimpleContainer left = new SimpleContainer(cols1.getComposite());

    left.addHeadline("Empfänger");
    left.addLabelPair("Zeile 1", control.getZeile1());
    left.addLabelPair("Zeile 2", control.getZeile2());
    left.addLabelPair("Zeile 3", control.getZeile3());
    left.addLabelPair("Zeile 4", control.getZeile4());
    left.addLabelPair("Zeile 5", control.getZeile5());
    left.addLabelPair("Zeile 6", control.getZeile6());
    left.addLabelPair("Zeile 7", control.getZeile7());

    SimpleContainer right = new SimpleContainer(cols1.getComposite());

    right.addHeadline("Datum");
    right.addLabelPair("Spende", control.getSpendedatum());
    right.addLabelPair("Bescheinigung", control.getBescheinigungsdatum());

    right.addHeadline("Betrag");
    right.addLabelPair("Betrag", control.getBetrag());

    right.addHeadline("Ersatz für Aufwendungen");
    right.addLabelPair("Ersatz für Aufwendungen", control
        .getErsatzAufwendungen());

    right.addHeadline("Formular");
    right.addLabelPair("Formular", control.getFormular());

    ButtonArea buttons = new ButtonArea(getParent(), 4);
    buttons.addButton(new Back(false));
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.SPENDENBESCHEINIGUNG, false, "help-browser.png");
    buttons.addButton(control.getPDFButton());
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
