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
 * Revision 1.15  2009/09/10 18:18:57  jost
 * neu: Buchungsklassen
 *
 * Revision 1.14  2009/08/19 20:59:46  jost
 * Link Buchungsarten aufgenommen.
 *
 * Revision 1.13  2009/08/17 18:52:12  jost
 * Neuer Pfad
 *
 * Revision 1.12  2009/08/09 15:28:14  jost
 * Umstellung auf neue Web-Site
 *
 * Revision 1.11  2009/04/13 11:40:14  jost
 * Neu: Lehrgänge
 *
 * Revision 1.10  2009/03/26 21:02:24  jost
 * Neu: Adressbuchexport
 *
 * Revision 1.9  2008/09/16 18:52:08  jost
 * Neu: Rechnung
 *
 * Revision 1.8  2008/08/10 12:36:12  jost
 * Abbuchung -> Abrechnung
 * Vorbereitung der Rechnungserstellung
 *
 * Revision 1.7  2008/07/18 20:12:14  jost
 * Neu: Formulare und Spendenbescheinigung
 *
 * Revision 1.6  2008/06/28 16:59:18  jost
 * Neu: Jahresabschluss
 *
 * Revision 1.5  2008/05/25 19:36:42  jost
 * Neu: Jahressaldo
 *
 * Revision 1.4  2008/05/24 14:04:08  jost
 * Redatkionelle Ã„nderung
 *
 * Revision 1.3  2008/05/22 06:53:26  jost
 * BuchfÃ¼hrung
 *
 * Revision 1.2  2008/04/10 18:59:25  jost
 * Neu: Benutzerdefinierte Datenfelder
 *
 * Revision 1.1  2008/01/01 19:50:45  jost
 * Erweiterung um Hilfe-Funktion
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

public class DokumentationUtil
{
  public DokumentationUtil()
  {

  }

  private static final String PRE = "http://www.jverein.de/";

  public static final String ADRESSBUCHEXPORT = PRE
      + "dokumentationauswertungadressbuchexport.php";

  public static final String ABRECHNUNG = PRE + "abrechnung.php";

  public static final String RECHNUNG = PRE + "rechnungen.php";

  public static final String AUSWERTUNGKURSTEILNEHMER = PRE
      + "dokumentationauswertungkursteilnehmer.php";

  public static final String AUSWERTUNGMITGLIEDER = PRE
      + "dokumentationauswertungmitglieder.php";

  public static final String BEITRAGSGRUPPEN = PRE
      + "administration_beitragsgruppen.php";

  public static final String BUCHUNGSARTEN = PRE
      + "administration_buchungsarten.php";

  public static final String BUCHUNGSKLASSEN = PRE
      + "administration_buchungsklassen.php";

  public static final String FELDDEFINITIONEN = PRE
      + "administration_felddefinitionen.php";

  public static final String FORMULARE = PRE + "administration_formulare.php";

  public static final String EINSTELLUNGEN = PRE
      + "administration_einstellungen.php";

  public static final String IMPORT = PRE + "administration_import.php";

  public static final String JUBILAEEN = PRE
      + "dokumentationauswertungjubilaeen.php";

  public static final String LEHRGANG = PRE + "lehrgaenge.php";

  public static final String MITGLIED = PRE + "mitglied.php";

  public static final String KURSTEILNEHMER = PRE + "kursteilnehmer.php";

  public static final String MANUELLEZAHLUNGSEINGAENGE = PRE
      + "manuelle_zahlungseingaenge.php";

  public static final String STAMMDATEN = PRE + "administration_stammdaten.php";

  public static final String STATISTIKMITGLIEDER = PRE
      + "dokumentationauswertungstatistik.php";

  public static final String WIEDERVORLAGE = PRE + "wiedervorlage.php";

  public static final String ZUSATZBETRAEGE = PRE + "zusatzbetraege.php";

  public static final String ZUSATZBETRAEGEIMPORT = PRE
      + "zusatzbetraegeimport.php";

  public static final String KONTEN = PRE
      + "dokumentationbuchfuehrungkonten.php";

  public static final String ANFANGSBESTAENDE = PRE
      + "dokumentationbuchfuehrunganfangsbestaende.php";

  public static final String BUCHUNGENAUSHIBISCUS = PRE
      + "Buchungen_aus_Hibiscus_übernehmen";

  public static final String BUCHUNGEN = PRE
      + "dokumentationbuchfuehrungbuchungenaushibiscusuebernehmen.php";

  public static final String JAHRESSALDO = PRE
      + "dokumentationbuchfuehrungjahressaldo.php";

  public static final String JAHRESABSCHLUSS = PRE
      + "dokumentationbuchfuehrungjahresabschluss.php";

  public static final String SPENDENBESCHEINIGUNG = PRE
      + "spendenbescheinigung.php";
}
