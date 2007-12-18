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
 * Revision 1.9  2007/12/02 13:44:14  jost
 * Neu: Beitragsmodelle
 *
 * Revision 1.8  2007/12/01 10:07:45  jost
 * H2-Support
 *
 * Revision 1.7  2007/08/22 20:44:55  jost
 * Bug #011762
 *
 * Revision 1.6  2007/03/25 17:06:02  jost
 * Plausibilitätsprüfung der Bankverbindung bei Barzahlung abgeschaltet
 * Herstellung des Famlienverbandes.
 *
 * Revision 1.5  2007/03/10 20:29:16  jost
 * Neu: Zahlungsweg
 *
 * Revision 1.4  2007/03/10 13:45:38  jost
 * Vermerke eingefÃ¼hrt.
 *
 * Revision 1.3  2007/02/23 20:28:42  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.2  2006/10/21 09:19:48  jost
 * Zusätzliche Plausis
 *
 * Revision 1.1  2006/09/20 15:39:48  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;
import java.util.Date;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.input.ZahlungswegInput;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class MitgliedImpl extends AbstractDBObject implements Mitglied
{
  private static final long serialVersionUID = 1L;

  public MitgliedImpl() throws RemoteException
  {
    super();
  }

  protected String getTableName()
  {
    return "mitglied";
  }

  public String getPrimaryAttribute() throws RemoteException
  {
    return "id";
  }

  protected void deleteCheck() throws ApplicationException
  {
  }

  protected void insertCheck() throws ApplicationException
  {
    try
    {
      plausi();
    }
    catch (RemoteException e)
    {
      String fehler = "Mitglied kann nicht gespeichert werden. Siehe system log";
      Logger.error(fehler, e);
      throw new ApplicationException(fehler);
    }
  }

  private void plausi() throws RemoteException, ApplicationException
  {
    if (getName() == null || getName().length() == 0)
    {
      throw new ApplicationException("Bitte Namen eingeben");
    }
    if (getVorname() == null || getVorname().length() == 0)
    {
      throw new ApplicationException("Bitte Vornamen eingeben");
    }
    if (getGeburtsdatum() == null && Einstellungen.isGeburtsdatumPflicht())
    {
      throw new ApplicationException("Bitte Geburtsdatum eingeben");
    }
    if (getGeschlecht() == null)
    {
      throw new ApplicationException("Bitte Geschlecht auswählen");
    }
    if (getEintritt() == null && Einstellungen.isEintrittsdatumPflicht())
    {
      throw new ApplicationException("Bitte Eintrittsdatum eingeben");
    }
    if (getZahlungsweg() == ZahlungswegInput.ABBUCHUNG
        && getBeitragsgruppe().getBetrag() > 0)
    {
      if (getBlz() == null || getBlz().length() == 0 || getKonto() == null
          || getKonto().length() == 0)
      {
        throw new ApplicationException("Bitte Bankverbindung eingeben");
      }
      if (!Einstellungen.checkAccountCRC(getBlz(), getKonto()))
      {
        throw new ApplicationException("BLZ/Kontonummer (" + getBlz() + "/"
            + getKonto() + ") ungültig. Bitte prüfen Sie Ihre Eingaben.");
      }
    }
    if (getZahlungsrhytmus() != 12 && getZahlungsrhytmus() != 6
        && getZahlungsrhytmus() != 3 && getZahlungsrhytmus() != 1)
    {
      throw new ApplicationException("Ungültiger Zahlungsrhytmus: "
          + getZahlungsrhytmus());
    }

    if (getAustritt() != null || getKuendigung() != null)
    {
      // Person ist ausgetreten
      // Hat das Mitglied für andere gezahlt?
      if (getBeitragsgruppe().getBeitragsArt() == 1)
      {
        // ja
        DBIterator famang = Einstellungen.getDBService().createList(
            Mitglied.class);
        famang.addFilter("zahlerid = " + getID());
        if (famang.hasNext())
        {
          throw new ApplicationException(
              "Diese Mitglied zahlt noch für andere Mitglieder. Zunächst Beitragsart der Angehörigen ändern!");
        }
      }
    }
  }

  protected void updateCheck() throws ApplicationException
  {
    try
    {
      plausi();
    }
    catch (RemoteException e)
    {
      String fehler = "Mitglied kann nicht gespeichert werden. Siehe system log";
      Logger.error(fehler, e);
      throw new ApplicationException(fehler);
    }
  }

  protected Class getForeignObject(String field) throws RemoteException
  {
    if ("beitragsgruppe".equals(field))
    {
      return Beitragsgruppe.class;
    }
    return null;
  }

  public String getAnrede() throws RemoteException
  {
    return (String) getAttribute("anrede");
  }

  public void setAnrede(String anrede) throws RemoteException
  {
    setAttribute("anrede", anrede);
  }

  public String getTitel() throws RemoteException
  {
    return (String) getAttribute("titel");
  }

  public void setTitel(String titel) throws RemoteException
  {
    setAttribute("titel", titel);
  }

  public String getName() throws RemoteException
  {
    return (String) getAttribute("name");
  }

  public void setName(String name) throws RemoteException
  {
    setAttribute("name", name);
  }

  public String getVorname() throws RemoteException
  {
    return (String) getAttribute("vorname");
  }

  public void setVorname(String vorname) throws RemoteException
  {
    setAttribute("vorname", vorname);
  }

  public String getStrasse() throws RemoteException
  {
    return (String) getAttribute("strasse");
  }

  public void setStrasse(String strasse) throws RemoteException
  {
    setAttribute("strasse", strasse);
  }

  public String getPlz() throws RemoteException
  {
    return (String) getAttribute("plz");
  }

  public void setPlz(String plz) throws RemoteException
  {
    setAttribute("plz", plz);
  }

  public String getOrt() throws RemoteException
  {
    return (String) getAttribute("ort");
  }

  public void setOrt(String ort) throws RemoteException
  {
    setAttribute("ort", ort);
  }

  public Integer getZahlungsweg() throws RemoteException
  {
    return (Integer) getAttribute("zahlungsweg");
  }

  public void setZahlungsweg(Integer zahlungsweg) throws RemoteException
  {
    setAttribute("zahlungsweg", zahlungsweg);
  }

  public Integer getZahlungsrhytmus() throws RemoteException
  {
    return (Integer) getAttribute("zahlungsrhytmus");
  }

  public void setZahlungsrhytmus(Integer zahlungsrhytmus)
      throws RemoteException
  {
    setAttribute("zahlungsrhytmus", zahlungsrhytmus);
  }

  public String getBlz() throws RemoteException
  {
    return (String) getAttribute("blz");
  }

  public void setBlz(String blz) throws RemoteException
  {
    setAttribute("blz", blz);
  }

  public String getKonto() throws RemoteException
  {
    return (String) getAttribute("konto");
  }

  public void setKonto(String konto) throws RemoteException
  {
    setAttribute("konto", konto);
  }

  public String getKontoinhaber() throws RemoteException
  {
    return (String) getAttribute("kontoinhaber");
  }

  public void setKontoinhaber(String kontoinhaber) throws RemoteException
  {
    setAttribute("kontoinhaber", kontoinhaber);
  }

  public Date getGeburtsdatum() throws RemoteException
  {
    return (Date) getAttribute("geburtsdatum");
  }

  public void setGeburtsdatum(Date geburtsdatum) throws RemoteException
  {
    setAttribute("geburtsdatum", geburtsdatum);
  }

  public void setGeburtsdatum(String geburtsdatum) throws RemoteException
  {
    setAttribute("geburtsdatum", toDate(geburtsdatum));
  }

  public String getGeschlecht() throws RemoteException
  {
    return (String) getAttribute("geschlecht");
  }

  public void setGeschlecht(String geschlecht) throws RemoteException
  {
    setAttribute("geschlecht", geschlecht);
  }

  public String getTelefonprivat() throws RemoteException
  {
    return (String) getAttribute("telefonprivat");
  }

  public void setTelefonprivat(String telefonprivat) throws RemoteException
  {
    setAttribute("telefonprivat", telefonprivat);
  }

  public String getTelefondienstlich() throws RemoteException
  {
    return (String) getAttribute("telefondienstlich");
  }

  public void setTelefondienstlich(String telefondienstlich)
      throws RemoteException
  {
    setAttribute("telefondienstlich", telefondienstlich);
  }

  public String getEmail() throws RemoteException
  {
    return (String) getAttribute("email");
  }

  public void setEmail(String email) throws RemoteException
  {
    setAttribute("email", email);
  }

  public Date getEintritt() throws RemoteException
  {
    return (Date) getAttribute("eintritt");
  }

  public void setEintritt(Date eintritt) throws RemoteException
  {
    setAttribute("eintritt", eintritt);
  }

  public void setEintritt(String eintritt) throws RemoteException
  {
    setAttribute("eintritt", toDate(eintritt));
  }

  public Beitragsgruppe getBeitragsgruppe() throws RemoteException
  {
    return (Beitragsgruppe) getAttribute("beitragsgruppe");
  }

  public int getBeitragsgruppeId() throws RemoteException
  {
    return Integer.parseInt(getBeitragsgruppe().getID());
  }

  public void setBeitragsgruppe(Integer beitragsgruppe) throws RemoteException
  {
    setAttribute("beitragsgruppe", beitragsgruppe);
  }

  public Integer getZahlerID() throws RemoteException
  {
    Integer zahlerid = (Integer) getAttribute("zahlerid");
    return zahlerid;
  }

  public void setZahlerID(Integer id) throws RemoteException
  {
    setAttribute("zahlerid", id);
  }

  public Date getAustritt() throws RemoteException
  {
    return (Date) getAttribute("austritt");
  }

  public void setAustritt(Date austritt) throws RemoteException
  {
    setAttribute("austritt", austritt);
  }

  public void setAustritt(String austritt) throws RemoteException
  {
    setAttribute("austritt", toDate(austritt));
  }

  public Date getKuendigung() throws RemoteException
  {
    return (Date) getAttribute("kuendigung");
  }

  public void setKuendigung(Date kuendigung) throws RemoteException
  {
    setAttribute("kuendigung", kuendigung);
  }

  public void setKuendigung(String kuendigung) throws RemoteException
  {
    setAttribute("kuendigung", toDate(kuendigung));
  }

  public String getVermerk1() throws RemoteException
  {
    return (String) getAttribute("vermerk1");
  }

  public void setVermerk1(String vermerk1) throws RemoteException
  {
    setAttribute("vermerk1", vermerk1);
  }

  public String getVermerk2() throws RemoteException
  {
    return (String) getAttribute("vermerk2");
  }

  public void setVermerk2(String vermerk2) throws RemoteException
  {
    setAttribute("vermerk2", vermerk2);
  }

  public void setEingabedatum() throws RemoteException
  {
    setAttribute("eingabedatum", new Date());
  }

  public Date getEingabedatum() throws RemoteException
  {
    return (Date) getAttribute("eingabedatum");
  }

  /**
   * Gibt den Namen aufbereitet zurück, Meier, Dr. Willi
   */
  public String getNameVorname() throws RemoteException
  {
    String ret = getName() + ", ";
    if (getTitel() != null)
    {
      ret += getTitel() + " ";
    }
    ret += getVorname();
    return ret;
  }

  /**
   * Gibt die Anschrift aufbereitet zurück
   */
  public String getAnschrift() throws RemoteException
  {
    return getStrasse() + ", " + getPlz() + " " + getOrt();
  }

  public Object getAttribute(String fieldName) throws RemoteException
  {
    if (fieldName.equals("namevorname"))
    {
      return getNameVorname();
    }
    return super.getAttribute(fieldName);
  }

  private Date toDate(String datum)
  {
    Date d = null;

    try
    {
      d = Einstellungen.DATEFORMAT.parse(datum);
    }
    catch (Exception e)
    {
      //
    }
    return d;
  }
}
