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
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.Variable.AllgemeineMap;
import de.jost_net.JVerein.Variable.MitgliedMap;
import de.jost_net.JVerein.Variable.MitgliedskontoMap;
import de.jost_net.JVerein.gui.control.MitgliedskontoControl;
import de.jost_net.JVerein.keys.Ausgabeart;
import de.jost_net.JVerein.keys.Ausgabesortierung;
import de.jost_net.JVerein.rmi.Formular;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.jost_net.JVerein.util.Dateiname;
import de.jost_net.JVerein.util.JVDateFormatJJJJMMTT;
import de.jost_net.JVerein.util.StringTool;
import de.willuhn.jameica.gui.GUI;

public abstract class AbstractMitgliedskontoDokument
{

  MitgliedskontoControl control;

  ArrayList<ArrayList<Mitgliedskonto>> mks = null;

  File file = null;

  FormularAufbereitung formularaufbereitung = null;

  ZipOutputStream zos = null;

  MitgliedskontoControl.TYP typ;

  public AbstractMitgliedskontoDokument(MitgliedskontoControl control,
      MitgliedskontoControl.TYP typ) throws IOException
  {
    this.control = control;
    this.typ = typ;
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
  }

  public void aufbereitung(Formular formular) throws IOException
  {
    for (ArrayList<Mitgliedskonto> mk : mks)
    {
      switch ((Ausgabeart) control.getAusgabeart().getValue())
      {
        case DRUCK:
          aufbereitenFormular(mk, formularaufbereitung, formular);
          break;
        case EMAIL:
          File f = File.createTempFile(getDateiname(mk), ".pdf");
          formularaufbereitung = new FormularAufbereitung(f);
          aufbereitenFormular(mk, formularaufbereitung, formular);
          formularaufbereitung.closeFormular();
          zos.putNextEntry(new ZipEntry(getDateiname(mk) + ".PDF"));
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
        new ZipMailer(file, (String) control.getBetreff(typ.name()).getValue(),
            (String) control.getTxt(typ.name()).getValue());
        break;
    }

  }

  File getDateiAuswahl(String extension) throws RemoteException
  {
    FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
    fd.setText("Ausgabedatei w‰hlen.");
    String path = control.getSettings().getString("lastdir",
        System.getProperty("user.home"));
    if (path != null && path.length() > 0)
    {
      fd.setFilterPath(path);
    }
    fd.setFileName(new Dateiname(typ.name(), "", Einstellungen.getEinstellung()
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

  /**
   * Liefert ein Array pro Mitglied mit Arrays der einzelnen Rechnungspositionen
   * 
   * @param currentObject
   */
  ArrayList<ArrayList<Mitgliedskonto>> getRechnungsempfaenger(
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

      Ausgabesortierung as = (Ausgabesortierung) control.getAusgabesortierung()
          .getValue();
      switch (as)
      {
        case NAME:
          // Sortiere nach Nachname und Vorname
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
          break;
        case PLZ:
          // Sortiere nach PLZ und Straﬂe
          Arrays.sort(mkn, new Comparator<Mitgliedskonto>()
          {

            @Override
            public int compare(Mitgliedskonto mk1, Mitgliedskonto mk2)
            {
              try
              {
                int c = mk1.getMitglied().getPlz()
                    .compareTo(mk2.getMitglied().getPlz());
                if (c != 0)
                {
                  return c;
                }
                c = mk1.getMitglied().getStrasse()
                    .compareTo(mk2.getMitglied().getStrasse());
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
          break;
      }

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

  void aufbereitenFormular(ArrayList<Mitgliedskonto> mk,
      FormularAufbereitung fa, Formular fo) throws RemoteException
  {
    Map<String, Object> map = new MitgliedskontoMap().getMap(mk, null);
    Mitglied m = mk.get(0).getMitglied();
    map = new MitgliedMap().getMap(m, map);
    map = new AllgemeineMap().getMap(map);
    fa.writeForm(fo, map);
  }

  String getDateiname(ArrayList<Mitgliedskonto> mk) throws RemoteException
  {
    Mitglied m = mk.get(0).getMitglied();
    String filename = m.getID() + "#"
        + new JVDateFormatJJJJMMTT().format(mk.get(0).getDatum()) + "#";
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
