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
package de.jost_net.JVerein.gui.view;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.KursteilnehmerDetailAction;
import de.jost_net.JVerein.gui.control.KursteilnehmerControl;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

public class KursteilnehmerSucheView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Suche Kursteilnehmer");

    KursteilnehmerControl control = new KursteilnehmerControl(this);

    String sql = "select count(*) from kursteilnehmer";
    DBService service = Einstellungen.getDBService();
    ResultSetExtractor rs = new ResultSetExtractor()
    {

      @Override
      public Object extract(ResultSet rs) throws SQLException
      {
        rs.next();
        return Long.valueOf(rs.getLong(1));
      }
    };
    Long anzahl = (Long) service.execute(sql, new Object[] {}, rs);

    LabelGroup group = new LabelGroup(getParent(), "Filter");
    group.addLabelPair("Name", control.getSuchname());
    group.addLabelPair("Eingabedatum von", control.getEingabedatumvon());
    group.addLabelPair("Eingabedatum bis", control.getEingabedatumbis());

    if (anzahl.longValue() > 0)
    {
      control.getKursteilnehmerTable().paint(getParent());
    }
    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.KURSTEILNEHMER, false, "help-browser.png");
    buttons.addButton("neu", new KursteilnehmerDetailAction(), null, false,
        "document-new.png");
    buttons.paint(this.getParent());
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Kursteilnehmer</span></p>"
        + "<p>Kursteilnehmer sind Personen, die nicht dem Verein angehören und an Kursen teilnehmen. "
        + "Die Kursgebühr kann abgebucht werden und die Personen werden durch die Statistik erfaßt.</p>"
        + "<p>Unter Name wird der Name des Kontoinhabers eingegeben. In den Verwendungszweck-Feldern "
        + "wird die Kursbezeichnung und ggfls. der Name des Teilnehmers (falls nicht identisch mit dem "
        + "Kontoinhaber) eingetragen.</p></form>";
  }
}
