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
package de.jost_net.JVerein.gui.control;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.FormularAction;
import de.jost_net.JVerein.gui.input.FormularArtInput;
import de.jost_net.JVerein.gui.menu.FormularMenu;
import de.jost_net.JVerein.rmi.Formular;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.Formatter;
import de.willuhn.jameica.gui.input.FileInput;
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

  private FormularArtInput art;

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

  public TextInput getBezeichnung() throws RemoteException
  {
    if (bezeichnung != null)
    {
      return bezeichnung;
    }
    bezeichnung = new TextInput((String) getFormular().getBezeichnung(), 50);
    return bezeichnung;
  }

  public FormularArtInput getArt() throws RemoteException
  {
    if (art != null)
    {
      return art;
    }
    art = new FormularArtInput(getFormular().getArt());
    return art;
  }

  public FileInput getDatei() throws RemoteException
  {
    if (datei != null)
    {
      return datei;
    }
    datei = new FileInput("", false, new String[]{"*.pdf", "*.PDF"});
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
      f.setBezeichnung((String) getBezeichnung().getValue());
      f.setArt((Integer) getArt().getValue());
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
    formularList.addColumn("Art", "art", new Formatter()
    {
      public String format(Object o)
      {
        if (o == null)
        {
          return "";
        }
        if (o instanceof Integer)
        {
          Integer art = (Integer) o;
          switch (art.intValue())
          {
            case FormularArtInput.SPENDENBESCHEINIGUNG:
              return "Spendenbescheinigung";
            case FormularArtInput.RECHNUNG:
              return "Rechnung";
          }
        }
        return "ungültig";
      }
    }, false, Column.ALIGN_LEFT);

    formularList.setRememberColWidths(true);
    formularList.setContextMenu(new FormularMenu());
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
      formularList.addItem((Formular) formulare.next());
    }
  }

}
