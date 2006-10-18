/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * jost@berlios.de
 * jverein.berlios.de
 * $Log$
 * Revision 1.1  2006/09/20 15:39:10  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TabFolder;

import de.jost_net.JVerein.gui.action.BackAction;
import de.jost_net.JVerein.gui.action.MitgliedDeleteAction;
import de.jost_net.JVerein.gui.action.MitgliedDetailAction;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.Color;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.gui.util.TabGroup;
import de.willuhn.util.ApplicationException;

public class MitgliedDetailView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Daten des Mitgliedes");

    final MitgliedControl control = new MitgliedControl(this);

    LabelGroup grGrund = new LabelGroup(getParent(), "Grunddaten");
    grGrund.getComposite().setSize(290, 190);
    grGrund.addLabelPair("Anrede", control.getAnrede());
    grGrund.addLabelPair("Titel", control.getTitel());
    grGrund.addLabelPair("Name", control.getName());
    grGrund.addLabelPair("Vorname", control.getVorname());
    grGrund.addLabelPair("Strasse", control.getStrasse());
    grGrund.addLabelPair("PLZ", control.getPlz());
    grGrund.addLabelPair("Ort", control.getOrt());
    grGrund.addLabelPair("Geburtsdatum", control.getGeburtsdatum());
    grGrund.addLabelPair("Geschlecht", control.getGeschlecht());

    TabFolder folder = new TabFolder(getParent(), SWT.NONE);
    folder.setLayoutData(new GridData(GridData.FILL_BOTH));
    folder.setBackground(Color.BACKGROUND.getSWTColor());

    TabGroup tab1 = new TabGroup(folder, "Bankverbindung");
    tab1.addLabelPair("BLZ", control.getBlz());
    tab1.addLabelPair("Konto", control.getKonto());
    tab1.addLabelPair("Kontoinhaber", control.getKontoinhaber());

    TabGroup tab2 = new TabGroup(folder, "Kommunikation");
    tab2.addLabelPair("Telefon priv.", control.getTelefonprivat());
    tab2.addLabelPair("Telefon dienstl.", control.getTelefondienstlich());
    tab2.addLabelPair("eMail", control.getEmail());

    TabGroup tab3 = new TabGroup(folder, "Mitgliedschaft");
    tab3.addLabelPair("Eintritt", control.getEintritt());
    tab3.addLabelPair("Betragsgruppe", control.getBeitragsgruppe());
    tab3.addLabelPair("Austritt", control.getAustritt());
    tab3.addLabelPair("Kündigung", control.getKuendigung());

    TabGroup tab4 = new TabGroup(folder, "Zusatzabbuchung");
    control.getZusatzabbuchungenTable().paint(tab4.getComposite());
    ButtonArea buttonszus = new ButtonArea(tab4.getComposite(), 1);
    buttonszus.addButton(control.getZusatzabbuchungNeu());

    ButtonArea buttons = new ButtonArea(getParent(), 4);

    buttons.addButton("<< Zurück", new BackAction());
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
