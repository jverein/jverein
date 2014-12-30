/**
 * 
 */
package de.jost_net.JVerein.gui.input;

import java.util.List;


import de.willuhn.datasource.pseudo.PseudoIterator;
import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.gui.input.SearchInput;
import de.willuhn.logging.Logger;
import de.jost_net.JVerein.Einstellungen;
import de.jost_net.JVerein.rmi.Buchungsart;


/**
 * Google like live search of BuchungsArt. Matching Buchungsarten, will be shown
 * up while typing.
 * Implements startSearch from SearchInput. Searches case insensitive in "Bezeichnung" and "Nummer" from the Buchungsart Table.
 * 
 * @author Thomas Laubrock
 *
 */
public class BuchungsartSearchInput extends SearchInput {

	public List startSearch(String text) {
		try {
			DBIterator result = Einstellungen.getDBService().createList(Buchungsart.class);
			if (text != null) {
				text = "%" + text.toUpperCase() + "%";
				result.addFilter("(UPPER(bezeichnung) like ? or nummer like ?)",
						new Object[] { text, text });
			}
			return PseudoIterator.asList(result);
		} catch (Exception e) {
			Logger.error("unable to load project list", e);
			return null;
		}
	}

}
