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

package de.jost_net.JVerein.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;

import com.itextpdf.text.DocumentException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.server.MitgliedUtils;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.logging.Logger;
import de.willuhn.util.ProgressMonitor;

public abstract class MitgliedschaftsjubilaeumsExport implements Exporter
{

  @Override
  public abstract String getName();

  @Override
  public abstract IOFormat[] getIOFormats(Class<?> objectType);

  protected File file;

  protected int jahr;

  protected int jubilarStartAlter;

  @Override
  public void doExport(final Object[] objects, IOFormat format, File file,
      ProgressMonitor monitor) throws DocumentException, IOException
  {
    this.file = file;
    setzeParameterDerListe(objects);
    DBIterator<Mitglied> mitgliederListe = ladeMitgliederAktivImGewaehltenJahr();

    JubilaeenParser jp = holeJubelJahreAusEinstellungen();

    open();
    while (jp.hasNext())
    {
      int jubi = jp.getNext();

      startJahrgang(jubi);
      sucheMitgliederFuerJahrgang(jubi, mitgliederListe);
      endeJahrgang();

    }
    close();
  }

  private void sucheMitgliederFuerJahrgang(int jubi,
      DBIterator<Mitglied> mitgliederListe) throws RemoteException
  {
    mitgliederListe.begin();
    while (mitgliederListe.hasNext())
    {
      Mitglied mitglied = (Mitglied) mitgliederListe.next();
      if (hatMitgliedJubileum(jubi, mitglied))
      {
        add(mitglied);
      }
    }
  }

  private boolean hatMitgliedJubileum(final int jubi, Mitglied mitglied)
      throws RemoteException
  {
    JubelHelfer jubelHelfer = new JubelHelfer(mitglied);
    return jubelHelfer.hatMitgliedJubileum(jubi);
  }

  private DBIterator<Mitglied> ladeMitgliederAktivImGewaehltenJahr()
      throws RemoteException
  {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, jahr);
    cal.set(Calendar.MONTH, Calendar.DECEMBER);
    cal.set(Calendar.DAY_OF_MONTH, 31);

    DBIterator<Mitglied> mitgliederListe = Einstellungen.getDBService()
        .createList(Mitglied.class);
    MitgliedUtils.setNurAktive(mitgliederListe, cal.getTime());
    MitgliedUtils.setMitglied(mitgliederListe);
    mitgliederListe.setOrder("order by eintritt");
    return mitgliederListe;
  }

  private JubilaeenParser holeJubelJahreAusEinstellungen()
      throws RemoteException
  {
    String jubilarListe = Einstellungen.getEinstellung().getJubilaeen();
    JubilaeenParser jp = new JubilaeenParser(jubilarListe);
    return jp;
  }

  /**
   * Ermittle die Kenndaten die für die Ermittlung der Liste wichtig sind. 1.
   * jahr - für welches Jahr soll die Liste erstellt werden 2. jubilarStartAlter
   * - ab welchem Alter beginnt die Zählung der Jubeljahre
   * 
   * @param objects
   * @throws RemoteException
   */
  private void setzeParameterDerListe(final Object[] objects)
      throws RemoteException
  {
    MitgliedControl control = (MitgliedControl) objects[0];
    jahr = control.getJJahr();
    jubilarStartAlter = Einstellungen.getEinstellung().getJubilarStartAlter();
    Logger.debug("Mitgliedschaftsjubiläum, Jahr=" + Integer.toString(jahr)
        + " StartAlter= " + Integer.toString(jubilarStartAlter));
  }

  @Override
  public String getDateiname()
  {
    return "mitgliedschaftsjubilare";
  }

  protected abstract void startJahrgang(int jahr) throws DocumentException;

  protected abstract void endeJahrgang() throws DocumentException;

  protected abstract void open()
      throws DocumentException, FileNotFoundException;

  protected abstract void add(Mitglied m) throws RemoteException;

  protected abstract void close() throws IOException, DocumentException;

  /**
   * Innerclass hilft beim Ermitteln ob das Mitglied das geforderte Jubilaeum im
   * Jahr imJahr hat. In den Einstellungen kann ein Mindestalter festgelegt
   * werden ab dem die Mitgliedschaft zu Jubilaeum zählt. Dieses wird hier
   * berücksichtigt.
   * 
   * @author Rolf
   */
  private class JubelHelfer
  {

    private Mitglied mitglied;

    private int eintrittsJahr;

    private int geburtsJahr;

    private int alterBeiEintritt;

    private int jahreImVerein;

    private JubelHelfer(Mitglied mitglied)
    {
      this.mitglied = mitglied;
    }

    public boolean hatMitgliedJubileum(final int jubilaeum)
        throws RemoteException
    {
      ermittlenEintrittsJahr();
      ermittlenGeburtsJahr();
      ermittlenAlterBeiEintritt();
      passeEintrittsJahrAnWennZuJung();
      ermittlenZaehlbareJahreImVerein();
      return (jahreImVerein == jubilaeum);
    }

    private void passeEintrittsJahrAnWennZuJung()
    {
      if (jubilarStartAlter < 1)
        return;
      if (alterBeiEintritt >= jubilarStartAlter)
        return;
      int jahreDieNichtZaehlen = jubilarStartAlter - alterBeiEintritt;
      eintrittsJahr += jahreDieNichtZaehlen;
    }

    private void ermittlenZaehlbareJahreImVerein()
    {
      jahreImVerein = jahr - eintrittsJahr;
    }

    private void ermittlenEintrittsJahr() throws RemoteException
    {
      eintrittsJahr = gibJahrVonDatum(mitglied.getEintritt());
    }

    private void ermittlenGeburtsJahr() throws RemoteException
    {
      geburtsJahr = gibJahrVonDatum(mitglied.getGeburtsdatum());
    }

    private void ermittlenAlterBeiEintritt()
    {
      alterBeiEintritt = eintrittsJahr - geburtsJahr;
    }

    private int gibJahrVonDatum(Date datum)
    {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(datum);
      return calendar.get(Calendar.YEAR);
    }

  }
}
