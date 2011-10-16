<? include ("frame.inc"); ?>
    <h1>H2 Datenbank mit OpenOffice-Base öffnen</h1>
    <p>Wichtig! Beim Zugriff mit OpenOffice-Base auf die JVerein-Datenbank muss JVerein geschlossen sein.</p>
    <h2>H2.jar in den Classpath aufnehmen</h2>
	<p>Die Bibliothek h2.jar mit dem Datenbank-Treiber ist in den OpenOffice-Classpath aufzunehmen. 
	   Dazu irgendein OpenOffice-Modul (z. B. Writer) öffnen. Unter Extras>Optionen>OpenOffice.org>Java>Class Path
	   den Pfad zur h2.jar auswählen. Im Normalfall ist die Bibliothek im Jameica-Verzeichnis im Lib-Verzeichnis vorhanden.</p>
	<img src='images/oobaseh2classpath1.png' class='screenshot'>
	<img src='images/oobaseh2classpath2.png' class='screenshot'>
	
	<h2>Datenbankassistent</h2>
	<p>OpenOffice-Base aufrufen</p>
	<img src='images/oobaseh2datenbankassistent0.png' class='screenshot'>
	<p>Als Datenquellen-URL ist h2:/pfad zur Datenbank   einzutragen. Beispiel: <code>h2:/home/heiner/jameica.buch/jverein/h2db/jverein</code>
	   Der Datenbanktreiber heißt org.h2.Driver</p>
	<img src='images/oobaseh2datenbankassistent1.png' class='screenshot'>
	<p>Benutzername: jverein, Kennwort erforderlich</p>
	<img src='images/oobaseh2datenbankassistent2.png' class='screenshot'>
	<p>Passwort: jverein</p>
	<img src='images/oobaseh2passwort.png' class='screenshot'>
	
<? include ("footer.inc"); ?>

