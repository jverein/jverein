/**********************************************************************
 * $Source$
 **********************************************************************/
package de.jost_net.JVerein.gui.view;

import de.jost_net.JVerein.gui.action.BackAction;
import de.jost_net.JVerein.gui.control.EinstellungControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.ButtonArea;
import de.willuhn.jameica.gui.util.Headline;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.util.ApplicationException;

public class EinstellungenView extends AbstractView
{
  public void bind() throws Exception
  {
    GUI.getView().setTitle("Einstellungen");

    final EinstellungControl control = new EinstellungControl(this);

    LabelGroup group = new LabelGroup(getParent(), "Einstellungen");
    group.addLabelPair("Geburtsdatum Pflichtfeld", control
        .getGeburtsdatumPflicht());
    group.addLabelPair("Eintrittsdatum Pflichtfeld", control
        .getEintrittsdatumPflicht());
    group.addLabelPair("Kommunikationsdaten anzeigen", control
        .getKommunikationsdaten());
    group.addLabelPair("Zusatzabbuchungen anzeigen *", control
        .getZusatzabbuchung());
    group.addLabelPair("Vermerke anzeigen", control.getVermerke());
    group.addLabelPair("Wiedervorlage anzeigen *", control.getWiedervorlage());
    group
        .addLabelPair("Kursteilnehmer anzeigen *", control.getKursteilnehmer());
    new Headline(getParent(), "* Änderung erfordert Neustart");
    ButtonArea buttons = new ButtonArea(getParent(), 2);
    buttons.addButton("<< Zurück", new BackAction());
    buttons.addButton("Speichern", new Action()
    {
      public void handleAction(Object context) throws ApplicationException
      {
        control.handleStore();
      }
    }, null, true);
  }

  public void unbind() throws ApplicationException
  {
  }
}
