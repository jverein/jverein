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

public class BuchungPart implements Part
{
  private BuchungsControl control;

  private AbstractView view;

  private boolean buchungabgeschlossen;

  public BuchungPart(BuchungsControl control, AbstractView view,
      boolean buchungabgeschlossen)
  {
    this.control = control;
    this.view = view;
    this.buchungabgeschlossen = buchungabgeschlossen;
  }

  @Override
  public void paint(Composite parent) throws RemoteException
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Buchung"));

    ScrolledContainer scrolled = new ScrolledContainer(parent, 1);

    ColumnLayout cols1 = new ColumnLayout(scrolled.getComposite(), 2);

    SimpleContainer grKontoauszug = new SimpleContainer(cols1.getComposite());

    grKontoauszug.addHeadline(JVereinPlugin.getI18n().tr("Buchung"));
    grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Buchungsnummer"),
        control.getID());
    grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Umsatz-ID"),
        control.getUmsatzid());
    grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Konto"),
        control.getKonto(true));
    grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Name"),
        control.getName());
    grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Betrag"),
        control.getBetrag());
    grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Verwendungszweck"),
        control.getZweck());
    grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Datum"),
        control.getDatum());
    grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Art"),
        control.getArt());
    if (Einstellungen.getEinstellung().getMitgliedskonto())
    {
      grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Mitgliedskonto"),
          control.getMitgliedskonto());
    }
    grKontoauszug.addLabelPair(JVereinPlugin.getI18n().tr("Kommentar"),
        control.getKommentar());

    SimpleContainer grBuchungsinfos = new SimpleContainer(cols1.getComposite());

    grBuchungsinfos.addHeadline(JVereinPlugin.getI18n().tr("Buchungsinfos"));
    grBuchungsinfos.addLabelPair(JVereinPlugin.getI18n().tr("Buchungsart"),
        control.getBuchungsart());
    grBuchungsinfos.addLabelPair(JVereinPlugin.getI18n().tr("Projekt"),
        control.getProjekt());
    grBuchungsinfos.addLabelPair(JVereinPlugin.getI18n().tr("Auszugsnummer"),
        control.getAuszugsnummer());
    grBuchungsinfos.addLabelPair(JVereinPlugin.getI18n().tr("Blattnummer"),
        control.getBlattnummer());

    SimpleContainer grSpendeninfos = grBuchungsinfos;
    grSpendeninfos.addHeadline(JVereinPlugin.getI18n().tr("Spendendetails"));
    grSpendeninfos.addLabelPair(
        JVereinPlugin.getI18n().tr("Erstattungsverzicht"),
        control.getVerzicht());

    if (JVereinPlugin.isArchiveServiceActive())
    {
      Buchung bu = (Buchung) control.getCurrentObject();
      if (!bu.isNewObject())
      {
        LabelGroup grDokument = new LabelGroup(scrolled.getComposite(),
            "Dokumente");
        BuchungDokument budo = (BuchungDokument) Einstellungen.getDBService()
            .createObject(BuchungDokument.class, null);
        budo.setReferenz(new Integer(bu.getID()));
        DokumentControl dcontrol = new DokumentControl(view, "buchungen",
            !buchungabgeschlossen);
        grDokument.addPart(dcontrol.getDokumenteList(budo));
        ButtonArea butts = new ButtonArea();
        butts.addButton(dcontrol.getNeuButton(budo));
        butts.paint(scrolled.getComposite());
      }
    }
  }
}
