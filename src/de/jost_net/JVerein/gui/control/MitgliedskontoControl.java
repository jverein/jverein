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
 * Revision 1.1  2010/05/18 20:19:24  jost
 * Vorabversion Mitgliedskonto
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.Messaging.MitgliedskontoMessage;
import de.jost_net.JVerein.gui.formatter.ZahlungswegFormatter;
import de.jost_net.JVerein.gui.menu.MitgliedskontoMenu;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.pseudo.PseudoIterator;
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
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.DecimalInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
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

  private Mitgliedskonto mkto;

  private TablePart mitgliedskontoList;

  private TreePart mitgliedskontoTree;

  private DateInput vondatum = null;

  private DateInput bisdatum = null;

  private TextInput suchname = null;

  private CheckboxInput differenz = null;

  private CheckboxInput offenePosten = null;

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

  public DateInput getVondatum() throws RemoteException
  {
    if (vondatum != null)
    {
      return vondatum;
    }
    Date d = null;
    this.vondatum = new DateInput(d, Einstellungen.DATEFORMAT);
    this.vondatum.setTitle("Anfangsdatum");
    this.vondatum.setText("Bitte Anfangsdatum wählen");
    vondatum.addListener(new FilterListener());
    return vondatum;
  }

  public DateInput getBisdatum() throws RemoteException
  {
    if (bisdatum != null)
    {
      return bisdatum;
    }
    Date d = null;
    this.bisdatum = new DateInput(d, Einstellungen.DATEFORMAT);
    this.bisdatum.setTitle("Endedatum");
    this.bisdatum.setText("Bitte Endedatum wählen");
    bisdatum.addListener(new FilterListener());
    return bisdatum;
  }

  public CheckboxInput getDifferenz() throws RemoteException
  {
    if (differenz != null)
    {
      return differenz;
    }
    differenz = new CheckboxInput(true);
    differenz.setName("Differenz");
    differenz.addListener(new FilterListener());
    return differenz;
  }

  public CheckboxInput getOffenePosten() throws RemoteException
  {
    if (offenePosten != null)
    {
      return offenePosten;
    }
    offenePosten = new CheckboxInput(true);
    offenePosten.setName("offene Posten");
    offenePosten.addListener(new FilterListener());
    return offenePosten;
  }

  public TextInput getSuchName() throws RemoteException
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

  public TablePart getMitgliedskontoList(Action action, ContextMenu menu) throws RemoteException
  {
    this.action = action;
    DBService service = Einstellungen.getDBService();
    Date d1 = null;
    java.sql.Date vd = null;
    java.sql.Date bd = null;
    if (vondatum != null)
    {
      d1 = (Date) vondatum.getValue();
      if (d1 != null)
      {
        vd = new java.sql.Date(d1.getTime());
      }
    }
    if (bisdatum != null)
    {
      d1 = (Date) bisdatum.getValue();
      if (d1 != null)
      {
        bd = new java.sql.Date(d1.getTime());
      }
    }
    String sql = "select mitgliedskonto.*, sum(mitgliedskonto.betrag) sollsumme, "
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
        where += "mitglied.name like ? or mitglied.vorname like ? or zweck1 like ? ";
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
    if (differenz != null && (Boolean) differenz.getValue())
    {
      sql += "having sollsumme <> istsumme or istsumme is null ";
    }
    if (offenePosten != null && (Boolean) offenePosten.getValue())
    {
      sql += "having sollsumme > istsumme or istsumme is null ";
    }
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
            return PseudoIterator.fromArray((GenericObject[]) ergebnis
                .toArray(new GenericObject[ergebnis.size()]));
          }
        });

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
      mitgliedskontoList.addColumn("Zahlungseingang", "istbetrag", new CurrencyFormatter(
          "", Einstellungen.DECIMALFORMAT));
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
        mitgliedskontoList.addItem((Mitgliedskonto) mitgliedskonten.next());
      }
    }
    return mitgliedskontoList;
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
