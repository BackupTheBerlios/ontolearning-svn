/**
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.datatypes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * @author 300353jv
 */
public class Document {
	private File file = null;

	public Document (File f) {
		this.file = f;
	}

	/**
	 * @return the contents of this document (file)
	 * @throws FileNotFoundException
	 */
	public BufferedReader read () throws FileNotFoundException {
		
		// Hier kan eventueel wat extra code waarmee de abstract eruit gefilterd wordt
		return new BufferedReader (new FileReader (this.file));
	}
}
