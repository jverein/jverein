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
 * Revision 1.4  2007/03/18 08:39:13  jost
 * Pflichtfelder gekennzeichnet
 * Bugfix Zahlungsweg
 *
 * Revision 1.3  2007/02/23 20:26:38  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.2  2006/10/29 07:48:48  jost
 * Neu: Mitgliederstatistik
 *
 * Revision 1.1  2006/09/20 15:38:30  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.kapott.hbci.manager.HBCIUtils;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Stammdaten;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class StammdatenControl extends AbstractControl
{
  private Input name;

  private Input blz;

  private Input konto;

  private Input altersgruppen;

  private Stammdaten stamm;

  public StammdatenControl(AbstractView view)
  {
    super(view);
    try
    {
      stamm = (Stammdaten) Einstellungen.getDBService().createObject(
          Stammdaten.class, "1");
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    }
  }

  private Stammdaten getStammdaten()
  {
    if (stamm != null)
    {
      return stamm;
    }
    stamm = (Stammdaten) getCurrentObject();
    return stamm;
  }

  public Input getName() throws RemoteException
  {
    if (name != null)
    {
      return name;
    }
    name = new TextInput(getStammdaten().getName(), 30);
    name.setMandatory(true);
    return name;
  }

  public Input getBlz() throws RemoteException
  {
    if (blz != null)
    {
      return blz;
    }
    blz = new TextInput(getStammdaten().getBlz(), 8);
    blz.setMandatory(true);
    BLZListener l = new BLZListener();
    blz.addListener(l);
    l.handleEvent(null);
    return blz;
  }

  public Input getKonto() throws RemoteException
  {
    if (konto != null)
    {
      return konto;
    }
    konto = new TextInput(getStammdaten().getKonto(), 10);
    konto.setMandatory(true);
    konto.setComment("für die Abbuchung");
    return konto;
  }

  public Input getAltersgruppen() throws RemoteException
  {
    if (altersgruppen != null)
    {
      return altersgruppen;
    }
    altersgruppen = new TextInput(getStammdaten().getAltersgruppen(), 50);
    return altersgruppen;
  }

  public void handleStore()
  {
    try
    {
      Stammdaten s = getStammdaten();
      s.setName((String) getName().getValue());
      s.setBlz((String) getBlz().getValue());
      s.setKonto((String) getKonto().getValue());
      s.setAltersgruppen((String) getAltersgruppen().getValue());
      s.store();
      GUI.getStatusBar().setSuccessText("Stammdaten gespeichert");
    }
    catch (ApplicationException e)
    {
      GUI.getView().setErrorText(e.getMessage());
    }
    catch (RemoteException e)
    {
      Logger.error("Fehler bei speichern der Stammdaten", e);
      GUI.getStatusBar().setErrorText("Fehler beim speichern der Stammdaten");
    }
  }

  /**
   * Sucht das Geldinstitut zur eingegebenen BLZ und zeigt es als Kommentar
   * hinter dem BLZ-Feld an.
   */
  private class BLZListener implements Listener
  {
    public void handleEvent(Event event)
    {
      try
      {
        String name = HBCIUtils.getNameForBLZ((String) getBlz().getValue());
        getBlz().setComment(name);
      }
      catch (RemoteException e)
      {
        Logger.error("error while updating blz comment", e);
      }
    }
  }
}
