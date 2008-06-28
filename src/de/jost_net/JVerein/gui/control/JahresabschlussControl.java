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
 * Revision 1.1  2008/05/22 06:47:13  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.menu.JahresabschlussMenu;
import de.jost_net.JVerein.gui.parts.JahressaldoList;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Jahresabschluss;
import de.jost_net.JVerein.util.Geschaeftsjahr;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class JahresabschlussControl extends AbstractControl
{
  private de.willuhn.jameica.system.Settings settings;

  private TablePart jahresabschlussList;

  private Part jahresabschlusssaldoList;

  private DateInput von;

  private DateInput bis;

  private DateInput datum;

  private TextInput name;

  private Jahresabschluss jahresabschluss;

  public JahresabschlussControl(AbstractView view)
  {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public Jahresabschluss getJahresabschluss()
  {
    if (jahresabschluss != null)
    {
      return jahresabschluss;
    }
    jahresabschluss = (Jahresabschluss) getCurrentObject();
    return jahresabschluss;
  }

  public DateInput getVon() throws RemoteException, ParseException
  {
    if (von != null)
    {
      return von;
    }
    von = new DateInput(getJahresabschluss().getVon());
    von.setEnabled(false);
    if (getJahresabschluss().isNewObject())
    {
      von.setValue(computeVonDatum());
    }
    return von;
  }

  private Date computeVonDatum() throws RemoteException, ParseException
  {
    DBIterator it = Einstellungen.getDBService().createList(
        Jahresabschluss.class);
    it.setOrder("ORDER BY bis DESC");
    if (it.hasNext())
    {
      Jahresabschluss ja = (Jahresabschluss) it.next();
      Calendar cal = Calendar.getInstance();
      cal.setTime(ja.getBis());
      cal.add(Calendar.DAY_OF_MONTH, 1);
      return cal.getTime();
    }
    it = Einstellungen.getDBService().createList(Buchung.class);
    it.setOrder("ORDER BY datum");
    if (it.hasNext())
    {
      Buchung b = (Buchung) it.next();
      Geschaeftsjahr gj = new Geschaeftsjahr(b.getDatum());
      return gj.getBeginnGeschaeftsjahr();
    }
    Geschaeftsjahr gj = new Geschaeftsjahr(new Date());
    return gj.getBeginnGeschaeftsjahr();
  }

  public DateInput getBis() throws RemoteException, ParseException
  {
    if (bis != null)
    {
      return bis;
    }
    Geschaeftsjahr gj = new Geschaeftsjahr((Date) von.getValue());
    bis = new DateInput(gj.getEndeGeschaeftsjahr());
    bis.setEnabled(false);
    return bis;
  }

  public DateInput getDatum() throws RemoteException
  {
    if (datum != null)
    {
      return datum;
    }
    datum = new DateInput(new Date());
    datum.setEnabled(false);
    return datum;
  }

  public TextInput getName() throws RemoteException
  {
    if (name != null)
    {
      return name;
    }
    name = new TextInput(getJahresabschluss().getName(), 50);
    return name;
  }

  public Part getJahresabschlussSaldo() throws RemoteException
  {
    if (jahresabschlusssaldoList != null)
    {
      return jahresabschlusssaldoList;
    }
    try
    {
      jahresabschlusssaldoList = new JahressaldoList(null, new Geschaeftsjahr(
          (Date) getVon().getValue())).getSaldoList();
    }
    catch (ApplicationException e)
    {
      throw new RemoteException(e.getMessage());
    }
    catch (ParseException e)
    {
      throw new RemoteException(e.getMessage());
    }
    return jahresabschlusssaldoList;
  }

  /**
   * This method stores the project using the current values.
   */
  public void handleStore()
  {
    try
    {
      Jahresabschluss ja = getJahresabschluss();
      ja.setVon((Date) getVon().getValue());
      ja.setBis((Date) getBis().getValue());
      ja.setDatum((Date) getDatum().getValue());
      ja.setName((String) getName().getValue());
      ja.store();
      GUI.getStatusBar().setSuccessText("Jahresabschluss gespeichert");
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei speichern des Jahresabschlusses";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
    catch (ParseException e)
    {
      String fehler = "Fehler bei speichern des Jahresabschlusses";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
    catch (ApplicationException e)
    {
      GUI.getStatusBar().setErrorText(e.getMessage());
    }
  }

  public Part getJahresabschlussList() throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator jahresabschluesse = service.createList(Jahresabschluss.class);
    jahresabschluesse.setOrder("ORDER BY von desc");

    jahresabschlussList = new TablePart(jahresabschluesse, null);
    jahresabschlussList.addColumn("von", "von", new DateFormatter(
        Einstellungen.DATEFORMAT));
    jahresabschlussList.addColumn("bis", "bis", new DateFormatter(
        Einstellungen.DATEFORMAT));
    jahresabschlussList.addColumn("Datum", "datum", new DateFormatter(
        Einstellungen.DATEFORMAT));
    jahresabschlussList.addColumn("Name", "name");
    jahresabschlussList.setRememberColWidths(true);
    jahresabschlussList.setContextMenu(new JahresabschlussMenu());
    jahresabschlussList.setRememberOrder(true);
    jahresabschlussList.setSummary(false);
    return jahresabschlussList;
  }

  public void refreshTable() throws RemoteException
  {
    jahresabschlussList.removeAll();
    DBIterator jahresabschluesse = Einstellungen.getDBService().createList(
        Jahresabschluss.class);
    jahresabschluesse.setOrder("ORDER BY von desc");
    while (jahresabschluesse.hasNext())
    {
      jahresabschlussList.addItem((Jahresabschluss) jahresabschluesse.next());
    }
  }

}
