<? include ("frame.inc"); ?>
    <h1>Dokumentation: Beitragsmodelle</h1>
	<h2>Allgemein</h2>
	<p>Zur Beitragsberechnung sind eine oder mehrere <a href="administration_beitragsgruppen.php">
		Beitragsgruppen</a> einzurichten. Z. B. Jugendliche, Erwachsene, Beitragsfreie. Jeder dieser 
		Beitragsgruppen wird ein Text und eine Beitragshöhe zugeordnet. Dabei kann es sich um Monats-, 
		Vierteljahres-, Halbjahres- oder Jahresbeträge handeln. Siehe unten.</p>
	<h2>Familientarif</h2>
	<p>Alle Mitglieder, für die der Familientarif gilt sind einzeln einzugeben. Zur Abbildung eines 
		Familientarifes sind 2 <a href="administration_beitragsgruppen.php">Beitragsgruppen</a> 
		einzurichten. Einmal eine Gruppe für den Zahler und eine Gruppe für die beitragsfreien 
		Familienmitglieder. Als Zahler ist immer eines der famlienangehörigen Mitglieder auszuwählen. 
		Es wird nicht ein evtl. zahlendes Elternteil, dass nicht Mitglied ist eingetragen. Die 
		Famlienmitglieder sind miteinander verknüpft. Tritt das zahlende Mitglied aus, erscheint ein 
		entsprechender Hinweis. Dann muss entweder ein bislang beitragsfreies Mitglied zum Zahler 
		erklärt werden oder der Familienverband wird aufgelöst und es werden Einzelbeiträge erhoben.</p>
	<h2>Beitragsmodelle</h2>
	<p>JVerein unterstützt verschiedene Betragsmodelle:</p>
	<ul>
		<li>Feste Zahlungstermine. Alle Mitglieder zahlen zu festen Terminen ihren Beitrag. Dieser 
			Termin kann jährlich, halbjährlich, vierteljährlich oder monatlich sein. Zu diesem Termin 
			wird für jedes Mitglied der Beitrag gemäß <a href="administration_beitragsgruppen.php">
			Beitragsgruppe</a> eingezogen.</li>
		<li>Unterschiedliche Zahlungstermine. Die Mitglieder zahlen einen Monatsbeitrag gemäß 
			<a href="administration_beitragsgruppen.php">Beitragsgruppe</a>. Dieser Beitrag wird 
			entweder jährlich (12 Monatsbeiträge), halbjährlich (6 Monatsbeiträge), vierteljährlich 
			(3 Monatsbeiträge) oder monatlich erhoben. Bei der Abrechnung muss dann ausgewählt werden,
			welche Beiträge abgerechnet werden sollen.</li>
	</ul>
	
    <!-- 
    $Log$
    Revision 1.2  2011-08-24 16:49:31  jost
    *** empty log message ***

    Revision 1.1  2009-05-08 14:46:22  jost
    shtml - php

    Revision 1.1  2009/05/03 15:33:30  jost
    Neue Homepage

    -->

<? include ("footer.inc"); ?>

