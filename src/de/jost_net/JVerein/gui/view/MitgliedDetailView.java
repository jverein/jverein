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
 * Revision 1.29  2009/06/11 21:03:39  jost
 * Vorbereitung I18N
 *
 * Revision 1.28  2009/04/25 05:29:53  jost
 * Neu: Juristische Personen  können als Mitglied gespeichert werden.
 *
 * Revision 1.27  2009/04/13 11:40:14  jost
 * Neu: Lehrgänge
 *
 * Revision 1.26  2009/01/20 20:09:24  jost
 * neue Icons
 *
 * Revision 1.25  2009/01/20 19:15:19  jost
 * neu: Back-Button mit Icon
 *
 * Revision 1.24  2008/12/22 21:17:44  jost
 * Zusatzabbuchung->Zusatzbetrag
 *
 * Revision 1.23  2008/11/29 13:11:50  jost
 * Refactoring: Code-Optimierung
 *
 * Revision 1.22  2008/11/16 16:57:58  jost
 * Speicherung der Einstellung von Property-Datei in die Datenbank verschoben.
 *
 * Revision 1.21  2008/11/13 20:17:46  jost
 * Adressierungszusatz aufgenommen.
 *
 * Revision 1.20  2008/07/14 07:58:57  jost
 * Redakt. Ã„nderung
 *
 * Revision 1.19  2008/06/29 07:58:15  jost
 * Neu: Handy
 *
 * Revision 1.18  2008/05/24 14:04:08  jost
 * Redatkionelle Ã„nderung
 *
 * Revision 1.17  2008/05/05 18:22:28  jost
 * Bugfix NPE bei Zusatzfeldern
 *
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
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.MitgliedDeleteAction;
import de.jost_net.JVerein.gui.action.MitgliedDetailAction;
import de.jost_net.JVerein.gui.control.EigenschaftenControl;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.keys.Beitragsmodel;
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
import de.willuhn.jameica.gui.internal.buttons.Back;
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
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Daten des Mitgliedes"));

    final MitgliedControl control = new MitgliedControl(this);

    ScrolledContainer scrolled = new ScrolledContainer(getParent());

    ColumnLayout cols1 = new ColumnLayout(scrolled.getComposite(), 2);
    SimpleContainer left = new SimpleContainer(cols1.getComposite());
    // left.addHeadline(JVereinPlugin.getI18n().tr("Grunddaten"));
    left.addInput(control.getAnrede());
    if (control.getMitglied().getPersonenart().equals("n"))
    {
      left.addInput(control.getTitel());
    }
    if (control.getMitglied().getPersonenart().equals("j"))
    {
      control.getName().setName(JVereinPlugin.getI18n().tr("Name Zeile 1"));
      control.getVorname().setName(JVereinPlugin.getI18n().tr("Name Zeile 2"));
      control.getVorname().setMandatory(false);
    }
    left.addInput(control.getName());
    left.addInput(control.getVorname());
    left.addInput(control.getAdressierungszusatz());

    SimpleContainer right = new SimpleContainer(cols1.getComposite());
    // right.addHeadline("");
    right.addInput(control.getStrasse());
    right.addInput(control.getPlz());
    right.addInput(control.getOrt());
    if (control.getMitglied().getPersonenart().equals("n"))
    {
      right.addInput(control.getGeburtsdatum());
      right.addInput(control.getGeschlecht());
    }

    if (Einstellungen.getEinstellung().getKommunikationsdaten())
    {
      ColumnLayout cols2 = new ColumnLayout(scrolled.getComposite(), 2);
      SimpleContainer left2 = new SimpleContainer(cols2.getComposite());
      // left2.addHeadline(JVereinPlugin.getI18n().tr("Kommunikation"));
      left2.addInput(control.getTelefonprivat());
      left2.addInput(control.getHandy());
      SimpleContainer right2 = new SimpleContainer(cols2.getComposite());
      // right2.addHeadline("");
      right2.addInput(control.getTelefondienstlich());
      right2.addInput(control.getEmail());
    }

    TabFolder folder = new TabFolder(scrolled.getComposite(), SWT.NONE);
    folder.setLayoutData(new GridData(GridData.FILL_BOTH));
    folder.setBackground(Color.BACKGROUND.getSWTColor());

    TabGroup tab3 = new TabGroup(folder, JVereinPlugin.getI18n().tr(
        "Mitgliedschaft"));
    if (Einstellungen.getEinstellung().getExterneMitgliedsnummer())
    {
      tab3.addInput(control.getExterneMitgliedsnummer());
    }
    tab3.addInput(control.getEintritt());
    tab3.addInput(control.getBeitragsgruppe());
    tab3.addInput(control.getAustritt());
    tab3.addInput(control.getKuendigung());
    DBIterator it = Einstellungen.getDBService().createList(
        Beitragsgruppe.class);
    it.addFilter("beitragsart = 1 or beitragsart = 2");
    if (it.hasNext())
    {
      tab3.addPart(control.getFamilienverband());
    }

    TabGroup tab1 = new TabGroup(folder, JVereinPlugin.getI18n().tr("Zahlung"));
    tab1.addInput(control.getZahlungsweg());
    if (Einstellungen.getEinstellung().getBeitragsmodel() == Beitragsmodel.MONATLICH12631)
    {
      tab1.addInput(control.getZahlungsrhytmus());
    }
    tab1.addInput(control.getKontoinhaber());
    tab1.addInput(control.getBlz());
    tab1.addInput(control.getKonto());

    if (Einstellungen.getEinstellung().getZusatzbetrag())
    {
      TabGroup tab4 = new TabGroup(folder, JVereinPlugin.getI18n().tr(
          "Zusatzbeträge"));
      control.getZusatzbetraegeTable().paint(tab4.getComposite());
      ButtonArea buttonszus = new ButtonArea(tab4.getComposite(), 1);
      buttonszus.addButton(control.getZusatzbetragNeu());
    }

    if (Einstellungen.getEinstellung().getVermerke())
    {
      TabGroup tab5 = new TabGroup(folder, JVereinPlugin.getI18n().tr(
          "Vermerke"));
      tab5.addLabelPair(JVereinPlugin.getI18n().tr("Vermerk 1"), control
          .getVermerk1());
      tab5.addLabelPair(JVereinPlugin.getI18n().tr("Vermerk 2"), control
          .getVermerk2());
    }

    if (Einstellungen.getEinstellung().getWiedervorlage())
    {
      TabGroup tab6 = new TabGroup(folder, JVereinPlugin.getI18n().tr(
          "Wiedervorlage"));
      control.getWiedervorlageTable().paint(tab6.getComposite());
      ButtonArea buttonswvl = new ButtonArea(tab6.getComposite(), 1);
      buttonswvl.addButton(control.getWiedervorlageNeu());
    }
    if (!JVereinDBService.SETTINGS.getString("database.driver",
        DBSupportH2Impl.class.getName()).equals(
        DBSupportMcKoiImpl.class.getName()))
    {
      TabGroup tab7 = new TabGroup(folder, JVereinPlugin.getI18n().tr(
          "Eigenschaften"));
      EigenschaftenControl econtrol = new EigenschaftenControl(this,
          (Mitglied) control.getCurrentObject());
      econtrol.getEigenschaftenTable().paint(tab7.getComposite());
      tab7.addText(JVereinPlugin.getI18n().tr(
          "Rechter Mausklick für Funktionen"), false);
    }
    TextInput[] zusatzfelder = control.getZusatzfelder();
    if (zusatzfelder != null)
    {
      TabGroup tab8 = new TabGroup(folder, JVereinPlugin.getI18n().tr(
          "Zusatzfelder"));
      ScrolledContainer cont = new ScrolledContainer(tab8.getComposite());
      for (TextInput inp : zusatzfelder)
      {
        cont.addInput(inp);
      }
    }

    if (Einstellungen.getEinstellung().getLehrgaenge())
    {
      TabGroup tab9 = new TabGroup(folder, JVereinPlugin.getI18n().tr(
          "Lehrgänge"));
      control.getLehrgaengeTable().paint(tab9.getComposite());
      ButtonArea buttonslehrg = new ButtonArea(tab9.getComposite(), 1);
      buttonslehrg.addButton(control.getLehrgangNeu());
    }

    ButtonArea buttons = new ButtonArea(getParent(), 5);
    buttons.addButton(new Back(false));
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.MITGLIED, false,
        "help-browser.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("neu"),
        new MitgliedDetailAction(), null, false, "document-new.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("löschen"),
        new MitgliedDeleteAction(), control.getCurrentObject(), false,
        "user-trash.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("speichern"), new Action()
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
