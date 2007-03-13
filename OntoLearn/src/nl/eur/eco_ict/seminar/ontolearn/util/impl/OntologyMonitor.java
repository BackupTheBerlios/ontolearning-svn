/**
 * OntoLearn a seminar project of:
 * - Remy Stibbe
 * - Hesing Kuo
 * - Nico Vaatstra
 * - Jasper Voskuilen
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.util.impl;

import java.util.Iterator;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.rdf.model.Resource;

import nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology;

/**
 * @author Jasper
 *
 */
public class OntologyMonitor {
	protected Ontology tomonitor = null;
	
	protected int concepts = 0;
	protected int relations = 0;
	protected int instances = 0;
	
	public OntologyMonitor (Ontology ontology){
		this.setOntology (ontology);
	}
	
	public void update (){
		this.concepts = this.getNumConcepts ();
		this.relations = this.getNumRelations ();
		this.instances = this.getNumInstances ();
	}
	
	public void setOntology (Ontology ontology){
		this.tomonitor = ontology;
		this.update ();
	}
	
	public String getDifferenceMsg (){
		int c, r, i;
		String result = "";
		
		c = this.getNumConcepts ();
		r = this.getNumRelations ();
		i = this.getNumInstances ();
		
		result += this.addedOrRemoved (this.concepts, c, "concepts");
		result += ", ";
		result += this.addedOrRemoved (this.relations, r, "relations");
		result += ", ";
		result += this.addedOrRemoved (this.instances, i, "instances");
		result += ". The ontology now contains: ";
		result += c + " concepts, ";
		result += r + " relations and ";
		result += i + " instances";
		
		this.update ();
		
		return result;
	}
	
	protected String addedOrRemoved (int before, int after, String type){
		if (before > after){
			return "" + (before - after) + " " + type + " removed";
		}
		return "" + (after - before) + " " + type +" added";
	}
	
	protected int getNumConcepts (){
		int count = 0;
		Iterator<?> i= this.tomonitor.getClasses ();
		while (i.hasNext()){
			i.next ();
			count++;
		}
		return count;
	}
	
	protected int getNumInstances (){
		int count = 0;
		Iterator<?> i= this.tomonitor.getIndividuals ();
		while (i.hasNext()){
			i.next ();
			count++;
		}
		return count;
	}
	
	protected int getNumRelations (){
		int count = 0;
		Iterator<OntClass> i= this.tomonitor.getClasses ();
		Resource temp = null;
		while (i.hasNext()){
			temp = i.next ();
			Iterator<?> r = temp.listProperties ();
			while (r.hasNext ()){
				r.next ();
			count++;
			}
		}
		return count;
	}
}
