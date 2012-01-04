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

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.BuchungNeuAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.BuchungsControl;
import de.jost_net.JVerein.gui.parts.BuchungPart;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;

public class BuchungView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Buchung"));

    final BuchungsControl control = new BuchungsControl(this);
    BuchungPart part = new BuchungPart(control, this);
    part.paint(this.getParent());
    // ScrolledContainer scrolled = new ScrolledContainer(getParent(), 1);
    //
    // LabelGroup grKontoauszug = new LabelGroup(scrolled.getComposite(),
    // JVereinPlugin.getI18n().tr("Buchung"));
    // grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Buchungsnummer"),
    // control.getID());
    // grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Umsatz-ID"),
    // control.getUmsatzid());
    // grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Konto"),
    // control.getKonto(true));
    // grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Name"),
    // control.getName());
    // grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Betrag"),
    // control.getBetrag());
    // grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Verwendungszweck"),
    // control.getZweck());
    // grKontoauszug.addLabelPair(
    // JVereinPlugin.getI18n().tr("Verwendungszweck 2"), control.getZweck2());
    // grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Datum"),
    // control.getDatum());
    // grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Art"),
    // control.getArt());
    // if (Einstellungen.getEinstellung().getMitgliedskonto())
    // {
    // grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Mitgliedskonto"),
    // control.getMitgliedskonto());
    // }
    // grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Kommentar"),
    // control.getKommentar());
    //
    // LabelGroup grBuchungsinfos = new LabelGroup(scrolled.getComposite(),
    // JVereinPlugin.getI18n().tr("Buchungsinfos"));
    // grBuchungsinfos.addLabelPair(JVereinPlugin.getI18n().tr("Buchungsart"),
    // control.getBuchungsart());
    // grBuchungsinfos.addLabelPair(JVereinPlugin.getI18n().tr("Auszugsnummer"),
    // control.getAuszugsnummer());
    // grBuchungsinfos.addLabelPair(JVereinPlugin.getI18n().tr("Blattnummer"),
    // control.getBlattnummer());
    //
    // LabelGroup grSpendeninfos = new LabelGroup(scrolled.getComposite(),
    // JVereinPlugin.getI18n().tr("Spendendetails"));
    // grSpendeninfos.addLabelPair(
    // JVereinPlugin.getI18n().tr("Erstattungsverzicht"),
    // control.getVerzicht());
    //
    // if (JVereinPlugin.isArchiveServiceActive())
    // {
    // Buchung bu = (Buchung) control.getCurrentObject();
    // if (!bu.isNewObject())
    // {
    // LabelGroup grDokument = new LabelGroup(scrolled.getComposite(),
    // "Dokumente");
    // BuchungDokument budo = (BuchungDokument) Einstellungen.getDBService()
    // .createObject(BuchungDokument.class, null);
    // budo.setReferenz(new Integer(bu.getID()));
    // DokumentControl dcontrol = new DokumentControl(this, "buchungen");
    // grDokument.addPart(dcontrol.getDokumenteList(budo));
    // ButtonArea butts = new ButtonArea();
    // butts.addButton(dcontrol.getNeuButton(budo));
    // butts.paint(scrolled.getComposite());
    // }
    // }

    ButtonArea buttons = new ButtonArea();
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.BUCHUNGEN, false,
        "help-browser.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("neu"),
        new BuchungNeuAction(), null, false, "document-new.png");
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
    return "<form><p><span color=\"header\" font=\"header\">Buchung</span></p>"
        + "<p>Zuordnung einer Buchungsart zu einer Buchung.</p></form>";
  }
}
