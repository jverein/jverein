<? include ("frame.inc"); ?>
	   
	<div style="float:left; width: 600px;border:0px">
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
        <li>Wiedervorlagetermine</li>
        <li>Vermerke</li>
        <li>Zusatzzahlungen (einmalig und wiederkehrend)</li>
        <li>Individuelle Zusatzfelder (ab Version 1.1)</li>
        <li>Lehrgänge (ab Version 1.2)</li>
     </ul>
     <p>Ab Version 1.2 können auch juristische Personen (Firmen, Organisationen, Behörden) als Mitglieder
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
	<p>Die Vereinsbuchführung (Einnamen/Ausgaben) kann ab Version 1.1 über eine integrierte einfache 
	   Buchführung erledigt werden.</p>
	<p>Die Mitgliederdaten können nach vielen Kriterien <a href="dokumentationauswertungmitglieder.php">
	   ausgewertet</a> werden. Die Ausgabe erfolgt im PDF-Format oder im CSV-Format. Die Erstellung 
	   einer Liste mit <a href="dokumentationauswertungjubilaeen.php">Mitgliedschafts- oder Altersjubliäen</a> ist möglich.</p>
	<p>Ab Version 1.1 ist die Erstellung von Rechnungen und Spendenbescheinigungen möglich.</p>
	<p>JVerein ist mit vielen Screenshots ausgiebig <a href='dokumentation.php'>dokumentiert</a>.</p>
	<p>JVerein steht unter GPL. In diesem Rahmen kann JVerein genutzt werden.</p>
	<p>Zusätzlich gibt es eine Bitte des Autors: Wenn JVerein "produktiv" genutzt wird, bitte ich um eine 
	   kurze Nachricht an <a href="mailto:heiner@jverein.de">heiner@jverein.de</a> mit dem Namen des Vereins 
	   und der Anzahl der Mitglieder. Diese Daten dienen der "Erfolgskontrolle".</p>
	   
	</div>   
	<div style="float:left; width:200px;  left: 850px; ">    
	   	<h1>News</h1>
    	<ul>
        	<li>09.08.2009: Veröffentlichung des <a href="http://prdownload.berlios.de/jverein/jverein.1.2.0-devel-190.zip">Release-Kandidaten 1 der Version 1.2.0</a>. Siehe <a href="changelog.php">Changelog</a></li>
       	 	<li>16.07.2009: Veröffentlichung der <a href="http://prdownload.berlios.de/jverein/jverein.1.2.0-devel-183.zip">Entwickler-Version 1.2.0 Rev. 183</a>. Siehe <a href="changelog.php">Changelog</a></li>
    		<li>14.03.2009: Veröffentlichung der <a href="http://prdownload.berlios.de/jverein/jverein.1.2.0-devel-173.zip">Entwickler-Version 1.2.0 Rev. 173</a>. Siehe <a href="changelog.php">Changelog</a></li> 
    	</ul>
	</div>
	   
	<!-- 
    $Log$
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
  

