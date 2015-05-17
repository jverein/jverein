package de.jost_net.JVerein.gui.input.Tools;

import java.rmi.RemoteException;

import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.gui.input.BuchungsartSearchInput;
import de.jost_net.JVerein.keys.BuchungBuchungsartAuswahl;
import de.jost_net.JVerein.keys.BuchungsartSort;
import de.jost_net.JVerein.rmi.Buchung;
import de.jost_net.JVerein.rmi.Buchungsart;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.input.AbstractInput;
import de.willuhn.jameica.gui.input.SelectInput;

public class BuchungsartInput
{
  public AbstractInput getBuchungsartInput(AbstractInput buchungsart,
      Buchung buchung) throws RemoteException
  {
    DBIterator list = Einstellungen.getDBService()
        .createList(Buchungsart.class);
    if (Einstellungen.getEinstellung().getBuchungsartSort() == BuchungsartSort.NACH_NUMMER)
    {
      list.setOrder("ORDER BY nummer");
    }
    else
    {
      list.setOrder("ORDER BY bezeichnung");
    }

    switch (Einstellungen.getEinstellung().getBuchungBuchungsartAuswahl())
    {
      case BuchungBuchungsartAuswahl.ComboBox:
        buchungsart = new SelectInput(list, buchung.getBuchungsart());
        switch (Einstellungen.getEinstellung().getBuchungsartSort())
        {
          case BuchungsartSort.NACH_NUMMER:
            ((SelectInput) buchungsart).setAttribute("nrbezeichnung");
            break;
          case BuchungsartSort.NACH_BEZEICHNUNG_NR:
            ((SelectInput) buchungsart).setAttribute("bezeichnungnr");
            break;
          default:
            ((SelectInput) buchungsart).setAttribute("bezeichnung");
            break;
        }
        ((SelectInput) buchungsart).setPleaseChoose("Bitte auswählen");
        break;
      case BuchungBuchungsartAuswahl.SearchInput:
      default: // default soll SearchInput sein. Eigentlich sollten die
        // Settings immer gesetzt sein, aber man weiss ja nie.
        buchungsart = new BuchungsartSearchInput();
        ((BuchungsartSearchInput) buchungsart).setAttribute("nrbezeichnung");
        ((BuchungsartSearchInput) buchungsart)
            .setSearchString("Zum Suchen tippen ...");
    }
    buchungsart.setValue(buchung.getBuchungsart());
    return buchungsart;
  }

}
