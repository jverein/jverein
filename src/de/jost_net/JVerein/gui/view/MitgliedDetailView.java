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
 * Revision 1.16  2008/04/10 19:00:17  jost
 * Neu: Benutzerdefinierte Datenfelder
 *
 * Revision 1.15  2008/04/03 17:29:06  jost
 * ScrolledPane von Olaf Ã¼bernommen.
 *
 * Revision 1.14  2008/03/16 07:37:18  jost
 * Layout verÃ¤ndert.
 * Ausgabe der Familiendaten nur, wenn auch entsprechende Beitragsgruppen existieren.
 *
 * Revision 1.13  2008/03/08 19:29:58  jost
 * Neu: Externe Mitgliedsnummer
 *
 * Revision 1.12  2008/01/25 16:04:08  jost
 * Neu: Eigenschaften des Mitgliedes
 *
 * Revision 1.11  2008/01/01 19:52:33  jost
 * Erweiterung um Hilfe-Funktion
 *
 * Revision 1.10  2007/12/02 13:43:29  jost
 * Neu: Beitragsmodelle
 *
 * Revision 1.9  2007/08/22 20:44:35  jost
 * Bug #011762
 *
 * Revision 1.8  2007/05/07 19:26:01  jost
 * Neu: Wiedervorlage
 *
 * Revision 1.7  2007/03/27 19:23:24  jost
 * Familienangehörige anzeigen
 *
 * Revision 1.6  2007/03/25 17:01:14  jost
 * Beitragsart aufgenommen.
 *
 * Revision 1.5  2007/03/10 20:28:32  jost
 * Neu: Zahlungsweg
 *
 * Revision 1.4  2007/03/10 13:43:44  jost
 * Vermerke eingefÃ¼hrt.
 *
 * Revision 1.3  2007/02/23 20:27:42  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.2  2006/10/18 06:01:26  jost
 * Überflüssige Import-Statements entfernt.
 *
 * Revision 1.1  2006/09/20 15:39:10  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TabFolder;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.BackAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.MitgliedDeleteAction;
import de.jost_net.JVerein.gui.action.MitgliedDetailAction;
import de.jost_net.JVerein.gui.control.EigenschaftenControl;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.gui.input.BeitragsmodelInput;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.JVereinDBService;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.server.DBSupportH2Impl;
import de.jost_net.JVerein.server.DBSupportMcKoiImpl;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.Color;
import de.willuhn.jameica.gui.util.ColumnLayout;
import de.willuhn.jameica.gui.util.ScrolledContainer;
import de.willuhn.jameica.gui.util.SimpleContainer;
import de.willuhn.jameica.gui.util.TabGroup;
import de.willuhn.util.ApplicationException;

public class MitgliedDetailView extends AbstractView
{

  public void bind() throws Exception
  {
    GUI.getView().setTitle("Daten des Mitgliedes");

    final MitgliedControl control = new MitgliedControl(this);

    ScrolledContainer scrolled = new ScrolledContainer(getParent());

    ColumnLayout cols1 = new ColumnLayout(scrolled.getComposite(), 2);
    SimpleContainer left = new SimpleContainer(cols1.getComposite());
    left.addHeadline("Grunddaten");
    left.addLabelPair("Anrede", control.getAnrede());
    left.addLabelPair("Titel", control.getTitel());
    left.addLabelPair("Name", control.getName());
    left.addLabelPair("Vorname", control.getVorname());

    SimpleContainer right = new SimpleContainer(cols1.getComposite());
    right.addHeadline("");
    right.addLabelPair("Strasse", control.getStrasse());
    right.addLabelPair("PLZ", control.getPlz());
    right.addLabelPair("Ort", control.getOrt());
    right.addLabelPair("Geburtsdatum", control.getGeburtsdatum());
    right.addLabelPair("Geschlecht", control.getGeschlecht());

    if (Einstellungen.isKommunikationsdaten())
    {
      ColumnLayout cols2 = new ColumnLayout(scrolled.getComposite(), 2);
      SimpleContainer left2 = new SimpleContainer(cols2.getComposite());
      left2.addHeadline("Kommunikation");
      left2.addLabelPair("Telefon priv.", control.getTelefonprivat());
      SimpleContainer right2 = new SimpleContainer(cols2.getComposite());
      right2.addHeadline("");
      right2.addLabelPair("Telefon dienstl.", control.getTelefondienstlich());
      right2.addLabelPair("eMail", control.getEmail());
    }

    ColumnLayout cols3 = new ColumnLayout(scrolled.getComposite(), 2);
    SimpleContainer left3 = new SimpleContainer(cols3.getComposite());
    left3.addHeadline("Bankverbindung");
    left3.addLabelPair("Zahlungsweg", control.getZahlungsweg());
    if (Einstellungen.getBeitragsmodel() == BeitragsmodelInput.MONATLICH12631)
    {
      left3.addLabelPair("Zahlungsrhytmus", control.getZahlungsrhytmus());
    }
    left3.addLabelPair("Kontoinhaber", control.getKontoinhaber());
    SimpleContainer right3 = new SimpleContainer(cols3.getComposite());
    right3.addHeadline("");
    right3.addLabelPair("BLZ", control.getBlz());
    right3.addLabelPair("Konto", control.getKonto());

    TabFolder folder = new TabFolder(scrolled.getComposite(), SWT.NONE);
    folder.setLayoutData(new GridData(GridData.FILL_BOTH));
    folder.setBackground(Color.BACKGROUND.getSWTColor());

    TabGroup tab3 = new TabGroup(folder, "Mitgliedschaft");
    if (Einstellungen.isExterneMitgliedsnummer())
    {
      tab3.addLabelPair("Ext. Mitgliedsnummer", control
          .getExterneMitgliedsnummer());
    }
    tab3.addLabelPair("Eintritt", control.getEintritt());
    tab3.addLabelPair("Betragsgruppe", control.getBeitragsgruppe());
    tab3.addLabelPair("Austritt", control.getAustritt());
    tab3.addLabelPair("Kündigung", control.getKuendigung());
    DBIterator it = Einstellungen.getDBService().createList(
        Beitragsgruppe.class);
    it.addFilter("beitragsart = 1 or beitragsart = 2");
    if (it.hasNext())
    {
      tab3.addPart(control.getFamilienverband());
    }
    if (Einstellungen.isZusatzabbuchung())
    {
      TabGroup tab4 = new TabGroup(folder, "Zusatzabbuchung");
      control.getZusatzabbuchungenTable().paint(tab4.getComposite());
      ButtonArea buttonszus = new ButtonArea(tab4.getComposite(), 1);
      buttonszus.addButton(control.getZusatzabbuchungNeu());
    }

    if (Einstellungen.isVermerke())
    {
      TabGroup tab5 = new TabGroup(folder, "Vermerke");
      tab5.addLabelPair("Vermerk 1", control.getVermerk1());
      tab5.addLabelPair("Vermerk 2", control.getVermerk2());
    }

    if (Einstellungen.isWiedervorlage())
    {
      TabGroup tab6 = new TabGroup(folder, "Wiedervorlage");
      control.getWiedervorlageTable().paint(tab6.getComposite());
      ButtonArea buttonswvl = new ButtonArea(tab6.getComposite(), 1);
      buttonswvl.addButton(control.getWiedervorlageNeu());
    }
    if (!JVereinDBService.SETTINGS.getString("database.driver",
        DBSupportH2Impl.class.getName()).equals(
        DBSupportMcKoiImpl.class.getName()))
    {
      TabGroup tab7 = new TabGroup(folder, "Eigenschaften");
      EigenschaftenControl econtrol = new EigenschaftenControl(this,
          (Mitglied) control.getCurrentObject());
      econtrol.getEigenschaftenTable().paint(tab7.getComposite());
      tab7.addText("Rechter Mausklick für Funktionen", false);
    }
    TextInput[] zusatzfelder = control.getZusatzfelder();
    if (zusatzfelder != null)
    {
      TabGroup tab8 = new TabGroup(folder, "Zusatzfelder");
      ScrolledContainer cont = new ScrolledContainer(tab8.getComposite());
      for (TextInput inp : zusatzfelder)
      {
        cont.addInput(inp);
      }
    }

    ButtonArea buttons = new ButtonArea(getParent(), 5);
    buttons.addButton("<< Zurück", new BackAction());
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.mitglied);
    buttons.addButton("Neu", new MitgliedDetailAction());
    buttons.addButton("Löschen", new MitgliedDeleteAction(), control
        .getCurrentObject());
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
