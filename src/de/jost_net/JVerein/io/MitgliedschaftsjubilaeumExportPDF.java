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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;

import de.jost_net.JVerein.io.Adressbuch.Adressaufbereitung;
import de.jost_net.JVerein.rmi.Mitglied;

public class MitgliedschaftsjubilaeumExportPDF extends
    MitgliedschaftsjubilaeumsExport
{

  private FileOutputStream fos;

  private Reporter reporter;

  private int anz;

  @Override
  public String getName()
  {
    return "Mitgliedschaftsjubilare PDF-Export";
  }

  @Override
  public IOFormat[] getIOFormats(Class<?> objectType)
  {
    if (objectType != Mitglied.class)
    {
      return null;
    }
    IOFormat f = new IOFormat()
    {

      @Override
      public String getName()
      {
        return MitgliedschaftsjubilaeumExportPDF.this.getName();
      }

      /**
       * @see de.willuhn.jameica.hbci.io.IOFormat#getFileExtensions()
       */
      @Override
      public String[] getFileExtensions()
      {
        return new String[] { "*.pdf" };
      }
    };
    return new IOFormat[] { f };
  }

  @Override
  public String getDateiname()
  {
    return "mitgliedschaftsjubilare";
  }

  @Override
  protected void open() throws DocumentException, FileNotFoundException
  {
    fos = new FileOutputStream(file);
    reporter = new Reporter(fos, String.format("Mitgliedschaftsjubilare %d",
        jahr), "", 3);
  }

  @Override
  protected void startJahrgang(int jahrgang) throws DocumentException
  {
    Paragraph pHeader = new Paragraph("\n"
        + String.format("%d-jähriges Jubiläum", jahrgang), Reporter.getFreeSans(11));
    reporter.add(pHeader);
    reporter.addHeaderColumn("Eintrittsdatum", Element.ALIGN_CENTER, 50,
        BaseColor.LIGHT_GRAY);

    reporter.addHeaderColumn("Name, Vorname", Element.ALIGN_CENTER, 100,
        BaseColor.LIGHT_GRAY);
    reporter.addHeaderColumn("Anschrift", Element.ALIGN_CENTER, 120,
        BaseColor.LIGHT_GRAY);
    reporter.addHeaderColumn("Kommunikation", Element.ALIGN_CENTER, 80,
        BaseColor.LIGHT_GRAY);
    reporter.createHeader();
    anz = 0;
  }

  @Override
  protected void endeJahrgang() throws DocumentException
  {
    if (anz == 0)
    {
      reporter.addColumn("", Element.ALIGN_LEFT);
      reporter.addColumn("kein Mitglied", Element.ALIGN_LEFT);
      reporter.addColumn("", Element.ALIGN_LEFT);
      reporter.addColumn("", Element.ALIGN_LEFT);
    }
    reporter.closeTable();
  }

  @Override
  protected void add(Mitglied m) throws RemoteException
  {
    reporter.addColumn(m.getEintritt(), Element.ALIGN_LEFT);
    reporter
        .addColumn(Adressaufbereitung.getNameVorname(m), Element.ALIGN_LEFT);
    reporter.addColumn(Adressaufbereitung.getAnschrift(m), Element.ALIGN_LEFT);
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

  @Override
  protected void close() throws IOException, DocumentException
  {
    reporter.close();
    fos.close();
  }
}