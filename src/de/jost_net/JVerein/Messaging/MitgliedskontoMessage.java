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

package de.jost_net.JVerein.Messaging;

import de.willuhn.datasource.GenericObject;
import de.willuhn.jameica.hbci.messaging.ObjectMessage;

/**
 * Wird versendet, wenn der Mitgliedskonto-Baum sich geaendert hat.
 */
public class MitgliedskontoMessage extends ObjectMessage
{
  public MitgliedskontoMessage(GenericObject object)
  {
    super(object);
  }
}