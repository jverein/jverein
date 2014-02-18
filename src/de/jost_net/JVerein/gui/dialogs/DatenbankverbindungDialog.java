/**********************************************************************
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

package de.jost_net.JVerein.gui.dialogs;

import java.rmi.RemoteException;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.keys.Datenbanktreiber;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.input.PasswordInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.OperationCanceledException;
import de.willuhn.jameica.system.Settings;

/**
 * Bearbeitung einer Bankverbindung.
 */
public class DatenbankverbindungDialog extends AbstractDialog<Settings>
{

  private Settings settings;

  private SelectInput driver = null;

  private TextInput url = null;

  private TextInput user = null;

  private PasswordInput password = null;

  /**
   * @param position
   * @throws RemoteException
   */
  public DatenbankverbindungDialog(Settings settings)
  {
    super(AbstractDialog.POSITION_CENTER);
    setTitle("Datenbankverbindung");
    this.settings = settings;
    setSize(400, 400);
  }

  @Override
  protected void paint(Composite parent) throws Exception
  {
    LabelGroup group = new LabelGroup(parent, " ");
    group.addInput(getDriver());
    group.addInput(getURL());
    group.addInput(getUser());
    group.addInput(getPassword());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("übernehmen", new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        // try
        // {
        // bankverbindung.setBlz((String) getBLZ().getValue());
        // bankverbindung.setKonto((String) getKonto().getValue());
        // bankverbindung.setBic((String) getBIC().getValue());
        // bankverbindung.setIban((String) getIBAN().getValue());
        // }
        // catch (RemoteException e)
        // {
        // status.setValue(e.getMessage());
        // }
        close();
      }
    }, null, true);
    buttons.addButton("abbrechen", new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        throw new OperationCanceledException();
      }
    });
    buttons.paint(parent);
  }

  /**
   * @see de.willuhn.jameica.gui.dialogs.AbstractDialog#getData()
   */
  @Override
  public Settings getData() throws Exception
  {
    return settings;
  }

  private SelectInput getDriver() throws RemoteException
  {
    if (driver != null)
    {
      return driver;
    }
    String[] drivers = { Datenbanktreiber.H2.getBezeichnung(),
        Datenbanktreiber.MYSQL.getBezeichnung() };
    driver = new SelectInput(drivers, settings.getString("jdbc.driver",
        Datenbanktreiber.H2.getBezeichnung()));
    driver.setName("Treiber");
    return driver;
  }

  private TextInput getURL() throws RemoteException
  {
    if (url != null)
    {
      return url;
    }
    String defaulturl = "";
    String drv = (String) getDriver().getValue();
    if (drv.equals(Datenbanktreiber.H2.getBezeichnung()))
    {
      String dir = Application.getPluginLoader().getPlugin(JVereinPlugin.class)
          .getResources().getWorkPath() +"/h2db/jverein";
      defaulturl = "jdbc:h2:" + dir;

    }
    if (drv.equals(Datenbanktreiber.MYSQL.getBezeichnung()))
    {
      defaulturl = "jdbc:mysql://localhost/jverein";
    }
    url = new TextInput(settings.getString("jdbc.url", defaulturl), 50);
    url.setName("URL");
    return url;
  }

  private TextInput getUser() throws RemoteException
  {
    if (user != null)
    {
      return user;
    }
    user = new TextInput(settings.getString("jdbc.user", "jverein"), 50);
    user.setName("Benutzer");
    return user;
  }

  private PasswordInput getPassword() throws RemoteException
  {
    if (password != null)
    {
      return password;
    }
    password = new PasswordInput(settings.getString("jdbc.password", "jverein"));
    password.setName("Passwort");
    return password;
  }
}
