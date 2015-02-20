/**********************************************************************
 * JVerein - Mitgliederverwaltung und einfache Buchhaltung für Vereine
 * Copyright (c) by Heiner Jostkleigrewe
 * Copyright (c) 2015 by Thomas Hooge
 * Main Project: heiner@jverein.dem  http://www.jverein.de/
 * Module Author: thomas@hoogi.de, http://www.hoogi.de/
 *
 * This file is part of JVerein.
 *
 * JVerein is free software: you can redistribute it and/or modify 
 * it under the terms of the  GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JVerein is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.io.File;
import java.io.FileInputStream;

import de.jost_net.JVerein.JVereinPlugin;
import de.willuhn.io.FileFinder;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.parts.FormTextPart;
import de.willuhn.jameica.plugin.AbstractPlugin;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.util.InfoReader;
import de.willuhn.logging.Logger;
import de.willuhn.util.I18N;

/**
 * Controller für die Lizenzinformationen.
 */
public class LizenzControl extends AbstractControl
{

  private FormTextPart libList = null;

  /**
   * ct.
   * 
   * @param view
   */
  public LizenzControl(AbstractView view)
  {
    super(view);
  }

  /**
   * Liefert eine Liste mit allen direkt von JVerein verwendeten Komponenten.
   * 
   * @return Liste der Komponenten.
   */
  public FormTextPart getLibList()
  {
    if (libList != null)
      return libList;

    I18N i18n = Application.getI18n();

    StringBuffer buffer = new StringBuffer();
    buffer.append("<form>");
    buffer.append("<p><span color=\"header\" font=\"header\">"
        + i18n.tr("Verwendete Komponenten") + "</span></p>");

    AbstractPlugin plugin = Application.getPluginLoader().getPlugin(
        JVereinPlugin.class);
    String path = plugin.getManifest().getPluginDir();

    FileFinder finder = new FileFinder(new File(path + "/lib"));
    finder.matches(".*?info\\.xml$");
    File[] infos = finder.findRecursive();
    for (int i = 0; i < infos.length; ++i)
    {
      if (!infos[i].isFile() || !infos[i].canRead())
      {
        Logger.warn("unable to read " + infos[i] + ", skipping");
        continue;
      }

      try
      {
        InfoReader ir = new InfoReader(new FileInputStream(infos[i]));
        buffer.append("<p>");
        buffer.append("<b>" + ir.getName() + "</b>");
        buffer.append("<br/>" + infos[i].getParentFile().getAbsolutePath());
        buffer.append("<br/>" + ir.getDescription());
        buffer.append("<br/>" + ir.getUrl());
        buffer.append("<br/>" + ir.getLicense());
        buffer.append("</p>");
      }
      catch (Exception e)
      {
        Logger.error("unable to parse " + infos[0], e);
      }
    }
    buffer.append("</form>");

    libList = new FormTextPart(buffer.toString());
    return libList;
  }

}
