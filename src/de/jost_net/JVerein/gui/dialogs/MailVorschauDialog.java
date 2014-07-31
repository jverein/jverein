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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import de.jost_net.JVerein.gui.control.MailControl;
import de.jost_net.JVerein.gui.control.MailControl.EvalMail;
import de.jost_net.JVerein.gui.formatter.DateiGroesseFormatter;
import de.jost_net.JVerein.rmi.MailAnhang;
import de.jost_net.JVerein.rmi.MailEmpfaenger;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.datasource.rmi.DBObject;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.input.TextAreaInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.gui.util.Color;
import de.willuhn.jameica.gui.util.SimpleContainer;
import de.willuhn.logging.Logger;

/**
 * Ein Dialog zur Vorschau einer Mail.
 */
public class MailVorschauDialog extends AbstractDialog<Object>
{

  private MailControl control;

  private MailEmpfaenger empf;

  private de.willuhn.jameica.system.Settings settings;

  public MailVorschauDialog(MailControl control, MailEmpfaenger mitglied,
      int position)
  {
    super(position);
    settings = new de.willuhn.jameica.system.Settings(this.getClass());
    this.control = control;
    this.empf = mitglied;
    setTitle("Mail-Vorschau");
    setSize(settings.getInt("width", 550), settings.getInt("height", 450));
    addCloseListener(new Listener()
    {

      @Override
      public void handleEvent(Event event)
      {
        settings.setAttribute("width", getShell().getSize().x);
        settings.setAttribute("height", getShell().getSize().y);
      }
    });

    try
    {
      this.open();
    }
    catch (Exception e)
    {
      Logger.error("Fehler", e);
    }
  }

  @Override
  protected void onEscape()
  {
    close();
  }

  @Override
  protected void paint(Composite parent) throws Exception
  {
    // ScrolledContainer scrolled = new ScrolledContainer(parent, 1);
    SimpleContainer container = new SimpleContainer(parent, true, 2);

    EvalMail em = control.new EvalMail(empf);

    TextInput empfaenger = new TextInput(empf.getMailAdresse());
    empfaenger.setEnabled(false);
    container.addLabelPair("Empfänger", empfaenger);
    TextInput betreff = new TextInput(
        em.evalBetreff(control.getBetreffString()));
    betreff.setEnabled(false);
    container.addLabelPair("Betreff", betreff);
    TextAreaInput body = new TextAreaInput(em.evalText(control.getTxtString()));
    body.setEnabled(false);
    container.addInput(body);

    if (control.getAnhang().getItems().size() > 0)
    {
      ArrayList<VorschauAnhang> anhang2 = new ArrayList<VorschauAnhang>();
      for (Object o : control.getAnhang().getItems())
      {
        MailAnhang a = (MailAnhang) o;
        if (a == null)
          continue;

        VorschauAnhang an = new VorschauAnhang();
        an.setDateiname(a.getDateiname());
        an.setDateigroesse(a.getAnhang().length);
        anhang2.add(an);
      }

      addLabel("Anhänge", container.getComposite(),
          GridData.VERTICAL_ALIGN_BEGINNING);
      Composite comp4 = new Composite(container.getComposite(), SWT.NONE);
      GridData gd4 = new GridData(GridData.FILL_HORIZONTAL);
      gd4.heightHint = 90;
      comp4.setLayoutData(gd4);
      GridLayout gl4 = new GridLayout();
      gl4.marginWidth = 0;
      comp4.setLayout(gl4);

      TablePart anhang = new TablePart(anhang2, null);
      anhang.addColumn("Dateiname", "dateiname");
      anhang.setRememberColWidths(true);
      anhang.setRememberOrder(true);
      anhang.setSummary(false);
      anhang.addColumn("Dateigröße", "dateigroesse",
          new DateiGroesseFormatter());
      anhang.paint(comp4);
    }

    ButtonArea b = new ButtonArea();
    b.addButton("Schließen", new Action()
    {

      @Override
      public void handleAction(Object context)
      {
        close();
      }
    });
    b.paint(parent);
  }

  @Override
  protected Object getData() throws Exception
  {
    return null;
  }

  public class VorschauAnhang extends AbstractDBObject implements DBObject
  {

    private static final long serialVersionUID = 3587065856368087787L;

    private HashMap<String, Object> properties;

    public VorschauAnhang() throws RemoteException
    {
      super();
      properties = new HashMap<String, Object>();
    }

    @Override
    public String getPrimaryAttribute()
    {
      return "dateiname";
    }

    @Override
    protected String getTableName()
    {
      return "vorschauanhang";
    }

    @Override
    public Object getAttribute(String key)
    {
      return properties.get(key);
    }

    @Override
    public Object setAttribute(String key, Object o)
    {
      properties.put(key, o);
      return true;
    }

    public void setDateiname(String dateiname)
    {
      setAttribute("dateiname", dateiname);
    }

    public void setDateigroesse(int groesse)
    {
      setAttribute("dateigroesse", groesse);
    }

  }

  private void addLabel(String name, Composite parent, int align)
  {
    Text text = new Text(parent, SWT.NONE);
    text.setBackground(Color.BACKGROUND.getSWTColor());
    text.setText(name);
    text.setLayoutData(new GridData(align));
  }
}
