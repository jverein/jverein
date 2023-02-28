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
package de.jost_net.JVerein.gui.input;

import java.rmi.RemoteException;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.QIFImportHead;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.input.SelectInput;

public class QIFExternKontenInput extends SelectInput
{
  public QIFExternKontenInput() throws RemoteException
  {
    super(init(), null);
    this.setPleaseChoose("Wähle ein externes Konto");
  }

  /**
   * Lade die Liste neu in die Componente
   * 
   * @throws RemoteException
   */
  public void refresh() throws RemoteException
  {
    GenericIterator<?> it = init();
    if (null == it)
      super.setList(null);
    else
      super.setList(PseudoIterator.asList(it));

    if (checkForItems())
    {
      checkForSelectedItem();
    }
  }

  private void checkForSelectedItem()
  {
    Object selectedObject = this.getValue();
    if (null == selectedObject)
    {
      // Erstes in der Liste selektieren
      this.setValue(getList().get(0));
      sendNotification();
      return;
    }
    if (getList().contains(selectedObject) == false)
    {
      this.setValue(null);
      sendNotification();
    }
  }

  private void sendNotification()
  {
    Display.getDefault().asyncExec(new Runnable()
    {
      @Override
      public void run()
      {
        Event event = new Event();
        event.type = SWT.Selection;
        getControl().notifyListeners(SWT.Selection, event);
      }
    });
  }

  /***
   * Disable die componente, wenn die Liste leer ist
   */
  public boolean checkForItems()
  {
    if (isEmpty())
    {
      super.setValue(null);
      sendNotification();
      return false;
    }
    return true;
  }

  public boolean isEmpty()
  {
    List<?> list = super.getList();
    if (null == list)
      return true;
    return list.isEmpty();
  }

  private static GenericIterator<QIFImportHead> init() throws RemoteException
  {
    DBIterator<QIFImportHead> it = Einstellungen.getDBService()
        .createList(QIFImportHead.class);
    it.setOrder("Order by " + QIFImportHead.COL_NAME);
    return it;
  }
}
