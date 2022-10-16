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
package de.jost_net.JVerein.gui.parts;

import java.rmi.RemoteException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;

import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.gui.util.GuiRepainter;
import de.jost_net.JVerein.keys.ArtBeitragsart;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.util.Container;
import de.willuhn.jameica.gui.util.SimpleContainer;
import de.willuhn.jameica.gui.util.TabGroup;
import de.willuhn.logging.Logger;

public class Familienverband implements Part
{

  private TabFolder tab;

  private Beitragsgruppe gruppe;

  private MitgliedControl control;

  private Container cont;

  private boolean visible;

  private Composite parent;

  public Familienverband(MitgliedControl control, Beitragsgruppe gruppe)
  {
    this.control = control;
    this.gruppe = gruppe;
    this.visible = true;
    this.parent = null;
    this.cont = null;
    this.tab = null;
  }

  /**
   * Zeichnet den Familienverband Part. Die Ausgabe hängt von Feld visible ab,
   * das über die Funktion setVisible(boolean) gesetzt wird.
   */
  @Override
  public void paint(Composite parent) throws RemoteException
  {
    if (this.parent == null)
    {
      this.parent = parent;
    }

    // Familienverband soll nicht angezeigt werden...
    if (!visible)
    {
      return;
    }

    // Familienverband soll angezeigt werden...

    // Hier beginnt das eigentlich Zeichnen des Familienverbandes:
    if (cont == null)
      cont = new SimpleContainer(parent, true, 5);

    final GridData grid = new GridData(GridData.FILL_HORIZONTAL);
    grid.grabExcessHorizontalSpace = true;
    cont.getComposite().setLayoutData(grid);

    boolean zeichneFamilienverbandAlsTabGroup = true;
    if (zeichneFamilienverbandAlsTabGroup)
    {
      final GridData g = new GridData(GridData.FILL_HORIZONTAL);

      tab = new TabFolder(cont.getComposite(), SWT.NONE);
      tab.setLayoutData(g);
      TabGroup tg1 = new TabGroup(tab, "Familienverband");
      control.getFamilienangehoerigenTable().paint(tg1.getComposite());
      TabGroup tg2 = new TabGroup(tab, "Zahlendes Familienmitglied");
      // erstelle neuen zahler: (force == true)
      control.getZahler(true).setComment(
          "Nur für Beitragsgruppenart: \"Familie: Angehörige\"");
      tg2.addLabelPair("Zahler", control.getZahler());

      if (gruppe != null)
      {
        setBeitragsgruppe(gruppe);
      }
    }
    else
    {
      tab = null;
      // erstelle neuen zahler: (force == true)
      cont.addLabelPair("Zahler", control.getZahler(true));
      control.getZahler().setMandatory(true);
      cont.addPart(control.getFamilienangehoerigenTable());
    }

  }

  /**
   * Aktiviert den ersten Tab, wenn Beitragsgruppe FAMILIE_ZAHLER ist, ansonsten
   * den zweiten Tab. So kann für Mitglieder deren Beitragsgruppe
   * FAMILIE_ANGEHOERIGER direkt auf dem zweiten Tab ihr Familien-Zahler
   * eingestellt werden.
   * 
   * @param gruppe
   */
  public void setBeitragsgruppe(Beitragsgruppe gruppe)
  {
    this.gruppe = gruppe;
    if (tab == null)
      return;
    try
    {
      if (gruppe.getBeitragsArt() == ArtBeitragsart.FAMILIE_ZAHLER)
        tab.setSelection(0);
      else if (gruppe.getBeitragsArt() == ArtBeitragsart.FAMILIE_ANGEHOERIGER)
        tab.setSelection(1);
    }
    catch (RemoteException e)
    {
      Logger.error("Fehler", e);
    }
    tab.redraw();
    tab.layout(true);
  }

  /**
   * Zeige GUI-Komponente für Familienverband an oder blendet diese aus.
   * 
   * @param showFamilienverband
   */
  public void setVisible(boolean showFamilienverband)
  {
    if (this.visible == showFamilienverband)
      return;
    this.visible = showFamilienverband;

    if (cont != null && !showFamilienverband)
    {
      cont.getComposite().dispose();
      cont = null;
    }

    if (parent != null)
    {
      try
      {
        paint(parent);
      }
      catch (RemoteException e)
      {
        Logger.error("Fehler", e);
      }
      GuiRepainter.repaint(parent);
      // updateGUI();
    }
  }
}
