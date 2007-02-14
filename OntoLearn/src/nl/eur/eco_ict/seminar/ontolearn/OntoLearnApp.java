/**
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import nl.eur.eco_ict.seminar.ontolearn.datatypes.Document;
import nl.eur.eco_ict.seminar.ontolearn.datatypes.JenaOntology;
import nl.eur.eco_ict.seminar.ontolearn.datatypes.Settings;
import nl.eur.eco_ict.seminar.ontolearn.extractor.AssociationBasedExtractor;
import nl.eur.eco_ict.seminar.ontolearn.extractor.HearstExtractor;
import nl.eur.eco_ict.seminar.ontolearn.pruner.PrunerStub;

/**
 * @author 300353jv
 *
 */
public class OntoLearnApp {
	private DocumentCrawler crawler = null;
	private Collection<Extractor> extractors = null;
	private Pruner pruner = null;
	private Settings settings = null;
	private Ontology ontology = null;

	protected OntoLearnApp (){}
	
	/**
	 * @param args
	 * @throws URISyntaxException 
	 */
	public static void main (String[] args) throws URISyntaxException {
		OntoLearnApp app = new OntoLearnApp ();
		app.retrieveSettings ();
		app.start ();
	}
	
	public void retrieveSettings (){
		this.settings = new Settings ();
		this.settings.setDocumentroot ("bla vla");
	}
	
	public void start () throws URISyntaxException{
		Document doc = null;
		
		// See if there's anything to process
		while (this.getCrawler().hasNext ()){
			doc = this.getCrawler ().getNext ();
			Iterator<Extractor> i = this.getExtractors ().iterator ();
			while(i.hasNext()){
				// Let each extractor process the document
				i.next().parse(doc, this.getOntology ());
			}
			// after all extractors have processed the document clean up
			this.getPruner ().prune(this.getOntology ());
		}
		
		// TODO Output ontology
	}
	
	protected DocumentCrawler getCrawler () throws URISyntaxException{
		if (this.crawler == null){
			this.crawler = new DocumentCrawler (this.getSettings().getDocumentroot ());
		}
		return this.crawler;
	}
	
	protected Settings getSettings (){
		if (this.settings == null){
			this.retrieveSettings ();
		}
		return this.settings;
	}
	
	protected Collection<Extractor> getExtractors (){
		if (this.extractors == null){
			this.extractors = new HashSet<Extractor>();
			this.extractors.add (new HearstExtractor());
			this.extractors.add (new AssociationBasedExtractor());
		}
		return this.extractors;
	}
	
	protected Pruner getPruner (){
		if (this.pruner == null){
			this.pruner = new PrunerStub();//TODO create a pruner
		}
		return this.pruner;
	}
	
	protected Ontology getOntology (){
		if (this.ontology == null){
			this.ontology = new JenaOntology ();
		}
		return this.ontology;
	}
	

}