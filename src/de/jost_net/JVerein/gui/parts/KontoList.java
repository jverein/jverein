/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * basiert auf KontoList aus Hibiscus
 * All rights reserved
 * heiner@jverein.de
 * www.jverein.de
 * $Log$
 **********************************************************************/

package de.jost_net.JVerein.gui.parts;

import java.rmi.RemoteException;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Konto;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.parts.TablePart;

/**
 * Implementierung einer fix und fertig vorkonfigurierten Liste aller Konten.
 */
public class KontoList extends TablePart implements Part
{
  public KontoList(Action action) throws RemoteException
  {
    this(init(), action);
  }

  public KontoList(GenericIterator konten, Action action)
  {
    super(konten, action);

    addColumn("Kontonummer", "nummer");
    // addColumn(i18n.tr("Bankleitzahl"), "blz", new Formatter()
    // {
    // public String format(Object o)
    // {
    // if (o == null)
    // return null;
    // try
    // {
    // String blz = o.toString();
    // String name = HBCIUtils.getNameForBLZ(blz);
    // if (name == null || name.length() == 0)
    // return blz;
    // return blz + " [" + name + "]";
    // }
    // catch (Exception e)
    // {
    // Logger.error("error while formatting blz", e);
    // return o.toString();
    // }
    // }
    // });
    addColumn("Bezeichnung", "bezeichnung");
    setRememberOrder(true);
    setRememberColWidths(true);
    

  }

  /**
   * @see de.willuhn.jameica.gui.Part#paint(org.eclipse.swt.widgets.Composite)
   */
  public synchronized void paint(Composite parent) throws RemoteException
  {
    super.paint(parent);
  }

  /**
   * Initialisiert die Konten-Liste.
   * 
   * @return Liste der Konten.
   * @throws RemoteException
   */
  private static DBIterator init() throws RemoteException
  {
    DBIterator i = Einstellungen.getDBService().createList(Konto.class);
    i.setOrder("ORDER BY nummer, bezeichnung");
    return i;
  }
}
