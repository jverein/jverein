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

package de.jost_net.JVerein.gui.dialogs;

import java.rmi.RemoteException;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.Messaging.FamilienbeitragMessage;
import de.jost_net.JVerein.Messaging.MitgliedskontoMessage;
import de.jost_net.JVerein.gui.control.FamilienbeitragNode;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.dialogs.SimpleDialog;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.gui.util.SWTUtil;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.OperationCanceledException;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Ein Dialog, ueber den man die Beitragsgruppe beim Auflösen eines
 * Familienverbandes auswählen kann.
 */
public class FamilienmitgliedEntfernenDialog extends AbstractDialog
{

  private String selected = null;

  private MitgliedControl control;

  public FamilienmitgliedEntfernenDialog(FamilienbeitragNode fbn)
  {
    super(AbstractDialog.POSITION_CENTER);
    setTitle(JVereinPlugin.getI18n().tr("Person aus Familienverband entfernen"));
    setSize(450, 450);
    control = new MitgliedControl(null);
    control.setMitglied(fbn.getMitglied());
    try
    {
      String kto = (String) control.getKonto().getValue();
      if (kto.length() == 0)
      {
        FamilienbeitragNode zahler = (FamilienbeitragNode) fbn.getParent();
        control.getKonto().setValue(zahler.getMitglied().getKonto());
        control.getBlz().setValue(zahler.getMitglied().getBlz());
        control.getKontoinhaber().setValue(
            zahler.getMitglied().getKontoinhaber());
      }
    }
    catch (RemoteException e)
    {
      Logger.error("Fehler FamilienmitgliedEntfernenDialog", e);
    }
  }

  @Override
  protected void paint(Composite parent) throws Exception
  {
    LabelGroup lgBeitragsgruppe = new LabelGroup(parent, "Beitragsgruppe");
    lgBeitragsgruppe.addLabelPair("Beitragsgruppe",
        control.getBeitragsgruppe(false));
    LabelGroup lgBank = new LabelGroup(parent, "Bankverbindung");
    lgBank.addLabelPair("Konto", control.getKonto());
    lgBank.addLabelPair("Bankleitzahl", control.getBlz());
    lgBank.addLabelPair("Kontoinhaber", control.getKontoinhaber());
    ButtonArea b = new ButtonArea(parent, 2);
    b.addButton(JVereinPlugin.getI18n().tr("weiter"), new Action()
    {
      public void handleAction(Object context)
      {
        Mitglied m = control.getMitglied();
        try
        {
          if (control.getBeitragsgruppe(false).getValue() == null)
          {
            throw new ApplicationException("Bitte Beitragsgruppe auswählen");
          }
          Beitragsgruppe bg = (Beitragsgruppe) control.getBeitragsgruppe(false)
              .getValue();
          m.setBeitragsgruppe(new Integer(bg.getID()));
          m.setKonto((String) control.getKonto().getValue());
          m.setBlz((String) control.getBlz().getValue());
          m.setKontoinhaber((String) control.getKontoinhaber().getValue());
          m.setZahlerID(null);
          m.setLetzteAenderung();
          m.store();
          Application.getMessagingFactory().sendMessage(
              new FamilienbeitragMessage(m));

          close();
        }
        catch (ApplicationException e)
        {
          SimpleDialog sd = new SimpleDialog(SimpleDialog.POSITION_CENTER);
          sd.setTitle("Fehler");
          sd.setText(e.getMessage());
          sd.setSideImage(SWTUtil.getImage("dialog-warning-large.png"));
          try
          {
            sd.open();
          }
          catch (Exception e1)
          {
            Logger.error("Fehler", e1);
          }
          Logger.error("Fehler", e);
          return;
        }
        catch (RemoteException e)
        {
          Logger.error("Fehler", e);
          return;
        }
      }
    });
    b.addButton(JVereinPlugin.getI18n().tr("abbrechen"), new Action()
    {
      public void handleAction(Object context)
      {
        throw new OperationCanceledException();
      }
    });
  }

  @Override
  protected Object getData() throws Exception
  {
    return this.selected;
  }

}
