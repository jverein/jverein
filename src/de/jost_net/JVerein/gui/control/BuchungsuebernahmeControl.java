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

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.io.Buchungsuebernahme;
import de.jost_net.JVerein.rmi.Konto;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;

public class BuchungsuebernahmeControl extends AbstractControl
{

  private Settings settings = null;

  private TablePart kontenlist = null;

  public BuchungsuebernahmeControl(AbstractView view)
  {
    super(view);
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public Button getStartButton()
  {
    Button button = new Button("starten", new Action()
    {
      @Override
      public void handleAction(Object context)
      {
        new Buchungsuebernahme();
      }
    }, null, true, "go.png");
    return button;
  }

  public TablePart getKontenList() throws RemoteException
  {
    List<Uebernahmekonto> ueko = new ArrayList<Uebernahmekonto>();
    DBService service = Einstellungen.getDBService();
    DBIterator kontenit = service.createList(Konto.class);
    kontenit.addFilter("hibiscusid > 0");
    kontenit.setOrder("ORDER BY nummer");
    while (kontenit.hasNext())
    {
      Konto k = (Konto) kontenit.next();
      Uebernahmekonto ue = new Uebernahmekonto();
      ue.setNummer(k.getNummer());

      // Max JVerein-Buchungs-ID ermitteln
      String sql = "select max(umsatzid) from buchung where konto = "
          + k.getID();
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
      ue.setJVereinMaxBuchungID((Integer) Einstellungen.getDBService().execute(
          sql, new Object[] {}, rs));

      try
      {
        de.willuhn.jameica.hbci.rmi.Konto hk = (de.willuhn.jameica.hbci.rmi.Konto) Einstellungen
            .getHibiscusDBService().createObject(
                de.willuhn.jameica.hbci.rmi.Konto.class,
                k.getHibiscusId().toString());
        ue.setHibiscusKontonummer(hk.getIban());

        // Max Hibiscus-Buchungs-ID ermitteln
        sql = "select max(id) from umsatz where konto_id = " + hk.getID();
        rs = new ResultSetExtractor()
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
        ue.setHibiscusMaxBuchungID((Integer) Einstellungen
            .getHibiscusDBService().execute(sql, new Object[] {}, rs));
      }
      catch (Exception e)
      {
        ue.setBemerkung("Konto nicht gefunden");
        Logger.error("Konto nicht gefunden", e);
      }
      if (ue.getHibiscusMaxBuchungID() > ue.getJVereinMaxBuchungID())
      {
        ue.setBemerkung("Übernahme erforderlich");
      }
      else
      {
        ue.setBemerkung("Keine Übernahme erforderlich");
      }
      ueko.add(ue);
    }

    // JVerein-Konten ohne Hibiscus-ID
    kontenit = service.createList(Konto.class);
    kontenit.addFilter("hibiscusid = -1");
    kontenit.setOrder("ORDER BY nummer");
    while (kontenit.hasNext())
    {
      Konto k = (Konto) kontenit.next();
      Uebernahmekonto ue = new Uebernahmekonto();
      ue.setNummer(k.getNummer());
      ue.setHibiscusKontonummer("");
      ue.setJVereinMaxBuchungID(0);
      ue.setHibiscusMaxBuchungID(0);
      ue.setBemerkung("JVerein-Konto ohne Hibiscus-Zuordnung");
      ueko.add(ue);
    }

    kontenlist = new TablePart(ueko, null);
    kontenlist.addColumn("Nummer", "nummer");
    kontenlist.addColumn("JVerein max. Buchungsnummer", "jVereinMaxBuchungID");
    kontenlist.addColumn("Hibiscus-Kontonummer", "hibiscusKontonummer");
    kontenlist
        .addColumn("Hibiscus max. Buchungsnummer", "hibiscusMaxBuchungID");
    kontenlist.addColumn("Bemerkung", "bemerkung");

    kontenlist.setRememberColWidths(true);
    kontenlist.setRememberOrder(true);
    return kontenlist;
  }

  public class Uebernahmekonto
  {
    private String nummer;

    private int jvereinmaxbuchungid;

    private String hibiscuskto;

    private int hibiscusmaxbuchungid;

    private String bemerkung;

    public Uebernahmekonto()
    {
      //
    }

    public void setNummer(String nummer)
    {
      this.nummer = nummer;
    }

    public String getNummer()
    {
      return this.nummer;
    }

    public int getJVereinMaxBuchungID()
    {
      return jvereinmaxbuchungid;
    }

    public void setJVereinMaxBuchungID(int jvereinmaxbuchungid)
    {
      this.jvereinmaxbuchungid = jvereinmaxbuchungid;
    }

    public String getHibiscusKontonummer()
    {
      return this.hibiscuskto;
    }

    public void setHibiscusKontonummer(String hibiscuskontonummer)
    {
      this.hibiscuskto = hibiscuskontonummer;
    }

    public int getHibiscusMaxBuchungID()
    {
      return this.hibiscusmaxbuchungid;
    }

    public void setHibiscusMaxBuchungID(int hibiscusmaxbuchungid)
    {
      this.hibiscusmaxbuchungid = hibiscusmaxbuchungid;
    }

    public String getBemerkung()
    {
      return this.bemerkung;
    }

    public void setBemerkung(String bemerkung)
    {
      this.bemerkung = bemerkung;
    }
  }
}