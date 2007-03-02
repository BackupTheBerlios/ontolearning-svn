/**
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.datatypes;

import java.net.URI;
import java.util.Iterator;

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
	/**
	 * Create (if non existant) and retrieve a new individual
	 * @param classname the type of individual
	 * @param indname the unique value of an individual
	 * @return a newly created individual of the specified type with the specified value
	 */
	public Individual addIndividual (String classname, String indname);

	/**
	 * Create (if non existant) and retrieve a new individual
	 * @param oclass
	 * @param indname
	 * @return a newly created individual of the specified type with the specified value
	 */
	public Individual addIndividual (OntClass oclass, String indname);
	
	/**
	 * @param classname
	 * @param indname
	 * @return true if an indivual of the given type and value is present in this ontology
	 */
	public boolean containsIndividual (String classname, String indname);
	
	/**
	 * @param oclass
	 * @param indname
	 * @return true if an indivual of the given type and value is present in this ontology
	 */
	public boolean containsIndividual (OntClass oclass, String indname);
	
	/**
	 * @param classname
	 * @param indname
	 * @return retrieve the individual of the given type and value, may return null if such an individual can not be found within the ontology
	 */
	public Individual getIndividual (String classname, String indname);
	
	/**
	 * @param oclass
	 * @param indname
	 * @return retrieve the individual of the given type and value, may return null if such an individual can not be found within the ontology
	 */
	public Individual getIndividual (OntClass oclass, String indname);
	
	public Iterator<Individual> getIndividuals ();
	
	public void remove (Individual ind);
	 
	// Classes

	/**
	 * Create (if non existant) and retrieve a new concept
	 * @param namespace
	 * @param classname
	 * @return add a new type (concept) to the ontology
	 */
	public OntClass addOClass (String namespace, String classname);

	/**
	 * Create (if non existant) and retrieve a new concept
	 * @param classname
	 * @return add a new type (concept) to the ontology, using the default namespace
	 */
	public OntClass addOClass (String classname);
		// TODO Enumerated classes
		// TODO Intersection class
		// TODO Union class
		// TODO Complement class
	
	/**
	 * @param namespace
	 * @param classname
	 * @return true if a concept of the given type is contained in this ontology
	 */
	public boolean containsOClass (String namespace, String classname);
	
	/**
	 * @param classname
	 * @return true if a concept of the given type is contained in this ontology
	 */
	public boolean containsOClass (String classname);

	public OntClass getOClass (String namespace, String classname);

	public OntClass getOClass (String classname);
	
	public void replace (OntClass original, OntClass replacement);
	
	public void remove (OntClass oclass);
	
	public void removeOClass (String classname); 
	
	/**
	 * @return an iterator over all the concepts present in this ontology
	 */
	public Iterator<OntClass> getClasses ();
	
	/**
	 * @param base
	 * @return an iterator over the classes which subclass the <code>base</code> concept
	 */
	public Iterator<OntClass> getSubClasses (OntClass base);
	
	
	// Properties
	
	/**
	 * Create (if non existant) and retrieve a new property which can only be linked to other concepts
	 * @param namespace
	 * @param propertyname
	 * @return an object-property of the given type in the given namespace
	 */
	public OntProperty addObjectProperty (String namespace, String propertyname);
	
	/**
	 * Create (if non existant) and retrieve a new property which can only be linked to other concepts
	 * @param propertyname
	 * @return an object-property of the given name in the default namespace
	 */
	public OntProperty addObjectProperty (String propertyname);
	
	/**
	 * Create (if non existant) and retrieve a new property which can only be linked to primitive datatypes
	 * @param namespace
	 * @param propertyname
	 * @return a data-property of the given name in the given namespace
	 */
	public OntProperty addDataProperty (String namespace, String propertyname);
	
	/**
	 * Create (if non existant) and retrieve a new property which can only be linked to primitive datatypes
	 * @param propertyname
	 * @return a data-property in the default namespace and with the given name
	 */
	public OntProperty addDataProperty (String propertyname);
	
	public boolean containsProperty (String namespace, String propertyname);
	
	public boolean containsProperty (String propertyname);
	
	public OntProperty getProperty (String namespace, String propertyname);
	
	public OntProperty getProperty (String propertyname);
	
	// Triplets
	
	/**
	 * @param domain
	 * @param property
	 * @param range
	 */
	public void addTriplet (OntClass domain, OntProperty property, Object range);
	
	public void addTriplet (OntClass domain, ObjectProperty property, OntClass range);
	
	public void addTriplet (OntClass domain, ObjectProperty property, OntClass range, OntClass value);
	
	public void addTriplet (OntClass domain, DatatypeProperty property, Resource datum);
	
	public boolean containsTriplet (OntClass domain, OntProperty property, OntClass range);
	
	public void removeTriplet (OntClass domain, OntProperty property, OntClass range);
	
	// TODO Restrictions
	
	// TODO Collections
		// TODO List
		// TODO Sequence
		// TODO Bag
	
	
	
	// Settings

	/**
	 * @param namespace the namespace which is used if none is specified as parameter
	 */
	public void setDefaultNamespace (String namespace);
	
	/**
	 * @return the actual model of which this class is a wrapper
	 */
	public OntModel getModel ();
	
	/**
	 * @param startontology
	 */
	public void insertOntology (URI startontology);
}
