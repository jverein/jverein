/**********************************************************************
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

import java.rmi.RemoteException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.AdresseDeleteAction;
import de.jost_net.JVerein.gui.action.AdresseDetailAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.KontoauszugAction;
import de.jost_net.JVerein.gui.action.MitgliedDeleteAction;
import de.jost_net.JVerein.gui.action.MitgliedDetailAction;
import de.jost_net.JVerein.gui.action.MitgliedDuplizierenAction;
import de.jost_net.JVerein.gui.action.MitgliedMailSendenAction;
import de.jost_net.JVerein.gui.action.PersonalbogenAction;
import de.jost_net.JVerein.gui.control.DokumentControl;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.gui.control.MitgliedskontoControl;
import de.jost_net.JVerein.gui.util.SimpleVerticalContainer;
import de.jost_net.JVerein.keys.ArtBeitragsart;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.MitgliedDokument;
import de.jost_net.JVerein.server.MitgliedUtils;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.Color;
import de.willuhn.jameica.gui.util.Container;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.gui.util.ScrolledContainer;
import de.willuhn.jameica.gui.util.SimpleContainer;
import de.willuhn.jameica.gui.util.TabGroup;
import de.willuhn.logging.Logger;

public abstract class AbstractAdresseDetailView extends AbstractView
{

  // Statische Variable, die den zuletzt ausgewählten Tab speichert.
  private static int tabindex = -1;

  // Die aufgerufene Funktion: B=Bearbeiten, N=Neu, D=Duplizieren
  int funktion = 'B';

  final MitgliedControl control = new MitgliedControl(this);

  @Override
  public void bind() throws Exception
  {
    // Funktion ermitteln
    if (control.getMitgliedsnummer().getValue() == null)
    {
      if (control.getName(false).getValue() == null)
        funktion = 'N';
      else
        funktion = 'D';
    }

    zeichneUeberschrift(); // Einschub Ende

    final MitgliedskontoControl controlMk = new MitgliedskontoControl(this);

    ScrolledContainer scrolled = new ScrolledContainer(getParent(), 1);

    SimpleContainer oben = new SimpleContainer(scrolled.getComposite(), false,
        1);

    final TabFolder folder = new TabFolder(scrolled.getComposite(), SWT.NONE);
    folder.setLayoutData(new GridData(GridData.FILL_BOTH));
    folder.setBackground(Color.BACKGROUND.getSWTColor());

    DBIterator<Mitglied> zhl = Einstellungen.getDBService()
        .createList(Mitglied.class);
    MitgliedUtils.setNurAktive(zhl);
    MitgliedUtils.setMitglied(zhl);
    zhl.setOrder("ORDER BY name, vorname");

    int anzahlSpalten = Einstellungen.getEinstellung()
        .getAnzahlSpaltenStammdaten();
    boolean showInTab = Einstellungen.getEinstellung()
        .getZeigeStammdatenInTab();
    zeicheStammdaten(showInTab ? folder : oben.getComposite(), anzahlSpalten);

    anzahlSpalten = Einstellungen.getEinstellung()
        .getAnzahlSpaltenMitgliedschaft();
    showInTab = Einstellungen.getEinstellung().getZeigeMitgliedschaftInTab();
    zeichneMitgliedschaft(showInTab ? folder : oben.getComposite(),
        anzahlSpalten);

    anzahlSpalten = Einstellungen.getEinstellung().getAnzahlSpaltenZahlung();
    showInTab = Einstellungen.getEinstellung().getZeigeZahlungInTab();
    zeichneZahlung(showInTab ? folder : oben.getComposite(), anzahlSpalten);

    showInTab = Einstellungen.getEinstellung().getZeigeZusatzbetraegeInTab();
    zeichneZusatzbeitraege(showInTab ? folder : oben.getComposite());

    showInTab = Einstellungen.getEinstellung().getZeigeMitgliedskontoInTab();
    zeichneMitgliedkonto(controlMk, showInTab ? folder : oben.getComposite());

    showInTab = Einstellungen.getEinstellung().getZeigeVermerkeInTab();
    zeichneVermerke(showInTab ? folder : oben.getComposite(), 1);

    showInTab = Einstellungen.getEinstellung().getZeigeWiedervorlageInTab();
    zeichneWiedervorlage(showInTab ? folder : oben.getComposite());

    showInTab = Einstellungen.getEinstellung().getZeigeMailsInTab();
    zeichneMails(showInTab ? folder : oben.getComposite());

    showInTab = Einstellungen.getEinstellung().getZeigeEigenschaftenInTab();
    zeichneEigenschaften(showInTab ? folder : oben.getComposite());

    anzahlSpalten = Einstellungen.getEinstellung()
        .getAnzahlSpaltenZusatzfelder();
    showInTab = Einstellungen.getEinstellung().getZeigeZusatzfelderInTab();
    zeichneZusatzfelder(showInTab ? folder : oben.getComposite(),
        anzahlSpalten);

    showInTab = Einstellungen.getEinstellung().getZeigeLehrgaengeInTab();
    zeichneLehrgaenge(showInTab ? folder : oben.getComposite());

    showInTab = Einstellungen.getEinstellung().getZeigeFotoInTab();
    zeichneMitgliedfoto(showInTab ? folder : oben.getComposite());

    anzahlSpalten = Einstellungen.getEinstellung().getAnzahlSpaltenLesefelder();
    showInTab = Einstellungen.getEinstellung().getZeigeLesefelderInTab();
    zeichneLesefelder(showInTab ? folder : oben.getComposite(), anzahlSpalten);

    zeichneMitgliedDetail(showInTab ? folder : oben.getComposite());

    zeichneDokumente(showInTab ? folder : oben.getComposite());

    // Aktivier zuletzt ausgewählten Tab.
    if (tabindex != -1)
    {
      folder.setSelection(tabindex);
    }
    folder.addSelectionListener(new SelectionListener()
    {

      // Wenn Tab angeklickt, speicher diesen um ihn später automatisch
      // wieder auszuwählen.
      @Override
      public void widgetSelected(SelectionEvent arg0)
      {
        tabindex = folder.getSelectionIndex();
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent arg0)
      {
        //
      }
    });

    zeichneButtonArea(getParent());

  }

  private void zeichneButtonArea(Composite parent) throws RemoteException
  {
    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.MITGLIED, false, "question-circle.png");
    if (!control.getMitglied().isNewObject())
    {
      MitgliedskontoControl mkcontrol = new MitgliedskontoControl(this);
      mkcontrol.getStartKontoauszugButton(control.getMitglied(), null, null);
      buttons.addButton(new Button("Kontoauszug", new KontoauszugAction(),
          control.getMitglied(), false, "file-invoice.png"));
    }
    if (isMitgliedDetail())
    {
      buttons.addButton(new Button("Personalbogen", new PersonalbogenAction(),
          control.getCurrentObject(), false, "receipt.png"));
      // R.M. 27.01.2013 Mitglieder sollten aus dem Dialog raus kopiert werden
      // können
      buttons
          .addButton(new Button("duplizieren", new MitgliedDuplizierenAction(),
              control.getCurrentObject(), false, "copy.png"));
    }
    buttons.addButton("Mail", new MitgliedMailSendenAction(),
        getCurrentObject(), false, "envelope-open.png");
    // buttons.addButton("neue Mail", new MailDetailAction(),
    // control.getCurrentObject(), false, "document-new.png");
    buttons.addButton("neu", (isMitgliedDetail() ? new MitgliedDetailAction()
        : new AdresseDetailAction()), null, false, "file.png");

    buttons.addButton("löschen",
        (isMitgliedDetail() ? new MitgliedDeleteAction()
            : new AdresseDeleteAction()),
        control.getCurrentObject(), false, "trash-alt.png");
    buttons.addButton("speichern", new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        control.handleStore();
        try
        {
          zeichneUeberschrift();
        }
        catch (RemoteException e)
        {
          Logger.error("Fehler", e);
        }
      }
    }, null, true, "save.png");
    buttons.paint(parent);
  }

  private void zeichneDokumente(Composite parentComposite)
      throws RemoteException
  {
    if (JVereinPlugin.isArchiveServiceActive()
        && !control.getMitglied().isNewObject())
    {
      Container cont = getTabOrLabelContainer(parentComposite, "Dokumente");

      MitgliedDokument mido = (MitgliedDokument) Einstellungen.getDBService()
          .createObject(MitgliedDokument.class, null);
      mido.setReferenz(new Long(control.getMitglied().getID()));
      DokumentControl dcontrol = new DokumentControl(this, "mitglieder", true);
      cont.addPart(dcontrol.getDokumenteList(mido));
      ButtonArea butts = new ButtonArea();
      butts.addButton(dcontrol.getNeuButton(mido));
      butts.paint(cont.getComposite());
    }
  }

  private void zeichneMitgliedDetail(Composite parentComposite)
      throws RemoteException
  {
    if (isMitgliedDetail()
        && Einstellungen.getEinstellung().getArbeitseinsatz())
    {
      Container cont = getTabOrLabelContainer(parentComposite,
          "Arbeitseinsatz");

      ButtonArea buttonsarbeins = new ButtonArea();
      buttonsarbeins.addButton(control.getArbeitseinsatzNeu());
      buttonsarbeins.paint(cont.getComposite());

      cont.getComposite().setLayoutData(new GridData(GridData.FILL_VERTICAL));
      cont.getComposite().setLayout(new GridLayout(1, false));

      control.getArbeitseinsatzTable().paint(cont.getComposite());
    }
  }

  private void zeichneLesefelder(Composite parentComposite, int spaltenanzahl)
      throws RemoteException
  {
    // TODO: getLesefelder() ist zu langsam. Inhalt von Lesefeldern sollte erst
    // evaluiert werden, wenn Lesefelder-Tab angeklickt wird.
    if (Einstellungen.getEinstellung().getUseLesefelder())
    {
      Input[] lesefelder = control.getLesefelder();
      if (lesefelder != null)
      {
        Container cont = getTabOrLabelContainer(parentComposite, "Lesefelder");
        SimpleVerticalContainer svc = new SimpleVerticalContainer(
            cont.getComposite(), false, spaltenanzahl);
        for (Input inp : lesefelder)
        {
          if (inp == null)
          {
            String errorText = "Achtung! Ungültiges Lesefeld-Skript gefunden. Diesen Fehler bitte unter www.jverein.de/forum melden!";
            Input errorInput = new TextInput(errorText);
            errorInput.setEnabled(false);
            svc.addInput(errorInput);
            GUI.getStatusBar().setErrorText(errorText);
          }
          else
          {
            svc.addInput(inp);
          }
        }
        svc.arrangeVertically();
        ButtonArea buttonszus = new ButtonArea();
        buttonszus.addButton(control.getLesefelderEdit());
        cont.addButtonArea(buttonszus);
      }
    }
  }

  private void zeichneMitgliedfoto(Composite parentComposite)
      throws RemoteException
  {
    if (isMitgliedDetail() && Einstellungen.getEinstellung().getMitgliedfoto())
    {
      Container cont = getTabOrLabelContainer(parentComposite, "Foto");
      cont.addLabelPair("Foto", control.getFoto());
    }
  }

  private void zeichneLehrgaenge(Composite parentComposite)
      throws RemoteException
  {
    if (isMitgliedDetail() && Einstellungen.getEinstellung().getLehrgaenge())
    {
      Container cont = getTabOrLabelContainer(parentComposite, "Lehrgänge");

      cont.getComposite().setLayoutData(new GridData(GridData.FILL_VERTICAL));
      cont.getComposite().setLayout(new GridLayout(1, false));

      ButtonArea buttonslehrg = new ButtonArea();
      buttonslehrg.addButton(control.getLehrgangNeu());
      buttonslehrg.paint(cont.getComposite());
      control.getLehrgaengeTable().paint(cont.getComposite());
    }
  }

  private void zeichneZusatzfelder(Composite parentComposite, int spaltenanzahl)
      throws RemoteException
  {
    Input[] zusatzfelder = control.getZusatzfelder();
    if (zusatzfelder != null)
    {
      Container cont = getTabOrLabelContainer(parentComposite, "Zusatzfelder");
      SimpleVerticalContainer svc = new SimpleVerticalContainer(
          cont.getComposite(), true, spaltenanzahl);
      for (Input inp : zusatzfelder)
      {
        svc.addInput(inp);
      }
      svc.arrangeVertically();
    }
  }

  private void zeichneEigenschaften(Composite parentComposite)
      throws RemoteException
  {
    // if (isMitgliedDetail())
    // {
    Container cont = getTabOrLabelContainer(parentComposite, "Eigenschaften");
    cont.getComposite().setLayout(new GridLayout(1, true));
    control.getEigenschaftenTree().paint(cont.getComposite());
    // }
  }

  private void zeichneWiedervorlage(Composite parentComposite)
      throws RemoteException
  {
    if (Einstellungen.getEinstellung().getWiedervorlage())
    {
      Container cont = getTabOrLabelContainer(parentComposite, "Wiedervorlage");

      cont.getComposite().setLayoutData(new GridData(GridData.FILL_VERTICAL));
      cont.getComposite().setLayout(new GridLayout(1, false));

      ButtonArea buttonswvl = new ButtonArea();
      buttonswvl.addButton(control.getWiedervorlageNeu());
      buttonswvl.paint(cont.getComposite());
      control.getWiedervorlageTable().paint(cont.getComposite());
    }
  }

  private void zeichneMails(Composite parentComposite) throws RemoteException
  {
    if (Einstellungen.getEinstellung().getSmtpServer() != null
        && Einstellungen.getEinstellung().getSmtpServer().length() > 0)
    {
      Container cont = getTabOrLabelContainer(parentComposite, "Mails");

      control.getMailTable().paint(cont.getComposite());
    }
  }

  private void zeichneVermerke(Composite parentComposite, int spaltenanzahl)
      throws RemoteException
  {
    if (Einstellungen.getEinstellung().getVermerke())
    {
      Container cont = getTabOrLabelContainer(parentComposite, "Vermerke");
      SimpleContainer cols = new SimpleContainer(cont.getComposite(), true,
          spaltenanzahl * 2);

      // Stelle sicher, dass Eingabefeld sich über mehrere Zeilen erstreckt.
      GridData gridData = new GridData(GridData.FILL_BOTH);
      gridData.minimumHeight = 80;
      // wenn der Vermerk mehr Zeilen benötigt, sollte er die Scrollbar
      // einblenden.
      gridData.heightHint = 80;
      cols.getComposite().setLayoutData(gridData);

      cols.addLabelPair("Vermerk 1", control.getVermerk1());
      cols.addLabelPair("Vermerk 2", control.getVermerk2());
    }
  }

  /**
   * Zeichnet das Mitgliedskonto, wenn dieses aktiviert ist und es sich nicht um
   * ein neues Mitglied handelt (für dieses macht ein Mitgliedskonto noch kein
   * Sinn!)
   * 
   */
  private void zeichneMitgliedkonto(MitgliedskontoControl controlMk,
      Composite parentComposite) throws RemoteException
  {
    if (!control.getMitglied().isNewObject())
    {
      Container cont = getTabOrLabelContainer(parentComposite,
          "Mitgliedskonto");
      controlMk.getMitgliedskontoTree(control.getMitglied())
          .paint(cont.getComposite());
    }
  }

  private void zeichneZusatzbeitraege(Composite parentComposite)
      throws RemoteException
  {
    if (Einstellungen.getEinstellung().getZusatzbetrag())
    {
      Container cont = getTabOrLabelContainer(parentComposite, "Zusatzbeträge");

      cont.getComposite().setLayoutData(new GridData(GridData.FILL_VERTICAL));
      cont.getComposite().setLayout(new GridLayout(1, false));

      ButtonArea buttonszus = new ButtonArea();
      buttonszus.addButton(control.getZusatzbetragNeu());
      buttonszus.paint(cont.getComposite());
      control.getZusatzbetraegeTable().paint(cont.getComposite());
    }
  }

  private void zeichneZahlung(Composite parentComposite, int spaltenanzahl)
      throws RemoteException
  {
    Container container = getTabOrLabelContainer(parentComposite, "Zahlung");
    GridLayout layout = new GridLayout(1, false);
    container.getComposite().setLayout(layout);

    LabelGroup zahlungsweg = new LabelGroup(container.getComposite(),
        "Zahlungsweg");

    zahlungsweg.addInput(control.getZahlungsweg());
    if (isMitgliedDetail())
    {
      switch (Einstellungen.getEinstellung().getBeitragsmodel())
      {
        case GLEICHERTERMINFUERALLE:
          break;
        case MONATLICH12631:
          zahlungsweg.addInput(control.getZahlungsrhythmus());
          break;
        case FLEXIBEL:
          zahlungsweg.addInput(control.getZahlungstermin());
          break;
      }
    }

    LabelGroup bankverbindung = control
        .getBankverbindungLabelGroup(container.getComposite());
    // bankverbindung
    // .getComposite()
    // .setVisible(
    // ((Zahlungsweg) control.getZahlungsweg().getValue()).getKey() ==
    // Zahlungsweg.BASISLASTSCHRIFT);

    SimpleVerticalContainer cols = new SimpleVerticalContainer(
        bankverbindung.getComposite(), true, spaltenanzahl);

    cols.addInput(control.getMandatID());
    cols.addInput(control.getMandatDatum());
    cols.addInput(control.getMandatVersion());
    cols.addInput(control.getLetzteLastschrift());
    cols.addInput(control.getIban());
    cols.addInput(control.getBic());
    cols.addSeparator();
    cols.addText("Abweichender Kontoinhaber", false);
    ButtonArea buttons2 = new ButtonArea();
    buttons2.addButton(control.getMitglied2KontoinhaberEintragenButton());
    cols.addButtonArea(buttons2);
    cols.addInput(control.getKtoiPersonenart());
    cols.addInput(control.getKtoiAnrede());
    cols.addInput(control.getKtoiTitel());
    cols.addInput(control.getKtoiName());
    cols.addInput(control.getKtoiVorname());
    cols.addInput(control.getKtoiStrasse());
    cols.addInput(control.getKtoiAdressierungszusatz());
    cols.addInput(control.getKtoiPlz());
    cols.addInput(control.getKtoiOrt());
    if (Einstellungen.getEinstellung().getAuslandsadressen())
    {
      cols.addInput(control.getKtoiStaat());
    }
    cols.addInput(control.getKtoiEmail());
    cols.addInput(control.getKtoiGeschlecht());
    // cols.addInput(control.getBlz());
    // cols.addInput(control.getKonto());
    cols.arrangeVertically();
  }

  /**
   * Erzeugt einen Container, der in einem TabFolder oder einer LabelGroup
   * eingebettet ist. Ist parentComposite ein TabFolder wird SimpleContainer in
   * eine TabGroup eingebettet, anderenfalls in eine LabelGroup.
   * 
   * @param parentComposite
   *          Parent composite in das TabGroup bzw. LabelGroup und
   *          SimpleContainer gezeichnet wird.
   * @param titel
   *          Beschriftung von TabGroup bzw. LabelGroup
   * @return SimpleContainer, in den Inhalt gezeichnet werden kann.
   */
  private Container getTabOrLabelContainer(Composite parentComposite,
      String titel)
  {
    Container container;
    if (parentComposite instanceof TabFolder)
    {
      container = new TabGroup((TabFolder) parentComposite, titel);
    }
    else
    {
      container = new LabelGroup(parentComposite, titel);
    }
    return container;
  }

  private void zeichneMitgliedschaft(Composite parentComposite,
      int spaltenanzahl) throws RemoteException
  {
    if (isMitgliedDetail())
    {
      Container container = getTabOrLabelContainer(parentComposite,
          "Mitgliedschaft");

      SimpleVerticalContainer cols = new SimpleVerticalContainer(
          container.getComposite(), false, spaltenanzahl);

      if (Einstellungen.getEinstellung().getExterneMitgliedsnummer())
      {
        cols.addInput(control.getExterneMitgliedsnummer());
      }
      else
      {
        cols.addInput(control.getMitgliedsnummer());
      }
      cols.addInput(control.getBeitragsgruppe(true));
      cols.addInput(control.getEintritt());
      cols.addInput(control.getAustritt());
      if (Einstellungen.getEinstellung().getIndividuelleBeitraege())
      {
        cols.addInput(control.getIndividuellerBeitrag());
      }
      cols.addInput(control.getKuendigung());
      if (Einstellungen.getEinstellung().getSterbedatum()
          && control.getMitglied().getPersonenart().equals("n"))
      {
        cols.addInput(control.getSterbetag());
      }
      cols.arrangeVertically();
      if (Einstellungen.getEinstellung().getSekundaereBeitragsgruppen())
      {
        container.addPart(control.getMitgliedSekundaereBeitragsgruppeView());
      }

      // Wenn es mindestens eine Beitragsgruppe mit Beitragsart
      // "Familie: Zahler"
      // oder "Familie: Angehöriger" gibt, zeige Familienverband-Part.
      // Dieser Familien-Part soll über die komplette Breite angezeigt werden,
      // kann daher nicht im SimpleVerticalContainer angezeigt werden.
      DBIterator<Beitragsgruppe> it = Einstellungen.getDBService()
          .createList(Beitragsgruppe.class);
      it.addFilter("beitragsart = ? or beitragsart = ?",
          ArtBeitragsart.FAMILIE_ZAHLER.getKey(),
          ArtBeitragsart.FAMILIE_ANGEHOERIGER.getKey());
      if (it.hasNext())
      {
        // Verstecke Familienverband wenn aktuelles Mitglied nicht Teil einer
        // Familie ist.
        if (isBeitragsGruppeFuerFamilieAktiv() == false)
        {
          control.getFamilienverband().setVisible(false);
        }
        // Container lässt nur das Hinzufügen von Parts zu.
        // Aus diesem Grund ist Part Familienverband dynamisch:
        // Entweder wird der Familienverband angezeigt (setShow(true))
        // oder ein leeres Composite (setShow(false))
        container.addPart(control.getFamilienverband());
      }
      if (isBeitragsGruppeFuerZahlerAktiv() == false)
        control.getZukuenftigeBeitraegeView().setVisible(false);
      container.addPart(control.getZukuenftigeBeitraegeView());
    }
  }

  private boolean isBeitragsGruppeFuerZahlerAktiv() throws RemoteException
  {
    Beitragsgruppe gruppe = control.getMitglied().getBeitragsgruppe();
    if (null == gruppe)
      return false;
    if (gruppe.getBeitragsArt() == ArtBeitragsart.FAMILIE_ANGEHOERIGER)
      return false;
    return true;
  }

  private boolean isBeitragsGruppeFuerFamilieAktiv() throws RemoteException
  {
    Beitragsgruppe gruppe = control.getMitglied().getBeitragsgruppe();
    if (null == gruppe)
      return false;
    if (gruppe.getBeitragsArt() == ArtBeitragsart.NORMAL)
      return false;
    return true;
  }

  /**
   * Zeichnet GUI-Felder für Stammdaten. Wenn Kommunikationsdaten aktiviert
   * sind, werden drei Spalten angezeigt, ansonsten zwei.
   * 
   * @param parentComposite
   *          Composite auf dem gezeichnet wird.
   * @throws RemoteException
   */
  private void zeicheStammdaten(Composite parentComposite, int spaltenanzahl)
      throws RemoteException
  {
    Container container = getTabOrLabelContainer(parentComposite, "Stammdaten");
    SimpleVerticalContainer cols = new SimpleVerticalContainer(
        container.getComposite(), true, spaltenanzahl);

    if (!isMitgliedDetail())
    {
      cols.addInput(control.getAdresstyp());
    }
    cols.addInput(control.getAnrede());
    if (control.getMitglied().getPersonenart().equals("n"))
    {
      cols.addInput(control.getTitel());
    }
    if (control.getMitglied().getPersonenart().equals("j"))
    {
      control.getName(true).setName("Name Zeile 1");
      control.getVorname().setName("Name Zeile 2");
      control.getVorname().setMandatory(false);
    }
    cols.addInput(control.getName(true));
    cols.addInput(control.getVorname());
    cols.addInput(control.getAdressierungszusatz());

    cols.addInput(control.getStrasse());
    cols.addInput(control.getPlz());
    cols.addInput(control.getOrt());
    if (Einstellungen.getEinstellung().getAuslandsadressen())
    {
      cols.addInput(control.getStaat());
    }
    if (control.getMitglied().getPersonenart().equals("n"))
    {
      cols.addInput(control.getGeburtsdatum());
      cols.addInput(control.getGeschlecht());
    }

    if (Einstellungen.getEinstellung().getKommunikationsdaten())
    {
      cols.addInput(control.getTelefonprivat());
      cols.addInput(control.getHandy());
      cols.addInput(control.getTelefondienstlich());
      cols.addInput(control.getEmail());
    }
    cols.arrangeVertically();
  }

  /**
   * Zeichnet den Mitglieds-/Adressnamen in die Überschriftszeile
   */
  private void zeichneUeberschrift() throws RemoteException
  {
    String mgname = "";
    if (funktion == 'N')
      mgname = "- Neuanlage - ";
    if (funktion == 'D')
      mgname = "- Duplizieren - ";

    if (funktion == 'B' && isMitgliedDetail())
    {
      if (Einstellungen.getEinstellung().getExterneMitgliedsnummer())
      {
        mgname = (String) control.getExterneMitgliedsnummer().getValue();
        if (mgname == null || mgname.isEmpty())
          mgname = "?";
        mgname = mgname + " - ";
      }
      else
        mgname = (String) control.getMitgliedsnummer().getValue() + " - ";
    }

    if (control.getName(false).getValue() != null)
      if (((String) control.getName(false).getValue()).isEmpty() == false)
      {
        mgname = mgname + (String) control.getName(false).getValue();
        if (((String) control.getTitel().getValue()).isEmpty() == false)
          mgname = mgname + ", " + (String) control.getTitel().getValue() + " "
              + (String) control.getVorname().getValue();
        else if (((String) control.getVorname().getValue()).isEmpty() == false)
          mgname = mgname + ", " + (String) control.getVorname().getValue();
      }
    GUI.getView().setTitle(getTitle() + " (" + mgname.trim() + ")");
  }

  public abstract String getTitle();

  public abstract boolean isMitgliedDetail();

}
