/**********************************************************************
 * Kopiert aus Hibiscus DonateView
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

import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.Container;
import de.willuhn.jameica.gui.util.SWTUtil;
import de.willuhn.jameica.gui.util.SimpleContainer;
import de.willuhn.jameica.hbci.Settings;
import de.willuhn.jameica.hbci.rmi.AuslandsUeberweisung;
import de.willuhn.jameica.hbci.rmi.SepaDauerauftrag;
import de.willuhn.jameica.hbci.rmi.Turnus;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;

/**
 * View fuer den Spenden-Aufruf.
 */
public class SpendenView extends AbstractView
{

  /**
   * @see de.willuhn.jameica.gui.AbstractView#bind()
   */
  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Spenden für JVerein");
    {
      Composite comp = new Composite(this.getParent(), SWT.NONE);
      comp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      comp.setLayout(SWTUtil.createGrid(2, false));

      Container container = new SimpleContainer(comp);
      container.addHeadline("Warum eigentlich?" + "  ");
      container.addText(
          "JVerein wird in meiner Freizeit entwickelt. Ursprünglich habe ich es entwickelt, da mir bei der "
              + "Übernahme meiner Tätigkeit als Kassierer eines Vereins keine geeignete Software zur Verfügung stand, die unter "
              + "LINUX ablauffähig ist. Später habe ich mich entschlossen JVerein als OpenSource zur Verfügung zu stellen. "
              + "Inzwischen nutzen ca. 2000 - 3000 Vereine (geschätzt aufgrund der Downloadzahlen und der Zuschriften) JVerein. "
              + "Es sind jede Menge Funktionalitäten zusätzlich entwickelt worden. Benutzer sind der Meinung, dass JVerein "
              + "den Vergleich mit professionellen Lösungen für viel Geld nicht scheuen muss. "
              + "\n\nZur Deckung der Kosten für Web-Server und Hardware, bitte ich um einen kleinen Beitrag. Der kann entweder "
              + "einmalig oder laufend geleistet werden. Nicht vergessen: Auftrag in Hibiscus abschicken."
              + "\n\nVielen Dank!\n\nHeiner Jostkleigrewe",
          true);

      Canvas c = SWTUtil.getCanvas(comp, SWTUtil.getImage("JVerein.png"),
          SWT.TOP | SWT.LEFT);
      ((GridData) c.getLayoutData()).minimumWidth = 340;
    }

    {
      final char[] kto = new char[] { '5', '6', '9', '1', '5', '3', '0', '0',
          '0' };
      final char[] blz = new char[] { '2', '0', '0', '4', '1', '1', '1', '1' };

      final char[] iban = new char[] { 'D', 'E', '1', '5', '2', '0', '0', '4',
          '1', '1', '1', '1', '0', '5', '6', '9', '1', '5', '3', '0', '0',
          '0' };
      final char[] bic = new char[] { 'C', 'O', 'B', 'A', 'D', 'E', 'H', 'D',
          'X', 'X', 'X' };

      final String name = "Heiner Jostkleigrewe";

      ButtonArea buttons = new ButtonArea();
      buttons.addButton("Dauerauftrag erstellen", new Action()
      {
        @Override
        public void handleAction(Object context)
        {
          try
          {
            SepaDauerauftrag d = (SepaDauerauftrag) Settings.getDBService()
                .createObject(SepaDauerauftrag.class, null);
            d.setGegenkontoBLZ(new String(blz));
            d.setGegenkontoNummer(new String(kto));
            d.setGegenkontoName(name);
            d.setZweck("Beitrag Weiterentwicklung");
            d.setZweck2("JVerein");
            // Wir lassen 7 Tage Vorlauf
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 7);
            d.setErsteZahlung(cal.getTime());
            Turnus turnus = (Turnus) Settings.getDBService()
                .createObject(Turnus.class, null);
            turnus.setIntervall(1);
            turnus.setTag(cal.get(Calendar.DAY_OF_MONTH));
            turnus.setZeiteinheit(Turnus.ZEITEINHEIT_MONATLICH);
            d.setTurnus(turnus);
            new de.willuhn.jameica.hbci.gui.action.SepaDauerauftragNew()
                .handleAction(d);
          }
          catch (Exception e)
          {
            Logger.error("unable to create dauerauftrag", e);
            Application.getMessagingFactory()
                .sendMessage(new StatusBarMessage(
                    "Fehler beim Anlegen des Dauerauftrages: " + e.getMessage(),
                    StatusBarMessage.TYPE_ERROR));
          }
        }
      }, null, false, "emblem-special.png");
      buttons.addButton("...oder SEPA-Überweisung", new Action()
      {
        @Override
        public void handleAction(Object context)
        {
          try
          {
            AuslandsUeberweisung u = (AuslandsUeberweisung) Settings
                .getDBService().createObject(AuslandsUeberweisung.class, null);
            u.setGegenkontoBLZ(new String(bic));
            u.setGegenkontoNummer(new String(iban));
            u.setGegenkontoName(name);
            u.setZweck("Beitrag Weiterentwicklung JVerein");
            new de.willuhn.jameica.hbci.gui.action.AuslandsUeberweisungNew()
                .handleAction(u);
          }
          catch (Exception e)
          {
            Logger.error("unable to create sepa ueberweisung", e);
            Application.getMessagingFactory().sendMessage(new StatusBarMessage(
                "Fehler beim Anlegen der SEPA-Überweisung: " + e.getMessage(),
                StatusBarMessage.TYPE_ERROR));
          }
        }
      }, null, false, "stock_next.png");
      buttons.paint(getParent());
    }
  }
}
