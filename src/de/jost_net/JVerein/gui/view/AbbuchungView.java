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
 * Revision 1.5  2007/07/06 11:37:18  jost
 * Zur Kompatibilität: Änderung der Plausi.
 *
 * Revision 1.4  2007/02/25 19:13:05  jost
 * Neu: Kursteilnehmer
 *
 * Revision 1.3  2007/02/23 20:27:28  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.2  2006/10/21 09:18:54  jost
 * Zusätzliche Plausi.
 *
 * Revision 1.1  2006/09/20 15:39:10  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.BackAction;
import de.jost_net.JVerein.gui.control.AbbuchungControl;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class AbbuchungView extends AbstractView
{
  public void bind() throws Exception
  {
    String sql = "select count(*) from stammdaten";
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
    if (anzahl.longValue() == 0)
    {
      throw new ApplicationException("Stammdaten fehlen. "
          + " Bitte unter Plugins|JVerein|Stammdaten erfassen.");
    }

    GUI.getView().setTitle("Abbuchung");

    final AbbuchungControl control = new AbbuchungControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Parameter");
    group.addLabelPair("Jahresabbuchung", control.getJahresabbuchung());
    group.addLabelPair("Von Eingabedatum", control.getVondatum());
    group.addLabelPair("Zahlungsgrund", control.getZahlungsgrund());
    group.addLabelPair("Zusatzabbuchung", control.getZusatzabbuchung());
    group.addLabelPair("Kursteilnehmer", control.getKursteilnehmer());

    ButtonArea buttons = new ButtonArea(this.getParent(), 2);
    buttons.addButton(control.getStartButton());
    buttons.addButton("<< Zurück", new BackAction());

  }

  public void unbind() throws ApplicationException
  {
  }
}
