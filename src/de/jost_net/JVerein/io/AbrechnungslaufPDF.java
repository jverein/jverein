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
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;

import de.jost_net.JVerein.io.Adressbuch.Adressaufbereitung;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Abrechnungslauf;
import de.jost_net.JVerein.rmi.Mitgliedskonto;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class AbrechnungslaufPDF
{

  public AbrechnungslaufPDF(DBIterator<Mitgliedskonto> it, final File file,
      final Abrechnungslauf lauf) throws ApplicationException
  {
    try
    {
      FileOutputStream fos = new FileOutputStream(file);
      String title = "Abrechnungslauf";
      String subtitle = "Nr. " + lauf.getNr() + " vom "
          + new JVDateFormatTTMMJJJJ().format(lauf.getDatum());
      Reporter reporter = new Reporter(fos, title, subtitle, it.size());

      reporter.addHeaderColumn("Fälligkeit", Element.ALIGN_CENTER, 80,
          BaseColor.LIGHT_GRAY);
      reporter.addHeaderColumn("Name", Element.ALIGN_CENTER, 190,
          BaseColor.LIGHT_GRAY);
      reporter.addHeaderColumn("Zweck", Element.ALIGN_CENTER, 190,
          BaseColor.LIGHT_GRAY);
      reporter.addHeaderColumn("Betrag", Element.ALIGN_CENTER, 60,
          BaseColor.LIGHT_GRAY);
      reporter.addHeaderColumn("Eingang", Element.ALIGN_CENTER, 60,
          BaseColor.LIGHT_GRAY);
      reporter.addHeaderColumn("Zahlungsweg", Element.ALIGN_CENTER, 100,
          BaseColor.LIGHT_GRAY);
      reporter.createHeader();

      while (it.hasNext())
      {
        Mitgliedskonto mk = it.next();

        reporter.addColumn(new JVDateFormatTTMMJJJJ().format(mk.getDatum()),
            Element.ALIGN_LEFT);
        reporter.addColumn(Adressaufbereitung.getNameVorname(mk.getMitglied()),
            Element.ALIGN_LEFT);
        reporter.addColumn(mk.getZweck1(), Element.ALIGN_LEFT);
        reporter.addColumn(mk.getBetrag());
        reporter.addColumn(mk.getIstSumme());
        reporter.addColumn(Zahlungsweg.get(mk.getZahlungsweg()),
            Element.ALIGN_LEFT);
      }
      reporter.closeTable();
      reporter.close();

      GUI.getStatusBar().setSuccessText("Auswertung fertig.");

      fos.close();
      FileViewer.show(file);
    }
    catch (DocumentException e)
    {
      Logger.error("error while creating report", e);
      throw new ApplicationException("Fehler", e);
    }
    catch (FileNotFoundException e)
    {
      Logger.error("error while creating report", e);
      throw new ApplicationException("Fehler", e);
    }
    catch (IOException e)
    {
      Logger.error("error while creating report", e);
      throw new ApplicationException("Fehler", e);
    }
  }
}
