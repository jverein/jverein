/**********************************************************************
 * Author Collin Finck
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
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.jost_net.JVerein.Einstellungen;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBObject;
import de.willuhn.datasource.rmi.ObjectNotFoundException;

/**
 * Cache fuer oft geladene Fachobjekte.
 */
class Cache
{

  private final static de.willuhn.jameica.system.Settings settings = new de.willuhn.jameica.system.Settings(
      Cache.class);

  private static int timeout = 0;

  // Enthaelt alle Caches.
  private final static Map<Class<?>, Cache> caches = new HashMap<>();

  // Der konkrete Cache
  private Map<String, DBObject> data = new HashMap<>();

  private Class<? extends DBObject> type = null;

  private long validTo = 0;

  static
  {
    settings.setStoreWhenRead(false);

    // Das Timeout betraegt nur 10 Sekunden. Mehr brauchen wir nicht.
    // Es geht ja nur darum, dass z.Bsp. beim Laden der Umsaetze die
    // immer wieder gleichen zugeordneten Konten oder Umsatz-Kategorien
    // nicht dauernd neu geladen sondern kurz zwischengespeichert werden
    // Das Timeout generell wird benoetigt, wenn mehrere Hibiscus-Instanzen
    // sich eine Datenbank teilen. Andernfalls wuerde Hibiscus die
    // Aenderungen der anderen nicht mitkriegen
    timeout = settings.getInt("timeout.seconds", 10);
  }

  /**
   * ct.
   */
  private Cache()
  {
    touch();
  }

  /**
   * Aktualisiert das Verfallsdatum des Caches.
   */
  private void touch()
  {
    this.validTo = System.currentTimeMillis() + (timeout * 1000);
  }

  /**
   * Liefert den Cache fuer den genannten Typ.
   * 
   * @param type
   *          der Typ.
   * @param init
   *          true, wenn der Cache bei der Erzeugung automatisch befuellt werden
   *          soll.
   * @return der Cache.
   * @throws RemoteException
   */
  static Cache get(Class<? extends DBObject> type, boolean init)
      throws RemoteException
  {
    Cache cache = caches.get(type);

    if (cache != null)
    {
      if (cache.validTo < System.currentTimeMillis())
      {
        caches.remove(type);
        cache = null; // Cache wegwerfen
      }
      else
      {
        cache.touch(); // Verfallsdatum aktualisieren
      }
    }

    // Cache erzeugen und mit Daten fuellen
    if (cache == null)
    {
      cache = new Cache();
      cache.type = type;

      if (init)
      {
        // Daten in den Cache laden
        DBIterator<?> list = Einstellungen.getDBService().createList(type);
        while (list.hasNext())
        {
          DBObject o =  list.next();
          cache.data.put(o.getID(), o);
        }
      }
      caches.put(type, cache);
    }
    return cache;
  }

  /**
   * Liefert ein Objekt aus dem Cache.
   * 
   * @param id
   *          die ID des Objektes.
   * @return das Objekt oder NULL, wenn es nicht existiert.
   * @throws RemoteException
   */
  DBObject get(Object id) throws RemoteException
  {
    if (id == null)
      return null;

    String s = id.toString();

    DBObject value = data.get(s);

    if (value == null)
    {
      // Noch nicht im Cache. Vielleicht koennen wir es noch laden
      try
      {
        value = Einstellungen.getDBService().createObject(type, s);
        put(value); // tun wir gleich in den Cache
      }
      catch (ObjectNotFoundException one)
      {
        // Objekt existiert nicht mehr
      }
    }
    return value;
  }

  /**
   * Speichert ein Objekt im Cache.
   * 
   * @param object
   *          das zu speichernde Objekt.
   * @throws RemoteException
   */
  void put(DBObject object) throws RemoteException
  {
    if (object == null)
      return;
    data.put(object.getID(), object);
  }

  /**
   * Entfernt ein Objekt aus dem Cache.
   * 
   * @param object
   *          das zu entfernende Objekt.
   * @throws RemoteException
   */
  void remove(DBObject object) throws RemoteException
  {
    if (object == null)
      return;
    data.remove(object.getID());
  }

  /**
   * Liefert alle Werte aus dem Cache.
   * 
   * @return Liste der Werte aus dem Cache.
   */
  Collection<DBObject> values()
  {
    return data.values();
  }
}
