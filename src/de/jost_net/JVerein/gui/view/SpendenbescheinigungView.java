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
 * Revision 1.9  2010-08-23 13:39:32  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.8  2010/01/03 08:58:53  jost
 * Neu-Button aufgenommen.
 *
 * Revision 1.7  2009/07/24 20:22:53  jost
 * Focus auf erstes Feld setzen.
 *
 * Revision 1.6  2009/06/11 21:03:39  jost
 * Vorbereitung I18N
 *
 * Revision 1.5  2009/01/27 18:51:03  jost
 * *** empty log message ***
 *
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

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.SpendenbescheinigungAction;
import de.jost_net.JVerein.gui.control.SpendenbescheinigungControl;
import de.jost_net.JVerein.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.ColumnLayout;
import de.willuhn.jameica.gui.util.ScrolledContainer;
import de.willuhn.jameica.gui.util.SimpleContainer;
import de.willuhn.util.ApplicationException;

public class SpendenbescheinigungView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Spendenbescheinigung"));

    final SpendenbescheinigungControl control = new SpendenbescheinigungControl(
        this);
    ScrolledContainer scrolled = new ScrolledContainer(getParent());

    ColumnLayout cols1 = new ColumnLayout(scrolled.getComposite(), 2);
    SimpleContainer left = new SimpleContainer(cols1.getComposite());

    left.addHeadline(JVereinPlugin.getI18n().tr("Empfänger"));
    left.addLabelPair(JVereinPlugin.getI18n().tr("Zeile 1"), control
        .getZeile1(true));
    left.addLabelPair(JVereinPlugin.getI18n().tr("Zeile 2"), control
        .getZeile2());
    left.addLabelPair(JVereinPlugin.getI18n().tr("Zeile 3"), control
        .getZeile3());
    left.addLabelPair(JVereinPlugin.getI18n().tr("Zeile 4"), control
        .getZeile4());
    left.addLabelPair(JVereinPlugin.getI18n().tr("Zeile 5"), control
        .getZeile5());
    left.addLabelPair(JVereinPlugin.getI18n().tr("Zeile 6"), control
        .getZeile6());
    left.addLabelPair(JVereinPlugin.getI18n().tr("Zeile 7"), control
        .getZeile7());

    SimpleContainer right = new SimpleContainer(cols1.getComposite());

    right.addHeadline(JVereinPlugin.getI18n().tr("Datum"));
    right.addLabelPair(JVereinPlugin.getI18n().tr("Spende"), control
        .getSpendedatum());
    right.addLabelPair(JVereinPlugin.getI18n().tr("Bescheinigung"), control
        .getBescheinigungsdatum());

    right.addHeadline(JVereinPlugin.getI18n().tr("Betrag"));
    right.addLabelPair(JVereinPlugin.getI18n().tr("Betrag"), control
        .getBetrag());

    right.addHeadline(JVereinPlugin.getI18n().tr("Ersatz für Aufwendungen"));
    right.addLabelPair(JVereinPlugin.getI18n().tr("Ersatz für Aufwendungen"),
        control.getErsatzAufwendungen());

    right.addHeadline(JVereinPlugin.getI18n().tr("Formular"));
    right.addLabelPair(JVereinPlugin.getI18n().tr("Formular"), control
        .getFormular());

    ButtonArea buttons = new ButtonArea(getParent(), 5);
    buttons.addButton(new Back(false));
    buttons.addButton(JVereinPlugin.getI18n().tr("&Hilfe"),
        new DokumentationAction(), DokumentationUtil.SPENDENBESCHEINIGUNG,
        false, "help-browser.png");
    buttons.addButton(control.getPDFButton());
    buttons.addButton(JVereinPlugin.getI18n().tr("&neu"),
        new SpendenbescheinigungAction(), null, false, "document-new.png");
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
    return "<form><p><span color=\"header\" font=\"header\">Spendenbescheinigung</span></p>"
        + "</form>";
  }
}
