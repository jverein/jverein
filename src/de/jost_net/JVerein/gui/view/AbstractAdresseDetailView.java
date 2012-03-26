/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * This program is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,  but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See 
 *  the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, 
 * see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de
 * www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TabFolder;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.AdresseDeleteAction;
import de.jost_net.JVerein.gui.action.AdresseDetailAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.KontoauszugAction;
import de.jost_net.JVerein.gui.action.MitgliedDeleteAction;
import de.jost_net.JVerein.gui.action.MitgliedDetailAction;
import de.jost_net.JVerein.gui.action.MitgliedMailSendenAction;
import de.jost_net.JVerein.gui.action.PersonalbogenAction;
import de.jost_net.JVerein.gui.control.DokumentControl;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.gui.control.MitgliedskontoControl;
import de.jost_net.JVerein.keys.Beitragsmodel;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.MitgliedDokument;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.Color;
import de.willuhn.jameica.gui.util.ColumnLayout;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.gui.util.ScrolledContainer;
import de.willuhn.jameica.gui.util.SimpleContainer;
import de.willuhn.jameica.gui.util.TabGroup;

public abstract class AbstractAdresseDetailView extends AbstractView
{

  private static int tabindex = -1;

  final MitgliedControl control = new MitgliedControl(this);

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr(getTitle()));

    final MitgliedskontoControl controlMk = new MitgliedskontoControl(this);

    ScrolledContainer scrolled = new ScrolledContainer(getParent());

    ColumnLayout cols1 = new ColumnLayout(scrolled.getComposite(), 2);
    SimpleContainer left = new SimpleContainer(cols1.getComposite());
    if (!isMitgliedDetail())
    {
      left.addInput(control.getAdresstyp());
    }
    left.addInput(control.getAnrede());
    if (control.getMitglied().getPersonenart().equals("n"))
    {
      left.addInput(control.getTitel());
    }
    if (control.getMitglied().getPersonenart().equals("j"))
    {
      control.getName(true).setName(JVereinPlugin.getI18n().tr("Name Zeile 1"));
      control.getVorname().setName(JVereinPlugin.getI18n().tr("Name Zeile 2"));
      control.getVorname().setMandatory(false);
    }
    left.addInput(control.getName(true));
    left.addInput(control.getVorname());
    left.addInput(control.getAdressierungszusatz());

    SimpleContainer right = new SimpleContainer(cols1.getComposite());
    right.addInput(control.getStrasse());
    right.addInput(control.getPlz());
    right.addInput(control.getOrt());
    if (Einstellungen.getEinstellung().getAuslandsadressen())
    {
      right.addInput(control.getStaat());
    }
    if (control.getMitglied().getPersonenart().equals("n"))
    {
      right.addInput(control.getGeburtsdatum());
      right.addInput(control.getGeschlecht());
    }

    if (Einstellungen.getEinstellung().getKommunikationsdaten())
    {
      ColumnLayout cols2 = new ColumnLayout(scrolled.getComposite(), 2);
      SimpleContainer left2 = new SimpleContainer(cols2.getComposite());
      left2.addInput(control.getTelefonprivat());
      left2.addInput(control.getHandy());
      SimpleContainer right2 = new SimpleContainer(cols2.getComposite());
      right2.addInput(control.getTelefondienstlich());
      right2.addInput(control.getEmail());
    }

    final TabFolder folder = new TabFolder(scrolled.getComposite(), SWT.NONE);
    folder.setLayoutData(new GridData(GridData.FILL_BOTH));
    folder.setBackground(Color.BACKGROUND.getSWTColor());

    if (isMitgliedDetail())
    {
      TabGroup tab3 = new TabGroup(folder, JVereinPlugin.getI18n().tr(
          "Mitgliedschaft"));
      if (Einstellungen.getEinstellung().getExterneMitgliedsnummer())
      {
        tab3.addInput(control.getExterneMitgliedsnummer());
      }
      else
      {
        tab3.addInput(control.getMitgliedsnummer());
      }
      tab3.addInput(control.getEintritt());
      tab3.addInput(control.getBeitragsgruppe(true));
      if (Einstellungen.getEinstellung().getIndividuelleBeitraege())
      {
        tab3.addInput(control.getIndividuellerBeitrag());
      }
      tab3.addInput(control.getAustritt());
      tab3.addInput(control.getKuendigung());
      if (Einstellungen.getEinstellung().getSterbedatum()
          && control.getMitglied().getPersonenart().equals("n"))
      {
        tab3.addInput(control.getSterbetag());
      }
      DBIterator it = Einstellungen.getDBService().createList(
          Beitragsgruppe.class);
      it.addFilter("beitragsart = 1 or beitragsart = 2");
      if (it.hasNext())
      {
        tab3.addPart(control.getFamilienverband());
      }
    }
    TabGroup tab1 = new TabGroup(folder, JVereinPlugin.getI18n().tr("Zahlung"));
    if (isMitgliedDetail())
    {
      tab1.addInput(control.getZahlungsweg());
      if (Einstellungen.getEinstellung().getBeitragsmodel() == Beitragsmodel.MONATLICH12631)
      {
        tab1.addInput(control.getZahlungsrhytmus());
      }
    }
    tab1.addInput(control.getKontoinhaber());
    tab1.addInput(control.getBlz());
    tab1.addInput(control.getKonto());
    tab1.addInput(control.getIban());

    if (isMitgliedDetail() && Einstellungen.getEinstellung().getZusatzbetrag())
    {
      TabGroup tab4 = new TabGroup(folder, JVereinPlugin.getI18n().tr(
          "Zusatzbeträge"));
      control.getZusatzbetraegeTable().paint(tab4.getComposite());
      ButtonArea buttonszus = new ButtonArea();
      buttonszus.addButton(control.getZusatzbetragNeu());
      buttonszus.paint(tab4.getComposite());
    }
    if (!control.getMitglied().isNewObject()
        && Einstellungen.getEinstellung().getMitgliedskonto())
    {
      TabGroup tabMitgliedskonto = new TabGroup(folder, JVereinPlugin.getI18n()
          .tr("Mitgliedskonto"));
      controlMk.getMitgliedskontoTree(control.getMitglied()).paint(
          tabMitgliedskonto.getComposite());
    }
    if (Einstellungen.getEinstellung().getVermerke())
    {
      TabGroup tab5 = new TabGroup(folder, JVereinPlugin.getI18n().tr(
          "Vermerke"));
      tab5.addLabelPair(JVereinPlugin.getI18n().tr("Vermerk 1"),
          control.getVermerk1());
      tab5.addLabelPair(JVereinPlugin.getI18n().tr("Vermerk 2"),
          control.getVermerk2());
    }

    if (Einstellungen.getEinstellung().getWiedervorlage())
    {
      TabGroup tab6 = new TabGroup(folder, JVereinPlugin.getI18n().tr(
          "Wiedervorlage"));
      control.getWiedervorlageTable().paint(tab6.getComposite());
      ButtonArea buttonswvl = new ButtonArea();
      buttonswvl.addButton(control.getWiedervorlageNeu());
      buttonswvl.paint(tab6.getComposite());
    }
    if (isMitgliedDetail())
    {
      TabGroup tab7 = new TabGroup(folder, JVereinPlugin.getI18n().tr(
          "Eigenschaften"));
      SimpleContainer sc = new SimpleContainer(tab7.getComposite(), true);
      ScrolledContainer scrolledEigenschaften = new ScrolledContainer(
          sc.getComposite());
      scrolledEigenschaften.getComposite().setSize(300, 200);
      control.getEigenschaftenTree()
          .paint(scrolledEigenschaften.getComposite());
    }
    Input[] zusatzfelder = control.getZusatzfelder();
    if (zusatzfelder != null)
    {
      TabGroup tab8 = new TabGroup(folder, JVereinPlugin.getI18n().tr(
          "Zusatzfelder"));
      ScrolledContainer cont = new ScrolledContainer(tab8.getComposite());
      for (Input inp : zusatzfelder)
      {
        cont.addInput(inp);
      }
    }

    if (isMitgliedDetail() && Einstellungen.getEinstellung().getLehrgaenge())
    {
      TabGroup tab9 = new TabGroup(folder, JVereinPlugin.getI18n().tr(
          "Lehrgänge"));
      control.getLehrgaengeTable().paint(tab9.getComposite());
      ButtonArea buttonslehrg = new ButtonArea();
      buttonslehrg.addButton(control.getLehrgangNeu());
      buttonslehrg.paint(tab9.getComposite());
    }
    if (isMitgliedDetail() && Einstellungen.getEinstellung().getMitgliedfoto())
    {
      TabGroup tab10 = new TabGroup(folder, JVereinPlugin.getI18n().tr("Foto"));
      tab10.addLabelPair("Foto", control.getFoto());
    }
    if (isMitgliedDetail()
        && Einstellungen.getEinstellung().getArbeitseinsatz())
    {
      TabGroup tabArbEins = new TabGroup(folder, JVereinPlugin.getI18n().tr(
          "Arbeitseinsatz"));
      control.getArbeitseinsatzTable().paint(tabArbEins.getComposite());
      ButtonArea buttonsarbeins = new ButtonArea();
      buttonsarbeins.addButton(control.getArbeitseinsatzNeu());
      buttonsarbeins.paint(tabArbEins.getComposite());
    }
    if (JVereinPlugin.isArchiveServiceActive()
        && !control.getMitglied().isNewObject())
    {
      TabGroup tabDokument = new TabGroup(folder, JVereinPlugin.getI18n().tr(
          "Dokumente"));
      LabelGroup grDokument = new LabelGroup(tabDokument.getComposite(),
          "Dokumente");
      MitgliedDokument mido = (MitgliedDokument) Einstellungen.getDBService()
          .createObject(MitgliedDokument.class, null);
      mido.setReferenz(new Integer(control.getMitglied().getID()));
      DokumentControl dcontrol = new DokumentControl(this, "mitglieder", true);
      grDokument.addPart(dcontrol.getDokumenteList(mido));
      ButtonArea butts = new ButtonArea();
      butts.addButton(dcontrol.getNeuButton(mido));
      butts.paint(grDokument.getComposite());
    }

    if (tabindex != -1)
    {
      folder.setSelection(tabindex);
    }
    folder.addSelectionListener(new SelectionListener()
    {

      public void widgetSelected(SelectionEvent arg0)
      {
        tabindex = folder.getSelectionIndex();
      }

      public void widgetDefaultSelected(SelectionEvent arg0)
      {
        //
      }
    });

    ButtonArea buttons = new ButtonArea();
    if (!control.getMitglied().isNewObject()
        && Einstellungen.getEinstellung().getMitgliedskonto())
    {
      MitgliedskontoControl mkcontrol = new MitgliedskontoControl(this);
      mkcontrol.getStartKontoauszugButton(control.getMitglied());
      buttons.addButton(new Button("Kontoauszug", new KontoauszugAction(),
          control.getMitglied(), false, "rechnung.png"));
    }
    if (isMitgliedDetail())
    {
      buttons.addButton(new Button("Personalbogen", new PersonalbogenAction(),
          control.getCurrentObject(), false, "rechnung.png"));
    }
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.MITGLIED, false,
        "help-browser.png");
    buttons.addButton("Mail", new MitgliedMailSendenAction(),
        getCurrentObject(), false, "mail-message-new.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("neu"),
        isMitgliedDetail() ? new MitgliedDetailAction()
            : new AdresseDetailAction(), null, false, "document-new.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("löschen"),
        (isMitgliedDetail() ? new MitgliedDeleteAction()
            : new AdresseDeleteAction()), control.getCurrentObject(), false,
        "user-trash.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("speichern"), new Action()
    {

      public void handleAction(Object context)
      {
        control.handleStore();
      }
    }, null, true, "document-save.png");
    buttons.paint(getParent());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Mitglied</span></p>"
        + "<li>Anrede: Herrn, Frau ...</li>"
        + "<li>Titel: Dr., Prof. Dr., ...</li>"
        + "<li>Sofern Auslandsadressen erfasst werden sollen, ist das unter Einstellungen anzuhaken. Dann kann auch der Wohnungsstaat eingegeben werden.</li>"
        + "<li>Adressierungszusatz: z. B. bei Lieschen Müller</li>"
        + "<li>Kontoinhaber: Falls das Mitglied nicht Kontoinhaber ist, können die entsprechenden Daten eingegeben werden.</li>"
        + "<li>Austritt: Das laut Satzung gültige Austrittsdatum.</li>"
        + "<li>Kündigung: Eingangsdatum der Kündigung.</li>"
        + "<li>Zusatzabbuchung: Sind für das Mitglied zusätzlich zum Jahresbeitrag weitere Beträge abzubuchen, können die entsprechenden Eingaben gemacht werden.</li>"
        + "</form>";
  }

  public abstract String getTitle();

  public abstract boolean isMitgliedDetail();
}
