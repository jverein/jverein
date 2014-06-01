package de.jost_net.JVerein.gui.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.willuhn.jameica.gui.util.Color;

public class JameicaUtil
{
  public static void addLabel(String name, Composite parent, int align)
  {
    Label lb = new Label(parent, SWT.NONE);
    lb.setBackground(Color.BACKGROUND.getSWTColor());
    lb.setText(name);
    lb.setLayoutData(new GridData(align));
  }

}
