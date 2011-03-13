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

    <!-- 
    $Log$
    Revision 1.2  2009/11/05 20:08:35  jost
    Hinweis auf Rechtsklick in der Mitgliedersuche aufgenommen.

    Revision 1.1  2009/05/08 14:46:22  jost
    shtml - php

    Revision 1.2  2009/05/03 15:33:30  jost
    Neue Homepage

    -->

<? include ("footer.inc"); ?>

