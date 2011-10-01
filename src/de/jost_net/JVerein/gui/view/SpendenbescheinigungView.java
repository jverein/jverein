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
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.SpendenbescheinigungAction;
import de.jost_net.JVerein.gui.control.SpendenbescheinigungControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.ColumnLayout;
import de.willuhn.jameica.gui.util.ScrolledContainer;
import de.willuhn.jameica.gui.util.SimpleContainer;

public class SpendenbescheinigungView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Spendenbescheinigung"));

    final SpendenbescheinigungControl control = new SpendenbescheinigungControl(
        this);
    ScrolledContainer scrolled = new ScrolledContainer(getParent());

    ColumnLayout cols1 = new ColumnLayout(scrolled.getComposite(), 2);
    SimpleContainer left = new SimpleContainer(cols1.getComposite());

    left.addHeadline("Spendenart");
    left.addLabelPair(JVereinPlugin.getI18n().tr("Spendenart"),
        control.getSpendenart());

    left.addHeadline(JVereinPlugin.getI18n().tr("Spender"));
    left.addLabelPair(JVereinPlugin.getI18n().tr("Zeile 1"),
        control.getZeile1(true));
    left.addLabelPair(JVereinPlugin.getI18n().tr("Zeile 2"),
        control.getZeile2());
    left.addLabelPair(JVereinPlugin.getI18n().tr("Zeile 3"),
        control.getZeile3());
    left.addLabelPair(JVereinPlugin.getI18n().tr("Zeile 4"),
        control.getZeile4());
    left.addLabelPair(JVereinPlugin.getI18n().tr("Zeile 5"),
        control.getZeile5());
    left.addLabelPair(JVereinPlugin.getI18n().tr("Zeile 6"),
        control.getZeile6());
    left.addLabelPair(JVereinPlugin.getI18n().tr("Zeile 7"),
        control.getZeile7());

    left.addHeadline(JVereinPlugin.getI18n().tr("Datum"));
    left.addLabelPair(JVereinPlugin.getI18n().tr("Spende"),
        control.getSpendedatum());
    left.addLabelPair(JVereinPlugin.getI18n().tr("Bescheinigung"),
        control.getBescheinigungsdatum());

    SimpleContainer right = new SimpleContainer(cols1.getComposite());

    right.addHeadline(JVereinPlugin.getI18n().tr("Betrag"));
    right.addLabelPair(JVereinPlugin.getI18n().tr("Betrag"),
        control.getBetrag());

    right.addHeadline(JVereinPlugin.getI18n().tr("Ersatz für Aufwendungen"));
    right.addLabelPair(JVereinPlugin.getI18n().tr("Ersatz für Aufwendungen"),
        control.getErsatzAufwendungen());

    right.addHeadline(JVereinPlugin.getI18n().tr("Formular"));
    right.addLabelPair(JVereinPlugin.getI18n().tr("Formular"),
        control.getFormular());

    right.addHeadline(JVereinPlugin.getI18n().tr(
        "Zusatzinformationen Sachspenden"));
    right.addLabelPair(JVereinPlugin.getI18n().tr("Bezeichnung Sachzuwendung"),
        control.getBezeichnungSachzuwendung());
    right.addLabelPair(JVereinPlugin.getI18n().tr("Herkunft"),
        control.getHerkunftSpende());
    right.addLabelPair(JVereinPlugin.getI18n().tr("Unterlagen Wertermittlung"),
        control.getUnterlagenWertermittlung());

    ButtonArea buttons = new ButtonArea(getParent(), 5);
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.SPENDENBESCHEINIGUNG,
        false, "help-browser.png");
    buttons.addButton(control.getPDFStandardButton());
    buttons.addButton(control.getPDFIndividuellButton());
    buttons.addButton(JVereinPlugin.getI18n().tr("neu"),
        new SpendenbescheinigungAction(), null, false, "document-new.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("speichern"), new Action()
    {

      public void handleAction(Object context)
      {
        control.handleStore();
      }
    }, null, true, "document-save.png");
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Spendenbescheinigung</span></p>"
        + "</form>";
  }
}
