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

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.gui.control.ReportControl;
import de.jost_net.JVerein.keys.Reportart;
import de.jost_net.JVerein.rmi.Konto;
import de.jost_net.JVerein.rmi.Report;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.system.OperationCanceledException;
import de.willuhn.util.ApplicationException;

/**
 * Ein Dialog, über den man einen Report auswählen kann.
 */
public class ReportAuswahlDialog extends AbstractDialog
{
  private Report choosen = null;

  private Reportart art;

  ReportControl control;

  public ReportAuswahlDialog(int position, Reportart art)
  {
    super(position);
    this.setTitle("Report-Auswahl");
    this.art = art;
    control = new ReportControl(null);
  }

  protected void paint(Composite parent) throws Exception
  {
    LabelGroup group = new LabelGroup(parent, "Report");

    group.addText("wählen Sie den Report aus", true);

    new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        if (context == null || !(context instanceof Konto))
        {
          return;
        }
        choosen = (Report) context;
        close();
      }
    };
    final TablePart reports = control.getReportList(art);
    reports.setContextMenu(null);
    reports.setMulti(false);
    reports.setSummary(false);
    reports.paint(parent);

    ButtonArea b = new ButtonArea(parent, 2);
    b.addButton("Übernehmen", new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        Object o = reports.getSelection();
        if (o == null || !(o instanceof Report))
        {
          return;
        }
        choosen = (Report) o;
        close();
      }
    });
    b.addButton("Abbrechen", new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        throw new OperationCanceledException();
      }
    });
  }

  /**
   * Liefert den ausgewaehlten Report zurück oder <code>null</code> wenn der
   * Abbrechen-Knopf gedrueckt wurde.
   */
  protected Object getData() throws Exception
  {
    return choosen;
  }

}
