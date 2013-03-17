package de.jost_net.JVerein.gui.dialogs;

import java.rmi.RemoteException;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.input.SEPALandInput;
import de.jost_net.JVerein.gui.input.SEPALandObject;
import de.jost_net.JVerein.io.IBankverbindung;
import de.jost_net.OBanToo.SEPA.IBAN;
import de.jost_net.OBanToo.SEPA.SEPAException;
import de.jost_net.OBanToo.SEPA.BankenDaten.Bank;
import de.jost_net.OBanToo.SEPA.BankenDaten.Banken;
import de.jost_net.OBanToo.SEPA.Land.SEPALaender;
import de.jost_net.OBanToo.SEPA.Land.SEPALand;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.util.LabelGroup;
import de.willuhn.jameica.system.OperationCanceledException;
import de.willuhn.logging.Logger;

/**
 * Bearbeitung einer Bankverbindung.
 */
public class BankverbindungDialog extends AbstractDialog<IBankverbindung>
{
  private IBankverbindung bankverbindung;

  private SEPALandInput land = null;

  private TextInput blz = null;

  private TextInput konto = null;

  private TextInput bic = null;

  private TextInput iban = null;

  private TextInput status = null;

  /**
   * @param position
   * @throws RemoteException
   */
  public BankverbindungDialog(int position, IBankverbindung bankverbindung)
      throws RemoteException
  {
    super(position);
    setTitle("Bankverbindung");
    this.bankverbindung = bankverbindung;
    setSize(400, 400);
  }

  @Override
  protected void paint(Composite parent) throws Exception
  {
    LabelGroup groupALT = new LabelGroup(parent, "Bankverbindung -alt-");
    groupALT.addLabelPair("Land", getSEPALand());
    groupALT.addInput(getBLZ());
    groupALT.addLabelPair("Konto", getKonto());

    LabelGroup groupNEU = new LabelGroup(parent, "Bankverbindung -neu-");
    groupNEU.addLabelPair("BIC", getBIC());
    groupNEU.addLabelPair("IBAN", getIBAN());

    getStatus().paint(parent);

    ButtonArea buttons = new ButtonArea();
    buttons.addButton(JVereinPlugin.getI18n().tr("übernehmen"), new Action()
    {
      @Override
      public void handleAction(Object context)
      {
        try
        {
          bankverbindung.setBlz((String) getBLZ().getValue());
          bankverbindung.setKonto((String) getKonto().getValue());
          bankverbindung.setBic((String) getBIC().getValue());
          bankverbindung.setIban((String) getIBAN().getValue());
        }
        catch (RemoteException e)
        {
          status.setValue(e.getMessage());
        }
        close();
      }
    }, null, true);
    buttons.addButton(JVereinPlugin.getI18n().tr("abbrechen"), new Action()
    {
      @Override
      public void handleAction(Object context)
      {
        throw new OperationCanceledException();
      }
    });
    buttons.paint(parent);
    // getShell().setMinimumSize(getShell().computeSize(SWT.DEFAULT,
    // SWT.DEFAULT));
  }

  /**
   * @see de.willuhn.jameica.gui.dialogs.AbstractDialog#getData()
   */
  @Override
  public IBankverbindung getData() throws Exception
  {
    return bankverbindung;
  }

  private SEPALandInput getSEPALand() throws RemoteException
  {
    if (land != null)
    {
      return land;
    }
    try
    {
      SEPALand la = SEPALaender.getLand(Einstellungen.getEinstellung()
          .getDefaultLand());
      if (bankverbindung.getIban() != null
          && bankverbindung.getIban().length() > 0)
      {
        IBAN i = new IBAN(bankverbindung.getIban());
        la = i.getLand();
      }
      land = new SEPALandInput(la);
      land.addListener(new AltBankListener());
    }
    catch (SEPAException e)
    {
      setStatus(e.getMessage());
      throw new RemoteException(e.getMessage());
    }
    return land;
  }

  private TextInput getKonto() throws RemoteException
  {
    if (konto != null)
    {
      return konto;
    }
    konto = new TextInput(bankverbindung.getKonto(), 12);
    konto.addListener(new AltBankListener());
    return konto;
  }

  private TextInput getBLZ() throws RemoteException
  {
    if (blz != null)
    {
      return blz;
    }
    blz = new TextInput(bankverbindung.getBlz(), 8);
    blz.setName("BLZ");
    blz.addListener(new AltBankListener());
    return blz;
  }

  private TextInput getBIC() throws RemoteException
  {
    if (bic != null)
    {
      return bic;
    }
    bic = new TextInput(bankverbindung.getBic(), 11);
    bic.setName("BIC");
    bic.setEnabled(false);
    return bic;
  }

  private TextInput getIBAN() throws RemoteException
  {
    if (iban != null)
    {
      return iban;
    }
    iban = new TextInput(bankverbindung.getIban(), 34);
    iban.setName("IBAN");
    iban.setEnabled(false);
    return iban;
  }

  private TextInput getStatus() throws RemoteException
  {
    if (status != null)
    {
      return status;
    }
    status = new TextInput("", 80);
    status.setName("Status");
    status.setEnabled(false);
    return status;
  }

  private void setStatus(String status)
  {
    try
    {
      getStatus().setValue(status);
    }
    catch (RemoteException e)
    {
      Logger.error("Fehler", e);
    }
  }

  private class AltBankListener implements Listener
  {

    @Override
    public void handleEvent(Event event)
    {
      String blzs = null;
      String kontos = null;
      SEPALand land = null;
      try
      {
        SEPALandObject slo = (SEPALandObject) getSEPALand().getValue();
        land = (SEPALand) slo.getAttribute("land");
        getBLZ().setMaxLength(land.getBankIdentifierLength());
        getKonto().setMaxLength(land.getAccountLength());
        blzs = (String) getBLZ().getValue();
        if (blzs.length() > 0)
        {
          Bank b = Banken.getBankByBLZ(blzs);
          if (b == null)
          {
            setStatus("Ungültige Bankleitzahl");
          }
          getBIC().setValue(b.getBIC());
        }
      }
      catch (Exception e)
      {
        setStatus(e.getMessage());
        Logger.error("Fehler", e);
        return;
      }
      try
      {
        kontos = (String) getKonto().getValue();
        IBAN i = new IBAN(kontos, blzs, land.getKennzeichen());
        getIBAN().setValue(i.getIBAN());
      }
      catch (Exception e)
      {
        setStatus(e.getMessage());
        Logger.error(e.getMessage());
        return;
      }
      setStatus("");
    }
  }

}
