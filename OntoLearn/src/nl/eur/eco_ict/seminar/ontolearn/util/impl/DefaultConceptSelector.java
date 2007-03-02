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
import java.util.Iterator;

import nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology;

import com.hp.hpl.jena.ontology.OntClass;

/**
 * @author Jasper
 *
 */
public class DefaultConceptSelector implements ConceptsOfInterestSelection {

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.util.impl.ConceptsOfInterestSelection#getConceptsOfInterest(nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology)
	 */
	public Collection<OntClass> getConceptsOfInterest (Ontology ontology) {
		Collection<OntClass> cdi = new HashSet<OntClass> ();
		return this.getConceptsOfInterest (ontology, cdi);
	}

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.util.impl.ConceptsOfInterestSelection#getConceptsOfInterest(nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology, java.util.Collection)
	 */
	public Collection<OntClass> getConceptsOfInterest (Ontology ontology, Collection<OntClass> cdi) {
		Iterator<OntClass> i = ontology.getClasses ();
		OntClass temp = null;
		while(i.hasNext()){
			temp = i.next ();
			if (temp.listSubClasses (true).hasNext () || temp.listSuperClasses (true).hasNext ()){
				cdi.add (temp);
			}
		}
		return cdi;
	}

}
