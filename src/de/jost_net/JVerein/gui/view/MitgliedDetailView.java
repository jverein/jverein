/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * All rights reserved
 * heiner@jverein.de
 * www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

public class MitgliedDetailView extends AbstractAdresseDetailView
{

  public String getTitle()
  {
    return "Daten des Mitgliedes";
  }

  public boolean isMitgliedDetail()
  {
    return true;
  }

}
