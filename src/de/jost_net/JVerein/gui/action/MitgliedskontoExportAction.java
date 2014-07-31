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
package de.jost_net.JVerein.gui.action;

import java.rmi.RemoteException;

import de.jost_net.JVerein.gui.control.MitgliedskontoControl;
import de.jost_net.JVerein.gui.dialogs.ExportDialog;
import de.jost_net.JVerein.gui.view.DokumentationUtil;
import de.jost_net.JVerein.io.Exporter;
import de.jost_net.JVerein.io.IORegistry;
import de.jost_net.JVerein.io.MitgliedskontoExport;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.system.OperationCanceledException;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class MitgliedskontoExportAction implements Action
{
  private EXPORT_TYP exportTyp;

  private Mitglied selectedMitglied;

  public MitgliedskontoExportAction(EXPORT_TYP exportTyp, Object currentObject)
  {
    try
    {
      this.exportTyp = exportTyp;
      if (null != currentObject)
      {
        if (currentObject instanceof Mitglied)
        {
          selectedMitglied = (Mitglied) currentObject;
        }
        else if (currentObject instanceof Mitgliedskonto)
        {
          Mitgliedskonto konto = (Mitgliedskonto) currentObject;
          selectedMitglied = konto.getMitglied();
        }
      }
    }
    catch (RemoteException e)
    {
      Logger.error("Export kann nicht initialisiert werden", e);
    }
  }

  /**
   * @see de.willuhn.jameica.gui.Action#handleAction(java.lang.Object)
   */
  @Override
  public void handleAction(Object context) throws ApplicationException
  {
    try
    {
      initExporter();
      ExportDialog d = new ExportDialog(gibSuchGrenzen(context),
          Mitgliedskonto.class, DokumentationUtil.MITGLIEDSKONTO_UEBERSICHT);
      d.open();
    }
    catch (OperationCanceledException oce)
    {
      Logger.info(oce.getMessage());
      return;
    }
    catch (ApplicationException ae)
    {
      throw ae;
    }
    catch (Exception e)
    {
      Logger.error("Fehler", e);
      GUI.getStatusBar().setErrorText(

      "Fehler beim exportieren der Mitgliedskonten");
    }
  }

  /**
   * Der Exporter bekommt seine Instanz bereits ziemlich früh, deshalb suchen
   * wir hier unseren um den Typ zu setzen.
   */
  private void initExporter()
  {
    Exporter[] exporters = IORegistry.getExporters();
    for (Exporter export : exporters)
    {
      if (export instanceof MitgliedskontoExport)
      {
        MitgliedskontoExport mkexport = (MitgliedskontoExport) export;
        mkexport.setExportTyp(exportTyp);
      }
    }
  }

  private Object[] gibSuchGrenzen(Object context) throws ApplicationException
  {
    if (context instanceof MitgliedskontoControl)
    {
      MitgliedskontoControl control = (MitgliedskontoControl) context;
      return control.getCVSExportGrenzen(selectedMitglied);
    }
    throw new ApplicationException(
        "Dieser Export wurde aus dem falschen Context aufgerufen!");
  }

  public enum EXPORT_TYP
  {
    MITGLIEDSKONTO("Mitgliedskonten"), MAHNUNGEN("Mahnungen"), RECHNUNGEN(
        "Rechnungen");

    private final String titel;

    private EXPORT_TYP(String name)
    {
      this.titel = name;
    }

    public String getDateiName()
    {
      return titel.toLowerCase();
    }

    public String getTitel()
    {
      return titel;
    }
  }
}
