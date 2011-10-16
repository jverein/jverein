<? include ("frame.inc"); ?>
	<h1>Nicht mehr aktuell - wird momentan aktualisiert</h1>
    <h1>Jameica + Plugins "portable"</h1>
	<p>Autor: Marco Hügel, Links aktualisiert von Guido (mfc)</p>
	<p></p>
	<p>Wie allgemein bekannt ist setzt das GUI-Framework Jameica sowie dadurch die verschiedenen 
		Plugins wie Hibiscus (Onlinebanking), JVerein (Vereinsverwaltung), Syntax (Finanzbuchhaltung) 
		oder Jollina (Lohnbuchhaltung) zwingend ein installiertes Java Runtime Environment (JRE) ab 
		Version 1.5 auf dem jeweils genutzten PC voraus. Jameica und die Plugins 
		selbst benötigen ja keine Installation, sondern müssen nur (richtig) entpackt werden.</p>
	<p>Zu Hause hat man zumeist eine JRE installiert. Wie ist es aber, wenn man z. B. in einem 
		Internetcafe o.ä. sitzt und seine Onlinebanking-Geschäfte nicht per Browser abwickeln möchte? 
		Hier kann man nicht zwingend von einer installierten JRE ausgehen, somit kann man evtl. 
		Jameica inkl. Plugins nicht nutzen.</p>
	<p>Dieser Frage bin ich nachgegangen und habe durch Recherchen im Internet bzw. durch die 
		gegebenen Möglichkeiten von Jameica auch eine Lösung für Windows (ab 2000) gefunden, die ich 
		hiermit näher vorstellen möchte.</p>
	<h2>Was wird benötigt?</h2>
	<ul>
		<li> Jameica (<a href="http://www.willuhn.de/">http://www.willuhn.de</a>)</li>
		<li> mindestens 1 Plugin (s.o.)</li>
		<li> Java Portablizer (<a href="http://portableapps.com/apps/utilities/java_portable">
			http://portableapps.com/apps/utilities/java_portable</a>)</li>
		<li> USB-Stick oder anderer Wechseldatenträger mit mindestens 128MB freiem Speicherplatz* 
			(je nach Anzahl der installierten Plugins entsprechend mehr!)</li>
		<li> Zur Einrichtung: PC mit installierter JRE ab Version 1.5</li>
	</ul>
	<p>*Der Speicherplatz wird hauptsächlich für die JRE (ca. 80MB) benötigt!</p>
	<h2>Vorgehensweise:</h2>
	<h3>1. Überprüfung Java-Version auf dem PC</h3>
	<p>Auf dem PC, auf welchem die portable Version erstellt werden soll, muss zunächst überprüft werden, 
		ob und in welcher Version die JRE installiert ist.</p>
	<p>Hierzu öffnen wir eine Eingabeaufforderung (Start -&gt; Programme -&gt; Zubehör -&gt; Eingabeaufforderung) 
		und geben den Befehl "java -version" ein:</p>
	<img src='images/Jameica_portable-img1.png' class='screenshot'>
	<p>Wir erhalten die aktuelle Version der JRE angezeigt, hier 1.6.0_02.</p>
	<p>Hinweis:</p>
	<p>Sollte auf dem PC noch kein JRE installiert sein, so muss dies zunächst nachgeholt werden.</p>
	<p>Download JRE:<a href="https://cds.sun.com/is-bin/INTERSHOP.enfinity/WFS/CDS-CDS_Developer-Site/en_US/-/USD/ViewProductDetail-Start?ProductRef=jre-6u20-oth-JPR@CDS-CDS_Developer">
		https://cds.sun.com/is-bin/INTERSHOP.enfinity/WFS/CDS-CDS_Developer-Site/en_US/-/USD/ViewProductDetail-Start?ProductRef=jre-6u20-oth-JPR@CDS-CDS_Developer</a>
		(gefunden über die Seite: http://java.sun.com/javase/downloads/index.jsp  dort JRE unter dem linken Icon JAVA anklicken)
		</p>
	<p>Nach der Installation können Sie gegebenenfalls nochmals die Java-Version überprüfen, sollte 
		aber nicht mehr notwendig sein!</p>
	<h3>2. Entpacken von Jameica + Plugin(s) auf einen Wechseldatenträger</h3>
	<p>Falls noch nicht geschehen, laden Sie sich bitte das Paket Jameica für Windows auf Ihren PC, 
		ebenfalls das/die Plugins, die Sie auf dem Wechseldatenträger nutzen wollen.</p>
	<p>Downloadlinks:</p>
	<p><a href="http://www.willuhn.de/">http://www.willuhn.de</a></p>
	<p><a href="http://www.jverein.de/">http://www.jverein.de</a></p>
	<p><a href="http://www.jollina.de/">http://www.jollina.de</a></p>
	<p>Entpacken Sie nun zunächst das Paket jameica-win32.zip auf Ihren Wechseldatenträger. Sie können 
		das Paket entweder direkt ins Root, also z.B. X:\ entpacken, oder aber in ein Unterverzeichnis 
		des Wechseldatenträgers, z.B. X:/Portablesoftware.</p>
	<p>Anschließend entpacken Sie das/die Plugin(s) in das Pluginsverzeichnis von Jameica. In 
		vorgenannten Beispielen:</p>
	<p>X:\jameica\plugins</p>
	<p>bzw.</p>
	<p>X:\Portablesoftware\jameica\plugins</p>
	<h3>3. Java vom lokalen PC auf den Wechseldatenträger "kopieren"</h3>
	<p>Laden Sie sich, so noch nicht geschehen, den Java Portablizer von 
		<a href="http://portableapps.com/apps/utilities/java_portable">
		http://portableapps.com/apps/utilities/java_portable</a>
		herunter.</p>
	<p>Starten Sie den Java Portablizer.</p>
	<img src='images/Jameica_portable-img2.png' class='screenshot'>
	<p>Dieses Fenster einfach mit "Next" bestätigen.</p>
	<img src='images/Jameica_portable-img3.png' class='screenshot'>
	<p>Geben Sie als Destination Folder den Installations-Pfad von Jameica auf dem Wechseldatenträger 
		an und am besten als Unterverzeichnis zusätzlich "Java". Klicken Sie anschließend auf "Install".</p>
	<p>Die notwendigen Java-Dateien werden nun ebenfalls auf den Stick kopiert.</p>
	<h3>4. jameica.bat an Java anpassen</h3>
	<p>Wechseln Sie in das Verzeichnis von Jameica auf dem Wechseldatenträger:</p>
	<img src='images/Jameica_portable-img4.png' class='screenshot'>
	<p>Mit der rechten Maustaste klicken Sie auf die Datei "jameica.bat" und wählen den Punkt 
		Bearbeiten.</p>
	<img src='images/Jameica_portable-img5.png' class='screenshot'>
	<p>Hier müssen Sie nun der Datei "javaw.exe" den richtigen Pfad mitgeben, damit Jameica auf das auf 
		dem Wechseldatenträger befindliche Java zugreift. In unserem Beispiel haben wir als Pfad "Java" 
		unterhalb des Installationspfades von Jameica gewählt. Die Datei "javaw.exe" befindet sich 
		hierunter nochmals im Verzeichnis "/bin".</p>
	<p>Statt "start javaw.exe ..." müssen Sie also "start Java/bin/javaw.exe ..." eingeben. Sollten Sie 
		einen anderen Installationspfad für die JRE gewählt haben, so müssen Sie den Pfad natürlich 
		entsprechend anpassen.</p>
	<p>Über Datei -&gt; Speichern können Sie die angepasste "jameica.bat" speichern.</p>
	<h3>5. Startdatei für Daten auf dem USB-Stick erstellen</h3>
	<p>Da standardmäßig die Daten von Jameica bzw. deren Plugins nach</p>
	<p>&quot;C:\Dokumente und Einstellungen\&lt;benutzername&gt;\.jameica&quot;</p>
	<p>gespeichert werden, müssen wir noch eine Startdatei erstellen, damit die Daten auf dem 
		Wechseldatenträger gespeichert werden.</p>
	<p>Beachte:</p>
	<p>Das Datenverzeichnis muss außerhalb des Programmverzeichnisses von Jameica liegen!!!</p>
	<p>Wechseln Sie zunächst in das Programmverzeichnis von Jameica auf dem Wechseldatenträger:</p>
	<img src='images/Jameica_portable-img6.png' class='screenshot'>
	<p>Per rechter Maustaste erstellen Sie über "Neu -&gt; Textdokument" ein neues Dokument und geben diesem 
		z. B. den Namen "jameica-portable.bat".</p>
	<img src='images/Jameica_portable-img7.png' class='screenshot'>
	<p>Diese Meldung mit "Ja" bestätigen.</p>
	<img src='images/Jameica_portable-img8.png' class='screenshot'>
	<p>Mit der rechten Maustaste klicken Sie auf die eben erstellte Datei und wählen den Punkt 
		"Bearbeiten", es öffnet sich ein leeres Editorfenster, in welches wir z. B. folgendes eingeben:</p>
	<img src='images/Jameica_portable-img9.png' class='screenshot'>
	<p>Beachte: Sollten Sie einen Pfad mit Unterordner(n) erstellen wollen z.B. "..\daten\jameica", 
		dann müssen die übergeordneten Ordner manuell angelegt werden. Im Beispiel muss also der 
		Ordner ..\daten bereits angelegt sein!</p>
	<p>Sollte der von Ihnen gewählte Pfad ein Leerzeichen enthalten, so setzen Sie den Pfad bitte in 
		Anführungszeichen, also so:</p>
	<img src='images/Jameica_portable-img10.png' class='screenshot'>
	<p>Anschließend speichern wir diese Datei über "Datei -&gt; Speichern" wieder ab.</p>
	<p><b>Fertig ist unsere portable Version von Jameica und dessen/deren Plugin(s)!!!</b></p>
	<p>Die portable Version von Jameica starten Sie bitte über die Datei "jameica-portable.bat". 
		Sollten Sie der Datei einen anderen Namen verpasst haben, dann entsprechend.</p>
	<p><center>Viel Spaß!!!</center></p>
	<p><b>Weitere Hinweise:</b></p>
	<ul>
		<li> Es wäre möglich, dass man die JRE für den Betrieb von Jameica und dessen Plugins noch 
			um diverse Dateien bereinigen kann, um den Umfang der Installation zu verringern. Leider 
			fehlen mir dazu die Kenntnisse!</li>
		<li> Wer bereits ein Java auf seinem Wechseldatenträger "installiert" hat kann auch dieses 
			nutzen, muss aber entsprechend die Pfade in der Datei "jameica.bat" anpassen.</li>
		<li> Für die weitere Einrichtung/Nutzung von Jameica bzw. der Plugins beachten Sie bitte 
			die Dokumentationen zum jeweiligen Produkt.</li>
	</ul>
	<p><b>Zusatzhinweis für Nutzer von komfortablen "Portable Apps Launcher"</b></p>
	<p>Es gibt diverse Tools mit denen man sich ein komfortables Startmenü für portable Programme 
		auf einem Wechseldatenträger erstellen kann.</p>
	<p>Sollte man ein solches im Einsatz haben, so sollte natürlich der Installationspfad entsprechend 
		deren Vorgaben gewählt werden.</p>
	<p>Durch den Einsatz eines solchen Programmes kann u. a. Punkt 5. (erstellen einer zusätzlichen 
		Startdatei) entfallen, so man der Startdatei Parameter mitgeben kann.</p>
	<p>Hier am Beispiel von ASuite (<a href="http://www.salvadorsoftware.com/software/asuite">
		http://www.salvadorsoftware.com/software/asuite</a>) erläutert.</p>
	<img src='images/Jameica_portable-img11.png' class='screenshot'>
	<p>In den Eigenschaften des Programmes:</p>
	<img src='images/Jameica_portable-img12.png' class='screenshot'>
	
<? include ("footer.inc"); ?>

