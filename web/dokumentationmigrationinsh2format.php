<? include ("frame.inc"); ?>
    <h1>Migrations der Datenbank ins H2-Format</h1>
	<h2>Wie läuft das ab?</h2>
	<p>Mit dem Update auf JVerein 1.0 (bzw. einer Entwicklerversion ab 02.12.2007) wird diese Migration 
		bei Bedarf automatisch gestartet. JVerein erkennt hierbei selbst, ob noch die alte McKoi-Datenbank 
		verwendet wird und führt die Datenübernehme nach eine Sicherheitsabfrage selbst durch. Der 
		Benutzer muss hierbei also nichts manuell konfigurieren sondern lediglich den Anweisungen folgen.</p>
	<p>Die neue Datenbank wird in folgendem Verzeichnis angelegt (insofern nicht explizit ein abweichendes 
		Benutzerverzeichnis angegeben wurde):</p>
	<table>
		<tr>
			<th>Betriebssystem</th>
			<th>Alte McKoi-Datenbank</th>
			<th>Neue H2-Datenbank</th>
		</tr>
		<tr>
			<td>Linux</td>
			<td>/home/&lt;benutzername&gt;/.jameica/jverein/db</td>
			<td>/home/&lt;benutzername&gt;/.jameica/jverein/h2db</td>
		</tr>
		<tr>
			<td>Windows 2000/XP</td>
			<td>C:\Dokumente und Einstellungen\&lt;benutzername&gt;\.jameica\jverein\db</td>
			<td>C:\Dokumente und Einstellungen\&lt;benutzername&gt;\.jameica\jverein\h2db</td>
		</tr>
		<tr>
			<td>Windows Vista</td>
			<td>C:\[Users/Benutzer]\&lt;benutzername&gt;\.jameica\jverein\db</td>
			<td>C:\[Users/Benutzer]\&lt;benutzername&gt;\.jameica\jverein\h2db</td>
		</tr>
		<tr>
			<td>MacOS</td>
			<td>/Users/&lt;benutzername&gt;/.jameica/jverein/db</td>
			<td>/Users/&lt;benutzername&gt;/.jameica/jverein/h2db</td>
		</tr>
	</table>
	<p>Die alte McKoi-Datenbank wird von dem Migrationsvorgang nicht gelöscht sondern bleibt zu 
		Backup-Zwecken erhalten.</p>
	<h2>Was ist, wenn etwas schief geht?</h2>
	<p>Falls es bei der Migration wider Erwarten zu einem Fehler kommen sollte und die Benutzerdaten 
		verloren gegangen sind, kann die Datenmigration rückgängig gemacht und wieder auf die alte 
		McKoi-Datenbank gewechselt werden. Öffne hierzu die Datei</p>
	<pre> de.jost_net.jverein.rmi.JVereinDBService.properties</pre>
	<p>in einem Texteditor. Sie befindet sich im Cfg-Verzeichnis...</p>
	<table>
		<tr>
			<th>Betriebssystem</th>
			<th>Verzeichnis</th>
		</tr>
		<tr>
			<td>Linux</td>
			<td>/home/&lt;benutzername&gt;/.jameica/cfg</td>
		</tr>
		<tr>
			<td>Windows 2000/XP</td>
			<td>C:\Dokumente und Einstellungen\&lt;benutzername&gt;\.jameica\cfg</td>
		</tr>
		<tr>
			<td>Windows Vista</td>
			<td>C:\[Users/Benutzer]\&lt;benutzername&gt;\.jameica\cfg</td>
		</tr>
		<tr>
			<td>MacOS</td>
			<td>/Users/&lt;benutzername&gt;/.jameica/cfg</td>
		</tr>
	</table>
	<p>...und enthält folgende Zeile:</p>
	<pre> database.driver=de.jost_net.jverein.server.DBSupportH2Impl</pre>
	<p>Ändere sie in (Gross-Kleinschreibung beachten!):</p>
	<pre> database.driver=de.jost_net.jverein.server.DBSupportMcKoiImpl</pre>
	<p>Beim nächsten Start von Hibiscus wird nun wieder die Sicherheitsabfrage zum Start der 
		Datenmigration erscheinen. Beantworte die Abfrage mit <i>Nein</i>, um vorerst weiterhin 
		mit der alten Datenbank arbeiten zu können. Melde den Fehler jedoch bitte per Mail an 
		<a href='mailto:heiner@jverein.de'>heiner@jverein.de</a>.</p>
	<h2>Was ist noch zu beachten?</h2>
	<p>Die Datei <i>de.jost_net.jverein.rmi.HBCIDBService.properties</i> enthält unter anderem 
		folgenden Parameter:</p>
	<pre> database.driver.h2.encryption.encryptedpassword=....</pre>
	<p>Er enthält das verschlüsselte Passwort der H2-Datenbank. Zusammen mit dem bei jedem Start 
		eingegebenen Master-Passwort und der Datei <i>jameica.keystore</i> (befindet sich ebenfalls 
		im Cfg-Verzeichnis) sind die Benutzerdaten in der Datenbank geschützt. Geht nur eine dieser 
		drei Informationen verloren, dann ist auch die komplette Benutzerdatenbank unwiederbringlich 
		verloren. Es existiert keine Möglichkeit, die Datenbank ohne diese Schlüssel wiederherzustellen. 
		Erstelle daher <b>regelmäßig</b> Backups des kompletten Benutzerverzeichnisses <i>.jameica</i>!</p>

    <!-- 
    $Log$
    Revision 1.1  2009/05/03 15:33:30  jost
    Neue Homepage

    -->

<? include ("footer.inc"); ?>

