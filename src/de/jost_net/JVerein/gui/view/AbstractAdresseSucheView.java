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
import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.rmi.Adresstyp;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.LabelInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;

public abstract class AbstractAdresseSucheView extends AbstractView
{

  private TablePart p;

  private Settings settings;

  final MitgliedControl control = new MitgliedControl(this);

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(getTitle());
    this.setCurrentObject(Einstellungen.getDBService().createObject(
        Mitglied.class, null)); // leeres Object erzeugen

    DBService service = Einstellungen.getDBService();
    String sql = "select count(*) from beitragsgruppe";
    ResultSetExtractor rs = new ResultSetExtractor()
    {
      @Override
      public Object extract(ResultSet rs) throws SQLException
      {
        rs.next();
        return Long.valueOf(rs.getLong(1));
      }
    };
    Long anzahlbeitragsgruppe = (Long) service
        .execute(sql, new Object[] {}, rs);
    if (anzahlbeitragsgruppe.longValue() == 0)
    {
      new LabelInput("Noch keine Beitragsgruppe erfaﬂt. Bitte unter "
          + "Administration|Beitragsgruppen erfassen.").paint(getParent());
    }
    rs = new ResultSetExtractor()
    {
      @Override
      public Object extract(ResultSet rs) throws SQLException
      {
        rs.next();
        return Long.valueOf(rs.getLong(1));
      }
    };

    getFilter();

    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);

    Long anzahl = (Long) service.execute(sql, new Object[] {}, rs);
    if (anzahl.longValue() > 0)
    {
      Adresstyp at = (Adresstyp) control.getSuchAdresstyp(1).getValue();
      Logger.debug(at.getID() + ": " + at.getBezeichnung());
      p = control.getMitgliedTable(Integer.parseInt(at.getID()),
          getDetailAction());
      p.paint(getParent());
    }
    ButtonArea buttons = new ButtonArea();
    buttons.addButton(getHilfeButton());
    if (anzahlbeitragsgruppe > 0)
    {
      buttons.addButton("neu", getDetailAction(), null, false,
          "document-new.png");
    }
    buttons.paint(this.getParent());
  }

  public void TabRefresh()
  {
    try
    {
      control.saveDefaults();
      Adresstyp at = (Adresstyp) control.getSuchAdresstyp(2).getValue();
      control.refreshMitgliedTable(Integer.parseInt(at.getID()));
    }
    catch (RemoteException e1)
    {
      Logger.error("Fehler", e1);
    }
  }

  public class FilterListener implements Listener
  {

    FilterListener()
    {
    }

    @Override
    public void handleEvent(Event event)
    {
      if (event.type != SWT.Selection && event.type != SWT.FocusOut)
      {
        return;
      }

      try
      {
        TabRefresh();
      }
      catch (NullPointerException e)
      {
        return;
      }
    }
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Mitgliedersuche</span></p>"
        + "<p>Durch einen Klick auf einen Karteireiter werden die Mitglieder "
        + "angezeigt, deren Familienname mit dem entsprechenden Buchstaben beginnt.</p>"
        + "<p>Anschlieﬂend kann das Mitglied durch einen Doppelklick ausgew‰hlt werden.</p>"
        + "</form>";
  }

  public abstract String getTitle();

  public abstract void getFilter() throws RemoteException;

  public abstract Action getDetailAction();

  public abstract Button getHilfeButton();
}