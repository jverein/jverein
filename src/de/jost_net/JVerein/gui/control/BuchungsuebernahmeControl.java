/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
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

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.input.KontoauswahlInput;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Konto;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
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
import de.willuhn.jameica.gui.input.DialogInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.hbci.HBCI;
import de.willuhn.jameica.hbci.rmi.Umsatz;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class BuchungsuebernahmeControl extends AbstractControl
{

  private Settings settings = null;

  private DialogInput konto = null;

  private TablePart buchungsList;

  public BuchungsuebernahmeControl(AbstractView view)
  {
    super(view);
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public DialogInput getKonto() throws RemoteException
  {
    if (konto != null)
    {
      return konto;
    }
    // String kontoid = settings.getString("kontoid", "");
    konto = new KontoauswahlInput().getKontoAuswahl(false, "", true);
    return konto;
  }

  public Button getSuchButton()
  {
    Button button = new Button("Suchen", new Action()
    {
      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        try
        {
          getBuchungsList();
          Konto k = (Konto) getKonto().getValue();
          settings.setAttribute("kontoid", k.getID());
        }
        catch (RemoteException e)
        {
          e.printStackTrace();
          throw new ApplicationException("Fehler beim Aufbau der Buchungsliste");
        }
      }
    }, null, false, "system-search.png");
    return button;
  }

  public Button getUebernahmeButton()
  {
    Button button = new Button("Übernahme", new Action()
    {
      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        try
        {
          List<?> buchungen = buchungsList.getItems();
          for (int i = 0; i < buchungen.size(); i++)
          {
            Umsatz u = (Umsatz) buchungen.get(i);
            if ((u.getFlags() & Umsatz.FLAG_NOTBOOKED) == 0)
            {
              Buchung b = (Buchung) Einstellungen.getDBService().createObject(
                  Buchung.class, null);
              b.setUmsatzid(new Integer(u.getID()));
              b.setKonto((Konto) getKonto().getValue());
              b.setName(u.getGegenkontoName());
              b.setBetrag(u.getBetrag());
              b.setZweck(u.getZweck());
              String[] moreLines = u.getWeitereVerwendungszwecke();
              String zweck = u.getZweck();
              String line2 = u.getZweck2();
              if (line2 != null && line2.trim().length() > 0)
              {
                zweck += "\r\n" + line2.trim();
              }
              if (moreLines != null && moreLines.length > 0)
              {
                for (String s : moreLines)
                {
                  if (s == null || s.trim().length() == 0)
                    continue;
                  zweck += "\r\n" + s.trim();
                }
              }
              b.setZweck(zweck);
              b.setDatum(u.getDatum());
              b.setArt(u.getArt());
              b.setKommentar(u.getKommentar());
              b.store();
              buchungsList.removeItem(u);
            }
          }
          GUI.getStatusBar().setSuccessText("Daten übernommen");
          GUI.getCurrentView().reload();
        }
        catch (ApplicationException ae)
        {
          GUI.getStatusBar().setErrorText(ae.getMessage());
          throw ae;
        }
        catch (Exception e)
        {
          Logger.error("error while reading objects from ", e);
          ApplicationException ae = new ApplicationException(
              "Fehler beim der Übernahme: ", e);
          GUI.getStatusBar().setErrorText(ae.getMessage());
          throw ae;
        }
      }
    }, null, true, "go.png");
    return button;
  }

  public Part getBuchungsList() throws RemoteException
  {
    Integer hibid = Integer.valueOf(-1);
    Integer jvid = Integer.valueOf(-1);
    Konto k = (Konto) getKonto().getValue();
    if (k != null && k.getHibiscusId() != null)
    {
      hibid = k.getHibiscusId();
      jvid = new Integer(k.getID());
    }
    DBService service = Einstellungen.getDBService();
    String sql = "select max(umsatzid) from buchung where konto = "
        + jvid.toString();

    ResultSetExtractor rs = new ResultSetExtractor()
    {
      @Override
      public Object extract(ResultSet rs) throws SQLException
      {
        if (!rs.next())
        {
          return Integer.valueOf(0);
        }
        return Integer.valueOf(rs.getInt(1));
      }
    };
    Integer maximum = (Integer) service.execute(sql, new Object[] {}, rs);

    try
    {
      DBService hibservice = (DBService) Application.getServiceFactory()
          .lookup(HBCI.class, "database");
      // try
      // {
      // @SuppressWarnings("unused")
      // // Hier wird getestet, ob das Hibiscus-Konto existiert. Die Variable
      // // wird nicht benötigt. Falls das Konto nicht existiert, wird eine
      // // Exception geworfen.
      // de.willuhn.jameica.hbci.rmi.Konto hkto =
      // (de.willuhn.jameica.hbci.rmi.Konto) hibservice
      // .createObject(de.willuhn.jameica.hbci.rmi.Konto.class, hibid + "");
      // }
      // catch (ObjectNotFoundException e)
      // {
      // DBIterator hibkonten = hibservice
      // .createList(de.willuhn.jameica.hbci.rmi.Konto.class);
      // hibkonten.addFilter("kontonummer = ?", k.getNummer());
      // if (hibkonten.size() == 1)
      // {
      // de.willuhn.jameica.hbci.rmi.Konto k2 =
      // (de.willuhn.jameica.hbci.rmi.Konto) hibkonten
      // .next();
      // k.setHibiscusId(Integer.parseInt(k2.getID()));
      // k.store();
      // }
      // else
      // {
      // GUI.getStatusBar().setErrorText(
      // "Konto " + k.getNummer() + " ist nicht ordnungsgemäß verknüpft!");
      // }
      // }
      DBIterator hibbuchungen = hibservice.createList(Umsatz.class);
      if (maximum.intValue() > 0)
      {
        hibbuchungen.addFilter("id >" + maximum);
      }
      hibbuchungen.addFilter("konto_id = ?", new Object[] { hibid });
      hibbuchungen.setOrder("ORDER BY id");

      if (buchungsList == null)
      {
        buchungsList = new TablePart(hibbuchungen, null);
        buchungsList.addColumn("Nr", "id-int");
        buchungsList.addColumn("Datum", "datum", new DateFormatter(
            new JVDateFormatTTMMJJJJ()));
        buchungsList.addColumn("Name", "name");
        buchungsList.addColumn("Verwendungszweck", "zweck");
        buchungsList.addColumn("Betrag", "betrag", new CurrencyFormatter("",
            Einstellungen.DECIMALFORMAT));
        buchungsList.setRememberColWidths(true);
        buchungsList.setRememberOrder(true);
        buchungsList.setSummary(true);
      }
      else
      {
        buchungsList.removeAll();
        while (hibbuchungen.hasNext())
        {
          buchungsList.addItem(hibbuchungen.next());
        }
      }
    }
    catch (Exception e)
    {
      throw new RemoteException(e.getMessage());
    }
    return buchungsList;
  }
}
