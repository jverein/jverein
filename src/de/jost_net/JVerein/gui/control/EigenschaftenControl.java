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
 * Revision 1.1  2008/01/25 16:01:56  jost
 * Neu: Eigenschaften des Mitgliedes
 *
 **********************************************************************/
package de.jost_net.JVerein.gui.control;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.action.EigenschaftenSelectAction;
import de.jost_net.JVerein.gui.menu.EigenschaftenMenu;
import de.jost_net.JVerein.rmi.Eigenschaften;
import de.jost_net.JVerein.rmi.Mitglied;
import de.willuhn.datasource.GenericIterator;
import de.willuhn.datasource.GenericObject;
import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ResultSetExtractor;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.input.LabelInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class EigenschaftenControl extends AbstractControl
{
  private Eigenschaften eigenschaft = null;

  private TextInput neu;

  private LabelInput statusbar;

  private Mitglied mitglied;

  // Liste der Eigenschaften
  private TablePart eigenschaftenList;

  // Liste der neuen Eigenschaften
  private TablePart eigenschaftenNeuList;

  public EigenschaftenControl(AbstractView view, Mitglied mitglied)
  {
    super(view);
    this.mitglied = mitglied;
  }

  public void setMitglied(Mitglied mitglied)
  {
    this.mitglied = mitglied;
  }

  public Eigenschaften getEigenschaften()
  {
    if (eigenschaft != null)
    {
      return eigenschaft;
    }
    eigenschaft = (Eigenschaften) getCurrentObject();
    return eigenschaft;
  }

  public TextInput getNeu()
  {
    if (neu != null)
    {
      return neu;
    }
    neu = new TextInput("", 50);
    return neu;
  }

  public LabelInput getStatusbar()
  {
    if (statusbar != null)
    {
      return statusbar;
    }
    statusbar = new LabelInput("");
    return statusbar;
  }

  public Part getEigenschaftenTable() throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator tags = service.createList(Eigenschaften.class);
    tags.addFilter("mitglied = " + mitglied.getID());
    tags.setOrder("ORDER BY eigenschaft");
    eigenschaftenList = new TablePart(tags, null);
    eigenschaftenList.setRememberOrder(true);
    eigenschaftenList.addColumn("Eigenschaft", "eigenschaft");
    eigenschaftenList.setContextMenu(new EigenschaftenMenu(this, mitglied));
    eigenschaftenList.setSummary(false);
    return eigenschaftenList;
  }

  public TablePart getEigenschaftenAuswahlTable() throws RemoteException
  {
    List<HilfsEigenschaft> hilfseigenschaften = getList(null);
    GenericIterator verfuegbareeigenschaften = PseudoIterator
        .fromArray((GenericObject[]) hilfseigenschaften
            .toArray(new GenericObject[hilfseigenschaften.size()]));

    eigenschaftenList = new TablePart(verfuegbareeigenschaften, null);
    eigenschaftenList.setRememberOrder(true);
    eigenschaftenList.addColumn("Eigenschaft", "eigenschaft");
    eigenschaftenList.setMulti(true);
    // eigenschaftenList.setCheckable(true);
    eigenschaftenList.setSummary(false);
    return eigenschaftenList;
  }

  public void refreshTable() throws RemoteException
  {
    eigenschaftenList.removeAll();
    DBIterator tags = Einstellungen.getDBService().createList(
        Eigenschaften.class);
    tags.addFilter("mitglied = " + mitglied.getID());
    tags.setOrder("ORDER BY eigenschaft");
    while (tags.hasNext())
    {
      eigenschaftenList.addItem((Eigenschaften) tags.next());
    }
  }

  public Part getEigenschaftenNeuTable() throws RemoteException
  {
    List<HilfsEigenschaft> hilfseigenschaften = getList(null);
    DBIterator eigenschaften = Einstellungen.getDBService().createList(
        Eigenschaften.class);
    eigenschaften.addFilter("mitglied = " + mitglied.getID());

    while (eigenschaften.hasNext())
    {
      Eigenschaften eigenschaft = (Eigenschaften) eigenschaften.next();
      for (int i = 0; i < hilfseigenschaften.size(); i++)
      {
        if (eigenschaft.getEigenschaft().equals(
            hilfseigenschaften.get(i).getEigenschaft()))
        {
          hilfseigenschaften.remove(i);
        }
      }
    }
    GenericIterator verfuegbareeigenschaften = PseudoIterator
        .fromArray((GenericObject[]) hilfseigenschaften
            .toArray(new GenericObject[hilfseigenschaften.size()]));

    eigenschaftenNeuList = new TablePart(verfuegbareeigenschaften,
        new EigenschaftenSelectAction(this));
    eigenschaftenNeuList.setRememberOrder(true);
    eigenschaftenNeuList.addColumn("Eigenschaft", "eigenschaft");
    eigenschaftenNeuList.setSummary(false);
    return eigenschaftenNeuList;
  }

  public void handleStore() throws RemoteException
  {
    try
    {
      Eigenschaften t = (Eigenschaften) Einstellungen.getDBService()
          .createObject(Eigenschaften.class, null);
      t.setMitglied(Integer.parseInt(mitglied.getID()));
      t.setEigenschaft((String) neu.getValue());
      t.store();
      GUI.getStatusBar().setSuccessText("Eigenschaft gespeichert");
    }
    catch (ApplicationException e)
    {
      GUI.getView().setErrorText(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  private List<HilfsEigenschaft> getList(String text)
  {
    try
    {
      ResultSetExtractor rs = new ResultSetExtractor()
      {
        List<HilfsEigenschaft> eigenschaften = new ArrayList<HilfsEigenschaft>();

        public Object extract(ResultSet rs) throws RemoteException,
            SQLException
        {
          while (rs.next())
          {
            eigenschaften.add(new HilfsEigenschaft(rs.getString(1)));
          }
          return eigenschaften;
        }
      };
      String sql = "select eigenschaft from eigenschaften where eigenschaft like ? "
          + "group by eigenschaft order by eigenschaft";
      if (text != null)
      {
        text = "%" + text + "%";
      }
      else
      {
        text = "%";
      }
      return (List<HilfsEigenschaft>) Einstellungen.getDBService().execute(sql,
          new Object[] { text }, rs);
    }
    catch (Exception e)
    {
      Logger.error("kann Liste der Eigenschaften nicht aufbauen", e);
      return null;
    }
  }
}
