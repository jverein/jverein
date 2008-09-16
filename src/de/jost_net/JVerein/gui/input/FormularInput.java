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
 * Revision 1.1  2008/07/18 20:10:21  jost
 * Neu: Formulare
 *
 **********************************************************************/

package de.jost_net.JVerein.gui.input;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Formular;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.input.SelectInput;

/**
 * Combo-Box, fuer die Auswahl von Formularen
 */
public class FormularInput extends SelectInput
{
  public FormularInput(int art, String id) throws RemoteException
  {
    super(init(art), initdefault(id));
  }

  public FormularInput(int art) throws RemoteException
  {
    super(init(art), null);
  }

  /**
   * @return initialisiert die Liste der Optionen.
   * @throws RemoteException
   */
  private static GenericIterator init(int art) throws RemoteException
  {
    DBIterator it = Einstellungen.getDBService().createList(Formular.class);
    it.addFilter("art = ?", new Object[] { art });
    return it;
  }

  private static GenericObject initdefault(String id) throws RemoteException
  {
    Formular f = (Formular) Einstellungen.getDBService().createObject(
        Formular.class, id);
    return f;
  }

}
