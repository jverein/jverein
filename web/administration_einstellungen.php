<? include ("frame.inc"); ?>
    <h1>Administration: Einstellungen</h1>
    <h3>Allgemein</h3>
	  <img src='images/Einstellungenallgemein.png' class='screenshot'>
    <p>Name und Anschrift des Vereins sowie die Bankverbindung für die Abbuchung können gespeichert 
    werden.sind Pflichtangaben.</p>
	  <p>Name und Bankverbindung werden für die Erstellung der DTAUS-Datei für die Abbuchung zwingend
	  benötigt. Die weiteren Angaben werden überwiegend bei Spendenbescheinigungen eingesetzt.</p>

    <h3>Anzeige</h3>
    <p>Durch die Einstellungen kann das Verhalten von JVerein beeinflußt werden.</p>
    <img src='images/Einstellungenanzeige.png' class='screenshot'>
    <p class='hervorgehoben'>Nach Änderungen der mit Stern gekennzeichneten Werte ist ein Neustart 
    von Jameica erforderlich.</p>
    <p>Folgende Einstellungen können vorgenommen werden:</p>
	  <ul>
      <li>Geburtsdatum und Eintrittsdatum Pflichtfeld</li>
      <li>Eingabefeld für das Sterbedatum vorhanden und auswertbar</li>
	    <li>Kommunikationsdaten beim Mitglied anzeigen: Telefonnummern und Email-Adressen werden angezeigt.</li>
	    <li>Tab Zusatzabbuchungen beim Mitglied anzeigen. Impliziert, dass die Übersicht der Zusatzabbuchungen 
	     (nicht) angezeigt wird und die Option bei der Abbuchung (in)aktiv ist.</li>
	    <li>Tab Vermerke beim Mitglied anzeigen. Beim Mitglied können 2 mal 255 Zeichen Vermerke gespeichert werden.</li>
	    <li>Tab Wiedervorlage beim Mitglied anzeigen. Impliziert, dass die Übersicht der Wiedervorlagen 
	         (nicht) angezeigt wird.</li>
    	<li>Kursteilnehmer ein-/ausblenden. Auswirkung auf die Abbuchung.</li>
    	<li>Lehrgänge anzeigen. Gglfs. können zu einem Mitglied die durchgeführten Lehrgänge mit Ergebnissen gespeichert werden.</li>
	    <li>Juristische Personen erlaubt. Die Eingabe von Firmen, Organisationen und Behörden als Mitglieder
		      wird erlaubt. Anstatt Name und Vorname werden Name-Zeile1 und Name-Zeile2 erfasst. Geburtsdatum
		      und Geschlecht werden nicht erfasst.</li>
		  <li>Mitgliedskonten. Zu jedem Mitglied wird gespeichert, welche Beträge es zahlen soll und gezahlt hat. Daraus
		      können Rechnungen und Mahnungen erzeugt werden.</li>
		  <li>Mitgliedsfoto. </li>
		  <li>Zusätzliche Adressen: Speicherung von weiteren Adressen. Z. B. Spender, Lieferanten, Trainer...</li>
		  <li>Auslandsadressen: Beim Mitglied kann zusätzlich der Wohnsitz-Staat gespeichert werden</li>
		  <li>Speicherung und Auswertung von Arbeitseinsätzen.</li>
		  <li>Speicherung von Dokumenten zu Mitgliedern und Buchungen.</li>
		  <li>Individuelle Beiträge: Grundsätzlich zahlt das Mitglied den Beitrag, der in der Beitragsgruppe angegeben wurde.
		      Sofern diese Option aktiviert wurde, kann bei jedem Mitglied ein abweichender individueller Beitrag angegeben 
		      werden.</li>
	    <li>Externe Mitgliedsnummern verwenden. Vereine, die auf Bundes- oder Landesebene organisiert 
	           sind und eine durchgängige Mitgliedsnummer verwalten möchten, können in JVerein eine externe 
	           Mitgliedsnummer speichern.</li>
	    <li>Verzögerungszeit in Millisekunden: Bei der Eingabe des Mitgliedes gibt es für Name, Vorname und Straße 
	       Suchfelder, die die Werte anzeigen, die zur bisherigen Eingabe passen. Mit der Verzögerungszeit kann eingestellt
	       werden, nach welcher Zeit diese Funktion anspringt.</li>	
	</ul>
	
	<h2><a name='beitraege'></a>Beiträge</h2>
  <img src='images/Einstellungenbeitraege.png' class='screenshot'>
	<p>Beitragsmodell, siehe auch <a href='dokumentationbeitragsmodelle.php'>Beitragsmodelle</a></</p>
	<p>Die Standardwerte für den Zahlungsrhytmus und den Zahlungsweg bei der Speicherung neuer Mitglieder kann eingestellt
	   werden.</p>
	
	<p>Ab Version 2.1 ist der Textschlüssel für die Erstellung der DTAUS-Datei festzulegen.</p>
	
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
  Rückbuchungsgebühren belastet werden. Im Zweifelsfall mit der Bank sprechen oder Lastschrift
  wählen.</p>
	
	<h2>Dateinamenmuster</h2>
	<img src='images/Einstellungendateinamen.png' class='screenshot'>
	<p>Bei der Ausgabe von Dateien (Abbuchung, Auswertungen...) werden die Dateinamen nach dem 
	vorgegebenen Muster aufgebaut. Es können zusätzliche, vom Betriebssystem unterstützte Zeichen, 
	in das Muster aufgenommen werden. Bleibt das Muster leer, wird kein Vorschlag für den Dateinnamen 
	angezeigt. Spendenbescheinigungen werden jeweils für den einzelnen Spender ausgestellt. Daher
	sollten zu leichteren Identifizierung Name und Vorname in den Dateinamen aufgenommen werden. </p>
	<p>Folgende Variable stehen zur Verfügung:</p>
	<ul>
	<li>a$ : Aufgabe (z. B. auswertung, abbuchung)</li>
	<li>d$ : Aktuelles Datum</li>
	<li>s$ : Sortierung. Wird nur bei Auswertungen gefüllt. Ansonsten leer.</li>
	<li>z$ : Aktuelle Zeit</li>
	<li>n$ : Name des Mitglieds</li>
	<li>v$ : Vorname des Mitglieds</li>
	</ul>

  <h2>Spendenbescheinigungen</h2>
  <img src='images/Einstellungenspendenbescheinigungen.png' class='screenshot'>
  <p>Hier können die Werte zur Erstellung von Spendenbescheinigungen eingestellt werden.</p> 
  <p>Mindestbetrag: Es werden nur Spendenbescheinigungen für Einzelspenden, die diesen Betrag übersteigen
    ausgestellt.</li>
  <p>Verzeichnis: In einem Durchlauf können mehrere Spendenbescheinigungen erstellt werden. Jede Spenden
    bescheinigung wird in ein eigenes Dokument ausgegeben und in das angegebene Verzeichnis gespeichert.</li>
  <p>Buchungsart drucken: Im Normalfall wird der Verwendungszweck aus der Buchung in die Spendenbescheinigung
    übernommen. Sofern diese Option aktiviert wurde, wird der Text aus der Buchungsart genommen.</p>
    
	<h2>Buchführung</h2>
	<img src='images/Einstellungenbuchfuehrung.png' class='screenshot'>
	<p>Beginn des Geschäftsjahres in der Form TT.MM. </p> 

	<h2>Rechnungen</h2>
	<img src='images/Einstellungenrechnungen.jpg' class='screenshot'>
	<p>Texte für die einzelnen Zahlungswege für den Rechnungsdruck. In den Text zur Abbuchung können die 
	   Variablen ${Konto} und ${BLZ} eingemischt werden.</p>
	
	<h2><a name='tabellen'></a>Tabellen</h2>
	<p>Festlegung der Spalten, die in Tabellen angezeigt werden sollen. 
	<img src='images/Einstellungentabellen.png' class='screenshot'>
	

	<h2><a name=mail></a>Mail</h2>
	<img src='images/Einstellungenmail.png' class='screenshot'>
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