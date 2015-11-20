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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.parts.ZusatzbetragPart;
import de.jost_net.JVerein.keys.IntervallZusatzzahlung;
import de.jost_net.JVerein.rmi.Zusatzbetrag;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.system.OperationCanceledException;
import de.willuhn.logging.Logger;

/**
 * Dialog zur Zuordnung von Zusatzbeträgen
 */
public class MitgliedZusatzbetragZuordnungDialog extends
    AbstractDialog<Zusatzbetrag>
{
  private ZusatzbetragPart part;

  private Zusatzbetrag zusatzbetrag;

  /**
   * @param position
   */
  public MitgliedZusatzbetragZuordnungDialog(int position)
  {
    super(position);
    setTitle("Zuordnung Zusatzbetrag");
  }

  @Override
  protected void paint(Composite parent) throws Exception
  {
    Zusatzbetrag zb = (Zusatzbetrag) Einstellungen.getDBService().createObject(
        Zusatzbetrag.class, null);
    part = new ZusatzbetragPart(zb);
    part.paint(parent);

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("zuordnen", new Action()
    {
      @Override
      public void handleAction(Object context)
      {
        try
        {
          zusatzbetrag = (Zusatzbetrag) Einstellungen.getDBService()
              .createObject(Zusatzbetrag.class, null);
          zusatzbetrag.setAusfuehrung((Date) part.getAusfuehrung().getValue());
          zusatzbetrag.setBetrag((Double) part.getBetrag().getValue());
          zusatzbetrag.setBuchungstext((String) part.getBuchungstext()
              .getValue());
          zusatzbetrag.setEndedatum((Date) part.getEndedatum().getValue());
          zusatzbetrag.setFaelligkeit((Date) part.getFaelligkeit().getValue());
          IntervallZusatzzahlung iz = (IntervallZusatzzahlung) part
              .getIntervall().getValue();
          zusatzbetrag.setIntervall(iz.getKey());
          zusatzbetrag
              .setStartdatum((Date) part.getStartdatum(true).getValue());
        }
        catch (RemoteException e)
        {
          Logger.error("Fehler", e);
        }
        close();
      }
    }, null, true);
    buttons.addButton("abbrechen", new Action()
    {
      @Override
      public void handleAction(Object context)
      {
        throw new OperationCanceledException();
      }
    });
    buttons.paint(parent);
    getShell().setMinimumSize(getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT));
  }

  /**
   * @see de.willuhn.jameica.gui.dialogs.AbstractDialog#getData()
   */
  @Override
  public Zusatzbetrag getData() throws Exception
  {
    return zusatzbetrag;
  }
}
