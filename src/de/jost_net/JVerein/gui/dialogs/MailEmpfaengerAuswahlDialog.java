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

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.control.MailControl;
import de.jost_net.JVerein.rmi.Eigenschaften;
import de.jost_net.JVerein.rmi.MailEmpfaenger;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.server.EigenschaftenNode;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.system.OperationCanceledException;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Ein Dialog, ueber den man Empfänger für eine Mail auswählen kann.
 */
public class MailEmpfaengerAuswahlDialog extends AbstractDialog<Object>
{

  private MailControl control;

  public MailEmpfaengerAuswahlDialog(MailControl control, int position)
  {
    super(position);
    this.control = control;
    setTitle("Mail-Empfänger");
    setSize(1000, 450);
  }

  @Override
  protected void paint(Composite parent) throws Exception
  {
    control.getMitgliedMitMail().paint(parent);
    for (Object o : control.getMitgliedMitMail().getItems(true))
    {
      control.getMitgliedMitMail().setChecked(o, false);
    }

    ButtonArea b = new ButtonArea();

    b.addButton("Eigenschaften", new Action()
    {
      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        try
        {
          EigenschaftenAuswahlDialog ead = new EigenschaftenAuswahlDialog(null,
              false, true);
          EigenschaftenAuswahlParameter param = ead.open();
          for (EigenschaftenNode node : param.getEigenschaften())
          {
            DBIterator<Eigenschaften> it = Einstellungen.getDBService()
                .createList(Eigenschaften.class);
            it.addFilter("eigenschaft = ?",
                new Object[] { node.getEigenschaft().getID() });
            while (it.hasNext())
            {
              Eigenschaften ei = it.next();
              control.getMitgliedMitMail().setChecked(ei.getMitglied(), true);
            }
          }
        }
        catch (Exception e)
        {
          throw new ApplicationException("Fehler" + e);
        }
      }
    });

    b.addButton("aktive Mitglieder", new Action()
    {
      @Override
      public void handleAction(Object context)
      {
        try
        {
          Date stichtag = new Date();
          for (Object o : control.getMitgliedMitMail().getItems(false))
          {
            Mitglied m = (Mitglied) o;
            if (m.getAdresstyp().getJVereinid() == 1
                && m.isAngemeldet(stichtag))
            {
              control.getMitgliedMitMail().setChecked(o, true);
            }
          }
        }
        catch (RemoteException e)
        {
          Logger.error("Fehler", e);
        }
      }
    });

    b.addButton("inaktive Mitglieder", new Action()
    {
      @Override
      public void handleAction(Object context)
      {
        try
        {
          Date stichtag = new Date();
          for (Object o : control.getMitgliedMitMail().getItems(false))
          {
            Mitglied m = (Mitglied) o;
            if (m.getAdresstyp().getJVereinid() == 1
                && !m.isAngemeldet(stichtag))
            {
              control.getMitgliedMitMail().setChecked(o, true);
            }
          }
        }
        catch (RemoteException e)
        {
          Logger.error("Fehler", e);
        }
      }
    });

    b.addButton("alle Adressen", new Action()
    {
      @Override
      public void handleAction(Object context)
      {
        try
        {
          for (Object o : control.getMitgliedMitMail().getItems(false))
          {
            Mitglied m = (Mitglied) o;
            if (m.getAdresstyp().getJVereinid() != 1)
            {
              control.getMitgliedMitMail().setChecked(o, true);
            }
          }
        }
        catch (RemoteException e)
        {
          Logger.error("Fehler", e);
        }
      }
    });

    b.addButton("aktive Mitglieder und Adressen", new Action()
    {
      @Override
      public void handleAction(Object context)
      {
        try
        {
          Date stichtag = new Date();
          for (Object o : control.getMitgliedMitMail().getItems(false))
          {
            Mitglied m = (Mitglied) o;
            if (m.getAdresstyp().getJVereinid() != 1
                || (m.getAdresstyp().getJVereinid() == 1
                    && m.isAngemeldet(stichtag)))
            {
              control.getMitgliedMitMail().setChecked(o, true);
            }
          }
        }
        catch (RemoteException e)
        {
          Logger.error("Fehler", e);
        }
      }
    });

    b.paint(parent);

    ButtonArea c = new ButtonArea();
    c.addButton("alle", new Action()
    {
      @Override
      public void handleAction(Object context)
      {
        try
        {
          for (Object o : control.getMitgliedMitMail().getItems(false))
          {
            control.getMitgliedMitMail().setChecked(o, true);
          }
        }
        catch (RemoteException e)
        {
          Logger.error("Fehler", e);
        }
      }
    });

    c.addButton("keinen", new Action()
    {
      @Override
      public void handleAction(Object context)
      {
        try
        {
          for (Object o : control.getMitgliedMitMail().getItems(false))
          {
            control.getMitgliedMitMail().setChecked(o, false);
          }
        }
        catch (RemoteException e)
        {
          Logger.error("Fehler", e);
        }
      }
    });
    c.addButton("übernehmen", new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        try
        {
          for (Object o : control.getMitgliedMitMail().getItems())
          {
            Mitglied m = (Mitglied) o;
            MailEmpfaenger me = (MailEmpfaenger) Einstellungen.getDBService()
                .createObject(MailEmpfaenger.class, null);
            me.setMitglied(m);
            control.addEmpfaenger(me);
          }
        }
        catch (RemoteException e)
        {
          Logger.error("Fehler", e);
        }
        close();
      }
    });

    c.addButton("abbrechen", new Action()
    {
      @Override
      public void handleAction(Object context)
      {
        throw new OperationCanceledException();
      }
    });

    c.paint(parent);
  }

  @Override
  protected Object getData() throws Exception
  {
    return null;
  }

}