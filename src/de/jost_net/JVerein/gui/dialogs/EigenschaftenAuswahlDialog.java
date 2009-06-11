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
 * Revision 1.1  2008/01/25 16:02:57  jost
 * Neu: Eigenschaften des Mitgliedes
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.dialogs;

import java.rmi.RemoteException;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.control.EigenschaftenControl;
import de.jost_net.JVerein.gui.control.HilfsEigenschaft;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

/**
 * Dialog, zur Auswahl von Eigenschaften eines Mitglied.
 */
public class EigenschaftenAuswahlDialog extends AbstractDialog
{
  private EigenschaftenControl control;

  private TablePart table;

  private String selection;

  public EigenschaftenAuswahlDialog() throws RemoteException
  {
    super(EigenschaftenAuswahlDialog.POSITION_CENTER);
    this.setSize(400, 400);
    setTitle(JVereinPlugin.getI18n().tr("Eigenschaften auswählen "));
    control = new EigenschaftenControl(null, null);
  }

  protected void paint(Composite parent) throws Exception
  {
    table = control.getEigenschaftenAuswahlTable();
    LabelGroup group = new LabelGroup(parent, JVereinPlugin.getI18n().tr(
        "Eigenschaften"), true);
    group.addPart(table);

    ButtonArea buttons = new ButtonArea(parent, 2);
    buttons.addButton(i18n.tr(JVereinPlugin.getI18n().tr("OK")), new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        Object o = table.getSelection();
        selection = "";
        if (o instanceof HilfsEigenschaft)
        {
          HilfsEigenschaft he = (HilfsEigenschaft) o;
          try
          {
            selection = "[" + he.getEigenschaft() + "]";
          }
          catch (RemoteException e)
          {
            throw new ApplicationException(e);
          }
        }
        else if (o instanceof HilfsEigenschaft[])
        {
          HilfsEigenschaft[] he = (HilfsEigenschaft[]) o;
          for (HilfsEigenschaft h : he)
          {
            try
            {
              selection += "[" + h.getEigenschaft() + "]";
            }
            catch (RemoteException e)
            {
              throw new ApplicationException(e);
            }
          }
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
