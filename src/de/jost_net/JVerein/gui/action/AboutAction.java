/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * jost@berlios.de
 * jverein.berlios.de
 * $Log$
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import de.jost_net.JVerein.gui.view.AboutView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class AboutAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    try
    {
      new AboutView(AbstractDialog.POSITION_CENTER).open();
    }
    catch (Exception e)
    {
      String fehler = "Fehler beim öffnen des AboutView-Dialoges";
      Logger.error(fehler, e);
      throw new ApplicationException(fehler);
    }
  }
}
