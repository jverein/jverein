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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.Color;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;

/**
 * This class implements the view of the dialog CSV-Vorlagen
 * 
 * @author Ruediger (based on Christian's import dialog)
 * 
 */
public class AuswertungVorlagenCsvView extends AbstractView
{
  private Table csvTable = null;

  private Button saveButton = null;

  private Button saveAsButton = null;

  private Button loadButton = null;

  private Label vorlageNameLabel = null;

  private String fileName = ""; // nur Basename

  private String dirName = ""; // Vorlagenverzeichnis

  /**
   * Default Constructor
   * 
   */
  public AuswertungVorlagenCsvView()
  {
  }

  @Override
  public void bind() throws Exception
  {
    try
    {
      dirName = Einstellungen.getEinstellung().getVorlagenCsvVerzeichnis();
    }
    catch (RemoteException e)
    {
      dirName = "";
    }

    GUI.getView().setTitle("Vorlagen für Mitglieder-Auswertung");

    final Composite parent = this.getParent();
    parent.setLayout(new GridLayout(2, false));

    // ------------------------------------------------
    // Bereich mit Vorlagenverzeichnis und Vorlagename
    // ------------------------------------------------
    Composite compDir = new Composite(parent, SWT.BORDER);
    compDir.setLayout(new GridLayout(2, false));
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
    gridData.horizontalSpan = 2;
    compDir.setLayoutData(gridData);

    // Vorlagenverzeichnis
    Label label = new Label(compDir, SWT.NONE);
    label.setText("Vorlagenverzeichnis:");
    label.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));

    label = new Label(compDir, SWT.NONE);
    setVorlagenverzeichnisLabel(label);
    label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

    // Vorlagename
    label = new Label(compDir, SWT.NONE);
    label.setText("Vorlage:");
    label.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));

    vorlageNameLabel = new Label(compDir, SWT.NONE);
    vorlageNameLabel.setText("");
    vorlageNameLabel
        .setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

    // ------------------------------------------------
    // Bereich mit
    // links: Liste mit verfügbaren Feldnamen
    // rechts: Tabelle mit Zuordnung Feldname zu Username
    // ------------------------------------------------
    final List list = new List(parent, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
    GridData availdata = new GridData(SWT.FILL, SWT.FILL, true, true);
    // availdata.heightHint = 500;
    availdata.widthHint = 200;
    list.setLayoutData(availdata);

    Mitglied m = (Mitglied) Einstellungen.getDBService().createObject(
        Mitglied.class, null);
    Set<String> keysUnsorted = m.getMap(null).keySet();
    java.util.List<String> keysSorted = new ArrayList<String>(keysUnsorted);
    java.util.Collections.sort(keysSorted);

    list.setItems(keysSorted.toArray(new String[0]));

    // Tabelle mit Zuordnung Feldname zu Username
    csvTable = new Table(this.getParent(), SWT.SINGLE | SWT.BORDER);
    csvTable.clearAll();
    csvTable.setLinesVisible(true);
    csvTable.setHeaderVisible(true);
    GridData necData = new GridData(SWT.FILL, SWT.FILL, true, true);
    // necData.heightHint = 500;
    necData.widthHint = 400;
    csvTable.setLayoutData(necData);
    TableColumn col0 = new TableColumn(csvTable, SWT.NONE);
    TableColumn col1 = new TableColumn(csvTable, SWT.NONE);
    col0.setText("JVerein Datenbank-Feld");
    col1.setText("Spaltenbezeichnung in CSV-Datei");
    col0.pack();
    col1.pack();

    // ------------------------------------------------
    // Bereich mit Buttons
    // ------------------------------------------------
    // Anmerkung: Klasse ButtonArea wird nicht verwendet, da keine Möglichkeit
    // zum disable/enable der Buttons
    Composite compButtons = new Composite(parent, SWT.NONE);
    compButtons.setLayout(new RowLayout());

    loadButton = new Button(compButtons, SWT.PUSH);
    loadButton.setText("Vorlage laden...");
    loadButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        vorlageLaden();
      }
    });

    saveAsButton = new Button(compButtons, SWT.PUSH);
    saveAsButton.setText("Vorlage speichern unter...");
    saveAsButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        vorlageSpeichernUnter();
      }
    });

    saveButton = new Button(compButtons, SWT.PUSH);
    saveButton.setText("Vorlage speichern");
    saveButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        vorlageSpeichern(dirName + File.separator + fileName);
      }
    });

    GridData chooseData = new GridData(SWT.RIGHT, SWT.TOP, true, false);
    chooseData.horizontalSpan = 2;
    compButtons.setLayoutData(chooseData);
    compButtons.pack();

    // enable/disable der Buttons
    if (dirName.isEmpty())
    {
      // ungueltiges Vorlagenverzeichnis: Buttons sperren
      loadButton.setEnabled(false);
      saveAsButton.setEnabled(false);
    }
    // save immer anfangs sperren, da noch kein Dateiname
    saveButton.setEnabled(false);

    // ------------------------------------------------
    // Editor fuer Anwendername in der rechten Tabellenspalte
    // ------------------------------------------------
    final TableEditor editor = new TableEditor(csvTable);
    editor.horizontalAlignment = SWT.LEFT;
    editor.grabHorizontal = true;

    csvTable.addListener(SWT.MouseDown, new Listener()
    {
      @Override
      public void handleEvent(Event event)
      {
        Rectangle clientArea = csvTable.getClientArea();
        Point pt = new Point(event.x, event.y);
        int index = csvTable.getTopIndex();

        while (index < csvTable.getItemCount())
        {
          boolean visible = false;
          final TableItem item = csvTable.getItem(index);
          final int column = 1;
          Rectangle rect = item.getBounds(column);

          if (rect.contains(pt))
          {
            final Text text = new Text(csvTable, SWT.NONE);
            Listener textListener = new Listener()
            {
              @Override
              public void handleEvent(final Event e)
              {
                switch (e.type)
                {
                  case SWT.FocusOut:
                    if (!item.getText(column).equals(text.getText()))
                    {
                      item.setText(column, text.getText());
                      setDirty();
                    }
                    text.dispose();
                    break;
                  case SWT.Traverse:
                    switch (e.detail)
                    {
                      case SWT.TRAVERSE_RETURN:
                        if (!item.getText(column).equals(text.getText()))
                        {
                          item.setText(column, text.getText());
                          setDirty();
                        }
                        // FALL THROUGH
                      case SWT.TRAVERSE_ESCAPE:
                        text.dispose();
                        e.doit = false;
                    }
                    break;
                }
              }
            };
            text.addListener(SWT.FocusOut, textListener);
            text.addListener(SWT.Traverse, textListener);
            editor.setEditor(text, item, column);
            text.setText(item.getText(column));
            // text.selectAll();
            text.setFocus();
            return;
          }
          if (!visible && rect.intersects(clientArea))
          {
            visible = true;
          }
          if (!visible)
            return;
          index++;
        }
      }
    });

    // ------------------------------------------------
    // Eintrag entfernen via Taste ENTF
    // ------------------------------------------------
    csvTable.addKeyListener(new KeyAdapter()
    {

      @Override
      public void keyPressed(KeyEvent event)
      {
        if (event.keyCode == SWT.BS || event.keyCode == SWT.DEL)
        {
          int idx = csvTable.getSelectionIndex();
          if (idx >= 0)
          {
            csvTable.remove(idx);
            setDirty();
          }
        }
      }
    });

    // ------------------------------------------------
    // Drag And Drop
    // ------------------------------------------------
    DragSource ds = new DragSource(list, DND.DROP_MOVE);
    ds.setTransfer(new Transfer[] { TextTransfer.getInstance() });
    ds.addDragListener(new DragSourceAdapter()
    {

      @Override
      public void dragSetData(DragSourceEvent event)
      {
        // data: "L" als erstes Zeichen, dann Name des Feldes
        // Beispiel: "Lmitglied_vorname"
        event.data = "L" + list.getSelection()[0];
      }
    });

    DragSource dsT = new DragSource(csvTable, DND.DROP_MOVE);
    dsT.setTransfer(new Transfer[] { TextTransfer.getInstance() });
    dsT.addDragListener(new DragSourceAdapter()
    {

      @Override
      public void dragSetData(DragSourceEvent event)
      {
        // data: "T" als erstes Zeichen, dann Index der Tabellenzeile
        // Beispiel: "T12"
        event.data = "T" + csvTable.getSelectionIndex();
      }
    });

    // Drop target
    DropTarget dt = new DropTarget(csvTable, DND.DROP_MOVE);
    dt.setTransfer(new Transfer[] { TextTransfer.getInstance() });
    dt.addDropListener(new DropTargetAdapter()
    {

      @Override
      public void drop(DropTargetEvent event)
      {
        // Ermittle Source infos
        String csvname;
        String username = null;
        String data = (String) event.data;
        int sourceIndex = -1;
        if (data.startsWith("L"))
        {
          // Source ist Liste mit allen jverein keys
          csvname = data.substring(1);
        }
        else
        {
          // Source ist Tabelle: Anwender will Reihenfolge der Tabelleneinträge
          // ändern
          sourceIndex = Integer.parseInt(data.substring(1));
          TableItem sourceItem = csvTable.getItem(sourceIndex);
          csvname = sourceItem.getText(0);
          username = sourceItem.getText(1);
        }

        // Ermittle Target infos
        int targetIndex = csvTable.getItemCount(); // end of table
        TableItem targetItem = (TableItem) event.item;
        if (targetItem != null)
        {
          // Drop auf einen existierenden Eintrag
          targetIndex = csvTable.indexOf(targetItem);
        }

        // Prüfe ob Source bereits in der Tabelle
        if (sourceIndex == -1)
        {
          // Source war ein Listeneintag (links), kein Tabelleneintrag (rechts)
          TableItem[] items = csvTable.getItems();
          for (int i = 0; i < items.length; i++)
          {
            if (items[i].getText(0).equals(csvname))
            {
              // csvname ist bereits in der Tabelle: nicht erneute Eintragen
              csvTable.setSelection(i);
              return;
            }
          }
        }

        // neuer Eintrag
        addItem(csvname, username, targetIndex);

        // Source item löschen, falls Source ebenfalls Tabelle war (Reihenfolge)
        if (sourceIndex >= 0)
        {
          // beachte Source über oder unter Target?
          if (sourceIndex > targetIndex)
          {
            sourceIndex++;
          }
          csvTable.remove(sourceIndex);
        }
        setDirty();
      }
    });

    parent.pack();
  }

  private void setVorlagenverzeichnisLabel(Label label)
  {
    if (this.dirName.isEmpty())
    {
      // kein Verzeichnis unter Einstellungen angegeben
      label.setText("kein Verzeichnis gewählt (Einstellungen->Dateinamen)");
      label.setForeground(Color.ERROR.getSWTColor());
    }
    else
    {
      File dirFile = new File(dirName);
      if (!dirFile.isDirectory())
      {
        // ungültiges Verzeichnis unter Einstellungen angegeben
        label.setText("Verzeichnis ungültig: " + dirName);
        label.setForeground(Color.ERROR.getSWTColor());
        dirName = "";
      }
      else
      {
        // ok
        label.setText(dirName);
      }
    }
  }

  private void addItem(String csvname, String username, int index)
  {
    if (username == null)
    {
      int i = csvname.indexOf("_");
      // 1st char upper case, rest lower case
      // Example: "mitglied_vorname" -> "Vorname"
      username = csvname.substring(i + 1, i + 2).toUpperCase()
          + csvname.substring(i + 2);
    }
    if (index == -1)
    {
      index = csvTable.getItemCount();
    }
    TableItem item = new TableItem(csvTable, SWT.NONE, index);
    item.setText(0, csvname);
    item.setText(1, username);
  }

  private void vorlageLaden()
  {
    try
    {
      FileDialog d = new FileDialog(GUI.getShell(), SWT.OPEN);
      d.setText("Vorlagen-Datei");
      d.setFilterExtensions(new String[] { "*.csv" });
      d.setFilterPath(dirName);

      String vorlagedateiname = d.open();
      if (vorlagedateiname == null)
        return; // cancel

      File file = new File(vorlagedateiname);
      CsvListReader reader = new CsvListReader(new FileReader(file),
          CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
      String[] headerUser = reader.read().toArray(new String[0]);
      String[] headerKeys = reader.read().toArray(new String[0]);

      if (headerUser.length != headerKeys.length)
      {
        throw new IOException("Number of elements in first 2 lines mismatch");
      }

      // clean and fill the table
      csvTable.removeAll();
      for (int i = 0; i < headerKeys.length; i++)
      {
        addItem(headerKeys[i], headerUser[i], -1);
      }
      setFilename(vorlagedateiname);

    }
    catch (Exception e)
    {
      Logger.error("unable to load file", e);
      Application.getMessagingFactory().sendMessage(
          new StatusBarMessage("Fehler beim Laden einer Vorlagendatei: "
              + e.getMessage(), StatusBarMessage.TYPE_ERROR));
    }
  }

  private void vorlageSpeichernUnter()
  {
    FileDialog d = new FileDialog(GUI.getShell(), SWT.SAVE);
    d.setText("Vorlagen-Datei");
    d.setFilterExtensions(new String[] { "*.csv" });
    d.setFilterPath(dirName);

    String vorlagedateiname = d.open();
    if (vorlagedateiname == null)
      return; // cancel

    vorlageSpeichern(vorlagedateiname);
  }

  private void vorlageSpeichern(String vorlagedateiname)
  {
    try
    {
      File file = new File(vorlagedateiname);
      CsvListWriter writer = new CsvListWriter(new FileWriter(file),
          CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
      Vector<String> headerUser = new Vector<String>();
      Vector<String> headerKeys = new Vector<String>();

      for (int i = 0; i < csvTable.getItemCount(); i++)
      {
        TableItem item = csvTable.getItem(i);
        headerKeys.add(item.getText(0));
        headerUser.add(item.getText(1));
      }
      writer.write(headerUser); // 1st line: user names
      writer.write(headerKeys); // 2nd line: jverein keys
      writer.close();

      setFilename(vorlagedateiname);
    }
    catch (Exception e)
    {
      Logger.error("unable to write file", e);
      Application.getMessagingFactory().sendMessage(
          new StatusBarMessage("Fehler beim Speichern einer Vorlagendatei: "
              + e.getMessage(), StatusBarMessage.TYPE_ERROR));
    }
  }

  private void setFilename(String fname)
  {
    if (!fname.isEmpty())
    {
      // speichere nur den Basename, ohne Verzeichnis-Anteil
      fname = new File(fname).getName();
    }

    this.vorlageNameLabel.setText(fname);
    this.fileName = fname;
    saveButton.setEnabled(!fname.isEmpty());
  }

  private void setDirty()
  {
    this.vorlageNameLabel.setText(this.fileName + " *");
  }

  @Override
  public String getHelp()
  {
    return "<form><p><span color=\"header\" font=\"header\">Vorlagen für Mitglieder-Auswertung</span></p>"
        + "<p>Einzelne Felder mit Drag&amp;Drop in die rechte Tabelle ziehen.</p>"
        + "<p>Löschen mittels Taste ENTF.</p>"
        + "<p>Umsortieren der Tabelle mit Drag&amp;Drop.</p>"
        + "<p>Benennen der Felder direkt in der Tabelle.</p>" + "</form>";
  }

}
