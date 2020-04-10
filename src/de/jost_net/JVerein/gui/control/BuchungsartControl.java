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

import java.io.File;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.BuchungsartAction;
import de.jost_net.JVerein.gui.formatter.JaNeinFormatter;
import de.jost_net.JVerein.gui.menu.BuchungsartMenu;
import de.jost_net.JVerein.io.FileViewer;
import de.jost_net.JVerein.io.Reporter;
import de.jost_net.JVerein.keys.ArtBuchungsart;
import de.jost_net.JVerein.keys.SteuersatzBuchungsart;
import de.jost_net.JVerein.keys.BuchungsartSort;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.jost_net.JVerein.rmi.Buchungsklasse;
import de.jost_net.JVerein.util.Dateiname;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.pseudo.PseudoIterator;
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
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.Column;
import de.willuhn.jameica.gui.parts.TablePart;
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

  private SelectInput steuersatz;
  
  private SelectInput steuer_buchungsart;

  private TextInput suchtext;

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
    bezeichnung = new TextInput(getBuchungsart().getBezeichnung(), 80);
    return bezeichnung;
  }

  public SelectInput getArt() throws RemoteException
  {
    if (art != null)
    {
      return art;
    }
    art = new SelectInput(ArtBuchungsart.getArray(),
        new ArtBuchungsart(getBuchungsart().getArt()));
    return art;
  }

  public CheckboxInput getSpende() throws RemoteException
  {
    if (spende != null)
    {
      return spende;
    }
    spende = new CheckboxInput(getBuchungsart().getSpende());
    spende.addListener(new Listener()
    {
      // Listener enabled / disabled Steuer Felder falls eine Spende ausgewählt wurde
      // (Steuer und Spende schließen sich aus)
      @Override
      public void handleEvent(Event event)
      {
        if (spende.hasChanged()) {
          if ((Boolean) spende.getValue()) {
            // disable und auf 0 setzen
            steuersatz.disable();
            steuersatz.setValue(new SteuersatzBuchungsart(0));
            steuer_buchungsart.disable();
            steuer_buchungsart.setValue(null);
          }
          else {
            // enable und auf Wert aus DB setzen
            steuersatz.enable();
            steuersatz.setValue(new SteuersatzBuchungsart(0));
            steuer_buchungsart.disable();
            steuer_buchungsart.setValue(null);
          }
        }
      }
    });
    return spende;
  }

  public SelectInput getSteuersatz() throws RemoteException
  {
    if (steuersatz != null)
    {
      return steuersatz;
    }
    steuersatz = new SelectInput(SteuersatzBuchungsart.getArray(),
        new SteuersatzBuchungsart(getBuchungsart().getSteuersatz()));
    if (getBuchungsart().getSpende()) {
      steuersatz.disable();
    }
      steuersatz.addListener(new Listener()
    {
      // Listener enabled / disabled Feld Buchungsart für Steuer falls Steuer = 0 ist
      @Override
      public void handleEvent(Event event)
      {
        if (steuersatz.hasChanged()) {
          if (((SteuersatzBuchungsart) steuersatz.getValue()).getSteuersatz() == 0) {
            // disable und auf 0 setzen
            steuer_buchungsart.disable();    
            steuer_buchungsart.setValue(null);
          }
          else {
            // enable und auf Wert aus DB setzen
            steuer_buchungsart.enable();
          }
        }
      }
    });
    return steuersatz;
  }

  public SelectInput getSteuerBuchungsart() throws RemoteException
  {
    if (steuer_buchungsart != null)
    {
      return steuer_buchungsart;
    }

    DBIterator<Buchungsart> it = Einstellungen.getDBService()
        .createList(Buchungsart.class);
    it.setOrder("ORDER BY nummer");
    it.addFilter("ID != " + getBuchungsart().getID());
    it.addFilter("steuersatz = 0");
    it.addFilter("spende = false");

    steuer_buchungsart = new SelectInput(it, null);
    steuer_buchungsart.setValue(null);
    steuer_buchungsart.setAttribute("bezeichnung");
    steuer_buchungsart.setPleaseChoose(" ");
    steuer_buchungsart.setPreselected(getBuchungsart().getSteuerBuchungsart());
    
    if (getBuchungsart().getSteuersatz() == 0) {
      steuer_buchungsart.disable();
    }
    return steuer_buchungsart;
  }
  
  public Input getBuchungsklasse() throws RemoteException
  {
    if (buchungsklasse != null)
    {
      return buchungsklasse;
    }
    DBIterator<Buchungsklasse> list = Einstellungen.getDBService()
        .createList(Buchungsklasse.class);
    if (Einstellungen.getEinstellung()
        .getBuchungsartSort() == BuchungsartSort.NACH_NUMMER)
    {
      list.setOrder("ORDER BY nummer");
    }
    else
    {
      list.setOrder("ORDER BY bezeichnung");
    }
    buchungsklasse = new SelectInput(list,
        getBuchungsart().getBuchungsklasse());
    buchungsklasse.setValue(getBuchungsart().getBuchungsklasse());
    switch (Einstellungen.getEinstellung().getBuchungsartSort())
    {
      case BuchungsartSort.NACH_NUMMER:
        buchungsklasse.setAttribute("nrbezeichnung");
        break;
      case BuchungsartSort.NACH_BEZEICHNUNG_NR:
        buchungsklasse.setAttribute("bezeichnungnr");
        break;
      default:
        buchungsklasse.setAttribute("bezeichnung");
        break;
    }

    buchungsklasse.setPleaseChoose("Bitte ausw�hlen");
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
        GUI.getStatusBar().setErrorText("Nummer fehlt");
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
      b.setSteuersatz(((SteuersatzBuchungsart) steuersatz.getValue()).getSteuersatz());      
      if (steuer_buchungsart.isEnabled())
      {
        if (steuer_buchungsart.getValue() instanceof Buchungsart) {
          b.setSteuerBuchungsart((String) ((Buchungsart) steuer_buchungsart.getValue()).getID());
        }
        else {
          throw new RemoteException("Keine Buchungsart für Steuer hinterlegt!");
        }
      }
      else {
        b.setSteuerBuchungsart(null);
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

  public TextInput getSuchtext()
  {
    if (suchtext != null)
    {
      return suchtext;
    }
    suchtext = new TextInput(settings.getString("suchtext", ""), 35);
    return suchtext;
  }

  @SuppressWarnings("unchecked")
  public Part getBuchungsartList() throws RemoteException
  {

    DBService service = Einstellungen.getDBService();
    DBIterator<Buchungsart> buchungsarten = service
        .createList(Buchungsart.class);
    buchungsarten.addFilter("nummer >= 0");
    if (!getSuchtext().getValue().equals(""))
    {
      String text = "%" + ((String) getSuchtext().getValue()).toUpperCase()
          + "%";
      buchungsarten.addFilter("(UPPER(bezeichnung) like ? or nummer like ?)",
          new Object[] { text, text });
    }
    buchungsarten.setOrder("ORDER BY nummer");

    if (buchungsartList == null)
    {

      buchungsartList = new TablePart(buchungsarten, new BuchungsartAction());
      buchungsartList.addColumn("Nummer", "nummer");
      buchungsartList.addColumn("Bezeichnung", "bezeichnung");
      buchungsartList.addColumn("Art", "art", new Formatter()
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
            return ArtBuchungsart.get((Integer) o);
          }
          return "ungültig";
        }
      }, false, Column.ALIGN_LEFT);
      buchungsartList.addColumn("Buchungsklasse", "buchungsklasse");
      buchungsartList.addColumn("Spende", "spende", new JaNeinFormatter());
      buchungsartList.addColumn("Steuersatz", "steuersatz", new Formatter()
      {
        @Override
        public String format(Object o)
        {
          if (o == null)
          {
            return "";
          }
          if (o instanceof Double)
          {
            return SteuersatzBuchungsart.get((Double) o);
          }
          return "ungültig";
        }
      }, false, Column.ALIGN_RIGHT);
      buchungsartList.addColumn("Steuer Buchungsart", "steuer_buchungsart", new Formatter()
      {
        @Override
        public String format(Object o)
        {
          if (o == null)
          {
            return "";
          }
          if (o instanceof String)
          {
            try {
              DBIterator<Buchungsart> steuer_buchungsart = Einstellungen.getDBService().createList(Buchungsart.class);
              steuer_buchungsart.addFilter("ID = " + (String) o);
              return steuer_buchungsart.next().getNummer() + "";
              
            } catch (RemoteException e) {
              return "";
            }
          }
          return "ungültig";
        }
      }, false, Column.ALIGN_RIGHT);
      buchungsartList.setContextMenu(new BuchungsartMenu());
      buchungsartList.setRememberColWidths(true);
      buchungsartList.setRememberOrder(true);
      buchungsartList.setRememberState(true);
      buchungsartList.setSummary(true);
    }
    else
    {
      buchungsartList.removeAll();

      for (Buchungsart bu : (List<Buchungsart>) PseudoIterator
          .asList(buchungsarten))
      {
        buchungsartList.addItem(bu);
      }
      buchungsartList.sort();
    }
    return buchungsartList;
  }

  public Button getPDFAusgabeButton()
  {
    Button b = new Button("PDF-Ausgabe", new Action()
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
          throw new ApplicationException(
              "Fehler beim Start der PDF-Ausgabe der Buchungsarten");
        }
      }
    }, null, true, "file-pdf.png");
    return b;
  }

  private void starteAuswertung() throws RemoteException
  {
    FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
    fd.setText("Ausgabedatei w�hlen.");
    String path = settings.getString("lastdir",
        System.getProperty("user.home"));
    if (path != null && path.length() > 0)
    {
      fd.setFilterPath(path);
    }
    fd.setFileName(new Dateiname("buchungsarten", "",
        Einstellungen.getEinstellung().getDateinamenmuster(), "PDF").get());
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
    final DBIterator<Buchungsart> it = Einstellungen.getDBService()
        .createList(Buchungsart.class);
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
          Reporter reporter = new Reporter(fos, "Buchungsarten", "", it.size());
          reporter.addHeaderColumn("Nummer", Element.ALIGN_LEFT, 20,
              BaseColor.LIGHT_GRAY);
          reporter.addHeaderColumn("Bezeichnung", Element.ALIGN_LEFT, 80,
              BaseColor.LIGHT_GRAY);
          reporter.addHeaderColumn("Art", Element.ALIGN_LEFT, 25,
              BaseColor.LIGHT_GRAY);
          reporter.addHeaderColumn("Buchungsklasse", Element.ALIGN_LEFT, 80,
              BaseColor.LIGHT_GRAY);
          reporter.addHeaderColumn("Spende", Element.ALIGN_CENTER, 20,
              BaseColor.LIGHT_GRAY);          
          reporter.addHeaderColumn("Steuersatz", Element.ALIGN_CENTER, 25,
              BaseColor.LIGHT_GRAY);                        
          reporter.addHeaderColumn("Steuer Buchungsart", Element.ALIGN_CENTER, 30,
              BaseColor.LIGHT_GRAY);
          reporter.createHeader();
          while (it.hasNext())
          {
            Buchungsart b = it.next();
            reporter.addColumn(b.getNummer() + "", Element.ALIGN_RIGHT);
            reporter.addColumn(b.getBezeichnung(), Element.ALIGN_LEFT);
            reporter.addColumn(ArtBuchungsart.get(b.getArt()), Element.ALIGN_LEFT);
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
            reporter.addColumn(SteuersatzBuchungsart.get(b.getSteuersatz()), Element.ALIGN_RIGHT);
            if (b.getSteuerBuchungsart() != null) {
              reporter.addColumn(b.getSteuerBuchungsart().getNummer() + "", Element.ALIGN_RIGHT);
            }
            else {
              reporter.addColumn("", Element.ALIGN_LEFT);
            }
          }
          reporter.closeTable();
          reporter.close();
          fos.close();
          GUI.getStatusBar().setSuccessText("Auswertung gestartet");
          GUI.getCurrentView().reload();
        }
        catch (Exception e)
        {
          Logger.error("Fehler", e);
          GUI.getStatusBar().setErrorText(e.getMessage());
          throw new ApplicationException(e);
        }
        FileViewer.show(file);
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
