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
 * Revision 1.2  2009/06/11 21:01:40  jost
 * Vorbereitung I18N
 *
 * Revision 1.1  2008/05/22 06:45:35  jost
 * Buchführung
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.dialogs.KontoAuswahlDialog;
import de.jost_net.JVerein.gui.view.AnfangsbestandView;
import de.jost_net.JVerein.rmi.Anfangsbestand;
import de.jost_net.JVerein.rmi.Konto;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.system.OperationCanceledException;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class AnfangsbestandNeuAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    Anfangsbestand anf;
    try
    {
      anf = (Anfangsbestand) Einstellungen.getDBService().createObject(
          Anfangsbestand.class, null);
      Konto k;
      if (context instanceof Konto)
      {
        k = (Konto) context;
        anf.setKonto(k);
      }
      else
      {
        KontoAuswahlDialog d = new KontoAuswahlDialog(
            KontoAuswahlDialog.POSITION_CENTER, false);
        try
        {
          context = d.open();
          k = (Konto) context;
          anf.setKonto(k);
        }
        catch (OperationCanceledException oce)
        {
          GUI.getStatusBar().setErrorText(
              JVereinPlugin.getI18n().tr("Vorgang abgebrochen"));
          return;
        }
        catch (Exception e)
        {
          Logger.error("error while choosing konto", e);
          GUI.getStatusBar().setErrorText(
              JVereinPlugin.getI18n().tr("Fehler bei der Auswahl des Kontos."));
        }
      }
      GUI.startView(AnfangsbestandView.class, anf);
    }
    catch (RemoteException e)
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Kann kein Anfangsbestand-Objekt erzeugen"));
    }
  }
}
