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
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.keys.ArtBeitragsart;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.MitgliedNextBGruppe;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * @author Rolf Mamat
 */
public class MitgliedNextBGruppeControl extends AbstractControl
{
  private SelectInput beitragsgruppe;

  private DateInput abDatum;

  private TextInput bemerkung;

  private TextInput mitgliedsName;

  private TextInput beitragsGruppeAktuell;

  private MitgliedNextBGruppe mitgliednextbgruppe;

  public MitgliedNextBGruppeControl(AbstractView view)
  {
    super(view);
  }

  private MitgliedNextBGruppe getMitgliedNextBGruppe() throws RemoteException
  {
    if (mitgliednextbgruppe != null)
    {
      return mitgliednextbgruppe;
    }
    if (getCurrentObject() instanceof Mitglied)
    {
      mitgliednextbgruppe = (MitgliedNextBGruppe) Einstellungen.getDBService()
          .createObject(MitgliedNextBGruppe.class, null);
      return mitgliednextbgruppe;
    }
    mitgliednextbgruppe = (MitgliedNextBGruppe) getCurrentObject();
    return mitgliednextbgruppe;
  }

  public TextInput getBemerkungsInput() throws RemoteException
  {
    if (bemerkung == null)
    {
      bemerkung = new TextInput(getMitgliedNextBGruppe().getBemerkung(), 30);
    }
    return bemerkung;
  }

  public TextInput getBeitragsgruppeAktuellInput() throws RemoteException
  {
    if (null == beitragsGruppeAktuell)
    {
      beitragsGruppeAktuell = new TextInput("", 30);
      beitragsGruppeAktuell.disable();
      beitragsGruppeAktuell.setComment("");
    }
    beitragsGruppeAktuell.setValue(getAktuelleBeitragsgruppe());
    return beitragsGruppeAktuell;
  }

  private String getAktuelleBeitragsgruppe() throws RemoteException
  {
    Mitglied mitglied = getMitglied();
    if (null == mitglied)
    {
      return null;
    }
    Beitragsgruppe beitragsGruppe = mitglied.getBeitragsgruppe();
    if (null == beitragsGruppe)
    {
      return null;
    }
    return beitragsGruppe.getBezeichnung();
  }

  public TextInput getMitgliedsnameInput() throws RemoteException
  {
    if (null == mitgliedsName)
    {
      mitgliedsName = new TextInput("", 30);
      mitgliedsName.disable();
      mitgliedsName.setComment("");
    }
    Mitglied mitglied = getMitglied();
    if (null != mitglied)
      mitgliedsName.setValue(mitglied.getAttribute(mitglied
          .getPrimaryAttribute()));
    return mitgliedsName;
  }

  private Mitglied getMitglied() throws RemoteException
  {
    Mitglied mitglied = null;
    if (this.view.getCurrentObject() instanceof Mitglied)
    {
      mitglied = (Mitglied) this.view.getCurrentObject();
    }
    if (this.view.getCurrentObject() instanceof MitgliedNextBGruppe)
    {
      MitgliedNextBGruppe mnb = (MitgliedNextBGruppe) this.view
          .getCurrentObject();
      mitglied = mnb.getMitglied();
    }
    return mitglied;
  }

  public DateInput getAbDatumInput() throws RemoteException
  {
    if (abDatum != null)
    {
      return abDatum;
    }

    this.abDatum = new DateInput(getMitgliedNextBGruppe().getAbDatum(),
        new JVDateFormatTTMMJJJJ());
    this.abDatum.setComment("Wann soll Beitragsgruppe aktiviert werden?");
    this.abDatum.setMandatory(true);
    return abDatum;
  }

  private boolean isFamilienMitglied() throws RemoteException
  {
    Mitglied mitglied = getMitglied();
    Beitragsgruppe beitragsGruppe = mitglied.getBeitragsgruppe();
    return (beitragsGruppe.getBeitragsArt() == ArtBeitragsart.FAMILIE_ZAHLER);
  }

  public Input getBeitragsgruppeInput() throws RemoteException
  {
    if (beitragsgruppe != null)
    {
      return beitragsgruppe;
    }
    DBIterator list = Einstellungen.getDBService().createList(
        Beitragsgruppe.class);
    list.setOrder("ORDER BY bezeichnung");
    if (isFamilienMitglied())
    {
      list.addFilter("beitragsart is not null");
      list.addFilter("beitragsart = ?", ArtBeitragsart.FAMILIE_ZAHLER.getKey());
    }
    else
    {
      list.addFilter("beitragsart is null or beitragsart not in (?,?)",
          ArtBeitragsart.FAMILIE_ZAHLER.getKey(),
          ArtBeitragsart.FAMILIE_ANGEHOERIGER.getKey());
    }
    beitragsgruppe = new SelectInput(list, getMitgliedNextBGruppe()
        .getBeitragsgruppe());
    beitragsgruppe.setName("Beitragsgruppe");
    beitragsgruppe.setMandatory(true);
    beitragsgruppe.setAttribute("bezeichnung");
    beitragsgruppe.setPleaseChoose("Bitte auswählen");
    beitragsgruppe.setComment("");
    return beitragsgruppe;
  }

  public Action getSpeichernAction()
  {
    return new Action()
    {
      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        try
        {
          speichernBeitragsGruppe();
        }
        catch (RemoteException ex)
        {
          Logger.error("Beitragsgruppe kann nicht gespeichert werden", ex);
          throw new ApplicationException(
              "Fehler beim Speichern der Beitragsgruppe", ex);
        }
      }
    };
  }

  private void speichernBeitragsGruppe() throws RemoteException,
      ApplicationException
  {
    MitgliedNextBGruppe mitgliedsBeitragsgruppe = getMitgliedNextBGruppe();
    mitgliedsBeitragsgruppe.setMitglied(getMitglied());
    mitgliedsBeitragsgruppe.setBemerkung((String) getBemerkungsInput()
        .getValue());
    mitgliedsBeitragsgruppe.setAbDatum((Date) getAbDatumInput().getValue());
    mitgliedsBeitragsgruppe.setBeitragsgruppe((Beitragsgruppe) beitragsgruppe
        .getValue());
    mitgliedsBeitragsgruppe.store();
    GUI.getStatusBar().setSuccessText("Beitragsgruppe gespeichert!!");
  }

}
