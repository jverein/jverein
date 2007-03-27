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
 **********************************************************************/
package de.jost_net.JVerein.gui.parts;

import java.rmi.RemoteException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;

import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.util.Container;
import de.willuhn.jameica.gui.util.SimpleContainer;
import de.willuhn.jameica.gui.util.TabGroup;

public class Familienverband implements Part
{
  private TabFolder tab;

  private Beitragsgruppe gruppe;

  private MitgliedControl control;

  private Container cont;

  public Familienverband(MitgliedControl control, Beitragsgruppe gruppe)
  {
    this.control = control;
    this.gruppe = gruppe;
  }

  public void paint(Composite parent) throws RemoteException
  {
    cont = new SimpleContainer(parent);
    final GridData g = new GridData(GridData.FILL_HORIZONTAL);

    tab = new TabFolder(cont.getComposite(), SWT.NONE);
    tab.setLayoutData(g);
    new TabGroup(tab, "");
    TabGroup tg1 = new TabGroup(tab, "");
    control.getFamilienangehoerigenTable().paint(tg1.getComposite());
    TabGroup tg2 = new TabGroup(tab, "");
    tg2.addLabelPair("Zahler", control.getZahler());
    if (gruppe != null)
    {
      tab.setSelection(gruppe.getBeitragsArt());
      tab.layout(true);
    }
  }

  public void setBeitragsgruppe(Beitragsgruppe gruppe)
  {
    this.gruppe = gruppe;
    try
    {
      tab.setSelection(gruppe.getBeitragsArt());
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    }
    tab.redraw();
    tab.layout(true);
  }

}
