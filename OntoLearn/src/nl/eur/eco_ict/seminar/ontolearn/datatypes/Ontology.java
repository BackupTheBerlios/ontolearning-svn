/**
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.datatypes;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * @author Jasper
 */
public interface Ontology {
	// TODO extend operations on Ontology
	// Als jullie iets nodig hebben van een ontology maak dan hier een xtra
	// methode aan dan zorgt Jasper wel dat de implementatie geregeld wordt

// Ontology modification methods
	// individuals
	public Individual addIndividual (String classname, String indname);

	public Individual addIndividual (OntClass oclass, String indname);
	
	public boolean containsIndividual (String classname, String indname);
	
	public boolean containsIndividual (OntClass oclass, String indname);
	
	public Individual getIndividual (String classname, String indname);
	
	public Individual getIndividual (OntClass oclass, String indname);
	
	// Classes

	public OntClass addOClass (String namespace, String classname);

	public OntClass addOClass (String classname);
		// TODO Enumerated classes
		// TODO Intersection class
		// TODO Union class
		// TODO Complement class
	
	public boolean containsOClass (String namespace, String classname);
	
	public boolean containsOClass (String classname);

	public OntClass getOClass (String namespace, String classname);

	public OntClass getOClass (String classname);
	
	
	// Properties
	
	public OntProperty addObjectProperty (String namespace, String propertyname);
	
	public OntProperty addObjectProperty (String propertyname);
	
	public OntProperty addDataProperty (String namespace, String propertyname);
	
	public OntProperty addDataProperty (String propertyname);
	
	public boolean containsProperty (String namespace, String propertyname);
	
	public boolean containsProperty (String propertyname);
	
	public OntProperty getProperty (String namespace, String propertyname);
	
	public OntProperty getProperty (String propertyname);
	
	// Triplets
	
	public void addTriplet (OntClass domain, OntProperty property, Object range);
	
	public void addTriplet (OntClass domain, ObjectProperty property, OntClass range);
	
	public void addTriplet (OntClass domain, DatatypeProperty property, Resource datum);
	
	public boolean containsTriplet (OntClass domain, OntProperty property, OntClass range);
	
	public void removeTriplet (OntClass domain, OntProperty property, OntClass range);
	
	// TODO Restrictions
	
	// TODO Collections
		// TODO List
		// TODO Sequence
		// TODO Bag
	
	
	
	// Settings

	public void setDefaultNamespace (String namespace);
	
	public OntModel getModel ();
}
