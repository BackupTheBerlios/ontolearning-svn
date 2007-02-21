/**
 * OntoLearn a seminar project of:
 * - Remy Stibbe
 * - Hesing Kuo
 * - Nico Vaatstra
 * - Jasper Voskuilen
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.testzone;

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntProperty;

import nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology;
import nl.eur.eco_ict.seminar.ontolearn.datatypes.impl.JenaOntology;

/**
 * @author 300353jv
 *
 */
public class OntologyTest {

	/**
	 * @param args
	 */
	public static void main (String[] args) {
		Ontology test = new JenaOntology ();
		test.setDefaultNamespace ("http://somthing.somewhere.org/");
		
		OntClass c1 = test.addOClass ("testclass");
		OntClass c2 = test.addOClass ("testclass2");
		OntProperty p = test.addObjectProperty ("testoprop");
		
		p.addDomain (c1);
		p.setRange (c2);
		
		//test.addTriplet (c1, (ObjectProperty)p, c2);
		
		System.out.println (test);
	}

}
