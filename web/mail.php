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
   <p>Durch einen Klick auf Eigenschaften öffnet sich ein Eigenschaftenauswahldialog. Alle Mitglieder
   mit den ausgewählten Eigenschaften werden hinzugefügt. Mit "alle" werden alle Mitglieder ausgewählt. Mit
   "keinen" wird die komplette Auswahl rückgängig gemacht.</p>
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
	 <tr><td>$empf.id</td><td>Interne Mitgliedsnummer</td></tr>
	 <tr><td>$empf.externemitgliedsnummer</td><td>externe Mitgliedsnummer</td></tr>
	 <tr><td>$empf.adresstyp</td><td>interne Nummer des Adresstyps</td></tr>
	 <tr><td>$empf.personentyp</td><td>Personentyp: n=natürliche Person, j=juristische Person</td></tr>
   <tr><td>$empf.anrede</td><td>Anrede</td></tr>
   <tr><td>$empf.titel</td><td>Titel</td></tr>
   <tr><td>$empf.name</td><td>Name</td></tr>
   <tr><td>$empf.vorname</td><td>Vorname</td></tr>
   <tr><td>$empf.adressierungszusatz</td><td>Adressierungszusatz</td></tr>
   <tr><td>$empf.strasse</td><td>Straße</td></tr>
   <tr><td>$empf.plz</td><td>Postleitzahl</td></tr>
   <tr><td>$empf.ort</td><td>Ort</td></tr>
   <tr><td>$empf.staat</td><td>Staat</td></tr>
   <tr><td>$empf.blz</td><td>Bankleitzahl</td></tr>
   <tr><td>$empf.konto</td><td>Kontonummer</td></tr>
   <tr><td>$empf.iban</td><td>IBAN</td></tr>
   <tr><td>$empf.kontoinhaber</td><td>Kontoinhaber</td></tr>
   <tr><td>$empf.geschlecht</td><td>Geschlecht m oder w</td></tr>
   <tr><td>$empf.telefonprivat</td><td>Telefon privat</td></tr>
   <tr><td>$empf.telefondienstlich</td><td>Telefon dienstlich</td></tr>
   <tr><td>$empf.handy</td><td>Handy</td></tr>
   <tr><td>$empf.email</td><td>Email-Adresse</td></tr>
   <tr><td>$empf.zahlungsweg</td><td>Schlüssel des Zahlungsweges</td></tr>
   <tr><td>$empf.zahlungsrhytmus</td><td>Schlüssel des Zahlungsrhytmus</td></tr>
   <tr><td>$empf.geburtsdatum</td><td>Geburtsdatum. Formatierung: $!{dateformat.format(${empf.geburtsdatum})} </td></tr>
   <tr><td>$empf.eintritt</td><td>Eintrittsdatum. Formatierung: $!{dateformat.format(${empf.eintrittsdatum})} </td></tr>
   <tr><td>$empf.austritt</td><td>Austrittsdatum. Formatierung: $!{dateformat.format(${empf.austrittsdatum})} </td></tr>
   <tr><td>$empf.kuendigung</td><td>Kündigungsdatum. Formatierung: $!{dateformat.format(${empf.kuendigung})} </td></tr>
   <tr><td>$empf.sterbetag</td><td>Sterbetag. Formatierung: $!{dateformat.format(${empf.sterbetag})} </td></tr>
   <tr><td>$empf.beitragsgruppe.bezeichnung</td><td>Bezeichnung der Beitragsgruppe. </td></tr>
   <tr><td>$empf.beitragsgruppe.betrag</td><td>Bezeichnung der Beitragsgruppe. Formatierung: $!{decimalformat.format(${empf.beitragsgruppe.betrag})}</td></tr>
   <tr><td>$empf.beitragsgruppe.beitragsart</td><td>Beitragsart</td></tr>
   <tr><td>$empf.beitragsgruppe.arbeitseinsatzstunden</td><td>Anzahl Stunden Arbeitseinsatz</td></tr>
   <tr><td>$empf.beitragsgruppe.arbeitseinsatzbetrag</td><td>Betrag pro Stunde fehlenden Arbeitseinsatzes. Formatierung: $!{decimalformat.format($empf.beitragsgruppe.arbeitseinsatzbetrag})} </td></tr>
   <tr><td>$empf.zahlerid</td><td>Interne Mitgliedsnummer des zahlenden Mitglieds </td></tr>
   <tr><td>$empf.vermerk1</td><td>1. Vermerk </td></tr>
   <tr><td>$empf.vermerk2</td><td>2. Vermerk </td></tr>
   <tr><td>$empf.eingabedatum</td><td>Datum der Ersteingabe des Datensatzes. Formatierung: $!{dateformat.format(${empf.eingabedatum})} </td></tr>
   <tr><td>$empf.letzteaenderung</td><td>Datum der letzten Änderung des Datensatzes. Formatierung: $!{dateformat.format(${empf.letzteaenderung})} </td></tr>
   <tr><td>$empf.namevorname</td><td>Name, Vorname </td></tr>
   <tr><td>$empf.vornamename</td><td>Vorname Name</td></tr>
   <tr><td>$empf.anschrift</td><td>Formatierte Anschrift</td></tr>
 </table>
    <!-- 
    $Log$
    Revision 1.6  2011-04-03 10:02:26  jost
    Ausgabe der Zusatzfelder

    Revision 1.5  2011-03-20 19:24:17  jost
    Zusätzliche Felder

    Revision 1.4  2010-03-27 20:11:16  jost
    EigenschaftenAuswahl überarbeitet.

    Revision 1.3  2010/02/16 18:07:29  jost
    *** empty log message ***

    Revision 1.2  2010/02/04 18:38:56  jost
    Zusätzliche Datenfelder

    Revision 1.1  2010/02/01 21:03:42  jost
    Neu: Einfache Mailfunktion

    -->
        
<? include ("footer.inc"); ?>

