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
package de.jost_net.JVerein.gui.dialogs;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;

import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.io.Exporter;
import de.jost_net.JVerein.io.FileViewer;
import de.jost_net.JVerein.io.IOFormat;
import de.jost_net.JVerein.io.IORegistry;
import de.jost_net.JVerein.util.Dateiname;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.LabelInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.Container;
import de.willuhn.jameica.gui.util.SimpleContainer;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

/**
 * Dialog, ueber den Daten exportiert werden koennen.
 */
public class ExportDialog extends AbstractDialog<Object>
{

  private final static int WINDOW_WIDTH = 420;

  private Input exporterListe = null;

  private CheckboxInput openFile = null;

  private Object[] objects = null;

  private Class<?> type = null;

  private Settings settings = null;

  private String helplink = null;

  /**
   * ct.
   * 
   * @param objects
   *          Liste der zu exportierenden Objekte.
   * @param type
   *          die Art der zu exportierenden Objekte.
   */
  public ExportDialog(Object[] objects, Class<?> type, String helplink)
  {
    super(POSITION_CENTER);
    setTitle(i18n.tr("Daten-Export"));
    this.setSize(WINDOW_WIDTH, SWT.DEFAULT);

    this.objects = objects;
    this.type = type;

    settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);
    this.helplink = helplink;
  }

  /**
   * @see de.willuhn.jameica.gui.dialogs.AbstractDialog#paint(org.eclipse.swt.widgets.Composite)
   */
  @Override
  protected void paint(Composite parent) throws Exception
  {
    Container group = new SimpleContainer(parent);
    group
        .addText(
            i18n.tr("Bitte wählen Sie das gewünschte Dateiformat aus für den Export aus"),
            true);

    Input formats = getExporterList();
    group.addLabelPair(i18n.tr("Verfügbare Formate:"), formats);

    boolean exportEnabled = !(formats instanceof LabelInput);

    if (exportEnabled)
    {
      group.addCheckbox(getOpenFile(), i18n.tr("Datei nach dem Export öffnen"));
    }
    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(), helplink, false,
        "help-browser.png");
    Button button = new Button(i18n.tr("Export starten"), new Action()
    {

      @Override
      public void handleAction(Object context) throws ApplicationException
      {
        export();
      }
    }, null, true, "ok.png");
    button.setEnabled(exportEnabled);
    buttons.addButton(button);
    buttons.addButton(i18n.tr("Abbrechen"), new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        close();
      }
    }, null, false, "process-stop.png");
    group.addButtonArea(buttons);

    getShell()
        .setMinimumSize(getShell().computeSize(WINDOW_WIDTH, SWT.DEFAULT));
  }

  /**
   * Exportiert die Daten.
   * 
   * @throws ApplicationException
   */
  private void export() throws ApplicationException
  {
    Exp exp = null;

    try
    {
      exp = (Exp) getExporterList().getValue();
    }
    catch (Exception e)
    {
      Logger.error("error while saving export file", e);
      throw new ApplicationException(
          i18n.tr("Fehler beim Starten des Exports"), e);
    }

    if (exp == null || exp.exporter == null)
    {
      throw new ApplicationException(
          i18n.tr("Bitte wählen Sie ein Export-Format aus"));
    }
    settings.setAttribute("lastformat", exp.format.getName());

    FileDialog fd = new FileDialog(GUI.getShell(), SWT.SAVE);
    fd.setText(i18n
        .tr("Bitte geben Sie eine Datei ein, in die die Daten exportiert werden sollen."));
    fd.setOverwrite(true);
    String[] se = exp.format.getFileExtensions();
    String ext = se == null ? "" : se[0];
    ext = ext.replaceAll("\\*.", ""); // "*." entfernen

    fd.setFileName(new Dateiname(exp.exporter.getDateiname(), "", "a$-d$z$",
        ext).get());

    String path = settings
        .getString("lastdir", System.getProperty("user.home"));
    if (path != null && path.length() > 0)
    {
      fd.setFilterPath(path);
    }
    final String s = fd.open();

    if (s == null || s.length() == 0)
    {
      close();
      return;
    }

    final File file = new File(s);

    // Wir merken uns noch das Verzeichnis vom letzten mal
    settings.setAttribute("lastdir", file.getParent());

    // Dialog schliessen
    final boolean open = ((Boolean) getOpenFile().getValue()).booleanValue();
    settings.setAttribute("open", open);
    close();

    final Exporter exporter = exp.exporter;
    final IOFormat format = exp.format;

    BackgroundTask t = new BackgroundTask()
    {

      @Override
      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        try
        {
          exporter.doExport(objects, format, file, monitor);
          monitor.setPercentComplete(100);
          monitor.setStatus(ProgressMonitor.STATUS_DONE);
          GUI.getStatusBar().setSuccessText(
              i18n.tr("Daten exportiert nach {0}", s));
          monitor.setStatusText(i18n.tr("Daten exportiert nach {0}", s));

          if (open)
          {
            FileViewer.show(file);
          }
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
          Logger.error("error while writing objects to " + s, e);
          ApplicationException ae = new ApplicationException(i18n.tr(
              "Fehler beim Exportieren der Daten in {0}", s), e);
          monitor.setStatusText(ae.getMessage());
          GUI.getStatusBar().setErrorText(ae.getMessage());
          throw ae;
        }
      }

      @Override
      public void interrupt()
      {
        //
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
   * Liefert eine Checkbox.
   * 
   * @return Checkbox.
   */
  private CheckboxInput getOpenFile()
  {
    if (this.openFile == null)
    {
      this.openFile = new CheckboxInput(settings.getBoolean("open", true));
    }
    return this.openFile;
  }

  /**
   * Liefert eine Liste der verfuegbaren Exporter.
   * 
   * @return Liste der Exporter.
   * @throws Exception
   */
  private Input getExporterList() throws Exception
  {
    if (exporterListe != null)
    {
      return exporterListe;
    }

    Exporter[] exporters = IORegistry.getExporters();

    int size = 0;
    ArrayList<Exp> l = new ArrayList<Exp>();
    String lastFormat = settings.getString("lastformat", null);
    Exp selected = null;

    for (int i = 0; i < exporters.length; ++i)
    {
      Exporter exp = exporters[i];
      if (exp == null)
      {
        continue;
      }
      IOFormat[] formats = exp.getIOFormats(type);
      if (formats == null || formats.length == 0)
      {
        Logger.debug("exporter " + exp.getName()
            + " provides no export formats for " + type + " skipping");
        continue;
      }
      for (int j = 0; j < formats.length; ++j)
      {
        size++;
        Exp e = new Exp(exp, formats[j]);
        l.add(e);

        String lf = e.format.getName();
        if (lastFormat != null && lf != null && lf.equals(lastFormat))
        {
          selected = e;
        }
      }
    }

    if (size == 0)
    {
      exporterListe = new LabelInput(i18n.tr("Keine Export-Filter verfügbar"));
      return exporterListe;
    }

    Collections.sort(l);
    Exp[] exp = l.toArray(new Exp[size]);
    exporterListe = new SelectInput(PseudoIterator.fromArray(exp), selected);
    return exporterListe;
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
   * Hilfsklasse zur Anzeige der Exporter.
   */
  private class Exp implements GenericObject, Comparable<Object>
  {

    private Exporter exporter = null;

    private IOFormat format = null;

    private Exp(Exporter exporter, IOFormat format)
    {
      this.exporter = exporter;
      this.format = format;
    }

    /**
     * @see de.willuhn.datasource.GenericObject#getAttribute(java.lang.String)
     */
    @Override
    public Object getAttribute(String arg0)
    {
      return this.format.getName();
    }

    /**
     * @see de.willuhn.datasource.GenericObject#getAttributeNames()
     */
    @Override
    public String[] getAttributeNames()
    {
      return new String[] { "name" };
    }

    /**
     * @see de.willuhn.datasource.GenericObject#getID()
     */
    @Override
    public String getID()

    {
      return this.exporter.getClass().getName() + "#" + this.format.getName();
    }

    /**
     * @see de.willuhn.datasource.GenericObject#getPrimaryAttribute()
     */
    @Override
    public String getPrimaryAttribute()
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
      {
        return false;
      }
      return this.getID().equals(arg0.getID());
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Object o)
    {
      if (o == null || !(o instanceof Exp))
      {
        return -1;
      }
      try
      {
        return this.format.getName().compareTo(((Exp) o).format.getName());
      }
      catch (Exception e)
      {
        // Tss, dann halt nicht
      }
      return 0;
    }
  }
}
