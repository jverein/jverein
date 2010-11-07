<?php

###########################################################################
#  Download Log / Tabelle
#  (c) 2008 by Peter Garstenauer & Reynhard Boegl for kick-image.at
#  Free Download: http://www.kick-image.at > WEBTOOLS
###########################################################################

###########################################################################
#  Einstellungen:
###########################################################################

# Textfile welches die Downloads zählt
$log_file = 'downloads.txt';

# Sprach-Einstellungen
$lang['title'] = 'Downloads';
$lang['no_download'] = 'Es wurden noch keine Downloads gez&auml;hlt!';
$lang['nummer'] = 'Nr.';
$lang['name'] = 'Dateiname';
$lang['anzahl_downloads'] = 'Downloads';
$lang['gesamt'] = 'Gesamtdownloads:'; // leer lassen wenn keine Gesamt-Anzahl gewünscht

###########################################################################
#  Sämtliche, zum Download zur Verfügung gestellten Dateien werden
#  von kick-image.at sorgfältig auf Viren und andere Schädlinge
#  überprüft. kick-image.at übernimmt jedoch keinerlei Haftung für
#  eventuell entstehende Schäden jeglicher Art, alle Downloads sind
#  freiwillig und auf eigene Gefahr!
###########################################################################

###########################################################################
#
#  Ab hier nichts mehr ändern!
#
###########################################################################
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="de">
<head>
<title><?php echo $lang['title']; ?></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<style type="text/css">
<!--
* {margin:0; padding:0;}
body {padding:30px; color:#000; font-family:Arial,Verdana,sans-serif; font-size:86%;}
h1 {font-size:1.2em; border-bottom:1px solid #ccc; margin:0 0 15px 0;}
table {margin:0 0 50px 10px; border-bottom:2px solid #f7f7f7;}
td {padding:5px 10px 5px 10px; border-left:2px solid #fff; border-right:2px solid #fff;}
td.bezeichnung {background-color:#efefef; border-top:3px solid #fff; font-weight:bold;}
td.name {}
td.anzahl {text-align:right; padding-right:15px;}
td.hell {}
td.dunkel {background-color:#f7f7f7;}
p {font-size:0.9em;}
a {color:#000;}
a:hover {text-decoration:none;}
-->
</style>
</head>

<body>

<h1><?php echo $lang['title']; ?></h1>
<table cellspacing='0' cellpadding='0' border='0'>
<?php
echo "<tr>\n";
echo " <td class='nummer bezeichnung'>" . $lang['nummer'] . "</td>\n";
echo " <td class='name bezeichnung'>" . $lang['name'] . "</td>\n";
echo " <td class='anzahl bezeichnung'>" . $lang['anzahl_downloads'] . "</td>\n";
echo "</tr>";
$file = file($log_file);
$anzahl = count($file);
$dlgesamt = "0";
if ($anzahl == 0) {
	echo "<tr>\n";
	echo " <td colspan='3'>" . $lang['no_download'] . "</td>\n";
	echo "</tr>";
} else {
	for ($i = 0; $i < $anzahl; $i++) {
		$zeile = split('\: ', $file[$i]);
		$dlname = trim($zeile[0]);
		$dlanzahl = trim($zeile[1]);
		$dlnummer = $i + 1;

		if ($hgf == "hell") {
			$hgf = "dunkel";
		} else {
			$hgf = "hell";
		}
		echo "<tr>\n";
		echo " <td class='nummer " . $hgf . "'>" . $dlnummer . "</td>\n";
		echo " <td class='name " . $hgf . "'>" . $dlname . "</td>\n";
		echo " <td class='anzahl " . $hgf . "'>" . $dlanzahl . "</td>\n";
		echo "</tr>";
		$dlgesamt = $dlgesamt + $dlanzahl;
	}
}
 if ($lang['gesamt']) {
		echo "<tr>\n";
		echo " <td class='name bezeichnung' colspan='2'><b>" . $lang['gesamt'] . "</b></td>\n";
		echo " <td class='anzahl bezeichnung'><b>" . $dlgesamt . "</b></td>\n";
		echo "</tr>";
 };
echo "\n</table>";
?>

<p>Download Log - &copy; 2008 by Peter Garstenauer &amp; Reynhard Boegl for <a href="http://www.kick-image.at">www.kick-image.at</a></p>
</body>
</html>
