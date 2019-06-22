/**********************************************************************
 * JVerein - Mitgliederverwaltung und einfache Buchhaltung für Vereine
 * Copyright (c) by Heiner Jostkleigrewe
 * Copyright (c) 2014 by Thomas Hooge
 * Main Project: heiner@jverein.dem  http://www.jverein.de/
 * Module Author: thomas@hoogi.de, http://www.hoogi.de/
 *
 * This file is part of JVerein.
 *
 * JVerein is free software: you can redistribute it and/or modify 
 * it under the terms of the  GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JVerein is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import com.itextpdf.text.DocumentException;

import de.jost_net.JVerein.rmi.Formularfeld;

public class FormularfelderExportCSV extends FormularfelderExport
{

  private ICsvMapWriter writer;

  private String[] header;

  private ArrayList<Formularfeld> formularfelder;

  @Override
  public String getName()
  {
    return "Formularfelder CSV-Export";
  }

  @Override
  public IOFormat[] getIOFormats(Class<?> objectType)
  {
    if (objectType != Formularfeld.class)
    {
      return null;
    }
    IOFormat f = new IOFormat()
    {

      @Override
      public String getName()
      {
        return FormularfelderExportCSV.this.getName();
      }

      /**
       * @see de.willuhn.jameica.hbci.io.IOFormat#getFileExtensions()
       */
      @Override
      public String[] getFileExtensions()
      {
        return new String[] { "*.csv" };
      }
    };
    return new IOFormat[] { f };
  }

  @Override
  public String getDateiname()
  {
    return "formularfelder";
  }

  @Override
  protected void add(Formularfeld ff)
  {
    formularfelder.add(ff);
  }

  @Override
  protected void open() throws DocumentException
  {
    formularfelder = new ArrayList<>();
    try
    {
      writer = new CsvMapWriter(new FileWriter(file),
          CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
      header = new String[] { "Name", "Seite", "X", "Y", "Font", "Fontsize",
          "Fontstyle" };
      writer.writeHeader(header);
    }
    catch (IOException e)
    {
      throw new DocumentException(e);
    }
  }

  @Override
  protected void close() throws IOException, DocumentException
  {
    Map<String, Object> ffmap = new HashMap<>();
    for (Formularfeld ff : formularfelder)
    {
      ffmap.put("Name", ff.getName());
      ffmap.put("Seite", ff.getSeite());
      ffmap.put("X", ff.getX());
      ffmap.put("Y", ff.getY());
      ffmap.put("Font", ff.getFont());
      ffmap.put("Fontsize", ff.getFontsize());
      ffmap.put("Fontstyle", ff.getFontstyle());
      writer.write(ffmap, header);
    }
    writer.close();
  }

}
