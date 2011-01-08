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
 * Revision 1.15  2010-12-17 19:05:03  jost
 * Bugfix Mitgliedskonto Summen
 *
 * Revision 1.14  2010-11-06 20:13:06  jost
 * Bugfix SQL-Statement (Fehlbetrag)
 *
 * Revision 1.13  2010-10-28 19:14:05  jost
 * Neu: Wohnsitzstaat
 *
 * Revision 1.12  2010-10-15 09:58:26  jost
 * Code aufgeräumt
 *
 * Revision 1.11  2010-09-12 07:43:16  jost
 * Bugfix Sollsumme.
 * Siehe auch http://www.jverein.de/forum/viewtopic.php?f=5&t=197
 *
 * Revision 1.10  2010-08-23 13:36:42  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.9  2010-08-16 20:16:45  jost
 * Neu: Mahnung
 *
 * Revision 1.8  2010-08-15 19:00:50  jost
 * Rechnungen auch für einen vorgegebenen Zeitraum ausgeben.
 *
 * Revision 1.7  2010-08-10 18:06:30  jost
 * Zahlungswegtexte für den Rechnungsdruck
 *
 * Revision 1.6  2010-08-10 15:58:58  jost
 * neues Feld Zahlungsgrund
 *
 * Revision 1.5  2010-08-10 05:39:04  jost
 * *** empty log message ***
 *
 * Revision 1.4  2010-08-08 19:32:56  jost
 * Zusammenfassung der Rechnungen
 *
 * Revision 1.3  2010-08-04 10:40:09  jost
 * Prerelease Rechnung
 *
 * Revision 1.2  2010-07-25 18:31:55  jost
 * Neu: Mitgliedskonto
 *
 * Revision 1.1  2010/05/18 20:19:24  jost
 * Vorabversion Mitgliedskonto
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.Messaging.MitgliedskontoMessage;
import de.jost_net.JVerein.gui.formatter.ZahlungswegFormatter;
import de.jost_net.JVerein.gui.input.FormularInput;
import de.jost_net.JVerein.gui.menu.MitgliedskontoMenu;
import de.jost_net.JVerein.io.FormularAufbereitung;
import de.jost_net.JVerein.keys.Formularart;
import de.jost_net.JVerein.keys.Zahlungsrhytmus;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Formular;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.jost_net.JVerein.util.Dateiname;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.pseudo.PseudoIterator;
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
import de.willuhn.jameica.gui.formatter.TreeFormatter;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.DecimalInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.ContextMenu;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.gui.parts.TreePart;
import de.willuhn.jameica.gui.util.SWTUtil;
import de.willuhn.jameica.messaging.Message;
import de.willuhn.jameica.messaging.MessageConsumer;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class MitgliedskontoControl extends AbstractControl
{

  private de.willuhn.jameica.system.Settings settings;

  private DateInput datum = null;

  private Input zweck1;

  private Input zweck2;

  private SelectInput zahlungsweg;

  private DecimalInput betrag;

  private FormularInput formular = null;

  private FormularAufbereitung fa;

  private Mitgliedskonto mkto;

  private TablePart mitgliedskontoList;

  private TablePart mitgliedskontoList2;

  private TreePart mitgliedskontoTree;

  public static final String DATUM_MITGLIEDSKONTO = "datum.mitgliedskonto.";

  public static final String DATUM_RECHNUNG = "datum.rechnung.";

  public static final String DATUM_MAHNUNG = "datum.mahnung.";

  private String datumverwendung = null;

  private DateInput vondatum = null;

  private DateInput bisdatum = null;

  private TextInput suchname = null;

  private TextInput suchname2 = null;

  private SelectInput differenz = null;

  // private CheckboxInput offenePosten = null;

  private MitgliedskontoMessageConsumer mc = null;

  private Action action;

  private ContextMenu menu;

  public MitgliedskontoControl(AbstractView view)
  {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public Mitgliedskonto getMitgliedskonto()
  {
    if (mkto != null)
    {
      return mkto;
    }
    mkto = (Mitgliedskonto) getCurrentObject();
    return mkto;
  }

  public DateInput getDatum() throws RemoteException
  {
    if (datum != null)
    {
      return datum;
    }

    Date d = getMitgliedskonto().getDatum();

    this.datum = new DateInput(d, Einstellungen.DATEFORMAT);
    this.datum.setTitle("Datum");
    this.datum.setText("Bitte Datum wählen");
    this.datum.addListener(new Listener()
    {

      public void handleEvent(Event event)
      {
        Date date = (Date) datum.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    this.datum.setMandatory(true);
    return datum;
  }

  public Input getZweck1() throws RemoteException
  {
    if (zweck1 != null)
    {
      return zweck1;
    }
    zweck1 = new TextInput(getMitgliedskonto().getZweck1(), 27);
    zweck1.setMandatory(true);
    return zweck1;
  }

  public Input getZweck2() throws RemoteException
  {
    if (zweck2 != null)
    {
      return zweck2;
    }
    zweck2 = new TextInput(getMitgliedskonto().getZweck2(), 27);
    return zweck2;
  }

  public SelectInput getZahlungsweg() throws RemoteException
  {
    if (zahlungsweg != null)
    {
      return zahlungsweg;
    }
    zahlungsweg = new SelectInput(Zahlungsweg.getArray(), new Zahlungsweg(
        Einstellungen.getEinstellung().getZahlungsweg()));
    zahlungsweg.setName("Zahlungsweg");
    return zahlungsweg;
  }

  public DecimalInput getBetrag() throws RemoteException
  {
    if (betrag != null)
    {
      return betrag;
    }
    betrag = new DecimalInput(getMitgliedskonto().getBetrag(),
        Einstellungen.DECIMALFORMAT);
    return betrag;
  }

  public FormularInput getFormular(int formulartyp) throws RemoteException
  {
    if (formular != null)
    {
      return formular;
    }
    formular = new FormularInput(formulartyp);
    return formular;
  }

  public DateInput getVondatum(String datumverwendung)
  {
    if (vondatum != null)
    {
      return vondatum;
    }
    Date d = null;
    this.datumverwendung = datumverwendung;

    String tmp = settings.getString(datumverwendung + "datumvon", null);
    if (tmp != null)
    {
      try
      {
        d = Einstellungen.DATEFORMAT.parse(tmp);
      }
      catch (ParseException e)
      {
        //
      }
    }

    this.vondatum = new DateInput(d, Einstellungen.DATEFORMAT);
    this.vondatum.setTitle("Anfangsdatum");
    this.vondatum.setText("Bitte Anfangsdatum wählen");
    vondatum.addListener(new FilterListener());
    return vondatum;
  }

  public DateInput getBisdatum(String datumverwendung)
  {
    if (bisdatum != null)
    {
      return bisdatum;
    }
    this.datumverwendung = datumverwendung;
    Date d = null;
    String tmp = settings.getString(datumverwendung + "datumbis", null);
    if (tmp != null)
    {
      try
      {
        d = Einstellungen.DATEFORMAT.parse(tmp);
      }
      catch (ParseException e)
      {
        //
      }
    }
    this.bisdatum = new DateInput(d, Einstellungen.DATEFORMAT);
    this.bisdatum.setTitle("Endedatum");
    this.bisdatum.setText("Bitte Endedatum wählen");
    bisdatum.addListener(new FilterListener());
    return bisdatum;
  }

  public SelectInput getDifferenz(String defaultval)
  {
    if (differenz != null)
    {
      return differenz;
    }
    differenz = new SelectInput(new Object[] { "egal", "Fehlbetrag",
        "Überzahlung" }, defaultval);
    differenz.setName("Differenz");
    differenz.addListener(new FilterListener());
    return differenz;
  }

  public TextInput getSuchName()
  {
    if (suchname != null)
    {
      return suchname;
    }
    suchname = new TextInput("", 30);
    suchname.setName("Name");
    suchname.addListener(new FilterListener());
    return suchname;
  }

  public TextInput getSuchName2()
  {
    if (suchname2 != null)
    {
      return suchname2;
    }
    suchname2 = new TextInput("", 30);
    suchname2.setName("Name");
    suchname2.addListener(new FilterListener());
    return suchname2;
  }

  public void handleStore()
  {
    try
    {
      Mitgliedskonto mkto = getMitgliedskonto();
      mkto.setBetrag((Double) getBetrag().getValue());
      mkto.setDatum((Date) getDatum().getValue());
      Zahlungsweg zw = (Zahlungsweg) getZahlungsweg().getValue();
      mkto.setZahlungsweg(zw.getKey());
      mkto.setZweck1((String) getZweck1().getValue());
      mkto.setZweck2((String) getZweck2().getValue());
      mkto.store();
      GUI.getStatusBar().setSuccessText("Mitgliedskonto gespeichert");
    }
    catch (ApplicationException e)
    {
      GUI.getStatusBar().setErrorText(e.getMessage());
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei speichern";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  public Part getMitgliedskontoTree(Mitglied mitglied) throws RemoteException
  {
    mitgliedskontoTree = new TreePart(new MitgliedskontoNode(mitglied), null)
    {

      @SuppressWarnings("unchecked")
      @Override
      public void paint(Composite composite) throws RemoteException
      {
        super.paint(composite);
        List<MitgliedskontoNode> items = mitgliedskontoTree.getItems();
        for (MitgliedskontoNode mkn : items)
        {
          GenericIterator items2 = mkn.getChildren();
          while (items2.hasNext())
          {
            MitgliedskontoNode mkn2 = (MitgliedskontoNode) items2.next();
            mitgliedskontoTree.setExpanded(mkn2, false);
          }
        }
      }
    };
    mitgliedskontoTree.addColumn("Name, Vorname", "name");
    mitgliedskontoTree.addColumn("Datum", "datum", new DateFormatter(
        Einstellungen.DATEFORMAT));
    mitgliedskontoTree.addColumn("Zweck1", "zweck1");
    mitgliedskontoTree.addColumn("Zweck2", "zweck2");
    mitgliedskontoTree.addColumn("Zahlungsweg", "zahlungsweg",
        new ZahlungswegFormatter());
    mitgliedskontoTree.addColumn("Soll", "soll", new CurrencyFormatter("",
        Einstellungen.DECIMALFORMAT));
    mitgliedskontoTree.addColumn("Ist", "ist", new CurrencyFormatter("",
        Einstellungen.DECIMALFORMAT));
    mitgliedskontoTree.addColumn("Differenz", "differenz",
        new CurrencyFormatter("", Einstellungen.DECIMALFORMAT));
    mitgliedskontoTree.setContextMenu(new MitgliedskontoMenu());
    mitgliedskontoTree.setRememberColWidths(true);
    mitgliedskontoTree.setRememberOrder(true);
    mitgliedskontoTree.setFormatter(new MitgliedskontoTreeFormatter());
    this.mc = new MitgliedskontoMessageConsumer();
    Application.getMessagingFactory().registerMessageConsumer(this.mc);

    return mitgliedskontoTree;
  }

  public TablePart getMitgliedskontoList(Action action, ContextMenu menu)
      throws RemoteException
  {
    this.action = action;
    GenericIterator mitgliedskonten = getMitgliedskontoIterator();
    if (mitgliedskontoList == null)
    {
      mitgliedskontoList = new TablePart(mitgliedskonten, action);
      mitgliedskontoList.addColumn("Datum", "datum", new DateFormatter(
          Einstellungen.DATEFORMAT));
      mitgliedskontoList.addColumn("Abrechnungslauf", "abrechnungslauf");
      mitgliedskontoList.addColumn("Name", "mitglied");
      mitgliedskontoList.addColumn("Zweck1", "zweck1");
      mitgliedskontoList.addColumn("Zweck2", "zweck2");
      mitgliedskontoList.addColumn("Betrag", "betrag", new CurrencyFormatter(
          "", Einstellungen.DECIMALFORMAT));
      mitgliedskontoList.addColumn("Zahlungseingang", "istbetrag",
          new CurrencyFormatter("", Einstellungen.DECIMALFORMAT));
      mitgliedskontoList.setContextMenu(menu);
      mitgliedskontoList.setRememberColWidths(true);
      mitgliedskontoList.setRememberOrder(true);
      mitgliedskontoList.setMulti(true);
      mitgliedskontoList.setSummary(true);
    }
    else
    {
      mitgliedskontoList.removeAll();
      while (mitgliedskonten.hasNext())
      {
        mitgliedskontoList.addItem(mitgliedskonten.next());
      }
    }
    return mitgliedskontoList;
  }

  public TablePart getMitgliedskontoList2(Action action, ContextMenu menu)
      throws RemoteException
  {
    this.action = action;
    DBIterator mitglieder = Einstellungen.getDBService().createList(
        Mitglied.class);
    if (suchname2 != null && suchname2.getValue() != null)
    {
      String where = "";
      ArrayList<String> object = new ArrayList<String>();
      StringTokenizer tok = new StringTokenizer((String) suchname2.getValue(),
          " ,-");
      int count = 0;
      while (tok.hasMoreElements())
      {
        if (count > 0)
        {
          where += "OR ";
        }
        count++;
        where += "upper(name) like upper(?) or upper(vorname) like upper(?) ";
        String o = tok.nextToken();
        object.add(o);
        object.add(o);
      }
      mitglieder.addFilter(where, object.toArray());
    }
    mitglieder.setOrder("order by name, vorname");
    if (mitgliedskontoList2 == null)
    {
      mitgliedskontoList2 = new TablePart(mitglieder, action);
      mitgliedskontoList2.addColumn("Name", "name");
      mitgliedskontoList2.addColumn("Vorname", "vorname");
      mitgliedskontoList2.setContextMenu(menu);
      mitgliedskontoList2.setRememberColWidths(true);
      mitgliedskontoList2.setRememberOrder(true);
      mitgliedskontoList2.setMulti(true);
      mitgliedskontoList2.setSummary(true);
    }
    else
    {
      mitgliedskontoList2.removeAll();
      while (mitglieder.hasNext())
      {
        mitgliedskontoList2.addItem(mitglieder.next());
      }
    }
    return mitgliedskontoList2;
  }

  private GenericIterator getMitgliedskontoIterator() throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    Date d1 = null;
    java.sql.Date vd = null;
    java.sql.Date bd = null;
    if (vondatum != null)
    {
      d1 = (Date) vondatum.getValue();
      if (d1 != null)
      {
        settings.setAttribute(datumverwendung + "datumvon",
            Einstellungen.DATEFORMAT.format(d1));
        vd = new java.sql.Date(d1.getTime());
      }
      else
      {
        settings.setAttribute(datumverwendung + "datumvon", "");
      }
    }
    if (bisdatum != null)
    {
      d1 = (Date) bisdatum.getValue();
      if (d1 != null)
      {
        settings.setAttribute(datumverwendung + "datumbis",
            Einstellungen.DATEFORMAT.format(d1));
        bd = new java.sql.Date(d1.getTime());
      }
      else
      {
        settings.setAttribute(datumverwendung + "datumbis", "");
      }
    }
    String sql = "select  mitgliedskonto.*, mitgliedskonto.betrag as sollsumme, "
        + "sum(buchung.betrag)  istsumme,mitglied.name, mitglied.vorname from mitgliedskonto "
        + "join mitglied on (mitgliedskonto.mitglied = mitglied.id) "
        + "left join buchung  on (buchung.mitgliedskonto = mitgliedskonto.id ) ";
    String where = "";
    ArrayList<Object> param = new ArrayList<Object>();
    if (vd != null)
    {
      where += (where.length() > 0 ? "and " : "")
          + "mitgliedskonto.datum >= ? ";
      param.add(vd);
    }
    if (bd != null)
    {
      where += (where.length() > 0 ? "and " : "")
          + "mitgliedskonto.datum <= ? ";
      param.add(bd);
    }
    if (suchname != null && suchname.getValue() != null)
    {
      StringTokenizer tok = new StringTokenizer((String) suchname.getValue(),
          " ,-");
      boolean hasElements = tok.hasMoreElements();
      if (hasElements && where.length() > 0)
      {
        where += "and ";
      }
      if (hasElements)
      {
        where += "(";
      }
      int count = 0;
      while (tok.hasMoreElements())
      {
        if (count > 0)
        {
          where += "OR ";
        }
        count++;
        where += "upper(mitglied.name) like upper(?) or upper(mitglied.vorname) like upper(?) or upper(zweck1) like upper(?) ";
        String token = "%" + tok.nextToken() + "%";
        param.add(token);
        param.add(token);
        param.add(token);
      }
      if (hasElements)
      {
        where += ") ";
      }
    }
    if (where.length() > 0)
    {
      sql += "WHERE " + where;
    }
    sql += "group by mitgliedskonto.id ";
    String diff = "";
    if (differenz != null)
    {
      diff = (String) differenz.getValue();
    }
    if (diff.equals("Fehlbetrag"))
    {
      sql += "having sollsumme > istsumme or istsumme is null ";
    }
    if (diff.equals("Überzahlung"))
    {
      sql += "having sollsumme < istsumme ";
    }
    // if (offenePosten != null && (Boolean) offenePosten.getValue())
    // {
    // sql += "having sollsumme > istsumme or istsumme is null ";
    // }
    sql += "order by mitglied.name, mitglied.vorname, mitgliedskonto.datum desc";
    PseudoIterator mitgliedskonten = (PseudoIterator) service.execute(sql,
        param.toArray(), new ResultSetExtractor()
        {

          public Object extract(ResultSet rs) throws RemoteException,
              SQLException
          {
            ArrayList<Mitgliedskonto> ergebnis = new ArrayList<Mitgliedskonto>();
            while (rs.next())
            {
              Mitgliedskonto mk = (Mitgliedskonto) Einstellungen.getDBService()
                  .createObject(Mitgliedskonto.class, rs.getString(1));
              mk.setBetrag(rs.getDouble("sollsumme"));
              mk.setIstBetrag(rs.getDouble("istsumme"));
              ergebnis.add(mk);
            }
            return PseudoIterator.fromArray(ergebnis
                .toArray(new GenericObject[ergebnis.size()]));
          }
        });

    return mitgliedskonten;
  }

  public Button getStartRechnungButton(final Object currentObject)
  {
    Button button = new Button("&starten", new Action()
    {

      public void handleAction(Object context)
      {

        try
        {
          generiereRechnung(currentObject);
        }
        catch (RemoteException e)
        {
          Logger.error("", e);
          GUI.getStatusBar().setErrorText(e.getMessage());
        }
        catch (IOException e)
        {
          Logger.error("", e);
          GUI.getStatusBar().setErrorText(e.getMessage());
        }
      }
    }, null, true, "go.png");
    return button;
  }

  private void generiereRechnung(Object currentObject) throws IOException
  {
    FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
    fd.setText("Ausgabedatei wählen.");
    String path = settings
        .getString("lastdir", System.getProperty("user.home"));
    if (path != null && path.length() > 0)
    {
      fd.setFilterPath(path);
    }
    fd.setFileName(new Dateiname("rechnung", "", Einstellungen.getEinstellung()
        .getDateinamenmuster(), "PDF").get());
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
    settings.setAttribute("lastdir", file.getParent());
    Formular form = (Formular) getFormular(Formularart.RECHNUNG).getValue();
    Formular fo = (Formular) Einstellungen.getDBService().createObject(
        Formular.class, form.getID());
    fa = new FormularAufbereitung(file);
    ArrayList<ArrayList<Mitgliedskonto>> mks = null;
    if (currentObject != null)
    {
      mks = getRechnungsempfaenger(currentObject);
    }
    else
    {
      DBIterator it = Einstellungen.getDBService().createList(
          Mitgliedskonto.class);
      Date d = null;
      if (getVondatum(datumverwendung).getValue() != null)
      {
        d = (Date) getVondatum(datumverwendung).getValue();
        if (d != null)
        {
          settings.setAttribute(datumverwendung + "datumvon",
              Einstellungen.DATEFORMAT.format(d));
        }

        it.addFilter("datum >= ?", new Object[] { d });
      }
      else
      {
        settings.setAttribute(datumverwendung + "datumvon", "");
      }
      if (getBisdatum(datumverwendung).getValue() != null)
      {
        d = (Date) getBisdatum(datumverwendung).getValue();
        if (d != null)
        {
          settings.setAttribute(datumverwendung + "datumbis",
              Einstellungen.DATEFORMAT.format(d));
        }
        it.addFilter("datum <= ?", new Object[] { d });
      }
      else
      {
        settings.setAttribute(datumverwendung + "datumbis", "");
      }

      Mitgliedskonto[] mk = new Mitgliedskonto[it.size()];
      int i = 0;
      while (it.hasNext())
      {
        mk[i] = (Mitgliedskonto) it.next();
        i++;
      }
      mks = getRechnungsempfaenger(mk);
    }
    for (ArrayList<Mitgliedskonto> mk : mks)
    {
      aufbereitenFormular(mk, fo);
    }
    fa.showFormular();

  }

  public Button getStartMahnungButton(final Object currentObject)
  {
    Button button = new Button("&starten", new Action()
    {

      public void handleAction(Object context)
      {

        try
        {
          generiereMahnung(currentObject);
        }
        catch (RemoteException e)
        {
          Logger.error("", e);
          GUI.getStatusBar().setErrorText(e.getMessage());
        }
        catch (IOException e)
        {
          Logger.error("", e);
          GUI.getStatusBar().setErrorText(e.getMessage());
        }
      }
    }, null, true, "go.png");
    return button;
  }

  private void generiereMahnung(Object currentObject) throws IOException
  {
    FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
    fd.setText("Ausgabedatei wählen.");
    String path = settings
        .getString("lastdir", System.getProperty("user.home"));
    if (path != null && path.length() > 0)
    {
      fd.setFilterPath(path);
    }
    fd.setFileName(new Dateiname("mahnung", "", Einstellungen.getEinstellung()
        .getDateinamenmuster(), "PDF").get());
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
    settings.setAttribute("lastdir", file.getParent());
    Formular form = (Formular) getFormular(Formularart.MAHNUNG).getValue();
    Formular fo = (Formular) Einstellungen.getDBService().createObject(
        Formular.class, form.getID());
    fa = new FormularAufbereitung(file);
    ArrayList<ArrayList<Mitgliedskonto>> mks = null;
    if (currentObject != null)
    {
      mks = getRechnungsempfaenger(currentObject);
    }
    else
    {
      GenericIterator it = getMitgliedskontoIterator();
      //
      //
      // Einstellungen.getDBService().createList(
      // Mitgliedskonto.class);
      // Date d = null;
      // if (getVondatum(datumverwendung).getValue() != null)
      // {
      // d = (Date) getVondatum(datumverwendung).getValue();
      // if (d != null)
      // {
      // settings.setAttribute(datumverwendung + "datumvon",
      // Einstellungen.DATEFORMAT.format(d));
      // }
      //
      // it.addFilter("datum >= ?", new Object[] { d });
      // }
      // else
      // {
      // settings.setAttribute(datumverwendung + "datumvon", "");
      // }
      // if (getBisdatum(datumverwendung).getValue() != null)
      // {
      // d = (Date) getBisdatum(datumverwendung).getValue();
      // if (d != null)
      // {
      // settings.setAttribute(datumverwendung + "datumbis",
      // Einstellungen.DATEFORMAT.format(d));
      // }
      // it.addFilter("datum <= ?", new Object[] { d });
      // }
      // else
      // {
      // settings.setAttribute(datumverwendung + "datumbis", "");
      // }

      Mitgliedskonto[] mk = new Mitgliedskonto[it.size()];
      int i = 0;
      while (it.hasNext())
      {
        mk[i] = (Mitgliedskonto) it.next();
        i++;
      }
      mks = getRechnungsempfaenger(mk);
    }
    for (ArrayList<Mitgliedskonto> mk : mks)
    {
      aufbereitenFormular(mk, fo);
    }
    fa.showFormular();

  }

  private void aufbereitenFormular(ArrayList<Mitgliedskonto> mk, Formular fo)
      throws RemoteException
  {
    HashMap<String, Object> map = new HashMap<String, Object>();

    Mitglied m = mk.get(0).getMitglied();

    String empfaenger = m.getAnrede()
        + "\n"
        + m.getVornameName()
        + "\n"
        + (m.getAdressierungszusatz().length() > 0 ? m.getAdressierungszusatz()
            + "\n" : "") + m.getStrasse() + "\n" + m.getPlz() + " "
        + m.getOrt();
    if (m.getStaat() != null && m.getStaat().length() > 0)
    {
      empfaenger += "\n" + m.getStaat();
    }
    map.put(FormularfeldControl.EMPFAENGER, empfaenger);
    ArrayList<Date> buda = new ArrayList<Date>();
    ArrayList<String> zg = new ArrayList<String>();
    ArrayList<String> zg1 = new ArrayList<String>();
    ArrayList<String> zg2 = new ArrayList<String>();
    ArrayList<Double> betrag = new ArrayList<Double>();
    double summe = 0;
    for (Mitgliedskonto mkto : mk)
    {
      buda.add(mkto.getDatum());
      zg.add(mkto.getZweck1() + " " + mkto.getZweck2());
      zg1.add(mkto.getZweck1());
      zg2.add(mkto.getZweck2());
      betrag.add(new Double(mkto.getBetrag()));
      summe += mkto.getBetrag();
    }
    if (buda.size() > 1)
    {
      zg1.add("Summe");
      zg.add("Summe");
      betrag.add(summe);
    }
    map.put(FormularfeldControl.BUCHUNGSDATUM, buda.toArray());
    map.put(FormularfeldControl.ZAHLUNGSGRUND, zg.toArray());
    map.put(FormularfeldControl.ZAHLUNGSGRUND1, zg1.toArray());
    map.put(FormularfeldControl.ZAHLUNGSGRUND2, zg2.toArray());
    map.put(FormularfeldControl.BETRAG, betrag.toArray());
    map.put(FormularfeldControl.ID, m.getID());
    map.put(FormularfeldControl.EXTERNEMITGLIEDSNUMMER,
        m.getExterneMitgliedsnummer());
    map.put(FormularfeldControl.ANREDE, m.getAnrede());
    map.put(FormularfeldControl.TITEL, m.getTitel());
    map.put(FormularfeldControl.NAME, m.getName());
    map.put(FormularfeldControl.VORNAME, m.getVorname());
    map.put(FormularfeldControl.ADRESSIERUNGSZUSATZ, m.getAdressierungszusatz());
    map.put(FormularfeldControl.STRASSE, m.getStrasse());
    map.put(FormularfeldControl.PLZ, m.getPlz());
    map.put(FormularfeldControl.ORT, m.getOrt());
    map.put(FormularfeldControl.STAAT, m.getStaat());
    map.put(FormularfeldControl.ZAHLUNGSRHYTMUS,
        new Zahlungsrhytmus(m.getZahlungsrhytmus()).getText());
    map.put(FormularfeldControl.BLZ, m.getBlz());
    map.put(FormularfeldControl.KONTO, m.getKonto());
    map.put(FormularfeldControl.KONTOINHABER, m.getKontoinhaber());
    map.put(FormularfeldControl.GEBURTSDATUM, m.getGeburtsdatum());
    map.put(FormularfeldControl.GESCHLECHT, m.getGeschlecht());
    map.put(FormularfeldControl.TELEFONPRIVAT, m.getTelefonprivat());
    map.put(FormularfeldControl.TELEFONDIENSTLICH, m.getTelefondienstlich());
    map.put(FormularfeldControl.HANDY, m.getHandy());
    map.put(FormularfeldControl.EMAIL, m.getEmail());
    map.put(FormularfeldControl.EINTRITT, m.getEintritt());
    map.put(FormularfeldControl.BEITRAGSGRUPPE, m.getBeitragsgruppe()
        .getBezeichnung());
    map.put(FormularfeldControl.AUSTRITT, m.getAustritt());
    map.put(FormularfeldControl.KUENDIGUNG, m.getKuendigung());
    String zahlungsweg = "";
    switch (mk.get(0).getMitglied().getZahlungsweg())
    {
      case Zahlungsweg.ABBUCHUNG:
      {
        zahlungsweg = Einstellungen.getEinstellung().getRechnungTextAbbuchung();
        zahlungsweg = zahlungsweg.replaceAll("\\$\\{Konto\\}", m.getKonto());
        zahlungsweg = zahlungsweg.replaceAll("\\$\\{BLZ\\}", m.getBlz());
        break;
      }
      case Zahlungsweg.BARZAHLUNG:
      {
        zahlungsweg = Einstellungen.getEinstellung().getRechnungTextBar();
        break;
      }
      case Zahlungsweg.ÜBERWEISUNG:
      {
        zahlungsweg = Einstellungen.getEinstellung()
            .getRechnungTextUeberweisung();
        break;
      }
    }
    map.put(FormularfeldControl.ZAHLUNGSWEG, zahlungsweg);
    map.put(FormularfeldControl.TAGESDATUM,
        Einstellungen.DATEFORMAT.format(new Date()));

    fa.writeForm(fo, map);
  }

  /**
   * Liefert ein Array pro Mitglied mit Arrays der einzelnen Rechnungspositionen
   * 
   * @param currentObject
   * @return
   */
  private ArrayList<ArrayList<Mitgliedskonto>> getRechnungsempfaenger(
      Object currentObject)
  {
    ArrayList<ArrayList<Mitgliedskonto>> ret = new ArrayList<ArrayList<Mitgliedskonto>>();
    if (currentObject instanceof Mitgliedskonto)
    {
      Mitgliedskonto mk = (Mitgliedskonto) currentObject;
      ArrayList<Mitgliedskonto> r = new ArrayList<Mitgliedskonto>();
      r.add(mk);
      ret.add(r);
      return ret;
    }
    if (currentObject instanceof Mitgliedskonto[])
    {
      Mitgliedskonto[] mkn = (Mitgliedskonto[]) currentObject;
      Arrays.sort(mkn, new Comparator<Mitgliedskonto>()
      {

        public int compare(Mitgliedskonto mk1, Mitgliedskonto mk2)
        {
          try
          {
            int c = mk1.getMitglied().getName()
                .compareTo(mk2.getMitglied().getName());
            if (c != 0)
            {
              return c;
            }
            c = mk1.getMitglied().getVorname()
                .compareTo(mk2.getMitglied().getVorname());
            if (c != 0)
            {
              return c;
            }
            return mk1.getMitglied().getID()
                .compareTo(mk2.getMitglied().getID());
          }
          catch (RemoteException e)
          {
            throw new RuntimeException(e);
          }
        }
      });
      try
      {
        ArrayList<Mitgliedskonto> r = new ArrayList<Mitgliedskonto>();
        r = new ArrayList<Mitgliedskonto>();
        for (Mitgliedskonto mk : mkn)
        {
          if (r.size() == 0
              || r.get(0).getMitglied().getID()
                  .equals(mk.getMitglied().getID()))
          {
            r.add(mk);
          }
          else
          {
            ret.add(r);
            r = new ArrayList<Mitgliedskonto>();
            r.add(mk);
          }
        }
        if (r.size() > 0)
        {
          ret.add(r);
        }
      }
      catch (RemoteException e)
      {
        throw new RuntimeException(e);
      }
    }
    return ret;
  }

  private class FilterListener implements Listener
  {

    public void handleEvent(Event event)
    {
      if (event.type == SWT.Selection || event.type != SWT.FocusOut)
      {
        try
        {
          getMitgliedskontoList(action, menu);
        }
        catch (RemoteException e)
        {
          Logger.error("Fehler", e);
        }
      }
    }
  }

  public class MitgliedskontoTreeFormatter implements TreeFormatter
  {

    public void format(TreeItem item)
    {
      MitgliedskontoNode mkn = (MitgliedskontoNode) item.getData();
      switch (mkn.getType())
      {
        case MitgliedskontoNode.MITGLIED:
          item.setImage(0, SWTUtil.getImage("user_suit.png"));
          break;
        case MitgliedskontoNode.SOLL:
          item.setImage(0, SWTUtil.getImage("accessories-calculator.png"));
          item.setExpanded(false);
          break;
        case MitgliedskontoNode.IST:
          item.setImage(0, SWTUtil.getImage("bundle-16x16x32b.png"));
          break;
      }
    }
  }

  /**
   * Wird benachrichtigt um die Anzeige zu aktualisieren.
   */
  private class MitgliedskontoMessageConsumer implements MessageConsumer
  {

    /**
     * @see de.willuhn.jameica.messaging.MessageConsumer#autoRegister()
     */
    public boolean autoRegister()
    {
      return false;
    }

    /**
     * @see de.willuhn.jameica.messaging.MessageConsumer#getExpectedMessageTypes()
     */
    public Class<?>[] getExpectedMessageTypes()
    {
      return new Class[] { MitgliedskontoMessage.class };
    }

    /**
     * @see de.willuhn.jameica.messaging.MessageConsumer#handleMessage(de.willuhn.jameica.messaging.Message)
     */
    public void handleMessage(final Message message) throws Exception
    {
      GUI.getDisplay().syncExec(new Runnable()
      {

        public void run()
        {
          try
          {
            if (mitgliedskontoTree == null)
            {
              // Eingabe-Feld existiert nicht. Also abmelden
              Application.getMessagingFactory().unRegisterMessageConsumer(
                  MitgliedskontoMessageConsumer.this);
              return;
            }

            MitgliedskontoMessage msg = (MitgliedskontoMessage) message;
            Mitglied mitglied = (Mitglied) msg.getObject();
            mitgliedskontoTree.setRootObject(new MitgliedskontoNode(mitglied));
          }
          catch (Exception e)
          {
            // Wenn hier ein Fehler auftrat, deregistrieren wir uns wieder
            Logger.error("unable to refresh saldo", e);
            Application.getMessagingFactory().unRegisterMessageConsumer(
                MitgliedskontoMessageConsumer.this);
          }
        }

      });
    }
  }

}
