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
 * Revision 1.1  2011-04-23 06:55:33  jost
 * Neu: Freie Formulare
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.io.File;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.Variable.AllgemeineMap;
import de.jost_net.JVerein.io.FormularAufbereitung;
import de.jost_net.JVerein.io.Variable;
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
   *          ID des Formulars
   */
  public FreiesFormularAction(String id)
  {
    this.id = id;
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);

  }

  public void handleAction(Object context) throws ApplicationException
  {
    Mitglied[] m = null;
    if (context != null
        && (context instanceof Mitglied || context instanceof Mitglied[]))
    {
      if (context instanceof Mitglied)
      {
        m = new Mitglied[] { (Mitglied) context };
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
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Kein Mitglied ausgewählt"));
    }
  }

  private void generiereFreiesFormular(Mitglied[] m) throws Exception
  {
    FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
    fd.setText("Ausgabedatei wählen.");
    String path = settings
        .getString("lastdir", System.getProperty("user.home"));
    if (path != null && path.length() > 0)
    {
      fd.setFilterPath(path);
    }
    fd.setFileName(new Dateiname("freiesformular", "", Einstellungen
        .getEinstellung().getDateinamenmuster(), "PDF").get());
    fd.setFilterExtensions(new String[] { "*.PDF" });

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
      Variable v = new Variable();
      v.set(mi);
      Formular fo = (Formular) Einstellungen.getDBService().createObject(
          Formular.class, id);
      Map<String, Object> map = mi.getMap(null);
      map = new AllgemeineMap().getMap(map);
      fa.writeForm(fo, map);
    }
    fa.showFormular();

  }
}
