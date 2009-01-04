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
package de.jost_net.JVerein.gui.dialogs;

import java.rmi.RemoteException;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.LabelInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.Color;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.system.OperationCanceledException;
import de.willuhn.util.ApplicationException;

/**
 * Dialog zur Zuordnung einer Buchungsart.
 */
public class BuchungsartZuordnungDialog extends AbstractDialog
{
  private SelectInput buchungsarten = null;

  private CheckboxInput ueberschreiben = null;

  private LabelInput status = null;

  private Buchungsart buchungsart = null;

  private boolean ueberschr;

  /**
   * @param position
   */
  public BuchungsartZuordnungDialog(int position)
  {
    super(position);
    setTitle("Zuordnung Buchungsart");
    setSize(400, 200);
  }

  protected void paint(Composite parent) throws Exception
  {
    LabelGroup group = new LabelGroup(parent, "");
    group.addLabelPair("Buchungsart", getBuchungsartAuswahl());
    group.addLabelPair("Buchungsarten überschreiben", getUeberschreiben());
    group.addLabelPair("", getStatus());

    ButtonArea buttons = new ButtonArea(parent, 2);
    buttons.addButton("Übernehmen", new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        if (buchungsarten.getValue() == null)
        {
          status.setValue("Bitte auswählen");
          status.setColor(Color.ERROR);
          return;
        }
        if (buchungsarten.getValue() instanceof Buchungsart)
        {
          buchungsart = (Buchungsart) buchungsarten.getValue();
        }
        try
        {
          ueberschr = (Boolean) getUeberschreiben().getValue();
        }
        catch (RemoteException e)
        {
          throw new ApplicationException(e);
        }
        close();
      }
    }, null, true);
    buttons.addButton("Abbrechen", new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        throw new OperationCanceledException();
      }
    });

  }

  /**
   * @see de.willuhn.jameica.gui.dialogs.AbstractDialog#getData()
   */
  public Object getData() throws Exception
  {
    return buchungsart;
  }

  public Buchungsart getBuchungsart()
  {
    return buchungsart;
  }

  public boolean getOverride()
  {
    return ueberschr;
  }

  private SelectInput getBuchungsartAuswahl() throws RemoteException
  {
    if (buchungsarten != null)
    {
      return buchungsarten;
    }
    DBIterator it = Einstellungen.getDBService().createList(Buchungsart.class);
    it.setOrder("ORDER BY nummer");
    buchungsarten = new SelectInput(it, null);
    buchungsarten.setPleaseChoose("Bitte Buchungsart auswählen");
    buchungsarten.addListener(new Listener()
    {
      public void handleEvent(Event event)
      {
        status.setValue("");
      }
    });
    return buchungsarten;
  }

  private LabelInput getStatus() throws RemoteException
  {
    if (status != null)
    {
      return status;
    }
    status = new LabelInput("");
    return status;
  }

  private CheckboxInput getUeberschreiben() throws RemoteException
  {
    if (ueberschreiben != null)
    {
      return ueberschreiben;
    }
    ueberschreiben = new CheckboxInput(false);
    return ueberschreiben;
  }
}
