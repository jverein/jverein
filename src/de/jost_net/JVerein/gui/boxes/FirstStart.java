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
 * Revision 1.1  2007/12/28 13:09:38  jost
 * Neue FirstStart-Box
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.boxes;

import java.rmi.RemoteException;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.BeitragsgruppeSucheAction;
import de.jost_net.JVerein.gui.action.EinstellungenAction;
import de.jost_net.JVerein.gui.action.StammdatenAction;
import de.willuhn.jameica.gui.boxes.AbstractBox;
import de.willuhn.jameica.gui.parts.FormTextPart;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.hbci.HBCI;
import de.willuhn.jameica.plugin.Manifest;
import de.willuhn.jameica.system.Application;

/**
 * Hilfe-Seite fuer den ersten Start.
 */
public class FirstStart extends AbstractBox
{

  public boolean isActive()
  {
    // Diese Box kann nur beim ersten Start ausgewaehlt/angezeigt werden.
    return Einstellungen.isFirstStart();
  }

  public boolean getDefaultEnabled()
  {
    return Einstellungen.isFirstStart();
  }

  public int getDefaultIndex()
  {
    return 0;
  }

  public String getName()
  {
    return "JVerein: Erste Schritte";
  }

  public boolean isEnabled()
  {
    // Diese Box kann nur beim ersten erfolgreichen Start ausgewaehlt/angezeigt
    // werden.
    Manifest mf = Application.getPluginLoader().getManifest(HBCI.class);
    return mf.isInstalled() && Einstellungen.isFirstStart();
  }

  public void paint(Composite parent) throws RemoteException
  {
    FormTextPart text = new FormTextPart();
    text.setText("<form><p><span color=\"header\" font=\"header\">"
        + "Herzlich willkommen" + "</span></p>" + "<p>"
        + "JVerein wird zum ersten Mal gestartet. Die Stammdaten des Vereins "
        + "und die Beitragsgruppe(n) sind zu erfassen. Auﬂerdem kann das "
        + "Verhalten von JVerein durch die Einstellungen beeinflusst werden."
        + "</p></form>");

    text.paint(parent);

    ButtonArea buttons = new ButtonArea(parent, 3);
    buttons.addButton("Stammdaten", new StammdatenAction(), null, true);
    buttons.addButton("Beitragsgruppen", new BeitragsgruppeSucheAction(), null);
    buttons.addButton("Einstellungen", new EinstellungenAction(), null);
  }
}
