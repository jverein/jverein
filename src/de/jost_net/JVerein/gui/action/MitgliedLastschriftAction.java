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
 * 
 * Erstellt von Rüdiger Wurth
 **********************************************************************/
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.io.Adressbuch.Adressaufbereitung;
import de.jost_net.JVerein.keys.ArtBeitragsart;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.SimpleDialog;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.jameica.hbci.Settings;
import de.willuhn.jameica.hbci.rmi.SepaLastSequenceType;
import de.willuhn.jameica.hbci.rmi.SepaLastschrift;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class MitgliedLastschriftAction implements Action
{

  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null || !(context instanceof Mitglied))
    {
      throw new ApplicationException("kein Mitglied ausgewählt");
    }
    Mitglied m = null; // Mitglied
    Mitglied mZ = null; // Zahler
    SepaLastschrift sl = null;
    try
    {
      m = (Mitglied) context;

      // pruefe wer der Zahler ist
      if (m.getBeitragsgruppe() != null
          && m.getBeitragsgruppe().getBeitragsArt() == ArtBeitragsart.FAMILIE_ANGEHOERIGER)
      {
        // Mitglied ist Familienangehoeriger, hat also anderen Zahler
        mZ = (Mitglied) Einstellungen.getDBService().createObject(
            Mitglied.class, m.getZahlerID() + "");

        if (!confirmDialog("Familienangehöriger",
            "Dieses Mitglied ist ein Familienangehöriger.\n\n"
                + "Als Konto wird das Konto des Zahlers belastet:\n"
                + "Zahler: " + mZ.getName() + "," + mZ.getVorname() + "\n"
                + "Kontoinhaber des Zahlers: " + mZ.getKontoinhaber(1)))
        {
          return;
        }

      }
      else
      {
        // Mitglied zahlt selbst
        mZ = m;
      }

      // pruefe Kontoinformationen
      if (checkSEPA(mZ))
      {
        sl = (SepaLastschrift) Settings.getDBService().createObject(
            SepaLastschrift.class, null);

        // Gläubiger-ID
        sl.setCreditorId(Einstellungen.getEinstellung().getGlaeubigerID());

        // Kontodaten: Name, BIC, IBAN
        sl.setGegenkontoName(mZ.getKontoinhaber(1));
        sl.setGegenkontoBLZ(mZ.getBic());
        sl.setGegenkontoNummer(mZ.getIban());

        // Mandat: ID, Datum, Typ
        sl.setMandateId(mZ.getMandatID());
        sl.setSignatureDate(mZ.getMandatDatum());
        sl.setSequenceType(SepaLastSequenceType.RCUR);

        // Verwendungszweck vorbelegen: "Mitgliedsnummer/Mitgliedsname"
        // Voranstellen eines Strings der zwingend ge?ndert werden muss,
        // damit der Anwender nicht vergisst den Verwendungszweck
        // korrekt einzugeben
        String verwendungszweck = "#ANPASSEN# "
            + (Einstellungen.getEinstellung().getExterneMitgliedsnummer() ? m
                .getExterneMitgliedsnummer() : m.getID()) + "/"
            + Adressaufbereitung.getNameVorname(m);
        sl.setZweck(verwendungszweck);

        GUI.startView(
            de.willuhn.jameica.hbci.gui.views.SepaLastschriftNew.class, sl);
      }
    }
    catch (Exception e)
    {
      throw new ApplicationException("Fehler bei manueller Lastschrift", e);
    }
  }

  private boolean checkSEPA(Mitglied m) throws RemoteException
  {

    // pruefe Zahlungsweg
    if (m.getZahlungsweg() == null
        || m.getZahlungsweg() != Zahlungsweg.BASISLASTSCHRIFT)
    {

      abortDialog("Fehler", "Zahlungsweg ist nicht Basislastschrift");
      return false;
    }

    // pruefe Mandatsdatum
    if (m.getMandatDatum() == Einstellungen.NODATE)
    {
      if (!confirmDialog("Mandat-Datum fehlt", "Kein Mandat-Datum vorhanden"))
      {
        return false;
      }
    }

    // pruefe Sepa Gueltigkeit: Datum der letzen Abbuchung
    Date letzte_lastschrift = m.getLetzteLastschrift();
    if (letzte_lastschrift != null)
    {
      Calendar sepagueltigkeit = Calendar.getInstance();
      sepagueltigkeit.add(Calendar.MONTH, -36);
      if (letzte_lastschrift.before(sepagueltigkeit.getTime()))
      {
        if (!confirmDialog("Letzte Lastschrift",
            "Letzte Lastschrift ist älter als 36 Monate"))
        {
          return false;
        }
      }
    }

    return true;
  }

  private boolean confirmDialog(String title, String text)
  {
    YesNoDialog d = new YesNoDialog(YesNoDialog.POSITION_CENTER);
    d.setTitle(title);
    d.setText(text + "\nWeiter?");
    try
    {
      Boolean choice = (Boolean) d.open();
      if (!choice.booleanValue())
      {
        return false;
      }
    }
    catch (Exception e)
    {
      Logger.error("Fehler bei Erstellen einer manuellen SEPA-Lastschrift", e);
      return false;
    }
    return true;
  }

  private void abortDialog(String title, String text)
  {
    SimpleDialog d = new SimpleDialog(SimpleDialog.POSITION_CENTER);
    d.setTitle(title);
    d.setText(text);
    try
    {
      d.open();
    }
    catch (Exception e)
    {
      Logger.error("Fehler bei Erstellen einer manuellen SEPA-Lastschrift", e);
    }
  }

}
