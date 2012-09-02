/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
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

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.logging.Logger;

public class AltersjubilaeumsExportPDF extends AltersjubilaeumsExport
{
  private FileOutputStream fos;

  private Reporter reporter;

  private int anz;

  public String getName()
  {
    return JVereinPlugin.getI18n().tr("Altersjubilare PDF-Export");
  }

  public IOFormat[] getIOFormats(Class<?> objectType)
  {
    if (objectType != Mitglied.class)
    {
      return null;
    }
    IOFormat f = new IOFormat()
    {
      public String getName()
      {
        return AltersjubilaeumsExportPDF.this.getName();
      }

      /**
       * @see de.willuhn.jameica.hbci.io.IOFormat#getFileExtensions()
       */
      public String[] getFileExtensions()
      {
        return new String[] { "*.pdf" };
      }
    };
    return new IOFormat[] { f };
  }

  public String getDateiname()
  {
    return JVereinPlugin.getI18n().tr("altersjubilare");
  }

  protected void open() throws DocumentException, FileNotFoundException
  {
    fos = new FileOutputStream(file);
    Logger.debug(JVereinPlugin.getI18n().tr("Altersjubilare, Jahr={0}",
        jahr + ""));
    reporter = new Reporter(fos, JVereinPlugin.getI18n().tr(
        "Altersjubilare {0}", jahr + ""), "", 3);
  }

  protected void startJahrgang(int jahrgang) throws DocumentException
  {
    Logger.debug(JVereinPlugin.getI18n().tr("Altersjubiläum, Jahrgang={0}",
        jahrgang + ""));
    Paragraph pHeader = new Paragraph("\n"
        + JVereinPlugin.getI18n().tr("{0}. Geburtstag", jahrgang + ""),
        FontFactory.getFont(FontFactory.HELVETICA, 11));
    reporter.add(pHeader);
    reporter.addHeaderColumn(JVereinPlugin.getI18n().tr("Geburtsdatum"),
        Element.ALIGN_CENTER, 50, Color.LIGHT_GRAY);

    reporter.addHeaderColumn(JVereinPlugin.getI18n().tr("Name, Vorname"),
        Element.ALIGN_CENTER, 100, Color.LIGHT_GRAY);
    reporter.addHeaderColumn(JVereinPlugin.getI18n().tr("Anschrift"),
        Element.ALIGN_CENTER, 120, Color.LIGHT_GRAY);
    reporter.addHeaderColumn(JVereinPlugin.getI18n().tr("Kommunikation"),
        Element.ALIGN_CENTER, 80, Color.LIGHT_GRAY);
    reporter.createHeader();
    anz = 0;
  }

  protected void endeJahrgang() throws DocumentException
  {
    if (anz == 0)
    {
      reporter.addColumn("", Element.ALIGN_LEFT);
      reporter.addColumn(JVereinPlugin.getI18n().tr("kein Mitglied"),
          Element.ALIGN_LEFT);
      reporter.addColumn("", Element.ALIGN_LEFT);
      reporter.addColumn("", Element.ALIGN_LEFT);
    }
    reporter.closeTable();
  }

  protected void add(Mitglied m) throws RemoteException
  {
    reporter.addColumn(m.getGeburtsdatum(), Element.ALIGN_LEFT);
    reporter.addColumn(m.getNameVorname(), Element.ALIGN_LEFT);
    reporter.addColumn(m.getAnschrift(), Element.ALIGN_LEFT);
    String kommunikation = m.getTelefonprivat();
    if (kommunikation.length() > 0 && m.getTelefondienstlich().length() > 0)
    {
      kommunikation += ", ";
    }
    kommunikation += m.getTelefondienstlich();

    if (kommunikation.length() > 0 && m.getEmail().length() > 0)
    {
      kommunikation += ", ";
    }
    kommunikation += m.getEmail();
    reporter.addColumn(kommunikation, Element.ALIGN_LEFT, false);
    anz++;
  }

  protected void close() throws IOException, DocumentException
  {
    reporter.close();
    fos.close();
  }

}
