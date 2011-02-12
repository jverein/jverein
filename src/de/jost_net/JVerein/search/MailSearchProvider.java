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
 * Revision 1.2  2010-10-15 09:58:30  jost
 * Code aufgeräumt
 *
 * Revision 1.1  2010-02-25 18:58:06  jost
 * Neu: Suche f. Mails
 *
 **********************************************************************/
package de.jost_net.JVerein.search;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
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

  public String getName()
  {
    return JVereinPlugin.getI18n().tr("Mails");
  }

  public List<MyResult> search(String search) throws RemoteException
  {
    if (search == null || search.length() == 0)
    {
      return null;
    }

    String text = "%" + search.toLowerCase() + "%";
    DBIterator list = Einstellungen.getDBService().createList(Mail.class);
    list.addFilter("LOWER(betreff) LIKE ? OR LOWER(txt) LIKE ?", new String[] {
        text, text });

    ArrayList<MyResult> results = new ArrayList<MyResult>();
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

    public void execute() throws ApplicationException
    {
      new MailDetailAction().handleAction(this.m);
    }

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
