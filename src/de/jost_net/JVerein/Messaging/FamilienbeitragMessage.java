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
 * $Log$
 **********************************************************************/

package de.jost_net.JVerein.Messaging;

import de.willuhn.datasource.GenericObject;
import de.willuhn.jameica.hbci.messaging.ObjectMessage;

/**
 * Wird versendet, wenn der FamilienbeitragsTree geaendert hat.
 */
public class FamilienbeitragMessage extends ObjectMessage
{
  public FamilienbeitragMessage(GenericObject object)
  {
    super(object);
  }
}