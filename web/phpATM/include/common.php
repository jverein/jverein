<?php
/***************************************************************************
 *                               common.php
 *                            -------------------
 *   begin                : Tuesday', Aug 15', 2002
 *   copyright            : ('C) 2008-2009 phpATM Dev Team
 *   email                : http://flavaclown.de/contact.php
 *
 *   $Id$
 *
 *
 ***************************************************************************/

/***************************************************************************
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License', or
 *   ('at your option) any later version.
 *
 ***************************************************************************/

if ( !defined('IN_PHPATM') )
{ die("Hacking attempt"); }

// Evita la visualizzazione di variabili non inizializzate
error_reporting(E_ERROR | E_WARNING | E_PARSE);

// Setta la locazione degli include (se possibile)
@ini_set('include_path', '.');

// Forza l'attivazione di alcune caratteristiche necessarie di php
@ini_set('file_uploads', 1);
@ini_set('allow_url_fopen', 1);
@ini_set('max_execution_time', '300');

// Alcuni include vitali, non cambiatene l'ordine!!!
$include_location = dirname($_SERVER['SCRIPT_FILENAME'])."/";
include($include_location.'include/functions.'.$phpExt);

// Impostazione dell'header location http/1.1 compliant
$header_location = ( @preg_match('/Microsoft|WebSTAR|Xitami/', getenv('SERVER_SOFTWARE')) ) ? 'Refresh: 0; URL=' : 'Location: ';
$testdir = strpos($_SERVER['PHP_SELF'],"/",1);
if ($testdir !== false)
{ $header_location = $header_location."http://".$_SERVER['HTTP_HOST'].dirname($_SERVER['PHP_SELF'])."/"; }
else
{ $header_location = $header_location."http://".$_SERVER['HTTP_HOST']."/"; }

// Utente con ip bloccato
if (is_ip_blocked(getenv('REMOTE_ADDR')))
{
	header($header_location.'ipblocked.'.$phpExt);
	exit;
}

// Registro la sessione o recupero quella attiva
session_start();

// Recupero la versione del php correntemente in uso
$version = phpversion();
$major = substr($version, 0, 1);
$release = substr($version, 2, 1);

// Leggo le variabili passate alla pagina in un colpo solo.
// Lo faccio prima di leggere il file di configurazione cosi' se qualche utente malizioso
// dovesse passarmi via url una variabile critica, l'inclusione seguente la ripristina
if ($major < 4)
{ die("Wrong PHP Version: minimum required 4.0.0 - currently installed ".phpversion()."<BR>Please upgrade"); }

// Inizializzo alcune variabili essenziali

$action = getPostVar('action');
if (!isset($action))
{ $action = getGetVar('action'); }
if (!isset($action))
{ $action = ''; }

// changed 09/09/2009 20:05
$logged_user_id = getSessionVar('logged_user_id');
$logged_user_name = getSessionVar('logged_user_name');
if (!isset($logged_user_id))
{ $logged_user_id = getCookieVar('logged_user_id'); }
if (!isset($logged_user_name))
{ $logged_user_name = getCookieVar('logged_user_name'); }

if (!isset($logged_user_name))
{ $logged_user_name = ''; }
	
$language = getSessionVar('language');
if (!isset($language))
{ $language = $dft_language; }

$skinindex = getSessionVar('skinindex');
if (!isset($skinindex))
{ $skinindex = 0; }

if ($skinindex > count($skins))
{ $skinindex = 0; }

$bordercolor=$skins[$skinindex]["bordercolor"];
$headercolor = $skins[$skinindex]["headercolor"];
$tablecolor=$skins[$skinindex]["tablecolor"];
$lightcolor=$skins[$skinindex]["lightcolor"];
$headerfontcolor=$skins[$skinindex]["headerfontcolor"];
$normalfontcolor=$skins[$skinindex]["normalfontcolor"];
$selectedfontcolor=$skins[$skinindex]["selectedfontcolor"];


// Inizializzo alcune variabili
$activationcode = USER_DISABLED;
$user_status = ANONYMOUS;

// Carico le info sull'utente loggato
if ($logged_user_name != '' && !check_is_user_session_active($logged_user_name))
{
	$user_status = ANONYMOUS;
	$logged_user_name = '';
}

// L'utente non  loggato
if ($user_status == ANONYMOUS)
{ $logged_user_name = ''; }

// L'utente loggato  disabilitato o non ha ancora attivato l'account
if ($activationcode != USER_ACTIVE)
{
	$user_status = ANONYMOUS;
	$logged_user_name = '';
}

// scrivo nella sessione le lingue disponibili, in modo da non dover leggere
// i file .lang ogni volta che una pagina viene richiamata
$languages = getSessionVar('languages');
if (!isset($languages) || !is_array($languages))
{
	$languages = available_languages($languages_folder_name);
	if (isset($_SESSION))
	{ $_SESSION['languages'] = $languages; }
	else
	{ $HTTP_SESSION_VARS['languages'] = $languages; }
}
$timeoffset = -$GMToffset + $languages[$language]['TimeZone'];
include($include_location."${languages_folder_name}/${language}.${phpExt}");

// Le pagine non devono rimanere in cache..
header("Expires: Mon, 03 Jan 2000 00:00:00 GMT");
header("Last-Modified: " . gmdate("D, d M Y H:i:s") . " GMT");
header("Cache-Control: no-cache, must-revalidate");
header("Pragma: no-cache");
if ($charsetencoding != "")
{ header('Content-Type: text/html; charset="'.$charsetencoding.'"'); }
?>