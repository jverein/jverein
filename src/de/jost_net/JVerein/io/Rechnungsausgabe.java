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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.Variable.AllgemeineMap;
import de.jost_net.JVerein.Variable.MitgliedskontoMap;
import de.jost_net.JVerein.gui.control.MitgliedskontoControl;
import de.jost_net.JVerein.keys.Ausgabeart;
import de.jost_net.JVerein.keys.FormularArt;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Formular;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.jost_net.JVerein.util.Dateiname;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.jost_net.JVerein.util.StringTool;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.GUI;

public class Rechnungsausgabe
{

  private MitgliedskontoControl control;

  public Rechnungsausgabe(MitgliedskontoControl control) throws IOException
  {
    File file = null;
    FormularAufbereitung formularaufbereitung = null;
    ZipOutputStream zos = null;
    this.control = control;
    switch ((Ausgabeart) control.getAusgabeart().getValue())
    {
      case DRUCK:
        file = getDateiAuswahl("PDF");
        formularaufbereitung = new FormularAufbereitung(file);
        break;
      case EMAIL:
        file = getDateiAuswahl("ZIP");
        zos = new ZipOutputStream(new FileOutputStream(file));
        break;
    }
    Formular form = (Formular) control.getFormular(FormularArt.RECHNUNG)
        .getValue();
    if (form == null)
    {
      throw new IOException("kein Rechnungsformular ausgewählt");
    }
    Formular formular = (Formular) Einstellungen.getDBService().createObject(
        Formular.class, form.getID());
    ArrayList<ArrayList<Mitgliedskonto>> mks = null;

    // Wurde ein Object übergeben?
    if (control.getCurrentObject() != null)
    {
      // Ja: Einzeldruck aus dem Kontextmenu
      mks = getRechnungsempfaenger(control.getCurrentObject());
    }
    else
    {
      // Nein: Sammeldruck aus der MitgliedskontoRechnungView
      DBIterator it = Einstellungen.getDBService().createList(
          Mitgliedskonto.class);
      Date d = null;
      if (control.getVondatum(control.getDatumverwendung()).getValue() != null)
      {
        d = (Date) control.getVondatum(control.getDatumverwendung()).getValue();
        if (d != null)
        {
          control.getSettings().setAttribute(
              control.getDatumverwendung() + "datumvon",
              new JVDateFormatTTMMJJJJ().format(d));
        }
        it.addFilter("datum >= ?", d);
      }
      else
      {
        control.getSettings().setAttribute(
            control.getDatumverwendung() + "datumvon", "");
      }
      if (control.getBisdatum(control.getDatumverwendung()).getValue() != null)
      {
        d = (Date) control.getBisdatum(control.getDatumverwendung()).getValue();
        if (d != null)
        {
          control.getSettings().setAttribute(
              control.getDatumverwendung() + "datumbis",
              new JVDateFormatTTMMJJJJ().format(d));
        }
        it.addFilter("datum <= ?", d);
      }
      else
      {
        control.getSettings().setAttribute(
            control.getDatumverwendung() + "datumbis", "");
      }
      if ((Boolean) control.getOhneAbbucher().getValue())
      {
        it.addFilter("zahlungsweg <> ?", Zahlungsweg.BASISLASTSCHRIFT);
      }

      Mitgliedskonto[] mk = new Mitgliedskonto[it.size()];
      int i = 0;
      while (it.hasNext())
      {
        mk[i] = (Mitgliedskonto) it.next();
        i++;
      }
      mks = getRechnungsempfaenger(mk);
    }
    for (ArrayList<Mitgliedskonto> mk : mks)
    {
      switch ((Ausgabeart) control.getAusgabeart().getValue())
      {
        case DRUCK:
          aufbereitenFormular(mk, formularaufbereitung, formular);
          break;
        case EMAIL:
          File f = File.createTempFile(getDateiname(mk.get(0).getMitglied()),
              ".pdf");
          formularaufbereitung = new FormularAufbereitung(f);
          aufbereitenFormular(mk, formularaufbereitung, formular);
          formularaufbereitung.closeFormular();
          zos.putNextEntry(new ZipEntry(getDateiname(mk.get(0).getMitglied())
              + ".PDF"));
          FileInputStream in = new FileInputStream(f);
          // buffer size
          byte[] b = new byte[1024];
          int count;
          while ((count = in.read(b)) > 0)
          {
            zos.write(b, 0, count);
          }
          in.close();
          break;
      }
    }
    switch ((Ausgabeart) control.getAusgabeart().getValue())
    {
      case DRUCK:
        formularaufbereitung.showFormular();
        break;
      case EMAIL:
        zos.close();
        new ZipMailer(file, (String) control.getBetreff().getValue(),
            (String) control.getTxt().getValue());
        break;
    }
  }

  private File getDateiAuswahl(String extension) throws RemoteException
  {
    FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
    fd.setText("Ausgabedatei wählen.");
    String path = control.getSettings().getString("lastdir",
        System.getProperty("user.home"));
    if (path != null && path.length() > 0)
    {
      fd.setFilterPath(path);
    }
    fd.setFileName(new Dateiname("rechnung", "", Einstellungen.getEinstellung()
        .getDateinamenmuster(), extension).get());
    fd.setFilterExtensions(new String[] { "*." + extension });

    String s = fd.open();
    if (s == null || s.length() == 0)
    {
      return null;
    }
    if (!s.endsWith("." + extension))
    {
      s = s + "." + extension;
    }
    final File file = new File(s);
    control.getSettings().setAttribute("lastdir", file.getParent());
    return file;
  }

  // private File getVerzeichnisAuswahl() throws RemoteException
  // {
  // DirectoryDialog dd = new DirectoryDialog(GUI.getShell(), SWT.SAVE);
  // dd.setText("Verzeichnis für den Mailversand wählen.");
  // String path = control.getSettings().getString("lastdir",
  // System.getProperty("user.home"));
  // if (path != null && path.length() > 0)
  // {
  // dd.setFilterPath(path);
  // }
  //
  // String s = dd.open();
  // if (s == null || s.length() == 0)
  // {
  // return null;
  // }
  // final File file = new File(s);
  // control.getSettings().setAttribute("lastdir", file.getParent());
  // return file;
  // }

  /**
   * Liefert ein Array pro Mitglied mit Arrays der einzelnen Rechnungspositionen
   * 
   * @param currentObject
   */
  private ArrayList<ArrayList<Mitgliedskonto>> getRechnungsempfaenger(
      Object currentObject)
  {
    ArrayList<ArrayList<Mitgliedskonto>> ret = new ArrayList<ArrayList<Mitgliedskonto>>();
    if (currentObject instanceof Mitgliedskonto)
    {
      Mitgliedskonto mk = (Mitgliedskonto) currentObject;
      ArrayList<Mitgliedskonto> r = new ArrayList<Mitgliedskonto>();
      r.add(mk);
      ret.add(r);
      return ret;
    }
    if (currentObject instanceof Mitgliedskonto[])
    {
      Mitgliedskonto[] mkn = (Mitgliedskonto[]) currentObject;
      Arrays.sort(mkn, new Comparator<Mitgliedskonto>()
      {

        @Override
        public int compare(Mitgliedskonto mk1, Mitgliedskonto mk2)
        {
          try
          {
            int c = mk1.getMitglied().getName()
                .compareTo(mk2.getMitglied().getName());
            if (c != 0)
            {
              return c;
            }
            c = mk1.getMitglied().getVorname()
                .compareTo(mk2.getMitglied().getVorname());
            if (c != 0)
            {
              return c;
            }
            return mk1.getMitglied().getID()
                .compareTo(mk2.getMitglied().getID());
          }
          catch (RemoteException e)
          {
            throw new RuntimeException(e);
          }
        }
      });
      try
      {
        ArrayList<Mitgliedskonto> r = new ArrayList<Mitgliedskonto>();
        r = new ArrayList<Mitgliedskonto>();
        for (Mitgliedskonto mk : mkn)
        {
          if (r.size() == 0
              || r.get(0).getMitglied().getID()
                  .equals(mk.getMitglied().getID()))
          {
            r.add(mk);
          }
          else
          {
            ret.add(r);
            r = new ArrayList<Mitgliedskonto>();
            r.add(mk);
          }
        }
        if (r.size() > 0)
        {
          ret.add(r);
        }
      }
      catch (RemoteException e)
      {
        throw new RuntimeException(e);
      }
    }
    return ret;
  }

  private void aufbereitenFormular(ArrayList<Mitgliedskonto> mk,
      FormularAufbereitung fa, Formular fo) throws RemoteException
  {
    Map<String, Object> map = new MitgliedskontoMap().getMap(mk, null);
    Mitglied m = mk.get(0).getMitglied();
    map = m.getMap(map);
    map = new AllgemeineMap().getMap(map);
    fa.writeForm(fo, map);
  }

  private String getDateiname(Mitglied m) throws RemoteException
  {
    String filename = m.getID() + "#";
    String email = StringTool.toNotNullString(m.getEmail());
    if (email.length() > 0)
    {
      filename += email;
    }
    else
    {
      filename += m.getName() + m.getVorname();
    }
    return filename;
  }
}
