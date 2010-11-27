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
 * Revision 1.7  2010-11-13 09:24:44  jost
 * Warnings entfernt.
 *
 * Revision 1.6  2010-10-15 09:58:26  jost
 * Code aufgeräumt
 *
 * Revision 1.5  2010-10-01 13:30:08  jost
 * Neu: PDF-Ausgabe der Zusatzbuchungen
 *
 * Revision 1.4  2010/02/08 18:14:42  jost
 * JaNeinFormatter f. Zusatzzahlungen
 *
 * Revision 1.3  2009/07/27 15:25:49  jost
 * Focus auf erstes Feld setzen.
 *
 * Revision 1.2  2009/06/22 18:15:00  jost
 * Einheitliche Ausgabe von Fehlermeldungen in der Statusbar
 *
 * Revision 1.1  2008/12/22 21:09:42  jost
 * Zusatzabbuchung->Zusatzbetrag
 *
 * Revision 1.10  2008/11/30 18:58:00  jost
 * Refactoring: Code-Optimierung
 *
 * Revision 1.9  2008/11/29 13:08:40  jost
 * Refactoring: Code-Optimierung
 *
 * Revision 1.8  2008/05/22 06:49:33  jost
 * Redaktionelle Ã„nderung
 *
 * Revision 1.7  2007/04/12 05:53:15  jost
 * Anpassung an aktuelles Jameica-Nightly-Build
 *
 * Revision 1.6  2007/03/30 13:23:35  jost
 * Wiederkehrende Zusatzabbuchungen.
 *
 * Revision 1.5  2007/03/18 08:39:27  jost
 * Pflichtfelder gekennzeichnet
 * Bugfix Zahlungsweg
 *
 * Revision 1.4  2007/02/23 20:26:38  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.3  2006/12/23 16:46:58  jost
 * Java 1.5 Kompatibilität
 *
 * Revision 1.2  2006/12/20 20:25:44  jost
 * Patch von Ullrich Schäfer, der die Primitive vs. Object Problematik adressiert.
 *
 * Revision 1.1  2006/09/20 15:38:30  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;

import com.lowagie.text.Element;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.ZusatzbetraegeAction;
import de.jost_net.JVerein.gui.formatter.JaNeinFormatter;
import de.jost_net.JVerein.gui.menu.ZusatzbetraegeMenu;
import de.jost_net.JVerein.io.Reporter;
import de.jost_net.JVerein.keys.IntervallZusatzzahlung;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Zusatzbetrag;
import de.jost_net.JVerein.util.Dateiname;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.formatter.Formatter;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.DecimalInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.internal.action.Program;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class ZusatzbetragControl extends AbstractControl
{

  private de.willuhn.jameica.system.Settings settings;

  private DateInput faelligkeit = null;

  private Input buchungstext;

  private DecimalInput betrag;

  private Zusatzbetrag zuab;

  private DateInput startdatum;

  private SelectInput intervall;

  private DateInput endedatum;

  private DateInput ausfuehrung = null;

  private SelectInput ausfuehrungSuch = null;

  private TablePart zusatzbetraegeList;

  public ZusatzbetragControl(AbstractView view)
  {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public Zusatzbetrag getZusatzbetrag()
  {
    if (zuab != null)
    {
      return zuab;
    }
    zuab = (Zusatzbetrag) getCurrentObject();
    return zuab;
  }

  public DateInput getFaelligkeit() throws RemoteException
  {
    if (faelligkeit != null)
    {
      return faelligkeit;
    }

    Date d = getZusatzbetrag().getFaelligkeit();

    this.faelligkeit = new DateInput(d, Einstellungen.DATEFORMAT);
    this.faelligkeit.setTitle("Fälligkeit");
    this.faelligkeit.setText("Bitte Fälligkeitsdatum wählen");
    this.faelligkeit.addListener(new Listener()
    {

      public void handleEvent(Event event)
      {
        Date date = (Date) faelligkeit.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return faelligkeit;
  }

  public Input getBuchungstext() throws RemoteException
  {
    if (buchungstext != null)
    {
      return buchungstext;
    }
    buchungstext = new TextInput(getZusatzbetrag().getBuchungstext(), 27);
    buchungstext.setMandatory(true);
    return buchungstext;
  }

  public DecimalInput getBetrag() throws RemoteException
  {
    if (betrag != null)
    {
      return betrag;
    }
    betrag = new DecimalInput(getZusatzbetrag().getBetrag(),
        Einstellungen.DECIMALFORMAT);
    betrag.setMandatory(true);
    return betrag;
  }

  public DateInput getStartdatum(boolean withFocus) throws RemoteException
  {
    if (startdatum != null)
    {
      return startdatum;
    }

    Date d = getZusatzbetrag().getStartdatum();
    this.startdatum = new DateInput(d, Einstellungen.DATEFORMAT);
    this.startdatum.setTitle("Startdatum");
    this.startdatum.setText("Bitte Startdatum wählen");
    this.startdatum.addListener(new Listener()
    {

      public void handleEvent(Event event)
      {
        Date date = (Date) startdatum.getValue();
        if (date == null)
        {
          return;
        }
        faelligkeit.setValue(date);
      }
    });
    if (withFocus)
    {
      startdatum.focus();
    }
    return startdatum;
  }

  public SelectInput getIntervall() throws RemoteException
  {
    if (intervall != null)
    {
      return intervall;
    }
    Integer i = getZusatzbetrag().getIntervall();
    if (i == null)
    {
      i = new Integer(0);
    }
    this.intervall = new SelectInput(IntervallZusatzzahlung.getArray(),
        new IntervallZusatzzahlung(i));
    return intervall;
  }

  public DateInput getEndedatum() throws RemoteException
  {
    if (endedatum != null)
    {
      return endedatum;
    }

    Date d = getZusatzbetrag().getEndedatum();
    this.endedatum = new DateInput(d, Einstellungen.DATEFORMAT);
    this.endedatum.setTitle("Startdatum");
    this.endedatum.setText("Bitte Startdatum wählen");
    this.endedatum.addListener(new Listener()
    {

      public void handleEvent(Event event)
      {
        Date date = (Date) endedatum.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return endedatum;
  }

  public DateInput getAusfuehrung() throws RemoteException
  {
    if (ausfuehrung != null)
    {
      return ausfuehrung;
    }

    Date d = getZusatzbetrag().getAusfuehrung();

    this.ausfuehrung = new DateInput(d, Einstellungen.DATEFORMAT);
    this.ausfuehrung.setTitle("Ausführung");
    this.ausfuehrung.setText("Bitte Ausführungsdatum wählen");
    this.ausfuehrung.addListener(new Listener()
    {

      public void handleEvent(Event event)
      {
        Date date = (Date) ausfuehrung.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    ausfuehrung.setEnabled(false);
    return ausfuehrung;
  }

  public SelectInput getAusfuehrungSuch() throws RemoteException
  {
    if (ausfuehrungSuch != null)
    {
      return ausfuehrungSuch;
    }

    final Vector<String> werte = new Vector<String>();
    werte.addElement("Alle");
    werte.addElement("Aktive");
    werte.addElement("Noch nicht ausgeführt");

    String sql = "select ausfuehrung from zusatzabbuchung where ausfuehrung is not null "
        + "group by ausfuehrung order by ausfuehrung desc";
    DBService service = Einstellungen.getDBService();

    ResultSetExtractor rs = new ResultSetExtractor()
    {

      public Object extract(ResultSet rs) throws SQLException
      {
        while (rs.next())
        {
          werte.addElement(Einstellungen.DATEFORMAT.format(rs.getDate(1)));
        }
        return null;
      }
    };
    service.execute(sql, new Object[] {}, rs);

    ausfuehrungSuch = new SelectInput(werte, werte.elementAt(0));
    ausfuehrungSuch.addListener(new Listener()
    {

      public void handleEvent(Event event)
      {
        try
        {
          getZusatzbetraegeList();
        }
        catch (RemoteException e)
        {
          e.printStackTrace();
        }
      }
    });

    return ausfuehrungSuch;
  }

  public void handleStore()
  {
    try
    {
      Zusatzbetrag z = getZusatzbetrag();
      z.setFaelligkeit((Date) getFaelligkeit().getValue());
      z.setStartdatum((Date) getStartdatum(false).getValue());
      IntervallZusatzzahlung iz = (IntervallZusatzzahlung) getIntervall()
          .getValue();
      z.setIntervall(iz.getKey());
      z.setEndedatum((Date) getEndedatum().getValue());
      z.setBuchungstext((String) getBuchungstext().getValue());
      Double d = (Double) getBetrag().getValue();
      z.setBetrag(d.doubleValue());
      z.store();
      GUI.getStatusBar().setSuccessText("Zusatzbetrag gespeichert");
    }
    catch (ApplicationException e)
    {
      GUI.getStatusBar().setErrorText(e.getMessage());
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei speichern des Zusatzbetrages";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  public Part getZusatzbetraegeList() throws RemoteException
  {
    DBIterator zusatzbetraege = getIterator();

    if (zusatzbetraegeList == null)
    {
      zusatzbetraegeList = new TablePart(zusatzbetraege,
          new ZusatzbetraegeAction(null));
      zusatzbetraegeList.addColumn("Name", "mitglied", new Formatter()
      {

        public String format(Object o)
        {
          Mitglied m = (Mitglied) o;
          if (m == null)
          {
            return null;
          }
          String name = null;
          try
          {
            name = m.getNameVorname();
          }
          catch (RemoteException e)
          {
            e.printStackTrace();
          }
          return name;
        }
      });
      zusatzbetraegeList.addColumn("Startdatum", "startdatum",
          new DateFormatter(Einstellungen.DATEFORMAT));
      zusatzbetraegeList.addColumn("nächste Fälligkeit", "faelligkeit",
          new DateFormatter(Einstellungen.DATEFORMAT));
      zusatzbetraegeList.addColumn("letzte Ausführung", "ausfuehrung",
          new DateFormatter(Einstellungen.DATEFORMAT));
      zusatzbetraegeList.addColumn("Intervall", "intervalltext");
      zusatzbetraegeList.addColumn("Endedatum", "endedatum", new DateFormatter(
          Einstellungen.DATEFORMAT));
      zusatzbetraegeList.addColumn("Buchungstext", "buchungstext");
      zusatzbetraegeList.addColumn("Betrag", "betrag", new CurrencyFormatter(
          "", Einstellungen.DECIMALFORMAT));
      zusatzbetraegeList.addColumn("aktiv", "aktiv", new JaNeinFormatter());

      zusatzbetraegeList.setContextMenu(new ZusatzbetraegeMenu(
          zusatzbetraegeList));
      zusatzbetraegeList.setRememberColWidths(true);
      zusatzbetraegeList.setRememberOrder(true);
      zusatzbetraegeList.setSummary(true);
    }
    else
    {
      zusatzbetraegeList.removeAll();
      while (zusatzbetraege.hasNext())
      {
        zusatzbetraegeList.addItem(zusatzbetraege.next());
      }
    }
    if (this.ausfuehrungSuch.getText().equals("Aktive"))
    {
      nichtAktiveEliminieren(zusatzbetraegeList);
    }
    return zusatzbetraegeList;
  }

  private DBIterator getIterator() throws RemoteException
  {
    DBIterator zusatzbetraege = Einstellungen.getDBService().createList(
        Zusatzbetrag.class);
    if (this.ausfuehrungSuch.getText().equals("Alle"))
    {
      // nichts tun
    }
    else if (this.ausfuehrungSuch.getText().equals("Aktive"))
    {
      // zunächst nichts tun
    }
    else if (this.ausfuehrungSuch.getText().equals("Noch nicht ausgeführt"))
    {
      zusatzbetraege.addFilter("ausfuehrung is null");
    }
    else
    {
      try
      {
        Date d = Einstellungen.DATEFORMAT.parse(this.ausfuehrungSuch.getText());
        java.sql.Date sqd = new java.sql.Date(d.getTime());
        zusatzbetraege.addFilter("ausfuehrung = ?", new Object[] { sqd });
      }
      catch (ParseException e)
      {
        e.printStackTrace();
      }
    }
    zusatzbetraege.setOrder("ORDER BY ausfuehrung DESC, faelligkeit DESC");
    return zusatzbetraege;
  }

  private void nichtAktiveEliminieren(TablePart table) throws RemoteException
  {
    List<?> li = table.getItems();
    Iterator<?> it = li.iterator();
    while (it.hasNext())
    {
      Zusatzbetrag z = (Zusatzbetrag) it.next();
      if (!z.isAktiv())
      {
        table.removeItem(z);
      }
    }
  }

  public Button getPDFAusgabeButton()
  {
    Button b = new Button("&PDF-Ausgabe", new Action()
    {

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
              "Fehler beim Start der PDF-Ausgabe der Zusatzbeträge");
        }
      }
    }, null, true, "acroread.png");
    return b;
  }

  private void starteAuswertung() throws RemoteException
  {
    FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
    fd.setText("Ausgabedatei wählen.");
    String path = settings
        .getString("lastdir", System.getProperty("user.home"));
    if (path != null && path.length() > 0)
    {
      fd.setFilterPath(path);
    }
    fd.setFileName(new Dateiname("zusatzbetraege", "", Einstellungen
        .getEinstellung().getDateinamenmuster(), "PDF").get());
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
    final DBIterator it = getIterator();
    settings.setAttribute("lastdir", file.getParent());
    BackgroundTask t = new BackgroundTask()
    {

      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        try
        {
          FileOutputStream fos = new FileOutputStream(file);
          Reporter reporter = new Reporter(fos, monitor, "Zusatzbeträge", "",
              it.size());
          reporter.addHeaderColumn("Mitglied", Element.ALIGN_LEFT, 60,
              Color.LIGHT_GRAY);
          reporter.addHeaderColumn("Startdatum", Element.ALIGN_LEFT, 30,
              Color.LIGHT_GRAY);
          reporter.addHeaderColumn("nächste Fälligkeit", Element.ALIGN_LEFT,
              30, Color.LIGHT_GRAY);
          reporter.addHeaderColumn("letzte Ausführung", Element.ALIGN_LEFT, 30,
              Color.LIGHT_GRAY);
          reporter.addHeaderColumn("Intervall", Element.ALIGN_LEFT, 30,
              Color.LIGHT_GRAY);
          reporter.addHeaderColumn("Endedatum", Element.ALIGN_LEFT, 30,
              Color.LIGHT_GRAY);
          reporter.addHeaderColumn("Buchungstext", Element.ALIGN_LEFT, 50,
              Color.LIGHT_GRAY);
          reporter.addHeaderColumn("Betrag", Element.ALIGN_RIGHT, 30,
              Color.LIGHT_GRAY);
          reporter.createHeader();
          while (it.hasNext())
          {
            Zusatzbetrag z = (Zusatzbetrag) it.next();
            reporter.addColumn(z.getMitglied().getNameVorname(),
                Element.ALIGN_LEFT);
            reporter.addColumn(z.getStartdatum(), Element.ALIGN_LEFT);
            reporter.addColumn(z.getFaelligkeit(), Element.ALIGN_LEFT);
            reporter.addColumn(z.getAusfuehrung(), Element.ALIGN_LEFT);
            reporter.addColumn(z.getIntervallText(), Element.ALIGN_LEFT);
            reporter.addColumn(z.getEndedatum(), Element.ALIGN_LEFT);
            reporter.addColumn(z.getBuchungstext(), Element.ALIGN_LEFT);
            reporter.addColumn(z.getBetrag());
            reporter.setNextRecord();
          }
          reporter.closeTable();
          reporter.close();
          fos.close();
          monitor.setPercentComplete(100);
          monitor.setStatus(ProgressMonitor.STATUS_DONE);
          GUI.getStatusBar().setSuccessText("Auswertung gestartet");
          GUI.getCurrentView().reload();
        }
        catch (Exception e)
        {
          Logger.error("Fehler", e);
          monitor.setStatusText(e.getMessage());
          monitor.setStatus(ProgressMonitor.STATUS_ERROR);
          GUI.getStatusBar().setErrorText(e.getMessage());
          throw new ApplicationException(e);
        }
        GUI.getDisplay().asyncExec(new Runnable()
        {

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

      public void interrupt()
      {
        //
      }

      public boolean isInterrupted()
      {
        return false;
      }
    };
    Application.getController().start(t);

  }
}