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
 * Revision 1.1  2008/09/16 18:52:44  jost
 * Neu: Rechnung
 *
 * Revision 1.14  2008/08/10 12:36:02  jost
 * Abbuchung -> Abrechnung
 * Vorbereitung der Rechnungserstellung
 *
 * Revision 1.13  2008/05/24 14:04:08  jost
 * Redatkionelle Änderung
 *
 * Revision 1.12  2008/01/31 19:39:37  jost
 * Berücksichtigung eines Stichtages für die Abbuchung
 *
 * Revision 1.11  2008/01/01 19:47:16  jost
 * Erweiterung um Hilfe-Funktion
 *
 * Revision 1.10  2007/12/26 18:13:19  jost
 * Lastschriften können jetzt als Einzellastschriften oder Sammellastschriften direkt in Hibuscus verbucht werden.
 *
 * Revision 1.9  2007/12/21 13:35:58  jost
 * Ausgabe der DTAUS-Datei im PDF-Format
 *
 * Revision 1.8  2007/12/02 13:41:18  jost
 * überflüssiges Import-Statement entfernt.
 *
 * Revision 1.7  2007/08/22 20:44:10  jost
 * Bug #011762
 *
 * Revision 1.6  2007/07/20 20:15:40  jost
 * Bessere Fehlermeldung
 *
 * Revision 1.5  2007/07/06 11:37:18  jost
 * Zur Kompatibilit�t: �nderung der Plausi.
 *
 * Revision 1.4  2007/02/25 19:13:05  jost
 * Neu: Kursteilnehmer
 *
 * Revision 1.3  2007/02/23 20:27:28  jost
 * Mail- und Webadresse im Header korrigiert.
 *
 * Revision 1.2  2006/10/21 09:18:54  jost
 * Zus�tzliche Plausi.
 *
 * Revision 1.1  2006/09/20 15:39:10  jost
 * *** empty log message ***
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.DokumentationAction;
import de.jost_net.JVerein.gui.control.RechnungControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.buttons.Back;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class RechnungView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Rechnung");

    final RechnungControl control = new RechnungControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Parameter");
    if (this.getCurrentObject() == null)
    {
      group.addLabelPair("von Datum", control.getVondatum());
      group.addLabelPair("bis Datum", control.getVondatum());
    }
    group.addLabelPair("Formular", control.getFormular());

    ButtonArea buttons = new ButtonArea(this.getParent(), 3);
    buttons.addButton(new Back(false));
    buttons.addButton("Hilfe", new DokumentationAction(),
        DokumentationUtil.RECHNUNG);
    buttons.addButton(control.getStartButton(this.getCurrentObject()));
  }

  public void unbind() throws ApplicationException
  {
  }
}
