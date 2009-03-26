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
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.action.ReportAction;
import de.jost_net.JVerein.gui.control.ReportControl;
import de.jost_net.JVerein.keys.Reportart;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.util.ApplicationException;

public class ReportListeView extends AbstractView
{
  public void bind() throws Exception
  {
    Reportart reportart = null;
    Object o = this.getCurrentObject();
    if (o != null && o instanceof Reportart)
    {
      reportart = (Reportart) o;
    }
    String header = "";
    if (reportart != null)
    {
      header = reportart.getText() + " - ";
    }
    header += "Reports";
    GUI.getView().setTitle(header);

    ReportControl control = new ReportControl(this);

    control.getReportList(reportart).paint(this.getParent());

    ButtonArea buttons = new ButtonArea(this.getParent(), 3);
    buttons.addButton(new Back(false));
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.REPORTS, false, "help-browser.png");
    buttons.addButton("neu", new ReportAction(), null, false,
        "document-new.png");
  }

  public void unbind() throws ApplicationException
  {
  }
}
