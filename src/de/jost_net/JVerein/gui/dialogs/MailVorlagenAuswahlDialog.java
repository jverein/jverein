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
 **********************************************************************/

package de.jost_net.JVerein.gui.dialogs;

import java.rmi.RemoteException;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.control.MailVorlageControl;
import de.jost_net.JVerein.rmi.MailVorlage;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.util.ApplicationException;

/**
 * Ein Dialog, ueber den die Vorlage für eine Mail ausgewählt werden kann.
 */
public class MailVorlagenAuswahlDialog extends AbstractDialog
{
  private MailVorlageControl control;

  private MailVorlage retval;

  public MailVorlagenAuswahlDialog(MailVorlageControl control, int position)
  {
    super(position);
    this.control = control;
    setTitle(JVereinPlugin.getI18n().tr("Mail-Vorlage"));
    setSize(550, 450);
  }

  @Override
  protected void paint(Composite parent) throws Exception
  {

    control.getMailVorlageTable().paint(parent);

    ButtonArea b = new ButtonArea(parent, 2);
    b.addButton(JVereinPlugin.getI18n().tr("verwenden"), new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        try
        {
          retval = (MailVorlage) control.getMailVorlageTable().getSelection();
        }
        catch (RemoteException e)
        {
          throw new ApplicationException(e.getMessage());
        }
        close();
      }
    });
    b.addButton(JVereinPlugin.getI18n().tr("ohne Mail-Vorlage"), new Action()
    {
      public void handleAction(Object context)
      {
        close();
      }
    });
  }

  @Override
  protected Object getData() throws Exception
  {
    return retval;
  }

}
