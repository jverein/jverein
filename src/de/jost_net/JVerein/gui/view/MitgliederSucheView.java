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
 * Revision 1.2  2006/10/21 09:19:30  jost
 * Korrekter Ablauf bei leerer Datenbank
 *
 * Revision 1.1  2006/09/20 15:39:10  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TabFolder;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.BackAction;
import de.jost_net.JVerein.gui.action.MitgliedDetailAction;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.Color;
import de.willuhn.jameica.gui.util.TabGroup;
import de.willuhn.util.ApplicationException;

public class MitgliederSucheView extends AbstractView
{

  public void bind() throws Exception
  {
    GUI.getView().setTitle("Suche Mitglied");

    MitgliedControl control = new MitgliedControl(this);

    String sql = "select count(*) from mitglied";
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

    if (anzahl.longValue() > 0)
    {
      TabFolder folder = new TabFolder(getParent(), SWT.NONE);
      folder.setLayoutData(new GridData(GridData.FILL_BOTH));
      folder.setBackground(Color.BACKGROUND.getSWTColor());

      TabGroup[] tab = new TabGroup[26];
      TablePart[] p = new TablePart[26];
      for (int i = 'A'; i <= 'Z'; i++)
      {
        char[] zeichen = { (char) i };
        tab[i - 'A'] = new TabGroup(folder, new String(zeichen));
        control.getMitgliedTable(p[i - 'A'], new String(zeichen)).paint(
            tab[i - 'A'].getComposite());
      }
      TabGroup tab1 = new TabGroup(folder, "Ä");
      TablePart p1 = null;
      control.getMitgliedTable(p1, "Ä").paint(tab1.getComposite());

      TabGroup tab2 = new TabGroup(folder, "Ö");
      TablePart p2 = null;
      control.getMitgliedTable(p2, "Ö").paint(tab2.getComposite());

      TabGroup tab3 = new TabGroup(folder, "Ü");
      TablePart p3 = null;
      control.getMitgliedTable(p3, "Ü").paint(tab3.getComposite());
    }
    ButtonArea buttons = new ButtonArea(this.getParent(), 2);
    buttons.addButton("Neu", new MitgliedDetailAction());
    buttons.addButton("<< Zurück", new BackAction());
  }

  public void unbind() throws ApplicationException
  {
  }
}
