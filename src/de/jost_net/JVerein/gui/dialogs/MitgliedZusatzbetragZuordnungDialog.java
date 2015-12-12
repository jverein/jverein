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
import de.jost_net.JVerein.gui.action.ZusatzbetragVorlageAuswahlAction;
import de.jost_net.JVerein.gui.parts.ZusatzbetragPart;
import de.jost_net.JVerein.keys.IntervallZusatzzahlung;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Zusatzbetrag;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.dialogs.SimpleDialog;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.system.OperationCanceledException;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Dialog zur Zuordnung von Zusatzbeträgen
 */
public class MitgliedZusatzbetragZuordnungDialog extends AbstractDialog<String>
{
  private ZusatzbetragPart part;

  private Mitglied[] m;

  private String message = "";

  /**
   * @param position
   */
  public MitgliedZusatzbetragZuordnungDialog(int position, Mitglied[] m)
  {
    super(position);
    setTitle("Zuordnung Zusatzbetrag");
    this.m = m;
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
        int count = 0;
        try
        {
          for (Mitglied mit : m)
          {
            Zusatzbetrag zb = (Zusatzbetrag) Einstellungen.getDBService()
                .createObject(Zusatzbetrag.class, null);
            zb.setAusfuehrung((Date) part.getAusfuehrung().getValue());
            zb.setBetrag((Double) part.getBetrag().getValue());
            zb.setBuchungstext((String) part.getBuchungstext().getValue());
            zb.setEndedatum((Date) part.getEndedatum().getValue());
            zb.setFaelligkeit((Date) part.getFaelligkeit().getValue());
            IntervallZusatzzahlung iz = (IntervallZusatzzahlung) part
                .getIntervall().getValue();
            zb.setIntervall(iz.getKey());
            zb.setMitglied(Integer.parseInt(mit.getID()));
            zb.setStartdatum((Date) part.getStartdatum(true).getValue());
            zb.store();
            count++;
          }
          message = String.format("%d Arbeitseinsätze gespeichert.", count);
        }
        catch (RemoteException e)
        {
          Logger.error("Fehler", e);
        }
        catch (ApplicationException e)
        {
          SimpleDialog sd = new SimpleDialog(AbstractDialog.POSITION_CENTER);
          sd.setText(e.getMessage());
          sd.setTitle("Fehler");
          try
          {
            sd.open();
          }
          catch (Exception e1)
          {
            Logger.error("Fehler", e1);
          }
          return;
        }

        close();
      }
    }, null, true);
    buttons.addButton("Vorlagen", new ZusatzbetragVorlageAuswahlAction(part));

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
  public String getData() throws Exception
  {
    return message;
  }
}
