package de.jost_net.JVerein.io;

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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Ersatz für die HeaderFooter-Klasse, die es bis iText 1.x gab. Wird zur Zeit
 * nur für den Footer gebraucht.
 */
class HeaderFooter extends PdfPageEventHelper
{

  String footer = null;

  int pagenumber;

  /**
   * 
   * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(com.itextpdf.text.pdf.PdfWriter,
   *      com.itextpdf.text.Document)
   */
  @Override
  public void onOpenDocument(PdfWriter writer, Document document)
  {
    //
  }

  @Override
  public void onChapter(PdfWriter writer, Document document,
      float paragraphPosition, Paragraph title)
  {
    //
  }

  /**
   * Increase the page number.
   */
  @Override
  public void onStartPage(PdfWriter writer, Document document)
  {
    pagenumber++;
  }

  public void setFooter(String footer)
  {
    this.footer = footer;
  }

  /**
   * Adds the header and the footer.
   * 
   */
  @Override
  public void onEndPage(PdfWriter writer, Document document)
  {
    Rectangle rect = document.getPageSize();
    switch (writer.getPageNumber() % 2)
    {
      case 0:
        // ColumnText.showTextAligned(writer.getDirectContent(),
        // Element.ALIGN_RIGHT, header[0], rect.getRight(), rect.getTop(), 0);
        break;
      case 1:
        // ColumnText.showTextAligned(writer.getDirectContent(),
        // Element.ALIGN_LEFT,
        // header[1], rect.getLeft(), rect.getTop(), 0);
        break;
    }
    float left = rect.getLeft() + document.leftMargin();
    float right = rect.getRight() - document.rightMargin();
    float bottom = rect.getBottom() + document.bottomMargin();
    PdfContentByte pc = writer.getDirectContent();
    pc.setColorStroke(BaseColor.BLACK);
    pc.setLineWidth(0.5f);
    pc.moveTo(left, bottom - 5);
    pc.lineTo(right, bottom - 5);
    pc.stroke();
    pc.moveTo(left, bottom - 25);
    pc.lineTo(right, bottom - 25);
    pc.stroke();

    ColumnText.showTextAligned(
        pc,
        Element.ALIGN_CENTER,
        new Phrase(footer + " " + pagenumber, Reporter.getFreeSans(7)),
        (left + right) / 2, bottom - 18, 0);
  }
}
