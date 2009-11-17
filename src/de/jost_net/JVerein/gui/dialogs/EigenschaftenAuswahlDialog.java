/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * heiner@jverein.de
 * www.jverein.de
 * $Log$
 * Revision 1.2  2009/06/11 21:02:41  jost
 * Vorbereitung I18N
 *
 * Revision 1.1  2008/01/25 16:02:57  jost
 * Neu: Eigenschaften des Mitgliedes
 *
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
import de.willuhn.jameica.gui.parts.TreePart;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

/**
 * Dialog, zur Auswahl von Eigenschaften eines Mitglied.
 */
public class EigenschaftenAuswahlDialog extends AbstractDialog
{
  private MitgliedControl control;

  private TreePart tree;

  private String selection = "";

  public EigenschaftenAuswahlDialog() throws RemoteException
  {
    super(EigenschaftenAuswahlDialog.POSITION_CENTER);
    this.setSize(400, 400);
    setTitle(JVereinPlugin.getI18n().tr("Eigenschaften auswählen "));
    control = new MitgliedControl(null);
  }

  protected void paint(Composite parent) throws Exception
  {
    LabelGroup group = new LabelGroup(parent, JVereinPlugin.getI18n().tr(
        "Eigenschaften"), true);
    tree = control.getEigenschaftenAuswahlTree();
    group.addPart(tree);

    ButtonArea buttons = new ButtonArea(parent, 2);
    buttons.addButton(i18n.tr(JVereinPlugin.getI18n().tr("OK")), new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        try
        {
          ArrayList checkednodes = (ArrayList) tree.getItems();
          for (Object o : checkednodes)
          {
            EigenschaftenNode checkedNode = (EigenschaftenNode) o;
            if (checkedNode.getNodeType() == EigenschaftenNode.EIGENSCHAFTEN)
            {
              if (selection.length() > 0)
              {
                selection += ",";
              }
              selection += checkedNode.getEigenschaft().getID();
            }
          }
        }
        catch (RemoteException e)
        {
          throw new ApplicationException(e.getMessage());
        }
        close();
      }
    });
  }

  protected Object getData() throws Exception
  {
    return selection;
  }
}
