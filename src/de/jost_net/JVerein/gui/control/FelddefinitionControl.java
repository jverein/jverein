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
 * Revision 1.1  2008/04/10 18:58:13  jost
 * Neu: Benutzerdefinierte Datenfelder
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.FelddefinitionDetailAction;
import de.jost_net.JVerein.gui.menu.FelddefinitionMenu;
import de.jost_net.JVerein.rmi.Felddefinition;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.IntegerInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class FelddefinitionControl extends AbstractControl
{
  private TablePart felddefinitionList;

  private Input name;

  private Input label;

  private IntegerInput laenge;

  private Felddefinition felddefinition;

  public FelddefinitionControl(AbstractView view)
  {
    super(view);
  }

  private Felddefinition getFelddefinition()
  {
    if (felddefinition != null)
    {
      return felddefinition;
    }
    felddefinition = (Felddefinition) getCurrentObject();
    return felddefinition;
  }

  public Input getName() throws RemoteException
  {
    if (name != null)
    {
      return name;
    }
    name = new TextInput(getFelddefinition().getName(), 50);
    name.setMandatory(true);
    return name;
  }

  public Input getLabel() throws RemoteException
  {
    if (label != null)
    {
      return label;
    }
    label = new TextInput(getFelddefinition().getLabel(), 50);
    label.setMandatory(true);
    return label;
  }

  public IntegerInput getLaenge() throws RemoteException
  {
    if (laenge != null)
    {
      return laenge;
    }
    laenge = new IntegerInput(getFelddefinition().getLaenge());
    laenge.setMandatory(true);
    return laenge;
  }

  public void handleStore()
  {
    try
    {
      Felddefinition f = getFelddefinition();
      f.setName((String) getName().getValue());
      f.setLabel((String) getLabel().getValue());
      Integer i = (Integer) getLaenge().getValue();
      f.setLaenge(i.intValue());
      f.store();
      GUI.getStatusBar().setSuccessText("Felddefinition gespeichert");
    }
    catch (ApplicationException e)
    {
      GUI.getStatusBar().setErrorText(e.getMessage());
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei speichern der Felddefinition";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  public TablePart getFelddefinitionTable() throws RemoteException
  {
    if (felddefinitionList != null)
    {
      return felddefinitionList;
    }
    DBService service = Einstellungen.getDBService();
    DBIterator fdef = service.createList(Felddefinition.class);
    felddefinitionList = new TablePart(fdef, new FelddefinitionDetailAction());
    felddefinitionList.addColumn("Name", "name");
    felddefinitionList.addColumn("Label", "label");
    felddefinitionList.addColumn("Länge", "laenge");
    felddefinitionList.setContextMenu(new FelddefinitionMenu());
    return felddefinitionList;
  }
}
