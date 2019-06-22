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
package de.jost_net.JVerein.search;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.MailDetailAction;
import de.jost_net.JVerein.rmi.Mail;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.search.Result;
import de.willuhn.jameica.search.SearchProvider;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Implementierung eines Search-Provider fuer die Suche nach Mails
 */
public class MailSearchProvider implements SearchProvider
{

  @Override
  public String getName()
  {
    return "Mails";
  }

  @Override
  public List<MyResult> search(String search) throws RemoteException
  {
    if (search == null || search.length() == 0)
    {
      return null;
    }

    String text = "%" + search.toLowerCase() + "%";
    DBIterator<Mail> list = Einstellungen.getDBService().createList(Mail.class);
    list.addFilter("LOWER(betreff) LIKE ? OR LOWER(txt) LIKE ?", text, text);

    ArrayList<MyResult> results = new ArrayList<>();
    while (list.hasNext())
    {
      results.add(new MyResult((Mail) list.next()));
    }
    return results;
  }

  /**
   * Hilfsklasse fuer die formatierte Anzeige der Ergebnisse.
   */
  private static class MyResult implements Result
  {

    private static final long serialVersionUID = -1685817053590491168L;

    private Mail m = null;

    private MyResult(Mail m)
    {
      this.m = m;
    }

    @Override
    public void execute() throws ApplicationException
    {
      new MailDetailAction().handleAction(this.m);
    }

    @Override
    public String getName()
    {
      try
      {
        return m.getBetreff() + ", " + m.getTxt();
      }
      catch (RemoteException re)
      {
        Logger.error("unable to determin result name", re);
        return null;
      }
    }

  }

}
