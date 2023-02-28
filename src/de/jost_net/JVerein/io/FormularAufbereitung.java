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
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Formular;
import de.jost_net.JVerein.rmi.Formularfeld;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.action.Program;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.util.ApplicationException;

public class FormularAufbereitung
{

  private Document doc;

  private FileOutputStream fos;

  private PdfWriter writer;

  private File f;

  private static final int links = 1;

  private static final int rechts = 2;

  private int buendig = links;

  /**
   * Öffnet die Datei und startet die PDF-Generierung
   * 
   * @param f
   *          Die Datei, in die geschrieben werden soll
   * @throws RemoteException
   */
  public FormularAufbereitung(final File f) throws RemoteException
  {
    this.f = f;
    try
    {
      doc = new Document();
      fos = new FileOutputStream(f);

      writer = PdfWriter.getInstance(doc, fos);
      doc.open();

    }
    catch (IOException e)
    {
      throw new RemoteException("Fehler", e);
    }
    catch (DocumentException e)
    {
      throw new RemoteException("Fehler", e);
    }
  }

  public void writeForm(Formular formular, Map<String, Object> map)
      throws RemoteException
  {
    try
    {
      PdfReader reader = new PdfReader(formular.getInhalt());
      int numOfPages = reader.getNumberOfPages();
      for (int i = 1; i <= numOfPages; i++)
      {
        doc.setPageSize(reader.getPageSize(i));
        doc.newPage();
        PdfImportedPage page = writer.getImportedPage(reader, i);
        PdfContentByte contentByte = writer.getDirectContent();
        contentByte.addTemplate(page, 0, 0);

        DBIterator<Formularfeld> it = Einstellungen.getDBService()
            .createList(Formularfeld.class);
        it.addFilter("formular = ? and seite = ?",
            new Object[] { formular.getID(), i });
        while (it.hasNext())
        {
          Formularfeld f =  it.next();
          goFormularfeld(contentByte, f, map.get(f.getName()));
        }
      }
    }
    catch (IOException e)
    {
      throw new RemoteException("Fehler", e);
    }
    catch (DocumentException e)
    {
      throw new RemoteException("Fehler", e);
    }
  }

  /**
   * Schließen des aktuellen Formulars, damit die Datei korrekt gespeichert wird
   * 
   * @throws IOException
   */
  public void closeFormular() throws IOException
  {
    doc.close();
    writer.close();
    fos.close();
  }

  /**
   * Anzeige des gerade aufbereiteten Formulars. Die Ausgabedatei wird vorher
   * geschlossen.
   * 
   * @throws IOException
   */
  public void showFormular() throws IOException
  {
    closeFormular();
    GUI.getDisplay().asyncExec(new Runnable()
    {

      @Override
      public void run()
      {
        try
        {
          new Program().handleAction(f);
        }
        catch (ApplicationException ae)
        {
          Application.getMessagingFactory().sendMessage(new StatusBarMessage(
              ae.getLocalizedMessage(), StatusBarMessage.TYPE_ERROR));
        }
      }
    });
  }

  private void goFormularfeld(PdfContentByte contentByte, Formularfeld feld,
      Object val) throws DocumentException, IOException
  {
    BaseFont bf = null;
    if (feld.getFont().startsWith("FreeSans"))
    {
      String filename = "/fonts/FreeSans";
      if (feld.getFont().length() > 8)
      {
        filename += feld.getFont().substring(9);
      }
      bf = BaseFont.createFont(filename+".ttf", BaseFont.IDENTITY_H, true);
    }
    else
    {
      bf = BaseFont.createFont(feld.getFont(), BaseFont.CP1250, false);
    }

    float x = mm2point(feld.getX().floatValue());
    float y = mm2point(feld.getY().floatValue());
    if (val == null)
    {
      return;
    }
    buendig = links;
    String stringVal = getString(val);
    stringVal = stringVal.replace("\\n", "\n");
    stringVal = stringVal.replaceAll("\r\n", "\n");
    String[] ss = stringVal.split("\n");
    for (String s : ss)
    {
      contentByte.setFontAndSize(bf, feld.getFontsize().floatValue());
      contentByte.beginText();
      float offset = 0;
      if (buendig == rechts)
      {
        offset = contentByte.getEffectiveStringWidth(s, true);
      }
      contentByte.moveText(x - offset, y);
      contentByte.showText(s);
      contentByte.endText();
      y -= feld.getFontsize().floatValue() + 3;
    }
  }

  private float mm2point(float mm)
  {
    return mm / 0.3514598f;
  }

  private String getString(Object val)
  {
    StringBuilder stringVal = new StringBuilder();
    if (val instanceof Object[])
    {
      Object[] o = (Object[]) val;
      if (o.length == 0)
      {
        return "";
      }
      if (o[0] instanceof String)
      {
        for (Object ostr : o)
        {
          stringVal.append((String) ostr);
          stringVal.append("\n");
        }
      }
      if (o[0] instanceof Date)
      {
        for (Object od : o)
        {
          stringVal.append(new JVDateFormatTTMMJJJJ().format((Date) od));
          stringVal.append("\n");
        }
      }
      if (o[0] instanceof Double)
      {
        for (Object od : o)
        {
          stringVal.append(Einstellungen.DECIMALFORMAT.format(od));
          stringVal.append("\n");
        }
        buendig = rechts;
      }

    }
    if (val instanceof String)
    {
      stringVal = new StringBuilder((String) val);
    }
    if (val instanceof Double)
    {
      stringVal = new StringBuilder(Einstellungen.DECIMALFORMAT.format(val));
      buendig = rechts;
    }
    if (val instanceof Integer)
    {
      stringVal = new StringBuilder(val.toString());
      buendig = rechts;
    }
    if (val instanceof Date)
    {
      stringVal = new StringBuilder(
          new JVDateFormatTTMMJJJJ().format((Date) val));
    }
    return stringVal.toString();
  }
}
