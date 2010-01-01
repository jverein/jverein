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

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.view.SpendenbescheinigungView;
import de.jost_net.JVerein.rmi.Spendenbescheinigung;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Spendenbescheinigung duplizieren
 */
public class SpendenbescheinigungDuplizierenAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null || !(context instanceof Spendenbescheinigung))
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Keine Spendenbescheinigung ausgewählt"));
    }
    try
    {
      Spendenbescheinigung spb = (Spendenbescheinigung) context;
      Spendenbescheinigung spb2 = (Spendenbescheinigung) Einstellungen
          .getDBService().createObject(Spendenbescheinigung.class, null);
      spb2.setBetrag(spb.getBetrag());
      spb2.setErsatzAufwendungen(spb.getErsatzAufwendungen());
      spb2.setFormular(spb.getFormular());
      spb2.setZeile1(spb.getZeile1());
      spb2.setZeile2(spb.getZeile2());
      spb2.setZeile3(spb.getZeile3());
      spb2.setZeile4(spb.getZeile4());
      spb2.setZeile5(spb.getZeile5());
      spb2.setZeile6(spb.getZeile6());
      spb2.setZeile7(spb.getZeile7());
      GUI.startView(SpendenbescheinigungView.class.getName(), spb2);
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Fehler beim Duplizieren der Spendenbescheinigung");
      GUI.getStatusBar().setErrorText(fehler);
      Logger.error(fehler, e);
    }
  }
}
