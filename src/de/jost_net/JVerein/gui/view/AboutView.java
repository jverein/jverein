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
package de.jost_net.JVerein.gui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Version;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.input.LabelInput;
import de.willuhn.jameica.gui.parts.FormTextPart;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.gui.util.SWTUtil;
import de.willuhn.jameica.plugin.AbstractPlugin;
import de.willuhn.jameica.system.Application;

public class AboutView extends AbstractDialog
{

  public AboutView(int position)
  {
    super(position);
    this.setSize(335, 500);
    this.setTitle(JVereinPlugin.getI18n().tr("Über..."));
  }

  @Override
  protected void paint(Composite parent) throws Exception
  {
    Label l = GUI.getStyleFactory().createLabel(parent, SWT.BORDER);
    l.setImage(SWTUtil.getImage("JVerein.png"));

    FormTextPart text = new FormTextPart();
    text.setText("<form><p><b>"
        + JVereinPlugin.getI18n().tr(
            "Plugin für die Vereinsverwaltung unter Jameica") + "</b></p>"
        + "<br/>Lizenz: GPL [ http://www.gnu.org/copyleft/gpl.html ]"
        + "<br/><p>Copyright by Heiner Jostkleigrewe [ heiner@jverein.de ]</p>"
        + "<p>web: http://www.jverein.de</p>"
        + "<p>Forum: http://www.jverein.de/forum</p></form>");

    text.paint(parent);

    LabelGroup group = new LabelGroup(parent, JVereinPlugin.getI18n().tr(
        "Information"));

    AbstractPlugin p = Application.getPluginLoader().getPlugin(
        JVereinPlugin.class);

    group.addLabelPair(JVereinPlugin.getI18n().tr("Version"), new LabelInput(""
        + p.getManifest().getVersion()));

    group.addLabelPair(JVereinPlugin.getI18n().tr("Build-Date"),
        new LabelInput("" + p.getManifest().getBuildDate()));

    group.addLabelPair(JVereinPlugin.getI18n().tr("Build-Nr"), new LabelInput(
        "" + p.getManifest().getBuildnumber()));

    Version v = (Version) Einstellungen.getDBService().createObject(
        Version.class, "1");
    group.addLabelPair(JVereinPlugin.getI18n().tr("Datenbank-Version"),
        new LabelInput("" + v.getVersion()));
    group.addLabelPair(JVereinPlugin.getI18n().tr("Arbeitsverzeichnis"),
        new LabelInput("" + p.getResources().getWorkPath()));

  }

  @Override
  protected Object getData() throws Exception
  {
    return null;
  }

  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Über</span></p></form>";
  }
}
