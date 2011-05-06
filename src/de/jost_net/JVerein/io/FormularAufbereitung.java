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
 * Revision 1.10  2011-05-06 14:49:53  jost
 * Neue Variablenmimik
 *
 * Revision 1.9  2011-04-23 06:56:32  jost
 * Neu: Freie Formulare
 *
 * Revision 1.8  2011-02-12 09:39:02  jost
 * Statische Codeanalyse mit Findbugs
 *
 * Revision 1.7  2010-12-02 21:05:48  jost
 * Bugfix Integer
 *
 * Revision 1.6  2010-10-15 09:58:29  jost
 * Code aufgeräumt
 *
 * Revision 1.5  2010-08-08 19:33:13  jost
 * Zusammenfassung der Rechnungen
 *
 * Revision 1.4  2009/09/21 18:19:29  jost
 * NullPointerException abgefangen.
 *
 * Revision 1.3  2009/04/10 17:46:17  jost
 * Zusätzliche Datenfelder für die Rechnungserstellung
 *
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
import java.util.Map;

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
        Formularfeld f = (Formularfeld) it.next();
        goFormularfeld(contentByte, f, map.get(f.getName()));
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
      Object val) throws DocumentException, IOException
  {

    BaseFont bf = BaseFont.createFont(feld.getFont(), BaseFont.CP1250, false);

    float x = mm2point(feld.getX().floatValue());
    float y = mm2point(feld.getY().floatValue());
    if (val == null)
    {
      return;
    }
    buendig = links;
    String stringVal = getString(val);
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
