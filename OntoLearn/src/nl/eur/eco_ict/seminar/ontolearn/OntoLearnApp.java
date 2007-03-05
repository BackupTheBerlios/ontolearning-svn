/**
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import nl.eur.eco_ict.seminar.ontolearn.datatypes.Document;
import nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology;
import nl.eur.eco_ict.seminar.ontolearn.datatypes.impl.JenaOntology;
import nl.eur.eco_ict.seminar.ontolearn.datatypes.impl.Settings;
import nl.eur.eco_ict.seminar.ontolearn.extractor.AssociationBasedExtractor;
import nl.eur.eco_ict.seminar.ontolearn.extractor.HearstExtractor;
import nl.eur.eco_ict.seminar.ontolearn.util.DocumentCrawler;
import nl.eur.eco_ict.seminar.ontolearn.util.Pruner;
import nl.eur.eco_ict.seminar.ontolearn.util.impl.DiskCrawler;
import nl.eur.eco_ict.seminar.ontolearn.util.impl.OWLPruner;
import nl.eur.eco_ict.seminar.ontolearn.util.impl.SettingsWindow;

/**
 * @author Jasper
 */
public class OntoLearnApp {
	private DocumentCrawler crawler = null;

	private Collection<Extractor> extractors = null;

	private Pruner pruner = null;

	private Settings settings = null;

	private Ontology ontology = null;

	protected OntoLearnApp () {}

	/**
	 * @param args
	 * @throws URISyntaxException
	 * @throws IOException 
	 */
	public static void main (String[] args) throws URISyntaxException, IOException {
		OntoLearnApp app = new OntoLearnApp ();
		app.retrieveSettings ();
		app.start ();
	}

	public void retrieveSettings () {
		// at the moment it needs to be hardcoded, but we could also display a
		// GUI widget to get the info
		this.settings = new Settings ();
		this.settings.setDocumentroot ("file:///"
				+ System.getProperty ("user.dir").replace ('\\', '/')
				+ "/data/testAbstracts/");
		this.settings.setOntNamespace ("http://X/");
		new SettingsWindow (this.settings);
	} // +System.getProperty("user.dir")+"\\testAbstracts\\"

	public void start () throws URISyntaxException, IOException {
		Document doc = null;
		Extractor e = null;
		Iterator<Extractor> i = null;
		long start, finish;

		// See if there's anything to process
		while (this.getCrawler ().hasNext ()
				&& (doc = this.getCrawler ().getNext ()) != null) {
			i = this.getExtractors ().iterator ();
			while (i.hasNext ()) {
				// Let each extractor process the document
				e = i.next ();
				start = System.currentTimeMillis ();
				try {
					System.out.println (e.getName () + " is processing "
							+ doc.getName ());
					e.parse (doc, this.getOntology ());
				} catch (Throwable t) {
					System.err.println (e.getName ()
							+ " messed up while processing " + doc.getName ()
							+ ":");
					t.printStackTrace ();
				}
				finish = System.currentTimeMillis ();
				System.out.println(e.getName () + " took " + ((finish-start)/1000.0) + " seconds to process " + doc.getName ());
			}

			// after all extractors have processed the document clean up
			this.getPruner ().prune (this.getOntology ());
		}

		// Once all documents have been processed call the onFinish method to
		// allow them to clean up, write the last data to the ontology etc.
		i = this.getExtractors ().iterator ();
		while (i.hasNext ()) {
			e = i.next ();
			try {
				e.onFinish (this.getOntology ());
			} catch (Throwable t) {
				System.err.println (e.getName () + " messed up:");
				t.printStackTrace ();
			}
			this.getPruner ().prune (this.getOntology ());
		}

		// Output ontology
		System.out.println (this.getOntology ().toString ());
		this.output (this.getOntology ());
	}

	protected DocumentCrawler getCrawler () throws URISyntaxException {
		if (this.crawler == null) {
			this.crawler = new DiskCrawler (this.getSettings ()
					.getDocumentroot ());
		}
		return this.crawler;
	}

	protected Settings getSettings () {
		if (this.settings == null) {
			this.retrieveSettings ();
		}
		return this.settings;
	}

	protected Collection<Extractor> getExtractors () {
		if (this.extractors == null) {
			this.extractors = new HashSet<Extractor> ();
			this.extractors.add (new HearstExtractor ());
			this.extractors.add (new AssociationBasedExtractor ());
			// extend list if new Extractors are created
		}
		return this.extractors;
	}

	protected Pruner getPruner () {
		if (this.pruner == null) {
			this.pruner = new OWLPruner ();
		}
		return this.pruner;
	}

	protected Ontology getOntology () {
		if (this.ontology == null) {
			this.ontology = new JenaOntology ();
			this.ontology.setDefaultNamespace (this.getSettings ()
					.getOntNamespace ());
			((JenaOntology) this.ontology).setDBInfo (this.getSettings ()
					.getDBinfo ());
			if (this.getSettings ().getStartingOntology ().isAbsolute ()) {
				this.ontology.insertOntology (this.getSettings ()
						.getStartingOntology ());
			}
		}
		return this.ontology;
	}
	
	protected void output (Ontology onto) throws IOException{
		String location = this.getSettings ().getOutputLocation ();
		System.out.println("Outputting resulting .rdf file to: "+location);
		
		File f = new File (location);
		
		if (f.exists ()){
			f.delete ();
		}
		if (!f.exists ()){
			f.createNewFile ();
		}
		
		if (f.canWrite ()){
			FileWriter fw = new FileWriter (f);
			onto.getModel ().write (fw);
		}
	}

}
