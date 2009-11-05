<? include ("frame.inc"); ?>
    <h1>Zusatzbeträge Import</h1>
    <h2>Default-Format</h2>
	<p>Zusatzbeträge können auch importiert werden. Der Import kann über eine CSV-Datei (Default-Format), 
	die nachfolgend beschrieben wird, erfolgen.
	Der Dateiname muss eine Endung haben. Z. B. .csv oder .txt. Es kann jede beliebige Endung verwendet werden. 
	Die Datenfelder werden durch Semikolon getrennt. Das Encoding muss ISO-8859-1 sein.</p>
	<p>Zur Zuordnung des Zusazbetrages zum Mitglied muss entweder die Mitglieds_Nr oder Nachname und Vorname angegeben
	werden. Die Angabe von Nachname und Vorname setzt voraus, dass es keine Doubletten bei den Namen gibt.</p>
	<table border="1">
	<tr>
		<td>Mitglieds_Nr</td>
		<td>Mitgliedsnummer. Muss angegeben sein, wenn Nachname und Vorname nicht angegeben wurden.</td>
	</tr>
	<tr>
		<td>Nachname</td>
		<td>Nachname. Muss in Kombination mit dem Vornamen abgegeben werden, wenn die Mitglieds_Nr nicht angegeben wurde.</td>
	</tr>
	<tr>
		<td>Vorname</td>
		<td>Vorname. Muss in Kombination mit dem Nachnamen abgegeben werden, wenn die Mitglieds_Nr nicht angegeben wurde.</td>
	</tr>
	<tr>
		<td>Betrag</td>
		<td>Betrag. Anstatt eines Komma ist ein Punkt anzugeben.</td>
	</tr>
	<tr>
		<td>Buchungstext</td>
		<td>Buchungstext. Max 27 Stellen. Darf kein Semikolon enthalten.</td>
	</tr>
	<tr>
		<td>Fälligkeit</td>
		<td>Datum der Fälligkeit des Betrages. Format TT.MM.JJJJ</td>
	</tr>
	<tr>
		<td>Intervall</td>
		<td>Intervall der Zahlung. 0=keine Wiederholung, 1 = monatlich, 2 = zweimonatlich, 3 = vierteljährlich, 6 = halbjährlich, 12 = jährlich</td>
	</tr>
	</table>
	<p>Die Mitgliedsnummer kann in der Treffermenge der Mitgliedersuche angezeigt werden. Dafür muss unter 
	Administration>Einstellungen>Tabellen ein Häkchen vor ID gesetzt werden.</p>
	<p>Jede Datei enthält eine Kopfzeile und pro Zusatzbuchung eine Zeile.</p>
	
	<h2>Spezialformate</h2>
	<p>In JVerein wird momentan ein Plugin-Mechanismus für Im- und Exportformate entwickelt. Damit wird es möglich sein, 
	spezielle Formate eingelesen werden. Möglich sind spezielle CSV-Formate, Datenbanken, XML ..... Ich Bedarfsfalle bitte
	ich um eine Mail.</p> 
	<!-- 
    $Log$
    Revision 1.1  2009/10/17 19:57:40  jost
    *** empty log message ***

    -->
	
<? include ("footer.inc"); ?>

