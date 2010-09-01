<? include ("frame.inc"); ?>
    <h1>MySQL-Support</h1>
    <h2>Vorbemerkungen</h2>
	<p>JVerein verwendet standardmäßig eine embedded Datenbank 
		(<a href="http://www.h2database.com/html/main.html">H2</a>), die beim ersten Start automatisch 
		eingerichtet wird. Seit JVerein 1.0 wird auch MySQL unterstützt. Somit ist es möglich, 
		eine JVerein-Datenbank von mehreren Arbeitsplätzen aus gleichzeitig zu nutzen.</p>
	<h2>Erstellung der MySQL-Datenbank</h2>
	<p>Verwenden Sie Ihr bevorzugtes Administrationswerkzeug 
		(z.Bsp. [<a href="http://www.phpmyadmin.net">PhpMyAdmin</a> oder 
		<a href="http://dev.mysql.com/downloads/gui-tools/5.0.html">MySQL-Administrator</a>), 
		um eine Datenbank mit dem Namen "jverein" sowie einen Benutzer anzulegen oder führen 
		Sie folgende Kommandos aus, um Datenbank und Benutzer mit dem Kommandozeilen-Werkzeug 
		"mysql" ("mysql.exe" unter Windows) anzulegen. Der angelegte Benutzer muss Lese- 
		und Schreibrechte in dieser Datenbank besitzen.</p>
		<ul>
			<li> Als Benutzer root auf der Datenbank anmelden:</li>
		</ul>
		<p>Linux:</p>
		<pre> mysql -u root -p</pre>
		<p>Windows: Öffnen Sie zuerst eine Eingabeaufforderung ("cmd.exe") und geben Sie 
			dann ein:</p>
		<pre> C:\Programme\mysql\bin\mysql.exe -u root -p</pre>
		<ul>
			<li> Datenbank anlegen:</li>
		</ul>
		<pre> mysql&gt; create database jverein;</pre>
		<ul>
			<li> Benutzer anlegen. Wenn die Datenbank im ganzen Intranet erreichbar sein 
				soll, verwenden Sie statt "localhost" beispielsweise "192.168.1.%", wenn 
				die IP-Adressen aller PCs in Ihrem LAN mit "192.168.1." beginnen oder "%", 
				wenn keine Einschränkungen gelten sollen.</li>
		</ul>
		<pre> 
		mysql&gt; CREATE USER 'jverein'@'localhost' IDENTIFIED BY '&lt;passwort&gt;';
		mysql&gt; GRANT ALL PRIVILEGES ON jverein.* TO 'jverein'@'localhost';
		</pre>
		<h2>Erstellung eines Install-Bundles und der Datenbank</h2>
		<p>Damit JVerein auf eine MySQL-Datenbank zugreifen kann, muss eine Konfigurationdatei 
			angepasst werden. Da diese beim ersten Start noch nicht existiert, würde JVerein 
			auf jedem Arbeitsplatz unnötig eine Embedded H2-Datenbank anlegen, die 
			anschliessend nicht gebraucht wird. Bereiten Sie daher mit den folgenden 
			Schritten ein vorkonfiguriertes Bundle vor, welches anschließend einfach 1:1 
			auf alle Arbeitsplatz-PCs kopiert werden kann.</p>
		<ul>
			<li> Installieren sie wie <a href="installation.php">beschrieben</a>. 
				Falls sie ein "heterogenes" Netz mit Windows- und Linux-Arbeitsplätzen nutzen, 
				dann verwenden Sie die All-In-One-Version von Jameica, welche unter beiden 
				Betriebssystemen lauffähig ist. Andernfalls können Sie die Windows- oder 
				Linux-Version verwenden.</li>
			<li> Erstellen Sie nun manuell ein Verzeichnis "cfg" im Programm-Verzeichnis 
				von Jameica.</li>
			<li> Erstellen Sie in diesem Verzeichnis eine Datei mit dem Namen 
				"de.jost_net.JVerein.rmi.JVereinDBService.properties". Öffnen Sie diese 
				mit einem Texteditor und tragen Sie folgenden Inhalt ein:
			</li>
		</ul>
		<pre> 
		database.driver=de.jost_net.JVerein.server.DBSupportMySqlImpl
 		database.driver.mysql.jdbcurl=jdbc\:mysql\://&lt;Server-IP&gt;\:&lt;port&gt;/&lt;datenbankname&gt;?useUnicode\=Yes&amp;characterEncoding\=ISO8859_1
 		database.driver.mysql.username=&lt;Username des MySQL-Users&gt;
 		database.driver.mysql.password=&lt;Passwort des MySQL-Users&gt;
 		database.driver.mysql.scriptprefix=mysql-
		</pre>
		<ul>
			<li> Ersetzen Sie die Werte &lt;datenbankname&gt;, &lt;port&gt;, 
				&lt;Username des MySQL-Users&gt;, &lt;Server-IP&gt; und
				&lt;Passwort des MySQL-Users&gt; durch den Datenbanknamen, 
				den Hostnamenoder die IP-Adresse des MySQL-Servers, den Port 
				(Standard: 3306), sowie Username und Passwort des MySQL-Benutzers. 
				(Siehe folgender Schritt für die Einrichtung der Datenbank).
			</li>
			<li> Erstellen Sie auf der MySQL-Datenbank auf dem Server einen neuen 
				Benutzer sowie eine Datenbank mit einem beliebigen Namen. Der angelegte 
				Benutzer muss Lese- und Schreibrechte in dieser Datenbank besitzen.</li>
		</ul>
		<h2>Test und Verteilung auf die Arbeitsplätze</h2>
		<p><b>Wichtig:</b> Die gerade manuell erstellte Konfigurations-Datei wird nur 
			dann verwendet, wenn noch kein Jameica-Benutzerverzeichnis mit abweichenden 
			Angaben existiert. Prüfen Sie also vor dem ersten Start, ob dieses existiert 
			und benennen Sie es ggf. während des Tests um:
		</p>
		<pre> 
		Linux: /home/&lt;username&gt;/.jameica
 		Windows: C:\Dokumente und Einstellungen\&lt;username&gt;\.jameica
		</pre>
		<ul>
			<li> Starten Sie nun diese Jameica-Installation durch Aufruf von "jameica.sh" 
				bzw. "jameica.bat". JVerein sollte nun keine eigene Datenbank erstellen 
				sondern stattdessen direkt auf die MySQL-Datenbank zugreifen.</li>
			<li> Verteilen Sie nun das vorkonfigurierte Install-Bundle (im Beispiel also 
				"C:\download\jameica") auf alle teilnehmenden Arbeitsplatz-PCs.</li>
			<li> Beachten Sie, daß auch auf den anderen Arbeitsplatz-PCs noch kein 
				Jameica-Benutzerverzeichnis existieren darf, da sonst die dort angegebene 
				Datenbank-Konfiguration (welche auf die interne H2-Datenbank verweist) 
				verwendet wird.</li>
		</ul>
		<p><b>Hinweis:</b> Auf allen Arbeitsplätzen muss die gleiche Version von JVerein 
			im Einsatz sein. Durch neue Versionen wird u. U. die Datenbankstruktur so 
			verändert, dass ältere Versionen damit nicht klar kommen.</p>
		<h3>Sicherheitshinweise</h3>
		<p>Nutzen Sie MySQL nur in gesicherten und vertrauenswürdigen Intranets, da die 
			Datenübertragung von MySQL standardmäßig unverschlüsselt erfolgt. Lesen Sie 
			alternativ die MySQL-Dokumentation zu 
			<a href="http://dev.mysql.com/doc/refman/5.1/de/secure-basics.html">
			Grundlegenden SSL-Konzepten</a> sowie der Einrichtung von 
			<a href="http://dev.mysql.com/doc/refman/5.1/de/connector-j-reference-using-ssl.html">
			SSL für MySQL</a>. 
			Die manuelle Erstellung sowie der Import des Server-Zertifikats sollte auf den 
			Arbeitsplätzen jedoch nicht nötig sein, da Jameica einen eigenen Keystore 
			verwendet und den Benutzer automatisch bei Bedarf zum Import des Zertifikats 
			auffordert.</p>
			
    <!-- 
    $Log$
    Revision 1.1  2009/05/08 14:46:22  jost
    shtml - php

    Revision 1.1  2009/05/03 15:33:30  jost
    Neue Homepage

    -->
    
<? include ("footer.inc"); ?>

