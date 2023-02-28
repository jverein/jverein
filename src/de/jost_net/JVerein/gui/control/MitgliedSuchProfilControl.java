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
package de.jost_net.JVerein.gui.control;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.Messaging.SuchprofilMessage;
import de.jost_net.JVerein.gui.action.SuchprofilLadenAction;
import de.jost_net.JVerein.gui.menu.SuchprofilMenu;
import de.jost_net.JVerein.rmi.Suchprofil;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.datasource.rmi.DBService;
import de.willuhn.datasource.rmi.ObjectNotFoundException;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.dialogs.TextDialog;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.TablePart;
import de.willuhn.jameica.messaging.Message;
import de.willuhn.jameica.messaging.MessageConsumer;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.OperationCanceledException;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;

public class MitgliedSuchProfilControl extends AbstractControl
{

  private TablePart profillist;

  private TextInput profilname;

  private SuchprofilMessageConsumer mc = null;

  public MitgliedSuchProfilControl(AbstractView view)
  {
    super(view);
  }

  public Settings getSettings()
  {
    return (Settings) this.getCurrentObject();
  }

  public TextInput getProfilname() throws RemoteException
  {
    if (profilname != null)
    {
      return profilname;
    }
    profilname = new TextInput(getSettings().getString("profilname", ""), 50);
    profilname.setName("Profilname");
    profilname.disable();
    return profilname;
  }

  public Part getSuchprofilList() throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator<Suchprofil> profile = service.createList(Suchprofil.class);
    profile.setOrder("ORDER BY bezeichnung");

    profillist = new TablePart(profile, new SuchprofilLadenAction());
    profillist.addColumn("Bezeichnung", "bezeichnung");
    profillist.setRememberColWidths(true);
    profillist.setRememberOrder(true);
    profillist.setContextMenu(new SuchprofilMenu(this));
    this.mc = new SuchprofilMessageConsumer();
    Application.getMessagingFactory().registerMessageConsumer(this.mc);
    return profillist;
  }

  public void refreshSuchprofilList() throws RemoteException
  {
    if (profillist == null)
    {
      return;
    }
    profillist.removeAll();

    DBIterator<Suchprofil> it = getIterator();
    while (it.hasNext())
    {
      Suchprofil sp = it.next();
      profillist.addItem(sp);
    }
  }

  private DBIterator<Suchprofil> getIterator() throws RemoteException
  {
    DBService service = Einstellungen.getDBService();
    DBIterator<Suchprofil> profile = service.createList(Suchprofil.class);
    profile.setOrder("ORDER BY bezeichnung");
    return profile;
  }

  public void handleStore(boolean saveas)
  {
    try
    {
      Settings s = getSettings();
      Suchprofil sp1 = null;
      try
      {
        sp1 = (Suchprofil) Einstellungen.getDBService()
            .createObject(Suchprofil.class, s.getString("id", null));
      }
      catch (ObjectNotFoundException e)
      {
        // Was nun?
      }
      if (sp1.getID() == null)
      {
        // Es gibt kein Suchprofil mit der angegebenen ID bzw. die
        // ID ist null. Nicht weiter schlimm, wir verfahren einfach wie
        // beim "Speichern unter"
        saveas = true;
      }
      if (saveas)
      {
        TextDialog td = new TextDialog(TextDialog.POSITION_CENTER);
        td.setLabelText("Bezeichnung");
        td.setTitle("Suchprofil speichern unter");
        td.setText(" ");
        try
        {
          Object o = td.open();
          getProfilname().setValue(o);
          sp1 = (Suchprofil) Einstellungen.getDBService()
              .createObject(Suchprofil.class, null);
        }
        catch (OperationCanceledException e)
        {
          // Abbruch
          return;
        }
        sp1.setClazz(MitgliedControl.class.getName());
        sp1.setBezeichnung((String) getProfilname().getValue());
        s.setAttribute("id", "");
        s.setAttribute("profilname", sp1.getBezeichnung());
        // Die ID ist nicht im Inhalt enthalten, da zu diesem Zeitpunkt
        // noch nicht bekannt. Macht aber nichts, da wir die ID einfach
        // direkt aus dem Primärschlüsselfeld des Datensatzes verwenden
        storeSettings(s, sp1);
        sp1.store();
        s.setAttribute("id", sp1.getID());
      }
      else
      {
        // Überschreiben eines vorhandenen Suchprofils
        s.setAttribute("id", sp1.getID());
        storeSettings(s, sp1);
        sp1.store();
      }

      Application.getMessagingFactory().sendMessage(new SuchprofilMessage(sp1));

      if (saveas)
      {
        GUI.getStatusBar().setSuccessText("Profil gespeichert");
      }
      else
      {
        GUI.getStatusBar().setSuccessText("Profil aktualisiert");
      }

    }
    catch (Exception e)
    {
      String fehler = "Fehler beim Speichern des Suchprofils";
      Logger.error(fehler, e);
      GUI.getStatusBar().setErrorText(fehler);
    }
  }

  /**
   * Settings werden in eine XML-Struktur serialisiert und als Byte-Array in das
   * Model übertragen
   * 
   * @param s
   *          Die zu speichernden Settings
   * @param sp1
   *          Das Model
   */
  private void storeSettings(Settings s, Suchprofil sp1)
      throws IOException, RemoteException
  {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    Properties prop = getSettings2Properties(s);
    prop.storeToXML(bos, "sicherung", "UTF8");
    sp1.setInhalt(bos.toByteArray());
  }

  /**
   * Settings können nicht direkt serialisiert werden. Daher werden sie in
   * Properties umgewandelt
   * 
   * @param settings
   *          Die umzuwandelnden Settings
   * @return Die Properties
   */
  private Properties getSettings2Properties(Settings settings)
  {
    Properties ret = new Properties();
    for (String key : settings.getAttributes())
    {
      ret.put(key, settings.getString(key, ""));
    }
    return ret;
  }

  /**
   * Wird benachrichtigt um die Anzeige zu aktualisieren.
   */
  private class SuchprofilMessageConsumer implements MessageConsumer
  {

    /**
     * @see de.willuhn.jameica.messaging.MessageConsumer#autoRegister()
     */
    @Override
    public boolean autoRegister()
    {
      return false;
    }

    /**
     * @see de.willuhn.jameica.messaging.MessageConsumer#getExpectedMessageTypes()
     */
    @Override
    public Class<?>[] getExpectedMessageTypes()
    {
      return new Class[] { SuchprofilMessage.class };
    }

    /**
     * @see de.willuhn.jameica.messaging.MessageConsumer#handleMessage(de.willuhn.jameica.messaging.Message)
     */
    @Override
    public void handleMessage(final Message message) throws Exception
    {
      GUI.getDisplay().syncExec(new Runnable()
      {

        @Override
        public void run()
        {
          try
          {
            if (profillist == null)
            {
              // Eingabe-Feld existiert nicht. Also abmelden
              Application.getMessagingFactory()
                  .unRegisterMessageConsumer(SuchprofilMessageConsumer.this);
              return;
            }
            refreshSuchprofilList();
          }
          catch (Exception e)
          {
            // Wenn hier ein Fehler auftrat, deregistrieren wir uns wieder
            Logger.error("unable to refresh Suchprofile", e);
            Application.getMessagingFactory()
                .unRegisterMessageConsumer(SuchprofilMessageConsumer.this);
          }
        }
      });
    }
  }

}
