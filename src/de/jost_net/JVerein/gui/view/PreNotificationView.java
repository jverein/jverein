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

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TabFolder;

import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.PreNotificationControl;
import de.jost_net.JVerein.keys.FormularArt;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.Color;
import de.willuhn.jameica.gui.util.TabGroup;

public class PreNotificationView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("SEPA Pre-Notification");

    final PreNotificationControl control = new PreNotificationControl(this);

    TabFolder folder = control.getFolder(getParent());
    folder.setLayoutData(new GridData(GridData.FILL_BOTH));
    folder.setBackground(Color.BACKGROUND.getSWTColor());

    TabGroup tabMailPDF = new TabGroup(folder, "Mail + PDF");

    tabMailPDF.addHeadline("Parameter");
    tabMailPDF.addInput(control.getOutput());
    tabMailPDF.addLabelPair("Formular",
        control.getFormular(FormularArt.SEPA_PRENOTIFICATION));

    tabMailPDF.addHeadline("Mail");

    tabMailPDF.addInput(control.getMailSubject());
    tabMailPDF.addInput(control.getMailBody());

    ButtonArea buttons1 = new ButtonArea();
    buttons1.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.PRENOTIFICATION, false, "help-browser.png");
    buttons1.addButton(control.getStartButton(this.getCurrentObject()));
    buttons1.paint(tabMailPDF.getComposite());

    TabGroup tab2 = new TabGroup(folder, "1 ct-Überweisung");

    tab2.addInput(control.getct1Ausgabe());
    tab2.addInput(control.getAusfuehrungsdatum());
    tab2.addInput(control.getVerwendungszweck());
    ButtonArea buttons2 = new ButtonArea();
    buttons2.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.PRENOTIFICATION, false, "help-browser.png");
    buttons2.addButton(control.getStart1ctUeberweisungButton(this
        .getCurrentObject()));
    buttons2.paint(tab2.getComposite());

  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">SEPA Pre-Notification</span> </p>"
        + "</form>";
  }
}
