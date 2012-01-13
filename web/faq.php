<? include ("frame.inc"); ?>
    <h1>FAQ</h1>
    <h2>Inhaltsverzeichnis</h2>
    <a href='#neuerlaptop'>Wie kann ich JVerein auf einen neuen Rechner bringen?</a><br>
    <a href='#programmordner'>Wo liegt der JVerein-Programmordner?</a><br>
    <a href='#datenordner'>Wo liegt der JVerein-Datenordner standardmäßig?</a><br>
    <a href='#datenordnerverschieben'>Wie kann ich den Datenordner an einen Nichtstandardplatz legen?</a><br>
    <a href='#internet'>Kann JVerein über ein Netzwerk/Internet betrieben werden?</a><br>
    <a href='#bankverbindung'>Warum kann ich eine Bankverbindung nicht speichern/importieren?</a><br>
    <br>
    <a name="neuerlaptop"></a>
    <p>Q: Ich habe mir einen neuen Laptop zugelegt. Nun möchte ich von meinem alten Laptop alles auf den Neuen übertragen.
          Welche Dateien bzw. Ordner muss ich mitnehmen? Muss ich das Prog. neu installieren?<br>
       A: Es muss der Programm- und der Datenordner auf den neuen Rechner übertragen werden.</p>
    <a name="programmordner"></a>
    <p>Q: Wo finde ich den Programmordner?<br>
       A: Es gibt keinen fest definierten Platz für den Programmordner. Sofern die Jameica-Suite
          installiert wurde, ist der Ordner unter c:\Programme\Jameica zu finden. Unter Linux 
          wird oft unter /opt/jameica oder ~/jameica installiert.</p>
    <a name="datenordner"></a>
    <p>Q: Wo finde ich den Datenordner standardmäßig?<br>
       A:
          <table>
          <tr>
          <th>Betriebssystem</th><th>Verzeichnis</th>
          </tr>
          <tr>
          <td>Linux</td><td>/home/&lt;username&gt;/.jameica</td>
          </tr>
          <tr>
          <td>Windows 2000/XP</td><td>C:\Dokumente und Einstellungen\&lt;username&gt;\.jameica</td>
          </tr>
          <tr>
          <td>Windows Vista</td><td>C:\Users\&lt;username&gt;\.jameica oder C:\Benutzer\&lt;username&gt;\.jameica</td>
          </tr>
          <tr>
          <td>MacOS</td><td>/Users/&lt;username&gt;/.jameica oder /Users/&lt;username&gt;/Library/jameica (falls Jameica 1.7 die erste benutzte Version war, dann letzteres, sonst das erst-genannte)</td>
          </tr>
          </table>    
          </p>
    <a name="datenordner"></a>
    <p>Q: Wie kann ich den Datenordner an einen Nichtstandardplatz legen?<br>
       A:
          <p>
          Beim Aufruf von Jameica wird der Schalter <code>-f pfad</code> angegeben.</p>
          <p>Beispiel: jameica.bat -f c:/meinejameicadaten</p>
          <p>siehe auch <a href='http://jameica.berlios.de/doku.php?id=support:install#optionale_startparameter'>Jameica-Doku</a>.
          <p>Unter Windows kann mit einem rechten Mausklick auf das Jameica-Icon &gt; Eigenschaften folgendes Bild geöffnet
             werden:</p>
          <img src='../images/jameicasuiteeigenschaften.png' class='screenshot'>
          <p>Im Feld Ziel wird der Schalter -f VERZEICHNIS wie angegeben verändert.</p>
    <a name='internet'></a>
    <p>Q: Kann JVerein übers Netzwerk/Internet betrieben werden?<br>
       A: JVerein kann seine Daten in einer MySQL-Datenbank speichern. Siehe auch 
          <a href='http://www.jverein.de/dokumentationmysql.php'>MySQL-Support</a>.
          Beim Betrieb über das Internet ist darauf zu achten, dass die Daten 
          verschlüsselt übertragen werden. Weiterhin wird darauf hingewiesen, dass Jameica,
          Hibiscus und JVerein keine Benutzerverwaltung haben. Jeder, der Zugriff auf das
          Verfahren hat, kann alles machen. Beim Umgang mit Geld sicher eine nicht
          befriedigende Angelegenheit. Es ist auch nicht nachvollziehbar, wer welche
          Änderung vorgenommen hat. Mir stellt sich allerdings immer wieder die Frage nach
          dem "Warum?". Meines Erachtens können die Daten durch eine Person gepflegt werden.
          Ich bin selber Kassenwart eines Vereins mit ca. 400 Mitgliedern und weiß somit 
          wovon ich rede. Den übrigen Vorstandsmitgliedern können PDF-Dokumente oder CSV-Dateien 
          zur Serienbriefgenerierung zur Verfügung
          gestellt werden. Darin kann auch ohne Probleme gesucht werden.</p>
    <a name='bankverbindung'> </a>
    <p>Q: Warum kann ich eine Bankverbindung nicht speichern/importieren?<br>
       A: Die Banken verwenden Prüfziffernmethoden zur Überprüfung der Kontonummern.
          Siehe <a href="http://www.bundesbank.de/zahlungsverkehr/zahlungsverkehr_pruefziffernberechnung.php">Prüfziffernberechnung bei der Deutschen Bundesbank</a>.
          JVerein verwendet zur Berechnung der Prüfziffern die Bibliothek 
          <a href="http://hbci4java.kapott.org/">HBCI4Java</a>. Bisher ist mir kein Fehler in der 
          Prüfziffernberechnung bekannt. Daher gehe ich davon aus, das die Meldung immer korrekt
          ausgegeben wird. Die Prüfziffernberechnung kann unter Hibiscus | Einstellungen | Grundeinstellungen |
          Kontonummern und Bankleitzahlen mittels Prüfziffern testen ausschalten. Damit wird die 
          Prüfziffernberechnung generell ausgeschaltet. Das gilt sowohl für Hibiscus als auch für JVerein.
 </p> 
          
<? include ("footer.inc"); ?>

