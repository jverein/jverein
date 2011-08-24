/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe 
 * basiert auf dem KontoAuswahlDialog aus Hibiscus
 * All rights reserved
 * heiner@jverein.de
 * www.jverein.de
 * $Log$
 * Revision 1.9  2011-06-12 07:08:24  jost
 * Spezialsuche bei Namen mit Namensvorsätzen (von, di, de ...)
 *
 * Revision 1.8  2011-05-05 19:50:47  jost
 * Bugfix Scrollbar
 *
 * Revision 1.7  2011-02-26 15:54:19  jost
 * Bugfix Mitgliedskontoauswahl bei neuer Buchung, mehrfacher Mitgliedskontoauswahl
 *
 * Revision 1.6  2011-01-08 10:45:18  jost
 * Erzeugung Sollbuchung bei Zuordnung des Mitgliedskontos
 *
 * Revision 1.5  2010-10-15 09:58:26  jost
 * Code aufgeräumt
 *
 * Revision 1.4  2010-09-12 19:59:25  jost
 * Mitgliedskontoauswahl kann rückgängig gemacht werden.
 *
 * Revision 1.3  2010-08-16 20:16:58  jost
 * Neu: Mahnung
 *
 * Revision 1.2  2010-08-08 11:32:29  jost
 * Nicht-Case-Sensitive-Suche
 *
 * Revision 1.1  2010-07-25 18:32:23  jost
 * Neu: Mitgliedskonto
 *
 **********************************************************************/

package de.jost_net.JVerein.gui.dialogs;

import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.BuchungsControl;
import de.jost_net.JVerein.gui.control.MitgliedskontoControl;
import de.jost_net.JVerein.gui.view.DokumentationUtil;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.gui.util.Container;
import de.willuhn.jameica.gui.util.SimpleContainer;
import de.willuhn.jameica.gui.util.TabGroup;

/**
 * Ein Dialog, ueber den man ein Mitgliedskonto auswaehlen kann.
 */
public class MitgliedskontoAuswahlDialog extends AbstractDialog
{

  private de.willuhn.jameica.system.Settings settings;

  private String text = null;

  private Object choosen = null;

  private MitgliedskontoControl control;

  private TablePart mitgliedskontolist = null;

  private TablePart mitgliedlist = null;

  private BuchungsControl buchung;

  public MitgliedskontoAuswahlDialog(int position, BuchungsControl buchung)
  {
    super(position, true);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);

    this.setSize(600, 400);
    this.setTitle(JVereinPlugin.getI18n().tr("Mitgliedskonto-Auswahl"));
    this.buchung = buchung;
    control = new MitgliedskontoControl(null);
  }

  @Override
  protected void paint(Composite parent) throws Exception
  {
    TabFolder folder = new TabFolder(parent, SWT.NONE);
    folder.setLayoutData(new GridData(GridData.FILL_BOTH));

    TabGroup tabNurIst = new TabGroup(folder, "nur Ist", false, 1);
    Container grNurIst = new SimpleContainer(tabNurIst.getComposite());
    grNurIst.addHeadline(JVereinPlugin.getI18n().tr(
        "Auswahl des Mitgliedskontos"));
    if (text == null || text.length() == 0)
    {
      text = JVereinPlugin.getI18n().tr(
          "Bitte wählen Sie das gewünschte Mitgliedskonto aus.");
    }
    grNurIst.addText(text, true);
    control.getSuchName().setValue(buchung.getName());
    grNurIst.addLabelPair("Name", control.getSuchName());
    grNurIst.addLabelPair("Differenz", control.getDifferenz("Fehlbetrag"));
    Action action = new Action()
    {

      public void handleAction(Object context)
      {
        if (context == null || !(context instanceof Mitgliedskonto))
        {
          return;
        }
        choosen = context;
        close();
      }
    };
    mitgliedskontolist = control.getMitgliedskontoList(action, null);
    mitgliedskontolist.paint(tabNurIst.getComposite());

    //
    TabGroup tabSollIst = new TabGroup(folder, "Soll u. Ist", true, 1);
    Container grSollIst = new SimpleContainer(tabSollIst.getComposite());
    grSollIst.addHeadline(JVereinPlugin.getI18n().tr(
        "Auswahl des Mitgliedskontos"));

    if (text == null || text.length() == 0)
    {
      text = JVereinPlugin.getI18n().tr(
          "Bitte wählen Sie das gewünschte Mitgliedskonto aus.");
    }
    grSollIst.addText(text, true);
    control.getSuchName2(true).setValue(buchung.getName().getValue());
    grSollIst.addLabelPair("Name", control.getSuchName2(false));
    grSollIst.addInput(control.getSpezialSuche());

    final Action action2 = new Action()
    {
      public void handleAction(Object context)
      {
        if (context == null || !(context instanceof Mitglied))
        {
          return;
        }
        choosen = context;
        close();
      }
    };
    mitgliedlist = control.getMitgliedskontoList2(action2, null);
    mitgliedlist.paint(tabSollIst.getComposite());

    ButtonArea b = new ButtonArea();

    b.addButton(i18n.tr(JVereinPlugin.getI18n().tr("übernehmen")), new Action()
    {
      public void handleAction(Object context)
      {
        Object o = mitgliedskontolist.getSelection();

        if (o instanceof Mitgliedskonto)
        {
          choosen = o;
          close();
        }
        else
        {
          o = mitgliedlist.getSelection();

          if (o instanceof Mitglied)
          {
            choosen = o;
            close();
          }
        }
        return;
      }
    }, null, true, "emblem-default.png");

    b.addButton(i18n.tr(JVereinPlugin.getI18n().tr("entfernen")), new Action()
    {

      public void handleAction(Object context)
      {
        choosen = null;
        close();
      }
    }, null, true, "edit-undo.png");
    b.addButton(JVereinPlugin.getI18n().tr("Hilfe"), new DokumentationAction(),
        DokumentationUtil.MITGLIEDSKONTO_AUSWAHL, false, "help-browser.png");

    b.addButton(i18n.tr("abbrechen"), new Action()
    {

      public void handleAction(Object context)
      {
        throw new OperationCanceledException();
      }
    }, null, false, "process-stop.png");
    b.paint(parent);
  }

  /**
   * Liefert das ausgewaehlte Mitgliedskonto zurueck oder <code>null</code> wenn
   * der Abbrechen-Knopf gedrueckt wurde.
   * 
   * @see de.willuhn.jameica.gui.dialogs.AbstractDialog#getData()
   */
  @Override
  protected Object getData() throws Exception
  {
    return choosen;
  }

  /**
   * Optionale Angabe des anzuzeigenden Textes. Wird hier kein Wert gesetzt,
   * wird ein Standard-Text angezeigt.
   * 
   * @param text
   */
  public void setText(String text)
  {
    this.text = text;
  }
}
