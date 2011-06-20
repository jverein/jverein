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
 * Revision 1.3  2011-02-12 09:29:59  jost
 * Statische Codeanalyse mit Findbugs
 *
 * Revision 1.2  2010-12-14 21:41:41  jost
 * Neu: Speicherung von Dokumenten
 *
 * Revision 1.1  2010-12-12 08:11:17  jost
 * Neu: Speicherung von Dokumenten
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.menu.DokumentMenu;
import de.jost_net.JVerein.gui.view.DokumentView;
import de.jost_net.JVerein.rmi.AbstractDokument;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.FileInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.messaging.QueryMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.util.ApplicationException;

public class DokumentControl extends AbstractControl
{
  private AbstractDokument doc;

  private DateInput datum = null;

  private TextInput bemerkung;

  private FileInput datei;

  private TablePart docsList;

  private Button neuButton;

  private Button speichernButton;

  private String verzeichnis;

  // private de.willuhn.jameica.system.Settings settings;

  public DokumentControl(AbstractView view, String verzeichnis)
  {
    super(view);
    this.verzeichnis = verzeichnis;
    // settings = new de.willuhn.jameica.system.Settings(this.getClass());
    // settings.setStoreWhenRead(true);
  }

  private AbstractDokument getDokument() throws RemoteException
  {
    doc = (AbstractDokument) getCurrentObject();
    if (doc == null)
    {
      throw new RemoteException("Programmfehler! Dokument fehlt");
    }
    return doc;
  }

  public DateInput getDatum() throws RemoteException
  {
    if (datum != null)
    {
      return datum;
    }
    Date d = getDokument().getDatum();
    if (d == null)
    {
      d = new Date();
    }
    this.datum = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.datum.setTitle("Datum");
    this.datum.setText("Bitte Datum wählen");
    this.datum.addListener(new Listener()
    {

      public void handleEvent(Event event)
      {
        Date date = (Date) datum.getValue();
        if (date == null)
        {
          return;
        }
      }
    });
    return datum;
  }

  public TextInput getBemerkung() throws RemoteException
  {
    if (bemerkung != null)
    {
      return bemerkung;
    }
    bemerkung = new TextInput(getDokument().getBemerkung(), 50);
    return bemerkung;
  }

  public FileInput getDatei() throws RemoteException
  {
    if (datei != null)
    {
      return datei;
    }
    datei = new FileInput("", false);
    return datei;
  }

  public Button getNeuButton(final AbstractDokument doc)
  {
    neuButton = new Button("neu", new Action()
    {

      public void handleAction(Object context)
      {
        GUI.startView(new DokumentView(verzeichnis), doc);
      }
    }, null);
    return neuButton;
  }

  public Button getSpeichernButton(final String verzeichnis)
  {
    speichernButton = new Button("Speichern", new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        try
        {
          speichern(verzeichnis);
        }
        catch (RemoteException e)
        {
          throw new ApplicationException(e);
        }
      }
    }, null, true, "document-save.png");
    return speichernButton;
  }

  /**
   * Speichern des Dokuments
   * 
   * @return UUID des Dokuments
   * @throws RemoteException
   * @throws ApplicationException
   */
  private void speichern(String verzeichnis) throws RemoteException,
      ApplicationException
  {
    try
    {
      File file = new File((String) datei.getValue());
      if (file.isDirectory())
      {
        throw new ApplicationException(
            "Verzeichnisse können nicht gespeichert werden.");
      }
      if (!file.exists())
      {
        throw new ApplicationException("Datei existiert nicht");
      }
      FileInputStream fis = new FileInputStream(file);
      if (fis.available() <= 0)
      {
        throw new ApplicationException("Datei ist leer");
      }
      // Dokument speichern
      String locverz = verzeichnis + doc.getReferenz();
      QueryMessage qm = new QueryMessage(locverz, fis);
      Application.getMessagingFactory()
          .getMessagingQueue("jameica.messaging.put").sendSyncMessage(qm);
      // Satz in die DB schreiben
      doc.setBemerkung((String) getBemerkung().getValue());
      String uuid = qm.getData().toString();
      doc.setUUID(uuid);
      doc.setDatum((Date) getDatum().getValue());
      doc.store();
      // Zusätzliche Eigenschaft speichern
      Map<String, String> map = new HashMap<String, String>();
      map.put("filename", file.getName());
      qm = new QueryMessage(uuid, map);
      Application.getMessagingFactory()
          .getMessagingQueue("jameica.messaging.putmeta").sendMessage(qm);
      speichernButton.setEnabled(false);
      GUI.getStatusBar().setSuccessText(
          JVereinPlugin.getI18n().tr("Dokument gespeichert"));
    }
    catch (FileNotFoundException e)
    {
      throw new ApplicationException("Datei existiert nicht");
    }
    catch (IOException e)
    {
      throw new ApplicationException("Allgemeiner Ein-/Ausgabe-Fehler");
    }
  }

  public Part getDokumenteList(AbstractDokument doc) throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator docs = service.createList(doc.getClass());
    docs.addFilter("referenz = ?", new Object[] { doc.getReferenz() });
    docs.setOrder("ORDER BY datum desc");

    docsList = new TablePart(docs, null /* new KontoAction() */);
    docsList.addColumn("Datum", "datum", new DateFormatter(new JVDateFormatTTMMJJJJ()));
    docsList.addColumn("Bemerkung", "bemerkung");
    docsList.setRememberColWidths(true);
    docsList.setContextMenu(new DokumentMenu());
    docsList.setRememberOrder(true);
    docsList.setSummary(true);
    return docsList;
  }

  public void refreshTable() throws RemoteException
  {
    docsList.removeAll();
    DBIterator docs = Einstellungen.getDBService().createList(doc.getClass());
    docs.addFilter("referenz = ?", new Object[] { doc.getReferenz() });
    docs.setOrder("ORDER BY datum desc");
    while (docs.hasNext())
    {
      docsList.addItem(docs.next());
    }
  }

}
