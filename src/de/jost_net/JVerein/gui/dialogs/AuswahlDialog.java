/**********************************************************************
 * basiert auf dem KontoAuswahlDialog aus Hibiscus Copyright (c) by Heiner Jostkleigrewe This
 * program is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de www.jverein.de
 **********************************************************************/

package de.jost_net.JVerein.gui.dialogs;

import java.util.List;
import org.eclipse.swt.widgets.Composite;

import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.system.OperationCanceledException;

/**
 * Ein Dialog, ueber den man ein Konto auswaehlen kann.
 */
public class AuswahlDialog extends AbstractDialog {
  private String text = null;
  private String subtitle = null;
  private String choosen = null;
  private SelectInput auswahl = null;

  /**
   * Dialog zur Kontenauswahl
   * 
   * @param position          An welcher Stelle soll der Dialog angezeigt werden?
   * @param keinkonto         Darf der Dialog auch ohne Kontenauswahl geschlossen werden?
   * @param nurHibiscus       Es sollen nur Hibiscus-Konten angezeigt werden
   * @param nurAktuelleKonten Es sollen nur aktuelle Konten angezeigt werden.
   */
  public AuswahlDialog(int position, List<String> auswahl, String title, String subtitle) {
    super(position);
    super.setSize(400, 300);
    this.setTitle(title);
    this.auswahl = new SelectInput(auswahl, 0);
    this.subtitle = subtitle;
  }

  @Override
  protected void paint(Composite parent) throws Exception {
    LabelGroup group = new LabelGroup(parent, subtitle);
    group.addText(text, true);
    group.addPart(auswahl);

    ButtonArea b = new ButtonArea();
    b.addButton(i18n.tr("auswählen"), new Action() {

      @Override
      public void handleAction(Object context) {
        choosen = auswahl.getText();
        close();
      }
    });
    b.paint(parent);
  }

  /**
   * Liefert das ausgewaehlte Element zurueck oder <code>null</code>
   * 
   * @see de.willuhn.jameica.gui.dialogs.AbstractDialog#getData()
   */
  @Override
  protected String getData() throws Exception {
    return choosen;
  }

  /**
   * Optionale Angabe des anzuzeigenden Textes. Wird hier kein Wert gesetzt, wird ein Standard-Text
   * angezeigt.
   * 
   * @param text
   */
  public void setText(String text) {
    this.text = text;
  }
}
