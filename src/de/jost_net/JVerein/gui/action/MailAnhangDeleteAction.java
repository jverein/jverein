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
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.control.MailControl;
import de.jost_net.JVerein.rmi.MailAnhang;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Löschen eines Mailanhanges
 */
public class MailAnhangDeleteAction implements Action
{
  private MailControl control = null;

  public MailAnhangDeleteAction(MailControl control)
  {
    this.control = control;
  }

  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null || !(context instanceof MailAnhang))
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Keinen Mail-Anhang ausgewählt"));
    }
    try
    {
      MailAnhang ma = (MailAnhang) context;
      control.removeAnhang(ma);
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Fehler beim entfernen eines Mailanhanges");
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
