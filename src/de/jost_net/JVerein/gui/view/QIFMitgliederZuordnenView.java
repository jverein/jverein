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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TabFolder;

import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.QIFMitgliedZuordnenControl;
import de.jost_net.JVerein.rmi.QIFImportPos;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.Color;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.gui.util.SimpleContainer;
import de.willuhn.jameica.gui.util.TabGroup;

public class QIFMitgliederZuordnenView extends AbstractView
{
  QIFImportPos importPos;

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Mitglieder zuordnen");
    init();

    QIFMitgliedZuordnenControl detailControl = new QIFMitgliedZuordnenControl(
        this);

    SimpleContainer maincontainer = new SimpleContainer(getParent(), true, 1);
    maincontainer.addText(
        "Ordnen Sie den externen Namen ein Mitglied aus JVerein zu.", true,
        Color.COMMENT);
    LabelGroup lblGroup = new LabelGroup(maincontainer.getComposite(), "");
    lblGroup.addLabelPair("alle Mitglieder zeigen",
        detailControl.getCheckBoxAlleMitgliederZeigen());
    lblGroup.addLabelPair("JVerein Mitglied: ",
        detailControl.getMitgliederInput());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.QIFIMPORT, false, "help-browser.png");
    buttons.addButton("Zuordnung Speichern",
        detailControl.getSpeichernAction(), null, false, "document-save.png");
    buttons.addButton("Zuordnung Entfernen",
        detailControl.getZuordnenEntfernenAction(), null, false, "clear.gif");
    buttons.paint(getParent());

    TabFolder folder = new TabFolder(maincontainer.getComposite(), SWT.V_SCROLL
        | SWT.BORDER);
    folder.setLayoutData(new GridData(GridData.FILL_BOTH));
    folder.setBackground(Color.BACKGROUND.getSWTColor());
    {
      TabGroup tabAllgemein = new TabGroup(folder, "Zuordnen", true, 1);
      tabAllgemein.addHeadline("Alle externen Namen..");
      tabAllgemein.addPart(detailControl.getExterneNamensListe());
    }

    {
      TabGroup tabBeispiel = new TabGroup(folder, "Beispielbuchungen", true, 1);
      tabBeispiel.addHeadline("Beispielbuchungen zum gewählten Namen");

      SimpleContainer labelContainer = new SimpleContainer(
          tabBeispiel.getComposite(), false, 2);
      labelContainer.addLabelPair("Importierer Name",
          detailControl.getExternerNameInput());

      tabBeispiel.addPart(detailControl.getBeispielPositionsListe());
    }

  }

  private void init() throws RemoteException
  {
    importPos = (QIFImportPos) getCurrentObject();
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Mitglieder</span></p>"
        + "<p>Ordnen Sie Mitglieder den externen Namen in den Buchungen zu. "
        + "Die Buchungen werden dann diesen Mitgliedkonten zugebucht.</p>"
        + "</form>";
  }

}
