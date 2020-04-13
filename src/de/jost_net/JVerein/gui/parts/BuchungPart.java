/**********************************************************************
 * Copyright (c) by Heiner Jostkleigrewe
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de | www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.gui.parts;

import java.rmi.RemoteException;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.control.BuchungsControl;
import de.jost_net.JVerein.gui.control.DokumentControl;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.BuchungDokument;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.ColumnLayout;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.gui.util.ScrolledContainer;
import de.willuhn.jameica.gui.util.SimpleContainer;

public class BuchungPart implements Part {
  private BuchungsControl control;

  private AbstractView view;

  private boolean buchungabgeschlossen;

  public BuchungPart(BuchungsControl control, AbstractView view, boolean buchungabgeschlossen) {
    this.control = control;
    this.view = view;
    this.buchungabgeschlossen = buchungabgeschlossen;
  }

  @Override
  public void paint(Composite parent) throws RemoteException {
    String title = (control.getBuchung().getSpeicherung() ? "Buchung" : "Splitbuchung");
    GUI.getView().setTitle(title);

    ScrolledContainer scrolled = new ScrolledContainer(parent, 1);

    ColumnLayout cols1 = new ColumnLayout(scrolled.getComposite(), 2);

    SimpleContainer grKontoauszug = new SimpleContainer(cols1.getComposite());

    grKontoauszug.addHeadline(title);
    if (!Einstellungen.getEinstellung().getVerwendeBelegnummer()) {
      grKontoauszug.addLabelPair("Buchungsnummer", control.getID());
    } else {
      grKontoauszug.addLabelPair("Belegnummer", control.getBelegnummer());
    }
    grKontoauszug.addLabelPair("Umsatz-ID", control.getUmsatzid());
    grKontoauszug.addLabelPair("Konto", control.getKonto(true));
    grKontoauszug.addLabelPair("Name", control.getName());
    grKontoauszug.addLabelPair("Betrag", control.getBetrag());
    grKontoauszug.addLabelPair("Verwendungszweck", control.getZweck());
    grKontoauszug.addLabelPair("Datum", control.getDatum());
    grKontoauszug.addLabelPair("Art", control.getArt());
    grKontoauszug.addLabelPair("Mitgliedskonto", control.getMitgliedskonto());
    grKontoauszug.addLabelPair("Kommentar", control.getKommentar());

    SimpleContainer grBuchungsinfos = new SimpleContainer(cols1.getComposite());

    grBuchungsinfos.addHeadline("Buchungsinfos");
    grBuchungsinfos.addLabelPair("Buchungsart", control.getBuchungsart());
    grBuchungsinfos.addLabelPair("Projekt", control.getProjekt());
    grBuchungsinfos.addLabelPair("Auszugsnummer", control.getAuszugsnummer());
    grBuchungsinfos.addLabelPair("Blattnummer", control.getBlattnummer());

    SimpleContainer grSpendeninfos = grBuchungsinfos;
    grSpendeninfos.addHeadline("Spendendetails");
    grSpendeninfos.addLabelPair("Erstattungsverzicht", control.getVerzicht());

    if (JVereinPlugin.isArchiveServiceActive()) {
      Buchung bu = (Buchung) control.getCurrentObject();
      if (!bu.isNewObject()) {
        LabelGroup grDokument = new LabelGroup(scrolled.getComposite(), "Dokumente");
        BuchungDokument budo = (BuchungDokument) Einstellungen.getDBService()
            .createObject(BuchungDokument.class, null);
        budo.setReferenz(new Long(bu.getID()));
        DokumentControl dcontrol = new DokumentControl(view, "buchungen", !buchungabgeschlossen);
        grDokument.addPart(dcontrol.getDokumenteList(budo));
        ButtonArea butts = new ButtonArea();
        butts.addButton(dcontrol.getNeuButton(budo));
        butts.paint(scrolled.getComposite());
      }
    }
  }
}
