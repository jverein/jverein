/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * jost@berlios.de
 * jverein.berlios.de
 * $Log$
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.Vector;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.ZusatzabbuchungAction;
import de.jost_net.JVerein.gui.menu.ZusatzabbuchungMenu;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Zusatzabbuchung;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
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
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class ZusatzabbuchungControl extends AbstractControl
{
  private DateInput faelligkeit = null;

  private Input buchungstext;

  private DecimalInput betrag;

  private Zusatzabbuchung zuab;

  private DateInput ausfuehrung = null;

  private SelectInput ausfuehrungSuch = null;

  private TablePart zusatzabbuchungsList;

  public ZusatzabbuchungControl(AbstractView view)
  {
    super(view);
  }

  public Zusatzabbuchung getZusatzabbuchung()
  {
    if (zuab != null)
    {
      return zuab;
    }
    zuab = (Zusatzabbuchung) getCurrentObject();
    return zuab;
  }

  public DateInput getFaelligkeit() throws RemoteException
  {
    if (faelligkeit != null)
    {
      return faelligkeit;
    }

    Date d = getZusatzabbuchung().getFaelligkeit();

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
    buchungstext = new TextInput(getZusatzabbuchung().getBuchungstext(), 27);
    return buchungstext;
  }

  public DecimalInput getBetrag() throws RemoteException
  {
    if (betrag != null)
    {
      return betrag;
    }
    betrag = new DecimalInput(getZusatzabbuchung().getBetrag(),
        Einstellungen.DECIMALFORMAT);
    return betrag;
  }

  public DateInput getAusfuehrung() throws RemoteException
  {
    if (ausfuehrung != null)
    {
      return ausfuehrung;
    }

    Date d = getZusatzabbuchung().getAusfuehrung();

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

  public SelectInput getAusführungSuch() throws RemoteException
  {
    if (ausfuehrungSuch != null)
    {
      return ausfuehrungSuch;
    }

    final Vector werte = new Vector();
    werte.addElement("Alle");
    werte.addElement("Noch nicht ausgeführt");

    String sql = "select ausfuehrung from zusatzabbuchung where ausfuehrung is not null "
        + "group by ausfuehrung order by ausfuehrung desc";
    DBService service = Einstellungen.getDBService();

    ResultSetExtractor rs = new ResultSetExtractor()
    {
      public Object extract(ResultSet rs) throws RemoteException, SQLException
      {
        while (rs.next())
        {
          werte.addElement(Einstellungen.DATEFORMAT.format(rs.getDate(1)));
        }
        return null;
      }
    };
    service.execute(sql, new Object[] {}, rs);

    ausfuehrungSuch = new SelectInput(werte, (String) werte.elementAt(1));
    ausfuehrungSuch.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        try
        {
          getZusatzabbuchungsList();
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
      Zusatzabbuchung z = getZusatzabbuchung();
      z.setFaelligkeit((Date) getFaelligkeit().getValue());
      z.setBuchungstext((String) getBuchungstext().getValue());
      Double d = (Double) getBetrag().getValue();
      z.setBetrag(d);
      try
      {
        z.store();
        GUI.getStatusBar().setSuccessText("Zusatzabbuchung gespeichert");
      }
      catch (ApplicationException e)
      {
        GUI.getView().setErrorText(e.getMessage());
      }
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei speichern der Zusatzabbuchung";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  public Part getZusatzabbuchungsList() throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator zusatzabbuchungen = service.createList(Zusatzabbuchung.class);
    if (this.ausfuehrungSuch.getText().equals("Alle"))
    {
      // nichts tun
    }
    else if (this.ausfuehrungSuch.getText().equals("Noch nicht ausgeführt"))
    {
      zusatzabbuchungen.addFilter("ausfuehrung is null");
    }
    else
    {
      try
      {
        Date d = Einstellungen.DATEFORMAT.parse(this.ausfuehrungSuch.getText());
        java.sql.Date sqd = new java.sql.Date(d.getTime());
        zusatzabbuchungen.addFilter("ausfuehrung = ?", new Object[] { sqd });
      }
      catch (ParseException e)
      {
        e.printStackTrace();
      }
    }
    zusatzabbuchungen.setOrder("ORDER BY ausfuehrung DESC, faelligkeit DESC");

    if (zusatzabbuchungsList == null)
    {
      zusatzabbuchungsList = new TablePart(zusatzabbuchungen,
          new ZusatzabbuchungAction(null));
      zusatzabbuchungsList.addColumn("Fälligkeit", "faelligkeit",
          new DateFormatter(Einstellungen.DATEFORMAT));
      zusatzabbuchungsList.addColumn("Name", "mitglied", new Formatter()
      {
        public String format(Object o)
        {
          Mitglied m = (Mitglied) o;
          if (m == null)
            return null;
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
      zusatzabbuchungsList.addColumn("Buchungstext", "buchungstext");
      zusatzabbuchungsList.addColumn("Betrag", "betrag", new CurrencyFormatter(
          "", Einstellungen.DECIMALFORMAT));
      zusatzabbuchungsList.addColumn("Ausführung", "ausfuehrung",
          new DateFormatter(Einstellungen.DATEFORMAT));
      zusatzabbuchungsList.setContextMenu(new ZusatzabbuchungMenu());
      zusatzabbuchungsList.setRememberColWidths(true);
      zusatzabbuchungsList.setRememberOrder(true);
      zusatzabbuchungsList.setSummary(true);
    }
    else
    {
      zusatzabbuchungsList.removeAll();
      while (zusatzabbuchungen.hasNext())
      {
        zusatzabbuchungsList
            .addItem((Zusatzabbuchung) zusatzabbuchungen.next());
      }
    }
    return zusatzabbuchungsList;
  }
}
