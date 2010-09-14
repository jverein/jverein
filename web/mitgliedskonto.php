<? include ("frame.inc"); ?>
    <h1>Mitgliedskonto</h1>
	<p>ab Version 1.4 verfügbar</p>
	
	<h2>Aktivierung</h2>
	<p>Zunächst ist das Mitgliedskonto zu aktivieren. Das geschieht unter Administration&gt;Einstellungen&gt;Anzeige
    <img src='images/MitgliedskontoEinstellungen.png' class='screenshot'>
    <p>Anschließend ist JVerein neu zu starten.</p> 
    <p>Die manuelle Überwachnung des Zahlungseingangs und die alte Version der Rechnungen sind mit der Version
    1.4 obsolet. Wer alte Daten noch nutzen will, kann dies über die Einstellungen reaktivieren. Ab Version 1.5 
    werden diese Daten nicht mehr zur Verfügung stehen.</p> 
    
    
    <h2>Abrechnung</h2>
    <p>Die Abrechnung schreibt Informationen zu Mitgliedsbeiträgen und Zusatzabrechnungen in die Tabelle
    Mitgliedskonto. Zusätzlich werden für den Zahlungsweg Abbuchung Informationen in die Buchungstabelle
    der Buchführung geschrieben.</p>
    <p>Fehlerhafte oder Test-Abrechnungen können rückgängig gemacht werden. In dem Abrechnungsformular 
    kann durch klick auf Rückgängig</p>
    <img src='images/MitgliedskontoRueckgaengig.png' class='screenshot'>
    <p>eine Übersicht der Abrechnungsläufe geöffnet werden:</p>
    <img src='images/MitgliedskontoAbrechnungslaeufe.png' class='screenshot'>
    <p>Durch einen Rechtsklick auf einen Abrechnungslauf öffnet sich ein 
    Kontextmenü mit der Option löschen:</p>
    <img src='images/MitgliedskontoAbrechnungslaeufeLoeschen.png' class='screenshot'>
    <p>Nach einer Bestätigung</p>
    <img src='images/MitgliedskontoAbrechnungslaufLoeschenBestaetigen.png' class='screenshot'>
  	<p>werden die verknüpften Datensätze aus der Mitgliedskontotabelle und der Buchungstabelle gelöscht.</p>  
    
    <h2><a name="uebersicht"></a>Mitgliedskontenübersicht</h2>
    <p>Es gibt eine zentrale Übersicht über alle Buchungen der Mitgliedskonten-Tabelle. Die Buchungen
    können über einen Zeitraum oder über einen Namen, bzw. Namensfragment gefiltert werden. Zusätzlich
    kann angegeben werden, ob nur Mitgliedskonten mit Differenzen zwischen Soll und Ist (Offene Posten
    oder Überzahlungen) angezeigt werden.</p>
    <img src='images/MitgliedskontenUebersicht.png' class='screenshot'>
    <p>Durch einen Doppelklick auf die Buchung erscheint das Mitglied.</p>
    <img src='images/MitgliedskontoMitglied.png' class='screenshot'>
    
    <h2>Mitgliedskonto beim Mitglied</h2>
    <img src='images/MitgliedskontoMitglied.png' class='screenshot'>
    <p>In der Baumansicht werden die Summen pro Mitglied, die einzelnen Mitgliedskonten-Sollbuchungen (Soll und
    zugeordnetes Ist, Rechnersymbol), sowie die einzelnen zugeordneten Istbuchungen (Geldscheine-Symbol) angezeigt.</p>
    <p>Mit einem rechten Mausklick auf das Mitglied öffnet sich ein Kontextmenü. Damit können neue
    Sollbuchungen aufgenommen werden.</p>
    <img src='images/MitgliedskontoNeu.png' class='screenshot'>
    <p>Mit einem rechten Mausklick auf eine Mitgliedskonto-Soll-Buchung öffnet sich ein Kontextmenü. Damit
    kann die Sollbuchung bearbeitet, oder, sofern keine Istbuchung zugeordnet ist, auch gelöscht werden.</p>
    
    <h2><a name="auswahl"></a>Buchungen dem Mitgliedskonto zuordnen</h2>
    <p>Unter Buchführung&gt;Buchungen ist eine Buchung auszuwählen und doppelt anzuklicken:</p>
    <img src='images/MitgliedskontoBuchungen.png' class='screenshot'>
    <p>Durch einen Klick auf ... neben Mitgliedskonto erscheint folgender Dialog:</p>
    <img src='images/MitgliedskontoAuswahl.png' class='screenshot'>
    <p>Der Name aus der Buchung wird in das Namensfeld übernommen. Der Inhalt wird in Wörter zerlegt und
    in den Spalten Name, Vorname und Verwendungszweck 1 gesucht. Zur Filterung des Buchungen steht weiterhin
    Differenz = egal, Fehlbetrag oder Überzahlung zur Verfügung. Durch einen Klick auf entfernen wird die 
    Mitgliedskontoinformation aus der Buchung entfernt. Damit können Fehleingaben korrigiert werden.
    </p>

    <h2><a name="rechnung">Rechnungen</h2>
    <p>wird noch ergänzt</p>
    
    <h2><a name="mahnung">Mahnungen</h2>
    <p>wird noch ergänzt</p>
    
 	<h2>sonstiges</h2>
 	<p>Die Einführung des Mitgliedskontos hat Auswirkung auf die Rechnungserstellung und die "Manuelle Überwachung 
 	des Zahlungseinganges".</p>
 	<p>Für die Erstellung der Rechnungen wurden bislang Daten in eine spezielle 
 	Rechnungstabelle geschrieben. Das ist jetzt nicht mehr erforderlich. Die Rechnungen
 	können aus den Informationen des Mitgliedskontos erstellt werden. Für eine Übergangszeit 
 	werden Rechnungen aus beiden Quellen erstellt werden können.</p>
 	<p>Die "Manuelle Überwachung des Zahlungseinganges" wurde bislang auch über eine eigene
 	Tabelle gelöst. Die ist jetzt durch das Mitgliedskonto nicht mehr erforderlich. Der Menüpunkt wird
 	defaultmäßig deaktviert. Unter Einstellungen kann der Menüpunkt reaktiviert werden.</p>
 	<p>In der Version 1.5 wird es die alte Rechnungsmimik und die manuelle Überwachung des Zahlungseinganges
 	nicht mehr geben.</p>   
    
    <!-- 
    $Log$
    Revision 1.3  2010-08-10 05:35:24  jost
    Hinweis auf die Reaktivierung alter Features

    Revision 1.2  2010-07-26 14:47:27  jost
    *** empty log message ***

    Revision 1.1  2010-07-25 18:49:30  jost
    Neu: Mitgliedskonto

    -->
    
<? include ("footer.inc"); ?>

