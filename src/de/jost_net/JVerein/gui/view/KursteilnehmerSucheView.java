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

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.BackAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.KursteilnehmerDetailAction;
import de.jost_net.JVerein.gui.control.KursteilnehmerControl;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class KursteilnehmerSucheView extends AbstractView
{

  public void bind() throws Exception
  {
    GUI.getView().setTitle("Suche Kursteilnehmer");

    KursteilnehmerControl control = new KursteilnehmerControl(this);

    String sql = "select count(*) from kursteilnehmer";
    DBService service = Einstellungen.getDBService();
    ResultSetExtractor rs = new ResultSetExtractor()
    {
      public Object extract(ResultSet rs) throws RemoteException, SQLException
      {
        rs.next();
        return new Long(rs.getLong(1));
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
    ButtonArea buttons = new ButtonArea(this.getParent(), 3);
    buttons.addButton("<< Zurück", new BackAction());
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.KURSTEILNEHMER);
    buttons.addButton("Neu", new KursteilnehmerDetailAction());
  }

  public void unbind() throws ApplicationException
  {
  }
}
