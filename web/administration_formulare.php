<? include ("frame.inc"); ?>
    <h1>Administration: Formulare</h1>
    <p>
    <p>In JVerein werden u. a. für Spendenbescheinigungen Formulare hinterlegt. Bei den Formularen 
    handelt es sich um PDF-Dokumente. Diese PDF-Dokumente können mit einem beliebigen Programm 
    (z. B. OOWriter oder in Verbindung mit einem PDF-Drucker wie FreePDF) erstellt werden. In 
    JVerein können zu jedem Formular Formularfelder definiert werden. Diese Formularfelder werden 
    auf dem PDF-Dokument an festen Stellen positioniert. JVerein schreibt dann an diesen Positionen 
    mit dem für das jeweilige Formularfeld definierten Zeichensatz und in der gewünschten 
    Zeichengröße Inhalte aus der Datenbank oder die auf der jeweiligen Maske eingetragenen Werte.</p>
    
    <p>Liste der bereits definierten Formulare:</p>
    <img src='images/Formulare.jpg' class='screenshot'>
    <p>Durch einen Klick auf "neu" bzw. mit einem Doppelklick auf ein bestehendes Formular öffnet sich
    das Formular-Bearbeitungsfenster - im zweiten Fall bereits gefüllt mit den vorhandenen Werten 
    (siehe unten). Mit einem Rechtsklick auf ein bestehendes Formular öffnet sich ein Kontextmenü. 
    Dort kann man auswählen: Der Dialog zur Definition von neuen bzw. zur Bearbeitung von bestehenden 
    Formularfeldern für das jeweilige Formular kann geöffnet werden, das Formular selbst kann mit den 
    Formularfeldern angezeigt werden, wobei Beispieldaten eingefügt werden, oder das Formular kann 
    gelöscht werden.</p>
    <img src='images/Formular.jpg' class='screenshot'>

	<p>Jedem Formular wird ein eindeutiger Name gegeben. Ein Formulartyp ist auszuwählen. Bei der 
	Neuaufnahme oder beim Update eines Formulares ist eine PDF-Datei auszuwählen. Die PDF-Vorlage 
	wird in der Datenbank gespeichert, d. h. wenn die Vorlage auf der Festplatte geändert wird, 
	hat das erst mal keine Auswirkungen auf JVerein bis es erneut importiert wird.</p>
	<p>Liste der Formularfelder:</p>
    <img src='images/Formularfelder.jpg' class='screenshot'>
    <p>Folgende Formularfelder stehen für Spendenbescheinigungen zur Verfügung:
    <table>
    	<tr>
    		<td>Tagesdatum</td><td>Enthält das aktuelle Datum im Format TT.MM.JJJJ</td>
    	</tr>
    	<tr>
    		<td>Empfänger</td><td>Empfänger der Spendenbescheinigung. Der jeweilige Dialog zur 
    								Ausstellung einer Spendenbescheinigung bzw. einer Rechnung 
    								enthält mehrere Zeilen für die Adresse. In manchen Dialogen 
    								werden diese Zeilen bereits aus den Bestandsdaten automatisch 
    								gefüllt. Sie werden an der definierten Position untereinander 
    								ausgegeben.</td>
    	</tr>
    	<tr>
    		<td>Bescheinigungsdatum<td>Datum der Bescheinigung aus der Datenbank. Formatiert TT.MM.JJJJ</td>
    	</tr>
    	<tr>
    		<td>Betrag</td><td>Höhe der Spende</td>
    	</tr>
    	<tr>
    		<td>Betrag in Worten</td><td>Der Betrag ausgeschrieben in Worten.</td>
    	</tr>
    	<tr>
    		<td>Spendedatum</td><td>Datum der Spende aus der Datenbank. Formatiert TT.MM.JJJJ</td>
    	</tr>
    	<tr>
    		<td>ErsatzAufwendungen</td><td>Aus der Datenbank</td>
    	</tr>
    </table>
    </p>
    <p>Folgende Formularfelder stehen für Rechnungen zur Verfügung:
    <table>
    	<tr>
    		<td>Tagesdatum</td><td>Enthält das aktuelle Datum im Format TT.MM.JJJJ</td>
    	</tr>
    	<tr>
    		<td>Empfänger</td><td>Empfänger der Rechnung. Formatiert für ein Adressfeld.</td>
    	</tr>
    	<tr>
    		<td>Zahlungsgrund</td><td>Multipel. Ab Version 1.4 können mehrere Positionen für ein Mitglied 
    		    in Rechnung gestellt werden. Zur korrekten Darstellung ist "Zahlungsgrund" zu 
    		    verwenden.</td>
    	</tr>
    	<tr>
    		<td>Zahlungsgrund1</td><td>Sollte ab Version 1.4 nicht mehr verwendet werden.</td>
    	</tr>
    	<tr>
    		<td>Zahlungsgrund2</td><td>Sollte ab Version 1.4 nicht mehr verwendet werden.</td>
    	</tr>
    	<tr>
    		<td>Buchungsdatum</td><td>Multipel</td>
    	</tr>
    	<tr>
    		<td>Betrag</td><td>Multipel</td>
    	</tr>
    	<tr>
    		<td>sowie alle Felder des Mitgliedsdatensatzes</td><td>&nbsp;</td>
    	</tr>
    </table>
    </p>
    <p>Im Menü "Formularfelder" öffnet sich durch einen Klick auf "neu" bzw. mit einem Doppelklick 
    auf ein bestehendes Formularfeld der Dialog zur Bearbeitung des Formularfelds - im zweiten Fall 
    bereits gefüllt mit den vorhandenen Werten (siehe unten). Mit einem Rechtsklick auf ein 
    bestehendes Formularfeld öffnet sich ein Kontextmenü, über das das Formularfeld gelöscht werden 
    kann.</p>
    
    <p>Die Definition eines Formularfeldes besteht aus dem Inhalt, der über ein vordefiniertes Feld 
    belegt wird (Empfänger, Betrag etc.), der Startposition auf der Seite (gemessen von links und 
    unten in Millimetern, eine Positionierung auf hundertstel Millimeter ist möglich), der 
    Schriftart/Font (auswählbar aus einer Liste) und der Schriftgröße (Font-Höhe).</p>

	<p>Wenn die Formularfelder definiert wurden, kann die Position in Verbindung mit dem aktuellen 
	Formular überprüft und angepasst werden. Gehe hierzu in das Menü "Formulare", markiere das 
	aktuell bearbeitete Formular und gehe über das Kontext-Menü (rechter Mausklick) auf "anzeigen". 
	Prüfe das Aussehen des generierten Formulars anhand der Testdaten und korrigiere gegebenenfalls 
	noch einmal die Positionen der Formularfelder und die Schriftgrößen bis das Gesamtbild passt.</p>
	<img src='images/Formularfeld.jpg' class='screenshot'>
	
    <p>Beispiel für ein Formular (ohne Daten)</p>
    <img src='images/Formularroh.jpg' class='screenshot'>
    <p>Beispiel für ein ausgefülltes Formular</p>
    <img src='images/Formularausgefuellt.jpg' class='screenshot'>
    
    <!-- 
    $Log$
    Revision 1.2  2010/01/03 08:59:09  jost
    *** empty log message ***

    Revision 1.1  2009/05/08 14:46:22  jost
    shtml - php

    Revision 1.2  2009/05/03 15:33:30  jost
    Neue Homepage

    -->
    
<? include ("footer.inc"); ?>

