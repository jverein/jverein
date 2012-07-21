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
package de.jost_net.JVerein.gui.dialogs;

import java.rmi.RemoteException;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.server.EigenschaftenNode;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.parts.TreePart;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.logging.Logger;

/**
 * Dialog, zur Auswahl von Eigenschaften eines Mitglied.
 */
public class EigenschaftenAuswahlDialog extends
    AbstractDialog<ArrayList<EigenschaftenNode>>
{

  private MitgliedControl control;

  private String defaults = null;

  private boolean ohnePflicht;

  private ArrayList<EigenschaftenNode> retval = new ArrayList<EigenschaftenNode>();

  /**
   * Eigenschaften oder Eigenschaftengruppen auswählen
   * 
   * @param defaults
   *          Liste der Eigenschaften-IDs durch Komma separiert.
   */
  public EigenschaftenAuswahlDialog(String defaults, boolean ohnePflicht)
  {
    super(EigenschaftenAuswahlDialog.POSITION_CENTER);
    this.setSize(400, 400);
    this.ohnePflicht = ohnePflicht;
    setTitle(JVereinPlugin.getI18n().tr("Eigenschaften auswählen "));
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

    LabelGroup group = new LabelGroup(parent, JVereinPlugin.getI18n().tr(
        "Eigenschaften"), true);

    group.addPart(tree);

    ButtonArea buttons = new ButtonArea();
    buttons.addButton(i18n.tr(JVereinPlugin.getI18n().tr("OK")), new Action()
    {

      public void handleAction(Object context)
      {
        try
        {
          retval = new ArrayList<EigenschaftenNode>();
          ArrayList<?> checkednodes = (ArrayList<?>) tree.getItems();
          for (Object o : checkednodes)
          {
            EigenschaftenNode checkedNode = (EigenschaftenNode) o;
            if (checkedNode.getNodeType() == EigenschaftenNode.EIGENSCHAFTEN)
            {
              retval.add(checkedNode);
            }
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
  protected ArrayList<EigenschaftenNode> getData()
  {
    return retval;
  }
}
