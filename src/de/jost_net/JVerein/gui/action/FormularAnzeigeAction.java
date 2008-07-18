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
package de.jost_net.JVerein.gui.action;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import jonelo.NumericalChameleon.SpokenNumbers.GermanNumber;

import de.jost_net.JVerein.io.FormularAufbereitung;
import de.jost_net.JVerein.rmi.Formular;
import de.willuhn.jameica.gui.Action;
import de.willuhn.util.ApplicationException;

public class FormularAnzeigeAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    Formular f = null;

    if (context != null && (context instanceof Formular))
    {
      f = (Formular) context;
    }
    else
    {
      throw new ApplicationException("Kein Formular zur Anzeige ausgewählt");
    }
    try
    {
      final File file = File.createTempFile("formular", ".pdf");
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("Empfänger",
          "Herr\nWilli Wichtig\nTestgasse 1\n12345Testenhausen");
      map.put("Betrag", "*1.234,00*");
      map.put("Betrag in Worten", GermanNumber.toString(1234));
      map.put("Spendedatum", "15.12.2008");
      map.put("Bescheinigungsdatum", "17.12.2008");
      FormularAufbereitung fab = new FormularAufbereitung(f, file, map);
    }
    catch (RemoteException e)
    {
      throw new ApplicationException(e);
    }
    catch (IOException e)
    {
      throw new ApplicationException(e);
    }
    catch (Exception e)
    {
      throw new ApplicationException(e);
    }
  }
}
