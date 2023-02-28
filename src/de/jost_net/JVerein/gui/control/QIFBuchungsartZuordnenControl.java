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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.jost_net.JVerein.rmi.QIFImportPos;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.CurrencyFormatter;
import de.willuhn.jameica.gui.formatter.DateFormatter;
import de.willuhn.jameica.gui.formatter.TableFormatter;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.Column;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.gui.util.Font;
import de.willuhn.jameica.gui.util.SWTUtil;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class QIFBuchungsartZuordnenControl extends AbstractControl
{

  private TablePart distinctBuchartListTable;

  private TablePart posBeispielListTable;

  private SelectInput buchungsartInput;

  private TextInput externeBuchungsartInput;

  private CheckboxInput cbboxPosSperre;

  private CheckboxInput cbMitgliedZuordnenErlaubt;

  private DBIterator<QIFImportPos> iteratorQIFImportPosList;

  private QIFImportPos qifImportPos;

  private Image imageOk;

  private Image imageError;

  private Image imageLocked;

  public QIFBuchungsartZuordnenControl(AbstractView view)
  {
    super(view);
    init();
  }

  private void init()
  {
    imageOk = SWTUtil.getImage("ok.png");
    imageError = SWTUtil.getImage("stop-circle.png");
    imageLocked = SWTUtil.getImage("locked.png");
  }

  public CheckboxInput getMitgliedZuordnenErlaubt()
  {
    if (null == cbMitgliedZuordnenErlaubt)
    {
      cbMitgliedZuordnenErlaubt = new CheckboxInput(true);
      cbMitgliedZuordnenErlaubt
          .setComment("Darf diese Buchungsart im Mitgliedskonto buchen?");
    }
    return cbMitgliedZuordnenErlaubt;
  }

  public CheckboxInput getPosSperreInput()
  {
    if (null == cbboxPosSperre)
    {
      cbboxPosSperre = new CheckboxInput(false);
      cbboxPosSperre.setComment(" ");
      cbboxPosSperre.addListener(new Listener()
      {

        @Override
        public void handleEvent(Event event)
        {
          cbboxPosSperre_valueChanged();
        }
      });
    }
    return cbboxPosSperre;
  }

  private void cbboxPosSperre_valueChanged()
  {
    Boolean sperre = (Boolean) cbboxPosSperre.getValue();
    if (sperre.booleanValue())
    {
      buchungsartInput.setValue(null);
      buchungsartInput.disable();
      cbMitgliedZuordnenErlaubt.disable();
    }
    else
    {
      buchungsartInput.enable();
      cbMitgliedZuordnenErlaubt.enable();
    }
  }

  public Input getExterneBuchungsartInput()
  {
    if (null == externeBuchungsartInput)
    {
      externeBuchungsartInput = new TextInput("", 30);
      externeBuchungsartInput.disable();
    }
    return externeBuchungsartInput;
  }

  private void aktuallisierenExterneBuchartInput()
  {
    try
    {
      if (null != qifImportPos)
      {
        getExterneBuchungsartInput().setValue(qifImportPos.getQIFBuchart());
        return;
      }
    }
    catch (RemoteException ex)
    {
      Logger.error("Fehler: ", ex);
    }
    getExterneBuchungsartInput().setValue("");
  }

  public Part getPositionsListe() throws RemoteException
  {
    if (null == posBeispielListTable)
    {
      posBeispielListTable = new TablePart(iteratorQIFImportPosList, null);
      posBeispielListTable.addColumn("Datum", QIFImportPos.COL_DATUM,
          new DateFormatter(new JVDateFormatTTMMJJJJ()));
      posBeispielListTable.addColumn("Beleg", QIFImportPos.COL_BELEG);
      posBeispielListTable.addColumn("Betrag", QIFImportPos.COL_BETRAG,
          new CurrencyFormatter("", Einstellungen.DECIMALFORMAT), false,
          Column.ALIGN_RIGHT);
      posBeispielListTable.addColumn("Name", QIFImportPos.COL_NAME);
      posBeispielListTable.addColumn("Zweck", QIFImportPos.COL_ZWECK);
      posBeispielListTable.addColumn("Konto", QIFImportPos.VIEW_QIFKONTO_NAME);

      posBeispielListTable.setRememberColWidths(true);
      posBeispielListTable.setRememberOrder(true);
    }
    else
    {
      aktuallisiereQIFPosListAnzeige();
    }
    return posBeispielListTable;
  }

  private void aktuallisiereQIFPosListAnzeige() throws RemoteException
  {
    iteratorQIFImportPosList.begin();

    posBeispielListTable.removeAll();
    while (iteratorQIFImportPosList.hasNext())
    {
      QIFImportPos importPos = iteratorQIFImportPosList.next();
      posBeispielListTable.addItem(importPos);
    }
    posBeispielListTable.sort();
  }

  private void synchronisiereComboBoxBuchungsArt() throws RemoteException
  {
    Buchungsart buchungsArt = null;
    if (null != qifImportPos)
      buchungsArt = qifImportPos.getBuchungsart();
    buchungsartInput.setValue(buchungsArt);

  }

  private Buchungsart getAktuelleBuchart() throws RemoteException
  {
    if (null == qifImportPos)
      return null;
    return qifImportPos.getBuchungsart();
  }

  private void ladeQIFPosListFuerQIFBuchungsart(QIFImportPos importPos)
      throws RemoteException
  {
    String qifBuchart = importPos.getQIFBuchart();
    iteratorQIFImportPosList = Einstellungen.getDBService()
        .createList(QIFImportPos.class);
    if (null != qifBuchart)
      iteratorQIFImportPosList.addFilter(QIFImportPos.COL_QIF_BUCHART + " = ?",
          qifBuchart);
    else
      iteratorQIFImportPosList.addFilter(QIFImportPos.COL_POSID + " = ?",
          importPos.getID());
    iteratorQIFImportPosList.setOrder("ORDER BY " + QIFImportPos.COL_POSID);
  }

  public Input getBuchungsartInput() throws RemoteException
  {
    if (buchungsartInput != null && !buchungsartInput.getControl().isDisposed())
    {
      return buchungsartInput;
    }
    DBIterator<Buchungsart> list = Einstellungen.getDBService()
        .createList(Buchungsart.class);
    list.join("buchungsklasse");
    list.addFilter("buchungsklasse.id = buchungsart.buchungsklasse");
    list.setOrder(
        "ORDER BY buchungsklasse.bezeichnung, buchungsart.art, buchungsart.bezeichnung");
    buchungsartInput = new SelectInput(list, getAktuelleBuchart());
    buchungsartInput.setAttribute("klasse-art-bez");
    buchungsartInput.setPleaseChoose("Bitte auswählen");
    buchungsartInput.setComment(" ");
    return buchungsartInput;
  }

  public Action getSpeichernAction()
  {
    return new SpeichernAction();
  }

  class SpeichernAction implements Action
  {

    private Buchungsart storeBuchart;

    private Boolean storeSperre;

    private Boolean storeMitgliedbar;

    @Override
    public void handleAction(Object context) throws ApplicationException
    {
      try
      {
        getInputValues();
        if (checkInputValues())
        {
          speichernNeueBuchungsart();
          refreshSelectedQIFImportPos();
        }
      }
      catch (RemoteException ex)
      {
        throw new ApplicationException(
            "Buchungsart kann nicht gespeichert werden", ex);
      }
    }

    private void getInputValues()
    {
      storeMitgliedbar = (Boolean) cbMitgliedZuordnenErlaubt.getValue();
      storeBuchart = (Buchungsart) buchungsartInput.getValue();
      storeSperre = (Boolean) cbboxPosSperre.getValue();
      if (storeSperre.booleanValue())
      {
        storeBuchart = null;
        storeMitgliedbar = Boolean.FALSE;
      }
    }

    private boolean checkInputValues()
    {
      if (storeSperre.booleanValue())
        return true;

      if (null == storeBuchart)
      {
        GUI.getStatusBar().setErrorText("Keine JVerein Buchungsart gewählt!!");
        return false;
      }
      return true;
    }

    private void speichernNeueBuchungsart()
        throws RemoteException, ApplicationException
    {
      iteratorQIFImportPosList.begin();
      while (iteratorQIFImportPosList.hasNext())
      {
        QIFImportPos importPos = iteratorQIFImportPosList.next();
        importPos.setBuchungsart(storeBuchart);
        importPos.setGesperrt(storeSperre);
        importPos.setMitgliedZuordenbar(storeMitgliedbar);
        importPos.store();
      }
    }
  }

  private void refreshSelectedQIFImportPos() throws RemoteException
  {
    qifImportPos.load(qifImportPos.getID());
    distinctBuchartListTable.updateItem(qifImportPos, qifImportPos);
  }

  // ------------------------------ Test

  /**
   * Erstelle eine Liste mit QIFImportPos in der alle externen Buchungsarten
   * einmal vorhanden sind. Positionen ohne QIFBuchungsart werden alle in die
   * Liste aufgenommen
   * 
   * @throws RemoteException
   */
  private GenericIterator<?> getDistinctQIFBuchartList() throws RemoteException
  {
    DBIterator<QIFImportPos> it = Einstellungen.getDBService()
        .createList(QIFImportPos.class);
    it.setOrder("ORDER BY " + QIFImportPos.COL_QIF_BUCHART + ","
        + QIFImportPos.COL_POSID);

    List<QIFImportPos> l = new ArrayList<>();

    String letzteBuchart = "";
    while (it.hasNext())
    {
      QIFImportPos pos = it.next();
      String qifBuchart = pos.getQIFBuchart();
      if (null == qifBuchart)
      {
        letzteBuchart = "";
        l.add(pos);
      }
      else if (letzteBuchart.equals(qifBuchart) == false)
      {
        letzteBuchart = new String(qifBuchart);
        l.add(pos);
      }
    }

    return PseudoIterator.fromArray(l.toArray(new QIFImportPos[l.size()]));
  }

  public Part getBuchartListe() throws RemoteException
  {
    if (null == distinctBuchartListTable)
    {
      distinctBuchartListTable = new TablePart(getDistinctQIFBuchartList(),
          null);
      distinctBuchartListTable.addColumn("Externe Buchungsart",
          QIFImportPos.COL_QIF_BUCHART);
      distinctBuchartListTable.addColumn("Status", "Status");
      distinctBuchartListTable.addColumn("JVerein Buchungsart",
          QIFImportPos.VIEW_BUCHART_BEZEICHNER);
      distinctBuchartListTable.addColumn("Mitgliedskonto",
          QIFImportPos.VIEW_MITGLIED_BAR);

      distinctBuchartListTable.setRememberColWidths(true);
      distinctBuchartListTable.setRememberOrder(true);
      distinctBuchartListTable.setFormatter(new BuchartListTableFormater());
      distinctBuchartListTable
          .addSelectionListener(new BuchartListSelectionListener());
    }
    return distinctBuchartListTable;
  }

  class BuchartListSelectionListener implements Listener
  {

    @Override
    public void handleEvent(Event event)
    {
      selectionChanged_BuchartList();
    }
  }

  private void selectionChanged_BuchartList()
  {
    try
    {
      qifImportPos = (QIFImportPos) distinctBuchartListTable.getSelection();
      cbboxPosSperre.setValue(qifImportPos.getGesperrt());
      cbMitgliedZuordnenErlaubt.setValue(qifImportPos.getMitgliedZuordenbar());
      ladeQIFPosListFuerQIFBuchungsart(qifImportPos);
      aktuallisiereQIFPosListAnzeige();
      synchronisiereComboBoxBuchungsArt();
      aktuallisierenExterneBuchartInput();
      cbboxPosSperre_valueChanged();
    }
    catch (Throwable ex)
    {
      Logger.error("Fehler", ex);
    }
  }

  class BuchartListTableFormater implements TableFormatter
  {

    @Override
    public void format(TableItem item)
    {
      if (null == item)
        return;

      try
      {
        QIFImportPos pos = (QIFImportPos) item.getData();
        if (pos.getGesperrt())
        {
          item.setImage(1, imageLocked);
        }
        else if (pos.getBuchungsart() == null)
        {
          item.setImage(1, imageError);
          item.setFont(2, Font.ITALIC.getSWTFont());
          item.setText(2, "Selektiere Zeile und ordne zu!!");
        }
        else
          item.setImage(1, imageOk);
      }
      catch (Throwable ex)
      {
        Logger.error("Fehler", ex);
      }
    }
  }
}
