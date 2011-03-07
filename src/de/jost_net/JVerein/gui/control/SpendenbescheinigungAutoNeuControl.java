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

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import sun.security.action.GetBooleanAction;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Spendenbescheinigung;
import de.jost_net.JVerein.server.SpendenbescheinigungNode;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.TreePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class SpendenbescheinigungAutoNeuControl extends AbstractControl
{

  private de.willuhn.jameica.system.Settings settings;

  private TreePart spbTree;

  public SpendenbescheinigungAutoNeuControl(AbstractView view)
  {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  // public Spendenbescheinigung getSpendenbescheinigung()
  // {
  // if (spendenbescheinigung != null)
  // {
  // return spendenbescheinigung;
  // }
  // spendenbescheinigung = (Spendenbescheinigung) getCurrentObject();
  // return spendenbescheinigung;
  // }

  /**
   * This method stores the project using the current values.
   */
  public void handleStore()
  {
    // try
    // {
    // Spendenbescheinigung spb = getSpendenbescheinigung();
    // spb.setZeile1((String) getZeile1(false).getValue());
    // spb.setZeile2((String) getZeile2().getValue());
    // spb.setZeile3((String) getZeile3().getValue());
    // spb.setZeile4((String) getZeile4().getValue());
    // spb.setZeile5((String) getZeile5().getValue());
    // spb.setZeile6((String) getZeile6().getValue());
    // spb.setZeile7((String) getZeile7().getValue());
    // spb.setSpendedatum((Date) getSpendedatum().getValue());
    // spb.setBescheinigungsdatum((Date) getBescheinigungsdatum().getValue());
    // spb.setBetrag((Double) getBetrag().getValue());
    // spb.setErsatzAufwendungen((Boolean) getErsatzAufwendungen().getValue());
    // spb.setFormular((Formular) getFormular().getValue());
    // spb.store();
    //
    // GUI.getStatusBar().setSuccessText("Spendenbescheinigung gespeichert");
    // }
    // catch (ApplicationException e)
    // {
    // GUI.getStatusBar().setErrorText(e.getMessage());
    // }
    // catch (RemoteException e)
    // {
    // String fehler = "Fehler bei Speichern der Spendenbescheinigung";
    // Logger.error(fehler, e);
    // GUI.getStatusBar().setErrorText(fehler);
    // }
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
    spbTree = new TreePart(new SpendenbescheinigungNode(), null);
    return spbTree;
  }

}
