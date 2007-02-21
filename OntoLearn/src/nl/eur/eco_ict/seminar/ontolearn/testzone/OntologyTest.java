/**
 * OntoLearn a seminar project of:
 * - Remy Stibbe
 * - Hesing Kuo
 * - Nico Vaatstra
 * - Jasper Voskuilen
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.testzone;

import java.net.MalformedURLException;

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntProperty;

import nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology;
import nl.eur.eco_ict.seminar.ontolearn.datatypes.impl.JenaOntology;
import nl.eur.eco_ict.seminar.ontolearn.datatypes.impl.Settings;

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
		Settings sett = new Settings ();
		((JenaOntology)test).setDBInfo (sett.getDBinfo ());
		
		OntClass c1 = test.addOClass ("iets");
		OntClass c2 = test.addOClass ("niets");
		OntProperty p = test.addObjectProperty ("heeft");
		
		p.addDomain (c1);
		p.setRange (c2);
		
		test.addTriplet (c1, p, c2);
		
		System.out.println (test);

	}

}
