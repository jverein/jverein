<?php
/***************************************************************************
 *                               configure.php
 *                            -------------------
 *   begin                : Tuesday', Aug 15', 2002
 *   copyright            : ('C) 2008-2009 phpATM Dev Team
 *   email                : flavaclown.de/contact.php
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

define('IN_PHPATM', true);
$include_location = dirname($_SERVER['SCRIPT_FILENAME'])."/";
include($include_location.'include/conf.php');
include($include_location.'include/common.'.$phpExt);

function print_configure_page()
{
	global $mess, $font, $normalfontcolor, $selectedfontcolor, $homeurl, $languages;
	global $uploadcentercaption,$logged_user_name,$mail_functions_enabled;
	global $tablecolor,$bordercolor,$headercolor,$headerfontcolor;
	global $phpExt, $reserved_files, $cfg_folder_name;


	echo "
	   <table border=\"0\" width=\"90%\" bgcolor=\"$bordercolor\" cellpadding=\"4\" cellspacing=\"1\">
	     <tr>
	       <th align=\"left\" bgcolor=\"$headercolor\" valign=\"middle\"><font size=\"2\" face=\"$font\" color=\"$headerfontcolor\">$mess[165]:</font></th>
	     </tr>";


	$handle = opendir($cfg_folder_name);
	while (false !== ($filename = readdir($handle)))
	{
		if(eregi("\.txt$|\.htm$|\.html$",$filename))
		{
			if (!is_dir("$cfg_folder_name/$filename") && !eregi($reserved_files, $filename))
			{
				echo"
					<tr>
					<td align=\"left\" bgColor=\"$tablecolor\"><font size=\"1\" face=\"$font\" color=\"$normalfontcolor\"><a href=\"configure.{$phpExt}?action=".ACTION_EDITFILE."&filename=$filename&".SID."\">$filename</a></font></td>
					</tr>";
			}
		}
	}
	closedir($handle);
	echo"</table>";
}

function show_file_editor($filename)
{
	global $mess, $font, $normalfontcolor, $selectedfontcolor, $languages;
	global $tablecolor,$bordercolor,$headercolor,$headerfontcolor;
	global $phpExt, $cfg_folder_name;

	if (!file_exists("$cfg_folder_name/$filename"))
	{ return; }

	$max_caracters = filesize("$cfg_folder_name/$filename");
	if ($max_caracters > 0)
	{
		$fp = @fopen("$cfg_folder_name/$filename", 'r');
		$filebody = fread($fp, $max_caracters);
		fclose($fp);
	}
	echo "<br>
	<table border=\"0\" width=\"90%\" bgcolor=\"$bordercolor\" cellpadding=\"4\" cellspacing=\"1\">
	<tr>
	  <th align=\"left\" bgcolor=\"$headercolor\" valign=\"middle\"><font size=\"2\" face=\"$font\" color=\"$headerfontcolor\">$mess[166]:</font></th>
	</tr>
	<tr>
	  <td align=\"center\" bgColor=\"$tablecolor\">
	    <form name=\"".ACTION_EDITFILE."\" action=\"configure.$phpExt?".SID."\" method=\"post\" style=\"margin: 0\">
	      <input type=\"hidden\" name=\"action\" value=\"".ACTION_SAVEFILE."\">
	      <input type=\"hidden\" name=\"filename\" value=\"$filename\">
	      <textarea class=\"vform\" name=\"filebody\" cols=\"82\" rows=\"20\">$filebody</textarea>
	      <input type=\"submit\" class=\"vform2\" value=\"$mess[168]\">
	    </form>
	  </td>
	</tr>
	</table>";
}

function show_default($message)
{
	global $logged_user_name, $mess;

	if ($logged_user_name != '')
	{
		if (check_is_user_session_active($logged_user_name))
		{
			if ($message == '')
			{ $message = $mess[164]; }
			place_message($mess[164], $message, basename(__FILE__));
			print_configure_page();
			return;
		}
	}

	if ($message == '')
	{ $message = $mess[42]; }
	place_message($mess[164], $message, basename(__FILE__));
}

//----------------------------------------------------------------------------
//      MAIN
//----------------------------------------------------------------------------

// Se l'utente non  loggato lo indirizzo al login
if ($logged_user_name == '')
{
	header($header_location.'login.'.$phpExt.'?'.SID);
	exit;
}

// Controllo che l'utente sia amministratore
if ($user_status != ADMIN)
{
	header($header_location.'index.'.$phpExt.'?'.SID);
	exit;
}

switch($action)
{
	case ACTION_SELECTSKIN;
		change_skin();
		show_default($mess[96]);
		break;

	case ACTION_EDITFILE;
	
		$filename = getGetVar('filename');

		place_message($mess[164], $mess[166], basename(__FILE__));
		show_file_editor($filename);
		break;

	case ACTION_SAVEFILE;
	
		$filename = getPostVar('filename');
		$filebody = getPostVar('filebody');

		if (!isset($filebody))
		{ break; }
		$filebody = stripslashes($filebody);
		$filebody = str_replace("&", "&amp;", $filebody);
		$filebody = preg_replace('/[^\x09\x0A\x0D\x20-\x7F]/e', '"&#".ord($0).";"', $filebody);
		$fp=@fopen("$cfg_folder_name/$filename","w+");
		fwrite($fp, $filebody);
		fclose($fp);
		show_default(sprintf($mess[167], $filename));
		break;

	default;
		show_default('');
		break;
}

show_footer_page();
?>