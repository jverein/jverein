<? include ("frame.inc"); ?>
    <h1>Administration: Import</h1>
	<p>Zur Zeit ist der Import von Daten über das durch das Programm SPG-Verein (Sparkasse) vorgegebene 
	CSV-Format realisiert. Einige zusätzliche Datenfelder können importiert werden. Der Dateiname muss 
	eine Endung haben. Z. B. .csv oder .txt. Es kann jede beliebige Endung verwendet werden. Die Daten 
	werden in SPG-Verein unter Extras - Daten exportieren mit folgenden Parametern ausgegeben:</p>
	<ul>
	<li>Vorlage: Winword</li>
	<li>Dateiendung: .doc</li>
	<li>Feldseparator:&nbsp;;=Strichpunkt/Semikolon</li>
	<li>Feldeinrahmung: keine</li>
	<li>Zeichensatz: ISO-8859-1</li>
	<li>Feldnamen: Ankreuzen</li>
	</ul>
	<p>Andere Programme müssen eine Datei mit folgenden Spalten erzeugen:</p>
	<table border="1">
	<tr>
		<th>Spalte</th>
		<th>Inhalt</th>
		<th>Max. Länge</th>
		<th>Spalte muss existieren</th>
		<th>Leere Spalte erlaubt</th>
	</tr>
	<tr>
		<td>Mitglieds_Nr</td>
		<td>Mitgliedsnummer. Wird bei der Verwendung von externen Mitgliedsnummern auch in die 
		    entsprechende Spalte eingetragen.</td>
		<td>&nbsp;</td>
		<td>ja</td>
		<td>nein</td>
	</tr>
  <tr>
    <td>Adresstyp</td>
    <td>1 = Mitglied, 2 = Spender 
    Weitere Adresstypen können über Administration | Adresstypen erfasst werden. Die ID wird in der
    Übersicht angezeigt. Ab Version 2.1</td>
    <td>2</td>
    <td>nein</td>
    <td>nein</td>
  </tr>
  <tr>
    <td>Personenart</td>
    <td>n = natürliche Person, j = juristische Person (Firma, Organisation, Behörde). 
    Wenn die Spalte Personenart nicht in der Importdatei existiert, wird defaultmäßig 'n' 
    übernommen.</td>
    <td>1</td>
    <td>nein</td>
    <td>nein</td>
  </tr>
	<tr>
		<td>Anrede</td>
		<td>Herrn/Frau</td>
		<td>10, ab V1.3: 40</td>
		<td>ja</td>
		<td>ja</td>
	</tr>
	<tr>
		<td>Titel</td>
		<td>Dr. ....</td>
		<td>20, ab V1.3: 40</td>
		<td>ja</td>
		<td>ja</td>
	</tr>
	<tr>
		<td>Nachname</td>
		<td>Nachname, wenn Personenart = j, dann Firmenname Zeile 1</td>
		<td>40</td>
		<td>ja</td>
		<td>nein</td>
	</tr>
	<tr>
		<td>Vorname</td>
		<td>Vorname, wenn Personenart = j, dann Firmenname Zeile 2</td>
		<td>40</td>
		<td>ja</td>
		<td>nein</td>
	</tr>
	<tr>
		<td>Adressierungszusatz</td>
		<td>Adressierungszusatz, z. B. bei Lieschen Müller (ab. V 1.1), max. 40 Stellen</td>
		<td>40</td>
		<td>nein</td>
		<td>ja</td>
	</tr>
	<tr>
		<td>Strasse</td>
		<td>Straßenname inkl. Hausnummer</td>
		<td>40</td>
		<td>ja</td>
		<td>nein</td>
	</tr>
	<tr>
		<td>Plz</td>
		<td>Postleitzahl</td>
		<td>10</td>
		<td>ja</td>
		<td>nein</td>
	</tr>
	<tr>
		<td>Ort</td>
		<td>Ort</td>
		<td>40</td>
		<td>ja</td>
		<td>nein</td>
	</tr>
	<tr>
		<td>Staat</td>
		<td>Staat bei Auslandsanschriften</td>
		<td>50</td>
		<td>nein</td>
		<td>ja</td>
	</tr>
	<tr>
		<td>Geburtsdatum</td>
		<td>Format TT.MM.JJJJ</td>
		<td>10</td>
		<td>ja</td>
		<td>in Abhängigkeit von den Einstellungen</td>
	</tr>
	<tr>
		<td>Sterbetag</td>
		<td>Format TT.MM.JJJJ</td>
		<td>10</td>
		<td>ja</td>
		<td>ja</td>
	</tr>
	<tr>
		<td>Geschlecht</td>
		<td>m oder w</td>
		<td>1</td>
		<td>ja</td>
		<td>ja</td>
	</tr>
	<tr>
		<td>Bankleitzahl</td>
		<td>Bankleitzahl</td>
		<td>8</td>
		<td>ja</td>
		<td>Wenn Zahlungsart Lastschrift: nein, sonst ja</td>
	</tr>
	<tr>
		<td>Kontonummer</td>
		<td>Kontonummer</td>
		<td>10</td>
		<td>ja</td>
		<td>Wenn Zahlungsart Lastschrift: nein, sonst ja</td>
	</tr>
	<tr>
		<td>Zahlungsart</td>
		<td>l (Kleinbuchstabe L)] für Lastschrift, b für Barzahlung oder u für Überweisung</td>
		<td>1</td>
		<td>ja</td>
		<td>ja</td>
	</tr>
	<tr>
		<td>Zahlungsrhytmus</td>
		<td>1 monatlich, 3 vierteljährlich, 6 halbjährlich, 12 jährlich, wenn keine Angabe erfolgt, wird jährlich angenommen.</td>
		<td>2</td>
		<td>nein</td>
		<td>ja</td>
	</tr>
	<tr>
		<td>Zahler</td>
		<td>Kontoinhaber, wenn nicht identisch mit dem Mitglied</td>
		<td>27</td>
		<td>ja</td>
		<td>ja</td>
	</tr>
	<tr>
		<td>Telefon_privat</td>
		<td>private Telefonnummer</td>
		<td>20</td>
		<td>ja</td>
		<td>ja</td>
	</tr>
	<tr>
		<td>Telefon_dienstlich</td>
		<td>dienstliche/geschäftliche Telefonnummer</td>
		<td>20</td>
		<td>ja</td>
		<td>ja</td>
	</tr>
	<tr>
		<td>Handy</td>
		<td>Mobile Telefonnummer</td>
		<td>20</td>
		<td>nein</td>
		<td>ja</td>
	</tr>
	<tr>
		<td>Email</td>
		<td>EMail-Adresse</td>
		<td>50</td>
		<td>ja</td>
		<td>ja</td>
	</tr>
	<tr>
		<td>Eintritt</td>
		<td>Eintrittsdatum im Format TT.MM.JJJJ</td>
		<td>10</td>
		<td>ja</td>
		<td>ja</td>
	</tr>
	<tr>
		<td>Beitragsart_1</td>
		<td>Bezeichnung der Beitragsart. Z. B. Jugendliche, Erwachsene, Familien ...</td>
		<td>30</td>
		<td>ja</td>
		<td>nein</td>
	</tr>
  <tr>
    <td>Beitrag_1</td>
    <td>Höhe des Beitrages in Euro (Format xxx,xx)</td>
    <td>&nbsp;</td>
    <td>ja</td>
    <td>nein</td>
  </tr>
  <tr>
    <td>individuellerbeitrag</td>
    <td>Höhe des individuellen Beitrages in Euro (Format xxx,xx). Ab Version 2.0</td>
    <td>&nbsp;</td>
    <td>nein</td>
    <td>ja</td>
  </tr>
	<tr>
		<td>Austritt</td>
		<td>Datum des Austritts im Format TT.MM.JJJJ. Je nach Vereinssatzung ist die Kündigung erst 
		    zum Jahresende wirksam. Hier wird das Wirksamwerden der Kündigung vermerkt. 
		    Format TT.MM.JJJJ.</td>
		<td>10</td>
		<td>ja</td>
		<td>ja</td>
	</tr>
	<tr>
		<td>Kuendigung</td>
		<td>Datum der Kündigung im Format TT.MM.JJJJ</td>
		<td>10</td>
		<td>ja</td>
		<td>ja</td>
	</tr>
	<tr>
		<td>Vermerk1</td>
		<td>1. Vermerk</td>
		<td>255</td>
		<td>nein</td>
		<td>ja</td>
	</tr>
	<tr>
		<td>Vermerk2</td>
		<td>2. Vermerk</td>
		<td>255</td>
		<td>nein</td>
		<td>ja</td>
	</tr>
  <tr>
    <td>Eigenschaft_xxxxx</td>
    <td>Eigenschaft eines Mitglieds. Diese Spalte kann mehrfach vorkommen. Anstatt von xxxxx wird die Eigenschaftengruppe eingetragen.
        Die importieren Eigenschaften dieser Gruppe zugeordnet.</td>
    <td>30</td>
    <td>nein</td>
    <td>ja</td>
  </tr>
	</table>
	<p>Als Feldtrennzeichen wird das Semikolon verwendet. Jede Zeile muss die gleiche Anzahl Semikola 
	enthalten. Die Datei darf keine Anführungszeichen enthalten. Bei jedem Mitglied müssen die Spalten 
	Beitragsart 1 und Beitrag 1 gefüllt sein.</p>
	<p>Beispieldatei:</p>
<code>
Mitglieds_Nr;Anrede;Titel;Nachname;Vorname;Straße;Plz;Ort;Geburtsdatum;Geschlecht;Bankleitzahl;Kontonummer;Zahlungsart;Zahler;Telefon_privat;Telefon_dienstlich;Email;Eintritt;Beitragsart_1;Beitrag_1;Austritt;Kündigung
22;Herrn;Dr.;Meier;Hans;Ackerstr.1;12345;Testenhausen;22.02.1970;m;12345678;12345;l;;12345;;hans.meier@web.de;01.01.2000;Erwachsene;22,00;;;
</code>
	<p>Jede Datei enthält eine Kopfzeile und pro Mitglied eine Zeile. Beim Import werden sowohl die 
	Beitragsgruppen-Tabelle als auch die Mitgliedertabelle aufgebaut. Ein erneuter Import löscht die 
	vorhandenen Daten nach einer entsprechenden Warnung.</p>
	<p>Sofern vor dem Import <a href="administration_felddefinitionen.php">Zusatzfelder definiert</a> 
	wurden, können diese auch importiert werden. Die Datenfelder sind entsprechend der Bezeichnung in der 
	Felddefinition in die Datei einzustellen.</p>
	<p>Die Eingabedatei muss ISO-8859-1-codiert sein.</p>
	<h1>Erweiterter Import ( ab Version 2.1 )</h1>
	<p>Vor jedem Import sollten Sie sich im klaren sein, welche Einstellungen sie vorgenommen haben. Z.B wenn sie Eintrittsdatum als Pflichtfeld definieren, dann muss f&uuml;r jedes Mitglied das Eintrittsdatum auch definiert sein. Au&szlig;erdem sollten sie ber&uuml;cksichtigen die, in der Tabelle definierte, maximale L&auml;nge die jeder Eintrag haben darf. Wenn Sie dann noch die unterst&uuml;tzten Formate ber&uuml;cksichtigen dann sollte einem Import nicht mehr viel im Weg stehen.</p>
	<p>Das augenscheinlichste was sich am Importer ver&auml;ndert hat ist die Oberfl&auml;che, diese ist nun zweigeteilt. Auf der linken Seite befindet sich eine Tabelle mit allen n&ouml;tigen ( rot hinterlegt ) und allen optionalen Spalten. Auf der rechten Seite erscheinen nach der Auswahl der zu importierenden Datei, die in dieser definierten Spaltennamen. Nach dem &ouml;ffenen werden automatisch die Spalten zugeordnet die den gleichen Namen besitzen, wobei die Gro&szlig;/Kleinschreibung ignoriert wird. Falls Sie Zusatzfelder definiert haben, so werden diese ebenfalls angezeigt. Und wenn es Eigenschaftsfelder in der Importdatei gibt, werden diese nach dem &ouml;ffnen ebenfalls direkt anzeigt und einander zugeordnet. Die restlichen Felder m&uuml;ssen sie dann mittels Drag &amp; Drop den enstprechenden Spalten zuweisen. Und sollte eine Zuweisung falsch sein, so k&ouml;nnen sie diese wieder leicht mit der Entf-Taste l&ouml;schen.</p>
	<p><img src='images/EinstellungenImport.png' alt="Import View" width="601" height="529" class='screenshot'></p>
	<p>Es gibt nicht nur sichtbare &Auml;nderungen sondern ein paar auch unter der Haube. Deshalb hat sich die Tabelle ein wenig ge&auml;ndert, vor allem die m&ouml;glichen Formatierungen wurden erweitert.
	  <!-- 
    $Log$
    Revision 1.21  2011/10/10 20:55:27  jost
    Import überarbeitet - Spaltenzuordnung im Dialog. Patch von Chrisitian Lutz.

    Revision 1.20  2011-09-25 13:27:56  jost
    *** empty log message ***

    Revision 1.19  2011-09-25 13:20:12  jost
    *** empty log message ***

    Revision 1.18  2011-08-24 16:04:03  jost
    *** empty log message ***

    Revision 1.17  2011-08-23 20:55:11  jost
    *** empty log message ***

    Revision 1.16  2011-07-25 08:05:41  jost
    individuellerbeitrag ist kein Pflichtfeld

    Revision 1.15  2011-06-23 05:53:41  jost
    *** empty log message ***

    Revision 1.14  2011-05-20 13:01:50  jost
    Neu: Individueller Beitrag

    Revision 1.13  2011-03-20 11:47:11  jost
    *** empty log message ***

    Revision 1.12  2010-10-30 11:32:40  jost
    Neu: Sterbetag

    Revision 1.11  2010-10-28 19:17:10  jost
    Neu: Wohnsitzstaat

    Revision 1.10  2010-10-25 20:32:00  jost
    Neu: Vermerk 1 und Vermerk2

    Revision 1.9  2010-09-19 17:51:44  jost
    *** empty log message ***

    Revision 1.8  2010/01/22 15:54:27  jost
    *** empty log message ***

    Revision 1.7  2009/11/22 19:49:57  jost
    *** empty log message ***

    Revision 1.6  2009/11/18 16:16:24  jost
    Bugfix Link

    Revision 1.5  2009/11/05 20:07:27  jost
    Zusätzliche Informationen über die Importfelder aufgenommen.

    Revision 1.4  2009/10/24 14:52:25  jost
    Max. Länge angegeben

    Revision 1.3  2009/08/19 21:01:28  jost
    *** empty log message ***

    Revision 1.2  2009/05/11 05:59:46  jost
    können/müssen

    Revision 1.1  2009/05/08 14:46:22  jost
    shtml - php

    Revision 1.2  2009/05/03 15:33:30  jost
    Neue Homepage

    -->
    Und falls Ihre Eintr&auml;ge in Einf&uuml;hrungstrichen eingefasst ist z.B. &quot;Name&quot;, dann werden diese automatisch entfernt.</p>
	<table border="1">
	  <tr>
	    <th>Spalte</th>
	    <th>Inhalt</th>
	    <th>Max. L&auml;nge</th>
	    <th>Spalte muss existieren</th>
	    <th>Leere Spalte erlaubt</th>
      </tr>
	  <tr>
	    <td>Mitglieds_Nr</td>
	    <td>Mitgliedsnummer, muss eine eindeutige ID sein, ansonsten kommt es zu einem Fehler. Wird bei der Verwendung von externen Mitgliedsnummern auch in die 
	      entsprechende Spalte eingetragen.</td>
	    <td>&nbsp;</td>
	    <td>ja</td>
	    <td>nein</td>
      </tr>
	  <tr>
	    <td>Personenart</td>
	    <td>n = nat&uuml;rliche Person, j = juristische Person (Firma, Organisation, Beh&ouml;rde). 
	      Wenn die Spalte Personenart nicht in der Importdatei existiert, wird defaultm&auml;&szlig;ig 'n' 
	      &uuml;bernommen.</td>
	    <td>1</td>
	    <td>nein</td>
	    <td>ja</td>
      </tr>
	  <tr>
	    <td>Anrede</td>
	    <td>Herrn / Frau</td>
	    <td>40</td>
	    <td>ja</td>
	    <td>ja</td>
      </tr>
	  <tr>
	    <td>Titel</td>
	    <td>Dr. ....</td>
	    <td>40</td>
	    <td>ja</td>
	    <td>ja</td>
      </tr>
	  <tr>
	    <td>Nachname</td>
	    <td>Nachname, wenn Personenart = j, dann Firmenname Zeile 1</td>
	    <td>40</td>
	    <td>ja</td>
	    <td>nein</td>
      </tr>
	  <tr>
	    <td>Vorname</td>
	    <td>Vorname, wenn Personenart = j, dann Firmenname Zeile 2</td>
	    <td>40</td>
	    <td>ja</td>
	    <td>nein</td>
      </tr>
	  <tr>
	    <td>Adressierungszusatz</td>
	    <td>Adressierungszusatz</td>
	    <td>40</td>
	    <td>nein</td>
	    <td>ja</td>
      </tr>
	  <tr>
	    <td>Strasse</td>
	    <td>Stra&szlig;enname inkl. Hausnummer</td>
	    <td>40</td>
	    <td>ja</td>
	    <td>nein</td>
      </tr>
	  <tr>
	    <td>Plz</td>
	    <td>Postleitzahl</td>
	    <td>10</td>
	    <td>ja</td>
	    <td>nein</td>
      </tr>
	  <tr>
	    <td>Ort</td>
	    <td>Ort</td>
	    <td>40</td>
	    <td>ja</td>
	    <td>nein</td>
      </tr>
	  <tr>
	    <td>Staat</td>
	    <td>Staat bei Auslandsanschriften</td>
	    <td>50</td>
	    <td>nein</td>
	    <td>ja</td>
      </tr>
	  <tr>
	    <td>Geburtsdatum</td>
	    <td>Format TT.MM.JJJJ oder TT.MM.JJ oder TT/MM/JJJJ oder TT/MM/JJJJ. Bei JJ wird funktioniert fuer alle Mitglieder j&uuml;nger hundert richtig, f&uuml;r &auml;lter Menschen muss es angepasst werden. Eine Warnung wird ausgegeben.</td>
	    <td>10</td>
	    <td>ja</td>
	    <td>in Abh&auml;ngigkeit von den Einstellungen</td>
      </tr>
	  <tr>
	    <td>Sterbetag</td>
	    <td> Sterbetag, Austritt sollte auch definiert sein, ansonsten wird das Sterbedatum als Austrittsdatum angenommen. Unterst&uuml;tzte Formate siehe Geburtsdtatum</td>
	    <td>10</td>
	    <td>ja</td>
	    <td>ja</td>
      </tr>
	  <tr>
	    <td>Geschlecht</td>
	    <td>muss mit einem kleinen oder gro&szlig;en m oder w beginnen z.B. g&uuml;ltig Weiblich, m&auml;nnlich oder auch M oder w, alles g&uuml;ltige Formate</td>
	    <td>1</td>
	    <td>ja</td>
	    <td>ja</td>
      </tr>
	  <tr>
	    <td>Bankleitzahl</td>
	    <td>Bankleitzahl</td>
	    <td>8</td>
	    <td>ja</td>
	    <td>Wenn Zahlungsart Lastschrift: nein, sonst ja</td>
      </tr>
	  <tr>
	    <td>Kontonummer</td>
	    <td>Kontonummer</td>
	    <td>10</td>
	    <td>ja</td>
	    <td>Wenn Zahlungsart Lastschrift: nein, sonst ja</td>
      </tr>
	  <tr>
	    <td>Zahlungsart</td>
	    <td><p>l (Kleinbuchstabe L)] oder Lastschrift oder Abbuchung oder Bankeinzug f&uuml;r Lastschrift, </p>
        <p>b oder bar f&uuml;r Barzahlung </p>
        <p> u oder ueberweisung f&uuml;r &Uuml;berweisung</p></td>
	    <td>1</td>
	    <td>ja</td>
	    <td>ja</td>
      </tr>
	  <tr>
	    <td>Zahlungsrhytmus</td>
	    <td>1 monatlich, 3 viertelj&auml;hrlich, 6 halbj&auml;hrlich, 12 j&auml;hrlich, wenn keine Angabe erfolgt, wird j&auml;hrlich angenommen.</td>
	    <td>2</td>
	    <td>nein</td>
	    <td>ja</td>
      </tr>
	  <tr>
	    <td>Zahler</td>
	    <td>Kontoinhaber, wenn nicht identisch mit dem Mitglied</td>
	    <td>27</td>
	    <td>ja</td>
	    <td>ja</td>
      </tr>
	  <tr>
	    <td>Telefon_privat</td>
	    <td>private Telefonnummer</td>
	    <td>20</td>
	    <td>ja</td>
	    <td>ja</td>
      </tr>
	  <tr>
	    <td>Telefon_dienstlich</td>
	    <td>dienstliche / gesch&auml;ftliche Telefonnummer</td>
	    <td>20</td>
	    <td>ja</td>
	    <td>ja</td>
      </tr>
	  <tr>
	    <td>Handy</td>
	    <td>Mobile Telefonnummer</td>
	    <td>20</td>
	    <td>nein</td>
	    <td>ja</td>
      </tr>
	  <tr>
	    <td>Email</td>
	    <td>EMail-Adresse</td>
	    <td>50</td>
	    <td>ja</td>
	    <td>ja</td>
      </tr>
	  <tr>
	    <td>Eintritt</td>
	    <td>Eintrittsdatum, unterst&uuml;tzte Formate siehe Geburtsdatum</td>
	    <td>10</td>
	    <td>ja</td>
	    <td>in Abh&auml;ngigkeit von den Einstellungen</td>
      </tr>
	  <tr>
	    <td>Beitragsart_1</td>
	    <td>Bezeichnung der Beitragsart. Z. B. Jugendliche, Erwachsene, Familien ...</td>
	    <td>30</td>
	    <td>ja</td>
	    <td>nein</td>
      </tr>
	  <tr>
	    <td>Beitrag_1</td>
	    <td>H&ouml;he des Beitrages in Euro (Format xxx,xx)</td>
	    <td>&nbsp;</td>
	    <td>ja</td>
	    <td>nein</td>
      </tr>
	  <tr>
	    <td>individuellerbeitrag</td>
	    <td>H&ouml;he des individuellen Beitrages in Euro (Format xxx,xx). Ab Version 2.0</td>
	    <td>&nbsp;</td>
	    <td>nein</td>
	    <td>ja</td>
      </tr>
	  <tr>
	    <td>Austritt</td>
	    <td>Datum des Austritts, je nach Vereinssatzung ist die K&uuml;ndigung erst 
	      zum Jahresende wirksam. Hier wird das Wirksamwerden der K&uuml;ndigung vermerkt. 
	      Unterst&uuml;tzte Formate siehe Geburtsdatum.</td>
	    <td>10</td>
	    <td>ja</td>
	    <td>ja</td>
      </tr>
	  <tr>
	    <td>Kuendigung</td>
	    <td>Datum der K&uuml;ndigung. Unterst&uuml;tzte Formate siehe Geburstdatum</td>
	    <td>10</td>
	    <td>ja</td>
	    <td>ja</td>
      </tr>
	  <tr>
	    <td>Vermerk1</td>
	    <td>1. Vermerk</td>
	    <td>255</td>
	    <td>nein</td>
	    <td>ja</td>
      </tr>
	  <tr>
	    <td>Vermerk2</td>
	    <td>2. Vermerk</td>
	    <td>255</td>
	    <td>nein</td>
	    <td>ja</td>
      </tr>
	  <tr>
	    <td>Eigenschaft_xxxxx</td>
	    <td>Eigenschaft eines Mitglieds. Diese Spalte kann mehrfach vorkommen. Anstatt von xxxxx wird die Eigenschaftengruppe eingetragen.
	      Die importieren Eigenschaften dieser Gruppe zugeordnet.</td>
	    <td>30</td>
	    <td>nein</td>
	    <td>ja</td>
      </tr>
    </table>
	<p>&nbsp;</p>
<? include ("footer.inc"); ?>

