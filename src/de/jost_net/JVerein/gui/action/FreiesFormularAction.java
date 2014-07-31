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
package de.jost_net.JVerein.gui.action;

import java.io.File;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.Variable.AllgemeineMap;
import de.jost_net.JVerein.io.FormularAufbereitung;
import de.jost_net.JVerein.rmi.Formular;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.util.Dateiname;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class FreiesFormularAction implements Action
{

  private Settings settings;

  private String id;

  /**
   * 
   * @param id
   *        ID des Formulars
   */
  public FreiesFormularAction(String id)
  {
    this.id = id;
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);

  }

  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    Mitglied[] m = null;
    if (context != null
        && (context instanceof Mitglied || context instanceof Mitglied[]))
    {
      if (context instanceof Mitglied)
      {
        m = new Mitglied[] { (Mitglied) context};
      }
      else if (context instanceof Mitglied[])
      {
        m = (Mitglied[]) context;
      }
      try
      {
        generiereFreiesFormular(m);
      }
      catch (Exception e)
      {
        Logger.error("Fehler", e);
        throw new ApplicationException("Fehler bei der Aufbereitung", e);
      }
    }
    else
    {
      throw new ApplicationException("Kein Mitglied ausgewählt");
    }
  }

  private void generiereFreiesFormular(Mitglied[] m) throws Exception
  {
    FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
    fd.setText("Ausgabedatei wählen.");
    String path = settings.getString("lastdir", System.getProperty("user.home"));
    if (path != null && path.length() > 0)
    {
      fd.setFilterPath(path);
    }
    fd.setFileName(new Dateiname("freiesformular", "",
        Einstellungen.getEinstellung().getDateinamenmuster(), "PDF").get());
    fd.setFilterExtensions(new String[] { "*.PDF"});

    String s = fd.open();
    if (s == null || s.length() == 0)
    {
      return;
    }
    if (!s.endsWith(".PDF"))
    {
      s = s + ".PDF";
    }
    final File file = new File(s);
    settings.setAttribute("lastdir", file.getParent());

    FormularAufbereitung fa = new FormularAufbereitung(file);
    for (Mitglied mi : m)
    {
      Formular fo = (Formular) Einstellungen.getDBService().createObject(
          Formular.class, id);
      Map<String, Object> map = mi.getMap(null);
      map = new AllgemeineMap().getMap(map);
      fa.writeForm(fo, map);
    }
    fa.showFormular();

  }
}
