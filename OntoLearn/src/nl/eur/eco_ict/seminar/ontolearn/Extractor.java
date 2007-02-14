/**
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn;

import nl.eur.eco_ict.seminar.ontolearn.datatypes.Document;

/**
 * @author 300353jv
 *
 */
public interface Extractor {
	
	/**
	 * @param doc the document to parse
	 * @param ontology the ontology to extent
	 */
	public void parse (Document doc, Ontology ontology);
}
