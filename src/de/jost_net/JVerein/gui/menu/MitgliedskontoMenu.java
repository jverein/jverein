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
 * Revision 1.1  2010-07-25 18:36:03  jost
 * Neu: Mitgliedskonto
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.menu;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.MitgliedskontoDetailAction;
import de.jost_net.JVerein.gui.action.MitgliedskontoDetailSollLoeschenAction;
import de.jost_net.JVerein.gui.action.MitgliedskontoDetailSollNeuAction;
import de.jost_net.JVerein.gui.control.MitgliedskontoNode;
import de.jost_net.JVerein.rmi.Buchung;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;
import de.willuhn.jameica.gui.parts.ContextMenuItem;
import de.willuhn.logging.Logger;
import de.willuhn.util.I18N;

/**
 * Kontext-Menu, welches an MitgliedskontenListen gehangen werden kann.
 */
public class MitgliedskontoMenu extends ContextMenu
{
  private final static I18N i18n = JVereinPlugin.getI18n();

  /**
   * Erzeugt ein Kontext-Menu fuer eine Liste von Mitgliedskonten.
   */
  public MitgliedskontoMenu()
  {
    addItem(new MitgliedItem(i18n.tr("neue Sollbuchung"),
        new MitgliedskontoDetailSollNeuAction(), "accessories-calculator.png"));
    addItem(ContextMenuItem.SEPARATOR);
    addItem(new SollItem(i18n.tr("Sollbuchung bearbeiten"),
        new MitgliedskontoDetailAction(), "accessories-calculator.png"));
    addItem(new SollOhneIstItem(i18n.tr("Sollbuchung löschen"),
        new MitgliedskontoDetailSollLoeschenAction(),
        "accessories-calculator.png"));
  }

  private class MitgliedItem extends CheckedContextMenuItem
  {
    /**
     * @param text
     * @param action
     * @param optionale
     *          Angabe eines Icons.
     */
    private MitgliedItem(String text, Action action, String icon)
    {
      super(text, action, icon);
    }

    public boolean isEnabledFor(Object o)
    {
      if (o instanceof MitgliedskontoNode)
      {
        MitgliedskontoNode mkn = (MitgliedskontoNode) o;
        if (mkn.getType() == MitgliedskontoNode.MITGLIED)
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

  private class SollItem extends CheckedContextMenuItem
  {
    /**
     * @param text
     * @param action
     * @param optionale
     *          Angabe eines Icons.
     */
    private SollItem(String text, Action action, String icon)
    {
      super(text, action, icon);
    }

    public boolean isEnabledFor(Object o)
    {
      if (o instanceof MitgliedskontoNode)
      {
        MitgliedskontoNode mkn = (MitgliedskontoNode) o;
        if (mkn.getType() == MitgliedskontoNode.SOLL)
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

  private class SollOhneIstItem extends CheckedContextMenuItem
  {
    /**
     * @param text
     * @param action
     * @param optionale
     *          Angabe eines Icons.
     */
    private SollOhneIstItem(String text, Action action, String icon)
    {
      super(text, action, icon);
    }

    public boolean isEnabledFor(Object o)
    {
      if (o instanceof MitgliedskontoNode)
      {
        MitgliedskontoNode mkn = (MitgliedskontoNode) o;
        if (mkn.getType() == MitgliedskontoNode.SOLL)
        {
          DBIterator it;
          try
          {
            it = Einstellungen.getDBService().createList(Buchung.class);
            it.addFilter("mitgliedskonto = ?", new Object[] { mkn.getID() });
            if (it.size() == 0)
            {
              return true;
            }
          }
          catch (RemoteException e)
          {
            Logger.error("Fehler", e);
          }
          return false;
        }
        else
        {
          return false;
        }
      }
      return super.isEnabledFor(o);
    }
  }

  private class IstItem extends CheckedContextMenuItem
  {
    /**
     * @param text
     * @param action
     * @param optionale
     *          Angabe eines Icons.
     */
    private IstItem(String text, Action action, String icon)
    {
      super(text, action, icon);
    }

    public boolean isEnabledFor(Object o)
    {
      if (o instanceof MitgliedskontoNode)
      {
        MitgliedskontoNode mkn = (MitgliedskontoNode) o;
        if (mkn.getType() == MitgliedskontoNode.IST)
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
