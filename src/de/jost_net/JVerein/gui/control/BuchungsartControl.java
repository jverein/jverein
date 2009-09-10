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
 * Revision 1.13  2009/07/24 20:17:10  jost
 * Focus auf erstes Feld setzen.
 *
 * Revision 1.12  2009/06/22 18:12:19  jost
 * *** empty log message ***
 *
 * Revision 1.11  2008/12/13 16:21:39  jost
 * Bugfix Standardwert
 *
 * Revision 1.10  2008/11/30 18:56:20  jost
 * Refactoring: Code-Optimierung
 *
 * Revision 1.9  2008/11/29 13:06:02  jost
 * Refactoring: Code-Optimierung
 *
 * Revision 1.8  2008/05/26 18:57:34  jost
 * Spaltenausrichtung
 *
 * Revision 1.7  2008/05/22 06:47:31  jost
 * BuchfÃ¼hrung
 *
 * Revision 1.6  2008/03/16 07:35:49  jost
 * Reaktivierung BuchfÃ¼hrung
 *
 * Revision 1.4  2007/02/23 20:26:22  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.3  2006/12/20 20:25:44  jost
 * Patch von Ullrich Schäfer, der die Primitive vs. Object Problematik adressiert.
 *
 * Revision 1.2  2006/09/25 19:04:02  jost
 * Vorbereitung Splitbuchungen
 *
 * Revision 1.1  2006/09/20 15:38:30  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.BuchungsartAction;
import de.jost_net.JVerein.gui.menu.BuchungsartMenu;
import de.jost_net.JVerein.keys.ArtBuchungsart;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.jost_net.JVerein.rmi.Buchungsklasse;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.Formatter;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.IntegerInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Column;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class BuchungsartControl extends AbstractControl
{
  private de.willuhn.jameica.system.Settings settings;

  private TablePart buchungsartList;

  private IntegerInput nummer;

  private Input bezeichnung;

  private SelectInput art;

  private SelectInput buchungsklasse;

  private Buchungsart buchungsart;

  public BuchungsartControl(AbstractView view)
  {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  private Buchungsart getBuchungsart()
  {
    if (buchungsart != null)
    {
      return buchungsart;
    }
    buchungsart = (Buchungsart) getCurrentObject();
    return buchungsart;
  }

  public IntegerInput getNummer(boolean withFocus) throws RemoteException
  {
    if (nummer != null)
    {
      return nummer;
    }
    nummer = new IntegerInput(getBuchungsart().getNummer());
    if (withFocus)
    {
      nummer.focus();
    }
    return nummer;
  }

  public Input getBezeichnung() throws RemoteException
  {
    if (bezeichnung != null)
    {
      return bezeichnung;
    }
    bezeichnung = new TextInput(getBuchungsart().getBezeichnung(), 30);
    return bezeichnung;
  }

  public SelectInput getArt() throws RemoteException
  {
    if (art != null)
    {
      return art;
    }
    art = new SelectInput(ArtBuchungsart.getArray(), getBuchungsart().getArt());
    return art;
  }

  public Input getBuchungsklasse() throws RemoteException
  {
    if (buchungsklasse != null)
    {
      return buchungsklasse;
    }
    DBIterator list = Einstellungen.getDBService().createList(
        Buchungsklasse.class);
    list.setOrder("ORDER BY nummer");
    buchungsklasse = new SelectInput(list, getBuchungsart().getBuchungsklasse());
    buchungsklasse.setValue(getBuchungsart().getBuchungsklasse());
    buchungsklasse.setAttribute("bezeichnung");
    buchungsklasse.setPleaseChoose("Bitte auswählen");
    return buchungsklasse;
  }

  /**
   * This method stores the project using the current values.
   */
  public void handleStore()
  {
    try
    {
      Buchungsart b = getBuchungsart();
      b.setNummer(((Integer) getNummer(false).getValue()).intValue());
      b.setBezeichnung((String) getBezeichnung().getValue());
      ArtBuchungsart ba = (ArtBuchungsart) getArt().getValue();
      b.setArt(ba.getKey());
      GenericObject o = (GenericObject) getBuchungsklasse().getValue();
      if (o != null)
      {
        b.setBuchungsklasse(new Integer(o.getID()));
      }
      else
      {
        b.setBuchungsklasse(null);
      }
      try
      {
        b.store();
        GUI.getStatusBar().setSuccessText("Buchungsart gespeichert");
      }
      catch (ApplicationException e)
      {
        GUI.getStatusBar().setErrorText(e.getMessage());
      }
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei speichern der Buchungsart";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  public Part getBuchungsartList() throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator buchungsarten = service.createList(Buchungsart.class);
    buchungsarten.addFilter("nummer >= 0");
    buchungsarten.setOrder("ORDER BY nummer");

    buchungsartList = new TablePart(buchungsarten, new BuchungsartAction());
    buchungsartList.addColumn("Nummer", "nummer");
    buchungsartList.addColumn("Bezeichnung", "bezeichnung");
    buchungsartList.addColumn("Art", "art", new Formatter()
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
            case 0:
              return "Einnahme";
            case 1:
              return "Ausgabe";
            case 2:
              return "Umbuchung";
          }
        }
        return "ungültig";
      }
    }, false, Column.ALIGN_LEFT);
    buchungsartList.setContextMenu(new BuchungsartMenu());
    buchungsartList.setRememberColWidths(true);
    buchungsartList.setRememberOrder(true);
    buchungsartList.setSummary(true);
    return buchungsartList;
  }
}
