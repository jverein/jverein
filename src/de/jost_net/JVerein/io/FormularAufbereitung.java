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
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
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
  public FormularAufbereitung(Formular formular, final File f,
      HashMap<String, Object> map) throws RemoteException
  {
    try
    {
      Document doc = new Document();
      FileOutputStream fos = new FileOutputStream(f);

      PdfWriter writer = PdfWriter.getInstance(doc, fos);
      doc.open();
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
    catch (IOException e)
    {
      throw new RemoteException("Fehler", e);
    }
    catch (DocumentException e)
    {
      throw new RemoteException("Fehler", e);
    }
  }

  private void goFormularfeld(PdfContentByte contentByte, Formularfeld feld,
      HashMap<String, Object> map) throws DocumentException, IOException
  {

    BaseFont bf = BaseFont.createFont(feld.getFont(), BaseFont.CP1250, false);

    float x = mm2point(feld.getX().floatValue());
    float y = mm2point(feld.getY().floatValue());
    String val = (String) map.get(feld.getName());
    String[] ss = val.split("\n");
    for (String s : ss)
    {
      contentByte.setFontAndSize(bf, feld.getFontsize().floatValue());
      contentByte.beginText();
      contentByte.moveText(x, y);
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
