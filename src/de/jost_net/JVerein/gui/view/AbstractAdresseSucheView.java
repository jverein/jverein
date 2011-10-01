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
    ButtonArea buttons = new ButtonArea(this.getParent(), 2);
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