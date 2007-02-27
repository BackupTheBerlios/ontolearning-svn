/**
 * OntoLearn a seminar project of:
 * - Remy Stibbe
 * - Hesing Kuo
 * - Nico Vaatstra
 * - Jasper Voskuilen
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.datatypes;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;

/**
 * 
 * An interface for a document which is to be processed by this application
 * @author Jasper
 *
 */
public interface Document {
	/**
	 * @return the name of the document
	 */
	public String getName ();
	/**
	 * @return a buffered reader over the entire content of this document
	 * @throws IOException
	 */
	public BufferedReader read () throws IOException;
	/**
	 * @return a buffered reader over this documents abstracts if any.
	 * @throws IOException
	 */
	public Collection<BufferedReader> readAbstracts ()throws IOException;
}
