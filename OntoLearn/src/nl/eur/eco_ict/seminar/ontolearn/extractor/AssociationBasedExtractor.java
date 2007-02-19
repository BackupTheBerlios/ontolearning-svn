/**
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.extractor;

import nl.eur.eco_ict.seminar.ontolearn.Extractor;
import nl.eur.eco_ict.seminar.ontolearn.datatypes.Document;
import nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology;
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
			MaxentTagger posTagger = new MaxentTagger();
			List myList = posTagger.tokenizeText(doc.readAbstracts());

			for (int x = 0, mySize = myList.size(); x < mySize; x++) {
				Sentence mySentence = (Sentence) myList.get(x);
				String myPOSString = posTagger.tagSentence(mySentence).toString();
			}
		}
		catch (IOException e) {
			System.out.println("Error: "+e);
		}
	}
}
