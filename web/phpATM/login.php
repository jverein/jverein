<?php
/***************************************************************************
 *                                login.php
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

function print_default()
{
global $phpExt, $mess, $font, $normalfontcolor, $selectedfontcolor, $languages;
global $uploadcentercaption,$require_email_confirmation;
global $tablecolor,$bordercolor,$headercolor,$headerfontcolor;
global $extra_character,$username_lenght,$registertable_enabled,$password_reminder_table_enabled;

	$tab = 1; // used for the "tabindex"

	echo "
<table border=\"0\" width=\"90%\" bgcolor=\"$bordercolor\" cellpadding=\"4\" cellspacing=\"1\">
<tr>
<th align=\"left\" bgcolor=\"$headercolor\" valign=\"middle\"><font size=\"2\" face=\"$font\" color=\"$headerfontcolor\">$mess[98]</font></th>
</tr>
<tr>
<td align=\"left\" bgcolor=\"$tablecolor\" valign=\"middle\">
<form name=\"userlogin\" action=\"login.$phpExt?".SID."\" method=\"post\" style=\"margin: 0\">
<input type=\"hidden\" name=\"action\" value=\"userlogin\">
<table border=\"0\" width=\"100%\" cellpadding=\"4\">
<tr>
<td align=\"left\" width=\"25%\"><label for=\"user_name\"><font size=\"1\" face=\"$font\" color=\"$normalfontcolor\">$mess[57]</font></label></td>
<td align=\"left\" width=\"75%\" colspan=\"2\"><input type=\"text\" name=\"user_name\" id=\"user_name\" class=\"vform\" size=\"25\" tabindex=\"".$tab++."\"></td>
</tr>
<tr>
<td align=\"left\" width=\"25%\"><label for=\"user_pass\"><font size=\"1\" face=\"$font\" color=\"$normalfontcolor\">$mess[83]</font></label></td>
<td align=\"left\" width=\"75%\" colspan=\"2\"><input type=\"password\" name=\"user_pass\" id=\"user_pass\" class=\"vform\" size=\"25\" tabindex=\"".$tab++."\"></td>
</tr>
<tr>
<td align=\"left\" width=\"25%\"><label for=\"user_always_logged\"><font size=\"1\" face=\"$font\" color=\"$normalfontcolor\">$mess[205]</font></label></td>
<td align=\"left\" width=\"65%\"><input type=\"checkbox\" name=\"user_always_logged\" id=\"user_always_logged\" value=\"user_always_logged\" tabindex=\"".$tab++."\"></td>
<td align=\"right\" width=\"10%\"><input type=\"submit\" class=\"vform2\" name=\"Submit\" value=\"$mess[73]\" tabindex=\"".$tab++."\"></td>
</tr>
</table>
</form>
</td>
</tr>
</table>
<br>";

	if ($registertable_enabled)
	{
		echo "
<table border=\"0\" width=\"90%\" bgcolor=\"$bordercolor\" cellpadding=\"4\" cellspacing=\"1\">
<tr>
<th align=\"left\" bgcolor=\"$headercolor\" valign=\"middle\"><font size=\"2\" face=\"$font\" color=\"$headerfontcolor\">$mess[99]</font></th>
</tr>
<tr>
<td align=\"left\" bgcolor=\"$tablecolor\" valign=\"middle\"><font size=\"1\" color=\"$normalfontcolor\" face=\"$font\">&nbsp;&nbsp;<a href=\"register.$phpExt?".SID."\" style=\"font-size:10px;\" tabindex=\"".$tab++."\">$mess[58]</a></font></td>
</tr>
</table>
<br>";
	}

	if ($password_reminder_table_enabled)
	{
		echo "
<table border=\"0\" width=\"90%\" bgcolor=\"$bordercolor\" cellpadding=\"4\" cellspacing=\"1\">
<tr>
<th align=\"left\" bgcolor=\"$headercolor\" valign=\"middle\"><font size=\"2\" face=\"$font\" color=\"$headerfontcolor\">$mess[100]</font></th>
</tr>
<tr>
<td align=\"left\" bgcolor=\"$tablecolor\" valign=\"middle\">
<form name=\"logonsystem\" action=\"login.$phpExt?".SID."\" method=\"post\" style=\"margin: 0\">
<input type=\"hidden\" name=\"action\" value=\"sendpassword\">
<table border=\"0\" width=\"100%\" cellpadding=\"4\">
<tr>
<td align=\"left\" width=\"25%\"><label for=\"user_name2\"><font size=\"1\" face=\"$font\" color=\"$normalfontcolor\">$mess[57]</font></label></td>
<td align=\"left\" width=\"75%\" colspan=\"2\"><input type=\"text\" name=\"user_name2\" id=\"user_name2\" class=\"vform\" size=\"25\" tabindex=\"".$tab++."\"></td>
</tr>
<tr>
<td align=\"left\" width=\"25%\"><label for=\"typed_email\"><font size=\"1\" face=\"$font\" color=\"$normalfontcolor\">$mess[91]</font></label></td>
<td align=\"left\" width=\"65%\"><input type=\"text\" name=\"typed_email\" id=\"typed_email\" class=\"vform\" size=\"25\" tabindex=\"".$tab++."\"></td>
<td align=\"right\" width=\"10%\"><input type=\"submit\" value=\"$mess[67]\" class=\"vform2\" tabindex=\"".$tab++."\"></td>
</tr>
</table>
</form>
</td>
</tr>
</table>";
	}
}


function print_user_profile()
{
	global $mess,$font,$normalfontcolor,$selectedfontcolor,$languages;
	global $uploadcentercaption,$logged_user_name,$mail_functions_enabled;
	global $tablecolor,$bordercolor,$headercolor,$headerfontcolor, $language;
	global $user_wish_receive_digest, $user_email, $user_account_creation_time;
	global $allow_choose_language, $dft_language, $phpExt;


	$account_date = getdate($user_account_creation_time);
	$month = $account_date['mon'];
	$mday = $account_date['mday'];
	$year = $account_date['year'];
	list($files_uploaded, $files_downloaded, $files_emailed) = load_userstat($logged_user_name);

	$tab = 1;

	echo "
<table border=\"0\" width=\"90%\" bgcolor=\"$bordercolor\" cellpadding=\"4\" cellspacing=\"1\">
<tr>
<th align=\"left\" bgcolor=\"$headercolor\" valign=\"middle\"><font size=\"2\" face=\"$font\" color=\"$headerfontcolor\">$mess[135]</font></th>
</tr>
<tr>
<td align=\"left\" bgcolor=\"$tablecolor\" valign=\"middle\">
<table border=\"0\" width=\"100%\" cellpadding=\"4\">
<tr>
<td align=\"left\" width=\"30%\"><font size=\"1\" face=\"$font\" color=\"$normalfontcolor\">$mess[136]</font></td>
<td align=\"left\" width=\"70%\"><font size=\"1\" face=\"$font\" color=\"$normalfontcolor\">$mess[$month] $mday, $year</font></td>
</tr>";

	if ($files_uploaded)
	{
		echo "
<tr>
<td align=\"left\" width=\"30%\"><font size=\"1\" face=\"$font\" color=\"$normalfontcolor\">$mess[151]:</font></td>
<td align=\"left\" width=\"70%\"><font size=\"1\" face=\"$font\" color=\"$normalfontcolor\">$files_uploaded</font></td>
</tr>";
	}

	if ($files_downloaded)
	{
		echo "
<tr>
<td align=\"left\" width=\"30%\"><font size=\"1\" face=\"$font\" color=\"$normalfontcolor\">$mess[152]:</font></td>
<td align=\"left\" width=\"70%\"><font size=\"1\" face=\"$font\" color=\"$normalfontcolor\">$files_downloaded</font></td>
</tr>";
	}

	if ($files_emailed)
	{
		echo "
<tr>
<td align=\"left\" width=\"30%\"><font size=\"1\" face=\"$font\" color=\"$normalfontcolor\">$mess[153]:</font></td>
<td align=\"left\" width=\"70%\"><font size=\"1\" face=\"$font\" color=\"$normalfontcolor\">$files_emailed</font></td>
</tr>";
	}

	echo "
</table>
</td>
</tr>
</table>
<br>
<table border=\"0\" width=\"90%\" bgcolor=\"$bordercolor\" cellpadding=\"4\" cellspacing=\"1\">
<tr>
<th align=\"left\" bgcolor=\"$headercolor\" valign=\"middle\"><font size=\"2\" face=\"$font\" color=\"$headerfontcolor\">$mess[126]</font></th>
</tr>
<tr>
<td align=\"left\" bgcolor=\"$tablecolor\" valign=\"middle\">
<form name=\"userprofile\" id=\"userprofile\" action=\"login.$phpExt?".SID."\" method=\"post\" style=\"margin: 0\">
<input type=\"hidden\" name=\"action\" value=\"customizeprofile\">
<table border=\"0\" width=\"100%\" cellpadding=\"4\">
<tr>
<td align=\"left\" width=\"30%\"><label for=\"typed_email\"><font size=\"1\" face=\"$font\" color=\"$normalfontcolor\">$mess[88]</font></label></td>
<td align=\"left\" width=\"60%\"><input type=\"text\" name=\"typed_email\" id=\"typed_email\" value=\"$user_email\" class=\"vform\" size=\"25\" tabindex=\"".$tab++."\"></td>";

	if ($allow_choose_language)
	{
		echo "
<td align=\"right\" width=\"10%\"></td></tr>
<tr>
<td align=\"left\" width=\"30%\"><label for=\"sel_lang\"><font size=\"1\" face=\"$font\" color=\"$normalfontcolor\">$mess[84]</font></label></td>
<td align=\"left\" width=\"60%\"><font size=\"1\" face=\"$font\" color=\"$normalfontcolor\">
<select name=\"sel_lang\" id=\"sel_lang\" class=\"vform\" size=\"1\" tabindex=\"".$tab++."\">";

		while (list($langid, $langdata) = each($languages))
		{
			if ($langid == $language)
			{ echo "<option value=\"$langid\" selected>".$langdata['LangName']."</option>"; }
			else
			{ echo "<option value=\"$langid\">".$langdata['LangName']."</option>"; }
		}

		echo "</select></font></td>";
	}

	if ($mail_functions_enabled)
	{
		echo "
<td align=\"right\" width=\"10%\"></td></tr>
<tr>
<td align=\"left\" width=\"30%\"><label for=\"digestcheckbox\"><font size=\"1\" face=\"$font\" color=\"$normalfontcolor\">$mess[119]</font></label></td>
<td align=\"left\" width=\"60%\">";

		if ($user_wish_receive_digest)
		{ echo "<input type=\"checkbox\" name=\"digestcheckbox\" id=\"digestcheckbox\" value=\"digestcheckbox\" tabindex=\"".$tab++."\" checked></td>"; }
		else
		{ echo "<input type=\"checkbox\" name=\"digestcheckbox\" id=\"digestcheckbox\" value=\"digestcheckbox\" tabindex=\"".$tab++."\"></td>"; }

		echo "
<td align=\"right\" width=\"10%\"><input type=\"submit\" name=\"Submit\" value=\"$mess[127]\" class=\"vform2\" tabindex=\"".$tab++."\"></td>
</tr>";
	}
	else
	{
		echo "
<td align=\"right\" width=\"10%\"><input type=\"submit\" name=\"Submit\" value=\"$mess[127]\" class=\"vform2\" tabindex=\"".$tab++."\"></td>
</tr>";
	}

	echo "
</table>
</form>
</td>
</tr>
</table>
<br>
<table border=\"0\" width=\"90%\" bgcolor=\"$bordercolor\" cellpadding=\"4\" cellspacing=\"1\">
<tr>
<th align=\"left\" bgcolor=\"$headercolor\" valign=\"middle\"><font size=\"2\" face=\"$font\" color=\"$headerfontcolor\">$mess[120]</font></th>
</tr>
<tr>
<td align=\"left\" bgcolor=\"$tablecolor\" valign=\"middle\">
<form name=\"changepass\" id=\"changepass\" action=\"login.$phpExt?".SID."\" method=\"post\" style=\"margin: 0\">
<input type=\"hidden\" name=\"action\" value=\"changepass\">
<table border=\"0\" width=\"100%\" cellpadding=\"4\">
<tr>
<td align=\"left\" width=\"30%\"><label for=\"old_pass\"><font size=\"1\" face=\"$font\" color=\"$normalfontcolor\">$mess[121]</font></label></td>
<td align=\"left\" width=\"70%\" colspan=\"2\"><input type=\"password\" name=\"old_pass\" id=\"old_pass\" class=\"vform\" size=\"25\" tabindex=\"".$tab++."\"></td>
</tr>
<tr>
<td align=\"left\" width=\"30%\"><label for=\"new_pass\"><font size=\"1\" face=\"$font\" color=\"$normalfontcolor\">$mess[83]</font></label></td>
<td align=\"left\" width=\"60%\"><input type=\"password\" name=\"new_pass\" id=\"new_pass\" class=\"vform\" size=\"25\" tabindex=\"".$tab++."\"></td>
<td align=\"right\" width=\"10%\"><input type=\"submit\" name=\"Submit\" value=\"$mess[127]\" class=\"vform2\" tabindex=\"".$tab++."\"></td>
</tr>
</table>
</form>
</td>
</tr>
</table>";
}

function show_default($message)
{
	global $logged_user_name, $user_status, $mess;

	if ($logged_user_name != '' && $user_status != ANONYMOUS)
	{
		if (check_is_user_session_active($logged_user_name))
		{
		// If user already entered, show logout screen
		$message = ($message == '') ? $mess[82] : $message;
		place_message($mess[82], $message, basename(__FILE__));
		print_user_profile();
		return;
		}
	}
	// Show default window
	if ($message == "")
	{ $message = $mess[42]; }
	place_message($mess[71], $message, basename(__FILE__));
	print_default();
}

//----------------------------------------------------------------------------
//      MAIN
//----------------------------------------------------------------------------

$user_name = getPostVar('user_name');
$user_pass = getPostVar('user_pass');
$user_always_logged = getPostVar('user_always_logged');
$old_pass = getPostVar('old_pass');
$new_pass = getPostVar('new_pass');
if($_REQUEST['user_name2'] != "")
{ $user_name = getPostVar('user_name2'); }
$typed_email = getPostVar('typed_email');
$digestcheckbox = getPostVar('digestcheckbox');
if ($allow_choose_language)
{ $sel_lang = getPostVar('sel_lang'); }

switch($action)
{

	case 'selectskin':
		change_skin();
		require($include_location."${languages_folder_name}/${language}.${phpExt}");
		show_default($mess[96]);
		break;


//----------------------------------------------------------------------------
//      User Login
//----------------------------------------------------------------------------

	case 'userlogin':

	// makes sure that the username have the lenght, as it's set in the conf.php
	$un_lenght = $username_lenght-2;

	// User name can contain only latin and number spases,
	// and space, "_", "-" symbols inside the name
	// changed by Flava Clown - 02.09.2007
	if (eregi("^[a-z0-9$extra_character][a-z0-9$extra_character _-]{0,$un_lenght}[a-z0-9]$", $user_name))
	{
		$userfilename = "$users_folder_name/$user_name";

		// Check the user name
		if (file_exists($userfilename))
		{
			// Check the password
			if (check_user_password($user_name, $user_pass))
			{
				if ($activationcode == USER_ACTIVE)
				{
					$logged_user_name = $user_name;

					// Generate & store new session id
					srand((double)microtime()*1000000);
					$logged_user_id = md5(rand().microtime());
					$enc_logged_user_id = md5($logged_user_id);

					// scrivo i dati nella sessione corrente
					if ($major > 4 || $release > 0)
					{
						$_SESSION['logged_user_name'] = $logged_user_name;
						$_SESSION['logged_user_id'] = $logged_user_id;
					}
					else
					{
						if (get_cfg_var('register_globals') == 'On')
						{
							session_register('logged_user_name');
							session_register('logged_user_id');
						}
						else
						{
							$_SESSION['logged_user_name'] = $logged_user_name;
							$_SESSION['logged_user_id'] = $logged_user_id;
						}
					}

					// scrivo i dati nel cookie, se richiesto
					if (isset($user_always_logged))
					{
						if ($user_always_logged == 'on')
						{
							setcookie("logged_user_name", $logged_user_name, time() + $cookievalidity * 3600, $cookiepath, $cookiedomain, $cookiesecure);
							setcookie("logged_user_id", $logged_user_id, time() + $cookievalidity * 3600, $cookiepath, $cookiedomain, $cookiesecure);
						}
					}

					// Write new session ID
					save_user_profile($logged_user_name);

					// added by Flava Clown - to have the last access time even without any activities like downloading
					list($files_uploaded, $files_downloaded, $files_emailed) = load_userstat($logged_user_name);
					save_userstat($logged_user_name, $files_uploaded, $files_downloaded, $files_emailed, time());

					// and go to the propertly page...
					header($header_location.'index.'.$phpExt.'?'.SID);
					exit;
				}
				elseif ($activationcode == USER_DISABLED)
				{
					place_message($mess[71], $mess[80]." ".sprintf($mess[101], "<a href=\"login.$phpExt?".SID."\" style=\"font-size:10px;\">", "</a>"), basename(__FILE__));
					break;
				}
				else
				{
					if (ereg('^m:', $user_temp_info))
					{ 	header($header_location.'confirm.'.$phpExt.'?'.SID); } // Deve confermare il cambio di email 
					else
					{ header($header_location.'activate.'.$phpExt.'?'.SID); } // Deve confermare la registrazione
					exit;
				}
			}
		}
	}
	$logged_user_name = '';
	$user_status = ANONYMOUS;

	// Show invalid password message
	place_message($mess[71], $mess[80]." ".sprintf($mess[101], "<a href=\"login.$phpExt?".SID."\" style=\"font-size:10px;\">", "</a>"), basename(__FILE__));
	break;


case 'logout':

	// Delete session infos
	$_SESSION = array();
	session_destroy();

	// Delete cookie
	setcookie("logged_user_name", "", time()-86400, $cookiepath, $cookiedomain, $cookiesecure); // 1 day ago
	setcookie("logged_user_id", "", time()-86400, $cookiepath, $cookiedomain, $cookiesecure); // 1 day ago

	// Mark that user logged out
	if ($logged_user_name != '')
	{
		load_user_profile($logged_user_name);
		$enc_logged_user_id = 0;
		save_user_profile($logged_user_name);
	}

	$user_status = ANONYMOUS;
	$logged_user_name = '';

	// Show succesfully logout message
	place_message($mess[72], $mess[102], basename(__FILE__));


	break;

case 'sendpassword':

	$userfilename = "$users_folder_name/$user_name";
	if (!file_exists($userfilename))
	{
		place_message($mess[58], sprintf($mess[122], $user_name).' '.sprintf($mess[101], "<a href=\"login.$phpExt?".SID."\" style=\"font-size:10px;\">", "</a>"), basename(__FILE__));
		break;
	}

	load_user_profile($user_name);
	if (!isset($typed_email))
	{ $typed_email = ""; }
	if ($user_email != $typed_email)
	{
		place_message($mess[59], $mess[123].' '.sprintf($mess[116], "<a href=\"login.$phpExt?".SID."\" style=\"font-size:10px;\">", "</a>"), basename(__FILE__));
		break;
	}

	// Generate new password
	$user_pass = generate_password();
	$enc_user_pass = md5($user_pass);
	// Send e-mail
	$body = sprintf($chpass_email_body, $user_pass)."\n\n";
	$body .= $confirm_email_end."\n";
	$body .= $admin_name."\n";
	$body .= "Email: $admin_email";

	$from="$admin_name <$admin_email>";
	if ($charsetencoding != "")
	{ $headers .= "Content-Type: text/plain; charset=$charsetencoding\n"; }
	else
	{ $headers .= "Content-Type: text/plain; charset=iso-8859-1\n"; }
	$headers .= "From: $from\r\nX-Mailer: phpATM";

	if (!$use_smtp)
	{ $result = @mail($user_email,$chpass_email_subject,$body,$headers); }
	else
	{
		if (!defined('SMTP_INCLUDED'))
		{ include($include_location.'include/smtp.'.$phpExt); }
		$result = smtpmail($user_email,$chpass_email_subject,$body,$headers);
	}

	if ($result)
	{
		// Save user profile
		save_user_profile($user_name);
		place_message($mess[59], $mess[124], basename(__FILE__));
	}
	else
	{ place_message($mess[59], $mess[177].' '.$mess[179], basename(__FILE__)); }
	break;


case 'customizeprofile':

	if ($logged_user_name != '')
	{
		if (check_is_user_session_active($logged_user_name))
		{
			$user_wish_receive_digest = 0;
			// changed by Flava_Clown - 06.06.2007
			if (isset($digestcheckbox) && $digestcheckbox != "0")
			{ $user_wish_receive_digest = 1; }

			$user_temp_info = "";
			if ($typed_email != $user_email)
			{
				if (eregi( "^([a-z0-9_]|\\-|\\.)+@(([a-z0-9_]|\\-)+\\.)+[a-z]{2,4}$", $typed_email))
				{
					if (($mail_functions_enabled) && ($require_email_confirmation))
					{
						srand((double)microtime()*1000000);
						$activationcode = rand() + 100;
						$user_temp_info = "m:".$activationcode.":".$typed_email;
					}
					else
					{ $user_email = $typed_email; }
				}
				else
				{
					show_default($mess[107]);
					break;
				}
			}

			if ($allow_choose_language)
			{ 
				$language = $sel_lang;
				require($include_location."${languages_folder_name}/${language}.${phpExt}");
			}

			save_user_profile($logged_user_name);

			if ($user_temp_info == "")
			{ show_default($mess[128]); }
			else
			{
				// Send confirmation e-mail
				$body=sprintf($confirm_email_body, $logged_user_name, $activationcode, "$installurl/confirm.$phpExt");
				$body .= $confirm_email_end."\n";
				$body .= $admin_name."\n";
				$body .= "Email: $admin_email"."\n";

				$from="$admin_name <$admin_email>";
				if ($charsetencoding != "")
				{ $headers="Content-Type: text/plain; charset=$charsetencoding\n"; }
				else
				{ $headers="Content-Type: text/plain; charset=iso-8859-1\n"; }
				$headers.="From: $from\r\nX-Mailer: phpATM";

				if (!$use_smtp)
				{ $result = @mail($typed_email,$confirm_email_subject,$body,$headers); }
				else
				{
					if (!defined('SMTP_INCLUDED'))
					{ include($include_location.'include/smtp.'.$phpExt); }
					$result = smtpmail($typed_email,$confirm_email_subject,$body,$headers);
				}

				if ($result)
				{ show_default($mess[128]." ".$mess[143]); }
				else
				{ show_default($mess[177]." ".$mess[179]); }
			}
		}
	}
break;

case 'changepass':

	if ($logged_user_name != '')
	{
		if (check_is_user_session_active($logged_user_name))
		{
			if (md5($old_pass) == $enc_user_pass)
			{
				if ($new_pass != "")
				{
					$enc_user_pass = md5($new_pass);
					save_user_profile($logged_user_name);
					show_default($mess[129]);
				}
				else
				{ show_default($mess[131]); }
			}
			else
			{ show_default($mess[130]); }
		}
	}
	break;

//----------------------------------------------------------------------------
//      DEFAULT
//----------------------------------------------------------------------------

default:
	show_default('');
	break;
}

show_footer_page();
?>