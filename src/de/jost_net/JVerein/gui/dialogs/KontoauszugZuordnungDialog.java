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
package de.jost_net.JVerein.gui.dialogs;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.JVereinPlugin;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.IntegerInput;
import de.willuhn.jameica.gui.input.LabelInput;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.Color;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.system.OperationCanceledException;

/**
 * Dialog zur Zuordnung der Kontoauszugsinformationen (Auszug/Blatt)
 */
public class KontoauszugZuordnungDialog extends AbstractDialog
{

  private IntegerInput auszug = null;

  private Integer intAuszug;

  private IntegerInput blatt = null;

  private Integer intBlatt;

  private CheckboxInput ueberschreiben = null;

  private LabelInput status = null;

  private boolean ueberschr;

  /**
   * @param position
   */
  public KontoauszugZuordnungDialog(int position)
  {
    super(position);
    setTitle(JVereinPlugin.getI18n().tr("Zuordnung Kontoauszugsinformationen"));
    setSize(400, 225);
  }

  @Override
  protected void paint(Composite parent) throws Exception
  {
    LabelGroup group = new LabelGroup(parent, "");
    group.addLabelPair(JVereinPlugin.getI18n().tr("Auszug"), getAuszug());
    group.addLabelPair(JVereinPlugin.getI18n().tr("Blatt"), getBlatt());
    group.addLabelPair(
        JVereinPlugin.getI18n().tr("Kontoauszugsinformationen überschreiben"),
        getUeberschreiben());
    group.addLabelPair("", getStatus());

    ButtonArea buttons = new ButtonArea(parent, 2);
    buttons.addButton(JVereinPlugin.getI18n().tr("übernehmen"), new Action()
    {

      public void handleAction(Object context)
      {
        intAuszug = (Integer) auszug.getValue();
        if (intAuszug.intValue() <= 0)
        {
          status.setValue(JVereinPlugin.getI18n().tr("Auszugsnummer fehlt"));
          status.setColor(Color.ERROR);
          return;
        }
        intBlatt = (Integer) blatt.getValue();
        if (intBlatt <= 0)
        {
          status.setValue(JVereinPlugin.getI18n().tr("Blattnummer fehlt"));
          status.setColor(Color.ERROR);
          return;
        }
        intAuszug = (Integer) getAuszug().getValue();
        intBlatt = (Integer) getBlatt().getValue();
        ueberschr = (Boolean) getUeberschreiben().getValue();
        close();
      }
    }, null, true);
    buttons.addButton(JVereinPlugin.getI18n().tr("abbrechen"), new Action()
    {

      public void handleAction(Object context)
      {
        throw new OperationCanceledException();
      }
    });

  }

  /**
   * @see de.willuhn.jameica.gui.dialogs.AbstractDialog#getData()
   */
  @Override
  public Object getData() throws Exception
  {
    return null;
  }

  public Integer getAuszugValue()
  {
    return intAuszug;
  }

  public Integer getBlattValue()
  {
    return intBlatt;
  }

  public boolean getOverride()
  {
    return ueberschr;
  }

  private IntegerInput getAuszug()
  {
    if (auszug != null)
    {
      return auszug;
    }
    auszug = new IntegerInput(0);
    return auszug;
  }

  private IntegerInput getBlatt()
  {
    if (blatt != null)
    {
      return blatt;
    }
    blatt = new IntegerInput(0);
    return blatt;
  }

  private LabelInput getStatus()
  {
    if (status != null)
    {
      return status;
    }
    status = new LabelInput("");
    return status;
  }

  private CheckboxInput getUeberschreiben()
  {
    if (ueberschreiben != null)
    {
      return ueberschreiben;
    }
    ueberschreiben = new CheckboxInput(false);
    return ueberschreiben;
  }
}
