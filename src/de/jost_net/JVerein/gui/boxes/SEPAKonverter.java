/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
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
package de.jost_net.JVerein.gui.boxes;

import java.rmi.RemoteException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Bank;
import de.jost_net.JVerein.rmi.Einstellung;
import de.jost_net.JVerein.rmi.Kursteilnehmer;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.util.SEPA;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.boxes.AbstractBox;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.Font;
import de.willuhn.jameica.gui.util.SWTUtil;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.jameica.system.Platform;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

/**
 * Seite fuer die SEPA-Konvertierung.
 */
public class SEPAKonverter extends AbstractBox
{
  private boolean aktiv = false;

  public SEPAKonverter()
  {
    try
    {
      DBIterator it0 = Einstellungen.getDBService().createList(Mitglied.class);

      DBIterator it1 = Einstellungen.getDBService().createList(Mitglied.class);
      it1.addFilter("iban is not null and blz is not null");
      DBIterator it2 = Einstellungen.getDBService().createList(
          Kursteilnehmer.class);
      it2.addFilter("iban is not null and blz is not null");
      if (it0.size() > 0 && (it1.size() == 0 || it2.size() == 0))
      {
        aktiv = true;
      }

    }
    catch (RemoteException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  @Override
  public boolean isActive()
  {
    return aktiv;
  }

  @Override
  public boolean getDefaultEnabled()
  {
    return aktiv;
  }

  @Override
  public int getDefaultIndex()
  {
    return 0;
  }

  @Override
  public String getName()
  {
    return JVereinPlugin.getI18n().tr("JVerein: SEPA-Konvertierung");
  }

  @Override
  public boolean isEnabled()
  {
    return aktiv;
  }

  @Override
  public void paint(Composite parent) throws RemoteException
  {
    // Wir unterscheiden hier beim Layout nach Windows/OSX und Rest.
    // Unter Windows und OSX sieht es ohne Rahmen und ohne Hintergrund besser
    // aus
    org.eclipse.swt.graphics.Color bg = null;
    int border = SWT.NONE;

    int os = Application.getPlatform().getOS();
    if (os != Platform.OS_WINDOWS && os != Platform.OS_WINDOWS_64
        && os != Platform.OS_MAC)
    {
      bg = GUI.getDisplay().getSystemColor(SWT.COLOR_WHITE);
      border = SWT.BORDER;
    }

    // 2-spaltige Anzeige. Links das Icon, rechts Text und Buttons
    Composite comp = new Composite(parent, border);
    comp.setBackground(bg);
    comp.setBackgroundMode(SWT.INHERIT_FORCE);
    comp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    comp.setLayout(new GridLayout(2, false));

    // Linke Spalte mit dem Icon
    {
      GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING
          | GridData.VERTICAL_ALIGN_BEGINNING);
      gd.verticalSpan = 3;
      Label icon = new Label(comp, SWT.NONE);
      icon.setBackground(bg);
      icon.setLayoutData(gd);
      icon.setImage(SWTUtil.getImage("jverein-icon-64x64.png"));
    }

    // Ueberschrift
    {
      Label title = new Label(comp, SWT.NONE);
      title.setBackground(bg);
      title.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      title.setFont(Font.H2.getSWTFont());
      title.setText(JVereinPlugin.getI18n().tr("SEPA-Konvertierung."));
    }

    // Text
    {
      Label desc = new Label(comp, SWT.WRAP);
      desc.setBackground(bg);
      desc.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      desc.setText(JVereinPlugin
          .getI18n()
          .tr("Konvertierung der Bankverbindung bei den Einstellungen, Mitgliedern und Kursteilnehmern "
              + "von BLZ und Konto zu BIC und IBAN"));
    }

    ButtonArea buttons = new ButtonArea();
    buttons.addButton(getButton());
    buttons.paint(parent);
  }

  private Button getButton()
  {
    Button b = new Button("starten", new Action()
    {
      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        BackgroundTask t = new BackgroundTask()
        {
          @Override
          public void run(ProgressMonitor monitor) throws ApplicationException
          {
            try
            {
              starte(monitor);
              monitor.setPercentComplete(100);
              monitor.setStatus(ProgressMonitor.STATUS_DONE);
              GUI.getCurrentView().reload();
            }
            catch (Exception e)
            {
              monitor.setStatus(ProgressMonitor.STATUS_ERROR);
            }
          }

          @Override
          public void interrupt()
          {
            //
          }

          @Override
          public boolean isInterrupted()
          {
            return false;
          }
        };
        Application.getController().start(t);

      }
    }, null, true, "document-save.png");
    return b;
  };

  @Override
  public int getHeight()
  {
    return 140;
  }

  private void starte(ProgressMonitor monitor) throws ApplicationException
  {
    try
    {
      // Einstellungen
      DBIterator it = Einstellungen.getDBService()
          .createList(Einstellung.class);
      while (it.hasNext())
      {
        Einstellung einstellung = (Einstellung) it.next();
        if (einstellung != null)
        {
          Bank bank = SEPA.getBankByBLZ(einstellung.getBlz());
          einstellung.setBic(bank.getBIC());
          einstellung.setIban(SEPA.createIban(einstellung.getKonto(),
              einstellung.getBlz(), Einstellungen.getEinstellung()
                  .getDefaultLand()));
          einstellung.store();
          Einstellungen.setEinstellung(einstellung);
          monitor.log("Einstellung: BIC und IBAN gesetzt");
        }
      }
      // Mitglieder
      it = Einstellungen.getDBService().createList(Mitglied.class);
      while (it.hasNext())
      {
        Mitglied m = (Mitglied) it.next();
        Mitglied m2 = (Mitglied) Einstellungen.getDBService().createObject(
            Mitglied.class, m.getID());
        if (m2.getBlz() != null && m2.getBlz().length() > 0)
        {
          Bank bank = SEPA.getBankByBLZ(m2.getBlz());
          m2.setBic(bank.getBIC());
          m2.setIban(SEPA.createIban(m2.getKonto(), m2.getBlz(), Einstellungen
              .getEinstellung().getDefaultLand()));
          m2.store();
        }
      }
      monitor.log("Mitglieder: BIC und IBAN gesetzt");
      // Kursteilnehmer
      it = Einstellungen.getDBService().createList(Kursteilnehmer.class);
      while (it.hasNext())
      {
        Kursteilnehmer k = (Kursteilnehmer) it.next();
        Kursteilnehmer k2 = (Kursteilnehmer) Einstellungen.getDBService()
            .createObject(Kursteilnehmer.class, k.getID());
        if (k2.getBlz() != null && k2.getBlz().length() > 0)
        {
          Bank bank = SEPA.getBankByBLZ(k2.getBlz());
          k2.setBic(bank.getBIC());
          k2.setIban(SEPA.createIban(k2.getKonto(), k2.getBlz(), Einstellungen
              .getEinstellung().getDefaultLand()));
          k2.store();
        }
      }
      monitor.log("Kursteilnehmer: BIC und IBAN gesetzt");
    }
    catch (Exception e)
    {
      monitor.log(e.getMessage());
      Logger.error("Fehler", e);
      throw new ApplicationException(e);
    }
  }

}
