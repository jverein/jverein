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
 * Revision 1.1  2010-11-17 04:49:05  jost
 * Erster Code zum Thema Arbeitseinsatz
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.input.ArbeitseinsatzUeberpruefungInput;
import de.jost_net.JVerein.gui.parts.ArbeitseinsatzUeberpruefungList;
import de.jost_net.JVerein.io.ArbeitseinsatzZeile;
import de.jost_net.JVerein.rmi.Arbeitseinsatz;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.DecimalInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class ArbeitseinsatzControl extends AbstractControl
{

  private Settings settings = null;

  private DateInput datum = null;

  private DecimalInput stunden = null;

  private Arbeitseinsatz aeins = null;

  private TextInput bemerkung = null;

  private ArbeitseinsatzUeberpruefungList arbeitseinsatzueberpruefungList;

  private SelectInput suchjahr = null;

  private ArbeitseinsatzUeberpruefungInput auswertungschluessel = null;

  public ArbeitseinsatzControl(AbstractView view)
  {
    super(view);
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public Arbeitseinsatz getArbeitseinsatz()
  {
    if (aeins != null)
    {
      return aeins;
    }
    aeins = (Arbeitseinsatz) getCurrentObject();
    return aeins;
  }

  public DateInput getDatum() throws RemoteException
  {
    if (datum != null)
    {
      return datum;
    }

    Date d = getArbeitseinsatz().getDatum();

    this.datum = new DateInput(d, Einstellungen.DATEFORMAT);
    this.datum.setTitle("Datum");
    this.datum.setText("Datum Arbeitseinsatz wählen");
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
    return datum;
  }

  public DecimalInput getStunden() throws RemoteException
  {
    if (stunden != null)
    {
      return stunden;
    }
    stunden = new DecimalInput(getArbeitseinsatz().getStunden(),
        new DecimalFormat("###,###.##"));
    return stunden;
  }

  public TextInput getBemerkung() throws RemoteException
  {
    if (bemerkung != null)
    {
      return bemerkung;
    }
    bemerkung = new TextInput(getArbeitseinsatz().getBemerkung(), 50);
    bemerkung.setName("Bemerkung");
    return bemerkung;
  }

  public ArbeitseinsatzUeberpruefungInput getAuswertungSchluessel()
      throws RemoteException
  {
    if (auswertungschluessel != null)
    {
      return auswertungschluessel;
    }
    auswertungschluessel = new ArbeitseinsatzUeberpruefungInput(1);
    return auswertungschluessel;
  }

  public void handleStore()
  {
    try
    {
      Arbeitseinsatz ae = getArbeitseinsatz();
      ae.setDatum((Date) getDatum().getValue());
      ae.setStunden((Double) getStunden().getValue());
      ae.setBemerkung((String) getBemerkung().getValue());
      ae.store();
      GUI.getStatusBar().setSuccessText("Arbeitseinsatz gespeichert");
    }
    catch (ApplicationException e)
    {
      GUI.getStatusBar().setErrorText(e.getMessage());
    }
    catch (RemoteException e)
    {
      String fehler = "Fehler bei speichern des Arbeitseinsatzes";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  public SelectInput getSuchJahr() throws RemoteException, ApplicationException
  {
    if (suchjahr != null)
    {
      return suchjahr;
    }
    DBIterator list = Einstellungen.getDBService().createList(
        Arbeitseinsatz.class);
    list.setOrder("ORDER BY datum");
    Arbeitseinsatz ae = null;
    Calendar von = Calendar.getInstance();
    if (list.hasNext())
    {
      ae = (Arbeitseinsatz) list.next();
      von.setTime(ae.getDatum());
    }
    else
    {
      throw new ApplicationException(
          "Abbruch! Es existiert noch kein Arbeitseinsatz");
    }
    Calendar bis = Calendar.getInstance();
    ArrayList<Integer> jahre = new ArrayList<Integer>();

    for (int i = von.get(Calendar.YEAR); i <= bis.get(Calendar.YEAR); i++)
    {
      jahre.add(i);
    }

    suchjahr = new SelectInput(jahre, settings.getInt("jahr", jahre.get(0)));
    // suchjahr.setPleaseChoose("Bitte auswählen");
    suchjahr.setPreselected(settings.getInt("jahr", bis.get(Calendar.YEAR)));
    return suchjahr;
  }

  public Part getArbeitseinsatzUeberpruefungList() throws ApplicationException
  {
    try
    {
      settings.setAttribute("jahr", (Integer) getSuchJahr().getValue());
      settings.setAttribute("schluessel", (Integer) getAuswertungSchluessel()
          .getValue());

      if (arbeitseinsatzueberpruefungList == null)
      {
        arbeitseinsatzueberpruefungList = new ArbeitseinsatzUeberpruefungList(
            null, (Integer) getSuchJahr().getValue(),
            (Integer) getAuswertungSchluessel().getValue());
      }
      else
      {
        arbeitseinsatzueberpruefungList.setJahr((Integer) getSuchJahr()
            .getValue());
        arbeitseinsatzueberpruefungList
            .setSchluessel((Integer) getAuswertungSchluessel().getValue());
        ArrayList<ArbeitseinsatzZeile> zeile = arbeitseinsatzueberpruefungList
            .getInfo();
        arbeitseinsatzueberpruefungList.removeAll();
        for (ArbeitseinsatzZeile az : zeile)
        {
          arbeitseinsatzueberpruefungList.addItem(az);
        }
      }
    }
    catch (RemoteException e)
    {
      throw new ApplicationException("Fehler aufgetreten " + e.getMessage());
    }
    return arbeitseinsatzueberpruefungList.getArbeitseinsatzUeberpruefungList();
  }
}
