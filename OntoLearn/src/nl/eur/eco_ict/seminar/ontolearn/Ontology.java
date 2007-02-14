/**
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn;

import com.hp.hpl.jena.ontology.OntClass;

/**
 * @author 300353jv
 */
public interface Ontology {
	// TODO extend operations on Ontology
	// Als jullie iets nodig hebben van een ontology maak dan hier een xtra
	// methode aan dan zorgt Jasper wel dat de implementatie geregeld wordt

	public void addIndividual (String classname, String indname);

	public void addIndividual (OntClass oclass, String indname);

	public void addOClass (String namespace, String classname);

	public void addOClass (String classname);

	public OntClass getOClass (String namespace, String classname);

	public OntClass getOClass (String classname);

	public void setDefaultNamespace (String namespace);
}
