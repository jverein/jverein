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
package de.jost_net.JVerein;

import java.io.File;

import de.willuhn.datasource.db.EmbeddedDatabase;
import de.willuhn.jameica.plugin.AbstractPlugin;
import de.willuhn.jameica.plugin.PluginResources;
import de.willuhn.jameica.system.Application;
import de.willuhn.util.ApplicationException;

/**
 * You need to have at least one class wich inherits from
 * <code>AbstractPlugin</code>. If so, Jameica will detect your plugin
 * automatically at startup.
 */
public class JVereinPlugin extends AbstractPlugin
{

  /**
   * constructor.
   * 
   * @param file
   *          the plugin file (your jar or the plugin directory).
   */
  public JVereinPlugin(File file)
  {
    super(file);
  }

  /**
   * This method is invoked on every startup. You can make here some stuff to
   * init your plugin. If you get some errors here and you dont want to activate
   * the plugin, simply throw an ApplicationException.
   * 
   * @see de.willuhn.jameica.plugin.AbstractPlugin#init()
   */
  public void init() throws ApplicationException
  {
    //
  }

  /**
   * This method is called only the first time, the plugin is loaded (before
   * executing init()). if your installation procedure was not successfull,
   * throw an ApplicationException.
   * 
   * @see de.willuhn.jameica.plugin.AbstractPlugin#install()
   */
  public void install() throws ApplicationException
  {
    // If we are running in client/server mode and this instance
    // is the client, we do not need to create a database.
    // Instead of this we will get our objects via RMI from
    // the server
    if (Application.inClientMode())
      return;
    try
    {

      // Let's create an embedded Database
      PluginResources res = Application.getPluginLoader().getPlugin(
          JVereinPlugin.class).getResources();
      EmbeddedDatabase db = new EmbeddedDatabase(res.getWorkPath() + "/db",
          "exampleuser", "examplepassword");

      // create the sql tables.
      db.executeSQLScript(new File(res.getPath() + "/sql/create.sql"));

      // That's all. Database installed and tables created ;)
    }
    catch (Exception e)
    {
      throw new ApplicationException("error while installing plugin", e);
    }
  }

  /**
   * This method will be executed on every version change.
   * 
   * @see de.willuhn.jameica.plugin.AbstractPlugin#update(double)
   */
  public void update(double oldVersion) throws ApplicationException
  {
  }

  /**
   * Here you can do some cleanup stuff. The method will be called on every
   * clean shutdown of jameica.
   * 
   * @see de.willuhn.jameica.plugin.AbstractPlugin#shutDown()
   */
  public void shutDown()
  {
  }
}
