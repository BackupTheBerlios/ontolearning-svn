/**
 * OntoLearn a seminar project of:
 * - Remy Stibbe
 * - Hesing Kuo
 * - Nico Vaatstra
 * - Jasper Voskuilen
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.util;

import java.net.URI;

import nl.eur.eco_ict.seminar.ontolearn.datatypes.Document;

/**
 * An interface for a subsystem which crawls through a document space (disk/web/etc.) and returns one document at a time
 * @author Jasper
 *
 */
public interface DocumentCrawler {
	public void setRoot (URI root);
	public Document getNext();
	public boolean hasNext();
}
