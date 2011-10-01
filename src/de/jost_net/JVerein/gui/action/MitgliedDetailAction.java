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

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.control.FamilienbeitragNode;
import de.jost_net.JVerein.gui.dialogs.PersonenartDialog;
import de.jost_net.JVerein.gui.view.MitgliedDetailView;
import de.jost_net.JVerein.io.ArbeitseinsatzZeile;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.ApplicationException;

public class MitgliedDetailAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    Mitglied m = null;
    try
    {
      if (context != null && context instanceof FamilienbeitragNode)
      {
        FamilienbeitragNode fbn = (FamilienbeitragNode) context;
        m = (Mitglied) fbn.getMitglied();
      }
      else if (context != null && context instanceof ArbeitseinsatzZeile)
      {
        ArbeitseinsatzZeile aez = (ArbeitseinsatzZeile) context;
        m = (Mitglied) aez.getAttribute("mitglied");
      }
      else if (context != null && (context instanceof Mitglied))
      {
        m = (Mitglied) context;
      }
      else if (context != null && (context instanceof Mitgliedskonto))
      {
        Mitgliedskonto mk = (Mitgliedskonto) context;
        m = mk.getMitglied();
      }
      else
      {
        m = (Mitglied) Einstellungen.getDBService().createObject(
            Mitglied.class, null);
        if (Einstellungen.getEinstellung().getJuristischePersonen())
        {
          PersonenartDialog pad = new PersonenartDialog(
              PersonenartDialog.POSITION_CENTER);
          String pa = (String) pad.open();
          m.setPersonenart(pa);
        }
        else
        {
          m.setPersonenart("n");
        }
      }
    }
    catch (Exception e)
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Fehler bei der Erzeugung eines neuen Mitgliedes"), e);
    }
    GUI.startView(new MitgliedDetailView(), m);
  }
}
