/**
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn;

import nl.eur.eco_ict.seminar.ontolearn.datatypes.Document;
import nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology;

/**
 * @author Jasper
 * 
 * A main building block of this application. Instances extract classes, properties and instances from a given document and add them to an ontology.
 *
 */
public interface Extractor {
	
	/**
	 * @param doc the document to parse
	 * @param ontology the ontology to extent
	 */
	public void parse (Document doc, Ontology ontology);
}
