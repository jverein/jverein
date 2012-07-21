/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
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
package de.jost_net.JVerein.gui.boxes;

import java.rmi.RemoteException;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.BeitragsgruppeSucheAction;
import de.jost_net.JVerein.gui.action.EinstellungenAction;
import de.willuhn.jameica.gui.boxes.AbstractBox;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.parts.FormTextPart;
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

    ButtonArea buttons = new ButtonArea();
    buttons.addButton(JVereinPlugin.getI18n().tr("Einstellungen"),
        new EinstellungenAction(), null);
    buttons.addButton(JVereinPlugin.getI18n().tr("Beitragsgruppen"),
        new BeitragsgruppeSucheAction(), null);
    buttons.paint(parent);
  }

  @Override
  public int getHeight()
  {
    return 160;
  }
}
