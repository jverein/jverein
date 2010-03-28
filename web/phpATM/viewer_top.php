<?php
/***************************************************************************
 *                              viewer_top.php
 *                            -------------------
 *   begin                : Saturday', Mar 08', 2003
 *   copyright            : ('C) 2002-03 Bugada Andrea
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

define('IN_PHPATM', true);

$include_location = dirname($_SERVER['SCRIPT_FILENAME'])."/";
include($include_location.'include/conf.php');
include($include_location.'include/common.'.$phpExt);

$filename  = getGetVar('file');
$directory = getGetDir('dir');

$current_dir = $uploads_folder_name;
if ($directory != '')
{ $current_dir.="/$directory"; }

$bodytag = $skins[$skinindex]["bodytag"];
echo "<html>
<head>
<title>$mess[26]: $filename</title>\n";
if ($charsetencoding != "")
{ echo "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=$charsetencoding\">\n"; }
echo "<link rel=\"stylesheet\" href=\"styles.css\" type=\"text/css\">
</head>
<body $bodytag>
<center>
<font face=\"$font\" color=\"$normalfontcolor\" size=\"2\">$mess[26] : <img src=\"images/".get_mimetype_img("$current_dir/$filename")."\" align=\"ABSMIDDLE\">
<b>".$filename."</b></font><img src=\"images/empty.gif\" width=\"20\" height=\"40\" border=\"0\" align=\"ABSMIDDLE\">
<a href=\"javascript:window.print()\"><img src=\"images/imprimer.gif\" alt=\"$mess[27]\" border=\"0\" align=\"ABSMIDDLE\"></a>
<a href=\"index.${phpExt}?action=downloadfile&filename=".$filename."&directory=".$directory."&".SID."\"><img src=\"images/download.gif\" alt=\"$mess[23]\" width=\"20\" height=\"20\" border=\"0\" align=\"ABSMIDDLE\"></a>
</center>
</body>
</html>";
?>