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
 * Revision 1.4  2010-10-15 09:58:27  jost
 * Code aufgeräumt
 *
 * Revision 1.3  2010-09-19 16:14:31  jost
 * Länge der Kontobezeichnung auf 255  Zeichen verlängert.
 *
 * Revision 1.2  2008/05/26 18:58:39  jost
 * Neu: ErÃ¶ffnungsdatum
 *
 * Revision 1.1  2008/05/22 06:48:32  jost
 * BuchfÃ¼hrung
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.KontoAction;
import de.jost_net.JVerein.gui.menu.KontoMenu;
import de.jost_net.JVerein.rmi.Konto;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.formatter.Formatter;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Column;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class KontoControl extends AbstractControl
{

  private de.willuhn.jameica.system.Settings settings;

  private TablePart kontenList;

  private TextInput nummer;

  private TextInput bezeichnung;

  private DateInput eroeffnung;

  private DateInput aufloesung;

  private TextInput hibiscusid;

  private Konto konto;

  public KontoControl(AbstractView view)
  {
    super(view);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  private Konto getKonto()
  {
    if (konto != null)
    {
      return konto;
    }
    konto = (Konto) getCurrentObject();
    return konto;
  }

  public TextInput getNummer() throws RemoteException
  {
    if (nummer != null)
    {
      return nummer;
    }
    nummer = new TextInput(getKonto().getNummer());
    return nummer;
  }

  public TextInput getBezeichnung() throws RemoteException
  {
    if (bezeichnung != null)
    {
      return bezeichnung;
    }
    bezeichnung = new TextInput(getKonto().getBezeichnung(), 255);
    return bezeichnung;
  }

  public DateInput getEroeffnung() throws RemoteException
  {
    if (eroeffnung != null)
    {
      return eroeffnung;
    }
    eroeffnung = new DateInput(getKonto().getEroeffnung(), new JVDateFormatTTMMJJJJ());
    return eroeffnung;
  }

  public DateInput getAufloesung() throws RemoteException
  {
    if (aufloesung != null)
    {
      return aufloesung;
    }
    aufloesung = new DateInput(getKonto().getAufloesung(), new JVDateFormatTTMMJJJJ());
    return aufloesung;
  }

  public TextInput getHibiscusId() throws RemoteException
  {
    if (hibiscusid != null)
    {
      return hibiscusid;
    }
    String hibid = "-1";
    try
    {
      hibid = getKonto().getHibiscusId().toString();
    }
    catch (NullPointerException e)
    {
      // nichts zu tun.
    }
    hibiscusid = new TextInput(hibid, 10);
    hibiscusid.setEnabled(false);
    return hibiscusid;
  }

  /**
   * This method stores the project using the current values.
   */
  public void handleStore()
  {
    try
    {
      Konto k = getKonto();
      k.setNummer((String) getNummer().getValue());
      k.setBezeichnung((String) getBezeichnung().getValue());
      k.setEroeffnung((Date) getEroeffnung().getValue());
      k.setAufloesung((Date) getAufloesung().getValue());
      k.setHibiscusId(new Integer((String) getHibiscusId().getValue()));
      k.store();
      GUI.getStatusBar().setSuccessText("Konto gespeichert");
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei speichern des Kontos";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
    catch (ApplicationException e)
    {
      String fehler = "Fehler bei speichern des Kontos";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  public Part getKontenList() throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator konten = service.createList(Konto.class);
    konten.setOrder("ORDER BY nummer");

    kontenList = new TablePart(konten, new KontoAction());
    kontenList.addColumn("Nummer", "nummer");
    kontenList.addColumn("Bezeichnung", "bezeichnung");
    kontenList.addColumn("Hibiscus-Konto", "hibiscusid", new Formatter()
    {

      public String format(Object o)
      {
        if (o == null)
        {
          return "nein";
        }
        if (o instanceof Integer)
        {
          Integer hibid = (Integer) o;
          if (hibid.intValue() >= 0)
          {
            return "ja";
          }
        }
        return "nein";
      }
    }, false, Column.ALIGN_LEFT);
    kontenList.addColumn("Konto-Eröffnung", "eroeffnung", new DateFormatter(
        new JVDateFormatTTMMJJJJ()));
    kontenList.addColumn("Konto-Auflösung", "aufloesung", new DateFormatter(
        new JVDateFormatTTMMJJJJ()));
    kontenList.setRememberColWidths(true);
    kontenList.setContextMenu(new KontoMenu());
    kontenList.setRememberOrder(true);
    kontenList.setSummary(true);
    return kontenList;
  }

  public void refreshTable() throws RemoteException
  {
    kontenList.removeAll();
    DBIterator konten = Einstellungen.getDBService().createList(Konto.class);
    konten.setOrder("ORDER BY nummer");
    while (konten.hasNext())
    {
      kontenList.addItem(konten.next());
    }
  }

}
