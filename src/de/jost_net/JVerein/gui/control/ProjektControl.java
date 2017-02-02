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
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.ProjektAction;
import de.jost_net.JVerein.gui.menu.ProjektMenu;
import de.jost_net.JVerein.rmi.Projekt;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class ProjektControl extends AbstractControl
{

  private de.willuhn.jameica.system.Settings settings;

  private TablePart projektList;

  private Input bezeichnung;

  private DateInput startDatum;

  private DateInput endeDatum;

  private Projekt projekt;

  public ProjektControl(AbstractView view)
  {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  private Projekt getProjekt()
  {
    if (projekt != null)
    {
      return projekt;
    }
    projekt = (Projekt) getCurrentObject();
    return projekt;
  }

  public Input getBezeichnung() throws RemoteException
  {
    if (bezeichnung != null)
    {
      return bezeichnung;
    }
    bezeichnung = new TextInput(getProjekt().getBezeichnung(), 50);
    return bezeichnung;
  }

  public Input getStartDatum() throws RemoteException
  {
    if (startDatum != null)
    {
      return startDatum;
    }

    Date d = getProjekt().getStartDatum();
    if (d.equals( Einstellungen.NODATE ))
    {
      d = null;
    }
    startDatum = new DateInput(d, new JVDateFormatTTMMJJJJ());
    startDatum.setName("Startdatum");
    startDatum.setTitle("Startdatum");
    startDatum.setText("Bitte Startdatum w?hlen");
    return startDatum;
  }

  public Input getEndeDatum() throws RemoteException
  {
    if (endeDatum != null)
    {
        return endeDatum;
    }

    Date d = getProjekt().getEndeDatum();
    if (d.equals( Einstellungen.NODATE ))
    {
        d = null;
    }
    endeDatum = new DateInput(d, new JVDateFormatTTMMJJJJ());
    endeDatum.setName("Endedatum");
    endeDatum.setTitle("Endedatum");
    endeDatum.setText("Bitte Endedatum w?hlen");
    return endeDatum;
  }

  /**
   * This method stores the project using the current values.
   */
  public void handleStore()
  {
    try
    {
      Projekt p = getProjekt();
      p.setBezeichnung((String) getBezeichnung().getValue());
      p.setStartDatum( (Date) getStartDatum().getValue() );
      p.setEndeDatum( (Date) getEndeDatum().getValue() );

      try
      {
        p.store();
        GUI.getStatusBar().setSuccessText("Projekt gespeichert");
      }
      catch (ApplicationException e)
      {
        GUI.getStatusBar().setErrorText(e.getMessage());
      }
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei speichern des Projektes";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  public Part getProjektList() throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator<Projekt> projekte = service.createList(Projekt.class);
    projekte.setOrder("ORDER BY bezeichnung");

    projektList = new TablePart(projekte, new ProjektAction());
    projektList.addColumn("Bezeichnung", "bezeichnung");
    projektList.addColumn("Startdatum", "startdatum");
    projektList.addColumn("Endedatum", "endedatum");
    projektList.setContextMenu(new ProjektMenu());
    projektList.setRememberColWidths(true);
    projektList.setRememberOrder(true);
    projektList.setSummary(true);
    return projektList;
  }

}
