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
 * Revision 1.12  2011-02-12 09:36:28  jost
 * Statische Codeanalyse mit Findbugs
 *
 * Revision 1.11  2011-01-15 09:46:47  jost
 * Tastatursteuerung wegen Problemen mit Jameica/Hibiscus wieder entfernt.
 *
 * Revision 1.10  2010-10-15 09:58:23  jost
 * Code aufgeräumt
 *
 * Revision 1.9  2010-10-07 19:49:22  jost
 * Hilfe in die View verlagert.
 *
 * Revision 1.8  2010-08-23 13:39:31  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.7  2009/06/11 21:03:39  jost
 * Vorbereitung I18N
 *
 * Revision 1.6  2009/01/20 20:09:24  jost
 * neue Icons
 *
 * Revision 1.5  2009/01/20 19:15:19  jost
 * neu: Back-Button mit Icon
 *
 * Revision 1.4  2008/09/30 10:20:46  jost
 * Kursteilnehmer kÃ¶nnen nach Namen und Eingabedatum gefiltert werden.
 *
 * Revision 1.3  2008/05/24 14:04:08  jost
 * Redatkionelle Ã„nderung
 *
 * Revision 1.2  2008/01/01 19:51:59  jost
 * Erweiterung um Hilfe-Funktion
 *
 * Revision 1.1  2007/02/25 19:13:46  jost
 * Neu: Kursteilnehmer
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.KursteilnehmerDetailAction;
import de.jost_net.JVerein.gui.control.KursteilnehmerControl;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;

public class KursteilnehmerSucheView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr("Suche Kursteilnehmer"));

    KursteilnehmerControl control = new KursteilnehmerControl(this);

    String sql = "select count(*) from kursteilnehmer";
    DBService service = Einstellungen.getDBService();
    ResultSetExtractor rs = new ResultSetExtractor()
    {

      public Object extract(ResultSet rs) throws SQLException
      {
        rs.next();
        return Long.valueOf(rs.getLong(1));
      }
    };
    Long anzahl = (Long) service.execute(sql, new Object[] {}, rs);

    LabelGroup group = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Filter"));
    group.addLabelPair(JVereinPlugin.getI18n().tr("Name"),
        control.getSuchname());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Eingabedatum von"),
        control.getEingabedatumvon());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Eingabedatum bis"),
        control.getEingabedatumbis());

    if (anzahl.longValue() > 0)
    {
      control.getKursteilnehmerTable().paint(getParent());
    }
    ButtonArea buttons = new ButtonArea(this.getParent(), 2);
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), DokumentationUtil.KURSTEILNEHMER, false,
        "help-browser.png");
    buttons.addButton(JVereinPlugin.getI18n().tr("neu"),
        new KursteilnehmerDetailAction(), null, false, "document-new.png");
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
