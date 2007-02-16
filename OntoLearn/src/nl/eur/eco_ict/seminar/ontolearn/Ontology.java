/**
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;

/**
 * @author Jasper
 */
public interface Ontology {
	// TODO extend operations on Ontology
	// Als jullie iets nodig hebben van een ontology maak dan hier een xtra
	// methode aan dan zorgt Jasper wel dat de implementatie geregeld wordt

	public Individual addIndividual (String classname, String indname);

	public Individual addIndividual (OntClass oclass, String indname);

	public OntClass addOClass (String namespace, String classname);

	public OntClass addOClass (String classname);

	public OntClass getOClass (String namespace, String classname);

	public OntClass getOClass (String classname);

	public void setDefaultNamespace (String namespace);
}
