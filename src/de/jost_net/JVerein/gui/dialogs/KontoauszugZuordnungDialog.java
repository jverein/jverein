/**********************************************************************
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.input.CheckboxInput;
import de.willuhn.jameica.gui.input.IntegerInput;
import de.willuhn.jameica.gui.input.LabelInput;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.Color;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.system.OperationCanceledException;

/**
 * Dialog zur Zuordnung der Kontoauszugsinformationen (Auszug/Blatt)
 */
public class KontoauszugZuordnungDialog extends AbstractDialog<Object>
{

  private IntegerInput auszugsnummer = null;

  private Integer intAuszugsnummer;

  private IntegerInput blattnummer = null;

  private Integer intBlattnummer;

  private CheckboxInput ueberschreiben = null;

  private LabelInput status = null;

  private boolean ueberschr;

  /**
   * @param position
   */
  public KontoauszugZuordnungDialog(int position, Integer auszugsnummer,
      Integer blattnummer)
  {
    super(position);
    setTitle("Zuordnung Kontoauszugsinformationen");
    setSize(400, 225);
    getAuszugsnummer().setValue(auszugsnummer);
    getBlattnummer().setValue(blattnummer);
  }

  @Override
  protected void paint(Composite parent) throws Exception
  {
    LabelGroup group = new LabelGroup(parent, "");
    group.addLabelPair("Auszug", getAuszugsnummer());
    group.addLabelPair("Blatt", getBlattnummer());
    group.addLabelPair("Kontoauszugsinformationen überschreiben",
        getUeberschreiben());
    group.addLabelPair("", getStatus());

    ButtonArea buttons = new ButtonArea();
    buttons.addButton("übernehmen", new Action()
    {

      @Override
      public void handleAction(Object context)
      {

        intAuszugsnummer = (Integer) getAuszugsnummer().getValue();
        if (intAuszugsnummer != null && intAuszugsnummer.intValue() <= 0)
        {
          status.setValue("Auszugsnummer muss leer oder Positiv sein.");
          status.setColor(Color.ERROR);
          return;
        }
        intBlattnummer = (Integer) getBlattnummer().getValue();
        if (intBlattnummer != null && intBlattnummer <= 0)
        {
          status.setValue("Blattnummer muss leer oder Positiv sein.");
          status.setColor(Color.ERROR);
          return;
        }

        ueberschr = (Boolean) getUeberschreiben().getValue();
        close();
      }
    }, null, true);
    buttons.addButton("abbrechen", new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        throw new OperationCanceledException();
      }
    });
    buttons.paint(parent);
    getShell().setMinimumSize(getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT));

  }

  /**
   * @see de.willuhn.jameica.gui.dialogs.AbstractDialog#getData()
   */
  @Override
  public Object getData() throws Exception
  {
    return null;
  }

  public Integer getAuszugsnummerWert()
  {
    return intAuszugsnummer;
  }

  public Integer getBlattnummerWert()
  {
    return intBlattnummer;
  }

  public boolean getOverride()
  {
    return ueberschr;
  }

  private IntegerInput getAuszugsnummer()
  {
    if (auszugsnummer != null)
    {
      return auszugsnummer;
    }
    auszugsnummer = new IntegerInput(-1);
    return auszugsnummer;
  }

  private IntegerInput getBlattnummer()
  {
    if (blattnummer != null)
    {
      return blattnummer;
    }
    blattnummer = new IntegerInput(-1);
    return blattnummer;
  }

  private LabelInput getStatus()
  {
    if (status != null)
    {
      return status;
    }
    status = new LabelInput("");
    return status;
  }

  private CheckboxInput getUeberschreiben()
  {
    if (ueberschreiben != null)
    {
      return ueberschreiben;
    }
    ueberschreiben = new CheckboxInput(true);
    return ueberschreiben;
  }
}
