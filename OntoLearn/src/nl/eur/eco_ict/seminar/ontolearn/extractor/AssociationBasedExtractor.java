/**
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.extractor;

import nl.eur.eco_ict.seminar.ontolearn.Extractor;
import nl.eur.eco_ict.seminar.ontolearn.datatypes.Document;
import nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology;
import nl.eur.eco_ict.seminar.ontolearn.util.PartOfSpeechTagger;
import nl.eur.eco_ict.seminar.ontolearn.util.Tokenizer;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * @author Remy
 */
public class AssociationBasedExtractor implements Extractor {

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.Extractor#parse(nl.eur.eco_ict.seminar.ontolearn.datatypes.Document, nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology)
	 */
	public void parse (Document doc, Ontology ontology) {
		System.out.println("Association Rules are parsing "+doc.getName ()+".");
		try {
			PartOfSpeechTagger posTagger = PartOfSpeechTagger.Factory.getInstance();
			Tokenizer tokenizer = Tokenizer.Factory.getInstance();
			List<String> myList = tokenizer.toSentences (doc.readAbstracts());

			for (int x = 0, mySize = myList.size(); x < mySize; x++) {
				String mySentence = myList.get(x);
				System.out.println ("mySentence: "+mySentence);
				String myPOSString = posTagger.tagInternal(mySentence);
				
				System.out.println ("mySentence: "+ myPOSString);
				
				
				// System.out.println(posTagger.tagSentence(mySentence.toString()) + " \r\n");
				// String x = posTagger.tagSentence(mySentence.toString());
			}
		}
		catch (IOException e) {
			System.out.println("Error: "+e);
		}catch (Exception e){
			e.printStackTrace ();
		}
	}
}
