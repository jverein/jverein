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
 **********************************************************************/

package de.jost_net.JVerein.gui.dialogs;

import java.rmi.RemoteException;
import java.util.Date;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.gui.view.DokumentationUtil;
import de.jost_net.JVerein.rmi.Auswertung;
import de.jost_net.JVerein.rmi.AuswertungPos;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.dialogs.SimpleDialog;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.system.Settings;
import de.willuhn.util.ApplicationException;

/**
 * Ein Dialog, ueber den man ein Mitgliedskonto auswaehlen kann.
 */
public class MitgliedAuswertungEinstellungenDialog extends AbstractDialog
{

  private Object choosen = null;

  private TextInput name;

  private MitgliedControl control;

  private Settings settings;

  private Auswertung ausw;

  public MitgliedAuswertungEinstellungenDialog(MitgliedControl control,
      Settings settings) throws RemoteException
  {
    super(AbstractDialog.POSITION_CENTER, true);
    this.settings = settings;
    this.setSize(600, 400);
    this.setTitle(JVereinPlugin.getI18n().tr(
        "Mitgliederauswertung - Einstellungen"));
    ausw = (Auswertung) Einstellungen.getDBService().createObject(
        Auswertung.class, settings.getString("einstellung.id", null));
    name = new TextInput(ausw.getBezeichnung(), 30);
    this.control = control;
  }

  @Override
  protected void paint(Composite parent) throws Exception
  {
    control.getAuswertungEinstellungenList().paint(parent);

    LabelGroup gr = new LabelGroup(parent, "Einstellungname");
    gr.addLabelPair("Name", name);
    ButtonArea b = new ButtonArea();
    Button bspeichern = new Button("Speichern", new Action()
    {

      public void handleAction(Object context) throws ApplicationException
      {
        try
        {
          store(ausw, control);
        }
        catch (RemoteException e)
        {
          throw new ApplicationException(e);
        }
        close();
      }
    }, null, true, "save.png");
    b.addButton(bspeichern);
    Button bspeichernunter = new Button("Speichern unter", new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        String n = (String) name.getValue();
        if (n == null || n.length() == 0)
        {
          outputError("Namen eingeben!");
          throw new ApplicationException("Name eingeben!");
        }
        try
        {
          DBIterator it = Einstellungen.getDBService().createList(
              Auswertung.class);
          it.addFilter("bezeichnung = ?", new Object[] { n });
          if (it.size() > 0)
          {
            outputError("Einstellung " + n + " existiert bereits!");
            throw new ApplicationException();
          }

          ausw = (Auswertung) Einstellungen.getDBService().createObject(
              Auswertung.class, null);
          ausw.setBezeichnung((String) name.getValue());
          ausw.store();
          settings.setAttribute("auswertung.name", ausw.getID());
          control.getAuswertungName().setValue(ausw.getBezeichnung());
          try
          {
            store(ausw, control);
          }
          catch (RemoteException e)
          {
            throw new ApplicationException(e);
          }

        }
        catch (RemoteException e)
        {
          throw new ApplicationException(e);
        }
        control.setAuswertungEinstellungenListSetNull();
        close();
      }
    }, null, true, "saveas.png");
    b.addButton(bspeichernunter);

    Button bladen = new Button("Laden", new Action()
    {
      public void handleAction(Object context)
      {
        try
        {
          Object obj = control.getAuswertungEinstellungenList().getSelection();
          ausw = (Auswertung) obj;
          DBIterator it = Einstellungen.getDBService().createList(
              AuswertungPos.class);
          it.addFilter("auswertung = ?", new Object[] { ausw.getID() });
          while (it.hasNext())
          {
            AuswertungPos pos = (AuswertungPos) it.next();
            if (pos.getFeld().equals(control.getMitgliedStatus().getName()))
            {
              control.getMitgliedStatus().setValue(pos.getZeichenfolge());
            }
            else if (pos.getFeld().equals(
                control.getEigenschaftenAuswahl().getName()))
            {
              control.getEigenschaftenAuswahl().setValue(pos.getZeichenfolge());
            }
            else if (pos.getFeld().equals(
                control.getGeburtsdatumvon().getName()))
            {
              control.getGeburtsdatumvon().setValue(pos.getDatum());
            }
            else if (pos.getFeld().equals(
                control.getGeburtsdatumbis().getName()))
            {
              control.getGeburtsdatumbis().setValue(pos.getDatum());
            }
            else if (pos.getFeld()
                .equals(control.getSterbedatumvon().getName()))
            {
              control.getSterbedatumvon().setValue(pos.getDatum());
            }
            else if (pos.getFeld()
                .equals(control.getSterbedatumbis().getName()))
            {
              control.getSterbedatumbis().setValue(pos.getDatum());
            }
            else if (pos.getFeld().equals(control.getGeschlecht().getName()))
            {
              control.getGeschlecht().setValue(pos.getZeichenfolge());
            }
            else if (pos.getFeld().equals(control.getOhneMail().getName()))
            {
              control.getOhneMail().setValue(pos.getJanein());
            }
            else if (pos.getFeld().equals(control.getEintrittvon().getName()))
            {
              control.getEintrittvon().setValue(pos.getDatum());
            }
            else if (pos.getFeld().equals(control.getEintrittbis().getName()))
            {
              control.getEintrittbis().setValue(pos.getDatum());
            }
            else if (pos.getFeld().equals(control.getAustrittvon().getName()))
            {
              control.getAustrittvon().setValue(pos.getDatum());
            }
            else if (pos.getFeld().equals(control.getAustrittvon().getName()))
            {
              control.getAustrittvon().setValue(pos.getDatum());
            }
            else if (pos.getFeld().equals(control.getAustrittbis().getName()))
            {
              control.getAustrittbis().setValue(pos.getDatum());
            }
            else if (pos.getFeld().equals(
                control.getBeitragsgruppeAusw().getName()))
            {
              control.getBeitragsgruppeAusw().setValue(pos.getZeichenfolge());
            }
            else if (pos.getFeld().equals(control.getAusgabe().getName()))
            {
              control.getAusgabe().setValue(pos.getZeichenfolge());
            }
            else if (pos.getFeld().equals(control.getSortierung().getName()))
            {
              control.getSortierung().setValue(pos.getZeichenfolge());
            }
            else if (pos.getFeld().equals(
                control.getAuswertungUeberschrift().getName()))
            {
              control.getAuswertungUeberschrift().setValue(
                  pos.getZeichenfolge());
            }
          }
        }
        catch (RemoteException e)
        {
          e.printStackTrace();
        }
        catch (NullPointerException e)
        {
          e.printStackTrace();
        }
        control.setAuswertungEinstellungenListSetNull();
        close();
      }
    }, null, true, "document-open.png");
    b.addButton(bladen);

    b.addButton(JVereinPlugin.getI18n().tr("Hilfe"), new DokumentationAction(),
        DokumentationUtil.MITGLIEDSKONTO_AUSWAHL, false, "help-browser.png");

    b.addButton(i18n.tr("abbrechen"), new Action()
    {

      public void handleAction(Object context)
      {
        control.setAuswertungEinstellungenListSetNull();
        close();
      }
    }, null, false, "process-stop.png");
    b.paint(parent);

    if (settings.getString("auswertung.name", null) == null)
    {
      bspeichern.setEnabled(false);
      bspeichernunter.setEnabled(true);
    }
    else
    {
      bspeichern.setEnabled(true);
      bspeichernunter.setEnabled(true);
    }
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

  private void outputError(String text)
  {
    SimpleDialog d = new SimpleDialog(SimpleDialog.POSITION_CENTER);
    d.setSize(300, 150);
    d.setPanelText("Auswertung - Einstellungen");
    d.setText(text);
    try
    {
      d.open();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private void store(Auswertung auswertung, MitgliedControl control)
      throws RemoteException, ApplicationException
  {
    storeRecord(auswertung, control.getMitgliedStatus().getName(),
        (String) control.getMitgliedStatus().getValue());
    storeRecord(auswertung, control.getEigenschaftenAuswahl().getName(),
        (String) control.getEigenschaftenAuswahl().getValue());
    storeRecord(auswertung, control.getGeburtsdatumvon().getName(),
        (Date) control.getGeburtsdatumvon().getValue());
    storeRecord(auswertung, control.getGeburtsdatumbis().getName(),
        (Date) control.getGeburtsdatumbis().getValue());
    storeRecord(auswertung, control.getSterbedatumvon().getName(),
        (Date) control.getSterbedatumvon().getValue());
    storeRecord(auswertung, control.getSterbedatumbis().getName(),
        (Date) control.getSterbedatumbis().getValue());
    storeRecord(auswertung, control.getGeschlecht().getName(), (String) control
        .getGeschlecht().getValue());
    storeRecord(auswertung, control.getOhneMail().getName(), (Boolean) control
        .getOhneMail().getValue());
    storeRecord(auswertung, control.getEintrittvon().getName(), (Date) control
        .getEintrittvon().getValue());
    storeRecord(auswertung, control.getEintrittbis().getName(), (Date) control
        .getEintrittbis().getValue());
    storeRecord(auswertung, control.getAustrittvon().getName(), (Date) control
        .getAustrittvon().getValue());
    storeRecord(auswertung, control.getAustrittbis().getName(), (Date) control
        .getAustrittbis().getValue());
    storeRecord(auswertung, control.getBeitragsgruppeAusw().getName(),
        (String) control.getBeitragsgruppeAusw().getValue());
    storeRecord(auswertung, control.getAusgabe().getName(), (String) control
        .getAusgabe().getValue());
    storeRecord(auswertung, control.getSortierung().getName(), (String) control
        .getSortierung().getValue());
    storeRecord(auswertung, control.getAuswertungUeberschrift().getName(),
        (String) control.getAuswertungUeberschrift().getValue());
  }

  private void storeRecord(Auswertung auswertung, String feld, Object value)
      throws RemoteException, ApplicationException
  {
    DBIterator it = Einstellungen.getDBService()
        .createList(AuswertungPos.class);
    it.addFilter("auswertung = ? and feld = ?",
        new Object[] { auswertung.getID(), feld });
    AuswertungPos pos = null;
    if (it.size() > 0)
    {
      pos = (AuswertungPos) it.next();
    }
    else
    {
      pos = (AuswertungPos) Einstellungen.getDBService().createObject(
          AuswertungPos.class, null);
      pos.setAuswertung(auswertung);
      pos.setFeld(feld);
    }
    pos.setZeichenfolge(null);
    pos.setDatum(null);
    pos.setGanzzahl(null);
    pos.setJanein(false);
    if (value instanceof String)
    {
      pos.setZeichenfolge((String) value);
    }
    else if (value instanceof Date)
    {
      pos.setDatum((Date) value);
    }
    else if (value instanceof Integer)
    {
      pos.setGanzzahl((Integer) value);
    }
    else if (value instanceof Boolean)
    {
      pos.setJanein((Boolean) value);
    }
    pos.store();
  }
}
