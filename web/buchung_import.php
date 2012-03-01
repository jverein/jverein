<? include ("frame.inc"); ?>
  <h1>Buchungs-Import</h1>
  <p>Buchungen können im CSV-Format importiert werden.
	Der Dateiname muss eine Endung haben. Z. B. .csv oder .txt. Es kann jede beliebige Endung verwendet werden. 
	Die Datenfelder werden durch Semikolon getrennt. Das Encoding kann ausgewählt werden.</p>
	
	<table border="1">
	    <tr><th>Feldbezeichnung</th><th>Pflicht/optional</th></tr>
      <tr><td>buchung_auszugsnummer</td><td>optional</td></tr>
      <tr><td>buchung_betrag</td><td>Pflichtfeld</td></tr>
      <tr><td>buchung_blattnummer</td><td>optional</td></tr>
      <tr><td>buchung_buchungsart_nummer</td><td>optional</td></tr>
      <tr><td>buchung_buchungsklasse_nummer</td><td>optional</td></tr>
      <tr><td>buchung_datum</td><td>Pflichtfeld</td></tr>
      <tr><td>buchung_kommentar</td><td>optional</td></tr>
      <tr><td>buchung_kontonummer</td><td>Pflichtfeld</td></tr>
      <tr><td>buchung_name</td><td>Pflichtfeld</td></tr>
      <tr><td>buchung_zweck1</td><td>Pflichtfeld</td></tr>
      <tr><td>buchung_zweck2</td><td>optional</td></tr>
	</table>

<? include ("footer.inc"); ?>

