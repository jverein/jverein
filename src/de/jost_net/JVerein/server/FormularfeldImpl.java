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
 * Revision 1.5  2010-11-13 09:30:16  jost
 * Warnings entfernt.
 *
 * Revision 1.4  2010-10-15 09:58:27  jost
 * Code aufgeräumt
 *
 * Revision 1.3  2009-06-11 21:04:23  jost
 * Vorbereitung I18N
 *
 * Revision 1.2  2008/11/29 13:15:45  jost
 * Refactoring: Warnungen beseitigt.
 *
 * Revision 1.1  2008/07/18 20:17:55  jost
 * Neu: Formulare
 *
 **********************************************************************/
package de.jost_net.JVerein.server;

import java.rmi.RemoteException;

import org.eclipse.swt.SWT;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.rmi.Formular;
import de.jost_net.JVerein.rmi.Formularfeld;
import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class FormularfeldImpl extends AbstractDBObject implements Formularfeld
{
  private static final long serialVersionUID = -5754574029501014426L;

  public FormularfeldImpl() throws RemoteException
  {
    super();
  }

  @Override
  protected String getTableName()
  {
    return "formularfeld";
  }

  @Override
  public String getPrimaryAttribute()
  {
    return "id";
  }

  @Override
  protected void deleteCheck()
  {
    //
  }

  @Override
  protected void insertCheck() throws ApplicationException
  {
    try
    {
      if (getName() == null || getName().length() == 0)
      {
        throw new ApplicationException(JVereinPlugin.getI18n().tr(
            "Bitte Namen eingeben"));
      }
    }
    catch (RemoteException e)
    {
      String fehler = JVereinPlugin.getI18n().tr(
          "Formularfeld kann nicht gespeichert werden. Siehe system log");
      Logger.error(fehler, e);
      throw new ApplicationException(fehler);
    }
  }

  @Override
  protected void updateCheck() throws ApplicationException
  {
    insertCheck();
  }

  @Override
  protected Class<?> getForeignObject(String field)
  {
    if ("formular".equals(field))
    {
      return Formular.class;
    }
    return null;
  }

  public Formular getFormular() throws RemoteException
  {
    return (Formular) getAttribute("formular");
  }

  public void setFormular(Formular formular) throws RemoteException
  {
    setAttribute("formular", formular);
  }

  public String getName() throws RemoteException
  {
    return (String) getAttribute("name");
  }

  public void setName(String name) throws RemoteException
  {
    setAttribute("name", name);
  }

  public Double getX() throws RemoteException
  {
    Double ret = (Double) getAttribute("x");
    if (ret == null)
    {
      ret = new Double(0);
    }
    return ret;
  }

  public void setX(Double x) throws RemoteException
  {
    setAttribute("x", x);
  }

  public Double getY() throws RemoteException
  {
    Double ret = (Double) getAttribute("y");
    if (ret == null)
    {
      ret = new Double(0);
    }

    return ret;
  }

  public void setY(Double y) throws RemoteException
  {
    setAttribute("y", y);
  }

  public String getFont() throws RemoteException
  {
    return (String) getAttribute("font");
  }

  public void setFont(String font) throws RemoteException
  {
    setAttribute("font", font);
  }

  public Integer getFontsize() throws RemoteException
  {
    Integer ret = (Integer) getAttribute("fontsize");
    if (ret == null)
    {
      ret = Integer.valueOf(10);
    }
    return ret;
  }

  public void setFontsize(Integer fontsize) throws RemoteException
  {
    setAttribute("fontsize", fontsize);
  }

  public Integer getFontstyle() throws RemoteException
  {
    Integer ret = (Integer) getAttribute("fontstyle");
    if (ret == null)
    {
      ret = Integer.valueOf(SWT.NORMAL);
    }
    return ret;
  }

  public void setFontstyle(Integer fontstyle) throws RemoteException
  {
    setAttribute("fontstyle", fontstyle);
  }

  @Override
  public Object getAttribute(String fieldName) throws RemoteException
  {
    return super.getAttribute(fieldName);
  }
}
