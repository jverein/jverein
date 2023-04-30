/**********************************************************************
 * basiert auf dem KontoAuswahlDialog aus Hibiscus
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

package de.jost_net.JVerein.gui.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;

import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.MitgliedskontoControl;
import de.jost_net.JVerein.gui.control.MitgliedskontoControl.DIFFERENZ;
import de.jost_net.JVerein.gui.view.DokumentationUtil;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.gui.util.Container;
import de.willuhn.jameica.gui.util.SimpleContainer;
import de.willuhn.jameica.gui.util.TabGroup;

/**
 * Ein Dialog, ueber den man ein Mitgliedskonto auswaehlen kann.
 */
public class MitgliedskontoAuswahlDialog extends AbstractDialog<Object>
{

  private de.willuhn.jameica.system.Settings settings;

  private String text = null;

  private Object choosen = null;

  private MitgliedskontoControl control;

  private TablePart mitgliedskontolist = null;

  private TablePart mitgliedlist = null;

  private Buchung buchung;

  public MitgliedskontoAuswahlDialog(Buchung buchung)
  {
    super(MitgliedskontoAuswahlDialog.POSITION_MOUSE, true);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);

    this.setSize(900, 700);
    this.setTitle("Mitgliedskonto-Auswahl");
    this.buchung = buchung;
    control = new MitgliedskontoControl(null);
  }

  @Override
  protected void paint(Composite parent) throws Exception
  {
    TabFolder folder = new TabFolder(parent, SWT.NONE);
    folder.setLayoutData(new GridData(GridData.FILL_BOTH));

    TabGroup tabNurIst = new TabGroup(folder, "nur Ist", false, 1);
    Container grNurIst = new SimpleContainer(tabNurIst.getComposite());
    grNurIst.addHeadline("Auswahl des Mitgliedskontos");
    if (text == null || text.length() == 0)
    {
      text = "Bitte wählen Sie das gewünschte Mitgliedskonto aus.";
    }
    grNurIst.addText(text, true);
    TextInput suNa = control.getSuchName();
    suNa.setValue(buchung.getName());
    grNurIst.addLabelPair("Name", suNa);
    grNurIst.addLabelPair("Differenz",
        control.getDifferenz(DIFFERENZ.EGAL));
    Action action = new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        if (context == null || !(context instanceof Mitgliedskonto))
        {
          return;
        }
        choosen = context;
        close();
      }
    };
    mitgliedskontolist = control.getMitgliedskontoList(action, null);
    mitgliedskontolist.paint(tabNurIst.getComposite());

    //
    TabGroup tabSollIst = new TabGroup(folder, "Soll u. Ist", true, 1);
    Container grSollIst = new SimpleContainer(tabSollIst.getComposite());
    grSollIst.addHeadline("Auswahl des Mitgliedskontos");

    if (text == null || text.length() == 0)
    {
      text = "Bitte wählen Sie das gewünschte Mitgliedskonto aus.";
    }
    grSollIst.addText(text, true);
    control.getSuchName2(true).setValue(buchung.getName());
    grSollIst.addLabelPair("Name", control.getSuchName2(false));
    grSollIst.addInput(control.getSpezialSuche());

    final Action action2 = new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        if (context == null || !(context instanceof Mitglied))
        {
          return;
        }
        choosen = context;
        close();
      }
    };
    mitgliedlist = control.getMitgliedskontoList2(action2, null);
    mitgliedlist.paint(tabSollIst.getComposite());

    ButtonArea b = new ButtonArea();

    b.addButton("übernehmen", new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        Object o = mitgliedskontolist.getSelection();

        if (o instanceof Mitgliedskonto)
        {
          choosen = o;
          close();
        }
        else
        {
          o = mitgliedlist.getSelection();

          if (o instanceof Mitglied)
          {
            choosen = o;
            close();
          }
        }
        return;
      }
    }, null, false, "check.png");

    b.addButton("entfernen", new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        choosen = null;
        close();
      }
    }, null, false, "undo.png");
    b.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.MITGLIEDSKONTO_AUSWAHL, false, "question-circle.png");

    b.addButton("abbrechen", new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        close();
      }
    }, null, false, "stop-circle.png");
    b.paint(parent);
  }

  /**
   * Liefert das ausgewaehlte Mitgliedskonto zurueck oder <code>null</code> wenn
   * der Abbrechen-Knopf gedrueckt wurde.
   * 
   * @see de.willuhn.jameica.gui.dialogs.AbstractDialog#getData()
   */
  @Override
  protected Object getData() throws Exception
  {
    return choosen;
  }

  /**
   * Optionale Angabe des anzuzeigenden Textes. Wird hier kein Wert gesetzt,
   * wird ein Standard-Text angezeigt.
   * 
   * @param text
   */
  public void setText(String text)
  {
    this.text = text;
  }
}
