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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.control.MitgliedskontoNode;
import de.jost_net.JVerein.io.Adressbuch.Adressaufbereitung;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.util.Dateiname;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.jameica.gui.GUI;

public class Kontoauszug
{

  private de.willuhn.jameica.system.Settings settings;

  private File file;

  private Reporter rpt;

  private Kontoauszug() throws IOException, DocumentException
  {
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    settings.setStoreWhenRead(true);
    init();
    rpt = new Reporter(new FileOutputStream(file), 40, 20, 20, 40);
  }

  public Kontoauszug(Object object, Date von, Date bis) throws Exception
  {
    this();
    if (object instanceof Mitglied)
    {
      generiereMitglied((Mitglied) object, von, bis);
    }
    else if (object instanceof Mitglied[])
    {
      Mitglied[] mitglieder = (Mitglied[]) object;
      for (Mitglied m : mitglieder)
      {
        generiereMitglied(m, von, bis);
      }
    }
    rpt.close();
    zeigeDokument();
  }

  private void init() throws IOException
  {
    FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
    fd.setText("Ausgabedatei wählen.");
    String path = settings
        .getString("lastdir", System.getProperty("user.home"));
    if (path != null && path.length() > 0)
    {
      fd.setFilterPath(path);
    }
    fd.setFileName(new Dateiname("kontoauszug", "", Einstellungen
        .getEinstellung().getDateinamenmuster(), "PDF").get());
    fd.setFilterExtensions(new String[] { "*.PDF" });

    String s = fd.open();
    if (s == null || s.length() == 0)
    {
      return;
    }
    if (!s.endsWith(".PDF"))
    {
      s = s + ".PDF";
    }
    file = new File(s);
    settings.setAttribute("lastdir", file.getParent());
  }

  private void generiereMitglied(Mitglied m, Date von, Date bis)
      throws RemoteException, DocumentException
  {
    rpt.newPage();
    rpt.add(Einstellungen.getEinstellung().getName(), 20);
    rpt.add(
        String.format("Kontoauszug %s", Adressaufbereitung.getVornameName(m)),
        18);
    JVDateFormatTTMMJJJJ jv = new JVDateFormatTTMMJJJJ();
    rpt.add(String.format("Stand: %s", jv.format(new Date())), 16);

    rpt.addHeaderColumn(" ", Element.ALIGN_CENTER, 20, BaseColor.LIGHT_GRAY);
    rpt.addHeaderColumn("Datum", Element.ALIGN_CENTER, 20, BaseColor.LIGHT_GRAY);
    rpt.addHeaderColumn("Zweck", Element.ALIGN_LEFT, 50, BaseColor.LIGHT_GRAY);
    rpt.addHeaderColumn("Zahlungsweg", Element.ALIGN_LEFT, 20,
        BaseColor.LIGHT_GRAY);
    rpt.addHeaderColumn("Soll", Element.ALIGN_RIGHT, 20, BaseColor.LIGHT_GRAY);
    rpt.addHeaderColumn("Ist", Element.ALIGN_RIGHT, 20, BaseColor.LIGHT_GRAY);
    rpt.addHeaderColumn("Differenz", Element.ALIGN_RIGHT, 20,
        BaseColor.LIGHT_GRAY);
    rpt.createHeader();

    MitgliedskontoNode node = new MitgliedskontoNode(m, von, bis);
    generiereZeile(node);
    GenericIterator<?> gi1 = node.getChildren();
    while (gi1.hasNext())
    {
      MitgliedskontoNode n1 = (MitgliedskontoNode) gi1.next();
      generiereZeile(n1);
      GenericIterator<?> gi2 = n1.getChildren();
      while (gi2.hasNext())
      {
        MitgliedskontoNode n2 = (MitgliedskontoNode) gi2.next();
        generiereZeile(n2);
      }
    }
    rpt.closeTable();
  }

  private void generiereZeile(MitgliedskontoNode node)
  {
    switch (node.getType())
    {
      case MitgliedskontoNode.MITGLIED:
        rpt.addColumn("Gesamt", Element.ALIGN_LEFT);
        break;
      case MitgliedskontoNode.SOLL:
        rpt.addColumn("Soll", Element.ALIGN_CENTER);
        break;
      case MitgliedskontoNode.IST:
        rpt.addColumn("Ist", Element.ALIGN_RIGHT);
        break;
    }
    rpt.addColumn((Date) node.getAttribute("datum"), Element.ALIGN_CENTER);
    rpt.addColumn((String) node.getAttribute("zweck1"), Element.ALIGN_LEFT);
    rpt.addColumn(Zahlungsweg.get((Integer) node.getAttribute("zahlungsweg")),
        Element.ALIGN_LEFT);
    rpt.addColumn((Double) node.getAttribute("soll"));
    rpt.addColumn((Double) node.getAttribute("ist"));
    rpt.addColumn((Double) node.getAttribute("differenz"));
  }

  private void zeigeDokument()
  {
    GUI.getStatusBar().setSuccessText("Kontoauszug erstellt");
    FileViewer.show(file);
  }
}