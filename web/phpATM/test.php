<?php
/***************************************************************************
 *                               test.php
 *                            -------------------
 *   begin                : Tuesday', Aug 15', 2002
 *   copyright            : ('C) 2002 Bugada Andrea
 *   email                : phpATM@free.fr
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
/*
   0. Controllare la versine di php
   1. Controllare che ini_set non sia disablitata
   2. Controllare che 'short_open_tag' non sia disabilitata
   3.
*/

// Controllo la versione di php in uso
$version = phpversion();
$major = substr($version, 0, 1);
$release = substr($version, 2, 1);

// 3.x.x: versione troppo vecchia
// 4.1.2: sessioni non funzionano
// 4.0.6: sessioni funzionano solo se 'register_global'  on

// Controllo che ini_set() non sia tra le funzioni disabilitate
if ($iniset_disabled = ereg("ini_set", get_cfg_var('disable_functions')))
{
	echo "<BR>Warning: ini_set() disabled";
}

// Non modificabile
echo "<BR>SafeMode active: ".ini_get('safe_mode');

// Non modificabile
echo "<BR>Max Upload FileSize: ".ini_get('upload_max_filesize');

// Non modificabile
echo "<BR>Post Max Size: ".ini_get('post_max_size');

// Non modificabile da script
echo "<BR>Memory limit: ".get_cfg_var('memory_limit');

// Non modificabile
echo "<BR>Upload temp dir: ".get_cfg_var('upload_temp_dir');

// Modificabile da script
echo "<BR>Upload enabled: ".ini_get('file_uploads');

// Modificabile da script
echo "<BR>Allow url fopen: ".ini_get('allow_url_fopen');

// Modificabile da script
echo "<BR>Include path: ".ini_get('include_path');

// Modificabile da script
echo "<BR>Session save path: ".ini_get('session.save_path');

// Modificabile da script
echo "<BR>Max execution time: ".ini_get('max_execution_time');

// Non modificabile da script
echo "<BR>Open basedir: ".ini_get('open_basedir ');

?>