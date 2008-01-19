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
 **********************************************************************/

package de.jost_net.JVerein.Temporaer;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.jost_net.JVerein.rmi.Kursteilnehmer;
import de.jost_net.JVerein.rmi.Mitglied;
import de.jost_net.JVerein.server.DBSupportH2Impl;
import de.jost_net.JVerein.server.DBSupportMcKoiImpl;
import de.jost_net.JVerein.server.JVereinDBServiceImpl;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.messaging.Message;
import de.willuhn.jameica.messaging.MessageConsumer;
import de.willuhn.jameica.messaging.SystemMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * Bei der Migration von McKoi zu H2 gab es auf meinem Rechner Probleme mit der
 * Zeitzone. Daher wurden alle Datumsfelder um einen Tag zurückgesetzt. Diese
 * Klasse repariert die Angaben.
 */
public class DateRepair implements MessageConsumer
{
  public boolean autoRegister()
  {
    return false;
  }

  public Class[] getExpectedMessageTypes()
  {
    return new Class[] { SystemMessage.class };
  }

  public void handleMessage(Message message) throws Exception
  {
    if (message == null || !(message instanceof SystemMessage))
    {
      return;
    }

    if (((SystemMessage) message).getStatusCode() != SystemMessage.SYSTEM_STARTED)
    {
      return;
    }
    if (!Application.getCallback().askUser("Datumsangaben reparieren"))
    {
      return;
    }
    Logger.warn("Reparatur der Datumsangaben");

    McKoiDBServiceImpl mckoi = new McKoiDBServiceImpl();
    mckoi.start();
    H2DBServiceImpl h2 = new H2DBServiceImpl();
    h2.start();
    try
    {
      DBIterator list = mckoi.createList(Mitglied.class);
      while (list.hasNext())
      {
        Mitglied mckoimitglied = (Mitglied) list.next();
        Mitglied h2mitglied = (Mitglied) h2.createObject(Mitglied.class,
            mckoimitglied.getID());
        if (h2mitglied == null)
        {
          System.out.println(mckoimitglied.getName()
              + mckoimitglied.getVorname());
        }
        else
        {
          System.out.println(h2mitglied.getName());
          h2mitglied.setGeburtsdatum(check(mckoimitglied.getVorname(),
              "Geburtsdatum", mckoimitglied.getGeburtsdatum(), h2mitglied
                  .getGeburtsdatum()));
          h2mitglied.setEintritt(check(mckoimitglied.getVorname(),
              "Eintrittsdatum", mckoimitglied.getEintritt(), h2mitglied
                  .getEintritt()));
          h2mitglied.setAustritt(check(mckoimitglied.getVorname(),
              "Austrittsdatum", mckoimitglied.getAustritt(), h2mitglied
                  .getAustritt()));
          h2mitglied.setKuendigung(check(mckoimitglied.getVorname(),
              "Kündigungsdatum", mckoimitglied.getKuendigung(), h2mitglied
                  .getKuendigung()));
          h2mitglied.store();
          System.out.println("-------------------------------------");
        }
      }

      list = mckoi.createList(Kursteilnehmer.class);
      while (list.hasNext())
      {
        Kursteilnehmer mckoikursteilnehmer = (Kursteilnehmer) list.next();
        Kursteilnehmer h2kursteilnehmer = (Kursteilnehmer) h2.createObject(
            Kursteilnehmer.class, mckoikursteilnehmer.getID());
        if (h2kursteilnehmer == null)
        {
          System.out.println(mckoikursteilnehmer.getName());
        }
        else
        {
          h2kursteilnehmer.setGeburtsdatum((check(
              mckoikursteilnehmer.getName(), "Geburtsdatum",
              mckoikursteilnehmer.getGeburtsdatum(), h2kursteilnehmer
                  .getGeburtsdatum())));
          h2kursteilnehmer.store();
        }
        System.out.println("-------------------------------------");
      }
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    }
    System.out.println("Fahre Datenbank herunter");
    h2.stop(false);
    mckoi.stop(false);
  }

  private Date check(String vorname, String feld, Date mckoi, Date h2)
  {
    if (mckoi == null && h2 != null)
    {
      System.out
          .println(vorname + ": " + feld + "mckoi ist null h2 aber nicht");
      return h2;
    }
    if (mckoi != null && h2 == null)
    {
      System.out
          .println(vorname + ": " + feld + "h2 ist null mckoi aber nicht");
      return h2;
    }
    if (mckoi == null && h2 == null)
    {
      return h2;
    }
    long differenz = mckoi.getTime() - h2.getTime();
    if (differenz > 0 && differenz <= 60 * 60 * 24 * 1000)
    {
      SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
      Calendar cal = Calendar.getInstance();
      cal.setTime(h2);
      cal.add(Calendar.HOUR, 24);
      cal.set(Calendar.HOUR, 0);
      System.out.println(vorname + ", " + feld + ": " + sdf.format(mckoi) + ":"
          + sdf.format(h2) + ", " + (mckoi.getTime() - h2.getTime()) + ":"
          + sdf.format(cal.getTime()));
      return new Date(cal.getTimeInMillis());
    }
    return h2;
  }

  /**
   * Wrapper des DB-Service, damit die Identifier gross geschrieben werden.
   */
  static class H2DBServiceImpl extends JVereinDBServiceImpl
  {
    private static final long serialVersionUID = 4298826411943981642L;

    public H2DBServiceImpl() throws RemoteException
    {
      super(DBSupportH2Impl.class.getName());

      // Der Konstruktor von DBSupportH2Impl hat bereits Gross-Schreibung
      // fuer HBCIDBService aktiviert - nochmal fuer die Migration
      // deaktivieren
      System.setProperty(JVereinDBServiceImpl.class.getName() + ".uppercase",
          "false");

      // Fuer uns selbst aktivieren wir es jedoch
      System
          .setProperty(H2DBServiceImpl.class.getName() + ".uppercase", "true");
    }
  }

  static class McKoiDBServiceImpl extends JVereinDBServiceImpl
  {
    private static final long serialVersionUID = -5809353547594245107L;

    public McKoiDBServiceImpl() throws RemoteException
    {
      super(DBSupportMcKoiImpl.class.getName());
    }
  }

}