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
package de.jost_net.JVerein.gui.view;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.BeitragsgruppeSucheAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.BeitragsgruppeControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class BeitragsgruppeDetailView extends AbstractView
{

  final BeitragsgruppeControl control = new BeitragsgruppeControl(this);

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Beitragsgruppe");

    LabelGroup group = new LabelGroup(getParent(), "Beitrag");
    group.addLabelPair("Bezeichnung", control.getBezeichnung(true));
    switch (Einstellungen.getEinstellung().getBeitragsmodel())
    {
      case GLEICHERTERMINFUERALLE:
      case MONATLICH12631:
      {
        group.addLabelPair("Betrag", control.getBetrag());
        break;
      }
      case FLEXIBEL:
      {
        group.addLabelPair("Betrag monatlich", control.getBetragMonatlich());
        group.addLabelPair("Betrag vierteljährlich",
            control.getBetragVierteljaehrlich());
        group.addLabelPair("Betrag halbjährlich",
            control.getBetragHalbjaehrlich());
        group.addLabelPair("Betrag jährlich", control.getBetragJaehrlich());
        break;
      }
    }
    group.addLabelPair("Beitragsart", control.getBeitragsArt());
    group.addLabelPair("Buchungsart", control.getBuchungsart());

    if (Einstellungen.getEinstellung().getArbeitseinsatz())
    {
      LabelGroup groupAe = new LabelGroup(getParent(), "Arbeitseinsatz");
      groupAe.addLabelPair("Stunden", control.getArbeitseinsatzStunden());
      groupAe.addLabelPair("Betrag", control.getArbeitseinsatzBetrag());
    }

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.BEITRAGSGRUPPEN, false, "help-browser.png");
    buttons.addButton("suche", new BeitragsgruppeSucheAction(), null, false,
        "system-search.png");
    buttons.addButton("speichern", new Action()
    {
      @Override
      public void handleAction(Object context)
      {
        control.handleStore();
      }
    }, null, true, "document-save.png");
    buttons.paint(this.getParent());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Beitragsgruppe</span></p>"
        + "<p>Die Bezeichung und die Höhe des Beitrages sind zu erfassen.</p>"
        + "<p>Durch die Kennzeichnung als Familientarif können einem Mitglied Familienmitglieder "
        + "zugeordnet werden. Dabei ist zubeachten, dass nur dem zahlenden Mitglied ein "
        + "Beitrag berechnet wird, die Angehörigen sind beitragsbefreit.</p>"
        + "</form>";
  }

  @Override
  public void unbind() throws ApplicationException
  {
    try
    {
      if (control.getBezeichnung(true).hasChanged()
          || control.getBetrag().hasChanged())
      {
        YesNoDialog dialog = new YesNoDialog(AbstractDialog.POSITION_CENTER);
        dialog.setText("Soll die Änderung gespeichert werden?");
        try
        {
          Boolean yesno = (Boolean) dialog.open();
          if (yesno)
          {
            throw new ApplicationException("Änderungen bitte speichern.");
          }
        }
        catch (Exception e)
        {
          throw new ApplicationException(e);
        }
      }
    }
    catch (RemoteException e)
    {
      throw new ApplicationException(e);
    }
  }
}
