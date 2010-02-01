<? include ("frame.inc"); ?>
    <h1>Administration: Einstellungen</h1>
    <p>Durch die Einstellungen kann das Verhalten von JVerein beeinflußt werden.</p>
    <h3>Ansicht</h3>
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
	
	<h2>Beiträge</h2>
    <img src='images/Einstellungenbeitraege.jpg' class='screenshot'>
	<p>Beitragsmodell, siehe auch Beitragsmodelle</</p>
	<p>Ab Version 1.3 können die Standardwerte für den Zahlungsweg und den Zahlungsrhytmus für die Neueingabe von Mitgliedern eingestellt werden.</p>
	
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
	<p>Beginn des Geschäftsjahres in der Form TT.MM. (Ab Version 1.1)</p> 

	<h2>Rechnungen</h2>
	<img src='images/Einstellungenrechnungen.jpg' class='screenshot'>
	<p>Festlegung, für welche Zahlungswege Rechnungsinformationen gespeichert werden sollen. (Ab Version 1.1)</p>
	
	<h2><a name='tabellen'></a>Tabellen</h2>
	<p>Festlegung der Spalten, die in Tabellen angezeigt werden sollen. 
	<img src='images/Einstellungentabellen.jpg' class='screenshot'>
	

	<h2><a name=mail></a>Mail</h2>
	<img src='images/Einstellungenmail.jpg' class='screenshot'>
	<p>Angaben für den Mail-Versand ab Version 1.3</p>
	
	<!-- 
    $Log$
    Revision 1.7  2010/01/10 20:59:50  jost
    *** empty log message ***

    Revision 1.6  2010/01/01 22:37:06  jost
    Standardwerte für Zahlungsweg und Zahlungsrhytmus können vorgegeben werden.

    Revision 1.5  2009/11/19 21:11:43  jost
    *** empty log message ***

    Revision 1.4  2009/11/17 21:05:16  jost
    *** empty log message ***

    Revision 1.3  2009/09/13 19:20:57  jost
    *** empty log message ***

    Revision 1.2  2009/09/01 19:53:56  jost
    Einstellungen Tabellen korrekt beschrieben

    Revision 1.1  2009/05/08 14:46:22  jost
    shtml - php

    Revision 1.3  2009/05/05 17:28:50  jost
    Neu: Juristische Personen als Mitglieder

    Revision 1.2  2009/05/03 15:33:30  jost
    Neue Homepage

    -->
	
	
 <? include ("footer.inc"); ?>

