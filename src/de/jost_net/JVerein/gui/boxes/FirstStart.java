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
package de.jost_net.JVerein.gui.boxes;

import java.rmi.RemoteException;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.BeitragsgruppeSucheAction;
import de.jost_net.JVerein.gui.action.EinstellungenAction;
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

  @Override
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
    return JVereinPlugin.getI18n().tr("JVerein: Erste Schritte");
  }

  @Override
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
        + JVereinPlugin.getI18n().tr("Herzlich willkommen")
        + "</span></p>"
        + "<p>"
        + JVereinPlugin
            .getI18n()
            .tr("JVerein wird zum ersten Mal gestartet. Die allgemeinen Daten des Vereins "
                + "(Name, eigene Bankverbindung) sowie Parameter zur Steuerung des Verhaltens von JVerein "
                + "sind in den Einstellung zu erfassen. Auﬂerdem sind Beitragsgruppen zur erfassen. ")
        + "</p></form>");

    text.paint(parent);

    ButtonArea buttons = new ButtonArea(parent, 2);
    buttons.addButton(JVereinPlugin.getI18n().tr("Einstellungen"),
        new EinstellungenAction(), null);
    buttons.addButton(JVereinPlugin.getI18n().tr("Beitragsgruppen"),
        new BeitragsgruppeSucheAction(), null);
  }

  @Override
  public int getHeight()
  {
    return 160;
  }
}
