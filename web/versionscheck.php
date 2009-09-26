<? 
  $version= $HTTP_GET_VARS[version];
  $build=$HTTP_GET_VARS[build];
  $devel = "no";
  
  if (strlen($version) > 6)
  {
  	$version = substr($version, 0, strlen($version)-6);
  	$devel = "yes";
  }
  if ($version < "1.2.0")
  {
    echo "neue Version unter www.jverein.de verfügbar";
    return;
  }
  if ($devel = "yes" and isset($build))
  {
    if ($build < "210")
    {
      echo "neue Entwicklerversion unter www.jverein.de verfügbar";
      return;
    }
  } 
?>