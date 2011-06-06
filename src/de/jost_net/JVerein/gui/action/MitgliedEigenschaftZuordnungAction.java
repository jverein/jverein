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

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.dialogs.EigenschaftenAuswahlDialog;
import de.jost_net.JVerein.rmi.Eigenschaften;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.server.EigenschaftenNode;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Eigenschaften an Mitglieder zuordnen.
 */
public class MitgliedEigenschaftZuordnungAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null && !(context instanceof Mitglied)
        && !(context instanceof Mitglied[]))
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Kein Mitglied ausgewählt"));
    }
    Mitglied[] m = null;
    if (context instanceof Mitglied)
    {
      m = new Mitglied[] { (Mitglied) context };
    }
    else if (context instanceof Mitglied[])
    {
      m = (Mitglied[]) context;
    }
    int anzErfolgreich = 0;
    int anzBereitsVorhanden = 0;
    try
    {
      EigenschaftenAuswahlDialog ead = new EigenschaftenAuswahlDialog("", true);
      ArrayList<EigenschaftenNode> eigenschaft = (ArrayList<EigenschaftenNode>) ead
          .open();
      for (EigenschaftenNode en : eigenschaft)
      {
        for (Mitglied mit : m)
        {
          Eigenschaften eig = (Eigenschaften) Einstellungen.getDBService()
              .createObject(Eigenschaften.class, null);
          eig.setEigenschaft(en.getEigenschaft().getID());
          eig.setMitglied(mit.getID());
          try
          {
            eig.store();
            anzErfolgreich++;
          }
          catch (RemoteException e)
          {
            if (e.getCause() instanceof SQLException)
            {
              anzBereitsVorhanden++;
            }
            else
            {
              throw new ApplicationException(e);
            }
          }
        }
      }
    }
    catch (Exception e)
    {
      Logger
          .error(
              JVereinPlugin.getI18n().tr(
                  "Fehler beim Anlegen neuer Eigenschaften"), e);
      return;
    }
    GUI.getStatusBar().setSuccessText(
        JVereinPlugin.getI18n().tr(
            anzErfolgreich + " Eigenschaft(en) angelegt. "
                + anzBereitsVorhanden + " waren bereits vorhanden."));
  }
}
