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

/**
 * 
 * An interface for a document which is to be processed by this application
 * @author Jasper
 *
 */
public interface Document {
	public String getName ();
	public BufferedReader read () throws IOException;
	public BufferedReader readAbstracts ()throws IOException;
}
