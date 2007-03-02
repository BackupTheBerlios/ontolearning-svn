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
public class CooperativeConceptSelector implements ConceptsOfInterestSelection {
	protected Collection<ConceptsOfInterestSelection> selectors = null;
	
	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.util.impl.ConceptsOfInterestSelection#getConceptsOfInterest(nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology)
	 */
	public Collection<OntClass> getConceptsOfInterest (Ontology ontology) {
		return this.getConceptsOfInterest (ontology, new HashSet<OntClass>());
	}

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.util.impl.ConceptsOfInterestSelection#getConceptsOfInterest(nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology, java.util.Collection)
	 */
	public Collection<OntClass> getConceptsOfInterest (Ontology ontology,
			Collection<OntClass> cdi) {
		int lastsize = 0;
		do{
			lastsize=cdi.size ();
			Iterator<ConceptsOfInterestSelection> i = this.getSelectors ().iterator ();
			while(i.hasNext ()){
				i.next ().getConceptsOfInterest (ontology, cdi);
			}
		}while((cdi.size () - lastsize) > 0);
		
		return cdi;
	}
	
	public void add (ConceptsOfInterestSelection selector){
		this.getSelectors ().add (selector);
	}
	
	protected Collection<ConceptsOfInterestSelection> getSelectors (){
		if (this.selectors == null){
			this.selectors = new HashSet<ConceptsOfInterestSelection>();
		}
		return this.selectors;
	}

}
