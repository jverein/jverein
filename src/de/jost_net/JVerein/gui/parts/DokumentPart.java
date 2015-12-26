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
package de.jost_net.JVerein.gui.parts;

import java.rmi.RemoteException;
import java.util.Date;

import org.eclipse.swt.widgets.Composite;

import de.jost_net.JVerein.rmi.AbstractDokument;
import de.jost_net.JVerein.util.JVDateFormatTTMMJJJJ;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.input.DateInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.util.LabelGroup;

public class DokumentPart implements Part
{
  private AbstractDokument dok;

  private DateInput datum = null;

  private TextInput bemerkung;

  public DokumentPart(AbstractDokument dok)
  {
    this.dok = dok;
  }

  @Override
  public void paint(Composite parent) throws RemoteException
  {
    LabelGroup group = new LabelGroup(parent, "Infos");
    group.addLabelPair("Datum", getDatum());
    group.addLabelPair("Bemerkung", getBemerkung());
  }

  public DateInput getDatum() throws RemoteException
  {
    if (datum != null)
    {
      return datum;
    }
    Date d = dok.getDatum();
    if (d == null)
    {
      d = new Date();
    }
    this.datum = new DateInput(d, new JVDateFormatTTMMJJJJ());
    this.datum.setTitle("Datum");
    this.datum.setText("Bitte Datum wählen");
    return datum;
  }

  public TextInput getBemerkung() throws RemoteException
  {
    if (bemerkung != null)
    {
      return bemerkung;
    }
    bemerkung = new TextInput(dok.getBemerkung(), 50);
    return bemerkung;
  }

}
