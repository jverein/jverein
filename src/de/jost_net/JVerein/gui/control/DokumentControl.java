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
package de.jost_net.JVerein.gui.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.Messaging.DokumentMessage;
import de.jost_net.JVerein.gui.menu.DokumentMenu;
import de.jost_net.JVerein.gui.parts.DokumentPart;
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
import de.willuhn.jameica.gui.input.FileInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.messaging.Message;
import de.willuhn.jameica.messaging.MessageConsumer;
import de.willuhn.jameica.messaging.QueryMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class DokumentControl extends AbstractControl
{

  private AbstractDokument doc;

  private DokumentPart dopa;

  private FileInput datei;

  private TablePart docsList;

  private Button neuButton;

  private Button speichernButton;

  private String verzeichnis;

  private boolean enabled;

  private DokumentMessageConsumer mc = null;

  public DokumentControl(AbstractView view, String verzeichnis, boolean enabled)
  {
    super(view);
    this.verzeichnis = verzeichnis;
    this.enabled = enabled;
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

  public FileInput getDatei()
  {
    if (datei != null)
    {
      return datei;
    }
    datei = new FileInput("", false);
    return datei;
  }

  public DokumentPart getDokumentPart() throws RemoteException
  {
    if (dopa != null)
    {
      return dopa;
    }
    dopa = new DokumentPart(getDokument());
    return dopa;
  }

  public Button getNeuButton(final AbstractDokument doc)
  {
    neuButton = new Button("neues Dokument", new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        GUI.startView(new DokumentView(verzeichnis), doc);
      }
    }, null);
    neuButton.setEnabled(enabled);
    return neuButton;
  }

  public Button getSpeichernButton(final String verzeichnis)
  {
    speichernButton = new Button("Speichern", new Action()
    {

      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        speichern(verzeichnis);
      }
    }, null, true, "save.png");
    return speichernButton;
  }

  /**
   * Speichern des Dokuments
   * 
   * @throws RemoteException
   * @throws ApplicationException
   */
  private void speichern(String verzeichnis) throws ApplicationException
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
        fis.close();
        throw new ApplicationException("Datei ist leer");
      }
      // Dokument speichern
      String locverz = verzeichnis + doc.getReferenz();
      QueryMessage qm = new QueryMessage(locverz, fis);
      Application.getMessagingFactory()
          .getMessagingQueue("jameica.messaging.put").sendSyncMessage(qm);
      // Satz in die DB schreiben
      doc.setBemerkung((String) dopa.getBemerkung().getValue());
      String uuid = qm.getData().toString();
      doc.setUUID(uuid);
      doc.setDatum((Date) dopa.getDatum().getValue());
      doc.store();
      // Zusätzliche Eigenschaft speichern
      Map<String, String> map = new HashMap<>();
      map.put("filename", file.getName());
      qm = new QueryMessage(uuid, map);
      Application.getMessagingFactory()
          .getMessagingQueue("jameica.messaging.putmeta").sendMessage(qm);
      speichernButton.setEnabled(false);
      GUI.getStatusBar().setSuccessText("Dokument gespeichert");
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
    DBIterator<AbstractDokument> docs = service.createList(doc.getClass());
    docs.addFilter("referenz = ?", new Object[] { doc.getReferenz() });
    docs.setOrder("ORDER BY datum desc");

    docsList = new TablePart(docs, null /* new KontoAction() */);
    docsList.addColumn("Datum", "datum",
        new DateFormatter(new JVDateFormatTTMMJJJJ()));
    docsList.addColumn("Bemerkung", "bemerkung");
    docsList.setRememberColWidths(true);
    docsList.setContextMenu(new DokumentMenu(enabled));
    docsList.setRememberOrder(true);
    docsList.setSummary(true);
    this.mc = new DokumentMessageConsumer();
    Application.getMessagingFactory().registerMessageConsumer(this.mc);

    return docsList;
  }

  public void refreshTable() throws RemoteException
  {
    docsList.removeAll();
    DBIterator<AbstractDokument> docs = Einstellungen.getDBService()
        .createList(doc.getClass());
    docs.addFilter("referenz = ?", new Object[] { doc.getReferenz() });
    docs.setOrder("ORDER BY datum desc");
    while (docs.hasNext())
    {
      docsList.addItem(docs.next());
    }
  }

  /**
   * Wird benachrichtigt um die Anzeige zu aktualisieren.
   */
  private class DokumentMessageConsumer implements MessageConsumer
  {

    /**
     * @see de.willuhn.jameica.messaging.MessageConsumer#autoRegister()
     */
    @Override
    public boolean autoRegister()
    {
      return false;
    }

    /**
     * @see de.willuhn.jameica.messaging.MessageConsumer#getExpectedMessageTypes()
     */
    @Override
    public Class<?>[] getExpectedMessageTypes()
    {
      return new Class[] { DokumentMessage.class };
    }

    /**
     * @see de.willuhn.jameica.messaging.MessageConsumer#handleMessage(de.willuhn.jameica.messaging.Message)
     */
    @Override
    public void handleMessage(final Message message) throws Exception
    {
      GUI.getDisplay().syncExec(new Runnable()
      {

        @Override
        public void run()
        {
          try
          {
            DokumentMessage dm = (DokumentMessage) message;
            doc = (AbstractDokument) dm.getObject();

            if (docsList == null)
            {
              // Eingabe-Feld existiert nicht. Also abmelden
              Application.getMessagingFactory()
                  .unRegisterMessageConsumer(DokumentMessageConsumer.this);
              return;
            }
            refreshTable();
          }
          catch (Exception e)
          {
            // Wenn hier ein Fehler auftrat, deregistrieren wir uns
            // wieder
            Logger.error("Dokumenteliste konnte nicht aktualisiert werden", e);
            Application.getMessagingFactory()
                .unRegisterMessageConsumer(DokumentMessageConsumer.this);
          }
        }
      });
    }
  }

}
