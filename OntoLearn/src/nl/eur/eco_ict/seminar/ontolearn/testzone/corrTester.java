/**
 * OntoLearn a seminar project of:
 * - Remy Stibbe
 * - Hesing Kuo
 * - Nico Vaatstra
 * - Jasper Voskuilen
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.testzone;

import nl.eur.eco_ict.seminar.ontolearn.extractor.AssociationBasedExtractor;
import nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology;
import nl.eur.eco_ict.seminar.ontolearn.datatypes.impl.JenaOntology;
/**
 * @author Administrator
 *
 */
public class corrTester {

	/**
	 * @param args
	 */
	public static void main (String[] args) {
	// TODO Auto-generated method stub
		Ontology ontology = new JenaOntology ();
		AssociationBasedExtractor testDB = new AssociationBasedExtractor();
		
		testDB.onFinish(ontology);
	}

}
