<?php
header("Location: http://www.jverein.de/wiki/index.php?title=Adressen");
echo "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n";
echo " \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n";
echo "<html xmlns=\"http://www.w3.org/1999/xhtml\"
  lang=\"de\" xml:lang=\"de\">\n";
echo "  <head>\n";
echo "    <meta http-equiv=\"content-type\"\n";
echo "      content=\"text/html; charset=utf-8\" />\n";
echo "    <title>Weiterleitung</title>\n";
echo "  </head>\n";
echo "  <body>\n";
echo "    <p>Die gesuchte Ressource wurde nach";
echo "      <a href=\"http://www.jverein.de/wiki/index.php?title=Adressen\">http://www.jverein.de/wiki/index.php?title=Adressen</a>";
echo "      verschoben.</p>\n";
echo "  </body>\n";
echo "</html>";
exit();
?>