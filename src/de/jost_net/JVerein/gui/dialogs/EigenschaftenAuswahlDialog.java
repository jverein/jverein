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
package de.jost_net.JVerein.gui.dialogs;

import java.rmi.RemoteException;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.server.EigenschaftenNode;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.parts.TreePart;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.logging.Logger;

/**
 * Dialog, zur Auswahl von Eigenschaften eines Mitglied.
 */
public class EigenschaftenAuswahlDialog extends
    AbstractDialog<EigenschaftenAuswahlParameter>
{

  private MitgliedControl control;

  private SelectInput eigenschaftenverknuepfung;

  private String defaults = null;

  private boolean ohnePflicht;

  private boolean verknuepfung;

  private EigenschaftenAuswahlParameter param;

  /**
   * Eigenschaften oder Eigenschaftengruppen auswählen
   * 
   * @param defaults
   *          Liste der Eigenschaften-IDs durch Komma separiert.
   */
  public EigenschaftenAuswahlDialog(String defaults, boolean ohnePflicht,
      boolean verknuepfung)
  {
    super(EigenschaftenAuswahlDialog.POSITION_CENTER);
    this.setSize(400, 400);
    this.ohnePflicht = ohnePflicht;
    this.verknuepfung = verknuepfung;
    setTitle("Eigenschaften auswählen ");
    control = new MitgliedControl(null);
    this.setDefaults(defaults);
  }

  /**
   * Speichert die Default-Werte.
   * 
   * @param defaults
   */
  public void setDefaults(String defaults)
  {
    this.defaults = defaults != null ? defaults : "";
  }

  @Override
  protected void paint(Composite parent) throws RemoteException
  {
    final TreePart tree = control.getEigenschaftenAuswahlTree(this.defaults,
        ohnePflicht);

    LabelGroup group = new LabelGroup(parent, "Eigenschaften", true);
    group.addPart(tree);
    if (verknuepfung)
    {
      group.addInput(getEigenschaftenVerknuepfung());
    }
    ButtonArea buttons = new ButtonArea();
    buttons.addButton("OK", new Action()
    {
      @Override
      public void handleAction(Object context)
      {
        try
        {
          param = new EigenschaftenAuswahlParameter();
          ArrayList<?> checkednodes = (ArrayList<?>) tree.getItems();
          for (Object o : checkednodes)
          {
            EigenschaftenNode checkedNode = (EigenschaftenNode) o;
            if (checkedNode.getNodeType() == EigenschaftenNode.EIGENSCHAFTEN)
            {
              param.add(checkedNode);
            }
          }
          if (verknuepfung)
          {
            param.setVerknuepfung((String) eigenschaftenverknuepfung.getValue());
          }
        }
        catch (RemoteException e)
        {
          Logger.error("Fehler", e);
        }
        close();
      }
    });
    buttons.paint(parent);
  }

  @Override
  protected EigenschaftenAuswahlParameter getData()
  {
    return param;
  }

  private SelectInput getEigenschaftenVerknuepfung()
  {
    if (eigenschaftenverknuepfung != null
        && !eigenschaftenverknuepfung.getControl().isDisposed())
    {
      return eigenschaftenverknuepfung;
    }
    ArrayList<String> werte = new ArrayList<String>();
    werte.add("und");
    werte.add("oder");
    eigenschaftenverknuepfung = new SelectInput(werte,
        control.getEigenschaftenVerknuepfung());
    eigenschaftenverknuepfung.setName("Gruppen-Verknüpfung");
    return eigenschaftenverknuepfung;
  }

}
