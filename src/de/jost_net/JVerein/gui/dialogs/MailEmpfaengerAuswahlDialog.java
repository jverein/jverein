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
 * Revision 1.3  2010-03-27 20:10:26  jost
 * Auswahl überarbeitet.
 *
 * Revision 1.2  2010/03/05 21:55:33  jost
 * Künftiges Feature auskommentiert.
 *
 * Revision 1.1  2010/02/01 20:59:26  jost
 * Neu: Einfache Mailfunktion
 *
 **********************************************************************/

package de.jost_net.JVerein.gui.dialogs;

import java.rmi.RemoteException;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.control.MailControl;
import de.jost_net.JVerein.rmi.Eigenschaften;
import de.jost_net.JVerein.rmi.MailEmpfaenger;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.server.EigenschaftenNode;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.system.OperationCanceledException;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Ein Dialog, ueber den man Empfänger für eine Mail auswählen kann.
 */
public class MailEmpfaengerAuswahlDialog extends AbstractDialog
{
  private MailControl control;

  public MailEmpfaengerAuswahlDialog(MailControl control, int position)
  {
    super(position);
    this.control = control;
    setTitle(JVereinPlugin.getI18n().tr("Mail-Empfänger"));
    setSize(550, 450);
  }

  @Override
  protected void paint(Composite parent) throws Exception
  {
    control.getMitgliedMitMail().paint(parent);
    for (Object o : control.getMitgliedMitMail().getItems(true))
    {
      control.getMitgliedMitMail().setChecked(o, false);
    }

    ButtonArea b = new ButtonArea(parent, 5);
    b.addButton(JVereinPlugin.getI18n().tr("Eigenschaften"), new Action()
    {
      @SuppressWarnings("unchecked")
      public void handleAction(Object context) throws ApplicationException
      {
        try
        {
          EigenschaftenAuswahlDialog ead = new EigenschaftenAuswahlDialog(null);
          ArrayList<Object> auswahl = (ArrayList<Object>) ead.open();
          for (Object o : auswahl)
          {
            EigenschaftenNode node = (EigenschaftenNode) o;
            DBIterator it = Einstellungen.getDBService().createList(
                Eigenschaften.class);
            it.addFilter("eigenschaft = ?", new Object[] { node
                .getEigenschaft().getID() });
            while (it.hasNext())
            {
              Eigenschaften ei = (Eigenschaften) it.next();
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
    b.addButton(JVereinPlugin.getI18n().tr("alle"), new Action()
    {
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
          Logger.error("Fehler:", e);
        }
      }
    });
    b.addButton(JVereinPlugin.getI18n().tr("keinen"), new Action()
    {
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
    b.addButton(JVereinPlugin.getI18n().tr("übernehmen"), new Action()
    {
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
    b.addButton(JVereinPlugin.getI18n().tr("abbrechen"), new Action()
    {
      public void handleAction(Object context)
      {
        throw new OperationCanceledException();
      }
    });
  }

  @Override
  protected Object getData() throws Exception
  {
    return null;
  }

}
