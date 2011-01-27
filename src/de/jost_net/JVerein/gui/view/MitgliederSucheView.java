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

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.MitgliedDetailAction;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.rmi.JVereinDBService;
import de.jost_net.JVerein.server.DBSupportH2Impl;
import de.jost_net.JVerein.server.DBSupportMcKoiImpl;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.DialogInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.IntegerInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.util.ColumnLayout;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.gui.util.SimpleContainer;

public class MitgliederSucheView extends AbstractPersonenSucheView
{

  public String getTitle()
  {
    return "Mitglieder suchen";
  }

  public void getFilter(MitgliedControl control) throws RemoteException
  {
    LabelGroup group = new LabelGroup(getParent(), JVereinPlugin.getI18n().tr(
        "Filter"));
    ColumnLayout cl = new ColumnLayout(group.getComposite(), 3);

    SimpleContainer left = new SimpleContainer(cl.getComposite());
    Input mitglstat = control.getMitgliedStatus();
    mitglstat.addListener(new FilterListener(control));
    left.addLabelPair(JVereinPlugin.getI18n().tr("Mitgliedschaft"), mitglstat);
    IntegerInput suchexternemitgliedsnummer = control
        .getSuchExterneMitgliedsnummer();
    suchexternemitgliedsnummer.addListener(new FilterListener(control));
    if (Einstellungen.getEinstellung().getExterneMitgliedsnummer())
    {
      left.addLabelPair(JVereinPlugin.getI18n().tr("Externe Mitgliedsnummer"),
          control.getSuchExterneMitgliedsnummer());
    }
    if (!JVereinDBService.SETTINGS.getString("database.driver",
        DBSupportH2Impl.class.getName()).equals(
        DBSupportMcKoiImpl.class.getName()))
    {
      DialogInput mitgleigenschaften = control.getEigenschaftenAuswahl();
      mitgleigenschaften.addListener(new FilterListener(control));
      left.addLabelPair(JVereinPlugin.getI18n().tr("Eigenschaften"),
          mitgleigenschaften);
    }

    SelectInput mitglbeitragsgruppe = control.getBeitragsgruppeAusw();
    mitglbeitragsgruppe.addListener(new FilterListener(control));
    left.addLabelPair(JVereinPlugin.getI18n().tr("Beitragsgruppe"),
        mitglbeitragsgruppe);

    SimpleContainer middle = new SimpleContainer(cl.getComposite());
    DateInput mitglgebdatvon = control.getGeburtsdatumvon();
    mitglgebdatvon.addListener(new FilterListener(control));
    middle.addLabelPair(JVereinPlugin.getI18n().tr("Geburtsdatum von"),
        mitglgebdatvon);
    DateInput mitglgebdatbis = control.getGeburtsdatumbis();
    mitglgebdatbis.addListener(new FilterListener(control));
    middle.addLabelPair(JVereinPlugin.getI18n().tr("Geburtsdatum bis"),
        mitglgebdatbis);
    SelectInput mitglgeschlecht = control.getGeschlecht();
    mitglgeschlecht.setMandatory(false);
    mitglgeschlecht.addListener(new FilterListener(control));
    middle.addLabelPair(JVereinPlugin.getI18n().tr("Geschlecht"),
        mitglgeschlecht);

    SimpleContainer right = new SimpleContainer(cl.getComposite());
    DateInput mitglsterbedatvon = control.getSterbedatumvon();
    mitglsterbedatvon.addListener(new FilterListener(control));
    right.addLabelPair(JVereinPlugin.getI18n().tr("Sterbedatum von"),
        mitglsterbedatvon);
    DateInput mitglsterbedatbis = control.getSterbedatumbis();
    mitglsterbedatbis.addListener(new FilterListener(control));
    right.addLabelPair(JVereinPlugin.getI18n().tr("Sterbedatum bis"),
        mitglsterbedatbis);
  }

  public int getAdresstyp()
  {
    return 1;
  }

  public Action getDetailAction()
  {
    return new MitgliedDetailAction();
  }
}