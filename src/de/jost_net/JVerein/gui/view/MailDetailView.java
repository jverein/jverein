/**********************************************************************
 * Copyright (c) by Heiner Jostkleigrewe
 * This program is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,  but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See 
 *  the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, 
 * see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de
 * www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import java.io.File;
import java.io.FileInputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.MailControl;
import de.jost_net.JVerein.gui.dialogs.MailEmpfaengerAuswahlDialog;
import de.jost_net.JVerein.gui.util.JameicaUtil;
import de.jost_net.JVerein.rmi.MailAnhang;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class MailDetailView extends AbstractView
{

  @Override
  public void bind() throws Exception
  {
    GUI.getView().setTitle("JVerein-Mail");

    final MailControl control = new MailControl(this);

    Composite comp = new Composite(this.getParent(), SWT.NONE);
    comp.setLayoutData(new GridData(GridData.FILL_BOTH));

    GridLayout layout = new GridLayout(2, false);
    comp.setLayout(layout);

    JameicaUtil.addLabel("Empfänger", comp, GridData.VERTICAL_ALIGN_BEGINNING);
    Composite comp2 = new Composite(comp, SWT.NONE);
    GridData gd2 = new GridData(GridData.FILL_HORIZONTAL);
    gd2.heightHint = 100;
    comp2.setLayoutData(gd2);
    GridLayout gl2 = new GridLayout();
    gl2.marginWidth = 0;
    comp2.setLayout(gl2);
    control.getEmpfaenger().paint(comp2);

    Composite comp3 = new Composite(comp, SWT.NONE);
    GridData gd3 = new GridData(GridData.HORIZONTAL_ALIGN_END);
    gd3.horizontalSpan = 2;
    comp3.setLayoutData(gd3);
    GridLayout gl3 = new GridLayout();
    gl3.marginWidth = 0;
    comp3.setLayout(gl3);
    Button add = new Button("Hinzufügen", new Action()
    {

      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        MailEmpfaengerAuswahlDialog mead = new MailEmpfaengerAuswahlDialog(
            control, MailEmpfaengerAuswahlDialog.POSITION_CENTER);
        try
        {
          mead.open();
        }
        catch (Exception e)
        {
          throw new ApplicationException(e.getMessage());
        }
      }
    });
    add.paint(comp3);

    JameicaUtil.addLabel("Betreff", comp, GridData.VERTICAL_ALIGN_CENTER);
    control.getBetreff().paint(comp);
    JameicaUtil.addLabel("Text", comp, GridData.VERTICAL_ALIGN_BEGINNING);
    control.getTxt().paint(comp);

    JameicaUtil.addLabel("", comp, GridData.VERTICAL_ALIGN_CENTER);
    LabelGroup group = new LabelGroup(comp, "Spendenquittung");
    group.addText("Achtung! - experimentell. Diese Funktion ermöglicht es, alle bereits erstellen Spendenquittungen (also in der Liste sichtbaren) innerhalb eines gewissen Datumbereichts automatisch an die E-Mail als PDF anzuhängen... theoretisch.", true);
    group.addLabelPair("", control.getSpendenquittung());
    group.addLabelPair("", control.getSpendenquittungIndividuell());
    group.addLabelPair("Von:", control.getSpendenquittungAnfang());
    group.addLabelPair("Bis:", control.getSpendenquittungEnde());
    
    JameicaUtil.addLabel("Anhang", comp, GridData.VERTICAL_ALIGN_BEGINNING);
    Composite comp4 = new Composite(comp, SWT.NONE);
    GridData gd4 = new GridData(GridData.FILL_HORIZONTAL);
    gd4.heightHint = 90;
    comp4.setLayoutData(gd4);
    GridLayout gl4 = new GridLayout();
    gl4.marginWidth = 0;
    comp4.setLayout(gl4);
    control.getAnhang().paint(comp4);

    Composite comp5 = new Composite(comp, SWT.NONE);
    GridData gd5 = new GridData(GridData.HORIZONTAL_ALIGN_END);
    gd5.horizontalSpan = 2;
    comp5.setLayoutData(gd3);
    GridLayout gl5 = new GridLayout();
    gl5.marginWidth = 0;
    comp5.setLayout(gl5);
    Button addAttachment = new Button("    " + "Anlage" + "    ", new Action()
    {

      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        Settings settings = new Settings(this.getClass());
        settings.setStoreWhenRead(true);
        FileDialog fd = new FileDialog(GUI.getShell(), SWT.OPEN);
        fd.setFilterPath(
            settings.getString("lastdir", System.getProperty("user.home")));
        fd.setText("Bitte wählen Sie einen Anhang aus.");
        String f = fd.open();
        if (f != null)
        {
          try
          {
            MailAnhang anh = (MailAnhang) Einstellungen.getDBService()
                .createObject(MailAnhang.class, null);
            anh.setDateiname(f.substring(
                f.lastIndexOf(System.getProperty("file.separator")) + 1));
            File file = new File(f);
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            fis.read(buffer);
            anh.setAnhang(buffer);
            control.addAnhang(anh);
            fis.close();
            settings.setAttribute("lastdir", file.getParent());
          }
          catch (Exception e)
          {
            Logger.error("", e);
            throw new ApplicationException(e);
          }
        }
      }
    });
    addAttachment.paint(comp5);

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.MAIL, false, "question-circle.png");
    buttons.addButton(control.getMailSpeichernButton());
    buttons.addButton(control.getMailReSendButton());
    buttons.addButton(control.getMailSendButton());
    buttons.paint(this.getParent());
  }
}
