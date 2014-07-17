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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.TableItem;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.io.IBANUpdate;
import de.jost_net.JVerein.io.IBankverbindung;
import de.jost_net.JVerein.io.Adressbuch.Adressaufbereitung;
import de.jost_net.JVerein.rmi.Einstellung;
import de.jost_net.JVerein.rmi.Kursteilnehmer;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.OBanToo.SEPA.BIC;
import de.jost_net.OBanToo.SEPA.IBAN;
import de.jost_net.OBanToo.SEPA.IBANCode;
import de.jost_net.OBanToo.SEPA.SEPAException;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.jameica.gui.formatter.Formatter;
import de.willuhn.jameica.gui.formatter.TableFormatter;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.gui.util.Color;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class SEPAKonvertierungControl extends AbstractControl
{

  private Settings settings;

  private ArrayList<IBANUpdate> zeile = new ArrayList<IBANUpdate>();

  private TablePart ibanupdateList;

  private DateInput mandatdatum;

  public SEPAKonvertierungControl(AbstractView view)
  {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public Button getButtonExtExport()
  {
    Button b = new Button("IBANHIN-Datei ausgeben", new Action()
    {
      @Override
      public void handleAction(Object context)
      {
        settings = new Settings(this.getClass());
        settings.setStoreWhenRead(true);

        FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
        String path = settings.getString("lastdir",
            System.getProperty("user.home"));
        if (path != null && path.length() > 0)
        {
          fd.setFilterPath(path);
        }
        fd.setFileName("IBANHIN.csv");
        fd.setText("Datei zur externen SEPA-Konvertierung");
        fd.setFilterExtensions(new String[] { "*.CSV" });
        fd.setFilterNames(new String[] { "Semikolon-separierte Datei" });

        String f = fd.open();
        try
        {
          File expdatei = new File(f);
          settings.setAttribute("lastdir", expdatei.getParent());
          exportiere(expdatei);
          GUI.getStatusBar().setSuccessText("Datei erstellt");
        }
        catch (RemoteException e)
        {
          Logger.error("Fehler", e);
        }
        catch (IOException e)
        {
          Logger.error("Fehler", e);
        }
      }
    });
    return b;
  }

  public Button getButtonMandatdatumSetzen()
  {
    Button b = new Button("Mandatdatum setzen", new Action()
    {
      @Override
      public void handleAction(Object context)
      {
        try
        {
          Date d = (Date) getMandatsdatum().getValue();
          if (d == null)
          {
            GUI.getStatusBar().setErrorText("Datum fehlt!");
          }
          Calendar dminus36 = Calendar.getInstance();
          dminus36.add(Calendar.MONTH, -36);
          if (d.before(dminus36.getTime()))
          {
            GUI.getStatusBar()
                .setErrorText(
                    "Datum liegt mehr als 36 Monate zurück. Das ist nicht zulässig!");
          }
          if (d.after(new Date()))
          {
            GUI.getStatusBar().setErrorText(
                "Datum darf nicht in der Zukunft liegen!");
          }
          YesNoDialog ynd = new YesNoDialog(YesNoDialog.POSITION_CENTER);
          ynd.setText("Wollen Sie bei allen Mitgliedern das Mandatsdatum setzen? Hinweis: Existierende Werte werden überschrieben");
          ynd.setTitle("Sicherheitsabfrage");
          Boolean answer = (Boolean) ynd.open();
          if (!answer)
          {
            GUI.getStatusBar().setErrorText("Abbruch!");
            return;
          }
          DBIterator it = Einstellungen.getDBService().createList(
              Mitglied.class);
          // it.addFilter("mandatdatum is not null");
          it.addFilter("iban is not null");
          while (it.hasNext())
          {
            Mitglied m1 = (Mitglied) it.next();
            Mitglied m2 = (Mitglied) Einstellungen.getDBService().createObject(
                Mitglied.class, m1.getID());
            m2.setMandatDatum((Date) getMandatsdatum().getValue());
            m2.store();
          }
          GUI.getStatusBar().setSuccessText(
              "Mandatsdatum bei allen Mitgliedern gesetzt");
        }
        catch (Exception e)
        {
          Logger.error("Fehler", e);
        }
      }
    });
    return b;
  }

  public DateInput getMandatsdatum()
  {
    if (mandatdatum != null)
    {
      return mandatdatum;
    }
    mandatdatum = new DateInput();
    mandatdatum.setName("Datum des Mandats");
    return mandatdatum;
  }

  public Button getButtonExtImport()
  {
    Button b = new Button("IBANRUECK einlesen", new Action()
    {

      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        settings = new Settings(this.getClass());
        settings.setStoreWhenRead(true);

        FileDialog fd = new FileDialog(GUI.getShell(), SWT.OPEN);
        String path = settings.getString("lastdir",
            System.getProperty("user.home"));
        if (path != null && path.length() > 0)
        {
          fd.setFilterPath(path);
        }

        fd.setText("IBANRUECK-Datei der externen SEPA-Konvertierung");
        fd.setFileName("IBANRUECK");
        String f = fd.open();
        try
        {
          File ibanrueck = new File(f);
          settings.setAttribute("lastdir", ibanrueck.getParent());
          importiere(ibanrueck);
          ibanupdateList.removeAll();
          for (IBANUpdate iu : zeile)
          {
            ibanupdateList.addItem(iu);
          }
        }
        catch (RemoteException e)
        {
          Logger.error("Fehler", e);
        }
        catch (IOException e)
        {
          Logger.error("Fehler", e);
        }
      }
    });
    return b;
  }

  public Button getButtonInterneKonvertierung()
  {
    Button b = new Button("Starten", new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        konvertiere();
        ibanupdateList.removeAll();
        for (IBANUpdate iu : zeile)
        {
          try
          {
            ibanupdateList.addItem(iu);
          }
          catch (RemoteException e)
          {
            Logger.error("Fehler", e);
          }
        }
      }
    });
    return b;
  }

  public void konvertiere()
  {
    zeile = new ArrayList<IBANUpdate>();
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
          IBANUpdate iu = new IBANUpdate(einstellung.getID(),
              einstellung.getName(), einstellung.getBlz(),
              einstellung.getKonto(), null, null, null);
          konvertiere(einstellung, iu);
          Einstellungen.setEinstellung(einstellung);
        }
      }
      // Mitglieder
      it = getMitglieder();
      while (it.hasNext())
      {
        Mitglied m = (Mitglied) it.next();
        Mitglied m2 = (Mitglied) Einstellungen.getDBService().createObject(
            Mitglied.class, m.getID());
        IBANUpdate iu = new IBANUpdate(m2.getID(),
            Adressaufbereitung.getNameVorname(m2), m2.getBlz(), m2.getKonto(),
            null, null, null);
        konvertiere(m2, iu);
      }
    }
    catch (RemoteException e)
    {
      Logger.error("Error", e);
    }
  }

  private void konvertiere(IBankverbindung obj, IBANUpdate iu)
  {
    zeile.add(iu);
    try
    {
      IBAN i = new IBAN(obj.getKonto(), obj.getBlz(), Einstellungen
          .getEinstellung().getDefaultLand());
      obj.setIban(i.getIBAN());
      iu.setIban(i.getIBAN());
      BIC bi = new BIC(obj.getKonto(), obj.getBlz(), Einstellungen
          .getEinstellung().getDefaultLand());
      obj.setBic(bi.getBIC());
      iu.setBic(bi.getBIC());
      obj.store();
      iu.setStatus("00");
    }
    catch (RemoteException e)
    {
      Logger.error("Fehler", e);
    }
    catch (SEPAException e)
    {
      switch (e.getFehler())
      {
        case BLZ_LEER:
        case BLZ_UNGUELTIG:
        case BLZ_UNGUELTIGE_LAENGE:
        {
          iu.setStatus("10");
          break;
        }
        case IBANREGEL_NICHT_IMPLEMENTIERT:
        case UNGUELTIGES_LAND:
        {
          iu.setStatus("50");
          break;
        }
        case KONTO_LEER:
        case KONTO_PRUEFZIFFER_FALSCH:
        case KONTO_UNGUELTIGE_LAENGE:
        case KONTO_PRUEFZIFFERNREGEL_NICHT_IMPLEMENTIERT:
        {
          iu.setStatus("11");
          break;
        }
      }
    }
    catch (ApplicationException e)
    {
      Logger.error("Fehler", e);
    }
  }

  public Part getList() throws RemoteException
  {
    ibanupdateList = new TablePart(getIterator(), null);
    ibanupdateList.addColumn("ID", "id");
    ibanupdateList.addColumn("Name, Vorname", "name");
    ibanupdateList.addColumn("BLZ", "blz");
    ibanupdateList.addColumn(("Konto"), "konto");
    ibanupdateList.addColumn(("IBAN"), "iban");
    ibanupdateList.addColumn(("BIC"), "bic");
    ibanupdateList.addColumn(("Status"), "status", new Formatter()
    {
      @Override
      public String format(Object o)
      {
        IBANCode ic = IBANCode.fromString((String) o);
        return ic.getMessage();
      }
    });
    ibanupdateList.setRememberColWidths(true);
    ibanupdateList.setFormatter(new TableFormatter()
    {
      @Override
      public void format(TableItem item)
      {
        IBANUpdate iu = (IBANUpdate) item.getData();
        if (iu.getStatusError())
        {
          item.setForeground(Color.ERROR.getSWTColor());
        }
      }
    });
    ibanupdateList.setRememberOrder(true);
    ibanupdateList.setSummary(true);
    return ibanupdateList;
  }

  private GenericIterator getIterator() throws RemoteException
  {
    GenericIterator gi = PseudoIterator.fromArray(zeile
        .toArray(new GenericObject[zeile.size()]));
    return gi;
  }

  private void exportiere(File file) throws IOException
  {
    BufferedWriter wr = new BufferedWriter(new PrintWriter(file));
    // Mitglieder
    DBIterator it = getMitglieder();
    while (it.hasNext())
    {
      Mitglied m = (Mitglied) it.next();
      schreibeExportsatz(wr, "M", m.getID(), m.getBlz(), m.getKonto());
    }
    // Einstellungen
    it = Einstellungen.getDBService().createList(Einstellung.class);
    it.addFilter("konto is not null and length(konto) > 0 and blz is not null and length(blz) > 0");
    while (it.hasNext())
    {
      Einstellung e = (Einstellung) it.next();
      schreibeExportsatz(wr, "E", e.getID(), e.getBlz(), e.getKonto());
    }
    wr.close();
  }

  private void schreibeExportsatz(BufferedWriter wr, String bereich, String id,
      String blz, String konto) throws IOException
  {
    wr.write("\"DE\";;");
    wr.write(bereich);
    wr.write(id);
    wr.write(";;;");
    wr.write(blz);
    wr.write(";");
    wr.write(konto);
    wr.write(";;;;;");
    wr.newLine();
  }

  private void importiere(File file) throws IOException, ApplicationException
  {
    zeile = new ArrayList<IBANUpdate>();
    ICsvListReader r = new CsvListReader(new FileReader(file),
        CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
    List<String> line = null;
    while ((line = r.read()) != null)
    {
      if (line.get(2).startsWith("M"))
      {
        Mitglied m = (Mitglied) Einstellungen.getDBService().createObject(
            Mitglied.class, line.get(2).substring(1));
        IBANUpdate iu = new IBANUpdate(m.getID(),
            Adressaufbereitung.getNameVorname(m), m.getBlz(), m.getKonto(),
            line.get(9), line.get(8), line.get(11));
        if (line.get(11).equals("00"))
        {
          m.setIban(line.get(9));
          m.setBic(line.get(8));
          m.store();
        }
        zeile.add(iu);
      }
      if (line.get(2).startsWith("K"))
      {
        Kursteilnehmer k = (Kursteilnehmer) Einstellungen.getDBService()
            .createObject(Kursteilnehmer.class, line.get(2).substring(1));
        IBANUpdate iu = new IBANUpdate(k.getID(),
            Adressaufbereitung.getNameVorname(k), k.getBlz(), k.getKonto(),
            line.get(9), line.get(8), line.get(11));
        if (line.get(11).equals("00"))
        {
          k.setIban(line.get(9));
          k.setBic(line.get(8));
          k.store();
        }
        zeile.add(iu);
      }
      if (line.get(2).startsWith("E"))
      {
        Einstellung e = (Einstellung) Einstellungen.getDBService()
            .createObject(Einstellung.class, line.get(2).substring(1));
        IBANUpdate iu = new IBANUpdate(e.getID(), e.getName(), e.getBlz(),
            e.getKonto(), line.get(9), line.get(8), line.get(11));
        if (line.get(11).equals("00"))
        {
          e.setIban(line.get(9));
          e.setBic(line.get(8));
          e.store();
        }
        zeile.add(iu);
      }
    }
  }

  public static DBIterator getMitglieder() throws RemoteException
  {
    DBIterator it = Einstellungen.getDBService().createList(Mitglied.class);
    it.addFilter("(konto is not null and length(konto) > 0 and blz is not null and length(blz) > 0)");
    it.addFilter(
        "((eintritt is null) or (eintritt is not null and eintritt <=?))",
        new Date());
    it.addFilter("(austritt is null or austritt > ?)", new Date());
    return it;
  }
}
