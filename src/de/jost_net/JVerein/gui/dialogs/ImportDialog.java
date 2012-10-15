/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
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

package de.jost_net.JVerein.gui.dialogs;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.input.EncodingInput;
import de.jost_net.JVerein.io.IOFormat;
import de.jost_net.JVerein.io.IORegistry;
import de.jost_net.JVerein.io.Importer;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.LabelInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.Container;
import de.willuhn.jameica.gui.util.SimpleContainer;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.jameica.system.OperationCanceledException;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

/**
 * Dialog, ueber den Daten importiert werden koennen.
 */
public class ImportDialog extends AbstractDialog<Object>
{
  private final static int WINDOW_WIDTH = 420;

  private Input importerListe = null;

  private GenericObject context = null;

  private Class<?> type = null;

  private Settings settings = null;

  private String helplink = null;

  private SelectInput encoding = null;

  /**
   * ct.
   * 
   * @param context
   *          Context.
   * @param type
   *          die Art der zu importierenden Objekte.
   * @throws RemoteException
   */
  public ImportDialog(GenericObject context, Class<?> type, boolean enc,
      String helplink) throws RemoteException
  {
    super(POSITION_CENTER);

    this.context = context;
    this.type = type;

    this.setTitle(JVereinPlugin.getI18n().tr("Daten-Import"));
    this.setSize(WINDOW_WIDTH, SWT.DEFAULT);

    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
    this.helplink = helplink;
    if (enc)
    {
      this.encoding = new EncodingInput(settings.getString("encoding", null));
    }
  }

  /**
   * @see de.willuhn.jameica.gui.dialogs.AbstractDialog#paint(org.eclipse.swt.widgets.Composite)
   */
  @Override
  protected void paint(Composite parent) throws Exception
  {
    Container group = new SimpleContainer(parent);
    group.addText(
        JVereinPlugin.getI18n().tr(
            "Bitte wählen Sie das gewünschte Dateiformat für den Import aus"),
        true);

    Input formats = getImporterList();
    group.addLabelPair(JVereinPlugin.getI18n().tr("Verfügbare Formate:"),
        formats);
    if (this.encoding != null)
    {
      group.addLabelPair(JVereinPlugin.getI18n().tr("Encoding"), encoding);
    }
    ButtonArea buttons = new ButtonArea();
    buttons.addButton(JVereinPlugin.getI18n().tr("Hilfe"),
        new DokumentationAction(), helplink, false, "help-browser.png");

    Button button = new Button(JVereinPlugin.getI18n().tr("Import starten"),
        new Action()
        {
          @Override
          public void handleAction(Object context) throws ApplicationException
          {
            doImport();
          }
        }, null, true, "import_obj.gif");
    button.setEnabled(!(formats instanceof LabelInput));
    buttons.addButton(button);
    buttons.addButton(JVereinPlugin.getI18n().tr("Abbrechen"), new Action()
    {
      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        throw new OperationCanceledException();
      }
    }, null, false, "process-stop.png");
    group.addButtonArea(buttons);
    getShell()
        .setMinimumSize(getShell().computeSize(WINDOW_WIDTH, SWT.DEFAULT));
  }

  /**
   * Importiert die Daten.
   * 
   * @throws ApplicationException
   */
  private void doImport() throws ApplicationException
  {
    Imp imp = null;

    try
    {
      imp = (Imp) getImporterList().getValue();
    }
    catch (Exception e)
    {
      Logger.error("error while saving import file", e);
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Fehler beim Starten des Imports"), e);
    }

    if (imp == null || imp.importer == null)
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Bitte wählen Sie ein Import-Format aus"));

    settings.setAttribute("lastformat", imp.format.getName());

    FileDialog fd = new FileDialog(GUI.getShell(), SWT.OPEN);
    fd.setText(i18n
        .tr("Bitte wählen Sie die Datei aus, welche für den Import verwendet werden soll."));
    fd.setFilterNames(imp.format.getFileExtensions());

    String path = settings
        .getString("lastdir", System.getProperty("user.home"));
    if (path != null && path.length() > 0)
      fd.setFilterPath(path);

    final String s = fd.open();

    if (s == null || s.length() == 0)
    {
      close();
      return;
    }

    final File file = new File(s);
    if (!file.exists() || !file.isFile())
      throw new ApplicationException(JVereinPlugin.getI18n().tr(
          "Datei existiert nicht oder ist nicht lesbar"));

    // Wir merken uns noch das Verzeichnis vom letzten mal
    settings.setAttribute("lastdir", file.getParent());
    if (encoding != null)
    {
      settings.setAttribute("encoding", encoding.getText());
    }
    final String enc = encoding != null ? encoding.getText() : null;
    // Dialog schliessen
    close();

    final Importer importer = imp.importer;
    final IOFormat format = imp.format;

    BackgroundTask t = new BackgroundTask()
    {
      @Override
      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        try
        {
          importer.doImport(context, format, file, enc, monitor);
          monitor.setPercentComplete(100);
          monitor.setStatus(ProgressMonitor.STATUS_DONE);
          GUI.getStatusBar().setSuccessText(
              JVereinPlugin.getI18n().tr("Daten importiert aus {0}", s));
          GUI.getCurrentView().reload();
        }
        catch (ApplicationException ae)
        {
          monitor.setStatus(ProgressMonitor.STATUS_ERROR);
          monitor.setStatusText(ae.getMessage());
          GUI.getStatusBar().setErrorText(ae.getMessage());
          throw ae;
        }
        catch (Exception e)
        {
          monitor.setStatus(ProgressMonitor.STATUS_ERROR);
          Logger.error("error while reading objects from " + s, e);
          ApplicationException ae = new ApplicationException(JVereinPlugin
              .getI18n().tr("Fehler beim Importieren der Daten aus {0}", s), e);
          monitor.setStatusText(ae.getMessage());
          GUI.getStatusBar().setErrorText(ae.getMessage());
          throw ae;
        }
      }

      @Override
      public void interrupt()
      {
      }

      @Override
      public boolean isInterrupted()
      {
        return false;
      }
    };

    Application.getController().start(t);
  }

  /**
   * Liefert eine Liste der verfuegbaren Importer.
   * 
   * @return Liste der Importer.
   * @throws Exception
   */
  private Input getImporterList() throws Exception
  {
    if (importerListe != null)
      return importerListe;

    Importer[] importers = IORegistry.getImporters();

    int size = 0;
    ArrayList<Imp> l = new ArrayList<Imp>();
    String lastFormat = settings.getString("lastformat", null);
    Imp selected = null;

    for (int i = 0; i < importers.length; ++i)
    {
      Importer imp = importers[i];
      if (imp == null)
        continue;
      IOFormat[] formats = imp.getIOFormats(type);
      if (formats == null || formats.length == 0)
      {
        Logger.debug("importer " + imp.getName()
            + " provides no import formats for " + type.getName()
            + ", skipping");
        continue;
      }
      for (int j = 0; j < formats.length; ++j)
      {
        size++;
        Imp im = new Imp(imp, formats[j]);
        l.add(im);

        String lf = im.format.getName();
        if (lastFormat != null && lf != null && lf.equals(lastFormat))
          selected = im;
      }
    }

    if (size == 0)
    {
      importerListe = new LabelInput(JVereinPlugin.getI18n().tr(
          "Keine Import-Filter verfügbar"));
      return importerListe;
    }

    Collections.sort(l);
    Imp[] imp = (Imp[]) l.toArray(new Imp[size]);
    importerListe = new SelectInput(PseudoIterator.fromArray(imp), selected);
    return importerListe;
  }

  /**
   * @see de.willuhn.jameica.gui.dialogs.AbstractDialog#getData()
   */
  @Override
  protected Object getData() throws Exception
  {
    return null;
  }

  /**
   * Hilfsklasse zur Anzeige der Importer.
   */
  private class Imp implements GenericObject, Comparable
  {
    private Importer importer = null;

    private IOFormat format = null;

    private Imp(Importer importer, IOFormat format)
    {
      this.importer = importer;
      this.format = format;
    }

    /**
     * @see de.willuhn.datasource.GenericObject#getAttribute(java.lang.String)
     */
    @Override
    public Object getAttribute(String arg0) throws RemoteException
    {
      return this.format.getName();
    }

    /**
     * @see de.willuhn.datasource.GenericObject#getAttributeNames()
     */
    @Override
    public String[] getAttributeNames() throws RemoteException
    {
      return new String[] { "name" };
    }

    /**
     * @see de.willuhn.datasource.GenericObject#getID()
     */
    @Override
    public String getID() throws RemoteException
    {
      return this.importer.getClass().getName() + "#"
          + this.format.getClass().getName();
    }

    /**
     * @see de.willuhn.datasource.GenericObject#getPrimaryAttribute()
     */
    @Override
    public String getPrimaryAttribute() throws RemoteException
    {
      return "name";
    }

    /**
     * @see de.willuhn.datasource.GenericObject#equals(de.willuhn.datasource.GenericObject)
     */
    @Override
    public boolean equals(GenericObject arg0) throws RemoteException
    {
      if (arg0 == null)
        return false;
      return this.getID().equals(arg0.getID());
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Object o)
    {
      if (o == null || !(o instanceof Imp))
        return -1;
      try
      {
        return this.format.getName().compareTo(((Imp) o).format.getName());
      }
      catch (Exception e)
      {
        // Tss, dann halt nicht
      }
      return 0;
    }
  }
}
