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
 * Revision 1.22  2011-05-11 15:51:00  jost
 * Speicherung Auswertungskriterien und Listenüberschrift
 *
 * Revision 1.21  2011-04-17 06:38:50  jost
 * Neu: Mitglieder-Selektion nach Zusatzfeldern
 *
 * Revision 1.20  2011-04-07 19:28:47  jost
 * Neue Zurückbutton-Mimik aus Jameica
 *
 * Revision 1.19  2011-02-02 22:00:10  jost
 * Auswertung erweitert um den Parameter "ohne EMail"
 *
 * Revision 1.18  2011-01-15 09:46:49  jost
 * Tastatursteuerung wegen Problemen mit Jameica/Hibiscus wieder entfernt.
 *
 * Revision 1.17  2010-11-21 21:09:39  jost
 * Mitgliedsstatus in die Auswertung aufgenommen.
 *
 * Revision 1.16  2010-10-30 11:29:54  jost
 * Neu: Sterbetag
 *
 * Revision 1.15  2010-10-15 09:58:24  jost
 * Code aufgeräumt
 *
 * Revision 1.14  2010-10-07 19:49:24  jost
 * Hilfe in die View verlagert.
 *
 * Revision 1.13  2010-08-23 13:39:33  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.12  2009/11/19 19:44:28  jost
 * Bugfix Eigenschaften
 *
 * Revision 1.11  2009/11/17 20:59:26  jost
 * Neu: Eigenschaft und EigenschaftGruppe
 *
 * Revision 1.10  2009/06/11 21:03:39  jost
 * Vorbereitung I18N
 *
 * Revision 1.9  2009/03/26 21:02:08  jost
 * Neu: Reports - Erste Version
 *
 * Revision 1.8  2009/01/20 20:09:24  jost
 * neue Icons
 *
 * Revision 1.7  2009/01/20 19:14:35  jost
 * neu: Back-Button mit Icon
 *
 * Revision 1.6  2008/11/11 20:47:48  jost
 * 2spaltiges Layout und Selektion nach Geschlecht
 *
 * Revision 1.5  2008/05/24 14:04:08  jost
 * Redatkionelle Ã„nderung
 *
 * Revision 1.4  2008/01/27 09:42:22  jost
 * Erweiterung der Auswertung um Eigenschaften
 *
 * Revision 1.3  2008/01/01 19:48:12  jost
 * Erweiterung um Hilfe-Funktion
 *
 * Revision 1.2  2007/02/23 20:27:28  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.1  2006/09/20 15:39:10  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.rmi.JVereinDBService;
import de.jost_net.JVerein.server.DBSupportH2Impl;
import de.jost_net.JVerein.server.DBSupportMcKoiImpl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.ColumnLayout;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.gui.util.SimpleContainer;

public class AuswertungMitgliedView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(
        JVereinPlugin.getI18n().tr("Auswertung Mitgliedsdaten"));

    final MitgliedControl control = new MitgliedControl(this);

    LabelGroup group = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Filter"));

    ColumnLayout cl = new ColumnLayout(group.getComposite(), 2);
    SimpleContainer left = new SimpleContainer(cl.getComposite());

    Input mitglstat = control.getMitgliedStatus();
    left.addInput(mitglstat);

    if (!JVereinDBService.SETTINGS.getString("database.driver",
        DBSupportH2Impl.class.getName()).equals(
        DBSupportMcKoiImpl.class.getName()))
    {
      left.addInput(control.getEigenschaftenAuswahl());
    }
    if (Einstellungen.getEinstellung().hasZusatzfelder())
    {
      left.addInput(control.getZusatzfelderAuswahl());
    }
    left.addInput(control.getGeburtsdatumvon());
    left.addInput(control.getGeburtsdatumbis());

    left.addInput(control.getSterbedatumvon());
    left.addInput(control.getSterbedatumbis());

    SelectInput inpGeschlecht = control.getGeschlecht();
    inpGeschlecht.setMandatory(false);
    left.addInput(inpGeschlecht);

    left.addInput(control.getOhneMail());

    SimpleContainer right = new SimpleContainer(cl.getComposite());
    right.addInput(control.getEintrittvon());
    right.addInput(control.getEintrittbis());

    right.addInput(control.getAustrittvon());
    right.addInput(control.getAustrittbis());

    right.addInput(control.getBeitragsgruppeAusw());

    right.addInput(control.getAusgabe());
    right.addInput(control.getSortierung());
    right.addInput(control.getAuswertungUeberschrift());

    LabelGroup gr = new LabelGroup(getParent(), "Einstellungen");
    gr.addLabelPair("Name", control.getAuswertungName());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.AUSWERTUNGMITGLIEDER,
        false, "help-browser.png");
    buttons.addButton(control.getAuswertungDialogButton());
    buttons.addButton(control.getDefaultValuesButton());
    buttons.addButton(control.getStartAuswertungButton());
    buttons.paint(getParent());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Auswertung Mitglieder</span></p>"
        + "<p>Der Mitgliederbestand kann nach Geburtsdatum, Eintrittsdatum, "
        + "Austrittsdatum und Beitragsgruppen selektiert werden. Werden "
        + "keine Angaben zum Austrittsdatum gemacht, werden nur Mitglieder "
        + "ausgewertet, die nicht ausgetreten sind.</p>"
        + "<p>Die Sortierung kann nach Name-Vorname, Eintrittsdatum oder "
        + "Austrittsdatum erfolgen.</p>"
        + "<p>Als Ausgabeformate stehen PDF und CSV zur Verfügung.</p>"
        + "</form>";
  }
}
