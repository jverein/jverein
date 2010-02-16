<? include ("frame.inc"); ?>
    <h1>Dokumentation: Mail</h1>
    <p>Ab Version 1.3</p>
    <p>In JVerein ist ein einfaches Mail-Programm integriert. Hiermit ist es möglich,
       einfache Textmails mit Dateianhängen an einzelne Mitglieder, Gruppen von Mitglieder oder alle 
       Mitglieder zu verschicken. Der Empfang von Mails ist nicht vorgesehen.</p>
       
    <p><b>Konfiguration</b></p>
    <p>Die Konfiguration ist unter <a href='administration_einstellungen.php#mail'>Einstellungen</a> 
    beschrieben.</p>
    
    <p><b>Mail-Vorlagen</b></p>
    <p>Für den Fall, dass immer ähnliche Mails versandt werden, kann die Tipparbeit durch
       Mailvorlagen minimiert werden.</p>
    <img src='images/Mailvorlagen.jpg' class='screenshot'>
	<p>Durch einen Doppelklick auf den Betreff oder durch einen Klick auf neu öffnet
	   sich das Bearbeitungsfenster.</p>
    <img src='images/Mailvorlage.jpg' class='screenshot'>
   
   <p><b>Mails</b></p>
   <p>In der Liste aller Mails wird der Betreff, das Bearbeitungs- und das Versanddatum angezeigt.</p>
   <img src='images/Mails.jpg' class='screenshot'>
   <p>Durch einen Klick auf neu öffnet sich ein Mail-Vorlagen-Auswahlfenster:</p>
   <img src='images/MailvorlagenAuswahl.jpg' class='screenshot'>
   <p>Entweder wird eine Mailvorlage ausgewählt oder es geht ohne Vorlage weiter.</p>
   <p>Ein Doppelklick auf eine Mail öffnet das Bearbeitungsfenster.</p>
   <img src='images/Mail.jpg' class='screenshot'>
   <p>Durch einen Klick auf hinzufügen öffnet sich folgendes Auswahlfenster:</p>
   <img src='images/Mailempfaengerauswahl.jpg' class='screenshot'>
   <p>Der Mailversand kann auch über einen Rechtsklick auf ein Mitglied ausgelöst werden:</p>
   <img src='images/MitgliedMailversand.jpg' class='screenshot'>
   <p><b>Variable</b></p>
   <p>Im Betreff und im Text können Variable eingefügt werden, die beim Mailversand mit den
   konkreten Daten gefüllt werden.</p>
   <table border="1">
   <tr><th>Name</th><th>Inhalt</th></tr>
   <tr><td>$email</td><td>Emailadresse des Empfängers</td></tr>
   <tr><td>$tagesdatum</td><td>Aktuelles Datum in der Form tt.mm.jjjj</td></tr>
   <tr><td>$vormonat</td><td>Vormonat im Format mm.jjjj</td></tr>
   <tr><td>$aktuellermonat</td><td>Aktueller Monat im Format mm.jjjj</td></tr>
   <tr><td>$folgemonat</td><td>Folgemonat im Format mm.jjjj</td></tr>
   <tr><td>$empf.anrede</td><td>Anrede</td></tr>
   <tr><td>$empf.titel</td><td>Titel</td></tr>
   <tr><td>$empf.name</td><td>Name</td></tr>
   <tr><td>$empf.vorname</td><td>Vorname</td></tr>
   <tr><td>$empf.adressierungszusatz</td><td>Adressierungszusatz</td></tr>
   <tr><td>$empf.strasse</td><td>Straße</td></tr>
   <tr><td>$empf.plz</td><td>Postleitzahl</td></tr>
   <tr><td>$empf.ort</td><td>Ort</td></tr>
   <tr><td>$empf.blz</td><td>Bankleitzahl</td></tr>
   <tr><td>$empf.konto</td><td>Kontonummer</td></tr>
   <tr><td>$empf.iban</td><td>IBAN</td></tr>
   <tr><td>$empf.kontoinhaber</td><td>Kontoinhaber</td></tr>
   <tr><td>$empf.geschlecht</td><td>Geschlecht m oder w</td></tr>
   <tr><td>$empf.telefonprivat</td><td>Telefon privat</td></tr>
   <tr><td>$empf.telefondienstlich</td><td>Telefon dienstlich</td></tr>
   <tr><td>$empf.handy</td><td>Handy</td></tr>
   </table>
    <!-- 
    $Log$
    Revision 1.2  2010/02/04 18:38:56  jost
    Zusätzliche Datenfelder

    Revision 1.1  2010/02/01 21:03:42  jost
    Neu: Einfache Mailfunktion

    -->
        
<? include ("footer.inc"); ?>

