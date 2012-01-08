<? include ("frame.inc"); ?>
    <h1>Spendenbescheinigung</h1>
    <p>Mit JVerein können Spendenbescheinigungen ausgestellt und gespeichert werden. Vorbereitend ist sind 
    	ein oder mehrere <a href="administration_formulare.php">Formulare</a> einzurichten.</p>
    <p>Übersicht über die Spendenbescheinigungen:</p>
    <img src='images/Spendenbescheinigungen.jpg' class='screenshot'>
 	<p>Durch einen Klick auf neu öffnet sich das Spendenbescheinigungs-Bearbeitungsfenster (siehe unten).
		Mit einem Doppelklick auf eine Spendenbescheinigung öffnet sich das Bearbeitungsfenster (siehe unten). 
		Mit einem Rechtsklick öffnet sich ein Kontextmenü. Damit können Spendenbescheinigungen gelöscht 
		werden. Durch einen Klick wird die Spendenbescheinigung im PDF-Format ausgegeben.</p>
	<p>Tipp! In der Mitgliedersuche kann man mit einem Klick auf die rechte Maustaste ein Kontextmenü öffnen.
	   	Darin den Menüpunkt 'Spendenbescheinigung' auswählen. Dann wird das Spendenbescheinigungsformular
	   	mit den Daten des Mitglieds gefüllt.</p>
    <img src='images/Spendenbescheinigung.jpg' class='screenshot'>
   <p>Ab Version 1.5</p>
   <p>Ab Version 1.5 gibt es folgende zusätzliche Features:</p>
   <ul>
			<li>Spendenbescheinigung für Sachspenden</li>
			<li>Automatische Generierung von Spendenbescheinigungen über das Mitgliedskonto</li>
	 </ul>
	 <p>Voraussetzungen für die automatische Generierung</p>
	 <ul>
	  <li>Administration|Allgemein Daten zum Verein und zu den Spendenbescheinigungen werden gespeichert.</li>
	 	<li>Administration|Ansicht Mitgliedskonto angehakt</li>
	 	<li>Administration|Buchungsarten mindestens eine Buchungsart hat ein Häkchen im Feld "Spende"</li>
	 	<li>Buchung wurde dem Mitgliedskonto und einer Buchungsart mit dem Merkmal Spende zugeordnet.</li> 
    <img src='images/Spendenbescheinigung.png' class='screenshot'>
	<p>Beispiel für eine Spendenbescheinigung:</p>
    <img src='images/Formularausgefuellt.jpg' class='screenshot'>
    
    <h2><a name="v21"></a>Spendenbescheinigungen ab Version 2.1</h2>
    <p>Spendenbescheinigungen können in Form von Einzelbestätigungen oder 
    Sammelbestätigungen erzeugt werden. Es sind zwei Formular-Arten 
    (Spendenbescheinigung und Sammelbestätigung) hierfür vorgesehen.</p>

    <h3>Einzelbestätigungen</h3>
    <p>Einzelbestätigungen können auf drei Arten erzeugt werden:</p>
    <ul>
        <li>manuell durch Eingabe aller Daten</li>
        <li>aus einem Mitglied / Mitgliedskonto (rechts Klick auf Buchung) 
            heraus für eine einzelne Buchung. In diesem Fall werden die 
            Mitgliedsdaten komplett in die Spendenbescheinigung übernommen, 
            die Buchung bestimmt den Betrag und das Spendendatum.</li>
        <li>Automatische Generierung (siehe unten)</li>
    </ul>

    <h3>Sammelbestätigungen</h3>
    <p>Sammelbestätigungen können auf zwei Arten erzeugt werden:</p>
    <ul>
        <li>aus einem Mitglied / Mitgliedskonto (rechts Klick auf Mitgliedsname) 
            heraus für alle Buchungen im Mitgliedskonto. Es werden nur die Buchungen 
            erfasst, die noch auf keiner Spendenbescheinigung oder Sammelbestätigung 
            ausgewiesen wurden. In diesem Fall werden die Mitgliedsdaten komplett in 
            die Spendenbescheinigung übernommen, die erste Buchung bestimmt das 
            Spendendatum, der Betrag ist die Summe der Beträge aller Buchungen.</li>
        <li>Automatische Generierung (siehe unten)</li>
    </ul>
    <h3>Automatische Generierung von Spendenbescheinigungen</h3>
    <p>In der Übersicht über Spendenbescheinigungen können über den Button 
       "neu (automatisch)" Spendenbescheinigungen generiert werden. Hier kommen die 
       Einstellungen zum Tragen. Es werden nur für die Mitglieder oder Spender 
       Spendenbescheinigungen erzeugt, die eine vollständige Adresse (Straße und 
       PLZ und Ort) eingetragen haben. Außerdem werden nur die Mitglieder oder 
       Spender erfasst, deren Spendenbetrag >= dem Mindestbetrag in den Einstellungen 
       ist.</p>
    <p>In der Übersicht werden zunächst alle Namen und Buchungen angezeigt, die 
       schließlich als Spendenbescheinigung angelegt werden. Der Typ der 
       Spendenbescheinigungen (Einzel / Sammel) macht sich an der Anzahl Buchungen 
       fest, die erfasst wurden.</p>
    <p>Über den Button "erstellen" werden Spendenbescheinigungen erzeugt.</p>

    <h3>Spendenbescheinigungen (Liste)</h3>
    <p>In der Liste können ein oder mehrere Einträge markiert werden. Über ein 
       Kontextmenu (rechter Mausklick) stehen verschiedene Aktionen zur Verfügung:</p>
    <ul>
       <li>Drucken (standard)</li>
       <li>Drucken (individuell)</li>
       <li>Duplizieren (nur wenn genau ein Eintrag gewählt ist)</li>
       <li>Löschen</li>
    </ul>
    <p>Sind mehrere Einträge markiert, wird die Aktion auf alle markierten Einträge 
       angewendet. Das Drucken beschränkt sich darauf, die Dokumente in dem in den 
       Einstellungen angegebenen Verzeichnis zu speichern.</p>
    <p>Über die Buttons in der Buttonleiste stehen die Erzeugung einer neuen bzw. die 
       automatische Generierung (siehe oben) von Spendenbescheinigungen zur Verfügung.</p>
    <p>Alle Einstellungen (siehe unten) werden sowohl bei dem Standard-Dokument als 
       auch dem individuellen Dokumenten berücksichtigt.</p>

    <h3>Spendenbescheinigung (Einzeldarstellung)</h3>
    <p>In der Einzeldarstellung wird der Ausdruck über Buttons ebenfalls angeboten. 
       Im Unterschied zum Druck aus der Liste heraus wird zunächst der Datei-Dialog 
       mit der Voreinstellung des Spendenbescheinigungsverzeichnisses aus den 
       Einstellungen und dem erzeugten Namen angeboten. Hier kann das Verzeichnis 
       und der Name noch einmal korrigiert werden.</p>
    <p>Der Ausdruck über die Buttons funktioniert nur, wenn die Spendenbescheinigung 
       bereits einmal gespeichert wurde. Die Aktionen "neu" und "drucken" direkt 
       hintereinander werden mit einer Fehlermeldung abgewiesen.</p>


  <h2>Weitere Anpassungen</h2>
  <h3>Formulare</h3>
  <p>Vorlagen von Formularen können nun auch mehrere Seiten umfassen. Formularfelder 
     können auch auf anderen Seiten als der ersten platziert werden (siehe auch 
     Formularfelder)</p>

  <h3>Formularfelder</h3>
  <p>Formularfelder können nun auch auf anderen Seiten als nur der ersten Seite 
     platziert werden. Hierzu gibt es die Spalte "Seite", mit der die Seitennummer 
     angegeben wird.</p>
  <p>Für Spendenbescheinigungen stehen nun ergänzend folgende zusätzlichen Felder zur 
     Verfügung:</p>
  <ul>
     <li>Spendenzeitraum Datum der ersten und letzten Buchung auf der 
         Sammelbestätigung</li>
     <li>Buchungsliste<br>
         Alle Buchungen als Liste formatiert:
         <pre>
      Datum Betrag  Verzicht  Zuwendungsart
      </pre>
      Für eine korrekte Formatierung sollte eine Schriftart mit fester Zeichenbereite 
      gewählt werden!</li>
  </ul>
  <h3>Einstellungen</h3>
  <p>Die Einstellungen wurden um ein paar Felder erweitert und einige Felder wurden 
     verlängert:</p>
  <ul>
     <li>Lang-Name<br>
         Langer Name des Vereins, bis 100 Zeichen</li>
     <li>BegünstigterZweck<br>
         Erweitert auf 100 Zeichen<li>
     <li>Straße<br>
         Erweitert auf 50 Zeichen</li>
     <li>Ort<br>
         Erweitert auf 50 Zeichen</li>
     <li>Dateinamenmuster Spende<br>
         Für die Generierung von Dokumenten mit Mitglieds-Bezug. Zunächst nur für 
         Spendenbescheinigungen genutzt, könte aber auch für Kontoauszug des 
         Mitgliedskontos oder den Personalbogen genutzt werden.</li>
     <li>Mindestbetrag für Spendenbescheinigungen<br>
         Allgemeine Einstellung ab welchem Betrag eine Spendenbescheinigung erstellt 
         werden soll. Diese Einstellung kommt bei der automatischen Generierung von 
         Spendebescheinigungen zum Tragen. Bei der Erzeugung einer 
         Spendenbescheinigung aus einem Mitglkiedskonto heraus, wird diese 
         Einstellung nicht beachtet.</li>
     <li>Verzeichnis für Spendenbescheinigungen<br>
         Um ein flüssiges Erzeugen von mehreren Dokumenten zu ermöglichen, kann hier 
         das Verzeichnis für die PDF-Dateien festgelegt werden. Wenn aus der Liste der 
         Spendenbescheinigungen heraus die Dokumente generiert werden, werden sie in 
         diese Verzeichnis geschrieben. Das Verzeichnis wird auch vorbelegt, wenn 
         eine Dokumentenerstellung aus der Detailansicht Spendenbescheinigung erfolgt.
         Hier wird jedoch der Dateidialog angeboten.</li>
     <li>Drucke Buchungsart<br>
         Ist das Häkchen gesetzt, wird in der Buchungsliste nicht der Zweck aus der 
         Buchung, sondern die der Buchung zugewiesene Buchungsart verwendet. Bei 
         sprechenden Namen eine einheitlichere Darstellung.</li>
     </ul>
    

<? include ("footer.inc"); ?>

