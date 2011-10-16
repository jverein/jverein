<? include ("frame.inc"); ?>
    <h1>Installation von Testversionen</h1>
    <h2>Wichtig!</h2>
    <p>Bei der Arbeit mit Testversionen kann immer etwas unvorhersehbares passieren. Daher ist
       es wichtig, eine Datensicherung anzufertigen. Der Programm- und der Datenordner 
       (<a href="faq.php#programmordner">siehe auch FAQ</a>) werden auf einen USB-Stick oder ein
       anderes externes Medium kopiert.
    </p>
    <h2>Programminstallation</h2>
    <p>Die Installation von Entwicklerversionen ist <a href='installation.php#entwicklerversion'>hier</a>
    beschrieben. Wichtig ist die Installation in ein "frisches" Verzeichnis. Z. B. jvereintest.</p>
    <h2>Datenordner</h2>
    <p>Der <a href="faq.php#datenordner">produktive Datenordner</a> wird zum Test-Datenordner kopiert. 
    Dazu wird mit Mitteln des Betriebssystems (z. B. Nautilus oder Explorer) der Ordner unter einem neuen
    Namen (z. B. jvereintest) kopiert. Nutzer der Datenbank MySQL kopieren sich die Datenbank. Dazu 
    wird mit mysqldump ein Dump erzeugt, mit einem Editor der Datenbankname geändert und die Daten wieder
    in die neue Datenbank geladen. In der Testumgebung wird der Datenbankname angepasst. Siehe auch 
    <a href="dokumentationmysql.php">MySQL-Support</a>.
    <h2>Programmstart</h2>
    <p>Der Programmstart kann je nach Vorliebe entweder über ein einzurichtendes 
    <a href="http://de.wikipedia.org/wiki/Icon_%28Computer%29">Icon</a> oder über die
    <a href="http://de.wikipedia.org/wiki/Kommandozeile">Kommandozeile</a> erfolgen. Gestartet wird
    das Skript für das entsprechende Betriebssystem aus dem neu eingerichteten Programmordner. Zusätzlich
    wird der Schalter -f &gt;datenordner&lt; angegeben.</p>
    <p>
    Beispiel für Linux:
    </p>
    <code>/home/heiner/jvereintest/jameica.sh -f /home/heiner/jverein.test</code>
    <p>
    Wichtig! Der Datenordner darf nicht innerhalb des Programmordners liegen. Wird der Schalter -f vergessen,
    wird das <a href="faq.php#programmordner">Standardordner</a> überschrieben.</p>
    </p>
       
<? include ("footer.inc"); ?>

