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
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;

import de.jost_net.JVerein.gui.action.MitgliedDetailAction;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.parts.TreePart;
import de.willuhn.jameica.system.Settings;

public class FamilienbeitragControl extends AbstractControl
{
  private TreePart familienbeitragtree;

  private Settings settings = null;

  public FamilienbeitragControl(AbstractView view)
  {
    super(view);
    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
  }

  public TreePart getFamilienbeitraegeTree() throws RemoteException
  {
    familienbeitragtree = new TreePart(new FamilienbeitragNode(),
        new MitgliedDetailAction());
    familienbeitragtree.addColumn("Name", "name");
    // lehrgaengeList.setContextMenu(new LehrgangMenu());
    familienbeitragtree.setRememberColWidths(true);
    familienbeitragtree.setRememberOrder(true);
    return familienbeitragtree;
  }

}
