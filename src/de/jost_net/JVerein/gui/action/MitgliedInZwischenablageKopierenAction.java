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
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.util.MitgliedSpaltenauswahl;
import de.jost_net.JVerein.util.Spalte;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class MitgliedInZwischenablageKopierenAction implements Action
{

  /**
   * Kopiere als String in die Zwischenablage.
   */
  public void setClipboardContents(String aString)
  {
    Clipboard cb = new Clipboard(GUI.getDisplay());
    TextTransfer textTransfer = TextTransfer.getInstance();
    cb.setContents(new Object[] { aString }, new Transfer[] { textTransfer });
  }

  /**
   * Kopiert sichtbare Informationen der markierten Mitglieder nach
   * Zwischenablage (mit Tabs getrennt)
   */
  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    if (context != null
        && (context instanceof Mitglied || context instanceof Mitglied[]))
    {
      // wird in Zwischenablage kopiert
      StringBuilder clip = new StringBuilder();

      // bestimme ausgewählte/selektierte Mitglieder
      ArrayList<Mitglied> ausgewählteMitglieder = new ArrayList<>();
      if (context instanceof Mitglied)
      {
        ausgewählteMitglieder.add((Mitglied) context);
      }
      else if (context instanceof Mitglied[])
      {
        for (Mitglied mitglied : (Mitglied[]) context)
        {
          ausgewählteMitglieder.add(mitglied);
        }
      }

      // bestimme aktive Spalten (die der Benutzer in der Liste sieht)
      MitgliedSpaltenauswahl alleSpalten = new MitgliedSpaltenauswahl();
      List<Spalte> aktiveSpalten = alleSpalten.getSpalten();

      for (Iterator<Spalte> iter = aktiveSpalten.iterator(); iter.hasNext();)
      {
        Spalte s = iter.next();
        if (!s.isChecked())
        {
          iter.remove();
        }
      }

      // schreibe Spaltenüberschriften nach clip
      /*
       * for (int i = 0; i < aktiveSpalten.size(); i++) {
       * if(aktiveSpalten.get(i).isChecked()) {
       * clip.append((aktiveSpalten.get(i).getSpaltenname()) );
       * clip.append("\t"); } } clip.append("\n");
       */

      // schreibe Mitgliederdaten nach clip
      for (Mitglied mitglied : ausgewählteMitglieder)
      {

        try
        {
          for (int i = 0; i < aktiveSpalten.size(); i++)
          {
            if (aktiveSpalten.get(i).isChecked())
            {
              clip.append(
                  mitglied.getAttribute(aktiveSpalten.get(i).getSpaltenname()));
              clip.append("\t");
            }
          }
          clip.append("\n");

        }
        catch (RemoteException ex)
        {
          Logger.error("Fehler", ex);
          throw new ApplicationException(
              "Kopieren in Zwischenablage fehlgeschlagen.");
        }
      }
      // kopiere clip in die Zwischenablage
      setClipboardContents(clip.toString());
    }
    else
    {
      throw new ApplicationException("Kein Mitglied ausgewählt");
    }
  }
}
