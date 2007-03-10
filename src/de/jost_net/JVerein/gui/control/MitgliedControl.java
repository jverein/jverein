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
 * Revision 1.6  2007/03/10 13:41:34  jost
 * Vermerke eingefÃ¼hrt.
 *
 * Revision 1.5  2007/02/23 20:26:38  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.4  2006/12/20 20:25:44  jost
 * Patch von Ullrich Schäfer, der die Primitive vs. Object Problematik adressiert.
 *
 * Revision 1.3  2006/10/29 07:48:29  jost
 * Neu: Mitgliederstatistik
 *
 * Revision 1.2  2006/10/20 07:36:14  jost
 * Fehlermeldung ausgeben, wenn keine Beitragsgruppe ausgewählt wurde.
 *
 * Revision 1.1  2006/09/20 15:38:30  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.kapott.hbci.manager.HBCIUtils;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.MitgliedDetailAction;
import de.jost_net.JVerein.gui.action.ZusatzabbuchungAction;
import de.jost_net.JVerein.gui.input.ZahlungswegInput;
import de.jost_net.JVerein.io.MitgliedAuswertungCSV;
import de.jost_net.JVerein.io.MitgliedAuswertungPDF;
import de.jost_net.JVerein.io.MitgliederStatistik;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Zusatzabbuchung;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextAreaInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

public class MitgliedControl extends AbstractControl
{
  private Input anrede;

  private Input titel;

  private Input name;

  private Input vorname;

  private Input strasse;

  private Input plz;

  private Input ort;

  private DateInput geburtsdatum = null;

  private SelectInput geschlecht;

  private ZahlungswegInput zahlungsweg;

  private Input blz;

  private Input konto;

  private Input kontoinhaber;

  private Input telefonprivat;

  private Input telefondienstlich;

  private Input email;

  private DateInput eintritt = null;

  private SelectInput beitragsgruppe;

  private DateInput austritt = null;

  private DateInput kuendigung = null;

  // Elemente für die Auswertung

  private DateInput geburtsdatumvon = null;

  private DateInput geburtsdatumbis = null;

  private DateInput eintrittvon = null;

  private DateInput eintrittbis;

  private DateInput austrittvon;

  private DateInput austrittbis;

  private TextAreaInput vermerk1;

  private TextAreaInput vermerk2;

  private SelectInput ausgabe;

  private SelectInput sortierung;

  private Mitglied mitglied;

  // Liste aller Zusatzabbuchungen
  private TablePart zusatzabbuchungenList;

  public MitgliedControl(AbstractView view)
  {
    super(view);
  }

  private Mitglied getMitglied()
  {
    if (mitglied != null)
    {
      return mitglied;
    }
    mitglied = (Mitglied) getCurrentObject();
    return mitglied;
  }

  public Input getAnrede() throws RemoteException
  {
    if (anrede != null)
    {
      return anrede;
    }
    anrede = new TextInput(getMitglied().getAnrede(), 10);
    return anrede;
  }

  public Input getTitel() throws RemoteException
  {
    if (titel != null)
    {
      return titel;
    }
    titel = new TextInput(getMitglied().getTitel(), 15);
    return titel;
  }

  public Input getName() throws RemoteException
  {
    if (name != null)
    {
      return name;
    }
    name = new TextInput(getMitglied().getName(), 40);
    return name;
  }

  public Input getVorname() throws RemoteException
  {
    if (vorname != null)
    {
      return vorname;
    }
    vorname = new TextInput(getMitglied().getVorname(), 40);
    return vorname;
  }

  public Input getStrasse() throws RemoteException
  {
    if (strasse != null)
    {
      return strasse;
    }
    strasse = new TextInput(getMitglied().getStrasse(), 40);
    return strasse;
  }

  public Input getPlz() throws RemoteException
  {
    if (plz != null)
    {
      return plz;
    }
    plz = new TextInput(getMitglied().getPlz(), 5);
    return plz;
  }

  public Input getOrt() throws RemoteException
  {
    if (ort != null)
    {
      return ort;
    }
    ort = new TextInput(getMitglied().getOrt(), 40);
    return ort;
  }

  public DateInput getGeburtsdatum() throws RemoteException
  {
    if (geburtsdatum != null)
    {
      return geburtsdatum;
    }
    Date d = getMitglied().getGeburtsdatum();
    this.geburtsdatum = new DateInput(d, Einstellungen.DATEFORMAT);
    this.geburtsdatum.setTitle("Geburtsdatum");
    this.geburtsdatum.setText("Bitte Geburtsdatum wählen");
    this.geburtsdatum.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) geburtsdatum.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return geburtsdatum;
  }

  public SelectInput getGeschlecht() throws RemoteException
  {
    if (geschlecht != null)
    {
      return geschlecht;
    }
    geschlecht = new SelectInput(new String[] { "m", "w" }, getMitglied()
        .getGeschlecht());
    geschlecht.setPleaseChoose("Bitte auswählen");
    return geschlecht;
  }

  public ZahlungswegInput getZahlungsweg() throws RemoteException
  {
    if (zahlungsweg != null)
    {
      return zahlungsweg;
    }
    zahlungsweg = new ZahlungswegInput(getMitglied().getZahlungsweg()
        .intValue());
    return zahlungsweg;
  }

  public Input getBlz() throws RemoteException
  {
    if (blz != null)
    {
      return blz;
    }
    blz = new TextInput(getMitglied().getBlz(), 8);
    BLZListener l = new BLZListener();
    blz.addListener(l);
    l.handleEvent(null); // Einmal initial ausfuehren
    return blz;
  }

  public Input getKonto() throws RemoteException
  {
    if (konto != null)
    {
      return konto;
    }
    konto = new TextInput(getMitglied().getKonto(), 10);
    return konto;
  }

  public Input getKontoinhaber() throws RemoteException
  {
    if (kontoinhaber != null)
    {
      return kontoinhaber;
    }
    kontoinhaber = new TextInput(getMitglied().getKontoinhaber(), 27);
    return kontoinhaber;
  }

  public Input getTelefonprivat() throws RemoteException
  {
    if (telefonprivat != null)
    {
      return telefonprivat;
    }
    telefonprivat = new TextInput(getMitglied().getTelefonprivat(), 15);
    return telefonprivat;
  }

  public Input getTelefondienstlich() throws RemoteException
  {
    if (telefondienstlich != null)
    {
      return telefondienstlich;
    }
    telefondienstlich = new TextInput(getMitglied().getTelefondienstlich(), 15);
    return telefondienstlich;
  }

  public Input getEmail() throws RemoteException
  {
    if (email != null)
    {
      return email;
    }
    email = new TextInput(getMitglied().getEmail(), 50);
    return email;
  }

  public DateInput getEintritt() throws RemoteException
  {
    if (eintritt != null)
    {
      return eintritt;
    }

    Date d = getMitglied().getEintritt();

    this.eintritt = new DateInput(d, Einstellungen.DATEFORMAT);
    this.eintritt.setTitle("Eintrittsdatum");
    this.eintritt.setText("Bitte Eintrittsdatum wählen");
    this.eintritt.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) eintritt.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return eintritt;
  }

  public Input getBeitragsgruppe() throws RemoteException
  {
    if (beitragsgruppe != null)
    {
      return beitragsgruppe;
    }
    beitragsgruppe = new SelectInput(Einstellungen.getDBService().createList(
        Beitragsgruppe.class), getMitglied().getBeitragsgruppe());
    beitragsgruppe.setValue(getMitglied().getBeitragsgruppe());
    beitragsgruppe.setAttribute("bezeichnung");
    beitragsgruppe.setPleaseChoose("Bitte auswählen");
    return beitragsgruppe;
  }

  public DateInput getAustritt() throws RemoteException
  {
    if (austritt != null)
    {
      return austritt;
    }
    Date d = getMitglied().getAustritt();

    this.austritt = new DateInput(d, Einstellungen.DATEFORMAT);
    this.austritt.setTitle("Austrittsdatum");
    this.austritt.setText("Bitte Austrittsdatum wählen");
    this.austritt.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) austritt.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return austritt;
  }

  public DateInput getKuendigung() throws RemoteException
  {
    if (kuendigung != null)
    {
      return kuendigung;
    }
    Date d = getMitglied().getKuendigung();

    this.kuendigung = new DateInput(d, Einstellungen.DATEFORMAT);
    this.kuendigung.setTitle("Kündigungsdatum");
    this.kuendigung.setText("Bitte Kündigungsdatum wählen");
    this.kuendigung.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) kuendigung.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return kuendigung;
  }

  public TextAreaInput getVermerk1() throws RemoteException
  {
    if (vermerk1 != null)
    {
      return vermerk1;
    }
    vermerk1 = new TextAreaInput(getMitglied().getVermerk1(), 255);
    return vermerk1;
  }

  public TextAreaInput getVermerk2() throws RemoteException
  {
    if (vermerk2 != null)
    {
      return vermerk2;
    }
    vermerk2 = new TextAreaInput(getMitglied().getVermerk2(), 255);
    return vermerk2;
  }

  public Part getZusatzabbuchungenTable() throws RemoteException
  {
    if (zusatzabbuchungenList != null)
    {
      return zusatzabbuchungenList;
    }
    DBService service = Einstellungen.getDBService();
    DBIterator zusatzabbuchungen = service.createList(Zusatzabbuchung.class);
    zusatzabbuchungen.addFilter("mitglied = " + getMitglied().getID());
    zusatzabbuchungenList = new TablePart(zusatzabbuchungen,
        new ZusatzabbuchungAction(getMitglied()));
    zusatzabbuchungenList.setRememberColWidths(true);
    zusatzabbuchungenList.setRememberOrder(true);

    zusatzabbuchungenList.addColumn("Fälligkeit", "faelligkeit",
        new DateFormatter(Einstellungen.DATEFORMAT));
    zusatzabbuchungenList.addColumn("Buchungstext", "buchungstext");
    zusatzabbuchungenList.addColumn("Betrag", "betrag", new CurrencyFormatter(
        "", Einstellungen.DECIMALFORMAT));
    zusatzabbuchungenList.addColumn("Ausführung", "ausfuehrung",
        new DateFormatter(Einstellungen.DATEFORMAT));

    return zusatzabbuchungenList;
  }

  public DateInput getGeburtsdatumvon() throws RemoteException
  {
    if (geburtsdatumvon != null)
    {
      return geburtsdatumvon;
    }
    Date d = null;

    this.geburtsdatumvon = new DateInput(d, Einstellungen.DATEFORMAT);
    this.geburtsdatumvon.setTitle("Geburtsdatum");
    this.geburtsdatumvon.setText("Beginn des Geburtszeitraumes");
    this.geburtsdatumvon.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) geburtsdatumvon.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return geburtsdatumvon;
  }

  public DateInput getGeburtsdatumbis() throws RemoteException
  {
    if (geburtsdatumbis != null)
    {
      return geburtsdatumbis;
    }
    Date d = null;

    this.geburtsdatumbis = new DateInput(d, Einstellungen.DATEFORMAT);
    this.geburtsdatumbis.setTitle("Geburtsdatum");
    this.geburtsdatumbis.setText("Ende des Geburtszeitraumes");
    this.geburtsdatumbis.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) geburtsdatumbis.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return geburtsdatumbis;
  }

  public DateInput getEintrittvon() throws RemoteException
  {
    if (eintrittvon != null)
    {
      return eintrittvon;
    }
    Date d = null;

    this.eintrittvon = new DateInput(d, Einstellungen.DATEFORMAT);
    this.eintrittvon.setTitle("Eintrittsdatum");
    this.eintrittvon.setText("Beginn des Eintrittszeitraumes");
    this.eintrittvon.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) eintrittvon.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return eintrittvon;
  }

  public DateInput getEintrittbis() throws RemoteException
  {
    if (eintrittbis != null)
    {
      return eintrittbis;
    }
    Date d = null;

    this.eintrittbis = new DateInput(d, Einstellungen.DATEFORMAT);
    this.eintrittbis.setTitle("Eintrittsdatum");
    this.eintrittbis.setText("Ende des Eintrittszeitraumes");
    this.eintrittbis.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) eintrittbis.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return eintrittbis;
  }

  public DateInput getAustrittvon() throws RemoteException
  {
    if (austrittvon != null)
    {
      return austrittvon;
    }
    Date d = null;

    this.austrittvon = new DateInput(d, Einstellungen.DATEFORMAT);
    this.austrittvon.setTitle("Austrittsdatum");
    this.austrittvon.setText("Beginn des Austrittszeitraumes");
    this.austrittvon.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) austrittvon.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return austrittvon;
  }

  public DateInput getAustrittbis() throws RemoteException
  {
    if (austrittbis != null)
    {
      return austrittbis;
    }
    Date d = null;

    this.austrittbis = new DateInput(d, Einstellungen.DATEFORMAT);
    this.austrittbis.setTitle("Austrittsdatum");
    this.austrittbis.setText("Ende des Austrittszeitraumes");
    this.austrittbis.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        Date date = (Date) austrittbis.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return austrittbis;
  }

  public Input getAusgabe() throws RemoteException
  {
    if (ausgabe != null)
    {
      return ausgabe;
    }
    String[] ausg = { "PDF", "CSV" };
    ausgabe = new SelectInput(ausg, "PDF");
    return ausgabe;
  }

  public Input getSortierung() throws RemoteException
  {
    if (sortierung != null)
    {
      return sortierung;
    }
    String[] sort = { "Name, Vorname", "Eintrittsdatum", "Geburtsdatum" };
    sortierung = new SelectInput(sort, "Name, Vorname");
    return sortierung;
  }

  public Button getStartAuswertungButton()
  {
    Button b = new Button("Start", new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        starteAuswertung();
      }
    }, null, true); // "true" defines this button as the default button
    return b;
  }

  public Button getStartStatistikButton()
  {
    Button b = new Button("Start", new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        starteStatistik();
      }
    }, null, true); // "true" defines this button as the default button
    return b;
  }

  public Button getZusatzabbuchungNeu()
  {
    return new Button("Neu", new ZusatzabbuchungAction(getMitglied()));
  }

  public Part getMitgliedTable(TablePart part, String anfangsbuchstabe)
      throws RemoteException
  {
    if (part != null)
    {
      return part;
    }
    DBService service = Einstellungen.getDBService();
    DBIterator mitglieder = service.createList(Mitglied.class);
    mitglieder.addFilter("name like '" + anfangsbuchstabe + "%' OR "
        + "name like '" + anfangsbuchstabe.toLowerCase() + "%'");
    part = new TablePart(mitglieder, new MitgliedDetailAction());

    part.addColumn("Name", "name");
    part.addColumn("Vorname", "vorname");
    part.addColumn("Strasse", "strasse");
    part.addColumn("Ort", "ort");
    part.addColumn("Telefon", "telefonprivat");
    part.addColumn("Eintritt", "eintritt", new DateFormatter(
        Einstellungen.DATEFORMAT));
    part.addColumn("Austritt", "austritt", new DateFormatter(
        Einstellungen.DATEFORMAT));
    return part;
  }

  public void handleStore()
  {
    try
    {
      Mitglied m = getMitglied();
      m.setAustritt((Date) getAustritt().getValue());
      m.setAnrede((String) getAnrede().getValue());
      GenericObject o = (GenericObject) getBeitragsgruppe().getValue();
      try
      {
        m.setBeitragsgruppe(new Integer(o.getID()));
      }
      catch (NullPointerException e)
      {
        throw new ApplicationException("Beitragsgruppe fehlt");
      }
      m.setZahlungsweg((Integer) getZahlungsweg().getValue());
      m.setBlz((String) getBlz().getValue());
      m.setEintritt((Date) getEintritt().getValue());
      m.setEmail((String) getEmail().getValue());
      m.setGeburtsdatum((Date) getGeburtsdatum().getValue());
      m.setGeschlecht((String) getGeschlecht().getValue());
      m.setKonto((String) getKonto().getValue());
      m.setKontoinhaber((String) getKontoinhaber().getValue());
      m.setKuendigung((Date) getKuendigung().getValue());
      m.setName((String) getName().getValue());
      m.setOrt((String) getOrt().getValue());
      m.setPlz((String) getPlz().getValue());
      m.setStrasse((String) getStrasse().getValue());
      m.setTelefondienstlich((String) getTelefondienstlich().getValue());
      m.setTelefonprivat((String) getTelefonprivat().getValue());
      m.setTitel((String) getTitel().getValue());
      m.setVermerk1((String) getVermerk1().getValue());
      m.setVermerk2((String) getVermerk2().getValue());
      m.setVorname((String) getVorname().getValue());
      if (m.getID() == null)
      {
        m.setEingabedatum();
      }
      m.store();
      GUI.getStatusBar().setSuccessText("Mitglied gespeichert");
    }
    catch (ApplicationException e)
    {
      GUI.getView().setErrorText(e.getMessage());
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei Speichern des Mitgliedes";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  private void starteAuswertung()
  {
    // Alle Mitglieder lesen
    DBIterator list;
    try
    {
      String subtitle = "";
      list = Einstellungen.getDBService().createList(Mitglied.class);
      if (geburtsdatumvon.getValue() != null)
      {
        Date d = (Date) geburtsdatumvon.getValue();
        subtitle += "Geburtsdatum von " + Einstellungen.DATEFORMAT.format(d)
            + "  ";
        list.addFilter("geburtsdatum >= ?", new Object[] { new java.sql.Date(d
            .getTime()) });
      }
      if (geburtsdatumbis.getValue() != null)
      {
        Date d = (Date) geburtsdatumbis.getValue();
        subtitle += "Geburtsdatum bis " + Einstellungen.DATEFORMAT.format(d)
            + "  ";
        list.addFilter("geburtsdatum <= ?", new Object[] { new java.sql.Date(d
            .getTime()) });
      }
      if (eintrittvon.getValue() != null)
      {
        Date d = (Date) eintrittvon.getValue();
        subtitle += "Eintritt von " + Einstellungen.DATEFORMAT.format(d) + "  ";
        list.addFilter("eintritt >= ?", new Object[] { new java.sql.Date(d
            .getTime()) });
      }
      if (eintrittbis.getValue() != null)
      {
        Date d = (Date) eintrittbis.getValue();
        subtitle += "Eintritt bis " + Einstellungen.DATEFORMAT.format(d) + "  ";
        list.addFilter("eintritt <= ?", new Object[] { new java.sql.Date(d
            .getTime()) });
      }
      if (austrittvon.getValue() != null)
      {
        Date d = (Date) austrittvon.getValue();
        subtitle += "Austritt von " + Einstellungen.DATEFORMAT.format(d) + "  ";
        list.addFilter("austritt >= ?", new Object[] { new java.sql.Date(d
            .getTime()) });
      }
      if (austrittbis.getValue() != null)
      {
        Date d = (Date) austrittbis.getValue();
        subtitle += "Austritt bis " + Einstellungen.DATEFORMAT.format(d) + "  ";
        list.addFilter("austritt <= ?", new Object[] { new java.sql.Date(d
            .getTime()) });
      }
      if (austrittvon.getValue() == null && austrittbis.getValue() == null)
      {
        subtitle += "ohne Ausgetretene  ";
        list.addFilter("austritt is null");
      }
      if (beitragsgruppe.getValue() != null)
      {
        Beitragsgruppe bg = (Beitragsgruppe) beitragsgruppe.getValue();
        subtitle += "nur Beitragsgruppe " + bg.getBezeichnung();
        list.addFilter("beitragsgruppe = ?", new Object[] { new Integer(bg
            .getID()) });
      }

      String sort = (String) sortierung.getValue();
      if (sort.equals("Name, Vorname"))
      {
        list.setOrder("ORDER BY name, vorname");
      }
      else if (sort.equals("Eintrittsdatum"))
      {
        list.setOrder("ORDER BY eintritt");
      }
      else if (sort.equals("Geburtsdatum"))
      {
        list.setOrder("ORDER BY geburtsdatum");
      }

      FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
      fd.setText("Ausgabedatei wählen.");

      Settings settings = new Settings(this.getClass());

      String path = settings.getString("lastdir", System
          .getProperty("user.home"));
      if (path != null && path.length() > 0)
        fd.setFilterPath(path);

      final String s = fd.open();

      if (s == null || s.length() == 0)
      {
        // close();
        return;
      }

      final File file = new File(s);

      String ausg = (String) ausgabe.getValue();
      if (ausg.equals("PDF"))
      {
        auswertungMitgliedPDF(list, file, subtitle);
      }
      if (ausg.equals("CSV"))
      {
        auswertungMitgliedCSV(list, file);
      }
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    }
  }

  private void starteStatistik()
  {
    FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
    fd.setText("Ausgabedatei wählen.");

    Settings settings = new Settings(this.getClass());

    String path = settings
        .getString("lastdir", System.getProperty("user.home"));
    if (path != null && path.length() > 0)
      fd.setFilterPath(path);

    final String s = fd.open();

    if (s == null || s.length() == 0)
    {
      // close();
      return;
    }

    final File file = new File(s);

    try
    {
      new MitgliederStatistik(file);
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    }
    catch (ApplicationException e)
    {
      e.printStackTrace();
    }
  }

  private void auswertungMitgliedPDF(final DBIterator list, final File file,
      final String subtitle)
  {
    BackgroundTask t = new BackgroundTask()
    {
      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        try
        {
          new MitgliedAuswertungPDF(list, file, monitor, subtitle);
          monitor.setPercentComplete(100);
          monitor.setStatus(ProgressMonitor.STATUS_DONE);
          GUI.getStatusBar().setSuccessText("Auswertung gestartet");
          GUI.getCurrentView().reload();
        }
        catch (ApplicationException ae)
        {
          monitor.setStatusText(ae.getMessage());
          monitor.setStatus(ProgressMonitor.STATUS_ERROR);
          GUI.getStatusBar().setErrorText(ae.getMessage());
          throw ae;
        }
        catch (RemoteException re)
        {
          monitor.setStatusText(re.getMessage());
          monitor.setStatus(ProgressMonitor.STATUS_ERROR);
          GUI.getStatusBar().setErrorText(re.getMessage());
          throw new ApplicationException(re);
        }
      }

      public void interrupt()
      {
      }

      public boolean isInterrupted()
      {
        return false;
      }
    };
    Application.getController().start(t);
  }

  private void auswertungMitgliedCSV(final DBIterator list, final File file)
  {
    BackgroundTask t = new BackgroundTask()
    {
      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        try
        {
          new MitgliedAuswertungCSV(list, file, monitor);
          monitor.setPercentComplete(100);
          monitor.setStatus(ProgressMonitor.STATUS_DONE);
          GUI.getStatusBar().setSuccessText("Auswertung gestartet");
          GUI.getCurrentView().reload();
        }
        catch (ApplicationException ae)
        {
          monitor.setStatusText(ae.getMessage());
          monitor.setStatus(ProgressMonitor.STATUS_ERROR);
          GUI.getStatusBar().setErrorText(ae.getMessage());
          throw ae;
        }
        catch (RemoteException re)
        {
          monitor.setStatusText(re.getMessage());
          monitor.setStatus(ProgressMonitor.STATUS_ERROR);
          GUI.getStatusBar().setErrorText(re.getMessage());
          throw new ApplicationException(re);
        }
      }

      public void interrupt()
      {
      }

      public boolean isInterrupted()
      {
        return false;
      }
    };
    Application.getController().start(t);
  }

  /**
   * Sucht das Geldinstitut zur eingegebenen BLZ und zeigt es als Kommentar
   * hinter dem BLZ-Feld an.
   */
  private class BLZListener implements Listener
  {
    public void handleEvent(Event event)
    {
      try
      {
        String name = HBCIUtils.getNameForBLZ((String) getBlz().getValue());
        getBlz().setComment(name);
      }
      catch (RemoteException e)
      {
        Logger.error("error while updating blz comment", e);
      }
    }
  }
}
