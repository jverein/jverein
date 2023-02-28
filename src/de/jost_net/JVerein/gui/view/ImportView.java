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
package de.jost_net.JVerein.gui.view;

import java.io.File;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.io.AssignedColumnsIO;
import de.jost_net.JVerein.io.CSVConnection;
import de.jost_net.JVerein.io.CSVFileHelper;
import de.jost_net.JVerein.io.Import;
import de.jost_net.JVerein.io.InternalColumns;
import de.jost_net.JVerein.rmi.Felddefinition;
import de.jost_net.JVerein.util.TableColumnReplacer;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.dialogs.SimpleDialog;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

/**
 * This class implements the view of the import dialog
 * 
 * @author Heiner
 * @author Christian
 * 
 */
public class ImportView extends AbstractView
{

  /* keeps all columns and their corresponding replacement saved */
  private TableColumnReplacer columns;

  /* Colors */
  private Color red;

  private Color green;

  /* Import File */
  private final CSVConnection csvConn;

  /* Assignements */
  private final AssignedColumnsIO acIO;

  /**
   * Default Constructor
   * 
   */
  public ImportView()
  {

    csvConn = new CSVConnection();
    acIO = new AssignedColumnsIO();
  }

  /**
   * add a new column, that is available in the program
   * 
   * @param table
   *          the content will be added to this table in the first column at the
   *          end
   * @param content
   *          content to be added
   * @param necessary
   *          if true the background will be red, else no change to the
   *          background
   */
  private void addColumnTableItemWithOrder(final Table table,
      final String content, final boolean necessary)
  {

    TableItem item = new TableItem(table, SWT.NONE);
    item.setText(0, content);

    if (necessary)
      item.setBackground(0, red);
  }

  /**
   * add to a defined TableItem the replacement column, and switch the
   * background from red to green.
   * 
   * @param curItem
   *          to this item the content will be placed in the second column
   * @param content
   *          to be added
   */
  private void addColumnReplacementTableItem(final TableItem curItem,
      final String content)
  {

    curItem.setText(1, content);
    if (curItem.getBackground(0).equals(red))
      curItem.setBackground(0, green);

  }

  /**
   * check the list of available columns, if one columns matches a internal
   * column, the will automatically assigned to each other
   * 
   * @param table
   *          with all internal items in the first column
   * @param availableColumns
   *          from the import file
   */
  private void autoColumnsAssignment(final Table table,
      final java.util.List<String> availableColumns)
  {
    TableItem[] items = table.getItems();

    for (TableItem item : items)
    {
      for (String column : availableColumns)
      {
        if (item.getText(0).equalsIgnoreCase(column))
        {

          addColumnReplacementTableItem(item, column);
          columns.setColumnReplacement(item.getText(0), column);
          break;
        }
      }
    }

    table.getColumn(1).pack();
  }

  @Override
  public void bind() throws Exception
  {

    GUI.getView().setTitle("Daten-Import");

    final Composite parent = this.getParent();
    parent.setLayout(new GridLayout(2, false));

    red = new Color(parent.getDisplay(), 255, 125, 125);
    green = new Color(parent.getDisplay(), 125, 255, 125);

    /* choose Import File and show the path in the label */
    Group loadComp = new Group(parent, SWT.NONE);
    loadComp.setLayout(new RowLayout());

    Button selectImportFile = new Button(loadComp, SWT.PUSH);
    selectImportFile.setText("Datei wählen");

    final Label showPathFile = new Label(loadComp, SWT.SHADOW_IN);

    GridData chooseData = new GridData(SWT.FILL, SWT.FILL, true, false);
    chooseData.horizontalSpan = 2;
    loadComp.setLayoutData(chooseData);
    loadComp.setVisible(true);
    loadComp.pack();

    /*
     * create table with all available column names not matter if necessary or
     * optional
     */
    /* define SWT Elements */
    final Table necTable = new Table(this.getParent(),
        SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
    necTable.clearAll();
    necTable.setLinesVisible(true);
    GridData necData = new GridData(SWT.FILL, SWT.FILL, true, true);
    necData.heightHint = 500;
    necData.widthHint = 300;
    necTable.setLayoutData(necData);
    new TableColumn(necTable, SWT.NONE);
    new TableColumn(necTable, SWT.NONE);
    resetAllAssignment(necTable);
    necTable.getColumn(0).pack();

    /* define action to remove a set assignment */
    necTable.addKeyListener(new KeyAdapter()
    {

      @Override
      public void keyPressed(KeyEvent event)
      {
        if (event.keyCode == SWT.BS || event.keyCode == SWT.DEL)
        {
          TableItem[] item = necTable.getSelection();

          /* if something is selected remove the assigned column */
          if (item.length > 0)
          {
            clearAssignment(item[0]);
          }
        }
      }
    });

    /*
     * Available columns from the import file
     */
    final List list = new List(parent, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
    GridData availdata = new GridData(SWT.FILL, SWT.FILL, true, true);
    availdata.heightHint = 500;
    availdata.widthHint = 200;
    list.setLayoutData(availdata);

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.IMPORT, false, "question-circle.png");

    /* Import Button */
    de.willuhn.jameica.gui.parts.Button importbt = new de.willuhn.jameica.gui.parts.Button(
        "Import", new Action()
        {
          @Override
          public void handleAction(Object context)
          {
            doImport();
          }
        });
    buttons.addButton(importbt);
    buttons.paint(getParent());
    /*
     * add all column headers found in the csv file to the available column
     * list.
     */
    selectImportFile.addSelectionListener(new SelectionAdapter()
    {

      @Override
      public void widgetSelected(SelectionEvent e)
      {
        loadFromImportFile(parent, showPathFile, necTable, list);
      }
    });

    /*
     * Drag And Drop
     * 
     * Create the drag source from the available list
     */
    DragSource ds = new DragSource(list, DND.DROP_MOVE);
    ds.setTransfer(new Transfer[] { TextTransfer.getInstance() });
    ds.addDragListener(new DragSourceAdapter()
    {

      @Override
      public void dragSetData(DragSourceEvent event)
      {
        // Set the data to be the first selected item's text
        event.data = list.getSelection()[0];
      }
    });

    /* Create drop target table */
    DropTarget dt = new DropTarget(necTable, DND.DROP_MOVE);
    dt.setTransfer(new Transfer[] { TextTransfer.getInstance() });
    dt.addDropListener(new DropTargetAdapter()
    {

      @Override
      public void drop(DropTargetEvent event)
      {
        TableItem curItem = (TableItem) event.item;
        if (curItem != null)
        { // only if a item is selected
          String movedItem = (String) event.data;
          addColumnReplacementTableItem(curItem, movedItem);
          columns.setColumnReplacement(curItem.getText(0), movedItem);
          acIO.writeAssignments(columns);
          necTable.getColumn(1).pack();
        }
      }
    });
    parent.pack();
  }

  /**
   * Clear a specified assignment
   * 
   * @param item
   *          the replacement in the second table will be removed, if the
   *          background was green it will be changed back to red
   */
  private void clearAssignment(final TableItem item)
  {
    if (item == null)
      return;

    if (item.getText(1).length() > 0)
    {
      item.setText(1, "");
      if (item.getBackground(0).equals(green))
      {
        item.setBackground(0, red);
      }
    }

    /* remove already defined replacement */
    columns.setColumnReplacement(item.getText(0), null);
  }

  /**
   * Importiert die Daten.
   */
  private void doImport()
  {
    /* Pruefe ob all notwendigen Spalten definiert sind */
    if (!columns.allNecessaryColumnsAvailable())
    {
      SimpleDialog sd = new SimpleDialog(AbstractDialog.POSITION_CENTER);
      sd.setText("Es wurden nicht alle Pflichtfelder (rot) festgelegt");
      sd.setTitle("Fehler");
      try
      {
        sd.open();
      }
      catch (Exception e)
      {
        Logger.error("Fehler", e);
      }
      return;
    }

    /* Sicherheitsnachfrage */
    YesNoDialog ynd = new YesNoDialog(AbstractDialog.POSITION_CENTER);
    ynd.setText("Achtung! Existierende Daten werden gelöscht. Weiter?");
    ynd.setTitle("Import");
    Boolean choice;
    try
    {
      choice = (Boolean) ynd.open();
      if (!choice.booleanValue())
        return;
    }
    catch (Exception e1)
    {
      Logger.error("Fehler", e1);
    }

    /* Wenn diese bestaetigt wurde dann wird der Import gestartet */
    BackgroundTask t = new BackgroundTask()
    {

      @Override
      public void run(ProgressMonitor monitor) throws ApplicationException
      {
        String s = csvConn.getFileName();

        CSVFileHelper cvsHelper = new CSVFileHelper();

        try
        {
          HashMap<String, String> colMap = new HashMap<>();
          colMap.putAll(columns.getNecessaryColumns());
          colMap.putAll(columns.getOptionalColumns());

          csvConn.openCsvDB();

          if (cvsHelper.checkCSVIntegrity(monitor, csvConn.getCSVFile()))
          {

            Import imp = new Import(monitor);
            imp.importFile(csvConn.getData(), csvConn.getNumberOfRows(),
                colMap);

            monitor.setPercentComplete(100);
            monitor.setStatus(ProgressMonitor.STATUS_DONE);
            GUI.getStatusBar()
                .setSuccessText(String.format("Daten importiert aus %s", s));
            GUI.getCurrentView().reload();
          }
          else
          {
            monitor.setStatus(ProgressMonitor.STATUS_ERROR);
            GUI.getStatusBar().setErrorText("Fehler in der Import Datei");
          }
          csvConn.closeCsvDB();
        }
        catch (SQLException sqlE)
        {
          monitor.setStatus(ProgressMonitor.STATUS_ERROR);
          Logger.error("error opening reading objects from " + s, sqlE);
          ApplicationException ae = new ApplicationException(
              String.format("Fehler beim Importieren der Daten aus %s, %s", s,
                  sqlE.getMessage()));
          monitor.setStatusText(ae.getMessage());
          GUI.getStatusBar().setErrorText(ae.getMessage());
          throw ae;
        }
        catch (ApplicationException ae)
        {
          monitor.setStatusText(ae.getMessage());
          monitor.setStatus(ProgressMonitor.STATUS_ERROR);
          GUI.getStatusBar().setErrorText(ae.getMessage());
          monitor.log("Abbruch des Imports!");
          throw ae;
        }
        catch (Exception e)
        {
          monitor.setStatus(ProgressMonitor.STATUS_ERROR);
          Logger.error("error while reading objects from " + s, e);
          ApplicationException ae = new ApplicationException(
              String.format("Fehler beim Importieren der Daten aus %s, %s", s,
                  e.getMessage()));
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
   * Find all columns with name starting with "Eigenschaft_"
   * 
   * @param columns
   *          all available Columns from the import file
   * @return all Eigenschaft columns
   */
  private java.util.List<String> getEigenschaftFields(
      final java.util.List<String> columns)
  {
    LinkedList<String> eigenschaftCol = new LinkedList<>();

    for (String column : columns)
    {
      if (column.startsWith("Eigenschaft_"))
      {
        eigenschaftCol.add(column);
      }
    }

    return eigenschaftCol;
  }

  /**
   * load all Data from Import File
   * 
   * @param parent
   * @param showPathFile
   * @param necTable
   * @param list
   */
  private void loadFromImportFile(final Composite parent,
      final Label showPathFile, final Table necTable, final List list)
  {
    try
    {
      resetAllAssignment(necTable);

      File importFile = selectFile(parent.getShell(), SWT.OPEN,
          "Bitte wählen Sie die Import-Datei aus.", "lastdir");

      if (importFile != null)
      {
        showPathFile.setText(importFile.getAbsolutePath());
        showPathFile.pack();

        if (importFile.getAbsolutePath().indexOf(' ') > 0)
        {
          GUI.getStatusBar().setErrorText(
              "Der Dateiname, bzw. der absolute Pfad, enthält Leerzeichen. Das ist nicht zulässig!");
          return;
        }

        /*
         * in the rare case, that a import File does have columns with the same
         * name it is necessary to ensure that they are unique bevor they will
         * be opened via the CSVConnection class. The helper class is checking
         * exactly this condition and if they aren't unique a temp file with
         * unique columns will be created.
         */
        CSVFileHelper csvHelper = new CSVFileHelper();
        importFile = csvHelper.replaceDuplicateColumn(importFile);

        /* set import file */
        if (csvConn.setCSVFile(importFile))
        {
          try
          {
            csvConn.openCsvDB();
            setLoadedItems(csvConn.getColumnHeaders(), necTable, list);
            columns = acIO.readAssignments(csvConn.getColumnHeaders(), columns);
            csvConn.closeCsvDB();
            updateColumnTable(necTable);
          }
          catch (SQLException e1)
          {
            GUI.getStatusBar().setErrorText(
                "Fehler - SQL Fehler beim lesen der Import Datei");
          }
        }
        else
        {
          GUI.getStatusBar()
              .setErrorText("Fehler - Formatierungsfehler in der Import Datei");
        }
      }
      else
      {
        GUI.getStatusBar()
            .setErrorText("Fehler - Import Datei existiert nicht");
      }
    }
    catch (RemoteException e2)
    {
      GUI.getStatusBar().setErrorText(
          "Fehler - Table Reset war nicht moeglich, bitte JVerein neustarten");
    }
  }

  /**
   * reset all assignments, only the original values survive, and no assignment
   * at all.
   * 
   * @param table
   *          this table will be reseted, too
   * @throws RemoteException
   */
  private void resetAllAssignment(final Table table) throws RemoteException
  {
    /*
     * create the container with all the columns, where their corresponding
     * names will be saved
     */
    columns = new TableColumnReplacer(InternalColumns.getNecessaryColumns(),
        InternalColumns.getOptionalColumns());

    table.removeAll();

    /* necessary fields */
    for (String tmp : InternalColumns.getNecessaryColumns())
    {
      addColumnTableItemWithOrder(table, tmp, true);
    }

    /* Optional fields */
    for (String tmp : InternalColumns.getOptionalColumns())
    {
      addColumnTableItemWithOrder(table, tmp, false);
    }

    /* Additional fields */
    DBIterator<Felddefinition> it = Einstellungen.getDBService()
        .createList(Felddefinition.class);
    for (int i = 0; i < it.size(); i++)
    {
      String addColumn = ( it.next()).getName();
      addColumnTableItemWithOrder(table, addColumn, true);
      columns.addColumn(addColumn, true);
    }

    /*
     * Note: just for clarification, this description doesn't explain the follow
     * code it is just a place holder for some code which is actually placed in
     * the import file selection listener
     * 
     * after selecting the import file it will be check if any "Eigenschaft"
     * columns are present if so they will be added to this table and the
     * assignment can't be modified.
     */

  }

  /**
   * This method opens a defined Dialog, where you may select a file. This file
   * will be returned.
   * 
   * @param parent
   *          a Shell to which the dialog is connected with
   * @param tagOpenClose
   *          define either SWT.OPEN or SWT.SAVE
   * @param dialogText
   *          this text appears in the dialog itself
   * @param settingsVariable
   *          to define where the last path will be saved for the next time.
   */
  private File selectFile(final Shell parent, final int tagOpenClose,
      final String dialogText, final String settingsVariable)
  {
    Settings settings = new Settings(this.getClass());
    settings.setStoreWhenRead(true);

    FileDialog fd = new FileDialog(parent, tagOpenClose);
    fd.setText(dialogText);

    String path = settings.getString(settingsVariable,
        System.getProperty("user.home"));
    if (path != null && path.length() > 0)
    {
      fd.setFilterPath(path);
    }
    final String s = fd.open();

    if (s == null || s.length() == 0)
    {
      GUI.getStatusBar()
          .setErrorText("Abbruch - Es wurde keine gueltige Datei gewaehlt");
      return null;
    }

    final File file = new File(s);
    if (tagOpenClose == SWT.OPEN && (!file.exists() || !file.isFile()))
    {
      GUI.getStatusBar().setErrorText("Abbruch - Datei existiert nicht");
      return null;
    }
    settings.setAttribute(settingsVariable, file.getParent());
    return file;
  }

  /**
   * add columns read from the import to a swt.List and auto assign matching
   * columns also find Eigenschaft fields and add the to the TableColumnReplacer
   * 
   * @param importColumns
   *          columns red from the import file
   * @param necTable
   *          the table where the columns get auto assigned to
   * @param list
   *          to this list all columns will be added
   */
  private void setLoadedItems(final java.util.List<String> importColumns,
      final Table necTable, final List list)
  {
    if (importColumns == null)
    {
      GUI.getStatusBar().setErrorText("Import Datei existiert nicht");
      return;
    }

    if (importColumns.size() == 0)
    {
      GUI.getStatusBar()
          .setErrorText("Konnte keine Spalten in der Import-Datei finden");
      return;
    }

    /* load items into the list */
    list.removeAll();
    for (String item : importColumns)
    {
      list.add(item);
    }

    /* check if -Eigenschaft_xxx- columns are available */
    for (String eigenCol : getEigenschaftFields(importColumns))
    {
      addColumnTableItemWithOrder(necTable, eigenCol, true);
      columns.addColumn(eigenCol, true);
    }
    necTable.getColumn(0).pack();

    /*
     * auto assignment assigns columns with the same name this has to be
     * executed after the check above, else the Eigenschafts columns could be
     * considered during the auto assignment
     */
    autoColumnsAssignment(necTable, importColumns);
  }

  /**
   * this table will be updated based on the TableColumnReplacer
   * 
   * @param table
   */
  private void updateColumnTable(final Table table)
  {
    TableItem[] items = table.getItems();
    for (TableItem item : items)
    {
      String newReplCol = columns.getReplacementForColumn(item.getText(0));

      /* if the text changed provide the changes to the table */
      if (newReplCol != null && item.getText(1) != null
          && !newReplCol.equals(item.getText(1)))
      {
        addColumnReplacementTableItem(item, newReplCol);
      }
    }

    table.getColumn(0).pack();
    table.getColumn(1).pack();
  }
}
