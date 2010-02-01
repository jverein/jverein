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
package de.jost_net.JVerein.gui.action;

import java.util.TreeSet;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.control.MailVorlageControl;
import de.jost_net.JVerein.gui.dialogs.MailVorlagenAuswahlDialog;
import de.jost_net.JVerein.gui.view.MailDetailView;
import de.jost_net.JVerein.rmi.Mail;
import de.jost_net.JVerein.rmi.MailEmpfaenger;
import de.jost_net.JVerein.rmi.MailVorlage;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class MailDetailAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    Mail m = null;

    if (context != null && (context instanceof Mail))
    {
      m = (Mail) context;
    }
    else
    {
      try
      {
        MailVorlagenAuswahlDialog mvad = new MailVorlagenAuswahlDialog(
            new MailVorlageControl(null),
            MailVorlagenAuswahlDialog.POSITION_CENTER);
        m = (Mail) Einstellungen.getDBService().createObject(Mail.class, null);
        MailVorlage mv = (MailVorlage) mvad.open();
        if (mv != null)
        {
          m.setBetreff(mv.getBetreff());
          m.setTxt(mv.getTxt());
        }
       }
      catch (Exception e)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Fehler bei der Erzeugung der neuen Mail"), e);
      }
    }
    GUI.startView(MailDetailView.class.getName(), m);
  }
}
