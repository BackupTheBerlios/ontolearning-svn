/**
 * OntoLearn a seminar project of:
 * - Remy Stibbe
 * - Hesing Kuo
 * - Nico Vaatstra
 * - Jasper Voskuilen
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.datatypes;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import nl.eur.eco_ict.seminar.ontolearn.Ontology;

/**
 * @author Jasper
 *
 */
public class JenaOntology implements Ontology {
	/**
	 * the actual ontology
	 */
	private OntModel model = null;
	
	/**
	 * The default namespace
	 */
	private String defaultNS = null;
	
	
	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.Ontology#addIndividual(java.lang.String, java.lang.String)
	 */
	public Individual addIndividual (String classname, String indname) {
		return this.addIndividual (this.getOClass (classname), indname);
	}

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.Ontology#addIndividual(com.hp.hpl.jena.ontology.OntClass, java.lang.String)
	 */
	public Individual addIndividual (OntClass oclass, String indname) {
		return this.getModel ().createIndividual (indname, oclass);
	}

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.Ontology#addOClass(java.lang.String, java.lang.String)
	 */
	public OntClass addOClass (String namespace, String classname) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.Ontology#addOClass(java.lang.String)
	 */
	public OntClass addOClass (String classname) {
		return this.addOClass (this.defaultNS, classname);
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
		return this.getOClass (this.defaultNS, classname);
	}

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.Ontology#setDefaultNamespace(java.lang.String)
	 */
	public void setDefaultNamespace (String namespace) {
		this.defaultNS = namespace;
	}
	
	protected OntModel getModel (){
		if (this.model == null){
			this.model = ModelFactory.createOntologyModel ();
		}
		return this.model;
	}
	
	public String toString (){
		return this.getModel ().toString ();
	}

}
