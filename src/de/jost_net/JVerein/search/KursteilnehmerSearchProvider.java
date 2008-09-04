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
 **********************************************************************/
package de.jost_net.JVerein.search;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.KursteilnehmerDetailAction;
import de.jost_net.JVerein.rmi.Kursteilnehmer;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.search.Result;
import de.willuhn.jameica.search.SearchProvider;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Implementierung eines Search-Provider fuer die Suche nach Kursteilnehmern
 */
public class KursteilnehmerSearchProvider implements SearchProvider
{
  public String getName()
  {
    return "Kursteilnehmer";
  }

  public List search(String search) throws RemoteException,
      ApplicationException
  {
    if (search == null || search.length() == 0)
      return null;

    String text = "%" + search.toLowerCase() + "%";
    DBIterator list = Einstellungen.getDBService().createList(
        Kursteilnehmer.class);
    list.addFilter("LOWER(name) LIKE ? OR " + "LOWER(vzweck1) LIKE ? OR "
        + "vzweck2 LIKE ? OR " + "blz LIKE ? OR " + "konto LIKE ?",
        new String[] { text, text, text, text, text });

    ArrayList results = new ArrayList();
    while (list.hasNext())
    {
      results.add(new MyResult((Kursteilnehmer) list.next()));
    }
    return results;
  }

  /**
   * Hilfsklasse fuer die formatierte Anzeige der Ergebnisse.
   */
  private class MyResult implements Result
  {
    private Kursteilnehmer k = null;

    private MyResult(Kursteilnehmer k)
    {
      this.k = k;
    }

    public void execute() throws RemoteException, ApplicationException
    {
      new KursteilnehmerDetailAction().handleAction(this.k);
    }

    public String getName()
    {
      try
      {
        return k.getName() + ", " + k.getVZweck1() + ", " + k.getVZweck2()
            + ", Konto: " + k.getKonto() + ", BLZ: " + k.getBlz();
      }
      catch (RemoteException re)
      {
        Logger.error("unable to determin result name", re);
        return null;
      }
    }

  }

}
