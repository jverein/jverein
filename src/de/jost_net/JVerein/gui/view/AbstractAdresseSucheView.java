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
 * Revision 1.1  2011-01-29 19:30:09  jost
 * Feinschliff
 *
 * Revision 1.2  2011-01-28 09:18:30  jost
 * Korrekte Detailaction
 *
 * Revision 1.1  2011-01-27 22:21:37  jost
 * Neu: Speicherung von weiteren Adressen in der Mitgliedertabelle
 *
 * Revision 1.28  2011-01-15 09:46:47  jost
 * Tastatursteuerung wegen Problemen mit Jameica/Hibiscus wieder entfernt.
 *
 * Revision 1.27  2010-10-30 11:30:53  jost
 * Neu: Sterbetag
 *
 * Revision 1.26  2010-10-15 09:58:24  jost
 * Code aufger‰umt
 *
 * Revision 1.25  2010-10-07 19:49:22  jost
 * Hilfe in die View verlagert.
 *
 * Revision 1.24  2010-08-23 13:39:31  jost
 * Optimierung Tastatursteuerung
 *
 * Revision 1.23  2009/11/19 19:44:42  jost
 * Bugfix Eigenschaften
 *
 * Revision 1.22  2009/11/17 21:01:13  jost
 * Neu: Eigenschaft und EigenschaftGruppe
 *
 * Revision 1.21  2009/06/11 21:03:39  jost
 * Vorbereitung I18N
 *
 * Revision 1.20  2009/01/20 20:09:24  jost
 * neue Icons
 *
 * Revision 1.19  2009/01/20 19:15:19  jost
 * neu: Back-Button mit Icon
 *
 * Revision 1.18  2008/11/16 16:58:08  jost
 * Speicherung der Einstellung von Property-Datei in die Datenbank verschoben.
 *
 * Revision 1.17  2008/11/11 20:48:06  jost
 * 2spaltiges Layout und Selektion nach Geschlecht
 *
 * Revision 1.16  2008/05/24 14:04:08  jost
 * Redatkionelle √Ñnderung
 *
 * Revision 1.15  2008/05/22 06:54:31  jost
 * Redaktionelle √Ñnderung
 *
 * Revision 1.14  2008/03/08 19:30:16  jost
 * Neu: Externe Mitgliedsnummer
 *
 * Revision 1.13  2008/01/26 16:22:34  jost
 * √úberfl√ºssigen Knopf entfernt.
 * Speicherung der Default-Werte
 *
 * Revision 1.12  2008/01/25 16:04:24  jost
 * Neu: Eigenschaften des Mitgliedes
 *
 * Revision 1.11  2008/01/01 19:52:45  jost
 * Erweiterung um Hilfe-Funktion
 *
 * Revision 1.10  2007/12/01 19:08:54  jost
 * Wegfall Standardtab f√ºr die Suche
 *
 * Revision 1.9  2007/09/16 17:52:37  jost
 * Selektion nach Mitgliedsstatus
 *
 * Revision 1.8  2007/08/23 19:26:09  jost
 * Bugfix
 *
 * Revision 1.7  2007/08/23 18:45:25  jost
 * Standard-Tab f√ºr die Mitglieder-Suche
 * und Bug #011764
 *
 * Revision 1.6  2007/07/20 20:15:52  jost
 * Bessere Fehlermeldung
 *
 * Revision 1.5  2007/04/03 16:03:24  jost
 * Meldung, wenn keine Beitragsgruppe erfaﬂt ist.
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.gui.internal.buttons.Back;
import de.jost_net.JVerein.rmi.Adresstyp;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.LabelInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.Color;
import de.willuhn.jameica.gui.util.TabGroup;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;

public abstract class AbstractAdresseSucheView extends AbstractView
{

  private TabFolder folder = null;

  private TablePart[] p;

  private TabGroup[] tab;

  private Settings settings;

  private final String[] b = { "A", "ƒ", "B", "C", "D", "E", "F", "G", "H",
      "I", "J", "K", "L", "M", "N", "O", "÷", "P", "Q", "R", "S", "T", "U",
      "‹", "V", "W", "X", "Y", "Z", "*" };

  final MitgliedControl control = new MitgliedControl(this);

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle(JVereinPlugin.getI18n().tr(getTitle()));
    this.setCurrentObject(Einstellungen.getDBService().createObject(
        Mitglied.class, null)); // leeres Object erzeugen

    DBService service = Einstellungen.getDBService();
    String sql = "select count(*) from beitragsgruppe";
    ResultSetExtractor rs = new ResultSetExtractor()
    {

      public Object extract(ResultSet rs) throws SQLException
      {
        rs.next();
        return Long.valueOf(rs.getLong(1));
      }
    };
    Long anzahlbeitragsgruppe = (Long) service
        .execute(sql, new Object[] {}, rs);
    if (anzahlbeitragsgruppe.longValue() == 0)
    {
      new LabelInput(JVereinPlugin.getI18n().tr(
          "Noch keine Beitragsgruppe erfaﬂt. Bitte unter "
              + "Administration|Beitragsgruppen erfassen.")).paint(getParent());
    }
    rs = new ResultSetExtractor()
    {

      public Object extract(ResultSet rs) throws SQLException
      {
        rs.next();
        return Long.valueOf(rs.getLong(1));
      }
    };

    getFilter();

    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);

    Long anzahl = (Long) service.execute(sql, new Object[] {}, rs);
    if (anzahl.longValue() > 0)
    {
      folder = new TabFolder(getParent(), SWT.NONE);
      folder.setLayoutData(new GridData(GridData.FILL_BOTH));
      folder.setBackground(Color.BACKGROUND.getSWTColor());

      tab = new TabGroup[b.length];
      p = new TablePart[b.length];

      for (int i = 0; i < b.length; i++)
      {
        tab[i] = new TabGroup(folder, b[i], true, 1);
      }
      int si = 0;
      for (int i = 0; i < b.length; i++)
      {
        if (b[i].equals(settings.getString("lasttab", "A")))
        {
          si = i;
        }
      }
      Adresstyp at = (Adresstyp) control.getSuchAdresstyp(1).getValue();
      Logger.debug(at.getID() + ": " + at.getBezeichnung());
      paintTable(control, si, Integer.parseInt(at.getID()));
      folder.setSelection(si);
      folder.addSelectionListener(new SelectionListener()
      {

        public void widgetDefaultSelected(SelectionEvent e)
        {
          //
        }

        public void widgetSelected(SelectionEvent e)
        {
          int si = folder.getSelectionIndex();
          settings.setAttribute("lasttab", b[si]);
          TabRefresh(si);
        }
      });
    }
    ButtonArea buttons = new ButtonArea(this.getParent(), 3);
    buttons.addButton(new Back(false));
    buttons.addButton(getHilfeButton());
    if (anzahlbeitragsgruppe > 0)
    {
      buttons.addButton(JVereinPlugin.getI18n().tr("neu"), getDetailAction(),
          null, false, "document-new.png");
    }
  }

  private void TabRefresh(int index)
  {
    try
    {
      control.saveDefaults();
      if (p[index] != null)
      {
        Control[] c = tab[index].getComposite().getChildren();
        for (Control c1 : c)
        {
          c1.dispose();
        }
      }
      Adresstyp at = (Adresstyp) control.getSuchAdresstyp(2).getValue();
      paintTable(control, index, Integer.parseInt(at.getID()));
    }
    catch (RemoteException e1)
    {
      e1.printStackTrace();
    }
  }

  private void paintTable(MitgliedControl control, int index, int adresstyp)
      throws RemoteException
  {
    p[index] = control.getMitgliedTable(b[index], adresstyp, getDetailAction());
    p[index].paint(tab[index].getComposite());

    folder.getParent().layout(true, true);
  }

  public class FilterListener implements Listener
  {

    FilterListener()
    {
    }

    public void handleEvent(Event event)
    {
      if (event.type != SWT.Selection && event.type != SWT.FocusOut)
      {
        return;
      }

      try
      {
        int si = folder.getSelectionIndex();
        TabRefresh(si);
      }
      catch (NullPointerException e)
      {
        return;
      }
    }
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Mitgliedersuche</span></p>"
        + "<p>Durch einen Klick auf einen Karteireiter werden die Mitglieder "
        + "angezeigt, deren Familienname mit dem entsprechenden Buchstaben beginnt.</p>"
        + "<p>Anschlieﬂend kann das Mitglied durch einen Doppelklick ausgew‰hlt werden.</p>"
        + "</form>";
  }

  public abstract String getTitle();

  public abstract void getFilter() throws RemoteException;

  public abstract Action getDetailAction();

  public abstract Button getHilfeButton();
}