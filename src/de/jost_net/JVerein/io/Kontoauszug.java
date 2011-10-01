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
 **********************************************************************/
package de.jost_net.JVerein.io;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.control.MitgliedskontoNode;
import de.jost_net.JVerein.keys.Zahlungsweg;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.util.Dateiname;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.action.Program;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.util.ApplicationException;

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

  public Kontoauszug(Object object) throws Exception
  {
    this();
    if (object instanceof Mitglied)
    {
      generiereMitglied((Mitglied) object);
    }
    else if (object instanceof Mitglied[])
    {
      Mitglied[] mitglieder = (Mitglied[]) object;
      for (Mitglied m : mitglieder)
      {
        generiereMitglied(m);
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

  private void generiereMitglied(Mitglied m) throws RemoteException,
      DocumentException
  {
    rpt.newPage();
    rpt.add(Einstellungen.getEinstellung().getName(), 20);
    rpt.add("Kontoauszug " + m.getVornameName(), 18);
    JVDateFormatTTMMJJJJ jv = new JVDateFormatTTMMJJJJ();
    rpt.add("Stand: " + jv.format(new Date()), 16);

    rpt.addHeaderColumn(" ", Element.ALIGN_CENTER, 20, Color.LIGHT_GRAY);
    rpt.addHeaderColumn("Datum", Element.ALIGN_CENTER, 20, Color.LIGHT_GRAY);
    rpt.addHeaderColumn("Zweck 1", Element.ALIGN_LEFT, 50, Color.LIGHT_GRAY);
    rpt.addHeaderColumn("Zweck 2", Element.ALIGN_LEFT, 50, Color.LIGHT_GRAY);
    rpt.addHeaderColumn("Zahlungsweg", Element.ALIGN_LEFT, 20, Color.LIGHT_GRAY);
    rpt.addHeaderColumn("Soll", Element.ALIGN_RIGHT, 20, Color.LIGHT_GRAY);
    rpt.addHeaderColumn("Ist", Element.ALIGN_RIGHT, 20, Color.LIGHT_GRAY);
    rpt.addHeaderColumn("Differenz", Element.ALIGN_RIGHT, 20, Color.LIGHT_GRAY);
    rpt.createHeader();

    MitgliedskontoNode node = new MitgliedskontoNode(m);
    generiereZeile(node);
    GenericIterator gi1 = node.getChildren();
    while (gi1.hasNext())
    {
      MitgliedskontoNode n1 = (MitgliedskontoNode) gi1.next();
      generiereZeile(n1);
      GenericIterator gi2 = n1.getChildren();
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
    rpt.addColumn((String) node.getAttribute("zweck2"), Element.ALIGN_LEFT);
    rpt.addColumn(Zahlungsweg.get((Integer) node.getAttribute("zahlungsweg")),
        Element.ALIGN_LEFT);
    rpt.addColumn((Double) node.getAttribute("soll"));
    rpt.addColumn((Double) node.getAttribute("ist"));
    rpt.addColumn((Double) node.getAttribute("differenz"));
  }

  private void zeigeDokument()
  {
    GUI.getStatusBar().setSuccessText("Spendenbescheinigung erstellt");
    GUI.getDisplay().asyncExec(new Runnable()
    {

      public void run()
      {
        try
        {
          new Program().handleAction(file);
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
}