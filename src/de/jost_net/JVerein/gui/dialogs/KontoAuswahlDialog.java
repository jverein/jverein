/**********************************************************************
 * basiert auf dem KontoAuswahlDialog aus Hibiscus
 * Copyright (c) by Heiner Jostkleigrewe
 * This program is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,  but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See 
 *  the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, 
 * see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de
 * www.jverein.de
 **********************************************************************/

package de.jost_net.JVerein.gui.dialogs;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.gui.parts.KontoList;
import de.jost_net.JVerein.rmi.Konto;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.system.OperationCanceledException;

/**
 * Ein Dialog, ueber den man ein Konto auswaehlen kann.
 */
public class KontoAuswahlDialog extends AbstractDialog<Konto>
{

  private String text = null;

  private Konto choosen = null;

  private boolean keinkonto;

  private boolean nurHibiscus;
  
  private boolean nurAktuelleKonten;

  /**
   * Dialog zur Kontenauswahl
   * @param position An welcher Stelle soll der Dialog angezeigt werden?
   * @param keinkonto Darf der Dialog auch ohne Kontenauswahl geschlossen werden?
   * @param nurHibiscus Es sollen nur Hibiscus-Konten angezeigt werden
   * @param nurAktuelleKonten Es sollen nur aktuelle Konten angezeigt werden.
   */
  public KontoAuswahlDialog(int position, boolean keinkonto,
      boolean nurHibiscus, boolean nurAktuelleKonten)
  {
    super(position);
    super.setSize(400, 300);
    this.setTitle("Konto-Auswahl");
    this.keinkonto = keinkonto;
    this.nurHibiscus = nurHibiscus;
    this.nurAktuelleKonten=nurAktuelleKonten;
  }

  @Override
  protected void paint(Composite parent) throws Exception
  {
    LabelGroup group = new LabelGroup(parent, "Verfügbare Konten");

    if (text == null || text.length() == 0)
    {
      text = "Bitte wählen Sie das gewünschte Konto aus.";
    }
    group.addText(text, true);

    Action a = new Action()
    {
      @Override
      public void handleAction(Object context)
      {
        // wenn kein Konto ausgewählt sein darf, wird null zurückgegeben.
        if (context == null && keinkonto)
        {
          choosen = null;
          return;
        }
        if (context == null || !(context instanceof Konto))
        {
          return;
        }
        choosen = (Konto) context;
        close();
      }
    };
    final KontoList konten = new de.jost_net.JVerein.gui.parts.KontoList(a,
        nurHibiscus, nurAktuelleKonten);
    konten.setContextMenu(null);
    konten.setMulti(false);
    konten.setSummary(false);
    konten.paint(parent);

    ButtonArea b = new ButtonArea();
    b.addButton(i18n.tr("übernehmen"), new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        Object o = konten.getSelection();
        if (o == null || !(o instanceof Konto))
          return;

        choosen = (Konto) o;
        close();
      }
    });
    if (keinkonto)
    {
      b.addButton("kein Konto", new Action()
      {

        @Override
        public void handleAction(Object context)
        {
          choosen = null;
          close();
        }
      });
    }
    b.addButton("abbrechen", new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        throw new OperationCanceledException();
      }
    });
    b.paint(parent);
  }

  /**
   * Liefert das ausgewaehlte Konto zurueck oder <code>null</code> wenn der
   * Abbrechen-Knopf gedrueckt wurde.
   * 
   * @see de.willuhn.jameica.gui.dialogs.AbstractDialog#getData()
   */
  @Override
  protected Konto getData() throws Exception
  {
    return choosen;
  }

  /**
   * Optionale Angabe des anzuzeigenden Textes. Wird hier kein Wert gesetzt,
   * wird ein Standard-Text angezeigt.
   * 
   * @param text
   */
  public void setText(String text)
  {
    this.text = text;
  }

}
