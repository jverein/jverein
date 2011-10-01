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
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.keys.Formularart;
import de.jost_net.JVerein.keys.Spendenart;
import de.jost_net.JVerein.rmi.Formular;
import de.jost_net.JVerein.rmi.Spendenbescheinigung;
import de.jost_net.JVerein.server.SpendenbescheinigungNode;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.TreePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class SpendenbescheinigungAutoNeuControl extends AbstractControl
{

  private de.willuhn.jameica.system.Settings settings;

  private SelectInput jahr;

  private TreePart spbTree;

  private SelectInput formular;

  public SpendenbescheinigungAutoNeuControl(AbstractView view)
  {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public SelectInput getJahr()
  {
    if (jahr != null)
    {
      return jahr;
    }
    Calendar cal = Calendar.getInstance();
    jahr = new SelectInput(new Object[] { cal.get(Calendar.YEAR),
        cal.get(Calendar.YEAR) - 1 }, cal.get(Calendar.YEAR));
    jahr.addListener(new Listener()
    {

      public void handleEvent(Event event)
      {
        try
        {
          spbTree.setRootObject(new SpendenbescheinigungNode(
              (Integer) getJahr().getValue()));
        }
        catch (RemoteException e)
        {
          Logger.error("Fehler", e);
        }
      }

    });
    return jahr;
  }

  public SelectInput getFormular() throws RemoteException
  {
    if (formular != null)
    {
      return formular;
    }
    DBIterator it = Einstellungen.getDBService().createList(Formular.class);
    it.addFilter("art = ?", new Object[] { Formularart.SPENDENBESCHEINIGUNG });
    formular = new SelectInput(it, null);
    return formular;
  }

  /**
   * This method stores the project using the current values.
   */
  public void handleStore()
  {
    //
  }

  public Button getSpendenbescheinigungErstellenButton()
  {
    Button b = new Button("erstellen", new Action()
    {

      public void handleAction(Object context) throws ApplicationException
      {
        try
        {

          List items = spbTree.getItems();
          SpendenbescheinigungNode spn = (SpendenbescheinigungNode) items
              .get(0);
          GenericIterator it1 = spn.getChildren();
          while (it1.hasNext())
          {
            SpendenbescheinigungNode sp1 = (SpendenbescheinigungNode) it1
                .next();
            Spendenbescheinigung spbescheinigung = (Spendenbescheinigung) Einstellungen
                .getDBService().createObject(Spendenbescheinigung.class, null);
            spbescheinigung.setSpendenart(Spendenart.GELDSPENDE);
            spbescheinigung.setMitglied(sp1.getMitglied());
            spbescheinigung.setZeile1(sp1.getMitglied().getAnrede());
            spbescheinigung.setZeile2(sp1.getMitglied().getVornameName());
            spbescheinigung.setZeile3(sp1.getMitglied().getStrasse());
            spbescheinigung.setZeile4(sp1.getMitglied().getPlz() + " "
                + sp1.getMitglied().getOrt());
            spbescheinigung.setErsatzAufwendungen(false);
            spbescheinigung.setBescheinigungsdatum(new Date());
            spbescheinigung.setSpendedatum(new Date());
            spbescheinigung.setBetrag(0.01);
            spbescheinigung.store();
            Date spendedatum = new Date();
            double summe = 0;
            GenericIterator it2 = sp1.getChildren();
            while (it2.hasNext())
            {
              SpendenbescheinigungNode sp2 = (SpendenbescheinigungNode) it2
                  .next();
              spendedatum = sp2.getBuchung().getDatum();
              summe += sp2.getBuchung().getBetrag();
              sp2.getBuchung().setSpendenbescheinigungId(
                  new Integer(spbescheinigung.getID()));
              sp2.getBuchung().store();
            }
            spbescheinigung.setSpendedatum(spendedatum);
            spbescheinigung.setBetrag(summe);
            spbescheinigung.setFormular((Formular) getFormular().getValue());
            spbescheinigung.store();
          }
          GUI.getStatusBar()
              .setSuccessText("Spendenbescheinigung(en) erstellt");
          spbTree.removeAll();
        }
        catch (RemoteException e)
        {
          Logger.error(e.getMessage());
          throw new ApplicationException(
              "Fehler bei der Aufbereitung der Spendenbescheinigung");
        }
      }
    }, null, false, "document-save.png");
    return b;
  }

  public Part getSpendenbescheinigungTree() throws RemoteException
  {
    spbTree = new TreePart(new SpendenbescheinigungNode((Integer) getJahr()
        .getValue()), null);
    return spbTree;
  }

}
