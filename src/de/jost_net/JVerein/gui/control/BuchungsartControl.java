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
package de.jost_net.JVerein.gui.control;

import java.io.File;
import java.io.FileOutputStream;
import java.rmi.RemoteException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.BuchungsartAction;
import de.jost_net.JVerein.gui.formatter.JaNeinFormatter;
import de.jost_net.JVerein.gui.menu.BuchungsartMenu;
import de.jost_net.JVerein.io.Reporter;
import de.jost_net.JVerein.keys.ArtBuchungsart;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.jost_net.JVerein.rmi.Buchungsklasse;
import de.jost_net.JVerein.util.Dateiname;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.Formatter;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.IntegerInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.internal.action.Program;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.Column;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class BuchungsartControl extends AbstractControl
{
  private de.willuhn.jameica.system.Settings settings;

  private TablePart buchungsartList;

  private IntegerInput nummer;

  private Input bezeichnung;

  private SelectInput art;

  private SelectInput buchungsklasse;

  private CheckboxInput spende;

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
    bezeichnung = new TextInput(getBuchungsart().getBezeichnung(), 50);
    return bezeichnung;
  }

  public SelectInput getArt() throws RemoteException
  {
    if (art != null)
    {
      return art;
    }
    art = new SelectInput(ArtBuchungsart.getArray(), new ArtBuchungsart(
        getBuchungsart().getArt()));
    return art;
  }

  public CheckboxInput getSpende() throws RemoteException
  {
    if (spende != null)
    {
      return spende;
    }
    spende = new CheckboxInput(getBuchungsart().getSpende());
    return spende;
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
    buchungsklasse.setPleaseChoose(JVereinPlugin.getI18n()
        .tr("Bitte auswählen"));
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
      try
      {
        b.setNummer(((Integer) getNummer(false).getValue()).intValue());
      }
      catch (NullPointerException e)
      {
        GUI.getStatusBar().setErrorText(
            JVereinPlugin.getI18n().tr("Nummer fehlt"));
        return;
      }
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
      b.setSpende((Boolean) spende.getValue());

      try
      {
        b.store();
        GUI.getStatusBar().setSuccessText(
            JVereinPlugin.getI18n().tr("Buchungsart gespeichert"));
      }
      catch (ApplicationException e)
      {
        GUI.getStatusBar().setErrorText(e.getMessage());
      }
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Fehler bei speichern der Buchungsart");
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
    buchungsartList.addColumn(JVereinPlugin.getI18n().tr("Nummer"), "nummer");
    buchungsartList.addColumn(JVereinPlugin.getI18n().tr("Bezeichnung"),
        "bezeichnung");
    buchungsartList.addColumn(JVereinPlugin.getI18n().tr("Art"), "art",
        new Formatter()
        {
          @Override
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
                  return JVereinPlugin.getI18n().tr("Einnahme");
                case 1:
                  return JVereinPlugin.getI18n().tr("Ausgabe");
                case 2:
                  return JVereinPlugin.getI18n().tr("Umbuchung");
              }
            }
            return JVereinPlugin.getI18n().tr("ungültig");
          }
        }, false, Column.ALIGN_LEFT);
    buchungsartList.addColumn(JVereinPlugin.getI18n().tr("Buchungsklasse"),
        "buchungsklasse");
    buchungsartList.addColumn(JVereinPlugin.getI18n().tr("Spende"), "spende",
        new JaNeinFormatter());
    buchungsartList.setContextMenu(new BuchungsartMenu());
    buchungsartList.setRememberColWidths(true);
    buchungsartList.setRememberOrder(true);
    buchungsartList.setSummary(true);
    return buchungsartList;
  }

  public Button getPDFAusgabeButton()
  {
    Button b = new Button(JVereinPlugin.getI18n().tr("PDF-Ausgabe"),
        new Action()
        {
          @Override
          public void handleAction(Object context) throws ApplicationException
          {
            try
            {
              starteAuswertung();
            }
            catch (RemoteException e)
            {
              Logger.error(e.getMessage());
              throw new ApplicationException(JVereinPlugin.getI18n().tr(
                  "Fehler beim Start der PDF-Ausgabe der Buchungsarten"));
            }
          }
        }, null, true, "acroread.png");
    return b;
  }

  private void starteAuswertung() throws RemoteException
  {
    FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
    fd.setText(JVereinPlugin.getI18n().tr("Ausgabedatei wählen."));
    String path = settings
        .getString("lastdir", System.getProperty("user.home"));
    if (path != null && path.length() > 0)
    {
      fd.setFilterPath(path);
    }
    fd.setFileName(new Dateiname(JVereinPlugin.getI18n().tr("buchungsarten"),
        "", Einstellungen.getEinstellung().getDateinamenmuster(), "PDF").get());
    fd.setFilterExtensions(new String[] { "*.PDF" });

    String s = fd.open();
    if (s == null || s.length() == 0)
    {
      return;
    }
    if (!s.endsWith(".PDF"))
    {
      s = s + ".PDF";
    }
    final File file = new File(s);
    final DBIterator it = Einstellungen.getDBService().createList(
        Buchungsart.class);
    it.setOrder("ORDER BY nummer");
    settings.setAttribute("lastdir", file.getParent());
    BackgroundTask t = new BackgroundTask()
    {
      @Override
      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        try
        {
          FileOutputStream fos = new FileOutputStream(file);
          Reporter reporter = new Reporter(fos, JVereinPlugin.getI18n().tr(
              "Buchungsarten"), "", it.size());
          reporter.addHeaderColumn(JVereinPlugin.getI18n().tr("Nummer"),
              Element.ALIGN_LEFT, 15, BaseColor.LIGHT_GRAY);
          reporter.addHeaderColumn(JVereinPlugin.getI18n().tr("Bezeichnung"),
              Element.ALIGN_LEFT, 80, BaseColor.LIGHT_GRAY);
          reporter.addHeaderColumn(JVereinPlugin.getI18n().tr("Art"),
              Element.ALIGN_LEFT, 25, BaseColor.LIGHT_GRAY);
          reporter.addHeaderColumn(
              JVereinPlugin.getI18n().tr("Buchungsklasse"), Element.ALIGN_LEFT,
              80, BaseColor.LIGHT_GRAY);
          reporter.addHeaderColumn(JVereinPlugin.getI18n().tr("Spende"),
              Element.ALIGN_CENTER, 10, BaseColor.LIGHT_GRAY);
          reporter.createHeader();
          while (it.hasNext())
          {
            Buchungsart b = (Buchungsart) it.next();
            reporter.addColumn(b.getNummer() + "", Element.ALIGN_RIGHT);
            reporter.addColumn(b.getBezeichnung(), Element.ALIGN_LEFT);
            String arttxt = "";
            switch (b.getArt())
            {
              case 0:
                arttxt = JVereinPlugin.getI18n().tr("Einnahme");
                break;
              case 1:
                arttxt = JVereinPlugin.getI18n().tr("Ausgabe");
                break;
              case 2:
                arttxt = JVereinPlugin.getI18n().tr("Umbuchung");
                break;
            }
            reporter.addColumn(arttxt, Element.ALIGN_LEFT);
            if (b.getBuchungsklasse() != null)
            {
              reporter.addColumn(b.getBuchungsklasse().getBezeichnung(),
                  Element.ALIGN_LEFT);
            }
            else
            {
              reporter.addColumn("", Element.ALIGN_LEFT);
            }
            reporter.addColumn(b.getSpende());
          }
          reporter.closeTable();
          reporter.close();
          fos.close();
          GUI.getStatusBar().setSuccessText(
              JVereinPlugin.getI18n().tr("Auswertung gestartet"));
          GUI.getCurrentView().reload();
        }
        catch (Exception e)
        {
          Logger.error(JVereinPlugin.getI18n().tr("Fehler"), e);
          GUI.getStatusBar().setErrorText(e.getMessage());
          throw new ApplicationException(e);
        }
        GUI.getDisplay().asyncExec(new Runnable()
        {
          @Override
          public void run()
          {
            try
            {
              new Program().handleAction(file);
            }
            catch (ApplicationException ae)
            {
              Application.getMessagingFactory().sendMessage(
                  new StatusBarMessage(ae.getLocalizedMessage(),
                      StatusBarMessage.TYPE_ERROR));
            }
          }
        });
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
}
