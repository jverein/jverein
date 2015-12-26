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
import java.util.Date;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.gui.parts.DokumentPart;
import de.jost_net.JVerein.rmi.AbstractDokument;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.system.OperationCanceledException;

/**
 * Ein Dialog, zur Bearbeitung von Dokument-Infos
 * */
public class DokumentDialog extends AbstractDialog<AbstractDokument>
{

  private AbstractDokument dok = null;

  private DokumentPart part = null;

  public DokumentDialog(AbstractDokument dok)
  {
    super(AbstractDialog.POSITION_CENTER);
    this.dok = dok;

    setTitle("Dokument-Infos");
    setSize(850, 350);
  }

  @Override
  protected void paint(Composite parent) throws Exception
  {
    getPart().paint(parent);
    ButtonArea b = new ButtonArea();
    b.addButton("speichern", new Action()
    {
      @Override
      public void handleAction(Object context)
      {
        try
        {
          dok.setDatum((Date) part.getDatum().getValue());
          dok.setBemerkung((String) part.getBemerkung().getValue());
        }
        catch (RemoteException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        close();
      }
    });
    b.addButton("abbrechen", new Action()
    {
      @Override
      public void handleAction(Object context)
      {
        throw new OperationCanceledException();
      }
    });
    b.paint(parent);
  }

  @Override
  protected AbstractDokument getData() throws Exception
  {
    return this.dok;
  }

  private DokumentPart getPart()
  {
    if (part != null)
    {
      return part;
    }
    part = new DokumentPart(dok);
    return part;
  }

}
