/**********************************************************************
 * $Author: Dietmar Janz $
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

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.AbrechnungslaufBuchungenControl;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.Container;
import de.willuhn.jameica.gui.util.SimpleContainer;
import de.willuhn.util.ApplicationException;

public class AbrechnungslaufBuchungenView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Abrechnungslauf");

    final AbrechnungslaufBuchungenControl control = new AbrechnungslaufBuchungenControl(
        this);

    SimpleContainer bereich = new SimpleContainer(getParent());
    SimpleContainer auslage = machSpalte(bereich, 0, 15, 0);
    SimpleContainer spalte1 = machSpalte(auslage, 1, 1, 0);
    SimpleContainer spalte2 = machSpalte(auslage, 2, 1, 0);
    SimpleContainer spalte3 = machSpalte(auslage, 5, 1, 0);
    SimpleContainer spalte4 = machSpalte(auslage, 7, 1, 0);

    spalte1.addText("Lauf", false);
    spalte2.addText("Datum", false);
    spalte3.addText("Zahlungsgrund", false);
    spalte4.addText("Bemerkung", false);

    spalte1.addSeparator();
    spalte2.addSeparator();
    spalte3.addSeparator();
    spalte4.addSeparator();

    spalte1.addText(control.getLauf().getValue().toString(), false);
    spalte2.addText(
        new JVDateFormatTTMMJJJJ().format(control.getDatum(false).getValue()),
        false);
    spalte3.addText(control.getZahlungsgrund().getValue().toString(), false);
    spalte4.addText(control.getBemerkung().getValue().toString(), false);

    control.getSollbuchungsList().paint(this.getParent());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton(control.getStartListeButton());
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.ABRECHNUNGSLAUF, false, "question-circle.png");
    buttons.paint(this.getParent());
  }

  SimpleContainer machSpalte(Container cont, int breite, int anzSpalten,
      int min) throws ApplicationException
  {
    GridLayout sl;
    GridData sd;
    SimpleContainer spalte = new SimpleContainer(cont.getComposite(), true);
    sd = (GridData) spalte.getComposite().getLayoutData();

    if (breite == 0)
    { // der ganze Behälter
      sd.grabExcessHorizontalSpace = true;
      sd.horizontalSpan = 1;
    }
    else
      sd.horizontalSpan = 2 * breite;

    if (min != 0)
      sd.minimumWidth = min;
    sl = (GridLayout) spalte.getComposite().getLayout();
    sl.numColumns = 2 * anzSpalten;
    sl.marginLeft = -5;
    sl.marginRight = -5;
    sl.makeColumnsEqualWidth = true;
    return spalte;
  }
}
