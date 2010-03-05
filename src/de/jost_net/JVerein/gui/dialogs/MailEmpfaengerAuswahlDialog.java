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
 * Revision 1.1  2010/02/01 20:59:26  jost
 * Neu: Einfache Mailfunktion
 *
 **********************************************************************/

package de.jost_net.JVerein.gui.dialogs;

import java.rmi.RemoteException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.control.MailControl;
import de.jost_net.JVerein.rmi.MailEmpfaenger;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.Color;
import de.willuhn.jameica.gui.util.TabGroup;
import de.willuhn.jameica.system.OperationCanceledException;
import de.willuhn.util.ApplicationException;

/**
 * Ein Dialog, ueber den man Empfänger für eine Mail auswählen kann.
 */
public class MailEmpfaengerAuswahlDialog extends AbstractDialog
{
  private MailControl control;

  public MailEmpfaengerAuswahlDialog(MailControl control, int position)
  {
    super(position);
    this.control = control;
    setTitle(JVereinPlugin.getI18n().tr("Mail-Empfänger"));
    setSize(550, 450);
  }

  protected void paint(Composite parent) throws Exception
  {
    TabFolder folder = new TabFolder(parent, SWT.NONE);
    folder.setLayoutData(new GridData(GridData.FILL_BOTH));
    folder.setBackground(Color.BACKGROUND.getSWTColor());

    TabGroup tab1 = new TabGroup(folder, JVereinPlugin.getI18n().tr(
        "Mitglieder"));
    control.getMitgliedMitMail().paint(tab1.getComposite());
    // TabGroup tab2 = new TabGroup(folder, JVereinPlugin.getI18n().tr(
    // "Eigenschaften"));
    // tab2
    // .addText(
    // "hier können demnächst die Mitglieder nach Eigenschaften ausgewählt werden. Wird noch implementiert.",
    // true);

    ButtonArea b = new ButtonArea(parent, 2);
    b.addButton(JVereinPlugin.getI18n().tr("übernehmen"), new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        try
        {
          for (Object o : control.getMitgliedMitMail().getItems())
          {
            Mitglied m = (Mitglied) o;
            MailEmpfaenger me = (MailEmpfaenger) Einstellungen.getDBService()
                .createObject(MailEmpfaenger.class, null);
            me.setMitglied(m);
            control.addEmpfaenger(me);
          }
        }
        catch (RemoteException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        {

        }
        close();
      }
    });
    b.addButton(JVereinPlugin.getI18n().tr("abbrechen"), new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        throw new OperationCanceledException();
      }
    });
  }

  protected Object getData() throws Exception
  {
    return null;
  }

}
