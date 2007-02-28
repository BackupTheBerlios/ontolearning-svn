/**
 * OntoLearn a seminar project of:
 * - Remy Stibbe
 * - Hesing Kuo
 * - Nico Vaatstra
 * - Jasper Voskuilen
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.util.impl;

import java.util.Collection;
import java.util.HashSet;

import nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology;

import com.hp.hpl.jena.ontology.OntClass;

/**
 * @author 300353jv
 *
 */
public class DefaultConceptSelector implements ConceptsOfInterestSelection {

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.util.impl.ConceptsOfInterestSelection#getConceptsOfInterest(nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology)
	 */
	public Collection<OntClass> getConceptsOfInterest (Ontology ontology) {
		Collection<OntClass> cdi = new HashSet<OntClass> ();
		// TODO Auto-generated method stub
		return cdi;
	}

}
