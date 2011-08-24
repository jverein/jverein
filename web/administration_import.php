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
		<td>Eigenschaft_xx</td>
		<td>Eigenschaft eines Mitglieds. Diese Spalte kann mehrfach vorkommen. Anstatt von xx werden 01-99 oder andere Werte eingetragen.
		    Die importieren Eigenschaften werden der Gruppe 'Noch nicht zugeordnet' zugeordnet.</td>
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
22;Herrn;Dr.;Meier;Hans;Ackerstr. 1;12345;Testenhausen;22.02.1970;m;12345678;12345;l;;12345;;hans.meier@web.de;01.01.2000;Erwachsene;22,00;;;
</code>
	<p>Jede Datei enthält eine Kopfzeile und pro Mitglied eine Zeile. Beim Import werden sowohl die 
	Beitragsgruppen-Tabelle als auch die Mitgliedertabelle aufgebaut. Ein erneuter Import löscht die 
	vorhandenen Daten nach einer entsprechenden Warnung.</p>
	<p>Sofern vor dem Import <a href="administration_felddefinitionen.php">Zusatzfelder definiert</a> 
	wurden, können diese auch importiert werden. Die Datenfelder sind entsprechend der Bezeichnung in der 
	Felddefinition in die Datei einzustellen.</p>
	<p>Die Eingabedatei muss ISO-8859-1-codiert sein.</p>
	
	<!-- 
    $Log$
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
	
<? include ("footer.inc"); ?>

