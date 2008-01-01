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
 * Revision 1.5  2007/12/02 13:43:16  jost
 * Neu: Beitragsmodelle
 *
 * Revision 1.4  2007/12/01 17:46:46  jost
 * Wegfall Standardtab fÃ¼r die Suche
 *
 * Revision 1.3  2007/08/23 19:25:50  jost
 * Header korrigiert.
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.BackAction;
import de.jost_net.JVerein.gui.control.EinstellungControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.Headline;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class EinstellungenView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Einstellungen");

    final EinstellungControl control = new EinstellungControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Anzeige");
    group.addLabelPair("Geburtsdatum Pflichtfeld", control
        .getGeburtsdatumPflicht());
    group.addLabelPair("Eintrittsdatum Pflichtfeld", control
        .getEintrittsdatumPflicht());
    group.addLabelPair("Kommunikationsdaten anzeigen", control
        .getKommunikationsdaten());
    group.addLabelPair("Zusatzabbuchungen anzeigen *", control
        .getZusatzabbuchung());
    group.addLabelPair("Vermerke anzeigen", control.getVermerke());
    group.addLabelPair("Wiedervorlage anzeigen *", control.getWiedervorlage());
    group
        .addLabelPair("Kursteilnehmer anzeigen *", control.getKursteilnehmer());

    LabelGroup groupAbu = new LabelGroup(getParent(), "Beiträge");
    groupAbu.addLabelPair("Beitragsmodel", control.getBeitragsmodel());

    LabelGroup groupDatei = new LabelGroup(getParent(), "Dateinamen");
    groupDatei.addLabelPair("Muster", control.getDateinamenmuster());
    groupDatei.addText("a$ = Aufgabe, d$ = Datum, s$ = Sortierung, z$ = Zeit",
        true);

    new Headline(getParent(), "* Änderung erfordert Neustart");
    ButtonArea buttons = new ButtonArea(getParent(), 2);
    buttons.addButton("<< Zurück", new BackAction());
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
