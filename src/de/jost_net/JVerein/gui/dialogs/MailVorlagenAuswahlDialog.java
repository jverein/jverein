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

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.gui.control.MailVorlageControl;
import de.jost_net.JVerein.rmi.MailVorlage;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.util.ApplicationException;

/**
 * Ein Dialog, ueber den die Vorlage für eine Mail ausgewählt werden kann.
 */
public class MailVorlagenAuswahlDialog extends AbstractDialog<MailVorlage>
{

  private MailVorlageControl control;

  private MailVorlage retval;

  public MailVorlagenAuswahlDialog(MailVorlageControl control, int position)
  {
    super(position);
    this.control = control;
    setTitle("Mail-Vorlage");
    setSize(550, 450);
  }

  @Override
  protected void paint(Composite parent) throws Exception
  {

    control.getMailVorlageTable().paint(parent);

    ButtonArea b = new ButtonArea();
    b.addButton("verwenden", new Action()
    {

      @Override
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
    b.addButton("ohne Mail-Vorlage", new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        close();
      }
    });
    b.paint(parent);
  }

  @Override
  protected MailVorlage getData() throws Exception
  {
    return retval;
  }

}
