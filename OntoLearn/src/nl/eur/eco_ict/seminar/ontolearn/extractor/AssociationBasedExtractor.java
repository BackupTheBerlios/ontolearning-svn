/**
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.extractor;

import java.sql.SQLException;
import java.util.List;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import nl.eur.eco_ict.seminar.ontolearn.Extractor;
import nl.eur.eco_ict.seminar.ontolearn.association.Occurance;
import nl.eur.eco_ict.seminar.ontolearn.association.AssociationDatabase;
import nl.eur.eco_ict.seminar.ontolearn.datatypes.Document;
import nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology;
import nl.eur.eco_ict.seminar.ontolearn.util.PartOfSpeechTagger;
import nl.eur.eco_ict.seminar.ontolearn.util.Tokenizer;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * @author Remy
 */
public class AssociationBasedExtractor implements Extractor {

	protected Collection <Occurance> occuranceMatrix = new HashSet<Occurance>();
	AssociationDatabase waardeDB = new AssociationDatabase();
	
	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.Extractor#parse(nl.eur.eco_ict.seminar.ontolearn.datatypes.Document, nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology)
	 */
	public void parse (Document doc, Ontology ontology) {

		// TODO Auto-generated method stub
		System.out.println("Association Rules are parsing "+doc.getName ()+".");

		try {
			PartOfSpeechTagger posTagger = PartOfSpeechTagger.Factory.getInstance();
			Tokenizer tokenizer = Tokenizer.Factory.getInstance();
			List<String> myList = tokenizer.toSentences (doc.readAbstracts());

			for (int x = 0, mySize = myList.size(); x < mySize; x++) {
				String mySentence = myList.get(x);
				// System.out.println ("mySentence: "+mySentence);
				String myPOSString = posTagger.tagInternal(mySentence);

				// System.out.println ("mySentence: "+ myPOSString);
				
				
				// System.out.println(posTagger.tagInternal(mySentence.toString()) + " \r\n");
				String y = posTagger.tagInternal(mySentence.toString());
				
				Scanner scanner = new Scanner ( y ).useDelimiter("\\s");
				
				while (scanner.hasNext ()) {
					String oneWordPOS = scanner.next();
					if (oneWordPOS.contains ("/NN") ) {
						int endWordPosition = oneWordPOS.indexOf ("/");
						String test = oneWordPOS.substring (0, endWordPosition);
						String test2 = test.toLowerCase ();
						if (this.getOccurance (test2, doc) == null) {
							this.add (test2, doc);
						}
						else {
							int ocWordcount = this.getOccurance (test2, doc).wordCount++;
							waardeDB.addConcept (test2 , doc.toString (), ocWordcount);	
						}
					}
				}
			}
			this.conceptsToDatabase();	
		}
		catch (IOException e) {
			System.out.println("Error: "+e);
		}catch (Exception e){
			e.printStackTrace ();
		}
	}
	
	public void add(String word, Document doc) throws SQLException {
		Occurance oc = new Occurance();
		oc.word = word;
		oc.documentName = doc.getName();
		oc.wordCount = 1;
		this.occuranceMatrix.add (oc);
		
		// add to database
		
		int i = oc.wordCount;
		waardeDB.addConcept (oc.documentName, oc.word, i);		
		
	}
	public Occurance getOccurance(String word, Document doc) {
		Iterator <Occurance> i = this.occuranceMatrix.iterator ();
		Occurance oc;
		while (i.hasNext ()) {
			oc = i.next ();
			if (oc.documentName.equals (doc.getName()) && oc.word.equals (word)) {
				return oc;
			}
		}
		return null;
	}
	public String tostring() {
		Iterator <Occurance> i = this.occuranceMatrix.iterator ();
		Occurance oc;
		String result = "";
		while (i.hasNext ()) {
			oc = i.next ();
			result += oc.word + " " + oc.documentName + " " + oc.wordCount;
			result += "\n";
		}
		return result;
	}
	
	public void conceptsToDatabase() throws SQLException {
		// AssociationDatabase waardeDB = new AssociationDatabase();
		// waardeDB.addConcepts ();
		System.out.println (this.tostring ());
	}
}
