/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * jost@berlios.de
 * jverein.berlios.de
 * $Log$
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.JVereinPlugin;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.input.LabelInput;
import de.willuhn.jameica.gui.parts.FormTextPart;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.plugin.AbstractPlugin;
import de.willuhn.jameica.system.Application;

public class AboutView extends AbstractDialog
{

  public AboutView(int position)
  {
    super(position);
    this.setTitle("Über...");
  }

  protected void paint(Composite parent) throws Exception
  {
    FormTextPart text = new FormTextPart();
    text.setText("<form>"
        + "<p><b>Plugin für die Vereinsverwaltung unter Jameica</b></p>"
        + "<br/>Licence: GPL (http://www.gnu.org/copyleft/gpl.html)"
        + "<br/><p>Copyright by Heiner Jostkleigrewe [jverein@jost-net.de]</p>"
        + "<p>http://jverein.berlios.de</p>" + "</form>");

    text.paint(parent);

    LabelGroup group = new LabelGroup(parent, " Information ");

    AbstractPlugin p = Application.getPluginLoader().getPlugin(
        JVereinPlugin.class);

    group.addLabelPair("Version", new LabelInput(""
        + p.getManifest().getVersion()));
    group.addLabelPair("Arbeitsverzeichnis", new LabelInput(""
        + p.getResources().getWorkPath()));

  }

  protected Object getData() throws Exception
  {
    return null;
  }
}
