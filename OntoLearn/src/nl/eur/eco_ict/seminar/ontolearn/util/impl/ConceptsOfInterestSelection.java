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

import com.hp.hpl.jena.ontology.OntClass;

import nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology;

/**
 * @author Jasper
 *
 */
public interface ConceptsOfInterestSelection {
	/**
	 * @param ontology
	 * @return the concepts of direct interest; those which may not be removed from the given ontology
	 */
	public Collection<OntClass> getConceptsOfInterest (Ontology ontology);
	
	public Collection<OntClass> getConceptsOfInterest (Ontology ontology, Collection<OntClass> cdi);
	
	public final class Factory{
		public static ConceptsOfInterestSelection custum = null;
		
		public static ConceptsOfInterestSelection getSelector (){
			return getCustum();
		}
		
		public static ConceptsOfInterestSelection getCustum (){
			if(custum == null){
				custum = new CooperativeConceptSelector();
				((CooperativeConceptSelector)custum).add(new DefaultConceptSelector());
			}
			return custum;
		}
	}
}
