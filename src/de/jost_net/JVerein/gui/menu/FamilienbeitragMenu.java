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
package de.jost_net.JVerein.gui.menu;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.FamilienmitgliedEntfernenAction;
import de.jost_net.JVerein.gui.control.FamilienbeitragNode;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;
import de.willuhn.util.I18N;

/**
 * Kontext-Menu zu den Famlienbeiträgen.
 */
public class FamilienbeitragMenu extends ContextMenu
{

  private final static I18N i18n = JVereinPlugin.getI18n();

  public FamilienbeitragMenu()
  {
    addItem(new AngehoerigerItem(i18n.tr("Aus Familienverband entfernen"),
        new FamilienmitgliedEntfernenAction(), "accessories-calculator.png"));
  }

  private static class AngehoerigerItem extends CheckedContextMenuItem
  {

    /**
     * @param text
     * @param action
     * @param optionale
     *          Angabe eines Icons.
     */
    private AngehoerigerItem(String text, Action action, String icon)
    {
      super(text, action, icon);
    }

    @Override
    public boolean isEnabledFor(Object o)
    {
      if (o instanceof FamilienbeitragNode)
      {
        FamilienbeitragNode fbn = (FamilienbeitragNode) o;
        if (fbn.getType() == FamilienbeitragNode.ANGEHOERIGER)
        {
          return true;
        }
        else
        {
          return false;
        }
      }
      return super.isEnabledFor(o);
    }
  }

}
