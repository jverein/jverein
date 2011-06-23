<? include ("frame.inc"); ?>
    <h1>Speicherung von Dokumenten</h1>
	<p>ab Version 2.0 verfügbar</p>
	
	<h2>Voraussetzungen</h2>
	<p>Die Dokumentenarchivierung wird über den ArchiveService von Jameica realisiert. 
	Entweder erfolgt die Installation lokal oder innerhalb eines LANs. Bei der Installation
	im LAN wird in einer Jameica-Instanz der Archive-Service installiert und gestartet. 
	In den weiteren Jameica-Instanzen ist keine Installation erforderlich. Jameica findet
	die zentrale Instanz automatisch im LAN. Unter Einstellungen ist die Dokumentenspeicherung
	zu aktivieren.</p>
	
	<p>Für den ArchiveService müssen folgende Jameica-Plugins zusätzlich installiert werden:</p>
	
	<ul>
		<li>jameica.messaging</li>
		<li>jameica.soap</li>
		<li>jameica.webadmin</li>
		<li>jameica.xmlrpc</li>
	</ul>
    <p>Die Plugins stehen als Nightly-Builds unter <a href='http://www.willuhn.de/products/jameica/download_ext.php'>erweiterte Jameica-Downloads</a>
	zur Verfügung. Zur Installation werden die Installationsdateien in das plugins-Verzeichnis von Jameica entpackt.</p>

	<h2>Allgemein</h2>
	<p>Zu Mitgliedern und Buchungen können Dokumente beliebiger Art
	und Anzahl gespeichert werden. Die Funktionalität steht nach der Installation der zusätzlichen 
	Plugins ohne weitere Konfiguration zur Verfügung.</p> 
	
    <h2>Neue Dokumente speichern</h2>
    <img src='images/dokumenteneu1.png' class='screenshot'>
    <p>Mit einem Klick auf neu öffnet sich folgendes Formular:</p>
    <img src='images/dokumenteneu2.png' class='screenshot'>
    <p>Es wird eine Datei ausgewählt. Standardmässig wird das Tagesdatum vorgegeben.
    Es kann hier z. B. auch das Datum des Beleges eingetragen werden. Zusätzlich kann
    zu jedem Dokument ein Kommentar eingetragen werden.</p>
 
    <h2>Dokumente anzeigen oder löschen</h2>
    <p>Mit einem Rechtsklick auf ein Dokument öffnet sich ein Kontextmenü. Mit den Menüpunkten
    kann ein Dokument entweder angezeigt oder gelöscht werden.</p>
    
    <h2>Datensicherung</h2>
    <p>Die gespeicherten Dokumente werden nicht durch die Sicherung von Jameica erfasst. Am einfachsten
    ist es, dass .jameica-Verzeichns komplett zu sichern.</p> 
    
    <!-- 
    $Log$
    Revision 1.3  2011-01-08 15:59:28  jost
    *** empty log message ***

    Revision 1.2  2010-12-17 19:07:31  jost
    *** empty log message ***

    Revision 1.1  2010-12-12 17:28:09  jost
    Neu: Speicherung von Dokumenten

    -->
    
<? include ("footer.inc"); ?>

