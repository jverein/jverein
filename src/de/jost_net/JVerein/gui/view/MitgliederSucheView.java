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
 * Revision 1.12  2008/01/25 16:04:24  jost
 * Neu: Eigenschaften des Mitgliedes
 *
 * Revision 1.11  2008/01/01 19:52:45  jost
 * Erweiterung um Hilfe-Funktion
 *
 * Revision 1.10  2007/12/01 19:08:54  jost
 * Wegfall Standardtab fÃ¼r die Suche
 *
 * Revision 1.9  2007/09/16 17:52:37  jost
 * Selektion nach Mitgliedsstatus
 *
 * Revision 1.8  2007/08/23 19:26:09  jost
 * Bugfix
 *
 * Revision 1.7  2007/08/23 18:45:25  jost
 * Standard-Tab fÃ¼r die Mitglieder-Suche
 * und Bug #011764
 *
 * Revision 1.6  2007/07/20 20:15:52  jost
 * Bessere Fehlermeldung
 *
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.BackAction;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.MitgliedDetailAction;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.rmi.JVereinDBService;
import de.jost_net.JVerein.server.DBSupportH2Impl;
import de.jost_net.JVerein.server.DBSupportMcKoiImpl;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.DialogInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.LabelInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.Color;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.gui.util.TabGroup;
import de.willuhn.jameica.system.Settings;
import de.willuhn.util.ApplicationException;

public class MitgliederSucheView extends AbstractView
{
  private TabFolder folder = null;

  private TablePart[] p;

  private TabGroup[] tab;

  private Settings settings;

  private final String[] b = { "A", "Ä", "B", "C", "D", "E", "F", "G", "H",
      "I", "J", "K", "L", "M", "N", "O", "Ö", "P", "Q", "R", "S", "T", "U",
      "Ü", "V", "W", "X", "Y", "Z", "*" };

  public void bind() throws Exception
  {
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

    LabelGroup group = new LabelGroup(getParent(), "Filter");
    Input mitglstat = control.getMitgliedStatus();
    mitglstat.addListener(new FilterListener(control));
    group.addLabelPair("Mitgliedschaft", mitglstat);
    if (!JVereinDBService.SETTINGS.getString("database.driver",
        DBSupportH2Impl.class.getName()).equals(
        DBSupportMcKoiImpl.class.getName()))
    {
      DialogInput mitgleigenschaften = control.getEigenschaftenAuswahl();
      mitgleigenschaften.addListener(new FilterListener(control));
      group.addLabelPair("Eigenschaften", mitgleigenschaften);
    }
    DateInput mitglgebdatvon = control.getGeburtsdatumvon();
    mitglgebdatvon.addListener(new FilterListener(control));
    group.addLabelPair("Geburtsdatum von", mitglgebdatvon);
    DateInput mitglgebdatbis = control.getGeburtsdatumbis();
    mitglgebdatbis.addListener(new FilterListener(control));
    group.addLabelPair("Geburtsdatum bis", mitglgebdatbis);
    SelectInput mitglbeitragsgruppe = control.getBeitragsgruppeAusw();
    mitglbeitragsgruppe.addListener(new FilterListener(control));
    group.addLabelPair("Beitragsgruppe", mitglbeitragsgruppe);

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
      paintTable(control, si);
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
          TabRefresh(control, si);
        }
      });
    }
    ButtonArea buttons = new ButtonArea(this.getParent(), 3);
    buttons.addButton("<< Zurück", new BackAction());
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.mitglied);
    if (anzahlbeitragsgruppe > 0)
    {
      buttons.addButton("Neu", new MitgliedDetailAction());
    }
  }

  public void unbind() throws ApplicationException
  {
  }

  private void TabRefresh(MitgliedControl control, int index)
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
      paintTable(control, index);
    }
    catch (RemoteException e1)
    {
      e1.printStackTrace();
    }
  }

  private void paintTable(MitgliedControl control, int index)
      throws RemoteException
  {
    p[index] = control.getMitgliedTable(b[index]);
    p[index].paint(tab[index].getComposite());

    folder.getParent().layout(true, true);
  }

  private class FilterListener implements Listener
  {
    private MitgliedControl control;

    FilterListener(MitgliedControl control)
    {
      this.control = control;
    }

    public void handleEvent(Event event)
    {
      if (event.type != SWT.Selection && event.type != SWT.FocusOut)
      {
        return;
      }
      int si = folder.getSelectionIndex();
      TabRefresh(control, si);
    }
  }
}