/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * heiner@jverein.de
 * www.jverein.de
 * $Log$
 * Revision 1.2  2008/09/16 18:52:54  jost
 * Refactoring Formularaufbereitung
 *
 * Revision 1.1  2008/07/18 20:15:55  jost
 * Neu: Formulare
 *
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Formular;
import de.jost_net.JVerein.rmi.Formularfeld;
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

  public void writeForm(Formular formular, HashMap<String, Object> map)
      throws RemoteException
  {
    try
    {
      doc.newPage();
      PdfReader reader = new PdfReader(formular.getInhalt());
      PdfImportedPage page = writer.getImportedPage(reader, 1);
      PdfContentByte contentByte = writer.getDirectContent();
      contentByte.addTemplate(page, 0, 0);

      DBIterator it = Einstellungen.getDBService().createList(
          Formularfeld.class);
      it.addFilter("formular = ?", new Object[] { formular.getID() });
      while (it.hasNext())
      {
        goFormularfeld(contentByte, (Formularfeld) it.next(), map);
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

  public void showFormular() throws IOException
  {
    doc.close();
    writer.close();
    fos.close();
    GUI.getDisplay().asyncExec(new Runnable()
    {
      public void run()
      {
        try
        {
          new Program().handleAction(f);
        }
        catch (ApplicationException ae)
        {
          Application.getMessagingFactory().sendMessage(
              new StatusBarMessage(ae.getLocalizedMessage(),
                  StatusBarMessage.TYPE_ERROR));
        }
      }
    });
  }

  private void goFormularfeld(PdfContentByte contentByte, Formularfeld feld,
      HashMap<String, Object> map) throws DocumentException, IOException
  {

    BaseFont bf = BaseFont.createFont(feld.getFont(), BaseFont.CP1250, false);

    float x = mm2point(feld.getX().floatValue());
    float y = mm2point(feld.getY().floatValue());
    Object val = map.get(feld.getName());
    int links = 1;
    int rechts = 2;
    int buendig = links;
    String stringVal = null;
    if (val instanceof String)
    {
      stringVal = (String) val;
    }
    if (val instanceof Double)
    {
      stringVal = Einstellungen.DECIMALFORMAT.format((Double) val);
      buendig = rechts;
    }
    if (val instanceof Date)
    {
      stringVal = Einstellungen.DATEFORMAT.format((Date) val);
    }
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
      y -= feld.getFontsize().floatValue();
    }
  }

  private float mm2point(float mm)
  {
    return (float) mm / 0.3514598f;
  }
}
