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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.Messaging.QIFImportHeaderMessage;
import de.jost_net.JVerein.rmi.QIFImportHead;
import de.jost_net.JVerein.rmi.QIFImportPos;
import de.jost_net.JVerein.server.QIFImportHeadImpl;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.ProgressMonitor;

/***
 * Diese Klasse Importiert eine aus Quicken exportiere Datei in die Tabellen
 * QIFImportHead und QIFImportPos
 * 
 * Eine QIF Datei darf nur ein Konto aus Quicken enthalten, weil Quicken einen
 * unvollständigen Export liefert, wenn mehrere Konten in eine Datei exportiert
 * werden. Dieser Fehler konnte mit unterschiedlichen Versionen von Quicken nach
 * vollzogen werden.
 * 
 * Es wird nicht das komplette Format unterstützt aber die ( hoffentlich )
 * wesentlichen Teile die vor allem empirisch ermittelt wurden.
 * 
 * Eine Beschreibung des Formates bekomment man hier
 * http://en.wikipedia.org/wiki/Quicken_Interchange_Format
 * 
 * @author Rolf Mamat
 * 
 */
public class QIFQuickenImport implements Importer
{
  enum QIF_TYPES
  {
    CASH("!Type:Cash"), BANK("!Type:Bank"), CCARD("!Type:CCard"),
    // INVST("!Type:Invst"),
    OTH_A("!Type:Oth A"), OTH_L("!Type:Oth L")
    // , INVOICE("!Type:Invoice")
    ;

    private final String code;

    private QIF_TYPES(String code)
    {
      this.code = code;
    }

    public boolean isType(final String text)
    {
      if (null == text)
        return false;
      return text.startsWith(code);
    }
  }

  private AbstractDBObject transactionHolder;

  private BufferedReader br = null;

  private String dateiName;

  private DecimalFormat decimalFormat = null;

  private int anzahlKonten;

  private int anzahlBuchungen;

  private int prozentFertig;

  @Override
  public String getName()
  {
    return "Quicken Export Datei";
  }

  @Override
  public IOFormat[] getIOFormats(Class<?> objectType)
  {
    if (objectType != QIFImportHead.class)
    {
      return null;
    }
    return new IOFormat[] { new QIFIOFormat() };
  }

  class QIFIOFormat implements IOFormat
  {

    @Override
    public String getName()
    {
      return QIFQuickenImport.this.getName();
    }

    @Override
    public String[] getFileExtensions()
    {
      return new String[] { "*.qif" };
    }

  }

  @Override
  public void doImport(Object context, IOFormat format, File file,
      String encoding, ProgressMonitor monitor) throws Exception
  {
    try
    {
      DBTransactionStart();

      initImport(monitor);

      monitor.setPercentComplete(10);
      monitor.setStatusText("Importiere neue Daten...");
      openReader(file);
      inhaltEinlesen(monitor);

      monitor.setStatusText("Importiere beenden...");

      DBTransactionCommit();
      monitorFertig(monitor);
    }
    catch (Exception ex)
    {
      DBTransactionRollback();
      throw ex;
    }
    finally
    {
      closeReader();
      sendFinishMessage();
    }
  }

  private void sendFinishMessage()
  {
    Application.getMessagingFactory().sendMessage(new QIFImportHeaderMessage());
  }

  private void initImport(ProgressMonitor monitor)
  {
    monitor.setStatus(ProgressMonitor.STATUS_RUNNING);
    monitor.setStatusText("Initialisieren...");
    monitor.setPercentComplete(5);

    anzahlBuchungen = 0;
    anzahlKonten = 0;
    prozentFertig = 10;
  }

  /**
   * Einlesen der QIF Datei.
   * 
   * Wir suchen erst eine Account Block - erste Zeile hat Text "!Account" Haben
   * wir diesen gefunden suchen wir den Typ Block - erste Zeile hat Text
   * "!Type:" Danach kommen die Buchungen für dieses Konto bis eine Zeile mit
   * "!" startet.
   * 
   * @param monitor
   * @throws RemoteException
   * @throws ApplicationException
   */
  private void inhaltEinlesen(ProgressMonitor monitor)
      throws RemoteException, ApplicationException
  {
    QIFBlock qifBlock = new QIFBlock();
    QIFType qifTyp = null;
    ArrayList<QIFKonto> kontoListe = new ArrayList<>();
    ArrayList<QIFType> typeListe = new ArrayList<>();

    while (qifBlock.readBlock())
    {
      monitorSpeichern(monitor);
      if (null != qifTyp)
      {
        QIFBuchung qifBuchung = (QIFBuchung) qifBlock.process(new QIFBuchung());
        if (null != qifBuchung)
        {
          speichernBuchung(qifTyp, qifBuchung);
          continue;
        }
        qifTyp = null;
      }

      if (kontoListe.isEmpty())
      {
        QIFKonto konto = (QIFKonto) qifBlock.process(new QIFKonto());
        if (null != konto)
        {
          kontoListe.add(konto);
          kontoBlockEinlesen(qifBlock, kontoListe);
        }
      }

      if (null == qifTyp)
      {
        qifTyp = (QIFType) qifBlock.process(new QIFType());
        if (null != qifTyp)
        {
          if (typeListe.isEmpty() == false)
          {
            Logger.error(
                "Die Datei enthält mehrere Konten. Weil Quicken Fehler bei solchen Exporten hat unterstützen wir dies nicht!");
            throw new ApplicationException(
                "Datei enthält Daten von mehreren Konten!! Bitte jedes Konto einzelen Exportieren!!");
          }

          loeschenAlteKontoDaten(qifTyp);
          QIFKonto qifKonto = findeKontoZuTyp(kontoListe, qifTyp);
          speichernKonto(qifKonto, qifTyp);
          typeListe.add(qifTyp);
        }
      }
    }
  }

  private QIFKonto findeKontoZuTyp(ArrayList<QIFKonto> kontoListe,
      QIFType qifTyp)
  {
    for (QIFKonto konto : kontoListe)
    {
      if (qifTyp.kontoName.indexOf(konto.name) >= 0)
        return konto;
    }
    return null;
  }

  private void kontoBlockEinlesen(QIFBlock qifBlock,
      ArrayList<QIFKonto> kontoListe) throws ApplicationException
  {
    while (qifBlock.readBlock())
    {
      QIFKonto konto = (QIFKonto) qifBlock.process(new QIFKonto(true));
      if (null == konto)
        break;
      kontoListe.add(konto);
    }
  }

  private void monitorFertig(ProgressMonitor monitor)
  {
    monitor.setStatusText(
        String.format("Fertig! Importiert wurden %d Konten mit %d Buchungen.",
            anzahlKonten, anzahlBuchungen));
    monitor.setStatus(ProgressMonitor.STATUS_DONE);
  }

  private void monitorSpeichern(ProgressMonitor monitor)
  {
    monitor
        .setStatusText(String.format("Importiere %d Konten mit %d Buchungen..",
            anzahlKonten, anzahlBuchungen));
    if (prozentFertig < 100)
    {
      if (anzahlBuchungen % 100 == 0)
      {
        ++prozentFertig;
        monitor.setPercentComplete(prozentFertig);
      }
    }
  }

  private void speichernBuchung(QIFType qifKonto, QIFBuchung qifBuchung)
      throws RemoteException, ApplicationException
  {
    QIFImportPos importPos = (QIFImportPos) Einstellungen.getDBService()
        .createObject(QIFImportPos.class, null);
    importPos.setBeleg(qifBuchung.beleg);
    importPos.setBetrag(getAsDouble(qifBuchung.betrag));
    importPos.setDatum(getAsDate(qifBuchung.datum));
    importPos.setName(qifBuchung.person);
    importPos.setQIFBuchart(qifBuchung.buchungsArt);
    importPos.setQIFImpHead(qifKonto.qifImportHead);
    importPos.setZweck(qifBuchung.bemerkung);
    importPos.store();

    qifBuchung.qifKonto = qifKonto;
    ++anzahlBuchungen;
  }

  private void loeschenAlteKontoDaten(QIFType qifType)
      throws RemoteException, ApplicationException
  {
    DBIterator<QIFImportHead> headerList = Einstellungen.getDBService()
        .createList(QIFImportHead.class);
    headerList.addFilter(QIFImportHead.COL_NAME + " = ?",
        qifType.getKontoName());
    while (headerList.hasNext())
    {
      QIFImportHead importHead = headerList.next();
      loeschenQIFImportPos(importHead);
      importHead.delete();
    }
  }

  private void loeschenQIFImportPos(QIFImportHead importHead)
      throws RemoteException, ApplicationException
  {
    DBIterator<QIFImportPos> it = Einstellungen.getDBService()
        .createList(QIFImportPos.class);
    it.addFilter(QIFImportPos.COL_HEADID + " = ?", importHead.getID());
    while (it.hasNext())
    {
      QIFImportPos a = (QIFImportPos) it.next();
      a.delete();
    }
  }

  private void speichernKonto(QIFKonto qifKonto, QIFType qifType)
      throws RemoteException, ApplicationException
  {
    QIFImportHead importHead = (QIFImportHead) Einstellungen.getDBService()
        .createObject(QIFImportHead.class, null);
    importHead.setImportDatum(new Date());
    importHead.setImportFile(dateiName);
    importHead.setName(qifType.getKontoName());
    importHead.setStartDate(getAsDate(qifType.datum));
    importHead.setStartSaldo(getAsDouble(qifType.eroeffnungsSaldo));

    if (null != qifKonto)
    {
      importHead.setBeschreibung(qifKonto.beschreibung);
    }
    importHead.store();

    qifType.qifImportHead = importHead;
    ++anzahlKonten;
  }

  private double getAsDouble(String betrag) throws ApplicationException
  {
    try
    {
      return getDecimalFormat().parse(betrag).doubleValue();
    }
    catch (ParseException ex)
    {
      throw new ApplicationException("Falsches Format beim Betrag: " + betrag);
    }
  }

  /**
   * IN Quicken sind die Zahlen mit Dezimalpunkt und Tausender Komma formatiert.
   * Beispiel: 7,669.38
   * 
   */
  private DecimalFormat getDecimalFormat()
  {
    if (null == decimalFormat)
    {
      DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
      dfs.setDecimalSeparator('.');
      dfs.setGroupingSeparator(',');
      decimalFormat = new DecimalFormat("##,##0.00", dfs);

    }
    return decimalFormat;
  }

  private Date getAsDate(String datum) throws ApplicationException
  {
    try
    {
      return sdf.parse(datum);
    }
    catch (ParseException ex)
    {
      throw new ApplicationException(
          "Falsches Datumsformat gelesen : " + datum);
    }
  }

  private SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yy");

  private void closeReader()
  {
    try
    {
      if (null != br)
        br.close();
    }
    catch (Throwable ex)
    {
      Logger.error("Fehler", ex);
    }
  }

  private void openReader(File file) throws ApplicationException
  {
    try
    {
      dateiName = file.getPath();
      br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
    }
    catch (IOException ex)
    {
      throw new ApplicationException("Exportdatei kann nicht geöffnet werden.",
          ex);
    }
  }

  public boolean hasFileDialog()
  {
    return true;
  }

  class QIFBlock
  {
    ArrayList<String> blockDaten;

    public boolean readBlock() throws ApplicationException
    {
      try
      {
        blockDaten = new ArrayList<>();
        String zeile = null;
        while (null != (zeile = br.readLine()))
        {
          if (zeile.startsWith("^"))
            return true;
          blockDaten.add(zeile);
        }
      }
      catch (IOException ex)
      {
        throw new ApplicationException(
            "Daten können nicht aus der Datei gelesen werden.", ex);
      }
      return false;
    }

    public QIFItem process(QIFItem item)
    {
      if (datenBlockPasstZuItem(item) == false)
        return null;

      setzeItemDaten(item);
      return item;
    }

    private void setzeItemDaten(QIFItem item)
    {
      item.clear();
      for (String datenZeile : blockDaten)
      {
        item.setzeDaten(datenZeile.charAt(0), datenZeile.substring(1));
      }
    }

    private boolean datenBlockPasstZuItem(QIFItem item)
    {
      if (blockDaten == null)
        return false;
      if (blockDaten.isEmpty())
        return false;
      return item.isItem(blockDaten);
    }

  }

  class QIFKonto implements QIFItem
  {
    String name;

    String beschreibung;

    String type;

    boolean imBlock;

    public QIFKonto()
    {
      this(false);
    }

    public QIFKonto(boolean imBlock)
    {
      this.imBlock = imBlock;
    }

    @Override
    public void clear()
    {
      name = null;
      beschreibung = null;
      type = null;
    }

    @Override
    public void setzeDaten(char kennung, String daten)
    {
      switch (kennung)
      {
        case 'N':
          name = daten;
          break;
        case 'T':
          type = daten;
          break;
        case 'D':
          beschreibung = daten;
          break;
      }
    }

    @Override
    public boolean isItem(List<String> datenList)
    {
      for (String daten : datenList)
      {
        if (daten.startsWith("!Account"))
          return !imBlock;
      }
      return imBlock;
    }
  }

  class QIFType implements QIFItem
  {
    String datum;

    String eroeffnungsSaldo;

    String kontoName;

    QIFImportHead qifImportHead;

    @Override
    public void setzeDaten(char kennung, String daten)
    {
      switch (kennung)
      {
        case 'D':
          datum = daten;
          break;
        case 'T':
          eroeffnungsSaldo = daten;
          break;
        case 'L':
          kontoName = daten;
          break;
      }

    }

    @Override
    public void clear()
    {
      datum = null;
      eroeffnungsSaldo = null;
      kontoName = null;
      qifImportHead = null;
    }

    public String getKontoName()
    {
      StringBuilder stb = new StringBuilder(kontoName);
      if (stb.charAt(0) == '[')
      {
        stb.deleteCharAt(0);
        stb.deleteCharAt(stb.length() - 1);
      }
      return stb.toString();
    }

    @Override
    public boolean isItem(List<String> datenList)
    {
      String daten = datenList.get(0);
      for (QIF_TYPES typ : QIF_TYPES.values())
      {
        if (typ.isType(daten))
          return true;
      }
      return false;
    }
  }

  /**
   * Diese Klasse beschreibt eine einzelne Buchung aus der QIF Datei. Achtung!!
   * Wenn die Daten des Feldes Buchungsart = L in [] stehen dann ist das eine
   * Umbuchung von einem Konto auf das andere. Diese Buchung muss u.U. im
   * Gegenkonto gebucht werden. Aus Tests wurde herausgefunden: Wenn beide
   * Konten in selber Exportdatei dann fehlt die Gegenbuchung. Wenn beide Konten
   * in getrennte Exportdateien dann ist die Gegenbuchung im Export
   * vorhanden.!!!
   * 
   * @author Rolf Mamat
   */
  class QIFBuchung implements QIFItem
  {
    QIFType qifKonto;

    boolean istTransfereBuchung;

    String datum;

    String betrag;

    String beleg;

    String person;

    String bemerkung;

    String buchungsArt;

    @Override
    public void setzeDaten(char kennung, String daten)
    {
      switch (kennung)
      {
        case 'D':
          datum = daten;
          break;
        case 'T':
          betrag = daten;
          break;
        case 'N':
          beleg = getStringMaxLaenge(daten, 30);
          break;
        case 'P':
          person = getStringMaxLaenge(daten, 100);
          break;
        case 'M':
          bemerkung = getStringMaxLaenge(daten, 100);
          break;
        case 'L':
          buchungsArt = daten;
          pruefeAufKontenTransfere();
          break;
      }
    }

    private String getStringMaxLaenge(String wert, int maxLaenge)
    {
      if (null == wert)
        return wert;
      if (wert.length() <= maxLaenge)
        return wert;
      return wert.substring(0, maxLaenge);
    }

    public QIFBuchung getGegenbuchung()
    {
      QIFBuchung gegenbuchung = new QIFBuchung();
      gegenbuchung.datum = this.datum;
      gegenbuchung.beleg = this.beleg;
      gegenbuchung.bemerkung = this.bemerkung;
      gegenbuchung.person = this.person;
      gegenbuchung.buchungsArt = this.qifKonto.kontoName;

      if (this.betrag.startsWith("-"))
        gegenbuchung.betrag = this.betrag.substring(1);
      else
        gegenbuchung.betrag = "-" + this.betrag;
      return gegenbuchung;
    }

    public boolean istGegenbuchungVon(QIFBuchung listItem)
    {
      if (datum.equals(listItem.datum) == false)
        return false;
      if (betrag.equals(listItem.betrag) == false)
        return false;
      return qifKonto.kontoName.equals(buchungsArt);
    }

    /**
     * Es handelt sich um eine Transferebuchung, wenn bei Buchungsart L die
     * buchungsArt in [ ] stehen.
     */
    private void pruefeAufKontenTransfere()
    {
      if (null == buchungsArt)
        return;
      if (buchungsArt.startsWith("[") && buchungsArt.endsWith("]"))
        istTransfereBuchung = true;
    }

    public String gibGegenKontoName()
    {
      if (istTransfereBuchung == false)
        return null;
      return buchungsArt;
    }

    @Override
    public void clear()
    {
      datum = null;
      betrag = null;
      beleg = null;
      person = null;
      bemerkung = null;
      buchungsArt = null;
      istTransfereBuchung = false;
      qifKonto = null;
    }

    @Override
    public boolean isItem(List<String> datenList)
    {
      return datenList.get(0).startsWith("!") == false;
    }
  }

  interface QIFItem
  {
    public void clear();

    public void setzeDaten(char kennung, String daten);

    public boolean isItem(List<String> datenList);
  }

  private void DBTransactionStart() throws RemoteException
  {
    transactionHolder = (QIFImportHeadImpl) Einstellungen.getDBService()
        .createObject(QIFImportHead.class, null);
    transactionHolder.transactionBegin();
  }

  private void DBTransactionCommit() throws RemoteException
  {
    if (null != transactionHolder)
      transactionHolder.transactionCommit();
    transactionHolder = null;
  }

  private void DBTransactionRollback() throws RemoteException
  {
    if (null != transactionHolder)
      transactionHolder.transactionRollback();
    transactionHolder = null;
  }

}