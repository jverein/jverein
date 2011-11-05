<? include ("frame.inc"); ?>
    <h1>Administration: Einstellungen</h1>
    <h3>Allgemein</h3>
	  <p>Ab Version 2.0 werden die bisherigen Stammdaten in den allgemeinen Einstellungen gespeichert.</p>
    <img src='images/Einstellungenallgemein.png' class='screenshot'>
    <p>Der Name des Vereins und die Bankverbindung für die Abbuchung sind Pflichtangaben.</p>
	  <p>Eine häufige Anfrage ist: Warum kann ich nicht den kompletten Vereinsnamen speichern? 
	     Simpler Grund ist, dass der Vereinsname momentan nur für die Erzeugung von DTAUS-Dateien 
       gebraucht wird. Dort ist der Name auf 27 Stellen begrenzt.</p>

    <h3>Ansicht</h3>
    <p>Durch die Einstellungen kann das Verhalten von JVerein beeinflußt werden.</p>
    <img src='images/Einstellungenansicht.png' class='screenshot'>
    <p class='hervorgehoben'>Nach Änderungen der mit Stern gekennzeichneten Werte ist ein Neustart 
    von Jameica erforderlich.</p>
	<p>Folgende Einstellungen können vorgenommen werden:</p>
	<ul>
	<li>Geburtsdatum und Eintrittsdatum Pflichtfeld</li>
	<li>Tab Kommunikationsdaten beim Mitglied anzeigen</li>
	<li>Tab Zusatzabbuchungen beim Mitglied anzeigen. Impliziert, dass die Übersicht der 
	Zusatzabbuchungen (nicht) angezeigt wird und die Option bei der Abbuchung (in)aktiv ist.</li>
	<li>Tab Vermerke beim Mitglied anzeigen.</li>
	<li>Tab Wiedervorlage beim Mitglied anzeigen. Impliziert, dass die Übersicht der Wiedervorlagen 
	(nicht) angezeigt wird.</li>
	<li>Kursteilnehmer ein-/ausblenden. Auswirkung auf die Abbuchung.</li>
	<li>Juristische Personen erlaubt. Die Eingabe von Firmen, Organisationen und Behörden als Mitglieder
		wird erlaubt. Anstatt Name und Vorname werden Name-Zeile1 und Name-Zeile2 erfasst. Geburtsdatum
		und Geschlecht werden nicht erfasst.</li>
	<li>Externe Mitgliedsnummern verwenden. Vereine, die auf Bundes- oder Landesebene organisiert 
	sind und eine durchgängige Mitgliedsnummer verwalten möchten, können in JVerein eine externe 
	Mitgliedsnummer speichern.</li>
	<li>Aktuelle Geburtstage - Tage vorher/Tage nachher - Unter Jameica|Startseite anpassen|JVerein: Aktuelle Geburtstage|aktivieren
	kann die Anzeige auf der Startseite eingeschaltet werden.</li> 
	</ul>
	
	<h2><a name='beitraege'></a>Beiträge</h2>
    <img src='images/Einstellungenbeitraege.png' class='screenshot'>
	<p>Beitragsmodell, siehe auch <a href='dokumentationbeitragsmodelle.php'>Beitragsmodelle</a></</p>
	<p>Ab Version 1.3 können die Standardwerte für den Zahlungsweg und den Zahlungsrhytmus für die 
	Neueingabe von Mitgliedern eingestellt werden.</p>
	
	<p>Ab Version 2.1 wird beim Standard-Zahlungsweg zusätzlich über das Feld DTAUS-Textschlüssel 
	unterschieden nach der Art. Dieser Zahlungsweg wird mit der DTAUS-Datei für einen Sammelauftrag 
	entweder direkt an die Bank oder an Hibiscus übergeben.</p>
	
	<p><b>Lastschrift:</b> Das Mitglied gestattet dem Verein, die Beiträge von seinem Konto 
	abzubuchen (Einzugsermächtigung). Der Kontoinhaber hat das Recht, über viele Jahre die 
	Lastschrift zu stornieren und das Geld zurück zu buchen. Dies ist zur Zeit die normale 
	Zahlungsweise und funktioniert immer. Bei Unsicherheit diese Auswahl treffen, bzw. mit der Bank klären.</p>
  <p><b>Abbuchung:</b> Das Mitglied beauftragt seine Bank, Abbuchungen dieses Vereins ohne 
  Rückfrage auszuführen. Der Kontoinhaber hat nicht mehr das Recht, die Lastschrift zu 
  stornieren und das Geld zurück zu buchen. Nur Unterdeckung verhindert die Abbuchung.
  In der Praxis bekommt der Verein bei der Freischaltung dieser Zahlungsart von seiner Bank 
  ein Blanko-Formular, das jedes Mitglied ausfüllt und seiner Bank übergibt (alternativ kann 
  es auch eingesammelt und vom Verein eingereicht werden). Diese zur Zeit besondere Art der 
  Abbuchung gibt dem Verein und der Bank erhöhte Sicherheit, da Abbuchungen nicht mehr 
  zurückgefordert werden können.</p>

  <p>Bei der Abwicklung der Abbuchung über Hibiscus kann die Art nachträglich durch Ändern der 
  einzelnen Buchungen innerhalb der Sammellastsachrift nachträglich geändert werden 
  (Doppelklick auf die Sammellastschrift und ändern der Einzeldaten für jedes Mitglied).
  Bei falscher Wahl der Zahlungsart auf "Abbuchung" können durch die Abweisung der Lastschrift 
  Rückbuchungsgebühren belastet werden.</p>
	
	<h2>Dateinamenmuster</h2>
	<img src='images/Einstellungendateinamen.jpg' class='screenshot'>
	<p>Bei der Ausgabe von Dateien (Abbuchung, Auswertungen...) werden die Dateinamen nach dem 
	vorgegebenen Muster aufgebaut. Es können zusätzliche, vom Betriebssystem unterstützte Zeichen, 
	in das Muster aufgenommen werden. Bleibt das Muster leer, wird kein Vorschlag für den Dateinnamen 
	angezeigt. </p>
	<p>Folgende Variable stehen zur Verfügung:</p>
	<ul>
	<li>a$ : Aufgabe (z. B. auswertung, abbuchung)</li>
	<li>d$ : Aktuelles Datum</li>
	<li>s$ : Sortierung. Wird nur bei Auswertungen gefüllt. Ansonsten leer.</li>
	<li>z$ : Aktuelle Zeit</li>
	</ul>
	
	<h2>Buchführung</h2>
	<img src='images/Einstellungenbuchfuehrung.jpg' class='screenshot'>
	<p>Beginn des Geschäftsjahres in der Form TT.MM. </p> 

	<h2>Rechnungen</h2>
	<img src='images/Einstellungenrechnungen.jpg' class='screenshot'>
	<p>Bis Version 1.3: Festlegung, für welche Zahlungswege Rechnungsinformationen gespeichert werden sollen. </p>
	<p>Ab Version 1.4: Texte für die einzelnen Zahlungswege für den Rechnungsdruck. In den Text zur Abbuchung 
	können die Variablen ${Konto} und ${BLZ} eingemischt werden.</p>
	<h2><a name='tabellen'></a>Tabellen</h2>
	<p>Festlegung der Spalten, die in Tabellen angezeigt werden sollen. 
	<img src='images/Einstellungentabellen.jpg' class='screenshot'>
	

	<h2><a name=mail></a>Mail</h2>
	<img src='images/Einstellungenmail.jpg' class='screenshot'>
	<p>Alternativ zur EMail-Adresse kann auch der Name zusätzlich im Format 
	<code>Vereinsname oder Mein Name &lt;vorstand@verein.de&gt</code>.
	Wichtig ist dabei das Format: (Name) (Spitze Klammer auf) (Email) (Spitze Klammer zu)</p>
	
	<h2><a name=statistik></a>Statistik</h2>
	<img src='images/Einstellungenstatistik.png' class='screenshot'>
		<p>Für statistische Zwecke können Altersgruppen wie im obigen Hardcopy dargestellt angegeben 
	werden.</p>
	<p>Zur Ausgabe einer Jubiläumsliste werden die Jubeljahre durch Komma getrennt eingetragen. 
	Ohne Eingabe werden die Standardwerte 10,25,40,50 verwendet.</p>
	<p>Es kann eine Liste der Altersjubilare ausgegeben werden. Ohne Eingabe werden 
	die Standardwerte 50,60,65,70,75,80,85,90,95 verwendet.</p>
	
 <? include ("footer.inc"); ?>

