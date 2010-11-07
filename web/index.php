<? include ("frame.inc"); ?>
	   
	<div style="float:left; width: 600px;border:0px">
	<p><a href="forum">Verbesserungsvorschläge und Fehlermeldungen bitte ins Forum posten</a>
    <h1>Home</h1>
    <p>
 <!--      <img src="images/JVerein_Box.jpg" alt="JVerein-Software-Box" >  -->
       <i>JVerein</i> ist eine Open-Source-Vereinsverwaltung mit einer Anbindung an die ebenfalls unter 
       Open-Source-Lizenz stehende Homebankingsoftware 
       <a href="http://www.willuhn.de/products/hibiscus/">Hibiscus</a>.
     </p>
     <p>Die Implementierung erfolgt mit Java. Der Ablauf auf vielen Plattformen ist damit gewährleistet. 
        Als GUI-Framework kommt <a href="http://www.willuhn.de/products/jameica/">Jameica</a>  zum Einsatz.
     </p>
     <p>Für jedes Mitglied können folgende Daten gespeichert werden:</p>
     <ul>
        <li>Namen</li>
        <li>Adresse</li>
        <li>Zahlungsweg</li>
        <li>Bankverbindung</li>
        <li>Zahlungrhytmus</li>
        <li>Kommunikationsdaten (Tel./eMail)</li>
        <li>Daten zur Mitgliedschaft (Eintritt, Beitragsgruppe, Austritt)</li>
        <li>Eigenschaften des Mitglieds</li>
        <li>Mitgliedskonto (ab Version 1.4)
        <li>Foto (ab Version 1.4)</li>
        <li>Wiedervorlagetermine</li>
        <li>Vermerke</li>
        <li>Zusatzzahlungen (einmalig und wiederkehrend)</li>
        <li>Individuelle Zusatzfelder</li>
        <li>Lehrgänge</li>
     </ul>
     <p>Es können neben natürlichen Personen auch juristische Personen (Firmen, Organisationen, Behörden) als Mitglieder
     	gespeichert werden.</p>
	<p>Für den Beitragseinzug können eine oder mehrere Beitragsgruppen gebildet werden. Die Abbildung eines 
	   Familientarifes mit der Bildung von Familienverbänden ist möglich. Es wird entweder ein fester 
	   Beitrag (pro Beitragsgruppe) in einem Intervall (z. B. Jahresbeitrag oder Monatsbeitrag) erhoben oder 
	   ein Monatsbeitrag der monatlich, vierteljährlich, halbjährlich oder jährlich eingezogen wird. 
	</p>
	<p>Der Beitragseinzug erfolgt in der Regel über das Abbuchungsverfahren. Die Abbuchungen werden im 
	   DTAUS-Format mit Hilfe meiner Bibliothek <a href="http://obantoo.berlios.de">OBanToo</a> ausgegeben 
	   und können in <a href="http://www.willuhn.de/products/hibiscus/">Hibiscus</a> importiert werden 
	   oder direkt an <a href="http://www.willuhn.de/products/hibiscus/">Hibiscus</a> als Einzel- oder 
	   Sammellastschrift übergeben werden. Alle Abrechnungsdaten werden festgehalten. Daraus können 
	   anschließend Rechnungen erstellt werden.</p>
	<p>Für Mitglieder, die nicht am Abbuchungsverfahren teilnehmen werden die Zahlungsdaten zur manuellen 
	   Überwachung des Zahlungseinganges festgehalten.</p>
	<p>Zusätzlich können für jedes Mitglied zu beliebigen Zeitpunkten Zusatzabbuchungen eingegeben werden 
	   (Strafgelder, Eigenanteile ...), die auch wiederkehrend sein können.</p>
	<p>Die Vereinsbuchführung (Einnamen/Ausgaben) kann über eine integrierte einfache 
	   Buchführung erledigt werden.</p>
	<p>Die Mitgliederdaten können nach vielen Kriterien <a href="dokumentationauswertungmitglieder.php">
	   ausgewertet</a> werden. Die Ausgabe erfolgt im PDF-Format oder im CSV-Format. Die Erstellung 
	   einer Liste mit <a href="dokumentationauswertungjubilaeen.php">Mitgliedschafts- oder Altersjubliäen</a> ist möglich.</p>
	<p>In Version 1.3 ist der Versand von Mails an Mitglieder realisiert worden. Der Versand ist an einzelne Mitglieder, 
	   Gruppen von Mitgliedern oder alle Mitglieder möglich. Für regelmäßig zu versendende Mails können Vorlagen erstellt 
	   werden. Durch Variable ist die Personalisierung der Mails möglich.</p>
	<p>Die Erstellung von Rechnungen, Mahnungen (ab Version 1.4) und Spendenbescheinigungen möglich.</p>
	<p>JVerein ist mit vielen Screenshots ausgiebig <a href='dokumentation.php'>dokumentiert</a>.</p>
	<p>JVerein steht unter GPL. In diesem Rahmen kann JVerein genutzt werden.</p>
	<p>Zusätzlich gibt es eine Bitte des Autors: Wenn JVerein "produktiv" genutzt wird, bitte ich um einen 
	   Eintrag im <a href="http://www.jverein.de/forum/viewforum.php?f=3">Forum unter Vorstellung der Verein, 
	   die JVerein nutzen</a>. Diese Daten dienen der "Erfolgskontrolle".</p>
	</div>   
	<div style="float:left; width:200px;  left: 850px; ">    
	   	<h1>News</h1>
    	<ul>
    	    <li>07.11.2010: Neuer Downloadbereich</li>
    		<li>05.07.2010: Neues Windows-Setup der Jameica-Suite (32 und 64 bit) im Downloadbereich.</li>
    	    <li>31.05.2010: Version 1.3.3 freigegeben</a>.</li>
    		<li>18.05.2010: Version 1.3.2 freigegeben</a>.</li> 
	   		<li>16.05.2010: Version 1.3.1 freigegeben</a>.</li> 
    		<li>29.04.2010: Das Windows-Setup für die Jameica-Suite steht jetzt im Downloadbereich zur Verfügung.</li> 
    		<li>09.04.2010: <a href='version1.3.0.php'>Version 1.3.0 freigegeben</a>.</li> 
    		<li>28.03.2010: Neuer  <a href='download.php'>Downloadbereich</a></li> 
    	</ul>
	</div>
	   
	<!-- 
    $Log$
    Revision 1.37  2010-11-04 21:47:34  jost
    *** empty log message ***

    Revision 1.36  2010-09-01 05:59:26  jost
    *** empty log message ***

    Revision 1.35  2010-07-05 18:49:22  jost
    *** empty log message ***

    Revision 1.34  2010-07-05 18:45:21  jost
    *** empty log message ***

    Revision 1.33  2010/05/18 20:51:27  jost
    *** empty log message ***

    Revision 1.32  2010/05/16 12:09:46  jost
    *** empty log message ***

    Revision 1.31  2010/04/29 12:45:50  jost
    *** empty log message ***

    Revision 1.30  2010/04/09 13:09:58  jost
    *** empty log message ***

    Revision 1.29  2010/04/09 08:56:56  jost
    Version 1.3.0

    Revision 1.28  2010/04/08 17:57:56  jost
    *** empty log message ***

    Revision 1.27  2010/03/28 18:37:06  jost
    *** empty log message ***

    Revision 1.26  2010/03/27 20:18:47  jost
    *** empty log message ***

    Revision 1.25  2010/03/16 19:22:14  jost
    Hinweis auf das Forum

    Revision 1.24  2010/03/05 21:56:02  jost
    Hinweis auf Mails

    Revision 1.23  2010/03/01 17:14:02  jost
    *** empty log message ***

    Revision 1.22  2010/02/15 20:07:25  jost
    *** empty log message ***

    Revision 1.21  2010/02/01 21:05:50  jost
    *** empty log message ***

    Revision 1.20  2010/01/10 21:00:12  jost
    *** empty log message ***

    Revision 1.19  2010/01/01 22:40:33  jost
    *** empty log message ***

    Revision 1.18  2010/01/01 21:40:37  jost
    *** empty log message ***

    Revision 1.17  2010/01/01 20:13:21  jost
    *** empty log message ***

    Revision 1.16  2009/12/22 18:51:56  jost
    *** empty log message ***

    Revision 1.15  2009/12/02 22:32:06  jost
    Neues Dokument: Zugriff mit OpenOffice-Base auf die H2-Datenbank

    Revision 1.14  2009/12/01 20:20:45  jost
    *** empty log message ***

    Revision 1.13  2009/10/31 14:23:14  jost
    *** empty log message ***

    Revision 1.12  2009/10/17 19:57:40  jost
    *** empty log message ***

    Revision 1.11  2009/09/27 21:31:48  jost
    *** empty log message ***

    Revision 1.10  2009/08/30 06:30:45  jost
    *** empty log message ***

    Revision 1.9  2009/08/24 20:08:17  jost
    *** empty log message ***

    Revision 1.8  2009/08/24 17:19:33  jost
    *** empty log message ***

    Revision 1.7  2009/08/23 18:59:21  jost
    Version 1.2

    Revision 1.6  2009/08/20 20:15:32  jost
    *** empty log message ***

    Revision 1.5  2009/08/17 18:52:58  jost
    neues Layout

    Revision 1.4  2009/08/09 15:36:17  jost
    *** empty log message ***

    Revision 1.3  2009/08/09 15:34:41  jost
    *** empty log message ***

    Revision 1.2  2009/07/17 11:42:16  jost
    *** empty log message ***

    Revision 1.1  2009/05/08 14:46:22  jost
    shtml - php

    Revision 1.22  2009/05/03 15:33:30  jost
    Neue Homepage

    -->
	   
 <? include ("footer.inc"); ?>
  

