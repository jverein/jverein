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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.FormularAction;
import de.jost_net.JVerein.gui.formatter.FormularartFormatter;
import de.jost_net.JVerein.gui.menu.FormularMenu;
import de.jost_net.JVerein.keys.FormularArt;
import de.jost_net.JVerein.rmi.Formular;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.input.FileInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Column;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class FormularControl extends AbstractControl
{

  private de.willuhn.jameica.system.Settings settings;

  private TablePart formularList;

  private TextInput bezeichnung;

  private SelectInput art;

  private FileInput datei;

  private Formular formular;

  public FormularControl(AbstractView view)
  {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public Formular getFormular()
  {
    if (formular != null)
    {
      return formular;
    }
    formular = (Formular) getCurrentObject();
    return formular;
  }

  public TextInput getBezeichnung(boolean withFocus) throws RemoteException
  {
    if (bezeichnung != null)
    {
      return bezeichnung;
    }
    bezeichnung = new TextInput(getFormular().getBezeichnung(), 50);
    if (withFocus)
    {
      bezeichnung.focus();
    }
    return bezeichnung;
  }

  public SelectInput getArt() throws RemoteException
  {
    if (art != null)
    {
      return art;
    }
    art = new SelectInput(FormularArt.values(), getFormular().getArt());
    return art;
  }

  public FileInput getDatei()
  {
    if (datei != null)
    {
      return datei;
    }
    datei = new FileInput("", false, new String[] { "*.pdf", "*.PDF" });
    return datei;
  }

  /**
   * This method stores the project using the current values.
   */
  public void handleStore()
  {
    try
    {
      Formular f = getFormular();
      f.setBezeichnung((String) getBezeichnung(true).getValue());
      FormularArt fa = (FormularArt) getArt().getValue();
      f.setArt(fa);
      String dat = (String) getDatei().getValue();

      if (dat.length() > 0)
      {
        FileInputStream fis = new FileInputStream(dat);
        byte[] b = new byte[fis.available()];
        fis.read(b);
        fis.close();
        f.setInhalt(b);
      }
      f.store();
      GUI.getStatusBar().setSuccessText("Formular gespeichert");
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler beim Speichern des Formulares";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
    catch (ApplicationException e)
    {
      Logger.error("Fehler", e);
      GUI.getStatusBar().setErrorText(e.getMessage());
    }
    catch (FileNotFoundException e)
    {
      Logger.error("Fehler", e);
      GUI.getStatusBar().setErrorText("Datei nicht gefunden");
    }
    catch (IOException e)
    {
      Logger.error("Fehler", e);
      GUI.getStatusBar().setErrorText("Ein-/Ausgabe-Fehler");
    }
  }

  public Part getFormularList() throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator formulare = service.createList(Formular.class);
    formulare.setOrder("ORDER BY art, bezeichnung");

    formularList = new TablePart(formulare, new FormularAction());
    formularList.addColumn("Bezeichnung", "bezeichnung");
    formularList.addColumn("Art", "art", new FormularartFormatter(), false,
        Column.ALIGN_LEFT);
    formularList.setRememberColWidths(true);
    formularList.setContextMenu(new FormularMenu(this));
    formularList.setRememberOrder(true);
    formularList.setSummary(false);
    return formularList;
  }

  public void refreshTable() throws RemoteException
  {
    formularList.removeAll();
    DBIterator formulare = Einstellungen.getDBService().createList(
        Formular.class);
    formulare.setOrder("ORDER BY art, bezeichnung");
    while (formulare.hasNext())
    {
      formularList.addItem(formulare.next());
    }
  }

}
