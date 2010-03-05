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
	<p>Die Vereinsbuchführung (Einnamen/Ausgaben) kann über eine integrierte einfache 
	   Buchführung erledigt werden.</p>
	<p>Die Mitgliederdaten können nach vielen Kriterien <a href="dokumentationauswertungmitglieder.php">
	   ausgewertet</a> werden. Die Ausgabe erfolgt im PDF-Format oder im CSV-Format. Die Erstellung 
	   einer Liste mit <a href="dokumentationauswertungjubilaeen.php">Mitgliedschafts- oder Altersjubliäen</a> ist möglich.</p>
	<p>In Version 1.3 ist der Versand von Mails an Mitglieder realisiert worden. Der Versand ist an einzelne Mitglieder, 
	   Gruppen von Mitgliedern oder alle Mitglieder möglich. Für regelmäßig zu versendende Mails können Vorlagen erstellt 
	   werden. Durch Variable ist die Personalisierung der Mails möglich.</p>
	<p>Die Erstellung von Rechnungen und Spendenbescheinigungen möglich.</p>
	<p>JVerein ist mit vielen Screenshots ausgiebig <a href='dokumentation.php'>dokumentiert</a>.</p>
	<p>JVerein steht unter GPL. In diesem Rahmen kann JVerein genutzt werden.</p>
	<p>Zusätzlich gibt es eine Bitte des Autors: Wenn JVerein "produktiv" genutzt wird, bitte ich um einen 
	   Eintrag im <a href="http://www.jverein.de/forum/viewforum.php?f=3">Forum unter Vorstellung der Verein, 
	   die JVerein nutzen</a>. Diese Daten dienen der "Erfolgskontrolle".</p>
	</div>   
	<div style="float:left; width:200px;  left: 850px; ">    
	   	<h1>News</h1>
    	<ul>
    		<li>01.03.2010: Veröffentlichung des <a href='download/index.php?jverein.1.3.0-rc1-231.zip'>Release Candidat 1 der Version 1.3.0 Rev. 231</a>. Änderungen siehe <a href='changelog.php'>Changelog</a></li> 
    		<li>15.02.2010: Veröffentlichung der <a href='download/index.php?jverein.1.3.0-devel-227.zip'>Entwickler-Version 1.3.0 Rev. 227</a>. Änderungen siehe <a href='changelog.php'>Changelog</a></li> 
    		<li>01.02.2010: Veröffentlichung der <a href='download/index.php?jverein.1.3.0-devel-226.zip'>Entwickler-Version 1.3.0 Rev. 226</a>. Änderungen siehe <a href='changelog.php'>Changelog</a></li> 
    		<li>01.01.2010: Veröffentlichung der <a href='download/index.php?jverein.1.3.0-devel-220.zip'>Entwickler-Version 1.3.0 Rev. 220</a>. Änderungen siehe <a href='changelog.php'>Changelog</a></li> 
    		<li>02.12.2009: Neues Dokument: <a href='openofficeh2.php'>Zugriff mit OpenOffice auf die H2-Datenbank</a></li>
    		<li>01.12.2009: Veröffentlichung der <a href='download/index.php?jverein.1.3.0-devel-217.zip'>Entwickler-Version 1.3.0 Rev. 217</a>. Änderungen siehe <a href='changelog.php'>Changelog</a></li> 
    		<li>30.10.2009: <a href="forum" target="new">Forum</a> ist online gegangen.</li> 
    		<li>17.10.2009: Neues <a href="buchfuehrungzusammenhaenge.php">Dokument</a> über die Zusammenhänge der Buchführung von Siegfried Kübel.</li> 
    	</ul>
	</div>
	   
	<!-- 
    $Log$
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
  

