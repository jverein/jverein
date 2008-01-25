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
 **********************************************************************/
package de.jost_net.JVerein.gui.dialogs;

import java.rmi.RemoteException;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.gui.control.EigenschaftenControl;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.Color;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

/**
 * Dialog, zur Eingabe neuer Eigenschaften zu einem Mitglied.
 */
public class EigenschaftenNeuDialog extends AbstractDialog
{
  private EigenschaftenControl control;

  public EigenschaftenNeuDialog(Mitglied m) throws RemoteException
  {
    super(EigenschaftenNeuDialog.POSITION_CENTER);
    this.setSize(400, 400);
    setTitle("Neue Eigenschaften zu " + m.getNameVorname());
    control = new EigenschaftenControl(null, m);
  }

  protected void paint(Composite parent) throws Exception
  {
    LabelGroup group = new LabelGroup(parent, "Existierende Eigenschaften",
        true);
    group.addPart(control.getEigenschaftenNeuTable());
    group.addLabelPair("Neue Eigenschaft", control.getNeu());

    ButtonArea buttons = new ButtonArea(parent, 3);
    buttons.addButton(i18n.tr("<< Zurück"), new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        close();
      }
    });
    buttons.addButton(i18n.tr("Speichern"), new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        String input = (String) control.getNeu().getValue();
        if (input.length() == 0)
        {
          throw new ApplicationException(
              "Bitte mit Doppelklick Eigenschaft auswählen oder über die Tastatur eingeben");
        }
        try
        {
          control.handleStore();
          close();
        }
        catch (RemoteException e)
        {
          if (e.getCause().getMessage().indexOf("Index oder ") > -1)
          {
            control.getStatusbar().setColor(Color.ERROR);
            control.getStatusbar().setValue("Eigenschaft schon vorhanden");
          }
          else
          {
            throw new ApplicationException(e);
          }
        }
      }
    });
    LabelGroup status = new LabelGroup(parent, "");
    status.addLabelPair("", control.getStatusbar());
  }

  protected Object getData() throws Exception
  {
    return null;
  }
}
