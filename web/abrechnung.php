<? include ("frame.inc"); ?>
  <h1>Abrechung</h1>
	<p>Die Abrechnung wird mit dem untenstehenden Bildschirm initiiert. Es können</p>
	<ul>
		<li>Mitgliedsbeiträge je nach <a href="dokumentationbeitragsmodelle.php">Beitragsmodell</a></li>
		<li>Beiträge für im laufenden Jahr eingetretene Mitglieder</li>
		<li>Zusatzbeträge</li>
		<li>Kursgebühren</li>
	</ul>
	<p>verarbeitet werden.</p>
	<p>Sofern als Modus <b>nicht</b> 'keine Beitragsabbuchung' ausgewählt wurde, werden für alle 
		Mitglieder, die nicht ausgetreten sind oder deren Austrittsdatum nach dem Stichtag liegt, 
		die Beiträge gemäß eingetragener Beitragsgruppe und Zahlungsrhytmus eingezogen.</p>
	<p>Für Mitglieder, die im Laufe des Jahres eingetreten sind, können ebenfalls die Beiträge 
		eingezogen werden. Dazu wird das Eingabedatum eingetragen, ab dem die Beiträge für 
		nachträglich eingetretene Mitglieder abgebucht werden sollen.</p>
	<p>Mit der Option Zusatzbeträge werden auch die zusätzlichen Beträge abgebucht.</p>
	<p>Für die Abbuchung werden die Daten werden in eine DTAUS-Datei geschrieben. Diese Datei kann entweder direkt auf 
		Diskette ausgegeben und bei der Bank eingereicht oder zum Beispiel in Hibiscus importiert 
		werden. Buchungen können als Einzel- oder Sammellastschriften direkt in Hibiscus verbucht 
		werden. Die Kontonummer in den <a href="administration_stammdaten.php">Stammdaten</a> 
		wird mit den Kontonummern in Hibiscus abgeglichen. Gibt es eine übereinstimmende Bankverbindung, 
		wird diese verwendet. Ansonsten erscheint der Hibiscus-Konto-Auswahldialog. </p>
  <p>Kompakte Abbuchung: Alle Abbuchungen von einem Konto werden in eine Abbuchung zusammengefasst, soweit die
    Verwendungszwecke (max. 15) in einen Datensatz passen.</p>
	<p>Die Abrechnungsdaten werden in das Mitgliedskonto geschrieben, sofern der entsprechende Parameter 
	  unter Administration|Einstellungen gesetzt wurde.</p>
	<p>Optional kann die ausgegebene DTAUS-Datei in ein PDF-Dokument zum Ausdruck ausgegeben werden.</p>
    <p>Teilnehmer von Kursen können abgerechnet werden. Kursteilnehmer sind Personen, die
		nicht Mitglied des Vereins sind. Sofern Mitglieder an Kursen teilnehmen, die zusätzlich
		abgerechnet werden, bieten sich die Zusatzabbuchungen an.</p>
    <img src='images/Abrechnung.png' class='screenshot'>
        
<? include ("footer.inc"); ?>

