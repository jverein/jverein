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
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.view.MitgliedDetailView;
import de.jost_net.JVerein.io.ILastschrift;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Kursteilnehmer;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.server.Bug;
import de.jost_net.OBanToo.SEPA.BIC;
import de.jost_net.OBanToo.SEPA.IBAN;
import de.jost_net.OBanToo.SEPA.SEPAException;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.system.Settings;

public class SEPABugsControl extends AbstractControl
{

  private Settings settings = null;

  private TablePart bugsList;

  private Date sepagueltigkeit;

  public SEPABugsControl(AbstractView view)
  {
    super(view);
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MONTH, -36);
    sepagueltigkeit = cal.getTime();
  }

  public Part getBugsList() throws RemoteException
  {
    bugsList = new TablePart(getBugs(), new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        Bug bug = (Bug) context;
        Object object = bug.getObject();
        if (object instanceof Mitglied)
        {
          GUI.startView(MitgliedDetailView.class.getName(), object);
        }
        if (object instanceof Kursteilnehmer)
        {
          GUI.startView(Kursteilnehmer.class.getName(), object);
        }
      }
    });
    bugsList.addColumn("Name", "name");
    bugsList.addColumn("Meldung", "meldung");
    bugsList.addColumn("Klassifikation", "klassifikationText");
    bugsList.setRememberColWidths(true);
    bugsList.setRememberOrder(true);
    bugsList.setSummary(true);
    return bugsList;
  }

  private List<Bug> getBugs() throws RemoteException
  {
    ArrayList<Bug> bugs = new ArrayList<>();

    DBIterator<Mitglied> it = getBaseIteratorMitglied();

    while (it.hasNext())
    {
      Mitglied m = (Mitglied) it.next();
      if ((m.getBeitragsgruppe().getBetrag() > 0
          || m.getBeitragsgruppe().getBetragMonatlich() > 0
          || m.getBeitragsgruppe().getBetragVierteljaehrlich() > 0
          || m.getBeitragsgruppe().getBetragHalbjaehrlich() > 0
          || m.getBeitragsgruppe().getBetragJaehrlich() > 0)
          && m.getZahlungsweg() == Zahlungsweg.BASISLASTSCHRIFT)
      {
        plausi(bugs, m);
      }
    }
    return bugs;
  }

  private void plausi(List<Bug> bugs, ILastschrift ls) throws RemoteException
  {
    if (ls.getMandatDatum().equals(Einstellungen.NODATE))
    {
      bugs.add(new Bug(ls,
          "Für die Basislastschrift fehlt das Mandatsdatum. Keine Lastschrift",
          Bug.HINT));
    }
    else if (ls.getMandatDatum().after(new Date()))
    {
      bugs.add(new Bug(ls,
          "Das Mandatsdatum liegt in der Zukunft. Keine Lastschrift",
          Bug.HINT));
    }

    try
    {
      new IBAN(ls.getIban());
    }
    catch (SEPAException e)
    {
      bugs.add(new Bug(ls, "Ungültige IBAN " + ls.getIban(), Bug.ERROR));
    }
    try
    {
      new BIC(ls.getBic());
    }
    catch (Exception e)
    {
      bugs.add(new Bug(ls, "Ungültige BIC " + ls.getBic(), Bug.ERROR));
    }
    // if (bic != null && iban != null)
    // {
    // String blz = iban.getBLZ();
    // Bank b = Banken.getBankByBLZ(blz);
    // if (!b.getBIC().equals(ls.getBic()))
    // {
    // bugs.add(new Bug(ls, "BIC passt nicht zur IBAN: " + ls.getBic() + ", "
    // + ls.getIban(), Bug.ERROR));
    // }
    // }
    Date letzte_lastschrift = ls.getLetzteLastschrift();
    if (letzte_lastschrift != null
        && letzte_lastschrift.before(sepagueltigkeit))
    {
      bugs.add(new Bug(ls,
          "Letzte Lastschrift ist älter als 36 Monate. Neues Mandat anfordern und eingeben.",
          Bug.ERROR));
    }
  }

  private DBIterator<Mitglied> getBaseIteratorMitglied() throws RemoteException
  {
    DBIterator<Mitglied> it = Einstellungen.getDBService()
        .createList(Mitglied.class);
    it.addFilter("(austritt is null or austritt > ?)", new Date());
    it.addFilter("adresstyp = 1");
    return it;
  }
}
