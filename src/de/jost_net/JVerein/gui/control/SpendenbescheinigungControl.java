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
 * Revision 1.8  2009/01/26 18:47:54  jost
 * Neu: Ersatz Aufwendungen
 *
 * Revision 1.7  2008/12/13 16:22:41  jost
 * Bugfix Tagesdatum
 *
 * Revision 1.6  2008/11/29 13:08:17  jost
 * Refactoring: Code-Optimierung
 *
 * Revision 1.5  2008/11/16 16:57:20  jost
 * Speicherung der Einstellung von Property-Datei in die Datenbank verschoben.
 *
 * Revision 1.4  2008/09/16 18:50:54  jost
 * Refactoring Formularaufbereitung
 *
 * Revision 1.3  2008/07/23 19:40:30  jost
 * Bugfix ..PDF
 *
 * Revision 1.2  2008/07/19 19:24:44  jost
 * Korrektes KontextmenÃ¼
 *
 * Revision 1.1  2008/07/18 20:09:46  jost
 * Neu: Spendenbescheinigung
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;

import jonelo.NumericalChameleon.SpokenNumbers.GermanNumber;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.SpendenbescheinigungAction;
import de.jost_net.JVerein.gui.input.FormularInput;
import de.jost_net.JVerein.gui.menu.SpendenbescheinigungMenu;
import de.jost_net.JVerein.io.FormularAufbereitung;
import de.jost_net.JVerein.keys.Formularart;
import de.jost_net.JVerein.rmi.Formular;
import de.jost_net.JVerein.rmi.Spendenbescheinigung;
import de.jost_net.JVerein.util.Dateiname;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.DecimalInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class SpendenbescheinigungControl extends AbstractControl
{
  private de.willuhn.jameica.system.Settings settings;

  private TablePart spbList;

  private TextInput zeile1;

  private TextInput zeile2;

  private TextInput zeile3;

  private TextInput zeile4;

  private TextInput zeile5;

  private TextInput zeile6;

  private TextInput zeile7;

  private DateInput spendedatum;

  private DateInput bescheinigungsdatum;

  private DecimalInput betrag;

  private FormularInput formular;

  private CheckboxInput ersatzaufwendungen;

  private Spendenbescheinigung spendenbescheinigung;

  public SpendenbescheinigungControl(AbstractView view)
  {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public Spendenbescheinigung getSpendenbescheinigung()
  {
    if (spendenbescheinigung != null)
    {
      return spendenbescheinigung;
    }
    spendenbescheinigung = (Spendenbescheinigung) getCurrentObject();
    return spendenbescheinigung;
  }

  public TextInput getZeile1() throws RemoteException
  {
    if (zeile1 != null)
    {
      return zeile1;
    }
    zeile1 = new TextInput((String) getSpendenbescheinigung().getZeile1(), 40);
    return zeile1;
  }

  public TextInput getZeile2() throws RemoteException
  {
    if (zeile2 != null)
    {
      return zeile2;
    }
    zeile2 = new TextInput((String) getSpendenbescheinigung().getZeile2(), 40);
    return zeile2;
  }

  public TextInput getZeile3() throws RemoteException
  {
    if (zeile3 != null)
    {
      return zeile3;
    }
    zeile3 = new TextInput((String) getSpendenbescheinigung().getZeile3(), 40);
    return zeile3;
  }

  public TextInput getZeile4() throws RemoteException
  {
    if (zeile4 != null)
    {
      return zeile4;
    }
    zeile4 = new TextInput((String) getSpendenbescheinigung().getZeile4(), 40);
    return zeile4;
  }

  public TextInput getZeile5() throws RemoteException
  {
    if (zeile5 != null)
    {
      return zeile5;
    }
    zeile5 = new TextInput((String) getSpendenbescheinigung().getZeile5(), 40);
    return zeile5;
  }

  public TextInput getZeile6() throws RemoteException
  {
    if (zeile6 != null)
    {
      return zeile6;
    }
    zeile6 = new TextInput((String) getSpendenbescheinigung().getZeile6(), 40);
    return zeile6;
  }

  public TextInput getZeile7() throws RemoteException
  {
    if (zeile7 != null)
    {
      return zeile7;
    }
    zeile7 = new TextInput((String) getSpendenbescheinigung().getZeile7(), 40);
    return zeile7;
  }

  public DateInput getSpendedatum() throws RemoteException
  {
    if (spendedatum != null)
    {
      return spendedatum;
    }
    spendedatum = new DateInput((Date) getSpendenbescheinigung()
        .getSpendedatum());
    return spendedatum;
  }

  public DateInput getBescheinigungsdatum() throws RemoteException
  {
    if (bescheinigungsdatum != null)
    {
      return bescheinigungsdatum;
    }
    bescheinigungsdatum = new DateInput((Date) getSpendenbescheinigung()
        .getBescheinigungsdatum());
    return bescheinigungsdatum;
  }

  public DecimalInput getBetrag() throws RemoteException
  {
    if (betrag != null)
    {
      return betrag;
    }
    betrag = new DecimalInput((Double) getSpendenbescheinigung().getBetrag(),
        Einstellungen.DECIMALFORMAT);
    return betrag;
  }

  public SelectInput getFormular() throws RemoteException
  {
    if (formular != null)
    {
      return formular;
    }
    String def = null;
    if (getSpendenbescheinigung().getFormular() != null)
    {
      def = getSpendenbescheinigung().getFormular().getID();
    }
    formular = new FormularInput(Formularart.SPENDENBESCHEINIGUNG, def);
    return formular;
  }

  public CheckboxInput getErsatzAufwendungen() throws RemoteException
  {
    if (ersatzaufwendungen != null)
    {
      return ersatzaufwendungen;
    }
    ersatzaufwendungen = new CheckboxInput(getSpendenbescheinigung()
        .getErsatzAufwendungen());
    return ersatzaufwendungen;
  }

  /**
   * This method stores the project using the current values.
   */
  public void handleStore()
  {
    try
    {
      Spendenbescheinigung spb = getSpendenbescheinigung();
      spb.setZeile1((String) getZeile1().getValue());
      spb.setZeile2((String) getZeile2().getValue());
      spb.setZeile3((String) getZeile3().getValue());
      spb.setZeile4((String) getZeile4().getValue());
      spb.setZeile5((String) getZeile5().getValue());
      spb.setZeile6((String) getZeile6().getValue());
      spb.setZeile7((String) getZeile7().getValue());
      spb.setSpendedatum((Date) getSpendedatum().getValue());
      spb.setBescheinigungsdatum((Date) getBescheinigungsdatum().getValue());
      spb.setBetrag((Double) getBetrag().getValue());
      spb.setErsatzAufwendungen((Boolean) getErsatzAufwendungen().getValue());
      spb.setFormular((Formular) getFormular().getValue());
      spb.store();

      GUI.getStatusBar().setSuccessText("Spendenbescheinigung gespeichert");
    }
    catch (ApplicationException e)
    {
      GUI.getView().setErrorText(e.getMessage());
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei Speichern der Spendenbescheinigung";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  public Button getPDFButton()
  {
    Button b = new Button("PDF", new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        try
        {
          generiereSpendenbescheinigung();
        }
        catch (RemoteException e)
        {
          Logger.error(e.getMessage());
          throw new ApplicationException(
              "Fehler bei der Aufbereitung der Spendenbescheinigung");
        }
        catch (IOException e)
        {
          Logger.error(e.getMessage());
          throw new ApplicationException(
              "Fehler bei der Aufbereitung der Spendenbescheinigung");
        }
      }
    }, null, true); // "true" defines this button as the default button
    return b;
  }

  private void generiereSpendenbescheinigung() throws IOException
  {
    FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
    fd.setText("Ausgabedatei wählen.");
    String path = settings
        .getString("lastdir", System.getProperty("user.home"));
    if (path != null && path.length() > 0)
    {
      fd.setFilterPath(path);
    }
    fd.setFileName(new Dateiname("spendenbescheinigung", "", Einstellungen
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
    settings.setAttribute("lastdir", file.getParent());
    Formular fo = (Formular) Einstellungen.getDBService().createObject(
        Formular.class, getSpendenbescheinigung().getFormular().getID());
    HashMap<String, Object> map = new HashMap<String, Object>();
    String empfaenger = (String) getZeile1().getValue() + "\n"
        + (String) getZeile2().getValue() + "\n"
        + (String) getZeile3().getValue() + "\n"
        + (String) getZeile4().getValue() + "\n"
        + (String) getZeile5().getValue() + "\n"
        + (String) getZeile6().getValue() + "\n"
        + (String) getZeile7().getValue() + "\n";
    map.put("Empfänger", empfaenger);
    String betrag = Einstellungen.DECIMALFORMAT.format((Double) getBetrag()
        .getValue());
    map.put("Betrag", "*" + betrag + "* Euro");
    Double dWert = (Double) getBetrag().getValue();
    try
    {
      String betraginworten = GermanNumber.toString(dWert.longValue());
      map.put("Betrag in Worten", "*" + betraginworten + "*");
    }
    catch (Exception e)
    {
      Logger.error("Fehler", e);
      throw new RemoteException(
          "Fehler bei der Aufbereitung des Betrages in Worten");
    }
    Date tmp = (Date) getBescheinigungsdatum().getValue();
    String bescheinigungsdatum = Einstellungen.DATEFORMAT.format(tmp);
    map.put("Bescheinigungsdatum", bescheinigungsdatum);
    tmp = (Date) getSpendedatum().getValue();
    String spendedatum = Einstellungen.DATEFORMAT.format(tmp);
    map.put("Spendedatum", spendedatum);
    String tagesdatum = Einstellungen.DATEFORMAT.format(new Date());
    map.put("Tagesdatum", tagesdatum);
    map.put("ErsatzAufwendungen",
        ((Boolean) ersatzaufwendungen.getValue() ? "X" : ""));
    FormularAufbereitung fa = new FormularAufbereitung(file);
    fa.writeForm(fo, map);
    fa.showFormular();

  }

  public Part getSpendenbescheinigungList() throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator spendenbescheinigungen = service
        .createList(Spendenbescheinigung.class);
    spendenbescheinigungen.setOrder("ORDER BY bescheinigungsdatum desc");

    spbList = new TablePart(spendenbescheinigungen,
        new SpendenbescheinigungAction());
    spbList.addColumn("Bescheinigungsdatum", "bescheinigungsdatum",
        new DateFormatter(Einstellungen.DATEFORMAT));
    spbList.addColumn("Spendedatum", "spendedatum", new DateFormatter(
        Einstellungen.DATEFORMAT));
    spbList.addColumn("Betrag", "betrag", new CurrencyFormatter("",
        Einstellungen.DECIMALFORMAT));
    spbList.addColumn("Zeile 1", "zeile1");
    spbList.addColumn("Zeile 2", "zeile2");
    spbList.addColumn("Zeile 3", "zeile3");
    spbList.addColumn("Zeile 4", "zeile4");
    spbList.addColumn("Zeile 5", "zeile5");
    spbList.addColumn("Zeile 6", "zeile6");
    spbList.addColumn("Zeile 7", "zeile7");

    spbList.setRememberColWidths(true);
    spbList.setContextMenu(new SpendenbescheinigungMenu());
    spbList.setRememberOrder(true);
    spbList.setSummary(false);
    return spbList;
  }

  public void refreshTable() throws RemoteException
  {
    spbList.removeAll();
    DBIterator spendenbescheinigungen = Einstellungen.getDBService()
        .createList(Spendenbescheinigung.class);
    spendenbescheinigungen.setOrder("ORDER BY bescheinigungsdatum desc");
    while (spendenbescheinigungen.hasNext())
    {
      spbList.addItem((Spendenbescheinigung) spendenbescheinigungen.next());
    }
  }

}
