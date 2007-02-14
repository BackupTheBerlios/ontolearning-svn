/**
 * OntoLearn a seminar project of:
 * - Remy Stibbe
 * - Hesing Kuo
 * - Nico Vaatstra
 * - Jasper Voskuilen
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.datatypes;

import com.hp.hpl.jena.ontology.OntClass;

import nl.eur.eco_ict.seminar.ontolearn.Ontology;

/**
 * @author 300353jv
 *
 */
public class JenaOntology implements Ontology {

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.Ontology#addIndividual(java.lang.String, java.lang.String)
	 */
	public void addIndividual (String classname, String indname) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.Ontology#addIndividual(com.hp.hpl.jena.ontology.OntClass, java.lang.String)
	 */
	public void addIndividual (OntClass oclass, String indname) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.Ontology#addOClass(java.lang.String, java.lang.String)
	 */
	public void addOClass (String namespace, String classname) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.Ontology#addOClass(java.lang.String)
	 */
	public void addOClass (String classname) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.Ontology#getOClass(java.lang.String, java.lang.String)
	 */
	public OntClass getOClass (String namespace, String classname) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.Ontology#getOClass(java.lang.String)
	 */
	public OntClass getOClass (String classname) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.Ontology#setDefaultNamespace(java.lang.String)
	 */
	public void setDefaultNamespace (String namespace) {
		// TODO Auto-generated method stub
		
	}

}
