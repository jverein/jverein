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
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.io.Adressbuch.Adressaufbereitung;
import de.jost_net.JVerein.rmi.Mail;
import de.jost_net.JVerein.rmi.MailEmpfaenger;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Loeschen von Mailempfaengern.
 */
public class MailemfaengerDeleteAction implements Action
{

  private MitgliedControl mc;

  public MailemfaengerDeleteAction(MitgliedControl mc)
  {
    this.mc = mc;
  }

  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null
        || (!(context instanceof Mail) && !(context instanceof Mail[])))
    {
      throw new ApplicationException("Keine Mail ausgewählt");
    }
    try
    {
      Mail[] mail = null;
      if (context instanceof Mail)
      {
        mail = new Mail[1];
        mail[0] = (Mail) context;
      }
      if (context instanceof Mail[])
      {
        mail = (Mail[]) context;
      }
      if (mail != null && mail.length > 0 && mail[0].isNewObject())
      {
        return;
      }
      Mitglied mitglied = (Mitglied) Einstellungen.getDBService()
          .createObject(Mitglied.class, mc.getMitglied().getID());
      YesNoDialog d = new YesNoDialog(YesNoDialog.POSITION_CENTER);
      if (mail == null)
      {
        return;
      }
      d.setTitle("Mail" + (mail.length > 1 ? "s" : "") + " von "
          + Adressaufbereitung.getVornameName(mitglied) + " löschen");
      d.setText("Wollen Sie diese Mail" + (mail.length > 1 ? "s" : "") + " von "
          + Adressaufbereitung.getVornameName(mitglied) + " wirklich löschen?");

      try
      {
        Boolean choice = (Boolean) d.open();
        if (!choice.booleanValue())
        {
          return;
        }
      }
      catch (Exception e)
      {
        Logger.error("Fehler beim Löschen der Mail", e);
        return;
      }
      for (Mail ma : mail)
      {
        DBIterator<MailEmpfaenger> me = Einstellungen.getDBService()
            .createList(MailEmpfaenger.class);
        me.addFilter("mail = ?", ma.getID());
        me.addFilter("mitglied = ?", mc.getMitglied().getID());
        while (me.hasNext())
        {
          MailEmpfaenger me2 = me.next();
          MailEmpfaenger me3 = (MailEmpfaenger) Einstellungen.getDBService()
              .createObject(MailEmpfaenger.class, me2.getID());
          me3.delete();
        }
        mc.getMailTable().removeItem(ma);
      }
      GUI.getStatusBar().setSuccessText(

          "Mail" + (mail.length > 1 ? "s" : "") + " gelöscht.");
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler beim Löschen der Mail.";
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
