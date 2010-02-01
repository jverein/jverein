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
 * Revision 1.5  2009/12/06 21:40:39  jost
 * Überflüssigen Code entfernt.
 *
 * Revision 1.4  2009/11/19 19:44:02  jost
 * Bugfix Eigenschaften
 *
 * Revision 1.3  2009/11/17 20:57:34  jost
 * Neu: Eigenschaft und EigenschaftGruppe
 *
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
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Dialog, zur Auswahl von Eigenschaften eines Mitglied.
 */
public class EigenschaftenAuswahlDialog extends AbstractDialog
{
  private MitgliedControl control;

  private TreePart tree;

  private Settings settings;

  private String tmp2 = "";

  public EigenschaftenAuswahlDialog(Settings settings) throws RemoteException
  {
    super(EigenschaftenAuswahlDialog.POSITION_CENTER);
    this.setSize(400, 400);
    setTitle(JVereinPlugin.getI18n().tr("Eigenschaften auswählen "));
    control = new MitgliedControl(null);
    this.settings = settings;
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
        String tmp1 = "";
        try
        {
          ArrayList<?> checkednodes = (ArrayList<?>) tree.getItems();
          for (Object o : checkednodes)
          {
            EigenschaftenNode checkedNode = (EigenschaftenNode) o;
            if (checkedNode.getNodeType() == EigenschaftenNode.EIGENSCHAFTEN)
            {
              if (tmp1.length() > 0)
              {
                tmp1 += ",";
                tmp2 += ", ";
              }
              tmp1 += checkedNode.getEigenschaft().getID();
              tmp2 += checkedNode.getEigenschaft().getBezeichnung();
            }
          }
        }
        catch (RemoteException e)
        {
          Logger.error("Fehler", e);
        }
        settings.setAttribute("mitglied.eigenschaften", tmp1);
        close();
      }
    });
  }

  protected Object getData() throws Exception
  {
    return tmp2;
  }
}
