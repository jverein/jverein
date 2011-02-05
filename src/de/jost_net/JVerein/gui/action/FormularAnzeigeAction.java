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
 * Revision 1.10  2010-08-10 15:58:14  jost
 * neues Feld Zahlungsgrund
 *
 * Revision 1.9  2010-08-08 19:31:44  jost
 * Bugfix
 *
 * Revision 1.8  2010-07-26 14:47:18  jost
 * Bugfix
 *
 * Revision 1.7  2009/10/17 19:44:19  jost
 * *** empty log message ***
 *
 * Revision 1.6  2009/09/21 18:18:04  jost
 * Weitere Testdaten aufgenommen.
 *
 * Revision 1.5  2009/06/11 21:02:05  jost
 * Vorbereitung I18N
 *
 * Revision 1.4  2008/12/29 09:19:15  jost
 * Vermeidung NPE: Tagesdatum besetzen
 *
 * Revision 1.3  2008/10/01 14:16:51  jost
 * Warnungen entfernt
 *
 * Revision 1.2  2008/09/16 18:26:13  jost
 * Refactoring Formularaufbereitung
 *
 * Revision 1.1  2008/07/18 20:06:19  jost
 * Neu: Formulare
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;

import jonelo.NumericalChameleon.SpokenNumbers.GermanNumber;
import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.control.FormularfeldControl;
import de.jost_net.JVerein.io.FormularAufbereitung;
import de.jost_net.JVerein.rmi.Formular;
import de.willuhn.jameica.gui.Action;
import de.willuhn.util.ApplicationException;

public class FormularAnzeigeAction implements Action
{
  public void handleAction(Object context) throws ApplicationException
  {
    Formular formular = null;

    if (context != null && (context instanceof Formular))
    {
      formular = (Formular) context;
    }
    else
    {
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Kein Formular zur Anzeige ausgewählt"));
    }
    try
    {
      final File file = File.createTempFile("formular", ".pdf");
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put(FormularfeldControl.EMPFAENGER,
          "Herr\nDr. Willi Wichtig\nTestgasse 1\n12345 Testenhausen");
      map.put(FormularfeldControl.BUCHUNGSDATUM, new Date());
      map.put(FormularfeldControl.ZAHLUNGSGRUND,
          "Zahlungsgrund1 Zahlungsgrund2");
      map.put(FormularfeldControl.ZAHLUNGSGRUND1, "Zahlungsgrund 1");
      map.put(FormularfeldControl.ZAHLUNGSGRUND2, "Zahlungsgrund 2");
      map.put(FormularfeldControl.BETRAG, new Double(1234));
      map.put("Betrag in Worten", GermanNumber.toString(1234));
      map.put(FormularfeldControl.ID, "444");
      map.put(FormularfeldControl.EXTERNEMITGLIEDSNUMMER, "9999");
      map.put(FormularfeldControl.ANREDE, "Herrn");
      map.put(FormularfeldControl.TITEL, "Dr.");
      map.put(FormularfeldControl.NAME, "Wichtig");
      map.put(FormularfeldControl.VORNAME, "Willi");
      map.put(FormularfeldControl.ADRESSIERUNGSZUSATZ, "Hinterhaus");
      map.put(FormularfeldControl.STRASSE, "Testgasse 1");
      map.put(FormularfeldControl.PLZ, "12345");
      map.put(FormularfeldControl.ORT, "Testenhausen");
      map.put(FormularfeldControl.ZAHLUNGSRHYTMUS, "jährlich");
      map.put(FormularfeldControl.BLZ, "10020030");
      map.put(FormularfeldControl.KONTO, "1234567");
      map.put(FormularfeldControl.KONTOINHABER, "Wichtig");
      map.put(FormularfeldControl.GEBURTSDATUM, new Date());
      map.put(FormularfeldControl.GESCHLECHT, "M");
      map.put(FormularfeldControl.TELEFONPRIVAT, "01234/56789");
      map.put(FormularfeldControl.TELEFONDIENSTLICH, "01234/55555");
      map.put(FormularfeldControl.HANDY, "0160/1234567");
      map.put(FormularfeldControl.EMAIL, "willi.wichtig@jverein.de");
      map.put(FormularfeldControl.EINTRITT, new Date());
      map.put(FormularfeldControl.BEITRAGSGRUPPE, "Erwachsene");
      map.put(FormularfeldControl.AUSTRITT, new Date());
      map.put(FormularfeldControl.KUENDIGUNG, new Date());
      map.put(FormularfeldControl.ZAHLUNGSWEG,
          "Abbuchung von Konto 1234567, BLZ: 10020030");
      map.put(FormularfeldControl.TAGESDATUM,
          Einstellungen.DATEFORMAT.format(new Date()));

      map.put("Spendedatum", "15.12.2008");
      map.put("Buchungsdatum", new Date());
      map.put("Bescheinigungsdatum", "17.12.2008");
      map.put("Tagesdatum", Einstellungen.DATEFORMAT.format(new Date()));
      map.put(FormularfeldControl.BUCHUNGSDATUM, new Date());

      FormularAufbereitung fab = new FormularAufbereitung(file);
      fab.writeForm(formular, map);
      fab.showFormular();
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
