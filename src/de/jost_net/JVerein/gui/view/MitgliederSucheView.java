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
 * Revision 1.5  2007/04/03 16:03:24  jost
 * Meldung, wenn keine Beitragsgruppe erfaßt ist.
 *
 * Revision 1.4  2007/03/25 17:02:43  jost
 * 1. Zeitoptimierung bei der Suche
 * 2. Tab mit allen Mitgliedern
 *
 * Revision 1.3  2007/02/23 20:27:42  jost
 * Mail- und Webadresse im Header korrigiert.
 *
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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import de.willuhn.jameica.gui.input.LabelInput;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.Color;
import de.willuhn.jameica.gui.util.TabGroup;
import de.willuhn.util.ApplicationException;

public class MitgliederSucheView extends AbstractView
{

  public void bind() throws Exception
  {
    final String[] b = { "A", "Ä", "B", "C", "D", "E", "F", "G", "H", "I", "J",
        "K", "L", "M", "N", "O", "Ö", "P", "Q", "R", "S", "T", "U", "Ü", "V",
        "W", "X", "Y", "Z", "*" };
    GUI.getView().setTitle("Suche Mitglied");

    final MitgliedControl control = new MitgliedControl(this);

    String sql = "select count(*) from beitragsgruppe";
    DBService service = Einstellungen.getDBService();
    ResultSetExtractor rs = new ResultSetExtractor()
    {
      public Object extract(ResultSet rs) throws RemoteException, SQLException
      {
        rs.next();
        return new Long(rs.getLong(1));
      }
    };
    Long anzahlbeitragsgruppe = (Long) service
        .execute(sql, new Object[] {}, rs);
    if (anzahlbeitragsgruppe.longValue() == 0)
    {
      new LabelInput("Noch keine Beitragsgruppe erfaßt. Bitte unter "
          + "Plugins|JVerein|Beitragsgruppe erfassen.").paint(getParent());
    }

    rs = new ResultSetExtractor()
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
      final TabFolder folder = new TabFolder(getParent(), SWT.NONE);
      folder.setLayoutData(new GridData(GridData.FILL_BOTH));
      folder.setBackground(Color.BACKGROUND.getSWTColor());

      final TabGroup[] tab = new TabGroup[b.length];
      final TablePart[] p = new TablePart[b.length];

      for (int i = 0; i < b.length; i++)
      {
        tab[i] = new TabGroup(folder, b[i]);
      }
      p[0] = control.getMitgliedTable(b[0]);
      tab[0].addPart(p[0]);
      folder.addSelectionListener(new SelectionListener()
      {
        public void widgetDefaultSelected(SelectionEvent e)
        {
          //
        }

        public void widgetSelected(SelectionEvent e)
        {
          int si = folder.getSelectionIndex();
          try
          {
            boolean gefuellt;
            if (p[si] == null)
            {
              gefuellt = false;
            }
            else
            {
              gefuellt = true;
            }
            p[si] = control.getMitgliedTable(b[si]);

            if (!gefuellt)
            {
              p[si].paint(tab[si].getComposite());
            }
            folder.getParent().layout(true, true);
          }
          catch (RemoteException e1)
          {
            e1.printStackTrace();
          }
        }
      });
    }
    ButtonArea buttons = new ButtonArea(this.getParent(), 2);
    if (anzahlbeitragsgruppe > 0)
    {
      buttons.addButton("Neu", new MitgliedDetailAction());
    }
    buttons.addButton("<< Zurück", new BackAction());
  }

  public void unbind() throws ApplicationException
  {
  }
}
