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
 * Revision 1.10  2010-10-15 09:58:25  jost
 * Code aufgeräumt
 *
 * Revision 1.9  2010-10-07 19:49:24  jost
 * Hilfe in die View verlagert.
 *
 * Revision 1.8  2010-09-08 10:48:42  jost
 * E->D
 *
 * Revision 1.7  2010-03-16 19:26:05  jost
 * Hinweis auf das Forum aufgenommen.
 *
 * Revision 1.6  2009/06/11 21:03:39  jost
 * Vorbereitung I18N
 *
 * Revision 1.5  2009/05/31 12:27:27  jost
 * Typografische Änderung.
 *
 * Revision 1.4  2008/12/27 15:18:30  jost
 * ZusÃ¤tzliche Infos werden ausgegeben.
 *
 * Revision 1.3  2007/12/01 17:46:22  jost
 * Redaktionelle Ã„nderungen und Aufnahme der Datenbankversion
 *
 * Revision 1.2  2007/02/23 20:27:28  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.1  2006/09/20 15:39:10  jost
 * *** empty log message ***
 *
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
            "Plugin für die Vereinsverwaltung unter Jameica")
        + "</b></p>"
        + "<br/>Lizenz: GPL [ http://www.gnu.org/copyleft/gpl.html ]"
        + "<br/><p>Copyright by Heiner Jostkleigrewe [ heiner@jverein.de ]</p>"
        + "<p>web: http://www.jverein.de</p>"+
        "<p>Forum: http://www.jverein.de/forum</p></form>");

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
