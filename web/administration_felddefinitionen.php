<? include ("frame.inc"); ?>
    <h1>Administration: Felddefinitionen</h1>
    <p>
    <img src='images/Felddefinitionen.jpg' class='screenshot'>
	<p>Der Benutzer kann zusätzliche Datenfelder definieren. Durch einen Klick auf neu öffnet sich folgendes 
	Fenster:
    <img src='images/Felddefinition.jpg' class='screenshot'>
    <p>Der Names des Feldes kann auch den Zeichen a-z und 0-9 und _ (Unterstrich) bestehen. Er darf keine 
    Leerzeichen enthalten und sich nicht mit existierenden Feldnamen überschneiden. Als Label kann ein 
    beliebiger Begriff verwendet werden, der bei der Eingabe der Daten den Feld vorangestellt wird. 
    </p>
    <p>Ab Version 1.3 kann aus den Datentypen
    <ul>
    	<li>Zeichenkette (bis zu 1.000 Zeichen lang)</li>
    	<li>Datum (Format TT.MM.JJJJ)</li>
    	<li>Ganzzahl</li>
    	<li>Ja/Nein-Wert</li>
    	<li>Währung</li>
    </ul>
    ausgewählt werden.</p>
    <p>Feldnamen und Label können jederzeit geändert werden. Daten gehen hierdurch nicht verloren. Bis zur 
    Version 1.2 (einschl.) kann ein Feld kann nur gelöscht werden, wenn bei keinem Mitglied Daten in diesem 
    Feld gespeichert sind. Ab Version 1.3 werden die Daten nach Rückfrage gelöscht.</li>
	<p>Bei der Änderung des Datentypen ist zu beachten, dass eine Konvertierung möglich sein muss. Beispielsweise
	kann ein Zusatzfeld vom Typ Zeichenfolge nur dann in den Typ Datum umgewandelt werden, wenn ausschließlich Daten
	in der Form TT.MM.JJJJ gespeichert sind. Alle Datentypen können in Zeichenfolge umgewandelt werden.</li>     

    <!-- 
    $Log$
    Revision 1.1  2009/05/08 14:46:22  jost
    shtml - php

    Revision 1.2  2009/05/03 15:33:30  jost
    Neue Homepage

    -->


<? include ("footer.inc"); ?>

