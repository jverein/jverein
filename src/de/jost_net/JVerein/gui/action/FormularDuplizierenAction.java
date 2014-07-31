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

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.control.FormularControl;
import de.jost_net.JVerein.rmi.Formular;
import de.jost_net.JVerein.rmi.Formularfeld;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.Action;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class FormularDuplizierenAction implements Action
{

  FormularControl control;

  public FormularDuplizierenAction(FormularControl control)
  {
    this.control = control;
  }

  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null || !(context instanceof Formular))
    {
      throw new ApplicationException("kein Formular ausgewählt");
    }
    try
    {
      Formular f1 = (Formular) context;
      Formular f2 = (Formular) Einstellungen.getDBService().createObject(
          Formular.class, f1.getID());
      f2.setID(null);
      f2.setBezeichnung(findeBezeichnung(f1.getBezeichnung()));
      f2.store();
      DBIterator it = Einstellungen.getDBService().createList(
          Formularfeld.class);
      it.addFilter("formular=?", f1.getID());
      while (it.hasNext())
      {
        Formularfeld ff1 = (Formularfeld) it.next();
        Formularfeld ff2 = (Formularfeld) Einstellungen.getDBService().createObject(
            Formularfeld.class, ff1.getID());
        ff2.setID(null);
        ff2.setFormular(f2);
        ff2.store();
      }
      control.refreshTable();
    }
    catch (Exception e)
    {
      Logger.error("Fehler: ", e);
      throw new ApplicationException("Fehler beim duplizieren eines Formulars",
          e);
    }
  }

  /**
   * Diese Methode sucht eine freie Bezeichnung für die Kopie. Dabei wird (1)
   * ...(n) an den Dateinamen angehängt, bis es eine nicht existierende
   * Bezeichnung gibt.
   */
  public String findeBezeichnung(String bezeichnung)
  {
    String ret = bezeichnung;
    try
    {
      for (int n = 1; n < Integer.MAX_VALUE; n++)
      {
        ret = bezeichnung + " (" + n + ")";
        DBIterator it = Einstellungen.getDBService().createList(Formular.class);
        it.addFilter("bezeichnung = ?", ret);
        if (it.size() == 0)
        {
          return ret;
        }
      }
    }
    catch (RemoteException e)
    {
      Logger.error("Fehler" + e);
    }
    return ret;
  }
}
