<?php
###########################################################################
#  Download Log
#  (c) 2008 by Peter Garstenauer & Reynhard Boegl for kick-image.at
#  Free Download: http://www.kick-image.at > WEBTOOLS
###########################################################################

###########################################################################
#
#    Datei-Verlinkung auf der Seite: download_log.php?dl=test.jpg
#
###########################################################################

###########################################################################
#  Einstellungen:
###########################################################################

# Pfad zum Ordner in dem die Download-Dateien liegen
$downloads_directory = 'downloads';

# Textfile welches die Downloads mitschreibt - benötigt Schreibrechte
$log_file = 'downloads.txt';

# E-mail-Adresse für Benachrichtigung bei Download
# leer lassen wenn keine Benachrichtgiung gewünscht
# mehrere E-mail-Adressen mit Beistrich getrennt
$email_message_to = 'downloads@jost-net.de';

# Sprach-Einstellungen
$lang['not_found'] = 'Die Datei wurde nicht gefunden!';

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

$dl = $_GET['dl'];
$dl = preg_replace('#[^a-z0-9._-]#i','',$dl);
if(!is_file($downloads_directory.'/'.$dl) or preg_match('#^\.#',$dl)) {
	if($dl){
		echo "<h2>" . $lang['not_found'] . "</h2>";
	}
}
else {
	if($email_message_to){
	    $ip=$_SERVER['REMOTE_ADDR'];
		mail($email_message_to,"Download: $dl, $ip","Download: $dl, $ip","FROM:$email_message_to");
	}
	if(is_file($log_file)){ // überschreiben
		$count = file($log_file);
	}
	else {
		$count = array();
	}
	
	foreach($count as $v){
		$temp = preg_split('# *\: *#',trim($v));
		if($temp[0]){
			$count_temp[$temp[0]] = $temp[1];
		}
	}
	$count_temp[$dl]++;
	$str_count = '';
	foreach($count_temp as $k => $v){
		$temp = preg_split('# *\: *#',trim($v));
		$str_count .= "$k: $v\r\n";
	}
	if($fh = fopen($log_file,'w')){
		fwrite($fh,$str_count);
		fclose($fh);
		@chmod($log_file,0777);
	}
	else {
		echo "ERROR: $log_file";
		exit;
	}

	header("Content-type: application/octet-stream");
	header("Content-disposition: attachment; filename=".$dl."");
	header("Content-Length: ".filesize($downloads_directory.'/'.$dl));
	header("Pragma: no-cache");
	header("Expires: 0");
	$fh = fopen($downloads_directory.'/'.$dl,'rb');
	while ($line = fgets($fh,1024))
	{
	  print $line;
	}
	fclose($fh);
	exit;
}

?>
