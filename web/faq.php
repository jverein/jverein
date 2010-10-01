<? include ("frame.inc"); ?>
    <h1>FAQ</h1>
    <h2>Inhaltsverzeichnis</h2>
    <a href='#neuerlaptop'>Wie kann ich JVerein auf einen neuen Rechner bringen?</a><br>
    <a href='#programmordner'>Wo liegt der JVerein-Programmordner?</a><br>
    <a href='#datenordner'>Wo liegt der JVerein-Datenordner standardmäßig?</a><br>
    <a href='#datenordnerverschieben'>Wie kann ich den Datenordner an einem Nichtstandardplatz legen?</a><br>
    <a href='#internet'>Kann JVerein über ein Netzwerk betrieben werden?</a><br>
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
    <p>Q: Wie kann ich den Datenordner an einem Nichtstandardplatz legen?<br>
       A:
          <p>
          Beim Aufruf von Jameica wird der Schalter <code>-f pfad</code> angegeben.</p>
          <p>Beispiel: jameica.bat -f c:/meinejameicadaten</p>
    <a name='internet'></a>
    <p>Q: Kann JVerein übers Netzwerk betrieben werden?<br>
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
    <!-- 
    $Log$
    Revision 1.4  2010-08-08 14:46:12  jost
    Neuer Beitrag und Inhaltsverzeichnis

    Revision 1.3  2010-08-05 07:52:52  jost
    neuer Anker

    Revision 1.2  2010-08-04 11:36:38  jost
    redakt

    Revision 1.1  2010-08-04 10:41:39  jost
    neu: FAQ

    -->
       
<? include ("footer.inc"); ?>

